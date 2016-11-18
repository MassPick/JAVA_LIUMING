package cn.com.szgao.enterprise;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.PrCiCouText;

/**
 * 合并文件
 * 
 * @author liuming
 *
 */
public class ExecutorsHeBin {

	// 日志对象
	public static final Logger log = LogManager.getLogger(ExecutorsTextToUniq.class);
	static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	static String usr = "postgres";
	static String psd = "615601.xcy*";

	static {

		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}

	public static void main(String[] args) throws InterruptedException {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			
			// -----------------14个 +3
//		toHeBin(new File("D:/lm/log/工商清洗数据New/吉林省"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/宁夏"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/青海省"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/西藏"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/总局"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/160407工商") );
//		toHeBin(new File("D:/lm/log/工商清洗数据New/160530与小数据企业名对比等数据") );
//		toHeBin(new File("D:/lm/log/工商清洗数据New/160617后补数据") );
//		toHeBin(new File("D:/lm/log/工商清洗数据New/甘肃省"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/广西"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/广州市"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/安徽省"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/北京市"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/福建省"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/广东省"));
//		toHeBin(new File("D:/lm/log/工商清洗数据New/深圳市"));
//		
		 toHeBin(new File("D:/lm/log/工商清洗数据New/江西省"));
		 toHeBin(new File("D:/lm/log/工商清洗数据New/湖北省"));
		 toHeBin(new File("D:/lm/log/工商清洗数据New/湖南省"));
			
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/山西省"));//------------ hui 10
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/陕西省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/新疆"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/云南省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/山东省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/上海市"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/四川省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/重庆市"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/天津市"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/浙江省"));
			
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/内蒙古"));//------------------- changyi 11
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/贵州省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/海南省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/黑龙江省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/湖北省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/江西省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/湖南省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/辽宁省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/江苏省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/河北省"));
//			 toHeBin(new File("D:/lm/log/工商清洗数据New/河南省"));
			

			 
			 
			 
//			 ScheduledThreadPoolExecutor excuter = new ScheduledThreadPoolExecutor(1);
			//-----------------14个
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/吉林省"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/宁夏"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/青海省"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/西藏"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/总局"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/160407工商"), 0, new FileIntoDataBaseToHeBin()));
			
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/甘肃省"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/广西"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/广州市"), 0, new FileIntoDataBaseToHeBin()));
			
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/安徽省"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/北京市"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/福建省"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/广东省"), 0, new FileIntoDataBaseToHeBin()));
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/深圳市"), 0, new FileIntoDataBaseToHeBin()));
			
			
			
			//--------------------------------------------

//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/甘肃省/工商-甘肃省-0001.txt"), 7073, new FileIntoDataBaseToHeBin()));   //7073  工商-甘肃省-0001.txt
//			excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/甘肃省/工商-甘肃省-0003.txt"), 13143, new FileIntoDataBaseToHeBin()));   //7073  工商-甘肃省-0001.txt
			
			

			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/宁夏"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/青海省"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/西藏"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/新疆"), 0, new
			// FileIntoDataBaseToHeBin()));

			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/海南省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/贵州省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/吉林省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/内蒙古"),0,new
			// FileIntoDataBaseToHeBin()));

			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/山西省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/陕西省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/云南省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/总局"),0,new
			// FileIntoDataBaseToHeBin()));

			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/安徽省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/北京市"),12,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/福建省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/甘肃省"), 0, new
			// FileIntoDataBaseToHeBin()));

			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/广东省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/广西"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/广州市"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/河北省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/河南省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/黑龙江省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/湖北省"),0,new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/湖南省"),0,new
			// FileIntoDataBaseToHeBin()));

			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/江苏省"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/江西省"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/辽宁省"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/山东省"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/上海市"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/深圳市"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/四川省"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/天津市"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/浙江省"), 0, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new File("D:/lm/log/工商清洗数据New/重庆市"), 0, new
			// FileIntoDataBaseToHeBin()));

			// ABB（中国）有限公司 E:\刘铭\data\北京市\工商-北京市-0001.txt

			// excuter.execute(new RunnableJsonHeBin(new
			// File("D:/lm/log/工商清洗数据New/上海市/工商-上海市-0002.txt"), 8205, new
			// FileIntoDataBaseToHeBin()));
			// excuter.execute(new RunnableJsonHeBin(new
			// File("D:/lm/log/工商清洗数据New/深圳市/工商-深圳市-0003.txt"), 0, new
			// FileIntoDataBaseToHeBin()));

