package cn.com.szgao.enterprise_c;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;

public class Tes {
	
	public static void main(String[] args) {
	/*	String id = "150103000049813";
		
		NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
		
		System.out.println(nbg.generate(id).toString());*/
		
		
		for(int i=100;i<10003;i++)
		{
			if(i%100==0)
			{
				System.out.println(i);
			}
		}
	}
	

}
