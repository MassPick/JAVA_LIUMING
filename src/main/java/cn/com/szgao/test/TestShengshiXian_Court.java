package cn.com.szgao.test;

import cn.com.szgao.util.StringUtils;

//import cn.com.szgao.wash.data.AdministrationUtils;

public class TestShengshiXian_Court {
	public static void main(String[] args) {
		long start=System.currentTimeMillis();

		// 未解决的：  ------------------------河南蒙古族自治县人民法院
		// 已解决的：甘肃矿区人民法院 "甘肃省甘南州合作市人民法院",   "天津市和平区人民法院"  吉林省大安市人民法院(省名和市名相同)  北京朝阳区人民法院 婺城法院(县级区) 葛洲坝人民法院 江西省西湖区法院 海东中级人民法院 金华磐安法院 湖北省沙洋人民法院
		// 武义法院

//		cn.com.szgao.enterprise.AdministrationUtils util = new cn.com.szgao.enterprise.AdministrationUtils();
//		util.initData();
		
		cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();
		util.initData();
		
		String ss1[]={"天津市东丽区人民法院"};//深圳南山区 、鹤岗市南山区人民法院              --------------建绐县人民法院     重庆市双桥区人民法院
		
//		 String ss1[]={ "吉林省大安市人民法院"};
//		String ss1[] = { "甘肃省甘南州合作市人民法院","天津市和平区人民法院","[江西]抚州市临川区人民法院","辽宁朝阳人民法院","天津市滨海新区人民法院","天津市大港区人民法院","北京市通州区人民法院" };
//		 String ss1[]={"庆云人民法院","婺城法院","婺城区法院","赣榆县AA公司" };
//		 String ss1[]={"湖南省长沙县人民法院","青海省大柴旦矿区人民法院","青海省海西自治州中级人民法院","青海省海西自治州中级人民法院","海南省洋浦经济开发区人民法院","红河州中级人民法院","海东中级人民法院","阿克苏XX法院","南京市中级法院","青海省海东中级法院","荔浦县人民法院","重庆市渝中区人民法院","东莞市第二人民法院","婺城法院","婺城区法院","赣榆县AA公司","新疆生产建设兵团第八师中级人民法院","天津市静海区人民法院","上海市金山区人民法院","上海市黄浦区人民法院","上海市虹口区人民法院","上海市奉贤区人民法院","上海市宝山区人民法院","青海省玉树市人民法院","青海省茫崖矿区人民法院","青海省冷湖矿区人民法院"		 };//用时: 1565
//		String ss1[]={"新疆生产建设兵团第八师中级人民法院","天津市静海区人民法院","上海市金山区人民法院","上海市黄浦区人民法院","上海市虹口区人民法院","上海市奉贤区人民法院","上海市宝山区人民法院","青海省玉树市人民法院","青海省茫崖矿区人民法院","青海省冷湖矿区人民法院"};
		 
		 for (String str : ss1) {
			String[] array = util.utils(str);
			for (String aaa : array) {
				System.out.println(aaa);
			}
			System.out.println("-------------------------------------------");
		}
		
		long end=System.currentTimeMillis();
		System.out.println("用时: "+ (end-start) );
	}
}
