package cn.com.szgao.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.couchbase.client.java.Bucket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.couchbase.client.java.Cluster;

import cn.com.szgao.dto.CodeIdVO;
import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.PrCiCouVO;

/**
 * 处理省市县数据
 * @author dell
 *
 */
public class PrCiCouText {
	 static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	 static String usr = "postgres";
	 static String psd = "615601.xcy*";
	 static String SQL_PR="SELECT admini_code,name FROM sprovince_t WHERE admini_code=?";
	 static String SQL_CI="SELECT admini_code,name,parent_admini_code FROM scity_t WHERE admini_code=?";
	 static String SQL_COU="SELECT admini_code,name,parent_admini_code FROM scountry_t WHERE admini_code=?";
	 static String SQL_PR2="SELECT admini_code,name FROM sprovince_t";
	 static String SQL_CI2="SELECT admini_code,name,parent_admini_code FROM scity_t ";
	 static String SQL_COU2="SELECT admini_code,name,parent_admini_code FROM scountry_t";
	 public static Connection connection=null;
	//集群对象
	static Cluster cluster=null;
	static Bucket bucket = null;	
	static Logger logger = LogManager.getLogger(PrCiCouText.class.getName());
	//存储省
	static Map<String,CodeIdVO> MAPPR=new HashMap<String,CodeIdVO>();
	//市
	static Map<String,CodeIdVO> MAPCI=new HashMap<String,CodeIdVO>();
	//县
	static Map<String,CodeIdVO> MAPCO=new HashMap<String,CodeIdVO>();
	public static void init(){
		//省
		initPr(SQL_PR2,1);
		//市
		initPr(SQL_CI2,2);
		//县
		initPr(SQL_COU2,3);
	}
	public static void initPr(String sql,int status){
		PreparedStatement stmt = null;
		ResultSet reut=null;
		try {			
			stmt=connection.prepareStatement(sql);
			reut=stmt.executeQuery();
			while(reut.next()){
				//省
				if(status==1){
					 MAPPR.put(reut.getString(1), new CodeIdVO(reut.getString(1),null,reut.getString(2)));
				}
				//市
				else if(status==2){
					MAPCI.put(reut.getString(1), new CodeIdVO(reut.getString(1),reut.getString(3),reut.getString(2)));
				}
				//县
				else if(status==3){
					MAPCO.put(reut.getString(1), new CodeIdVO(reut.getString(1),reut.getString(3),reut.getString(2)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("prcicout:"+e);
		}
	}
	
	/**
	 * 处理注册码
	 * @param value
	 * @return
	 */
	static Pattern pattern = Pattern.compile("[0-9a-zA-Z]*");
	public static List<CodeVO> getregNumList(String value){
		List<CodeVO> list=new ArrayList<CodeVO>();
		String code=value.replace("&nbsp;", "");
		if(code.length()==15||code.length()==13){
			//注册号
			list.add(new CodeVO(value,1));
			return list;
		}
		else if(code.length()==18){
			//信用代码
			list.add(new CodeVO(value,2));
			return list;
		}
		else{
			//可能存在问题，可能是注册号与信用代码中间存在特殊字符
			return getCodes(value);
		}
	}	
	//拆分注册码信用代码
	public static List<CodeVO> getCodes(String value){
		 Matcher matcher=null;
		 StringBuffer sb=new StringBuffer();
		 String[] a=value.split("");
		 for(String c:a){
			 matcher = pattern.matcher(c);
			 if(matcher.matches()){
				 sb.append(c);
			 }else{
				 sb.append(",");
			 }
		 }
		 a=sb.toString().split(",");
		 List<CodeVO> list=new ArrayList<CodeVO>();
		 for(String c:a)
		 {
			 if(null!=c&&!"".equals(c))
			 {
				 if(c.length()==15||c.length()==13)
				 {
					//注册号
				    list.add(new CodeVO(c,1));
				 }
				 else if(c.length()==18)
				 {
					//信用代码
					list.add(new CodeVO(c,2)); 
				 }
			 }
		 }
		 return list;
	}
	/**
	 * 根据注册号或信用代码获取省市县
	 */
	public static CodeIdVO prcicout(String value,String sql,int status){
		PreparedStatement stmt = null;
		ResultSet reut=null;
		try {			
			stmt=connection.prepareStatement(sql);
			stmt.setString(1,value);
			reut=stmt.executeQuery();
			while(reut.next()){
				//省
				if(status==1){
					return new CodeIdVO(reut.getString(1),null,reut.getString(2));
				}
				//市,县
				else if(status==2){
					return new CodeIdVO(reut.getString(1),reut.getString(3),reut.getString(2));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("prcicout:"+e);
		}
		return null;
	}
	//拆分注册号与信用代码
	public static void getCreditRgeNum(List<CodeVO> code,EnterpriseVO enter){
		//清空以有数据
		enter.setRegNum(null);
		enter.setCreditCode(null);
		for(CodeVO c:code){
			if(c.getStatus()==1){
				//注册号
				enter.setRegNum(c.getCode());
			}else{
			    //信用代码
				enter.setCreditCode(c.getCode());
			}
		}
	}
	public static PrCiCouVO prcicouName(String code){
		PrCiCouVO pr=null;
		//查询省
		//CodeIdVO codevo=prcicout(code,SQL_PR,1);
		CodeIdVO codevo=MAPPR.get(code);
		if(null!=codevo){
			pr=new PrCiCouVO();
			pr.setProvince(codevo.getName());
			return pr;
		}
		//查询市
		//codevo=prcicout(code,SQL_CI,2);
		codevo=MAPCI.get(code);
		if(null!=codevo){
			pr=new PrCiCouVO();
			pr.setCity(codevo.getName());
			//查询省
			//codevo=prcicout(codevo.getParent_admini_code(),SQL_PR,1);
			codevo=MAPPR.get(codevo.getParent_admini_code());
			if(null!=codevo){
				pr.setProvince(codevo.getName());
			}
			return pr;
		}
		//查询县
		//codevo=prcicout(code,SQL_COU,2);
		codevo=MAPCO.get(code);
		if(null!=codevo){
			pr=new PrCiCouVO();
			pr.setCountry(codevo.getName());
			//查询市
			//codevo=prcicout(codevo.getParent_admini_code(),SQL_CI,2);
			codevo=MAPCI.get(codevo.getParent_admini_code());
			if(null!=codevo){
				pr.setCity(codevo.getName());
				//查询省
				//codevo=prcicout(codevo.getParent_admini_code(),SQL_PR,1);
				codevo=MAPPR.get(codevo.getParent_admini_code());
				if(null!=codevo){
					pr.setProvince(codevo.getName());
				}
			}
			else{
				//查询省
				codevo=MAPCO.get(code);
				codevo=MAPPR.get(codevo.getParent_admini_code());
				if(null!=codevo){
					pr.setProvince(codevo.getName());
				}
			}
			return pr;
		}
		//如果没找到，就取注册号前2位+四个零
		if(null == pr)
		{
			String provinceCode = code.substring(0,2)+"0000";
			CodeIdVO codeVO=MAPPR.get(provinceCode);
			if(null!=codeVO)
			{
				pr=new PrCiCouVO();
				pr.setProvince(codeVO.getName());
			}
			provinceCode = code.substring(0,4)+"00";
			codeVO=MAPCI.get(provinceCode);
			if(null!=codeVO && null != pr)
			{
				pr.setCity(codeVO.getName());				
				return pr;
			}
		}
		return null;
	}
}
