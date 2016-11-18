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
import cn.com.szgao.dto.LawOfficeLawyerVO;
import cn.com.szgao.dto.LawOfficeVO;
import cn.com.szgao.dto.TaxLawVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.PostgresqlUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;

/**
 * 律师事务所 ---------------的律师
 * 
 * @author liuming
 * @Date 2016年10月20日 上午11:22:41
 */
public class InsertLawOffice_LawyerToPG {

	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();

	static int count = 0;
	// 日志对象
	private static Logger log = LogManager.getLogger(InsertLawOffice_LawyerToPG.class);
	// 根据字符串生成UUID对象
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	public static void main(String[] args) {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));

		util.initData();

		try {
			// show(new
			// File("C:\\data\\税务评级+重大税务违法\\纳税信用A级纳税人名单\\taxpayer-2015.json"),0,"");
			show(new File("C:\\data\\66law(华律网)\\66law_20161014\\66law_20161014.json"), 0, "");

			log.info("---------------------  律师事务所重复数： " + repet);
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
				// fileS = null;

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

	// String encoding_from = "GB18030";
	static String encoding_from = "UTF-8";
	static String encoding_to = "UTF-8";

	static int repet = 0;

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
		// 22个

		String sqlInsert = "INSERT INTO law_office_lawyer_t(law_office_uuid,law_office_name,name "
				+ ",professional_field,helped_people_num,telephone,detail_link" + ",score,like_num,flag )"
				+ "VALUES(?,?,?,?,?,?,?,?,?,? )";

		try {
			conn = PostgresqlUtils.getConnection();
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
				System.out.println("总行：" + readNum);
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

					if (StringUtils.isNull(vo.getName())) {
						continue;
					}
					System.out.println(vo.getName());
					// vo = doVo(vo);

					String law_office_uuid = StringUtils.NBG.generate(vo.getName()).toString();
					String law_office_name = vo.getName();

					List<LawOfficeLawyerVO> list_lawyer = vo.getLawyers();
					if (list_lawyer.size() == 0) {
						continue;
					}
					for (LawOfficeLawyerVO lawOfficeLawyerVO : list_lawyer) {

						String name = lawOfficeLawyerVO.getName();
						if (StringUtils.isNull(name)) {
							continue;
						}

						String te = PostgresqlUtils
								.getValueFromSql("SELECT count(1) from law_office_lawyer_t WHERE law_office_uuid='"
										+ law_office_uuid + "'  and name='" + name + "'  ");
						if ("1".equals(te)) {
							repet++;
							log.info(vo.getName() + "# " + repet);
							continue;
						}
						

						String detail_link = StringUtils.isWebsite(lawOfficeLawyerVO.getDetail_link())
								? lawOfficeLawyerVO.getDetail_link() : null;
						String professional_field = lawOfficeLawyerVO.getProfessional_field();

						if (!StringUtils.isNull(professional_field) && professional_field.indexOf("擅长专业：") != -1) {
							professional_field = professional_field.substring(professional_field.indexOf("擅长专业：") + 5);
						}

						stmtInsert.setString(1, law_office_uuid);
						stmtInsert.setString(2, law_office_name);

						stmtInsert.setString(3, name);
						stmtInsert.setString(4, professional_field);
						stmtInsert.setString(5, lawOfficeLawyerVO.getHelped_people_num());

						stmtInsert.setString(6, lawOfficeLawyerVO.getTelephone());
						stmtInsert.setString(7, detail_link);
						stmtInsert.setString(8, lawOfficeLawyerVO.getScore());
						stmtInsert.setString(9, lawOfficeLawyerVO.getLike_num());
						//

						stmtInsert.setString(10, "1");// 1 华律网 2 文书

						// Timestamp ts = new
						// Timestamp(System.currentTimeMillis());
						// String tsStr = vo.getCollect_time();
						// try {
						// ts = Timestamp.valueOf(tsStr);
						// stmtInsert.setTimestamp(19, ts);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }

						// stmtInsert.execute();

						stmtInsert.addBatch();

						countLen++;
						System.out.println("----------入库： "+countLen);
						if (countLen % 10 == 0) {
							stmtInsert.executeBatch();
							conn.commit();
							stmtInsert.clearBatch();
							conn.setAutoCommit(false);
							stmtInsert = conn.prepareStatement(sqlInsert);
							// log.info("----写入PG库成功");
						}

						contentList.add(gs.toJson(vo));
						if (countLen % 10 == 0) {

							// LogUtils.writerDataToLogList(fileS, contentList);
							// LogUtils.writerTxt(fw, contentList);

							contentList = null;
							contentList = new ArrayList<String>();
						}
						// System.out.println(countLen);
						// key1 = vo.getKey();

					}

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
				// LogUtils.writerDataToLogList(fileS, contentList);

				log.info("---线程名" + Thread.currentThread().getName() + "----  " + countLen / 1000 + "  总数:  " + countLen
						+ "  已读行数:" + readNum + "   file: " + file.getPath() + "       CaseNum: " + vo.getName()
						+ "  KEY:  " + key1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
