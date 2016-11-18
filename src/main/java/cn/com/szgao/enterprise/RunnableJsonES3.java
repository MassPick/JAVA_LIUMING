package cn.com.szgao.enterprise;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.com.szgao.log.ThreadLoggerFactory;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.ElasticSearchConnUtils220;

public class RunnableJsonES3 implements Runnable{
	
	private static Logger log = LogManager.getLogger(RunnableJsonES3.class);
	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}
	
	//日志对象
//	private   Logger log = LogManager.getLogger(RunnableJsonES3.class);
	private File file;
	private FileIntoDataBaseToES3 fileIntoDataBase2;
	private int startNum;
	public RunnableJsonES3(File file,int startNum,FileIntoDataBaseToES3 fileIntoDataBase2){
		this.file=file;
		this.fileIntoDataBase2=fileIntoDataBase2;
	}
	public RunnableJsonES3(){}
	public void run() {
		log.error("error111111111111111111111");
//		Logger log=FileIntoDataBaseToES3.getLog();
		try {
			long da=System.currentTimeMillis();
			fileIntoDataBase2=new FileIntoDataBaseToES3();
			fileIntoDataBase2.show(file,startNum);
//			fileIntoDataBase2.setLog(log);
			log.info("线程名:"+Thread.currentThread().getName());
			log.info("总数量："+fileIntoDataBase2.count);
			log.info("总耗时:"+((System.currentTimeMillis()-da)/1000)+"秒");
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	
	
	//日志对象
//	private   Logger log = LogManager.getLogger(RunnableJsonES3.class);
//	private File file;
//	private FileIntoDataBaseToES3 fileIntoDataBase2;
//	private int startNum;
//	public RunnableJsonES3(File file,int startNum,FileIntoDataBaseToES3 fileIntoDataBase2){
//		this.file=file;
//		this.fileIntoDataBase2=fileIntoDataBase2;
//	}
//	public RunnableJsonES3(){}
//	public void run() {
//		Logger log=FileIntoDataBaseToES3.getLog();
//		try {
//			long da=System.currentTimeMillis();
//			fileIntoDataBase2=new FileIntoDataBaseToES3(log);
//			fileIntoDataBase2.show(file,startNum);
//			fileIntoDataBase2.setLog(log);
//			log.info("线程名:"+Thread.currentThread().getName());
//			log.info("总数量："+fileIntoDataBase2.count);
//			log.info("总耗时:"+((System.currentTimeMillis()-da)/1000)+"秒");
//		} catch (IOException e) {
//			e.printStackTrace();
//			log.error(e.getMessage());
//		} catch (ParseException e) {
//			e.printStackTrace();
//			log.error(e.getMessage());
//		}
//	}

}
