package cn.com.szgao.enterprise;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.couchbase.client.java.Bucket;

import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.ElasticSearchConnUtils220;
import cn.com.szgao.util.PrCiCouText;

/**
 * 深圳
 * 
 * @author xiongchangyi
 *
 */
public class ExecutorsTextToESOld {
	// 日志对象
	public static final Logger log = LogManager.getLogger(ExecutorsTextToESOld.class);
	static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	static String usr = "postgres";
	static String psd = "615601.xcy*";
	static Bucket bucket_etp_check = null;
	static {

		try {
			PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
			
//			while (true) {
//				try {
//					bucket_etp_check = CouchbaseConnect.commonBucket("192.168.1.30:8091", "businessD");
//					break;
//				} catch (Exception e) {
//					log.info("---------------------------> 连BC超时");
//					log.error(e.getMessage());
//				}
//			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ScheduledThreadPoolExecutor excuter = new ScheduledThreadPoolExecutor(3);
		excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/宁夏"), 0, new FileIntoDataBaseToES2Old()));
//		 excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/青海省"), 0, new
//				 FileIntoDataBaseToES2Old()));
//		 excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/西藏"), 0, new
//				 FileIntoDataBaseToES2Old()));
		 excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/新疆"), 0, new
				 FileIntoDataBaseToES2Old()));

		 excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/海南省"),0,new
		 FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/贵州省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/吉林省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/内蒙古"),0,new
		// FileIntoDataBaseToES2Old()));

		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/山西省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/陕西省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/云南省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/总局"),0,new
		// FileIntoDataBaseToES2Old()));

		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/安徽省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/北京市"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/福建省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/甘肃省"),0,new
		// FileIntoDataBaseToES2Old()));

		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/广东省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/广西"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/广州市"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/河北省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/河南省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/黑龙江省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/湖北省"),0,new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/湖南省"),0,new
		// FileIntoDataBaseToES2Old()));

		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/江苏省"), 0, new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/江西省"), 0, new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/辽宁省"), 0, new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/山东省"), 0, new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/上海市"), 0, new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/深圳市"), 0, new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/四川省"), 0, new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/天津市"), 0, new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/浙江省"), 0, new
		// FileIntoDataBaseToES2Old()));
		// excuter.execute(new RunnableJsonESOld(new File("E:/工商数据已去重/重庆市"), 0, new
		// FileIntoDataBaseToES2Old()));

		// excuter.execute(new RunnableJson(new
		// File("E:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補02#"),new
		// FileIntoDataBase2()));
		// excuter.execute(new RunnableJson(new
		// File("E:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補03#"),new
		// FileIntoDataBase2()));
		// excuter.execute(new RunnableJson(new
		// File("E:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補04#"),new
		// FileIntoDataBase2()));
		// excuter.execute(new RunnableJson(new
		// File("E:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補05#"),new
		// FileIntoDataBase2()));

		 excuter.shutdown();

		
		 
		 
//	    long startTime = System.currentTimeMillis();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
//
//		test();
//
//		long endTime = System.currentTimeMillis();
//		Date endDate = new Date(endTime);
//		// log.info("路径：" + fileDir.getPath());
//		log.info("开始时间--------------------" + formatter.format(startTime));
//		log.info("结束时间--------------------" + formatter.format(endDate));
//		log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
//		log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
//		log.info("Took : " + (float) ((endTime - startTime) / 1000) / 3600 + "小时");

	}

	public static void test() {

		File fi = new File("D:\\lm\\log\\工商-深圳市-0008.txt");
		FileIntoDataBaseToES2Old file = new FileIntoDataBaseToES2Old(log);

		try {
			file.show(fi, 0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
}
