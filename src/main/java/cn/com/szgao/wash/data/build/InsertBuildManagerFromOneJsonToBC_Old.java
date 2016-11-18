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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import cn.com.szgao.dto.BuildManagerVO;
import cn.com.szgao.dto.BuildQulificationVO;
import cn.com.szgao.dto.BuildVO;
import cn.com.szgao.dto.HonourDetailVO;
import cn.com.szgao.dto.HonourVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;
import rx.functions.Func1;

import rx.Observable;

/**
 * 将一个个建设通的经营信息  入库到CB C:\data\建设通和建设市场发布平台\建设通\经营信息
 * /**
 * 建设通经营  因数据有带*的，要去掉     1、先旧的 InsertBuildManagerFromOneJsonToBC_Old    2、后新的
 * @author liuming
 * @Date 2016年8月29日 下午4:18:17
 */
 
public class InsertBuildManagerFromOneJsonToBC_Old {

	private static Logger log = LogManager.getLogger(InsertBuildManagerFromOneJsonToBC_Old.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(InsertBuildManagerFromOneJsonToBC_Old.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {

		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		 
//		 File file = new File("C:/data/建设通和建设市场发布平台/建设通/经营信息");
		 File file = new File("C:/data/建设通和建设市场发布平台_新/建设通/经营/经营");
		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();

			show(file);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
			log.info("路径：" + file.getPath());
			log.info("开始时间--------------------" + formatter.format(startTime));
			log.info("结束时间--------------------" + formatter.format(endDate));
			log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
			log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
			log.info("Total : " + count);
			log.info("Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
			log.info("Speed : " + (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60))) + "个/分钟");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static Bucket bucket = null;

	static int num=0;
	@SuppressWarnings("null")
	public static void readFile(File file) {
		num++;
		System.out.println(num);
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
				isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
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
			BuildManagerVO vo = null;
			BuildManagerDetailVO buildManagerDetailVo = null;
			Gson gs = new Gson();
			JSONObject temJson = null;
			String key1 = "";

			String companyName = file.getName().substring(0,file.getName().indexOf(".json")).replace(" ", "").replace("(", "（").replace(")", "）");  
			if (StringUtils.isNull(companyName)) {
				return;
			}
			String key_companyName=StringUtils.NBG.generate(companyName).toString();

			List<JsonDocument> documents = new ArrayList<JsonDocument>();
			JsonObject content = null;
			int countLen = 0;
//			List<BuildManagerDetailVO  > buildManagerDetail = new ArrayList<BuildManagerDetailVO>();
			String te = null;
//			StringBuffer sb = new StringBuffer();
			while ((te = reader.readLine()) != null) {
				
				try {
					temJson = JSONObject.fromObject(te.toString());
				} catch (Exception e) {
					logger.error("json异常:" + file.getPath() + "----" + temp);
					continue;
				}

				try {
					buildManagerDetailVo = gs.fromJson(te.toString(), BuildManagerDetailVO.class);
//					sb = new StringBuffer();
					if(StringUtils.isNull(buildManagerDetailVo.getIncident() )){
						continue;
					}
					buildManagerDetailVo.setIncidentTime(DateUtils.toYMDOfChaStr_ESZZ2(buildManagerDetailVo.getIncidentTime()));
					buildManagerDetailVo.setBuildManagerDetailId(StringUtils.NBG.generate(buildManagerDetailVo.getIncidentLink()).toString());
					buildManagerDetailVo.setCompanyName(companyName);
					buildManagerDetailVo.setBuildId(key_companyName);
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
					
				} catch (Exception e) {
					logger.error("json转vo异常:" + file.getPath() + "----" + te.toString());
					continue;
				}
			}
	 
			

		} catch (Exception e) {
			logger.info(e);
		} finally {
 
		}
	}

	public static void show(File file) throws IOException, ParseException {
		if (file.isFile()) {
			try {
				readFile(file);
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
						readFile(fi);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					show(fi);
				} else {
					continue;
				}
			}
		}
	}

}
