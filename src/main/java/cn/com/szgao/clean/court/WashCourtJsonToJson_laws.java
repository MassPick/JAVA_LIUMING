package cn.com.szgao.clean.court;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 清洗条文
 * @author liuming
 * @Date 2016年11月7日 下午5:30:24
 */
@SuppressWarnings("unused")
public class WashCourtJsonToJson_laws {
	public WashCourtJsonToJson_laws() {
	}

	// public FileIntoDataBase2p5(Logger log) {
	// this.log = log;
	// }

	private static Logger log = LogManager.getLogger(WashCourtJsonToJson_laws.class.getName());

	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}

	/**
	 * json与对象互转
	 */
	Gson gs = new Gson();

	// 日志对象
	// private static Logger log;
	// 根据字符串生成UUID对象
	NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	// // 报告集合
	// Map<String, JSONArray> reportMap = new HashMap<String, JSONArray>();
	// // 公司ID集合
	// // List<String> companyIdList = new ArrayList<String>();
	// // 股权出质登记信息
	// JSONArray recordArray = null;
	// // 股权出质登记信息详情
	// List<JSONArray> pledgeList = new ArrayList<JSONArray>();
	// // 行政处罚信息
	// JSONArray punishmentArray = null;
	// // 行政处罚信息详情
	// JSONArray punishDetArray = null;

	int count = 0;

	/**
	 * 循环调用是否是目录
	 * 
	 * @param file
	 * @throws ParseException
	 * @throws IOException
	 */
	public void show(File file, int startNum) throws IOException, ParseException {
		System.out.println(file.getPath());
		if (file.isFile()) {
			count += 1;
			log.info("---------------数量:" + count + "---线程名" + Thread.currentThread().getName() + "---"
					+ file.getPath());
			try {
				readFileByLines(file, startNum);
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
					count += 1;
					log.info("---------------------数量:" + count + "---线程名" + Thread.currentThread().getName() + "---"
							+ fi.getPath());
					try {
						readFileByLines(fi, startNum);
					} catch (Exception e) {
						e.printStackTrace();
						log.info(e);
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					show(fi, startNum);
				} else {
					continue;
				}
			}
		}

	}

	int SUMM=0;
	@SuppressWarnings("rawtypes")
	private void readFileByLines(File file, int startNum) throws Exception {

		// 无基本信息统计数据
		int basicSum = 0;
		// 无注册号统计数据
		int regSum = 0;

		// 文件名当批次号
		String batchNum = file.getName();
		// 拼接字符对象
		StringBuffer sb = new StringBuffer();
		// FileReader fr = new FileReader(file);
		// int ch = 0;
		// while((ch = fr.read())!=-1 )
		// {
		// sb.append((char)ch);
		// }
		// fr.close();
		// fr = null;

		// BufferedWriter fw = null;
		String encoding_from = "UTF-8";// GB18030
		// String encoding_to = "UTF-8";
		BufferedReader reader = null;
		try {
			// 华 GB18030

			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "UTF-8");
			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "GBK");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encoding_from);
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String tempT = null;

		int readNum = 0;

		// D:\工商数据已去重\160407工商\
//		String ss = file.getPath().substring(file.getPath().indexOf("data") + 4);
//		String ss2 = file.getPath().substring(file.getPath().indexOf("data") + 5, file.getPath().lastIndexOf("\\"));

