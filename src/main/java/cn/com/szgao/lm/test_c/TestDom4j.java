package cn.com.szgao.lm.test_c;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TestDom4j {

	/**
	 * @param args
	 * @author liuming
	 * @throws DocumentException
	 * @Date 2015-10-27 下午4:20:55
	 */
	public static void main(String[] args) throws DocumentException {
//		SAXReader reader = new SAXReader();
//		File file = new File("D:\\temp\\data.xml");
//		Document document = reader.read(file);
//		Element root = document.getRootElement();
//		List<Element> childElements = root.elements();
//		for (Element child : childElements) {
//			// 未知属性名情况下
//			List<Attribute> attributeList = child.attributes();
//			for (Attribute attr : attributeList) {
//				System.out.println(attr.getName() + ": " + attr.getValue());
//			}
//
//		}
		
//		SAXReader reader = new SAXReader(); 
//		File f=new File("D:\\temp\\data.xml");
//		Document document = reader.read(f); 
//		System.out.println(document);
		
		
		getdata();

	}
	
	
	public static void getdata() throws DocumentException{
		SAXReader reader = new SAXReader();  
		Document  document = reader.read(new File("D:/temp/data2.xml"));  
		Element rootElm = document.getRootElement();  
		Element root1Elm = rootElm.element("userlist");  
		List nodes = root1Elm.elements("item");  
		    for (Iterator it = nodes.iterator(); it.hasNext();) {  
		      Element elm = (Element) it.next();  
		      System.out.println("index:"+elm.attributeValue("index")+" level:"+elm.attributeValue("level")+" nickname:"+elm.attributeValue("nickname")+" country:"+elm.attributeValue("country")+" weiwang:"+elm.attributeValue("weiwang"));       
		   } 
//		    try{  
//		        Document  doc = reader.read(new File("D:/temp/data2.xml"));  
//		        List projects=doc.selectNodes("ReturnInfo/userlist/item");  
//		        Iterator it=projects.iterator();  
//		        while(it.hasNext()){  
//		          Element elm=(Element)it.next();        
//		          System.out.println("index:"+elm.attributeValue("index")+" level:"+elm.attributeValue("level")+" nickname:"+elm.attributeValue("nickname")+" country:"+elm.attributeValue("country")+" weiwang:"+elm.attributeValue("weiwang")); 
//		        }  
//		             
//		    }  catch(Exception ex){  
//		       ex.printStackTrace();  
//		    }    
	}
}
