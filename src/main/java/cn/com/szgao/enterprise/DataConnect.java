package cn.com.szgao.enterprise;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * 通用连接数据库的方法
 * @param  conn: 连接对象
 * @author xiongchangyi
 * @since 2018-05-08
 * @version 1.0
 */
public class DataConnect {	
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb";
		String usr = "postgres";
		String psd = "615601.xcy*";
		Connection conn = null;
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, usr, psd);
		return conn;
	}

	public static Connection getConnection2(){
		String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb";
		String usr = "postgres";
		String psd = "615601.xcy*";
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, usr, psd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static Connection getConnection3(){
		String url = "jdbc:postgresql://192.168.1.2:5432/extractdb";
		String usr = "postgres";
		String psd = "615601.xcy*";
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(url, usr, psd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
