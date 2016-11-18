package cn.com.szgao.wash.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import cn.com.szgao.dto.IndustryVO;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;

 



/**
 * 数据库工具类：连接数据库；
 * 查询省、市、县区等数据
 * @author xiongchangyi
 * @since 2015-06-01
 */
public class DataUtils {
	//连接服务器
	private Cluster cluster = null;
	//桶对象
	private Bucket bucket = null;
	/**
	 * 查询【省】集合，存储字段是：common_name,province_name
	 */
	static Map<String,String> shengMap = new HashMap<String,String>();
	/**
	 * 查询【地级市】集合，存储字段是short_name,city_name
	 */
	static Map<String,String> shiMap = new HashMap<String,String>();
	/**
	 * 查询【县级市】集合，存储字段：short_name,country
	 */
	static Map<String,String> xianJiShiMap = new HashMap<String,String>();
	/**
	 * 查询【县】集合，存储字段：short_name,country
	 */
	static Map<String,String> xianMap = new HashMap<String,String>();
	/**
	 * 查询【区】集合，存储字段：country
	 */
	static List<String> quList = new ArrayList<String>();
	
	/**
	 * 查询【区】集合，存储字段：short_name,country
	 */
	static Map<String,String> quMap = new HashMap<String,String>();
	
	/**
	 * 查询【县级市、县、旗】集合，存储字段：short_name4,country  XianJiShiXianQu
	 */
	static Map<String,String> xianJiShiXianMap = new HashMap<String,String>();
	/**
	 * 查询【县级市、县、旗】集合，存储字段：country,parent_id
	 */
	static Map<String,Integer> xianJiShiXianQuMap = new HashMap<String,Integer>();
	/**
	 * 查询【地级市名称、省ID】
	 * 存储字段： city_name,parent_id
	 */
	static Map<String,Integer> province_city_map = new HashMap<String,Integer>();
	/**
	 * 查询【简称县级市、简称县、全称区、全称旗，全称县作为value】
	 * 存储字段：short_name2, country
	 */
	static Map<String,String> jianChenXianQuanChenXianMap = new HashMap<String,String>();
	
	/**
	 * 查询【行政区划代码】、【行政区】
	 * 表： adp_t
	 */
	static Map<String,String> adminCodeMap = new HashMap<String,String>();
	
