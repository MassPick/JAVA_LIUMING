package cn.com.szgao.wash.data.executed;


import cn.com.szgao.wash.data.AdministrationUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
//import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.dto.ExecutedVO;
//import cn.com.szgao.enterprise.EnterpriceJSONDataBreakFaith;
//import cn.com.szgao.enterprise.EnterpriceJSONDataBreakFaithLog;
//import cn.com.szgao.enterprise.EnterpriceJSONDataExcuted;
//import cn.com.szgao.enterprise.EnterpriceJSONDataExecutedLogNew;
import cn.com.szgao.enterprise.RunnableJson;
//import cn.com.szgao.enterprise.RunnableJsonBreakFaith;
import cn.com.szgao.enterprise_c.EnterpriceJSONDataExecutedLogNew;
import cn.com.szgao.enterprise_c.RunnableJsonExecutedLogNew;
//import cn.com.szgao.enterprise.RunnableJsonBreakFaithLog;
//import cn.com.szgao.enterprise.RunnableJsonExecutedLogNew;
import cn.com.szgao.util.CouchbaseConnect;
//import cn.com.szgao.util.ElasticSearchConnUtils;
//import cn.com.szgao.util.ElasticSearchUtils;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.DataUtils;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.WrapperFilterBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

@SuppressWarnings("unused")
/**
 * 
 * @author liuming
 *
 */
public class InsertExecutedToLogNew {

	// 日志对象
	private static Logger log = LogManager.getLogger(InsertExecutedToLogNew.class);

	static int SIZE=1000;
	
