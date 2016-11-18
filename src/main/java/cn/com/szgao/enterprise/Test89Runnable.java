package cn.com.szgao.enterprise;


import org.apache.log4j.Logger;

import cn.com.szgao.log.ThreadLoggerFactory;
/**
 * 写库多线程
 * @author xiongchangyi
 *
 */
public class Test89Runnable implements Runnable{
	//目录或文件
	private String filePath;
	//类
	private Test89 test;
	//表名称
	private String tableName;
	/**
	 * 构造方法
	 * @param filePath
	 * @param test
	 * @param tableName
	 */
	public Test89Runnable(String filePath,Test89 test,String tableName)
	{
		this.filePath = filePath;
		this.test = test;
		this.tableName= tableName;
	}
	/**
	 * 线程的方法
	 */
	public void run() {
		Logger log=ThreadLoggerFactory.getLogger();
		log.info(Thread.currentThread().getName());
		test.setLog(log);
		try
		{
			long da=System.currentTimeMillis();
			test.test(filePath, tableName);
			log.info("线程名:"+Thread.currentThread().getName());
			log.info("总数量："+test.COUNT);
			log.info("总耗时:"+((System.currentTimeMillis()-da)/1000)+"秒");
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}
}
