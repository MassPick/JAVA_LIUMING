package cn.com.szgao.enterprise;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.FirstInstryVO;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.StringUtils;
import rx.Observable;
import rx.functions.Func1;


/**
 * 将经营范围的第一个标点符号的行业写到CB，等计算数量
 * @author liuming
 * @Date 2016年7月21日 下午6:52:26
 */
public class CountFistIndustryToCB {
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(CountFistIndustryToCB.class.getName());
//	static Map<String, List<RecordData>> MAPS = new HashMap<String, List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //
	static long REPEATSUM = 0; // 去重后数据条数
	static long SUM = 0;
	static long count = 0;

	static long SUM_Count = 0;// 总数

	public static void main(String[] args) throws Exception {
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));

		// 导入文件地址

		// 记录 2003777 ---- 1048635

		// ----以下两个存在114 的 courtEJ
		// File file=new File("E:\\中国裁判文书网展示文书详细页面（旧版高院）Excle");
//		File file = new File("F:\\2016的文书清洗结果_n");
//		File file = new File("F:\\2015以前_1_n22");   //--  3618895     715867
		
		File file = new File("D:\\工商数据已去重_new\\rinsecom_new\\1pi\\1.json");   //-- 存在： 4581660  10228198  入库 5646538

		// ----以下两个存在114 的 courtLocal
		// File file3=new File("E:\\法院旧版地方Excle");
		// File file4=new File("E:\\法院旧版地方Json");

		Bucket bucket = null;
		// bucket = connectionBucket(bucket);
		try {
			show(file, bucket);
//			show(file2, bucket);
			// show(file3,bucket);
			// show(file4,bucket);
			//
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			// file = null;
			// bucket.close();
			cluster2 = null;
		}
		
		logger.info("存在： " +EXIST   )  ;
		logger.info("入库： " +INTO   )  ;
		logger.info("总数：   " + SUM_Count);
		
		
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");
		logger.info("每秒: " + (SUM_Count / ((System.currentTimeMillis() - da) / 1000)) + "个");
		
		
		

