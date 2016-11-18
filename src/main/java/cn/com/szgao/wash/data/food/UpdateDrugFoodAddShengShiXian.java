package cn.com.szgao.wash.data.food;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import cn.com.szgao.dto.CompanyNameVO;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.PostgresqlUtils;
import cn.com.szgao.util.PrCiCouText;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.DataUtils;
import net.sf.json.JSONObject;
import rx.Observable;
import rx.functions.Func1;

/**
 * 添加省市县
 * 
 * @author liuming
 * @ClassName UpdateDrugFoodAddShengShiXian
 * @date 2016年5月16日 上午9:44:07
 */
public class UpdateDrugFoodAddShengShiXian {
	private static Logger logger = LogManager.getLogger(UpdateDrugFoodAddShengShiXian.class.getName());
	static long COUNT = 0;// 正确数量
	static long SUM = 0; // 所有数量
	static long ERRORSUM = 0;// 出错数量
	static long NOSUM = 0;// 无数据
	static long NOSTANDARD;// 非标准数据，如不是html数据
	static long NOSTANDARDSWF;// SWF数据
	static long NOSTANDARDDOC;// DOC数据
	static long existNum;// 已存在ID数
	static long inNum;// 入库数

	static Connection conn = null;
	static PreparedStatement stmt = null;

	static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	static String usr = "postgres";
	static String psd = "615601.xcy*";

	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			PrCiCouText.connection = DriverManager.getConnection(url, usr, psd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DataUtils.initData();
		util.initData();
	}

	static Connection connU = null;

