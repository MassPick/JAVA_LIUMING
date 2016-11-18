package cn.com.szgao.clean.notice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;

import com.couchbase.client.deps.io.netty.util.internal.StringUtil;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

import cn.com.szgao.util.DateUtils;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.wash.data.AdministrationUtils;

/**
 * 以html为准，利用CB进行数据清洗    全国公告
 * 
 * @author liuming
 * @Date 2016年6月16日 下午2:32:12
 */
public class ExtractionHtmlCourtpubToTxt {
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(ExtractionHtmlCourtpubToTxt.class.getName());
	private static Logger log = LogManager.getLogger(ExtractionHtmlCourtpubToTxt.class.getName());
	static Map<String, List<RecordData>> MAPS = new HashMap<String, List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //
	static long REPEATSUM = 0; // 去重后数据条数
	static long SUM = 0;
	static long count = 0;

	static AdministrationUtils util;

	/**
	 * 裁判文书 数据写库PostgreSql和couchbase JSON导入extracl_url_t表和court桶
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 导入文件地址
		  
		
//		File file = new File("D:/法院公告/前/有json/2015-04-28~2015-06-28/法院公告2html/法院公告2html/e15326c8-084c-56f5-b53d-ca5d3646e97b.html");
		
//		File file = new File("D:/法院公告/前/2015-04-28号之前/法院公告1（html）/downloadreprocess");
		File file = new File("D:/法院公告/前/2015-04-28~2015-06-28/法院公告2html/法院公告2html");
		
		
//		File file = new File("D:/法院公告/前/有json");
		
//		File file = new File("D:/法院公告/前/有json/2015-06-27~2015-07-21/fayuangonggao/fayuangonggao/html");
//		File file = new File("D:/法院公告/前/有json/2015-07-21-2015-09-17/7.21-9.17/html");
//		File file = new File("D:/法院公告/前/有json/2015-09-18~2015-10-12/html/html");  
//		File file = new File("D:/法院公告/前/有json/2015-10-13~2015-11-12/html/html");
//		File file = new File("D:/法院公告/前/有json/2015-11-13~2015-12-09/html/html");
//		File file = new File("D:/法院公告/前/有json/2015-12-10~2015-12-28/html/html");
//		File file = new File("D:/法院公告/前/有json/2015-12-29/html/html");
//		File file = new File("D:/法院公告/前/有json/2015-12-30~2016-01-06/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-01-07/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-01-08~2016-01-09/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-01-10/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-01-11/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-01-12~2016-01-18/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-01-19~2016-01-30/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-01-31~2016-02-16/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-02-17~2016-03-01/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-03-02~2016-03-06/html/html");
//		File file = new File("D:/法院公告/前/有json/2016-03-07/html");
//		File file = new File("D:/法院公告/前/有json/2016-03-08~2016-03-16/html");
//		File file = new File("D:/法院公告/前/有json/2016-03-17~2016-03-22/html");
//		File file = new File("D:/法院公告/前/有json/2016-03-23~2016-04-05/html");
		
		
		util = new AdministrationUtils();
		util.initData(); // 查询行政区

		Bucket bucket = null;
		while (true) {
			try {
				// 更新文档
				bucket = CouchbaseConnect.commonBucket("192.168.1.114:8091", "courtPub");
				break;
			} catch (Exception e) {
				logger.info("---------------------------> 插入BC超时");
				logger.error(e.getMessage());
			}
		}
		
		// bucket = connectionBucket(bucket);
		try {
			show(file, bucket);
 

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			file = null;
			// bucket.close();
			cluster2 = null;
		}
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		log.info("开始时间--------------------" + formatter.format(startTime));

		log.info("结束时间--------------------" + formatter.format(endDate));

		log.info("JSON没有的案由数:  " + caseNum + "html解析出的: " + caseNum_Y1 + "html没解析出的: " + caseNum_N1);
		log.info("JSON有的案由数匹配数据的:  " + caseNum_Y + "JSON有的案由数没匹配数据的: " + caseNum_N);

		log.info("Time : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
		log.info("Time : " + (float) ((float) ((endTime - startTime) / 1000) / 60) + "分钟");
		log.info("Time : " + (float) ((endTime - startTime) / 1000) + "秒");
		log.info("Total : " + countNum);
		log.info("Speed : " + (float) (countNum / (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
				+ "个/小时");
		log.info("Speed : " + (float) (countNum / (float) ((float) ((endTime - startTime) / 1000) / 60)) + "个/分种");
		log.info("Speed : " + (float) (countNum / (float) ((endTime - startTime) / 1000)) + "个/秒");

		log.info("找不到Html的数量  : " + countNotHtml);

		record();
		System.exit(0);
	}

	static {
		String filePathUn = "D:/lm/log/没省的法院名.txt";
		File fileSUn = new File(filePathUn);
		String encoding_from1U = "UTF-8";

		try {
			if (!fileSUn.exists()) {
				try {
					fileSUn.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
				try {
					fwUn2 = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				// fileSUn.delete();
				// fileSUn = new File(filePathUn);
				// try {
				// fwUn2 = new BufferedWriter(
				// new OutputStreamWriter(new FileOutputStream(fileSUn, true),
				// encoding_from1U));
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	// 连接CB
	private static Bucket connectionBucket(Bucket bucket) {
		try {
			bucket = connectionCouchBaseLocal();// 本地CB
		} catch (Exception e) {
			while (true) {
				try {
					bucket = connectionCouchBaseLocal();// 本地CB
					break;
				} catch (Exception ee) {
					logger.error(ee);
				}
			}
		}

		return bucket;
	}

	/**
	 * 递归遍历文件
	 * 
	 * @param file
	 * @throws @throws
	 *             Exception
	 */
	private static void show(File file, Bucket bucket) throws Exception {
		if (file.isFile()) {
			if ( file.getPath().indexOf("html") != -1  )
					   {
				long da = System.currentTimeMillis();
				create(file, bucket);
				System.out.println("读取<<" + file.getPath() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
				return;
			}

		}
		File[] files = file.listFiles();
		if(files.length>0){
			System.out.println("----files  第一个：---" + files[0].getPath());
		}
		
		
		for (File fi : files) {
			if (fi.isFile()) {
				if (fi.getName().contains("download_fail")) {
					continue;
				}
				if (fi.getPath().indexOf("html") != -1 ) {
					long da = System.currentTimeMillis();
//					String name = fi.getParentFile().getPath();
//					name = name.substring(name.lastIndexOf("\\") + 1, name.length());
					create(fi, bucket);
					System.out.println( 
							"读取" + fi.getPath() + "<<" + fi.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
				}else{
					continue;
				}
			} else if (fi.isDirectory()) {
				// 只认带url的
				show(fi, bucket);

			} else {
				continue;
			}
		}
	}

	static int countNum = 0;
	static int countNotHtml = 0;

	/**
	 * 生成文件的个数
	 */
	static int countP = 0;
	static BufferedWriter fwUn = null;
	static BufferedWriter fwUn2 = null;

	/**
	 * 写数据
	 * 
	 * @param <JSONObject>
	 * @param file
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	private static <ObjectDataVO, JSONObject> void create(File file, Bucket bucket)
			throws Exception, UnsupportedEncodingException {

		String name = file.getParentFile().getPath();
		name = name.substring(name.lastIndexOf("\\") + 1, name.length());
		BufferedReader reader = null;
		Gson gson = new Gson();
		List<WholeCourtVO> list = new ArrayList<WholeCourtVO>();
		String temp = null;
		int sum = 0;
		String fileHtmlpath = file.getPath();
		String filePath = file.getPath();

		WholeCourtVO archJson = null;
		WholeCourtVO archHtml = null;
		WholeCourtVO finalVo = null;
		JsonDocument queryDoc = null;
		
		WholeCourtVO tempVo = null;
		
		File htmFile = null;
		try {
			reader = new BufferedReader(new FileReader(file));
//			while ((temp = reader.readLine()) != null) {

				if (countNum % 10000 == 0) {
					countP++;
					String folderPathUn = "F:\\法院公告(清洗)\\20160818法院公告(清洗)_前";
					String filePathUn = "F:\\法院公告(清洗)\\20160818法院公告(清洗)_前\\" + (countP)+"_"+DateUtils.getDateyyyyMMddhhmmss()  + "人民法院公告网_前.json";
					
					// 创建文件夹
					FileUtils.newFolder(folderPathUn);
					File fileSUn = new File(filePathUn);
					String encoding_from1U = "UTF-8";

					try {
						if (!fileSUn.exists()) {
							try {
								fileSUn.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
								log.error(e);
							}
							fwUn = new BufferedWriter(
									new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
						} else {
							fileSUn.delete();
							fileSUn = new File(filePathUn);
							fwUn = new BufferedWriter(
									new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}

				try {
					String key=file.getPath().substring(file.getPath().lastIndexOf("\\")+1, file.getPath().lastIndexOf("."));
					queryDoc = bucket.get(key, 60, TimeUnit.MINUTES);
					if (null != queryDoc) {
						archJson =StringUtils.GSON.fromJson(queryDoc.content().toString(), WholeCourtVO.class);
						archJson.setWholeCourtId(key);
					}
					
					archHtml = ExtractionHtmlCourtpub.getVoFromHtml(file);
					if(null!=archHtml){
						archHtml.setWholeCourtId(key);
						archHtml.setFilePathHtml(file.getPath());
					}
				
					
				} catch (Exception e) {
					countNotHtml++;
					countNum++;
					System.out.println(countNum);
					log.error(" archHtml = ExtractionHtml.getVoFromHtml(htmFile);:  " + e.getMessage());
					e.printStackTrace();
					// 合并vo
					finalVo = heBinVo(archJson, archHtml, 90);
					finalVo.setFilePathHtml(fileHtmlpath);

					writerString(fwUn, StringUtils.GSON.toJson(finalVo));
				}

				// 合并vo
				finalVo = heBinVo(archJson, archHtml, null);
				finalVo.setFilePathHtml(fileHtmlpath);
				countNum++;

				System.out.println(countNum);
				writerString(fwUn, StringUtils.GSON.toJson(finalVo));

				// 生成的写到txt
				// list.add(archJson);
//			}
			// list = removeDuplicate(list); // 去除本次集合中重复数据
			// sum = list.size();
			// boolean result = createJsonToCB(list, bucket);
			// REPEATSUM += list.size();
			// if (!result) {
			// logger.error("读取" + name + "<<" + file.getName() +
			// ">>文件时发生JSON异常!");
			// }
			// temp = null;
			// list = null;
			// list = new ArrayList<WholeCourtVO>();
			// statisticalCount(file, sum);
		} catch (Exception e) {
			// logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生IO异常:"
			// + e.getMessage());
		} finally {
			// logger.info(name + "<<" + file.getName() + ">>记录条数为：" + sum);
			// reader.close();
			// list = null;
			// file = null;
			// reader.close();
		}
	}

	static int caseNum = 0;
	static int caseNum_Y = 0;
	static int caseNum_N = 0;
	static int caseNum_Y1 = 0;
	static int caseNum_N1 = 0;

	/**
	 * 将html的vo合并到json的vo
	 * 
	 * @param archJson
	 * @param archHtml
	 * @return
	 */
	public static WholeCourtVO heBinVo(WholeCourtVO archJson1, WholeCourtVO archHtml, Integer dataFrom) {
		String[] array = null;
		WholeCourtVO archJson=new WholeCourtVO();
		
		if(null!=archJson1){
			archJson=archJson1;
			archJson.setWholeCourtId(archJson1.getWholeCourtId());
		}
//		if (null != archJson) {

			if(StringUtils.isNull(archJson.getProvince())){
				// 省市县
				if (!StringUtils.isNull(archJson.getCourtName())) {
					array = util.utils(archJson.getCourtName());
					// 记录没有省市县的法院名
					if (array[0] == null) {
						writerString(fwUn2, archJson.getCourtName());
					}
				}
			}
			
			

			if (!StringUtils.isNull(archJson.getPubDate())) {
				archJson.setPubDate(DateUtils.toYMDOfChaStr_ESZZ2(archJson.getPubDate()));
			}
			if (!StringUtils.isNull(archJson.getCollectTime())) {
				archJson.setCollectTime(DateUtils.toYMDOfChaStr_ESZZ2(archJson.getCollectTime()));
			}

			 
			
			

			if (null != archHtml) {
				
				if (StringUtils.isNull(archJson.getWholeCourtId() )) {
					archJson.setWholeCourtId(archHtml.getWholeCourtId());
				}
				
				if (StringUtils.isNull(archJson.getTitle())) {
					archJson.setTitle(archHtml.getTitle());
				}
				
				if (StringUtils.isNull(archJson.getDetailLink())) {
					archJson.setDetailLink(archHtml.getDetailLink());
				}
				if (StringUtils.isNull(archJson.getPdfLink())) {
					archJson.setPdfLink(archHtml.getPdfLink());
				}
				
			 
				 
				if (StringUtils.isNull(archJson.getCourtName())&&StringUtils.isNull(archJson.getProvince()) ) {
					
					
					archJson.setCourtName(archHtml.getCourtName());
					//[北京]北京市丰台区人民法院
					
					if(!StringUtils.isNull(archJson.getCourtName())&&  archJson.getCourtName().indexOf("[")!=-1 && archJson.getCourtName().indexOf("]")!=-1){
						String temp=archJson.getCourtName().substring(archJson.getCourtName().indexOf("]")+1 );
						
						array = util.utils(temp);
						// 记录没有省市县的法院名
						if (array[0] == null) {
							writerString(fwUn2, archHtml.getCourtName());
						}
					}else{
						array = util.utils(archHtml.getCourtName());
						// 记录没有省市县的法院名
						if (array[0] == null) {
							writerString(fwUn2, archHtml.getCourtName());
						}
					}
					
					if(!StringUtils.isNull(archJson.getCourtName())&&  archJson.getCourtName().indexOf("[")!=-1 && archJson.getCourtName().indexOf("]")!=-1){
						String temp=archJson.getCourtName().substring(archJson.getCourtName().indexOf("]")+1 );
						archJson.setCourtName(temp);
						archJson.setPersonName(temp);
					}
					
				}
				
				if (StringUtils.isNull(archJson.getPubDate())) {
					archJson.setPubDate(
							DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(archHtml.getPubDate())));
				}
				 
				
				if (StringUtils.isNull(archJson.getPubType())) {// 公告类型
					archJson.setPubType(archHtml.getPubType());
				}
				if(!StringUtils.isNull(archHtml.getClient())){//当事人
					archJson.setClient(archHtml.getClient());
				}
				
				
				if (StringUtils.isNull(archJson.getPubContent())) {//  公告内容
					archJson.setPubContent(archHtml.getPubContent());
				}
				
				 

				if (StringUtils.isNull(archJson.getSummary())) {// 摘要
					// archJson.setSummary(archHtml.getSummary());

					String summary = "";

					// if(!StringUtils.isNull(archJson.getPlaintiff())){
					// summary+="原告："+archJson.getPlaintiff()+" ";
					// }
					// if(!StringUtils.isNull(archJson.getDefendant())){
					// summary+="被告："+archJson.getDefendant()+" ";
					// }

					if (!StringUtils.isNull(archJson.getClient())) {
						summary += "当事人：" + archJson.getClient() + " ";
					}

					if (!StringUtils.isNull(archJson.getCourtName())) {
						summary += "法院：" + archJson.getCourtName() + " ";
					}
					
					if (!StringUtils.isNull(archJson.getPubDate())) {
						if (!StringUtils.isNull(DateUtils.toYMDFromZZ(archJson.getPubDate()))) {
							summary += "发布日期：" + DateUtils.strZZToYMD(archJson.getPubDate()) + " ";
						}
					}
					
					if (!StringUtils.isNull(archJson.getPubContent())) {
						summary += " " + archJson.getPubContent() + " ";
					}
					
					if (!StringUtils.isNull(summary)) {
						if (summary.length() > 151)
							summary = summary.substring(0, 147) + "...";
					}
					archJson.setSummary(summary);
				}
				archJson.setPath(archHtml.getPath());
			}

			// 省市县
			if (null != array) {
				if (null != array[0] && !"".equals(array[0])) {
					archJson.setProvince(array[0]);
				}
				if (null != array[1] && !"".equals(array[1])) {
					archJson.setCity(array[1]);
				}
				if (null != array[2] && !"".equals(array[2])) {
					archJson.setArea(array[2]);
				}
			}

			archJson.setSource(4);
			archJson.setFlag(1);
			// 数据来源 1 旧版高院 2 旧版地方 3 新版 4 旧版高院按法院查    6中国裁判文书网展示文书详细页面（旧版高院）
//			archJson.setDataFrom(dataFrom);
//		}
		return archJson;
	}

	/**
	 * 得到案由
	 * 
	 * @Description: TODO
	 * @param text
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月8日 上午10:24:07
	 */
//	public static String getCaseFromStr(String text) {
//
//		if (StringUtils.isNull(text)) {
//			return null;
//		}
//		if (text.length() > 200) {
//			System.out.println(text.substring(0, 199));
//		}
//		for (String val : CourtData.LISTCasecause_small_small) {
//			if (text.indexOf(val) != -1) {
//				return val;
//			}
//		}
//		for (String val : CourtData.LISTCasecause_small) {
//			if (text.indexOf(val) != -1) {
//				return val;
//			}
//		}
//		for (String val : CourtData.LISTCasecause_middle) {
//			if (text.indexOf(val) != -1) {
//				return val;
//			}
//		}
//		for (String val : CourtData.LISTCasecause_big) {
//			if (text.indexOf(val) != -1) {
//				return val;
//			}
//		}
//
//		String result = null;
//
//		return result;
//	}

	/**
	 * 裁判文书json文件导入CB
	 * 
	 * @param list
	 * @param bucket
	 * @return
	 * @throws Exception
	 */
	// public static boolean createJsonToCB(List<WholeCourtVO> list, Bucket
	// bucket) throws Exception {
	// Gson gson = new Gson();
	// JsonDocument doc = null;
	// WholeCourtVO archs = null;
	// List<RelativeVO> relativeList = null;
	// List<RelativeVO> lists = null;
	// RelativeVO relative = null;
	// try {
	// for (int i = 0; i < list.size(); i++) {
	// count++;
	// archs = new WholeCourtVO();
	// relativeList = new ArrayList<RelativeVO>();
	// lists = new ArrayList<RelativeVO>();
	// String uuid = list.get(i).getWholeCourtId();
	// relative = new RelativeVO();
	// relativeList = list.get(i).getRelativeCases();
	// // ------------------------------以下为关联文书-----------------
	// for (int a = 0; a < relativeList.size(); a++) {
	// if (null != relativeList.get(a).getApprovalDate()
	// && !"".equals(relativeList.get(a).getApprovalDate())) {
	// relative.setApprovalDate(relativeList.get(a).getApprovalDate().toString());
	// }
	// if (null != relativeList.get(a).getCaseNum() &&
	// !"".equals(relativeList.get(a).getCaseNum())) {
	// relative.setCaseNum(relativeList.get(a).getCaseNum().toString());
	// }
	// if (null != relativeList.get(a).getClosedType()
	// && !"".equals(relativeList.get(a).getClosedType())) {
	// relative.setClosedType(relativeList.get(a).getClosedType().toString());
	// }
	// if (null != relativeList.get(a).getCourtName() &&
	// !"".equals(relativeList.get(a).getCourtName())) {
	// relative.setCourtName(relativeList.get(a).getCourtName().toString());
	// }
	// if (null != relativeList.get(a).getMark() &&
	// !"".equals(relativeList.get(a).getMark())) {
	// relative.setMark(relativeList.get(a).getMark().toString());
	// }
	// if (null != relativeList.get(a).getRelative_id()
	// && !"".equals(relativeList.get(a).getRelative_id())) {
	// relative.setRelative_id(relativeList.get(a).getRelative_id().toString());
	// }
	// if (null != relativeList.get(a).getSuitType() &&
	// !"".equals(relativeList.get(a).getSuitType())) {
	// relative.setSuitType(relativeList.get(a).getSuitType().toString());
	// }
	// if (null != relativeList.get(a).getType() &&
	// !"".equals(relativeList.get(a).getType())) {
	// relative.setType(relativeList.get(a).getType().toString());
	// }
	// lists.add(relative);
	// }
	// // ---------------------------------以上为关联文书----------------------------
	// if (lists.size() > 0) {
	// archs.setRelativeCases(lists);
	// }
	// if (null != list.get(i).getProvince() &&
	// !"".equals(list.get(i).getProvince())) {
	// archs.setProvince(list.get(i).getProvince().toString());
	// }
	// if (null != list.get(i).getApprovalDate() &&
	// !"".equals(list.get(i).getApprovalDate())) {
	// archs.setApprovalDate(list.get(i).getApprovalDate().toString());
	// }
	// if (null != list.get(i).getCollectDate() &&
	// !"".equals(list.get(i).getCollectDate())) {
	// archs.setCollectDate(list.get(i).getCollectDate().toString());
	// }
	// if (null != list.get(i).getCaseCause() &&
	// !"".equals(list.get(i).getCaseCause())) {
	// archs.setCaseCause(list.get(i).getCaseCause().toString());
	// }
	// if (null != list.get(i).getPublishDate() &&
	// !"".equals(list.get(i).getPublishDate())) {
	// archs.setPublishDate(list.get(i).getPublishDate().toString());
	// }
	// if (null != list.get(i).getCatalog() &&
	// !"".equals(list.get(i).getCatalog())) {
	// archs.setCatalog(list.get(i).getCatalog().toString());
	// }
	// if (null != list.get(i).getCaseNum() &&
	// !"".equals(list.get(i).getCaseNum())) {
	// archs.setCaseNum(list.get(i).getCaseNum().toString());
	// }
	// if (null != list.get(i).getCity() && !"".equals(list.get(i).getCity())) {
	// archs.setCity(list.get(i).getCity().toString());
	// }
	// if (null != list.get(i).getTitle() && !"".equals(list.get(i).getTitle()))
	// {
	// archs.setTitle(list.get(i).getTitle().toString());
	// }
	// if (null != list.get(i).getArea() && !"".equals(list.get(i).getArea())) {
	// archs.setArea(list.get(i).getArea().toString());
	// }
	// if (null != list.get(i).getDetailLink() &&
	// !"".equals(list.get(i).getDetailLink())) {
	// archs.setDetailLink(list.get(i).getDetailLink().toString());
	// }
	// if (null != list.get(i).getSummary() &&
	// !"".equals(list.get(i).getSummary())) {
	// archs.setSummary(list.get(i).getSummary().toString());
	// }
	// if (null != list.get(i).getCourtName() &&
	// !"".equals(list.get(i).getCourtName())) {
	// archs.setCourtName(list.get(i).getCourtName().toString());
	// }
	// if (null != list.get(i).getSuitType() &&
	// !"".equals(list.get(i).getSuitType())) {
	// archs.setSuitType(list.get(i).getSuitType().toString());
	// }
	//
	// String jsonss = gson.toJson(archs);
	//
	// doc = JsonDocument.create(uuid, JsonObject.fromJson(jsonss));
	// bucket.upsert(doc);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// } finally {
	// gson = null;
	// doc = null;
	// archs = null;
	// relativeList = null;
	// lists = null;
	// relative = null;
	// }
	// return true;
	// }

	/**
	 * 统计导入的各地的记录条数
	 */
	public static void statisticalCount(File file, long count) {
		// 取省名
		String provinceName = file.getParentFile().getParent();
		provinceName = provinceName.substring(provinceName.lastIndexOf("\\") + 1, provinceName.length());
		// 取市名
		String city = file.getParentFile().getPath();
		city = city.substring(city.lastIndexOf("\\") + 1, city.length());
		List<RecordData> list = MAPS.get(provinceName);
		if (null == list || list.size() <= 0) {
			list = new ArrayList<RecordData>();
			list.add(new RecordData(provinceName, city, count));
			MAPS.put(provinceName, list);
		} else {
			boolean result = true;
			for (RecordData re : list) {
				if (re.getCityName().equalsIgnoreCase(city)) {
					re.setNumberData(re.getNumberData() + count);
					result = false;
					break;
				}
			}
			if (result) {
				list.add(new RecordData(provinceName, city, count));
				MAPS.put(provinceName, list);
			}
		}
	}

	/**
	 * 记录各地数据
	 */
	public static void record() {
		long sumCount = 0;
		long sum = 0;
		for (Map.Entry<String, List<RecordData>> map : MAPS.entrySet()) {
			logger.info("###:" + map.getKey());
			List<RecordData> list = map.getValue();
			sum = 0;
			for (RecordData recordData : list) {
				logger.info("###:" + recordData.getCityName() + "----记录条数:" + recordData.getNumberData());
				sum += recordData.getNumberData();
			}
			sumCount += sum;
			logger.info(map.getKey() + "省总数据条数据:" + sum);
			logger.info("------------------------------");
		}
		logger.info("总文件数据条数:" + sumCount);
		logger.info("去重后的数据条数据:" + REPEATSUM);
		logger.info("错误数据条数:" + ERRORSUM);
	}

	/**
	 * 根据UUID去重
	 * 
	 * @param list
	 * @return
	 */
	public static List<WholeCourtVO> removeDuplicate(List<WholeCourtVO> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getWholeCourtId().equals(list.get(i).getWholeCourtId())) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	/**
	 * 连接postgreSql库
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
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
	 * 
	 * @return
	 */
	public static Bucket connectionCouchBaseLocal() {
		// 连接指定的桶
		return cluster2.openBucket("court_New", 1, TimeUnit.MINUTES);
	}

	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + System.getProperty("line.separator"));

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

	public static void writerString2(String str) {
		try {
			BufferedWriter fwUn = null;
			if (countNum == 0 || countNum == 1 || countNum % 10000 == 0) {
				countP++;
				String folderPathUn = "D:/lm/log/2016法院清洗后数据3";
				String filePathUn = "D:\\lm\\log\\2016法院清洗后数据3\\" + (countP) + ".json";
				// 创建文件夹
				FileUtils.newFolder(folderPathUn);
				File fileSUn = new File(filePathUn);
				String encoding_from1U = "UTF-8";

				try {
					if (!fileSUn.exists()) {
						try {
							fileSUn.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
							log.error(e);
						}
						fwUn = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
					} else {
						fileSUn.delete();
						fileSUn = new File(filePathUn);
						fwUn = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}

			fwUn.append(str + System.getProperty("line.separator"));
			// fw.newLine();
			fwUn.flush(); // 全部写入缓存中的内容
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

	private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
}
