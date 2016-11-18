package cn.com.szgao.lm.test_c;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.AdministrationUtils;
import cn.com.szgao.wash.data.DataUtils;

public class TestReadFile {

	// 日志对象
		private static Logger log = LogManager.getLogger(TestReadFile.class);
		private static AdministrationUtils util = new AdministrationUtils();
		private static int count=0;
		private static Connection conn=null;
		
		static
		{
			DataUtils connUtil = new DataUtils();
			try {
			    conn = null;
				conn = connUtil.getConnection();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	/**
	 * @param args
	 * @author liuming
	 * @throws SQLException 
	 * @Date 2015-10-30 下午5:57:39
	 */
	public static void main(String[] args) throws SQLException {
		long startTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年-MM月dd日-HH时mm分ss秒");
		PropertyConfigurator
		.configure("D:\\workSpace2\\workSpace\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		AdministrationUtils util = new AdministrationUtils();
		// 查询行政区
		util.initData();
		
		String filepath = "E:\\work\\Data";
		readfile(filepath);
		
		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		System.out.println("结束时间--------------------"
				+ formatter.format(endDate));
		System.out.println("Took : "
				+ (float) ((endTime - startTime) / 1000) + "秒");
		System.out.println("Took : "
				+ (float) ((endTime - startTime) / 1000) / 60 + "分钟");
		System.out.println("Total : " + count);
		System.out
				.println("Took : "
						+ (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60)
						+ "小时");
		System.out
				.println("Speed : "
						+ (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
						+ "个/小时");
	}

	public static void readfile(String filepath) throws SQLException {
		String sqlInsert="INSERT INTO court_t(court_name,province,city,area,court_name2,create_by,modify_date)VALUES(?,?,?,?,?,'lm',LOCALTIMESTAMP)";
		
		PreparedStatement stmtInsert = null;
		String tempCourtName="";
		try {
			stmtInsert = conn.prepareStatement(sqlInsert);
		}catch (SQLException e1) {
			e1.printStackTrace();
		}
		File file = new File(filepath);
		if (!file.isDirectory()) {
//			System.out.println("文件");
			System.out.println("path=" + file.getPath());
			System.out.println("absolutepath=" + file.getAbsolutePath());
			System.out.println("name=" + file.getName());
		} 
		else if (file.isDirectory()) {
			count++;
//			System.out.println("文件夹");
			System.out.println(file.getName());
			log.info(file.getName());
			tempCourtName=file.getName();
			System.out.println(file.getName());
			stmtInsert.setString(5, file.getName());
			tempCourtName=file.getName();
			//处理法院
			//去空格
			tempCourtName = StringUtils.removeBlank(tempCourtName);
			//处理特殊字符
			tempCourtName = StringUtils.removeSpecialCharacters(tempCourtName);
			//去标点
			tempCourtName = StringUtils.removePunctuation(tempCourtName);
			stmtInsert.setString(1, tempCourtName);
			
			// array[0]省，array[1]地级市，array[2]县级市、县、区
			String[] array = util.utils(tempCourtName);
			if(!StringUtils.isNull(array[0])){
				stmtInsert.setString(2, array[0]);
			}else{
				stmtInsert.setString(2, null);
			}
			if(!StringUtils.isNull(array[1])){
				stmtInsert.setString(3, array[1]);
			}else{
				stmtInsert.setString(3, null);
			}
			if(!StringUtils.isNull(array[2])){
				stmtInsert.setString(4, array[2]);
			}else{
				stmtInsert.setString(4, null);
			}
			stmtInsert.execute();
			
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath + "//" + filelist[i]);
				if (!readfile.isDirectory()) {
//					System.out.println("path=" + readfile.getPath());
//					System.out.println("absolutepath="
//							+ readfile.getAbsolutePath());
//					System.out.println("name=" + readfile.getName());
				} else if (readfile.isDirectory()) {
					readfile(filepath + "//" + filelist[i]);
//					System.out.println(readfile.getName());
				}
			}
		}
	}

}
