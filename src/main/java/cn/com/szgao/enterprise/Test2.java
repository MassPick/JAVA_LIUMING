package cn.com.szgao.enterprise;

public class Test2 {
	public static void sortStringArray(String[] arrStr) {
		String temp;
		for (int i = 0; i < arrStr.length; i++) {
			for (int j = arrStr.length - 1; j > i; j--) {
				if (arrStr[i].length() < arrStr[j].length()) {
					System.out.println(arrStr[i] +"  "+ arrStr[j] )   ;
					
					temp = arrStr[i];
					arrStr[i] = arrStr[j];
					arrStr[j] = temp;
				}
			}
		}
	}

	public static void main(String[] args) {
//		String[] arrStr = { "yours", "am", "I" };
		
		String strs="内|的|技术开发|技术|开发|技术转让";
		String[] arrStr = strs.split("\\|");
		sortStringArray(arrStr);
		for (int i = 0; i < arrStr.length; i++) {
			System.out.println(arrStr[i]);
		}
		
		
		int i , j ; 
        String s[][] ; 
        s = new String[3][] ; 
        s[0] = new String[2] ; 
        s[1] = new String[3] ; 
        s[2] = new String[2] ; 
        for(i=0 ; i<s.length ; i++) { 
            for(j=0 ; j <s[i].length ; j++) { 
                s[i][j] = new String("我的位置是：" + i + "," + j) ; 
            } 
        } 
        for(i=0 ; i<s.length ; i++) { 
            for(j=0 ; j<s[i].length ; j++) { 
                System.out.println(s[i][j]) ; 
            } 
        } 
		
		
	}
}
