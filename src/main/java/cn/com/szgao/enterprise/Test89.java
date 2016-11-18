package cn.com.szgao.enterprise;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 除贵州省外的省份
 * @author xiongchangyi
 *
 */
public class Test89 {
	/**
	 * 日志对象
	 */
	private Logger log;
	public Test89()
	{
		
	}
	public Test89(Logger log)
	{
		this.log=log;
	}
	public Logger getLog() {
		return log;
	}
	public void setLog(Logger log) {
		this.log = log;
	}
	/**
	 * 记录无基本信息的表
	 */
	Connection CONN =DataConnect.getConnection3();
	/**
	 * 正式的写数据
	 */
	 Connection CONN2 =DataConnect.getConnection2();
	/**
	 * 表名称
	 */
	String tableName = null;
	/**
	 * 正常企业名称集合
	 */
	List<String> List = new ArrayList<String>();
	/**
	 * 无基本信息的企业名称集合
	 */
	List<String> errorList = new ArrayList<String>(); 
	//重复
	int chongfu = 0;
	int COUNT=1;
	/**
	 * COMMITCOUNT条提交数据
	 */
	int COMMITCOUNT = 100;
	/**
	 * 根据字符串生成UUID对象
	 */
	NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	/**
	 * 线程调用的方法
	 * @param filePath 文件路径
	 * @param tableName 对应的表
	 */
	public void test(String filePath,String tableName)
	{
		//表名称
		this.tableName = tableName;
		//数据文件路径
		File directFiles = new File(filePath);
		//数据文件数组\
		long sum=System.currentTimeMillis();
	    //写数据
	    show(directFiles);
	    //防止遗漏
		if(List.size()>0)
		{
			long da=System.currentTimeMillis();
			log.info("第"+(COUNT++)+":>>>>>");
			createsql(List);
			log.info("耗时:"+((System.currentTimeMillis()-da)/1000)+"秒");	
			List=null;
			List=new ArrayList<String>();
		}
		//防止无基本信息的企业名称遗漏
		if(null!=errorList && errorList.size()>0)
		{
			try {
				insertErrorData();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		long sum2=System.currentTimeMillis();
		log.info("总耗时:"+((sum2-sum)/1000));
	}
	
	public void show(File file){
		if(file.isFile())
		{
			create(file);
			return;
		}
		File[] files=file.listFiles();
		if(null != files && files.length>0)
		{
			for(File fi:files)
			{
				if(fi.isFile())
				{		
					create(fi);
				}
				else if(fi.isDirectory())
				{
					show(fi);
				}
				else
				{
					continue;
				}
			}
		}
	}
	
	public void create(File file){
		String value=null;
		//读取文件里面的内容的方法
		 value=readDataIntoDataBase2(file);
		//调用写无基本信息的方法
		if(null!=errorList && errorList.size()>=COMMITCOUNT)
		{
			try {
				insertErrorData();
			} catch (SQLException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		if(null!=value && 0!=value.hashCode())
		{
			List.add(value);
		}
		//file.delete();
		if(List.size()>=1000)
		{
			long da=System.currentTimeMillis();
			log.info("第"+(COUNT++)+"批:>>>>>");
			createsql(List);
			log.info("耗时:"+((System.currentTimeMillis()-da))+"毫秒");	
			List=null;
			List=new ArrayList<String>();
		}
	}
	
	public void createsql(List<String> list){
		PreparedStatement stmt = null;
		try {
			CONN2.setAutoCommit(false); 
			stmt = CONN2.prepareStatement("INSERT INTO "+tableName+"(name) VALUES(?)");
			for(String value:list){
				stmt.setString(1,value);
				stmt.addBatch();
			}
			stmt.executeBatch();
			CONN2.commit();
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
	
	public String readDataIntoDataBase2(File file)
	{
		System.out.println(file.getName());
		//拼接字符对象
		StringBuffer sb = new StringBuffer();
		FileReader fr=null;
		String name=null;
		try 
		{
			fr = new FileReader(file);
			int ch = 0;    
			while((ch = fr.read())!=-1 )
			{ 
				sb.append((char)ch);				
			} 
			JSONObject obj= JSONObject.fromObject(sb.toString());
			sb = null;
			JSONObject jsobj = null;
			if(null != obj)
			{
				if(!obj.containsKey("datas"))
				{
					//无基本信息-统计数量 存储到集合errorList
					if(obj.containsKey("compname"))
					{
						errorList.add(obj.getString("compname"));
						System.out.println("【无基本信息】");
					}
					return null;
				}
				jsobj = obj.getJSONObject("datas");
			}
			String companyId = null;
			if(!jsobj.containsKey("基本信息"))
			{
				if(!jsobj.containsKey("企业基本信息"))
				{
					//无基本信息-统计数量 存储到集合errorList
					if(obj.containsKey("compname"))
					{
						errorList.add(obj.getString("compname"));
						System.out.println("【无基本信息】");
					}
					return null;
				}
			}
			JSONObject baseObj = null;
			if(jsobj.containsKey("基本信息"))
			{
				JSONArray array = jsobj.getJSONArray("基本信息");
				if(null!=array&&array.size()>0)
				{
					baseObj = array.getJSONObject(0);
				}
				else
				{
					//无基本信息-统计数量 存储到集合errorList
					if(obj.containsKey("compname"))
					{
						errorList.add(obj.getString("compname"));
						System.out.println("【无基本信息】");
					}
					return null;
				}
			}
			else if(jsobj.containsKey("企业基本信息"))
			{
				JSONArray array = jsobj.getJSONArray("企业基本信息");
				if(null!=array&&array.size()>0)
				{
					baseObj = array.getJSONObject(0);
				}
				else
				{
					//无基本信息-统计数量 存储到集合errorList
					if(obj.containsKey("compname"))
					{
						errorList.add(obj.getString("compname"));
						System.out.println("【无基本信息】");
					}
					return null;
				}
			}
			if(!baseObj.containsKey("注册号"))
			{
				if(!baseObj.containsKey("统一社会信用代码/注册号"))
				{
					if(!baseObj.containsKey("注册号/统一社会信用代码"))
					{
						if(!baseObj.containsKey("营业执照注册号统一社会信用代码"))
						{
							//无基本信息-统计数量 存储到集合errorList
							if(obj.containsKey("compname"))
							{
								errorList.add(obj.getString("compname"));
								System.out.println("【无基本信息】");
							}
							return null;
						}
					}
				}
			}
			String num = null;
			if(baseObj.containsKey("注册号"))
			{
				num = baseObj.getString("注册号");
			}
			else if(baseObj.containsKey("统一社会信用代码/注册号"))
			{
				num = baseObj.getString("统一社会信用代码/注册号");
			}
			else if(baseObj.containsKey("注册号/统一社会信用代码"))
			{
				num = baseObj.getString("注册号/统一社会信用代码");
			}
			else if(baseObj.containsKey("营业执照注册号统一社会信用代码"))
			{
				num = baseObj.getString("营业执照注册号统一社会信用代码");
			}
			if(null != num && 0 != num.hashCode())
			{
				companyId = nbg.generate(num).toString();
			} 
			//如果公司ID都没有，没有必要走下去了
			if(null == companyId)
			{
				//无基本信息-统计数量 存储到集合errorList
				if(obj.containsKey("compname"))
				{
					errorList.add(obj.getString("compname"));
				}
				return null;
			}
			//判断是否存在该公司，避免去重			
			if(baseObj.containsKey("名称"))
			{
				String companyName = baseObj.getString("名称");
				//重复
				if(!checkDump(companyName))
				{
					chongfu += 1;
					log.info("重复"+chongfu+"---文件名"+file.getPath());
					return null;
				}
				return companyName;
			}
			else if(baseObj.containsKey("企业名称"))
			{
				String companyName = baseObj.getString("企业名称");
				//如果为假，则是重复
				if(!checkDump(companyName))
				{
					chongfu += 1;
					log.info("重复"+chongfu+"---文件名"+file.getPath());
					return null;
				}
				return companyName;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			//log.error(file.getName()+":"+e1.getMessage());
		}
		finally{
			try {
				if(null!=fr)
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fr = null;
			sb=null;
		}	    
        return name;  
	}	
	/**
	 * 检查企业是否重复
	 * @param tableName
	 * @param companyName
	 * @return
	 * @throws SQLException 
	 */
	public boolean checkDump(String companyName) throws SQLException
	{
		PreparedStatement ppst = CONN2.prepareStatement("SELECT COUNT(*) FROM "+tableName+" WHERE name=?");
		ppst.setString(1, companyName);
		//ppst.setString(2, companyName);
		ResultSet set = ppst.executeQuery();
		int itemCount = 0;
		while(set.next())
		{
			itemCount = set.getInt(1);
		}
		set.close();
		ppst.close();
		if(itemCount>0)
		{
			return false;//重复
		}
		return true;
	}
	/**
	 * 写无注册号、无基本信息的数据
	 * @throws SQLException 
	 */
	public void insertErrorData() throws SQLException
	{
		if(null!= errorList && errorList.size()>0)
		{
			CONN.setAutoCommit(false);
			PreparedStatement ppst = CONN.prepareStatement("INSERT INTO "+tableName+"(NAME) VALUES(?)");
			for(String name:errorList)
			{
				ppst.setString(1, name);
				ppst.addBatch();
			}
			ppst.executeBatch();
			CONN.commit();
			ppst.close();
			errorList = null;
			errorList = new ArrayList<String>();
		}
	}
}
 