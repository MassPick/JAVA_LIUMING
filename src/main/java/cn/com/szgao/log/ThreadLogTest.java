package cn.com.szgao.log;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ThreadLogTest {

	static Logger logger=Logger.getLogger(ThreadLogTest.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//PropertyConfigurator.configure("G:\\Mass\\log\\log4j.properties");
		/*MyThread myThread=new MyThread();
		for(int i=1;i<11;i++){
			new Thread(new MyThread()).start();
		}*/
		ScheduledThreadPoolExecutor excuter=new ScheduledThreadPoolExecutor(4);
		excuter.execute(new MyThread());
		excuter.execute(new MyThread());
		excuter.execute(new MyThread());
		excuter.execute(new MyThread());
		excuter.shutdown();
	}

}
	class MyThread implements Runnable{
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
				Logger logger=ThreadLoggerFactory.getLogger();
				//logger.debug(Thread.currentThread().getName()+" -----debug");
				for(int i=0;i<10000;i++)
				logger.info(Thread.currentThread().getName()+" -----info:"+i);
		}
		
	}