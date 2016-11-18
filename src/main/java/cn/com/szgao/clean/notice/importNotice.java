package cn.com.szgao.clean.notice;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import cn.com.szgao.notice.esAndcb.RecordData;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.StringUtils;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

public class importNotice {
	private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(importNotice.class.getName());
	static Map<String,List<RecordData>> MAPS=new HashMap<String,List<RecordData>>();
	static long ERRORSUM=0;	//出错数据条数
	static long INPUTSUM=0;	//
	static long REPEATSUM=0;	//去重后数据条数
	static long SUM = 0 ;
	/**
	 * 法院公告
	 * 数据写库PostgreSql和couchbase
	 * JSON导入extracl_url_t表和court桶
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {	
		//清空不用的文件或日志
//				File filepor=new File("D:\\Company_File\\log4j-1216\\Java20\\batchImport.log");   
//				if(filepor.exists()){
//					filepor.delete();			
//				}
//				filepor=null;
		long da=System.currentTimeMillis();
		PropertyConfigurator.configure("F:\\work\\WorkSpace_Eclipse\\WorkSpace_Eclipse\\MassPick\\WebContent\\WEB-INF\\log4j.properties");    
		//导入文件地址
		File file=new File("D:\\Notice\\法院公告\\DF\\20160222\\JSON");
		Bucket bucket = null;
		bucket = connectionBucket(bucket);
		try {
			show(file,bucket);	
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		finally{
			file = null;
			bucket.close();
			cluster2 = null;
		}
		logger.info("所有文件总耗时"+(((System.currentTimeMillis()-da)/1000)/60)+"分钟");
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
			logger.info("读取<<"+file.getName()+">>文件耗时"+(System.currentTimeMillis()-da)+"毫秒");
			return;
		}
		File[] files=file.listFiles();
		System.out.println("----files---" + files);
		for(File fi:files){
			if(fi.isFile()){
				long da=System.currentTimeMillis();		
				String name=fi.getParentFile().getPath();
				name=name.substring(name.lastIndexOf("\\")+1,name.length());			
				create(fi,bucket);
				logger.info("读取"+name+"<<"+fi.getName()+">>文件耗时"+(System.currentTimeMillis()-da)+"毫秒");
			}
			else if(fi.isDirectory()){
				show(fi,bucket);
			}
			else{
				continue;
			}
		}
	}
	/**
	 * 写数据   
	 * @param <JSONObject>
	 * @param file
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	private static <ObjectDataVO, JSONObject> void create(File file,Bucket bucket) throws Exception, UnsupportedEncodingException{
		String name=file.getParentFile().getPath();
		name=name.substring(name.lastIndexOf("\\")+1,name.length());
		 BufferedReader reader = null;
	        Gson gson = new Gson();	
	        NoticeVO notice = null;
			List<NoticeVO> list =  new ArrayList<NoticeVO>();
			 String temp = null;
			 int sum = 0;
			 int i = 0;
		try {
            reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
            	notice = gson.fromJson(temp, NoticeVO.class);
            	notice.setUuid(StringUtils.getUUID(notice.getUrl().toString()));
            	list.add(notice);
            	if(list.size()>=1000){
            		  list = removeDuplicate(list); //去除本次集合中重复数据
            		  sum=sum + list.size();
            		boolean result2=jdbcCreateJSONPostgresql(list);
            		if(result2){
            		logger.info("第:"+i+"批次:"+list.size()+"条数据");
            		list = null;
            		list =  new ArrayList<NoticeVO>();
            		}
            	}
            }
            if(list.size()>0){
            list = removeDuplicate(list); //去除本次集合中重复数据
            sum=sum + list.size();
            	boolean result=createJsonToCB(list,bucket);
            	if(!result){			
            		logger.error("读取"+name+"<<"+file.getName()+">>文件时发生JSON异常!");
            	}
            	statisticalCount(file,sum);
            }
		} catch (Exception e) {
			logger.error("读取"+name+"<<"+file.getName()+">>文件时发生IO异常:"+e.getMessage());			
		}
		finally{
			logger.info(name+"<<"+file.getName()+">>记录条数为："+sum);
			reader.close();
			file=null;
			temp = null;
        	list = null;
		}	
	}
	/**
	 * 入库couchbase
	 * @param urlId
	 * @return
	 * @throws Exception
	 */
	public static boolean createJsonToCB(List<NoticeVO> list,Bucket bucket) throws Exception {
		JsonDocument doc =null;
		Gson gson=new Gson();
		NoticeVO notice = null;
		try {
			for(int i=0;i<list.size();i++){
				notice = new NoticeVO();
				if(null != list.get(i).getTitle() && !"".equals(list.get(i).getTitle())){
					notice.setTitle(list.get(i).getTitle());   //title
				}
				if(null != list.get(i).getUrl() && !"".equals(list.get(i).getUrl())){
					notice.setDetailLink(list.get(i).getUrl());   //URL
				}
				if(null != list.get(i).getDate() && !"".equals(list.get(i).getDate())){
					notice.setPubDate(DateUtils.getReplaceAllDate(list.get(i).getDate()));
				}
				if(null != list.get(i).getLoadtime() && !"".equals(list.get(i).getLoadtime())){
					notice.setCollectTime(DateUtils.getReplaceAllDate(list.get(i).getLoadtime()));
				}
				String jsonss=gson.toJson(notice);
				String urlId=StringUtils.getUUID(list.get(i).getUrl().toString());
				doc = JsonDocument.create(urlId,JsonObject.fromJson(jsonss));
//				logger.info("------------所有内容："+doc);
				//插入JSON文档到库 
				bucket.upsert(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			doc = null;
			notice = null;
			gson = null;
		}
        	return true;
	}
	
	
	/**
	 * 用jdbc把Json数据写入PG
	 * 裁判文书
	 * @throws ClassNotFoundException 
	 */
	public static boolean jdbcCreateJSONPostgresql(List<NoticeVO> list) throws SQLException, ClassNotFoundException{
			Connection conn=getConnection();	
			PreparedStatement stmt=null;//供查询使用
			PreparedStatement stmt2=null;//供插入操作使用
			ResultSet rset=null;
			conn.setAutoCommit(false); 
			int i=0;
			int sum=list.size();
			int count=list.size();
			int index=1;
			String value=JsonSql(count);
			stmt=conn.prepareStatement(value);
			String urlId = null;

			for(int n=0;n<list.size();n++){
				urlId= StringUtils.getUUID(list.get(n).getUrl().toString());
				stmt.setString(index++, urlId);
			}
			rset =stmt.executeQuery();
			Map<String,String> map=JsongetMap(rset);
			stmt2=conn.prepareStatement("INSERT INTO court_pub_t_001(URL_ID,URL,URL_TEXT,URL_STATE,CREATE_DATE,LAST_MODIFY_DATE) values(?,?,?,?,?,?)");
			String urlvalue=null;
			
			try {
				for(i=0;i<sum;i++)
				{						
					//需要指定URL生成UUID的字符串作为参数			
					  urlId =StringUtils.getUUID(list.get(i).getUrl().toString());
//					urlId = StringUtils.getUUID(list.get(i).toString());	//根据整条数据生成UUID	
					java.sql.Date data=new java.sql.Date(System.currentTimeMillis());
					urlvalue=map.get(urlId);
					if(null==urlvalue){
						stmt2.setString(1,urlId);	
						stmt2.setString(2,list.get(i).getUrl().toString());	
						stmt2.setString(3,list.get(i).getTitle().toString());
						stmt2.setShort(4, (byte)1);
						stmt2.setDate(5,data);
						stmt2.setDate(6,data);
						stmt2.addBatch();
					}
				}		
			} catch (Exception e) {
				logger.error(e.getMessage());
				conn.rollback();
				return false;
			}
			finally{
				stmt2.executeBatch();
				stmt2.clearBatch();
				map.clear();	
				conn.commit();		
				rset.close();
				stmt.close();
				stmt2.close();
				conn.close();
			}
		return true;
	}
	
	/**
	 * json数据写pg
	 * 裁判文书
	 * @param count
	 * @return
	 */
	private static String JsonSql(int count){
		StringBuffer sb=new StringBuffer("SELECT URL_ID FROM court_pub_t_001 WHERE URL_ID IN (");
		int index=0;		
		for(index=0;index<count;index++){
			if(index==0){				
				sb.append("?");
			}
			else{
				sb.append(",").append("?");
			}
		}
		sb.append(")");
//		logger.info("----------查询SQL："+sb.toString());
		return sb.toString();
	}
	
	/**
	 * 设置map数据
	 * @param rset
	 * @return
	 * @throws SQLException
	 */
	private static Map<String,String> JsongetMap(ResultSet rset) throws SQLException{		
		Map<String,String> map=new HashMap<String,String>();
		while(rset.next()){
			String val=rset.getString(1);
			map.put(val,val);
		}
		return map;
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
	public static List<NoticeVO> removeDuplicate(List<NoticeVO> list) { 
		for ( int i = 0 ; i < list.size() - 1 ; i ++ ) { 
		for ( int j = list.size() - 1 ; j > i; j -- ) { 
		if (list.get(j).getUuid().equals(list.get(i).getUuid())) { 
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
//		String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb";
		String url = "jdbc:postgresql://192.168.1.2:5432/extractdb";
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
			return cluster2.openBucket("courtPub",1,TimeUnit.MINUTES);	
		}
}
