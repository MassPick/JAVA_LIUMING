package cn.com.szgao.enterprise_c;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RunnableJsonBreakFaithLog2 implements Runnable{
	//日志对象
	private   Logger log = LogManager.getLogger(RunnableJsonBreakFaithLog2.class);
	private File file;
	private int startNum;
	private EnterpriceJSONDataBreakFaithLog2 fileIntoDataBase2;
	private String targetFDir;
	public RunnableJsonBreakFaithLog2(File file,int startNum,String targetFDir,EnterpriceJSONDataBreakFaithLog2 fileIntoDataBase2){
		this.file=file;
		this.fileIntoDataBase2=fileIntoDataBase2;
		this.startNum=startNum;
		this.targetFDir=targetFDir;
	}
	public RunnableJsonBreakFaithLog2(){}
	public void run() {
		try {
			fileIntoDataBase2.show(file,startNum,targetFDir);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
}
