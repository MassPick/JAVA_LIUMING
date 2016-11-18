package cn.com.szgao.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocUtils {
	
	/**
	 * 读doc 2007
	 * @param filePath
	 * @return   
	 * @return String  
	 * @author liuming
	 * @date 2016年11月14日  下午3:04:49
	 */
	@SuppressWarnings("resource")
	public static String readWord2007(String filePath) {
		String res = "";
		try {
			OPCPackage oPCPackage = POIXMLDocument.openPackage(filePath);
			XWPFDocument xwpf = new XWPFDocument(oPCPackage);
			POIXMLTextExtractor ex = new XWPFWordExtractor(xwpf);
			res = ex.getText();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 读doc 2003
	 * @param filePath
	 * @return   
	 * @return String  
	 * @author liuming
	 * @date 2016年11月14日  下午3:04:49
	 */
	@SuppressWarnings("resource")
	public static String readWord2003(String filePath) {
		String res = "";
		try {
			FileInputStream fis = new FileInputStream(filePath);
			WordExtractor wordExtractor = new WordExtractor(fis);
			res = wordExtractor.getText();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
}
