package cn.com.szgao.lm.test_c;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;

public class TestUUID {

	/**
	 * @param args
	 * @author liuming
	 * @Date 2015-10-23 上午8:59:24 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
		
		String key3=nbg.generate("http://zhixing.court.gov.cn/search/detail?id=5299999").toString();
		System.out.println(key3);
		
		
//		String key1=nbg.generate("http://zhixing.court.gov.cn/search/detail?id=44552607").toString();
//		System.out.println("pc1:  "+key1);
	}
}

