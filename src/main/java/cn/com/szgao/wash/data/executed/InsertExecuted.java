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
import cn.com.szgao.enterprise_c.EnterpriceJSONDataExcuted;
import cn.com.szgao.enterprise_c.RunnableJson;
//import cn.com.szgao.enterprise.EnterpriceJSONDataBreakFaith;
//import cn.com.szgao.enterprise.EnterpriceJSONDataExcuted;
//import cn.com.szgao.enterprise.RunnableJson;
//import cn.com.szgao.enterprise.RunnableJsonBreakFaith;
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
public class InsertExecuted {

	// 日志对象
	private static Logger log = LogManager.getLogger(InsertExecuted.class);

	/**
	 * @param args
	 */
	@SuppressWarnings({ })
	public static void main(String[] args) {
		
//		PropertyConfigurator
//				.configure("D:\\workSpace2\\workSpace\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		
		PropertyConfigurator
		.configure("D:\\data\\git\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		
		//D:\data\git\MassPick
		DataUtils connUtil = new DataUtils();
		AdministrationUtils util = new AdministrationUtils();
		// 查询行政区
		util.initData();
		
//		EnterpriceJSONDataExcuted fileIntoDataBase2=new EnterpriceJSONDataExcuted();
//		File file=new File("C:\\lm\\data\\pc6");
		
		ScheduledThreadPoolExecutor excuter=new ScheduledThreadPoolExecutor(4);
		
//		excuter.execute(new RunnableJson(new File("C:\\data\\被执行人\\108200022-110900001\\excute1"),new EnterpriceJSONDataExcuted()));
//		excuter.execute(new RunnableJson(new File("C:\\data\\被执行人\\108200022-110900001\\excute2"),new EnterpriceJSONDataExcuted()));
//		excuter.execute(new RunnableJson(new File("C:\\data\\被执行人\\108200022-110900001\\excute3"),new EnterpriceJSONDataExcuted()));
//		excuter.execute(new RunnableJson(new File("C:\\data\\被执行人\\108200022-110900001\\excute4"),new EnterpriceJSONDataExcuted()));
		
		excuter.execute(new RunnableJson(new File("C:\\data\\被执行人\\110900001-112150014\\110900001-112150014\\excute1.json"),new EnterpriceJSONDataExcuted()));
		excuter.execute(new RunnableJson(new File("C:\\data\\被执行人\\110900001-112150014\\110900001-112150014\\excute2.json"),new EnterpriceJSONDataExcuted()));
		excuter.execute(new RunnableJson(new File("C:\\data\\被执行人\\110900001-112150014\\110900001-112150014\\excute3.json"),new EnterpriceJSONDataExcuted()));
		excuter.execute(new RunnableJson(new File("C:\\data\\被执行人\\110900001-112150014\\110900001-112150014\\excute4.json"),new EnterpriceJSONDataExcuted()));
		excuter.shutdown();
		
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
