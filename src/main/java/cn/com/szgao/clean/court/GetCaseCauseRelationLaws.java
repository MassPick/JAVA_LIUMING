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

import com.couchbase.client.deps.io.netty.util.internal.StringUtil;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

///////\//import cn.com.szgao.court.esAndcb.CommonConstant;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.clean.court.ExtractthepeopleText;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.wash.data.AdministrationUtils;

/**
 * 得到案由与条文的关系
 * 
 * @author liuming
 * @Date 2016年11月1日 上午10:40:20
 */
public class GetCaseCauseRelationLaws {
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(GetCaseCauseRelationLaws.class.getName());
	private static Logger log = LogManager.getLogger(GetCaseCauseRelationLaws.class.getName());
	static Map<String, List<RecordData>> MAPS = new HashMap<String, List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //
	static long REPEATSUM = 0; // 去重后数据条数
	static long SUM = 0;
	static long count = 0;

	static AdministrationUtils util;

	/**
	 * 裁判文书 数据写库PostgreSql和couchbase JSON导入extracl_url_t表和court桶
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));

		// 导入文件地址

		File file = new File("D:\\lm\\log\\1_20161025072109新版裁判文书网展示文书(以裁判日期查询)_2014年_2.json");

		util = new AdministrationUtils();
		util.initData(); // 查询行政区

		Bucket bucket = null;
		// bucket = connectionBucket(bucket);
		try {
			show(file, bucket);
			
			
			printMap(map1);

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

		record();
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
	private static void show(File file, Bucket bucket) throws Exception {
		if (file.isFile()) {
			long da = System.currentTimeMillis();
			create(file, bucket);
			logger.info("读取<<" + file.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
			return;
		}
		File[] files = file.listFiles();
		System.out.println("----files---" + files);
		for (File fi : files) {
			if (fi.isFile()) {
				if (fi.getName().contains("download_fail")) {
					continue;
				}
				long da = System.currentTimeMillis();
				String name = fi.getParentFile().getPath();
				name = name.substring(name.lastIndexOf("\\") + 1, name.length());
				create(fi, bucket);
				logger.info("读取" + name + "<<" + fi.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
			} else if (fi.isDirectory()) {
				show(fi, bucket);
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

	public static Map<String, HashMap> map1 = new HashMap<String, HashMap>();

	public static void printMap(Map<String, HashMap> mapt) {

		
		String folderPathUn = "D:/lm/log/案由与条文对应";
		String filePathUn = "D:\\lm\\log\\案由与条文对应\\" +  "案由与条文对应_"
				+ DateUtils.getDateyyyyMMddhhmmss() + ".json";
		// 创建文件夹
		FileUtils.newFolder(folderPathUn);
		File fileSUn = new File(filePathUn);
		String encoding_from1U = "UTF-8";

		try {
			if (!fileSUn.exists()) {
				try {
					fileSUn.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
				try {
					fwUn = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				fileSUn.delete();
				fileSUn = new File(filePathUn);
				try {
					fwUn = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		int aa=0;
		
		
		for (Map.Entry<String, HashMap> entry : mapt.entrySet()) {
			System.out.println(aa++);
			String laws = "";
			HashMap<String, String> hm = entry.getValue();
			for (Map.Entry<String, String> entry_t : hm.entrySet()) {
				laws += entry_t.getKey() + "#";
			}

			System.out.println(entry.getKey() + "|" + laws);
			writerString(fwUn, entry.getKey() + "|" + laws);

		}
	}

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

		File htmFile = null;

		String caseCause = null;
		HashMap map_t = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((temp = reader.readLine()) != null) {

				try {
					countNum++;
					System.out.println(countNum);

					archJson = gson.fromJson(temp, WholeCourtVO.class);
//					archJson.setWholeCourtId(StringUtils.getUUID(archJson.getDetailLink().toString()));
					caseCause = archJson.getCaseCause();

					if (StringUtils.isNull(archJson.getApprovalDateY()) || StringUtils.isNull(archJson.getLaws())
							|| StringUtils.isNull(caseCause)) {
						continue;
					} else {
						if ("2014".equals(archJson.getApprovalDateY()) || "2015".equals(archJson.getApprovalDateY())) {

							if (map1.get(caseCause) == null) {
								map_t = new HashMap<String, String>();
							} else {
								map_t = map1.get(caseCause);
							}
							String ss1[] = archJson.getLaws().split("</br>");
							for (int i = 0; i < ss1.length; i++) {
								System.out.println(ss1[i]);
								map_t.put(ss1[i], ss1[i]);
							}
							map1.put(caseCause, map_t);
						}
					}

				} catch (Exception e) {

					continue;
				}

			}

		} catch (Exception e) {
		} finally {
			caseCause = null;
			map_t = null;
		}
	}

	static int caseNum = 0;
	static int caseNum_Y = 0;
	static int caseNum_N = 0;
	static int caseNum_Y1 = 0;
	static int caseNum_N1 = 0;
 

	 
	/**
	 * 记录各地数据
	 */
	public static void record() {
		long sumCount = 0;
		long sum = 0;
		for (Map.Entry<String, List<RecordData>> map : MAPS.entrySet()) {
			logger.info("###:" + map.getKey());
			List<RecordData> list = map.getValue();
			sum = 0;
			for (RecordData recordData : list) {
				logger.info("###:" + recordData.getCityName() + "----记录条数:" + recordData.getNumberData());
				sum += recordData.getNumberData();
			}
			sumCount += sum;
			logger.info(map.getKey() + "省总数据条数据:" + sum);
			logger.info("------------------------------");
		}
		logger.info("总文件数据条数:" + sumCount);
		logger.info("去重后的数据条数据:" + REPEATSUM);
		logger.info("错误数据条数:" + ERRORSUM);
	}

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

	public static void writerString2(String str) {
		try {
			BufferedWriter fwUn = null;
			if (countNum == 0 || countNum == 1 || countNum % 10000 == 0) {
				countP++;
				String folderPathUn = "D:/lm/log/2016法院清洗后数据3";
				String filePathUn = "D:\\lm\\log\\2016法院清洗后数据3\\" + (countP) + ".json";
				// 创建文件夹
				FileUtils.newFolder(folderPathUn);
				File fileSUn = new File(filePathUn);
				String encoding_from1U = "UTF-8";

				try {
					if (!fileSUn.exists()) {
						try {
							fileSUn.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
							log.error(e);
						}
						fwUn = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
					} else {
						fileSUn.delete();
						fileSUn = new File(filePathUn);
						fwUn = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}

			fwUn.append(str + System.getProperty("line.separator"));
			// fw.newLine();
			fwUn.flush(); // 全部写入缓存中的内容
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

	private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
}
