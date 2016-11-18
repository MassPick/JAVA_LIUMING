package cn.com.szgao.util;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.queryParser.surround.parser.QueryParser;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import cn.com.szgao.dto.ESVO;

public class ElasticSearchConnUtils220 {

	static Settings settings = null;
	static Client client = null;
	private static Logger log = LogManager.getLogger(ElasticSearchConnUtils220.class);

	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("cluster.name", "pq");
		// map.put("index.refresh_interval", "-1s");
		// map.put("client", "true");
		// map.put("data", "false");
		// map.put("client.transport.ignore_cluster_name", "true");
		// map.put("client.transport.nodes_sampler_interval", "10s");
		// map.put("client.transport.ping_timeout", "30s");
		//
		// Settings settings = Settings.settingsBuilder().put(map).build();
		// try {
		// client = TransportClient.builder().settings(settings).build()
		// .addTransportAddress(new
		// InetSocketTransportAddress(InetAddress.getByName("192.168.1.19"),
		// 9300));
		// System.out.println("---------------->");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		Map<String, String> map = new HashMap<String, String>();
		map.put("cluster.name", "masspick");
		map.put("index.refresh_interval", "-1s");
		map.put("client", "true");
		map.put("data", "false");
		map.put("client.transport.ignore_cluster_name", "true");
		map.put("client.transport.nodes_sampler_interval", "10s");
		map.put("client.transport.ping_timeout", "30s");
		Settings settings = Settings.settingsBuilder().put(map).build();
		try {
			client = TransportClient.builder().settings(settings).build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.1.9"), 9300));
			System.out.println("---------------->");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	// public static void doListEsData100(String index,String
	// type,HashMap<String, String> hashMap) {
	// if(null==hashMap&&hashMap.size()==0){
	// return ;
	// }
	// // Client client = ElasticsearchUtil.getClient();
	// // List<IndexRequest> requests = new ArrayList<IndexRequest>();
	// // IndexRequest request =null;
	//// BulkRequestBuilder bulkRequest = client.prepareBulk();
	// BulkRequestBuilder bulkRequest = client .prepareBulk();
	//// bulkRequest.setRefresh(false);
	//
	//
	// if(null!=hashMap&&hashMap.size()>0){
	// int i=0;
	// String key = null;
	// String value = null;
	// // 遍历HashMap的另一个方法
	// Set<Entry<String, String>> sets = hashMap.entrySet();
	// for (Entry<String, String> entry : sets) {
	// i++;
	// key = entry.getKey();
	// value = entry.getValue();
	// bulkRequest.add(
	// client.prepareIndex(index,type, key).setSource(value).setRefresh(false));
	// if(i%1000==0){
	// try {
	//// BulkResponse bulkResponse = bulkRequest.get();
	// BulkResponse bulkResponse = bulkRequest.execute().actionGet();
	//
	// bulkRequest = client .prepareBulk();
	//
	// } catch (Exception e) {
	// System.out.println(e.getMessage());
	// log.error(e.getMessage());
	// }
	// }
	// }
	//
	// try {
	//// BulkResponse bulkResponse = bulkRequest.get();
	// BulkResponse bulkResponse = bulkRequest.execute().actionGet();
	// } catch (Exception e) {
	// System.out.println(e.getMessage());
	// log.error(e.getMessage());
	// }
	//
	// }
	//
	// }

	public static void doListEsData100(String index, String type, HashMap<String, String> hashMap) {
		if (null == hashMap || hashMap.size() == 0) {
			return;
		}
		BulkRequestBuilder bulkRequest = null;
		try {
			bulkRequest = client.prepareBulk();
			String key = null;
			String value = null;

			// // 遍历HashMap的另一个方法
			Set<Entry<String, String>> sets = hashMap.entrySet();
			for (Entry<String, String> entry : sets) {
				key = entry.getKey();
				value = entry.getValue();
				bulkRequest.add(client.prepareIndex(index, type, key).setSource(value).setRefresh(false));
			}

			bulkRequest.get();
			bulkRequest.request().requests().clear();

			System.out.println("---------------------------------------------------------ES提交成功");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("---------------------------------------------------------ES失败---------------------");
		} finally {
			bulkRequest = null;
			index = null;
			type = null;
		}
	}

	// static long endTimeT= System.currentTimeMillis();
	public static void doListEsData100(List<ESVO> listESVO) {
		long startTime = System.currentTimeMillis();
		// log.info("当前读文件理时间 : " + (float) ((startTime - endTimeT ) / 1000) +
		// "秒");
		if (null == listESVO || listESVO.size() == 0) {
			return;
		}

		BulkRequestBuilder bulkRequest = null;
		try {
			bulkRequest = client.prepareBulk();

			for (Iterator iterator = listESVO.iterator(); iterator.hasNext();) {
				ESVO esvo = (ESVO) iterator.next();
				bulkRequest.add(client.prepareIndex(esvo.getIndex(), esvo.getType(), esvo.getKey())
						.setSource(esvo.getSource()).setRefresh(false));
			}
			bulkRequest.get();
			bulkRequest.request().requests().clear();

			System.out.println("---------------------------------------------------------ES提交成功");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("---------------------------------------------------------ES失败---------------------");
			log.info(e.getMessage());
		} finally {
			bulkRequest = null;
		}
		long endTime = System.currentTimeMillis();
		// endTimeT=endTime;
		log.info("当前ES批处理时间 : " + (float) ((endTime - startTime) / 1000) + "秒" + "  " + ((endTime - startTime)) + "毫秒");
		System.out.println("111111111");
	}

	public static void doListEsData(String index, String type, HashMap<String, String> hashMap) {
		long startTime = System.currentTimeMillis();
		// Client client = ElasticsearchUtil.getClient();
		// List<IndexRequest> requests = new ArrayList<IndexRequest>();
		// IndexRequest request =null;
		// BulkRequestBuilder bulkRequest = client.prepareBulk();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		// bulkRequest.setRefresh(false);

		String key = null;
		String value = null;
		// 遍历HashMap的另一个方法
		Set<Entry<String, String>> sets = hashMap.entrySet();
		for (Entry<String, String> entry : sets) {
			key = entry.getKey();
			value = entry.getValue();
			// System.out.println(value);
			// System.out.println(StringUtils.GSON.toJson(value));

			// bulkRequest.add(
			// client.prepareIndex(index,type,
			// key).setSource(StringUtils.GSON.toJson(value)).setRefresh(false));

			bulkRequest.add(client.prepareIndex(index, type, key).setSource(value).setRefresh(false));

		}
		try {
			// BulkResponse bulkResponse = bulkRequest.get();
			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if (bulkResponse.hasFailures()) {
				// System.out.println("批量处理失败!");
				for (Entry<String, String> entry : sets) {
					key = entry.getKey();
					value = entry.getValue();
					log.error("批量处理失败:" + key + ":" + value);
				}

			} else {
				bulkRequest.request().requests().clear();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.error(e.getMessage());
		}

		long endTime = System.currentTimeMillis();
		log.info("当前批处理时间 : " + (float) ((endTime - startTime) / 1000) + "秒");

	}

	
	/**
	 * 模糊查询
	 * @param index
	 * @param type
	 * @param name
	 * @param value
	 * @return
	 * @throws InterruptedException   
	 * @return SearchResponse  
	 * @author liuming
	 * @date 2016年11月10日  下午5:12:19
	 */
	public static SearchResponse getSearchResponseByFuzzy(String index,String type, String name, String value)
			throws InterruptedException {
		SearchResponse scrollResp = null;
		while (true) {
			try {
				// QueryBuilder
				// qb=QueryBuilders.wildcardQuery(name,"*"+value+"*");
				
				QueryBuilder qb = QueryBuilders.wildcardQuery(name, value);
				
				// QueryBuilder qb= QueryBuilders.boolQuery().must(
				// QueryBuilders.termQuery(name,value));

				scrollResp = client.prepareSearch(index).setTypes(type).setQuery(qb)
						// .setFrom(0)
						.setSize(5000).execute().actionGet();
				if (scrollResp != null) {
					return scrollResp;
				}

			} catch (NoNodeAvailableException cont) {
				log.info("-------------------- 重连-------------------- ");
				Thread.sleep(1000);
			}
		}
	}

	/**
	 * must查询
	 * @param index
	 * @param name
	 * @param value
	 * @return
	 * @throws InterruptedException   
	 * @return SearchResponse  
	 * @author liuming
	 * @date 2015年12月29日  下午4:44:56
	 */
	public static SearchResponse getSearchResponseByMust(String index,String type, String name,String value) throws InterruptedException {
		SearchResponse scrollResp = null;
		while (true) {
			try {
//				QueryBuilder qb=QueryBuilders.wildcardQuery(name,"*"+value+"*");
//				QueryBuilder qb=QueryBuilders.wildcardQuery(name,value);
//				QueryBuilder qb= QueryBuilders.boolQuery().must( QueryBuilders.termQuery(name,value));
				
				QueryBuilder qb = QueryBuilders.multiMatchQuery(name, value);
				
				
				scrollResp = client
						.prepareSearch(index).setTypes(type)
//						 .setQuery(qb)
						
						 .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
						 .setPostFilter(qb)
						 
//						 .setFrom(0)
//						 .setSize(5000000)
						.execute().actionGet();
				if (scrollResp != null) {
					return scrollResp;
				}

			} catch (NoNodeAvailableException cont) {
				log.info("-------------------- 重连-------------------- ");
				Thread.sleep(1000);
			}
		}
	}
	
	
	public static SearchResponse getSearchResponseTerm(String index,String type, String name,String value) throws InterruptedException {
		SearchResponse scrollResp = null;
		while (true) {
			try {
				SearchRequestBuilder  sbuilder = client.prepareSearch(index) //index name
				        .setTypes(type) //type name
				        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				        .setQuery(QueryBuilders.termQuery(name, value))                // Query
//				        .setPostFilter(QueryBuilders.rangeQuery("eventCount").from(1).to(18))  // Filter
				        .setFrom(0).setSize(60).setExplain(true);
				System.out.println(sbuilder.toString());
				SearchResponse response = sbuilder.execute().actionGet();
				return response;
//				System.out.println(response.toString());
				

			} catch (NoNodeAvailableException cont) {
				log.info("-------------------- 重连-------------------- ");
				Thread.sleep(1000);
			}
		}
	}
	
	
	
	
	public static SearchResponse getSearchResponseBymultiMatchQuery(String index,String type, String name,String value) throws InterruptedException {
		SearchResponse scrollResp = null;
		while (true) {
			try {
				 scrollResp = client.prepareSearch(index)
						.setTypes(type)
						.setQuery(QueryBuilders.multiMatchQuery( value,name
//								"clients.cn"
//								,"executed.cn"
//								,"company.cn"
//								,"client.cn"
								)
								.type(Type.PHRASE_PREFIX).minimumShouldMatch("100%"))
						.setSize(5000).setFetchSource("source", null)
						// 这个游标维持多长时间
						.setScroll(TimeValue.timeValueMinutes(2)).execute().actionGet();
				if (scrollResp != null) {
					return scrollResp;
				}

			} catch (NoNodeAvailableException cont) {
				log.info("-------------------- 重连-------------------- ");
				Thread.sleep(1000);
			}
		}
	}
	
	
	
	
	
//	
	// public static void list(){
	//
	// Client esClient = client;
	// SearchResponse searchResponse = esClient.prepareSearch("company")
	// .setTypes("etp_t")
	// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(200)
	// .setScroll(TimeValue.timeValueMinutes(8)).execute().actionGet();
	// for (SearchHit hit : searchResponse.getHits()) {
	// System.out.println(hit.getSourceAsString()+":"+searchResponse.getHits().getTotalHits());
	// }
	// int i=1;
	// while (true) {
	// searchResponse =
	// esClient.prepareSearchScroll(searchResponse.getScrollId())
	// .setScroll(TimeValue.timeValueMinutes(8)).execute().actionGet();
	//
	// for (SearchHit hit : searchResponse.getHits()) {
	// System.out.println(i+":"+hit.getSourceAsString());
	// i++;
	// }
	// if (searchResponse.getHits().getHits().length == 0) {
	// break;
	// }
	// System.out.println("-----------------------------");
	// }
	// esClient.close();
	// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(200)
	// .setScroll(TimeValue.timeValueMinutes(8)).execute().actionGet();
	// for (SearchHit hit : searchResponse.getHits()) {
	// System.out.println(hit.getSourceAsString()+":"+searchResponse.getHits().getTotalHits());
	// }
	// int i=1;
	// while (true) {
	// searchResponse =
	// esClient.prepareSearchScroll(searchResponse.getScrollId())
	// .setScroll(TimeValue.timeValueMinutes(8)).execute().actionGet();
	//
	// for (SearchHit hit : searchResponse.getHits()) {
	// System.out.println(i+":"+hit.getSourceAsString());
	// i++;
	// }
	// if (searchResponse.getHits().getHits().length == 0) {
	// break;
	// }
	// System.out.println("-----------------------------");
	// }
	//
	// }

}
