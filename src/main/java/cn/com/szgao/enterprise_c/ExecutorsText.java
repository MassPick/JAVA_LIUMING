package cn.com.szgao.enterprise_c;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.log4j.PropertyConfigurator;

import cn.com.szgao.wash.data.DataUtils;
/**
 * 山东、辽宁、重庆、江苏
 * 
 * @author xiongchangyi
 *
 */
public class ExecutorsText {
	static{//D:\maven_test\Mass_1\src\main\resources\log
//		PropertyConfigurator.configure("D:\\maven_test\\Mass_1\\log\\log4j.properties");
//		PropertyConfigurator.configure("D:\\maven_test\\Mass_1\\src\\main\\resources\\log\\log4j.properties");
		
		PropertyConfigurator
		.configure("D:\\workSpace2\\workSpace\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		DataUtils.initData();
	}
	public static void main(String[] args) {	
		ScheduledThreadPoolExecutor excuter=new ScheduledThreadPoolExecutor(1);		
		excuter.execute(new RunnableJson2(new File("D:\\data\\已入库zhixing_data\\已入库zhixing_data\\pc1\4.30-5.4"),new EnterpriceJSONDataExcuted()));
		excuter.shutdown();
	}

}
