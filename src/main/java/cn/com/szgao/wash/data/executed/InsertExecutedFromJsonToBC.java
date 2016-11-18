package cn.com.szgao.wash.data.executed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * json失信Id入库
 * 
 * @author liuming
 * @ClassName GetBreakFaithIdFromJsonToPG
 * @date 2016年1月11日 下午2:53:41
 */
public class InsertExecutedFromJsonToBC {

	private static Logger log = LogManager.getLogger(InsertExecutedFromJsonToBC.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(InsertExecutedFromJsonToBC.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {
		// File file = new
		// File("C:\\data\\失信\\shixin20160111\\500000至1000000\\500000至1000000.json");

		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 文件
		

		File file3 = new File("D:\\lm\\log\\temp\\被执行人\\执行executed_旧\\执行executed");
		File file4 = new File("D:\\lm\\log\\temp\\被执行人\\102649800-104976000");
		File file44 = new File("D:\\lm\\log\\temp\\被执行人\\104976000-108200022");//-------es可能没有写
		File file5 = new File("D:\\lm\\log\\temp\\被执行人\\108200022-110900001");
		File file6 = new File("D:\\lm\\log\\temp\\被执行人\\110900001-112150014");
		File file7 = new File("D:\\lm\\log\\temp\\被执行人\\112150014-113345749");
		File file8 = new File("D:\\lm\\log\\temp\\被执行人\\113345749-113925345");
		File file9 = new File("D:\\lm\\log\\temp\\被执行人\\113925345-115065350");
		File file10 = new File("D:\\lm\\log\\temp\\被执行人\\115065350-115901410");
		File file11 = new File("D:\\lm\\log\\temp\\被执行人\\115901410-117097800");////////---es可能没有写
		
		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();
//			show(file3);
//			show(file4);
			show(file44);
//			show(file5);
//			show(file6);
//			show(file7);
//			show(file8);
//			show(file9);
//			show(file10);
			show(file11);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
			log.info("路径：" + file4.getPath());
			log.info("开始时间--------------------" + formatter.format(startTime));
			log.info("结束时间--------------------" + formatter.format(endDate));
			log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
			log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
			log.info("Total : " + count);
			log.info("Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
			log.info("Speed : " + (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
					+ "个/小时");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static Bucket bucket = null;

	
	public static BufferedWriter fwUn = null;
	static{
		String filePathUn="D:\\lm\\log\\temp\\执行中的失信数据.json";
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
//					fileSUn.delete();
//					fileSUn = new File(filePathUn);
//					try {
//						fwUn = new BufferedWriter(
//								new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
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
			WholeCourtVO vo = null;
			Gson gs = new Gson();
			JSONObject temJson = null;
			String key1 = "";

			List<JsonDocument> documents = new ArrayList<JsonDocument>();

			JsonObject content = null;
			int countLen = 0;

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
						vo = gs.fromJson(temp, WholeCourtVO.class);
					} catch (Exception e) {
						logger.error("json转vo异常:" + file.getPath() + "----" + temp);
						continue;
					}

					if (vo != null) {

						
						//判断是不是存在失信的数据
						if(null==vo.getExecuted()){
							writerString(fwUn, new Gson().toJson(vo));
							continue;
						}
						
						
						if(null!=vo.getWholeCourtId()){
							key1=vo.getWholeCourtId();
						}else{
							if(null!=vo.getKey()){
								key1=vo.getKey();
							}
						}
						if(StringUtils.isNull(key1)){
							continue;
						}
						
						vo.setOpenTime(DateUtils.toYMDOfChaStr_ESZZ2(vo.getOpenTime()));
						vo.setOpenTimeNew(null);
						
						String subjectMatter=vo.getSubjectMatter();
						 if (StringUtils.isNumericDecimal(subjectMatter)) {
								vo.setSubjectMatter(subjectMatter);
								
								//double
								vo.setSubjectMatterN( StringUtils.convertStringToDouble(subjectMatter, 4)  );
						 }
						
						vo.setSource(1);
						vo.setWholeCourtId(null);
						vo.setKey(null);
						
						content = JsonObject.fromJson(gs.toJson(vo));
						documents.add(JsonDocument.create(key1, content));
						countLen++;

						System.out.println(
								"---线程名" + Thread.currentThread().getName() + "---- " + countLen + "   " + key1);
						if (countLen % 1000 == 0) {
							while (true) {
								try {
									// 更新文档
									bucket = CouchbaseConnect.commonBucket("192.168.1.6:8091", "executed");
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
							log.info("----  " + countLen / 1000 + " 批次" + "  " + result + "  总数:  " + countLen
									+ "   file: " + file.getPath() + "       excuted: " + vo.getExecuted() + "  KEY:  "
									+ key1);
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
			
			if(documents.size()>0){
				
				while (true) {
					try {
						// 更新文档
						bucket = CouchbaseConnect.commonBucket("192.168.1.6:8091", "executed");
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
				log.info("----  " + countLen / 1000 + " 批次" + "  " + result + "  总数:  " + countLen
						+ "   file: " + file.getPath() + "       excuted: " + vo.getExecuted() + "  KEY:  "
						+ key1);
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
