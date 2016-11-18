package cn.com.szgao.enterprise_c;
/*package cn.com.szgao.enterprise;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.google.gson.Gson;

import cn.com.szgao.dto.EnterpriseVO;

*//**
 * 工商数据去重
 * @author xiongchangyi
 *
 *//*
public class EnterpiseDeleteDup {
	//集群对象
	static Cluster cluster = CouchbaseCluster.create("192.168.0.251");
	*//**
	 * 经营异常信息Bucket=etp_abnormal_e
	 *//*
	//static Bucket bucket_abnormal = cluster.openBucket("etp_abnormal_e","sz2015");	
	*//**
	 * 分支机构Bucket=etp_branch_e
	 *//*
	//static Bucket bucket_branch = cluster.openBucket("etp_branch_e");
	*//**
	 * 抽查检查信息Bucket=etp_check_e
	 *//*
	//static Bucket bucket_check = cluster.openBucket("etp_check_e");
	*//**
	 * 变更事项Bucket=etp_event_item_e
	 *//*
	static Bucket bucket_change = cluster.openBucket("etp_event_item_e","2015sz");
	*//**
	 * 股东详情Bucket=etp_holder_det_e
	 *//*
	static Bucket bucket_det = cluster.openBucket("etp_holder_det_e","2015sz");
	*//**
	 * 股东信息Bucket=etp_holder_e
	 *//*
	static Bucket bucket_holder = cluster.openBucket("etp_holder_e","2015sz");
	*//**
	 * 违法信息Bucket=etp_illelnfo_e
	 *//*
	//static Bucket bucket_illel = cluster.openBucket("etp_illelnfo_e");
	*//**
	 * 主要人员Bucket=etp_mhold_e
	 *//*
	static Bucket bucket_mhold = cluster.openBucket("etp_mhold_e","2015sz");
	*//**
	 * 动产抵押登记信息Bucket=etp_mortgage_e
	 *//*
	//static Bucket bucket_mort = cluster.openBucket("etp_mortgage_e");
	*//**
	 * 行政处罚信息Bucket=etp_punish_e
	 *//*
	//static Bucket bucket_punish = cluster.openBucket("etp_punish_e");
	*//**
	 * 股权出质登记信息Bucket=etp_remise_e
	 *//*
	//static Bucket bucket_remise = cluster.openBucket("etp_remise_e");
	*//**
	 * 清算信息Bucket=etp_settlement_e
	 *//*
	//static Bucket bucket_settlement = cluster.openBucket("etp_settlement_e");
	*//**
	 * 公司基本信息Bucket=etp_settlement_e
	 *//*
	static Bucket bucket_etp = cluster.openBucket("etp_t","2015sz");		
	//日志对象
	private static Logger log = LogManager.getLogger(EnterpiseDeleteDup.class);
	//连接ES集群的设置
	static Settings settings = ImmutableSettings.settingsBuilder()
	        .put("cluster.name", "xcy").put("client.transport.sniff", true).build();
	//连接ES集群的客户端
	static Client client = new TransportClient(settings)
		.addTransportAddress(new InetSocketTransportAddress("192.168.0.252", 9302))
		.addTransportAddress(new InetSocketTransportAddress("192.168.0.253", 9303))
		.addTransportAddress(new InetSocketTransportAddress("192.168.0.251", 9301));
	//查询响应对象
	static SearchResponse termSearch = null;
	//Google对象，对象与JSON互转
	static Gson gson = new Gson();
	 //统计安徽	 
	static int anhuiCount = 0;
	//统计北京
	static int beijingCount = 0;
	//统计重庆
	static int chongqinCount = 0;
	//统计福建
	static int fujianCount = 0;
	//统计甘肃
	static int gansuCount = 0;
	//统计广东
	static int guangdongCount = 0;
	//统计广西
	static int guangxiCount = 0;
	//统计贵州
	static int guizhouCount = 0;
	//统计海南
	static int hainanCount = 0;
	//统计河北
	static int hebeiCount = 0;
	//统计黑龙江
	static int heilongjiangCount = 0;
	//统计河南
	static int henanCount = 0;
	//统计湖北
	static int hubeiCount = 0;
	//统计湖南
	static int hunanCount = 0;
	//统计江苏
	static int jiangsuCount = 0;
	//统计江西
	static int jiangxiCount = 0;
	//统计吉林
	static int jilinCount = 0;
	//统计辽宁
	static int liaoningCount = 0;
	//统计内蒙古
	static int neimengCount = 0;
	//统计宁夏
	static int ningxiaCount = 0;
	//统计青海
	static int qinghaiCount = 0;
	//统计陕西
	static int shanxiCount = 0;
	//统计山西
	static int shanXiCount = 0;
	//统计山东
	static int shandongCount = 0;
	//统计四川
	static int sichuanCount = 0;
	//统计上海
	static int shanhaiCount = 0;
	//统计天津
	static int tianjinCount = 0;
	//统计新疆
	static int xinjiangCount = 0;
	//统计西藏
	static int xizangCount = 0;
	//统计云南
	static int yunnanCount = 0;
	//统计浙江
	static int zhejiangCount = 0;
	*//**
	 * baseSet存储需要删除基本信息的所有KEY
	 *//*
	static Set<String> baseSet = new HashSet<String>();
	*//**
	 * holderSet存储需要删除股东信息的所有KEY
	 *//*
	static Set<String> holderSet = new HashSet<String>();
	*//**
	 * mManagerSet存储需要删除主要人员信息的所有KEY
	 *//*
	static Set<String> mManagerSet = new HashSet<String>();
	*//**
	 * holderDetSet存储需要删除股东详情信息的所有KEY
	 *//*
	static Set<String> holderDetSet = new HashSet<String>();
	*//**
	 * changSet存储需要删除变更信息的所有KEY
	 *//*
	static Set<String> changSet = new HashSet<String>();
	
	public static void main(String[] args) {
		PropertyConfigurator.configure("D:\\temp_workSpace9\\Mass\\log\\log4j.properties");
		for(int i = 0; i<= 707224; i++)
		{
			int count = i*100;
			termSearch = client.prepareSearch("etp_t")//指定同步数据时候的ES index
					.setTypes("couchbaseDocument")
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.boolQuery()
							.must(QueryBuilders.wildcardQuery("company", "*"))
							)
					.setFrom(count)
					.setSize(100)	
					.execute().actionGet();
			//迭代器
			Iterator<SearchHit> it = termSearch.getHits().iterator();
			termSearch = null;
			List<EnterpriseVO> companyList = doObject(it);
			if(null != companyList && companyList.size()>0)
			{
				for(int j = 0; j<companyList.size(); j++)
				{
					EnterpriseVO enterVO = companyList.get(j);
					if(null != enterVO)
					{						
						if(null != enterVO.getCompany() && enterVO.getCompany().length()>2)
						{
							//删除
							deleteDump(enterVO);
						}
					}
				}
			}
			if(null != baseSet)
			{
				//写到文件去
				Iterator<String> iter = baseSet.iterator();
				writeKey(iter,1);
			}
			if(null != holderSet)
			{
				//写到文件去
				Iterator<String> iter = baseSet.iterator();
				writeKey(iter,2);
			}
			if(null != mManagerSet)
			{
				//写到文件去
				Iterator<String> iter = baseSet.iterator();
				writeKey(iter,3);
			}
			if(null != holderDetSet)
			{
				//写到文件去
				Iterator<String> iter = baseSet.iterator();
				writeKey(iter,4);
			}
			if(null != changSet)
			{
				//写到文件去
				Iterator<String> iter = baseSet.iterator();
				writeKey(iter,5);
			}
		}
		
	}
	public static void writeKey(Iterator<String> iter,int flag)
	{
		FileOutputStream fos = null;
		OutputStreamWriter osw = null; 
		String fileName = "";
		if(flag == 1)
		{
			fileName = "d://baseSet.txt";
		}
		else if(flag == 2)
		{
			fileName = "d://holderSet.txt";
		}	
		else if(flag == 3)
		{
			fileName = "d://mManagerSet.txt";
		}	
		else if(flag == 4)
		{
			fileName = "d://holderDetSet.txt";
		}	
		else if(flag == 5)
		{
			fileName = "d://changSet.txt";
		}	
		try{ 
			while(iter.hasNext())
			{
				fos  = new FileOutputStream(fileName); 
				osw = new OutputStreamWriter(fos, "UTF-8"); 
				osw.write(iter.next()+"\n"); 
				osw.flush();					
			}				  
		 } 
		 catch(Exception e) 
		 { 
		        e.printStackTrace(); 
		 }
		finally
		{
		    	try {
		    		osw.close();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}				    	
		    	fos = null;
		    	osw = null;
		 }			
	}
	*//**
	 * 处理查询后的对象
	 * @param its 迭代器
	 * @return List<ExecutedVO> 处理后的对象集合
	 *//*
	public static List<EnterpriseVO> doObject(Iterator<SearchHit> its)
	{
		List<EnterpriseVO> resultList = new ArrayList<EnterpriseVO>();
		EnterpriseVO vo = null;
		while(its.hasNext())
		{
			SearchHit sh = its.next();	
			//获得DOC的JSON集合
			Map<String,Object> map = sh.getSource();
			//值对应的集合
			Collection<Object> coll = map.values();
			//值遍历器
			Iterator<Object> valueIter = coll.iterator();
			//只有一个对象
			if(valueIter.hasNext())
			{				
				Object obj= valueIter.next();
				if(null != obj)
				{
					vo = gson.fromJson(gson.toJson(obj), EnterpriseVO.class);
					//文档的ID/KEY
					vo.setCompanyId(sh.getId());
					resultList.add(vo);
					vo = null;
				}
			}
		}
		return resultList;
	}
	
	*//**
	 * 删除公司
	 * @param companyName
	 *//*
	public static void deleteDump(EnterpriseVO enterVO)
	{
		termSearch = client.prepareSearch("etp_t")//指定同步数据时候的ES index
				.setTypes("couchbaseDocument")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				//.setQuery(termQuery)
				.setQuery(
							QueryBuilders.boolQuery()
							.must(QueryBuilders.termQuery("company", enterVO.getCompany()))													
						)
				.setSize(100000000)					
				.execute().actionGet();
		//迭代器
		Iterator<SearchHit> it = termSearch.getHits().iterator();
		termSearch = null;
		List<EnterpriseVO> companyList = doObject(it);
		if(null != companyList && companyList.size()>0)
		{
			for(int i = 0; i<companyList.size();i++)
			{
				//保留一个
				if(i>0)
				{
					EnterpriseVO vo = companyList.get(i);
					//如果登记机关和注册号不相同则跳过，不做删除操作
					if(null != enterVO.getRegOffice() && null != vo.getRegOffice())
					{						
						if(!enterVO.getRegOffice().equals(vo.getRegOffice()))
						{
							continue;
						}
					}
					else if(null != enterVO.getRegNum() && null != vo.getRegNum())
					{
						if(!enterVO.getRegNum().equals(vo.getRegNum()))
						{
							continue;
						}
					}	
					//统计各省的重复条数
					if(null != vo.getProvince())
					{
						countDump(vo.getProvince());
					}
					
					log.info("\n基准>>>名称:"+enterVO.getCompany()+";注册号:"+enterVO.getRegNum()+";登记机关:"+enterVO.getRegOffice()+"\n"
							+"相同>>>名称:"+vo.getCompany()+";注册号:"+vo.getRegNum()+";登记机关:"+vo.getRegOffice());
					//把所以重复的公司key都存储到baseSet
					baseSet.add(vo.getCompanyId());
					//删除CB里面的公司
					//bucket_etp.remove(vo.getCompanyId(),2000,TimeUnit.MILLISECONDS);
					//删除ES里面的公司 etp_t
					//deleteEsById("etp_t",vo.getCompanyId());
					//删除【主要人员信息】  etp_mhold_e
					List<String> mholdeIdList = listId("etp_mhold_e",vo.getCompanyId(),"companyId");
					if(null != mholdeIdList && mholdeIdList.size()>0)
					{
						mManagerSet.addAll(mholdeIdList);
					   
						for(String id:mholdeIdList)
						{
							bucket_mhold.remove(id,2000,TimeUnit.MILLISECONDS);
							deleteEsById("etp_mhold_e",id);
						}						
										
					}				
					mholdeIdList = null;					
					//删除【股东信息】 etp_holder_e
					List<String> holdeIdList = listId("etp_holder_e",vo.getCompanyId(),"companyId");
					if(null != holdeIdList && holdeIdList.size()>0)
					{
						//把要删除的股东key存储到holderSet
						holderSet.addAll(holdeIdList);
						for(String id:holdeIdList)
						{
						  
							bucket_holder.remove(id);
							deleteEsById("etp_holder_e",id);
						 
							//删除股东详情etp_holder_det_e
							List<String> detIdList = listId("etp_holder_det_e",id,"holderId");
							if(null != detIdList && detIdList.size()>0)
							{
								//把要删除的股东详情的key存储到holderDetSet
								holderDetSet.addAll(detIdList);
							  for(String ids:detIdList)
								{
									bucket_change.remove(ids,2000,TimeUnit.MILLISECONDS);
									deleteEsById("etp_holder_e",ids);
								}			
							  			
							}
							detIdList = null;
						}						
					}	
					holdeIdList = null;					
					//删除变更信息 etp_event_item_e
					List<String> eventIdList = listId("etp_holder_e",vo.getCompanyId(),"companyId");
					if(null != eventIdList && eventIdList.size()>0)
					{
						changSet.addAll(eventIdList);
					
						for(String id:eventIdList)
						{
							bucket_change.remove(id,2000,TimeUnit.MILLISECONDS);
							deleteEsById("etp_holder_e",id);
						}			
								
					}	
					eventIdList = null;
			
				}
			}
		}
	}
	*//**
	 * 统计各省的重复条数
	 * @param province 省
	 *//*
	public static void countDump(String province)
	{
		if("北京市".equals(province))
		{
			beijingCount += 1;
		}
		else if("天津市".equals(province))
		{
			tianjinCount += 1;
		}
		else if("上海市".equals(province))
		{
			shanhaiCount += 1;
		}
		else if("重庆市".equals(province))
		{
			chongqinCount += 1;
		}
		else if("河北省".equals(province))
		{
			hebeiCount += 1;
		}
		else if("山西省".equals(province))
		{
			shanXiCount += 1;
		}
		else if("内蒙古自治区".equals(province))
		{
			neimengCount += 1;
		}
		else if("辽宁省".equals(province))
		{
			liaoningCount += 1;
		}
		else if("吉林省".equals(province))
		{
			jilinCount += 1;
		}
		else if("黑龙江省".equals(province))
		{
			heilongjiangCount += 1;
		}
		else if("江苏省".equals(province))
		{
			jiangsuCount += 1;
		}
		else if("浙江省".equals(province))
		{
			zhejiangCount += 1;
		}
		else if("安徽省".equals(province))
		{
			anhuiCount += 1;
		}
		else if("福建省".equals(province))
		{
			fujianCount += 1;
		}
		else if("江西省".equals(province))
		{
			jiangxiCount += 1;
		}
		else if("山东省".equals(province))
		{
			shandongCount += 1;
		}
		else if("河南省".equals(province))
		{
			henanCount += 1;
		}
		else if("湖北省".equals(province))
		{
			hubeiCount += 1;
		}
		else if("湖南省".equals(province))
		{
			hunanCount += 1;
		}		
		else if("广东省".equals(province))
		{
			guangdongCount += 1;
		}
		else if("广西壮族自治区".equals(province))
		{
			guangxiCount += 1;
		}
		else if("海南省".equals(province))
		{
			hainanCount += 1;
		}
		else if("四川省".equals(province))
		{
			sichuanCount += 1;
		}
		else if("贵州省".equals(province))
		{
			guizhouCount += 1;
		}		
		else if("云南省".equals(province))
		{
			yunnanCount += 1;
		}
		else if("西藏自治区".equals(province))
		{
			xizangCount += 1;
		}
		else if("陕西省".equals(province))
		{
			shanxiCount += 1;
		}
		else if("甘肃省".equals(province))
		{
			gansuCount += 1;
		}
		else if("青海省".equals(province))
		{
			qinghaiCount += 1;
		}
		else if("宁夏回族自治区".equals(province))
		{
			ningxiaCount += 1;
		}
		else if("新疆维吾尔自治区".equals(province))
		{
			xinjiangCount += 1;
		}		
	}
	*//**
	 * 
	 * @param index
	 * @param id
	 *//*
	public static void deleteIds(String index,String id)
	{
		@SuppressWarnings("unused")
		DeleteByQueryResponse response = client.prepareDeleteByQuery(index)
		        .setQuery(QueryBuilders.termQuery("holderId", id))
		        .execute()
		        .actionGet();
	}
	
	 * 按照id删除ES里面的数据
	 * @param index 索引
	 * @param id ES里面的ID
	 
	@SuppressWarnings("unused")
	public static void deleteEsById(String index,String id)
	{
		if(null != id)
		{
			DeleteResponse response = client.prepareDelete(index, "couchbaseDocument", id)
					.execute()
					.actionGet();
		}
		
	}
	*//**
	 * 查询公司ID为companyId的信息
	 * @param index  索引
	 * @param id ID
	 * @param fieldName 字段
	 * @return 每条记录的ID集合
	 *//*
	public static List<String> listId(String index,String id,String fieldName)
	{
		List<String> idList = new ArrayList<String>();
		termSearch = client.prepareSearch(index)//指定同步数据时候的ES index
				.setTypes("couchbaseDocument")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(
							QueryBuilders.boolQuery()
							.must(QueryBuilders.termQuery(fieldName, id))													
						)
				.setSize(1000)					
				.execute().actionGet();
		Iterator<SearchHit> its = termSearch.getHits().iterator();
		termSearch = null;
		while(its.hasNext())
		{
			SearchHit sh = its.next();
			idList.add(sh.getId());
		}
		return idList;
	}
}
*/