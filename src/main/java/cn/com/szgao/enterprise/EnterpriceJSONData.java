/*package cn.com.szgao.enterprise;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import cn.com.szgao.dto.ChangeVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.HolderDetailVO;
import cn.com.szgao.dto.HolderVO;
import cn.com.szgao.dto.MainManagerVO;
import cn.com.szgao.dto.RemarkVO;
import cn.com.szgao.dto.TempMainManagerVO;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

*//**
 * 工商JSON文件数据写入对应的桶中
 * @author xiongchangyi
 *
 *//*
public class EnterpriceJSONData {
	*//**
	 * json与对象互转
	 *//*
	static Gson gs = new Gson();	
	*//**
	 * 工具对象
	 *//*
	static DataUtils utils = new DataUtils();
	//集群对象
	static Cluster cluster = CouchbaseCluster.create("192.168.0.252");
	*//**
	 * 经营异常信息Bucket=etp_abnormal_e
	 *//*
	static Bucket bucket_abnormal = cluster.openBucket("etp_abnormal_e");	
	*//**
	 * 分支机构Bucket=etp_branch_e
	 *//*
	static Bucket bucket_branch = cluster.openBucket("etp_branch_e");
	*//**
	 * 抽查检查信息Bucket=etp_check_e
	 *//*
	static Bucket bucket_check = cluster.openBucket("etp_check_e");
	*//**
	 * 变更事项Bucket=etp_event_item_e
	 *//*
	static Bucket bucket_change = cluster.openBucket("etp_event_item_e");
	*//**
	 * 股东详情Bucket=etp_holder_det_e
	 *//*
	static Bucket bucket_det = cluster.openBucket("etp_holder_det_e");
	*//**
	 * 股东信息Bucket=etp_holder_e
	 *//*
	static Bucket bucket_holder = cluster.openBucket("etp_holder_e");
	*//**
	 * 违法信息Bucket=etp_illelnfo_e
	 *//*
	static Bucket bucket_illel = cluster.openBucket("etp_illelnfo_e");
	*//**
	 * 主要人员Bucket=etp_mhold_e
	 *//*
	static Bucket bucket_mhold = cluster.openBucket("etp_mhold_e");
	*//**
	 * 动产抵押登记信息Bucket=etp_mortgage_e
	 *//*
	static Bucket bucket_mort = cluster.openBucket("etp_mortgage_e");
	*//**
	 * 行政处罚信息Bucket=etp_punish_e
	 *//*
	static Bucket bucket_punish = cluster.openBucket("etp_punish_e");
	*//**
	 * 股权出质登记信息Bucket=etp_remise_e
	 *//*
	static Bucket bucket_remise = cluster.openBucket("etp_remise_e");
	*//**
	 * 清算信息Bucket=etp_settlement_e
	 *//*
	static Bucket bucket_settlement = cluster.openBucket("etp_settlement_e");
	*//**
	 * 公司基本信息Bucket=etp_settlement_e
	 *//*
	static Bucket bucket_etp = cluster.openBucket("etp_t");	
	//日志对象
	private static Logger log = LogManager.getLogger(EnterpriceJSONData.class);	
	*//**
	 * 查询省、市、县、区等的行政区号
	 *//*
	public static void initData(){
		//加载县名称和行政区划、市的ID
		utils.listCountryCityIdAdminCode();
		//加载市名称和行政区划、省的ID
		utils.listCityAdminCodeProvinceId();
		//加载省名称和行政区划
		utils.listProvinceAdminCode();
	}
	public static void main(String[] args) {
		PropertyConfigurator.configure("D:\\temp_workSpace1\\WorkSpace_Eclipse\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		//初始化数据到集合
		initData();
		File directFiles = new File("E:\\Temp_File\\2015-9-15\\已获取数据\\上海");		
		//数据文件数组
		File[] files = directFiles.listFiles();
		if(null != files)
		{
			for(int i = 0;i < files.length;i++)
			{				
				//读取文件里面的内容的方法
				try {
					readDataIntoDataBase(files[i]);
					files[i].delete();
				} catch (Exception e) {
					log.info(files[i]+"\n"+e);
				}
			}			
		}
	}
	*//**
	 * 写库
	 * @param file
	 * @throws Exception
	 *//*
	@SuppressWarnings({ "unchecked" })
	public static void readDataIntoDataBase(File file) throws Exception
	{
		System.out.println(file.getName());
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
		JSONObject json= JSONObject.fromString(sb.toString());	
		sb = null;//释放对象
		JSONObject datas = json.getJSONObject("datas");
		Iterator<Object> iter =datas.keys();
		String companyId = UUID.randomUUID().toString();
		while(iter.hasNext())
		{
			String key = iter.next().toString();
			if("基本信息".equals(key)||"企业基本信息".equals(key))
			{
				JSONArray baseArray = datas.getJSONArray("基本信息");		
				Object baseObj = baseArray.get(0);	
				//基本信息
				if(null != baseObj)
				{
					insertBaseInfo(baseObj,companyId);					
				}
				baseObj = null;
				baseArray = null;
			}
			//股东信息   别名：股东信息（后面有备注）、主管部门（出资人）信息、投资人信息、出资人信息、合伙人信息
			else if(key.startsWith("股东信息"))
			{
				//拿key去提取，主要是因为股东信息后面有多余的备注
				JSONArray holderArray = datas.getJSONArray(key);	
				if(null != holderArray)
				{
					insertHolder(holderArray,"股东信息",companyId);
				}
				holderArray = null;
			}
			else if("主管部门（出资人）信息".equals(key))
			{
				//拿key去提取，主要是因为股东信息后面有多余的备注
				JSONArray holderArray = datas.getJSONArray(key);	
				if(null != holderArray)
				{
					insertHolder(holderArray,key,companyId);
				}
				holderArray = null;
			}
			else if("投资人信息".equals(key))
			{
				//拿key去提取，主要是因为股东信息后面有多余的备注
				JSONArray holderArray = datas.getJSONArray(key);	
				if(null != holderArray)
				{
					insertHolder(holderArray,key,companyId);
				}
				holderArray = null;
			}
			else if("合伙人信息".equals(key))
			{
				//拿key去提取，主要是因为股东信息后面有多余的备注
				JSONArray holderArray = datas.getJSONArray(key);	
				if(null != holderArray)
				{
					insertHolder(holderArray,key,companyId);
				}
				holderArray = null;
			}
			//股东详情信息	别名：股东及出资信息、股东出资信息
			else if("股东详情信息".equals(key))
			{
				//拿key去提取，主要是因为股东信息后面有多余的备注
				JSONArray holderDetArray = datas.getJSONArray(key);	
				if(null != holderDetArray)
				{
					insertHolderDetail(holderDetArray,key,companyId);
				}
				holderDetArray = null;
			}
			else if("股东及出资信息".equals(key))
			{
				//拿key去提取，主要是因为股东信息后面有多余的备注
				JSONArray holderDetArray = datas.getJSONArray(key);	
				if(null != holderDetArray)
				{
					insertHolderDetail(holderDetArray,key,companyId);
				}
				holderDetArray = null;
			}
			else if("股东出资信息".equals(key))
			{
				//拿key去提取，主要是因为股东信息后面有多余的备注
				JSONArray holderDetArray = datas.getJSONArray(key);	
				if(null != holderDetArray)
				{
					insertHolderDetail(holderDetArray,key,companyId);
				}
				holderDetArray = null;
			}			
			//主要人员信息   别名：主要人员及信息、参加经营的家庭成员姓名
			else if("主要人员信息".equals(key))
			{				
				JSONArray mholderArray = datas.getJSONArray(key);	
				if(null != mholderArray)
				{
					insertMholder(mholderArray,key,companyId);
				}
				mholderArray = null;
			}
			else if("主要人员及信息".equals(key))
			{				
				JSONArray mholderArray = datas.getJSONArray(key);	
				if(null != mholderArray)
				{
					insertMholder(mholderArray,key,companyId);
				}
				mholderArray = null;
			}
			else if("参加经营的家庭成员姓名".equals(key))
			{				
				JSONArray mholderArray = datas.getJSONArray(key);	
				if(null != mholderArray)
				{
					insertMholder(mholderArray,key,companyId);
				}
				mholderArray = null;
			}
			//变更信息
			else if("变更信息".equals(key))
			{
				JSONArray changeArray = datas.getJSONArray(key);	
				if(null != changeArray)
				{
					insertChange(changeArray,companyId,datas);
				}
			}
			else
			{
				log.info(datas.getJSONArray(key));
			}
			//分支机构信息
			
			//抽查检查信息
			
			//股权出质登记信息
			
			//经营异常信息		
			
			//严重违法信息
			
			//行政处罚信息		
			
			//动产抵押登记信息		
			
			//清算信息	
			
		}//while
	}
	*//**
	 * 处理公司基本信息
	 * @param baseObj 处理的数据
	 * @throws ParseException 
	 *//*
	public static void insertBaseInfo(Object baseObj,String companyId) throws ParseException
	{
		System.out.println("+++++++++++++++++++++++基本信息+++++++++++++++++++");
		ObjectMapper mapper = new ObjectMapper();  	      
		JsonNode node = null;
		//key的遍历器
		Iterator keyIter = null;
		//企业对象
		EnterpriseVO enterVO = new EnterpriseVO();;
		//字段与页面标签映射对象、表名与标签
		RemarkVO remarkVO = new RemarkVO();
		remarkVO.setBucketName("基本信息");
		//对象的json对象
		JsonObject jsonObject = null;
		JsonDocument doc = null;
		try {
			node = mapper.readTree(baseObj.toString());
			keyIter = node.getFieldNames();			
			while(keyIter.hasNext())
			{
				String key = keyIter.next().toString();				
				String value = node.get(key).getTextValue();
				if("注册号".equals(key))
				{
					enterVO.setRegNum(value);
					//获得省市县
					String pccityArray[] = listCountryCityProvince(value);
					if(null != pccityArray[0])
					{
						enterVO.setProvince(pccityArray[0]);//省
					}
					if(null != pccityArray[1])
					{
						enterVO.setCity(pccityArray[1]);//市
					}
					if(null != pccityArray[2])
					{
						enterVO.setArea(pccityArray[2]);//县
					}
				}
				else if("名称".equals(key))
				{
					enterVO.setCompany(value);
				}
				else if("类型".equals(key))
				{
					enterVO.setType(value);
				}
				else if("法定代表人".equals(key)||"负责人".equals(key)||"投资人".equals(key)
						||"经营者".equals(key)||"执行事务合伙人".equals(key)||"股东".equals(key))
				{
					remarkVO.setLegalRep(key);
					enterVO.setLegalRep(value);					
				}			
				else if("住所".equals(key)||"营业场所".equals(key)||"主要经营场所".equals(key))
				{
					if(null != value)
					{
						enterVO.setLocation(value.replace(" ", ""));
					}
				}
				else if("注册资本".equals(key)||"成员出资总额".equals(key))
				{
					remarkVO.setRegCapital(key);
					enterVO.setRegCapital(value);
				}
				else if("成立日期".equals(key)||"注册日期".equals(key))
				{
					enterVO.setRegDate(formatDate(value));
				}
				else if("经营期限自".equals(key)||"营业期限自".equals(key)||"合伙期限自".equals(key))
				{
					remarkVO.setStartTime(key);
					enterVO.setStartTime(formatDate(value));
				}
				else if("经营期限至".equals(key)||"营业期限至".equals(key)||"合伙期限至".equals(key))
				{
					remarkVO.setEndTime(key);
					enterVO.setEndTime(formatDate(value));
				}
				else if("经营范围".equals(key)||"业务范围".equals(key))
				{
					enterVO.setScope(value);
				}
				else if("登记机关".equals(key))
				{
					enterVO.setRegOffice(value);
				}
				else if("核准日期".equals(key))
				{
					enterVO.setApproveDate(formatDate(value));
				}
				else if("登记状态".equals(key)||"经营状态".equals(key))
				{
					enterVO.setRegState(value);
				}
				else if("吊销日期".equals(key))
				{
					enterVO.setRevokeDate(formatDate(value));
				}
				else if("组成形式".equals(key))
				{
					enterVO.setComposition(value);
				}							
			}
			//设置备注对象
			enterVO.setRemark(remarkVO);
			jsonObject = JsonObject.fromJson(gs.toJson(enterVO));
			enterVO = null;
			doc = JsonDocument.create(companyId, jsonObject);
			bucket_etp.upsert(doc);
			jsonObject = null;
			doc = null;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			//log.info(e);
		} catch (IOException e) {
			e.printStackTrace();
			//log.info(e);
		}	
		
		mapper = null;	
		node = null;
	}
	*//**
	 * 处理股东信息
	 * @param holderArray 处理的数据
	 *//*
	public static void insertHolder(JSONArray holderArray,String bucketName,String companyId)
	{
		System.out.println("+++++++++++++++++++++++股东信息+++++++++++++++++++");
		if(!holderArray.isEmpty())
		{
			ObjectMapper mapper = new ObjectMapper();
			//遍历股东信息
			Iterator iter = holderArray.iterator();
			JsonNode node = null;
			//股东信息对象
			Object holderObject = null;
			//遍历股东信息对象
			Iterator<String> keyIter = null;
			HolderVO holderVO = null;
			RemarkVO remarkVO = null;
			//对象的json对象
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			try {
				while(iter.hasNext())
				{	
					holderVO = new HolderVO();	
					remarkVO = new RemarkVO();
					remarkVO.setBucketName(bucketName);
					holderVO.setCompanyId(companyId);
					holderObject = iter.next();	
					node = mapper.readTree(holderObject.toString());					
					keyIter = node.getFieldNames();
					while(keyIter.hasNext())
					{
						String key = keyIter.next().toString();
						String value = node.get(key).getTextValue();
						//判断是哪个字段
						if("股东".equals(key)||"姓名".equals(key)||"合伙人".equals(key)
								||"发起人".equals(key)||"出资人".equals(key))
						{
							remarkVO.setHolder(key);
							holderVO.setHolder(value);
						}
						//合伙人类型  	别名：股东类型、出资人类型、发起人类型		
						else if("股东类型".equals(key)||"出资人类型".equals(key)||"合伙人类型".equals(key)
								||"发起人类型".equals(key))
						{
							remarkVO.setType(key);
							holderVO.setType(value);
						}
						// 证照/证件类型   别名：证照证件类型
						else if("证照/证件类型".equals(key)||"证照证件类型".equals(key))
						{
							holderVO.setLicenseType(value);
						}	
						// 证照/证件号码	别名：证照证件号码
						else if("证照/证件号码".equals(key)||"证照证件号码".equals(key))
						{
							holderVO.setLicenseNum(value);
						}
						//equityPart 出资方式
						else if("出资方式".equals(key) || "投资方式".equals(key))
						{
							holderVO.setEquityPart(value);
						}
					}
					holderVO.setRemark(remarkVO);					
					remarkVO = null;
					jsonObject = JsonObject.fromJson(gs.toJson(holderVO));
					holderVO = null;
					doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
					bucket_holder.upsert(doc);		
					jsonObject = null;
					doc = null;
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				//log.info(e);
			} catch (IOException e) {
				e.printStackTrace();
				//log.info(e);
			}	
			finally{
				mapper = null;
				iter = null;
				node = null;
				holderObject = null;
				keyIter = null;
			}
		}
		
	}
	*//**
	 * 处理股东详细信息
	 * @param holderDetArray 处理的数据
	 * @throws ParseException 
	 *//*
	public static void insertHolderDetail(JSONArray holderDetArray,String bucketName,String holderId) throws ParseException
	{
		//股东详情信息	别名：股东及出资信息、股东出资信息、股东信息
		if(!holderDetArray.isEmpty())
		{
			ObjectMapper mapper = new ObjectMapper();
			//遍历股东详细信息
			Iterator iter = holderDetArray.iterator();
			JsonNode node = null;
			//股东详细信息对象
			Object holderDetObject = null;
			//遍历股东详细信息对象
			Iterator<String> keyIter = null;
			HolderDetailVO holderDetVO = null;
			RemarkVO remarkVO = null;
			//对象的json对象
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			try {
				while(iter.hasNext())
				{
					holderDetVO = new HolderDetailVO();
					holderDetVO.setHolderId(holderId);
					remarkVO = new RemarkVO();
					//前台显示的名称，映射桶的中文含义
					remarkVO.setBucketName(bucketName);
					holderDetObject = iter.next();					
					node = mapper.readTree(holderDetObject.toString());
					keyIter = node.getFieldNames();
					while(keyIter.hasNext())
					{
						String key = keyIter.next().toString();
						String value = node.get(key).getTextValue();
						//股东名称	别名：股东、发起人、投资人名称
						if("股东名称".equals(key)||"姓名".equals(key)||"投资人名称".equals(key)
								||"发起人".equals(key)||"股东".equals(key))
						{
							remarkVO.setHolder(key);
							holderDetVO.setHolder(value);
						}
						//投资人类型
						else if("投资人类型".equals(key))
						{
							holderDetVO.setType(value);
						}
						// 认缴出资额	        别名：认缴出资额(万元)
						else if("认缴出资额".equals(key)||"认缴出资额(万元)".equals(key)
								||"认缴出资额（万元）".equals(key))
						{
							holderDetVO.setSubcriCapital(value);
						}
						// 认缴出资方式	别名：出资方式、认缴明细
						else if("认缴出资方式".equals(key)||"出资方式".equals(key)
								||"认缴明细".equals(key))
						{
							holderDetVO.setConMethod(value);
						}
						//认缴出资时间	别名：认缴出资日期
						else if("认缴出资时间".equals(key)||"认缴出资日期".equals(key))
						{
							
							holderDetVO.setConsidDate(formatDate(value));
							
						}
						//实缴出资额	        别名：实缴出资额(万元)、实缴明细
						else if("实缴出资额".equals(key)||"实缴出资额(万元)".equals(key)
								||"实缴明细".equals(key)||"实缴出资额（万元）".equals(key))
						{
							holderDetVO.setActualCapital(value);
						}
						// 实缴出方式	        别名：实缴出资方式、出资方式6
						else if("实缴出方式".equals(key)||"实缴出资方式".equals(key)
								||"出资方式6".equals(key))
						{
							holderDetVO.setFactMethod(value);
						}
						// 实缴出时间	      别名：实缴出资日期、实缴出资时间
						else if("实缴出时间".equals(key)||"实缴出资日期".equals(key)
								||"实缴出资时间".equals(key))
						{
							holderDetVO.setActualDate(formatDate(value));
						}
						// 认缴额(万元）
						else if("认缴额(万元)".equals(key)||"认缴额（万元）".equals(key))
						{
							holderDetVO.setConCapital(value);
						}
					    // 实缴额(万元）
						else if("实缴额(万元)".equals(key)||"实缴额（万元）".equals(key))
						{
							holderDetVO.setFactCapital(value);
						}
					}
					holderDetVO.setRemark(remarkVO);
					remarkVO = null;
					//写库
					jsonObject = JsonObject.fromJson(gs.toJson(holderDetVO));
					holderDetVO = null;
					doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
					bucket_det.upsert(doc);
					jsonObject = null;
					doc = null;
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				mapper = null;
				iter = null;
				node = null;
				holderDetObject = null;
				keyIter = null;
			}
		}
	}
	*//**
	 * 处理主要人员信息
	 * @param mholderArray 处理的数据
	 *//*
	public static void insertMholder(JSONArray mholderArray,String bucketName,String companyId)
	{
		System.out.println("+++++++++++++++++++++++主要人员信息+++++++++++++++++++");
		//主要人员信息   别名：主要人员及信息、参加经营的家庭成员姓名
		if(!mholderArray.isEmpty())
		{
			ObjectMapper mapper = new ObjectMapper();
			//遍历主要人员信息
			Iterator iter = mholderArray.iterator();
			JsonNode node = null;
			//主要人员信息对象
			Object mholderObject = null;
			//遍历主要人员信息对象
			Iterator<String> keyIter = null;
			MainManagerVO mainVO = null;
			RemarkVO remarkVO = null;
			//主要人员信息的临时实体对象
			TempMainManagerVO tempVO = null;
			//对象的json对象
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			try {
				while(iter.hasNext())
				{
					//主要人员信息的临时实体对象
					tempVO = new TempMainManagerVO();
					tempVO.setCompanyId(companyId);
					remarkVO = new RemarkVO();
					remarkVO.setBucketName(bucketName);
					tempVO.setRemarkVO(remarkVO);
					remarkVO = null;
					mholderObject = iter.next();
					node = mapper.readTree(mholderObject.toString());
					keyIter = node.getFieldNames();
					while(keyIter.hasNext())
					{
						String key = keyIter.next().toString();
						String value = node.get(key).getTextValue();
						//姓名 	别名：姓名4
						if("姓名".equals(key))
						{
							if("".equals(value)||null == value||"null".equals(value)
									||" ".equals(value))
							{
								continue;
							}
							tempVO.setManagerName1(value);
						}
						else if("姓名4".equals(key))
						{
							if("".equals(value)||null == value||"null".equals(value)
									||" ".equals(value))
							{
								continue;
							}
							tempVO.setManagerName2(value);
						}
						else if("姓名7".equals(key))
						{
							if("".equals(value)||null == value||"null".equals(value)
									||" ".equals(value))
							{
								continue;
							}
							tempVO.setManagerName3(value);
						}
						else if("职务".equals(key))
						{
							if("".equals(value)||null == value||"null".equals(value)
									||" ".equals(value))
							{
								continue;
							}
							tempVO.setPosition1(value);
						}
						else if("职务5".equals(key))
						{
							if("".equals(value)||null == value||"null".equals(value)
									||" ".equals(value))
							{
								continue;
							}
							tempVO.setPosition2(value);
						}
						else if("职务8".equals(key))
						{
							if("".equals(value)||null == value||"null".equals(value)
									||" ".equals(value))
							{
								continue;
							}
							tempVO.setPosition3(value);
						}
					}//遍历json对象里面的所有key
					//写库	
					if(null != tempVO.getManagerName1() && null != tempVO.getPosition1())
					{
						mainVO = new MainManagerVO();
						mainVO.setRemark(tempVO.getRemarkVO());
						mainVO.setCompanyId(tempVO.getCompanyId());
						mainVO.setManagerName(tempVO.getManagerName1());
						mainVO.setPosition(tempVO.getPosition1());
						jsonObject = JsonObject.fromJson(gs.toJson(mainVO));
						mainVO = null;
						doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
						bucket_mhold.upsert(doc);
						jsonObject = null;
						doc = null;
					}
					if(null != tempVO.getManagerName2() && null != tempVO.getPosition2())
					{
						mainVO = new MainManagerVO();
						mainVO.setRemark(tempVO.getRemarkVO());
						mainVO.setCompanyId(tempVO.getCompanyId());
						mainVO.setManagerName(tempVO.getManagerName2());
						mainVO.setPosition(tempVO.getPosition2());
						jsonObject = JsonObject.fromJson(gs.toJson(mainVO));
						mainVO = null;
						doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
						bucket_mhold.upsert(doc);
						jsonObject = null;
						doc = null;
					}
					if(null != tempVO.getManagerName3() && null != tempVO.getPosition3())
					{
						mainVO = new MainManagerVO();
						mainVO.setRemark(tempVO.getRemarkVO());
						mainVO.setCompanyId(tempVO.getCompanyId());
						mainVO.setManagerName(tempVO.getManagerName3());
						mainVO.setPosition(tempVO.getPosition3());
						jsonObject = JsonObject.fromJson(gs.toJson(mainVO));
						mainVO = null;
						doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
						bucket_mhold.upsert(doc);
						jsonObject = null;
						doc = null;
					}
				}
				tempVO = null;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				mapper = null;
				iter = null;
				node = null;
				mholderObject = null;
				keyIter = null;
			}
		}
		
	}
	*//**
	 * 处理变更信息
	 * 存在点击“详情” ，出现表格的形式
	 * @param changArray 处理的数据
	 * @throws ParseException 
	 *//*
	public static void insertChange(JSONArray changArray,String companyId,JSONObject datas) throws ParseException
	{
		if(!changArray.isEmpty())
		{
			ObjectMapper mapper = new ObjectMapper();
			//遍历主要人员信息
			Iterator iter = changArray.iterator();
			JsonNode node = null;
			//主要人员信息对象
			Object changeObject = null;
			//遍历主要人员信息对象
			Iterator<String> keyIter = null;
			RemarkVO remarkVO = null;
			ChangeVO changeVO = null;
			//对象的json对象
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			try {
				while(iter.hasNext())
				{
					remarkVO = new RemarkVO();
					remarkVO.setBucketName("变更信息");
					changeVO = new ChangeVO();
					changeVO.setCompanyId(companyId);
					changeVO.setRemark(remarkVO);
					changeObject = iter.next();
					node = mapper.readTree(changeObject.toString());
					keyIter = node.getFieldNames();						
					while(keyIter.hasNext())
					{
						String key = keyIter.next().toString();
						String value = node.get(key).getTextValue();
						if(null != value && !"暂无数据".equals(value) &&
								!"".equals(value) && !"null".equals(value)
								&& !"无".equals(value) && !"不公示".equals(value))
						{
							if("变更事项".equals(key))
							{
								changeVO.setChangeEvent(value);
							}
							else if("变更前内容".equals(key))
							{
								//当变更前信息里面的值为  详细  的时候
								if("详细".equals(value.trim()))
								{
									detailChange(changeVO,datas);
								}
								else
								{
									changeVO.setChangeBefore(value);
								}
							}
							else if("变更后内容".equals(key))
							{
								changeVO.setChangeAfter(value);
							}
							else if("变更日期".equals(key))
							{
								changeVO.setChangeDate(formatDate(value));
							}						
						}
					}
					//写库
					jsonObject = JsonObject.fromJson(gs.toJson(changeVO));
					doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
					bucket_change.upsert(doc);
					remarkVO = null;
					changeVO = null;
					jsonObject = null;
					doc = null;
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				mapper = null;
				iter = null;
				node = null;
				changeObject = null;
				keyIter = null;
			}
		}
	}
	*//**
	 * 变更信息为“详细”的情况
	 * @param changeVO 变更对象
	 * @param datas 所有数据的大集合
	 * @throws JsonProcessingException 
	 * @throws IOException
	 *//*
	public static void detailChange(ChangeVO changeVO,JSONObject datas) throws JsonProcessingException, IOException
	{
		//是详细的时候
		ObjectMapper mapper_beaf = new ObjectMapper();
		Iterator iters = datas.keys();
		//节点
		JsonNode befNode = null;
		//变更前后对象
		Object befObject = null;
		while(iters.hasNext())
		{
			String keys = iters.next().toString();
			if(keys.startsWith("变更前"))
			{
				//变更前有多种叫法
				JSONArray before = datas.getJSONArray(keys);										
				if(!before.isEmpty())
				{
					Iterator iter_obj = before.iterator();
					//这里只会遍历一次
					while(iter_obj.hasNext())
					{
						befObject = iter_obj.next();
						befNode = mapper_beaf.readTree(befObject.toString());
						//Key迭代器
						Iterator it = befNode.getFieldNames();
						//防止有（注：标有*标志的为法定代表人）的情况；前台从第2个分号开始分割
						String befContent = keys+";";												  
						while(it.hasNext())
						{
							String key_ = it.next().toString();
							if(key_.startsWith("序号"))
							{
								continue;
							}
							String value_ = befNode.get(key_).getTextValue();
							Pattern pattern = Pattern.compile("^\\D{1,}[1-9]$");
							Matcher m = pattern.matcher(key_);
							if(m.matches())
							{
								key_ = key_.substring(0, key_.length()-1);
							}
							pattern = null;
							m = null;
							befContent += key_ + ":"+ value_ + ";"; 
						}
						changeVO.setChangeBefore(befContent);
					}
				}
			}
			else if(keys.startsWith("变更后"))
			{
				JSONArray after = datas.getJSONArray(keys);
				if(!after.isEmpty())
				{
					Iterator iter_obj = after.iterator();
					//这里只会遍历一次
					while(iter_obj.hasNext())
					{
						befObject = iter_obj.next();
						befNode = mapper_beaf.readTree(befObject.toString());
						//Key迭代器
						Iterator it = befNode.getFieldNames();
						//防止有（注：标有*标志的为法定代表人）的情况；前台从第2个分号开始分割
						String afContent = keys+";";												  
						while(it.hasNext())
						{
							String key_ = it.next().toString();
							if(key_.startsWith("序号"))
							{
								continue;
							}
							String value_ = befNode.get(key_).getTextValue();
							Pattern pattern = Pattern.compile("^\\D{1,}[1-9]$");
							Matcher m = pattern.matcher(key_);
							if(m.matches())
							{
								key_ = key_.substring(0, key_.length()-1);
							}
							pattern = null;
							m = null;
							afContent += key_ + ":"+ value_ + ";"; 
						}
						changeVO.setChangeAfter(afContent);
					}
				}
			}
		}
		
	}
	*//**
	 * 处理股权出质登记信息
	 * @param json 处理的数据
	 *//*
	public static void insertRemise(String json)
	{
		//需要统计别名
	}
	
	*//**
	 * 处理分支机构信息
	 * @param json 处理的数据
	 *//*
	public static void insertBranck(JSONArray branchArray)
	{
		
	}
	*//**
	 * 处理抽查检查信息
	 * @param json 处理的数据
	 *//*
	public static void insertCheck(String json)
	{
		
	}
	*//**
	 * 处理经营异常信息
	 * @param json 处理的数据
	 *//*
	public static void insertAbnormal(String json)
	{
		
	}
	*//**
	 * 处理行政处罚信息
	 * @param json 处理的数据
	 *//*
	public static void insertPunish(String json)
	{
		
	}
	*//**
	 * 处理严重违法信息
	 * @param json 处理的数据
	 *//*
	public static void insertIllel(String json)
	{
		
	}
	*//**
	 * 处理动产抵押登记信息
	 * @param json 处理的数据
	 *//*
	public static void insertMortgage(String json)
	{
		
	}
	*//**
	 * 处理清算信息
	 * @param json 处理的数据
	 *//*
	public static void insertSettlement(String json)
	{
		
	}
	*//**
	 * 特殊字符替换成""
	 * @param temp 需要处理的字符串
	 * @return 处理后的字符串
	 *//*
	public static String doString3(String temp)
	{
		int index=-1;
		if(index==-1)
		{
			index = temp.indexOf("\t");	
			if(index != -1)
			{
				temp = temp.replace("\t", "");
			}
		}
		if(index==-1)
		{
			index = temp.indexOf(" ");	
			if(index != -1)
			{
				temp = temp.replace(" ", "");
			}
		}
		if(index==-1)
		{
			index = temp.indexOf("\n");	
			if(index != -1)
			{
				temp = temp.replace("\n", "");
			}
		}
		if(index ==-1)
		{
			index = temp.indexOf("、");
			if(index != -1)
			{
				temp = temp.replace("、", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("\"");
			if(index != -1)
			{
				temp = temp.replace("\"", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("#");
			if(index != -1)
			{
				temp = temp.replace("#", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("$");
			if(index != -1)
			{
				temp = temp.replace("$", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("!");
			if(index != -1)
			{
				temp = temp.replace("!", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("@");
			if(index != -1)
			{
				temp = temp.replace("@", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("%");
			if(index != -1)
			{
				temp = temp.replace("%", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("&");
			if(index != -1)
			{
				temp = temp.replace("&", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("+");
			if(index != -1)
			{
				temp = temp.replace("+", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("-");
			if(index != -1)
			{
				temp = temp.replace("-", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("_");
			if(index != -1)
			{
				temp = temp.replace("_", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("*");
			if(index != -1)
			{
				temp = temp.replace("*", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf(":");
			if(index != -1)
			{
				temp = temp.replace(":", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("?");
			if(index != -1)
			{
				temp = temp.replace("?", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf(";");
			if(index != -1)
			{
				temp = temp.replace(";", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf(",");
			if(index != -1)
			{
				temp = temp.replace(",", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("0");
			if(index != -1)
			{
				temp = temp.replace("0", "");
			}
		}
		if(index == -1)
		{
			index = temp.indexOf("/");
			if(index != -1)
			{
				temp = temp.replace("/", "");
			}
		}
		return temp;
	}
	*//**
	 * 特殊字符的索引
	 * @param temp 在此字符串找特殊字符
	 * @return 处理之后的字符串
	 *//*
	public static int doStringIndex(String temp)
	{
		int index=-1;
		if(index==-1)
		{
			index = temp.indexOf("\t");	
		}
		if(index==-1)
		{
			index = temp.indexOf(" ");	
		}
		if(index==-1)
		{
			index = temp.indexOf("\n");	
		}
		if(index ==-1)
		{
			index = temp.indexOf("、");
		}
		if(index == -1)
		{
			index = temp.indexOf("\"");
		}
		if(index == -1)
		{
			index = temp.indexOf("#");
		}
		if(index == -1)
		{
			index = temp.indexOf("$");
		}
		if(index == -1)
		{
			index = temp.indexOf("!");
		}
		if(index == -1)
		{
			index = temp.indexOf("@");
		}
		if(index == -1)
		{
			index = temp.indexOf("%");
		}
		if(index == -1)
		{
			index = temp.indexOf("&");
		}
		if(index == -1)
		{
			index = temp.indexOf("+");
		}
		if(index == -1)
		{
			index = temp.indexOf("-");
		}
		if(index == -1)
		{
			index = temp.indexOf("_");
		}
		if(index == -1)
		{
			index = temp.indexOf(",");
		}
		if(index == -1)
		{
			index = temp.indexOf("0");
		}
		if(index == -1)
		{
			index = temp.indexOf("/");
		}
		return index;
	}

	*//**
	 * 把字符串由全角转成半角
	 * @param doString 全角字符串
	 * @return 返回全角字符串对应的半角字符串
	 * @author xiongchangyi
	 * @since 2019-8-4
	 *//*
	public static String full2HalfChange(String doString)
	{
		if(null == doString)
		{
			return null;
		}
		StringBuffer outStrBuf = new StringBuffer("");
		 String Tstr = "";
		 byte[] b = null;
		 for (int i = 0; i < doString.length(); i++)
		 {
			 Tstr = doString.substring(i, i + 1);
			 // 全角空格转换成半角空格
			 if (Tstr.equals("　")) 
			 {
				 outStrBuf.append(" ");
				 continue;
			 }
			 try 
			 {
				 b = Tstr.getBytes("unicode");
				 // 得到 unicode 字节数据
				 if (b[2] == -1) 
				 {
							 // 表示全角
					b[3] = (byte) (b[3] + 32);
					b[2] = 0;
					outStrBuf.append(new String(b, "unicode"));
				 } 
				 else 
				 {
					 outStrBuf.append(Tstr);
				 }
			 } 
			 catch(UnsupportedEncodingException e) 
			 {
				 e.printStackTrace();
			 }
	 	} 
	 	return outStrBuf.toString();
	 }
	*//**
	 * 通过行政区划获得行政区
	 * @param adminiCode 行政区划
	 * @return 省市县
	 *//*
	public static String[] listCountryCityProvince(String adminiCode)
	{
		//0~2元素是省、市、县
		String []countryCityProvince = new String[3];
		if(null != adminiCode)
		{
			//截取注册号的行政区划
			adminiCode = adminiCode.substring(0, 6);
			//如果adminiCode是县的行政区划
			if(DataUtils.adminCountryMap.containsKey(adminiCode))
			{
				//县名称、地级市ID
				String array[] = DataUtils.adminCountryMap.get(adminiCode);
				countryCityProvince[2] = array[0];//县名称
				if(null != array[1])
				{
					int cityId = Integer.parseInt(array[1]);
					//直辖市的ID: '北京市','天津市','重庆市','上海市'
					if(cityId == 400||cityId == 401||cityId == 402||cityId == 403)
					{
						String provinceName = utils.listProvinceNameByProvinceId(cityId);
						if(null != provinceName)
						{
							countryCityProvince[0] = provinceName;//直辖市名称
							return countryCityProvince;
						}
					}
					//listProvinceNameByProvinceId
					Map<String,Integer> cityProvinceMap = utils.listCityProvinceIdByCityId(cityId);
					//市名称
					Set<String> cityNameSet = cityProvinceMap.keySet();
					Iterator<String> iter = cityNameSet.iterator();
					if(iter.hasNext())
					{
						countryCityProvince[1] = iter.next();//市名称
						cityNameSet = null;
						iter = null;
					}
					//省的ID
					Collection<Integer> conllection = cityProvinceMap.values();
					Iterator<Integer> iterator = conllection.iterator();
					if(iterator.hasNext())
					{
						String provinceName = utils.listProvinceNameByProvinceId(iterator.next());
						if(null != provinceName)
						{
							countryCityProvince[0] = provinceName;//省名称
							iterator = null;
							conllection = null;
						}
					}
				}
			}
			else if(DataUtils.adminCityMap.containsKey(adminiCode))
			{
				String array[] = DataUtils.adminCityMap.get(adminiCode);
				countryCityProvince[1] = array[0];//获得市名称
				int provinceId = Integer.parseInt(array[1]);//省ID
				String provinceName = utils.listProvinceNameByProvinceId(provinceId);
				if(null != provinceName)
				{
					countryCityProvince[0] = provinceName;//获得省名称
				}
			}
			else if(DataUtils.adminProvinceMap.containsKey(adminiCode))
			{
				countryCityProvince[0] = DataUtils.adminProvinceMap.get(adminiCode);//获得省名称
			}
		}
		return countryCityProvince;
	}
	*//**
	 * 格式化日期
	 * @param date
	 * @return
	 * @throws ParseException 
	 *//*
	public static String formatDate(String date) throws ParseException
	{
		String result = null;
		SimpleDateFormat sf = null;
		if(null != date)
		{
			//yyyy年MM月dd日 情况
			if(date.contains("年") && date.contains("月"))
			{
				result = date;
				return result;
			}
			String yyymmdd = "^[12]\\d{3}(0\\d|1[0-2])([0-2]\\d|3[01])$";//yyyyMMdd
			String yyyy_mm_dd = "^[0-9]{4}-[0-9]{1,}-[0-9]{1,}$";//yyyy-MM-dd
			String yyyy = "^\\d{4}\\/\\d{1,}\\/\\d{1,}$";//yyyy/MM/dd
			//yyyyMMdd 情况
			Pattern pattern = Pattern.compile(yyymmdd);
			Matcher matcher = pattern.matcher(date);
			if(matcher.matches())
			{
				sf = new SimpleDateFormat("yyyyMMdd");
				Date d = sf.parse(date);
				sf = null;
				sf = new SimpleDateFormat("yyyy年MM月dd日");
				result = sf.format(d);
				sf = null;
			}
			else
			{
				//yyyy-MM-dd 情况
				pattern = Pattern.compile(yyyy_mm_dd);
				matcher = pattern.matcher(date);
				if(matcher.matches())
				{
					sf = new SimpleDateFormat("yyyy-MM-dd");
					Date d = sf.parse(date);
					sf = null;
					sf = new SimpleDateFormat("yyyy年MM月dd日");
					result = sf.format(d);
					sf = null;
				}
				else
				{
					//  yyyy/MM/dd 情况
					pattern = Pattern.compile(yyyy);
					matcher = pattern.matcher(date);
					if(matcher.matches())
					{
						sf = new SimpleDateFormat("yyyy/MM/dd");
						Date d = sf.parse(date);
						sf = null;
						sf = new SimpleDateFormat("yyyy年MM月dd日");
						result = sf.format(d);
						sf = null;
					}
				}
			}
		}
		return result;
	}
}
*/