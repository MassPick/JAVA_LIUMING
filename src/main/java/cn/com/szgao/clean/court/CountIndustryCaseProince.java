package cn.com.szgao.clean.court;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

///////\//import cn.com.szgao.court.esAndcb.CommonConstant;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.ElasticSearchConnUtils220;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.wash.data.AdministrationUtils;


/**
 * 统计 行业下的案由数
 * @author liuming
 * @Date 2016年11月10日 下午4:09:31
 */
public class CountIndustryCaseProince {
	static AdministrationUtils util;
	
	
	public static String[] LAWCASE_AD={"民事","刑事","行政","知识产权","赔偿","执行","涉外","海事"};
	static int countNum = 0;
	static int countNotHtml = 0;
	

	/**
	 * 生成文件的个数
	 */
	static int countP = 0;
	static BufferedWriter fwUn = null;
	static BufferedWriter fwUn2 = null;
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(CountIndustryCaseProince.class.getName());
	static Map<String, List<RecordData>> MAPS = new HashMap<String, List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //
	static long REPEATSUM = 0; // 去重后数据条数
	static long SUM = 0;
	static long count = 0;

	/**
	 * 裁判文书 数据写库PostgreSql和couchbase JSON导入extracl_url_t表和court桶
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
 
		
		
		SearchResponse sr01=ElasticSearchConnUtils220.getSearchResponseTerm("company","etp","regState","已吊销");
		System.out.println(sr01.getContext());
		
		for (SearchHit hit : sr01.getHits()) {
			
			System.out.println(hit.getSourceAsString());
//			 wh=gson.fromJson(hit.getSourceAsString(),WhVO.class);
			
		}
		
		
		
		SearchResponse sr0=ElasticSearchConnUtils220.getSearchResponseBymultiMatchQuery("company","etp","company.cn","富源机械厂");
		System.out.println(sr0.getContext());
		
		SearchResponse sr1=ElasticSearchConnUtils220.getSearchResponseByMust("company","etp","company.un","富源机械厂");
		System.out.println(sr1.getContext());
		
		SearchResponse sr=ElasticSearchConnUtils220.getSearchResponseByFuzzy("wholecourt2","court","clients.un","上海奥极通信息技术有限公司");
		System.out.println(sr.getContext());
		
		
		PreparedStatement provinceStmt = null;// 查询省
		Connection conn = null;// 连接
		ResultSet rs = null;// 结果集
		String provinceSql = " SELECT company,company_id,province,city,area   from  litt_company_data WHERE type_id=1 ";

		try {
			conn = getConnection();
			provinceStmt = conn.prepareStatement(provinceSql); // 预编译查询
			rs = provinceStmt.executeQuery();// 查询省
			while (rs.next()) {
				WholeCourtVO vo=new WholeCourtVO();
				
				String company= rs.getObject("company") != null ? rs.getObject("company").toString() : null;
				if(StringUtils.isNull(company)){
					continue;
				}
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
		
		
		try {
			
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
//			bucket.close();
			cluster2 = null;
		}
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");
		
		logger.info("总数： "+countNum);
	}
	
	

	/**
	 * 连接postgreSql库
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		String url = "jdbc:postgresql://192.168.1.2:5432/mpdb";
		String usr = "postgres";
		String psd = "615601.xcy*";
		Connection conn = null;
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, usr, psd);
		return conn;
	}

	/**
	 * 链接couchbase桶
	 * 
	 * @return
	 */
	public static Bucket connectionCouchBaseLocal() {
		// 连接指定的桶
		return cluster2.openBucket("court_New", 1, TimeUnit.MINUTES);
	}

	private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
}
