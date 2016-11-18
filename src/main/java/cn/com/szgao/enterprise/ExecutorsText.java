package cn.com.szgao.enterprise;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.couchbase.client.java.Bucket;

import cn.com.szgao.dto.IndustryVO;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.ExcelUtils;
import cn.com.szgao.util.PrCiCouText;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.DataUtils;

/**
 * 深圳
 * 
 * @author xiongchangyi
 *
 */
public class ExecutorsText {

	public static Bucket bucket = null;

	/**
	 * N名词 in_name,industry_name
	 */
	public static Map<String, Object> mapN = null;
	/**
	 * V动词  in_name,industry_name
	 */
	public static Map<String, Object> mapV = null;
	
	/**
	 * 标准行业  industry_name  ----vo
	 */
	public static Map<String, IndustryVO> mapVN_N = null;
	public static Map<String, IndustryVO> mapN_N = null;
	public static Map<String, IndustryVO> mapV_N = null;
	
	
	
	/**
	 * 词条所有
	 */
	public static Map<String, IndustryVO> mapV_AND_N = null;
	
	
	
	
	
	// 日志对象
	public static final Logger log = LogManager.getLogger(FileIntoDataBase2.class);
	static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	static String usr = "postgres";
	static String psd = "615601.xcy*";
	public static String[] ERCOEDING = { "й", "෨", "Ժ", "ۼ", "ҩ", "ල", "ɷ", "ص", "δ", "ġ", "Ϊ", "ط", "Ϣ", "ȡ", "Ӫ", "ã",
			"", "�" };

