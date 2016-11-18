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

/**
 * 读文件夹名
 * @author liuming
 *
 */
public class TestReadFile3 {

	// 日志对象
	private static Logger log = LogManager.getLogger(TestReadFile3.class);
	private static AdministrationUtils util = new AdministrationUtils();
	private static int count = 0;
	private static Connection conn = null;
	private static double sumHtml=0;
	private static double sumCourt=0;

	static {
		DataUtils connUtil = new DataUtils();
		try {
			conn = null;
			conn = connUtil.getConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
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
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		PropertyConfigurator.configure("D:\\workSpace2\\workSpace\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		AdministrationUtils util = new AdministrationUtils();
		// 查询行政区
		util.initData();

		String filepath = "E:\\temp";
//		 String filepath = "E:\\temp\\上海市\\No文档\\上海法院法律文书检索中心\\上海法院法律文书检索中心\\上海法院法院文书检索中心-刑事";

		readfile(filepath);

		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		System.out.println("结束时间--------------------" + formatter.format(endDate));
		
		log.info("文件数： "+sumHtml);
		log.info("文件夹数： "+sumCourt);
		System.out.println("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
		System.out.println("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
		System.out.println("Total : " + count);
		System.out.println("Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
		System.out.println(
				"Speed : " + (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60)) + "个/小时");
	}

	public static void readfile(String filepath) throws SQLException {
		String sqlInsert = "INSERT INTO court2_t(court_name,province,city,area,court_name2,create_by,modify_date)VALUES(?,?,?,?,?,'lm20151126',LOCALTIMESTAMP)";

		PreparedStatement stmtInsert = null;
		String tempCourtName = "";
		try {
			stmtInsert = conn.prepareStatement(sqlInsert);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		File file = new File(filepath);
		if (!file.isDirectory()) {
			// System.out.println("文件");
			System.out.println("path=" + file.getPath());
			System.out.println("absolutepath=" + file.getAbsolutePath());
			System.out.println("name=" + file.getName());
		} else if (file.isDirectory()) {
			count++;
			// System.out.println("文件夹");
			System.out.println(file.getPath());
			log.info(file.getName());
			tempCourtName = file.getName();
//			System.out.println(file.getName());

			if (!StringUtils.isNull(tempCourtName)) {
				if (tempCourtName.length() >= 3) {

					if (!tempCourtName.contains("文档")
							&& tempCourtName.substring(tempCourtName.length() - 2).equals("法院")) {

						stmtInsert.setString(5, file.getName());
						tempCourtName = file.getName();

						// 处理法院
						// 去空格
						tempCourtName = StringUtils.removeBlank(tempCourtName);
						// 处理特殊字符
						tempCourtName = StringUtils.removeSpecialCharacters(tempCourtName);
						// 去标点
						tempCourtName = StringUtils.removePunctuation(tempCourtName);
						// 去数字
						tempCourtName = StringUtils.removeNum(tempCourtName);
						// 去字符
						tempCourtName = StringUtils.removeCharacter(tempCourtName);

						if (!StringUtils.isNull(tempCourtName)) {
							stmtInsert.setString(1, tempCourtName);
							// array[0]省，array[1]地级市，array[2]县级市、县、区
							String[] array = util.utils(tempCourtName);
							if (!StringUtils.isNull(array[0])) {
								stmtInsert.setString(2, array[0]);
							} else {
								stmtInsert.setString(2, null);
							}
							if (!StringUtils.isNull(array[1])) {
								stmtInsert.setString(3, array[1]);
							} else {
								stmtInsert.setString(3, null);
							}
							if (!StringUtils.isNull(array[2])) {
								stmtInsert.setString(4, array[2]);
							} else {
								stmtInsert.setString(4, null);
							}
							System.out.println(sumCourt++);
							stmtInsert.execute();
						}
					}
				}
			}

			int j = 0;
			System.out.println("------------------------------------------------遍历文件开始");
			String[] filelist = file.list();
			System.out.println("------------------------------------------------遍历文件结束");
			sumHtml=sumHtml+filelist.length;
			if (filelist.length > 300) {
				j = 300;

				for (int i = 0; i < 300; i++) {
					try {
						File readfile = new File(filepath + "//" + filelist[i]);
						if (!readfile.isDirectory()) {
							System.out.println("--i "+i);
						} else if (readfile.isDirectory()) {
							System.out.println("--j "+j);
							j =0;
							readfile(filepath + "//" + filelist[i]);
						}
					} catch (Exception e) {
						log.error("取文件夹越界");
					}
					
				}
			}
			
			else {
				for (int a = 0; a < filelist.length; a++) {
					File readfile = new File(filepath + "//" + filelist[a]);
					if (!readfile.isDirectory()) {
						 System.out.println("--a "+a);
					} else if (readfile.isDirectory()) {
						System.out.println("--j "+j);
						j =0;
						readfile(filepath + "//" + filelist[a]);
					}
				}
			}

			System.out.println("---------  " + j);
		}
	}

}
