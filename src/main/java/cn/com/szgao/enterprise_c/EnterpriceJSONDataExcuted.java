package cn.com.szgao.enterprise_c;

import cn.com.szgao.wash.data.AdministrationUtils;
import cn.com.szgao.wash.data.DataUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import rx.Observable;
import rx.functions.Func1;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

 
import cn.com.szgao.dto.ExecutedVO;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

 
public class EnterpriceJSONDataExcuted {
	/**
	 * json与对象互转
	 */
	Gson gs = new Gson();
	/**
	 * 工具对象
	 */
	DataUtils utils = new DataUtils();
	/**
	 * 工具类，通过住所、登记机关获得行政区
	 */
	AdministrationUtils u = new AdministrationUtils();
	
	List<JsonDocument> documents = new ArrayList<JsonDocument>();
	 Bucket bucket=null;
	private static String  key=null;
	 JsonObject content=null;
	 
	
 
	// 日志对象
	private Logger log = LogManager.getLogger(EnterpriceJSONDataExcuted.class);
	// 根据字符串生成UUID对象
	NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	// 变更前修改内容
	List<String> beforeList = new ArrayList<String>();
	// 变更后修改内容
	List<String> afterList = new ArrayList<String>();
	// 公司ID集合
	List<String> companyIdList = new ArrayList<String>();
	int count = 0;
	

//	public void main(String[] args) {
//		PropertyConfigurator.configure("D:\\Maven_WorkSpace\\space1\\Mass\\log\\log4j.properties");
//		// 初始化数据库的行政区数据
//		// u.initData();
//		File dicFile = new File("D:\\xcy\\pc6\\2\\山东省");// E:/Temp_File/工商数据/pao
//		try {
//			show(dicFile);
//		} catch (IOException e) {
//			log.info(e);
//			e.printStackTrace();
//		} catch (ParseException e) {
//			log.info(e);
//			e.printStackTrace();
//		}
//	}

