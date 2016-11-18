package cn.com.szgao.enterprise_c;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RunnableJsonBreakFaithNewToBC implements Runnable{
	//日志对象
	private   Logger log = LogManager.getLogger(RunnableJsonBreakFaithNewToBC.class);
	private File file;
	private EnterpriceJSONDataBreakFaithNewToBC fileIntoDataBase2;
	public RunnableJsonBreakFaithNewToBC(File file,EnterpriceJSONDataBreakFaithNewToBC fileIntoDataBase2){
		this.file=file;
		this.fileIntoDataBase2=fileIntoDataBase2;
	}
	public RunnableJsonBreakFaithNewToBC(){}
	public void run() {
		try {
			fileIntoDataBase2.show(file);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

}
