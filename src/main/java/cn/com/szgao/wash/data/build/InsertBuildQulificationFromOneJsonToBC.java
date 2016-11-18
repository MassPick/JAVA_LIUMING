package cn.com.szgao.wash.data.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.dto.BuildQulificationVO;
import cn.com.szgao.dto.BuildVO;
import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.dto.PrCiCouVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.wash.data.DataUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.PrCiCouText;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;
import rx.functions.Func1;

import rx.Observable;

/**
 * 将建设通资质写入CB库 1 先写建设通的旧数据 2 再建设通的新数据 3 再全国的旧数据
 * 
 * @author liuming
 * @Date 2016年8月25日 下午5:06:40
 */
public class InsertBuildQulificationFromOneJsonToBC {

	private static Logger log = LogManager.getLogger(InsertBuildQulificationFromOneJsonToBC.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(InsertBuildQulificationFromOneJsonToBC.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	static String usr = "postgres";
	static String psd = "615601.xcy*";

	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			PrCiCouText.connection = DriverManager.getConnection(url, usr, psd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DataUtils.initData();
		util.initData();
	}

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {

		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 文件
		// 说明：------------------------------------------------ 先入建设通，再建筑市场进行覆盖

		// File file = new File("C:/data/建设通和建设市场发布平台/建设通/主信息合并");

		// File file = new File("C:/data/建设通和建设市场发布平台/建筑市场发布平台/建筑市场监控1合并");

		// File file = new File("C:/data/建设通和建设市场发布平台/建筑市场发布平台/建筑市场平台");

		// File file1 = new File("C:/data/建设通和建设市场发布平台_新/建设通/主信息/主信息");//
		// --------------UTF-8编码
		// File file = new
		// File("C:/data/建设通和建设市场发布平台_新/建设通/主信息/主信息/jianshetong1/阿克苏地区金鼎水利水电工程有限责任公司.json");//--------------UTF-8编码

		// File file2 = new
		// File("C:/data/建设通和建设市场发布平台_新/建筑市场发布平台/建筑市场平台/建筑市场平台");//
		// ---------------
		// GB2312编码
		// File file2 = new
		// File("C:/data/建设通和建设市场发布平台_新/建筑市场发布平台/建筑市场平台/建筑市场平台/四川极力建筑劳务有限公司.json");//---------------
		// GB2312编码

		File file1 = new File("C:/data/建设通和建设市场发布平台_新/建设通/cbi_json_new");// --------------UTF-8编码

		Integer source = 0;// 1 全国建设市场监督
						   // 0 或null是建设通----

		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();

			show(file1, 0, "UTF-8", 0);
			// show(file2,1,"GB2312");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
			// log.info("路径：" + file2.getPath());
			log.info("开始时间--------------------" + formatter.format(startTime));
			log.info("结束时间--------------------" + formatter.format(endDate));
			log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
			log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
			log.info("Total : " + count);
			log.info("Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
			log.info("Speed : " + (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60))) + "个/分钟");
			log.info("Speed : "
					+ (float) (count / ((float) ((float) ((float) ((endTime - startTime) / 1000) / 60)) / 60)) + "个/秒");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static Bucket bucket = null;

	public static void readFile(File file, Integer source, String coding, int numLen) {
		int flag = 0;
		int countM = 0;
		int countNull = 0;
		int size = 1000;
		String tempCourtName = "";
		long startTime = System.currentTimeMillis();
		long startTime2 = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");

		BufferedReader reader = null;
		try {

			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(new FileInputStream(file), coding);
				// isr = new InputStreamReader(new FileInputStream(file),
				// "GB2312");
				// isr = new InputStreamReader(new FileInputStream(file),
				// "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "GB18030");
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			String temp = null;
			BuildVO vo = null;
			Gson gs = new Gson();
			JSONObject temJson = null;
			String key1 = "";

			List<JsonDocument> documents = new ArrayList<JsonDocument>();

			JsonObject content = null;
			int countLen = 0;
			StringBuffer sb = new StringBuffer();

			String te = null;
			while ((te = reader.readLine()) != null) {
				count++;
				System.out.println(count);
				if (count < numLen) {
					continue;
				}

				try {
					try {
						temJson = JSONObject.fromObject(te);
					} catch (Exception e) {
						logger.error("json异常:" + file.getPath() + "----" + temp);
						continue;
					}
					try {
						vo = gs.fromJson(te, BuildVO.class);
					} catch (Exception e) {
						logger.error("json转vo异常:" + file.getPath() + "----" + temp);
						continue;
					}

					if (vo != null) {

						if (StringUtils.isNull(vo.getCompanyName())) {

							continue;
						}
						System.out.println("-- " + vo.getCompanyName());

						List<BuildQulificationVO> qualificationGrade = vo.getQualificationGrade();
						if (qualificationGrade == null || qualificationGrade.size() == 0) {
							continue;
						}

						String companyName = vo.getCompanyName().replace(" ", "").replace("(", "（").replace(")", "）");
						String key_company = StringUtils.NBG.generate(companyName).toString();
						List<BuildQulificationVO> listquliT = new ArrayList<BuildQulificationVO>();

						String qualification_item = null;
						List<BuildQulificationVO> listquli2 = vo.getQualificationGrade();
						if (null != listquli2 && listquli2.size() > 0) {
							Iterator<BuildQulificationVO> iter = listquli2.iterator();
							while (iter.hasNext()) {
								BuildQulificationVO vq = iter.next();
								vq.setCertificateDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getCertificateDate()));
								vq.setExpireDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getExpireDate()));

								if (!StringUtils.isNull(vq.getQualification_item())) {

									if (vq.getQualification_item().contains("：")) {// 建设通

										qualification_item = vq.getQualification_item()
												.substring(vq.getQualification_item().indexOf("：") + 1)
												.replace(" ", "");
										qualification_item = qualification_item.replaceAll("\\[", "")
												.replaceAll("\\]", "").replaceAll("一级", "壹级").replaceAll("二级", "贰级")
												.replaceAll("三级", "叁级").replaceAll("四级", "肆级");

										vq.setQualification_item(qualification_item);

										// 处理日期 "update_date": "2016年06月" 加1日
										if (vq.getUpdate_date() != null && (vq.getUpdate_date().indexOf("年") != -1
												&& vq.getUpdate_date().indexOf("月") != -1
												&& vq.getUpdate_date().indexOf("日") == -1)) {
											vq.setCertificateDate(
													DateUtils.toYMDOfChaStr_ESZZ2(vq.getUpdate_date() + "1日"));
											vq.setUpdate_date(null);
										}

										key1 = StringUtils.NBG.generate(vo.getCompanyName() + qualification_item)
												.toString();
										vq.setBulidQulificationId(key1);
										vq.setSource(source);
										vq.setCompanyName(companyName);
										vq.setBuildId(key_company);
										listquliT.add(vq);

									} else {// 建筑平台
										String tempStr[] = vq.getQualification_item().split(",");
										for (int i4 = 0; i4 < tempStr.length; i4++) {
											BuildQulificationVO vq1 = new BuildQulificationVO();
											vq1.setCertificateNo(vq.getCertificateNo());
											vq1.setExpireDate(vq.getExpireDate());
											vq1.setCertificateDate(vq.getCertificateDate());
											vq1.setQualification_item(tempStr[i4]);

											key1 = StringUtils.NBG.generate(vo.getCompanyName() + qualification_item)
													.toString();
											vq1.setBulidQulificationId(key1);
											vq1.setSource(source);
											vq1.setCompanyName(companyName);
											vq1.setBuildId(key_company);
											listquliT.add(vq1);
										}
									}

								} else {
									vq.setQualification_item(null);
								}

							}
						}

						int flagS = 0;
						System.out.println(count + "  " + key1);

						while (true) {
							try {
								// 更新文档
								bucket = CouchbaseConnect.commonBucket("192.168.1.13:8091", "buildQulification");
								break;
							} catch (Exception e) {
								log.info("---------------------------> 插入BC超时");
								log.error(e.getMessage());
							}
						}

						if (listquliT.size() > 0) {

							for (BuildQulificationVO buildQulificationVO : listquliT) {
								content = JsonObject.fromJson(gs.toJson(buildQulificationVO));
								// documents.add(JsonDocument.create(buildQulificationVO.getBulidQulificationId(),
								// content));

								countLen++;
								bucket.upsert(
										JsonDocument.create(buildQulificationVO.getBulidQulificationId(), content));

								// System.out.println(
								// "---线程名" + Thread.currentThread().getName() +
								// "---- " +
								// countLen + " " + key1);

								System.out.println("---- " + countLen + "   " + buildQulificationVO.getBuildId() + " "
										+ buildQulificationVO.getBulidQulificationId() + " "
										+ buildQulificationVO.getCompanyName() + " "
										+ buildQulificationVO.getQualification_item());
							}

						} else {
							log.info("无资质： " + temp);
							return;

						}

					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

			}

			// }

		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {

				// logger.info("结束!!!!!!!!!!!");
				// logger.info("空数据" + countNull);
				//
				// long endTime = System.currentTimeMillis();
				// Date endDate = new Date(endTime);
				// System.out.println("结束时间--------------------" +
				// formatter.format(endDate));
				// System.out.println("Took : " + (float) ((endTime - startTime)
				// / 1000) + "秒");
				// System.out.println("Took : " + (float) ((endTime - startTime)
				// / 1000) / 60 + "分钟");
				// System.out.println("Total : " + count);
				// System.out.println(
				// "Took : " + (float) ((float) ((float) ((endTime - startTime)
				// / 1000) / 60) / 60) + "小时");
				// System.out.println("Speed : "
				// + (float) (count / ((float) ((float) ((endTime - startTime) /
				// 1000) / 60) / 60)) + "个/小时");

			} catch (Exception e) {
				logger.info(e);
			}
		}
	}

	public static void show(File file, Integer source, String coding, int numLen) throws IOException, ParseException {
		if (file.isFile()) {
			try {
				readFile(file, source, coding, numLen);
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
					try {
						readFile(fi, source, coding, numLen);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					show(fi, source, coding, numLen);
				} else {
					continue;
				}
			}
		}
	}

	/**
	 * 处理注册号、信用代码
	 * 
	 * @param regNum
	 * @return
	 */
	public static PrCiCouVO getAdmin(String regNum) {
		List<CodeVO> code = PrCiCouText.getregNumList(regNum);
		PrCiCouVO f = null;
		for (CodeVO ce : code) {
			try {
				// 注册号
				if (ce.getStatus() == 1) {
					String va = ce.getCode().substring(0, 6);
					f = PrCiCouText.prcicouName(va);
				} else {
					// 信用代码
					String va = ce.getCode().substring(2, 8); // 从第2位开始截取
					f = PrCiCouText.prcicouName(va);
				}
				if (null != f) {
					return f;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("getAdmin:" + e);
			}
		}
		return null;
	}

}
