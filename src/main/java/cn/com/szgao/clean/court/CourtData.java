package cn.com.szgao.clean.court;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.com.szgao.dto.ClientVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.StringUtils;

public class CourtData {
	
	public static List<String> LISTCasecause_small_small = new ArrayList<String>();// 案由小小类
	public static List<String> LISTCasecause_small = new ArrayList<String>();// 案由小类
	public static List<String> LISTCasecause_middle = new ArrayList<String>();// 案由中类
	public static List<String> LISTCasecause_big = new ArrayList<String>();// 案由大类
	
	
	
	public static Map<String,WholeCourtVO> MAPCasecause_small_small=new HashMap<String, WholeCourtVO>();
	public static Map<String,WholeCourtVO> MAPCasecause_small=new HashMap<String, WholeCourtVO>();
	public static Map<String,WholeCourtVO> MAPCasecause_middle=new HashMap<String, WholeCourtVO>();
	public static Map<String,WholeCourtVO> MAPCasecause_big=new HashMap<String, WholeCourtVO>();
    
    
    

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
		String provinceSql = " SELECT  casecause_type,type_big,type_middle ,type_small,type_small_small   FROM casecause_type_t WHERE source  ='官网案由分类'  ";

		try {
			conn = getConnection();
			provinceStmt = conn.prepareStatement(provinceSql); // 预编译查询
			rs = provinceStmt.executeQuery();// 查询省
			while (rs.next()) {
				WholeCourtVO vo=new WholeCourtVO();
				vo.setType1(rs.getObject("casecause_type") != null ? rs.getObject("casecause_type").toString() : null);
				vo.setType2(rs.getObject("type_big") != null ? rs.getObject("type_big").toString() : null);
				vo.setType3(rs.getObject("type_middle") != null ? rs.getObject("type_middle").toString() : null);
				vo.setType4(rs.getObject("type_small") != null ? rs.getObject("type_small").toString() : null);
				vo.setType5(rs.getObject("type_small_small") != null ? rs.getObject("type_small_small").toString() : null);
				
				
				
				
				
				if (!StringUtils.isNull(rs.getObject("type_big") != null ? rs.getObject("type_big").toString() : "")) {
					LISTCasecause_big1.add(rs.getObject("type_big").toString());
					
					MAPCasecause_big.put(rs.getObject("type_big").toString(), vo);
				}
				if (!StringUtils.isNull(rs.getString("type_middle") != null ? rs.getString("type_middle") : "")) {
					LISTCasecause_middle1.add(rs.getString("type_middle").toString());
					
					MAPCasecause_middle.put(rs.getString("type_middle").toString(), vo);
				}
				if (!StringUtils.isNull(rs.getString("type_small") != null ? rs.getString("type_small") : "")) {
					LISTCasecause_small1.add(rs.getString("type_small").toString());
					

					MAPCasecause_small.put(rs.getString("type_small").toString(), vo);
					
				}
				if (!StringUtils
						.isNull(rs.getString("type_small_small") != null ? rs.getString("type_small_small") : "")) {
					LISTCasecause_small_small1.add(rs.getString("type_small_small").toString());
					
					MAPCasecause_small_small.put(rs.getString("type_small_small").toString(), vo);
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
	

	public static List<ClientVO> mapClientList = null;

	static {
		mapClientList = getlistsClientVO();

		getlistsCasecause();
		// for (ClientVO clientVO : mapClientList) {
		// System.out.println(clientVO.getClientFrom());
		// }
	}
	
	
	/**
	 * 得到当事人分类列表 list
	 * 
	 * @return
	 */
	public static List<ClientVO> getlistsClientVO() {
		List<ClientVO> list = new ArrayList<ClientVO>();
		// TreeMap<String, ClientVO> mapV = new TreeMap<String, ClientVO>();

		PreparedStatement provinceStmt = null;//  
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

	
	
	
}
