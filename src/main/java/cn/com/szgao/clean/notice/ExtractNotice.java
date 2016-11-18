package cn.com.szgao.clean.notice;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;
import cn.com.szgao.dto.NoticeVO;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.wash.data.AdministrationUtils;

/**
 * 公告HTML抓取写入couchbase
 * @author Administrator
 *
 */
public class ExtractNotice {
	private static Logger logger = LogManager.getLogger(ExtractNotice.class.getName());
	public static String[] charset = {"utf-8","gbk","gb2312","gb18030","big5"};
	public static  String[] ERCOEDING={"й","෨","Ժ","ۼ","ҩ","ල","ɷ","ص","δ","ġ","Ϊ","ط","Ϣ","ȡ","Ӫ","ã","","Դ","ڲ","Ѱ","�"};
	public static String [] source = {""};
	static long count = 0;  //总数
	static Map<String, String> MAPS = new HashMap<String, String>();
	static {
		MAPS.put("html", "html");
		MAPS.put("htm", "htm");
	}
	static long ERRORSUM=0;	//错误数据
	static long INPUTSUM=0;	//已插入数据
	static long REPEATSUM=0;
	//所有文书类型
	private static String[] suitype= {"起诉状副本及开庭传票","裁判文书","公示催告","开庭传票","破产文书","起诉状、上诉状副本","宣告失踪、死亡","执行文书","其他","其它"};
	//抽取公告内容关键字
	private static String[] keywordke1= {"：原告","希望","票号","：我院受理",":我院受理","我院受理","：本院受理的",":本院受理的",":本院受理","：本院受理","本院受理","：本院","： 本院","本院于","关于申请人","申请人","上诉人","：关于申请执行人","本院受理",":","："};
	private static String[] keywordke2= {"现通知你们自公告之日起","现通知你自公告之日起","你司自公","现本院依法","限你们自公告之日起","限你自公告之日起","限你们自本公告之日起","限你于公告之日",
		"限你自本公告之日起","限你自本公告","本判决自公告之日起","自发出公告之日起","自发出本公告之日起","自本公告发出之日起","本公告自发出之日起","自本公告见报之日起","你们自公告之日起","你自公告之日起","现自公告之日起",
		"自公告之日","自本公告之日","自公告发出之日起","自判决公告之日起","。。[","出票人","。"};
	//抽取公告主体键字
	private static String[] keywordke4= {":原告","：原告",":我院受理","：我院受理",":本院受理","：本院受理","：本院","： 本院","因银行","因其持有","因遗失","破产清算一案","申请破产","：上诉人","：关于申请执行人","：关于申请人",":","：","公司"};
	/**
	 * 公告抽取
	 * 更新couchbase
	 * 抓取HTML修改courtPub
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		//清空不用的文件或日志
//		File filepor=new File("E:\\Company_File\\log4j-0811\\Java1\\batchImport.log");   
//		if(filepor.exists()){
//			filepor.delete();			
//		}
//		filepor=null;
		PropertyConfigurator.configure("F:\\work\\WorkSpace_Eclipse\\WorkSpace_Eclipse\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		long da = System.currentTimeMillis();
		File file = new File("D:\\Notice\\法院公告\\QG\\new");
		AdministrationUtils util =new AdministrationUtils();
		try {
			show(file,util);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			file = null;
		}
		logger.info(count + ":数量");
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");
	}
	
	//链接查询的桶
	private static JsonObject connectionBucket1(String value){
		JsonDocument doc = null;
		JsonObject obj = null;
			while (true) {
				try {
					doc = JsonDocument.create(value);
					Bucket bucket = connectionCouchBaseNotice1();
					obj = bucket.get(doc) == null ? null : bucket.get(doc).content();
					bucket = null;
					break;
			}catch(Exception ee){
				logger.error("超时重写："+ee.getMessage());
			}
		}
			return obj;
	}
	//写入的桶
	private static void connectionBucket2(JsonDocument doc){
		if (null == doc) {
			return;
		}
		Bucket bucket2  =  null;
			while(true){	
				try{
					bucket2 = connectionCouchBaseNotice2();//链接写入的桶
					bucket2.upsert(doc);
					bucket2 = null;
					break;
				}
				catch(Exception ee){
					logger.error("超时重写："+ee.getMessage());
				}
			}
	}
	
	
	/**
	 * 递归遍历html文件
	 * @param file
	 * @throws
	 * @throws Exception
	 */
	private static void show(File file,AdministrationUtils util) throws Exception {
		String html = null;
		NoticeVO notice = null;
		List<NoticeVO> notices = null;
		Document doc;
		notices = new ArrayList<NoticeVO>();
		int i = 0 ;
			try {
				if (file.isFile()) {
					notice = new NoticeVO();
					logger.info(file.getPath());
					String suffix = file.getName();
					suffix = suffix.substring(suffix.indexOf(".") + 1, suffix.length());
					suffix = MAPS.get(suffix);
					if (null == suffix) {
						return;
					}
					for(String val : charset){//匹配不同编码格式
						doc = Jsoup.parse(file, val);
						html = doc.body().text();//取页面body标签中所有内容
						boolean Garbled = getErrorCode(html);//判断编码是否错误
						if (Garbled == false) {
//							logger.info(   "页面编码错误！！！");
							i++;
							if(i==5){ html = null; }
							continue;
						}
						i=0;
						break;
						}
					html = getReplaceAll(ExtractText(html));
					notice.setPubPerson(getPubPerson(html));//公告人
					notice.setClient(getClient(html));//公告主体
					notice.setPubDate(getPubDate(html));//公告日期
					notice.setPubContent(getPubContent(html));//公告内容
					notice.setPubType(getPubType(html));//公告类型
					notice.setProvince(getProvince(html));//省
					notice.setSource(getSource(html));
					notice.setUuid(file.getName().substring(0,file.getName().lastIndexOf(".")));//修改时需要，新增必须注释本行
					showLog(notice);
					notices.add(notice);
					logger.info("<<--------------------------end---------------------------->>");
					boolean result = updateJsonDataNotice(notices,util);//进入修改
					logger.info("<<-------------------------result----------------------------->>"+result);
					if (!result) {
						ERRORSUM++;
						logger.info(file.getPath() + ":更新失败");
					}
//					count += notices.size();
					notices = null;
					return;
				}
				logger.info("<<----------------------------end-------------------------->>");
				File[] files = file.listFiles();
				for (File fi : files) {
					if (fi.isFile()) {
						notice = new NoticeVO();
						logger.info(fi.getPath());
						String suffix = fi.getName();
						suffix = suffix.substring(suffix.indexOf(".") + 1, suffix.length());
						suffix = MAPS.get(suffix);
						if (null == suffix) {
							continue;
						}
						for(String val : charset){//匹配不同编码格式
							doc = Jsoup.parse(fi, val);
							html = doc.body().text();//取页面body标签中所有内容
							boolean Garbled = getErrorCode(html);//判断编码是否错误
							if (Garbled == false) {
//								logger.info(   "页面编码错误！！！");		
								i++;
								if(i==5){
									html = null;
								}
								continue;
							}
							i=0;
							break;
							}
						//
						html = getReplaceAll(ExtractText(html));  //  截取公告
						notice.setPubPerson(getPubPerson(html));//法院名
						notice.setClient(getClient(html));//公告主体
						notice.setPubDate(getPubDate(html));//公告日期
						notice.setPubContent(getPubContent(html));//公告内容
						notice.setPubType(getPubType(html));//公告类型
						notice.setProvince(getProvince(html));//省
						notice.setSource(getSource(html));
						notice.setUuid(fi.getName().substring(0,fi.getName().lastIndexOf(".")));//修改时需要，新增必须注释本行
//						showLog(notice);
						notices.add(notice);
						if (notices.size() >= 1000) {
						boolean result = updateJsonDataNotice(notices,util);//进入修改
//						count += notices.size();
						if (!result) {
							ERRORSUM++;
							logger.info(fi.getPath() + ":更新失败1");
						}
							notices = null;
							notices = new ArrayList<NoticeVO>();
						}
					} 
					else if (fi.isDirectory()) {
						logger.info(fi.getName());
						show(fi,util);
					}
					else {
						continue;
					}
				}
				if (null != notices && notices.size() > 0) {
				boolean result = updateJsonDataNotice(notices,util);//进入修改
//				count += notices.size();
					if (!result) {
						logger.info("更新失败2");
					}
					notices = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 去掉特殊字符
	 * @param value
	 * @return
	 */
	 public static String getReplaceAll(String value){
		 StringBuffer sb=null;				  
		   if(value!=null&&!"".equals(value)){
			    value=value.replaceAll(",","，");
			    value=value.replaceAll("�","O");
			    value=value.replaceAll("[×,X,Ｘ,x,╳,＊,\\*]","某");
//			    value=value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”,・ ,:,<,/>,</,>,a-z,A-Z,-,+,=,},{,.,#,\",',-,%,^,*]","");
			    value=value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,” ,:,<,/>,</,>,a-z,A-Z,-,+,=,},{,.,#,\",',-,%,^,*]","");//去. 因为金额可能带小数点
				value=getSpecialStringALL(value);
			    value=value.trim();
				sb=new StringBuffer();
				sb.append(value);
		   }
		 return sb==null?null:sb.toString();
	 }
   /**
     * 去掉特殊字符
     * @param value
     * @return
     */
    public static String getSpecialStringALL(String value){
    	if(null==value||"".equals(value)){return null;} 
    	char[] chs=value.toCharArray();
    	 StringBuffer sb=new StringBuffer();
    	 for(char c:chs){
    		 if(((int)c)!=12288&&((int)c)!=160){
    			 sb.append(String.valueOf(c));
    		 }
    	 }
    	 return sb.toString();
    } 
    /**
	 * 打印信息
	 * @param notice
	 */
    public static void showLog(NoticeVO notice){
    	if(null==notice){return;}
    	logger.info("<<------------------------------------------------------>>");
		logger.info("机关:"+notice.getClient()+"相关的"+notice.getPubType()+"公告");
		logger.info("发文机构:"+notice.getPubPerson());
		logger.info("公告时间："+notice.getPubDate());
		logger.info("公告内容："+notice.getPubContent());
		logger.info("UUID:"+notice.getUuid());
		logger.info("<<------------------------------------------------------>>");
    }
   
    /**
     * 抽取公告全文
     * @param value
     * @return
     */
    public static String ExtractText(String value){
    	if(value == null && "".equals(value)){return null;}
		try{
			int index = value.lastIndexOf("公告内容");
		    if(index<= 0){return null;}
		    int index1=value.indexOf("下载打印本公告");
		    if(index1 <= 0){return null;}
			return value.substring(index+4,index1);
		}
		catch(Exception e){
			logger.error("提取公告全文出错:"+e.getMessage());
		}			
		return value;
	}
    
  /**
   * 判断是否存在乱码
   * @param value
   * @return
   */
    public static boolean getErrorCode(String value){
    	if(value == null || "".equals(value)){return false;}
    	for(String val:ERCOEDING){
    		int index = value.lastIndexOf(val);
    		if(index <= 0){
    			continue;
    		}
    		return false;
    	}
    	return true;
    }
    
    /**
     * 提取发文机构（法院）
     * @param value
     * @return
     */
	public static String getSource(String value){
		if(value == null || "".equals(value)){return null;}
		try {
			int index1 = value.lastIndexOf("刊登版面");
			int index2 = value.lastIndexOf("。")+1;
			value = value.substring(index2, index1);
		} catch (Exception e) {
			logger.error("提取发文机构出错："+e.getMessage());
		}
//		System.out.println(value);
		return value;
	}
    
    /**
     * 提取公告人
     * @param value
     * @return
     */
    public static String getPubPerson(String value){
    	if(value == null || "".equals(value)){return null;}
		try{
			int index1 = value.lastIndexOf("]");
		    int idnex=value.lastIndexOf("院");
		    if(idnex==-1){return null;}
			return value.substring(index1+1,idnex+1);
		}
		catch(Exception e){
			logger.error("提取公告人出错:"+e.getMessage());
		}			
		return null;
	} 
    
    /**
     * 全国可抽取省
     * @param value
     * @return
     */
    public static String getProvince(String value){
    	if(value == null || "".equals(value)){return null;}
    	try {
			int index =  value.lastIndexOf("[");
			if(index == -1){return null;}
			int index1 =  value.lastIndexOf("]");
			if(index1 == -1){return null;}
			return value.substring(index+1, index1);
		} catch (Exception e) {
			logger.error("提取省份出错:"+e.getMessage());
		}
    	return null;
    }
    /**
     * 提取公告当事人
     * @param value
     * @return
     */
    public static String getClient(String value){
    	if(value == null || "".equals(value)){return null;}
  		try{
  			for(String val:suitype){
  		    int index=value.indexOf(val);
  		    if(index >= 0){
  		    	index = index +val.length();
  		    	value = value.substring(index, value.length());
  		    	for(String val1:suitype){
  		  		    int index1=value.indexOf(val1);
  		  		    if(index1 >= 0){
  		  		    index1 = index1 +val.length();
		  		  for(String val2:keywordke4){
		  			  int index2 = value.indexOf(val2);
		  			  if(index2>=0){
		  				  value = value.substring(index,index2);
		  				int index3 = value.length()/2;
		  				if(value.substring(0,index3).equals(value.substring(index3, value.length()))){
		  					value = value.substring(0,index3);
		  				}
		  				value = getReplaceAll(value);
		  			return value;
		  		  }
		  			}
  		  		    }
  		    	}
  			}
  		}
  		}
  		catch(Exception e){
  			logger.error("提取公告当事人出错:"+e.getMessage());
  		}			
  		return null;
  	}
   
    
    /**
     * 提取日期
     * @param value
     * @return
     */
    public static String getPubDate(String value){
    	if(value == null || "".equals(value)){return null;}
  		try{
  		    int index1 = value.lastIndexOf("刊登日期");
  		    int index2=value.lastIndexOf("上传日期");
  		    if(index1==-1 || index2 == -1){return null;}
  		    value =  value.substring(index1+4,index2);
//  		    System.out.println("公告日期："+value);
  			return value;
  		}
  		catch(Exception e){
  			logger.error("提取日期出错:"+e.getMessage());
  		}			
  		return null;
  	}
    
    /**
     * 提取类型
     * @param value
     * @return
     */
    public static String getPubType(String value){
    	if(value == null || "".equals(value)){return null;}
  		try{
  			for(String val:suitype){
  				int index = value.indexOf(val);
  				if(index >= 0){
  					return val;
  				}
  				if(val == null || "".equals(val)){
  					return "其它";
  				}
  			}
  		}
  		catch(Exception e){
  			logger.error("提取文书类型出错:"+e.getMessage());
  		}			
  		return null;
  	}
    
    /**
     * 得到公告内容（限抽取全国）
     * @param value
     * @return
     */
    public static String getPubContent(String value){
    	if(value == null || "".equals(value)){return null;}
    	if(getPubContent1(value) != null && !"".equals(getPubContent1(value))){
    		return getPubContent1(value);
    	}else if(getPubContent2(value) != null && !"".equals(getPubContent2(value))){
    		return getPubContent2(value);
    	}
    	return null;
    }
    
    /**
     * 抽取公告内容方法1
     * @param value
     * @return
     */
    public static String getPubContent1(String value){
    	if(value == null || "".equals(value)){return null;}
    	String values;
  		  try {
			for(String val:keywordke1){
				  int index=value.indexOf(val);
				    if(index >=0){
				    	index = index+val.length();
				  for(String val2:keywordke2){
					  int index2 = value.lastIndexOf(val2);
					  if(index2>=0){
						  values = value.substring(index,index2);
					  			return values;
					  		}
				  		}
				    }
			  }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			values = null;
		}
  		return value;
    }
    
    /**
     * 抽取公告内容方法2
     * @param value
     * @return
     */
    public static String getPubContent2(String value){
    	if(value == null || "".equals(value)){return null;}
			for(String val : keywordke1){
				int index = value.indexOf(val);
				if(index == -1){continue;}
				value = value.substring(index);
				index = value.indexOf("。");
				value = value.substring(index);
				return value;
			}
    	return null;
    }
    /***
	 * 公告抓取
	 */
	public static boolean updateJsonDataNotice(List<NoticeVO> list,AdministrationUtils util)
			throws Exception {
		if(null==list||list.size()<=0){
			return false;
		}
		String array[] = null;
		JsonDocument doc=null;
		JsonObject obj=null;		
		Gson gson = new Gson();
		NoticeVO notices = null;
		try {
		for(NoticeVO notice:list)
		{
			count++;
			//查询数据	
			obj = connectionBucket1(notice.getUuid());
			if(obj == null){
				logger.info("匹配不到UUID:"+notice.getUuid());
				continue;	
			}
			notices = new NoticeVO();
			//获取couchbase中已有的数据
			if(null!=obj.get("isCourtPub")&&!"".equals(obj.get("isCourtPub").toString())){				
				notices.setIsCourtPub(obj.get("isCourtPub").toString());//  法院公告/非法院公告
			}
			if(null!=obj.get("pubType")&&!"".equals(obj.get("pubType").toString())){				
				notices.setPubType(obj.get("pubType").toString());//	公告的类型
			}
			if(null!=notice.getPubType() && !"".equals(notice.getPubType())){
				notices.setPubType(notice.getPubType());
			}
			if(null!=obj.get("pubPerson")&&!"".equals(obj.get("pubPerson").toString())){				
				notices.setPubPerson(obj.get("pubPerson").toString());//	公告的类型
			}
			if(null!=notice.getPubPerson() && !"".equals(notice.getPubPerson())){
				notices.setPubPerson(notice.getPubPerson());//	公告类型
			}
			if(null!=obj.get("client")&&!"".equals(obj.get("client").toString())){				
				notices.setClient(obj.get("client").toString());	//当事人
			}
			if(null!=notice.getClient() && !"".equals(notice.getClient())){
				notices.setClient(notice.getClient());//当事人
			}
			if(null!=obj.get("pubDate")&&!"".equals(obj.get("pubDate").toString())){				
				notices.setPubDate(DateUtils.getReplaceAllDate(obj.get("pubDate").toString()));	//公告时间
			}
			if(null!=notice.getPubDate() && !"".equals(notice.getPubDate())){
				notices.setPubDate(DateUtils.getReplaceAllDate(notice.getPubDate()));//	公告时间
			}
			if(null!=obj.get("detailLink")&&!"".equals(obj.get("detailLink").toString())){				
				notices.setDetailLink(obj.get("detailLink").toString());	//公告链接（HTML）
			}
			if(null!=obj.get("pdfLink")&&!"".equals(obj.get("pdfLink").toString())){				
				notices.setPdfLink(obj.get("pdfLink").toString());//公告链接（pdf）
			}
			if(null!=obj.get("collectTime")&&!"".equals(obj.get("collectTime").toString())){				
				notices.setCollectTime(DateUtils.getReplaceAllDate(obj.get("collectTime").toString()));//采集时间
			}
			if(null!=obj.get("source")&&!"".equals(obj.get("source").toString())){				
//				notices.setSource(getReplaceAllDate(obj.get("source").toString()));//发文机构
//				array = util.utils(getReplaceAllDate(obj.get("source").toString()));
				notices.setSource( obj.get("source").toString() );//发文机构
				array = util.utils( obj.get("source").toString() );
				
			}
			if(null!=notice.getSource()&&!"".equals(notice.getSource())){				
				notices.setSource(notice.getSource());//发文机构
				array = util.utils(notice.getSource());
			}
			
			if (null != array) {
				if(null != array[0] && !"".equals(array[0])){
					notices.setProvince(array[0]);
				}
				if(null != array[1] && !"".equals(array[1])){
					notices.setCity(array[1]);
				}
				if(null != array[2] && !"".equals(array[2])){
					notices.setArea(array[2]);
				}
			}
			
			if(null!=obj.get("subject")&&!"".equals(obj.get("subject").toString())){				
//				notices.setSubject(getReplaceAllDate(obj.get("subject").toString()));
				notices.setSubject( obj.get("subject").toString( ));
			}
			if(null!=obj.get("pubContent")&&!"".equals(obj.get("pubContent").toString())){				
				notices.setPubContent(obj.get("pubContent").toString());//发文机构
			}
			if(null!=notice.getPubContent() && !"".equals(notice.getPubContent())){
				notices.setPubContent(notice.getPubContent());//	公告时间
			}
			if(null!=obj.get("province")&&!"".equals(obj.get("province").toString())){				
				notices.setProvince(obj.get("province").toString());
			}
			if(null!=notice.getProvince() && !"".equals(notice.getProvince())){
				notices.setProvince(notice.getProvince());//	公告时间
			}
			if(null!=obj.get("city")&&!"".equals(obj.get("city").toString())){				
				notices.setCity(obj.get("city").toString());
			}
			if(null!=obj.get("area")&&!"".equals(obj.get("area").toString())){				
				notices.setArea(obj.get("area").toString());
			}
			if(null!=obj.get("path")&&!"".equals(obj.get("path").toString())){				
				notices.setPath(obj.get("path").toString());
			}
			if(null!=notice.getPath() && !"".equals(notice.getPath())){
				notices.setPath(notice.getPath());
			}
			String jsonss = gson.toJson(notices);
			doc = JsonDocument.create(notice.getUuid(),JsonObject.fromJson(jsonss));
			logger.info("第"+count+"条数据的uuid："+notice.getUuid());
				connectionBucket2(doc);
			
		}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		finally{
			obj=null;
			doc=null;
			
		}
		return true;
	}
	
	 private static Cluster cluster1 = CouchbaseCluster.create("192.168.1.30");
	 private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
	 public static Bucket connectionCouchBaseNotice1(){
			//连接指定查询的桶		
			return cluster1.openBucket("courtPub2");
		}
	 public static Bucket connectionCouchBaseNotice2(){
			//连接指定写入的桶		
			return cluster2.openBucket("courtPub2");
		}
	 
}
