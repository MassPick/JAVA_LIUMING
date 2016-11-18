package cn.com.szgao.timer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.queue.*;

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.BlobUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.LogUtils;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.AdministrationUtils;
import net.sf.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

@SuppressWarnings("unused")
public class TimerBreakfaith {

//	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=masspickdata;"
//			+ "AccountKey=O1x8o0Mr/TrSVwU62cQV5nFOi9h58ELMDlhxsQjmaMHj8RLbeAFitaS0qmELri7XXdUogqds1kR1QxclE+CCOA==;"
//			+ "EndpointSuffix=core.chinacloudapi.cn";

	private static Logger log = LogManager.getLogger(TimerBreakfaith.class);
	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	static Gson gs = new Gson();

	public static void main(String[] args) {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		AdministrationUtils util = new AdministrationUtils();
		// 查询行政区
		util.initData();
		timer7();
	}

	public static void sayHello() {

		int i = 1;// 计数
		while (true) {
			// 这里写上你的监控的具体逻辑代码
			String queueName = "shixin";// shixin myqueue
			if (countLen(queueName) > 0) {

				// BlobUtils.lastNext(queueName);

//				System.out.println("删除");
				// 处理数据
				// deleteMessageMulti("test");

				CloudQueue queue = null;
				try {
					queue = BlobUtils.queueClient.getQueueReference(queueName);

				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (StorageException e) {
					e.printStackTrace();
				}
				try {
					CloudQueueMessage retrievedMessage = queue.retrieveMessage();
					String retrieve_id = retrievedMessage.getId();
//					System.out.println(retrieve_id + "  " + retrievedMessage.getMessageContentAsString());
					if (retrievedMessage != null) {

						// 处理数据
						WholeCourtVO vo = getMsgToVo(retrievedMessage);
//						System.out.println(countLen);

//						System.out.println(gs.toJson(vo));
						//写入CB
						insertToCB(vo);
						//写到文本
						writerString(fw,gs.toJson(vo));
						
						//写到MQ
						try {
							send(gs.toJson(vo));
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (TimeoutException e) {
							e.printStackTrace();
						}
						
						// Process the message in less than 30 seconds, and then
						// delete the message.
						 queue.deleteMessage(retrievedMessage);
					}
				} catch (StorageException e) {
					e.printStackTrace();
				}

			} else {
				try {
					Thread.sleep(1000 * 60*60*24);// 60秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
//			System.out.println("hello。。 " + i);
//			i++;
			// 设置每次监控的间隔时间
//			try {
//				Thread.sleep(3000);// 3秒
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

		}
	}
	
	/**
	 * 发MQ
	 * @param message
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public static void send(String message) throws IOException, InterruptedException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.1.5");// MQ的IP
		factory.setPort(5050);// MQ端口
		factory.setUsername("qq123");// MQ用户名
		factory.setPassword("qq123");// MQ密码
		com.rabbitmq.client.Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// for(int i=0;i<150;i++)
		// {
		channel.basicPublish("", "queue_wholecourt", null, message.getBytes());
		System.out.println(" Sent:  " + message );
		// Thread.sleep(1000);
		// }
		channel.close();
		connection.close();
		factory.clone();
	}
	
	
 
	
	
	
	static Bucket bucket=null;
	static int lian_num=0;
	static{
//		  bucket=CouchbaseConnect.commonBucket("192.168.1.114:8091", "breakFaithUpdate");
			while (true) {
				try {
					// 更新文档
					bucket=CouchbaseConnect.commonBucket("192.168.1.114:8091", "breakFaithUpdate");
					break;
				} catch (Exception e) {
					System.out.println("正在连接 "+(lian_num++));
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					log.info("---------------------------> 插入BC超时");
					log.error(e.getMessage());
				}
			}
	}
	
	/**
	 * 将数据写到CB
	 * @param vo
	 */
	public static void insertToCB(WholeCourtVO vo){
		JsonObject obj = null;
		JsonDocument doc = null;
		JsonDocument queryDoc = null;
		
		if(vo!=null){
			if(!StringUtils.isNull(vo.getWholeCourtId())){
				obj = JsonObject.fromJson(gs.toJson(vo));
				// 创建JSON文档
				doc = JsonDocument.create(vo.getWholeCourtId(), obj);
//				while (true) {
//					try {
//						// 更新文档
//						CouchbaseConnect.commonBucket("192.168.1.114:8091", "breakFaithUpdate").upsert(doc);
//						break;
//					} catch (Exception e) {
//						log.info("---------------------------> 插入BC超时");
//						log.error(e.getMessage());
//					}
//				}
				
				try {
					// 更新文档
					bucket.upsert(doc);
				} catch (Exception e) {
					
					while (true) {
					try {
						// 更新文档
						CouchbaseConnect.commonBucket("192.168.1.114:8091", "breakFaithUpdate").upsert(doc);
						break;
					} catch (Exception e1) {
						log.info("---------------------------> 插入BC超时");
						log.error(e1.getMessage());
					}
				}
					
					log.info("---------------------------> 插入BC超时");
					log.error(e.getMessage());
				}
			}
		}
	}
	

	public static long countLen(String queueName) {
		long cachedMessageCount = 0;
		try {
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(BlobUtils.storageConnectionString);

			CloudQueueClient queueClient = storageAccount.createCloudQueueClient();
			CloudQueue queue = queueClient.getQueueReference(queueName);
			queue.downloadAttributes();
			cachedMessageCount = queue.getApproximateMessageCount();
			System.out.println("------------queue 里的数量：  " + cachedMessageCount);
			// System.out.println(String.format("Queue length: %d",
			// cachedMessageCount));
			return cachedMessageCount;

		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
		}
		return cachedMessageCount;
	}

	static int countLen = 0;

	/**
	 * 对列转换为VO
	 * 
	 * @param retrievedMessage
	 * @return
	 */
	public static WholeCourtVO getMsgToVo(CloudQueueMessage retrievedMessage) {
		BreakFaithVO vo = null;
		WholeCourtVO vo_new = null;
		Gson gs = new Gson();
		String sbIdNum = "";// //身份证号
		JSONObject temJson = null;
		String key1 = null;
		String temp = null;

		if (retrievedMessage != null) {
			String retrieve_id = retrievedMessage.getId();
//			try {
//				System.out.println(retrieve_id + "  " + retrievedMessage.getMessageContentAsString());
//			} catch (StorageException e1) {
//				e1.printStackTrace();
//			}
			String msg = null;
			try {
				msg = retrievedMessage.getMessageContentAsString();
				//写到源数据文件
				writerString(fwY,msg);
			} catch (StorageException e1) {
				e1.printStackTrace();
			}

			if (StringUtils.isNull(msg)) {
				return null;
			}

			try {
				vo = gs.fromJson(msg, BreakFaithVO.class);
			} catch (Exception e) {
				log.error("json异常 :" + msg + " queue id: " + retrievedMessage.getId() + "\n");

			}

			if (vo != null) {

				if (!StringUtils.isNull(vo.getDetailLink())) {
					String detailLink = vo.getDetailLink();

					// http://shixin.court.gov.cn/detail?id=1000068
					// http://shixin.court.gov.cn/findDetai?id=4218712&pCode=4245
					if (detailLink.contains("&pCode=") && detailLink.contains("?id")) {
						String s2 = detailLink.substring(detailLink.indexOf("?id=") + 4, detailLink.indexOf("&pCode"));
						System.out.println(s2);
						key1 = nbg.generate(s2).toString();
						s2 = null;
					} else if (!detailLink.contains("&pCode=")&&detailLink.contains("?id")) {
						String s2 = detailLink.substring(detailLink.indexOf("?id=") + 4);
						System.out.println(s2);
						key1 = nbg.generate(s2).toString();
					} else {
						key1 = nbg.generate(vo.getDetailLink()).toString();
					}

					detailLink = null;
				} else {
					key1 = UUID.randomUUID().toString();
				}

				// 法院
				if (null != vo.getCourtName()) {

					if (!StringUtils.isNull(vo.getPersonName())) {
						String tempPersonName = vo.getPersonName();
						// 取人名中可能存在的身份证
						sbIdNum = StringUtils.pickUpManyCardId(tempPersonName);
						if (!StringUtils.isNull(sbIdNum)) {
							vo.setIdNum(StringUtils.subSpeCharBlank(sbIdNum));
							tempPersonName = StringUtils.subSpeCharBlank(StringUtils.removeNum3(tempPersonName)).trim();
							vo.setPersonName(tempPersonName);
						}
					}
				}
				// 处理 法定代表人或者负责人姓名
				out: if (!StringUtils.isNull(vo.getRepName())) {

					String tempRepName = vo.getRepName().trim();
					if (tempRepName.length() == 1 || "0".equals(tempRepName) || "无".equals(tempRepName)
							|| "空".equals(tempRepName) || "*".equals(tempRepName)) {
						vo.setRepName(null);
						break out;
					}
					// 取人名中可能存在的身份证
					sbIdNum = StringUtils.pickUpManyCardId(tempRepName);
					if (!StringUtils.isNull(sbIdNum)) {
						vo.setIdNum(StringUtils.subSpeCharBlank(sbIdNum));
						tempRepName = StringUtils.subSpeCharBlank(StringUtils.removeNum3(tempRepName)).trim();
						vo.setRepName(tempRepName);
					}
				}
				// 判断身份证是否包含“无”等字段
				if (!StringUtils.isNull(vo.getIdNum())) {
					String tempIdNum = vo.getIdNum().trim();
					if ("无".equals(tempIdNum) || "000000000000000".equals(tempIdNum)
							|| "000000000000000000".equals(tempIdNum)) {
						vo.setIdNum(null);
					}

					else if (StringUtils.isNull(vo.getOrgCode()) && tempIdNum.matches("[a-zA-Z0-9]{8}-[a-zA-Z0-9]")) {
						vo.setOrgCode(tempIdNum.toUpperCase());
						vo.setIdNum(null);
						log.error("身份证是组织代码");
						log.error(temJson);
					} else if (tempIdNum.length() < 15) {

						// vo.setOrgCode(null);
						// vo.setIdNum(null);
						// log.error("身份证不足15位:"+key1);
						// log.error(temJson);

						String tempOrgCode = tempIdNum.replace(" ", "").replace("－", "-").replace("—", "-")
								.replace("(", "").replace(")", "").replace("（", "").replace("）", "");
						if (!tempOrgCode.matches("[a-zA-Z0-9]{8}-[a-zA-Z0-9]")) {
							if (tempOrgCode.length() == 9) {
								tempOrgCode = tempOrgCode.substring(0, tempOrgCode.length() - 1) + "-"
										+ tempOrgCode.substring(tempOrgCode.length() - 1);
								vo.setOrgCode(tempOrgCode.toUpperCase());
								vo.setIdNum(null);
								log.error("身份证是组织代码:" + key1);
								log.error(temJson);
							} else {
								vo.setOrgCode(tempOrgCode);
								vo.setIdNum(null);
								log.error("身份证不足15位:" + key1);
								log.error(temJson);
							}
						}
					} else {
						// 统计转化为大写 X
						vo.setIdNum(tempIdNum.toUpperCase());
					}
					tempIdNum = null;
				}
				// 性别为空，而身份证不为空时，补齐性别
				if ((null == vo.getGender() || "".equals(vo.getGender())) && null != vo.getIdNum()) {
					String gender = StringUtils.getSexFromIdCard(vo.getIdNum());
					vo.setGender(gender);
				}
				// 处理法院
				String tempCourtName = vo.getCourtName();
				tempCourtName = StringUtils.removeBlank(tempCourtName);
				tempCourtName = StringUtils.QtoNull(tempCourtName);
				vo.setCourtName(tempCourtName);

				// array[0]省，array[1]地级市，array[2]县级市、县、区
				String[] array = util.utils(vo.getCourtName());
				if (null != array) {
					vo.setProvince(array[0]);
					vo.setCity(array[1]);
					vo.setArea(array[2]);
				}
				// 处理 执行依据文号
				if (null != vo.getExecuteNum()) {
					String tempExNum = StringUtils.toDBC(vo.getExecuteNum()).replace("（", "(");
					tempExNum = tempExNum.replace("）", ")");
					tempExNum = tempExNum.replace("【", "(");
					tempExNum = tempExNum.replace("】", ")");
					tempExNum = tempExNum.replace("[", "(");
					tempExNum = tempExNum.replace("]", ")");
					tempExNum = tempExNum.replace("{", "(");
					tempExNum = tempExNum.replace("}", ")");
					tempExNum = tempExNum.replace("｛", "(");
					tempExNum = tempExNum.replace("｝", ")");
					if ("".equals(tempExNum) || "无".equals(tempExNum.trim()) || "空".equals(tempExNum.trim())
							|| "文书编号".equals(tempExNum.trim())) {
						vo.setExecuteNum(null);
					} else {
						vo.setExecuteNum(tempExNum);
					}
				}
				if (null != vo.getCaseNum()) {
					String tempCaNum = StringUtils.toDBC(vo.getCaseNum()).replace("（", "(");
					tempCaNum = tempCaNum.replace("）", ")");
					if ("".equals(tempCaNum)) {
						vo.setCaseNum(null);
					} else {
						vo.setCaseNum(tempCaNum);
					}
				}
				// 处理 生效法律文书确定的义务
				if (null != vo.getLiability()) {
					// 替换特殊字符
					String tempLaNum = StringUtils.removeBlank(vo.getLiability()).replace("（", "(");
					tempLaNum = tempLaNum.replace("）", ")");
					tempLaNum = tempLaNum.replace("^~^", "");
					// tempLaNum = tempLaNum.replace("空", "");
					// tempLaNum = tempLaNum.replace("无", "");
					if ("".equals(tempLaNum) || "无".equals(tempLaNum.trim()) || "空".equals(tempLaNum.trim())
							|| "略".equals(tempLaNum.trim())) {
						vo.setLiability(null);
					} else {
						vo.setLiability(tempLaNum);
					}
				}
				// 做出执行依据单位
				out: if (null != vo.getDepartment()) {
					String tempDepartment = StringUtils.removeBlank(vo.getDepartment()).replace("（", "(");
					tempDepartment = tempDepartment.replace("）", ")");
					tempDepartment = tempDepartment.replace("^~^", "");
					// tempDepartment = tempDepartment.replace("空", "");
					// tempDepartment = tempDepartment.replace("无", "");
					tempDepartment = tempDepartment.replace("*", "");

					if (tempDepartment.trim().length() == 1 || "".equals(tempDepartment) || "空".equals(tempDepartment)
							|| "无".equals(tempDepartment)) {
						vo.setDepartment(null);
						break out;
					} else if (StringUtils.isNumeric(tempDepartment.trim())) {
						vo.setDepartment(null);
						break out;
					} else {
						// 处理单个"("
						if ((StringUtils.countCharacter(tempDepartment, "\\(") == 1
								&& StringUtils.countCharacter(tempDepartment, "\\)") == 0)) {
							tempDepartment = tempDepartment.replace("(", "");
						}
						// 处理单个")"
						if ((StringUtils.countCharacter(tempDepartment, "\\)") == 1
								&& StringUtils.countCharacter(tempDepartment, "\\(") == 0)) {
							tempDepartment = tempDepartment.replace(")", "");
						}
						vo.setDepartment(tempDepartment);
					}
				}

				if (null != vo.getOrgCode()) {
					String tempOrgCode = vo.getOrgCode();
					if (StringUtils.isHasHanZi(tempOrgCode) || tempOrgCode.trim().length() < 9
							|| "0".equals(tempOrgCode) || "-".equals(tempOrgCode) || "00000000".equals(tempOrgCode)
							|| "00000000-0".equals(tempOrgCode) || "12345678".equals(tempOrgCode)
							|| tempOrgCode.contains("*")) {
						vo.setOrgCode(null);
					} else {
						tempOrgCode = tempOrgCode.replace(" ", "").replace("－", "-").replace("—", "-");
						if (!tempOrgCode.matches("[a-zA-Z0-9]{8}-[a-zA-Z0-9]")) {
							if (tempOrgCode.length() == 9) {
								tempOrgCode = tempOrgCode.substring(0, tempOrgCode.length() - 1) + "-"
										+ tempOrgCode.substring(tempOrgCode.length() - 1);
							}
						}
						vo.setOrgCode(tempOrgCode);
					}
				}

				if (!StringUtils.isNull(vo.getLiability())) {
					vo.setLiability(vo.getLiability().replace("^~^", ""));
				}
				if (!StringUtils.isNull(vo.getLiability())) {
					if ("略".equals(vo.getLiability()) || "无".equals(vo.getLiability())
							|| "空".equals(vo.getLiability())) {
						vo.setLiability(null);
					}
					vo.setLiability(vo.getLiability().replace("^~^", ""));
				}

				// 设置新的VO
				vo_new = new WholeCourtVO();

				// 设置摘要
				if (null != vo.getPersonName()) {
					String summary = vo.getPersonName() + ";执行法院: " + vo.getCourtName() + ";案号：" + vo.getCaseNum()
							+ ";立案时间：" + vo.getOpenTime() + ";履行的义务： " + vo.getLiability();
					summary = summary.replace("null", "");
					if (null != summary && summary.length() > 100) {
						vo_new.setSummary(summary.substring(0, 100));
					} else {
						vo_new.setSummary(summary);
					}
				}
				if (null != vo.getCompany()) {
					String summary = vo.getCompany() + ";执行法院: " + vo.getCourtName() + ";案号：" + vo.getCaseNum()
							+ ";立案时间：" + vo.getOpenTime() + ";履行的义务： " + vo.getLiability();
					summary = summary.replace("null", "");
					if (null != summary && summary.length() > 100) {
						vo_new.setSummary(summary.substring(0, 98) + "...");
					} else {
						vo_new.setSummary(summary);
					}
				}

				// 2 表示失信
				vo_new.setSource(2);
				// ------------------------------------------------默认
				// 第一批批二批默认无效
				vo_new.setFlag(10);
				String openTimeNew = vo.getOpenTime();
//				vo_new.setOpenTimeNew(openTimeNew);

				// 立案时间
				if (null != vo.getOpenTime()) {
					vo_new.setOpenTime(DateUtils.toYMDOfChaStr_ESZZ2(vo.getOpenTime().toString()));
				}
				if (null != vo.getPublishDate()) {
					vo_new.setPublishDate(DateUtils.toYMDOfChaStr_ESZZ2(vo.getPublishDate().toString()));
				}
				// 采集时间
				// vo_new.setCollectDate(DateUtils.toYMDOfChaStr_ES(vo.getCollectDate()));
				vo_new.setCollectDate(DateUtils.toYMDOfChaStr_ESZZ2(vo.getCollectDate()));
				// 年龄
				if (!StringUtils.isNull(vo.getAge())) {
					if (StringUtils.isNumeric(vo.getAge())) {
						if (Integer.valueOf(vo.getAge()) > 0 && Integer.valueOf(vo.getAge()) < 120) {
							vo_new.setAge(Integer.valueOf(vo.getAge()));
						} else {
							vo_new.setAge(null);
						}
					} else {
						vo_new.setAge(null);
						log.error("年龄异常:" + key1);
						log.error(temJson);
					}
				}

				vo_new.setArea(vo.getArea());
				vo_new.setCaseNum(vo.getCaseNum());
				vo_new.setCity(vo.getCity());
				vo_new.setCompany(vo.getCompany());
				// vo_new.setCompanyId(vo.getCompanyId());
				vo_new.setCourtName(vo.getCourtName());
				vo_new.setDepartment(vo.getDepartment());
				vo_new.setDetail(vo.getDetail());
				vo_new.setDetailLink(vo.getDetailLink());
				vo_new.setExecuteNum(vo.getExecuteNum());
				// vo_new.setFollow(vo.getFollow());
				vo_new.setGender(vo.getGender());
				vo_new.setIdNum(vo.getIdNum());
				vo_new.setLiability(vo.getLiability());
				vo_new.setOrgCode(vo.getOrgCode());
				vo_new.setPerformance(vo.getPerformance());
				vo_new.setPersonName(vo.getPersonName());
				vo_new.setProvince(vo.getProvince());
				// vo_new.setRepId(vo.getRepId());
				vo_new.setRepName(vo.getRepName());

				vo_new.setWholeCourtId(key1);
				// vo_new.setKey(key1);

//				System.out.println(countLen);

				// content = JsonObject.fromJson(gs.toJson(vo_new));
				//
				// documents.add(JsonDocument.create(key1, content));

				countLen++;
				System.out.println(countLen);
				
				
				//处理时间
				vo_new.setOpenTime(DateUtils.getOkDate(vo_new.getOpenTime()));
				vo_new.setPublishDate(DateUtils.getOkDate(vo_new.getPublishDate()));
				
				

			}

			// Process the message in less than 30 seconds, and then delete the
			// message.
			// queue.deleteMessage(retrievedMessage);
			// 处理数据

		}

		return vo_new;

	}

	static BufferedWriter fw = null;
	static BufferedWriter fwY = null;
	public static void timer7() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date startDate = null;
		try {
			startDate = dateFormatter.parse("2016/05/30 01:06:00");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try {

					Thread.sleep(6000);
					
					String ss=new java.text.SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());//当前日期作文件名
					String folderPath = "D:/lm/log/失信清洗数据queue/"  ;
					String filePath = "D:\\lm\\log\\失信清洗数据queue\\失信清洗数据queue_" + ss+".txt";

					// 创建文件夹
					FileUtils.newFolder(folderPath);
					File fileS = new File(filePath);
					String encoding_from1 = "UTF-8";
					
					try {
						if (!fileS.exists()) {
							try {
								fileS.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
								log.error(e);
							}
							fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
						} else {
//							fileS.delete();
							fileS = new File(filePath);
							fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					
					String folderPathY = "D:/lm/log/失信清洗数据queue_源数据/"  ;
					String filePathY = "D:\\lm\\log\\失信清洗数据queue_源数据\\失信待清洗数据queue_" + ss+".txt";
					FileUtils.newFolder(folderPathY);
					File fileSY = new File(filePathY);
					try {
						if (!fileSY.exists()) {
							try {
								fileSY.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
								log.error(e);
							}
							fwY = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSY, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
						} else {
//							fileS.delete();
							fileS = new File(filePath);
							fwY = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSY, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					
					
					sayHello();
					

				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("execute task!" + new Timestamp(this.scheduledExecutionTime()));
			}
		}, startDate, 60 * 1000);
	}
	
	public static void writerString(BufferedWriter fw, String str) {
		try {
//			fw.append(str + "\n");
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

}
