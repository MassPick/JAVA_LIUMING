//package cn.com.szgao.wash.data.executed;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.common.unit.TimeValue;
////import org.elasticsearch.index.query.FilterBuilders;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//
//import com.couchbase.client.java.Bucket;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//
//import cn.com.szgao.dto.DocWholeCourtVO;
//import cn.com.szgao.dto.WholeCourtVO;
//import cn.com.szgao.util.ConfigUtils;
//import cn.com.szgao.util.CouchbaseConnect;
//import cn.com.szgao.util.EsUtil;
//import cn.com.szgao.util.StringUtils;
//
//public class InsertExecutedFromESToCBOrText {
//    static Gson gson=new Gson();
//    static int index=0;
//	public static void main(String[] args) {
//		//PropertyConfigurator.configure("D:\\Users\\dell\\git\\maven-webapp\\src\\main\\webapp\\WEB-INF\\log4j.properties");
//		//Logger log = LogManager.getLogger(TextES2.class);
//		// TODO Auto-generated method stub
//		long da=System.currentTimeMillis();
//		show();
//		System.out.println((System.currentTimeMillis()-da)/1000);
//		System.out.println();
//	}
//	public static void show(){
//		//QueryBuilder qb = QueryBuilders.termsQuery("", "");
//		Client esClient = EsUtil.getClient();
//	    SearchResponse searchResponse = esClient.prepareSearch("wholecourt")
//	    //加上这个据说可以提高性能，但第一次却不返回结果
//	    .setSearchType(SearchType.SCAN).setPostFilter(FilterBuilders.termFilter("doc.source",1))
//	    //实际返回的数量为5*index的主分片格式
//	        .setSize(1000)
//	        //这个游标维持多长时间
//	        .setScroll(TimeValue.timeValueMinutes(8))
//	        .execute().actionGet();
//	    //第一次查询，只返回数量和一个scrollId
//	    System.out.println(searchResponse.getHits().getTotalHits());
//	    System.out.println(searchResponse.getHits().hits().length);
//	    //第一次运行没有结果
//	    for (SearchHit hit : searchResponse.getHits()) {
//	        System.out.println(hit.getSourceAsString());
//	    }
//	    System.out.println("------------------------------");
//	    int sum=0;
//	    JsonObject json=null;
//	    List<String> list=new ArrayList<String>();
//	    List<WholeCourtVO> lists=new ArrayList<WholeCourtVO>();
//	    
//	    WholeCourtVO vo=null; 
//	    
//	    DocWholeCourtVO docVo=null;
//	    //使用上次的scrollId继续访问
//	    while(true){
//		    searchResponse = esClient.prepareSearchScroll(searchResponse.getScrollId())
//		        .setScroll(TimeValue.timeValueMinutes(8))
//		        .execute().actionGet();
//		    System.out.println(searchResponse.getHits().getTotalHits());
//		   // System.out.println(searchResponse.getHits().hits().length);
//		    for (SearchHit hit : searchResponse.getHits()) {
//		        //System.out.println(hit.getSourceAsString());
//		    	
//		    	
////		        json = gson.fromJson(hit.getSourceAsString(), JsonObject.class);
////		        list.add(json.get("doc").toString());
//		        
//		    	docVo = gson.fromJson(hit.getSourceAsString(), DocWholeCourtVO.class);
//		    	vo=docVo.getDoc();
//		    	vo.setSubjectMatterN(StringUtils.convertStringToDouble(vo.getSubjectMatter(),4));
//		    	lists.add(vo);
////		        list.add(json.get("doc").toString());
//		        
//		        
//		        sum++;
//		        System.out.println(sum);
//		        if(lists.size()>=10000){
//		        	index++;
//		        	//System.out.println(true);
//		        	filteText2(lists);
//		        	
//		        	
//		        	list.clear();
//		        	list=null;
//		        	list=new ArrayList<String>();
//		        }
//		       //log.info(hit.getSourceAsString());
//		    }
//		    if (searchResponse.getHits().getHits().length == 0) {
//		        break;
//		    }
//		    //if(sum>=1000000){break;}
//	    }
//	    esClient.close();
//	    if(lists.size()>0){
//        	index++;
//        	//System.out.println(true);
//        	filteText2(lists);
//        	list.clear();
//        	list=null;
//        }
//	}
//	
//	public static  void writeToCB(List<String> value){
//		FileWriter filewr=null;
//		try {
//			 filewr=new FileWriter("G:\\执行executed\\"+index+".txt", true);
//			for(String val:value){
//				filewr.write(val+ System.getProperty("line.separator"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//		}finally {
//			
//			if(null!=filewr){
//				try {
//					filewr.close();
//					//outStream.close();
//				} catch (IOException e) {					
//					e.printStackTrace();
//				}
//			}
//		}
//	}
////	private static Logger log = LogManager.getLogger(InsertExecutedFromESToCBOrText.class);
////	public static Bucket bucketLocal = null;
////	static{
////		
//////		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
////		
////		while (true) {
////			try {
////				// 更新文档
////				bucketLocal = CouchbaseConnect.commonBucket("192.168.1.30:8091", "courtName");
////				break;
////			} catch (Exception e) {
//////				log.info("---------------------------> 插入BC超时");
//////				log.error(e.getMessage());
////			}
////		}
////	}
//	
//	private static Gson gs=new Gson();
//	
//	public static void filteText2(List<WholeCourtVO> value){
//		FileWriter filewr=null;
//		try {
//			 filewr=new FileWriter("D:\\lm\\log\\temp\\被执行人\\被执行人_旧数据清洗(从ES来)\\"+index+".txt", true);
//			
//			 for (WholeCourtVO wholeCourtVO : value) {
//				 filewr.write( gs.toJson(wholeCourtVO)  + System.getProperty("line.separator"));
//			}
//			  
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//		}finally {
//			
//			if(null!=filewr){
//				try {
//					filewr.close();
//					//outStream.close();
//				} catch (IOException e) {					
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	
//	public static void filteText(List<String> value){
//		FileWriter filewr=null;
//		try {
//			 filewr=new FileWriter("D:\\lm\\log\\temp\\被执行人_旧数据清洗(从ES来)\\"+index+".txt", true);
//			for(String val:value){
//				filewr.write(val+ System.getProperty("line.separator"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			
//		}finally {
//			
//			if(null!=filewr){
//				try {
//					filewr.close();
//					//outStream.close();
//				} catch (IOException e) {					
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//}
