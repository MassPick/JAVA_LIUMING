package cn.com.szgao.lm.test_c;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.AdministrationUtils;
import cn.com.szgao.wash.data.DataUtils;

@SuppressWarnings("unused")
public class Test {

	/**
	 * 跳出当前if
	 * 
	 * @param args
	 * @author liuming
	 * @Date 2015-9-14 下午3:12:28
	 */
	public static void main(String[] args) {

		// 未解决的：  
		// 已解决的：甘肃矿区人民法院 "甘肃省甘南州合作市人民法院",   "天津市和平区人民法院"  吉林省大安市人民法院(省名和市名相同)  北京朝阳区人民法院 婺城法院(县级区) 葛洲坝人民法院 江西省西湖区法院 海东中级人民法院 金华磐安法院 湖北省沙洋人民法院
		// 武义法院

	        
		AdministrationUtils util = new AdministrationUtils();
		util.initData();
		
//		 String ss1[]={ "吉林省大安市人民法院"};
		String ss1[] = {"天津市和平区人民法院","[江西]抚州市临川区人民法院","辽宁朝阳人民法院","天津市滨海新区人民法院","天津市大港区人民法院","北京市通州区人民法院" };
//		 String ss1[]={"庆云人民法院","婺城法院","婺城区法院","赣榆县AA公司" };
//		 String ss1[]={"海南省洋浦经济开发区人民法院","红河州中级人民法院","海东中级人民法院","阿克苏XX法院","南京市中级法院","青海省海东中级法院","荔浦县人民法院","重庆市渝中区人民法院","东莞市第二人民法院","婺城法院","婺城区法院","赣榆县AA公司"		 };
		for (String str : ss1) {
			String[] array = util.utils(str);
			for (String aaa : array) {
				System.out.println(aaa);
			}
			System.out.println("-------------------------------------------");
		}
		
//		 String str = "!!！？？!!!!%*）%￥！KTV去符号标号！！当然,，。!!..**半角";  
//	        System.out.println(str);  
//	        String str1 = str.replaceAll("[\\pP\\p{Punct}]", "");  
//	        System.out.println("str1:" + str1);  
	        
//	        
//	        String str2 = "!!！？？!!!!%*）%￥！KTV去符号标号！！当然,，。!!..**半角 ;·山东省胶州市人民法院 ".replaceAll( "[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "");  
//	        System.out.println( str2);  
//	        
	        
//	        str = ",.!，，D_NAME。！；‘’”“《》**dfs  #$%^&()-+1431221中国123漢字かどうかのjavaを決定";  
//	        str = "!!！？？!!!!%*）%￥！KTV去符号标号！！当然,，。!!..**半角 ; ".replaceAll("[\\pP‘’“”]", "");  
//	        System.out.println(str);  
	        
//	        System.out.println(StringUtils.removePunctuation("№¿¬ìå【？?？★?／¿¬ìå<陕西省澄城县人民法院"));
		
		

//		String str = "-南京市栖霞区人民法院".replaceAll("-", "");
//		System.out.println(str);

		// 判断身份证是否包含“无”等字段
		// if (!StringUtils.isNull("-")) {
		// String tempIdNum = "-".trim();
		// if ("无".equals(tempIdNum)||"0".equals(tempIdNum)
		// || "000000000000000".equals(tempIdNum)
		// ||
		// "000000000000000000".equals(tempIdNum)||StringUtils.isSameChars(tempIdNum))
		// {
		// //vo.setIdNum(null);
		// System.out.println("AAA");
		// }else if(tempIdNum.length()<15){
		// //vo.setIdNum(null);
		// System.out.println("22");
		// }
		// else if(!StringUtils.isCardId(tempIdNum)){//判断是否为18位或15位
		// //vo.setIdNum(null);
		// System.out.println("BB");
		// }
		// else {
		// // 统计转化为大写 X
		// //vo.setIdNum(tempIdNum.toUpperCase());
		// System.out.println("CC");
		// }
		// }

		// String s="(２００7)盐民二初字第70号";
		// System.out.println(ToDBC(s));
		// System.out.println("2007");
		// System.out.println(ToSBC(s));
		// 跳出if
		/*
		 * for (int i = 0; i < 10; i++) { out:if (!"null".equals("")) { String
		 * url = ""; if(!"".equals(url.trim())){ System.out.println("out1");
		 * break out; };
		 * 
		 * if("".equals(url.trim())){ System.out.println("out2"); break out; };
		 * // code1 if(true){ System.out.println("22222222222"); } } // code2
		 * if("2"=="2"){ System.out.println("这是后面一个IF"); } }
		 */

		// int[][] m = { { 1, 2, 3, 1 }, { 1, 3 }, { 3, 4, 2 } };
		// int sum = 0;
		// for (int i = 0; i < m.length; i++) { // 循环第一维下标
		// // sum += m[i].length; //第二维的长度相加
		// for (int j = 0; j < m[i].length; j++) {
		// int is = m[i][j];
		// }
		// System.out.println(m[i]);
		// }

		// String[] names = { "刘备", "曹操", "诸葛亮" };
		// List list1 = Arrays.asList(names);
		// list1 = new ArrayList(list1);
		// for (Object object : list1) {
		// System.out.println(object);
		// }

		// System.out.println( "扶余县 人民法院 ".replaceAll("[ |　]", " ").replace(" ",
		// ""));

		// String ss="AAA长寿县XX公司";//再好拆到"AAA长寿"、"县XX公司" 这时就无法找到对应的，所以还有最后合并后的查询
		// String ss="A寿县XX公司";
		// String ss="北京市宣武区XX公司";//北京市宣武 区XX公司
		// String ss="新疆巴哈密地区巴里坤哈萨克自治县宝宝公司";
		// String ss="新疆巴里坤哈萨克自治县宝宝公司";
		// String ss="新疆巴里坤哈萨克自治县中级人民法院";
		// String ss="宣武区XX公司";//北京市宣武 区XX公司
		// String ss="丰南市XX公司";
		// String ss="离石区XX公司";
		// String ss="重庆市梁平县人民法院";
		// String ss="金华武义法院";//婺城法院
		// String ss="金华武义法院";//婺城法院
		// DataUtils connUtil = new DataUtils();
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}

}
