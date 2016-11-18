//package cn.com.szgao.enterprise;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.elasticsearch.action.bulk.BulkRequestBuilder;
//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.ImmutableSettings;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.springframework.web.client.RestTemplate;
//
//import com.google.gson.Gson;
//
//import cn.com.szgao.dto.CodeVO;
//import cn.com.szgao.dto.EnterpriseVO;
//import cn.com.szgao.dto.EtpCouchbaseVO;
//import cn.com.szgao.dto.EtpDocVO;
//import cn.com.szgao.dto.ListEtpCouchbaseVO;
//
//public class TestEnterprisetIntoEs {
//	private static Logger log = LogManager.getLogger(TestEnterprisetIntoEs.class);
//	static Gson gson=new Gson();
//	public static void main(String[] args) {
//		PropertyConfigurator.configure("D:/mavenSpace6/Mass/log/log4j.properties");
//		try {
//			text();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}		
//	}
//	public static void text() throws IOException, ParseException {
//        Map<String, Object> urlVariables = new HashMap<String, Object>();
//        urlVariables.put("limits",10000);
//        urlVariables.put("inclusive_ends", true);
//        urlVariables.put("skips", 1);
//        String key=null;
//        RestTemplate template=new RestTemplate();
//        String result=null;
//        int item = 0;
//        //Blob
//        while(true)
//        {
//        	if(null==key)
//        	{
//        		//http://192.168.1.5:8092/executedN/_design/ex_all/_view/ex_all?inclusive_end={inclusive_ends}&limit={limits}&skip={skips}
//        		 result = template.getForObject("http://192.168.1.5:8092/etp_t/_design/etp_t/_view/etp_t_s?inclusive_end={inclusive_ends}&limit={limits}&skip={skips}",
//        				String.class, urlVariables);
//        	}
//        	else
//        	{
//        		//http://192.168.1.5:8092/executedN/_design/ex_all/_view/ex_all?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip={skips}
//        		urlVariables.put("startkeys","\""+key+"\"");
//        		result = template.getForObject("http://192.168.1.5:8092/etp_t/_design/etp_t/_view/etp_t_s?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip={skips}",
//        					String.class, urlVariables);
//        	}
//    		String value = null;
//			try {
//				value = new String(result.getBytes("ISO-8859-1"), "utf-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//				log.error(e);
//			}
//			ListEtpCouchbaseVO vo = gson.fromJson(value, ListEtpCouchbaseVO.class);
//    		if(null != vo)
//    		{
//    			List<EtpCouchbaseVO> list = vo.getRows();
//    			if(null!=list&&list.size()>0)
//    			{
//    				EtpCouchbaseVO cbvo = null;
//    				EnterpriseVO enVO = null;
//    				for(int i=0;i<list.size();i++)
//    				{
//    					item += 1;
//    					System.out.println(item);
//    					cbvo = list.get(i);
//    					if(null != cbvo)
//    					{
//    						key = cbvo.getKey();    
//    						enVO = cbvo.getValue();
//    						if(null != enVO)
//    		    			{
//    							List<CodeVO> code = null;
//    							String regNum = null;
//    							String creditCode = null;
//    							if(null!=enVO.getRegNum())
//    							//注册号或信用代码是否有效
//    							{
//    								code=getregNumList(enVO.getRegNum());
//    							}
//    							if((null==code||(null!=code&&code.size()==0))&&null!=enVO.getCreditCode())
//    							{
//    								code=getregNumList(enVO.getCreditCode());
//    							}
//    							if(null!=code&&code.size()>0)
//    							{
//    								for(CodeVO co:code)
//    								{
//    									System.out.println(co.getCode()+":"+co.getStatus());
//    									if(co.getStatus()==1)
//    									{
//    										//注册号
//    										regNum = co.getCode();
//    									}
//    									else
//    									{
//    										//信用代码
//    										creditCode = co.getCode();
//    									}
//    								}
//    								code.clear();
//    								code=null;					
//    							}  
//    							if(null!=regNum||null!=creditCode)
//    							{
//    								EtpDocVO doc = new EtpDocVO();
//    								EnterpriseVO enVO2 = new EnterpriseVO();
//    								enVO2.setCompanyId(key);
//    								enVO2.setCompany(enVO.getCompany());
//    								enVO2.setLocation(enVO.getLocation());
//    								enVO2.setScope(enVO.getScope());    								
//									enVO2.setRegNum(enVO.getRegNum());
//									enVO2.setProvince(enVO.getProvince());
//									enVO2.setCity(enVO.getCity());
//									enVO2.setArea(enVO.getArea());
//    								if(null!=enVO.getRegNum()&&enVO.getRegNum().length()==18)
//    								{
//    									enVO2.setCreditCode(enVO.getRegNum());
//    									enVO2.setRegNum(null);
//    								}
//    								enVO2.setCreditCode(enVO.getCreditCode());
//    								doc.setDoc(enVO2);
//    								docMap.put(key, doc);
//    								enVO2 = null;
//    								enVO = null;
//    								panduan();//6000条开始写库
//    							}
//    		    			}
//    					}
//    					cbvo = null;
//    				}
//    			}
//    			list = null;
//    		}
//    		if(item+1 >= vo.getTotal_rows())
//			{
//				break;
//			}
//    		vo = null;
//        }
//	} 
//	public static void panduan()
//	{
//		if(null!=docMap&&docMap.size()>=10000)
//		{
//			write();
//		}
//	}
//	static Map<String,EtpDocVO> docMap = new HashMap<String,EtpDocVO>();
//	static Settings settings = ImmutableSettings.settingsBuilder()
//	        .put("cluster.name", "elasticsearch").put("client.transport.sniff", true).build();//elasticsearch
//	//客户端对象
//	static Client client = new TransportClient(settings)
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.7", 9307))
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.8", 9308));//192.168.1.2
//	public static void write(){
//		List<IndexRequest> requests = new ArrayList<IndexRequest>(); 
//		IndexRequest request =null;
//		Iterator<String> iter = docMap.keySet().iterator();
//		while(iter.hasNext())
//		{
//			String key = iter.next();
//			System.out.println(gson.toJson(docMap.get(key)));
//			request = client  
//		                .prepareIndex("etp_tstr","couchbaseDocument",key).setSource(gson.toJson(docMap.get(key))).setRefresh(true)  
//		                .request();  
//			  requests.add(request);
//		}
//		 BulkRequestBuilder bulkRequest = client.prepareBulk(); 	  
//		 for (IndexRequest idnex : requests) 
//		 {  
//		        bulkRequest.add(idnex);  
//		 }	  
//		 BulkResponse bulkResponse= bulkRequest.execute().actionGet();  
//		 if (bulkResponse.hasFailures()) 
//		 {  
//			 log.info("部分数据批量处理失败!");
//			 System.out.println("批量处理失败!");
//		 }
//		 else
//		 {
//			 bulkRequest.request().requests().clear();
//		 }
//		 docMap = null;
//		 docMap = new HashMap<String,EtpDocVO>();
//	}
//	/**
//	 * 处理注册号与信用代码
//	 * @param value
//	 * @return
//	 */
//	public static List<CodeVO> getregNumList(String value){
//		if(null==value){return null;}
//		List<CodeVO> list=new ArrayList<CodeVO>();
//		String code=value.replaceAll("&nbsp;\r\t", "").replace("&nbsp;", "").replace("\r\t", "").replace(" ", "");
//		if(code.length()==15||code.length()==13){
//			if(pattern2.matcher(code).matches())
//			{
//				//注册号
//				list.add(new CodeVO(code,1));
//			}
//			else
//			{
//				if(code.substring(code.length()-1,code.length()).equals("号")||code.substring(0,1).equals("企"))
//				{
//					//注册号
//					list.add(new CodeVO(code,1));
//				 }
//			}
//			return list;
//		}
//		else if(code.length()==18)
//		{
//			if(!pattern2.matcher(code).matches())
//			{
//				return null;
//			}
//			//信用代码
//			list.add(new CodeVO(code,2));
//			return list;
//		}
//		else
//		{
//			//可能存在问题，可能是注册号与信用代码中间存在特殊字符
//			return getCodes(code);
//		}
//	}
//	/**
//	 * 拆分注册码信用代码
//	 * @param value
//	 * @return
//	 */
//	//判断数字和字母，汉字
//	public static Pattern pattern = Pattern.compile("[0-9a-zA-Z\u4e00-\u9fa5]*");
//	//判断数字和字母
//	public static Pattern pattern2 = Pattern.compile("[0-9a-zA-Z]*");
//	public static List<CodeVO> getCodes(String value){
//		 Matcher matcher=null;
//		 StringBuffer sb=new StringBuffer();
//		 String[] a=value.split("");
//		 for(String c:a){
//			 matcher = pattern.matcher(c);
//			 if(matcher.matches()){
//				 sb.append(c);
//			 }else{
//				 sb.append(",");
//			 }
//		 }
//		 a=sb.toString().split(",");
//		 List<CodeVO> list=new ArrayList<CodeVO>();
//		 for(String c:a){
//			 if(null!=c&&!"".equals(c)){
//				 if(c.length()<=4){continue;}
//				 if(c.length()==15||c.length()==13){
//					 if(pattern2.matcher(c).matches()){
//						//注册号
//						list.add(new CodeVO(c,1)); 
//					 }
//					 else{
//						 if(c.substring(c.length()-1,c.length()).equals("号")||
//									c.substring(0,1).equals("企")){
//								//注册号
//								list.add(new CodeVO(c,1));
//						 }
//					 }
//					
//				 }				 
//				 else if(c.length()==18){
//					//if(!pattern2.matcher(c).matches()){continue;}
//					//信用代码
//					list.add(new CodeVO(c,2)); 
//				 }
//				 else if(c.substring(c.length()-1,c.length()).equals("号")||
//						 c.substring(0,1).equals("企")){
//					//注册号
//					list.add(new CodeVO(c,1));
//				 }
//			 }
//		 }
//		 return list;
//	}
//}
