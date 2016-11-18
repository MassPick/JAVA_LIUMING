package cn.com.szgao.timer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
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
import cn.com.szgao.dto.ExecutedVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.BlobUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.LogUtils;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.AdministrationUtils;
//import javassist.compiler.ast.Symbol;
import net.sf.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

@SuppressWarnings("unused")

/**
 * 更新被执行
 * 
 * @author liuming
 *
 */
public class TimerExecuted {

	// static String
	// accountKey="O1x8o0Mr/TrSVwU62cQV5nFOi9h58ELMDlhxsQjmaMHj8RLbeAFitaS0qmELri7XXdUogqds1kR1QxclE+CCOA==";
	// static{
	// System.out.println(accountKey.length());
	// }

	// public static final String storageConnectionString =
	// "DefaultEndpointsProtocol=http;" + "AccountName=masspickdata;"
	// + "AccountKey="+accountKey+";"
	// + "EndpointSuffix=core.chinacloudapi.cn";

	// public static final String storageConnectionString =
	// "DefaultEndpointsProtocol=http;" + "AccountName=masspickdata;"
	// +
	// "AccountKey=O1x8o0Mr/TrSVwU62cQV5nFOi9h58ELMDlhxsQjmaMHj8RLbeAFitaS0qmELri7XXdUogqds1kR1QxclE+CCOA==;"
	// + "EndpointSuffix=core.chinacloudapi.cn";

	private static Logger log = LogManager.getLogger(TimerExecuted.class);
	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	static Gson gs = new Gson();

	public static void main(String[] args) {
		// PropertyConfigurator.configure("D:\\data\\git\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
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
			String queueName = "zhixing";// zhixing

			long numQueue = countLen(queueName);
			System.out.println("queue队列中的消息数量： " + numQueue);

			if (numQueue > 0) {

				// BlobUtils.lastNext(queueName);

				// System.out.println("删除");
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

					// CloudQueueMessage retrievedMessage = queue.peekMessage();

					String retrieve_id = retrievedMessage.getId();
					// System.out.println(retrieve_id + " " +
					// retrievedMessage.getMessageContentAsString());
					if (retrievedMessage != null) {

						// 处理数据
						WholeCourtVO vo = doVo(retrievedMessage);
						// System.out.println(countLen);

						// System.out.println(gs.toJson(vo));
						// 写入CB
						insertToCB(vo);
						// 写到文本
						writerString(fw, gs.toJson(vo));

						// 写到MQ
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
					Thread.sleep(1000 * 60 * 60 * 24);// 60秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// System.out.println("hello。。 " + i);
			// i++;
			// 设置每次监控的间隔时间
			// try {
			// Thread.sleep(3000);// 3秒
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }

		}
	}

