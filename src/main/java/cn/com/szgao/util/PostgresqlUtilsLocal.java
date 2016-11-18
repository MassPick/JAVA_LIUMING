package cn.com.szgao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 通用连接数据库的方法
 * 
 * @param conn:
 *            连接对象
 * @author xiongchangyi
 * @since 2018-05-08
 * @version 1.0
 */
public  class PostgresqlUtilsLocal {
	public  static Connection getConnection() throws ClassNotFoundException, SQLException {
		
		String url = "jdbc:postgresql://localhost:5432/demo";
		String usr = "postgres";
		String psd = "masspick";
		
		
		/*String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb";
//		String url = "jdbc:postgresql://192.168.1.2:5432/mpdb";
		String usr = "postgres";
		String psd = "615601.xcy*";*/
		Connection conn = null;
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, usr, psd);
		return conn;
	}

	/**
	 * 由sql得到一个值
	 * @param sql
	 * @return
	 */
	public static String getValueFromSql(String sql) {
		String temp = "";
		Connection conn = null;
		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		try {
			
			try {
				conn = getConnection();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				stmt = conn.prepareStatement(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				resultSet = stmt.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (resultSet.next()) {
					temp = resultSet.getString(1) != null ? resultSet.getString(1) : "";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
		} finally {
			if(resultSet!=null){
				try {
					resultSet.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return temp;
	}

}
