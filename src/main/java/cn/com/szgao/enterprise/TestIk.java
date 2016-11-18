package cn.com.szgao.enterprise;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.com.szgao.util.StringUtils;

public class TestIk {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Analyzer anal = new IKAnalyzer();
//		StringReader reader = new StringReader("生产建筑工程机械");
//		// 分词
//		TokenStream ts = null;
//		ts = anal.tokenStream("", reader);
//		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
//		try {
//			ts.reset();
//
//			String strs = "";
//			// 遍历分词数据
//			while (ts.incrementToken()) {
//				String temp = term.toString();
//				 System.out.println(temp);
//				// // getIndustry(temp);
//				strs += term.toString() + "|";
//				// System.out.print(term.toString() + "|" );
//			}
//			reader.close();
//			// String[] arrStr1 = strs.split("\\|");
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		String str = "马云和马化腾是两个牛人";
//        IKAnalysis(str);
		
		
		String text="湖南省盐城市博马云奕网络技术有限公司张三,李四小明,王五";  
		
        StringReader sr=new StringReader(text);  
        IKSegmenter ik=new IKSegmenter(sr, true);  
        Lexeme lex=null;  
        try {
			while((lex=ik.next())!=null){  
			    System.out.println(lex.getLexemeText());  
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	
	 public static String IKAnalysis(String str) {
	        StringBuffer sb = new StringBuffer();
	        try {
	            byte[] bt = str.getBytes();
	            InputStream ip = new ByteArrayInputStream(bt);
	            Reader read = new InputStreamReader(ip);
	            IKSegmenter iks = new IKSegmenter(read, false);
	            Lexeme t;
	            while ((t = iks.next()) != null) {
	                sb.append(t.getLexemeText() + " , ");
	            }
	            sb.delete(sb.length() - 1, sb.length());
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        System.out.println(sb.toString());
	        return sb.toString();
	    }
	 
	 

}
