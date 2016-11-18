package cn.com.szgao.test;

import java.util.BitSet;

public class TestBitSet {
	private static String toBitSet(String str){  
        String result = "[";  
        BitSet bitSet = new BitSet();  
        byte[] strBits = str.getBytes();  
        for(int i = 0; i< strBits.length * 8; i++){  
            if((strBits[i / 8] & (128 >> (i % 8))) > 0){  
                bitSet.set(i);  
            }  
        }  
        for(int i = 0; i < bitSet.length(); i++){  
            if(bitSet.get(i))  
                result += "1";  
            else  
                result +="0";  
        }  
        result += "]";  
        return result;  
    }  
   
    public static void main(String[] args) {  
        String str = "测试一下";  
        System.out.println(toBitSet(str));  
    }  
}
