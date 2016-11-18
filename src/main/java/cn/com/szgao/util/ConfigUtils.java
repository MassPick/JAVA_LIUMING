package cn.com.szgao.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConfigUtils {
	
	
	
	
	private static void loadConfig() {
		Properties props = new Properties();
		while (true) {
			try {
				props = PropertiesLoaderUtils.loadAllProperties("message.properties");
				String ss = props.getProperty("log4j.propertiespath");
				for (Object key : props.keySet()) {
					System.out.print(key + ":");
					System.out.println(props.get(key));
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 由key得到value
	 * @param key
	 * @return
	 */
	public static String  getPropertyValue(String key){
		Properties props = new Properties();
		while (true) {
			try {
				props = PropertiesLoaderUtils.loadAllProperties("message.properties");
				String ss = props.getProperty("log4j.propertiespath");
				if(null==ss){
					return null;
				}else{
					return ss;
				}
 
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
