package cn.com.szgao.wash.data.build;

import java.io.BufferedWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.web.client.RestTemplate;

import com.couchbase.client.java.Bucket;
import com.google.gson.Gson;

import cn.com.szgao.dto.BuildQulificationVO;
import cn.com.szgao.dto.BuildVO;
import cn.com.szgao.dto.BusinessDirectoryVO;
import cn.com.szgao.dto.Doc;
import cn.com.szgao.dto.FfBuildQulificationVO;
import cn.com.szgao.dto.FfBuildVO;
import cn.com.szgao.dto.RowsBuildQulificationVO;
import cn.com.szgao.dto.RowsBuildVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.PostgresqlUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;

/**
 * 将CB中的建筑资质写入PG 
 * @author liuming
 * @Date 2016年9月18日 上午11:09:42
 */
@SuppressWarnings("unused")
public class InsertBuildQulificationFromCBToPG_N {

	public static void main(String[] args) throws UnsupportedEncodingException {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));

		long startTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		Date date = new Date(startTime);
		log.info("开始时间--------------------" + formatter.format(date));
		 
		log.debug(
				"-------------------------------------------------- 开始debug!!InsertEtpEventItemFromCBToES!!!!!!!!!!!!!!!!!");
		log.info(
				"-------------------------------------------------- 开始info!!!!InsertEtpEventItemFromCBToES!!!!!!!!!!!!!!!");

		doListData3();

		log.debug(
				"-------------------------------------------------- 结束debug!!InsertEtpEventItemFromCBToES!!!!!!!!!!!!!!!!!");
		log.info(
				"-------------------------------------------------- 结束info!!!!InsertEtpEventItemFromCBToES!!!!!!!!!!!!!!!");

		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		log.info("结束时间--------------------" + formatter.format(endDate));
		log.info("Time : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
		log.info("Time : " + (float) (  (float)((endTime - startTime) / 1000) / 60) + "分钟");
		log.info("Time : " + (float) ((endTime - startTime) / 1000) + "秒");
		
		log.info("Total : " + i);

		log.info("Speed : " + (float) (i / (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) ) + "个/小时");
		log.info("Speed : " + (float) (i /(float) (  (float)( (endTime - startTime) / 1000) / 60)  ) + "个/分种");
		log.info("Speed : " + (float) (i / (float) ((endTime - startTime) / 1000) )
				+ "个/秒");
	}

	static int i = 0;
	private static Logger log = LogManager.getLogger(InsertBuildQulificationFromCBToPG_N.class);
	static Bucket bucket = null;

	static Connection conn = null;
	static PreparedStatement stmtInsert = null;
	// 19个
	static String sqlInsert = "INSERT INTO build_qulification_t_n(build_base_id,company_name,qualification_item ,certificateno,expire_date,certificate_date,collect_time,source )"
			+ "VALUES(?::uuid,    ?,?,?,?,?,?,? )";

	// static String sqlInsert = "INSERT INTO
	// build_base_t(build_base_id,'honorLink','manageLink','companyName','organizationCode',safetyPermits',detailLink','contactPerson','businessLicenseNo','contactNumber','companyBrief','industrial','commercialRegistered','contactFax','source','provice','city','detailAdress','collectTime
	// )"
	// + "VALUES(?::uuid, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	static {
		try {
			conn = PostgresqlUtils.getConnection();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			conn.setAutoCommit(false);

			stmtInsert = conn.prepareStatement(sqlInsert);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * 单条增加
	 * 
	 * @param id
	 * @param vo
	 */
	public static void indertPG(String id, BuildVO vo) {
		try {
			stmtInsert.setString(1, id);
			stmtInsert.setString(2, vo.getHonorLink());
			stmtInsert.setString(3, vo.getManageLink());
			stmtInsert.setString(4, vo.getCompanyName());
			stmtInsert.setString(5, vo.getOrganizationCode());
			stmtInsert.setString(6, vo.getSafetyPermits());
			stmtInsert.setString(7, vo.getDetailLink());
			stmtInsert.setString(8, vo.getContactPerson());
			stmtInsert.setString(9, vo.getBusinessLicenseNo());
			stmtInsert.setString(10, vo.getContactNumber());
			stmtInsert.setString(11, vo.getCompanyBrief());
			stmtInsert.setString(12, vo.getIndustrial());
			stmtInsert.setString(13, vo.getCommercialRegistered());
			stmtInsert.setString(14, vo.getContactFax());
			stmtInsert.setInt(15, vo.getSource() != null ? vo.getSource() : 0);
			stmtInsert.setString(16, vo.getProvice());
			stmtInsert.setString(17, vo.getCity());
			stmtInsert.setString(18, vo.getDetailAdress());
			if (null != vo.getCollectTime()) {
				stmtInsert.setTimestamp(19, new Timestamp(DateUtils.strToDateFromZZ(vo.getCollectTime()).getTime()));
			} else {
				stmtInsert.setTimestamp(19, null);
			}

			stmtInsert.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 多条增加
	 * 
	 * @param id
	 * @param vo
	 */
	public static void indertPGList(List<BuildQulificationVO> list) {

		for (BuildQulificationVO vo : list) {
			try {
				stmtInsert.setString(1, vo.getBuildId());
				stmtInsert.setString(2, vo.getCompanyName());
				stmtInsert.setString(3, vo.getQualification_item());
				stmtInsert.setString(4, vo.getCertificateNo());
				if (null != vo.getExpireDate()) {
					stmtInsert.setTimestamp(5,
							new Timestamp(DateUtils.strToDateFromZZ(vo.getExpireDate()).getTime()));
				} else {
					stmtInsert.setTimestamp(5, null);
				}
				if (null != vo.getCertificateDate() ) {
					stmtInsert.setTimestamp(6,
							new Timestamp(DateUtils.strToDateFromZZ(vo.getCertificateDate()).getTime()));
				} else {
					stmtInsert.setTimestamp(6, null);
				}
				
				if (null != vo.getCollectTime()) {
					stmtInsert.setTimestamp(7,
							new Timestamp(DateUtils.strToDateFromZZ(vo.getCollectTime()).getTime()));
				} else {
					stmtInsert.setTimestamp(7, null);
				}
				
				stmtInsert.setInt(8, vo.getSource());
				stmtInsert.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			stmtInsert.executeBatch();
			conn.commit();
			stmtInsert.clearBatch();
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

		int size = 50;// 每次提交的大小
		List<BuildQulificationVO> list = new ArrayList<BuildQulificationVO>();

		while (true) {
			if (null == key) {

				while (true) {

					// 237208:01d0549b-8246-5b5c-9c07-bffe1cdcd2cd
					// 10185100:4dc8657f-d2b5-596e-b50c-b566a113b1f9
					try {
						result = template.getForObject(
//								"http://192.168.1.13:8092/build/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&skip=0",
								"http://192.168.1.13:8092/buildQulification/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&skip=0",
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
//								"http://192.168.1.13:8092/build/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=1",
								"http://192.168.1.13:8092/buildQulification/_design/dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip=1",
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

			FfBuildQulificationVO jsn = gson.fromJson(value, FfBuildQulificationVO.class);

			for (RowsBuildQulificationVO r : jsn.getRows()) {
				key = r.getKey();
				i++;
				System.out.println(i + ":" + key);
				BuildQulificationVO whVo = r.getValue();// 00006a27-0c95-48a3-8249-645457d71b65
				if (whVo == null) {
					continue;
				}
				String company = whVo.getCompanyName();
				if (StringUtils.isNull(company)) {
					continue;
				}
				
//				List<BuildQulificationVO> listQu=whVo.getQualificationGrade();
//				if(listQu.size()>0){
//					for (BuildQulificationVO buildQulificationVO : listQu) {
//						if(!StringUtils.isNull(buildQulificationVO.getQualification_item())){
//							buildQulificationVO.setBuildId(key);
//							buildQulificationVO.setCompanyName(company);
//							list.add(buildQulificationVO);
//						}
//					}
//				}
				list.add(whVo);
				
				if (list.size() >= size) {
					indertPGList(list);
					list = new ArrayList<BuildQulificationVO>();
				}
				// indertPG(key, whVo);
			}

			if (i >= jsn.getTotal_rows()) {
				break;
			}

		}
		if (list.size() > 0) {
			indertPGList(list);
		}

	}

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

}
