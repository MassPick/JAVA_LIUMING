package cn.com.szgao.test;

import cn.com.szgao.wash.data.AdministrationUtils;

public class Test_ShenShiXian {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AdministrationUtils util =new AdministrationUtils();
		util.initData(); // 查询行政区
		String[] array=util.utils("华盖创意（北京）图像技术有限公司与南京新模式软件集成有限公司侵犯著作财产权纠纷二审民事判决书江苏省高级人民法院");
		System.out.println("省： "+array[0]+" 市： "+array[1]+" 县： "+array[2]);
		
		
		
	}

}
