package cn.com.szgao.clean.court;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.google.gson.Gson;

import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.wash.data.AdministrationUtils;

public class SplitterFile {
	private static Logger logger = LogManager.getLogger(ExtractionJsonAndHtmlToTxtOldJsonUseCBLocalSZheJGKW.class.getName());
	private static Logger log = LogManager.getLogger(ExtractionJsonAndHtmlToTxtOldJsonUseCBLocalSZheJGKW.class.getName());
	/**
	 * 湖北省的一个法院中有html又有swf，现在拆开
	 * 
	 * @Description: TODO
	 * @param args
	 * @return void
	 * @author liuming
	 * @date 2016年7月6日 下午5:26:37
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 导入文件地址
		  
		
		File file = new File("E:/法院详细页面/地方/湖北省/要拆");
		
		 

		Bucket bucket = null;
	 
		
		// bucket = connectionBucket(bucket);
		try {
			show(file, bucket);
 

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			file = null;
			// bucket.close();
		}
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		log.info("开始时间--------------------" + formatter.format(startTime));

		log.info("结束时间--------------------" + formatter.format(endDate));
 

		log.info("Time : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
		log.info("Time : " + (float) ((float) ((endTime - startTime) / 1000) / 60) + "分钟");
		log.info("Time : " + (float) ((endTime - startTime) / 1000) + "秒");
		log.info("Total : " + countNum);
		log.info("Speed : " + (float) (countNum / (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
				+ "个/小时");
		log.info("Speed : " + (float) (countNum / (float) ((float) ((endTime - startTime) / 1000) / 60)) + "个/分种");
		log.info("Speed : " + (float) (countNum / (float) ((endTime - startTime) / 1000)) + "个/秒");

		log.info("找不到Html的数量  : " + countNotHtml);

		System.exit(0);
	}

	private static void show(File file, Bucket bucket) throws Exception {
		if (file.isFile()) {
			 
				long da = System.currentTimeMillis();
				create(file, bucket);
				System.out.println("读取<<" + file.getPath() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
			 
		}
		File[] files = file.listFiles();
		if (files.length > 0) {
			System.out.println("----files  第一个：---" + files[0].getPath());
		}

		for (File fi : files) {
			if (fi.isFile()) {
				 
					create(fi, bucket);
			 
			} else if (fi.isDirectory()) {
				// 只认带url的
				show(fi, bucket);

			} else {
				continue;
			}
		}
	}

	static int countNum = 0;
	static int countNotHtml = 0;
	static int countP = 0;
	static BufferedWriter fwUn = null;
	static BufferedWriter fwUn2 = null;

	private static <ObjectDataVO, JSONObject> void create(File file, Bucket bucket)
			throws Exception, UnsupportedEncodingException {

		try {
			String name = file.getParentFile().getPath();
			name = name.substring(name.lastIndexOf("\\") + 1, name.length());
			BufferedReader reader = null;
			Gson gson = new Gson();
			List<WholeCourtVO> list = new ArrayList<WholeCourtVO>();
			String temp = null;
			int sum = 0;
			String fileHtmlpath = file.getPath();
			String filePath = file.getPath();

			WholeCourtVO archJson = null;
			WholeCourtVO archHtml = null;
			WholeCourtVO finalVo = null;
			JsonDocument queryDoc = null;

			WholeCourtVO tempVo = null;

			String suffix = file.getName();

			suffix = suffix.substring(suffix.indexOf(".") + 1, suffix.length());

			if ("swf".equals(suffix)) {
				String file_path = file.getPath().replace("要拆", "1_swf");
				FileUtils.newFolder(file_path.substring(0, file_path.lastIndexOf("\\")));
				FileUtils.copyFile(file.getPath(), file_path);
			} else if ("html".equals(suffix)) {
				String file_path = file.getPath().replace("要拆", "1_html");
				FileUtils.newFolder(file_path.substring(0, file_path.lastIndexOf("\\")));
				FileUtils.copyFile(file.getPath(), file_path);
			}
			
			System.out.println(countNum++);

		} catch (Exception e) {
			// logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生IO异常:"
			// + e.getMessage());
		} finally {
			// logger.info(name + "<<" + file.getName() + ">>记录条数为：" + sum);
			// reader.close();
			// list = null;
			// file = null;
			// reader.close();
		}
	}

}
