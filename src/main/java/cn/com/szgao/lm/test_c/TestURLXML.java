package cn.com.szgao.lm.test_c;

 
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestURLXML {

	/**
	 * @param args
	 * @author liuming
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws DocumentException 
	 * @Date 2015-10-27 下午3:08:08 
	 */
	public static void main(String[] args) throws  Exception {
//		StringBuffer temp=new StringBuffer();
//		String strHtml="";
//		String url = "http://192.168.0.251:8080/masspick-web-0.1/CXFService.cxf/V1/es/resource/court?groupby=catalog&page_size=10";
//		URLConnection uc = new URL(url).openConnection();
//		                uc.setConnectTimeout(10000);
//		                uc.setDoOutput(true);
//		                InputStream in = new BufferedInputStream(uc.getInputStream());
//		            
//		                Reader rd = new InputStreamReader(in,"UTF-8");
//		                int c = 0;
//		                while ((c = rd.read()) != -1) {
//		                    temp.append((char) c);
//		                }
//		                in.close();
//		                strHtml = temp.toString();
//		                System.out.println(strHtml);
//		                
//		                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();   
//		    	        DocumentBuilder db = dbf.newDocumentBuilder();   
//		    	        
//		    	        Document d = (Document) db.parse(in);   
//		    	        String evtid = ((Element) d).getElementsByTagName("catalog").item(0).getFirstChild().getNodeValue();   
//		                System.out.println(evtid);
		                
//		                String url = "http://192.168.0.251:8080/masspick-web-0.1/CXFService.cxf/V1/es/resource/court?groupby=courtName&page_size=50000";
//		                 SAXReader readXML = new SAXReader();
//		                  Document doc  = readXML.read(url);
//		                  List projects=doc.selectNodes("es/map/entry/key/value");  
//		                  Iterator it=projects.iterator();  
//		                  while(it.hasNext()){  
//		                    Element elm=(Element)it.next();        
//		                    System.out.println(""+elm.getAttribute("courtName")+"  "+elm.getAttribute("count") ); 
//		                  }
		
		
//		try {  
//            SAXReader reader = new SAXReader();  
//            InputStream in = TestURLXML.class.getClassLoader().getResourceAsStream("D:\\temp\\data.xml");  
//            Document doc = reader.read(in);  
//            Element root = (Element) doc.getRootElement();  
//            List projects=doc.selectNodes("es/map/entry/key/value");  
//            Iterator it=projects.iterator();  
//            while(it.hasNext()){  
//              Element elm=(Element)it.next();        
//              System.out.println("catalog:"+elm.getAttribute("catalog")+" count:"+elm.getAttribute("count") ); 
//            }
//        } catch (DocumentException e) {  
//            e.printStackTrace();  
//        }  
		
	 
		getdata();
		                  
	}
	
//	@SuppressWarnings("unchecked")  
//    public static void readNode(Element root, String prefix) {  
//        if (root == null) return;  
//        // 获取属性  
//        List<Attribute> attrs = root.getAttributes();  
//        if (attrs != null && attrs.size() > 0) {  
//            System.err.print(prefix);  
//            for (Attribute attr : attrs) {  
//                System.err.print(attr.getValue() + " ");  
//            }  
//            System.err.println();  
//        }  
//        // 获取他的子节点  
//        List<Element> childNodes = root.elements();  
//        prefix += "\t";  
//        for (Element e : childNodes) {  
//            readNode(e, prefix);  
//        }  
//    }  
	
	public  static void getdata(){
		try {
			   try{  
				   SAXReader reader = new SAXReader();  
			        Document  doc = reader.read(  new File("D:\\WorkDoc\\court.xml")   );  
			        List projects=doc.selectNodes("es/map/entry/value/list");  
			        Iterator it=projects.iterator();  
			        while(it.hasNext()){  
			          Element elm=(Element)it.next();   
			          System.out.println("catalog:"+elm.getAttribute("catalog")+" count:"+elm.getAttribute("count") ); 
			        }  
			             
			    }  catch(Exception ex){  
			       ex.printStackTrace();  
			    }    
			   
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	

}
