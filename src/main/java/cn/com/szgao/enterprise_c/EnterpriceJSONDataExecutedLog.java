package cn.com.szgao.enterprise_c;

import cn.com.szgao.wash.data.AdministrationUtils;
import cn.com.szgao.wash.data.DataUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import rx.Observable;
import rx.functions.Func1;

import com.couchbase.client.deps.io.netty.util.internal.StringUtil;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.dto.BreakFaithVONew;
import cn.com.szgao.dto.ExecutedVO;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.LogUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 
 * @author
 *
 */
public class EnterpriceJSONDataExecutedLog {

	/**
	 * json与对象互转
	 */
	Gson gs = new Gson();
	/**
	 * 工具对象
	 */
	DataUtils utils = new DataUtils();
	/**
	 * 工具类，通过住所、登记机关获得行政区
	 */
	AdministrationUtils u = new AdministrationUtils();

	List<JsonDocument> documents = new ArrayList<JsonDocument>();
	Bucket bucket = null;
	private static String key = null;
	JsonObject content = null;

	// 日志对象
	private static Logger log = LogManager.getLogger(EnterpriceJSONDataExecutedLog.class);
	// 根据字符串生成UUID对象
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	// 变更前修改内容
	List<String> beforeList = new ArrayList<String>();
	// 变更后修改内容
	List<String> afterList = new ArrayList<String>();
	// 公司ID集合
	List<String> companyIdList = new ArrayList<String>();
	int count = 0;

	/**
	 * 循环调用是否是目录
	 * 
	 * @param file
	 * @throws ParseException
	 * @throws IOException
	 */
	public void show(File file, int startNum, String targetFDir) throws IOException, ParseException {
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
				fileS = null;

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

	int countLen = 0;
	File fileS = null;
	BufferedWriter fw = null;

//	String encoding_from = "GB18030";
	String encoding_from = "UTF-8";
	String encoding_to = "UTF-8";

	@SuppressWarnings("null")
	private void readFileByLines(File file, int startNum, String targetFDir) throws IOException, ParseException {
		System.out.println(file.getPath());

		JSONObject temJson = null;
		BufferedReader reader = null;
		JsonObject obj = null;
		JsonDocument doc = null;

		int readNum = 0;

		Connection conn = null;
		PreparedStatement stmtInsert = null;
		String sqlInsert = "INSERT INTO temp_t(temp01,temp02,temp03,temp04,temp05,temp06,temp07,temp08,temp09,temp10,temp11,temp12,temp13,temp14,temp15,temp16,temp17,temp18,temp19,temp20,key )"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?::uuid)";
		try {
			conn = PostgresqlUtilsLocal.getConnection();
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
		ExecutedVO vo = null;
		BreakFaithVONew vo_new = null;

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
					vo = gs.fromJson(temp, ExecutedVO.class);
				} catch (Exception e) {
					log.error("json转vo异常file.getPath():" + file.getPath() + "\n");
					log.error("json转vo异常:" + temJson + "\n");
					continue;
				}

				if (vo != null) {

					vo = doVo(vo);

					stmtInsert.setString(1, vo.getCaseState());
					stmtInsert.setString(2, vo.getOpenTime());
					stmtInsert.setString(3, vo.getExecuted());
					stmtInsert.setString(4, vo.getCollectTime());
					stmtInsert.setString(5, vo.getIdNum());
					stmtInsert.setString(6, vo.getDetailLink());
					stmtInsert.setString(7, vo.getCourtName());
					stmtInsert.setString(8, vo.getSubjectMatter() != null ? vo.getSubjectMatter().toString() : null);
					stmtInsert.setString(9, vo.getCaseNum());

					// 后加的
					stmtInsert.setString(10, vo.getProvince());
					stmtInsert.setString(11, vo.getCity());
					stmtInsert.setString(12, vo.getArea());
					stmtInsert.setString(13, vo.getFlag() != null ? vo.getFlag().toString() : null);
					stmtInsert.setString(14, vo.getSource() != null ? vo.getSource().toString() : null);
					stmtInsert.setString(15, vo.getSummary());

					stmtInsert.setString(16, vo.getModifyDate());
					stmtInsert.setString(17, null);
					stmtInsert.setString(18, null);
					stmtInsert.setString(19, null);
					stmtInsert.setString(20, null);

					stmtInsert.setString(21, vo.getKey());

					// stmtInsert.execute();

					stmtInsert.addBatch();

					if (countLen % 10000 == 0) {
						filename = "/excuted" + countLen + ".log";
						// targetFDir
						fileS = new File(targetFDir + filename);

						// fileS=new
						// File("D:/lm/log/temp/被执行人/112150014-113345749/data1"+filename);
						if (!fileS.exists()) {
							fileS.createNewFile();
							fw = new BufferedWriter(
									new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from)); // 指定编码格式，以免读取时中文字符异常

						} else {
							fileS.delete();
							fileS = new File(targetFDir + filename);
							fw = new BufferedWriter(
									new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from)); // 指定编码格式，以免读取时中文字符异常

						}
					}