	/**
	 * 发MQ
	 * 
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
		System.out.println(" Sent:  " + message);
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
					bucket=CouchbaseConnect.commonBucket("192.168.1.114:8091", "executedUpdate");
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
	 * 
	 * @param vo
	 */
	public static void insertToCB(WholeCourtVO vo) {
		JsonObject obj = null;
		JsonDocument doc = null;
		JsonDocument queryDoc = null;

		if (vo != null) {
			if (!StringUtils.isNull(vo.getWholeCourtId())) {
				obj = JsonObject.fromJson(gs.toJson(vo));
				// 创建JSON文档
				doc = JsonDocument.create(vo.getWholeCourtId(), obj);
				// while (true) {
				// try {
				// // 更新文档
				// CouchbaseConnect.commonBucket("192.168.1.114:8091",
				// "executedUpdate").upsert(doc);
				// break;
				// } catch (Exception e) {
				// log.info("---------------------------> 插入BC超时");
				// log.error(e.getMessage());
				// }
				// }

				try {
					// 更新文档
					bucket.upsert(doc);
				} catch (Exception e) {

					while (true) {
						try {
							// 更新文档
							CouchbaseConnect.commonBucket("192.168.1.114:8091", "executedUpdate").upsert(doc);
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

					String ss = new java.text.SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());// 当前日期作文件名
					String folderPath = "D:/lm/log/被执行清洗数据queue/";
					String filePath = "D:\\lm\\log\\被执行清洗数据queue\\被执行清洗数据queue_" + ss + ".txt";

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
							fw = new BufferedWriter(
									new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
						} else {
							// fileS.delete();
							fileS = new File(filePath);
							fw = new BufferedWriter(
									new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}

					String folderPathY = "D:/lm/log/被执行清洗数据queue_源数据/";
					String filePathY = "D:\\lm\\log\\被执行清洗数据queue_源数据\\被执行待清洗数据queue_" + ss + ".txt";
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
							fwY = new BufferedWriter(
									new OutputStreamWriter(new FileOutputStream(fileSY, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
						} else {
							// fileS.delete();
							fileS = new File(filePath);
							fwY = new BufferedWriter(
									new OutputStreamWriter(new FileOutputStream(fileSY, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
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

	/**
	 * 处理数据
	 * 
	 * @param retrievedMessage
	 * @return
	 */
	public static WholeCourtVO doVo(CloudQueueMessage retrievedMessage) {

		if (retrievedMessage == null) {
			return null;
		}

		String retrieve_id = retrievedMessage.getId();
		// try {
		// System.out.println(retrieve_id + " " +
		// retrievedMessage.getMessageContentAsString());
		// } catch (StorageException e1) {
		// e1.printStackTrace();
		// }
		String msg = null;
		try {
			msg = retrievedMessage.getMessageContentAsString();
			// 写到源数据文件
			writerString(fwY, msg);
		} catch (StorageException e1) {
			e1.printStackTrace();
		}

		if (StringUtils.isNull(msg)) {
			return null;
		}

		WholeCourtVO vo = null;

		try {
			vo = gs.fromJson(msg, WholeCourtVO.class);
		} catch (Exception e) {
			log.error("json异常 :" + msg + " queue id: " + retrievedMessage.getId() + "\n");

		}

		String key1 = null;
		if (vo != null) {
			// 合并company personname 到executed
			if (StringUtils.isNull(vo.getExecuted())) {
				if (!StringUtils.isNull(vo.getCompany())) {
					vo.setExecuted(StringUtils.removeSepar(vo.getCompany()));
				}
				if (!StringUtils.isNull(vo.getPersonName())) {
					vo.setExecuted(StringUtils.removeSepar(vo.getPersonName()));
				}
				vo.setCompany(null);
				vo.setPersonName(null);
			} else {
				vo.setCompany(null);
				vo.setPersonName(null);
			}

			if (!StringUtils.isNull(vo.getDetailLink())) {
				// url有变化
				// 新：http://zhixing.court.gov.cn/search/newdetail?id=116851919&j_captcha=87224
				// 旧：http://zhixing.court.gov.cn/search/detail?id=103231356

				// 生成规则还是旧的url
				if (vo.getDetailLink().indexOf("newdetail") != -1 && vo.getDetailLink().indexOf("&j_captcha") != -1) {

					key1 = nbg.generate(vo.getDetailLink().replace("newdetail", "detail").substring(0,
							vo.getDetailLink().indexOf("&j_captcha") - 3)).toString();
				} else {
					key1 = nbg.generate(vo.getDetailLink()).toString();
				}

			} else {
				key1 = UUID.randomUUID().toString();
			}
			// vo.setKey(key1);
			vo.setWholeCourtId(key1);

			// 判断身份证是否包含“无”等字段
			if (!StringUtils.isNull(vo.getIdNum())) {
				String tempIdNum = vo.getIdNum().trim();
				if ("无".equals(tempIdNum) || "0".equals(tempIdNum) || tempIdNum.length() == 1
						|| "000000000000000".equals(tempIdNum) || "000000000000000000".equals(tempIdNum)
						|| StringUtils.isSameChars(tempIdNum)) {
					vo.setIdNum(null);
				} else if (tempIdNum.length() < 15) {
					vo.setIdNum(null);
				}
				// else if(!StringUtils.isCardId(tempIdNum)){//判断是否为18位或15位
				// vo.setIdNum(null);
				// }
				// else if(tempIdNum.contains("*")){//可能会有19位
				if (StringUtils.hasDigit(tempIdNum) && !StringUtils.isHasHanziSpeChar(tempIdNum)
						&& !StringUtils.hasCharAtoZNotX(tempIdNum)) {
					vo.setIdNum(tempIdNum.toUpperCase());
				}
				// }
				else {
					// 统计转化为大写 X
					vo.setIdNum(tempIdNum.toUpperCase());
				}
			}
			// array[0]省，array[1]地级市，array[2]县级市、县、区
			if (!StringUtils.isNull(vo.getCourtName())) {
				String[] array = util.utils(vo.getCourtName());
				if (null != array) {
					vo.setProvince(array[0]);
					vo.setCity(array[1]);
					vo.setArea(array[2]);
				}
			}
			// 处理文号
			if (null != vo.getCaseNum()) {
				String tempExNum = StringUtils.toDBC(vo.getCaseNum()).replace("（", "(");
				tempExNum = tempExNum.replace("）", ")");
				tempExNum = tempExNum.replace("【", "(");
				tempExNum = tempExNum.replace("】", ")");
				tempExNum = tempExNum.replace("[", "(");
				tempExNum = tempExNum.replace("]", ")");
				if ("".equals(tempExNum) || "无".equals(tempExNum.trim()) || "空".equals(tempExNum.trim())) {
					vo.setCaseNum(null);
				} else {
					vo.setCaseNum(tempExNum);
				}
			}

			if (null != vo.getOrgCode()) {
				String tempOrgCode = vo.getOrgCode();
				if (StringUtils.isHasHanZi(tempOrgCode) || tempOrgCode.trim().length() < 9 || "0".equals(tempOrgCode)
						|| "-".equals(tempOrgCode) || "00000000".equals(tempOrgCode) || "00000000-0".equals(tempOrgCode)
						|| "12345678".equals(tempOrgCode) || tempOrgCode.contains("*")) {
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

			// 1 表示执行数据
			vo.setSource(1);
			// 1 有效
			vo.setFlag(1);
			vo.setModifyDate(DateUtils.toYMDOfChaStr_ES(DateUtils.getDateyyyy_MMddhhmmss()));

			if ("1".equals(vo.getCaseState())) {
				vo.setCaseState("已结案");
			} else {
				vo.setCaseState("执行中");
			}

			if (!StringUtils.isNull(vo.getCollectTime())) {
				if (!vo.getCollectTime().contains("+0800")) {
					vo.setCollectTime(DateUtils.toYMDOfChaStr_ESZZ2(vo.getCollectTime()));
				}
			}

			if (!StringUtils.isNull(vo.getOpenTime())) {
				if (!vo.getOpenTime().contains("+0800")) {
					vo.setOpenTime(DateUtils.toYMDOfChaStr_ESZZ2(vo.getOpenTime()));
				}
			}

			// 处理执行标的
			if (!StringUtils.isNull(vo.getSubjectMatter())) {
				String subjectMatter = vo.getSubjectMatter();
				if (subjectMatter.contains("E")) {
					BigDecimal db = new BigDecimal(subjectMatter);
					String ii = db.toPlainString();
					vo.setSubjectMatter(ii);
				} else if (StringUtils.isNumericDecimal(subjectMatter)) {
					vo.setSubjectMatter(subjectMatter);

					// double
					vo.setSubjectMatterN(StringUtils.convertStringToDouble(subjectMatter, 4));
				} else {
					log.error("处理执行标的异常：" + vo.getKey() + "--" + subjectMatter + "--" + new Gson().toJson(vo));
					vo.setSubjectMatter(null);
				}
			}

			String summary = "";
			StringBuffer sbSummary = new StringBuffer();
			if (!StringUtils.isNull(vo.getExecuted())) {
				sbSummary.append(vo.getExecuted() + ";");
			}
			if (null != vo.getSubjectMatter()) {
				sbSummary.append("执行标的：" + vo.getSubjectMatter() + ";");
			}
			if (!StringUtils.isNull(vo.getCourtName())) {
				sbSummary.append("执行法院：" + vo.getCourtName() + ";");
			}
			if (!StringUtils.isNull(vo.getCaseNum())) {
				sbSummary.append("案号：" + vo.getCaseNum() + ";");
			}
			if (!StringUtils.isNull(vo.getOpenTime())) {
				sbSummary.append("立案时间：" + DateUtils.toYMDFromZZ(vo.getOpenTime()) + ";");
			}
			summary = sbSummary.toString();
			// 摘要
			// String summary = vo.getExecuted()+";执行标的:
			// "+vo.getSubjectMatter()+";执行法院："+vo.getCourtName()+";案号："+vo.getCaseNum()+";立案时间:"+vo.getOpenTime();
			// summary = summary.replace("null", "");
			vo.setSummary(summary);

			// 处理时间
			vo.setOpenTime(DateUtils.getOkDate(vo.getOpenTime()));

		}
		return vo;

	}

	public static void writerString(BufferedWriter fw, String str) {
		try {
			// fw.append(str + "\n");
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
