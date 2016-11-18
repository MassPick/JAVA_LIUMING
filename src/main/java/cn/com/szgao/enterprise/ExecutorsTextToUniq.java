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

import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.ElasticSearchConnUtils220;
import cn.com.szgao.util.PrCiCouText;

/**
 * 深圳
 * 
 * @author xiongchangyi
 *
 */
public class ExecutorsTextToUniq {
	// 日志对象
	public static final Logger log = LogManager.getLogger(ExecutorsTextToUniq.class);
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
		
		try {
			FileIntoDataBaseToUniq  ff=new FileIntoDataBaseToUniq();
			
			
			
//			ff.show(new File("D:/工商数据已排序/吉林省.json"), 0);//------------------------liu 14
//			ff.show(new File("D:/工商数据已排序/宁夏.json"), 0);
//			ff.show(new File("D:/工商数据已排序/青海省.json"), 0);
//			ff.show(new File("D:/工商数据已排序/西藏.json"), 0);
//			ff.show(new File("D:/工商数据已排序/总局.json"), 0);
//			ff.show(new File("D:/工商数据已排序/160407工商.json"), 0);
//			ff.show(new File("D:/工商数据已排序/160530与小数据企业名对比等数据.json"), 0);
//			ff.show(new File("D:/工商数据已排序/160617后补数据.json"), 0);
//			ff.show(new File("D:/工商数据已排序/甘肃省.json"), 0);
//			ff.show(new File("D:/工商数据已排序/广西.json"), 0);
//			ff.show(new File("D:/工商数据已排序/广州市.json"), 0);
//			ff.show(new File("D:/工商数据已排序/安徽省.json"), 0);
//			ff.show(new File("D:/工商数据已排序/北京市.json"), 0);
//			ff.show(new File("D:/工商数据已排序/福建省.json"), 0);
//			ff.show(new File("D:/工商数据已排序/广东省.json"), 0);
//			ff.show(new File("D:/工商数据已排序/深圳市.json"), 0);
			
			
//			
//			ff.show(new File("D:/工商数据已排序/山西省.json"), 0);// ------------ hui 10
//			ff.show(new File("D:/工商数据已排序/陕西省.json"), 0);
//			ff.show(new File("D:/工商数据已排序/新疆.json"), 0);
//			ff.show(new File("D:/工商数据已排序/云南省.json"), 0);
//			ff.show(new File("D:/工商数据已排序/山东省.json"), 0);
//			ff.show(new File("D:/工商数据已排序/上海市.json"), 0);
//			ff.show(new File("D:/工商数据已排序/四川省.json"), 0);
//			ff.show(new File("D:/工商数据已排序/重庆市.json"), 0);
//			ff.show(new File("D:/工商数据已排序/天津市.json"), 0);
//			ff.show(new File("D:/工商数据已排序/浙江省.json"), 0);
//			
//			 ff.show(new File("D:/工商数据已排序/内蒙古.json"), 0);//------------------- changyi 11
//			 ff.show(new File("D:/工商数据已排序/贵州省.json"), 0);
//			 ff.show(new File("D:/工商数据已排序/海南省.json"), 0);
//			 ff.show(new File("D:/工商数据已排序/黑龙江省.json"), 0);
//			 ff.show(new File("D:/工商数据已排序/湖北省.json"), 0);
//			 ff.show(new File("D:/工商数据已排序/江西省.json"), 0);
//			 ff.show(new File("D:/工商数据已排序/湖南省.json"), 0);
//			 ff.show(new File("D:/工商数据已排序/辽宁省.json"), 0);
//			 ff.show(new File("D:/工商数据已排序/江苏省.json"), 0);
//			 ff.show(new File("D:/工商数据已排序/河北省.json"), 0);
//			 ff.show(new File("D:/工商数据已排序/河南省.json"), 0);
		} catch (Exception e) {
			
		}
		
		
		

//		ScheduledThreadPoolExecutor excuter = new ScheduledThreadPoolExecutor(1);
		
		
		//-----------------14个
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/吉林省.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/宁夏.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/青海省.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/西藏.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/总局.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/160407工商.json"), 0, new FileIntoDataBaseToUniq()));
		
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/甘肃省.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/广西.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/广州市.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/安徽省.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/北京市.json"), 0, new FileIntoDataBaseToUniq()));//数量:218904---线程名pool-1-thread-1文件： E:\工商数据已排序\北京市.json
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/福建省.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/广东省.json"), 0, new FileIntoDataBaseToUniq()));
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/深圳市.json"), 0, new FileIntoDataBaseToUniq()));
		
		
		
		
		//---------------------
//		excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/宁夏.json"),0,new FileIntoDataBaseToUniq()));
//		 excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/青海省.json"),0,new
//				 FileIntoDataBaseToUniq()));
//		 excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/西藏.json"),0,new
//				 FileIntoDataBaseToUniq()));
//		 excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/新疆.json"),0,new
//				 FileIntoDataBaseToUniq()));

		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/海南省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/贵州省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/吉林省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/内蒙古.json"),0,new
		// FileIntoDataBaseToUniq()));

		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/山西省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/陕西省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/云南省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/总局.json"),0,new
		// FileIntoDataBaseToUniq()));

		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/安徽省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/北京市.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/福建省.json"),0,new
		// FileIntoDataBaseToUniq()));
//		 excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/甘肃省.json"),0,new
//		 FileIntoDataBaseToUniq()));

		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/广东省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/广西.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/广州市.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/河北省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/河南省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/黑龙江省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/湖北省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/湖南省.json"),0,new
		// FileIntoDataBaseToUniq()));

		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/江苏省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/江西省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/辽宁省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/山东省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/上海市.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/深圳市.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/四川省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/天津市.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/浙江省.json"),0,new
		// FileIntoDataBaseToUniq()));
		// excuter.execute(new RunnableJsonUniq(new File("D:/工商数据已排序/重庆市.json"),0,new
		// FileIntoDataBaseToUniq()));

		// excuter.execute(new RunnableJson(new
		// File("D:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補02#"),new
		// FileIntoDataBase2()));
		// excuter.execute(new RunnableJson(new
		// File("D:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補03#"),new
		// FileIntoDataBase2()));
		// excuter.execute(new RunnableJson(new
		// File("D:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補04#"),new
		// FileIntoDataBase2()));
		// excuter.execute(new RunnableJson(new
		// File("D:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補05#"),new
		// FileIntoDataBase2()));

//		 excuter.shutdown();

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

//	public static void test() {
//
//		File fi = new File("D:\\lm\\log\\工商-深圳市-0008.txt");
//		FileIntoDataBaseToUniq file = new FileIntoDataBaseToUniq(log);
//
//		try {
//			file.show(fi, 0);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//
//	}
}
