package cn.com.szgao.wash.data.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.dto.BuildQulificationVO;
import cn.com.szgao.dto.BuildVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;
import rx.functions.Func1;

import rx.Observable;

/**
 * 将一个个公司入库到CB        C:\data\建设通和建设市场发布平台\建筑市场发布平台\建筑市场监控1合并
 * 
 * @author liuming
 *
 */
public class InsertBuildFromOneJsonToBC_Old {

	private static Logger log = LogManager.getLogger(InsertBuildFromOneJsonToBC_Old.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(InsertBuildFromOneJsonToBC_Old.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {

		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 文件
		//说明：------------------------------------------------             先入建设通，再建筑市场进行覆盖
		
		
//		File file = new File("C:/data/建设通和建设市场发布平台/建设通/主信息合并");

		File file = new File("C:/data/建设通和建设市场发布平台/建筑市场发布平台/建筑市场监控1合并");
		

		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();
			
			show(file);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
			log.info("路径：" + file.getPath());
			log.info("开始时间--------------------" + formatter.format(startTime));
			log.info("结束时间--------------------" + formatter.format(endDate));
			log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
			log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
			log.info("Total : " + count);
			log.info("Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
			log.info("Speed : " + (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60) ))
					+ "个/分钟");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static Bucket bucket = null;

	public static void readFile(File file) {
		int flag = 0;
		int countM = 0;
		int countNull = 0;
		int size = 1000;
		String tempCourtName = "";
		long startTime = System.currentTimeMillis();
		long startTime2 = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");

		BufferedReader reader = null;
		try {

			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "GB18030");
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			String temp = null;
			BuildVO vo = null;
			Gson gs = new Gson();
			JSONObject temJson = null;
			String key1 = "";

			List<JsonDocument> documents = new ArrayList<JsonDocument>();

			JsonObject content = null;
			int countLen = 0;
			StringBuffer sb = new StringBuffer();

			// String te = null;
			// while ((te = reader.readLine()) != null) {
			// sb.append(te);
			// }
			// if(sb.length()==0){
			// return;
			// }

			// temp = sb.toString();
			while ((temp = reader.readLine()) != null) {
				count++;
				countM++;
				System.out.println(count);
				try {
					try {
						temJson = JSONObject.fromObject(temp);
					} catch (Exception e) {
						logger.error("json异常:" + file.getPath() + "----" + temp);
						continue;
					}
					try {
						vo = gs.fromJson(temp, BuildVO.class);
					} catch (Exception e) {
						logger.error("json转vo异常:" + file.getPath() + "----" + temp);
						continue;
					}

					if (vo != null) {
						
						if(StringUtils.isNull(vo.getCompanyName())){
							continue;
						}

						if (null != vo.getBusinessLicenseNo()) {
							key1 = StringUtils.NBG.generate(vo.getCompanyName()).toString();
						} else {
						}
						
						if(StringUtils.isNull(vo.getContactFax())){
							vo.setContactFax(null);
						}
						if(StringUtils.isNull(vo.getContactNumber() )){
							vo.setContactNumber(null);
						}
						if(StringUtils.isNull(vo.getContactPerson())){
							vo.setContactPerson(null);
						}
						
						List<BuildQulificationVO> listquliT=new ArrayList<BuildQulificationVO>();
						//建筑质资信息 (建设通)
						List<BuildQulificationVO> listquli=vo.getQulification();
						if(null!=listquli&&listquli.size()>0){
							 Iterator<BuildQulificationVO> iter =listquli.iterator();  
						        while(iter.hasNext())  
						        {  
						        	BuildQulificationVO vq= iter.next();  
						        	vq.setCertificateDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getCertificateDate()));
						        	vq.setExpireDate(     DateUtils.toYMDOfChaStr_ESZZ2( vq.getExpireDate()  ));
						        	vq.setQualification_item(vq.getQualification_item()!=null?vq.getQualification_item():vq.getQualifiedScope() !=null?vq.getQualifiedScope():null );
						        	vq.setQualifiedScope(null);
						        	listquliT.add(vq);
						        } 
						}
						//建筑质资信息 (建筑监督)
						List<BuildQulificationVO> listquli2=vo.getQualificationGrade() ;
						if(null!=listquli2&&listquli2.size()>0){
							 Iterator<BuildQulificationVO> iter =listquli2.iterator();  
						        while(iter.hasNext())  
						        {  
						        	BuildQulificationVO vq= iter.next();  
						        	vq.setCertificateDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getCertificateDate()));
						        	vq.setExpireDate(     DateUtils.toYMDOfChaStr_ESZZ2( vq.getExpireDate()  ));
						        	vq.setQualification_item(vq.getQualification_item()!=null?vq.getQualification_item():vq.getQualifiedScope() !=null?vq.getQualifiedScope():null );
						        	vq.setQualifiedScope(null);
						        	listquliT.add(vq);
						        } 
						}
						
						
						if(listquliT.size()>0){
							vo.setQulification(null);
							vo.setQualificationGrade(listquliT); 
						}else{
							log.info("无资质： "+temp);
							continue;
							
						}

						vo.setBuildId(key1);
						// 更新时间
						vo.setUpdateTime(DateUtils.getDateyyyyMMddhhmmssZZ());
						vo.setCollectTime(DateUtils.toYMDOfChaStr_ESZZ2(vo.getCollectTime()));
						content = JsonObject.fromJson(gs.toJson(vo));
						documents.add(JsonDocument.create(key1, content));
						countLen++;
						
//						while (true) {
//							try {
//								// 更新文档
//								bucket = CouchbaseConnect.commonBucket("192.168.1.30:8091", "build");
//								break;
//							} catch (Exception e) {
//								log.info("---------------------------> 插入BC超时");
//								log.error(e.getMessage());
//							}
//						}
//						
//						bucket.upsert(JsonDocument.create(key1, content) );

						System.out.println(
								"---线程名" + Thread.currentThread().getName() + "---- " + countLen + "   " + key1);
						if (countLen % 100 == 0) {
							while (true) {
								try {
									// 更新文档
									bucket = CouchbaseConnect.commonBucket("192.168.1.30:8091", "build");
									break;
								} catch (Exception e) {
									log.info("---------------------------> 插入BC超时");
									log.error(e.getMessage());
								}
							}
							
							Observable.from(documents).flatMap(new Func1<JsonDocument, Observable<JsonDocument>>() {
								public Observable<JsonDocument> call(final JsonDocument docToInsert) {
									return bucket.async().upsert(docToInsert);
								}
							}).last().toBlocking().single();

							content = null;
							documents = null;
							documents = new ArrayList<JsonDocument>();
							long endTime = System.currentTimeMillis();

							String result = (float) ((endTime - startTime) / 1000) + "秒";
//							log.info("----  " + countLen / 1000 + " 批次" + "  " + result + "  总数:  " + countLen
//									+ "   file: " + file.getPath() + "       company: " + vo.getCompanyName()
//									+ "  KEY:  " + key1);
							endTime = 0;
							startTime = System.currentTimeMillis();
							result = null;
							key1 = null;
						}

					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}

			if (documents.size() > 0) {

				while (true) {
					try {
						// 更新文档
						bucket = CouchbaseConnect.commonBucket("192.168.1.30:8091", "build");
						break;
					} catch (Exception e) {
						log.info("---------------------------> 插入BC超时");
						log.error(e.getMessage());
					}
				}

				Observable.from(documents).flatMap(new Func1<JsonDocument, Observable<JsonDocument>>() {
					public Observable<JsonDocument> call(final JsonDocument docToInsert) {
						return bucket.async().upsert(docToInsert);
					}
				}).last().toBlocking().single();

				content = null;
				documents = null;
				documents = new ArrayList<JsonDocument>();
				long endTime = System.currentTimeMillis();

				String result = (float) ((endTime - startTime) / 1000) + "秒";
				log.info("----  " + countLen / 1000 + " 批次" + "  " + result + "  总数:  " + countLen + "   file: "
						+ file.getPath() + "       company: " + vo.getCompanyName() + "  KEY:  " + key1);
				endTime = 0;
				startTime = System.currentTimeMillis();
				result = null;
				key1 = null;
			}

		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {

				logger.info("结束!!!!!!!!!!!");
				logger.info("空数据" + countNull);

				long endTime = System.currentTimeMillis();
				Date endDate = new Date(endTime);
				System.out.println("结束时间--------------------" + formatter.format(endDate));
				System.out.println("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
				System.out.println("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
				System.out.println("Total : " + count);
				System.out.println(
						"Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
				System.out.println("Speed : "
						+ (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60)) + "个/小时");

			} catch (Exception e) {
				logger.info(e);
			}
		}
	}

	public static void show(File file) throws IOException, ParseException {
		if (file.isFile()) {
			try {
				readFile(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		if (null != files) {
			for (File fi : files) {
				if (fi.isFile()) {
					try {
						readFile(fi);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					show(fi);
				} else {
					continue;
				}
			}
		}
	}

}
