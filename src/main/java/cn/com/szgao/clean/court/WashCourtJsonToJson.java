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
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 深圳
 * 
 * @author xiongchangyi
 *
 */
@SuppressWarnings("unused")
public class WashCourtJsonToJson {
	public WashCourtJsonToJson() {
	}

	// public FileIntoDataBase2p5(Logger log) {
	// this.log = log;
	// }

	private static Logger log = LogManager.getLogger(WashCourtJsonToJson.class.getName());

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

	/**
	 * 公司ID集合，
	 */
	List<String> companyIdList = new ArrayList<String>();
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
		
		String filePathUn =  file.getPath().replace("法院详细页面(清洗)", "法院详细页面(清洗)_无当事人LOG") ;
		
		
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

			// 处理正反方
			if (null != arch.getClients()) {
				try {
					String res[]=CourtUtils.getSplitClient(arch.getClients());
					arch.setPlaintiff(res[0]); // 原告相关人 √
					arch.setDefendant(res[1]); // 被告相关人 √
				} catch (Exception e) {
					log.error("-----------------------------------处理当事人异常:" + arch.getClients());
				}
				
			}else{
				writerString(fwUn,"无当事人: " +arch.getFilePathHtml()   +" " +arch.getFilePath());
			}

			writerString(fw, StringUtils.GSON.toJson(arch));
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
