package cn.com.szgao.enterprise_c;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RunnableJson2 implements Runnable {

	//日志对象
		private   Logger log = LogManager.getLogger(RunnableJson2.class);
		private File file;
		private EnterpriceJSONDataExcuted fileIntoDataBase2;
		public RunnableJson2(File file,EnterpriceJSONDataExcuted fileIntoDataBase2){
			this.file=file;
			this.fileIntoDataBase2=fileIntoDataBase2;
		}
		public RunnableJson2(){}
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
