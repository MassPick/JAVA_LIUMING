package cn.com.szgao.enterprise;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Test89ExecuteThread {
	public static void main(String[] args) {
		ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(10);
		//线程参数1.路径，2.对象，3.表名	
		stpe.execute(new Test89Runnable("E:\\Temp_File\\工商数据\\pao1\\福建省", new Test89(), "dump_fujian"));
		stpe.execute(new Test89Runnable("E:\\Temp_File\\工商数据\\pao1\\广西省", new Test89(), "dump_guangxi"));
		stpe.execute(new Test89Runnable("E:\\Temp_File\\工商数据\\pao1\\海南省", new Test89(), "dump_hainan"));
		stpe.execute(new Test89Runnable("E:\\Temp_File\\工商数据\\pao1\\湖北省", new Test89(), "dump_hubei"));
		stpe.execute(new Test89Runnable("E:\\Temp_File\\工商数据\\pao2\\四川省", new Test89(), "dump_sichuan"));
		stpe.execute(new Test89Runnable("E:\\Temp_File\\工商数据\\pao2\\天津市", new Test89(), "dump_tianjin"));
		stpe.execute(new Test89Runnable("E:\\Temp_File\\工商数据\\pao3\\河北省", new Test89(), "dump_hebei"));
		stpe.execute(new Test89Runnable("E:\\Temp_File\\工商数据\\深圳", new Test89(), "dump_guangdong_shenzheng"));
		stpe.execute(new Test89Runnable("E:\\Temp_File\\工商数据\\pao1\\广东省\\广州", new Test89(), "dump_guangdong_guangzhou"));
		stpe.shutdown();
	}
}
