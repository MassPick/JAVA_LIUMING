package cn.com.szgao.wash.data.court;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.web.client.RestTemplate;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.dto.BuildManagerDetailVO;
import cn.com.szgao.dto.BuildVO;
import cn.com.szgao.dto.DocWholeCourtNameVO;
import cn.com.szgao.dto.DocWholeCourtVO;
import cn.com.szgao.dto.FfBuildVO;
import cn.com.szgao.dto.FfWholeCourtNameVO;
import cn.com.szgao.dto.FfWholeCourtVO;
import cn.com.szgao.dto.RowsBuildVO;
import cn.com.szgao.dto.RowsWholeCourtNameVO;
import cn.com.szgao.dto.RowsWholeCourtVO;
import cn.com.szgao.dto.WholeCourtNameVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.BloomFilter;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.EsUtil;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;
import rx.functions.Func1;

import rx.Observable;




/**
 * 法院名进cb  
 * @author liuming
 * @Date 2016年5月11日 上午10:34:24
 */
public class UniqCourtNameFromTxtToCB {

	private static Logger log = LogManager.getLogger(UniqCourtNameFromTxtToCB.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(UniqCourtNameFromTxtToCB.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 文件
		
		
		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();
			
			show(new File("D:\\lm\\log\\法院名_马星.txt"));
			
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
			log.info("开始时间--------------------" + formatter.format(startTime));
			
			log.info("结束时间--------------------" + formatter.format(endDate));
			log.info("Time : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
			log.info("Time : " + (float) (  (float)((endTime - startTime) / 1000) / 60) + "分钟");
			log.info("Time : " + (float) ((endTime - startTime) / 1000) + "秒");
			log.info("Total : " + count);
			log.info("Speed : " + (float) (count / (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) ) + "个/小时");
			log.info("Speed : " + (float) (count/(float) (  (float)( (endTime - startTime) / 1000) / 60)  ) + "个/分种");
			log.info("Speed : " + (float) (count/ (float) ((endTime - startTime) / 1000) )
					+ "个/秒");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch ( Exception e) {
			e.printStackTrace();
		}

	}

	static BloomFilter bf=new BloomFilter();
	
	public static Bucket bucket = null;
	
	//新版本无法院或案号记录
	static int noCourt=0;
	static int size=100000;

	public static void readFile(File file) {
		int flag = 0;
		int countM = 0;
		int countNull = 0;
		String tempCourtName = "";
		long startTime = System.currentTimeMillis();
		long startTime2 = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		
		BufferedWriter fwUn = null;
		
		

		BufferedReader reader = null;
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

		try {
			String temp = null;
			WholeCourtNameVO vo = null;
			Gson gs = new Gson();
			JSONObject temJson = null;
			String key1 = "";

			List<JsonDocument> documents = new ArrayList<JsonDocument>();

			JsonObject content = null;
			int countLen = 0;

			while ((temp = reader.readLine()) != null) {
				vo=new WholeCourtNameVO();
				 
				count++;
				countM++;
				System.out.println(count);
				try {

					if(temp.contains("法院")){
						vo.setCourtName( temp.replaceAll("[\\n,\\t,\\r,\\s,&nbsp;\\\\]", "").replace(" ","") );
						
					}


					if (vo != null) {

						if(!StringUtils.isNull(vo.getCourtName() ) ){
							if(vo.getCourtName().length()>40||vo.getCourtName().length()<5){
								continue;
							}
//							vo.setFlag(1);
							
//							json = "{\"courtName\":\""+whVo.getCourtName().replaceAll("[\\n,\\t,\\r,\\s,&nbsp;\\\\]", "").replace(" ","")+"\"}";  
//							content = JsonObject.fromJson(  json) ;
							content = JsonObject.fromJson(StringUtils.GSON.toJson(vo)  ) ;
							//入CB库 
							bucketLocal.upsert(JsonDocument.create( StringUtils.NBG.generate(vo.getCourtName().replaceAll("[\\n,\\t,\\r,\\s,&nbsp;\\\\]", "").replace(" ","")).toString()   , content));

						}
//						else{//没有  案号的记录下来
//							writerString(fwUn, new Gson().toJson(vo));
//						}

//						
//						vo.setWholeCourtId(null);
//						vo.setKey(null);
						
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			
			 

		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {

				logger.info("结束!!!!!!!!!!!");
				logger.info("空数据" + countNull);

				long endTime = System.currentTimeMillis();
				Date endDate = new Date(endTime);
				System.out.println("结束时间--------------------" + formatter.format(endDate));
				System.out.println("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
				System.out.println("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
				System.out.println("Total : " + count);
				System.out.println(
						"Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
				System.out.println("Speed : "
						+ (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60)) + "个/小时");

			} catch (Exception e) {
				logger.info(e);
			}
		}
	}

	public static void show(File file) throws IOException, ParseException {
		if (file.isFile()) {
			try {
				readFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		if (null != files) {
			for (File fi : files) {
				if (fi.isFile()) {
					try {
						readFile(fi);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					show(fi);
				} else {
					continue;
				}
			}
		}
	}
	static int uniqi = 0;//旧数据去重后的数据
	static int noCourtOld = 0;//旧数据没有法院或案号的
	
	static int i = 0;
	 
	
	public static Bucket bucketLocal = null;
	static{
		while (true) {
			try {
				// 更新文档
				bucketLocal = CouchbaseConnect.commonBucket("192.168.1.30:8091", "courtName");
				break;
			} catch (Exception e) {
				log.info("---------------------------> 插入BC超时");
				log.error(e.getMessage());
			}
		}
	}
	
	
	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + "\n");

			// fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			log.error("写文件异常" + e.getMessage());
		} finally {
			// if (fw != null) {
			// try {
			// fw.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}
	

}
