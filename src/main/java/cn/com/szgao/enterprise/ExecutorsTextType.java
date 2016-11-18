package cn.com.szgao.enterprise;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/**
 * 深圳
 * @author xiongchangyi
 *
 */
public class ExecutorsTextType {
	//日志对象
	public static  final Logger log = LogManager.getLogger(FileIntoDataBase2.class);
//	static{
//		DataUtils.initData();
//	}
	public static void main(String[] args) throws InterruptedException {
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		ScheduledThreadPoolExecutor excuter=new ScheduledThreadPoolExecutor(5);
		/*excuter.execute(new RunnableJson(new File("E:\\Temp_File\\工商数据\\深圳\\深圳-第2批\\VPS_深圳"),new FileIntoDataBase2()));
		excuter.execute(new RunnableJson(new File("E:\\Temp_File\\工商数据\\深圳\\深圳-第1批"),new FileIntoDataBase2()));*/
		
//		excuter.execute(new RunnableJson(new File("E:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補01#"),new FileIntoDataBase2()));
//		excuter.execute(new RunnableJson(new File("E:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補02#"),new FileIntoDataBase2()));
//		excuter.execute(new RunnableJson(new File("E:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補03#"),new FileIntoDataBase2()));
//		excuter.execute(new RunnableJson(new File("E:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補04#"),new FileIntoDataBase2()));
//		excuter.execute(new RunnableJson(new File("E:/Temp_File/工商数据/第3批/深圳市/深圳正式数据補05#"),new FileIntoDataBase2()));
//		excuter.shutdown();
		
		test();
		
		
	}
	public static void test()
	{
		
//		File fi=new File("E:\\Temp_File\\工商数据\\第3批\\深圳市\\深圳正式数据補02#\\深圳\\深圳-深圳市丛风科技有限公司-20151203192839.json");

//		File fi=new File("E:\\刘铭\\工商-重庆市-0005.txt");
//		File fi=new File("E:\\刘铭\\广州\\工商-广州市-0007.txt");
		File fi=new File("E:\\刘铭\\data");
		
		FileIntoDataBase2pType file=new FileIntoDataBase2pType(log);
		try {
			file.show(fi,  0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
	}
}
