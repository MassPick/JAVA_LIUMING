package cn.com.szgao.clean.notice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.BlobUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.wash.data.AdministrationUtils;

/**
 * 将html\pdf上传到blob             公告HTML\pdf上传
 * 
 * @author liuming
 * @Date 2016年7月7日 下午7:15:08
 */
public class InseartCourtPubToBlob {
	
	
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(InseartCourtPubToBlob.class.getName());
	private static Logger log = LogManager.getLogger(InseartCourtPubToBlob.class.getName());
	// static Map<String, List<RecordData>> MAPS = new HashMap<String,
	// List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //
	static long REPEATSUM = 0; // 去重后数据条数
	static long SUM = 0;
	static long count = 0;

	static AdministrationUtils util;

	 
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 导入文件地址

//		File file = new File("E:\\法院公告\\2016-03-23~2016-04-05\\html\\000a9a48-664f-58a0-bc0a-6d0eca8fa9f9.html");
		File file = new File("D:\\法院公告");
		

		// util = new AdministrationUtils();
		// util.initData(); // 查询行政区

//		Bucket bucket = null;

		// bucket = connectionBucket(bucket);
		try {

			CloudStorageAccount storageAccount = CloudStorageAccount.parse(BlobUtils.storageConnectionString);
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			CloudBlobContainer container = blobClient.getContainerReference("courtpub");

			try {
				show(file, container);
			} catch (Throwable e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			file = null;
			// bucket.close();
			cluster2 = null;
		}
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		log.info("开始时间--------------------" + formatter.format(startTime));

		log.info("结束时间--------------------" + formatter.format(endDate));

		log.info("JSON没有的案由数:  " + caseNum + "html解析出的: " + caseNum_Y1 + "html没解析出的: " + caseNum_N1);
		log.info("JSON有的案由数匹配数据的:  " + caseNum_Y + "JSON有的案由数没匹配数据的: " + caseNum_N);

		log.info("Time : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
		log.info("Time : " + (float) ((float) ((endTime - startTime) / 1000) / 60) + "分钟");
		log.info("Time : " + (float) ((endTime - startTime) / 1000) + "秒");
		log.info("Total : " + countNum);
		log.info("Speed : " + (float) (countNum / (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
				+ "个/小时");
		log.info("Speed : " + (float) (countNum / (float) ((float) ((endTime - startTime) / 1000) / 60)) + "个/分种");
		log.info("Speed : " + (float) (countNum / (float) ((endTime - startTime) / 1000)) + "个/秒");

		log.info("找不到Html的数量  : " + countNotHtml);

		// record();
		System.exit(0);
	}

	// 连接CB
	private static Bucket connectionBucket(Bucket bucket) {
		try {
			bucket = connectionCouchBaseLocal();// 本地CB
		} catch (Exception e) {
			while (true) {
				try {
					bucket = connectionCouchBaseLocal();// 本地CB
					break;
				} catch (Exception ee) {
					logger.error(ee);
				}
			}
		}

		return bucket;
	}

	/**
	 * 递归遍历文件
	 * 
	 * @param file
	 * @throws @throws
	 *             Exception
	 */
 //	static // private static void show(File file, Bucket bucket) throws Exception {
	// if (file.isFile()) {
	// if ( file.getPath().indexOf("html") != -1 )
	// {
	// long da = System.currentTimeMillis();
	// create(file, bucket);
	// System.out.println("读取<<" + file.getPath() + ">>文件耗时" +
	// (System.currentTimeMillis() - da) + "毫秒");
	// return;
	// }
	//
	// }
	// File[] files = file.listFiles();
	// if(files.length>0){
	// System.out.println("----files 第一个：---" + files[0].getPath());
	// }
	//
	//
	// for (File fi : files) {
	// if (fi.isFile()) {
	// if (fi.getName().contains("download_fail")) {
	// continue;
	// }
	// if (fi.getPath().indexOf("html") != -1 ) {
	// long da = System.currentTimeMillis();
	//// String name = fi.getParentFile().getPath();
	//// name = name.substring(name.lastIndexOf("\\") + 1, name.length());
	// create(fi, bucket);
	// System.out.println(
	// "读取" + fi.getPath() + "<<" + fi.getName() + ">>文件耗时" +
	// (System.currentTimeMillis() - da) + "毫秒");
	// }else{
	// break;
	// }
	// } else if (fi.isDirectory()) {
	// // 只认带url的
	// show(fi, bucket);
	//
	// } else {
	// continue;
	// }
	// }
	// }

	static int SUMM = 0;

	public static void show(File file, CloudBlobContainer container) throws Throwable {
		 
		if (file.isFile()) {

			// System.out.println("数量:"+thredSum+"---线程名"+Thread.currentThread().getName());
			try {
				String hz=file.getPath().substring(file.getPath().lastIndexOf(".")+1) ;
//				if("html".equals(hz)  || "pdf".equals(hz)  ){
					if(  "pdf".equals(hz)  ){
					upload(file, container);
				}
			
				
				System.out.println(SUMM++);
				// thredSum += 1;
			} catch (Exception e) {
				log.error(e.getMessage() + ":" + file.getName());
			}
			return;
		}
		File[] files = file.listFiles();
		for (File fi : files) {
			if (fi.isFile()) {
				// System.out.println("数量:"+thredSum+"---线程名"+Thread.currentThread().getName());
				try {
					
//					upload(fi, container);
					String hz=fi.getPath().substring(fi.getPath().lastIndexOf(".")+1) ;
					System.out.println(SUMM++  +"   "+hz );
//					if("html".equals(hz)  || "pdf".equals(hz) ){
					if(  "pdf".equals(hz) ){
						upload(fi, container);
					}
//					else{
//						break;
//					}
					
					// thredSum += 1;
				} catch (Exception e) {
					log.error("异常1:" + e.getMessage() + ":" + file.getName());
				}
			} 
//			else if(fi.getName().indexOf("pdf")!=-1){
//				break;
//			}
			else if (fi.isDirectory()) {
				show(fi, container);
			} else {
				continue;
			}
		}
	}

	static int countNum = 0;
	static int countNotHtml = 0;

	/**
	 * 生成文件的个数
	 */
	static int countP = 0;
	static BufferedWriter fwUn = null;
	static BufferedWriter fwUn2 = null;

	/**
	 * 写数据
	 * 
	 * @param <JSONObject>
	 * @param file
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	private static <ObjectDataVO, JSONObject> void create(File file, Bucket bucket)
			throws Exception, UnsupportedEncodingException {

		String name = file.getParentFile().getPath();
		name = name.substring(name.lastIndexOf("\\") + 1, name.length());
		try {

			// list = removeDuplicate(list); // 去除本次集合中重复数据
			// sum = list.size();
			// boolean result = createJsonToCB(list, bucket);
			// REPEATSUM += list.size();
			// if (!result) {
			// logger.error("读取" + name + "<<" + file.getName() +
			// ">>文件时发生JSON异常!");
			// }
			// temp = null;
			// list = null;
			// list = new ArrayList<WholeCourtVO>();
			// statisticalCount(file, sum);
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

	static Map<String, String> MAPS = new HashMap<String, String>();

	static {
		MAPS.put("html", "html");
		MAPS.put("htm", "htm");
		MAPS.put("doc", "doc");
		MAPS.put("swf", "swf");
		MAPS.put("pdf", "pdf");
	}

	public static void upload(File file, CloudBlobContainer container) throws Throwable {
		CloudBlockBlob blob = null;
		String str = "";
		try {
			if (file.isFile()) {
				String suffix = file.getName();
				suffix = suffix.substring(suffix.indexOf(".") + 1, suffix.length());
				suffix = MAPS.get(suffix);
				if (null == suffix) {
					return;
				}
				if (suffix == "doc" && "doc".equals(suffix)) {
					str = "application/msword";
				} else if (suffix == "pdf" && "pdf".equals(suffix)) {
					str = "application/pdf";
				} else if (suffix == "swf" && "swf".equals(suffix)) {
					str = "application/x-shockwave-flash";
				} else if (suffix == "html" && "html".equals(suffix)) {
					str = "text/html";
				} else if (suffix == "htm" && "htm".equals(suffix)) {
					str = "text/html";
				}
				// blob = container.getBlockBlobReference(file.getName());
//				System.out.println(file.getPath());
//				System.out.println(file.getName());
				blob = container.getBlockBlobReference( file.getName() );
				blob.getProperties().setContentType(str);
				blob.uploadFromFile(file.toString());
				// blob.upload(new FileInputStream(file), file.length());
				count++;
				log.info("第：" + count + "条数据的ID为：" + file.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			blob = null;
			file = null;
		}
	}

	static int caseNum = 0;
	static int caseNum_Y = 0;
	static int caseNum_N = 0;
	static int caseNum_Y1 = 0;
	static int caseNum_N1 = 0;

	// /**
	// * 统计导入的各地的记录条数
	// */
	// public static void statisticalCount(File file, long count) {
	// // 取省名
	// String provinceName = file.getParentFile().getParent();
	// provinceName = provinceName.substring(provinceName.lastIndexOf("\\") + 1,
	// provinceName.length());
	// // 取市名
	// String city = file.getParentFile().getPath();
	// city = city.substring(city.lastIndexOf("\\") + 1, city.length());
	// List<RecordData> list = MAPS.get(provinceName);
	// if (null == list || list.size() <= 0) {
	// list = new ArrayList<RecordData>();
	// list.add(new RecordData(provinceName, city, count));
	// MAPS.put(provinceName, list);
	// } else {
	// boolean result = true;
	// for (RecordData re : list) {
	// if (re.getCityName().equalsIgnoreCase(city)) {
	// re.setNumberData(re.getNumberData() + count);
	// result = false;
	// break;
	// }
	// }
	// if (result) {
	// list.add(new RecordData(provinceName, city, count));
	// MAPS.put(provinceName, list);
	// }
	// }
	// }

	// /**
	// * 记录各地数据
	// */
	// public static void record() {
	// long sumCount = 0;
	// long sum = 0;
	// for (Map.Entry<String, List<RecordData>> map : MAPS.entrySet()) {
	// logger.info("###:" + map.getKey());
	// List<RecordData> list = map.getValue();
	// sum = 0;
	// for (RecordData recordData : list) {
	// logger.info("###:" + recordData.getCityName() + "----记录条数:" +
	// recordData.getNumberData());
	// sum += recordData.getNumberData();
	// }
	// sumCount += sum;
	// logger.info(map.getKey() + "省总数据条数据:" + sum);
	// logger.info("------------------------------");
	// }
	// logger.info("总文件数据条数:" + sumCount);
	// logger.info("去重后的数据条数据:" + REPEATSUM);
	// logger.info("错误数据条数:" + ERRORSUM);
	// }

	/**
	 * 根据UUID去重
	 * 
	 * @param list
	 * @return
	 */
	public static List<WholeCourtVO> removeDuplicate(List<WholeCourtVO> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getWholeCourtId().equals(list.get(i).getWholeCourtId())) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	/**
	 * 连接postgreSql库
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb";
		String usr = "postgres";
		String psd = "615601.xcy*";
		Connection conn = null;
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, usr, psd);
		return conn;
	}

	/**
	 * 链接couchbase桶
	 * 
	 * @return
	 */
	public static Bucket connectionCouchBaseLocal() {
		// 连接指定的桶
		return cluster2.openBucket("court_New", 1, TimeUnit.MINUTES);
	}

	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + System.getProperty("line.separator"));

			// fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			log.error("写文件异常" + e.getMessage());
		} finally {
			// if (fw != null) {
			// try {
			// fw.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}

	// public static void writerString2(String str) {
	// try {
	// BufferedWriter fwUn = null;
	// if (countNum == 0 || countNum == 1 || countNum % 10000 == 0) {
	// countP++;
	// String folderPathUn = "D:/lm/log/2016法院清洗后数据3";
	// String filePathUn = "D:\\lm\\log\\2016法院清洗后数据3\\" + (countP) + ".json";
	// // 创建文件夹
	// FileUtils.newFolder(folderPathUn);
	// File fileSUn = new File(filePathUn);
	// String encoding_from1U = "UTF-8";
	//
	// try {
	// if (!fileSUn.exists()) {
	// try {
	// fileSUn.createNewFile();
	// } catch (IOException e) {
	// e.printStackTrace();
	// log.error(e);
	// }
	// fwUn = new BufferedWriter(
	// new OutputStreamWriter(new FileOutputStream(fileSUn, true),
	// encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
	// } else {
	// fileSUn.delete();
	// fileSUn = new File(filePathUn);
	// fwUn = new BufferedWriter(
	// new OutputStreamWriter(new FileOutputStream(fileSUn, true),
	// encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
	// }
	// } catch (FileNotFoundException e1) {
	// e1.printStackTrace();
	// }
	// }
	//
	// fwUn.append(str + System.getProperty("line.separator"));
	// // fw.newLine();
	// fwUn.flush(); // 全部写入缓存中的内容
	// } catch (Exception e) {
	// log.error("写文件异常" + e.getMessage());
	// } finally {
	// // if (fw != null) {
	// // try {
	// // fw.close();
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	// // }
	// }
	// }

	private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
}
