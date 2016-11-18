package cn.com.szgao.enterprise;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.ChangeVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.HolderDetailVO;
import cn.com.szgao.dto.HolderVO;
import cn.com.szgao.dto.MainManagerVO;
import cn.com.szgao.dto.RemarkVO;
import cn.com.szgao.dto.TempHolderVO;
import cn.com.szgao.dto.TempMainManagerVO;
import cn.com.szgao.wash.data.AdministrationUtils;
import cn.com.szgao.wash.data.DataUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 深圳
 * @author xiongchangyi
 *
 */
public class FileIntoDataBase2 {
	public FileIntoDataBase2(){}
	public FileIntoDataBase2(Logger log){
		this.log=log;
	}
	/**
	 * json与对象互转
	 */
	  Gson gs = new Gson();	
	/**
	 * 工具对象
	 */
	  DataUtils utils = new DataUtils();
	/**
	 * 工具类，通过住所、登记机关获得行政区
	 */
	 AdministrationUtils u = new AdministrationUtils();

	//日志对象
	private   Logger log ;
	//根据字符串生成UUID对象
	  NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	//股东数组
	  JSONArray tuziArray = null;
	//股东详情数组
	  JSONArray chuziArray = null;
	  List<String> beforeList = new ArrayList<String>();
	  List<String> afterList = new ArrayList<String>();
	/**
	 * 公司ID集合，
	 */
	 List<String> companyIdList = new ArrayList<String>(); 
	 int count = 0;
	 //无基本信息统计数据
	 private int basicSum = 0;
	 //无注册号统计数据
	 private int regSum = 0;	
	/**
	 * 循环调用是否是目录
	 * @param file
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public void show(File file) throws IOException, ParseException{
        System.out.println(file.getPath());
		if(file.isFile())
		{
			count += 1;
			System.out.println("数量:"+count+"---线程名"+Thread.currentThread().getName());
			try{
				readFileByLines(file);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			//file.delete();
			return;
		}
		File[] files=file.listFiles();
		if(null!=files)
		{
			for(File fi:files)
			{
				if(fi.isFile())
				{	
					count += 1;
					System.out.println("数量:"+count+"---线程名"+Thread.currentThread().getName());
					try{
						readFileByLines(fi);
					}catch(Exception e)
					{
						e.printStackTrace();
						log.info(e);
					}
					//fi.delete();
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
	@SuppressWarnings("rawtypes")
	private void readFileByLines(File file) throws Exception{		
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
		String doString = sb.toString();
		JSONObject obj = null;
		try
		{
			obj = JSONObject.fromObject(doString);
		}
		catch(Exception e)
		{
			log.info(e);
			return;
		}
		JSONObject jsobj = null;//datas
		if(null != obj)
		{
			if(!obj.containsKey("datas"))
			{
				regSum += 1;
				log.info("basicSum: "+basicSum+"---线程名"+file.getPath());				
				return;
			}
			jsobj = obj.getJSONObject("datas");
		}
		Iterator changeIter = jsobj.keys();
		while(changeIter.hasNext())
		{
			String key = changeIter.next().toString();
			if(key.startsWith("变更前"))
			{
				String before = null;
				JSONArray beforeArray = jsobj.getJSONArray(key);
				if(!beforeArray.isEmpty())
				{
					for(int i = 0;i<beforeArray.size();i++)
					{
						JSONObject beforeObj = beforeArray.getJSONObject(i);
						Iterator it = beforeObj.keys();
						while(it.hasNext())
						{
							String beforeKey = it.next().toString();
							if(!"序号".equals(beforeKey))
							{
								before += beforeKey+":"+ beforeObj.get(beforeKey)+";";
							}
						}
						before += "+";//前台分割使用的标识符						
					}
				}
				if(null!=before)
				{
					before = before.replace("null", "");//去掉Null
					before = before.substring(0, before.lastIndexOf("+"));//去掉最后一个+
					beforeList.add(before);
				}	
			}
			else if(key.startsWith("变更后"))
			{
				String after = null;
				JSONArray afterArray = jsobj.getJSONArray(key);
				if(!afterArray.isEmpty())
				{
					for(int i = 0;i<afterArray.size();i++)
					{
						JSONObject afterObj = afterArray.getJSONObject(i);
						Iterator it = afterObj.keys();
						while(it.hasNext())
						{
							String afterKey = it.next().toString();
							if(!"序号".equals(afterKey))
							{
								after += afterKey+":"+afterObj.get(afterKey)+";";
							}
						}
						after += "+";//前台分割使用的标识符
					}
				}
				if(null != after)
				{
					after = after.replace("null", "");//去掉Null
					after = after.substring(0, after.lastIndexOf("+"));//去掉最后一个+
					afterList.add(after);
				}
			}
		}	
		String companyId = null;
		String ats = null;
		String holderLabelName = null;//值为股东信息、投资人信息、、前台显示的标签
		String holderDetLabelName = null;//值为股东详细信息、投资人详细信息、、前台显示的标签
		try
		{			
			if(!jsobj.containsKey("基本信息"))
			{
				basicSum += 1;
				log.info("basicSum: "+basicSum+"---文件名"+file.getPath());
				return;
			}
			JSONObject baseObj = null;
			if(jsobj.containsKey("基本信息"))
			{
				baseObj = jsobj.getJSONArray("基本信息").getJSONObject(0);					
			}
			else if(jsobj.containsKey("企业基本信息"))
			{
				baseObj = jsobj.getJSONArray("企业基本信息").getJSONObject(0);
			}
			if(!baseObj.containsKey("注册号"))
			{
				if(!baseObj.containsKey("统一社会信用代码/注册号"))
				{
					if(!baseObj.containsKey("注册号/统一社会信用代码"))
					{
						if(!baseObj.containsKey("营业执照注册号统一社会信用代码"))
						{
							regSum += 1;
							log.info("regSum: " + regSum+"---文件名"+file.getPath());
							return;
						}
					}
				}
			}
			ats = baseObj.getString("名称");
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
			baseObj = null;	
			//如果公司ID都没有，没有必要走下去了
			if(null == companyId)
			{
				regSum += 1;
				log.info("regSum: " + regSum+"---文件名"+file.getPath());
				return;
			}
		}
		catch(Exception e)
		{
			log.info(file.getPath()+"\n"+e);
			return;
		}
		//判断是否存在该公司，避免去重
		JsonDocument jsonDoc = null;
		try{
			System.out.println(companyId+"\t"+ats);
			Bucket bucket = ClusterUtil.commonBucket("etp_t");//etp_t
			jsonDoc = bucket.get(companyId,1,TimeUnit.MINUTES);
			bucket=null;
		}
		catch(Exception e)
		{
			log.info(e);
			while(true)
			{	
				try
				{
					Bucket bucket = ClusterUtil.commonBucket("etp_t");
					jsonDoc = bucket.get(companyId,1,TimeUnit.MINUTES);
					bucket=null;
					break;
				}
				catch(Exception ee)
				{
					log.info(ee);
				}
			}
		}
		if(null != jsonDoc)
		{
			return;
		}
		Iterator iter = jsobj.keys();	
		while(iter.hasNext())
		{			
		    String key = iter.next().toString();		    
			if("基本信息".equals(key)||"企业基本信息".equals(key))
			{
				JSONArray baseArray = jsobj.getJSONArray(key);		
				JSONObject baseObj = baseArray.getJSONObject(0);
				//基本信息
				if(null != baseObj && null != companyId)
				{
					insertBaseInfo(baseObj,companyId,obj);					
				}
				baseObj = null;
				baseArray = null;
			}
			//股东信息   别名：股东信息（后面有备注）、主管部门（出资人）信息、投资人信息、出资人信息、合伙人信息
			else if(key.startsWith("股东信息"))
			{
				tuziArray = jsobj.getJSONArray(key);
				holderLabelName = "股东信息";
			}
			else if("主管部门（出资人）信息".equals(key))
			{
				tuziArray = jsobj.getJSONArray(key);
				holderLabelName = "主管部门（出资人）信息";
			}
			else if("投资人信息".equals(key))
			{
				tuziArray = jsobj.getJSONArray(key);
				holderLabelName = "投资人信息";
			}
			else if(null != key && key.contains("发起人"))
			{
				
				JSONArray temp = jsobj.getJSONArray(key);
				if(temp.isEmpty())
				{
					continue;
				}
				tuziArray = temp;
				holderLabelName = "发起人";
			}
			else if("合伙人信息".equals(key))
			{
				tuziArray = jsobj.getJSONArray(key);
				holderLabelName = "合伙人信息";
			}
			//股东详情信息	别名：股东及出资信息、股东出资信息
			else if("股东详情信息".equals(key))
			{
				chuziArray = jsobj.getJSONArray(key);
				holderDetLabelName = "股东详情信息";
			}
			else if("股东及出资信息".equals(key))
			{
				chuziArray = jsobj.getJSONArray(key);
				holderDetLabelName = "股东及出资信息";
			}
			else if(key.contains("股东出资"))//股东出资信息、股东出资详情
			{
				JSONArray temp = jsobj.getJSONArray(key);
				if(!temp.isEmpty())
				{
					chuziArray = temp;
					holderDetLabelName = "股东出资信息";
				}
			}
			else if("投资人及出资信息".equals(key))
			{
				chuziArray = jsobj.getJSONArray(key);
				holderDetLabelName = "投资人及出资信息";
			}	
			else if(key.contains("变更"))
			{
				if(null != companyId)
				{
					insertChange(jsobj.getJSONArray(key),companyId);
				}
			}
			else if(null != key && key.contains("主要人员"))
			{
				JSONArray array = jsobj.getJSONArray(key);
				if(!array.isEmpty() && null != companyId)
				{
					insertMholder(array,key,companyId);
				}
			}
		}
		TempHolderVO tempVO = null;
		RemarkVO remarkVO = null;
		List<TempHolderVO> holderAndDetList = new ArrayList<TempHolderVO>();
		List<HolderVO> holderList = new ArrayList<HolderVO>();
		List<HolderDetailVO> holderDetList = new ArrayList<HolderDetailVO>();
		//判断 股东、股东详情是否为空
		if(null != tuziArray && tuziArray.size()>0)
		{
			if(null != chuziArray && chuziArray.size()>0)
			{
				for(int i = 0; i < tuziArray.size();i++)
				{
					tempVO = new TempHolderVO();
					remarkVO = new RemarkVO();
					JSONObject tuziObj = tuziArray.getJSONObject(i);
					JSONObject chziObj = chuziArray.getJSONObject(i);
					Iterator tuziIter = tuziObj.keys();
					Iterator chziIter = chziObj.keys();
					int count = 0;
					while(chziIter.hasNext())
					{
						count +=1;
						//投资人key
						String tuziKey = null;
						if(count<5)
						{
							tuziKey = tuziIter.next().toString();
						}
						//投资人出资详细key
						String chziKey = chziIter.next().toString();						
						if(null!=tuziKey&&("股东".equals(tuziKey)||"姓名".equals(tuziKey)||"合伙人".equals(tuziKey)||tuziKey.contains("股东/发起人名称")
								||tuziKey.contains("股东/发起人")||tuziKey.contains("股东/发起人信息")||tuziKey.contains("股东（发起人）")
								||tuziKey.contains("股东/出资人")||tuziKey.contains("股东/合伙人信息")||tuziKey.contains("股东/合伙人信息")
								||"发起人".equals(tuziKey)||"出资人".equals(tuziKey)||"合伙人信息".equals(tuziKey)
								||"投资人".equals(tuziKey)||"投资人名称".equals(tuziKey)||"发起人信息".equals(tuziKey)))
						{
							remarkVO.setHolder(tuziKey);
							tempVO.setHolder(tuziObj.get(tuziKey).toString());
						}
						//合伙人类型  	别名：股东类型、出资人类型、发起人类型		
						else if(null!=tuziKey&&("股东类型".equals(tuziKey)||"出资人类型".equals(tuziKey)
								||"合伙人类型".equals(tuziKey)||"股东（发起人）类型".equals(tuziKey)
								||"发起人类型".equals(tuziKey)||"投资人类型".equals(tuziKey)))
						{
							remarkVO.setType(tuziKey);
							tempVO.setType(tuziObj.get(tuziKey).toString());
						}
						// 证照/证件类型   别名：证照证件类型
						else if(null!=tuziKey&&("证照/证件类型".equals(tuziKey)||"证照证件类型".equals(tuziKey)))
						{
							tempVO.setLicenseType(tuziObj.get(tuziKey).toString());
						}	
						// 证照/证件号码	别名：证照证件号码
						else if(null!=tuziKey&&("证照/证件号码".equals(tuziKey)||"证照证件号码".equals(tuziKey)))
						{
							tempVO.setLicenseNum(tuziObj.get(tuziKey).toString());
						}
						//equityPart 出资方式
						else if(null!=tuziKey&&("出资方式".equals(tuziKey) || "投资方式".equals(tuziKey)))
						{
							tempVO.setEquityPart(tuziObj.get(tuziKey).toString());
						}			
						// 认缴出资额	        别名：认缴出资额(万元)
						if("认缴出资额".equals(chziKey)||"认缴出资额(万元)".equals(chziKey)
								||"认缴出资额（万元）".equals(chziKey))
						{
							tempVO.setSubcriCapital(chziObj.get(chziKey).toString());
						}
						// 认缴出资方式	别名：出资方式、认缴明细
						else if("认缴出资方式".equals(chziKey)||"出资方式".equals(chziKey)
								||"认缴明细".equals(chziKey))
						{
							tempVO.setConMethod(chziObj.get(chziKey).toString());
						}
						//认缴出资时间	别名：认缴出资日期
						else if("认缴出资时间".equals(chziKey)||"认缴出资日期".equals(chziKey))
						{
							tempVO.setConsidDate(formatDate(chziObj.get(chziKey).toString()));
						}
						//实缴出资额	        别名：实缴出资额(万元)、实缴明细
						else if("实缴出资额".equals(chziKey)||"实缴出资额(万元)".equals(chziKey)
								||"实缴明细".equals(chziKey)||"实缴出资额（万元）".equals(chziKey))
						{
							tempVO.setActualCapital(chziObj.get(chziKey).toString());
						}
						// 实缴出方式	        别名：实缴出资方式、出资方式6
						else if("实缴出方式".equals(chziKey)||"实缴出资方式".equals(chziKey)
								||"出资方式6".equals(chziKey))
						{
							tempVO.setFactMethod(chziObj.get(chziKey).toString());
						}
						// 实缴出时间	      别名：实缴出资日期、实缴出资时间
						else if("实缴出时间".equals(chziKey)||"实缴出资日期".equals(chziKey)
								||"实缴出资时间".equals(chziKey))
						{
							tempVO.setActualDate(formatDate(chziObj.get(chziKey).toString()));
						}
						// 认缴额(万元）
						else if("认缴额(万元)".equals(chziKey)||"认缴额（万元）".equals(chziKey))
						{
							tempVO.setConCapital(chziObj.get(chziKey).toString());
						}
					    // 实缴额(万元）
						else if("实缴额(万元)".equals(chziKey)||"实缴额（万元）".equals(chziKey))
						{
							tempVO.setFactCapital(chziObj.get(chziKey).toString());
						}
					}
					tempVO.setRemark(remarkVO);
					holderAndDetList.add(tempVO);
					tempVO = null;
					remarkVO = null;
				}
			}
			else
			{
				holder(tuziArray,holderLabelName,companyId);
			}
			//置空股东信息、投资人信息
			tuziArray = null;
			//股东详细信息、投资人详细信息
			chuziArray = null;
		}		
		//拆分股东和股东详情数据
		if(null != holderAndDetList && holderAndDetList.size()>0)
		{
			HolderVO holderVo = null;
			HolderDetailVO detailVo = null;
			for(TempHolderVO vo:holderAndDetList)
			{
				//股东
				String keyHolder = UUID.randomUUID().toString();//股东ID				
				holderVo = new HolderVO();
				holderVo.setHolderId(keyHolder);//股东ID
				holderVo.setCompanyId(companyId);//公司
				holderVo.setHolder(vo.getHolder());//股东名称
				holderVo.setType(vo.getType());//股东类型
				holderVo.setLicenseType(vo.getLicenseType());//证件类型
				holderVo.setLicenseNum(vo.getLicenseNum()); //证件编码
				holderVo.setEquityPart(vo.getEquityPart());//出资方式
				holderVo.setRemark(vo.getRemark());
				holderVo.getRemark().setBucketName(holderLabelName);//标签名称
				holderList.add(holderVo);//存储在集合
				//股东详情	
				detailVo = new HolderDetailVO();//股东详情对象		
				detailVo.setHolder(vo.getHolder());//股东姓名
				detailVo.setType(vo.getType());//股东类型
				detailVo.setHolderId(keyHolder);//股东ID
				detailVo.setSubcriCapital(vo.getSubcriCapital());
				detailVo.setConCapital(vo.getConCapital());
				detailVo.setConMethod(vo.getConMethod());
				detailVo.setConsidDate(vo.getConsidDate());
				detailVo.setActualCapital(vo.getActualCapital());
				detailVo.setActualDate(vo.getActualDate());
				detailVo.setFactCapital(vo.getFactCapital());//实际出资额
				detailVo.setFactMethod(vo.getFactMethod());//实际出资方式
				detailVo.setRemark(vo.getRemark());
				detailVo.getRemark().setBucketName(holderDetLabelName);
				holderDetList.add(detailVo);
			}
		}
		holderAndDetList = null;
		JsonObject jsonObject = null;
		JsonDocument doc = null;
		//写库
		if(null != holderList && holderList.size()>0)
		{
			for(HolderVO holderVO:holderList)
			{
				//股东的ID
				String key = holderVO.getHolderId();
				holderVO.setHolderId(null);
				jsonObject = JsonObject.fromJson(gs.toJson(holderVO));
				doc = JsonDocument.create(key, jsonObject);
				createJsonDocument("etp_holder_e",doc);//etp_holder_e
				jsonObject = null;
				doc = null;				
			}
		}
		//写库
		if(null!=holderDetList && holderDetList.size()>0)
		{
			for(HolderDetailVO detVO:holderDetList)
			{
				jsonObject = JsonObject.fromJson(gs.toJson(detVO));
				doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
				createJsonDocument("etp_holder_det_e",doc);
				jsonObject = null;
				doc = null;	
			}
		}
	}
	/**
	 * 处理公司基本信息
	 * @param baseObj 处理的数据
	 * @param companyId 公司ID
	 * @param jsonArray json对象
	 * @throws ParseException 
	 */
	@SuppressWarnings("rawtypes")
	public void insertBaseInfo(JSONObject baseObj,String companyId,JSONObject obj) throws Exception
	{
		//企业对象
		EnterpriseVO enterVO = new EnterpriseVO();;
		//字段与页面标签映射对象、表名与标签
		RemarkVO remarkVO = new RemarkVO();
		remarkVO.setBucketName("基本信息");
		//对象的json对象
		JsonObject jsonObject = null;
		JsonDocument doc = null;	
		//key的遍历器
		Iterator keyIter = baseObj.keys();			
		while(keyIter.hasNext())
		{
			String key = keyIter.next().toString();				
			String value = baseObj.get(key).toString();
			if(null != value && "null".equals(value))
			{
				value = null;
			}
			//贵州
			if("名称".equals(key) && null == value)
			{
				value = obj.get("compname").toString();
			}
			if(key.endsWith("注册号"))
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
			else if("统一社会信用代码".equals(key))
			{
				enterVO.setCreditCode(value);
			}
			else if(key.contains("类型"))
			{
				enterVO.setType(value);
			}
			else if("法定代表人".equals(key)||"负责人".equals(key)||"投资人".equals(key)
					||"经营者".equals(key)||"执行事务合伙人".equals(key)||"股东".equals(key)
					||key.startsWith("首席")||key.startsWith("姓名"))
			{
				if(null != enterVO.getCompany())
				{
					remarkVO.setLegalRep(key);
					enterVO.setLegalRep(value);					
				}
			}				
			else if("住所".equals(key)||key.contains("场所"))
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
			else if(key.contains("期限自"))
			{
				remarkVO.setStartTime(key);
				enterVO.setStartTime(formatDate(value));					
			}
			else if(key.contains("期限至"))
			{
				remarkVO.setEndTime(key);
				enterVO.setEndTime(formatDate(value));
			}
			else if(key.contains("范围"))
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
			else if("吊销日期".equals(key)||"注销日期".equals(key))
			{
				enterVO.setRevokeDate(formatDate(value));
			}
			else if("组成形式".equals(key))
			{
				enterVO.setComposition(value);
			}							
		}
		//通过注册号没有查询到行政区的情况
		if(null == enterVO.getProvince() && null == enterVO.getCity())
		{
			if(null != enterVO.getLocation())
			{
				if(0!=enterVO.getLocation().hashCode())
				{
					//通过企业的住所字段获得行政区
					String admin[] = doAdmin(u.enterp2(enterVO.getLocation()));
					enterVO.setProvince(admin[0]);
					enterVO.setCity(admin[1]);
					enterVO.setArea(admin[2]);
				}
			}			
		}
		if(null == enterVO.getProvince() && null == enterVO.getCity())
		{
			if(null != enterVO.getRegOffice())
			{
				if(0!=enterVO.getRegOffice().hashCode())
				{
					//通过 登记机关 字段获得行政区
					String admin[] = doAdmin(u.enterp2(enterVO.getRegOffice()));
					enterVO.setProvince(admin[0]);
					enterVO.setCity(admin[1]);
					enterVO.setArea(admin[2]);				
				}
			}
			else 
			{
				//通过企业名称
				if(null != enterVO.getCompany())
				{
					if(0!=enterVO.getCompany().hashCode())
					{
						String array[] = doAdmin(u.enterp(enterVO.getCompany()));
						enterVO.setProvince(array[0]);
						enterVO.setCity(array[1]);
						enterVO.setArea(array[2]);		
					}
				}
				else
				{
					enterVO.setProvince(obj.get("province").toString());
				}
			}
		}
		JSONObject jsobj = obj.getJSONObject("datas");
		JSONArray jyfwArray = null;
		//从大对象里面取经营范围
		if(jsobj.containsKey("经营范围信息"))
		{
			jyfwArray = jsobj.getJSONArray("经营范围信息");
		}
		else if(jsobj.containsKey("业务范围信息"))
		{
			jyfwArray = jsobj.getJSONArray("业务范围信息");
		}
		if(null!=jyfwArray)
		{
			JSONObject jyfwObject = jyfwArray.getJSONObject(0);
			Iterator it = jyfwObject.keys();
			while(it.hasNext())
			{
				enterVO.setScope(jyfwObject.get(it.next().toString()).toString());
			}
		}
		//设置备注对象
		enterVO.setRemark(remarkVO);
		if(obj.containsKey("url"))
		{
			String url = obj.getString("url");
			if(null!=url && 0 != url.hashCode())
			{
				enterVO.setUrl(url);
			}
		}
		jsonObject = JsonObject.fromJson(gs.toJson(enterVO));
		enterVO = null;
		doc = JsonDocument.create(companyId, jsonObject);	
		createJsonDocument("etp_t",doc);//etp_t
		jsonObject = null;
	
	}
	/**
	 * 写数据
	 */
	public void createJsonDocument(String name,JsonDocument doc){
		if(null==doc){return;}
		Bucket bucket = ClusterUtil.commonBucket(name);
		while(true){
			try {
				log.info("json:"+doc.toString());
				bucket.upsert(doc,1,TimeUnit.MINUTES);
				break;
			} catch (Exception e) {
				System.out.println("超时:"+e.getMessage());
				System.out.println("重写:"+doc.toString());	
			}
		}
	}
	/**
	 * 股东信息
	 * @param tuziArray 股东信息数组
	 * @param bucketName前台标签名称
	 * @param companyId 公司ID
	 */
	@SuppressWarnings("rawtypes")
	public void holder(JSONArray tuziArray,String bucketName,String companyId)
	{
		if(null != tuziArray && tuziArray.size()>0)
		{
			HolderVO holderVO = null;
			RemarkVO remarkVO = null;
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			for(int i = 0; i < tuziArray.size();i++)
			{
				holderVO = new HolderVO();
				holderVO.setCompanyId(companyId);
				remarkVO = new RemarkVO();
				remarkVO.setBucketName(bucketName);//前台标签名称				
				JSONObject tuziObj = tuziArray.getJSONObject(i);
				Iterator tuziIter = tuziObj.keys();
				while(tuziIter.hasNext())
				{
					String tuziKey = tuziIter.next().toString();
					if("股东".equals(tuziKey)||"姓名".equals(tuziKey)||"合伙人".equals(tuziKey)||tuziKey.contains("股东/发起人名称")
							||tuziKey.contains("股东/发起人")||tuziKey.contains("股东/发起人信息")||tuziKey.contains("股东（发起人）")
							||tuziKey.contains("股东/出资人")||tuziKey.contains("股东/合伙人信息")||tuziKey.contains("股东/合伙人信息")
							||"发起人".equals(tuziKey)||"出资人".equals(tuziKey)||"合伙人信息".equals(tuziKey)
							||"投资人".equals(tuziKey)||"投资人名称".equals(tuziKey)||"发起人信息".equals(tuziKey))
					{
						remarkVO.setHolder(tuziKey);
						holderVO.setHolder(tuziObj.get(tuziKey).toString());
					}
					//合伙人类型  	别名：股东类型、出资人类型、发起人类型		
					else if("股东类型".equals(tuziKey)||"出资人类型".equals(tuziKey)
							||"合伙人类型".equals(tuziKey)||"股东（发起人）类型".equals(tuziKey)
							||"发起人类型".equals(tuziKey)||"投资人类型".equals(tuziKey))
					{
						remarkVO.setType(tuziKey);
						holderVO.setType(tuziObj.get(tuziKey).toString());
					}
					// 证照/证件类型   别名：证照证件类型
					else if("证照/证件类型".equals(tuziKey)||"证照证件类型".equals(tuziKey))
					{
						holderVO.setLicenseType(tuziObj.get(tuziKey).toString());
					}	
					// 证照/证件号码	别名：证照证件号码
					else if("证照/证件号码".equals(tuziKey)||"证照证件号码".equals(tuziKey))
					{
						holderVO.setLicenseNum(tuziObj.get(tuziKey).toString());
					}
					//equityPart 出资方式
					else if("出资方式".equals(tuziKey) || "投资方式".equals(tuziKey))
					{
						holderVO.setEquityPart(tuziObj.get(tuziKey).toString());
					}	
				}
				holderVO.setRemark(remarkVO);
				//写库
				jsonObject = JsonObject.fromJson(gs.toJson(holderVO));
				doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
				createJsonDocument("etp_holder_e",doc);//etp_holder_e
				jsonObject = null;
				doc = null;				
			}
		}
	}	
	/**
	 * 格式化日期
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public String formatDate(String date) throws ParseException
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
					else
					{
						int index_xie = date.indexOf("/");
						int index_mao = date.indexOf(":");
						if(4 == index_xie && -1!= index_mao)
						{
							String newDate = date.substring(0,date.indexOf(" "));
							//  yyyy/MM/dd hh:mm:ss 情况
							pattern = Pattern.compile(yyyy);
							matcher = pattern.matcher(newDate);
							if(matcher.matches())
							{
								sf = new SimpleDateFormat("yyyy/MM/dd");
								Date d = sf.parse(newDate);
								sf = null;
								sf = new SimpleDateFormat("yyyy年MM月dd日");
								result = sf.format(d);
								sf = null;
							}
						}
						//处理：Mon Apr 22 00:00:00 CST 2013格式
						if(date.contains("CST"))
						{
							sf = new SimpleDateFormat("yyyy年MM月dd日");
							@SuppressWarnings("deprecation")
							Date dt = new Date(date);
							result = sf.format(dt);
							sf = null;
							dt = null;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 处理主要人员信息
	 * @param mholderArray 处理的数据
	 * @param bucketName 前台标签
	 * @param companyId 公司ID
	 */
	@SuppressWarnings("rawtypes")
	public void insertMholder(JSONArray mholderArray,String bucketName,String companyId)
	{
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
						createJsonDocument("etp_mhold_e",doc);//etp_mhold_e
						jsonObject = null;
						//bucket=null;
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
						/*Bucket bucket = ClusterUtil.commonBucket("etp_mhold_e");
						bucket.upsert(doc);*/
						createJsonDocument("etp_mhold_e",doc);//etp_mhold_e
						jsonObject = null;
						//bucket=null;
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
					/*	Bucket bucket = ClusterUtil.commonBucket("etp_mhold_e");
						bucket.upsert(doc);*/
						createJsonDocument("etp_mhold_e",doc);//etp_mhold_e
						jsonObject = null;
						//bucket=null;
						doc = null;
					}
				}
				tempVO = null;
			} catch (JsonProcessingException e) {
				log.info(e);
			} catch (IOException e) {
				log.info(e);
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
	/**
	 * 处理变更信息
	 * 存在点击“详情” ，出现表格的形式
	 * @param changArray 处理的数据
	 * @param before 变更前内容字符串
	 * @param after  变更后内容字符串 
	 * @param companyId 公司ID
	 * @throws ParseException 
	 */
	@SuppressWarnings("rawtypes")
	public void insertChange(JSONArray changArray,String companyId) throws ParseException
	{
		if(!changArray.isEmpty())
		{
			RemarkVO remarkVO = null;
			//变更对象
			ChangeVO changeVO = null;
			//对象的json对象
			JsonObject jsonObject = null;
			JsonDocument doc = null;
			int count = 0;
			for(int i=0;i<changArray.size();i++)
			{				
				remarkVO = new RemarkVO();
				remarkVO.setBucketName("变更信息");
				changeVO = new ChangeVO();
				changeVO.setCompanyId(companyId);
				changeVO.setRemark(remarkVO);
				JSONObject obj = changArray.getJSONObject(i);
				Iterator iter = obj.keys();
				boolean flag = false;				
				while(iter.hasNext())
				{
					String key = iter.next().toString();
					String value = obj.get(key).toString();
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
								flag = true;
								count += 1;
								if(count <= beforeList.size())
								{
									changeVO.setChangeBefore(beforeList.get(count-1));
								}
							}
							else
							{
								changeVO.setChangeBefore(value);
							}
						}
						else if("变更后内容".equals(key))
						{
							//不是详情
							if(!flag)
							{
								changeVO.setChangeAfter(value);
							}
							else
							{
								changeVO.setChangeDate(formatDate(value));
								if(count <= afterList.size())
								{
									changeVO.setChangeAfter(afterList.get(count-1));
								}
							}
						}
						else if("变更日期".equals(key))
						{	
							if(!flag)
							{
								changeVO.setChangeDate(formatDate(value));
							}
							
						}		
					}
				}
				//写库
				if(null != changeVO && null != changeVO.getChangeEvent())
				{
					jsonObject = JsonObject.fromJson(gs.toJson(changeVO));
					doc = JsonDocument.create(UUID.randomUUID().toString(), jsonObject);
					//写库
					createJsonDocument("etp_event_item_e",doc);
					//bucket=null;
				}
				remarkVO = null;
				changeVO = null;
				jsonObject = null;
				doc = null;
			}		
		}
		beforeList = null;
		beforeList = new ArrayList<String>();
		afterList = null;
		afterList = new ArrayList<String>();
	}
	/**
	 * 公用查库的方法
	 * @param bucket 指定的桶
	 * @param doc 文档
	 */
	public void commonInsert(Bucket bucket,JsonDocument doc)
	{
		try
		{
			bucket.upsert(doc);
		}
		catch(Exception e)
		{
			log.info(e);
			while(true)
			{
				try
				{
					bucket.upsert(doc);
					break;
				}
				catch(Exception ee)
				{
					log.info(ee);
				}
			}
		}
	}

	/**
	 * 通过行政区划获得行政区
	 * @param adminiCode 行政区划
	 * @return 省市县数组
	 */
	public String[] listCountryCityProvince(String adminiCode)
	{
		//0~2元素是省、市、县
		String []countryCityProvince = new String[3];
		if(null != adminiCode)
		{
			if(adminiCode.length()<6)
			{
				return countryCityProvince;
			}	
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
	/**
	 * 行政区划数组处理直辖市
	 * @param admin 行政区数组
	 * @return 行政区数组结果
	 */
	public String[] doAdmin(String admin[])
	{		
		if(null != admin[0] || null != admin[1])
		{
			if(null != admin[1] && (admin[1].equals("北京市")||admin[1].equals("天津市")
					||admin[1].equals("重庆市")||admin[1].equals("上海市")))
			{
				admin[0] = admin[1];
				admin[1] = null;
			}
		}		
		return admin;
	}
}
