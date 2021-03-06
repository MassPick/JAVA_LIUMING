package cn.com.szgao.wash.data.breakfaith;

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

import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.ElasticSearchConnUtils220;
import cn.com.szgao.util.PrCiCouText;

/**
 * 深圳
 * 
 * @author xiongchangyi
 *
 */
public class InsertToESFromJsonBreakfatith {
	// 日志对象
	public static final Logger log = LogManager.getLogger(InsertToESFromJsonBreakfatith.class);
	static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	static String usr = "postgres";
	static String psd = "615601.xcy*";

	static {

		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ScheduledThreadPoolExecutor excuter = new ScheduledThreadPoolExecutor(3);
		excuter.execute(new RunnableJsonESBreakfaith(new File("D:/lm/log/temp/失信/20160217/shixin_0-150W"), 0, new FileIntoDataBaseToESBreakfaith()));
		excuter.execute(new RunnableJsonESBreakfaith(new File("D:/lm/log/temp/失信/20160217/shixin_150W-300W"), 0, new FileIntoDataBaseToESBreakfaith()));
		excuter.execute(new RunnableJsonESBreakfaith(new File("D:/lm/log/temp/失信/20160217/shixin_300W-350W"), 0, new FileIntoDataBaseToESBreakfaith()));
		excuter.shutdown();
		
		
//		try {
//			FileIntoDataBaseToESBreakfaith fb=new FileIntoDataBaseToESBreakfaith();
//			fb.show(new File("D:/lm/log/temp/失信/20160217/shixin_0-150W/breakfaith80000.log"), 0);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

		
		 
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
		FileIntoDataBaseToESBreakfaith file = new FileIntoDataBaseToESBreakfaith(log);

		try {
			file.show(fi, 0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
}
