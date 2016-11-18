package cn.com.szgao.clean.court;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.postgresql.jdbc2.ArrayAssistantRegistry;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import cn.com.szgao.dto.ClientVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.StringUtils;

/**
 * 取文书的案由
 * 
 * @author liuming
 * @ClassName ExtractionHtmlCaseCause
 * @date 2016年5月25日 下午4:19:15
 */
public class ExtractionHtmlGetCaseCause2 {
	
	public static List<String> LISTCasecause_small_small = new ArrayList<String>();// 案由小小类
	public static List<String> LISTCasecause_small = new ArrayList<String>();// 案由小类
	public static List<String> LISTCasecause_middle = new ArrayList<String>();// 案由中类
	public static List<String> LISTCasecause_big = new ArrayList<String>();// 案由大类

//	static {
//		LISTCasecause_small_small = new ArrayList<String>();// 案由小小类
//		LISTCasecause_small = new ArrayList<String>();// 案由小类
//		LISTCasecause_middle = new ArrayList<String>();// 案由中类
//		LISTCasecause_big = new ArrayList<String>();// 案由大类
//	}

	/**
	 * 得到案由的列表
	 * 
	 * @return void
	 * @author liuming
	 * @date 2016年6月8日 上午10:14:55
	 */
	public static void getlistsCasecause() {

		
		  List<String> LISTCasecause_small_small1 = new ArrayList<String>();// 案由小小类
		  List<String> LISTCasecause_small1 = new ArrayList<String>();// 案由小类
		  List<String> LISTCasecause_middle1 = new ArrayList<String>();// 案由中类
		  List<String> LISTCasecause_big1 = new ArrayList<String>();// 案由大类
		
		// TreeMap<String, ClientVO> mapV = new TreeMap<String, ClientVO>();

		PreparedStatement provinceStmt = null;// 查询省
		Connection conn = null;// 连接
		ResultSet rs = null;// 结果集
		String provinceSql = " SELECT  casecause_type,type_big,type_middle ,type_small,type_small_small   FROM casecause_type_t_copy WHERE source  ='官网案由分类'  ";

		try {
			conn = getConnection();
			provinceStmt = conn.prepareStatement(provinceSql); // 预编译查询
			rs = provinceStmt.executeQuery();// 查询省
			while (rs.next()) {
				if (!StringUtils.isNull(rs.getObject("type_big") != null ? rs.getObject("type_big").toString() : "")) {
					LISTCasecause_big1.add(rs.getObject("type_big").toString());
				}
				if (!StringUtils.isNull(rs.getString("type_middle") != null ? rs.getString("type_middle") : "")) {
					LISTCasecause_middle1.add(rs.getString("type_middle").toString());
				}
				if (!StringUtils.isNull(rs.getString("type_small") != null ? rs.getString("type_small") : "")) {
					LISTCasecause_small1.add(rs.getString("type_small").toString());
				}
				if (!StringUtils
						.isNull(rs.getString("type_small_small") != null ? rs.getString("type_small_small") : "")) {
					LISTCasecause_small_small1.add(rs.getString("type_small_small").toString());
				}
			}
			
			LISTCasecause_small_small=LISTCasecause_small_small1;
			LISTCasecause_small=LISTCasecause_small1;
			LISTCasecause_middle=LISTCasecause_middle1;
			LISTCasecause_big=LISTCasecause_big1;
			  
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != provinceStmt) {
					provinceStmt.close();
					provinceStmt = null;
				}
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	

	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		long da = System.currentTimeMillis();

		// File file = new
		// File("D:\\lm\\log\\民事案件-2016-01-04\\64722518-6e71-5328-bedd-957d7391171e.html");
		File file = new File("D:\\lm\\log\\民事案件-2016-01-04");

		// 88261f10-92d0-5f71-8340-2c8a86a8882b.html

		try {

			show(file);
			Statistics(file);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} finally {
			file = null;
		}

		logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis() - da) / 1000)) + "秒数");
		logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 1)) + "分钟");
		logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 60)) + "小时");
		logger.info("平均每秒" + (float) (SUM / (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (1 * 1))));
		logger.info("平均每分" + (float) (SUM / (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 1))));
		logger.info("平均每时" + (float) (SUM / (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 60))));
	}

	static int countNum = 0;
	static int countNotHtml = 0;

	/**
	 * 生成文件的个数
	 */
	static int countP = 0;

	static BufferedWriter fwUn = null;

	static BufferedWriter fwUn2 = null;
	static {
		String filePathUn = "D:/lm/log/案号22.txt";
		File fileSUn = new File(filePathUn);
		String encoding_from1U = "UTF-8";

		try {
			if (!fileSUn.exists()) {
				try {
					fileSUn.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fwUn2 = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				// fileSUn.delete();
				// fileSUn = new File(filePathUn);
				// try {
				// fwUn2 = new BufferedWriter(
				// new OutputStreamWriter(new FileOutputStream(fileSUn, true),
				// encoding_from1U));
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	static BufferedWriter fwUn3 = null;

	static {
		String filePathUn = "D:/lm/log/案号33.txt";
		File fileSUn = new File(filePathUn);
		String encoding_from1U = "UTF-8";

		try {
			if (!fileSUn.exists()) {
				try {
					fileSUn.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fwUn3 = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				// fileSUn.delete();
				// fileSUn = new File(filePathUn);
				// try {
				// fwUn2 = new BufferedWriter(
				// new OutputStreamWriter(new FileOutputStream(fileSUn, true),
				// encoding_from1U));
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
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
	// ----------------------王某某; 劳动争议一案; 广水市××山××大道××号; 纷一案中; 到庭 参加

	// ----------------------原审被告 ）南通启益建设集团有限公司; 法定代表 人尹向东;

	/**
	 * 递归遍历html文件
	 * 
	 * @param file
	 * @param province
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private static void show(File file) throws Exception {
		String html = null;
		Document doc;
		int i = 0;

		if (file.isFile()) {
			SUM++;

			// if(SUM<3332){
			// return;
			// }
			System.out.println(SUM);
			String suffix = file.getName();
			suffix = suffix.substring(suffix.indexOf(".") + 1, suffix.length());
			suffix = MAPS.get(suffix);
			if (null == suffix) {
				NOSTANDARD++;
				logger.error("非标准数据：" + NOSTANDARD + "  " + file.getPath());
				return;
			}
			if ("swf".equals(suffix)) {
				NOSTANDARDSWF++;
				logger.info("SWF数据：" + NOSTANDARDSWF + "  " + file.getPath());
				insertDataToBlob(file);
				return;
			}
			if ("doc".equals(suffix)) {
				NOSTANDARDDOC++;
				logger.info("DOC数据：" + NOSTANDARDDOC + "  " + file.getPath());
				insertDataToBlob(file);
				return;
			}

			// logger.info(SUM + " 路径:" + file.getPath());
			for (String val : charset) { // 匹配不同编码格式
				try {
					doc = Jsoup.parse(file, val);
				} catch (Exception e) {
					NOSUM++;
					logger.error("空数据：" + NOSUM + "  " + file.getPath());
					return;
				}
				String html2 = doc.text();

				if (html2 == null || "" == html2) {
					NOSUM++;
					logger.error("空数据：" + NOSUM + "  " + file.getPath());
					continue;
				}
				boolean garbled = getErrorCode(html2);// 判断编码是否错误
				if (garbled == false) {
					i++;
					if (i == 5) {
						html2 = null;
						ERRORSUM++;
						logger.error("异常字符错误条数：" + ERRORSUM + "  " + file.getPath());
					}
					continue;
				}

				System.out.println(file.getPath());

				if (countNum % 100 == 0) {
					countP++;
					String folderPathUn = "D:/lm/log/2016法院清洗案由5";
					String filePathUn = "D:\\lm\\log\\2016法院清洗案由5\\" + (countP) + ".json";
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

				// html = doc.body()
				// .getElementsByAttributeValue("style",
				// "LINE-HEIGHT:
				// 25pt;TEXT-ALIGN:justify;TEXT-JUSTIFY:inter-ideograph;
				// TEXT-INDENT: 30pt; MARGIN: 0.5pt 0cm;FONT-FAMILY: 仿宋;
				// FONT-SIZE: 16pt;")
				// .text();
				// Elements el = doc.body().getElementsByAttributeValue("style",
				// "LINE-HEIGHT:
				// 25pt;TEXT-ALIGN:justify;TEXT-JUSTIFY:inter-ideograph;
				// TEXT-INDENT: 30pt; MARGIN: 0.5pt 0cm;FONT-FAMILY: 仿宋;
				// FONT-SIZE: 16pt;");
				// for (Element element : el) {
				// String s = element.text();
				// System.out.println(s);
				// }
				// System.out.println(html);

				TreeSet<String> ts = new TreeSet<String>();

				int fa = 0;// 判断第一个a标签前是不是出现过当事人信息，如果大于0说明出现
				int flag_end = 0; // 判断是不是到了非当事人部分
				Elements all = doc.body().getAllElements();

				String case1 = getCaseFromStr(doc.body().text());
				System.out.println("---------->>案由：" + case1);
				writerString(fwUn2, (case1!=null?case1:"NULL")   + "#" +  "#" + file.getName());

				for (Element element_all : all) {
					String tagName = element_all.tagName();
					// System.out.println(tagName);

					if ("div".equals(tagName)) {
						Attributes as = element_all.attributes();
						for (Attribute attribute : as) {
							// System.out.println(attribute.getKey());
							// System.out.println( attribute.getValue() );

							if ("style".equals(attribute.getKey())
									&& "LINE-HEIGHT: 25pt;TEXT-ALIGN:justify;TEXT-JUSTIFY:inter-ideograph; TEXT-INDENT: 30pt; MARGIN: 0.5pt 0cm;FONT-FAMILY: 仿宋; FONT-SIZE: 16pt;"
											.equals(attribute.getValue())) {
								// System.out.println(element_all.text());
								fa++;

								String temp1 = element_all.text();
								// String temp2=getCase(temp1);
								// System.out.println(temp2);

								if (StringUtils.isNull(temp1)) {
									continue;
								}
								temp1 = temp1.replace(":", "：").replace(",", "，").replace(";", "：");

								if (!StringUtils.isNull(temp1)) {
									for (String val1 : CLIENT_END) {
										if (temp1.indexOf(val1) != -1) {
											flag_end = 1;

											writerString(fwUn3, val1 + "#" + temp1 + "#" + SUM + "#" + file.getName());
											break;
										}
									}
								}

								if (flag_end == 1) {
									break;
								}

								// 先得到要洗内容 如 原告王又阊（曾用名：王有昌），男，1967年12月15日出生。 先得到
								// " 原告王又阊（曾用名：王有昌），"
								// String[] cArray = new String[] { "，", "。" };
								// for (int ii = 0; ii < cArray.length; ii++) {
								// if (temp1.indexOf(cArray[i].toString()) !=
								// -1) {
								// temp1 = temp1.substring(0,
								// temp1.indexOf(cArray[i].toString()));
								// if (temp1.indexOf("（") != -1) {//规避
								// 委托代理人董艳丽（特别授权，公司员工）。
								// temp1 = temp1.substring(0,
								// temp1.indexOf("（"));
								// }
								// break;
								// }
								// }
								for (ClientVO clvo : mapClientList) {
									// System.out.println("keyV= " +
									// entry.getKey() + " and
									// value= " + entry.getValue());
									if (!StringUtils.isNull(clvo.getClientFrom())) {

										String key = clvo.getClientFrom();
										if (temp1.indexOf(key) != -1) {
											// 得到前缀后的内容
											String cl = temp1.substring(temp1.indexOf(key) + key.length());
											String[] strs1 = splitClient(cl);
											// String[] strs =
											// cl.split("[；,。,：]");
											if (null != strs1) {
												for (String s : strs1) {
//													System.out
//															.println(clvo.getClientType() + "#" + key + "#" + s + ";");
//													writerString(fwUn2, clvo.getClientType() + "#" + key + "#" + s + "#"
//															+ SUM + "#" + file.getName());
													 
													ts.add(clvo.getClientType() + "#" + key + "#" + s);
													// break;
												}
											}

											break;
										}
									}
								}

								// String resultClient = sbfClient.toString();
								// if (resultClient.indexOf(";") != -1) {
								// resultClient = resultClient.substring(0,
								// resultClient.length() - 1);
								// }

								// System.out.println("--------->"+resultClient);

								// writerString(fwUn, temp2+"|"+file.getPath());
								// writerString(fwUn2, resultClient );
								countNum++;

							}
							if (fa > 20) {
								flag_end = 1;
								break;
							}
						}
					}

					if (flag_end == 1) {
						break;
					}

					if (fa > 0) {
						// 第一次遇到div 以外的标签就退出
						// if ("a".equals(tagName)) {
						// break;
						// }
					}
				}

				// 遍历TreeSet
				StringBuffer sbfClient = new StringBuffer();
				if (ts.size() > 0) {
					Iterator it = ts.iterator();
					while (it.hasNext()) {
						String ss = it.next().toString();
						sbfClient.append(ss + ";");
						System.out.println("结果： " + ss + ",");
					}
					String resultClient = sbfClient.toString();
					if (resultClient.indexOf(";") != -1) {
						resultClient = resultClient.substring(0, resultClient.length() - 1);
						System.out.println("最后结果： " + resultClient);
					}
				}

				if (null == html) {
					NOSUM++;
					// logger.error("无关键数据：" + NOSUM + " " + file.getPath());
					return;
				} else {
					// 判断是不是文书
					int flag = 0;
					for (String per : PERSONNEL) {
						if (html.contains(per)) {
							flag = 1;
							break;
						}
					}
					if (flag == 0) {
						return;
					}
				}

				// }

				if (garbled == true) {
					if (html.length() > 120) {
						logger.info(
								"----> " + html.substring(0, 50) + "---------" + html.substring(html.length() - 50));
					} else {
						logger.info("----> " + html);
					}
				}

				i = 0;
				break;
			}
			String fileName2 = file.getPath().substring(file.getPath().lastIndexOf("\\") + 1,
					file.getPath().lastIndexOf("."));
			if (null != html) {
				// 写html变量的数据到Blob
				insertData(html, fileName2);
				fileName2 = null;
			}
		} else if (file.isDirectory()) {
			logger.info("文件夹: " + file.getPath());
			File[] files = file.listFiles();
			for (File fi : files) {
				show(fi);
			}
		}
	}

	/**
	 * 得到案由
	 * 
	 * @Description: TODO
	 * @param text
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月8日 上午10:24:07
	 */
	public static String getCaseFromStr(String text) {

	
		if (StringUtils.isNull(text)) {
			return null;
		}
		if(text.length()>200){
			System.out.println(text.substring(0, 199));
		}
		for (String val : LISTCasecause_small_small) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}
		for (String val : LISTCasecause_small) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}
		for (String val : LISTCasecause_middle) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}
		for (String val : LISTCasecause_big) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}

		String result = null;

		return result;
	}

	public static TreeMap<String, ClientVO> mapClient = null;
	// static {
	// mapClient = listClientVO();
	// System.out.println("加载当事人分类");
	//
	// for (Entry<String, ClientVO> entry : mapClient.entrySet()) {
	//
	// System.out.println(entry.getKey());
	// }
	// }

	public static List<ClientVO> mapClientList = null;

	static {
		mapClientList = getlistsClientVO();

		getlistsCasecause();
		// for (ClientVO clientVO : mapClientList) {
		// System.out.println(clientVO.getClientFrom());
		// }
	}

	/**
	 * 字符串数组去重
	 * 
	 * @param s
	 * @return
	 */
	public static String[] uniqStrings(String[] s) {
		if (s == null) {
			return null;
		}
		if (s.length == 1) {
			return s;
		}
		TreeSet<String> tr = new TreeSet<String>();
		for (int i = 0; i < s.length; i++) {
			tr.add(s[i]);
		}
		String[] s2 = new String[tr.size()];
		for (int i = 0; i < s2.length; i++) {
			s2[i] = tr.pollFirst();// 从TreeSet中取出元素重新赋给数组
		}
		return s2;
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
		sss = sss.replace("(", "（").replace(")", "）").replace("【", "（").replace("】", "）").replace("[", "（").replace("]",
				"）");
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

	/**
	 * 通用连接数据库的方法 PG DATA = duplicatedb
	 * 
	 * @param conn:
	 *            连接对象
	 * @author xiongchangyi
	 * @since 2018-05-08
	 * @version 1.0
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
	 * 得到当事人分类列表
	 * 
	 * @return
	 */
	public static TreeMap<String, ClientVO> listClientVO() {
		TreeMap<String, ClientVO> mapV = new TreeMap<String, ClientVO>();

		PreparedStatement provinceStmt = null;// 查询省
		Connection conn = null;// 连接
		ResultSet rs = null;// 结果集
		String provinceSql = "SELECT    client_from, client_type   FROM court_client_type_t  where  client_from is NOT NULL   ORDER BY   order_id ASC ";

		try {
			conn = getConnection();
			provinceStmt = conn.prepareStatement(provinceSql); // 预编译查询
			rs = provinceStmt.executeQuery();// 查询省
			while (rs.next()) {
				ClientVO vo = new ClientVO();
				vo.setClientFrom(rs.getObject(1).toString());
				vo.setClientType(rs.getObject(2).toString());

				mapV.put(rs.getString(1), vo);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != provinceStmt) {
					provinceStmt.close();
					provinceStmt = null;
				}
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return mapV;
	}

	/**
	 * 得到当事人分类列表 list
	 * 
	 * @return
	 */
	public static List<ClientVO> getlistsClientVO() {
		List<ClientVO> list = new ArrayList<ClientVO>();
		// TreeMap<String, ClientVO> mapV = new TreeMap<String, ClientVO>();

		PreparedStatement provinceStmt = null;// 查询省
		Connection conn = null;// 连接
		ResultSet rs = null;// 结果集
		String provinceSql = "SELECT    client_from, client_type   FROM court_client_type_t  where  client_from is NOT NULL   ORDER BY   order_id ASC ";

		try {
			conn = getConnection();
			provinceStmt = conn.prepareStatement(provinceSql); // 预编译查询
			rs = provinceStmt.executeQuery();// 查询省
			while (rs.next()) {
				ClientVO vo = new ClientVO();
				vo.setClientFrom(rs.getObject(1).toString());
				vo.setClientType(rs.getObject(2).toString());
				list.add(vo);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != provinceStmt) {
					provinceStmt.close();
					provinceStmt = null;
				}
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return list;
	}

	

	/**
	 * 得到案由
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年5月25日 下午6:10:30
	 */
	public static String getCase(String str) {
		String temp = null;
		if (StringUtils.isNull(str)) {
			return null;
		}
		str = str.replace(":", "：").replace(",", "，");
		// if(str.indexOf("：")!=-1&&str.indexOf("，")!=-1){
		// temp=str.substring(0,str.indexOf("："));
		// return temp ;
		// }

		String[] cArray = new String[] { "：", "，" };
		for (int i = 0; i < cArray.length; i++) {

			if (str.indexOf(cArray[i].toString()) != -1) {
				temp = str.substring(0, str.indexOf(cArray[i].toString()));
				return temp;
			}
		}

		return str;
	}

	/**
	 * 从 去前缀后的内容 得到当事人信息 以 、判断是不是有多个
	 * 
	 * @param str
	 * @return
	 */
	public static String[] splitClient(String str) {

		if (StringUtils.isNull(str)) {
			return null;
		}

		// 去括号内容
		str = removeBlank(str);
		String str1 = null;

		str = str.replace(":", "：").replace(",", "，").replace(";", "；");
		String[] cArray = new String[] { "，", "；", "。" };// 行结束
		for (int i = 0; i < cArray.length; i++) {

			if (str.indexOf(cArray[i].toString()) != -1) {
				str1 = str.substring(0, str.indexOf(cArray[i].toString()));

				if (str1.equals("：") || str1.equals("。") || str1.equals("；") || str1.equals("，")) {// 原告：。
					str1 = null;
					continue;
				}
				// str1 =
				// str1.split("[。，；：]").length>0?str1.split("[。，；：]")[0]:"" ;
				// //委托代理人。陈美琴。 第一个可能是。
				if (StringUtils.isNull(str1)) {
					continue;
				} else {
					break;
				}
			}
		}

		if (StringUtils.isNull(str1)) {
			return null;
		}

		String strs[] = str1.split("、");
		// String[]strs1 = new String[]{};
		for (int i = 0; i < strs.length; i++) {
			String stri = strs[i].replace("：", "").replace("；", "").replace("，", "").replace("。", "");
			strs[i] = stri;
		}
		return strs;
	}

	// 判断是否存在乱码
	public static boolean getErrorCode(String value) {
		for (String val : ERCOEDING) {
			int index = value.indexOf(val);
			if (index <= 0) {
				continue;
			}
			return false;
		}
		return true;
	}

	public static void Statistics(File file) {
		logger.info("--------------------- " + file.getPath());
		COUNT = SUM - ERRORSUM - NOSUM - NOSTANDARD - NOSTANDARDSWF - NOSTANDARDDOC;
		logger.info("所有数据：" + SUM + "条");
		logger.info("正确数据：" + COUNT + "条");
		logger.info("错误数据" + ERRORSUM + "条");
		logger.info("空数据" + NOSUM + "条");
		logger.info("非标准数据" + NOSTANDARD + "条");
		logger.info("SWF数据" + NOSTANDARDSWF + "条");
		logger.info("SWF数据" + NOSTANDARDDOC + "条");
	}

	/**
	 * 把抽取的html放入Blob/ES
	 * 
	 * @param data
	 *            抽取的html正文
	 * @param fileName
	 *            文件名称
	 * @author xiongchangyi
	 * @throws URISyntaxException
	 * @throws InvalidKeyException
	 * @throws StorageException
	 * @throws IOException
	 */
	public static void insertData(String data, String fileName)
			throws InvalidKeyException, URISyntaxException, StorageException, IOException {
		// 往Blob上面写数据，文件名称是Html的名称，以txt格式存储

		while (true) {
			try {
				CloudBlockBlob blob = container.getBlockBlobReference(fileName + ".txt");
				blob.uploadText(data);
				break;
			} catch (Exception e) {
				try {
					Thread.sleep(5000);
					logger.error("连blob 超时------");
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		// System.out.println(blob.downloadText());
	}

	public static void insertDataToBlob(File file) {
		try {
			while (true) {
				try {
					String fileName2 = file.getPath().substring(file.getPath().lastIndexOf("\\") + 1);
					CloudBlockBlob blob = container.getBlockBlobReference(fileName2);
					blob.upload(new FileInputStream(file), file.length());
					// System.out.println("upload swf ok");
					break;
				} catch (Exception e) {
					try {
						Thread.sleep(5000);
						logger.error("连blob 超时------");
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getDataAll_XJ(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}

		Element divs1 = doc.select("div#wenshu").first();

		value = divs1.text();
		value = getReplaceAll(value);
		return value;
	}

	/**
	 * 安徽
	 * 
	 * @param doc
	 * @return
	 */
	public static String getDataAll_AH(Document doc) {
		String value = "";
		String temp = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		value = doc.body().text();

		if (null == value || "" == value) {
			return null;
		} else {
			value = getReplaceAll(value);
		}

		int index = 0;

		for (String date : DATESTATUS) {
			if ("点击".equals(date) || "点击数".equals(date) || "浏览次数".equals(date) || "浏览数".equals(date)
					|| "浏览".equals(date) || "访问人数".equals(date) || "发布时间".equals(date)) {

				if (value.contains(date)) {
					int indexTemp = value.indexOf(date);
					// 数字个数 及- "2015-10-11 20:32"
					int num = 0;

					if (value.contains("发布日期") && value.contains("浏览次数")) {
						int temUp = value.lastIndexOf("发布日期");
						int temDown = value.lastIndexOf("浏览次数");
						if (temUp >= temDown) {
							indexTemp = temUp;
							temp = value.substring(indexTemp + "发布日期".length(), indexTemp + "发布日期".length() + 15);
							for (int i = temp.length(); --i >= 0;) {
								if ("-".charAt(0) == temp.charAt(i)) {
									num++;
								}
							}
						} else {
							indexTemp = temDown;
							temp = value.substring(indexTemp + "浏览次数".length(), indexTemp + "浏览次数".length() + 15);
						}
					}
					num += includeNumericNum(temp);

					if (temp.contains("次")) {
						value = value.substring(indexTemp + date.length() + num + 1);
					} else {
						value = value.substring(indexTemp + date.length() + num);
					}
					break;
				}
			}

			index = value.indexOf(date);
			if (index >= 0) {
				value = value.substring(index + date.length(), value.length());
				break;
			}
		}

		for (String date : DATESTATUSEND) {

			if (value.contains("上一篇") && value.contains("下一篇")) {
				int temUp = value.lastIndexOf("上一篇");
				int temDown = value.lastIndexOf("下一篇");
				if (temUp <= temDown) {
					index = temUp;
				} else {
					index = temDown;
				}
				value = value.substring(0, index);
				return value;
			}
			index = value.lastIndexOf(date);
			if (index >= 0) {
				value = value.substring(0, index);
				return value;
			}
		}

		return value;
	}

	public static String getDataAll_DongZhiXian(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select("div[id=wenshu]").first();

		if (null == divs1) {
			value = doc.text();
			if (value.contains("收藏本页")) {
				value = value.substring(value.lastIndexOf("收藏本页") + 4);
			}
			if (value.contains("来源： 中国法院网")) {
				value = value.substring(0, value.lastIndexOf("来源： 中国法院网"));
			}
		} else {
			value = divs1.text();
		}

		value = getReplaceAll(value);
		return value;
	}

	public static String getDataAll_DuJiQu(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select("div.news-con").first();

		value = divs1.text();
		value = getReplaceAll(value);
		return value;
	}

	public static String getDataAll_HUAIBEI(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select("div.content").first();

		value = divs1.text();
		if (value.contains("(责任编辑：淮北中院网)")) {
			value = value.substring(0, value.indexOf("(责任编辑：淮北中院网)"));
		}
		value = getReplaceAll(value);
		return value;
	}

	/**
	 * 鸠江区人民法院
	 * 
	 * @param doc
	 * @return
	 */
	public static String getDataAll_JiuJiangQu(Document doc) {
		String value = "";
		String temp = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		value = doc.body().text();

		if (null == value || "" == value) {
			return null;
		} else {
			value = getReplaceAll(value);
		}
		int index = 0;

		for (String date : DATESTATUS) {

			if ("点击".equals(date) || "点击数".equals(date) || "浏览".equals(date) || "浏览数".equals(date)
					|| "访问人数".equals(date)) {

				if (value.contains(date)) {
					int indexTemp = value.indexOf(date);
					temp = value.substring(indexTemp, indexTemp + 15);
					int num = includeNumericNum(temp);
					if (temp.contains("次")) {
						value = value.substring(indexTemp + date.length() + num + 1);
					} else {
						value = value.substring(indexTemp + date.length() + num);
					}
					break;
				}
			}

			index = value.indexOf(date);
			if (index >= 0) {
				value = value.substring(index + date.length(), value.length());
				break;
			}
		}
		for (String date : DATESTATUSEND) {

			if ("书记员".equals(date)) {

				if (value.contains(date)) {

					int indexTemp = value.lastIndexOf(date);
					temp = value.substring(indexTemp, indexTemp + 15);
					if (temp.contains("附")) {
						int te = temp.indexOf("附");
						value = value.substring(0, indexTemp + te);
					}
					break;
				}
			}

			index = value.lastIndexOf(date);
			if (index >= 0) {
				value = value.substring(0, index);
				return value;
			}
		}

		return value;
	}

	public static String getDataAll_ShouXian(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select("div#zoom").first();
		value = divs1.text();
		value = getReplaceAll(value);
		return value;
	}

	/**
	 * 由DIV的属性得到 DIV的内容
	 * 
	 * @param doc
	 * @param div
	 * @return
	 */
	public static String getDivContent(Document doc, String div) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select(div).first();
		value = divs1.text();
		value = getReplaceAll(value);
		return value;
	}

	/**
	 * 由DIV的属性 及开始，结束字符得到 最后的内容
	 * 
	 * @param doc
	 * @param div
	 * @param beginStr
	 * @param endStr
	 * @param flag
	 *            默认为FLASE 及开始字符 后截取
	 * @return
	 */
	public static String getContent(Document doc, String div, String beginStr, String endStr, boolean flag) {

		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (null != div && "" != div) {
			Element divs1 = doc.select(div).first();
			if (null != divs1) {
				value = divs1.text();
			}
		}

		if (value == null || "".equals(value)) {
			return null;
		}

		String valueT = getReplaceAll(value);
		// 判断是不是文书
		int flag1 = 0;
		for (String per : PERSONNEL) {
			if (valueT.contains(per)) {
				flag1 = 1;
				break;
			}
		}
		if (flag1 == 0) {
			return null;
		}

		if (!StringUtils.isNull(beginStr)) {
			int beginIndex = value.indexOf(beginStr);
			if (beginIndex != -1) {
				value = value.substring(beginIndex + beginStr.length());
				if (flag == true) {
					value = value.substring(beginIndex);
				}
			}
		}

		value = getReplaceAll(value);
		if (!StringUtils.isNull(endStr)) {
			int endIndex = value.lastIndexOf(endStr);
			if (endIndex != -1) {
				value = value.substring(0, endIndex);
			}

		}

		return value;
	}

	/**
	 * 
	 * @param doc
	 * @param div
	 * @param beginStr
	 * @param endStr
	 * @param flag
	 * @param subBeginStr
	 *            需截取的开始字符
	 * @param subEndStr
	 *            需截取的结束字符
	 * @return
	 */
	// public static String getContent(Document doc, String div, String
	// beginStr, String endStr, boolean flag,
	// String subBeginStr, String subEndStr, String subStr) {
	// String value = "";
	// if (doc == null || "".equals(doc)) {
	// return null;
	// }
	// // value = doc.text();
	// value = doc.body().text();
	// if (StringUtils.isNull(value)) {
	// return null;
	// }
	// if (null != div && "" != div) {
	// Element divs1 = doc.select(div).first();
	// if (null != divs1) {
	// value = divs1.text();
	// }
	// }
	//
	// String valueT = getReplaceAll(value);
	// // 判断是不是文书
	// int flag1 = 0;
	// for (String per : PERSONNEL) {
	// if (valueT.contains(per)) {
	// flag1 = 1;
	// break;
	// }
	// }
	// if (flag1 == 0) {
	// return null;
	// }
	//
	// if (!StringUtils.isNull(beginStr)) {
	// int beginIndex = value.indexOf(beginStr);
	// if (beginIndex != -1) {
	// value = value.substring(beginIndex + beginStr.length());
	// if (flag == true) {
	// value = value.substring(beginIndex);
	// }
	// }
	// }
	//
	// value = getReplaceAll(value);
	// if (!StringUtils.isNull(endStr)) {
	// int endIndex = value.lastIndexOf(endStr);
	// if (endIndex != -1) {
	// value = value.substring(0, endIndex);
	// }
	//
	// }
	//
	// if (value.length() > 100) {
	// if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {
	//
	// int index1 = value.indexOf(subBeginStr);
	// int index2 = value.indexOf(subEndStr);
	// if ((index2 - index1) > 0 && (index2 - index1) < 80) {
	// String value1 = value.substring(0, index1);
	// String value2 = value.substring(index2 + subEndStr.length());
	// value = value1 + value2;
	// value1 = null;
	// value2 = null;
	// }
	// }
	// }
	// // 截取掉不需求的字符
	// if (!StringUtils.isNull(subStr)) {
	// value = value.replaceAll(subStr, "");
	// }
	//
	// return value;
	// }
	/**
	 * 
	 * @param doc
	 * @param div
	 * @param beginStr
	 * @param endStr
	 * @param flag
	 * @param subBeginStr
	 * @param subEndStr
	 * @param zeStr
	 *            List<String> result=new ArrayList<String>();
	 * @return
	 */
	// public static String getContent(Document doc, String div, String
	// beginStr, String endStr, boolean flag,
	// String subBeginStr, String subEndStr,List<String> zeStr) {
	// String value = "";
	// if (doc == null || "".equals(doc)) {
	// return null;
	// }
	// // value = doc.text();
	// value = doc.body().text();
	// if (StringUtils.isNull(value)) {
	// return null;
	// }
	// if (null != div && "" != div) {
	// Element divs1 = doc.select(div).first();
	// if (null != divs1) {
	// value = divs1.text();
	// }
	// }
	// if (StringUtils.isNull(value)) {
	// return null;
	// }
	//
	// String valueT = getReplaceAll(value);
	// // 判断是不是文书
	// int flag1 = 0;
	// for (String per : PERSONNEL) {
	// if (valueT.contains(per)) {
	// flag1 = 1;
	// break;
	// }
	// }
	// if (flag1 == 0) {
	// return null;
	// }
	//
	// if (!StringUtils.isNull(beginStr)) {
	// int beginIndex = value.indexOf(beginStr);
	// if (beginIndex != -1) {
	// value = value.substring(beginIndex + beginStr.length());
	// if (flag == true) {
	// value = value.substring(beginIndex);
	// }
	// }
	// }
	//
	// value = getReplaceAll(value);
	// if (!StringUtils.isNull(endStr)) {
	// int endIndex = value.lastIndexOf(endStr);
	// if (endIndex != -1) {
	// value = value.substring(0, endIndex);
	// }
	//
	// }
	//
	// if (value.length() > 100) {
	// if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {
	//
	// int index1 = value.indexOf(subBeginStr);
	// int index2 = value.indexOf(subEndStr);
	// if ((index2 - index1) > 0 && (index2 - index1) < 80) {
	// String value1 = value.substring(0, index1);
	// String value2 = value.substring(index2 + subEndStr.length());
	// value = value1 + value2;
	// value1 = null;
	// value2 = null;
	// }
	// }
	// }
	//
	//
	// //正则
	// if(null!=zeStr){
	// if(zeStr.size()>0){
	// for(String s1:zeStr){
	// Pattern p = Pattern.compile(s1);
	// Matcher m = p.matcher(value);
	// String str=null;
	// if(m.find()){
	// str=m.group();
	// value=value.replaceAll(str, "");
	// }
	// }
	// }
	// }
	//
	// return value;
	// }

	/**
	 * 
	 * @param doc
	 * @param div
	 * @param beginStr
	 *            去掉前
	 * @param endStr
	 *            去掉后
	 * @param flag
	 * @param subBeginStr
	 *            需截取的开始字符
	 * @param subEndStr
	 *            需截取的结束字符
	 * @param subStr
	 *            去掉不需要字符
	 * @return
	 */
	public static String getContent(Document doc, String div, String beginStr, String endStr, boolean flag,
			String subBeginStr, String subEndStr, String subStr) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (null != div && "" != div) {
			Element divs1 = doc.select(div).first();
			if (null != divs1) {
				value = divs1.text();
			}
		}
		if (StringUtils.isNull(value)) {
			return null;
		}
		String valueT = getReplaceAll(value);
		// 判断是不是文书
		int flag1 = 0;
		for (String per : PERSONNEL) {
			if (valueT.contains(per)) {
				flag1 = 1;
				break;
			}
		}
		if (flag1 == 0) {
			return null;
		}

		if (!StringUtils.isNull(beginStr)) {
			int beginIndex = value.indexOf(beginStr);
			if (beginIndex != -1) {
				value = value.substring(beginIndex + beginStr.length());
				if (flag == true) {
					value = value.substring(beginIndex);
				}
			}
		}

		value = getReplaceAll(value);
		if (!StringUtils.isNull(endStr)) {
			int endIndex = value.lastIndexOf(endStr);
			if (endIndex != -1) {
				value = value.substring(0, endIndex);
			}

		}

		if (value.length() > 100) {
			if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {

				int index1 = value.indexOf(subBeginStr);
				int index2 = value.indexOf(subEndStr);
				if ((index2 - index1) > 0 && (index2 - index1) < 80) {
					String value1 = value.substring(0, index1);
					String value2 = value.substring(index2 + subEndStr.length());
					value = value1 + value2;
					value1 = null;
					value2 = null;
				}
			}
		}
		// 截取掉不需求的字符
		if (!StringUtils.isNull(subStr)) {
			value = value.replaceAll(subStr, "");
		}

		return value;
	}

	public static String getContent(Document doc, String[] div, String beginStr, String endStr, boolean flag,
			String subBeginStr, String subEndStr, String subStr) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (null != div) {
			if (div.length > 0) {
				for (int i = 0; i < div.length; i++) {
					String str = div[i];
					Element divs1 = doc.select(str).first();
					if (null != divs1) {
						value = divs1.text();
						if (!StringUtils.isNull(value)) {
							value = divs1.text();
							break;
						}
					}
				}
			}
		}

		String valueT = getReplaceAll(value);
		// 判断是不是文书
		int flag1 = 0;
		for (String per : PERSONNEL) {
			if (valueT.contains(per)) {
				flag1 = 1;
				break;
			}
		}
		if (flag1 == 0) {
			return null;
		}

		if (!StringUtils.isNull(beginStr)) {
			int beginIndex = value.indexOf(beginStr);
			if (beginIndex != -1) {
				value = value.substring(beginIndex + beginStr.length());
				if (flag == true) {
					value = value.substring(beginIndex);
				}
			}
		}

		value = getReplaceAll(value);
		if (!StringUtils.isNull(endStr)) {
			int endIndex = value.lastIndexOf(endStr);
			if (endIndex != -1) {
				value = value.substring(0, endIndex);
			}

		}

		if (value.length() > 100) {
			if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {

				int index1 = value.indexOf(subBeginStr);
				int index2 = value.indexOf(subEndStr);
				if (index1 != -1 && index2 != -1) {
					if ((index2 - index1) > 0 && (index2 - index1) < 80) {
						String value1 = value.substring(0, index1);
						String value2 = value.substring(index2 + subEndStr.length());
						value = value1 + value2;
						value1 = null;
						value2 = null;
					}
				}

			}
		}
		// 截取掉不需求的字符
		if (!StringUtils.isNull(subStr)) {
			value = value.replaceAll(subStr, "");
		}

		return value;
	}

	public static String getContent(Document doc, String div, String beginStr, String endStr, boolean flag,
			String subBeginStr, String subEndStr, String subStr, List<String> zeStr) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (null != div && "" != div) {
			Element divs1 = doc.select(div).first();
			if (null != divs1) {
				value = divs1.text();
			}
		}
		if (StringUtils.isNull(value)) {
			return null;
		}
		String valueT = getReplaceAll(value);
		// 判断是不是文书
		int flag1 = 0;
		for (String per : PERSONNEL) {
			if (valueT.contains(per)) {
				flag1 = 1;
				break;
			}
		}
		if (flag1 == 0) {
			return null;
		}

		if (!StringUtils.isNull(beginStr)) {
			int beginIndex = value.indexOf(beginStr);
			if (beginIndex != -1) {
				value = value.substring(beginIndex + beginStr.length());
				if (flag == true) {
					value = value.substring(beginIndex);
				}
			}
		}

		value = getReplaceAll(value);
		if (!StringUtils.isNull(endStr)) {
			int endIndex = value.lastIndexOf(endStr);
			if (endIndex != -1) {
				value = value.substring(0, endIndex);
			}

		}

		if (value.length() > 100) {
			if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {

				int index1 = value.indexOf(subBeginStr);
				int index2 = value.indexOf(subEndStr);
				if ((index2 - index1) > 0 && (index2 - index1) < 80) {
					String value1 = value.substring(0, index1);
					String value2 = value.substring(index2 + subEndStr.length());
					value = value1 + value2;
					value1 = null;
					value2 = null;
				}
			}
		}
		// 截取掉不需求的字符
		if (!StringUtils.isNull(subStr)) {
			value = value.replaceAll(subStr, "");
		}

		// 正则
		if (null != zeStr) {
			if (zeStr.size() > 0) {
				for (String s1 : zeStr) {
					Pattern p = Pattern.compile(s1);
					Matcher m = p.matcher(value);
					String str = null;
					if (m.find()) {
						str = m.group();
						value = value.replaceAll(str, "");
					}
				}
			}
		}

		return value;
	}

	/**
	 * 
	 * @param doc
	 * @param subBeginStr
	 *            需要截取字段开始
	 * @param subEndStr
	 *            需要截取字段结束
	 * @param flag
	 *            默认flag为false 从字段结束处开始计算
	 * @param subStr
	 *            需要截取的字段
	 * @return
	 */
	public static String getContent(String doc, String subBeginStr, String subEndStr, Boolean flag, String subStr) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		value = doc;
		if (value.length() > 100) {
			if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {

				int index1 = value.indexOf(subBeginStr);
				int index2 = value.indexOf(subEndStr);
				if ((index2 - index1) > 0 && (index2 - index1) < 80) {
					String value1 = value.substring(0, index1);

					String value2 = value.substring(index2 + subEndStr.length());
					if (flag == true) {
						value2 = value.substring(index2);
					}
					value = value1 + value2;
					value1 = null;
					value2 = null;
				}
			}
		}
		// 截取掉不需求的字符
		if (!StringUtils.isNull(subStr)) {
			value = value.replaceAll(subStr, "");
		}

		return value;
	}

	// 去掉特殊字符
	public static String getSpecialStringALL(String value) {
		if (null == value || "".equals(value)) {
			return null;
		}
		char[] chs = value.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char c : chs) {
			if (((int) c) != 12288 && ((int) c) != 160) {
				sb.append(String.valueOf(c));
			}
		}
		return sb.toString();
	}

	public static String cleanBegin(String value) {
		String[] str = value.split("");
		StringBuffer stt = new StringBuffer();
		for (String val : clean) {

			if (val != str[0]) {
				stt.append(str);
			}
		}
		String aa = stt.toString();
		return aa;
	}

	/**
	 * 往ES里面写数据
	 * 
	 * @param data
	 *            要写的字段
	 * @param id
	 *            ID
	 */
	public static void insertEsData(String data, String id) {

	}

	// 判断字符是否非数字
	public static boolean isDigit(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	// 去掉无用字符
	public static String getReplaceAll(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		StringBuffer sb = null;
		if (value != null && !"".equals(value)) {
			value = value.replaceAll(",", "，");
			value = value.replaceAll(" ,o,O", "〇");
			value = value.replaceAll("[×,X,Ｘ,x,╳,＊,\\*]", "某");

			value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”,:,<,/>,</,>,-,+,=,},{,#,\",',-,%,^,*,〗,〖,?,？]", ""); // a-z,A-Z,没有去掉字母

			// value =
			// value.replaceAll("[\n,\r,“,”,<,/>,</,>,-,+,=,},{,#,\",',%,^,*,〗,〖]",
			// ""); // a-z,A-Z,没有去掉字母，没去冒号，没去空格 ，及 -

			// value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,“,”
			// ,<,/>,</,>,-,+,=,},{,#,\",',-,%,^,*,〗,〖]", ""); //
			// a-z,A-Z,没有去掉字母，没去冒号
			// value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”
			// ,:,<,/>,</,>,-,+,=,},{,#,\",',-,%,^,*]", ""); // a-z,A-Z,没有去掉字母

			// value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”,・
			// ,:,<,/>,</,>,a-z,A-Z,-,+,=,},{,.,#,\",',-,%,^,*]",""); //去掉所有字母
			value = getSpecialStringALL(value);
			value = value.trim();
			sb = new StringBuffer();
			sb.append(value);
		}
		return sb == null ? "" : sb.toString();
	}

	// 提取全文
	public static String getDataAll(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		value = getReplaceAll(value);
		// value = cleanBegin(value);
		// logger.info("------------"+value);
		int index = 0;
		index = value.indexOf("。");
		if (index == -1) {
			return null;
		}
		String value2 = value.substring(0, index);
		for (String date : DATESTATUS) {
			index = value2.indexOf(date);
			if (index >= 0) {
				value = value.substring(index + date.length(), value.length());
			}
		}
		for (String date : DATESTATUSEND) {
			index = value2.lastIndexOf(date);
			if (index >= 0) {
				value = value.substring(0, index);
				return value;
			}
		}

		return value;
	}

	/**
	 * 删除字符串中的数字
	 * 
	 * @param str
	 * @return
	 */
	public static String removeNum(String str) {
		return str.replaceAll("\\d+", "");
	}

	/**
	 * 判断字符串中是否包含数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIncludeNumeric(String str) {
		if (null == str || "" == str) {
			return false;
		}
		for (int i = str.length(); --i >= 0;) {

			if (Character.isDigit(str.charAt(i))) {
				return true;
			}

		}
		return false;
	}

	/**
	 * 得到字符串中数字的个数
	 * 
	 * @param str
	 * @return
	 */
	public static int includeNumericNum(String str) {
		int fal = 0;
		if (null == str || "" == str) {
			return fal;
		}
		for (int i = str.length(); --i >= 0;) {
			if (Character.isDigit(str.charAt(i))) {
				fal++;
			}

		}
		return fal;
	}

	static long COUNT = 0;// 正确数量
	static long SUM = 0; // 所有数量
	static long ERRORSUM = 0;// 出错数量
	static long NOSUM = 0;// 无数据
	static long NOSTANDARD;// 非标准数据，如不是html数据
	static long NOSTANDARDSWF;// SWF数据
	static long NOSTANDARDDOC;// DOC数据
	static Map<String, String> MAPS = new HashMap<String, String>();

	// 劳动争议一案; 纠纷 广水市××山××大道××号; 纷一案中; 到庭 参加 本院 受理 后 认为 吴xx已交纳 XXXXX
	// ----------------自然人、企业名、当事人分类名里不能出现这样的 ----,"本院","本案"
	// --委托代理人蔡红，湖北陵燕律师事务所律师。代理权限：参加诉讼、调解。 "参加",
	// 委托代理人：袁沛良，东莞市启明律师事务所律师（于2015年7月10日被撤回授权）。 "撤回",
	// 诉讼代理人：贺国兵，湖北××律师事务所律师。代理权限为：代为进行答辩，调查，参加开庭审理、陈述事实、质证活动，发表代理意见，代收各种诉讼文书。
	// "答辩", "到庭", "参加", "解除",
	// 被告：中国人寿财产保险给付有限公司芜湖市中心支公司，住所地安徽省芜湖市镜湖区。 "给付",
	// 原审被告李庆华，女，1970年9月22日出生，汉族，创业集团精细化工有偿解除劳动合同工人。 解除
	// 委托代理人任世昭、戴武，该公司法务部顾问。代理权限：代为承认、放弃、变更诉讼请求，进行和解等。 "请求",
	// 被告熊斌，因涉嫌刑事犯罪，现羁押于宜都市看守所。 涉嫌 "涉嫌",
	// 原审被告范有信（已死亡），男，1954年5月5日出生，汉族，无职业。 "死亡",
	// 委托代理人董仕平，湖北维天律师事务所律师。代理权限：代为承认、放弃、变更诉求、和解、代签法律文书。 诉求 "诉求",
	// 委托代理人王剑峰，该行法律顾问室工作人员。代理权限为特别授权，即代为提出诉前保全申请、代为承认、放弃、变更诉讼请求，进行和解等。 提出 提起
	// "提出", "提起",
	// 委托代理人杨和存（代理权限：代为收集证据，参加诉讼，辩论，代收法律文书），随县新街法律服务所法律工作者。 "证据", "证明",
	// 业主张长江，男，1986年1月15日出生，汉族，现住址同上。 "主张",
	// 原告重庆市道路交通事故社会救助基金管理中心，住所地重庆市渝北区青枫北路12号，组织机构代码55409394-Ｘ。 "事故",
	// 委托代理人黎玉石。代理权限为陈述事实、参加辩论等一般授权。 事实
	// 委托代理人刘晓莲，广东维强律师事务所律师。代理权限：特别授权（调查取证、协商、谈判；提起诉讼，办理立案相关事宜；提起反诉、提出答辩，参与庭审活动；提出撤诉、变更、放弃或承认诉讼请求；进行和解、调解；提起上诉等）。
	// "办理",

	public static String[] CLIENT_END = { "一案", "纠纷", "争议", "认定", "确认", "认为", "辩称", "承担", "负担", "签订", "无异议", "涉及", "非法",
			"诉称", "改判", "作为", "同居", "同居生活", "副本", "事实", "享受", "相互认识", "婚姻关系", "判处", "驳回", "赔偿", "通告", "违法", "劫罪", "判决",
			"行驶", "危险驾驶", "诈骗", "盗窃", "强奸", "聚众斗殴", "寻衅滋事", "贩卖毒品", "运输毒品", "故意伤害", "涉嫌诽谤", "抢劫", "绑架", "勒索", "杀人",
			"纠纷", "非法拘禁", "运输毒品", "破坏", "违法", "非法", "犯罪", "未履行生效法律文书", "未履行法律文书", "申请强制执行", "受理", "生效" };

	static {
		MAPS.put("html", "html");
		MAPS.put("htm", "htm");
		// MAPS.put("txt", "txt");
		MAPS.put("swf", "swf");
		MAPS.put("doc", "doc");
	}

	public static String[] clean = { "0-9", "a-z", "A-Z" };
	public static String[] charset = { "utf-8", "gbk", "gb2312", "gb18030", "big5" };
	public static String[] ERCOEDING = { "й", "෨", "Ժ", "ۼ", "ҩ", "ල", "ɷ", "ص", "δ", "ġ", "Ϊ", "ط", "Ϣ", "ȡ", "Ӫ", "ã",
			"", "�" };
	public static String[] DATESTATUS = { "点击数", "点击率", "浏览次数", "浏览", "访问人数", "[大|中|小]", "大中小", "大小",
			"裁判文书公开是依据国家有关法律及最高人民法院等有关规定，相关事宜请与各审判法院联系。", "【关闭窗口】", "【关闭窗口】", "提交时间", "提交日期", "发布时间", "发布日期", "关注时间",
			"编辑时间", "编辑日期", "发表时间", "录入时间", "更新时间", "作者点击", "点击时间", "点击", "字体", "∣法律社区", "发表于", "阅读", "点击", "日期", "作者",
			"时间", "今天是" };

	// public static String[] DATESTATUS = {"", "提交时间", "提交日期", "发布时间", "发布日期",
	// "关注时间", "编辑时间", "编辑日期", "发表时间", "录入时间",
	// "更新时间", "作者点击", "点击时间", "【关闭窗口】", "大中小", "大小", "字体", "点击率", "浏览", "点击数",
	// "∣法律社区", "发表于", "阅读", "点击", "日期",
	// "作者", "时间", "今天是" };

	public static String[] DATESTATUSEND = { "上一篇", "公告", "下一篇", "作者|信息来源", "[打印本页]", "打印本页|", "打印本页 ", "关闭窗口", "相关链接",
			"法院门户", "主办单位" };
	// 文档结束处人员职称
	public static String[] PERSONNEL = { "执行员", "审判长", "审判员", "书记员", "提交日期", "原告", "被告", "申诉人", "委托代理人", "被申诉人",
			"法定代表人", "通 知 书", "驳回申诉通知书", "特此通知" };

	private static Logger logger = LogManager.getLogger(ExtractionHtmlGetCaseCause2.class.getName());

	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=masspickdata;"
			+ "AccountKey=qi8o0gS/Jl2/se9uB3LJFi6QMc562IZuHf4oIwfVkGBDKychIwk39Mes9pNu5ni0gBOoru+DF7SN/RZ0V6oEyQ==;"
			+ "EndpointSuffix=core.chinacloudapi.cn";

	static CloudStorageAccount storageAccount = null;
	static CloudBlobClient blobClient = null;
	static CloudBlobContainer container = null;

	static {

		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		blobClient = storageAccount.createCloudBlobClient();
		try {
			container = blobClient.getContainerReference("courtnew");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}
}