	//县
	static Map<String,Integer> countryMap = new HashMap<String,Integer>();
	//市名称和省的ID
	//县名称和市的ID
	static Map<String,Integer> city_country_map = new HashMap<String,Integer>();
	//县级市简称和上级市ID
	static Map<String,Integer> shortCountryMap = new HashMap<String,Integer>();
	
	
	/**
	 * 查询县、地级市的ID,
	 * 存储字段：行政区划、县名称、地级市ID
	 * 表：country_t
	 */
	public static final Map<String,String[]> adminCountryMap = Collections.synchronizedMap(new HashMap<String,String[]>());
	/**
	 * 查询地级市、省的ID,
	 * 存储字段：行政区划、地级市名称、省的ID
	 * 表：city_t
	 */
	public static final Map<String,String[]> adminCityMap = Collections.synchronizedMap(new HashMap<String,String[]>());
	/**
	 * 查询省,
	 * 存储字段：行政区划、省名称
	 * 表：province_t
	 */
	public static final Map<String,String> adminProvinceMap = Collections.synchronizedMap(new HashMap<String,String>());
	
	
	/**
	 * 通用连接数据库的方法
	 * PG DATA = duplicatedb
	 * @param  conn: 连接对象
	 * @author xiongchangyi
	 * @since 2018-05-08
	 * @version 1.0
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb";
		String usr = "postgres";
		String psd = "615601.xcy*";
		Connection conn = null;
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, usr, psd);
		return conn;
	}
	/**
	 * 通用连接数据库的方法
	 * PG DATA = extractdb
	 * @param  conn: 连接对象
	 * @author xiongchangyi
	 * @since 2018-05-08
	 * @version 1.0
	 */
	public Connection getExtractdbConnection() throws ClassNotFoundException, SQLException{
		String url = "jdbc:postgresql://192.168.251:5432/extractdb";
		String usr = "postgres";
		String psd = "615601.xcy*";
		Connection conn = null;
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, usr, psd);
		return conn;
	}
	/**
	 * 查询【省】，存储字段：common_name,  全 province_name      +++++++++++++
	 * @author xiongchangyi
	 * @since 2018-05-08
	 * @version 1.0
	 */
	public void listProvince()
	{
		PreparedStatement provinceStmt = null;//查询省
		Connection conn = null;//连接
		ResultSet rs = null;//结果集
		String provinceSql = "SELECT common_name,province_name FROM province_t";//省
		try {
			conn = getConnection();	
			provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
			rs = provinceStmt.executeQuery();//查询省
			while(rs.next())
			{
				shengMap.put(rs.getString(1),rs.getString(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=provinceStmt)
		          {
		        	  provinceStmt.close();		        
		        	  provinceStmt = null;		        		          
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	
	/**
	 * 查询【地级市】，存储字段：short_name,city_name      +++++++++++++++++
	 */
	public void listCity()
	{
		PreparedStatement cityStmt = null;//查询市
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String citySql = "SELECT short_name2,city_name FROM city_t WHERE short_name IS NOT NULL";//市
		try {
			conn = getConnection();	
			cityStmt = conn.prepareStatement(citySql);		//预编译查询
			rs = cityStmt.executeQuery();//查询市
			while(rs.next())
			{
				shiMap.put(rs.getString(1),rs.getString(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=cityStmt)
		          {
		        	  cityStmt.close();
		        	  cityStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	
	/**
	 * 查询【县级市】，存储字段：short_name,country          +++++++++++++
	 */
	public void listAllShortCityName(){
		PreparedStatement countryStmt = null;//查询县
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT short_name,country FROM country_t WHERE country like '%市'"; //县
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);  //预编译查询
			rs = countryStmt.executeQuery();				  //查询县
			while(rs.next())
			{
				xianJiShiMap.put(rs.getString(1),rs.getString(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	
 
	/**
	 * 查询县级 【区】存储字段：short_name2,country        
	 * 
	 * @author liuming
	 * @Date 2015-9-29 下午4:21:11
	 */
	public void listAllCountryQu(){
		PreparedStatement countryStmt = null;//查询县、旗
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
//		String countrySql = "SELECT short_name2,country FROM country_t WHERE country LIKE '%区'  "; //县
		String countrySql = "SELECT short_name4,country FROM country_t WHERE country LIKE '%区'  "; //县
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);  //预编译查询
			rs = countryStmt.executeQuery();				  //查询县
			while(rs.next())
			{
				quMap.put(rs.getString(1),rs.getString(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	
	public void listAllCountry(){
		PreparedStatement countryStmt = null;//查询县、旗
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT short_name,country FROM country_t WHERE country LIKE '%县' OR country LIKE '%旗'"; //县
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);  //预编译查询
			rs = countryStmt.executeQuery();				  //查询县
			while(rs.next())
			{
				xianMap.put(rs.getString(1),rs.getString(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	
	
	/**
	 * 查询【区】，存储字段：country             +++++++++++++++          
	 */
	public void listAllArea(){
		PreparedStatement countryStmt = null;//查询区
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT country FROM country_t WHERE country LIKE '%区'"; //区
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);  //预编译查询
			rs = countryStmt.executeQuery();				  //查询县
			while(rs.next())
			{
				quList.add(rs.getString(1));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	/**
	 * 查询【县级市、县、旗】集合
	 * 存储字段：short_name,country       +++++++++++++++          
	 */
	public void listXianJiShiXian(){
		PreparedStatement countryStmt = null;
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
//		String countrySql = "SELECT short_name,country FROM country_t WHERE country LIKE '%市' OR country LIKE '%县' OR country LIKE '%旗'"; 
//		String countrySql = "SELECT short_name4,country FROM country_t WHERE country LIKE '%市' OR country LIKE '%县' OR country LIKE '%旗' "; 
//		String countrySql = "SELECT short_name4,country FROM country_t "; 
		String countrySql = "SELECT short_name5,country FROM country_t "; 
		
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);  //预编译查询
			rs = countryStmt.executeQuery();				  //查询县
			while(rs.next())
			{
				xianJiShiXianMap.put(rs.getString(1),rs.getString(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  rs.close();
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	
	/**
	 * 不带条件，查询【所有县级市、县、区】及地级市ID      ++++++++++++++++++
	 * 存储字段：country,parent_id
	 */
	public void listXianJiShiXianQu()
	{
		PreparedStatement countryStmt = null;//查询县、县级市、区
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT country,parent_id FROM country_t"; //县、县级市、区，及父id
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);  //预编译查询
			rs = countryStmt.executeQuery();				  //查询县
			while(rs.next())
			{
				xianJiShiXianQuMap.put(rs.getString(1),rs.getInt(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	/**
	 * 不带条件，查询【所有简称县级市、简称县、全称区】     ++++++++++++++++++
	 * 存储字段：short_name2, country
	 */
	public void JianChenShiJianChenXianQuanQuQi()
	{
		PreparedStatement countryStmt = null;//查询县、县级市、区
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT SHORT_NAME2,COUNTRY FROM COUNTRY_T WHERE SHORT_NAME2 IS NOT NULL"; //简称县、简称县级市、全区
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);  //预编译查询
			rs = countryStmt.executeQuery();				  //查询县
			while(rs.next())
			{
				jianChenXianQuanChenXianMap.put(rs.getString(1),rs.getString(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}

	/**
	 * 通过县名查询【地级县及市ID】
	 * 存储字段：parent_id,city_name
	 * @param countryName  县名
	 * @return
	 * @author liuming
	 * @Date 2015-9-30 下午12:17:22
	 */
	public Map<Integer,String> listCountryCityIdBycountryName(String  countryName){
		Map<Integer,String> cityNameAndProvinceIdMap = new HashMap<Integer,String>();
		PreparedStatement countryStmt = null;//查询市
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT parent_id,country FROM country_t WHERE country=?";//县
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);//预编译查询
			countryStmt.setString(1, countryName);
			rs = countryStmt.executeQuery();//查询县
			while(rs.next())
			{
				cityNameAndProvinceIdMap.put(rs.getInt(1),rs.getString(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		return cityNameAndProvinceIdMap;
	}
	/**
	 * 
	 * @param 通过市ProviceID查询【地级市市名称和ID】                                ++++++++++++++++++++++++
	 * 存储字段：city_name,city_id
	 * @return
	 * @author liuming
	 * @Date 2015-9-30 上午11:25:42
	 */
	public Map<String,Integer> listCityIdByProvinceId(int provincId){
		Map<String,Integer> cityNameAndProvinceIdMap = new HashMap<String,Integer>();
		PreparedStatement countryStmt = null;//查询市
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT city_name,city_id FROM city_t WHERE parent_id=?";//
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);//预编译查询
			countryStmt.setInt(1, provincId);
			rs = countryStmt.executeQuery();//查询县
			while(rs.next())
			{
				cityNameAndProvinceIdMap.put(rs.getString(1),rs.getInt(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		return cityNameAndProvinceIdMap;
	}
	/**
	 * 
	 * @param 通过市ProviceName查询【地级市市名称和市ID】                                ++++++++++++++++++++++++
	 * 存储字段：city_name,city_id
	 * @return
	 * @author liuming
	 * @Date 2015-9-30 上午11:25:42
	 */
	public Map<Integer,String> listCityIdByProvinceName(String provincName){
		Map<Integer,String> cityNameAndProvinceIdMap = new HashMap<Integer,String>();
		PreparedStatement countryStmt = null;//查询市
		Connection conn = null;//连接
		ResultSet  rs= null;//结果集		
		String countrySql = "SELECT city_name,city_id FROM city_t WHERE parent_id=(SELECT province_id FROM province_t WHERE province_name=?) ORDER BY city_id";//
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);//预编译查询
			countryStmt.setString(1, provincName);
			rs = countryStmt.executeQuery();//查询县
			while(rs.next())
			{
				cityNameAndProvinceIdMap.put(rs.getInt(2),rs.getString(1));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		return cityNameAndProvinceIdMap;
	}
	
	/**
	 * 
	 * @param 通过市ProviceName查询【县名名称short_name4和country】                                ++++++++++++++++++++++++
	 * 存储字段：short_name4,country
	 * @return
	 * @author liuming
	 * @Date 2015-9-30 上午11:25:42
	 */
	public Map<String,String> listCountryIdByProvinceName(String provincName){
		Map<String,String> cityNameAndProvinceIdMap = new HashMap<String,String>();
		PreparedStatement countryStmt = null;//查询市
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		
		
		String countrySql ="";
		
		if(provincName.contains("北京")||provincName.contains("天津")||provincName.contains("重庆")||provincName.contains("上海")){
			countrySql ="SELECT short_name5,country FROM country_t WHERE parent_id IN (SELECT province_id FROM province_t  WHERE  province_name = ?) ";
		}else{
			countrySql=" SELECT short_name5,country FROM country_t WHERE parent_id IN "
					+"(SELECT city_id FROM city_t where parent_id ="
					+"(SELECT province_id FROM province_t  WHERE  province_name = ?)"
					+")";
		}
		
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);//预编译查询
			countryStmt.setString(1,  provincName );
			rs = countryStmt.executeQuery();//查询县
			while(rs.next())
			{
				cityNameAndProvinceIdMap.put(rs.getString(1),rs.getString(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		return cityNameAndProvinceIdMap;
	}
	
	
	/**
	 * 通过地级市ID，查询地级市名称
	 *查询【地级市名称和省ID】,cityNameAndProvinceIdMap
	 * @param cityId
	 * @return
	 */
	public Map<String,Integer> listCityProvinceIdByCityId(int cityId){
		Map<String,Integer> cityNameAndProvinceIdMap = new HashMap<String,Integer>();
		PreparedStatement countryStmt = null;//查询市
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT city_name,parent_id FROM city_t WHERE city_id=?";//县
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);//预编译查询
			countryStmt.setInt(1, cityId);
			rs = countryStmt.executeQuery();//查询县
			while(rs.next())
			{
				cityNameAndProvinceIdMap.put(rs.getString(1),rs.getInt(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		return cityNameAndProvinceIdMap;
	}
	
	/**
	 * 通过省ID查询省名称                                                          +++++++++++++++++++++++++++
	 * @param id: 是省ID
	 * @author xiongchangyi
	 * @since 2018-05-08
	 * @version 1.0
	 */
	public String listProvinceNameByProvinceId(int id)
	{
		PreparedStatement provinceStmt = null;//查询省
		Connection conn = null;//连接
		ResultSet rs = null;//结果集
		String provinceSql = "SELECT province_name FROM province_t WHERE province_id=?";//省
		try {
			conn = getConnection();	
			provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
			provinceStmt.setInt(1, id);
			rs = provinceStmt.executeQuery();//查询省
			if(rs.next())
			{
				return rs.getString(1);
			}	
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=provinceStmt)
		          {
		        	  provinceStmt.close();		        
		        	  provinceStmt = null;		        		          
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		return null;
	}
	/**
	 * 通过省名查询省ID
	 * @param provinceName 省名
	 * @return
	 * @author liuming
	 * @Date 2015-9-30 上午11:45:59
	 */
	public int listProvinceIdByProvinceName(String provinceName)
	{
		PreparedStatement provinceStmt = null;//查询省
		Connection conn = null;//连接
		ResultSet rs = null;//结果集
		String provinceSql = "SELECT province_id FROM province_t WHERE province_name=?";//省
		try {
			conn = getConnection();	
			provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
			provinceStmt.setString(1, provinceName);
			rs = provinceStmt.executeQuery();//查询省
			if(rs.next())
			{
				return rs.getInt(1);
			}	
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=provinceStmt)
		          {
		        	  provinceStmt.close();		        
		        	  provinceStmt = null;		        		          
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		return 0;
	}
	
	/**
	 * 不带条件查询【市名称】和【省的ID】                         ++++++++++++++++++++++++++++++
	 * 存储字段：city_name,parent_id
	 */
	public void listProvinceCity()
	{
		PreparedStatement cityStmt = null;//查询市
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String citySql = "SELECT city_name,parent_id FROM city_t";//市
		try {			
			conn = getConnection();	
			cityStmt = conn.prepareStatement(citySql);		//预编译查询
			rs = cityStmt.executeQuery();//查询市
			while(rs.next())
			{
				province_city_map.put(rs.getString(1),rs.getInt(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=cityStmt)
		          {
		        	  cityStmt.close();
		        	  cityStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	/**
	 * 查询 县 数据到集合中
	 */
	public void listCountry()
	{
		PreparedStatement countryStmt = null;//查询县
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT country,parent_id FROM country_t country like '%县'"; //县
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);  //预编译查询
			rs = countryStmt.executeQuery();				  //查询县
			while(rs.next())
			{
				countryMap.put(rs.getString(1),rs.getInt(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	

	/**
	 * 查询市ID和县名称
	 */
	public void listCityIdCountry()
	{
		PreparedStatement cityStmt = null;//查询市
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String citySql = "SELECT country,parent_id FROM country_t";
		try {			
			conn = getConnection();	
			cityStmt = conn.prepareStatement(citySql);		//预编译查询
			rs = cityStmt.executeQuery();//查询市
			while(rs.next())
			{
				city_country_map.put(rs.getString(1),rs.getInt(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=cityStmt)
		          {
		        	  cityStmt.close();
		        	  cityStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	}
	/**
	 * 通过县名称，查询市的ID
	 * 通过区名称，查询市ID
	 * 上面2个都可以通用这个方法
	 * 
	 */
	public Map<String,Integer> listCityIdByCountryName(String country){
		//县名称和市的ID
		Map<String,Integer> city_country_map = new HashMap<String,Integer>();		
		PreparedStatement countryStmt = null;//查询市
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT country,parent_id FROM country_t WHERE country=?";//县
		try {
			conn = getConnection();	
			countryStmt = conn.prepareStatement(countrySql);//预编译查询
			countryStmt.setString(1, country);
			rs = countryStmt.executeQuery();//查询县
			while(rs.next())
			{
				city_country_map.put(rs.getString(1),rs.getInt(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
		return city_country_map;
	}
	
	/**
	 * 通过县简称查询市ID和县简称的Map
	 */
	public void listCountryParentId(){
		PreparedStatement countryStmt = null;//查询县级市
		Connection conn = null;//连接
		ResultSet rs = null;//结果集		
		String countrySql = "SELECT short_name,parent_id FROM country_t WHERE short_name IS NOT NULL";
		try {
			countryStmt = getConnection().prepareStatement(countrySql);		//预编译查询
			rs = countryStmt.executeQuery();//查询市
			while(rs.next())
			{
				shortCountryMap.put(rs.getString(1),rs.getInt(2));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=countryStmt)
		          {
		        	  countryStmt.close();
		        	  countryStmt = null;
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }		
	}
	
	
	
	/**
	 * CouchBase公共查询
	 * @param ip CouchBase的IP地址
	 * @param bucketName Bucket名称
	 * @param view_dev 视图的设计名称
	 * @param viewName 视图的名称
	 * @param limit 视图限制条数
	 * @return 返回视图里面所有数据记录条数迭代器
	 */
	public Iterator<ViewRow> commonViewIterator(String ip,String bucketName,String view_dev,String viewName,int limit){
		//连接服务器
		Cluster cluster = CouchbaseCluster.create(ip);
		//连接指定的桶
		Bucket bucket = cluster.openBucket(bucketName);
		//查询视图
		ViewResult result = bucket.query(ViewQuery.from(view_dev, viewName).limit(limit));	
		Iterator<ViewRow> iterator = result.rows();		
		return iterator;
	}
	/**
	 * CouchBase公共查询
	 * @param ip CouchBase的IP地址
	 * @param bucketName Bucket名称
	 * @return 返回Bucket对象
	 */
	public Bucket commonBucket(String ip,String bucketName){
		//连接服务器
		cluster = CouchbaseCluster.create(ip);
		//连接指定的桶
		bucket = cluster.openBucket(bucketName);
		return bucket;
	}
	/**
	 * 关闭桶
	 * 关闭集群连接对象
	 */
	public void closeBucketAndDisconnetion(){
		bucket.close();		
		cluster.disconnect();
	}
	/**
	 * 通过身份证获得行政区划
	 * @param idNum
	 * @return 行政区划数组result[0]省、result[1]市、result[2]县/区/县级市
	 * @author xiongchangyi
	 * @since 2019-7-31
	 */
	public String[] listProvinceCityAreaByAdmini(String idNum){
		String result[] = new String[3];
		if(null == idNum)
		{
			return result;
		}
		//如果长度不是15或者18位，则不是身份证
		if(null != idNum && (idNum.length() != 15 && idNum.length() != 18))
		{
			return result;
		}
		//如果全部是0表示失效身份证
		if(null != idNum && (idNum.equals("000000000000000")&& idNum.equals("000000000000000000")))
		{
			return result;
		}		
		String adminisCode = idNum.substring(0, 6);
		//查询行政区划名称和行政区划代码
		adminCode();
		if(adminCodeMap.containsKey(adminisCode))//身份证可以获取到行政区
		{
			String admin = adminCodeMap.get(adminisCode);
			
			//该行政区化代码是否属于县，县级市，区				
			if(DataUtils.xianJiShiXianQuMap.containsKey(admin))
			{
				result[2] = admin;//县、区、县级市
				Map<String,Integer> map = listCityProvinceIdByCityId(DataUtils.xianJiShiXianQuMap.get(admin));
				if(null != map)
				{
					//市名称
					Set<String> set = map.keySet();
					Iterator<String> it = set.iterator();
					if(it.hasNext())
					{
						result[1] = it.next();//市
					}
					set = null;
					it = null;
					Collection<Integer> coll = map.values();
					Iterator<Integer> ita = coll.iterator();
					int provinceId = 0;
					if(ita.hasNext())
					{
						provinceId = ita.next();
					}
					coll = null;
					ita = null;
					if(0 != provinceId)
					{
						String provinceName = listProvinceNameByProvinceId(provinceId);
						if(null != provinceName)
						{
							result[0] = provinceName;
						}
					}
				}
			}
			//该行政区化代码是否属于地级市
			else if(DataUtils.province_city_map.containsKey(admin))
			{
				result[1] = admin;//地级市
				int provinceId = DataUtils.province_city_map.get(admin);
				if(0 != provinceId)
				{
					result[0] = listProvinceNameByProvinceId(provinceId);
				}
			}
			//该行政区号是否属于省
			else if(DataUtils.shengMap.containsValue(admin))
			{
				result[0] = admin;
			}
		}		
		return result;
	}
	
	 /**
	  * 获得行政区划和行政区代码
	  * @author xiongchangyi
	  * @since 2019-7-31
	  */
	 public static void adminCode()
	 {
		PreparedStatement adminStmt = null;//查询行政区划和代码
		Connection conn = null;//连接
		ResultSet rs = null;//结果集
		DataUtils dut = new DataUtils();
		String sql = "SELECT adp_name,adp_code FROM adp_t";//行政区划和代码
		try {
			conn = dut.getExtractdbConnection();	
			adminStmt = conn.prepareStatement(sql);	//预编译查询
			rs = adminStmt.executeQuery();//查询行政区划和代码
			while(rs.next())
			{
				adminCodeMap.put(rs.getString(2),rs.getString(1));
			}			
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
		      try{
		          if(null !=adminStmt)
		          {
		        	  
		        	  rs.close();
		        	  rs = null;
		        	  
		        	  adminStmt.close();		        
		        	  adminStmt = null;		        		          
		         }
		      }catch(SQLException se){
		    }
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }
		   }
	 	}
	 

		
		
		
		/**
		 * 获得：行政区划号码、县、地级市ID
		 *  
		 */
		public void listCountryCityIdAdminCode()
		{
			PreparedStatement adminStmt = null;//查询行政区划和代码
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			DataUtils dut = new DataUtils();
			String sql = "SELECT TT.COUNTRY,TT.PARENT_ID,TT.ADMINI_CODE FROM COUNTRY_T TT";//行政区划和代码
			try {
				conn = dut.getConnection();	
				adminStmt = conn.prepareStatement(sql);	//预编译查询
				rs = adminStmt.executeQuery();//查询行政区划和代码
				String []array = null;
				while(rs.next())
				{
					array = new String[2];
					array[0] = rs.getString(1);//COUNTRY
					array[1] = rs.getString(2);//PARENT_ID
					adminCountryMap.put(rs.getString(3),array);
					array = null;
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=adminStmt)
			          {
			        	  rs.close();
			        	  rs = null;
			        	  adminStmt.close();		        
			        	  adminStmt = null;	
			        	  conn.close();
			        	  conn=null;
			         }
			      }catch(SQLException se){
			    }
			 }
		}
		/**
		 * 获得：行政区划、地级市名称、省的ID
		 *  
		 */
		public void listCityAdminCodeProvinceId()
		{
			PreparedStatement adminStmt = null;//查询行政区划和代码
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			DataUtils dut = new DataUtils();
			String sql = "SELECT CC.CITY_NAME,CC.PARENT_ID,CC.ADMINI_CODE FROM CITY_T CC";//行政区划和代码
			try {
				conn = dut.getConnection();	
				adminStmt = conn.prepareStatement(sql);	//预编译查询
				rs = adminStmt.executeQuery();//查询行政区划和代码
				String []array = null;
				while(rs.next())
				{
					array = new String[2];
					array[0] = rs.getString(1);//CITY_NAME
					array[1] = rs.getString(2);//PARENT_ID
					adminCityMap.put(rs.getString(3),array);
					array = null;
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=adminStmt)
			          {
			        	  rs.close();
			        	  rs = null;
			        	  adminStmt.close();		        
			        	  adminStmt = null;	
			        	  conn.close();
			        	  conn=null;
			         }
			      }catch(SQLException se){
			    }
			   }
		}
		/**
		 * 获得：行政区划、省名称
		 *  
		 */
		public void listProvinceAdminCode()
		{
			PreparedStatement adminStmt = null;//查询行政区划和代码
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			DataUtils dut = new DataUtils();
			String sql = "SELECT PP.PROVINCE_NAME,PP.ADMINI_CODE FROM PROVINCE_T PP";//行政区划和代码
			try {
				conn = dut.getConnection();	
				adminStmt = conn.prepareStatement(sql);	//预编译查询
				rs = adminStmt.executeQuery();//查询行政区划和代码
				while(rs.next())
				{
					adminProvinceMap.put(rs.getString(2),rs.getString(1));
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=adminStmt)
			          {
			        	  rs.close();
			        	  rs = null;
			        	  adminStmt.close();		        
			        	  adminStmt = null;	
			        	  conn.close();
			        	  conn=null;
			         }
			      }catch(SQLException se){
			    }
			   }
		}
		
		public   Map<String, Object> listIndustryV()
		{
			 Map<String, Object> mapV =  new HashMap<String, Object>();
			 
			PreparedStatement provinceStmt = null;//查询省
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			String provinceSql = "SELECT name,industry_name FROM industrynv_t where flag='V'  and industry_name is NOT NULL ";//省
			try {
				conn = getConnection();	
				provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
				rs = provinceStmt.executeQuery();//查询省
				while(rs.next())
				{
					mapV.put(rs.getString(1),rs.getObject(2));
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=provinceStmt)
			          {
			        	  provinceStmt.close();		        
			        	  provinceStmt = null;		        		          
			         }
			      }catch(SQLException se){
			    }
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			return mapV;
		}
		
		

		public   Map<String, Object> listIndustryN()
		{
			 Map<String, Object> mapN = new HashMap<String, Object>();
			 
			PreparedStatement provinceStmt = null;//查询省
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			String provinceSql = "SELECT name,industry_name FROM industrynv_t where flag='N' and industry_name is NOT NULL  ";//省
			try {
				conn = getConnection();	
				provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
				rs = provinceStmt.executeQuery();//查询省
				while(rs.next())
				{
					mapN.put(rs.getString(1),rs.getObject(2) );
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=provinceStmt)
			          {
			        	  provinceStmt.close();		        
			        	  provinceStmt = null;		        		          
			         }
			      }catch(SQLException se){
			    }
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			return mapN;
		}
		
		
	 /**
		 * 法院名------------------省市县
		 * @return   
		 * @return Map<String,Object>  
		 * @author liuming
		 * @date 2016年5月6日  下午3:31:17
		 */
		public    Map<String, Object> listCourtName()
		{
			 Map<String, Object> mapN = new HashMap<String, Object>();
			 String temp="";
			 
			PreparedStatement provinceStmt = null;//查询省
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			String provinceSql = "SELECT court_name2,province,city,area FROM court_name_t WHERE court_name2 is NOT NULL  ";//省
			try {
				conn = getConnection();	
				provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
				rs = provinceStmt.executeQuery();//查询省
				while(rs.next())
				{
					if(null!= rs.getObject(2) && !"".equals(rs.getObject(2).toString()) ){
						temp+=rs.getObject(2).toString(); 
					}else{
						continue;
					}
					
					if(null!= rs.getObject(3) && !"".equals(rs.getObject(3).toString())){
						temp+="-"+rs.getObject(3).toString(); 
					}else{
						temp+="-NULL";
					}
					
					if(null!= rs.getObject(4) && !"".equals(rs.getObject(4).toString())){
						temp+="-"+rs.getObject(4).toString(); 
					}else{
						temp+="-NULL";
					}
					
					
					
					mapN.put(rs.getString(1),    temp  );
					temp="";
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=provinceStmt)
			          {
			        	  provinceStmt.close();		        
			        	  provinceStmt = null;		        		          
			         }
			      }catch(SQLException se){
			    }
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			return mapN;
		}
		 
		
		/**
		 * 初始化省、市、县数据
		 * 从数据库查询行政区数据
		 * @author xiongchangyi
		 * @since 2018-6-23
		 */
		public static synchronized  void initData()
		{
			//ExecutorsText.log.info("初始化省市县数据");		
			//查询行政区数据
			DataUtils utils = new DataUtils();
			//查询【省】，shengMap
			utils.listProvince();
			//查询【地级市】, shiMap
			utils.listCity();
			//查询【县级市】, xianJiShiMap
			utils.listAllShortCityName();
			//查询【县】 , xianMap
			utils.listAllCountry();
			//查询【区】 , quMap
			utils.listAllCountry();
			//查询【县级市、县、旗】, xianJiShiXianMap
			utils.listXianJiShiXian();
			//查询【县级市、县、旗】,xianJiShiXianQuMap
			utils.listXianJiShiXianQu();
			//查询【市名称】和【省的ID】,province_city_map
			utils.listProvinceCity();
			//查询所有区，不包括  县、旗、县级市
			utils.listAllArea();
			//简称县级市、简称县、全称旗、区
			utils.JianChenShiJianChenXianQuanQuQi();
			//区行政区，市ID
			utils.listCountryCityIdAdminCode();
			//市行政区，省ID
			utils.listCityAdminCodeProvinceId();
			//省行政区
			utils.listProvinceAdminCode();
		}
		
		
		
		 
		/**
		 * 行业动词  匹配词条  
		 * @return   
		 * @return Map<String,IndustryVO>  
		 * @author liuming
		 * @date 2016年6月29日  上午10:45:53
		 */
		public   Map<String, IndustryVO> listIndustryV_New()
		{
			 Map<String, IndustryVO> mapV =  new HashMap<String, IndustryVO>();
			 
			PreparedStatement provinceStmt = null;//查询省
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			String provinceSql = "SELECT in_name,industry_name,industry_code,industry_id,flag FROM industrynv_n_t where flag='V'  and industry_name is NOT NULL ORDER BY  length (in_name)  DESC "; 
			try {
				conn = getConnection();	
				provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
				rs = provinceStmt.executeQuery();//查询省
				while(rs.next())
				{
					IndustryVO vo=new IndustryVO();
					vo.setIndustry_name(rs.getObject(2).toString());
					vo.setIndustry_code(rs.getObject(3)!=null?rs.getObject(3).toString():null);
					vo.setIndustry_id(rs.getObject(4).toString());
					vo.setFlag(rs.getObject(5).toString());
					vo.setIn_name(rs.getObject(1).toString());
					mapV.put(rs.getString(1),vo);
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=provinceStmt)
			          {
			        	  provinceStmt.close();		        
			        	  provinceStmt = null;		        		          
			         }
			      }catch(SQLException se){
			    }
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			return mapV;
		}
		
		/**
		 *   in_name   -----  vo   所有，不区别动名词
		 * @return   
		 * @return Map<String,IndustryVO>  
		 * @author liuming
		 * @date 2016年7月5日  下午4:58:09
		 */
		public   Map<String, IndustryVO> listIndustryVANDN_New()
		{
			 Map<String, IndustryVO> mapV =  new HashMap<String, IndustryVO>();
			 
			PreparedStatement provinceStmt = null;//查询省
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			String provinceSql = "SELECT in_name,industry_name,industry_code,industry_id,flag FROM industrynv_n_t where  industry_name is NOT NULL ORDER BY  length (in_name)  DESC "; 
			try {
				conn = getConnection();	
				provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
				rs = provinceStmt.executeQuery();//查询省
				while(rs.next())
				{
					IndustryVO vo=new IndustryVO();
					vo.setIndustry_name(rs.getObject(2).toString());
					vo.setIndustry_code(rs.getObject(3)!=null?rs.getObject(3).toString():null);
					vo.setIndustry_id(rs.getObject(4).toString());
					vo.setFlag(rs.getObject(5).toString());
					vo.setIn_name(rs.getObject(1).toString());
					mapV.put(rs.getString(1),vo);
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=provinceStmt)
			          {
			        	  provinceStmt.close();		        
			        	  provinceStmt = null;		        		          
			         }
			      }catch(SQLException se){
			    }
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			return mapV;
		}
		
		/**
		 *  匹配   行业名称  industry_name  ----vo
		 * @return   
		 * @return Map<String,IndustryVO>  
		 * @author liuming
		 * @date 2016年6月29日  上午10:42:35
		 */
		public   Map<String, IndustryVO> listIndustryVAndN_New()
		{
			 Map<String, IndustryVO> mapV =  new HashMap<String, IndustryVO>();
			 
			PreparedStatement provinceStmt = null;//查询省
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
//			String provinceSql = "SELECT industry_name,industry_name,industry_code,industry_id,flag FROM industrynv_n_t where flag='V'  and industry_name is NOT NULL "; 
			String provinceSql = "SELECT industry_name,industry_name,industry_code,industry_id,flag FROM industrynv_n_t where   industry_name is NOT NULL  ORDER BY  length (industry_name)  DESC"; 
			try {
				conn = getConnection();	
				provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
				rs = provinceStmt.executeQuery();//查询省
				while(rs.next())
				{
					IndustryVO vo=new IndustryVO();
					vo.setIndustry_name(rs.getObject(2).toString());
					vo.setIndustry_code(rs.getObject(3)!=null?rs.getObject(3).toString():null);
					vo.setIndustry_id(rs.getObject(4).toString());
					vo.setFlag(rs.getObject(5).toString());
					vo.setIn_name(rs.getObject(1).toString());
					mapV.put(rs.getString(1),vo);
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=provinceStmt)
			          {
			        	  provinceStmt.close();		        
			        	  provinceStmt = null;		        		          
			         }
			      }catch(SQLException se){
			    }
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			return mapV;
		}
		
		/**
		 * 行业名词 匹配词条    in_name   ---vo
		 * @return   
		 * @return Map<String,IndustryVO>  
		 * @author liuming
		 * @date 2016年6月29日  上午10:43:13
		 */
		public   Map<String, IndustryVO> listIndustryN_New()
		{
			 Map<String, IndustryVO> mapN = new HashMap<String, IndustryVO>();
			 
			PreparedStatement provinceStmt = null;//查询省
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			String provinceSql = "SELECT in_name,industry_name,industry_code,industry_id,flag FROM industrynv_n_t where flag='N' and industry_name is NOT NULL ORDER BY  length (in_name)  DESC ";//省
			try {
				conn = getConnection();	
				provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
				rs = provinceStmt.executeQuery();//查询省
				while(rs.next())
				{
					IndustryVO vo=new IndustryVO();
					vo.setIndustry_name(rs.getObject(2).toString());
					vo.setIndustry_code(rs.getObject(3)!=null?rs.getObject(3).toString():null);
					vo.setIndustry_id(rs.getObject(4).toString());
					vo.setFlag(rs.getObject(5).toString());
					vo.setIn_name(rs.getObject(1).toString());
					mapN.put(rs.getString(1),vo);
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=provinceStmt)
			          {
			        	  provinceStmt.close();		        
			        	  provinceStmt = null;		        		          
			         }
			      }catch(SQLException se){
			    }
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			return mapN;
		}
		
		
		public   Map<String, IndustryVO> listIndustryN_V_New_ASC()
		{
			 Map<String, IndustryVO> mapN = new HashMap<String, IndustryVO>();
			 
			PreparedStatement provinceStmt = null;//查询省
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			String provinceSql = "SELECT in_name,industry_name,industry_code,industry_id,flag FROM industrynv_n_t where industry_name is NOT NULL ORDER BY  length (in_name)  ASC ";//省
			try {
				conn = getConnection();	
				provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
				rs = provinceStmt.executeQuery();//查询省
				while(rs.next())
				{
					IndustryVO vo=new IndustryVO();
					vo.setIndustry_name(rs.getObject(2).toString());
					vo.setIndustry_code(rs.getObject(3)!=null?rs.getObject(3).toString():null);
					vo.setIndustry_id(rs.getObject(4).toString());
					vo.setFlag(rs.getObject(5).toString());
					vo.setIn_name(rs.getObject(1).toString());
					mapN.put(rs.getString(1),vo);
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=provinceStmt)
			          {
			        	  provinceStmt.close();		        
			        	  provinceStmt = null;		        		          
			         }
			      }catch(SQLException se){
			    }
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			return mapN;
		}
		
		/**
		 * 得到省
		 * @return   
		 * @return Map<String,String>  
		 * @author liuming
		 * @date 2016年6月17日  下午3:42:41
		 */
		public static   Map<String, String> listProince ()
		{
			 Map<String, String> mapN = new HashMap<String, String>();
			 
			PreparedStatement provinceStmt = null;//查询省
			Connection conn = null;//连接
			ResultSet rs = null;//结果集
			String provinceSql = "SELECT DISTINCT common_name, province_name  FROM  province_t ";//省
			try {
				conn = getConnection();	
				provinceStmt = conn.prepareStatement(provinceSql);	//预编译查询
				rs = provinceStmt.executeQuery();//查询省
				while(rs.next())
				{
					mapN.put(rs.getString(1),rs.getObject(2).toString());
				}			
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (SQLException e) {			
				e.printStackTrace();
			}finally{
			      try{
			          if(null !=provinceStmt)
			          {
			        	  provinceStmt.close();		        
			        	  provinceStmt = null;		        		          
			         }
			      }catch(SQLException se){
			    }
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			return mapN;
		}
		 
		
		
}
