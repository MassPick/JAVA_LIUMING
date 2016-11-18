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
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;
import rx.functions.Func1;

import rx.Observable;



/**
 * 将法院名写到CB库
 * @author liuming
 * @Date 2016年4月28日 上午11:10:16
 */
public class UniqCourtNameToCB {

	private static Logger log = LogManager.getLogger(UniqCourtNameToCB.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(UniqCourtNameToCB.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 文件
		
//		File file = new File("D:\\lm\\log\\temp\\被执行人\\113925345-115065350");
		
//		File file = new File("D:\\lm\\log\\temp\\被执行人\\115065350-115901410");
//		File file = new File("D:\\lm\\log\\temp\\被执行人\\108200022-110900001");
		File file = new File("E:\\法院测试");
		
		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();
			
//			show(file);
			
			doListData3();
			
			
			log.info( "  新版数据无法院或案号： "+  noCourt +"  旧版数据无法院或案号： "+  noCourtOld+"  旧版数据去重后： "+  uniqi);
			
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
			log.info("路径：" + file.getPath());
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
				
				 
				

				count++;
				countM++;
				System.out.println(count);
				try {
					try {
						temJson = JSONObject.fromObject(temp);
					} catch (Exception e) {
						logger.error("json异常:" + file.getPath() + "----" + temp);
						continue;
					}
					try {
						vo = gs.fromJson(temp, WholeCourtNameVO.class);
					} catch (Exception e) {
						logger.error("json转vo异常:" + file.getPath() + "----" + temp);
						continue;
					}

					if (vo != null) {

 
						
						if(!StringUtils.isNull(vo.getCourtName() ) ){
							if(vo.getCourtName().length()>40||vo.getCourtName().length()<5){
								continue;
							}
							vo.setFlag(1);
							
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
	public static void doListData3() throws UnsupportedEncodingException {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		urlVariables.put("limits", 5000);
		urlVariables.put("inclusive_ends", true);
		urlVariables.put("skips", 0);
		String key = null;
		RestTemplate template = new RestTemplate();
		String result = null;
		Gson gson = new Gson();

		int source = 0;
		String openTime = null;
		String openTime_temp = null;
		String openTime_temp_new = null;

		String summary = null;
		String pubDate = null;


		
		BufferedWriter fwUn = null;
		
		
		BufferedWriter fwUnOld = null;
		
		
		
		while (true) {
			if (null == key) {

				while (true) {

					// 237208:01d0549b-8246-5b5c-9c07-bffe1cdcd2cd
					// 10185100:4dc8657f-d2b5-596e-b50c-b566a113b1f9
					try {
						result = template.getForObject(
								"http://192.168.1.4:8092/executedN/_design/court_only/_view/court_only?inclusive_end={inclusive_ends}&limit={limits}&skip=0",
								// "http://192.168.1.30:8092/etp_tt/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&skip=0",
								String.class, urlVariables);
								// 10,504,378

						// key="4dbe6c31-e1b6-547f-9ee2-de5b351ef6fc";
						// urlVariables.put("startkeys", "\"" + key + "\"");
						// result = template.getForObject(
						// "http://192.168.1.5:8092/executedN/_design/ex_all/_view/ex_all?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=0",
						// String.class, urlVariables);

						log.info(i + "  5000分页:" + key);
						break;
					} catch (Exception e) {
						log.error("---------------------------> 连BC查询   --超时");
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {

						}
						log.error(e.getMessage());
					}
				}

			} else {
				while (true) {
					try {
						// key="4dc8657f-d2b5-596e-b50c-b566a113b1f9";
						urlVariables.put("startkeys", "\"" + key + "\"");
						result = template.getForObject(
								"http://192.168.1.4:8092/executedN/_design/court_only/_view/court_only?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=1",
								// "http://192.168.1.30:8092/etp_tt/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=1",
								String.class, urlVariables);
						log.info(i + "  5000分页:" + key);
						break;
					} catch (Exception e) {
						log.error("---------------------------> 连BC查询   --超时");
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {

						}
						log.error(e.getMessage());
					}
				}
			}
			String value = new String(result.getBytes("ISO-8859-1"), "utf-8");

			FfWholeCourtNameVO jsn = gson.fromJson(value, FfWholeCourtNameVO.class);
			JsonObject content = null;
			String json=null;
			for (RowsWholeCourtNameVO r : jsn.getRows()) {
				
				key = r.getKey();
				i++;
//				System.out.println(i + ":" + key);
				
				System.out.println(i );
				WholeCourtNameVO whVo = r.getValue();// 00006a27-0c95-48a3-8249-645457d71b65
				if (whVo == null) {
					continue;
				}

				if(!StringUtils.isNull(whVo.getCourtName()) ){
					if(whVo.getCourtName().length()>40||whVo.getCourtName().length()<5){
						continue;
					}
					whVo.setFlag(0);
					
//					json = "{\"courtName\":\""+whVo.getCourtName().replaceAll("[\\n,\\t,\\r,\\s,&nbsp;\\\\]", "").replace(" ","")+"\"}";  
//					content = JsonObject.fromJson(  json) ;
					content = JsonObject.fromJson( gson.toJson(whVo)  ) ;
					//入CB库 
					bucketLocal.upsert(JsonDocument.create( StringUtils.NBG.generate(whVo.getCourtName().replaceAll("[\\n,\\t,\\r,\\s,&nbsp;\\\\]", "").replace(" ","")).toString()   , content));
					
				}else{ 
					continue;
				}
				 
			}

			if (i >= jsn.getTotal_rows()) {
				break;
			}

		}

	}
	
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
