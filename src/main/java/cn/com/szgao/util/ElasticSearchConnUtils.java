//package cn.com.szgao.util;
//
//import java.lang.reflect.Constructor;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.elasticsearch.action.bulk.BulkRequestBuilder;
//import org.elasticsearch.action.index.IndexRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.NoNodeAvailableException;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.ImmutableSettings;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.FilterBuilders;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.QueryStringQueryBuilder;
////import org.elasticsearch.common.netty.util.internal.ReusableIterator;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.sort.SortOrder;
//
//import com.couchbase.client.java.Bucket;
//import com.couchbase.client.java.CouchbaseCluster;
//import com.google.gson.Gson;
//
//import cn.com.szgao.dto.DocumentVO;
//
//
///**
// * 
// * 
// * 项目名称：MassPick 类名称：ElasticSearchUtils 类描述： 创建人：liuming 创建时间：2015-9-1
// * 上午10:12:25 修改人：liuming 修改时间：2015-9-1 上午10:12:25 修改备注：
// * 
// * @version
// * 
// */
//public class ElasticSearchConnUtils {
//	// 日志对象
//	private static Logger log = LogManager
//			.getLogger(ElasticSearchConnUtils.class);
//	
//	private static Logger logger = LogManager
//			.getLogger(ElasticSearchConnUtils.class);
//	
//
//	private static  SearchResponse searchResponse;
//	static Map<String, String> m = new HashMap<String, String>();
//	// 设置client.transport.sniff为true来使客户端去嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中，
//	static Settings settings = ImmutableSettings.settingsBuilder().put(m)
//			
//			
//			
////			.put("cluster.name", "elasticsearch")
//			.put("cluster.name", "elasticsearch30")
//			.put("client.transport.sniff", true).build();
//
//	// 创建私有对象
//	private static TransportClient client;
//	
//
//	
//	static {
//		try {
//			Class<?> clazz = Class.forName(TransportClient.class.getName());
//			Constructor<?> constructor = clazz
//					.getDeclaredConstructor(Settings.class);
//			constructor.setAccessible(true);
//			client = (TransportClient) constructor.newInstance(settings);
//			// client.addTransportAddress(new InetSocketTransportAddress(
//			// "192.168.0.30", 9330));
//
//			// client.addTransportAddress(new InetSocketTransportAddress(
//			// "192.168.0.252", 9302));
//			client
////			.addTransportAddress(
////					new InetSocketTransportAddress("192.168.0.251", 9301))
//			
////			.addTransportAddress(
////							new InetSocketTransportAddress("192.168.1.6",
////									9306))
//			.addTransportAddress(
//					new InetSocketTransportAddress("192.168.1.30",
//							9330))
//					
//			// .addTransportAddress(new
//			// InetSocketTransportAddress("192.168.0.253", 9303))
//			// .addTransportAddress(new
//			// InetSocketTransportAddress("192.168.0.254", 9304))
//			;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// 取得实例
//	public static synchronized TransportClient getTransportClient() {
//		return client;
//
//	}
//	
//	
//	 
//	
//	
//
//	/**
//	 * 得到TransportClient
//	 * @return
//	 * @author liuming
//	 * @Date 2015-10-13 下午6:05:26
//	 */
//	public static synchronized TransportClient getClient() {
////		if (null == client) {
//			try {
//				Class<?> clazz = Class.forName(TransportClient.class.getName());
//				Constructor<?> constructor = clazz
//						.getDeclaredConstructor(Settings.class);
//				constructor.setAccessible(true);
//				client = (TransportClient) constructor.newInstance(settings);
//				
//				client.addTransportAddress(
//						new InetSocketTransportAddress("192.168.1.30", 9330))
//				
////				client.addTransportAddress(
////						new InetSocketTransportAddress("192.168.1.6", 9306))
////						.addTransportAddress(
////								new InetSocketTransportAddress("192.168.0.254",
////										9304))
//				;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
////		}
//		return client;
//	}
//
//	/**
//	 * 得到 SearchResponse
//	 * @param index
//	 * @param from
//	 * @param size
//	 * @return
//	 * @author liuming
//	 * @Date 2015-10-13 下午5:52:59
//	 */
//	public static synchronized SearchResponse getSearResponse(String index,
//			int from ,int size) {
////		if (null == client) {
//			getClient();
////		}
//		searchResponse = client
//				.prepareSearch(index).setTypes("couchbaseDocument")
//				.setFrom(from).setSize(size)
//				.addSort("id", SortOrder.ASC).execute().actionGet(); ;		
//		return searchResponse;
//	}
//	
//	
//
//	public static SearchResponse getSearchResponse(String index, int form,
//			int size) throws InterruptedException {
//		SearchResponse scrollResp = null;
//		while (true) {
//			try {
//				// OR查询
//				// QueryBuilder qb=QueryBuilders.boolQuery()
//				// .should(QueryBuilders.termQuery("id",
//				// "c766fa5f-44ca-5dd9-8958-810cb1dc207d"))
//				// // .should(QueryBuilders.termQuery("id",
//				// "b790068a-61bc-56fe-b8d2-ff1a1db6fc7f"))
//				// // .should(QueryBuilders.termQuery("id",
//				// "f5b1cc6a-8847-5cea-9298-3a2a32ee7c1c"))
//				// ;
//				// OR查询
//				// BoolQueryBuilder qb = QueryBuilders.boolQuery().should(new
//				// QueryStringQueryBuilder("c766fa5f-44ca-5dd9-8958-810cb1dc207d").field("id"))
//				// .should(new
//				// QueryStringQueryBuilder("b790068a-61bc-56fe-b8d2-ff1a1db6fc7f").field("id"))
//				// .should(new
//				// QueryStringQueryBuilder("f5b1cc6a-8847-5cea-9298-3a2a32ee7c1c").field("id"));
//				// QueryBuilder qb = QueryBuilders.boolQuery().must(missing)
//				// .setQuery(QueryBuilders.termQuery("multi", "test"))
//
//				// SearchResponse response = client.prepareSearch("index1",
//				// "index2")
//				// //设置要查询的索引(index)
//				// .setTypes("type1", "type2")
//				// //设置type, 这个在建立索引的时候同时设置了, 或者可以使用head工具查看
//				// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//				// .setQuery(QueryBuilders.termQuery("multi", "test"))
//				// .setFilter(FilterBuilders.missingFilter("name"));
//
//				scrollResp = client
//				// .prepareSearch("ef")
//						.prepareSearch(index).setTypes("couchbaseDocument")
//
//						// .setPostFilter(FilterBuilders.missingFilter("area"))
//
//						// .setPostFilter(FilterBuilders.andFilter(FilterBuilders.inFilter("orgCode",
//						// "－")))
//						// .setQuery(QueryBuilder)
//						// .setQuery(qb)
//						// .setPostFilter(
//						// FilterBuilders.orFilter(FilterBuilders.termFilter(
//						// "id",
//						// "fd031aab-56f6-5ba8-ae8c-c76c1b089d1a")))
//						// .setPostFilter(
//						// FilterBuilders.orFilter(FilterBuilders.termFilter(
//						// "id",
//						// "b790068a-61bc-56fe-b8d2-ff1a1db6fc7f")))
//						// .setPostFilter(FilterBuilders.andFilter(FilterBuilders.termFilter("orgCode","752575878")))
//						// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//						.setFrom(form).setSize(size)
//						// 起始 from 大小size 90985后面
//						// //fffe3edd-4f61-4b3e-b84d-3b49177797ce
//						.addSort("id", SortOrder.ASC).execute().actionGet();
//				if (scrollResp != null) {
//					return scrollResp;
//				}
//
//			} catch (NoNodeAvailableException cont) {
//				log.info("-------------------- 重连-------------------- ");
//				Thread.sleep(1000);
//				 
//			}
//		}
//	}
//	
//	
//	
//	/**
//	 * 模糊查询
//	 * @param index
//	 * @param name
//	 * @param value
//	 * @return
//	 * @throws InterruptedException   
//	 * @return SearchResponse  
//	 * @author liuming
//	 * @date 2015年12月24日  下午2:39:54
//	 */
//	public static SearchResponse getSearchResponseByFuzzy(String index, String name,String value) throws InterruptedException {
//		SearchResponse scrollResp = null;
//		while (true) {
//			try {
////				QueryBuilder qb=QueryBuilders.wildcardQuery(name,"*"+value+"*");
//				QueryBuilder qb=QueryBuilders.wildcardQuery(name,value);
////				QueryBuilder qb= QueryBuilders.boolQuery().must( QueryBuilders.termQuery(name,value));
//				
//				scrollResp = client
//						.prepareSearch(index).setTypes("couchbaseDocument")
//						 .setQuery(qb)
////						 .setFrom(0)
//						 .setSize(5000000)
//						.execute().actionGet();
//				if (scrollResp != null) {
//					return scrollResp;
//				}
//
//			} catch (NoNodeAvailableException cont) {
//				log.info("-------------------- 重连-------------------- ");
//				Thread.sleep(1000);
//			}
//		}
//	}
//	
//	
//	/**
//	 * must查询
//	 * @param index
//	 * @param name
//	 * @param value
//	 * @return
//	 * @throws InterruptedException   
//	 * @return SearchResponse  
//	 * @author liuming
//	 * @date 2015年12月29日  下午4:44:56
//	 */
//	public static SearchResponse getSearchResponseByMust(String index, String name,String value) throws InterruptedException {
//		SearchResponse scrollResp = null;
//		while (true) {
//			try {
////				QueryBuilder qb=QueryBuilders.wildcardQuery(name,"*"+value+"*");
////				QueryBuilder qb=QueryBuilders.wildcardQuery(name,value);
////				QueryBuilder qb= QueryBuilders.boolQuery().must( QueryBuilders.termQuery(name,value));
//				
//				scrollResp = client
//						.prepareSearch(index).setTypes("couchbaseDocument")
////						 .setQuery(qb)
//						
//						 .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//						 .setPostFilter(
//							 FilterBuilders.andFilter(FilterBuilders.termFilter( name,	value)))
//						 
////						 .setFrom(0)
//						 .setSize(5000000)
//						.execute().actionGet();
//				if (scrollResp != null) {
//					return scrollResp;
//				}
//
//			} catch (NoNodeAvailableException cont) {
//				log.info("-------------------- 重连-------------------- ");
//				Thread.sleep(1000);
//			}
//		}
//	}
//	
//	
//	
//	/**
//	 * 更新es数据
//	 * @param key
//	 * @param value
//	 * @param index
//	 * @param type
//	 * @return
//	 */
//	public static boolean updateEsData(String key, String value, String index, String type) {
//		Client elient = null;
//		IndexRequestBuilder requestBuilder = null;
//		try {
//			elient =   getClient();
//			requestBuilder = elient.prepareIndex(index, type, key).setRefresh(true);
//			requestBuilder.setSource(value).execute().actionGet();
//			return true;
//		} catch (Exception e) {
//			logger.error("updateEsData", e);
//			return false;
//		} finally {
//			requestBuilder = null;
//			elient = null;
//			key = null;
//			value = null;
//			index = null;
//			type = null;
//		}
//	}
//	
//	/**
//	 * 更新es数据
//	 * 
//	 * @param doc
//	 * @param index
//	 * @param type
//	 * @return
//	 */
//	public static boolean updateEsDataList(Map<String, DocumentVO> doc, String index, String type) {
//		if (null == doc) {
//			return false;
//		}
//		if (doc.size() == 0) {
//			return false;
//		}
//		Client client = null;
//		BulkRequestBuilder bulkRequest = null;
//		try {
//			client =  getClient();
//			bulkRequest = client.prepareBulk();
//			for (Map.Entry<String, DocumentVO> map : doc.entrySet()) {
//				bulkRequest.add(client.prepareIndex(index, type, map.getKey())
//						.setSource(StringUtils.GSON.toJson(map.getValue())).setRefresh(true));
//			}
//			bulkRequest.execute().actionGet();
//			bulkRequest.request().requests().clear();
//			return true;
//		} catch (Exception e) {
//			logger.error("updateEsDataList", e);
//		} finally {
//			client = null;
//			bulkRequest = null;
//			index = null;
//			type = null;
//			if (null != doc && doc.size() > 0) {
//				doc.clear();
//				doc = null;
//			}
//		}
//		return false;
//	}
//	
//	
//	
//	
//}
