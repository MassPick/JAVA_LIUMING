package cn.com.szgao.enterprise;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.poi.ss.formula.functions.T;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.web.client.RestTemplate;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.bucket.BucketManager;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.view.DefaultView;
import com.couchbase.client.java.view.DesignDocument;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.dto.CompanyVO;
import cn.com.szgao.dto.CourtVO;
import cn.com.szgao.dto.Doc;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.FfFirstIndusrtyVO;
import cn.com.szgao.dto.FirstInstryVO;
import cn.com.szgao.dto.IndustryVO;
import cn.com.szgao.dto.RowsFirstIndusrtyVO;
import cn.com.szgao.util.ConfigUtils;
//import cn.com.szgao.dto.EtpEventItemCBIndexVO;
//import cn.com.szgao.dto.EtpEventItemChildVO;
//import cn.com.szgao.dto.EtpEventItemVO;
//import cn.com.szgao.dto.Ff;
//import cn.com.szgao.dto.FfCompanyVO;
//import cn.com.szgao.dto.FfCourtVO;
//import cn.com.szgao.dto.FfFirstIndusrtyVO;
//import cn.com.szgao.dto.FfEtpEventItemVO;
//import cn.com.szgao.dto.FfEtpVO;
//import cn.com.szgao.dto.Rows;
//import cn.com.szgao.dto.RowsCompanyVO;
//import cn.com.szgao.dto.RowsCourtVO;
//import cn.com.szgao.dto.RowsFirstIndusrtyVO;
//import cn.com.szgao.dto.RowsEtpEventItemVO;
//import cn.com.szgao.dto.RowsEtpVO;
//import cn.com.szgao.dto.EtpVO;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.PostgresqlUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;
import rx.Observable;
import rx.functions.Func1;

/**
 * 将CB中行业第个词进行 行业清洗后
 * 
 * @author liuming
 * @Date 2016年7月22日 上午10:11:23
 */
