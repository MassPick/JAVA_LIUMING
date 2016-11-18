package cn.com.szgao.enterprise;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.couchbase.client.java.Bucket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.com.szgao.dto.CompanyHolder;
import cn.com.szgao.dto.DocWholeCourtVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.HolderVO;
import cn.com.szgao.dto.MainManagerVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.EsUtil;
import cn.com.szgao.util.StringUtils;

/**
 * 到得股东信息与企业信息的关联关系 之后可以根据输入的自然人或法人查询其所有关联的企业。
 * 
 * @author liuming
 *
 */
public class GetHolderFromESToTextOne {
	static Gson gson = new Gson();
	static int index = 0;

	public static void main(String[] args) {
		// PropertyConfigurator.configure("D:\\Users\\dell\\git\\maven-webapp\\src\\main\\webapp\\WEB-INF\\log4j.properties");
		// Logger log = LogManager.getLogger(TextES2.class);
		// TODO Auto-generated method stub
		long da = System.currentTimeMillis();
		show();
		System.out.println((System.currentTimeMillis() - da) / 1000);
		System.out.println();
	}

	static int count=0;
	@SuppressWarnings("deprecation")
	public static void show() {
		// QueryBuilder qb = QueryBuilders.termsQuery("", "");

		SearchResponse searchResponse_2 = null;

		Client esClient = EsUtil.getClient();
		SearchResponse searchResponse = esClient.prepareSearch("company").setTypes("etp_t")
				// .setTypes("etp_t","etp_holder_e")
				// 加上这个据说可以提高性能，但第一次却不返回结果
				.setSearchType(SearchType.SCAN)
				// .setPostFilter(FilterBuilders.termFilter("doc.source",1))
				// 实际返回的数量为5*index的主分片格式
				.setSize(1000)
				// 这个游标维持多长时间
				.setScroll(TimeValue.timeValueMinutes(8)).execute().actionGet();
		// 第一次查询，只返回数量和一个scrollId
		System.out.println(searchResponse.getHits().getTotalHits());
		System.out.println(searchResponse.getHits().hits().length);
		// 第一次运行没有结果
		for (SearchHit hit : searchResponse.getHits()) {
			System.out.println(hit.getSourceAsString());
		}
		System.out.println("------------------------------");
		int sum = 0;
		JsonObject json = null;
		List<String> list = new ArrayList<String>();
		List<CompanyHolder> lists = new ArrayList<CompanyHolder>();

		EnterpriseVO vo = null;
		String companyId = null;

		// DocWholeCourtVO docVo=null;
		// 使用上次的scrollId继续访问
		while (true) {
			searchResponse = esClient.prepareSearchScroll(searchResponse.getScrollId())
					.setScroll(TimeValue.timeValueMinutes(8)).execute().actionGet();
			System.out.println(searchResponse.getHits().getTotalHits());
			// System.out.println(searchResponse.getHits().hits().length);
			
			 
			for (SearchHit hit : searchResponse.getHits()) {
				
//				list.add(hit.getId());
				
			System.out.println(count++);
				
				// System.out.println(hit.getSourceAsString());
				companyId = hit.getId();

				// json = gson.fromJson(hit.getSourceAsString(),
				// JsonObject.class);
				// list.add(json.get("doc").toString());

				vo = gson.fromJson(hit.getSourceAsString(), EnterpriseVO.class);
				vo.setCompanyId(hit.getId());
				
				
//				System.out.println(esClient.prepareIndex("company", "etp_holder_e", companyId).toString() );
				
				
//				esClient.prepareIndex("company", "etp_holder_e", companyId)
				
//				BulkRequestBuilder bulkRequest = esClient.prepareBulk();
//				esClient.prepareGet(arg0, arg1, arg2)
				
				

				searchResponse_2 = esClient.prepareSearch("company")
						.setTypes("etp_holder_e")
						
//						.setTypes("etp_holder_e", "etp_mhold_e")
						// 加上这个据说可以提高性能，但第一次却不返回结果
//						.setSearchType(SearchType.SCAN)
						
//						.setPostFilter(QueryBuilders.termQuery("_id", "7505023c-0eb2-58ff-8b05-38c939e19eaf"))
						
						.setPostFilter(QueryBuilders.termQuery("companyId", companyId))
						
//						.setQuery(QueryBuilders.idsQuery("7505023c-0eb2-58ff-8b05-38c939e19eaf"))
//						QueryBuilders.termQuery("_id", "342637917921708032");
						
						
						
//						.setPostFilter(QueryBuilders.idsQuery(companyId) )
						 
//						.setPostFilter(QueryBuilders.termsQuery("companyId",list) )
						// 实际返回的数量为5*index的主分片格式
						.setSize(1000)
						// 这个游标维持多长时间
						.setScroll(TimeValue.timeValueMinutes(8)).execute().actionGet();
				if (null != searchResponse_2) {
					for (SearchHit hit_2 : searchResponse_2.getHits()) {
						if (null != hit_2.getSourceAsString()) {
							if (hit_2.getSourceAsString().indexOf("holderId") != -1) {// 股东
								HolderVO holderVo = gson.fromJson(hit_2.getSourceAsString(), HolderVO.class);

								CompanyHolder ch = new CompanyHolder();
								ch.setCompany(vo.getCompany());
								ch.setRegNum(vo.getCreditCode() != null ? vo.getCreditCode()
										: (vo.getRegNum() != null ? vo.getRegNum() : null));
								ch.setHolder(holderVo.getHolder());
								ch.setHolder_type(holderVo.getType());
								ch.setProvince(vo.getProvince());
								ch.setCity(vo.getCity());
								ch.setArea(vo.getArea());
								ch.setCompanyId(vo.getCompanyId());
								lists.add(ch);

								if (lists.size() >= 100) {
									index++;
									// System.out.println(true);
									filteText2(lists);

									lists.clear();
									lists = null;
									lists = new ArrayList<CompanyHolder>();
								}

							}
//							else if (hit_2.getSourceAsString().indexOf("mainManagerId") != -1) {// 高管
//								MainManagerVO mainManagerVo = gson.fromJson(hit_2.getSourceAsString(),
//										MainManagerVO.class);
//
//							}

						}

					}
				}

//				sum++;
//				System.out.println(sum);
				
				// log.info(hit.getSourceAsString());
			}
			if (searchResponse.getHits().getHits().length == 0) {
				break;
			}
			// if(sum>=1000000){break;}
		}
		esClient.close();
		if (lists.size() > 0) {
			index++;
			// System.out.println(true);
			filteText2(lists);
			lists.clear();
			lists = null;
		}
	}

	public static void writeToCB(List<String> value) {
		FileWriter filewr = null;
		try {
			filewr = new FileWriter("G:\\执行executed\\" + index + ".txt", true);
			for (String val : value) {
				filewr.write(val + System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (null != filewr) {
				try {
					filewr.close();
					// outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static Gson gs = new Gson();

	public static void filteText2(List<CompanyHolder> value) {
		FileWriter filewr = null;
		try {
			filewr = new FileWriter("D:\\lm\\log\\temp\\股东反查\\" + index + ".txt", true);

			for (CompanyHolder wholeCourtVO : value) {
				filewr.write(gs.toJson(wholeCourtVO) + System.getProperty("line.separator"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (null != filewr) {
				try {
					filewr.close();
					// outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	 
}
