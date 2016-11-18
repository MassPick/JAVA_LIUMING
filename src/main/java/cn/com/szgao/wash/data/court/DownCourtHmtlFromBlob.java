package cn.com.szgao.wash.data.court;

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
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
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

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.dto.BuildManagerDetailVO;
import cn.com.szgao.dto.BuildVO;
import cn.com.szgao.dto.DocWholeCourtVO;
import cn.com.szgao.dto.FfBuildVO;
import cn.com.szgao.dto.FfWholeCourtVO;
import cn.com.szgao.dto.RowsBuildVO;
import cn.com.szgao.dto.RowsWholeCourtVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.BloomFilter;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.EsUtil;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;
import rx.functions.Func1;

import rx.Observable;

import com.microsoft.azure.storage.AccessCondition;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

/**
 * 
 * @author Administrator
 * @Date 2016年6月12日 下午10:06:34
 */
public class DownCourtHmtlFromBlob {

	private static Logger log = LogManager.getLogger(DownCourtHmtlFromBlob.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(DownCourtHmtlFromBlob.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=masspickdata;"
			+ "AccountKey=F+3Zs1jdXtK1udTX352Hp08rOW0FS9Jvq3oDcmQbAzbwSQuQiQLxT9g5pkMf8NStfC6jjfkTl+AlqaMYbMSJ2A==;"
			+ "EndpointSuffix=core.chinacloudapi.cn";
	
	public static CloudBlobContainer container;
	static{
		CloudStorageAccount storageAccount;
		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			 container = blobClient.getContainerReference("courts");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 文件

		// File file = new File("D:\\lm\\log\\temp\\被执行人\\113925345-115065350");

		// File file = new File("D:\\lm\\log\\temp\\被执行人\\115065350-115901410");
		// File file = new File("D:\\lm\\log\\temp\\被执行人\\108200022-110900001");
//		File file = new File("F:\\法院20160612\\2015以前_1\\1.json");
		File file = new File("F:\\lm\\log\\法院旧数据按案号去重后");

		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();

			 show(file);

//			show_es();

			// doListData3();

			log.info("  新版数据无法院或案号： " + noCourt + "  旧版数据无法院或案号： " + noCourtOld + "  旧版数据去重后： " + uniqi);

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
	static int size = 100000;

	public static void readFile(File file) {
		int flag = 0;
		int countM = 0;
		int countNull = 0;
		String tempCourtName = "";
		long startTime = System.currentTimeMillis();
		long startTime2 = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");

		BufferedWriter fwUn = null;

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
			
			String folderPathUn = "F:/lm/log/法院旧数据按案号去重后_下载HTML/"+file.getName();
			folderPathUn=folderPathUn.substring(0, folderPathUn.lastIndexOf("."));
			// 创建文件夹
			FileUtils.newFolder(folderPathUn);
			
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

						// if(null!=vo.getWholeCourtId()){
						// key1=vo.getWholeCourtId();
						// }else{
						// if(null!=vo.getKey()){
						// key1=vo.getKey();
						// }
						// }
						// if(StringUtils.isNull(key1)){
						// continue;
						// }

						if (!StringUtils.isNull(vo.getWholeCourtId())) {
							CloudBlockBlob blob = container.getBlockBlobReference(vo.getWholeCourtId()+".html");
//							String str = blob.downloadText();
							if(blob!=null){
								blob.download(new FileOutputStream(folderPathUn+"/"+vo.getWholeCourtId()+".html"));
							}
						} 
//						vo.setWholeCourtId(null);
//						vo.setKey(null);

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

	static int uniqi = 0;// 旧数据去重后的数据
	static int noCourtOld = 0;// 旧数据没有法院或案号的

	static int i = 0;

	static int index = 0;
	static Gson gson = new Gson();

	@SuppressWarnings("deprecation")
	public static void show_es() {

		BufferedWriter fwUn = null;
		BufferedWriter fwUnOld = null;

		// QueryBuilder qb = QueryBuilders.termsQuery("", "");
		Client esClient = EsUtil.getClient();
		// SearchResponse searchResponse = esClient.prepareSearch("wholecourt")
		// //加上这个据说可以提高性能，但第一次却不返回结果
		//// .setSearchType(SearchType.SCAN).setPostFilter(FilterBuilders.termFilter("doc.source",1))
		// .setTypes("court").setSearchType(SearchType.SCAN)
		// //实际返回的数量为5*index的主分片格式
		// .setSize(1000)
		// //这个游标维持多长时间
		// .setScroll(TimeValue.timeValueMinutes(8))
		// .execute().actionGet();

		SearchResponse searchResponse = esClient.prepareSearch("wholecourt").setTypes("court")
				// 加上这个据说可以提高性能，但第一次却不返回结果
				// .setSearchType(SearchType.SCAN).setPostFilter(FilterBuilders.termFilter("doc.source",1))
				// .setSearchType(SearchType.valueOf("executed")).setPostFilter(
				// QueryBuilders.termQuery("doc.source",1) )
				// 实际返回的数量为5*index的主分片格式
				.setSize(1000)
				// 这个游标维持多长时间
				.setScroll(TimeValue.timeValueMinutes(8)).execute().actionGet();

		// 第一次查询，只返回数量和一个scrollId
		System.out.println(searchResponse.getHits().getTotalHits());
		System.out.println(searchResponse.getHits().hits().length);
		// 第一次运行没有结果
//		for (SearchHit hit : searchResponse.getHits()) {
//			System.out.println(hit.getSourceAsString());
//		}
		System.out.println("------------------------------");
		int sum = 0;
		JsonObject json = null;
		List<String> list = new ArrayList<String>();
		List<WholeCourtVO> lists = new ArrayList<WholeCourtVO>();

		WholeCourtVO vo = null;

		DocWholeCourtVO docVo = null;
		// 使用上次的scrollId继续访问
		while (true) {
			searchResponse = esClient.prepareSearchScroll(searchResponse.getScrollId())
					.setScroll(TimeValue.timeValueMinutes(8)).execute().actionGet();
			System.out.println(searchResponse.getHits().getTotalHits());
			// System.out.println(searchResponse.getHits().hits().length);
			for (SearchHit hit : searchResponse.getHits()) {
				// System.out.println(hit.getSourceAsString());

				// json = gson.fromJson(hit.getSourceAsString(),
				// JsonObject.class);
				// list.add(json.get("doc").toString());

				vo = gson.fromJson(hit.getSourceAsString(), WholeCourtVO.class);
//				vo = docVo.getDoc();
				// vo.setSubjectMatterN(StringUtils.convertStringToDouble(vo.getSubjectMatter(),4));
//				lists.add(vo);
				// list.add(json.get("doc").toString());

				sum++;
				System.out.println(sum);

				if (uniqi % size == 0) {
					String folderPathUn = "F:/lm/log/法院旧数据按案号去重后/";
					String filePathUn = "F:\\lm\\log\\法院旧数据按案号去重后\\" + uniqi + ".json";
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

				if (noCourtOld % size == 0) {
					String folderPathUn = "F:/lm/log/法院旧数据无案号/";
					String filePathUn = "F:\\lm\\log\\法院旧数据无案号\\" + noCourtOld + ".json";
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
								fwUnOld = new BufferedWriter(
										new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} // 指定编码格式，以免读取时中文字符异常
						} else {
							fileSUn.delete();
							fileSUn = new File(filePathUn);
							try {
								fwUnOld = new BufferedWriter(
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

				if (vo == null) {
					continue;
				}

				if (!StringUtils.isNull(vo.getCaseNum())) {
					if (bf.contains(StringUtils.NBG.generate(vo.getCaseNum()).toString())) {

					} else {
						uniqi++;
						// 写数据 旧数据在新数据中不存在的
						writerString(fwUn, new Gson().toJson(vo));
					}
				} else {// 旧数据 没有法院或案号
					noCourtOld++;
					writerString(fwUnOld, new Gson().toJson(vo));
				}

				// if(lists.size()>=100000){
				// index++;
				// //System.out.println(true);
				//
				//// filteText2(lists);
				//
				//
				// list.clear();
				// list=null;
				// list=new ArrayList<String>();
				// }
				// log.info(hit.getSourceAsString());
			}
			if (searchResponse.getHits().getHits().length == 0) {
				break;
			}
			// if(sum>=1000000){break;}
		}
		esClient.close();
		// if(lists.size()>0){
		// index++;
		// //System.out.println(true);
		//
		//// filteText2(lists);
		//
		// list.clear();
		// list=null;
		// }
	}

	public static void doListData3() throws UnsupportedEncodingException {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		urlVariables.put("limits", 5000);
		urlVariables.put("inclusive_ends", true);
		urlVariables.put("skips", 0);
		String key = null;
		RestTemplate template = new RestTemplate();
		String result = null;
		Gson gson = new Gson();

		int source = 0;
		String openTime = null;
		String openTime_temp = null;
		String openTime_temp_new = null;

		String summary = null;
		String pubDate = null;

		BufferedWriter fwUn = null;
		BufferedWriter fwUnOld = null;

		while (true) {
			if (null == key) {

				while (true) {

					// 237208:01d0549b-8246-5b5c-9c07-bffe1cdcd2cd
					// 10185100:4dc8657f-d2b5-596e-b50c-b566a113b1f9
					try {
						result = template.getForObject(
								"http://192.168.1.4:8092/executedN/_design/court_only/_view/court_only?inclusive_end={inclusive_ends}&limit={limits}&skip=0",
								// "http://192.168.1.30:8092/etp_tt/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&skip=0",
								String.class, urlVariables);
								// 10,504,378

						// key="4dbe6c31-e1b6-547f-9ee2-de5b351ef6fc";
						// urlVariables.put("startkeys", "\"" + key + "\"");
						// result = template.getForObject(
						// "http://192.168.1.5:8092/executedN/_design/ex_all/_view/ex_all?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=0",
						// String.class, urlVariables);

						log.info(i + "  5000分页:" + key);
						break;
					} catch (Exception e) {
						log.error("---------------------------> 连BC查询   --超时");
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {

						}
						log.error(e.getMessage());
					}
				}

			} else {
				while (true) {
					try {
						// key="4dc8657f-d2b5-596e-b50c-b566a113b1f9";
						urlVariables.put("startkeys", "\"" + key + "\"");
						result = template.getForObject(
								"http://192.168.1.4:8092/executedN/_design/court_only/_view/court_only?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=1",
								// "http://192.168.1.30:8092/etp_tt/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=1",
								String.class, urlVariables);
						log.info(i + "  5000分页:" + key);
						break;
					} catch (Exception e) {
						log.error("---------------------------> 连BC查询   --超时");
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {

						}
						log.error(e.getMessage());
					}
				}
			}
			String value = new String(result.getBytes("ISO-8859-1"), "utf-8");

			FfWholeCourtVO jsn = gson.fromJson(value, FfWholeCourtVO.class);

			for (RowsWholeCourtVO r : jsn.getRows()) {

				if (uniqi % size == 0) {
					String folderPathUn = "D:/lm/log/法院旧数据按案号去重后/";
					String filePathUn = "D:\\lm\\log\\法院旧数据按案号去重后\\" + uniqi + ".json";
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

				if (noCourtOld % size == 0) {
					String folderPathUn = "D:/lm/log/法院旧数据无案号/";
					String filePathUn = "D:\\lm\\log\\法院旧数据无案号\\" + noCourtOld + ".json";
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
								fwUnOld = new BufferedWriter(
										new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} // 指定编码格式，以免读取时中文字符异常
						} else {
							fileSUn.delete();
							fileSUn = new File(filePathUn);
							try {
								fwUnOld = new BufferedWriter(
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

				key = r.getKey();
				i++;
				// System.out.println(i + ":" + key);

				System.out.println(i);
				WholeCourtVO whVo = r.getValue();// 00006a27-0c95-48a3-8249-645457d71b65
				if (whVo == null) {
					continue;
				}

				if (!StringUtils.isNull(whVo.getCaseNum())) {
					if (bf.contains(StringUtils.NBG.generate(whVo.getCaseNum()).toString())) {

					} else {
						uniqi++;
						// 写数据 旧数据在新数据中不存在的
						writerString(fwUn, new Gson().toJson(whVo));
					}
				} else {// 旧数据 没有法院或案号
					noCourtOld++;
					writerString(fwUnOld, new Gson().toJson(whVo));
				}

			}

			if (i >= jsn.getTotal_rows()) {
				break;
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
