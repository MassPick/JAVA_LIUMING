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
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.LogUtils;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 
 * @author
 *
 */
public class EnterpriceJSONDataBreakFaithLog {
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
	private Logger log = LogManager.getLogger(EnterpriceJSONDataBreakFaithLog.class);
	// 根据字符串生成UUID对象
	NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
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
				System.out.println(count);
				readFileByLines(fi, startNum, targetFDir);
				// fi.delete();
			} else if (fi.isDirectory()) {
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

	BufferedWriter fw = null;

	String encoding_from = "GB18030";
	String encoding_to = "UTF-8";

	private void readFileByLines(File file, int startNum, String targetFDir) throws IOException, ParseException {
		System.out.println(file.getPath());

		File fileS = null;
		JSONObject temJson = null;
		BufferedReader reader = null;
		JsonObject obj = null;
		JsonDocument doc = null;
		int countLen = 0;
		int readNum = 0;

		cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();
		try {
			// reader = new BufferedReader(new FileReader(file));
			// InputStreamReader in= new InputStreamReader(new
			// FileInputStream(file),"GB18030'");
			// reader = new BufferedReader(in);
			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "GB18030");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String key1 = null;
		String temp = null;
		BreakFaithVO vo = null;
		WholeCourtVO vo_new = null;

		Gson gs = new Gson();
		long startTime = System.currentTimeMillis();

		String sbIdNum = "";// //身份证号
		String filename = null;

		while ((temp = reader.readLine()) != null) {
			try {

				readNum++;

				if (readNum < startNum) {
					continue;
				}

				try {
					temJson = JSONObject.fromObject(temp);
				} catch (Exception e) {
					log.error("json异常file.getPath():" + file.getPath() + "\n");
					log.error("json异常:" + temJson + "\n");
					continue;
				}

				System.out.println(temJson);
				try {
					vo = gs.fromJson(temp, BreakFaithVO.class);
				} catch (Exception e) {
					log.error("json转vo异常file.getPath():" + file.getPath() + "\n");
					log.error("json转vo异常:" + temJson + "\n");
					continue;
				}

				if (vo != null) {

					if (!StringUtils.isNull(vo.getDetailLink())) {
						String detailLink = vo.getDetailLink();

						// if (detailLink.contains("?pCode=")) {
						// String s1 = detailLink.substring(0,
						// detailLink.indexOf("?pCode=") + 1);
						// String s2 =
						// detailLink.substring(detailLink.indexOf("&id=") + 1);
						//
						// detailLink = s1 + s2;
						// key1 = nbg.generate(detailLink).toString();
						// s1 = null;
						// s2 = null;
						// } else {
						// key1 = nbg.generate(vo.getDetailLink()).toString();
						// }
						// 取URL中的ID号
						// http://shixin.court.gov.cn/detail?id=1000068
						if (detailLink.contains("id=")) {
							String s2 = detailLink.substring(detailLink.lastIndexOf("=") + 1);
							key1 = nbg.generate(s2).toString();
							System.out.println(s2);
							s2 = null;
						} else {
							key1 = nbg.generate(vo.getDetailLink()).toString();
						}

						detailLink = null;
					} else {
						key1 = UUID.randomUUID().toString();
					}

					// 法院
					if (null != vo.getCourtName()) {

						if (!StringUtils.isNull(vo.getPersonName())) {
							String tempPersonName = vo.getPersonName();
							// 取人名中可能存在的身份证
							sbIdNum = StringUtils.pickUpManyCardId(tempPersonName);
							if (!StringUtils.isNull(sbIdNum)) {
								vo.setIdNum(StringUtils.subSpeCharBlank(sbIdNum));
								tempPersonName = StringUtils.subSpeCharBlank(StringUtils.removeNum3(tempPersonName))
										.trim();
								vo.setPersonName(tempPersonName);
							}
						}
					}
					// 处理 法定代表人或者负责人姓名
					out: if (!StringUtils.isNull(vo.getRepName())) {

						String tempRepName = vo.getRepName().trim();
						if (tempRepName.length() == 1 || "0".equals(tempRepName) || "无".equals(tempRepName)
								|| "空".equals(tempRepName) || "*".equals(tempRepName)) {
							vo.setRepName(null);
							break out;
						}
						// 取人名中可能存在的身份证
						sbIdNum = StringUtils.pickUpManyCardId(tempRepName);
						if (!StringUtils.isNull(sbIdNum)) {
							vo.setIdNum(StringUtils.subSpeCharBlank(sbIdNum));
							tempRepName = StringUtils.subSpeCharBlank(StringUtils.removeNum3(tempRepName)).trim();
							vo.setRepName(tempRepName);
						}
					}
					// 判断身份证是否包含“无”等字段
					if (!StringUtils.isNull(vo.getIdNum())) {
						String tempIdNum = vo.getIdNum().trim();
						if ("无".equals(tempIdNum) || "000000000000000".equals(tempIdNum)
								|| "000000000000000000".equals(tempIdNum)) {
							vo.setIdNum(null);
						}

						else if (StringUtils.isNull(vo.getOrgCode())
								&& tempIdNum.matches("[a-zA-Z0-9]{8}-[a-zA-Z0-9]")) {
							vo.setOrgCode(tempIdNum.toUpperCase());
							vo.setIdNum(null);
							log.error("身份证是组织代码");
							log.error(temJson);
						} else if (tempIdNum.length() < 15) {

							// vo.setOrgCode(null);
							// vo.setIdNum(null);
							// log.error("身份证不足15位:"+key1);
							// log.error(temJson);

							String tempOrgCode = tempIdNum.replace(" ", "").replace("－", "-").replace("—", "-")
									.replace("(", "").replace(")", "").replace("（", "").replace("）", "");
							if (!tempOrgCode.matches("[a-zA-Z0-9]{8}-[a-zA-Z0-9]")) {
								if (tempOrgCode.length() == 9) {
									tempOrgCode = tempOrgCode.substring(0, tempOrgCode.length() - 1) + "-"
											+ tempOrgCode.substring(tempOrgCode.length() - 1);
									vo.setOrgCode(tempOrgCode.toUpperCase());
									vo.setIdNum(null);
									log.error("身份证是组织代码:" + key1);
									log.error(temJson);
								} else {
									vo.setOrgCode(tempOrgCode);
									vo.setIdNum(null);
									log.error("身份证不足15位:" + key1);
									log.error(temJson);
								}
							}
						} else {
							// 统计转化为大写 X
							vo.setIdNum(tempIdNum.toUpperCase());
						}
						tempIdNum = null;
					}
					// 性别为空，而身份证不为空时，补齐性别
					if ((null == vo.getGender() || "".equals(vo.getGender())) && null != vo.getIdNum()) {
						String gender = StringUtils.getSexFromIdCard(vo.getIdNum());
						vo.setGender(gender);
					}
					// 处理法院
					String tempCourtName = vo.getCourtName();
					tempCourtName = StringUtils.removeBlank(tempCourtName);
					tempCourtName = StringUtils.QtoNull(tempCourtName);
					vo.setCourtName(tempCourtName);

					// array[0]省，array[1]地级市，array[2]县级市、县、区
					String[] array = util.utils(vo.getCourtName());
					if (null != array) {
						vo.setProvince(array[0]);
						vo.setCity(array[1]);
						vo.setArea(array[2]);
					}
					// 处理 执行依据文号
					if (null != vo.getExecuteNum()) {
						String tempExNum = StringUtils.toDBC(vo.getExecuteNum()).replace("（", "(");
						tempExNum = tempExNum.replace("）", ")");
						tempExNum = tempExNum.replace("【", "(");
						tempExNum = tempExNum.replace("】", ")");
						tempExNum = tempExNum.replace("[", "(");
						tempExNum = tempExNum.replace("]", ")");
						tempExNum = tempExNum.replace("{", "(");
						tempExNum = tempExNum.replace("}", ")");
						tempExNum = tempExNum.replace("｛", "(");
						tempExNum = tempExNum.replace("｝", ")");
						if ("".equals(tempExNum) || "无".equals(tempExNum.trim()) || "空".equals(tempExNum.trim())
								|| "文书编号".equals(tempExNum.trim())) {
							vo.setExecuteNum(null);
						} else {
							vo.setExecuteNum(tempExNum);
						}
					}
					if (null != vo.getCaseNum()) {
						String tempCaNum = StringUtils.toDBC(vo.getCaseNum()).replace("（", "(");
						tempCaNum = tempCaNum.replace("）", ")");
						if ("".equals(tempCaNum)) {
							vo.setCaseNum(null);
						} else {
							vo.setCaseNum(tempCaNum);
						}
					}
					// 处理 生效法律文书确定的义务
					if (null != vo.getLiability()) {
						// 替换特殊字符
						String tempLaNum = StringUtils.removeBlank(vo.getLiability()).replace("（", "(");
						tempLaNum = tempLaNum.replace("）", ")");
						tempLaNum = tempLaNum.replace("^~^", "");
						// tempLaNum = tempLaNum.replace("空", "");
						// tempLaNum = tempLaNum.replace("无", "");
						if ("".equals(tempLaNum) || "无".equals(tempLaNum.trim()) || "空".equals(tempLaNum.trim())
								|| "略".equals(tempLaNum.trim())) {
							vo.setLiability(null);
						} else {
							vo.setLiability(tempLaNum);
						}
					}
					// 做出执行依据单位
					out: if (null != vo.getDepartment()) {
						String tempDepartment = StringUtils.removeBlank(vo.getDepartment()).replace("（", "(");
						tempDepartment = tempDepartment.replace("）", ")");
						tempDepartment = tempDepartment.replace("^~^", "");
						// tempDepartment = tempDepartment.replace("空", "");
						// tempDepartment = tempDepartment.replace("无", "");
						tempDepartment = tempDepartment.replace("*", "");

						if (tempDepartment.trim().length() == 1 || "".equals(tempDepartment)
								|| "空".equals(tempDepartment) || "无".equals(tempDepartment)) {
							vo.setDepartment(null);
							break out;
						} else if (StringUtils.isNumeric(tempDepartment.trim())) {
							vo.setDepartment(null);
							break out;
						} else {
							// 处理单个"("
							if ((StringUtils.countCharacter(tempDepartment, "\\(") == 1
									&& StringUtils.countCharacter(tempDepartment, "\\)") == 0)) {
								tempDepartment = tempDepartment.replace("(", "");
							}
							// 处理单个")"
							if ((StringUtils.countCharacter(tempDepartment, "\\)") == 1
									&& StringUtils.countCharacter(tempDepartment, "\\(") == 0)) {
								tempDepartment = tempDepartment.replace(")", "");
							}
							vo.setDepartment(tempDepartment);
						}
					}

					if (null != vo.getOrgCode()) {
						String tempOrgCode = vo.getOrgCode();
						if (StringUtils.isHasHanZi(tempOrgCode) || tempOrgCode.trim().length() < 9
								|| "0".equals(tempOrgCode) || "-".equals(tempOrgCode) || "00000000".equals(tempOrgCode)
								|| "00000000-0".equals(tempOrgCode) || "12345678".equals(tempOrgCode)
								|| tempOrgCode.contains("*")) {
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

					if (!StringUtils.isNull(vo.getLiability())) {
						vo.setLiability(vo.getLiability().replace("^~^", ""));
					}
					if (!StringUtils.isNull(vo.getLiability())) {
						if ("略".equals(vo.getLiability()) || "无".equals(vo.getLiability())
								|| "空".equals(vo.getLiability())) {
							vo.setLiability(null);
						}
						vo.setLiability(vo.getLiability().replace("^~^", ""));
					}

					// 设置新的VO
					vo_new = new WholeCourtVO();

					// 设置摘要
					if (null != vo.getPersonName()) {
						String summary = vo.getPersonName() + ";执行法院: " + vo.getCourtName() + ";案号：" + vo.getCaseNum()
								+ ";立案时间：" + vo.getOpenTime() + ";履行的义务： " + vo.getLiability();
						summary = summary.replace("null", "");
						if (null != summary && summary.length() > 100) {
							vo_new.setSummary(summary.substring(0, 100));
						} else {
							vo_new.setSummary(summary);
						}
					}
					if (null != vo.getCompany()) {
						String summary = vo.getCompany() + ";执行法院: " + vo.getCourtName() + ";案号：" + vo.getCaseNum()
								+ ";立案时间：" + vo.getOpenTime() + ";履行的义务： " + vo.getLiability();
						summary = summary.replace("null", "");
						if (null != summary && summary.length() > 100) {
							vo_new.setSummary(summary.substring(0, 98) + "...");
						} else {
							vo_new.setSummary(summary);
						}
					}

					// 2 表示失信
					vo_new.setSource(2);
					// ------------------------------------------------默认
					// 第一批批二批默认无效
					vo_new.setFlag(3);

					// 立案时间
					if (null != vo.getOpenTime()) {
						vo_new.setOpenTime(DateUtils.toYMDOfChaStr(vo.getOpenTime().toString()));
					}
					if (null != vo.getPublishDate()) {
						vo_new.setPublishDate(DateUtils.toYMDOfChaStr(vo.getPublishDate().toString()));
					}
					// 采集时间
					vo_new.setCollectDate(DateUtils.toYMDOfChaStr_ES(vo.getCollectDate()));
					// 年龄

					if (!StringUtils.isNull(vo.getAge())) {
						if (StringUtils.isNumeric(vo.getAge())) {
							if (Integer.valueOf(vo.getAge()) > 0 && Integer.valueOf(vo.getAge()) < 120) {
								vo_new.setAge(Integer.valueOf(vo.getAge()));
							} else {
								vo_new.setAge(null);
							}
						} else {
							vo_new.setAge(null);
							log.error("年龄异常:" + key1);
							log.error("年龄异常:" + file.getPath());
							log.error(temJson);
						}
					}

					vo_new.setArea(vo.getArea());
					vo_new.setCaseNum(vo.getCaseNum());
					vo_new.setCity(vo.getCity());
					vo_new.setCompany(vo.getCompany());
					// vo_new.setCompanyId(vo.getCompanyId());
					vo_new.setCourtName(vo.getCourtName());
					vo_new.setDepartment(vo.getDepartment());
					vo_new.setDetail(vo.getDetail());
					vo_new.setDetailLink(vo.getDetailLink());
					vo_new.setExecuteNum(vo.getExecuteNum());
					// vo_new.setFollow(vo.getFollow());
					vo_new.setGender(vo.getGender());
					vo_new.setIdNum(vo.getIdNum());
					vo_new.setLiability(vo.getLiability());
					vo_new.setOrgCode(vo.getOrgCode());
					vo_new.setPerformance(vo.getPerformance());
					vo_new.setPersonName(vo.getPersonName());
					vo_new.setProvince(vo.getProvince());
					// vo_new.setRepId(vo.getRepId());
					vo_new.setRepName(vo.getRepName());

					vo_new.setKey(key1);

					if (countLen % 10000 == 0) {
						filename = "/temp" + countLen + ".log";
						fileS = new File("D:/lm/log/temp/失信/20150702/shixin500000" + filename);
						if (!fileS.exists()) {
							fileS.createNewFile();
						} else {
							fileS.delete();
							fileS = new File("D:/lm/log/temp/失信/20150702/shixin500000" + filename);
						}
					}

//					if (countLen % 10000 == 0) {
//						filename = "/breakfaith" + countLen + ".log";
//						// targetFDir
//						fileS = new File(targetFDir + filename);
//
//						// fileS=new
//						// File("D:/lm/log/temp/被执行人/112150014-113345749/data1"+filename);
//						if (!fileS.exists()) {
//							fileS.createNewFile();
//							fw = new BufferedWriter(
//									new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from)); // 指定编码格式，以免读取时中文字符异常
//						} else {
//							fileS.delete();
//							fileS = new File(targetFDir + filename);
//							fw = new BufferedWriter(
//									new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from)); // 指定编码格式，以免读取时中文字符异常
//							// fileS=new
//							// File("D:/lm/log/temp/被执行人/112150014-113345749/data1"+filename);
//						}
//					}

					System.out.println(countLen);
					LogUtils.writerDataToLog(fileS, gs.toJson(vo_new));

					// content = JsonObject.fromJson(gs.toJson(vo_new));
					//
					// documents.add(JsonDocument.create(key1, content));

					countLen++;
					System.out.println(countLen);
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

	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public String formatDate(String date) throws ParseException {
		String result = null;
		SimpleDateFormat sf = null;
		if (null != date) {
			// yyyy年MM月dd日 情况
			if (date.contains("年") && date.contains("月")) {
				result = date;
				return result;
			}
			String yyymmdd = "^[12]\\d{3}(0\\d|1[0-2])([0-2]\\d|3[01])$";// yyyyMMdd
			String yyyy_mm_dd = "^[0-9]{4}-[0-9]{1,}-[0-9]{1,}$";// yyyy-MM-dd
			String yyyy = "^\\d{4}\\/\\d{1,}\\/\\d{1,}$";// yyyy/MM/dd
			// yyyyMMdd 情况
			Pattern pattern = Pattern.compile(yyymmdd);
			Matcher matcher = pattern.matcher(date);
			if (matcher.matches()) {
				sf = new SimpleDateFormat("yyyyMMdd");
				Date d = sf.parse(date);
				sf = null;
				sf = new SimpleDateFormat("yyyy年MM月dd日");
				result = sf.format(d);
				sf = null;
			} else {
				// yyyy-MM-dd 情况
				pattern = Pattern.compile(yyyy_mm_dd);
				matcher = pattern.matcher(date);
				if (matcher.matches()) {
					sf = new SimpleDateFormat("yyyy-MM-dd");
					Date d = sf.parse(date);
					sf = null;
					sf = new SimpleDateFormat("yyyy年MM月dd日");
					result = sf.format(d);
					sf = null;
				} else {
					// yyyy/MM/dd 情况
					pattern = Pattern.compile(yyyy);
					matcher = pattern.matcher(date);
					if (matcher.matches()) {
						sf = new SimpleDateFormat("yyyy/MM/dd");
						Date d = sf.parse(date);
						sf = null;
						sf = new SimpleDateFormat("yyyy年MM月dd日");
						result = sf.format(d);
						sf = null;
					} else {
						int index_xie = date.indexOf("/");
						int index_mao = date.indexOf(":");
						if (4 == index_xie && -1 != index_mao) {
							String newDate = date.substring(0, date.indexOf(" "));
							// yyyy/MM/dd 情况
							pattern = Pattern.compile(yyyy);
							matcher = pattern.matcher(newDate);
							if (matcher.matches()) {
								sf = new SimpleDateFormat("yyyy/MM/dd");
								Date d = sf.parse(newDate);
								sf = null;
								sf = new SimpleDateFormat("yyyy年MM月dd日");
								result = sf.format(d);
								sf = null;
							}
						}
					}
				}
			}
		}
		return result;
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