	static {
		try {
			Class.forName("org.postgresql.Driver");
			PrCiCouText.connection = DriverManager.getConnection(url, usr, psd);
			DataUtils.initData();

			// 加载动名词
			// File file = new
			// File("D:\\data\\mavenSpace6_1\\Mass\\data\\名词＋动词的结果.xlsx");

			try {
				// mapN = ExcelUtils.getListFromExcelByLineTwoColumn(file, 0, 1,
				// 0, 4);
				// mapV = ExcelUtils.getListFromExcelByLineTwoColumn(file, 0, 1,
				// 0, 4);

				mapN = new DataUtils().listIndustryN();
				mapV = new DataUtils().listIndustryV();
				
				mapVN_N=new DataUtils().listIndustryVAndN_New();
				mapN_N=new DataUtils().listIndustryN_New();
				mapV_N=new DataUtils().listIndustryV_New();
				
				mapV_AND_N=new DataUtils().listIndustryVANDN_New();
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("加载动名词");

			////////////------------------读某企业的行业
//			while (true) {
//				try {
//					bucket = CouchbaseConnect.commonBucket("192.168.1.30:8091", "businessD");
//					break;
//				} catch (Exception e) {
//					log.info("---------------------------> 连BC超时");
//					log.error(e.getMessage());
//				}
//			}
			
			
			
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PrCiCouText.init();
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			FileIntoDataBase2p5 ff = new FileIntoDataBase2p5();
			
//			ff.show(new File("E:/刘铭/data/160617后补数据/工商-广东-0001.txt"), 0);
//			ff.show(new File("E:/刘铭/data/160530与小数据企业名对比等数据/工商-Json-0001/工商-Json-0001.txt"), 0);//------------------------liu 14
			
//			ff.show(new File("E:/刘铭/data/160617后补数据"), 0);//------------------------liu 14
//			ff.show(new File("E:/刘铭/data/160617后补数据/工商-special-0001.txt"), 0);
//			ff.show(new File("E:/刘铭/data/江苏省/工商-江苏省-0026.txt"), 38510);
//			ff.show(new File("E:/刘铭/data/160530与小数据企业名对比等数据/工商-小数据2-0001.txt"), 2217);//------------------------liu 14
			
//			ff.show(new File("E:/刘铭/data/辽宁省/工商-辽宁省-0002.txt"), 47038);
			
//			ff.show(new File("E:/工商数据已去重/北京市180000.json"), 0);//数量:34431文件： E:\工商数据已去重\北京市180000.json
			
//			ff.show(new File("E:/刘铭/data/北京市/工商-北京市-0023.txt"),40541);
			
			ff.show(new File("E:\\刘铭\\data\\合并各省\\台湾省.json"),0);
			
//			ff.show(new File("E:/刘铭/data/吉林省"), 0);//------------------------liu 14
//			ff.show(new File("E:/刘铭/data/宁夏"), 0);
//			ff.show(new File("E:/刘铭/data/青海省"), 0);
//			ff.show(new File("E:/刘铭/data/西藏"), 0);
//			ff.show(new File("E:/刘铭/data/总局"), 0);
//			ff.show(new File("E:/刘铭/data/160407工商"), 0);
//			ff.show(new File("E:/刘铭/data/160530与小数据企业名对比等数据"), 0);
//			ff.show(new File("E:/刘铭/data/160617后补数据"), 0);
			

//			ff.show(new File("E:/刘铭/data/甘肃省"), 0);
//			ff.show(new File("E:/刘铭/data/广西"), 0);
//			ff.show(new File("E:/刘铭/data/广州市"), 0);
//			ff.show(new File("E:/刘铭/data/安徽省"), 0);
//			ff.show(new File("E:/刘铭/data/北京市"), 0);
//			ff.show(new File("E:/刘铭/data/福建省"), 0);
//			ff.show(new File("E:/刘铭/data/广东省"), 0);
//			ff.show(new File("E:/刘铭/data/深圳市"), 0);
//			
//			ff.show(new File("E:/刘铭/data/山西省"), 0);// ------------ hui 10
//			ff.show(new File("E:/刘铭/data/陕西省"), 0);
//			ff.show(new File("E:/刘铭/data/新疆"), 0);
//			ff.show(new File("E:/刘铭/data/云南省"), 0);
//			ff.show(new File("E:/刘铭/data/山东省"), 0);
//			ff.show(new File("E:/刘铭/data/上海市"), 0);
//			ff.show(new File("E:/刘铭/data/四川省"), 0);
//			ff.show(new File("E:/刘铭/data/重庆市"), 0);
//			ff.show(new File("E:/刘铭/data/天津市"), 0);
//			ff.show(new File("E:/刘铭/data/浙江省"), 0);
//			
//			 ff.show(new File("E:/刘铭/data/内蒙古"), 0);//------------------- changyi 11
//			 ff.show(new File("E:/刘铭/data/贵州省"), 0);
//			 ff.show(new File("E:/刘铭/data/海南省"), 0);
//			 ff.show(new File("E:/刘铭/data/黑龙江省"), 0);
//			 ff.show(new File("E:/刘铭/data/湖北省"), 0);
//			 ff.show(new File("E:/刘铭/data/江西省"), 0);
//			 ff.show(new File("E:/刘铭/data/湖南省"), 0);
//			 ff.show(new File("E:/刘铭/data/辽宁省"), 0);
//			 ff.show(new File("E:/刘铭/data/江苏省"), 0);
//			 ff.show(new File("E:/刘铭/data/河北省"), 0);
//			 ff.show(new File("E:/刘铭/data/河南省"), 0);
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
		
		
		
		
		
		
		
		
		// ScheduledThreadPoolExecutor excuter = new
		// ScheduledThreadPoolExecutor(1);

		// -----------------14个
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/吉林省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/宁夏"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/青海省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/西藏"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/总局"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/160407工商"), 0,
		// new FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/甘肃省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/广西"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/广州市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/安徽省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/北京市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/福建省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/广东省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/深圳市"), 0, new
		// FileIntoDataBase2p5()));

		// ------------------------------

		// excuter.execute(new RunnableJson5(new
		// File("E:/刘铭/data/福建省/工商-福建省-0001.txt"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("E:/刘铭/data/安徽省/工商-安徽省-0001.txt"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("E:/刘铭/data/广东省/工商-广东省-0011.txt"), 0, new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new
		// File("E:/刘铭/data/北京市/工商-北京市-0023.txt"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("E:/刘铭/data/广西/工商-广西-0008.txt"), 30567, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("E:/刘铭/data/福建省/工商-福建省-0008.txt"), 0, new
		// FileIntoDataBase2p5()));//详情不全
		// excuter.execute(new RunnableJson5(new
		// File("E:/刘铭/data/总局/工商-总局-0001.txt"), 2231, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("E:/刘铭/data/甘肃省/工商-甘肃省-0001.txt"), 7073, new
		// FileIntoDataBase2p5())); //7073 工商-甘肃省-0001.txt
		// excuter.execute(new RunnableJson5(new
		// File("E:/刘铭/data/甘肃省/工商-甘肃省-0003.txt"), 13143, new
		// FileIntoDataBase2p5())); //7073 工商-甘肃省-0001.txt

		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/宁夏"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/青海省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/西藏"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/新疆"), 0, new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/海南省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/贵州省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/吉林省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/内蒙古"),0,new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/山西省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/陕西省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/云南省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/总局"),0,new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/安徽省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/北京市"),12,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/福建省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/甘肃省"), 0, new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/广东省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/广西"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/广州市"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/河北省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/河南省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/黑龙江省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/湖北省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/湖南省"),0,new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/江苏省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/江西省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/辽宁省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/山东省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/上海市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/深圳市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/四川省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/天津市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/浙江省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("E:/刘铭/data/重庆市"), 0, new
		// FileIntoDataBase2p5()));

		// excuter.shutdown();

		// test();

	}

	// public static void test() {
	//
	// // File fi=new
	// //
	// File("E:\\Temp_File\\工商数据\\第3批\\深圳市\\深圳正式数据補02#\\深圳\\深圳-深圳市丛风科技有限公司-20151203192839.json");
	//
	// // File fi=new File("E:\\刘铭\\data\\工商-重庆市-0005.txt");
	// // File fi=new File("E:\\刘铭\\广州\\工商-广州市-0007.txt");
	// // File fi=new File("E:\\刘铭\\data\\工商-安徽省-0007.txt"); //312 行政处罚
	// // File fi=new File("E:\\刘铭\\data\\工商-北京市-0013.txt"); //65 66
	// // File fi=new File("E:\\刘铭\\data\\工商-福建省-0007.txt"); //74
	// // File fi=new File("E:\\刘铭\\data\\工商-广东省-0011.txt");
	// File fi = new File("E:\\刘铭\\data\\工商-深圳市-0008.txt");
	// // File fi=new File("E:\\刘铭\\data\\工商-甘肃省-0002.txt");
	//
	// // File fi=new File("E:\\刘铭\\data\\工商-广西-0008.txt");//1164 pledge 年报信息
	// // File fi=new File("E:\\刘铭\\data\\工商-贵州省-0002.txt");
	// // File fi=new File("E:\\刘铭\\data\\工商-河北省-0017.txt");
	//
	// // FileIntoDataBase2p2 file=new FileIntoDataBase2p2(log);
	// // FileIntoDataBase2p3 file=new FileIntoDataBase2p3(log);
	// FileIntoDataBase2p4 file = new FileIntoDataBase2p4(log);
	// try {
	// file.show(fi, 0);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// }

	// 判断是否存在乱码
	public static boolean getErrorCode(String value) {

		// System.out.println(value);
		if (StringUtils.isNull(value)) {
			return false;
		}

		for (String val : ERCOEDING) {
			int index = value.indexOf(val);
			if (index > 0) {
				return true;
			}
		}
		return false;
	}

}
