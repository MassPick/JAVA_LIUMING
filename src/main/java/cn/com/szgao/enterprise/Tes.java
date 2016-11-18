//package cn.com.szgao.enterprise;
//
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.ImmutableSettings;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//
//public class Tes {
//	
//	@SuppressWarnings("resource")
//	public static void main(String[] args) {
//		
//		Settings settings = ImmutableSettings.settingsBuilder()
//		        .put("cluster.name", "elasticsearch").put("client.transport.sniff", true).build();//elasticsearch
//		//客户端对象
//		Client client = new TransportClient(settings)
//				.addTransportAddress(new InetSocketTransportAddress("192.168.1.7", 9307))
//				.addTransportAddress(new InetSocketTransportAddress("192.168.1.6", 9306));//192.168.1.2
//		
//		QueryBuilder qb = QueryBuilders.termQuery("province", "江苏省");
//	    SearchResponse searchResponse = client.prepareSearch("etp_t")
//	    //加上这个据说可以提高性能，但第一次却不返回结果
//	    .setSearchType(SearchType.SCAN)
//	    .setQuery(qb)
//	    //实际返回的数量为5*index的主分片格式
//	        .setSize(200)
//	        //这个游标维持多长时间
//	        .setScroll(TimeValue.timeValueMinutes(8))
//	        .execute().actionGet();
//	    //第一次查询，只返回数量和一个scrollId
//	    System.out.println(searchResponse.getHits().getTotalHits());
//	    //第一次运行没有结果
//	    for (SearchHit hit : searchResponse.getHits()) 
//	    {
//	        System.out.println(hit.getSourceAsString());
//	    }
//		
//	}
//
//}
