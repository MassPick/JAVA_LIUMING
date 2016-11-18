//package cn.com.szgao.enterprise;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URISyntaxException;
//import java.security.InvalidKeyException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
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
//import com.google.gson.Gson;
//import com.microsoft.azure.storage.CloudStorageAccount;
//import com.microsoft.azure.storage.StorageException;
//import com.microsoft.azure.storage.blob.CloudBlobClient;
//import com.microsoft.azure.storage.blob.CloudBlobContainer;
//import com.microsoft.azure.storage.blob.CloudBlockBlob;
//
//import cn.com.szgao.dto.CouchbaseVO;
//import cn.com.szgao.dto.Doc;
//import cn.com.szgao.dto.ListCouchbaseVO;
//import cn.com.szgao.dto.WholeCourtVO;
//
///**
// * 被执行、失信、裁判文书，
// * @author xiongchangyi
// *
// */
//public class WholeCourtData {
//	static Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
//	static Matcher matcher = null;
//	private static Logger log = LogManager.getLogger(WholeCourtData.class);
//	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
//	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//	static CloudStorageAccount storageAccount = null;
//	static CloudBlobClient blobClient = null;
//	static CloudBlobContainer container_court = null;
//	static CloudBlobContainer container_courts;
//	static CloudBlobContainer container_pub = null;
//	//裁判文书的blob路径
//	static String court = "courtnew";//http://masspickdata.blob.core.chinacloudapi.cn/
//	//公告
//	static String courtPub = "courtpub";//http://masspickdata.blob.core.chinacloudapi.cn/
//	static String courts = "courts";//http://masspickdata.blob.core.chinacloudapi.cn/
//	static CloudBlockBlob blob = null;
//	
//	//Blob参数
//	public static final String storageConnectionString =
//	        "DefaultEndpointsProtocol=http;"
//	        + "AccountName=masspickdata;"
//	        + "AccountKey=qi8o0gS/Jl2/se9uB3LJFi6QMc562IZuHf4oIwfVkGBDKychIwk39Mes9pNu5ni0gBOoru+DF7SN/RZ0V6oEyQ==;"
//	        +"EndpointSuffix=core.chinacloudapi.cn";
//	//转json对象
//	static Gson gson=new Gson();
//	static Settings settings = ImmutableSettings.settingsBuilder()
//	        .put("cluster.name", "elasticsearch").put("client.transport.sniff", true).build();//elasticsearch
//	//客户端对象
//	static Client client = new TransportClient(settings)
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.7", 9307))
//			.addTransportAddress(new InetSocketTransportAddress("192.168.1.8", 9308));//192.168.1.2
//	public static void main(String[] args) {
//		PropertyConfigurator.configure("D:/mavenSpace6/Mass/log/log4j.properties");
//		try {
//			storageAccount = CloudStorageAccount.parse(storageConnectionString);
//			blobClient = storageAccount.createCloudBlobClient();
//			container_court  = blobClient.getContainerReference(court);
//			container_courts  = blobClient.getContainerReference(courts);
//			//container_pub = blobClient.getContainerReference(courtPub);
//			text();
//		} catch (IOException e) {
//			e.printStackTrace();
//			log.error(e);
//		} catch (InvalidKeyException e) {
//			e.printStackTrace();
//			log.error(e);
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//			log.error(e);
//		} catch (StorageException e) {
//			e.printStackTrace();
//			log.error(e);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			log.error(e);
//		}		
//	}
//	//存储批量的对象
//	static List<Doc> docList = new ArrayList<Doc>();
//	public static void text() throws IOException, ParseException {
//        Map<String, Object> urlVariables = new HashMap<String, Object>();
//        urlVariables.put("limits",5000);
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
//        		//http://192.168.1.4:8092/executedN/_design/dev_dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&skip={skips}
//        		 result = template.getForObject("http://192.168.1.5:8092/executedN/_design/ex_all/_view/ex_all?inclusive_end={inclusive_ends}&limit={limits}&skip={skips}",
//        				String.class, urlVariables);
//        	}
//        	else
//        	{
//        		//http://192.168.1.5:8092/executedN/_design/ex_all/_view/ex_all?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip={skips}
//        		//http://192.168.1.4:8092/executedN/_design/dev_dd/_view/dd?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip={skips}
//        		urlVariables.put("startkeys","\""+key+"\"");
//        		result = template.getForObject("http://192.168.1.5:8092/executedN/_design/ex_all/_view/ex_all?inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip={skips}",
//        					String.class, urlVariables);
//        	}
//    		String value = null;
//			try {
//				value = new String(result.getBytes("ISO-8859-1"), "utf-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//				log.error(e);
//			}
//			ListCouchbaseVO vo = gson.fromJson(value, ListCouchbaseVO.class);
//    		if(null != vo)
//    		{
//    			List<CouchbaseVO> list = vo.getRows();
//    			if(null!=list&&list.size()>0)
//    			{
//    				CouchbaseVO cbvo = null;
//    				WholeCourtVO wholeVO = null;
//    				for(int i=0;i<list.size();i++)
//    				{
//    					item += 1;
//    					System.out.println(item);
//    					cbvo = list.get(i);
//    					if(null != cbvo)
//    					{
//    						//long start = System.currentTimeMillis();
//    						key = cbvo.getKey();    
//    						wholeVO = cbvo.getValue();
//    						if(null != wholeVO)
//    		    			{
//    							if(null != wholeVO.getOpenTime())
//    							{
//    								matcher = pattern.matcher(wholeVO.getOpenTime());
//    								if(matcher.matches())
//    								{
//    									wholeVO.setOpenTime(sdf.format(sdf2.parse(wholeVO.getOpenTime())));
//    								}
//    								matcher = null;
//    							}
//    							/*	if(null != wholeVO.getPublishDate()&&wholeVO.getPublishDate().contains("-"))
//    							{
//    								matcher = pattern.matcher(wholeVO.getPublishDate());
//    								if(matcher.matches())
//    								{
//    									wholeVO.setPublishDate(sdf.format(sdf2.parse(wholeVO.getPublishDate())));
//    								}
//    								matcher = null;
//    							}*/
//    							wholeVO.setWholeCourtId(key);
//    							//1表示被执行，2表示失信，3表示裁判文书，4表示法院公告，5表示工商数据
//    		    				if(null != wholeVO.getSource())
//    		    				{
//    		    					if(wholeVO.getSource()==3)
//    		    					{
//    		    						Doc doc = new Doc();
//    		    						doc.setDoc(wholeVO);
//    		    						docList.add(doc);
//    		    						doc = null;
//    		    						wholeVO = null;
//    		    						panDuan();//判断到了多少个元素写库
//    		    					/*	long start = System.currentTimeMillis();
//    		    						doCourt(container_court,key,wholeVO);
//    		    						long end = System.currentTimeMillis();
//    		    						System.out.println((end-start)+"毫秒");    */		    						
//    		    					}
//    		    					else if(wholeVO.getSource()==4)
//    		    					{    		    						
//    		    						doPub(key,wholeVO);    		    						
//    		    					}
//    		    					else if(wholeVO.getSource()==1)
//    		    					{
//    		    						Doc doc = new Doc();
//    		    						doc.setDoc(wholeVO);    		    					
//    		    						docList.add(doc);
//    		    						doc = null;
//    		    						wholeVO = null;
//    		    						panDuan();//判断到了多少个元素写库
//    		    					}
//    		    				}    		    				
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
//	
//	/**
//	 * 处理法院裁判文书
//	 * @param key ID
//	 * @param wholeVO
//	 */
//	public static void doCourt(CloudBlobContainer container,String key,WholeCourtVO wholeVO){
//		try 
//		{
//			blob = container.getBlockBlobReference(key+"."+wholeVO.getPath());
//			if(blob.exists())
//			{
//				wholeVO.setContent(blob.downloadText());
//			}
//			else
//			{
//				blob = container_courts.getBlockBlobReference(key+"."+wholeVO.getPath());
//				if(blob.exists())
//				{
//					wholeVO.setContent(blob.downloadText());
//				}
//			}
//			Doc doc = new Doc();
//			doc.setDoc(wholeVO);			
//			wholeVO = null;
//			//write(key,doc);
//			doc = null;
//		} catch (URISyntaxException e) {			
//			e.printStackTrace();
//			log.error(e);
//		} catch (StorageException e) {			
//			e.printStackTrace();
//			log.error(e);
//		} catch (IOException e) {
//			e.printStackTrace();
//			log.error(e);
//		}
//	}
//	static String path = "D:\\ssss\\Notice\\Notice\\";
//	public static void doPub(String key,WholeCourtVO wholeVO) throws IOException{
//		File file = new File(path+key+".txt");
//		if(file.exists())
//		{
//			StringBuffer sb = new StringBuffer();
//			FileReader	fr = new FileReader(file);
//			int ch = 0;    
//			while((ch = fr.read())!=-1 )
//			{ 
//				sb.append((char)ch);				
//			} 
//			fr.close();
//			fr = null;
//			if(null!=sb)
//			{
//				wholeVO.setContent(sb.toString());
//				sb = null;
//			}
//		}
//		file = null;
//		Doc doc = new Doc();
//		doc.setDoc(wholeVO);
//		docList.add(doc);
//		wholeVO = null;
//		doc = null;
//		panDuan();//判断到了多少个元素写库
//	}
//	
//	/**
//	 * 判断docList到了多少个元素才开始写库
//	 */
//	public static void panDuan(){
//		if(null != docList && docList.size()>=2000)
//		{
//			write();
//		}
//	}
//	public static void write(){
//		List<IndexRequest> requests = new ArrayList<IndexRequest>(); 
//		IndexRequest request =null;
//		for(Doc doc:docList)
//		{
//			  request = client  
//		                .prepareIndex("wholecourt","couchbaseDocument",doc.getDoc().getWholeCourtId()).setSource(gson.toJson(doc)).setRefresh(true)  
//		                .request();  
//			  requests.add(request);  
//		 }		  	  
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
//		 docList = null;
//		 docList = new ArrayList<Doc>();
//	}
//	
//}
