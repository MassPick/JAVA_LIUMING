package cn.com.szgao.enterprise;

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

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.ChangeVO;
import cn.com.szgao.dto.EnterpriseListVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.HolderDetailDtlVO;
import cn.com.szgao.dto.HolderDetailVO;
import cn.com.szgao.dto.HolderVO;
import cn.com.szgao.dto.MainManagerVO;
import cn.com.szgao.dto.RemarkVO;
import cn.com.szgao.dto.TempHolderVO;
import cn.com.szgao.dto.TempMainManagerVO;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.PostgresqlUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.AdministrationUtils;
import cn.com.szgao.wash.data.DataUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 深圳
 * 
 * @author xiongchangyi
 *
 */
public class FileIntoDataBase2pType {
	public FileIntoDataBase2pType() {
	}

	public FileIntoDataBase2pType(Logger log) {
		this.log = log;
	}

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

	// 日志对象
	private Logger log;
	// 根据字符串生成UUID对象
	NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	/**
	 * 股东数组
	 */
	JSONArray holderArray = null;
	/**
	 * 股东详情数组
	 */
	JSONArray holderDetArray = null;
	List<String> beforeList = new ArrayList<String>();
	List<String> afterList = new ArrayList<String>();
	/**
	 * 公司ID集合，
	 */
	List<String> companyIdList = new ArrayList<String>();
	int count = 0;
	// 无基本信息统计数据
	private int basicSum = 0;
	// 无注册号统计数据
	private int regSum = 0;

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
			System.out.println("数量:" + count + "---线程名" + Thread.currentThread().getName());
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
					System.out.println("数量:" + count + "---线程名" + Thread.currentThread().getName());
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

