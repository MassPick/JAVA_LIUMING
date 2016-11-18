package cn.com.szgao.clean.court;

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

import cn.com.szgao.util.StringUtils;


/**
 * 清洗法院  JSON 到JSON
 * @author liuming
 * @Date 2016年7月1日 上午11:14:50
 */
public class ExecutorsWashCourtJsonToJson {

	public static Bucket bucket = null;

	public static Map<String, Object> mapN = null;
	public static Map<String, Object> mapV = null;
	
	
	
	// 日志对象
	public static final Logger log = LogManager.getLogger(ExecutorsWashCourtJsonToJson.class);
	static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	static String usr = "postgres";
	static String psd = "615601.xcy*";
	public static String[] ERCOEDING = { "й", "෨", "Ժ", "ۼ", "ҩ", "ල", "ɷ", "ص", "δ", "ġ", "Ϊ", "ط", "Ϣ", "ȡ", "Ӫ", "ã",
			"", "�" };

	 

	public static void main(String[] args) throws InterruptedException {
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			WashCourtJsonToJson ff = new WashCourtJsonToJson();
			ff.show(new File("D:\\法院详细页面(清洗)\\中国裁判文书网展示文书详细页面（旧版高院）\\1_20160623081829.json"), 0);
			log.info("-----完");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
		
		
		
		
		
		
		
		
		// ScheduledThreadPoolExecutor excuter = new
		// ScheduledThreadPoolExecutor(1);

		// -----------------14个
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/吉林省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/宁夏"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/青海省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/西藏"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/总局"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/160407工商"), 0,
		// new FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/甘肃省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/广西"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/广州市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/安徽省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/北京市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/福建省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/广东省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/深圳市"), 0, new
		// FileIntoDataBase2p5()));

		// ------------------------------

		// excuter.execute(new RunnableJson5(new
		// File("D:/工商数据已去重/福建省/工商-福建省-0001.txt"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("D:/工商数据已去重/安徽省/工商-安徽省-0001.txt"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("D:/工商数据已去重/广东省/工商-广东省-0011.txt"), 0, new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new
		// File("D:/工商数据已去重/北京市/工商-北京市-0023.txt"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("D:/工商数据已去重/广西/工商-广西-0008.txt"), 30567, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("D:/工商数据已去重/福建省/工商-福建省-0008.txt"), 0, new
		// FileIntoDataBase2p5()));//详情不全
		// excuter.execute(new RunnableJson5(new
		// File("D:/工商数据已去重/总局/工商-总局-0001.txt"), 2231, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new
		// File("D:/工商数据已去重/甘肃省/工商-甘肃省-0001.txt"), 7073, new
		// FileIntoDataBase2p5())); //7073 工商-甘肃省-0001.txt
		// excuter.execute(new RunnableJson5(new
		// File("D:/工商数据已去重/甘肃省/工商-甘肃省-0003.txt"), 13143, new
		// FileIntoDataBase2p5())); //7073 工商-甘肃省-0001.txt

		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/宁夏"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/青海省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/西藏"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/新疆"), 0, new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/海南省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/贵州省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/吉林省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/内蒙古"),0,new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/山西省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/陕西省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/云南省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/总局"),0,new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/安徽省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/北京市"),12,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/福建省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/甘肃省"), 0, new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/广东省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/广西"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/广州市"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/河北省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/河南省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/黑龙江省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/湖北省"),0,new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/湖南省"),0,new
		// FileIntoDataBase2p5()));

		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/江苏省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/江西省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/辽宁省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/山东省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/上海市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/深圳市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/四川省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/天津市"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/浙江省"), 0, new
		// FileIntoDataBase2p5()));
		// excuter.execute(new RunnableJson5(new File("D:/工商数据已去重/重庆市"), 0, new
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
