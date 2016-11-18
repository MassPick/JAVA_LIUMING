package cn.com.szgao.enterprise_c;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RunnableJsonBreakFaithToBlob implements Runnable{
	//日志对象
	private   Logger log = LogManager.getLogger(RunnableJsonBreakFaithToBlob.class);
	private File file;
	private int startNum;
	private EnterpriceJSONDataBreakFaithToBlob fileIntoDataBase2;
	public RunnableJsonBreakFaithToBlob(File file,int startNum,EnterpriceJSONDataBreakFaithToBlob fileIntoDataBase2){
		this.file=file;
		this.fileIntoDataBase2=fileIntoDataBase2;
		this.startNum=startNum;
	}
	public RunnableJsonBreakFaithToBlob(){}
	public void run() {
		try {
			fileIntoDataBase2.show(file,startNum);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
}
