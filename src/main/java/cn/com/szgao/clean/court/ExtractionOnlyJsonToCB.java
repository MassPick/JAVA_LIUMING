package cn.com.szgao.clean.court;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

///////\//import cn.com.szgao.court.esAndcb.CommonConstant;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.CouchbaseConnect;
import rx.Observable;
import rx.functions.Func1;


/**
 * 将 excel json产生的json数据  批量  写到 CB 中
 * @author liuming
 * @Date 2016年6月23日 下午3:05:47
 */
public class ExtractionOnlyJsonToCB {
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(ExtractionOnlyJsonToCB.class.getName());
	static Map<String,List<RecordData>> MAPS=new HashMap<String,List<RecordData>>();
	static long ERRORSUM=0;	//出错数据条数
	static long INPUTSUM=0;	//
	static long REPEATSUM=0;	//去重后数据条数
	static long SUM = 0 ;
	static long count = 0 ;
	
	static long SUM_Count = 0 ;//总数

	
	public static void main(String[] args) throws Exception {	
		long da=System.currentTimeMillis();
		PropertyConfigurator.configure("D:\\data\\wenhao\\Mass\\Mass\\log\\log4j.properties");    
		//导入文件地址
		
		
		//----以下两个存在114 的 courtEJ
//		File file=new File("E:\\中国裁判文书网展示文书详细页面（旧版高院）Excle");
		File file=new File("E:\\中国裁判文书网展示文书详细页面（旧版高院）Json");
		
		
		//----以下两个存在114 的 courtLocal
		File file3=new File("E:\\法院旧版地方Excle");
		File file4=new File("E:\\法院旧版地方Json");
		
		
//		------------------这个数据没有入库？
		
		
		
		Bucket bucket = null;
//		bucket = connectionBucket(bucket);
		try {
//			show(file,bucket);	
			
			show(file3,bucket);	
			show(file4,bucket);	
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}finally{
//			file = null;
//			bucket.close();
			cluster2 = null;
		}
		logger.info("所有文件总耗时"+(((System.currentTimeMillis()-da)/1000)/60)+"分钟");
		logger.info("总数：   "+ SUM_Count    );
		logger.info("每秒: " +  (SUM_Count/( (System.currentTimeMillis()-da)/1000)   )  +"个"   );
		
		record();
	}
	
	//连接CB
	private static Bucket connectionBucket(Bucket bucket){
		try {
			bucket = connectionCouchBaseLocal();//本地CB
		} catch (Exception e) {
			while(true){	
				try{
					bucket = connectionCouchBaseLocal();//本地CB
					break;
				}
				catch(Exception ee){
					logger.error(ee);
				}
			}
		}
		
		return bucket;
	}
	
	/**
	 * 递归遍历文件
	 * @param file
	 * @throws  
	 * @throws Exception 
	 */
	private static void show(File file,Bucket bucket) throws Exception{
		if(file.isFile()){
			long da=System.currentTimeMillis();				
			create(file,bucket);
			System.out.println ("读取<<"+file.getName()+">>文件耗时"+(System.currentTimeMillis()-da)+"毫秒");
			return;
		}
		File[] files=file.listFiles();
		if(files.length>0){
			System.out.println("----files  第一个：---" + files[0].getPath());
		}
		for(File fi:files){
			if(fi.isFile()){
				if(fi.getName().contains("download_fail")){
					continue;
				}
				long da=System.currentTimeMillis();		
				String name=fi.getParentFile().getPath();
				name=name.substring(name.lastIndexOf("\\")+1,name.length());			
				create(fi,bucket);
				System.out.println ("读取"+name+"<<"+fi.getName()+">>文件耗时"+(System.currentTimeMillis()-da)+"毫秒");
			}
			else if(fi.isDirectory()){
				show(fi,bucket);
			}
			else{
				continue;
			}
		}
	}
	
	static List<JsonDocument> documents = new ArrayList<JsonDocument>();
	static Bucket bucket = null;
	static Bucket bucket2 = null;
	static JsonObject content = null;
	 
