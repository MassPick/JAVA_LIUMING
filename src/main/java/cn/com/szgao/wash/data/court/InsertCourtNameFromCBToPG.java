package cn.com.szgao.wash.data.court;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import cn.com.szgao.dto.FfWholeCourtVO;
import cn.com.szgao.dto.RowsWholeCourtVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.PostgresqlUtils;
import cn.com.szgao.util.StringUtils;

public class InsertCourtNameFromCBToPG {

	private static Logger log = LogManager.getLogger(InsertCourtNameFromCBToPG.class);
	static int i = 0;
	static int count = 0;

	public static void main(String[] args) {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));

		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();

			doListData3();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
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

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static Connection conn = null;

	static {
		try {
			conn = PostgresqlUtils.getConnection();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	static Connection connU = null;

	static {
		try {
			connU = PostgresqlUtils.getConnection();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	static Connection connI = null;

	static {
		try {
			connI = PostgresqlUtils.getConnection();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();

	static {
		util.initData();
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
		String sqlUpdate = null;
		String sqlInsert = null;

		PreparedStatement stmt = null;
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;


		while (true) {
			if (null == key) {

				while (true) {

					try {
						result = template.getForObject(
								"http://192.168.1.30:8092/courtName/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&skip=0",
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

			} else {
				while (true) {
					try {
						urlVariables.put("startkeys", "\"" + key + "\"");
						result = template.getForObject(
								"http://192.168.1.30:8092/courtName/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=1",
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
				key = r.getKey();
				count++;
				i++;
				System.out.println(i + ":" + key);
				WholeCourtVO whVo = r.getValue();// 00006a27-0c95-48a3-8249-645457d71b65
				if (whVo == null) {
					continue;
				}

				if (whVo.getCourtName() == null) {
					continue;
				}

				String sqlS = "SELECT count(1) from court_name_t WHERE 1=1 and court_name2 ='" + whVo.getCourtName()
						+ "'  ";

				String te = PostgresqlUtils.getValueFromSqlCon(conn, sqlS);
				int temp1 = Integer.valueOf(te);

				if (temp1 == 0) {

					String[] array = util.utils(whVo.getCourtName());
					sqlInsert = "INSERT INTO   court_name_t  (province,city,area,court_name,court_name2,modify_by)VALUES(?,?,?,?,?,?)   ";
					try {
						// conn.setAutoCommit(false);
						stmtInsert = connI.prepareStatement(sqlInsert);
						stmtInsert.setString(1, array[0]);
						stmtInsert.setString(2, array[1]);
						stmtInsert.setString(3, array[2]);
						stmtInsert.setString(4, whVo.getCourtName());
						stmtInsert.setString(5, whVo.getCourtName());
						stmtInsert.setString(6, "lm20160510_马星_失信执行法院名");
						stmtInsert.executeUpdate();

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					 
				}

			}

			if (i >= jsn.getTotal_rows()) {
				break;
			}
		}
	}

}
