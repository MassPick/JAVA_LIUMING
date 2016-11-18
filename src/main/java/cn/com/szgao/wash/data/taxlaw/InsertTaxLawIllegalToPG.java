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
import cn.com.szgao.dto.TaxLawVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.PostgresqlUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;


/**
 * 重大税收违法案件信息  true表示企业重大纳税违法；false表示自然人重大纳税违法.  评级：违法评级为D，企业信用评级为A，其他为B/C          暂没有管自然人
 * @author liuming
 * @Date 2016年8月10日 上午9:49:13
 */
public class InsertTaxLawIllegalToPG {

	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();
	
	static int count = 0;
	// 日志对象
	private static Logger log = LogManager.getLogger(InsertTaxLawIllegalToPG.class);
	// 根据字符串生成UUID对象
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	
	
	public static void main(String[] args) {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		
		util.initData();
		
		try {
			show(new File("C:\\data\\税务评级+重大税务违法\\重大税收违法案件信息\\tax_illegal_company.json"),0,"");   
//			show(new File("C:\\data\\税务评级+重大税务违法\\重大税收违法案件信息\\tax_illegal_person.json"),0,"");
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
		String sqlInsert = "INSERT INTO TAX_LAW_T(finagender,reg_address,casequality,finaname,legalrep,"
				+ "taxpayer_id,org_code,punishment,illegal_activity,legalrep_certificate,"
				+ "legalrep_gender,taxpayer,url,intermediary,financial_certificate,"
				+ "type,rate,year,collect_time,province,city,area )"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
		TaxLawVO vo = null;
		TaxLawVO vo_new = null;

		Gson gs = new Gson();
		long startTime = System.currentTimeMillis();

		String sbIdNum = "";// //身份证号
		String filename = null;
		List<String> contentList = new ArrayList<String>();

		while ((temp = reader.readLine()) != null) {
			try {
				readNum++;
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
					vo = gs.fromJson(temp, TaxLawVO.class);
				} catch (Exception e) {
					log.error("json转vo异常file.getPath():" + file.getPath() + "\n");
					log.error("json转vo异常:" + temJson + "\n");
					continue;
				}

				if (vo != null) {

//					vo = doVo(vo);
					
//					String te = PostgresqlUtils.getValueFromSql("SELECT count(1) from TAX_LAW_T WHERE taxpayer='"+vo.getTaxpayer_name()+"' and year='"+vo.getYear()+"' ");
					String te = PostgresqlUtils.getValueFromSql("SELECT count(1) from TAX_LAW_T WHERE taxpayer='"+vo.getTaxpayer_name()+"'  ");
					if("1".equals(te)){
						continue;
					}
					

					stmtInsert.setString(1, vo.getFinancial_chief_gender());
					stmtInsert.setString(2, vo.getReg_addr() );
					stmtInsert.setString(3, vo.getCase_nature() );
					stmtInsert.setString(4, vo.getFinancial_chief_name());
					stmtInsert.setString(5, vo.getRep_name());
					stmtInsert.setString(6, vo.getTaxpayer_id());
					stmtInsert.setString(7, vo.getOrg_code() );
					stmtInsert.setString(8, vo.getPunishment()  != null ? vo.getPunishment().toString() : null);
					stmtInsert.setString(9, vo.getLlegal_activity());

					
					// 
					vo.setLegalrep_certificate(    (  StringUtils.isNull(vo.getRep_id_type())==false?vo.getRep_id_type():"")     + (StringUtils.isNull (vo.getRep_id())==false? (":"+vo.getRep_id()):"" )  );
					
					stmtInsert.setString(10, vo.getLegalrep_certificate() );//---法定代表人证件名称及号码
					
					stmtInsert.setString(11, vo.getRep_gender());
					stmtInsert.setString(12, vo.getTaxpayer_name());
					stmtInsert.setString(13, vo.getDetail_link()   != null ? vo.getDetail_link().toString() : null);
					stmtInsert.setString(14, vo.getIntermediary_info() != null ? vo.getIntermediary_info().toString() : null);
					
					vo.setFinancial_certificate(( StringUtils.isNull( vo.getFinancial_chief_id_type())  ==false?vo.getFinancial_chief_id_type():"")   + (   StringUtils.isNull (vo.getFinancial_chief_id())==false? (":"+vo.getFinancial_chief_id()):"" )  );
					stmtInsert.setString(15, vo.getFinancial_certificate() );//---
					
					
					stmtInsert.setBoolean(16, true); //true表示企业重大纳税违法；false表示自然人重大纳税违法.
//					stmtInsert.setBoolean(16, false); //true表示企业重大纳税违法；false表示自然人重大纳税违法.

					vo.setRate("D");
					stmtInsert.setString(17, vo.getRate());//---
					
					vo.setYear("2016");
					stmtInsert.setString(18, vo.getYear());
					
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					String tsStr = vo.getCollect_time();
					try {
						ts = Timestamp.valueOf(tsStr);
						stmtInsert.setTimestamp(19, ts);
//						System.out.println(ts);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					String[] array = util.utils_company(vo.getProvince()+vo.getTaxpayer_name()+vo.getReg_addr());
					stmtInsert.setString(20, array[0]);
					stmtInsert.setString(21, array[1]);
					stmtInsert.setString(22, array[2]);
					

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
						+ "  已读行数:" + readNum + "   file: " + file.getPath() + "       CaseNum: " + vo.getTaxpayer_name()
						+ "  KEY:  " + key1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