	/**
	 * 写数据     
	 * @param <JSONObject>
	 * @param file
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	private static <ObjectDataVO, JSONObject> void create(File file,Bucket bucket1) throws Exception, UnsupportedEncodingException{
		
		int countLen = 0;
		
		String name=file.getParentFile().getPath();
		name=name.substring(name.lastIndexOf("\\")+1,name.length());
		 BufferedReader reader = null;
	        Gson gson = new Gson();	
	        WholeCourtVO arch = null;
//			List<WholeCourtVO> list =  new ArrayList<WholeCourtVO>();
			 String temp = null;
			 int sum = 0;
		try {
            reader = new BufferedReader(new FileReader(file));
        	long startTime = System.currentTimeMillis();
            while ((temp = reader.readLine()) != null) {
            	arch = gson.fromJson(temp, WholeCourtVO.class);
            	
				content = JsonObject.fromJson(gson.toJson(arch));
				documents.add(JsonDocument.create(arch.getWholeCourtId(), content));
				
				SUM_Count++;
				System.out.println("总数：  "+SUM_Count);
            	countLen++;
            	System.out.println(countLen);
            	if (countLen % 1000 == 0) {
					while (true) {
						try {
							// 更新文档
//							bucket = CouchbaseConnect.commonBucket("192.168.1.114:8091", "courtEJ");
							bucket = CouchbaseConnect.commonBucket("192.168.1.114:8091", "courtLocal");
							
							break;
						} catch (Exception e) {
							logger.info("---------------------------> 插入BC超时");
							logger.error(e.getMessage());
						}
					}

					Observable.from(documents).flatMap(new Func1<JsonDocument, Observable<JsonDocument>>() {
						public Observable<JsonDocument> call(final JsonDocument docToInsert) {
							return bucket.async().upsert(docToInsert);
						}
					}).last().toBlocking().single();
					
					content = null;
					documents = null;
					documents = new ArrayList<JsonDocument>();
					long endTime = System.currentTimeMillis();

					String result = (float) ((endTime - startTime) / 1000) + "秒";
					logger.info("----  " + countLen / 1000 + " 批次" + "  " + result + "  总数:  " + countLen + "   file: "
							+ file.getPath()  + "  KEY:  " + arch.getWholeCourtId())     ;
					endTime = 0;
					startTime = System.currentTimeMillis();
					result = null;
				}
            	
            }
            
            if(documents!=null&&documents.size()>0){
    			while (true) {
    				try {
    					// 更新文档
//						bucket = CouchbaseConnect.commonBucket("192.168.1.114:8091", "courtEJ");
						bucket = CouchbaseConnect.commonBucket("192.168.1.114:8091", "courtLocal");

    					Observable.from(documents).flatMap(new Func1<JsonDocument, Observable<JsonDocument>>() {
    						public Observable<JsonDocument> call(final JsonDocument docToInsert) {
    							return bucket.async().upsert(docToInsert);
    						}
    					}).last().toBlocking().single();
    					content = null;
    					documents = null;
    					documents = new ArrayList<JsonDocument>();

    					long endTime = System.currentTimeMillis();
    					String result = (float) ((endTime - startTime) / 1000) + "秒";
    					logger.info("----  " + countLen / 1000 + " 批次" + "  " + result + "  总数:  " + countLen + "   file: "
    							+ file.getPath()  + "  KEY:  " + arch.getWholeCourtId())     ;
    					endTime = 0;
    					startTime = System.currentTimeMillis();
    					result = null;
//    					key = null;
    					break;
    				} catch (Exception e) {
    					logger.info("---------------------------> 插入BC超时");
    					logger.error(e.getMessage());
    				}
    			}
    		}
            
 
		} catch (Exception e) {
			logger.error("读取"+name+"<<"+file.getName()+">>文件时发生IO异常:"+e.getMessage());			
		}
		finally{
			logger.info(name+"<<"+file.getName()+">>记录条数为："+sum);
			reader.close();
//			list = null;
			file=null;
			reader.close();
		}	
	}
	
	
	/**
	 * 裁判文书json文件导入CB
	 * @param list
	 * @param bucket
	 * @return
	 * @throws Exception
	 */
	public static boolean createJsonToCB (List<WholeCourtVO> list,Bucket bucket) throws Exception {
		Gson gson=new Gson();
		JsonDocument doc =null;
		WholeCourtVO archs = null;
		List<RelativeVO> relativeList = null;
		List<RelativeVO> lists = null;
		RelativeVO relative = null;
		try {
			for(int i = 0 ; i < list.size() ; i++){
				count++;
				archs = new WholeCourtVO();
				relativeList = new ArrayList<RelativeVO>();
				lists = new ArrayList<RelativeVO>();
				String uuid = list.get(i).getWholeCourtId();
//				relative = new RelativeVO();
//				relativeList = list.get(i).getRelativeCases();
//				//------------------------------以下为关联文书-----------------
//				for(int a = 0 ; a <relativeList.size() ; a++){
//					if(null != relativeList.get(a).getApprovalDate() && !"".equals(relativeList.get(a).getApprovalDate())){
//						relative.setApprovalDate(relativeList.get(a).getApprovalDate().toString());
//					}
//					if(null != relativeList.get(a).getCaseNum() && !"".equals(relativeList.get(a).getCaseNum())){
//						relative.setCaseNum(relativeList.get(a).getCaseNum().toString());
//					}
//					if(null != relativeList.get(a).getClosedType() && !"".equals(relativeList.get(a).getClosedType())){
//						relative.setClosedType(relativeList.get(a).getClosedType().toString());
//					}
//					if(null != relativeList.get(a).getCourtName() && !"".equals(relativeList.get(a).getCourtName())){
//						relative.setCourtName(relativeList.get(a).getCourtName().toString());
//					}
//					if(null != relativeList.get(a).getMark() && !"".equals(relativeList.get(a).getMark())){
//						relative.setMark(relativeList.get(a).getMark().toString());
//					}
//					if(null != relativeList.get(a).getRelative_id() && !"".equals(relativeList.get(a).getRelative_id())){
//						relative.setRelative_id(relativeList.get(a).getRelative_id().toString());
//					}
//					if(null != relativeList.get(a).getSuitType() && !"".equals(relativeList.get(a).getSuitType())){
//						relative.setSuitType(relativeList.get(a).getSuitType().toString());
//					}
//					if(null != relativeList.get(a).getType() && !"".equals(relativeList.get(a).getType())){
//						relative.setType(relativeList.get(a).getType().toString());
//					}
//					lists.add(relative);
//				}
//				//---------------------------------以上为关联文书----------------------------
//				if(lists.size()>0){
//					archs.setRelativeCases(lists);
//				}
//				if(null != list.get(i).getProvince() && !"".equals(list.get(i).getProvince())){
//					archs.setProvince(list.get(i).getProvince().toString());
//				}
//				if(null != list.get(i).getApprovalDate() && !"".equals(list.get(i).getApprovalDate())){
//					archs.setApprovalDate(list.get(i).getApprovalDate().toString());
//				}
//				if(null != list.get(i).getCollectDate() && !"".equals(list.get(i).getCollectDate())){
//					archs.setCollectDate(list.get(i).getCollectDate().toString());
//				}
//				if(null != list.get(i).getCaseCause() && !"".equals(list.get(i).getCaseCause())){
//					archs.setCaseCause(list.get(i).getCaseCause().toString());
//				}
//				if(null != list.get(i).getPublishDate() && !"".equals(list.get(i).getPublishDate())){
//					archs.setPublishDate(list.get(i).getPublishDate().toString());
//				}
//				if(null != list.get(i).getCatalog() && !"".equals(list.get(i).getCatalog())){
//					archs.setCatalog(list.get(i).getCatalog().toString());
//				}
//				if(null != list.get(i).getCaseNum() && !"".equals(list.get(i).getCaseNum())){
//					archs.setCaseNum(list.get(i).getCaseNum().toString());
//				}
//				if(null != list.get(i).getCity() && !"".equals(list.get(i).getCity())){
//					archs.setCity(list.get(i).getCity().toString());
//				}
//				if(null != list.get(i).getTitle() && !"".equals(list.get(i).getTitle())){
//					archs.setTitle(list.get(i).getTitle().toString());
//				}
//				if(null != list.get(i).getArea() && !"".equals(list.get(i).getArea())){
//					archs.setArea(list.get(i).getArea().toString());
//				}
//				if(null != list.get(i).getDetailLink() && !"".equals(list.get(i).getDetailLink())){
//					archs.setDetailLink(list.get(i).getDetailLink().toString());
//				}
//				if(null != list.get(i).getSummary() && !"".equals(list.get(i).getSummary())){
//					archs.setSummary(list.get(i).getSummary().toString());
//				}
//				if(null != list.get(i).getCourtName() && !"".equals(list.get(i).getCourtName())){
//					archs.setCourtName(list.get(i).getCourtName().toString());
//				}
//				if(null != list.get(i).getSuitType() && !"".equals(list.get(i).getSuitType())){
//					archs.setSuitType(list.get(i).getSuitType().toString());
//				}
				
				String jsonss = gson.toJson(archs);
				
				doc = JsonDocument.create(uuid,JsonObject.fromJson(jsonss));
				bucket.upsert(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			gson = null;
			doc = null;
			archs = null;
			relativeList = null;
			lists = null;
			relative = null;
		}
		return true;
	}
	
	/**
	 * 统计导入的各地的记录条数
	 */
	public static void statisticalCount(File file,long count){		
		//取省名
		String provinceName=file.getParentFile().getParent();
		provinceName=provinceName.substring(provinceName.lastIndexOf("\\")+1,provinceName.length());
		//取市名
		String city =file.getParentFile().getPath();
		city=city.substring(city.lastIndexOf("\\")+1,city.length());
		List<RecordData> list=MAPS.get(provinceName);
		if(null==list||list.size()<=0){
			list=new ArrayList<RecordData>();
			list.add(new RecordData(provinceName,city,count));
			MAPS.put(provinceName,list);
		}
		else{
			boolean result=true;
			for(RecordData re:list){
				if(re.getCityName().equalsIgnoreCase(city)){
					re.setNumberData(re.getNumberData()+count);
					result=false;
					break;
				}
			}
			if(result){
				list.add(new RecordData(provinceName,city,count));
				MAPS.put(provinceName,list);
			}		
		}
	}
	/**
	 * 记录各地数据
	 */
	public static void record(){
		long sumCount=0;
		long sum=0;
		for(Map.Entry<String,List<RecordData>> map:MAPS.entrySet()){
			logger.info("###:"+map.getKey());
			List<RecordData> list=map.getValue();
			 sum=0;
			for(RecordData recordData:list){
				logger.info("###:"+recordData.getCityName()+"----记录条数:"+recordData.getNumberData());
				sum+=recordData.getNumberData();
			}
			sumCount+=sum;
			logger.info(map.getKey()+"省总数据条数据:"+sum);
			logger.info("------------------------------");
		}
		logger.info("总文件数据条数:"+sumCount);
		logger.info("去重后的数据条数据:"+REPEATSUM);
		logger.info("错误数据条数:"+ERRORSUM);
	}
	
	/**
	 * 根据UUID去重
	 * @param list
	 * @return
	 */
	public static List<WholeCourtVO> removeDuplicate(List<WholeCourtVO> list) { 
		for ( int i = 0 ; i < list.size() - 1 ; i ++ ) { 
		for ( int j = list.size() - 1 ; j > i; j -- ) { 
		if (list.get(j).getWholeCourtId().equals(list.get(i).getWholeCourtId())) { 
		list.remove(j); 
				} 
			} 
		} 
		return list;
		} 
	
	/**
	 * 连接postgreSql库
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException,SQLException {
		String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb";
		String usr = "postgres";
		String psd = "615601.xcy*";
		Connection conn = null;
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, usr, psd);
		return conn;
	}
	/**
	 * 链接couchbase桶
	 * @return
	 */
	 public static Bucket connectionCouchBaseLocal(){
			//连接指定的桶		
			return cluster2.openBucket("court_New",1,TimeUnit.MINUTES);	
		}
	 private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
}
