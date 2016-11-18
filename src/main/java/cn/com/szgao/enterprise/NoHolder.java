//package cn.com.szgao.enterprise;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.elasticsearch.action.search.SearchRequestBuilder;
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
//import com.google.gson.Gson;
//
//import cn.com.szgao.dto.EnterpriseVO;
//
///**
// * 
// * @author xiongchangyi
// *
// */
//public class NoHolder {
//	static Connection conn = null;
//	/**
//	 * 企业索引
//	 */
//	private static final String INDEX_ONE = "etp_t";
//	static Gson gson = new Gson();
//	static PreparedStatement pst = null;
//	//ID
//	static List<String> list = new ArrayList<String>();
//	//ID
//	static List<String> list2 = new ArrayList<String>();
//	static Settings settings = ImmutableSettings.settingsBuilder()
//	        .put("cluster.name", "elasticsearch").put("client.transport.sniff", true).build();
//	//客户端对象
//	static Client client = new TransportClient(settings)
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.2", 9302))
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.3", 9303))
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.4", 9304))
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.5", 9305))
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.6", 9306))
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.8", 9308))
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.7", 9307));
//	public static void main(String[] args) throws IOException {
//		try {
//			conn = DataConnect.getConnection();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		//获得110万ID
//		// getDate();
//		//（江苏、北京、山东、甘肃）、(安徽)、(广西)、(黑龙江)、(广州)、(内蒙古)、(福建)、(广东)、（河北）
//		//(湖北)、(湖南)、(河南)、山西、陕西、宁夏、(新疆)、(贵州)、(辽宁)、(西藏)、青海、(浙江)、重庆、天津、(上海)、(四川)、吉林
//		QueryBuilder qb = QueryBuilders.boolQuery()
//				.must(QueryBuilders.wildcardQuery("province", "宁夏*"))
//				.must(QueryBuilders.wildcardQuery("company", "*公司"))
//				.mustNot(QueryBuilders.wildcardQuery("company", "*支公司"))
//				.mustNot(QueryBuilders.wildcardQuery("company", "*子公司"))
//				.mustNot(QueryBuilders.wildcardQuery("company", "*分公司"));
//				//.mustNot(QueryBuilders.termQuery("city", "广州市"));
//		SearchResponse searchResponse = client.prepareSearch(INDEX_ONE)
//			    //加上这个据说可以提高性能，但第一次却不返回结果
//			    .setSearchType(SearchType.SCAN)
//			    .setQuery(qb)
//			    //实际返回的数量为5*index的主分片格式
//		        .setSize(200)
//		        //这个游标维持多长时间
//		        .setScroll(TimeValue.timeValueMinutes(8))
//		        .execute().actionGet();
//		Iterator<SearchHit> its = searchResponse.getHits().iterator();
//		System.out.println(searchResponse.getHits().totalHits());
//		for(SearchHit hit:searchResponse.getHits())
//		{
//			System.out.println(hit.getSourceAsString());
//		}
//		while(true)
//	    {
//		    searchResponse = client.prepareSearchScroll(searchResponse.getScrollId())
//		        .setScroll(TimeValue.timeValueMinutes(8))
//		        .execute().actionGet();
//			if (searchResponse.getHits().getHits().length == 0) 
//		    {
//		        break;
//		    }
//			its = searchResponse.getHits().iterator();
//			doResult(its);
//			its = null;
//	    }
//		//写库
//		String sql = "insert into dump_ningxia(name) values(?)";
//		try {
//			pst = conn.prepareStatement(sql);
//			for(int i=0;i<list2.size();i++){
//				pst.setString(1, list2.get(i));
//				pst.executeUpdate();
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				pst.close();
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			
//		}
//	}
//	static EnterpriseVO taxVO = null;	
//	public static void doResult(Iterator<SearchHit> its){
//		Map<String,Object> map = null;
//		Collection<Object> coll = null;		
//		while(its.hasNext())
//		{			
//			//获得行对象
//			SearchHit sh = its.next();
//			String idd = sh.getId();
//			//在行对象里面找2个对象，一个是doc对象,另一个是meta对象
//			map = sh.getSource();
//			coll = map.values();
//			//遍历coll集合
//			Iterator<Object> iter = coll.iterator();
//			//取第一个对象,第2个对象是es的meta对象
//			if(iter.hasNext())
//			{
//				//第一个对象，即doc
//				Object obj= iter.next();
//				taxVO = gson.fromJson(gson.toJson(obj), EnterpriseVO.class);
//				obj = null;
//				if(null!=taxVO)
//				{
//					doWrite(idd,taxVO.getCompany(),taxVO.getUrl());
//				}
//			}
//			map = null;
//			coll = null;
//			taxVO = null;
//		}		
//	}
//	static SearchRequestBuilder searchRequestBuilder = client.prepareSearch("etp_holder_e");
//	static SearchResponse sr = null;
//	public static void doWrite(String id,String name,String url){
//		//判断是否存在
//		searchRequestBuilder.setTypes("couchbaseDocument");
//		searchRequestBuilder.setQuery(QueryBuilders.termQuery("companyId", id));
//		sr = searchRequestBuilder.execute().actionGet();
//		long count = sr.getHits().getTotalHits();
//		if(count == 0)
//		{
//			if(null!=url&&url.hashCode()!=0)
//			{
//				list2.add(name+" "+url);
//			}
//			else
//			{
//				list2.add(name);
//			}
//			System.out.println(count+"\t"+name);
//		}
//		/*		
//		String sql = "insert into no_holder_t(id,name) values(?,?)";
//		try {
//			pst = conn.prepareStatement(sql);
//			pst.setString(1, id);
//			pst.setString(2, name);
//			pst.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				pst.close();
//				pst = null;
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}*/
//	}
//	public static void getDate(){
//		String sql = "SELECT ID FROM no_holder_t";
//		ResultSet rs = null;
//		try {
//			pst = conn.prepareStatement(sql);
//			rs = pst.executeQuery();
//			while(rs.next()){
//				list.add(rs.getString(1));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				rs.close();
//				pst.close();
//				pst = null;
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		//Client client=ElasticsearchUtil.getClient();
//		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("etp_holder_e");
//		searchRequestBuilder.setTypes("couchbaseDocument");
//		SearchResponse sr = null;
//		for(int i=0;i<list.size();i++)
//		{
//			searchRequestBuilder.setQuery(QueryBuilders.termQuery("companyId", list.get(i)));
//			sr = searchRequestBuilder.execute().actionGet();
//			long count = sr.getHits().getTotalHits();
//			if(count == 0)
//			{
//				list2.add(list.get(i));
//				System.out.println(count);
//			}
//		}
//	}
//}
