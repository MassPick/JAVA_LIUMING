package cn.com.szgao.wash.data.taxlaw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.ExecutedVO;
import cn.com.szgao.dto.LawOfficeVO;
import cn.com.szgao.dto.TaxLawVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.PostgresqlUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;



/**
 * 律师事务所
 * @author liuming
 * @Date 2016年10月20日 上午11:22:41
 */
public class InsertLawOfficeToPG {

	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();
	
	static int count = 0;
	// 日志对象
	private static Logger log = LogManager.getLogger(InsertLawOfficeToPG.class);
	// 根据字符串生成UUID对象
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	
	
	public static void main(String[] args) {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		
		util.initData();
		
		try {
//			show(new File("C:\\data\\税务评级+重大税务违法\\纳税信用A级纳税人名单\\taxpayer-2015.json"),0,"");
			show(new File("C:\\data\\66law(华律网)\\66law_20161014\\66law_20161014.json"),0,"");
			
			log.info("---------------------  律师事务所重复数： "+repet);  // 律师事务所重复数： 5948

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void show(File file, int startNum, String targetFDir) throws IOException, ParseException {
		long startTime = System.currentTimeMillis();
		if (file.isFile()) {
			count += 1;
			log.info("数量:" + count + "---线程名" + Thread.currentThread().getName() + "---" + file.getPath());
			System.out.println(count);
			readFileByLines(file, startNum, targetFDir);
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		for (File fi : files) {
			if (fi.isFile()) {
				count += 1;
				log.info("数量:" + count + "---线程名" + Thread.currentThread().getName() + "----" + file.getPath());
				// System.out.println(count);
				readFileByLines(fi, startNum, targetFDir);
				// fi.delete();
			} else if (fi.isDirectory()) {

				// 为创建新文件用
				countLen = 0;
//				fileS = null;

				show(fi, startNum, targetFDir);
				
			} else {
				continue;
			}
		}
		// logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis()
		// - da) / 1000)) + "秒数");
		// logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis()
		// - da) / 1000) / (60 * 1)) + "分钟");
		// logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis()
		// - da) / 1000) / (60 * 60)) + "小时");
		// logger.info("平均每秒" + (float) (SUM / (float) ((float)
		// ((System.currentTimeMillis() - da) / 1000) / (1 * 1))));
		// logger.info("平均每分" + (float) (SUM / (float) ((float)
		// ((System.currentTimeMillis() - da) / 1000) / (60 * 1))));
		// logger.info("平均每时" + (float) (SUM / (float) ((float)
		// ((System.currentTimeMillis() - da) / 1000) / (60 * 60))));
	}

	static int countLen = 0;
	File fileS = null;
	BufferedWriter fw = null;

//	String encoding_from = "GB18030";
	static String encoding_from = "UTF-8";
	static String encoding_to = "UTF-8";
	
	static int repet=0;

	@SuppressWarnings("null")
	private static void readFileByLines(File file, int startNum, String targetFDir) throws IOException, ParseException {
		System.out.println(file.getPath());

		JSONObject temJson = null;
		BufferedReader reader = null;
		JsonObject obj = null;
		JsonDocument doc = null;

		int readNum = 0;

		Connection conn = null;
		PreparedStatement stmtInsert = null;
		//22个
		
		
		
		String sqlInsert = "INSERT INTO law_office_t(website,province,fax "
				+ ",collect_time,detail_link,address,city"
				+ ",scope_of_business,law_office_name,introduction,email,law_firm_director,telephone,law_office_uuid,flag )"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
		
		
		
		
		try {
			conn = PostgresqlUtils .getConnection();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			stmtInsert = conn.prepareStatement(sqlInsert);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		try {
			conn.setAutoCommit(false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

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

		String key1 = null;
		String temp = null;
		LawOfficeVO vo = null;
		LawOfficeVO vo_new = null;

		Gson gs = new Gson();
		long startTime = System.currentTimeMillis();

		String sbIdNum = "";// //身份证号
		String filename = null;
		List<String> contentList = new ArrayList<String>();
		

		while ((temp = reader.readLine()) != null) {
			try {
				readNum++;
				System.out.println("总行："+readNum);
				if (readNum < startNum) {
					continue;
				}
				if (StringUtils.isNull(temp)) {
					continue;
				}
				try {
					temJson = JSONObject.fromObject(temp);
				} catch (Exception e) {
					log.error("json异常file.getPath():" + file.getPath() + "\n");
					log.error("json异常:" + temJson + "\n");
					continue;
				}

				// System.out.println(temJson);
				try {
					vo = gs.fromJson(temp, LawOfficeVO.class);
				} catch (Exception e) {
					log.error("json转vo异常file.getPath():" + file.getPath() + "\n");
					log.error("json转vo异常:" + temJson + "\n");
					continue;
				}

				if (vo != null) {


					
					if(StringUtils.isNull(vo.getName())){
						continue;
					}
					System.out.println(vo.getName());
//					vo = doVo(vo);
					
					String law_office_uuid=StringUtils.NBG.generate(vo.getName()).toString();
					
					String te = PostgresqlUtils.getValueFromSql("SELECT count(1) from law_office_t WHERE law_office_uuid='"+law_office_uuid+"'   ");
					if("1".equals(te)){
						repet++;
						log.info(vo.getName()+ "# "+repet);
						continue;
					}
					
					String website=StringUtils.isWebsite(vo.getWebsite())?vo.getWebsite():null;
					String detail_link=StringUtils.isWebsite(vo.getDetail_link())?vo.getDetail_link():null;
					String email=StringUtils.isEmail(vo.getEmail())?vo.getEmail():null;
					String[] array = util.utils(vo.getProvince()+vo.getCity());
					if (null != array) {
						vo.setProvince(array[0]);
						vo.setCity(array[1]);
						
					}
					
					//boss
					String law_firm_director=vo.getLaw_firm_director();
					if(!StringUtils.isNull(law_firm_director)){
						if(law_firm_director.length()==0||law_firm_director.length()==1){
							law_firm_director=null;
						}
					}
					//简介
					String introduction=vo.getIntroduction();
					if(!StringUtils.isNull(introduction)){
						if("暂无简介".equals(introduction )){
							introduction=null;
						}
					}
					
					
					//电话
					String telephone=vo.getTelephone();
					if(!StringUtils.isNull(telephone)){
						telephone=telephone.replace("：", "").replace("+", "");
					}
					//fax 传真
					String fax=vo.getFax();
					if(!StringUtils.isNull(fax)&&fax.indexOf("无")!=-1){
						fax=null;
					}

					stmtInsert.setString(1, website);
					
					stmtInsert.setString(2, vo.getProvince() );
					
					stmtInsert.setString(3,fax!=null?fax.replace("－", "-"):null );
					stmtInsert.setString(4, vo.getCollect_time()); 
					stmtInsert.setString(5, detail_link);
					
					stmtInsert.setString(6, vo.getAddress());
					stmtInsert.setString(7, vo.getCity() );
					stmtInsert.setString(8, vo.getScope_of_business());
					stmtInsert.setString(9, vo.getName());

					
					// 
					stmtInsert.setString(10, introduction );// 
					
					stmtInsert.setString(11, email);
					stmtInsert.setString(12, law_firm_director);

					
					stmtInsert.setString(13, telephone != null ? telephone : null);
					stmtInsert.setString(14, law_office_uuid);
					stmtInsert.setString(15, "1");//		1 华律网   2 文书
					
//					Timestamp ts = new Timestamp(System.currentTimeMillis());
//					String tsStr = vo.getCollect_time();
//					try {
//						ts = Timestamp.valueOf(tsStr);
//						stmtInsert.setTimestamp(19, ts);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					
//					 stmtInsert.execute();

					stmtInsert.addBatch();

//					if (countLen % 10000 == 0) {
//						filename = "/excuted" + countLen + ".log";
//						// targetFDir
//						fileS = new File(targetFDir + filename);
//
//						// fileS=new
//						// File("D:/lm/log/temp/被执行人/112150014-113345749/data1"+filename);
//						if (!fileS.exists()) {
//							fileS.createNewFile();
//							fw = new BufferedWriter(
//									new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from)); // 指定编码格式，以免读取时中文字符异常
//
//						} else {
//							fileS.delete();
//							fileS = new File(targetFDir + filename);
//							fw = new BufferedWriter(
//									new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from)); // 指定编码格式，以免读取时中文字符异常
//						}
//					}

					countLen++;
					System.out.println(countLen);
					if (countLen % 10 == 0) {
						stmtInsert.executeBatch();
						conn.commit();
						stmtInsert.clearBatch();
						conn.setAutoCommit(false);
						stmtInsert = conn.prepareStatement(sqlInsert);
						// log.info("----写入PG库成功");
					}
					
					// LogUtils.writerDataToLog(fileS, gs.toJson(vo));

					contentList.add(gs.toJson(vo));
					if (countLen % 10 == 0) {

						// LogUtils.writerDataToLogList(fileS, contentList);
//						LogUtils.writerTxt(fw, contentList);

						contentList = null;
						contentList = new ArrayList<String>();
					}
					// System.out.println(countLen);
//					key1 = vo.getKey();

//					System.out.println("---线程名" + Thread.currentThread().getName() + "---- " + countLen + "   " + key1
//							+ "-----" + file.getPath());

//					if (countLen % 1000 == 0) {
//						content = null;
//						documents = null;
//						documents = new ArrayList<JsonDocument>();
//						long endTime = System.currentTimeMillis();
//						String result = (float) ((endTime - startTime) / 1000) + "秒";
//						log.info("----  " + countLen / 1000 + " 批次" + "  " + result + "  总数:  " + countLen + "  已读行数:"
//								+ readNum + "   file: " + file.getPath() + "       CaseNum: " + vo.getCaseNum()
//								+ "  KEY:  " + key1);
//						endTime = 0;
//						startTime = System.currentTimeMillis();
//						result = null;
//						key1 = null;
//					}

				}
			} catch (Exception e) {
				log.info(e);
				return;
			}
		}
		try {
			if (contentList.size() > 0) {
				stmtInsert.executeBatch();
				conn.commit();
				stmtInsert.clearBatch();
				stmtInsert.close();
				conn.close();
//				LogUtils.writerDataToLogList(fileS, contentList);

				log.info("---线程名" + Thread.currentThread().getName() + "----  " + countLen / 1000 + "  总数:  " + countLen
						+ "  已读行数:" + readNum + "   file: " + file.getPath() + "       CaseNum: " + vo.getName()						+ "  KEY:  " + key1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