	/**
	 * @param args
	 */
	@SuppressWarnings({ })
	public static void main(String[] args) {
		
		PropertyConfigurator
				.configure("D:\\data\\git\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		DataUtils connUtil = new DataUtils();
		AdministrationUtils util = new AdministrationUtils();
		// 查询行政区
		util.initData();
		
//		EnterpriceJSONDataExcuted fileIntoDataBase2=new EnterpriceJSONDataExcuted();
//		File file=new File("C:\\lm\\data\\pc6");
		
		log.info("----------------------------------------开始 InsertExecutedToLog ");
		log.error("---------------------------------------开始 InsertExecutedToLog");
		
	
		
		//目标文件夹
//		String targetFDir1="D:/lm/log/temp/被执行人/112150014-113345749/data1";
//		File fileDir = new File("D:/lm/log/temp/被执行人/112150014-113345749/data1");
		
		  String[] targetFDir={
//				  "D:/lm/log/temp/被执行人/112150014-113345749B/data1/data",
//				  "D:/lm/log/temp/被执行人/112150014-113345749B/data2/data",
//				  "D:/lm/log/temp/被执行人/112150014-113345749B/data3/data",
//				  "D:/lm/log/temp/被执行人/112150014-113345749B/data4/data"
				  
//				  "D:/lm/log/temp/被执行人/113345749-113925345/data/data",
//				  "D:/lm/log/temp/被执行人/113345749-113925345/data2/data",
//				  "D:/lm/log/temp/被执行人/113345749-113925345/data3/data",
//				  "D:/lm/log/temp/被执行人/113345749-113925345/data4/data"
				  
//				  "D:/lm/log/temp/被执行人/113925345-115065350/data/data",
//				  "D:/lm/log/temp/被执行人/113925345-115065350/data2/data",
//				  "D:/lm/log/temp/被执行人/113925345-115065350/data3/data",
//				  "D:/lm/log/temp/被执行人/113925345-115065350/data4/data"
				  
//				  "D:/lm/log/temp/被执行人/115065350-115901410/data1/data",
//				  "D:/lm/log/temp/被执行人/115065350-115901410/data2/data",
//				  "D:/lm/log/temp/被执行人/115065350-115901410/data3/data",
//				  "D:/lm/log/temp/被执行人/115065350-115901410/data4/data"
				  
//				  "D:/lm/log/temp/被执行人/108200022-110900001/excute1",
//				  "D:/lm/log/temp/被执行人/108200022-110900001/excute2",
//				  "D:/lm/log/temp/被执行人/108200022-110900001/excute3",
//				  "D:/lm/log/temp/被执行人/108200022-110900001/excute4"
				  
//				  "D:/lm/log/temp/被执行人/110900001-112150014/excute1",
//				  "D:/lm/log/temp/被执行人/110900001-112150014/excute2",
//				  "D:/lm/log/temp/被执行人/110900001-112150014/excute3",
//				  "D:/lm/log/temp/被执行人/110900001-112150014/excute4"
				  
//				  "D:/lm/log/temp/被执行人/102649800-104976000/excute1",
//				  "D:/lm/log/temp/被执行人/102649800-104976000/excute2",
//				  "D:/lm/log/temp/被执行人/102649800-104976000/excute3",
//				  "D:/lm/log/temp/被执行人/102649800-104976000/excute4"
				  
//				  "D:/lm/log/temp/被执行人/115901410-117097800"
				  
//				  "D:/lm/log/temp/被执行人/104976000-108200022/2",
//				  "D:/lm/log/temp/被执行人/104976000-108200022/3",
//				  "D:/lm/log/temp/被执行人/104976000-108200022/4"
				  
				  
				  "D:/lm/log/temp/被执行人/117097801-117600000"
				  
				  
				  
				  };
		  File fileDir=null;
		   for(int i=0;i<targetFDir.length;i++)
		   {
			   fileDir=new File(targetFDir[i]);
			   if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
		   }
		 
		System.out.println("完成");
		ScheduledThreadPoolExecutor excuter=new ScheduledThreadPoolExecutor(1);
		
		
		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\117097801-117600000"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
		
		
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\104976000-108200022\\2"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\104976000-108200022\\3"),0,targetFDir[1] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\104976000-108200022\\4"),0,targetFDir[2] ,new EnterpriceJSONDataExecutedLogNew()));
		
		
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\115901410-117097800"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
				
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\102649800-104976000\\filtered_110000000_1.json"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\102649800-104976000\\filtered_110000000_2.json"),0,targetFDir[1] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\102649800-104976000\\filtered_110000000_3.json"),0,targetFDir[2] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\102649800-104976000\\filtered_110000000_4.json"),0,targetFDir[3] ,new EnterpriceJSONDataExecutedLogNew()));
		
		
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\110900001-112150014\\excute1.json"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\110900001-112150014\\excute2.json"),0,targetFDir[1] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\110900001-112150014\\excute3.json"),0,targetFDir[2] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\110900001-112150014\\excute4.json"),0,targetFDir[3] ,new EnterpriceJSONDataExecutedLogNew()));
		
		
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\108200022-110900001\\excute1"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\108200022-110900001\\excute2"),0,targetFDir[1] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\108200022-110900001\\excute3"),0,targetFDir[2] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\108200022-110900001\\excute4"),0,targetFDir[3] ,new EnterpriceJSONDataExecutedLogNew()));
		
		
		
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113925345-115065350\\data\\data"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113925345-115065350\\data2\\data"),0,targetFDir[1] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113925345-115065350\\data3\\data"),0,targetFDir[2] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113925345-115065350\\data4\\data"),0,targetFDir[3] ,new EnterpriceJSONDataExecutedLogNew()));
		
		
		
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\115065350-115901410\\data1\\data"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\115065350-115901410\\data2\\data"),0,targetFDir[1] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\115065350-115901410\\data3\\data"),0,targetFDir[2] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\115065350-115901410\\data4\\data"),0,targetFDir[3] ,new EnterpriceJSONDataExecutedLogNew()));
		
		
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113925345-115065350\\data\\data"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113925345-115065350\\data2\\data"),0,targetFDir[1] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113925345-115065350\\data3\\data"),0,targetFDir[2] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113925345-115065350\\data4\\data"),0,targetFDir[3] ,new EnterpriceJSONDataExecutedLogNew()));
		
		
		
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113345749-113925345\\data\\data"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113345749-113925345\\data2\\data"),0,targetFDir[1] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113345749-113925345\\data3\\data"),0,targetFDir[2] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\113345749-113925345\\data4\\data"),0,targetFDir[3] ,new EnterpriceJSONDataExecutedLogNew()));
		
		
		
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\112150014-113345749\\data1\\data"),0,targetFDir[0] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\112150014-113345749\\data2\\data"),0,targetFDir[1] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\112150014-113345749\\data3\\data"),0,targetFDir[2] ,new EnterpriceJSONDataExecutedLogNew()));
//		excuter.execute(new RunnableJsonExecutedLogNew(new File("C:\\data\\被执行人\\112150014-113345749\\data4\\data"),0,targetFDir[3] ,new EnterpriceJSONDataExecutedLogNew()));
		
		excuter.shutdown();
		
		log.info("----------------------------------------结束 InsertExecutedToLog");
		log.error("---------------------------------------结束 InsertExecutedToLog");
		
//		try {
//			fileIntoDataBase2.show(file);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}

}
