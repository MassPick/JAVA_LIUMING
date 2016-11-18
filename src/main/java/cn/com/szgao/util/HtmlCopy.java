package cn.com.szgao.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
public class HtmlCopy {
	private static Logger logger = LogManager.getLogger(HtmlCopy.class.getName());
	static Map<String, String> MAPS = new HashMap<String, String>();
	static {
		MAPS.put("html", "html");
		MAPS.put("htm", "htm");
		MAPS.put("txt", "txt");
		MAPS.put("swf", "swf");
		MAPS.put("png", "png");
	}
	static long count = 0;// 总数量
	public static void main(String[] args) throws Throwable {
		PropertyConfigurator
		.configure("D:\\workSpace2\\workSpace\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
		long da = System.currentTimeMillis();
		File file = new File("E:\\temp\\福建省");
		try {
			first(file);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			file = null;
			logger.info("总文件数："+count);
			logger.info("所有文件总耗时"+ (((System.currentTimeMillis() - da) / 1000) / 60 ) + "分钟");
		}
	}
	public static void first(File file) throws Throwable {
			if (file.isFile()) {
				String suffix = file.getName();
				suffix = suffix.substring(suffix.indexOf(".") + 1,
						suffix.length());
				suffix = MAPS.get(suffix);
				if (null == suffix) {
					return;
				}
				logger.info("网址:" + file.getPath());
				createFileTxt(file);
				return;
			}
			File[] files = file.listFiles();
			for (File fi : files) {
				if (fi.isFile()) {
					String suffix = fi.getName();
					suffix = suffix.substring(suffix.indexOf(".") + 1,
							suffix.length());
					suffix = MAPS.get(suffix);
					if (null == suffix) {
						return;
					}
					createFileTxt(fi);
				}else if (fi.isDirectory()) {
						logger.info(fi.getName());
						first(fi);
					} else {
						continue;
					}
			}
	}
	
	/**
	 * 根据对象ID生成文件名
	 * 
	 * @param notice
	 * @throws Throwable 
	 */
	public static void createFileTxt(File file) throws Throwable {
		FileWriter fw = null;
		BufferedWriter bw = null;
		String uuid = file.getName().substring(0, file.getName().lastIndexOf("."));
		try {
			count++;
//			String str =file.getParent().substring(1);//获取文件目录，动态在D盘生成相对应的文件目录
//			for(String val : PROVINCE){
//				int index = str.indexOf(val);
//				if(index == -1){continue;}
//				
//			}
//			System.out.println(str);
			File files = new File("C:\\temp\\福建省2");
			
			//如果文件夹不存在则创建    
			if  (!files .exists()  && !files .isDirectory())      
			{       
			    logger.info(files+"//不存在");  
			    files .mkdirs();  //如果该磁盘不存在此目录就自动生成，且可以生成多级目录
			}
			fw = new FileWriter(files +"/" + uuid + ".html", true);
			bw = new BufferedWriter(fw);
			 logger.info("第:" + count + "条数据ID为：" + uuid);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (bw != null) {bw.close();}
				if (fw != null) {fw.close();}
			} catch (IOException e) {
				logger.error(e);
			}
		}

	}
}
