package cn.com.szgao.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.StringUtils;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println("哈".split("-")[0]);
		// System.out.println("sssss " + null);
		// System.out.println(DateUtils.getDateyyyyMMddhhmmssZZ());

		// System.out.println(""+null);
		// System.out.println();
		//
		// //龙宽玉\u0000
		// //股东
		// String s="龙宽玉\u0000";
		// if(s.indexOf("\u0000")!=-1){// //
		// System.out.println(s.substring(0,s.indexOf("\u0000")));// // } ////
																	// holder_type
																	// // String
																	// sa="执行董事兼经理\u003c!DOCTYPEHTMLPUBLIC\"-//W3C//DTD";
		// if(sa.indexOf("\u003c")!=-1){// //
		// System.out.println(sa.substring(0,sa.indexOf("\u003c")) // // );// } //
																// //holder
																//
																// sa="白慧中Struts";
																// if(sa.indexOf("Struts")!=-1){
																// System.out.println(sa.substring(0,sa.indexOf("Struts"))
																// );
																// }
																//// 白慧中Struts
																//
																//
																// //注册号
																// "regNum":"350500400858\u0000"
		// sa="350500400858\u0000";// if(sa.indexOf("\u0000")!=-1){// //
		// System.out.println(sa.substring(0,sa.indexOf("\u0000")) // //
																// );//
																// }

//		String a = "《中华人民共和国道路交通安全法实施条例》 （十）项";
//		System.out.println(StringUtils.replaceSZ(a));
		
//		String ss[]="《中华人民共和>国刑法》b第2百四，《中华人民共和国刑法》第一百324。《中华人民共和国刑法》第一百四十四条</br>《中华人民共和国刑法》".split("[</br>。，]");
//		for (int i = 0; i < ss.length; i++) {
//			System.out.println(ss[i]);
//		}
		
//		System.out.println("《最高人民法院关于适用<；中华人民共和国刑事诉讼法>；的解释》第一百五十一条".replace("<；", "").replace("<；", ">；"));
		
		
		String s1="《中华人民共和国婚姻法》第三十七条／第四十二条";
		 String aa[]=s1.split("[／/、]");
		 for (int i = 0; i < aa.length; i++) {
			System.out.println(aa[i]);
		}
		
		
		
//		System.out.println(StringUtils.getChinseSZ(0));
//		System.out.println(StringUtils.getChinseSZ(122));
//		System.out.println(StringUtils.getChinseSZ(1202));
//		System.out.println(StringUtils.getChinseSZ(12002));
		
		// List<String> digitList = new ArrayList<String>();
//		Pattern p = Pattern.compile("[0-9\\.]+");
//		Matcher m = p.matcher(a);

//		while (m.find()) {
//			System.out.print(m.group() + ",");
//		}
		
//		System.out.println(digui(4));  4*3*2*1

	}
	
	
//	public static String  replaceSZ(String str) {
//		Pattern p = Pattern.compile("[0-9\\.]+");
//		Matcher m = p.matcher(str);
//		if(m.find()){
//			
//			String m_t=m.group();
//			String m_1= str.substring(0, str.indexOf(m_t)    );
//			String m_2= str.substring(  str.indexOf(m_t) +m_t.length()  );
//			
//			Integer ta = Integer.valueOf(m_t);
//			String s_max = StringUtils.getChinseSZ(ta);
//			
//			String res=m_1+s_max+m_2;
//			
//			return replaceSZ(res);
//		}else{
//			return str;
//		}
//	}

	public static long digui(int n) {
		if (n <= 1)
			return 1;
		else
			return digui(n - 1) * n;
	}

}