@SuppressWarnings("unused")
public class UpdateFirstIndusrtyFromCBToPG_Uniq_First {
	private static Logger logger = LogManager.getLogger(FileIntoDataBase2p5.class.getName());

	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}

	static Bucket bucket = null;

	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	static long count = 0;

	static long SUM_Count = 0;// 总数
	static long EXIST = 0;//
	static long INTO = 0;//
	
	static long SUMM=0;
	static long SUMNO=0;
	
	

	public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
		long startTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		Date date = new Date(startTime);
		log.info("开始时间--------------------" + formatter.format(date));
		log.debug("-------------------------------------------------- 开始debug!!!!!!!!!!!!!!!!!!!");
		log.info("-------------------------------------------------- 开始info!!!!!!!!!!!!!!!!!!!");

		doListData();

		log.debug("-------------------------------------------------- 结束debug!!!!!!!!!!!!!!!!!!!");
		log.info("-------------------------------------------------- 结束info!!!!!!!!!!!!!!!!!!!");

		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		log.info("-----空字符串： "+SUMNO);
		log.info("-----没匹配的： "+SUMM);
		log.info("结束时间--------------------" + formatter.format(endDate));
		log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
		log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
		log.info("Total : " + i);
		log.info("Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
		log.info("Speed : " + (float) (i / ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60)) + "个/小时");
	}

	// static EtpEventItemCBIndexVO vo = null;
	static int i = 0;
	static long i3 = 0;
	private static Logger log = LogManager.getLogger(UpdateFirstIndusrtyFromCBToPG_Uniq_First.class);

	/**
	 * 批量处理
	 * 
	 * @throws UnsupportedEncodingException
	 * @return void
	 * @author liuming
	 * @throws ParseException
	 * @date 2016年1月19日 下午4:19:32
	 */
	public static void doListData() throws UnsupportedEncodingException, ParseException {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		urlVariables.put("limits", 5000);
		urlVariables.put("inclusive_ends", true);
		urlVariables.put("skips", 0);
		String key = null;
		RestTemplate template = new RestTemplate();
		String result = null;
		Gson gson = new Gson();
		String companyId = null;
		String changeEvent = null;
		String changeBefore = null;
		String changeAfter = null;

		JsonDocument queryDoc = null;
		JsonObject obj = null;
		JsonDocument doc = null;
		int source = 0;
		String approvalDate = null;
		String publishDate = null;

		String approvalDateNew = null;
		String publishDateNew = null;

		String approvalDate_temp = null;
		String publishDate_temp = null;

		String summary = "";
		String courtName = null;

		String openTime = null;
		String openTime_temp = null;
		String openTime_temp_new = null;
		String key1 = "";
		String company = "";
		String province = "";
		String city = "";
		long numP = 0;
		long numN = 0;
		long _numN = 0;

		int numNN = 0;

		JsonObject content = null;
		List<JsonDocument> documents = new ArrayList<JsonDocument>();

		List<Map<String, Object>> listMap1 = new LinkedList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 没有省的
		Map<String, Object> map1 = new HashMap<String, Object>();
		// 没企业名
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> mapN = new HashMap<String, Object>();

		List<CodeVO> list = new ArrayList<CodeVO>();

		File fileS = new File("D:/lm/log/b140216a.log");
		String encoding_from = "UTF-8";
		BufferedWriter fw = null;
		// try {
		// if (!fileS.exists()) {
		// try {
		// fileS.createNewFile();
		// } catch (IOException e) {
		// }
		// fw = new BufferedWriter(new OutputStreamWriter(new
		// FileOutputStream(fileS, true), encoding_from)); // 指定编码格式，以免读取时中文字符异常
		// } else {
		// // fileS.delete();
		// // fileS = new File("D:/lm/log/b13013911224A.log");
		// fw = new BufferedWriter(new OutputStreamWriter(new
		// FileOutputStream(fileS, true), encoding_from)); // 指定编码格式，以免读取时中文字符异常
		// }
		// } catch (FileNotFoundException e1) {
		// e1.printStackTrace();
		// }
		Analyzer anal = new IKAnalyzer();
		FirstInstryVO arch = null;
		FirstInstryVO vo_cb = null;

		// key排序
		String ss = "";
		for (Entry<String, IndustryVO> entry : ExecutorsText.mapV_AND_N.entrySet()) {
			if (entry.getValue() != null && entry.getKey() != null && !"".equals(entry.getKey())) {
				ss += entry.getKey() + "|";
			}
		}
		String[] arrStr1_ss = ss.split("\\|");
		StringUtils.sortStringArray(arrStr1_ss, 1);// 排序

		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtInsert = null;
		int flag = 0;
		int size = 5;

		String sqlInsert = "INSERT INTO first_indusrty_uniq_first_t(in_name,in_count,industry_name,industry_code,industry_id,ik_str,in_name_from)VALUES(?,?,?,?,?,?,?)";
		try {
			conn = PostgresqlUtils.getConnection();
			try {
				conn.setAutoCommit(false);
				stmtInsert = conn.prepareStatement(sqlInsert);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// writerString(fw, "------------------------------------kaisi----");
		while (true) {
			if (null == key) {
				result = template.getForObject(
						// "http://192.168.1.3:8092/etp_t/_design/etp_t/_view/etp_t_s?inclusive_end={inclusive_ends}&limit={limits}&skip=0",
						"http://192.168.1.114:8092/firstIndustry/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&skip=0",

				String.class, urlVariables);
				writerString(fw, i + "  5000分页:" + key);
				log.info(i + "  5000分页:" + key);

			} else {
				urlVariables.put("startkeys", "\"" + key + "\"");
				result = template.getForObject(
						// "http://192.168.1.3:8092/etp_t/_design/etp_t/_view/etp_t_s?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=1",
						"http://192.168.1.114:8092/firstIndustry/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=1",

				String.class, urlVariables);
				writerString(fw, i + "  5000分页:" + key);
				log.info(i + "  5000分页:" + key);

			}
			String value = new String(result.getBytes("ISO-8859-1"), "utf-8");

			// FfCompanyVO jsn = gson.fromJson(value, FfCompanyVO.class);
			FfFirstIndusrtyVO jsn = gson.fromJson(value, FfFirstIndusrtyVO.class);

			for (RowsFirstIndusrtyVO r : jsn.getRows()) {
				key = r.getKey();
				i++;
				System.out.println((i) + ":" + key);
				// System.out.println(i);
				FirstInstryVO whVo = r.getValue();
				String str = whVo.getName();
				Long count = whVo.getCount();
				System.out.println(str);
				if (StringUtils.isNull(str)) {
					SUMNO++;
					System.out.println("空串： "+SUMNO);
					continue;
				}
				int falgg = 0;
				IndustryVO vo = null;

				// String str_map[]=new String[]{};
				String str_map[] = new String[ExecutorsText.mapV_AND_N.size()];


				for (int i = 0; i < arrStr1_ss.length; i++) {
					String str_arrStr1_ss = arrStr1_ss[i];
					
					if (str.indexOf(str_arrStr1_ss) != -1) {// 说明可入库

						// "飞机".indexOf("飞机场")
						falgg = 1;
						vo =ExecutorsText.mapV_AND_N.get(str_arrStr1_ss);
						break;

					} else { // "飞机场".indexOf("飞机")
						falgg = 0;
					}
				}
				

				StringReader reader = new StringReader(str);
				// 分词
				TokenStream ts = null;
				ts = anal.tokenStream("", reader);
				CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
				String strs = "";
				String strs_temp = "";
				try {
					ts.reset();

					// 遍历分词数据
					while (ts.incrementToken()) {
						String temp = term.toString();
						if (!StringUtils.isNull(temp)) {
							// getIndustry(temp);
							strs += term.toString() + "|";
							// System.out.print(term.toString() + "|" );
						}
					}
					reader.close();

					String[] arrStr1 = strs.split("\\|");
					StringUtils.sortStringArray(arrStr1, 1);// 排序
					for (int i = 0; i < arrStr1.length; i++) {
						strs_temp += arrStr1[i] + "|";
					}
					if (!StringUtils.isNull(strs_temp) && strs_temp.indexOf("|") != -1) {
						strs_temp = strs_temp.substring(0, strs_temp.lastIndexOf("|"));
					}
					

				} catch (IOException e) {
					e.printStackTrace();
				}

				if (falgg == 0) {
					
					try {

						stmtInsert.setString(1, str);
						stmtInsert.setLong(2, count);
						stmtInsert.setString(3, "");
						stmtInsert.setString(4, "");
						stmtInsert.setString(5, "");
						stmtInsert.setString(6, strs_temp);
						stmtInsert.setString(7, "");

						stmtInsert.addBatch();
//						stmtInsert.execute();
						
						SUMM++;
						System.out.println("---------------- 没匹配到的");

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					continue;
					
				}
				if (falgg == 1) {
					try {

						stmtInsert.setString(1, str);
						stmtInsert.setLong(2, count);
						stmtInsert.setString(3, vo.getIndustry_name());
						stmtInsert.setString(4, vo.getIndustry_code());
						stmtInsert.setString(5, vo.getIndustry_id());
						stmtInsert.setString(6, strs_temp);
						stmtInsert.setString(7, vo.getIn_name());

						stmtInsert.addBatch();
//						stmtInsert.execute();

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				System.out.println(str);

				flag++;

				if (flag >= size) {
					try {
						while (true) {
							try {
								stmtInsert.executeBatch();
								break;
							} catch (Exception e) {
								try {
									Thread.sleep(3000);
									System.out.println("睡三秒");
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								logger.info("---------------------------> 连BC超时");
								logger.error(e.getMessage());
							}
						}

						conn.commit();
						stmtInsert.clearBatch();
						flag = 0;
						// tempCourtName = null;
						conn.setAutoCommit(false);
						stmtInsert = conn.prepareStatement(sqlInsert);
						
						logger.info("----写入PG库成功");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// i3++;
				// System.out.println(" ID：" + key + " 总数:" + i + " 数：" + i3);

			}
			
			try {
				while (true) {
					try {
						stmtInsert.executeBatch();
						break;
					} catch (Exception e) {
						try {
							Thread.sleep(3000);
							System.out.println("睡三秒");
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						logger.info("---------------------------> 连BC超时");
						logger.error(e.getMessage());
					}
				}
				
				conn.commit();
				stmtInsert.clearBatch();
				
				
				// stmtInsert.close();
				// conn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (i >= jsn.getTotal_rows()) {
				break;
			}
		}

		try {
			stmtInsert.executeBatch();
			conn.commit();
			stmtInsert.clearBatch();
			stmtInsert.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static Bucket bucketEtp = null;
	static JsonDocument queryDocEtp = null;
	static JsonObject obj2 = null;
	static JsonDocument doc2 = null;
	static Gson gson = new Gson();

	/**
	 * 
	 * 1 表示 etp_tt存在 etp_ttN不存在 0 都存在
	 * 
	 * @param companyId
	 * @return
	 * @return Integer
	 * @author liuming
	 * @date 2016年2月25日 下午12:22:35
	 */
	public static Integer findDiff(String companyId, EnterpriseVO whVo) {
		// 企业基本信息
		while (true) {
			try {
				bucketEtp = (Bucket) CouchbaseConnect.commonBucket("192.168.1.4:8091", "etp_ttN");
				queryDocEtp = ((com.couchbase.client.java.Bucket) bucketEtp).get(companyId, 60, TimeUnit.MINUTES);
				break;
			} catch (Exception e) {
				log.info("---------------------------> 连BC超时");
				log.error(e.getMessage());
			}
		}
		// etp_tt存在 etp_ttN不存在
		if (null == queryDocEtp) {

			obj2 = JsonObject.fromJson(gson.toJson(whVo));
			// 创建JSON文档
			doc2 = JsonDocument.create(companyId, obj2);
			while (true) {
				try {
					// 更新文档
					CouchbaseConnect.commonBucket("192.168.1.30:8091", "ddd").upsert(doc2);
					break;
				} catch (Exception e) {
					log.error("--------------------------->本地 连BC超时");
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					log.error(e.getMessage());
				}
			}

			// 还入PG
			// indertPG(companyId, whVo);
			// list.add(new EtpSVO(companyId, 1, 0));
			return 1;
		}
		return 0;

	}

	// static Connection conn = null;
	// static PreparedStatement stmtInsert = null;
	// static String sqlInsert = "INSERT INTO
	// etp_chayi_etp_ttN_etp_tt_t(provice,company,strcontent,key )"
	// + "VALUES(?,?,?::uuid)";
	//
	// static {
	// try {
	// conn = PostgresqlUtilsLocal.getConnection();
	// } catch (ClassNotFoundException e2) {
	// e2.printStackTrace();
	// } catch (SQLException e2) {
	// e2.printStackTrace();
	// }
	// try {
	// stmtInsert = conn.prepareStatement(sqlInsert);
	// } catch (SQLException e2) {
	// e2.printStackTrace();
	// }
	// }

	// public static void indertPG(String companyId, EnterpriseVO whVo) {
	//
	// try {
	// stmtInsert.setString(1, whVo.getProvince());
	// stmtInsert.setString(1, whVo.getCompany());
	// stmtInsert.setString(3, gson.toJson(whVo));
	// stmtInsert.setString(4, companyId);
	// stmtInsert.execute();
	//
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + "\n");

			// fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			// Log.error("写文件异常"+e.getMessage());
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

	public static Pattern pattern = Pattern.compile("[0-9a-zA-Z\u4e00-\u9fa5]*");
	public static Pattern pattern2 = Pattern.compile("[0-9a-zA-Z]*");

	/**
	 * 处理注册号与信用代码
	 * 
	 * @param value
	 * @return
	 */
	public static List<CodeVO> getregNumList(String value) {
		if (null == value) {
			return null;
		}
		List<CodeVO> list = new ArrayList<CodeVO>();
		String code = value.replaceAll("[&nbsp;\r\t]", "");
		if (code.length() == 15 || code.length() == 13) {
			if (pattern2.matcher(code).matches()) {
				// 注册号
				list.add(new CodeVO(code, 1));
			} else {
				if (code.substring(code.length() - 1, code.length()).equals("号") || code.substring(0, 1).equals("企")) {
					// 注册号
					list.add(new CodeVO(code, 1));
				}
			}
			return list;

		} else if (code.length() == 18) {
			if (!pattern2.matcher(code).matches()) {
				return null;
			}
			// 信用代码
			list.add(new CodeVO(code, 2));
			return list;
		} else {
			// 可能存在问题，可能是注册号与信用代码中间存在特殊字符
			return getCodes(code);
		}
	}

	/**
	 * 拆分注册码信用代码
	 * 
	 * @param value
	 * @return
	 */
	public static List<CodeVO> getCodes(String value) {
		Matcher matcher = null;
		StringBuffer sb = new StringBuffer();
		String[] a = value.split("");
		for (String c : a) {
			matcher = pattern.matcher(c);
			if (matcher.matches()) {
				sb.append(c);
			} else {
				sb.append(",");
			}
		}
		a = sb.toString().split(",");
		List<CodeVO> list = new ArrayList<CodeVO>();
		for (String c : a) {
			if (null != c && !"".equals(c)) {
				if (c.length() <= 4) {
					continue;
				}
				if (c.length() == 15 || c.length() == 13) {
					if (pattern2.matcher(c).matches()) {
						// 注册号
						list.add(new CodeVO(c, 1));
					} else {
						if (c.substring(c.length() - 1, c.length()).equals("号") || c.substring(0, 1).equals("企")) {
							// 注册号
							list.add(new CodeVO(c, 1));
						}
					}
				} else if (c.length() == 18) {
					// if(!pattern2.matcher(c).matches()){continue;}
					// 信用代码
					list.add(new CodeVO(c, 2));
				} else if (c.substring(c.length() - 1, c.length()).equals("号") || c.substring(0, 1).equals("企")) {
					// 注册号
					list.add(new CodeVO(c, 1));
				}
			}
		}
		return list;
	}

}
