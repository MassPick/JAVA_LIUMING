package cn.com.szgao.test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import cn.com.szgao.util.StringUtils;

public class Testbit {
	 private static List<BitSet> bitSetList = new ArrayList<BitSet>();  
	  
	    /** 
	     * 问题原因：因为数据库中的key_index设置过大导致内存泄露， 
	     * 我系统中得到的BitSet是要保留在内存中，以便下一次直接使用。 
	     * 因为key_index最初我们库中都是比较小的,所以无问题，在一个 
	     * 开发库中把所有key_index值更新为它自己的id，而恰好这个库中的id是比较大的，就引发了这个问题 
	     * @param args 
	     */  
	    public static void main(String[] args) {  
	        long totalSize = 0;  
	        for (int key_index = 300000; key_index < 305000; key_index++) {  
//	        	String uuid=StringUtils.NBG.generate(String.valueOf(  key_index)).toString();
	        	System.out.println(key_index);
	            BitSet bitSet = new BitSet();  
	            try {  
	                bitSet.set(key_index);  
	                bitSetList.add(bitSet);  
	                totalSize += bitSet.size() / 8 / 1024;  
	                System.out.println("累计：" + totalSize + "kb");  //181960kb  181960kb
	            } catch (Throwable e) {  
	                System.out.println(key_index + "累计：" + totalSize / 1024 + "mb导致了内存溢出");  
	                System.exit(1);  
	            }  
	        }  
	  
	    }  
}