//		String folderPath = "D:/工商数据已去重_2/" + ss2;
		String filePath = file.getPath().replace("法院详细页面(清洗)", "法院详细页面(清洗)_new") ;

		// 创建文件夹
		// FileUtils.newFolder(folderPath);

		File fileS = new File(filePath);
		FileUtils.mkFile(fileS);

		String encoding_from1 = "UTF-8";
		BufferedWriter fw = null;
		try {
			if (!fileS.exists()) {
				try {
					fileS.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
			} else {
				fileS.delete();
				fileS = new File(filePath);
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		String filePathUn =  file.getPath().replace("法院详细页面(清洗)", "法院详细页面(清洗)_无条文LOG") ;
		
		File fileSUn = new File(filePathUn);
		FileUtils.mkFile(fileSUn);
		
		String encoding_from1U = "UTF-8";
		BufferedWriter fwUn = null;
		try {
			if (!fileSUn.exists()) {
				try {
					fileSUn.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
				fwUn = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
			} else {
				fileSUn.delete();
				fileSUn = new File(filePathUn);
				fwUn = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		
		int M = 1;// 1M上限
		while ((tempT = reader.readLine()) != null) {

			// tempT="";

			readNum++;
			SUMM++;
			System.out.println(SUMM);
			
			// if(readNum<34431){
			// continue;
			// }
			// if(readNum==34431){
			//
			// writerString(fwUn, tempT);
			// break;
			// }

			if (readNum < startNum) {
				continue;
			}

			if (tempT == null || tempT == "") {
				continue;
			}
			JSONObject temJson = null;
			WholeCourtVO arch = new WholeCourtVO();
			try {
				temJson = JSONObject.fromObject(tempT);
			} catch (Exception e) {
				log.error("json异常:" + file.getPath() + "----" + tempT);
				continue;
			}
			try {
				arch = gs.fromJson(tempT, WholeCourtVO.class);
			} catch (Exception e) {
				log.error("json转vo异常:" + file.getPath() + "----" + tempT);
				continue;
			}

			// 处理条文
			if (null != arch.getLaws()) {
				try {
					String laws=CourtUtils.getLaws(arch.getLaws());
					
					if(!StringUtils.isNull(laws)){
						laws=laws.replace("第一十零条", "第十条")
								.replace("第一十条", "第十条")
								.replace("第一十一条", "第十一条")
								.replace("第一十二条", "第十二条")
								.replace("第一十三条", "第十三条")
								.replace("第一十四条", "第十四条")
								.replace("第一十五条", "第十五条")
								.replace("第一十六条", "第十六条")
								.replace("第一十七条", "第十七条")
								.replace("第一十八条", "第十八条")
								.replace("第一十九条", "第十九条")
								
								
								.replace("第一十款", "第十款")
								.replace("第一十一款", "第十一款")
								.replace("第一十二款", "第十二款")
								.replace("第一十三款", "第十三款")
								.replace("第一十四款", "第十四款")
								.replace("第一十五款", "第十五款")
								.replace("第一十六款", "第十六款")
								.replace("第一十七款", "第十七款")
								.replace("第一十八款", "第十八款")
								.replace("第一十九款", "第十九款")
								
								
								.replace("第一十项", "第十项")
								.replace("第一十一项", "第十一项")
								.replace("第一十二项", "第十二项")
								.replace("第一十三项", "第十三项")
								.replace("第一十四项", "第十四项")
								.replace("第一十五项", "第十五项")
								.replace("第一十六项", "第十六项")
								.replace("第一十七项", "第十七项")
								.replace("第一十八项", "第十八项")
								.replace("第一十九项", "第十九项")
								
								
								.replace("第（一十）项", "第（十）项")
								.replace("第（一十一）项", "第（十一）项")
								.replace("第（一十二）项", "第（十二）项")
								.replace("第（一十三）项", "第（十三）项")
								.replace("第（一十四）项", "第（十四）项")
								.replace("第（一十五）项", "第（十五）项")
								.replace("第（一十六）项", "第（十六）项")
								.replace("第（一十七）项", "第（十七）项")
								.replace("第（一十八）项", "第（十八）项")
								.replace("第（一十九）项", "第（十九）项")
								
								
								.replace("零条", "条")
								.replace("零款", "款")
								.replace("零项", "项")
								.replace("零）项", "零）项")
								
								
								;
					}
					arch.setLaws(laws);

					//案件类型
					if(!StringUtils.isNull(arch.getCatalog())){
						if(arch.getCatalog().indexOf("民事")!=-1  ){
							arch.setCatalog("民事案件");
						}else if (arch.getCatalog().indexOf("刑事")!=-1){
							arch.setCatalog("刑事案件");
						}else if (arch.getCatalog().indexOf("执行")!=-1){
							arch.setCatalog("执行案件");
						}else if (arch.getCatalog().indexOf("执行")!=-1){
							arch.setCatalog("执行案件");
						}else if (arch.getCatalog().indexOf("行政")!=-1){
							arch.setCatalog("行政案件");
						}else if (arch.getCatalog().indexOf("赔偿")!=-1){
							arch.setCatalog("赔偿案件");
						}
						
						
						else if (arch.getCatalog().indexOf("知识产权")!=-1){
							arch.setCatalog("知识产权案件");
						}else if (arch.getCatalog().indexOf("海事")!=-1){
							arch.setCatalog("海事案件"); 
						}else if (arch.getCatalog().indexOf("商事")!=-1){
							arch.setCatalog("商事案件"); 
						}else {
							arch.setCatalog("其他案件");
						}
					}
					
					
					//裁判日期
					//处理日期 1900——————当前日期
					arch.setApprovalDate(DateUtils.getOkDate(arch.getApprovalDate()));
					if(StringUtils.isNull(arch.getApprovalDate())){
						arch.setApprovalDateY(null);
						arch.setApprovalDateYM(null);
					}
					
				} catch (Exception e) {
					log.error("-----------------------------------处理当事人异常:" + arch.getClients());
				}
				
			}else{
//				writerString(fwUn,"无条文: " +arch.getFilePathHtml()   +" " +arch.getFilePath());
			}
			
			
			//处理日期
			

//			writerString(fw, StringUtils.GSON.toJson(arch));
			
			if(!StringUtils.isNull(arch.getLaws())){
				String ss[]=arch.getLaws().split("</br>");
				for (int i = 0; i < ss.length; i++) {
					writerString(fw, ss[i]);
				}
			}
			
		}

	}
	public static void writerString(BufferedWriter fw, String str) {
		try {
			// fw.append(str + "\n");
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
	 

}