					countLen++;
					if (countLen % 100 == 0) {
						stmtInsert.executeBatch();
						conn.commit();
						stmtInsert.clearBatch();
						conn.setAutoCommit(false);
						stmtInsert = conn.prepareStatement(sqlInsert);
						// log.info("----写入PG库成功");
					}

					// LogUtils.writerDataToLog(fileS, gs.toJson(vo));

					contentList.add(gs.toJson(vo));
					if (countLen % 100 == 0) {

						// LogUtils.writerDataToLogList(fileS, contentList);
						LogUtils.writerTxt(fw, contentList);

						contentList = null;
						contentList = new ArrayList<String>();
					}
					// System.out.println(countLen);
					key1 = vo.getKey();

					System.out.println("---线程名" + Thread.currentThread().getName() + "---- " + countLen + "   " + key1
							+ "-----" + file.getPath());

					if (countLen % 1000 == 0) {
						content = null;
						documents = null;
						documents = new ArrayList<JsonDocument>();
						long endTime = System.currentTimeMillis();
						String result = (float) ((endTime - startTime) / 1000) + "秒";
						log.info("----  " + countLen / 1000 + " 批次" + "  " + result + "  总数:  " + countLen + "  已读行数:"
								+ readNum + "   file: " + file.getPath() + "       CaseNum: " + vo.getCaseNum()
								+ "  KEY:  " + key1);
						endTime = 0;
						startTime = System.currentTimeMillis();
						result = null;
						key1 = null;
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
				LogUtils.writerDataToLogList(fileS, contentList);

				log.info("---线程名" + Thread.currentThread().getName() + "----  " + countLen / 1000 + "  总数:  " + countLen
						+ "  已读行数:" + readNum + "   file: " + file.getPath() + "       CaseNum: " + vo.getCaseNum()
						+ "  KEY:  " + key1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();

	/**
	 * 处理VO
	 * 
	 * @param voT
	 * @return
	 */
	public static ExecutedVO doVo(ExecutedVO voT) {
		ExecutedVO vo = voT;
		String key1 = null;
		if (vo != null) {
			// 合并company personname 到executed
			if (StringUtils.isNull(vo.getExecuted())) {
				if (!StringUtils.isNull(vo.getCompany())) {
					vo.setExecuted(StringUtils.removeSepar(vo.getCompany()));
				}
				if (!StringUtils.isNull(vo.getPersonName())) {
					vo.setExecuted(StringUtils.removeSepar(vo.getPersonName()));
				}
				vo.setCompany(null);
				vo.setPersonName(null);
			} else {
				vo.setCompany(null);
				vo.setPersonName(null);
			}

			if (!StringUtils.isNull(vo.getDetailLink())) {
				key1 = nbg.generate(vo.getDetailLink()).toString();
			} else {
				key1 = UUID.randomUUID().toString();
			}
			vo.setKey(key1);

			// 判断身份证是否包含“无”等字段
			if (!StringUtils.isNull(vo.getIdNum())) {
				String tempIdNum = vo.getIdNum().trim();
				if ("无".equals(tempIdNum) || "0".equals(tempIdNum) || tempIdNum.length() == 1
						|| "000000000000000".equals(tempIdNum) || "000000000000000000".equals(tempIdNum)
						|| StringUtils.isSameChars(tempIdNum)) {
					vo.setIdNum(null);
				} else if (tempIdNum.length() < 15) {
					vo.setIdNum(null);
				}
				// else if(!StringUtils.isCardId(tempIdNum)){//判断是否为18位或15位
				// vo.setIdNum(null);
				// }
				// else if(tempIdNum.contains("*")){//可能会有19位
				if (StringUtils.hasDigit(tempIdNum) && !StringUtils.isHasHanziSpeChar(tempIdNum)
						&& !StringUtils.hasCharAtoZNotX(tempIdNum)) {
					vo.setIdNum(tempIdNum.toUpperCase());
				}
				// }
				else {
					// 统计转化为大写 X
					vo.setIdNum(tempIdNum.toUpperCase());
				}
			}
			// array[0]省，array[1]地级市，array[2]县级市、县、区
			if (!StringUtils.isNull(vo.getCourtName())) {
				String[] array = util.utils(vo.getCourtName());
				if (null != array) {
					vo.setProvince(array[0]);
					vo.setCity(array[1]);
					vo.setArea(array[2]);
				}
			}
			// 处理文号
			if (null != vo.getCaseNum()) {
				String tempExNum = StringUtils.toDBC(vo.getCaseNum()).replace("（", "(");
				tempExNum = tempExNum.replace("）", ")");
				tempExNum = tempExNum.replace("【", "(");
				tempExNum = tempExNum.replace("】", ")");
				tempExNum = tempExNum.replace("[", "(");
				tempExNum = tempExNum.replace("]", ")");
				if ("".equals(tempExNum) || "无".equals(tempExNum.trim()) || "空".equals(tempExNum.trim())) {
					vo.setCaseNum(null);
				} else {
					vo.setCaseNum(tempExNum);
				}
			}

			if (null != vo.getOrgCode()) {
				String tempOrgCode = vo.getOrgCode();
				if (StringUtils.isHasHanZi(tempOrgCode) || tempOrgCode.trim().length() < 9 || "0".equals(tempOrgCode)
						|| "-".equals(tempOrgCode) || "00000000".equals(tempOrgCode) || "00000000-0".equals(tempOrgCode)
						|| "12345678".equals(tempOrgCode) || tempOrgCode.contains("*")) {
					vo.setOrgCode(null);
				} else {
					tempOrgCode = tempOrgCode.replace(" ", "").replace("－", "-").replace("—", "-");
					if (!tempOrgCode.matches("[a-zA-Z0-9]{8}-[a-zA-Z0-9]")) {
						if (tempOrgCode.length() == 9) {
							tempOrgCode = tempOrgCode.substring(0, tempOrgCode.length() - 1) + "-"
									+ tempOrgCode.substring(tempOrgCode.length() - 1);
						}
					}
					vo.setOrgCode(tempOrgCode);
				}
			}

			// 1 表示执行数据
			vo.setSource(1);
			// 1 有效
			vo.setFlag(1);
			vo.setModifyDate(DateUtils.toYMDOfChaStr_ES(DateUtils.getDateyyyy_MMddhhmmss()));

			if ("1".equals(vo.getCaseState())) {
				vo.setCaseState("已结案");
			} else {
				vo.setCaseState("执行中");
			}

			if (!StringUtils.isNull(vo.getCollectTime())) {
				if (!vo.getCollectTime().contains("+0800")) {
					vo.setCollectTime(DateUtils.toYMDOfChaStr_ESZZ2(vo.getCollectTime()));
				}
			}

			if (!StringUtils.isNull(vo.getOpenTime())) {
				if (!vo.getOpenTime().contains("+0800")) {
					vo.setOpenTime(DateUtils.toYMDOfChaStr_ESZZ2(vo.getOpenTime()));
				}
			}

			// 处理执行标的
			if (!StringUtils.isNull(vo.getSubjectMatter())) {
				String subjectMatter = vo.getSubjectMatter();
				if (subjectMatter.contains("E")) {
					BigDecimal db = new BigDecimal(subjectMatter);
					String ii = db.toPlainString();
					vo.setSubjectMatter(ii);
				} else if (StringUtils.isNumericDecimal(subjectMatter)) {
					vo.setSubjectMatter(subjectMatter);
				} else {
					log.error("处理执行标的异常：" + vo.getKey() + "--" + subjectMatter + "--" + new Gson().toJson(vo));
					vo.setSubjectMatter(null);
				}
			}

			String summary = "";
			StringBuffer sbSummary = new StringBuffer();
			if (!StringUtils.isNull(vo.getExecuted())) {
				sbSummary.append(vo.getExecuted() + ";");
			}
			if (null != vo.getSubjectMatter()) {
				sbSummary.append("执行标的：" + vo.getSubjectMatter() + ";");
			}
			if (!StringUtils.isNull(vo.getCourtName())) {
				sbSummary.append("执行法院：" + vo.getCourtName() + ";");
			}
			if (!StringUtils.isNull(vo.getCaseNum())) {
				sbSummary.append("案号：" + vo.getCaseNum() + ";");
			}
			if (!StringUtils.isNull(vo.getOpenTime())) {
				sbSummary.append("立案时间：" + DateUtils.toYMDFromZZ(vo.getOpenTime()) + ";");
			}
			summary = sbSummary.toString();
			// 摘要
			// String summary = vo.getExecuted()+";执行标的:
			// "+vo.getSubjectMatter()+";执行法院："+vo.getCourtName()+";案号："+vo.getCaseNum()+";立案时间:"+vo.getOpenTime();
			// summary = summary.replace("null", "");
			vo.setSummary(summary);

		}
		return vo;

	}

	/**
	 * 公用查库的方法
	 * 
	 * @param bucket
	 *            指定的桶
	 * @param doc
	 *            文档
	 */
	public void commonInsert(Bucket bucket, JsonDocument doc) {
		try {
			bucket.upsert(doc, 5000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			log.info(e);
			while (true) {
				try {
					bucket.upsert(doc, 5000, TimeUnit.MILLISECONDS);
					break;
				} catch (Exception ee) {
					log.info(ee);
				}
			}
		}
	}

	/**
	 * 通过行政区划获得行政区
	 * 
	 * @param adminiCode
	 *            行政区划
	 * @return 省市县数组
	 */
	public String[] listCountryCityProvince(String adminiCode) {
		// 0~2元素是省、市、县
		String[] countryCityProvince = new String[3];
		if (null != adminiCode) {
			if (adminiCode.length() < 6) {
				return countryCityProvince;
			}
			// 截取注册号的行政区划
			adminiCode = adminiCode.substring(0, 6);
			// 如果adminiCode是县的行政区划
			if (DataUtils.adminCountryMap.containsKey(adminiCode)) {
				// 县名称、地级市ID
				String array[] = DataUtils.adminCountryMap.get(adminiCode);
				countryCityProvince[2] = array[0];// 县名称
				if (null != array[1]) {
					int cityId = Integer.parseInt(array[1]);
					// 直辖市的ID: '北京市','天津市','重庆市','上海市'
					if (cityId == 400 || cityId == 401 || cityId == 402 || cityId == 403) {
						String provinceName = utils.listProvinceNameByProvinceId(cityId);
						if (null != provinceName) {
							countryCityProvince[0] = provinceName;// 直辖市名称
							return countryCityProvince;
						}
					}
					// listProvinceNameByProvinceId
					Map<String, Integer> cityProvinceMap = utils.listCityProvinceIdByCityId(cityId);
					// 市名称
					Set<String> cityNameSet = cityProvinceMap.keySet();
					Iterator<String> iter = cityNameSet.iterator();
					if (iter.hasNext()) {
						countryCityProvince[1] = iter.next();// 市名称
						cityNameSet = null;
						iter = null;
					}
					// 省的ID
					Collection<Integer> conllection = cityProvinceMap.values();
					Iterator<Integer> iterator = conllection.iterator();
					if (iterator.hasNext()) {
						String provinceName = utils.listProvinceNameByProvinceId(iterator.next());
						if (null != provinceName) {
							countryCityProvince[0] = provinceName;// 省名称
							iterator = null;
							conllection = null;
						}
					}
				}
			} else if (DataUtils.adminCityMap.containsKey(adminiCode)) {
				String array[] = DataUtils.adminCityMap.get(adminiCode);
				countryCityProvince[1] = array[0];// 获得市名称
				int provinceId = Integer.parseInt(array[1]);// 省ID
				String provinceName = utils.listProvinceNameByProvinceId(provinceId);
				if (null != provinceName) {
					countryCityProvince[0] = provinceName;// 获得省名称
				}
			} else if (DataUtils.adminProvinceMap.containsKey(adminiCode)) {
				countryCityProvince[0] = DataUtils.adminProvinceMap.get(adminiCode);// 获得省名称
			}
		}
		return countryCityProvince;
	}

	/**
	 * 行政区划数组处理直辖市
	 * 
	 * @param admin
	 *            行政区数组
	 * @return 行政区数组结果
	 */
	public String[] doAdmin(String admin[]) {
		if (null != admin[0] || null != admin[1]) {
			if (null != admin[1] && (admin[1].equals("北京市") || admin[1].equals("天津市") || admin[1].equals("重庆市")
					|| admin[1].equals("上海市"))) {
				admin[0] = admin[1];
				admin[1] = null;
			}
		}
		return admin;
	}

	/**
	 * 写数据
	 */
	public void createJsonDocument(String name, JsonDocument doc) {
		Bucket bucket = ClusterUtil.commonBucket(name);
		while (true) {
			try {
				bucket.upsert(doc);
				break;
			} catch (Exception e) {
				log.error("超时:" + e.getMessage());
				log.error("重写:" + doc.toString());
			} finally {

			}
		}
	}
}
