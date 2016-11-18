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
import cn.com.szgao.dto.BuildManagerDetailVO;
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
 * 建设通经营  因数据有带*的，要去掉     1、先旧的 InsertBuildManagerFromOneJsonToBC_Old    2、后新的
 * @author liuming
 * @Date 2016年8月29日 下午4:18:17
 */
public class InsertBuildManagerFromOneJsonToBC_New {

	private static Logger log = LogManager.getLogger(InsertBuildManagerFromOneJsonToBC_New.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(InsertBuildManagerFromOneJsonToBC_New.class);
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

		Integer source = 0;// 1 全国建设市场监督 0
							// 或null是建设通-----------------------------------------------------------------------重点

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
						List<BuildManagerDetailVO> lists = vo.getManagementInfos();
						if (lists == null || lists.size() == 0) {
							continue;
						}

						String companyName = vo.getCompanyName().replace(" ", "").replace("(", "（").replace(")", "）");
						String key_company = StringUtils.NBG.generate(companyName).toString();
						// List<BuildQulificationVO> listquliT = new
						// ArrayList<BuildQulificationVO>();

						for (BuildManagerDetailVO buildManagerDetailVo : lists) {
							if (StringUtils.isNull(buildManagerDetailVo.getIncident())) {
								continue;
							}
							if(StringUtils.countCharacter(buildManagerDetailVo.getIncident(), "\\*")>3){
								continue;
							}
							
							
							buildManagerDetailVo.setIncidentTime(
									DateUtils.toYMDOfChaStr_ESZZ2(buildManagerDetailVo.getIncidentTime()));
							buildManagerDetailVo.setBuildManagerDetailId(
									StringUtils.NBG.generate(buildManagerDetailVo.getIncidentLink()).toString());
							buildManagerDetailVo.setCompanyName(companyName);
							buildManagerDetailVo.setBuildId(key_company);
							
							System.out.println(buildManagerDetailVo.getBuildManagerDetailId());
							while (true) {
								try {
									// 更新文档
									bucket = CouchbaseConnect.commonBucket("192.168.1.13:8091", "buildManager");
									break;
								} catch (Exception e) {
									log.info("---------------------------> 插入BC超时");
									log.error(e.getMessage());
								}
							}

							try {
								content = JsonObject.fromJson(gs.toJson(buildManagerDetailVo));
							}

							catch (Exception e) {
								log.error(e.getMessage());
							}
							countLen++;

							bucket.upsert(JsonDocument.create(buildManagerDetailVo.getBuildManagerDetailId(), content));
						}

					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

			}

		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {

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
