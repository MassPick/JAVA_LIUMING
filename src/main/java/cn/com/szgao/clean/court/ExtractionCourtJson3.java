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

///////\//import cn.com.szgao.court.esAndcb.CommonConstant;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.wash.data.AdministrationUtils;

/**
 * 中国裁判文书网展示文书详细页面（旧版高院）  的Json    写到  json     
 * 
 * @author liuming
 * @Date 2016年6月17日 上午10:17:58
 */
public class ExtractionCourtJson3 {
	static AdministrationUtils util;
	
	
	public static String[] LAWCASE_AD={"民事","刑事","行政","知识产权","赔偿","执行","涉外","海事"};
	static int countNum = 0;
	static int countNotHtml = 0;

	/**
	 * 生成文件的个数
	 */
	static int countP = 0;
	static BufferedWriter fwUn = null;
	static BufferedWriter fwUn2 = null;
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(ExtractionCourtJson3.class.getName());
	static Map<String, List<RecordData>> MAPS = new HashMap<String, List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //
	static long REPEATSUM = 0; // 去重后数据条数
	static long SUM = 0;
	static long count = 0;

	/**
	 * 裁判文书 数据写库PostgreSql和couchbase JSON导入extracl_url_t表和court桶
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 导入文件地址
		// File file=new
		// File("G:\\Data\\2016新数据\\JSON\\JSON20131231-20130601\\2013-11-04");
		// File file=new File("C:\\data\\文书新版本\\JSON\\JSON20131231-20130601");
		
//		File file = new File("E:\\地方法院\\JSON的省份");
		
		
		//---------------------------------------数据来源    1   旧版高院    2  旧版地方法院   3  新版  4 旧版高院按法院名查 41      5  旧版地方法院Excel   
//		File file = new File("E:\\地方法院\\EXCEL的省份\\通过法院名在裁判文书上采集的");    //总数量 10238568
		
		
		
		//----------------------------3965962  以下三个总量
		File file = new File("E:\\法院详细页面\\中国裁判文书网展示文书详细页面（旧版高院）\\zgcpwsw-20150831");
		File file2 = new File("E:\\法院详细页面\\中国裁判文书网展示文书详细页面（旧版高院）\\zgcpwsw-20151102");
		File file3 = new File("E:\\法院详细页面\\中国裁判文书网展示文书详细页面（旧版高院）\\zgcpwsw-20151127");
		
		
		
		
		util = new AdministrationUtils();
		util.initData(); // 查询行政区
		
		
		// File file=new
		// File("C:\\data\\新版裁判文书JSON+HTML\\2016年\\JSON\\JSON20160115-20160101\\2016-01-04\\刑事案件-2016-01-04.json");

		Bucket bucket = null;
//		bucket = connectionBucket(bucket);
		try {
			show(file, bucket);
			show(file2, bucket);
			show(file3, bucket);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			file = null;
//			bucket.close();
			cluster2 = null;
		}
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");
		record();
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
			if ("json".equals(file.getName().substring(file.getName().lastIndexOf(".") + 1))  && ( file.getPath().indexOf("resource")!=-1  ||  file.getPath().indexOf("url")!=-1 )  ) {
//				if ("json".equals(fi.getName().substring(fi.getName().lastIndexOf(".") + 1))) {
				long da = System.currentTimeMillis();
				create(file, bucket);
				logger.info("读取<<" + file.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
				return;
			}
		}
//		if(file.isDirectory()){
//			if (  ( file.getPath().indexOf("resource")!=-1  ||  file.getPath().indexOf("url")!=-1 )  ) {
//				
//			}else{
//				
//			}
//		}
		
		File[] files = file.listFiles();
		System.out.println("----files---" + files);
		for (File fi : files) {
			if (fi.isFile()) {
				if (fi.getName().contains("download_fail")) {
					continue;
				}
//				if ("json".equals(fi.getName().substring(fi.getName().lastIndexOf(".") + 1))) {
				if ("json".equals(fi.getName().substring(fi.getName().lastIndexOf(".") + 1))  && (    fi.getPath() .indexOf("url")!=-1 )  ) {

					long da = System.currentTimeMillis();
					String name = fi.getParentFile().getPath();
					name = name.substring(name.lastIndexOf("\\") + 1, name.length());
					create(fi, bucket);
					logger.info(
							"读取" + name + "<<" + fi.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
				}

			} else if (fi.isDirectory()  ) {
				show(fi, bucket);
			} else {
				continue;
			}
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
		WholeCourtVO arch = null;
		List<WholeCourtVO> list = new ArrayList<WholeCourtVO>();
		String temp = null;
		int sum = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			
			int flag=0;
			String [] array = new String [3] ;
			String tempName=file.getName().substring(0, file.getName().lastIndexOf("."));
			if(!StringUtils.isNull(tempName)&tempName.substring(tempName.length()-1).equals("院")){
				array = util.utils(  tempName );
				flag=1;
			}
			
			while ((temp = reader.readLine()) != null) {
				
				if (countNum % 10000 == 0) {
					countP++;
					String folderPathUn = "E:\\中国裁判文书网展示文书详细页面（旧版高院）Json";
					String filePathUn = "E:\\中国裁判文书网展示文书详细页面（旧版高院）Json\\" + (countP)+"_"+DateUtils.getDateyyyyMMddhhmmss() + ".json";
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
								logger.error(e);
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
				
				
				arch = gson.fromJson(temp, WholeCourtVO.class);
				
				if(StringUtils.isNull(arch.getDetailLink())){
					continue;
				}
				
				//案件
				for (String tempLaw : LAWCASE_AD) {
					if(!StringUtils.isNull(arch.getCatalog())){
						if(arch.getCatalog().indexOf(tempLaw)!=-1){
							arch.setCatalog(tempLaw);
							break;
						}
					}
				}
				arch.setPublishDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(arch.getPublishDate())));
				arch.setCollectDate(DateUtils.toYMDOfChaStr_ESZZ2(arch.getCollectDate()));
				//省市县
				
				if(flag==0){
					if (!StringUtils.isNull(arch.getCourtName())||!StringUtils.isNull(arch.getProvince())||!StringUtils.isNull(arch.getCity())||!StringUtils.isNull(arch.getArea())  ) {
						array = util.utils(  arch.getProvince()+arch.getCity()+ arch.getArea()+ arch.getCourtName());
					}
				}
				
				arch.setProvince(array[0]);
				arch.setCity(array[1]);
				arch.setArea(array[2]);
				
				if(StringUtils.isNull(arch.getTitle())){
					arch.setTitle(null);
				}
				if(StringUtils.isNull(arch.getCatalog())){
					arch.setCatalog(null);
				}
				if(StringUtils.isNull(arch.getCaseNum())){
					arch.setCaseNum(null);
				}
				if(StringUtils.isNull(arch.getCourtName())){
					arch.setCourtName(null);
				}
				if(StringUtils.isNull(arch.getPublishDate())){
					arch.setPublishDate(null);
				}
				if(StringUtils.isNull(arch.getProvince())){
					arch.setProvince(null);
				}
				if(StringUtils.isNull(arch.getCity())){
					arch.setCity(null);
				}
				if(StringUtils.isNull(arch.getArea())){
					arch.setArea(null);
				}
				
				
				arch.setWholeCourtId(StringUtils.getUUID(arch.getDetailLink().toString()));
				arch.setDataFrom( 41);
				countNum++;
				System.out.println(countNum);
				writerString(fwUn, StringUtils.GSON.toJson(arch));
				
				list.add(arch);
			}
//			list = removeDuplicate(list); // 去除本次集合中重复数据
//			sum = list.size();
//			
//			boolean result = createJsonToCB(list, bucket);
//			
//			REPEATSUM += list.size();
//			if (!result) {
//				logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生JSON异常!");
//			}
			temp = null;
			list = null;
			list = new ArrayList<WholeCourtVO>();
			statisticalCount(file, sum);
		} catch (Exception e) {
			logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生IO异常:" + e.getMessage());
		} finally {
			logger.info(name + "<<" + file.getName() + ">>记录条数为：" + sum);
			reader.close();
			list = null;
			file = null;
			reader.close();
		}
	}
	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + System.getProperty("line.separator"));

			// fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			logger.error("写文件异常" + e.getMessage());
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
				String uuid = list.get(i).getWholeCourtId();
				String jsonss = gson.toJson(list.get(i));
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

	private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
}
