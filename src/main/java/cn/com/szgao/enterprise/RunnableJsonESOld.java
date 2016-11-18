package cn.com.szgao.enterprise;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.com.szgao.log.ThreadLoggerFactory;
import cn.com.szgao.util.ConfigUtils;

public class RunnableJsonESOld implements Runnable{
	
	private static Logger log = LogManager.getLogger(RunnableJsonESOld.class);
	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}
	
	
	
	//日志对象
	//private   Logger log = LogManager.getLogger(FileIntoDataBase2.class);
	private File file;
	private FileIntoDataBaseToES2Old fileIntoDataBase2;
	private int startNum;
	public RunnableJsonESOld(File file,int startNum,FileIntoDataBaseToES2Old fileIntoDataBase2){
		this.file=file;
		this.fileIntoDataBase2=fileIntoDataBase2;
	}
	public RunnableJsonESOld(){}
	public void run() {
//		Logger log=ThreadLoggerFactory.getLogger();
		try {
			long da=System.currentTimeMillis();
			fileIntoDataBase2=new FileIntoDataBaseToES2Old();
			fileIntoDataBase2.show(file,startNum);
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

}
