package cn.com.szgao.enterprise;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.PostgresqlUtils;
//import cn.com.szgao.dto.CustomsVO;
//import cn.com.szgao.dto.EtpEventItemVO;
//import cn.com.szgao.dto.TempVo;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
//import cn.com.szgao.wash.company.BusinessDirectory_Main;
import cn.com.szgao.wash.data.DataUtils;
import net.sf.json.JSONObject;


/**
 * 将股东信息写入PG， 两两，或三三
 * @author liuming
 * @Date 2016年7月15日 下午4:11:55
 */
public class InsertCompanyHolderFromJsonToPGBatch {
	
	
	private static Logger log = LogManager.getLogger(InsertCompanyHolderFromJsonToPGBatch.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(InsertCompanyHolderFromJsonToPGBatch.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {
//		File file = new File("C:\\data\\失信\\shixin20160111\\500000至1000000\\500000至1000000.json");
		
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 文件
		File file = new File("E:\\刘铭\\data\\合并各省\\安徽省-final.tsv");
		
		
		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();
			show(file);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
			log.info("路径：" + file.getPath());
			log.info("开始时间--------------------" + formatter.format(startTime));
			log.info("结束时间--------------------" + formatter.format(endDate));
			log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
			log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
			log.info("Total : " + count);
			log.info("Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
			log.info("Speed : " + (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
					+ "个/小时");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void readFile(File file){
		
		
		int flag = 0;
		int countM = 0;
		int countNull = 0;
		int size = 1000;
		long startTime = System.currentTimeMillis();
		long startTime2 = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");

		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtInsert = null;
		String sqlInsert = "INSERT INTO holder_group_t(holders,company_id,company,reg_um,batch_num)VALUES(?,?::uuid,?,?,?)";
		
		BufferedReader reader = null;
		try {
			
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
//			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GB18030");
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			conn = PostgresqlUtils.getConnection();
			try {
				conn.setAutoCommit(false);
				stmtInsert = conn.prepareStatement(sqlInsert);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			String temp = null;
			
			BreakFaithVO vo = null;
			Gson gs = new Gson();
			JSONObject temJson = null;
			String key1 = "";
			String key2 = "";
			while ((temp = reader.readLine()) != null) {
				
				count++;
				countM++;
//				System.out.println(count);
				
				try {
//					System.out.println(temp);
					String temp_str[]=temp.split("	");//﻿15566/中华人民共和国居民身份证	91340100783080413Y	安徽昆鹏建筑安装有限公司	63ca9d8b-ec94-547e-83c2-86215013a7b2	工商-Json-0001.txt
					for (int i = 0; i < temp_str.length; i++) {
						String str  = temp_str[i];
						System.out.println(str);
						if(i==0){//股东
							String holders=temp_str[i]!=null?temp_str[i].trim():null;
							
							
							if(!StringUtils.isNull(holders)){
								
//								if(holders.indexOf("中华人民共和国居民身份证")!=-1){   暂缺  15566/中华人民共和国居民身份证
//									continue;
//								}
								
								stmtInsert.setString(1, holders);
							}else{
								stmtInsert.setString(1, null);
							}
						}else if(i==1){//注册号
							String temp_1=temp_str[i]!=null?temp_str[i].trim():null; 
							if(!StringUtils.isNull(temp_1)){
								stmtInsert.setString(4, temp_1);
							}else{
								stmtInsert.setString(4, null);
							}
						}else if(i==2){//名称
							String temp_1=temp_str[i]!=null?temp_str[i].trim():null; 
							if(!StringUtils.isNull(temp_1)){
								stmtInsert.setString(3, temp_1);
							}else{
								stmtInsert.setString(3, null);
							}
						}else if(i==3){//企业ID
							String temp_1=temp_str[i]!=null?temp_str[i].trim():null; 
							if(!StringUtils.isNull(temp_1)){
								stmtInsert.setString(2, temp_1);
							}else{
								stmtInsert.setString(2, null);
							}
						}else if(i==4){//批号
							String temp_1=temp_str[i];
							if(!StringUtils.isNull(temp_1)){
								stmtInsert.setString(5, temp_1);
							}else{
								stmtInsert.setString(5, null);
							}
						}
					}
					
					stmtInsert.addBatch();
					flag++;
					
					if (flag >= size) {
						stmtInsert.executeBatch();
						conn.commit();
						stmtInsert.clearBatch();
						flag = 0;
						conn.setAutoCommit(false);
						stmtInsert = conn.prepareStatement(sqlInsert);
						logger.info("----写入PG库成功");
					}

					if (countM % size == 0) {

						long endTime = System.currentTimeMillis();
						String result = (float) ((endTime - startTime2) / 1000) + "秒";
						logger.info("----  " + countM / size + " 批次" + "  " + result + " 每批:" + size + "  总数:  "
								+ countM);
						startTime2 = System.currentTimeMillis();
					}
					System.out.println(countM);
					
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}

			stmtInsert.executeBatch();
			conn.commit();
			stmtInsert.clearBatch();
			stmtInsert.close();
			conn.close();

		} catch (ClassNotFoundException e) {
			logger.info(e);
		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (stmtInsert != null) {
					stmtInsert.close();
				}
				if (conn != null) {
					conn.close();
				}
				
				logger.info("结束!!!!!!!!!!!");
				logger.info("空数据" + countNull);

				long endTime = System.currentTimeMillis();
				Date endDate = new Date(endTime);
				System.out.println("结束时间--------------------" + formatter.format(endDate));
				System.out.println("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
				System.out.println("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
				System.out.println("Total : " + count);
				System.out.println(
						"Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
				System.out.println("Speed : "
						+ (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60)) + "个/小时");

			} catch (Exception e) {
				logger.info(e);
			}
		}
	}
	
	
	public static void show(File file) throws IOException, ParseException {
		if (file.isFile()) {
			try {
				readFile(file);
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
					try {
						readFile(fi);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					show(fi);
				} else {
					continue;
				}
			}
		}
	}
	
	

}
