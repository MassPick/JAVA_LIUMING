package cn.com.szgao.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import cn.com.szgao.dto.ABC;
import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.ReportGuaranteeVO;
import cn.com.szgao.enterprise.FileIntoDataBase2p5;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.ObjectUtils;
import cn.com.szgao.util.StringUtils;

public class TestFile {
	private static Logger log = LogManager.getLogger(FileIntoDataBase2p5.class.getName());
	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}
	
	public static void main(String[] args) {
		File file=new File("E:/工商数据已去重/北京市180000.json");
		test(file,34431);
	}

	@SuppressWarnings("unused")
	public static void test(File file, int line) {
		String encoding_from = "UTF-8";// GB18030
		BufferedReader reader = null;
		try {
			// 华 GB18030

			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(new FileInputStream(file), encoding_from);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		int readNum = 0;
		String tempT = null;
		
		
		String filePathUn = "E:\\jilu.txt" ;
		File fileSUn = new File(filePathUn);
		String encoding_from1U = "UTF-8";
		BufferedWriter fwUn = null;
		try {
			if (!fileSUn.exists()) {
				try {
					fileSUn.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fwUn = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
				fileSUn.delete();
				fileSUn = new File(filePathUn);
				try {
					fwUn = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			while ((tempT = reader.readLine()) != null) {
				readNum++;
				int M=1;//10M上限
				
				long size=tempT.getBytes().length;
				System.out.println(readNum+"  "+ size);
				
				if(readNum==34432||readNum==37546){//37546  124519992
					               //34432  124519992
					System.out.println("------------");
					log.error("--------数据大于1M :"+tempT.substring(0, 500));
					size=0;
					continue;
				}
				
				if(size>M*1024*1024){
					writerString(fwUn, tempT);
//					log.error("--------数据大于1M :"+tempT);
					break;
				}
				
//				System.out.println(readNum);
//				if (readNum < 34430) {
//					continue;
//				}
//				if (readNum == 34430) {
//					writerString(fwUn, tempT);
//					break;
//				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + "\n");

			// fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			log.error("写文件异常" + e.getMessage());
		} finally {
			// if (fw != null) {
			// try {
			// fw.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}

}
