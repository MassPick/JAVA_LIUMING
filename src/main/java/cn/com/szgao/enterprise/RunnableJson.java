package cn.com.szgao.enterprise;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import cn.com.szgao.log.ThreadLoggerFactory;

public class RunnableJson implements Runnable{
	//日志对象
	//private   Logger log = LogManager.getLogger(FileIntoDataBase2.class);
	private File file;
	private FileIntoDataBase2 fileIntoDataBase2;
	public RunnableJson(File file,FileIntoDataBase2 fileIntoDataBase2){
		this.file=file;
		//this.fileIntoDataBase2=fileIntoDataBase2;
	}
	public RunnableJson(){}
	public void run() {
		Logger log=ThreadLoggerFactory.getLogger();
		try {
			long da=System.currentTimeMillis();
			fileIntoDataBase2=new FileIntoDataBase2(log);
			fileIntoDataBase2.show(file);
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