	/**
	 * 循环调用是否是目录
	 * 
	 * @param file
	 * @throws ParseException
	 * @throws IOException
	 */
	public void show(File file) throws IOException, ParseException {
		long startTime = System.currentTimeMillis();
		if (file.isFile()) {
			count += 1;
			log.info("数量:"+count+"---线程名"+Thread.currentThread().getName());
			System.out.println(count);
			readFileByLines(file);
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		for (File fi : files) {
			if (fi.isFile()) {
				count += 1;
				log.info("数量:"+count+"---线程名"+Thread.currentThread().getName());
				System.out.println(count);
				readFileByLines(fi);
				// fi.delete();
			} else if (fi.isDirectory()) {
				show(fi);
			} else {
				continue;
			}
		}
	}
	private void readFileByLines(File file) throws IOException, ParseException {
		System.out.println(file.getPath());
		JSONObject temJson = null;
		BufferedReader reader = null;
		JsonObject obj = null;
		JsonDocument doc = null;
		int countLen = 0;
		 cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();
		try {
//			reader = new BufferedReader(new FileReader(file));
			
//			InputStreamReader in=   new InputStreamReader(new FileInputStream(file),"GB18030'");
//			reader = new BufferedReader(in);
			
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GB18030");
			reader = new BufferedReader(isr);
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String 	key1=null;
		String temp = null;
		ExecutedVO vo = null;
		Gson gs = new Gson();
		long startTime = System.currentTimeMillis();
		while ((temp = reader.readLine()) != null) {
			try {
				try {
					temJson = JSONObject.fromObject(temp);
				} catch (Exception e) {
//					 log.error("json异常:"+ file.getPath()+"----------------"+  temp);
					 log.error("json异常:"+  temp);
					continue;
				}
				
				System.out.println(temJson);
				try {
					vo = gs.fromJson(temp, ExecutedVO.class);
				} catch (Exception e) {
//					log.error("json转vo异常:"+ file.getPath()+"----------------"+  temJson);
					log.error("json转vo异常:"+  temJson);
					continue;
				}
				
				if (vo != null) {
//					System.out.println("comp: "+ vo.getCompany()+"------- person: "+vo.getPersonName());
					//合并company personname 到executed
					if(StringUtils.isNull(vo.getExecuted())){
						if(!StringUtils.isNull(vo.getCompany())){
							vo.setExecuted( StringUtils.removeSepar(vo.getCompany()));
						}
						if(!StringUtils.isNull(vo.getPersonName())){
							vo.setExecuted( StringUtils.removeSepar( vo.getPersonName()));
						}
						vo.setCompany(null);
						vo.setPersonName(null);
					}else{
						vo.setCompany(null);
						vo.setPersonName(null);
					}
					
					// 判断身份证是否包含“无”等字段
					if (!StringUtils.isNull(vo.getIdNum())) {
						String tempIdNum = vo.getIdNum().trim();
						if ("无".equals(tempIdNum)||"0".equals(tempIdNum)
								|| "000000000000000".equals(tempIdNum)
								|| "000000000000000000".equals(tempIdNum)||StringUtils.isSameChars(tempIdNum)) {
							vo.setIdNum(null);
						}else if(tempIdNum.length()<15){
							vo.setIdNum(null);
						}
//						else if(!StringUtils.isCardId(tempIdNum)){//判断是否为18位或15位
//							vo.setIdNum(null);
//						}
//						else if(tempIdNum.contains("*")){//可能会有19位
							if(StringUtils.hasDigit(tempIdNum)&&!StringUtils.isHasHanziSpeChar(tempIdNum)&& !StringUtils.hasCharAtoZNotX(tempIdNum)){
								vo.setIdNum(tempIdNum.toUpperCase());
							}
//						}
						else {
							// 统计转化为大写 X
							vo.setIdNum(tempIdNum.toUpperCase());
						}
					}
					// array[0]省，array[1]地级市，array[2]县级市、县、区
					if(!StringUtils.isNull(vo.getCourtName())){
						String[] array = util.utils(vo.getCourtName());
						if (null != array) {
							vo.setProvince(array[0]);
							vo.setCity(array[1]);
							vo.setArea(array[2]);
						}
					}
					//处理文号
					if (null != vo.getCaseNum() ) {
						String tempExNum =StringUtils.toDBC(  vo.getCaseNum()).replace("（",
								"(");
						tempExNum = tempExNum.replace("）", ")");
						tempExNum = tempExNum.replace("【", "(");
						tempExNum = tempExNum.replace("】", ")");
						tempExNum = tempExNum.replace("[", "(");
						tempExNum = tempExNum.replace("]", ")");
						if("".equals(tempExNum)||"无".equals(tempExNum.trim())||"空".equals(tempExNum.trim())){
							vo.setCaseNum(null);
						}else{
							vo.setCaseNum(tempExNum);
						}
					}
					 
					if (null != vo.getOrgCode()) {
						String tempOrgCode = vo.getOrgCode();
						if ( StringUtils.isHasHanZi(tempOrgCode) ||tempOrgCode.trim().length()<9||"0".equals(tempOrgCode)||"-".equals(tempOrgCode)
								|| "00000000".equals(tempOrgCode)
								|| "00000000-0".equals(tempOrgCode)
								|| "12345678".equals(tempOrgCode)||tempOrgCode.contains("*")) {
							vo.setOrgCode(null);
						} else {
							tempOrgCode = tempOrgCode.replace(" ", "")
									.replace("－", "-").replace("—", "-");
							if (!tempOrgCode
									.matches("[a-zA-Z0-9]{8}-[a-zA-Z0-9]")) {
								if (tempOrgCode.length() == 9) {
									tempOrgCode = tempOrgCode.substring(0,
											tempOrgCode.length() - 1)
											+ "-"
											+ tempOrgCode
													.substring(tempOrgCode
															.length() - 1);
								}
							}
							vo.setOrgCode(tempOrgCode);
						}
					}
					
					// 1 表示执行数据
					vo.setSource(1);
					// 1 有效
					vo.setFlag(1);
					
					vo.setModifyDate(DateUtils.toYMDOfChaStr_ES(DateUtils.getDateyyyy_MMddhhmmss()));
					
					
//					if("已结案".equals(vo.getCaseState())){
//						vo.setCaseState(1);
//					}else if("执行中".equals(vo.getCaseState())){
//						vo.setCaseState(0);
//					}
					
					if("1".equals(vo.getCaseState())){
						vo.setCaseState("已结案");
					}else {
						vo.setCaseState("执行中");
					}
					
					if(null!=vo.getOpenTime2()){
						vo.setOpenTime(DateUtils.toYMDOfChaStr(vo.getOpenTime2().toString()));
//						vo.setOpenTime(DateUtils.toYMDOfCha(vo.getOpenTime2().toString()));
					}
					
//					if(null!=vo.getCollectTime2()){
//						vo.setCollectTime(DateUtils.strToDateLong(vo.getCollectTime2().toString()));
//					}
					
					vo.setCollectTime( DateUtils.toYMDOfChaStr_ES(vo.getCollectTime2()) );
					
					vo.setOpenTime2(null);
					vo.setCollectTime2(null);
					
					if(!StringUtils.isNull(vo.getDetailLink())){
						key1=nbg.generate(vo.getDetailLink()).toString();
					}else{
						key1=UUID.randomUUID().toString();
					}
					
					//摘要
					String summary = vo.getExecuted()+";执行标的: "+vo.getSubjectMatter()+";执行法院："+vo.getCourtName()+";案号："+vo.getCaseNum()+";立案时间:"+vo.getOpenTime();
					summary = summary.replace("null", "");
					vo.setSummary(summary);
					
				
					content=JsonObject.fromJson(gs.toJson(vo));
					documents.add(JsonDocument.create(key1, content));
					countLen++;
					
					System.out.println("---线程名"+Thread.currentThread().getName()+"---- "+countLen+"   "+key1);
					if(countLen%1000==0){
						while (true) {
							try {
								// 更新文档
								bucket=CouchbaseConnect.commonBucket("192.168.1.4:8091", "executedN");
								break;
							} catch (Exception e) {
								log.info("---------------------------> 插入BC超时");
								log.error(e.getMessage());
							}
						}
						
						Observable
					    .from(documents)
					    .flatMap(new Func1<JsonDocument, Observable<JsonDocument>>() {
					        public Observable<JsonDocument> call(final JsonDocument docToInsert) {
					            return bucket.async().upsert(docToInsert);
					        }
					    })
					    .last()
					    .toBlocking()
					    .single();
						
				    	content=null;
				    	documents=null;
				    	documents = new ArrayList<JsonDocument>();
						long endTime = System.currentTimeMillis();
						
						String result=(float) ( (endTime - startTime) / 1000) + "秒";
						log.info("----  "+countLen/1000+" 批次"  +"  "+result   +"  总数:  "+countLen+"   file: "+file.getPath() +"       excuted: "+ vo.getExecuted() +"  KEY:  "+key1 );
						endTime=0;
						startTime = System.currentTimeMillis();
						result=null;
						key1=null;
					}
				}				
			} catch (Exception e) {
				log.info(e);
				return;
			}
		}
		
		
		while (true) {
			try {
				// 更新文档
				bucket=CouchbaseConnect.commonBucket("192.168.1.4:8091", "executedN");
//				bucket=CouchbaseConnect.commonBucket("192.168.0.254:8091", "default");
			
				Observable
			    .from(documents)
			    .flatMap(new Func1<JsonDocument, Observable<JsonDocument>>() {
			        public Observable<JsonDocument> call(final JsonDocument docToInsert) {
			            return bucket.async().upsert(docToInsert);
			        }
			    })
			    .last()
			    .toBlocking()
			    .single();
				
		    	content=null;
		    	documents=null;
		    	documents = new ArrayList<JsonDocument>();
		    	
				long endTime = System.currentTimeMillis();
				String result=(float) ( (endTime - startTime) / 1000) + "秒";
				log.info("----  "+countLen/1000+" 批次"  +"  "+result   +"  总数:  "+countLen+"   file: "+file.getPath() +"       excuted: "+ vo.getExecuted() +"  KEY:  "+key1 );
				endTime=0;
				startTime = System.currentTimeMillis();
				result=null;
				key=null;
				break;
			} catch (Exception e) {
				log.info("---------------------------> 插入BC超时");
				log.error(e.getMessage());
			}
		}
	}

	 

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public String formatDate(String date) throws ParseException {
		String result = null;
		SimpleDateFormat sf = null;
		if (null != date) {
			// yyyy年MM月dd日 情况
			if (date.contains("年") && date.contains("月")) {
				result = date;
				return result;
			}
			String yyymmdd = "^[12]\\d{3}(0\\d|1[0-2])([0-2]\\d|3[01])$";// yyyyMMdd
			String yyyy_mm_dd = "^[0-9]{4}-[0-9]{1,}-[0-9]{1,}$";// yyyy-MM-dd
			String yyyy = "^\\d{4}\\/\\d{1,}\\/\\d{1,}$";// yyyy/MM/dd
			// yyyyMMdd 情况
			Pattern pattern = Pattern.compile(yyymmdd);
			Matcher matcher = pattern.matcher(date);
			if (matcher.matches()) {
				sf = new SimpleDateFormat("yyyyMMdd");
				Date d = sf.parse(date);
				sf = null;
				sf = new SimpleDateFormat("yyyy年MM月dd日");
				result = sf.format(d);
				sf = null;
			} else {
				// yyyy-MM-dd 情况
				pattern = Pattern.compile(yyyy_mm_dd);
				matcher = pattern.matcher(date);
				if (matcher.matches()) {
					sf = new SimpleDateFormat("yyyy-MM-dd");
					Date d = sf.parse(date);
					sf = null;
					sf = new SimpleDateFormat("yyyy年MM月dd日");
					result = sf.format(d);
					sf = null;
				} else {
					// yyyy/MM/dd 情况
					pattern = Pattern.compile(yyyy);
					matcher = pattern.matcher(date);
					if (matcher.matches()) {
						sf = new SimpleDateFormat("yyyy/MM/dd");
						Date d = sf.parse(date);
						sf = null;
						sf = new SimpleDateFormat("yyyy年MM月dd日");
						result = sf.format(d);
						sf = null;
					} else {
						int index_xie = date.indexOf("/");
						int index_mao = date.indexOf(":");
						if (4 == index_xie && -1 != index_mao) {
							String newDate = date.substring(0, date.indexOf(" "));
							// yyyy/MM/dd 情况
							pattern = Pattern.compile(yyyy);
							matcher = pattern.matcher(newDate);
							if (matcher.matches()) {
								sf = new SimpleDateFormat("yyyy/MM/dd");
								Date d = sf.parse(newDate);
								sf = null;
								sf = new SimpleDateFormat("yyyy年MM月dd日");
								result = sf.format(d);
								sf = null;
							}
						}
					}
				}
			}
		}
		return result;
	}

	 

	/**
	 * 公用查库的方法
	 * 
	 * @param bucket
	 *            指定的桶
	 * @param doc
	 *            文档
	 */
	public void commonInsert(Bucket bucket, JsonDocument doc) {
		try {
			bucket.upsert(doc, 5000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			log.info(e);
			while (true) {
				try {
					bucket.upsert(doc, 5000, TimeUnit.MILLISECONDS);
					break;
				} catch (Exception ee) {
					log.info(ee);
				}
			}
		}
	}

	/**
	 * 通过行政区划获得行政区
	 * 
	 * @param adminiCode
	 *            行政区划
	 * @return 省市县数组
	 */
	public String[] listCountryCityProvince(String adminiCode) {
		// 0~2元素是省、市、县
		String[] countryCityProvince = new String[3];
		if (null != adminiCode) {
			if (adminiCode.length() < 6) {
				return countryCityProvince;
			}
			// 截取注册号的行政区划
			adminiCode = adminiCode.substring(0, 6);
			// 如果adminiCode是县的行政区划
			if (DataUtils.adminCountryMap.containsKey(adminiCode)) {
				// 县名称、地级市ID
				String array[] = DataUtils.adminCountryMap.get(adminiCode);
				countryCityProvince[2] = array[0];// 县名称
				if (null != array[1]) {
					int cityId = Integer.parseInt(array[1]);
					// 直辖市的ID: '北京市','天津市','重庆市','上海市'
					if (cityId == 400 || cityId == 401 || cityId == 402 || cityId == 403) {
						String provinceName = utils.listProvinceNameByProvinceId(cityId);
						if (null != provinceName) {
							countryCityProvince[0] = provinceName;// 直辖市名称
							return countryCityProvince;
						}
					}
					// listProvinceNameByProvinceId
					Map<String, Integer> cityProvinceMap = utils.listCityProvinceIdByCityId(cityId);
					// 市名称
					Set<String> cityNameSet = cityProvinceMap.keySet();
					Iterator<String> iter = cityNameSet.iterator();
					if (iter.hasNext()) {
						countryCityProvince[1] = iter.next();// 市名称
						cityNameSet = null;
						iter = null;
					}
					// 省的ID
					Collection<Integer> conllection = cityProvinceMap.values();
					Iterator<Integer> iterator = conllection.iterator();
					if (iterator.hasNext()) {
						String provinceName = utils.listProvinceNameByProvinceId(iterator.next());
						if (null != provinceName) {
							countryCityProvince[0] = provinceName;// 省名称
							iterator = null;
							conllection = null;
						}
					}
				}
			} else if (DataUtils.adminCityMap.containsKey(adminiCode)) {
				String array[] = DataUtils.adminCityMap.get(adminiCode);
				countryCityProvince[1] = array[0];// 获得市名称
				int provinceId = Integer.parseInt(array[1]);// 省ID
				String provinceName = utils.listProvinceNameByProvinceId(provinceId);
				if (null != provinceName) {
					countryCityProvince[0] = provinceName;// 获得省名称
				}
			} else if (DataUtils.adminProvinceMap.containsKey(adminiCode)) {
				countryCityProvince[0] = DataUtils.adminProvinceMap.get(adminiCode);// 获得省名称
			}
		}
		return countryCityProvince;
	}

	/**
	 * 行政区划数组处理直辖市
	 * 
	 * @param admin
	 *            行政区数组
	 * @return 行政区数组结果
	 */
	public String[] doAdmin(String admin[]) {
		if (null != admin[0] || null != admin[1]) {
			if (null != admin[1] && (admin[1].equals("北京市") || admin[1].equals("天津市") || admin[1].equals("重庆市")
					|| admin[1].equals("上海市"))) {
				admin[0] = admin[1];
				admin[1] = null;
			}
		}
		return admin;
	}

	/**
	 * 写数据
	 */
	public void createJsonDocument(String name, JsonDocument doc) {
		Bucket bucket = ClusterUtil.commonBucket(name);
		while (true) {
			try {
				bucket.upsert(doc);
				break;
			} catch (Exception e) {
				log.error("超时:" + e.getMessage());
				log.error("重写:" + doc.toString());
			} finally {

			}
		}
	}
}