//		record();
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
			System.out.println("读取<<" + file.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
			return;
		}
		File[] files = file.listFiles();
		if (files.length > 0) {
			System.out.println("----files  第一个：---" + files[0].getPath());
		}
		for (File fi : files) {
			if (fi.isFile()) {
				if (fi.getName().contains("download_fail")) {
					continue;
				}
				long da = System.currentTimeMillis();
				String name = fi.getParentFile().getPath();
				name = name.substring(name.lastIndexOf("\\") + 1, name.length());
				create(fi, bucket);
				System.out.println(
						"读取" + name + "<<" + fi.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
			} else if (fi.isDirectory()) {
				show(fi, bucket);
			} else {
				continue;
			}
		}
	}

	static List<JsonDocument> documents = new ArrayList<JsonDocument>();
	static Bucket bucket = null;
	static Bucket bucket2 = null;
	static JsonObject content = null;
	static long EXIST=0;//
	static long INTO=0;//
	/**
	 * 写数据
	 * 
	 * @param <JSONObject>
	 * @param file
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	private static <ObjectDataVO, JSONObject> void create(File file, Bucket bucket1)
			throws Exception, UnsupportedEncodingException {

		int countLen = 0;

		String name = file.getParentFile().getPath();
		name = name.substring(name.lastIndexOf("\\") + 1, name.length());
		BufferedReader reader = null;
		Gson gson = new Gson();
		FirstInstryVO arch = null;
		EnterpriseVO vo = null;
		FirstInstryVO vo_cb = null;

		// List<WholeCourtVO> list = new ArrayList<WholeCourtVO>();
		String temp = null;
		int sum = 0;
		

		try {

			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		

		try {
//			reader = new BufferedReader(new FileReader(file));
			long startTime = System.currentTimeMillis();
			while ((temp = reader.readLine()) != null) {
				SUM_Count++;
				
				vo = gson.fromJson(temp, EnterpriseVO.class);
				
				if(StringUtils.isNull(vo.getScope())){
					continue;
				}
				
				String indu = removeBlank(vo.getScope());
				if(StringUtils.isNull(indu)){
					continue;
				}
				
				String[] sourceStrArray = indu.split("[、;；。.：:，,]");// 分割出来的字符数组
				String str=sourceStrArray[0];
				
				String temp_T="";
				if(StringUtils.isNull(str)){
					continue;
				}else{
					
					if ("许可经营项目".equals(str) || "经营项目".equals(str)||"一般经营项目".equals(str) ) {
						 if(!StringUtils.isNull(sourceStrArray[1])){
							 temp_T=sourceStrArray[1];
						 }
						
					}else{
						temp_T=str;
					}
				}
				
				if(StringUtils.isNull(temp_T)){
					continue;
				}
				
				String key =StringUtils.NBG.generate(temp_T).toString(); 
				arch = new FirstInstryVO();
				arch.setName(temp_T);
				
				
				
				JsonDocument queryDoc = null;
				JsonObject obj = null;
				JsonDocument doc = null;

 
				obj = JsonObject.fromJson(gson.toJson(arch));
				// 创建JSON文档
				doc = JsonDocument.create(key, obj);

				while (true) {
					try {
						bucket = (Bucket) CouchbaseConnect.commonBucket("192.168.1.114:8091", "firstIndustry");
						queryDoc = ((com.couchbase.client.java.Bucket) bucket).get(key, 60, TimeUnit.MINUTES);
						break;
					} catch (Exception e) {
						logger.info("---------------------------> 连BC超时");
						logger.error(e.getMessage());
					}
				}

				if (null != queryDoc) {// 说明存在
					
					vo_cb =StringUtils.GSON.fromJson(queryDoc.content().toString(), FirstInstryVO.class);
					
					Long ll=vo_cb.getCount();
					ll=ll+1;
					
					arch.setCount(ll);
					obj = JsonObject.fromJson(gson.toJson(arch));
					// 创建JSON文档
					doc = JsonDocument.create(key, obj);
					
					EXIST++;
					System.out.println("存在------： "+EXIST   +" KEY :  "+key  );
					logger.info("-- "+key+ "  "+file.getPath());
					CouchbaseConnect.commonBucket("192.168.1.114:8091", "firstIndustry").upsert(doc);

				} else {// 入库
					arch.setCount((long) 1);
					obj = JsonObject.fromJson(gson.toJson(arch));
					// 创建JSON文档
					doc = JsonDocument.create(key, obj);
					CouchbaseConnect.commonBucket("192.168.1.114:8091", "firstIndustry").upsert(doc);
					INTO++;
					System.out.println("-入库："+INTO);
				}

//				content = JsonObject.fromJson(gson.toJson(arch));
//				documents.add(JsonDocument.create(arch.getWholeCourtId(), content));

				
				System.out.println("总数-：  " + SUM_Count);

			}


		} catch (Exception e) {
			logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生IO异常:" + e.getMessage());
			
		} finally {
			logger.info(name + "<<" + file.getName() + ">>记录条数为：" + sum);
			reader.close();
			// list = null;
			file = null;
			reader.close();
		}
	}

	/**
	 * 裁判文书json文件导入CB
	 * 
	 * @param list
	 * @param bucket
	 * @return
	 * @throws Exception
	 */
	public static boolean createJsonToCB(List<WholeCourtVO> list, Bucket bucket) throws Exception {
		Gson gson = new Gson();
		JsonDocument doc = null;
		WholeCourtVO archs = null;
		List<RelativeVO> relativeList = null;
		List<RelativeVO> lists = null;
		RelativeVO relative = null;
		try {
			for (int i = 0; i < list.size(); i++) {
				count++;
				archs = new WholeCourtVO();
				relativeList = new ArrayList<RelativeVO>();
				lists = new ArrayList<RelativeVO>();
				String uuid = list.get(i).getWholeCourtId();
				// relative = new RelativeVO();
				// relativeList = list.get(i).getRelativeCases();
				// //------------------------------以下为关联文书-----------------
				// for(int a = 0 ; a <relativeList.size() ; a++){
				// if(null != relativeList.get(a).getApprovalDate() &&
				// !"".equals(relativeList.get(a).getApprovalDate())){
				// relative.setApprovalDate(relativeList.get(a).getApprovalDate().toString());
				// }
				// if(null != relativeList.get(a).getCaseNum() &&
				// !"".equals(relativeList.get(a).getCaseNum())){
				// relative.setCaseNum(relativeList.get(a).getCaseNum().toString());
				// }
				// if(null != relativeList.get(a).getClosedType() &&
				// !"".equals(relativeList.get(a).getClosedType())){
				// relative.setClosedType(relativeList.get(a).getClosedType().toString());
				// }
				// if(null != relativeList.get(a).getCourtName() &&
				// !"".equals(relativeList.get(a).getCourtName())){
				// relative.setCourtName(relativeList.get(a).getCourtName().toString());
				// }
				// if(null != relativeList.get(a).getMark() &&
				// !"".equals(relativeList.get(a).getMark())){
				// relative.setMark(relativeList.get(a).getMark().toString());
				// }
				// if(null != relativeList.get(a).getRelative_id() &&
				// !"".equals(relativeList.get(a).getRelative_id())){
				// relative.setRelative_id(relativeList.get(a).getRelative_id().toString());
				// }
				// if(null != relativeList.get(a).getSuitType() &&
				// !"".equals(relativeList.get(a).getSuitType())){
				// relative.setSuitType(relativeList.get(a).getSuitType().toString());
				// }
				// if(null != relativeList.get(a).getType() &&
				// !"".equals(relativeList.get(a).getType())){
				// relative.setType(relativeList.get(a).getType().toString());
				// }
				// lists.add(relative);
				// }
				// //---------------------------------以上为关联文书----------------------------
				// if(lists.size()>0){
				// archs.setRelativeCases(lists);
				// }
				// if(null != list.get(i).getProvince() &&
				// !"".equals(list.get(i).getProvince())){
				// archs.setProvince(list.get(i).getProvince().toString());
				// }
				// if(null != list.get(i).getApprovalDate() &&
				// !"".equals(list.get(i).getApprovalDate())){
				// archs.setApprovalDate(list.get(i).getApprovalDate().toString());
				// }
				// if(null != list.get(i).getCollectDate() &&
				// !"".equals(list.get(i).getCollectDate())){
				// archs.setCollectDate(list.get(i).getCollectDate().toString());
				// }
				// if(null != list.get(i).getCaseCause() &&
				// !"".equals(list.get(i).getCaseCause())){
				// archs.setCaseCause(list.get(i).getCaseCause().toString());
				// }
				// if(null != list.get(i).getPublishDate() &&
				// !"".equals(list.get(i).getPublishDate())){
				// archs.setPublishDate(list.get(i).getPublishDate().toString());
				// }
				// if(null != list.get(i).getCatalog() &&
				// !"".equals(list.get(i).getCatalog())){
				// archs.setCatalog(list.get(i).getCatalog().toString());
				// }
				// if(null != list.get(i).getCaseNum() &&
				// !"".equals(list.get(i).getCaseNum())){
				// archs.setCaseNum(list.get(i).getCaseNum().toString());
				// }
				// if(null != list.get(i).getCity() &&
				// !"".equals(list.get(i).getCity())){
				// archs.setCity(list.get(i).getCity().toString());
				// }
				// if(null != list.get(i).getTitle() &&
				// !"".equals(list.get(i).getTitle())){
				// archs.setTitle(list.get(i).getTitle().toString());
				// }
				// if(null != list.get(i).getArea() &&
				// !"".equals(list.get(i).getArea())){
				// archs.setArea(list.get(i).getArea().toString());
				// }
				// if(null != list.get(i).getDetailLink() &&
				// !"".equals(list.get(i).getDetailLink())){
				// archs.setDetailLink(list.get(i).getDetailLink().toString());
				// }
				// if(null != list.get(i).getSummary() &&
				// !"".equals(list.get(i).getSummary())){
				// archs.setSummary(list.get(i).getSummary().toString());
				// }
				// if(null != list.get(i).getCourtName() &&
				// !"".equals(list.get(i).getCourtName())){
				// archs.setCourtName(list.get(i).getCourtName().toString());
				// }
				// if(null != list.get(i).getSuitType() &&
				// !"".equals(list.get(i).getSuitType())){
				// archs.setSuitType(list.get(i).getSuitType().toString());
				// }

				String jsonss = gson.toJson(archs);

				doc = JsonDocument.create(uuid, JsonObject.fromJson(jsonss));
				bucket.upsert(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			gson = null;
			doc = null;
			archs = null;
			relativeList = null;
			lists = null;
			relative = null;
		}
		return true;
	}


//	/**
//	 * 统计导入的各地的记录条数
//	 */
//	public static void statisticalCount(File file, long count) {
//		// 取省名
//		String provinceName = file.getParentFile().getParent();
//		provinceName = provinceName.substring(provinceName.lastIndexOf("\\") + 1, provinceName.length());
//		// 取市名
//		String city = file.getParentFile().getPath();
//		city = city.substring(city.lastIndexOf("\\") + 1, city.length());
//		List<RecordData> list = MAPS.get(provinceName);
//		if (null == list || list.size() <= 0) {
//			list = new ArrayList<RecordData>();
//			list.add(new RecordData(provinceName, city, count));
//			MAPS.put(provinceName, list);
//		} else {
//			boolean result = true;
//			for (RecordData re : list) {
//				if (re.getCityName().equalsIgnoreCase(city)) {
//					re.setNumberData(re.getNumberData() + count);
//					result = false;
//					break;
//				}
//			}
//			if (result) {
//				list.add(new RecordData(provinceName, city, count));
//				MAPS.put(provinceName, list);
//			}
//		}
//	}

//	/**
//	 * 记录各地数据
//	 */
//	public static void record() {
//		long sumCount = 0;
//		long sum = 0;
//		for (Map.Entry<String, List<RecordData>> map : MAPS.entrySet()) {
//			logger.info("###:" + map.getKey());
//			List<RecordData> list = map.getValue();
//			sum = 0;
//			for (RecordData recordData : list) {
//				logger.info("###:" + recordData.getCityName() + "----记录条数:" + recordData.getNumberData());
//				sum += recordData.getNumberData();
//			}
//			sumCount += sum;
//			logger.info(map.getKey() + "省总数据条数据:" + sum);
//			logger.info("------------------------------");
//		}
//		logger.info("总文件数据条数:" + sumCount);
//		logger.info("去重后的数据条数据:" + REPEATSUM);
//		logger.info("错误数据条数:" + ERRORSUM);
//	}

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
	/**
	 * 去() 及内容
	 * 
	 * @param str
	 * @return
	 */
	public static String removeBlank(String str) {

		if (StringUtils.isNull(str)) {
			return null;
		}
		String sss = str;
		sss = sss.replace("(", "（").replace(")", "）").replace("【", "（").replace("】", "）").replace("[", "（")
				.replace("]", "）").replace("『", "（").replace("』", "）");
		try {
			int a1 = sss.indexOf("（");
			int b1 = sss.indexOf("）") + 1;
			int num = 0;
			char[] chars = sss.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if ('（' == chars[i]) {
					num++;
				}
			}

			for (int i = 0; i < num; i++) {
				if (a1 != -1) {
					if (b1 != -1 && b1 != 0 && a1 < b1) {
						sss = sss.substring(0, a1) + sss.substring(b1);
						a1 = sss.indexOf("（");
						b1 = sss.indexOf("）") + 1;
						if (b1 < a1) {
							String temp = sss.substring(a1);
							if (temp.indexOf("）") != -1) {
								int tempi = temp.indexOf("）") + 1;
								b1 = a1 + tempi;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			return sss;
		}
		return sss;
	}

	private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
}
