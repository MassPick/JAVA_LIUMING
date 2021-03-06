package cn.com.szgao.clean.court;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.DateUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.web.client.RestTemplate;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.util.DateUtils;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.BloomFilter;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;
import rx.functions.Func1;

import rx.Observable;


/**
 * 先写新版本的法院数据，再写旧版本的法院数据  ,以 案号+省市县 作为uuid
 * 
 * 去重新旧法院数据 BloomFilter 先将新版本的数据从json文件只取 案号===>uuid1 ，放bitset集合， 再取服务器旧版本数据生成的
 * 案号===>uuid2 ，如果uuid2 不在集合中 ，则取出这条数据放一文件中
 * @author liuming
 * @Date 2016年6月27日 下午12:00:11
 */
public class UniqCourtFromNewAndOld_Json {
	
	
	static int uniqi = 0;// 旧数据去重后的数据
	static int noCourtOld = 0;// 旧数据没有法院或案号的

	static int i = 0;

	static int index = 0;
	static Gson gson = new Gson();

	private static Logger log = LogManager.getLogger(UniqCourtFromNewAndOld_Json.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(UniqCourtFromNewAndOld_Json.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 文件
//		File file = new File("F:\\2016的文书(JSON20160613-20160530)\\1_20160624034502.json");
 
//		File file = new File("F:\\2016的文书(JSON20160613-20160530)");
//		File file2 = new File("F:\\2015以前_1");
//		File file3 = new File("F:\\2015以前_2");
		
//		File file = new File("H:\\Share2\\刘铭\\20161109_法院新数据去重后\\1950000_2016111712492520161109_法院新数据去重后.json");
		
		File file = new File("H:\\Share2\\刘铭\\新版裁判文书网展示文书(以发布日期查询)_20161109");
		File file2 = new File("H:\\Share2\\刘铭\\20161109_法院新数据去重后");
		
		
		
		File file11 = new File("H:\\Share2\\刘铭\\旧法院数据_20161109\\中国裁判文书网展示文书详细页面（旧版高院）_20161109");
		File file12 = new File("H:\\Share2\\刘铭\\旧法院数据_20161109\\通过法院名查询的裁判文书（旧版高院）");
		File file13 = new File("H:\\Share2\\刘铭\\旧法院数据_20161109\\地方_旧版本高院");
		File file14 = new File("H:\\Share2\\刘铭\\旧法院数据_20161109\\地方_标1");
		File file15 = new File("H:\\Share2\\刘铭\\旧法院数据_20161109\\地方_标2");
		File file16 = new File("H:\\Share2\\刘铭\\旧法院数据_20161109\\地方_浙江省_浙江法院公开网");
		File file17 = new File("H:\\Share2\\刘铭\\旧法院数据_20161109\\doc");
		File file18 = new File("H:\\Share2\\刘铭\\旧法院数据_20161109\\swf");
		File file19 = new File("H:\\Share2\\刘铭\\旧法院数据_20161109\\地方_其他");
		
//		File file = new File("E:\\法院旧版数据清洗\\通过法院名查询的裁判文书（旧版高院）\\1.json");
//		File file2 = new File("E:\\法院旧版数据清洗\\通过法院名查询的裁判文书（旧版高院）\\2.json");
//		File file3 = new File("E:\\法院旧版数据清洗\\旧版高院\\1.json");
		
		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();

			 show(file);
			 show(file2);
//			 show(file3);
			 
			 //去重
			 show2(file11);
			 show2(file12);
			 show2(file13);
			 show2(file14);
			 show2(file15);
			 show2(file16);
			 show2(file17);
			 show2(file18);
			 show2(file19);


			log.info("  新版数据无法院或案号： " + noCourt + "  旧版数据无法院或案号： " + noCourtOld+ "  旧版数据总量： " + sumOld  + "  旧版数据去重后： " + uniqi);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
			log.info("路径：" + file.getPath());
			log.info("开始时间--------------------" + formatter.format(startTime));

			log.info("结束时间--------------------" + formatter.format(endDate));
			log.info("Time : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
			log.info("Time : " + (float) ((float) ((endTime - startTime) / 1000) / 60) + "分钟");
			log.info("Time : " + (float) ((endTime - startTime) / 1000) + "秒");
			log.info("Total : " + count);
			log.info("Speed : " + (float) (count / (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
					+ "个/小时");
			log.info("Speed : " + (float) (count / (float) ((float) ((endTime - startTime) / 1000) / 60)) + "个/分种");
			log.info("Speed : " + (float) (count / (float) ((endTime - startTime) / 1000)) + "个/秒");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static BloomFilter bf = new BloomFilter();

	public static Bucket bucket = null;

	// 新版本无法院或案号记录
	static int noCourt = 0;
	static int size = 10000;

	/**
	 * 新法院新数据无案号
	 */
	static BufferedWriter fwUnNoCaseNum_New = null;
	/**
	 * 旧法院 数据无案号
	 */
	static BufferedWriter fwUnNoCaseNum_Old = null;
	
	public static void readFile(File file) {
		int flag = 0;
		int countM = 0;
		int countNull = 0;
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

//				if (  (noCourt % size == 0 && noCourt>0)   || (noCourt==0&& count==0)) {
//					
//					String folderPathUn = "D:/lm/log/法院新数据无案号/";
//					String filePathUn = "D:\\lm\\log\\法院新数据无案号\\" + noCourt+"_" + DateUtils.getDateyyyyMMddhhmmss()+ "法院新数据无案号.json";
//					// 创建文件夹
//					FileUtils.newFolder(folderPathUn);
//					File fileSUn = new File(filePathUn);
//					String encoding_from1U = "UTF-8";
//					
//
//					try {
//						if (!fileSUn.exists()) {
//							try {
//								fileSUn.createNewFile();
//							} catch (IOException e) {
//								e.printStackTrace();
//								log.error(e);
//							}
//							try {
//								fwUnNoCaseNum_New = new BufferedWriter(
//										new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
//							} catch (UnsupportedEncodingException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} // 指定编码格式，以免读取时中文字符异常
//						} else {
//							fileSUn.delete();
//							fileSUn = new File(filePathUn);
//							try {
//								fwUnNoCaseNum_New = new BufferedWriter(
//										new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
//							} catch (UnsupportedEncodingException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} // 指定编码格式，以免读取时中文字符异常
//						}
//					} catch (FileNotFoundException e1) {
//						e1.printStackTrace();
//					}
//				}

				count++;
				countM++;
				System.out.println("新数据:"+count  +  "   "+file.getPath());
				try {
					try {
						if(StringUtils.isBig1M(temp)){
							logger.info("内容大于1M ：" + file.getPath());
							continue;
						}
						
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

						if (!StringUtils.isNull(vo.getCaseNum())) {
							
							String tempStr=vo.getCaseNum()+ (vo.getProvince()!=null?vo.getProvince():"")+(vo.getCity()!=null?vo.getCity():"")+(vo.getArea()!=null?vo.getArea():"");
							if (bf.contains(StringUtils.NBG.generate(tempStr).toString())) {
								
							}else {
								bf.add(StringUtils.NBG.generate(tempStr).toString());
							}
							tempStr=null;
						} else {// 没有 案号的记录下来
							noCourt++;
//							writerString(fwUnNoCaseNum_New, new Gson().toJson(vo));
						}

//						vo.setWholeCourtId(null);
//						vo.setKey(null);

					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
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
	
	/*
	 *旧法院数据去重后
	 */
	static BufferedWriter fwUn = null;
	/**
	 * 旧版本数据总量
	 */
	static int sumOld = 0;
	
	public static void readFile2(File file) {
		int flag = 0;
		int countM = 0;
		int countNull = 0;
		String tempCourtName = "";
		long startTime = System.currentTimeMillis();
		long startTime2 = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");



		BufferedReader reader = null;
		
//		BufferedWriter fwUnOld = null;
		
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


			JsonObject content = null;
			int countLen = 0;

			while ((temp = reader.readLine()) != null) {

				count++;
				countM++;
				System.out.println("---> "+count);
				try {
					try {
						if(StringUtils.isBig1M(temp)){
							logger.info("内容大于1M ：" + file.getPath());
							continue;
						}
						
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

						sumOld++;
						System.out.println("旧版本:"+sumOld   +"  " +file.getPath());

						if (uniqi % size == 0) {
							String folderPathUn = "H:\\Share2\\刘铭\\20161109_法院旧数据去重后";
							String filePathUn = "H:\\Share2\\刘铭\\20161109_法院旧数据去重后\\" + uniqi+"_" + DateUtils.getDateyyyyMMddhhmmss() + "_20161109_法院旧数据去重后.json";
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
						}

//						if (( noCourtOld % size == 0  &&noCourtOld>0  )  || (noCourtOld==0&& uniqi==0)  ) {
//							String folderPathUn = "D:/lm/log/法院旧数据无案号/";
//							String filePathUn = "D:\\lm\\log\\法院旧数据无案号\\" + noCourtOld+"_" + DateUtils.getDateyyyyMMddhhmmss()+"法院旧数据无案号.json";
//							// 创建文件夹
//							FileUtils.newFolder(folderPathUn);
//							File fileSUn = new File(filePathUn);
//							String encoding_from1U = "UTF-8";
//
//							try {
//								if (!fileSUn.exists()) {
//									try {
//										fileSUn.createNewFile();
//									} catch (IOException e) {
//										e.printStackTrace();
//										log.error(e);
//									}
//									try {
//										fwUnNoCaseNum_Old = new BufferedWriter(
//												new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
//									} catch (UnsupportedEncodingException e) {
//										e.printStackTrace();
//									} // 指定编码格式，以免读取时中文字符异常
//								} else {
//									fileSUn.delete();
//									fileSUn = new File(filePathUn);
//									try {
//										fwUnNoCaseNum_Old = new BufferedWriter(
//												new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
//									} catch (UnsupportedEncodingException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									} // 指定编码格式，以免读取时中文字符异常
//								}
//							} catch (FileNotFoundException e1) {
//								e1.printStackTrace();
//							}
//						}
//						if (vo == null) {
//							continue;
//						}
						
						if (!StringUtils.isNull(vo.getCaseNum())) {
							String tempStr=vo.getCaseNum()+ (vo.getProvince()!=null?vo.getProvince():"")+(vo.getCity()!=null?vo.getCity():"")+(vo.getArea()!=null?vo.getArea():"");
							if (bf.contains(StringUtils.NBG.generate(tempStr).toString())) {

							} else {
								uniqi++;
								// 写数据 旧数据在新数据中不存在的
								writerString(fwUn, new Gson().toJson(vo));
							}
						} else {// 旧数据 没有法院或案号
							noCourtOld++;
//							writerString(fwUnNoCaseNum_Old, new Gson().toJson(vo));
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
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
	
	public static void show2(File file) throws IOException, ParseException {
		if (file.isFile()) {
			try {
				readFile2(file);
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
						readFile2(fi);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					show2(fi);
				} else {
					continue;
				}
			}
		}
	}
	
	 

	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + "\n");

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