	static {
		try {
			conn = PostgresqlUtils.getConnection();
			connU = PostgresqlUtils.getConnection();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * 
	 * @Description: TODO
	 * @param args
	 * @throws IOException
	 * @throws Exception
	 * @return void
	 * @author liuming
	 * @date 2015年12月1日 下午6:58:21
	 */
	public static void main(String[] args) throws IOException, Exception {

		PropertyConfigurator.configure("D:\\data\\git\\blob\\log\\log4j.properties");
		long da = System.currentTimeMillis();

		File file = null;
		try {
//			file = new File("C:/data/食品药品/第三次拷贝/08-14outputHtml/outputHtml/GSP认证");

			// ------------------------- 1 食品药品 2 建筑 3 海关 4 百强 5 税务
			
//			show("etp_ranking_t", "ranking_id", new String[] { "company" }, 4 , 0);
			show("TAX_LAW_T", "tax_id", new String[] { "taxpayer" }, 5 , 0);
//			show("customs_rate_t", "rate_id", new String[] { "company" }, 3 , 0);
			
//			show("ETP_HUNDRED_T", "HUNDRED_id", new String[] { "company" }, 4 , 0);  //是province 不是province1
//			show("INDUSTRY_COMPANY_RANK_T", "id", new String[] { "company" }, 4 , 0);//是province 不是province1
			
			
//			show("APPARATUS_CATALOG_TEST_T", "id", new String[] {   "test_unit"}, 1,1);//医疗器械检测中心受检目录	31162
//			show("DRUG_GSP_T", "id", new String[] { "province", "company"}, 1,1);//GSP认证   150759
//			show("APPARATUS_DOMESTIC_HISTORY_T", "id", new String[] {   "company"}, 1,1);//国产器械（历史数据）   119580
//			show("APPARATUS_DOMESTIC_T", "id", new String[] {"reg_name","reg_addree"}, 1,1);//国产器械   30215 
//			show("DRUG_PRODUCT_T", "id", new String[] { "province","company","reg_address" }, 1);//7179
//			show("DRUG_OPERATE_T", "id", new String[] { "reg_office" }, 1);  //137429
//			show("FOOD_LICENSE_T", "id", new String[] {"reg_office" , "company" }, 1);//食品生产许可获证企业   168471  168471
//			show("FOOD_ADDITIVES_T", "id", new String[] { "province","company","report_depart" }, 1,1);//2873
//			 show("APPARATUS_FOREIGN_T","id",  new String[] { "agt_name" },  1,1);//进口器械  40956
//				show("DRUG_SUPPLEMENT_T", "id", new String[] {  "office" ,"company"}, 1,1);//药品注册补充申请备案情况公示     47065
//				show("FOOD_CHECK_T", "id", new String[] {     "nominal_company"}, 1,1);//国家食品安全监督抽检(合格/不合格)    80204
//			show("DRUG_CLINICL_GOV_T", "id", new String[] { "province","gov_name" }, 1,1);//823
//			show("DRUG_PATENT_T", "id", new String[] { "patentee"}, 1,1);//1935
//			show("DRUG_NET_CATALOG_T", "id", new String[] {"province","company"}, 1,1);//3919
//			 show("APPARATUS_FIRST_T", "id", new String[] {"record_name" }     ,1,1);//4931
//				show("DRUG_IMPORT_SUPERVISE_T", "id", new String[] {"province","city","agent_gov"}, 1,1);// 进口药品电子监管工作代理机构   383
//			show("APPARATUS_FOREIGN_HISTORY_T", "id", new String[] {"agent" } ,1,1);//进口器械（历史数据）11767
			





//			show("APPARATUS_DOMESTIC_T", "id", new String[] {"reg_name","reg_address"}, 1);
			// show("FOOD_LICENSE_T","company",1);
			// show("FOOD_ADDITIVES_T","company",1);
			// show("FOOD_CHECK_T","nominal_company",1);
			// show("FOOD_CHECK_T","sampling_name",1);
			// show("DRUG_CLINICL_GOV_T","gov_name",1);
			// show("DRUG_SUPPLEMENT_T","company",1);
			// show("DRUG_OPERATE_T","company",1);
			// show("DRUG_PATENT_T","application",1);
			// show("DRUG_PATENT_T","patentee",1);
			// show("DRUG_NET_CATALOG_T","company",1);
			// show("DRUG_IMPORT_SUPERVISE_T","agent_gov",1);
			// show("APPARATUS_DOMESTIC_T","reg_name",1);
			// show("APPARATUS_DOMESTIC_HISTORY_T","company",1);
			// show("APPARATUS_FOREIGN_T","service",1);
			// show("APPARATUS_FOREIGN_HISTORY_T","service",1);
			// show("DRUG_PRODUCT_T","company",1);
			

			// show("build_base_t","company_name",2);//建筑
			// show("customs_rate_t","company",3);//海关
			// show("etp_ranking_t","company",4);//百强
			// show("TAX_LAW_T","taxpayer",5);//税

			logger.info("总数：" + SUM);
			logger.info("入库数" + inNum);
			logger.info("已存在数" + existNum);

			logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis() - da) / 1000)) + "秒数");
			logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 1)) + "分钟");
			logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 60)) + "小时");
			logger.info(
					"平均每秒" + (float) (sumcount / (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (1 * 1))));
			logger.info(
					"平均每分" + (float) (sumcount / (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 1))));
			logger.info(
					"平均每时" + (float) (sumcount / (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 60))));

		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} finally {
			file = null;
		}

	}

	static int sumcount=0;
	static int page_size = 1000;
	public static Bucket bucket = null;

	private static void show(String table, String searchColum, String[] strs, Integer flag,Integer intOrString) throws Exception {

		PreparedStatement stmtUpdate = null;

		String search_clum = "";
		for (String str : strs) {
			search_clum += str + "||" + "  ";
		}
		search_clum = search_clum.substring(0, search_clum.lastIndexOf("||")) + " AS search_clum";
		List<JsonDocument> documents = new ArrayList<JsonDocument>();
		JsonObject content = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtInsert = null;

		ResultSet resultSet = null;
		JSONObject temJson = null;

		String sqlQ = " SELECT " + searchColum + " , " + search_clum + " FROM " + table + "  ORDER BY "+searchColum+" LIMIT " + page_size
				+ "  OFFSET   ";

		// 得到总数
		String te = PostgresqlUtils.getValueFromSql("SELECT count(1) from " + table + " ");

		CompanyNameVO vo = null;
		int temp = Integer.valueOf(te);
		String[] array = null;
		String sqlUpdate = null;
		for (int iii = 0; iii <= (temp / page_size); iii++)//
		{
			SUM++;

			stmt = conn.prepareStatement(sqlQ + (iii * page_size));
			resultSet = stmt.executeQuery();

			while (resultSet.next()) {

				sumcount++;
				
				String key = resultSet.getString(1);
				String value = resultSet.getString(2);
				 
				if(StringUtils.isNull(value)){
					continue;
				}

				System.out.println(sumcount+   " table: " + table + "  id: " + key);
				if (!StringUtils.isNull(value)) {
					array = util.utils_company(value);
					if (null != array[0]) {
						// vo.setProvince(array[0]);
						// vo.setCity(array[1]);
						// vo.setArea(array[2]);
					} else {
						continue;
					}
				}
				if (StringUtils.isNull(key)) {
					continue;
				}
				// sqlUpdate="UPDATE "+table+" SET province1=
				// "+array[0]+",city1= "+array[1]+",area1= "+array[2]+" WHERE
				// id= '"+key+"' ";
			 
					sqlUpdate = "UPDATE  " + table + " SET province1= ?,city1= ?,area1=?  WHERE "+searchColum+"= ? ";
				try {
					stmtUpdate = connU.prepareStatement(sqlUpdate);
					stmtUpdate.setString(1, array[0]);
					// if(!StringUtils.isNull(array[1])){
					// stmtUpdate.setString(2, array[1]);
					// }
					// if(!StringUtils.isNull(array[2])){
					// stmtUpdate.setString(3, array[2]);
					// }
					stmtUpdate.setString(2, array[1]);
					stmtUpdate.setString(3, array[2]);
					
					if(intOrString==1){
						stmtUpdate.setString(4, key);
					}else{
						stmtUpdate.setInt(4, Integer.valueOf(key) );
					}
					
					
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				try {
					stmtUpdate.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			System.out.println(SUM * page_size);

		}

	}

}