//			excuter.shutdown();

//		    long startTime = System.currentTimeMillis();
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
	//
//			test();
	//
//			long endTime = System.currentTimeMillis();
//			Date endDate = new Date(endTime);
//			// log.info("路径：" + fileDir.getPath());
//			log.info("开始时间--------------------" + formatter.format(startTime));
//			log.info("结束时间--------------------" + formatter.format(endDate));
//			log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
//			log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
//			log.info("Took : " + (float) ((endTime - startTime) / 1000) / 3600 + "小时");

		}

	public void test() {
		long startTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		Date date = new Date(startTime);
		System.out.println("开始时间--------------------" + formatter.format(date));

		List<Path> sources = new ArrayList<Path>();

		String path = "E:/工商数据未排序/甘肃省.json";
		File fileS = new File(path);
		if (!fileS.exists()) {
			try {
				fileS.createNewFile();

			} catch (IOException e) {
			}
		} else {
			fileS.delete();
			fileS = new File(path);
		}
		Path target = fileS.toPath();

		File file = new File("D:/lm/log/工商清洗数据New/甘肃省");

		if (file.isFile()) {
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		if (null != files) {
			for (File fi : files) {
				if (fi.isFile()) {
					sources.add(fi.toPath());
				} else if (fi.isDirectory()) {
				} else {
					continue;
				}
			}
		}

		for (Path f : sources) {
			System.out.println(f.getFileName());
			try {
				Files.write(target, Files.readAllLines(f, Charset.defaultCharset()), Charset.defaultCharset(),
						StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		System.out.println("结束时间--------------------" + formatter.format(endDate));
		System.out.println("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
		System.out.println("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");

	}

	public static void toHeBin(File file) {

		long da = System.currentTimeMillis();

		System.out.println("--------------合并开始: " + file.getPath());
		if (file.isFile()) {
			try {
			} catch (Exception e) {
				e.printStackTrace();
			}
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		if (null != files) {

			// File file = new File("D:/lm/log/工商清洗数据New/甘肃省");
			String ss2 = file.getPath().substring(file.getPath().indexOf("工商清洗数据New") + 10);// 省名
			String filePath2 = "E:\\工商数据未排序\\" + ss2 + ".json";

			// 创建文件夹
			// FileUtils.newFolder(folderPath2);

			List<Path> sources = new ArrayList<Path>();

			File fileS = new File(filePath2);
			if (!fileS.exists()) {
				try {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					fileS.createNewFile();
				} catch (IOException e) {
				}
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				fileS.delete();
				fileS = new File(filePath2);
			}
			Path target = fileS.toPath();

			if (file.isFile()) {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
				// file.delete();
				return;
			}

			File[] files1 = file.listFiles();
			if (null != files) {
				for (File fi : files1) {
					if (fi.isFile()) {
						sources.add(fi.toPath());
					} else if (fi.isDirectory()) {
					} else {
						continue;
					}
				}
			}

			for (Path f : sources) {
				log.info("合并: " + f.getFileName());
				try {
					Files.write(target, Files.readAllLines(f, Charset.defaultCharset()), Charset.defaultCharset(),
							StandardOpenOption.APPEND);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		System.out.println("--------------合并结束: " + file.getPath());
		System.out.println("总耗时:" + ((System.currentTimeMillis() - da) / 1000) + "秒");
		System.out.println("总耗时:" + (float) ((System.currentTimeMillis() - da) / 1000) / 60 + "分");
		System.out.println(
				"总耗时:" + (float) ((float) ((float) ((System.currentTimeMillis() - da) / 1000) / 60) / 60) + "时");

	}

}
