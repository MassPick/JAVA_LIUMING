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
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.gson.Gson;

import cn.com.szgao.util.DateUtils;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.wash.data.AdministrationUtils;

/**
 * 得到案由与条文的关系 及计数          
 * 
 * 条文下的案由数排序
 * 
 * 案由出现次数排序
 * 
 * 条文出现次数排序
 * 
 * @author liuming
 * @Date 2016年11月1日 上午10:40:20
 */
public class GetCaseCauseRelationLawsCount {
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(GetCaseCauseRelationLawsCount.class.getName());
	private static Logger log = LogManager.getLogger(GetCaseCauseRelationLawsCount.class.getName());
	static Map<String, List<RecordData>> MAPS = new HashMap<String, List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //
	static long REPEATSUM = 0; // 去重后数据条数
	static long SUM = 0;
	static long count = 0;

	static AdministrationUtils util;

 
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));

		// 导入文件地址

//		File file = new File("D:\\lm\\log\\1_20161025072109新版裁判文书网展示文书(以裁判日期查询)_2014年_2.json");
		File file = new File("D:\\lm\\log\\1_20161025072109新版裁判文书网展示文书(以裁判日期查询)_2014年_2_2.json");
		

//		util = new AdministrationUtils();
//		util.initData(); // 查询行政区

		try {
			show(file );

			printMap(map1,map1_law_casecause,map_caseCouse_num,map_laws_num);
				

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			file = null;
		}
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		log.info("开始时间--------------------" + formatter.format(startTime));

		log.info("结束时间--------------------" + formatter.format(endDate));

		log.info("JSON没有的案由数:  " + caseNum + "html解析出的: " + caseNum_Y1 + "html没解析出的: " + caseNum_N1);
		log.info("JSON有的案由数匹配数据的:  " + caseNum_Y + "JSON有的案由数没匹配数据的: " + caseNum_N);

		log.info("Time : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
		log.info("Time : " + (float) ((float) ((endTime - startTime) / 1000) / 60) + "分钟");
		log.info("Time : " + (float) ((endTime - startTime) / 1000) + "秒");
		log.info("Total : " + countNum);
		log.info("Speed : " + (float) (countNum / (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
				+ "个/小时");
		log.info("Speed : " + (float) (countNum / (float) ((float) ((endTime - startTime) / 1000) / 60)) + "个/分种");
		log.info("Speed : " + (float) (countNum / (float) ((endTime - startTime) / 1000)) + "个/秒");

		log.info("找不到Html的数量  : " + countNotHtml);

		record();
		System.exit(0);
	}


	/**
	 * 递归遍历文件
	 * 
	 * @param file
	 * @throws @throws
	 *             Exception
	 */
	private static void show(File file ) throws Exception {
		if (file.isFile()) {
			long da = System.currentTimeMillis();
			create(file );
			logger.info("读取<<" + file.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
			return;
		}
		File[] files = file.listFiles();
		System.out.println("----files---" + files);
		for (File fi : files) {
			if (fi.isFile()) {
				if (fi.getName().contains("download_fail")) {
					continue;
				}
				long da = System.currentTimeMillis();
				String name = fi.getParentFile().getPath();
				name = name.substring(name.lastIndexOf("\\") + 1, name.length());
				create(fi );
				logger.info("读取" + name + "<<" + fi.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
			} else if (fi.isDirectory()) {
				show(fi );
			} else {
				continue;
			}
		}
	}

	static int countNum = 0;
	static int countNotHtml = 0;

	/**
	 * 生成文件的个数
	 */
	static int countP = 0;

	static BufferedWriter fwUn = null;
	static BufferedWriter fwUn2 = null;
	static BufferedWriter fwUn_map_caseCouse_num = null;
	static BufferedWriter fwUn_map_laws_num = null;

	/**
	 * 统计某案由内条文件的数量
	 */
	public static Map<String, HashMap<String, Long>> map1 = new HashMap<String, HashMap<String, Long>>();

	/**
	 * 统计某条文下的案由数
	 */
	public static Map<String, HashMap<String, Long>> map1_law_casecause = new HashMap<String, HashMap<String, Long>>();

	/**
	 * 统计案由数据
	 */
	public static HashMap<String, Long> map_caseCouse_num = new HashMap<String, Long>();
	/**
	 * 统计条文数量
	 */
	public static HashMap<String, Long> map_laws_num = new HashMap<String, Long>();
	

	/**
	 * 
	 * @param mapt 案由下的条文
	 * @param mapt_law_casecause 条文下的案由
	 * @param mapt_casecause_num 案由计数
	 * @param mapt_laws_num  条文记数
	 * @return void
	 * @author liuming
	 * @date 2016年11月2日 下午4:00:07
	 */
	public static void printMap(Map<String, HashMap<String, Long>> mapt,
			Map<String, HashMap<String, Long>> mapt_law_casecause, HashMap<String, Long> mapt_casecause_num,
			HashMap<String, Long> mapt_laws_num) {

		String folderPathUn = "D:/lm/log/案由与条文对应";
		String filePathUn = "D:\\lm\\log\\案由与条文对应\\" + "案由下的条文_" + DateUtils.getDateyyyyMMddhhmmss() + ".json";
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
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				fileSUn.delete();
				fileSUn = new File(filePathUn);
				try {
					fwUn = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// String folderPathUn = "D:/lm/log/案由与条文对应";
		String filePathUn_law_casecause = "D:\\lm\\log\\案由与条文对应\\" + "条文下的案由_" + DateUtils.getDateyyyyMMddhhmmss()
				+ ".json";
		// 创建文件夹
		FileUtils.newFolder(folderPathUn);
		File fileSUn_law_casecause = new File(filePathUn_law_casecause);
		// String encoding_from1U = "UTF-8";

		try {
			if (!fileSUn_law_casecause.exists()) {
				try {
					fileSUn_law_casecause.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
				try {
					fwUn2 = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn_law_casecause, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				fileSUn_law_casecause.delete();
				fileSUn_law_casecause = new File(filePathUn);
				try {
					fwUn2 = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn_law_casecause, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// String folderPathUn = "D:/lm/log/案由与条文对应";
		String filePathUn_caseCouse_num = "D:\\lm\\log\\案由与条文对应\\" + "案由计数_" + DateUtils.getDateyyyyMMddhhmmss()
				+ ".json";
		// 创建文件夹
		FileUtils.newFolder(folderPathUn);
		File fileSUn_caseCouse_num = new File(filePathUn_caseCouse_num);
		// String encoding_from1U = "UTF-8";

		try {
			if (!fileSUn_caseCouse_num.exists()) {
				try {
					fileSUn_caseCouse_num.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
				try {
					fwUn_map_caseCouse_num = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn_caseCouse_num, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				fileSUn_caseCouse_num.delete();
				fileSUn_caseCouse_num = new File(filePathUn);
				try {
					fwUn_map_caseCouse_num = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn_caseCouse_num, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// String folderPathUn = "D:/lm/log/案由与条文对应";
		String filePathUn_laws_num = "D:\\lm\\log\\案由与条文对应\\" + "条文计数_" + DateUtils.getDateyyyyMMddhhmmss() + ".json";
		// 创建文件夹
		FileUtils.newFolder(folderPathUn);
		File fileSUn_laws_num = new File(filePathUn_laws_num);
		// String encoding_from1U = "UTF-8";

		try {
			if (!fileSUn_laws_num.exists()) {
				try {
					fileSUn_laws_num.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
				try {
					fwUn_map_laws_num = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn_laws_num, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				fileSUn_laws_num.delete();
				fileSUn_laws_num = new File(filePathUn);
				try {
					fwUn_map_laws_num = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn_laws_num, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		int aa = 0;

		if (null != mapt && mapt.size() > 0) {
			for (Map.Entry<String, HashMap<String, Long>> entry : mapt.entrySet()) {
				System.out.println("案由下打条文  " + aa++);
				String laws = "";
				HashMap<String, Long> hm = entry.getValue();

				ValueComparator bvc = new ValueComparator(hm);
				TreeMap<String, Long> sorted_map = new TreeMap<String, Long>(bvc);
				System.out.println("unsorted map: " + hm);

				sorted_map.putAll(hm);

				// 排序后
				for (Map.Entry<String, Long> entry_t : sorted_map.entrySet()) {
					laws += entry_t.getKey() + "#" + entry_t.getValue() + ";";
				}
				System.out.println(entry.getKey() + "|" + laws);
				writerString(fwUn, entry.getKey() + "|" + laws);
			}
		}
		aa = 0;

		if (null != mapt_law_casecause && mapt_law_casecause.size() > 0) {
			for (Map.Entry<String, HashMap<String, Long>> entry : mapt_law_casecause.entrySet()) {
				System.out.println("条文下的案由  " + aa++);
				String laws = "";
				HashMap<String, Long> hm = entry.getValue();

				ValueComparator bvc = new ValueComparator(hm);
				TreeMap<String, Long> sorted_map = new TreeMap<String, Long>(bvc);
				System.out.println("unsorted map: " + hm);

				sorted_map.putAll(hm);

				// 排序后
				for (Map.Entry<String, Long> entry_t : sorted_map.entrySet()) {
					laws += entry_t.getKey() + "#" + entry_t.getValue() + ";";
				}
				System.out.println(entry.getKey() + "|" + laws);
				writerString(fwUn2, entry.getKey() + "|" + laws);
			}
		}

		aa = 0;
		if (null != mapt_casecause_num && mapt_casecause_num.size() > 0) {

			System.out.println("案由计数  " + aa++);

			ValueComparator bvc = new ValueComparator(mapt_casecause_num);
			TreeMap<String, Long> sorted_map = new TreeMap<String, Long>(bvc);

			sorted_map.putAll(mapt_casecause_num);

			// 排序后
			for (Map.Entry<String, Long> entry_t : sorted_map.entrySet()) {
				writerString(fwUn_map_caseCouse_num, entry_t.getKey() + "|" + entry_t.getValue());
			}
		}

		aa = 0;
		if (null != mapt_laws_num && mapt_laws_num.size() > 0) {
			System.out.println("条文计数  " + aa++);

			ValueComparator bvc = new ValueComparator(mapt_laws_num);
			TreeMap<String, Long> sorted_map = new TreeMap<String, Long>(bvc);

			sorted_map.putAll(mapt_laws_num);

			// 排序后
			for (Map.Entry<String, Long> entry_t : sorted_map.entrySet()) {
				writerString(fwUn_map_laws_num, entry_t.getKey() + "|" + entry_t.getValue());
			}
		}

		aa = 0;

	}

	/**
	 * 写数据
	 * 
	 * @param <JSONObject>
	 * @param file
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	private static <ObjectDataVO, JSONObject> void create(File file )
			throws Exception, UnsupportedEncodingException {

		String name = file.getParentFile().getPath();
		name = name.substring(name.lastIndexOf("\\") + 1, name.length());
		BufferedReader reader = null;
		Gson gson = new Gson();
		String temp = null;

		WholeCourtVO archJson = null;


		String caseCause = null;


		HashMap<String, Long> map_t = null;
		HashMap<String, Long> map_t_law_casecause = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((temp = reader.readLine()) != null) {

				try {
					countNum++;
					System.out.println(countNum);

					archJson = gson.fromJson(temp, WholeCourtVO.class);
					// archJson.setWholeCourtId(StringUtils.getUUID(archJson.getDetailLink().toString()));
					caseCause = archJson.getCaseCause();
					if (StringUtils.isNull(archJson.getApprovalDateY()) || StringUtils.isNull(archJson.getLaws())
							|| StringUtils.isNull(caseCause)) {
						continue;
					} else {
						if ("2014".equals(archJson.getApprovalDateY()) || "2015".equals(archJson.getApprovalDateY())) {

							// 案由计数
							if (map_caseCouse_num.get(caseCause) == null) {
								map_caseCouse_num.put(caseCause, 1L);
							} else {
								map_caseCouse_num.put(caseCause, map_caseCouse_num.get(caseCause) + 1L);
							}

							// 条文计数
							String ss11[] = archJson.getLaws().split("</br>");
							for (int i = 0; i < ss11.length; i++) {

								String temp_key = ss11[i];
								if (StringUtils.isNull(temp_key)) {
									continue;
								}
								if (map_laws_num.get(temp_key) == null) {
									map_laws_num.put(temp_key, 1L);
								} else {
									map_laws_num.put(temp_key, map_laws_num.get(temp_key) + 1L);
								}
							}
							
							

							// 某条文下的案由数
							String ss22[] = archJson.getLaws().split("</br>");
							for (int i = 0; i < ss22.length; i++) {

								String temp_key = ss22[i];
								if (StringUtils.isNull(temp_key)) {
									continue;
								}

								if (map1_law_casecause.get(temp_key) == null) {
									map_t_law_casecause = new HashMap<String, Long>();
									map_t_law_casecause.put(caseCause, 1L);

								} else {
									map_t_law_casecause=map1_law_casecause.get(temp_key);
									map_t_law_casecause.put(caseCause, (map_t_law_casecause.get(caseCause)!=null?map_t_law_casecause.get(caseCause):0L )    + 1L);
								}
								

								if (map_t_law_casecause.size() > 0) {
									map1_law_casecause.put(temp_key, map_t_law_casecause);
								}

							}

							// 案由下的条文计数
							if (map1.get(caseCause) == null) {

								map_t = new HashMap<String, Long>();

								String ss1[] = archJson.getLaws().split("</br>");
								for (int i = 0; i < ss1.length; i++) {
									System.out.println(ss1[i]);
									map_t.put(ss1[i], Long.valueOf(1));
								}

							} else {
								map_t = map1.get(caseCause);

								String ss1[] = archJson.getLaws().split("</br>");
								for (int i = 0; i < ss1.length; i++) {
									int ff = 0;
									if (StringUtils.isNull(ss1[i])) {
										continue;
									}
									for (Map.Entry<String, Long> entry : map_t.entrySet()) {
										String key = entry.getKey();
										Long value = entry.getValue();

										if (ss1[i].equals(key)) {
											map_t.put(ss1[i], value + 1);
											ff = 1;
										}
									}
									if (ff == 0) {
										map_t.put(ss1[i], 1L);
									}
								}
							}

							if (map_t.size() > 0) {
								map1.put(caseCause, map_t);
							}

						}
					}

				} catch (Exception e) {

					continue;
				}

			}

		} catch (Exception e) {
		} finally {
			caseCause = null;
			map_t = null;
		}
	}

	static int caseNum = 0;
	static int caseNum_Y = 0;
	static int caseNum_N = 0;
	static int caseNum_Y1 = 0;
	static int caseNum_N1 = 0;

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


	public static void writerString(BufferedWriter fw, String str) {
		try {
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

	public static void writerString2(String str) {
		try {
			BufferedWriter fwUn = null;
			if (countNum == 0 || countNum == 1 || countNum % 10000 == 0) {
				countP++;
				String folderPathUn = "D:/lm/log/2016法院清洗后数据3";
				String filePathUn = "D:\\lm\\log\\2016法院清洗后数据3\\" + (countP) + ".json";
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

			fwUn.append(str + System.getProperty("line.separator"));
			// fw.newLine();
			fwUn.flush(); // 全部写入缓存中的内容
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

class ValueComparator implements Comparator<String> {

	Map<String, Long> base;

	public ValueComparator(Map<String, Long> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}
