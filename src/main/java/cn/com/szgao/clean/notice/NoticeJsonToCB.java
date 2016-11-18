package cn.com.szgao.clean.notice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

//import cn.com.szgao.court.esAndcb.CommonConstant;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.NoticeVO;
import cn.com.szgao.util.StringUtils;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

/**
 * 裁判文书json文件写入postgreSql和couchbase库
 * @author Administrator
 */
public class NoticeJsonToCB {
	private static Logger logger = LogManager.getLogger(NoticeJsonToCB.class.getName());
	static Map<String, List<RecordData>> MAPS = new HashMap<String, List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //写入数据量
	static long REPEATSUM = 0; // 去重后数据条数
	static long count = 0;
	/**
	 * 裁判文书 数据写库PostgreSql和couchbase JSON导入extracl_url_t表和court桶
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(
				"F:\\My\\Mass\\Mass\\log\\log4j.properties");
		// 导入文件地址
		File file = new File("D:\\Notice\\法院公告\\QG\\json");
		Bucket bucket = null;
		bucket = connectionBucket(bucket);
		try {
			show(file, bucket);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			file = null;
			bucket.close();
		}
		// logger.info("数量："+count);
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");
		record();
	}

	// 连接CB
	private static Bucket connectionBucket(Bucket bucket) {
		try {
			bucket = connectionCouchBase();// 连接服务器CB
		} catch (Exception e) {
			while (true) {
				try {
					bucket = connectionCouchBase();
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
		try {
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
					count = 0;
					long da = System.currentTimeMillis();
					String name = fi.getParentFile().getPath();
					name = name.substring(name.lastIndexOf("\\") + 1, name.length());
					create(fi, bucket);
					logger.info(
							"读取" + name + "<<" + fi.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
				} else if (fi.isDirectory()) {
					show(fi, bucket);
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		NoticeVO notice = null;
		List<NoticeVO> list = new ArrayList<NoticeVO>();
		String temp = null;
		int sum = 0;
		int count = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((temp = reader.readLine()) != null) {
				notice = gson.fromJson(temp, NoticeVO.class);
				notice.setUuid(StringUtils.getUUID(notice.getDetailLink().toString()));
				list.add(notice);
				list = NoticeJsonToCB.removeDuplicate(list); // 方法去重
				if (list.size() >= 1000) {
					count++;
					sum = sum + list.size();
					boolean result = createJsonPostgreSQL(list, bucket);
					REPEATSUM += list.size();
					if (!result) {
						logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生JSON异常!");
						ERRORSUM += sum;
					}
					logger.info("第：" + count + "批数据：" + list.size());
					notice = null;
					temp = null;
					list = null;
					list = new ArrayList<NoticeVO>();
				}
			}
			if (list.size() >= 0) {
				count++;
				sum = sum + list.size();
				boolean result = NoticeJsonToCB.createJsonPostgreSQL(list, bucket);
				REPEATSUM += list.size();
				if (!result) {
					logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生JSON异常!");
					ERRORSUM += sum;
				}
				logger.info("第：" + count + "批数据：" + list.size());
				temp = null;
				list = null;
				list = new ArrayList<NoticeVO>();
			}
			statisticalCount(file, sum);
		} catch (Exception e) {
			logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生IO异常:" + e.getMessage());
		} finally {
			logger.info(name + "<<" + file.getName() + ">>记录条数为：" + sum);
			reader.close();
			file = null;
			reader.close();
		}
	}

	/**
	 * 入库couchbase
	 * 
	 * @param arch
	 * @param urlId
	 * @return
	 * @throws Exception
	 */
	public static boolean createJsonPostgreSQL(List<NoticeVO> list, Bucket bucket) throws Exception {
		JsonDocument doc = null;
		String urlId = null;
		Gson gson = new Gson();
		if (list.size() >= 0) {
			count++;
		}

		try {
			for (int i = 0; i < list.size(); i++) {
				urlId = list.get(i).getUuid();
				String jsonss = gson.toJson(list.get(i));
				doc = JsonDocument.create(urlId, JsonObject.fromJson(jsonss));
				// logger.info("-------数据："+doc);
				bucket.upsert(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			logger.info("----第:" + count + "批次:" + list.size() + "条数据");
		}
		return true;
	}


	/**
	 * 案号清洗
	 * 
	 * @param value
	 * @return
	 */
	public static String replaceAllCaseNum(String value) {
		if (value == null && "".equals(value)) {
			return null;
		}
		value = value.replaceAll("[(,（,〔,【]", "(");
		value = value.replaceAll("[),）,﹞,】]", ")");
		value = value.replaceAll("]", ")");
		value = value.replaceAll("[", "(");
		value = value.trim();
		return value;
	}

	/**
	 * 把字符串由全角转成半角
	 * 
	 * @param doString
	 *            全角字符串
	 * @return 返回全角字符串对应的半角字符串
	 * @since 2015-8-10
	 */
	public static String full2HalfChange(String doString) {
		if (null == doString) {
			return null;
		}
		StringBuffer outStrBuf = new StringBuffer("");
		String Tstr = "";
		byte[] b = null;
		for (int i = 0; i < doString.length(); i++) {
			Tstr = doString.substring(i, i + 1);
			// 全角空格转换成半角空格
			if (Tstr.equals("　")) {
				outStrBuf.append(" ");
				continue;
			}
			try {
				b = Tstr.getBytes("unicode");
				// 得到 unicode 字节数据
				if (b[2] == -1) {
					// 表示全角
					b[3] = (byte) (b[3] + 32);
					b[2] = 0;
					outStrBuf.append(new String(b, "unicode"));
				} else {
					outStrBuf.append(Tstr);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return outStrBuf.toString();
	}

	/**
	 * 统计导入的各地的记录条数
	 */
	public static void statisticalCount(File file, long count) {
		// 取省名
		String provinceName = file.getParentFile().getParent();
		provinceName = provinceName.substring(provinceName.lastIndexOf("\\") + 1, provinceName.length());
		// 取市名
		String city = file.getParentFile().getPath();
		city = city.substring(city.lastIndexOf("\\") + 1, city.length());
		List<RecordData> list = MAPS.get(provinceName);
		if (null == list || list.size() <= 0) {
			list = new ArrayList<RecordData>();
			list.add(new RecordData(provinceName, city, count));
			MAPS.put(provinceName, list);
		} else {
			boolean result = true;
			for (RecordData re : list) {
				if (re.getCityName().equalsIgnoreCase(city)) {
					re.setNumberData(re.getNumberData() + count);
					result = false;
					break;
				}
			}
			if (result) {
				list.add(new RecordData(provinceName, city, count));
				MAPS.put(provinceName, list);
			}
		}
	}

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
	 * 根据UUID去重 已废弃
	 * 
	 * @param li
	 * @return
	 */
	public static List<NoticeVO> getNewList(List<NoticeVO> li) {
		List<NoticeVO> list = new ArrayList<NoticeVO>();
		for (int i = 0; i < li.size(); i++) {
			NoticeVO str = li.get(i); // 获取传入集合对象的每一个元素
			if (!list.contains(str)) { // 查看新集合中是否有指定的元素，如果没有则加入
				list.add(str);
			}
		}
		return list; // 返回集合
	}

	/**
	 * 根据UUID去重
	 * 
	 * @param list
	 * @return
	 */
	public static List<NoticeVO> removeDuplicate(List<NoticeVO> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getUuid().equals(list.get(i).getUuid())) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	private static Cluster cluster = CouchbaseCluster.create("192.168.1.30");

	public static Bucket connectionCouchBase() {
		// 连接指定的桶
		return cluster.openBucket("courtPub2", 1, TimeUnit.MINUTES);
	}
}
