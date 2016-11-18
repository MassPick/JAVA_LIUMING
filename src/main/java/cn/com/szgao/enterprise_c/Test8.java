package cn.com.szgao.enterprise_c;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import cn.com.szgao.dto.CompanyVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Test8 {
	//基准集合
	static List<CompanyVO> baseList = new ArrayList<CompanyVO>();
	//对比集合
	static List<CompanyVO> compareList = new ArrayList<CompanyVO>();
	static Connection CONN =DataConnect.getConnection2();
	static List<String> List=new ArrayList<String>();
	static int COUNT=1;
	static String SQL = "INSERT INTO dump_hunan(name,province) VALUES(?,'湖南')";
	//日志对象
	private static Logger log = LogManager.getLogger(Test8.class);
	public static void main(String[] args) {
		PropertyConfigurator.configure("D:\\xcy\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		//数据文件路径
		File directFiles = new File("E:\\Temp_File\\2015-9-22\\跑广东其他");
		//数据文件数组
		long sum=System.currentTimeMillis();

		    //写数据
		    show(directFiles);
		    //防止遗漏
			if(List.size()>0){
				long da=System.currentTimeMillis();
				log.info("第"+(COUNT++)+":>>>>>");
				createsql(List);
				log.info("耗时:"+((System.currentTimeMillis()-da)/1000)+"秒");	
				List=null;
				List=new ArrayList<String>();
			}
	
		try {
			CONN.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			directFiles = null;
		}	
		long sum2=System.currentTimeMillis();
		log.info("总耗时:"+((sum2-sum)/1000));
	}
	
	public static void show(File file){
		if(file.isFile()){
			create(file);
			return;
		}
		File[] files=file.listFiles();
		for(File fi:files){
			if(fi.isFile()){		
				create(fi);
			}
			else if(fi.isDirectory()){
				show(fi);
			}
			else{
				continue;
			}
		}
	}
	
	
	public static void create(File file){
		String value=null;
		//读取文件里面的内容的方法
		 value=readDataIntoDataBase2(file);
		if(null!=value){
			List.add(value);
		}
		//file.delete();
		if(List.size()>=1000){
			long da=System.currentTimeMillis();
			log.info("第"+(COUNT++)+"批:>>>>>");
			createsql(List);
			log.info("耗时:"+((System.currentTimeMillis()-da))+"毫秒");	
			List=null;
			List=new ArrayList<String>();
		}

	}
	
	
	public static void createsql(List<String> list){
		PreparedStatement stmt = null;
		try {
			CONN.setAutoCommit(false); 
			stmt = CONN.prepareStatement(SQL);
			for(String value:list){
				stmt.setString(1,value);
				stmt.addBatch();
			}
			stmt.executeBatch();
			CONN.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(null!=stmt){
				  stmt.cancel();
				  stmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static String readDataIntoDataBase2(File file)
	{
		//log.info(file.getName());
		//拼接字符对象
		StringBuffer sb = new StringBuffer();
		FileReader fr=null;
		String name=null;
		try {
			fr = new FileReader(file);
			int ch = 0;    
			while((ch = fr.read())!=-1 )
			{ 
				sb.append((char)ch);				
			} 
			JSONObject json= null;//JSONObject.fromString(sb.toString());
			name = json.getString("compname");
		} catch (Exception  e1) {	
			log.error(file.getName());
			log.error(e1.getMessage());
		}
		finally{
			try {
				if(null!=fr)
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fr = null;
			//file.delete();
			sb=null;
		}	    
        return name;
  
	}	
	
	@SuppressWarnings("static-access")
	public static void readDataIntoDataBase(File file) throws Exception
	{
		//拼接字符对象
		StringBuffer sb = new StringBuffer();
		FileReader	fr = new FileReader(file);
		int ch = 0;    
		while((ch = fr.read())!=-1 )
		{ 
			sb.append((char)ch);				
		} 
		fr.close();
		fr = null;
		JSONObject json= null;//JSONObject.fromString(sb.toString());	
		sb = null;//释放对象
		String name = json.getString("compname");
		Connection conn = new DataConnect().getConnection();
		conn.setAutoCommit(false); 
		String sql = "INSERT INTO dump_guangdong(NAME,province) VALUES(?,'广东')";
		PreparedStatement stmt = conn.prepareStatement(sql);
		if(null != name)
		{
			stmt.setString(1,name);			
			stmt.executeUpdate();
		}
		stmt.close();
		conn.close();
		json = null;
	}
	
}
 