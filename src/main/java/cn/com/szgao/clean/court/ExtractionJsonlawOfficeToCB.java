package cn.com.szgao.clean.court;
import java.io.BufferedReader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
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
//import com.couchbase.client.java.document.JsonDocument;
//import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

///////\//import cn.com.szgao.court.esAndcb.CommonConstant;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;


/**
 * 将文书中律师事务所数据写到CB
 * @author liuming
 * @Date 2016年10月13日 下午5:46:27
 */
public class ExtractionJsonlawOfficeToCB {
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(ExtractionJsonlawOfficeToCB.class.getName());
	static Map<String,List<RecordData>> MAPS=new HashMap<String,List<RecordData>>();
	static long ERRORSUM=0;	//出错数据条数
	static long INPUTSUM=0;	//
	static long REPEATSUM=0;	//去重后数据条数
	static long SUM = 0 ;
	static long count = 0 ;
	/**
	 * 裁判文书
	 * 数据写库PostgreSql和couchbase
	 * JSON导入extracl_url_t表和court桶
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {	
		long da=System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));    
		//导入文件地址
//		File file=new File("G:\\Data\\2016新数据\\JSON\\JSON20131231-20130601\\2013-11-04");
//		File file=new File("C:\\data\\文书新版本\\JSON\\JSON20131231-20130601");
//		File file=new File("C:\\data\\新版裁判文书JSON+HTML\\2016年\\JSON");
		File file=new File("D:\\lm\\log\\2016法院清洗后数据6\\1_20161013034132新版裁判文书网展示文书(以裁判日期查询)_2016年.json");
		
		
		Bucket bucket = null;
		try {
			show(file,bucket);	
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}finally{
			file = null;
//			bucket.close();
//			cluster2 = null;
		}
		logger.info("所有文件总耗时"+(((System.currentTimeMillis()-da)/1000)/60)+"分钟");
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
				if(fi.getName().contains("download_fail")){
					continue;
				}
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
		JsonObject content = null;
		int countLen = 0;
		BufferedReader reader = null;
		
		String key1="";
		try {

			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "GB18030");
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		
		
		String temp = null;
		WholeCourtVO vo = null;
		Gson gs = new Gson();
		net.sf.json.JSONObject  temJson = null;
		
		String te = null;
//		StringBuffer sb = new StringBuffer();
		while ((te = reader.readLine()) != null) {
			
			try {
				
				temJson = net.sf.json.JSONObject.fromObject(te.toString());
				
			} catch (Exception e) {
				logger.error("json异常:" + file.getPath() + "----" + temp);
				continue;
			}

			try {
				vo = gs.fromJson(te.toString(), WholeCourtVO.class);
//				sb = new StringBuffer();
				if(StringUtils.isNull(vo.getLawOfficeP() )&&StringUtils.isNull(vo.getLawyerD())){
					continue;
				}
			} catch (Exception e) {
				logger.error("json转vo异常:" + file.getPath() + "----" + te.toString());
				continue;
			}
			
			String l_p=vo.getLawOfficeP();
			String l_d=vo.getLawOfficeD();
			WholeCourtVO wc=new WholeCourtVO();
			if(!StringUtils.isNull(l_p)){
				String[] stemp =l_p.split(";");
				for (int i = 0; i < stemp.length; i++) {
					String[] s_p=stemp[i].split("#");
					
					key1 = StringUtils.NBG.generate(s_p[0]).toString();
					wc.setLawOffice(s_p[0]);
					content = JsonObject.fromJson(gs.toJson(wc));
					while (true) {
						try {
							// 更新文档
							bucket = CouchbaseConnect.commonBucket("192.168.1.13:8091", "lawOffice");
							bucket.upsert(JsonDocument.create(key1, content));
							break;
						} catch (Exception e) {
							logger.info("---------------------------> 插入BC超时");
							logger.error(e.getMessage());
						}
					}	
					
				}
			}
			if(!StringUtils.isNull(l_d)){
				String[] stemp =l_d.split(";");
				for (int i = 0; i < stemp.length; i++) {
					String[] s_p=stemp[i].split("#");
					
					key1 = StringUtils.NBG.generate(s_p[0]).toString();
					wc.setLawOffice(s_p[0]);
					content = JsonObject.fromJson(gs.toJson(wc));
					while (true) {
						try {
							// 更新文档
							bucket = CouchbaseConnect.commonBucket("192.168.1.13:8091", "lawOffice");
							bucket.upsert(JsonDocument.create(key1, content));
							break;
						} catch (Exception e) {
							logger.info("---------------------------> 插入BC超时");
							logger.error(e.getMessage());
						}
					}	
					
				}
			}
			
			countLen++;
			System.out.println(countLen  +"  "+key1);
		}
		 
		

		 
	}
	
	  
	 
}
