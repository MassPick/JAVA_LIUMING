package cn.com.szgao.clean.court;

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

/////////\//import cn.com.szgao.court.esAndcb.CommonConstant;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;
//import cn.com.szgao.clean.court.ExtractthepeopleText;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.wash.data.AdministrationUtils;

/**
 * 对应文件夹生成保存的文件
 * @author liuming
 * @Date 2016年6月15日 下午4:55:32
 */
public class ExtractionJsonAndHtmlToTxtOldSaveDir {
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(ExtractionJsonAndHtmlToTxtOldSaveDir.class.getName());
	private static Logger log = LogManager.getLogger(ExtractionJsonAndHtmlToTxtOldSaveDir.class.getName());
	static Map<String, List<RecordData>> MAPS = new HashMap<String, List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //
	static long REPEATSUM = 0; // 去重后数据条数
	static long SUM = 0;
	static long count = 0;

	static AdministrationUtils util;
	
	static BufferedWriter fwUn4 = null;
	static {
		String filePathUn = "D:/lm/log/测试22.txt";
		File fileSUn = new File(filePathUn);
		String encoding_from1U = "UTF-8";

		try {
			if (!fileSUn.exists()) {
				try {
					fileSUn.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fwUn4 = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				 fileSUn.delete();
				 fileSUn = new File(filePathUn);
				 try {
					 fwUn4 = new BufferedWriter(
				 new OutputStreamWriter(new FileOutputStream(fileSUn, true),
				 encoding_from1U));
				 } catch (UnsupportedEncodingException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
				 } // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	

	/**
	 * 处理旧版本的文书
	 * 
	 * @Description: TODO
	 * @param args
	 * @throws Exception
	 * @return void
	 * @author liuming
	 * @date 2016年6月14日 上午9:48:46
	 */
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 导入文件地址
		// File file=new
		// File("G:\\Data\\2016新数据\\JSON\\JSON20131231-20130601\\2013-11-04");
		// File file=new File("C:\\data\\文书新版本\\JSON\\JSON20131231-20130601");

		// File file = new File(
		// "F:\\DataSheet\\Data\\新版裁判文书JSON+HTML\\2016年\\JSON");

		// File file = new File(
		// "F:\\DataSheet\\Data\\新版裁判文书JSON+HTML\\2016年\\JSON\\JSON20160430-20160415\\2016-04-30\\民事案件-2016-04-30.json");

		// File file = new File(
		// "F:\\DataSheet\\Data\\新版裁判文书JSON+HTML\\2016年\\JSON\\JSON20160430-20160415\\2016-04-30");

		// File file=new File("C:\\data\\新版裁判文书JSON+HTML\\2016年\\JSON");
		// File file=new
		// File("C:\\data\\新版裁判文书JSON+HTML\\2016年\\JSON\\JSON20160115-20160101\\2016-01-04\\执行案件-2016-01-04.json");

//		File file = new File(
//				"E:/法院详细页面/中国裁判文书网展示文书详细页面（旧版高院）/zgcpwsw201503/cpwsw/ms/0000da9f-93c3-565d-bc31-c1e61e7c2ffb.html");
		
//		File file = new File(
//				"E:/法院详细页面/中国裁判文书网展示文书详细页面（旧版高院）/zgcpwsw201503/cpwsw/ms");
//		File file = new File(
//				"E:/法院详细页面/中国裁判文书网展示文书详细页面（旧版高院）/zgcpwsw201503/cpwsw/xz");
		
		File file = new File(
				"E:\\法院详细页面\\中国裁判文书网展示文书详细页面（旧版高院）\\zgcpwsw-20151127\\zgcpwsw20151127(重庆、广西)\\广西");
		
		
		
//		File file = new File(
//				"E:/法院详细页面/中国裁判文书网展示文书详细页面（旧版高院）/zgcpwsw201503/cpwsw/xs/00009726-5527-5bc1-8a02-5497e4ef2dff.html");
		
		
//		File file=new File("C:\\Users\\liuming\\Desktop\\00b3fda1-0425-5caa-a44a-95a09734ac05.html");

		util = new AdministrationUtils();
		util.initData(); // 查询行政区

		Bucket bucket = null;
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
			
			
			System.out.println(file.getPath());
			System.out.println(file.getParent());
			 
			//创建文件夹
			FileUtils.newFolderMultistep(file.getParent().replace("法院详细页面", "法院详细页面(清洗)"));
			 

			String temp = file.getName().substring(file.getName().lastIndexOf(".")+1).toLowerCase();
			if ( !(temp.equals("html") ||  temp.equals("htm"))  ) {
				log.error("------>不是HMTL文件： " + file.getPath());
				return;
			}

			long da = System.currentTimeMillis();
			create(file, bucket);
			logger.info("读取<<" + file.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
			return;
		}
		File[] files = file.listFiles();
		System.out.println("----files---" + files);
		for (File fi : files) {
			if (fi.isFile()) {
				if (fi.getName().contains("download_fail")) {
					continue;
				}

				String temp = fi.getName().substring(fi.getName().lastIndexOf(".")+1).toLowerCase();
				if ( !(temp.equals("html") ||  temp.equals("htm"))  ) {
					log.error("------>不是HMTL文件： " + fi.getPath());
					continue;
				}
				long da = System.currentTimeMillis();
				String name = fi.getParentFile().getPath();
				name = name.substring(name.lastIndexOf("\\") + 1, name.length());
				create(fi, bucket);
				logger.info("读取" + name + "<<" + fi.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
			} else if (fi.isDirectory()) {
				
				System.out.println(fi.getPath());
				//创建文件夹
				FileUtils.newFolderMultistep(file.getParent().replace("法院详细页面", "法院详细页面(清洗)"));
				
				
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

		File htmFile = null;
		try {

			if (countNum % 10000 == 0) {
				countP++;
				String folderPathUn = "D:/lm/log/2016法院清洗后数据7";
				String filePathUn =file.getParent()+"\\"+   (countP)+"_"+DateUtils.getDateyyyyMMddhhmmss() + ".json";
				filePathUn=filePathUn.replace("法院详细页面", "法院详细页面(清洗)");
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
				archHtml = ExtractionHtmlOld.getVoFromHtml(file);

			} catch (Exception e) {
				countNotHtml++;
				countNum++;
				System.out.println(countNum);
				log.error("异常 archHtml = ExtractionHtml.getVoFromHtml   :  " + e.getMessage());
				// 合并vo
				// finalVo = heBinVo(archJson, archHtml,1);
				// writerString(fwUn, StringUtils.GSON.toJson(finalVo));
			}

			if(archHtml==null){
				return;
			}
			// 合并vo
			finalVo = heBinVo(archHtml, 1);
			finalVo.setPath(filePath);

			countNum++;

			System.out.println(countNum);
			writerString(fwUn, StringUtils.GSON.toJson(finalVo));

			writerString(fwUn4, finalVo.getCourtName()+"|"+ finalVo.getClients() +"|"+ finalVo.getJudges() +"|"+finalVo.getCaseCause()+"|"+finalVo.getCaseNum()+"|"+finalVo.getPublishDate()+"|"+finalVo.getApprovalDate()+"|"+finalVo.getTitle() +"|"+finalVo.getCatalog() +"|"+finalVo.getWritType() +"|"+file.getPath() );
			
			// 生成的写到txt
			// list.add(archJson);

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
	public static WholeCourtVO heBinVo(WholeCourtVO archHtml, Integer dataFrom) {

		WholeCourtVO archJson = new WholeCourtVO();
		String[] array = null;

		// if (null != archJson) {

		// 省市县
		// if (!StringUtils.isNull(archJson.getCourtName())) {
		// array = util.utils(archJson.getCourtName());
		// //记录没有省市县的法院名
		// if(array[0]==null){
		// writerString(fwUn2, archJson.getCourtName() );
		// }
		// }

		// if (!StringUtils.isNull(archJson.getPublishDate())) {
		// archJson.setPublishDate(DateUtils.toYMDOfChaStr_ESZZ2(archJson.getPublishDate()));
		// }
		// if (!StringUtils.isNull(archJson.getCollectDate())) {
		// archJson.setCollectDate(DateUtils.toYMDOfChaStr_ESZZ2(archJson.getCollectDate()));
		// }
		//
		// if (!StringUtils.isNull(archJson.getSuitDate())) {// 起诉日期
		// archJson.setSuitDate(DateUtils.toYMDOfChaStr_ESZZ2(archJson.getSuitDate()));
		// }
		// if (!StringUtils.isNull(archJson.getApprovalDate())) {// 审结日期
		// archJson.setApprovalDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(archJson.getApprovalDate())));
		// }

		if (null != archHtml) {
			if (StringUtils.isNull(archJson.getTitle())) {
				archJson.setTitle(archHtml.getTitle());
			}
			if (StringUtils.isNull(archJson.getDetailLink())) {
				archJson.setDetailLink(archHtml.getDetailLink());
			}
			if (StringUtils.isNull(archJson.getCatalog())) {
				archJson.setCatalog(archHtml.getCatalog());
			}
			if (StringUtils.isNull(archJson.getCaseNum())) {
				archJson.setCaseNum(archHtml.getCaseNum());
			}
			if (StringUtils.isNull(archJson.getCourtName())) {
				archJson.setCourtName(archHtml.getCourtName());
				array = util.utils(archHtml.getCourtName());
				// 记录没有省市县的法院名
				if (array[0] == null) {
					writerString(fwUn2, archHtml.getCourtName());
				}
			}
			if (StringUtils.isNull(archJson.getPublishDate())) {
				archJson.setPublishDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(archHtml.getPublishDate())));
			}
			if (StringUtils.isNull(archJson.getPlaintiff())) {// 原告
				archJson.setPlaintiff(archHtml.getPlaintiff());
			}
			if (StringUtils.isNull(archJson.getDefendant())) {// 被告
				archJson.setDefendant(archHtml.getDefendant());
			}
			if (StringUtils.isNull(archJson.getApproval())) {// 审判结果
				archJson.setApproval(archHtml.getApproval());
			}
			if (StringUtils.isNull(archJson.getSuitType())) {// 起诉类型
				archJson.setSuitType(archHtml.getSuitType());
			}
			if (StringUtils.isNull(archJson.getSuitDate())) {// 起诉日期
				archJson.setSuitDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(archHtml.getSuitDate())));
			}
			if (StringUtils.isNull(archJson.getApprovalDate())) {// 审结日期
				archJson.setApprovalDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(archHtml.getApprovalDate())));
			}
			if (StringUtils.isNull(archJson.getCaseCause())) {// 案由
				archJson.setCaseCause(archHtml.getCaseCause());
				caseNum++;
				if (!StringUtils.isNull(archHtml.getCaseCause())) {
					caseNum_Y1++;
				} else {
					caseNum_N1++;
				}
			}

			else {
				String ss = getCaseFromStr(archJson.getCaseCause());
				if (!StringUtils.isNull(ss)) {
					caseNum_Y++;
				} else {
					caseNum_N++;
				}
			}

			if (StringUtils.isNull(archJson.getClients())) {// 当事人词组
				archJson.setClients(archHtml.getClients());
			}

			if (StringUtils.isNull(archJson.getJudges())) {// 审判员词组
				archJson.setJudges(archHtml.getJudges());
			}

			// 数据来源 1 旧版高院 2 旧版地方 3 新版
			archJson.setDataFrom(dataFrom);

			if (StringUtils.isNull(archJson.getSummary())) {// 摘要
				// archJson.setSummary(archHtml.getSummary());

				String summary = "";

//				if (!StringUtils.isNull(archJson.getPlaintiff())) {
//					summary += "原告：" + archJson.getPlaintiff() + " ";
//				}
//				if (!StringUtils.isNull(archJson.getDefendant())) {
//					summary += "被告：" + archJson.getDefendant() + " ";
//				}
				
				if (!StringUtils.isNull(archJson.getClients())) {
					summary += "当事人：" + archJson.getClients()+ " ";
				}
				
				if (!StringUtils.isNull(archJson.getCourtName())) {
					summary += "法院：" + archJson.getCourtName() + " ";
				}
				if (!StringUtils.isNull(archJson.getCaseNum())) {
					summary += "案号：" + archJson.getCaseNum() + " ";
				}
				if (!StringUtils.isNull(archJson.getCatalog())) {
					summary += "案件类型：" + archJson.getCatalog() + " ";
				}
				if (!StringUtils.isNull(archJson.getPublishDate())) {
					if (!StringUtils.isNull(DateUtils.toYMDFromZZ(archJson.getPublishDate()))) {
						summary += "发布日期：" + DateUtils.strZZToYMD(archJson.getPublishDate()) + " ";
					}
				}
				if (!StringUtils.isNull(archJson.getCaseCause())) {
					summary += "案由：" + archJson.getCaseCause();
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
		//处理日期 1900——————当前日期
		archJson.setApprovalDate(DateUtils.getOkDate(archJson.getApprovalDate()));
		if(StringUtils.isNull(archJson.getApprovalDate())){
			archJson.setApprovalDateY(null);
			archJson.setApprovalDateYM(null);
		}
		//案件类型
		if(!StringUtils.isNull(archJson.getCatalog())){
			if(archJson.getCatalog().indexOf("民事")!=-1  ){
				archJson.setCatalog("民事案件");
			}else if (archJson.getCatalog().indexOf("刑事")!=-1){
				archJson.setCatalog("刑事案件");
			}else if (archJson.getCatalog().indexOf("执行")!=-1){
				archJson.setCatalog("执行案件");
			}else if (archJson.getCatalog().indexOf("执行")!=-1){
				archJson.setCatalog("执行案件");
			}else if (archJson.getCatalog().indexOf("行政")!=-1){
				archJson.setCatalog("行政案件");
			}else if (archJson.getCatalog().indexOf("赔偿")!=-1){
				archJson.setCatalog("赔偿案件");
			}
			else if (archJson.getCatalog().indexOf("知识产权")!=-1){
				archJson.setCatalog("知识产权案件");
			}else if (archJson.getCatalog().indexOf("海事")!=-1  ){
				archJson.setCatalog("海事案件"); 
			}else if (archJson.getCatalog().indexOf("商事")!=-1){
				archJson.setCatalog("商事案件"); 
			}else {
				archJson.setCatalog("其他案件");
			}
		}
		archJson.setSource(3);
		archJson.setFlag(1);

		// }
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
	public static String getCaseFromStr(String text) {

		if (StringUtils.isNull(text)) {
			return null;
		}
		if (text.length() > 200) {
			System.out.println(text.substring(0, 199));
		}
		for (String val : CourtData.LISTCasecause_small_small) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}
		for (String val : CourtData.LISTCasecause_small) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}
		for (String val : CourtData.LISTCasecause_middle) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}
		for (String val : CourtData.LISTCasecause_big) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}

		String result = null;

		return result;
	}

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