	@SuppressWarnings("rawtypes")
	private void readFileByLines(File file, int startNum) throws Exception {
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

		// File fileS = new File("D:/lm/log/工商-重庆市-0005A.txt");
		File fileS = new File("D:/lm/log/工商-广州市-0007A.txt");
		String encoding_from1 = "UTF-8";
		BufferedWriter fw = null;
		try {
			if (!fileS.exists()) {
				try {
					fileS.createNewFile();
				} catch (IOException e) {
				}
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
			} else {
				// fileS.delete();
				// fileS = new File("D:/lm/log/b13013911224A.log");
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		while ((tempT = reader.readLine()) != null) {

			/**
			 * 股东信息
			 */
			List<HolderVO> holder = new ArrayList<HolderVO>();
			/**
			 * 股东详情
			 */
			List<HolderDetailVO> holderDetail = new ArrayList<HolderDetailVO>();
			/**
			 * 变更信息
			 */
			List<ChangeVO> change = new ArrayList<ChangeVO>();
			/**
			 * 主要成员信息
			 */
			List<MainManagerVO> mainManager = new ArrayList<MainManagerVO>();

			EnterpriseVO enterVO = new EnterpriseVO();
			EnterpriseVO enterVOT = new EnterpriseVO();

			readNum++;
			if (readNum < startNum) {
				continue;
			}
			System.out.println("-->>>>>>> " + (readNum));
			if (tempT == null || tempT == "") {
				continue;
			}

			String doString = tempT.toString();
			JSONObject obj = null;
			// System.out.println("in..."+doString);
			try {
				obj = JSONObject.fromObject(doString);
			} catch (Exception e) {
				log.info(e);
				return;
			}

			// if(null!=doString){
			// if(doString.contains("create_times")){
			// String
			// tm=doString.substring(doString.indexOf("create_times")+"create_times".length()+2,
			// endIndex)
			// }
			// }

			// 得到生成时间
			String createTime = getCreateTime(obj);

			JSONObject jsobj = null;// datas
			if (null != obj) {
				if (!obj.containsKey("datas")) {
					regSum += 1;
					log.info("basicSum: " + basicSum + "---线程名" + file.getPath());
					return;
				}
				jsobj = obj.getJSONObject("datas");
			}

			String sqlS = "SELECT count(1) from cout_etp_type_t WHERE 1=1 ";
			String sqlInsert = null;
			PreparedStatement stmtInsert = null;

			String lev1 = null;
			String lev2 = null;
			String lev3 = null;
			String lev4 = null;
			String lev5 = null;
			String lev6 = null;

			

			Iterator changeIter = jsobj.keys();
			while (changeIter.hasNext()) {
				String sqlST = "";
				// 第一层
				lev1 = changeIter.next().toString();
				sqlST += " AND lev1='" + lev1 + "' ";
				
				String before = null;
				JSONArray beforeArray = null;
				try {
					beforeArray = jsobj.getJSONArray(lev1);
					tolev(sqlS + sqlST, 1, lev1, lev2, lev3, lev4, lev5);
					
					if (!beforeArray.isEmpty()) {
						JSONObject beforeObj = beforeArray.getJSONObject(0);
						Iterator it = beforeObj.keys();
						while (it.hasNext()) {
							lev2 = it.next().toString();
							String sqlST2 = "";
							sqlST2  = " AND lev2='" + lev2 + "' ";
							
							JSONArray beforeArray2 = null;
							try {
								beforeArray2 = jsobj.getJSONArray(lev2);
								System.out.println("2-->"+beforeArray2);
								tolev(sqlS + sqlST+ sqlST2, 2, lev1, lev2, lev3, lev4, lev5);
								
								if (!beforeArray2.isEmpty()) {
									JSONObject beforeObj2 = beforeArray2.getJSONObject(0);
									
									Iterator it2 = beforeObj2.keys();
									while (it2.hasNext()) {
										lev3 = it2.next().toString();
										String sqlST3 = "";
										sqlST3 = " AND lev3='" + lev3 + "' ";
										JSONArray beforeArray3 = null;
										try {
											beforeArray3 = jsobj.getJSONArray(lev2);
											tolev(sqlS + sqlST+ sqlST2+ sqlST3, 3, lev1, lev2, lev3, lev4, lev5);
											
											if (!beforeArray3.isEmpty()) {
												JSONObject beforeObj4 = beforeArray3.getJSONObject(0);
												
												Iterator it3 = beforeObj4.keys();
												while (it3.hasNext()) {
													lev4 = it3.next().toString();
													String sqlST4 = "";
													sqlST4 = " AND lev4='" + lev4 + "' ";
													JSONArray beforeArray5 = null;
													try {
														beforeArray5 = jsobj.getJSONArray(lev4);
														tolev(sqlS + sqlST+sqlST2+sqlST3+sqlST4, 4, lev1, lev2, lev3, lev4, lev5);
													} catch (Exception e) {
														tolev(sqlS + sqlST+sqlST2+sqlST3+sqlST4, 4, lev1, lev2, lev3, lev4, lev5);
														sqlST4="";
//														break;
													}
													
													
												}
											}
											
										} catch (Exception e) {
											tolev(sqlS + sqlST+ sqlST2+ sqlST3, 3, lev1, lev2, lev3, lev4, lev5);
											sqlST3="";
//											break;
										}
										
									}
								}
								
							} catch (Exception e) {
								
								tolev(sqlS + sqlST+ sqlST2, 2, lev1, lev2, lev3, lev4, lev5);
								sqlST2="";
//								break;
							}
							
						}
					}

				} catch (Exception e) {
					// 不是json
					
					tolev(sqlS + sqlST, 1, lev1, lev2, lev3, lev4, lev5);
					sqlST="";
					break;
					
				}

			}


		}

	}
	
	
	private static void tolev(String sqlS,int level,String lev1,String lev2,String lev3,String lev4,String lev5){
		
		System.out.println(sqlS);
		PreparedStatement stmtInsert = null;
		String sqlInsert=null;
		String te = PostgresqlUtils.getValueFromSqlCon(conn, sqlS);
		
		int temp1 = Integer.valueOf(te);
		if (temp1 == 0) {

			try {
				// conn.setAutoCommit(false);
				if(level==1){
					sqlInsert = "INSERT INTO   cout_etp_type_t  (lev1)VALUES(?)   ";
				}else if(level==2){
					sqlInsert = "INSERT INTO   cout_etp_type_t  (lev1,lev2)VALUES(?,?)   ";
				}else if(level==3){
					sqlInsert = "INSERT INTO   cout_etp_type_t  (lev1,lev2,lev3)VALUES(?,?,?)   ";
				}else if(level==4){
					sqlInsert = "INSERT INTO   cout_etp_type_t  (lev1,lev2,lev3,lev4)VALUES(?,?,?,?)   ";
				}else if(level==5){
					sqlInsert = "INSERT INTO   cout_etp_type_t  (lev1,lev2,lev3,lev4,lev5)VALUES(?,?,?,?,?)   ";
				}
				
				
				stmtInsert = connI.prepareStatement(sqlInsert);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				if(level==1){
					stmtInsert.setString(1, lev1);
				}else if(level==2){
					stmtInsert.setString(1, lev1);
					stmtInsert.setString(2, lev2);
				}else if(level==3){
					stmtInsert.setString(1, lev1);
					stmtInsert.setString(2, lev2);
					stmtInsert.setString(3, lev3);
				}else if(level==4){
					stmtInsert.setString(1, lev1);
					stmtInsert.setString(2, lev2);
					stmtInsert.setString(3, lev3);
					stmtInsert.setString(4, lev4);
				}else if(level==5){
					stmtInsert.setString(1, lev1);
					stmtInsert.setString(2, lev2);
					stmtInsert.setString(3, lev3);
					stmtInsert.setString(4, lev4);
					stmtInsert.setString(4, lev5);
				}
				
				
				stmtInsert.executeUpdate();

			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}
	}

	static Connection conn = null;

	static {
		try {
			conn = PostgresqlUtilsLocal.getConnection();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	static Connection connU = null;

	static {
		try {
			connU = PostgresqlUtilsLocal.getConnection();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	static Connection connI = null;

	static {
		try {
			connI = PostgresqlUtilsLocal.getConnection();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * 处理公司基本信息
	 * 
	 * @param baseObj
	 *            处理的数据
	 * @param companyId
	 *            公司ID
	 * @param jsonArray
	 *            json对象
	 * @throws ParseException
	 */
	@SuppressWarnings("rawtypes")
	public EnterpriseVO insertBaseInfo(JSONObject baseObj, String companyId, JSONObject obj) throws Exception {
		// 企业对象
		EnterpriseVO enterVO = new EnterpriseVO();
		;
		// 字段与页面标签映射对象、表名与标签
		RemarkVO remarkVO = new RemarkVO();
		remarkVO.setBucketName("基本信息");
		// 对象的json对象
		JsonObject jsonObject = null;
		JsonDocument doc = null;
		// key的遍历器
		Iterator keyIter = baseObj.keys();
		while (keyIter.hasNext()) {
			String key = keyIter.next().toString();
			String value = baseObj.get(key).toString();
			if (null != value && "null".equals(value)) {
				value = null;
			}
			// 贵州
			if ("名称".equals(key) && null == value) {
				value = obj.get("compname").toString();
			}
			if (key.endsWith("注册号")) {
				enterVO.setRegNum(value);
				// 获得省市县
				String pccityArray[] = listCountryCityProvince(value);
				if (null != pccityArray[0]) {
					enterVO.setProvince(pccityArray[0]);// 省
				}
				if (null != pccityArray[1]) {
					enterVO.setCity(pccityArray[1]);// 市
				}
				if (null != pccityArray[2]) {
					enterVO.setArea(pccityArray[2]);// 县
				}
			} else if ("名称".equals(key)) {
				enterVO.setCompany(value);
			} else if ("统一社会信用代码".equals(key)) {
				enterVO.setCreditCode(value);
			} else if (key.contains("类型")) {
				enterVO.setType(value);
			} else if ("法定代表人".equals(key) || "负责人".equals(key) || "投资人".equals(key) || "经营者".equals(key)
					|| "执行事务合伙人".equals(key) || "股东".equals(key) || key.startsWith("首席") || key.startsWith("姓名")) {
				if (null != enterVO.getCompany()) {
					remarkVO.setLegalRep(key);
					enterVO.setLegalRep(value);
				}
			} else if ("住所".equals(key) || key.contains("场所")) {
				if (null != value) {
					enterVO.setLocation(value.replace(" ", ""));
				}
			} else if ("注册资本".equals(key) || "成员出资总额".equals(key)) {
				remarkVO.setRegCapital(key);
				enterVO.setRegCapital(value);
			} else if ("成立日期".equals(key) || "注册日期".equals(key)) {
				enterVO.setRegDate(DateUtils.toYMDOfChaStr_ESZZ2(value));
			} else if (key.contains("期限自")) {
				remarkVO.setStartTime(key);
				enterVO.setStartTime(DateUtils.toYMDOfChaStr_ESZZ2(value));
			} else if (key.contains("期限至")) {
				remarkVO.setEndTime(key);
				enterVO.setEndTime(DateUtils.toYMDOfChaStr_ESZZ2(value));
			} else if (key.contains("范围")) {
				enterVO.setScope(value);

			} else if ("登记机关".equals(key)) {
				enterVO.setRegOffice(value);
			} else if ("核准日期".equals(key)) {
				enterVO.setApproveDate(DateUtils.toYMDOfChaStr_ESZZ2(value));
			} else if ("登记状态".equals(key) || "经营状态".equals(key)) {
				enterVO.setRegState(value);
			} else if ("吊销日期".equals(key) || "注销日期".equals(key)) {
				enterVO.setRevokeDate(DateUtils.toYMDOfChaStr_ESZZ2(value));
			} else if ("组成形式".equals(key)) {
				enterVO.setComposition(value);
			}
		}
		// 通过注册号没有查询到行政区的情况
		if (null == enterVO.getProvince() && null == enterVO.getCity()) {
			if (null != enterVO.getLocation()) {
				if (0 != enterVO.getLocation().hashCode()) {
					// 通过企业的住所字段获得行政区
					String admin[] = doAdmin(u.enterp2(enterVO.getLocation()));
					enterVO.setProvince(admin[0]);
					enterVO.setCity(admin[1]);
					enterVO.setArea(admin[2]);
				}
			}
		}
		if (null == enterVO.getProvince() && null == enterVO.getCity()) {
			if (null != enterVO.getRegOffice()) {
				if (0 != enterVO.getRegOffice().hashCode()) {
					// 通过 登记机关 字段获得行政区
					String admin[] = doAdmin(u.enterp2(enterVO.getRegOffice()));
					enterVO.setProvince(admin[0]);
					enterVO.setCity(admin[1]);
					enterVO.setArea(admin[2]);
				}
			} else {
				// 通过企业名称
				if (null != enterVO.getCompany()) {
					if (0 != enterVO.getCompany().hashCode()) {
						String array[] = doAdmin(u.enterp(enterVO.getCompany()));
						enterVO.setProvince(array[0]);
						enterVO.setCity(array[1]);
						enterVO.setArea(array[2]);
					}
				} else {
					enterVO.setProvince(obj.get("province").toString());
				}
			}
		}
		JSONObject jsobj = obj.getJSONObject("datas");
		JSONArray jyfwArray = null;
		// 从大对象里面取经营范围
		if (jsobj.containsKey("经营范围信息")) {
			jyfwArray = jsobj.getJSONArray("经营范围信息");
		} else if (jsobj.containsKey("业务范围信息")) {
			jyfwArray = jsobj.getJSONArray("业务范围信息");
		}
		if (null != jyfwArray) {
			JSONObject jyfwObject = jyfwArray.getJSONObject(0);
			Iterator it = jyfwObject.keys();
			while (it.hasNext()) {
				enterVO.setScope(jyfwObject.get(it.next().toString()).toString());
			}
		}
		// 设置备注对象
		enterVO.setRemark(remarkVO);
		if (obj.containsKey("url")) {
			String url = obj.getString("url");
			if (null != url && 0 != url.hashCode()) {
				enterVO.setUrl(url);
			}
		}

		/*
		 * jsonObject = JsonObject.fromJson(gs.toJson(enterVO)); enterVO = null;
		 * doc = JsonDocument.create(companyId, jsonObject);
		 */
		// createJsonDocument("etp_t", doc);// etp_t
		jsonObject = null;
		return enterVO;
	}

	/**
	 * 写数据
	 */
	public void createJsonDocument(String name, JsonDocument doc) {
		if (null == doc) {
			return;
		}
		Bucket bucket = ClusterUtil.commonBucket(name);
		while (true) {
			try {
				log.info("json:" + doc.toString());
				bucket.upsert(doc, 1, TimeUnit.MINUTES);
				break;
			} catch (Exception e) {
				System.out.println("超时:" + e.getMessage());
				System.out.println("重写:" + doc.toString());
			}
		}
	}

	/**
	 * 股东信息
	 * 
	 * @param holderArray
	 *            股东信息数组
	 * @param bucketName前台标签名称
	 * @param companyId
	 *            公司ID
	 */
	@SuppressWarnings("rawtypes")
	public void holder(JSONArray holderArray, String bucketName, String companyId) {
		if (null != holderArray && holderArray.size() > 0) {
			HolderVO holderVO = null;
			RemarkVO remarkVO = null;
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			for (int i = 0; i < holderArray.size(); i++) {
				holderVO = new HolderVO();
				holderVO.setCompanyId(companyId);
				remarkVO = new RemarkVO();
				remarkVO.setBucketName(bucketName);// 前台标签名称
				JSONObject tuziObj = holderArray.getJSONObject(i);
				Iterator tuziIter = tuziObj.keys();
				while (tuziIter.hasNext()) {
					String tuziKey = tuziIter.next().toString();
					if ("股东".equals(tuziKey) || "姓名".equals(tuziKey) || "合伙人".equals(tuziKey)
							|| tuziKey.contains("股东/发起人名称") || tuziKey.contains("股东/发起人")
							|| tuziKey.contains("股东/发起人信息") || tuziKey.contains("股东（发起人）") || tuziKey.contains("股东/出资人")
							|| tuziKey.contains("股东/合伙人信息") || tuziKey.contains("股东/合伙人信息") || "发起人".equals(tuziKey)
							|| "出资人".equals(tuziKey) || "合伙人信息".equals(tuziKey) || "投资人".equals(tuziKey)
							|| "投资人名称".equals(tuziKey) || "发起人信息".equals(tuziKey)) {
						remarkVO.setHolder(tuziKey);
						holderVO.setHolder(tuziObj.get(tuziKey).toString());
					}
					// 合伙人类型 别名：股东类型、出资人类型、发起人类型、股东/发起人类型
					else if ("股东类型".equals(tuziKey) || "出资人类型".equals(tuziKey) || "合伙人类型".equals(tuziKey)
							|| "股东（发起人）类型".equals(tuziKey) || "发起人类型".equals(tuziKey) || "投资人类型".equals(tuziKey)
							|| "股东/发起人类型".equals(tuziKey)) {
						remarkVO.setType(tuziKey);
						holderVO.setType(tuziObj.get(tuziKey).toString());
					}
					// 证照/证件类型 别名：证照证件类型
					else if ("证照/证件类型".equals(tuziKey) || "证照证件类型".equals(tuziKey)) {
						holderVO.setLicenseType(tuziObj.get(tuziKey).toString());
					}
					// 证照/证件号码 别名：证照证件号码
					else if ("证照/证件号码".equals(tuziKey) || "证照证件号码".equals(tuziKey)) {
						holderVO.setLicenseNum(tuziObj.get(tuziKey).toString());
					}
					// equityPart 出资方式
					else if ("出资方式".equals(tuziKey) || "投资方式".equals(tuziKey)) {
						holderVO.setEquityPart(tuziObj.get(tuziKey).toString());
					}
				}
				holderVO.setRemark(remarkVO);
				// 写库
				jsonObject = JsonObject.fromJson(gs.toJson(holderVO));
				doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
				// createJsonDocument("etp_holder_e", doc);// etp_holder_e
				jsonObject = null;
				doc = null;
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
							// yyyy/MM/dd hh:mm:ss 情况
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
						// 处理：Mon Apr 22 00:00:00 CST 2013格式
						if (date.contains("CST")) {
							sf = new SimpleDateFormat("yyyy年MM月dd日");
							@SuppressWarnings("deprecation")
							Date dt = new Date(date);
							result = sf.format(dt);
							sf = null;
							dt = null;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 处理主要人员信息
	 * 
	 * @param mholderArray
	 *            处理的数据
	 * @param bucketName
	 *            前台标签
	 * @param companyId
	 *            公司ID
	 */
	@SuppressWarnings("rawtypes")
	public List<MainManagerVO> insertMholder(JSONArray mholderArray, String bucketName, String companyId) {

		// List<MainManagerVO> mainManager=new ArrayList<MainManagerVO>();
		List<MainManagerVO> maiList2 = new ArrayList<MainManagerVO>();

		// 主要人员信息 别名：主要人员及信息、参加经营的家庭成员姓名
		if (!mholderArray.isEmpty()) {
			ObjectMapper mapper = new ObjectMapper();
			// 遍历主要人员信息
			Iterator iter = mholderArray.iterator();
			JsonNode node = null;
			// 主要人员信息对象
			Object mholderObject = null;
			// 遍历主要人员信息对象
			Iterator<String> keyIter = null;
			MainManagerVO mainVO = null;
			RemarkVO remarkVO = null;
			// 主要人员信息的临时实体对象
			TempMainManagerVO tempVO = null;
			// 对象的json对象
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			try {
				while (iter.hasNext()) {
					// 主要人员信息的临时实体对象
					tempVO = new TempMainManagerVO();
					tempVO.setCompanyId(companyId);
					/*
					 * remarkVO = new RemarkVO();
					 * remarkVO.setBucketName(bucketName);
					 * tempVO.setRemarkVO(remarkVO); remarkVO = null;
					 */
					mholderObject = iter.next();
					node = mapper.readTree(mholderObject.toString());
					keyIter = node.getFieldNames();
					while (keyIter.hasNext()) {
						String key = keyIter.next().toString();
						String value = node.get(key).getTextValue();
						// 姓名 别名：姓名4
						if ("姓名".equals(key)) {
							if ("".equals(value) || null == value || "null".equals(value) || " ".equals(value)) {
								continue;
							}
							tempVO.setManagerName1(value);
						} else if ("姓名4".equals(key)) {
							if ("".equals(value) || null == value || "null".equals(value) || " ".equals(value)) {
								continue;
							}
							tempVO.setManagerName2(value);
						} else if ("姓名7".equals(key)) {
							if ("".equals(value) || null == value || "null".equals(value) || " ".equals(value)) {
								continue;
							}
							tempVO.setManagerName3(value);
						} else if ("职务".equals(key)) {
							if ("".equals(value) || null == value || "null".equals(value) || " ".equals(value)) {
								continue;
							}
							tempVO.setPosition1(value);
						} else if ("职务5".equals(key)) {
							if ("".equals(value) || null == value || "null".equals(value) || " ".equals(value)) {
								continue;
							}
							tempVO.setPosition2(value);
						} else if ("职务8".equals(key)) {
							if ("".equals(value) || null == value || "null".equals(value) || " ".equals(value)) {
								continue;
							}
							tempVO.setPosition3(value);
						}
					} // 遍历json对象里面的所有key
						// 写库
					if (null != tempVO.getManagerName1() && null != tempVO.getPosition1()) {
						mainVO = new MainManagerVO();
						mainVO.setRemark(tempVO.getRemarkVO());
						mainVO.setCompanyId(tempVO.getCompanyId());
						mainVO.setManagerName(tempVO.getManagerName1());
						mainVO.setPosition(tempVO.getPosition1());
						maiList2.add(mainVO);

						/*
						 * jsonObject = JsonObject.fromJson(gs.toJson(mainVO));
						 * mainVO = null; doc =
						 * JsonDocument.create(UUID.randomUUID().toString(),
						 * jsonObject); //createJsonDocument("etp_mhold_e",
						 * doc);// etp_mhold_e jsonObject = null; //
						 * bucket=null; doc = null;
						 */
					}
					if (null != tempVO.getManagerName2() && null != tempVO.getPosition2()) {
						mainVO = new MainManagerVO();
						mainVO.setRemark(tempVO.getRemarkVO());
						mainVO.setCompanyId(tempVO.getCompanyId());
						mainVO.setManagerName(tempVO.getManagerName2());
						mainVO.setPosition(tempVO.getPosition2());
						maiList2.add(mainVO);
						/*
						 * jsonObject = JsonObject.fromJson(gs.toJson(mainVO));
						 * mainVO = null; doc =
						 * JsonDocument.create(UUID.randomUUID().toString(),
						 * jsonObject);
						 * 
						 * Bucket bucket =
						 * ClusterUtil.commonBucket("etp_mhold_e");
						 * bucket.upsert(doc);
						 * 
						 * //createJsonDocument("etp_mhold_e", doc);//
						 * etp_mhold_e jsonObject = null; // bucket=null; doc =
						 * null;
						 */
					}
					if (null != tempVO.getManagerName3() && null != tempVO.getPosition3()) {
						mainVO = new MainManagerVO();
						mainVO.setRemark(tempVO.getRemarkVO());
						mainVO.setCompanyId(tempVO.getCompanyId());
						mainVO.setManagerName(tempVO.getManagerName3());
						mainVO.setPosition(tempVO.getPosition3());
						maiList2.add(mainVO);

						/*
						 * jsonObject = JsonObject.fromJson(gs.toJson(mainVO));
						 * mainVO = null; doc =
						 * JsonDocument.create(UUID.randomUUID().toString(),
						 * jsonObject);
						 * 
						 * Bucket bucket =
						 * ClusterUtil.commonBucket("etp_mhold_e");
						 * bucket.upsert(doc);
						 * 
						 * //createJsonDocument("etp_mhold_e", doc);//
						 * etp_mhold_e jsonObject = null; // bucket=null; doc =
						 * null;
						 */
					}
					mainVO = null;
				}
				tempVO = null;
			} catch (JsonProcessingException e) {
				log.info(e);
			} catch (IOException e) {
				log.info(e);
			} finally {
				mapper = null;
				iter = null;
				node = null;
				mholderObject = null;
				keyIter = null;
			}
		}
		return maiList2;

	}

	/**
	 * 处理变更信息 存在点击“详情” ，出现表格的形式
	 * 
	 * @param changArray
	 *            处理的数据
	 * @param before
	 *            变更前内容字符串
	 * @param after
	 *            变更后内容字符串
	 * @param companyId
	 *            公司ID
	 * @throws ParseException
	 */
	@SuppressWarnings("rawtypes")
	public List<ChangeVO> insertChange(JSONArray changArray, String companyId) throws ParseException {
		List<ChangeVO> change = new ArrayList<ChangeVO>();
		if (!changArray.isEmpty()) {
			RemarkVO remarkVO = null;
			// 变更对象
			ChangeVO changeVO = null;
			// 对象的json对象
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			int count = 0;
			for (int i = 0; i < changArray.size(); i++) {
				remarkVO = new RemarkVO();
				remarkVO.setBucketName("变更信息");
				changeVO = new ChangeVO();
				changeVO.setCompanyId(companyId);
				changeVO.setRemark(remarkVO);
				JSONObject obj = changArray.getJSONObject(i);
				Iterator iter = obj.keys();
				boolean flag = false;
				while (iter.hasNext()) {
					String key = iter.next().toString();
					String value = obj.get(key).toString();
					if (null != value && !"暂无数据".equals(value) && !"".equals(value) && !"null".equals(value)
							&& !"无".equals(value) && !"不公示".equals(value)) {
						if ("变更事项".equals(key)) {
							changeVO.setChangeEvent(value);
						} else if ("变更前内容".equals(key)) {
							// 当变更前信息里面的值为 详细 的时候
							if ("详细".equals(value.trim())) {
								flag = true;
								count += 1;
								if (count <= beforeList.size()) {
									changeVO.setChangeBefore(deleteMoreFuhao(beforeList.get(count - 1)));
								}
							} else {
								changeVO.setChangeBefore(deleteMoreFuhao(value));
							}
						} else if ("变更后内容".equals(key)) {
							// 不是详情
							if (!flag) {
								changeVO.setChangeAfter(deleteMoreFuhao(value));
							} else {
								changeVO.setChangeDate(DateUtils.toYMDOfChaStr_ESZZ2(value));
								if (count <= afterList.size()) {
									changeVO.setChangeAfter(deleteMoreFuhao(afterList.get(count - 1)));
								}
							}
						} else if ("变更日期".equals(key)) {
							if (!flag) {
								changeVO.setChangeDate(DateUtils.toYMDOfChaStr_ESZZ2(value));
							}

						}
					}
				}
				// 写库
				if (null != changeVO && null != changeVO.getChangeEvent()) {
					change.add(changeVO);

					// jsonObject = JsonObject.fromJson(gs.toJson(changeVO));
					// doc = JsonDocument.create(UUID.randomUUID().toString(),
					// jsonObject);

					// 写库
					// createJsonDocument("etp_event_item_e", doc);
					// bucket=null;
				}
				remarkVO = null;
				changeVO = null;
				jsonObject = null;
				doc = null;
			}
		}
		beforeList = null;
		beforeList = new ArrayList<String>();
		afterList = null;
		afterList = new ArrayList<String>();

		return change;
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
			bucket.upsert(doc);
		} catch (Exception e) {
			log.info(e);
			while (true) {
				try {
					bucket.upsert(doc);
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
	 * 去掉特殊符号
	 * 
	 * @param doTemp
	 * @return
	 */
	public String deleteMoreFuhao(String doTemp) {
		if (null == doTemp || doTemp == "") {
			return null;
		}
		return doTemp.replace("&nbsp", "").replace(";", "").replace("\r", "").replace("\t", "").replace("\n", "")
				.replace(" ", "").replace("\"", "");
	}

	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + "\n");

			// fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			// Log.error("写文件异常"+e.getMessage());
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

	/**
	 * 检查股东信息中是否包含股东详情 1 表示包含 0 表示不包含
	 * 
	 * @param tuziArray
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("rawtypes")
	public String holderConDel(JSONArray tuziArray) throws ParseException {
		String flag = "0";
		if (null != tuziArray && tuziArray.size() > 0) {
			for (int i = 0; i < tuziArray.size(); i++) {
				JSONObject tuziObj = tuziArray.getJSONObject(i);
				Iterator tuziIter = tuziObj.keys();
				while (tuziIter.hasNext()) {
					String key = tuziIter.next().toString();
					// String value = tuziObj.get(key).toString();
					// if(null != value && "null".equals(value))
					// {
					// value = null;
					// continue;
					// }

					if (key.contains("认缴")) {
						flag = "1";
						break;
					}

				}

			}

		}
		return flag;
	}

	/**
	 * 股东信息在一起时
	 * 
	 * @param tuziArray
	 * @param bucketName
	 * @param companyId
	 * @return
	 * @throws ParseException
	 */
	public List<TempHolderVO> getholderAndDetList(JSONArray tuziArray, String bucketName, String companyId)
			throws ParseException {
		List<TempHolderVO> getholderAndDetList = new ArrayList<TempHolderVO>();
		if (null != tuziArray && tuziArray.size() > 0) {
			// 股东对象
			HolderVO holderVO = null;
			// 股东详情对象
			HolderDetailVO holderDetVO = null;
			TempHolderVO tempVO = null;
			RemarkVO remarkVO = null;
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			for (int i = 0; i < tuziArray.size(); i++) {
				tempVO = new TempHolderVO();// 临时股东对象
				remarkVO = new RemarkVO();// 备注对象
				remarkVO.setBucketName(bucketName);// 前台标签名称
				JSONObject tuziObj = tuziArray.getJSONObject(i);
				Iterator tuziIter = tuziObj.keys();
				while (tuziIter.hasNext()) {
					String key = tuziIter.next().toString();
					String value = tuziObj.get(key).toString();
					if (null != value && "null".equals(value)) {
						value = null;

						// 是否可以无效？
						// continue;
					}
					// 判断是哪个字段
					if ("股东".equals(key) || "姓名".equals(key) || "合伙人".equals(key) || key.contains("股东/发起人名称")
							|| key.contains("股东/发起人") || key.contains("股东/发起人信息") || key.contains("股东（发起人）")
							|| key.contains("股东/出资人") || key.contains("股东/合伙人信息") || key.contains("股东/合伙人信息")
							|| "发起人".equals(key) || "出资人".equals(key) || "合伙人信息".equals(key) || "投资人".equals(key)
							|| "投资人名称".equals(key) || "发起人信息".equals(key)) {
						remarkVO.setHolder(key);
						tempVO.setHolder(value);

					}
					// 合伙人类型 别名：股东类型、出资人类型、发起人类型、股东/发起人类型
					else if ("股东类型".equals(key) || "出资人类型".equals(key) || "合伙人类型".equals(key) || "股东（发起人）类型".equals(key)
							|| "发起人类型".equals(key) || "投资人类型".equals(key) || "股东/发起人类型".equals(key)) {
						remarkVO.setType(key);
						tempVO.setType(value);
					}
					// 证照/证件类型 别名：证照证件类型
					else if ("证照/证件类型".equals(key) || "证照证件类型".equals(key)) {
						tempVO.setLicenseType(value);
					}
					// 证照/证件号码 别名：证照证件号码
					else if ("证照/证件号码".equals(key) || "证照证件号码".equals(key)) {
						tempVO.setLicenseNum(value);
					}
					// equityPart 出资方式
					else if ("出资方式".equals(key) || "投资方式".equals(key)) {
						tempVO.setEquityPart(value);
					}

					// 认缴额(万元）
					else if ("认缴额(万元)".equals(key) || "认缴额（万元）".equals(key)) {
						tempVO.setConCapital(value);
					}
					// 实缴额(万元）
					else if ("实缴额(万元)".equals(key) || "实缴额（万元）".equals(key)) {
						tempVO.setFactCapital(value);
					}

					// 认缴出资额 别名：认缴出资额(万元)
					// else if ("认缴出资额".equals(key) || "认缴出资额(万元)".equals(key)
					// || "认缴额（万元）".equals(key)
					// || "认缴出资额（万元）".equals(key) || "认缴额(万元)".equals(key)) {
					// tempVO.setSubcriCapital(value);
					// }
					else if ("认缴出资额".equals(key) || "认缴出资额(万元)".equals(key) || "认缴出资额（万元）".equals(key)) {
						tempVO.setSubcriCapital(value);
					}

					// 认缴出资方式 别名：出资方式、认缴明细
					else if ("认缴出资方式".equals(key) || "出资方式".equals(key) || "认缴明细".equals(key)) {
						tempVO.setConMethod(value);
					}
					// 认缴出资时间 别名：认缴出资日期
					else if ("认缴出资时间".equals(key) || "认缴出资日期".equals(key)) {
						tempVO.setConsidDate(formatDate(value));
					}
					// 实缴出资额 别名：实缴出资额(万元)、实缴明细
					// else if ("实缴出资额".equals(key) || "实缴出资额(万元)".equals(key)
					// || "实缴额(万元)".equals(key)
					// || "实缴明细".equals(key) || "实缴出资额（万元）".equals(key) ||
					// "实缴额（万元）".equals(key)) {
					// tempVO.setActualCapital(value);
					// }
					else if ("实缴出资额".equals(key) || "实缴出资额(万元)".equals(key) || "实缴明细".equals(key)
							|| "实缴出资额（万元）".equals(key)) {
						tempVO.setActualCapital(value);
					}
					// 实缴出方式 别名：实缴出资方式、出资方式6
					else if ("实缴出方式".equals(key) || "实缴出资方式".equals(key) || "出资方式6".equals(key)) {
						tempVO.setFactMethod(value);
					}
					// 实缴出时间 别名：实缴出资日期、实缴出资时间
					else if ("实缴出时间".equals(key) || "实缴出资日期".equals(key) || "实缴出资时间".equals(key)) {
						tempVO.setActualDate(formatDate(value));
					}

				}
				getholderAndDetList.add(tempVO);

			}
		}
		return getholderAndDetList;
	}

	/**
	 * 处理 //2015-09-02T04:07:01.658587+08:00
	 * 
	 * @param obj
	 * @return
	 */
	public String getCreateTime(JSONObject obj) {
		String jsbTime = null;
		if (null != obj) {
			if (obj.containsKey("create_times")) {
				jsbTime = (String) obj.get("create_times");
			}
		}
		// 2015-09-02T04:07:01.658587+08:00
		String temp = DateUtils.toYMDOfChaStr_ESZZ2(jsbTime);
		if (null == temp) {
			temp = DateUtils.getDateyyyyMMddhhmmssZZ();
		}
		return temp;
	}

}
