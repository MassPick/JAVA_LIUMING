package cn.com.szgao.test;

import java.util.BitSet;

import cn.com.szgao.util.StringUtils;

public class BloomFilter {
	/* BitSet初始分配2^24个bit */
	private static final int DEFAULT_SIZE = 50 << 25;
	/* 不同哈希函数的种子，一般应取质数 */
	private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };
	private BitSet bits = new BitSet(DEFAULT_SIZE);
	/* 哈希函数对象 */
	private SimpleHash[] func = new SimpleHash[seeds.length];

	public BloomFilter() {
		for (int i = 0; i < seeds.length; i++) {
			func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
		}
	}

	// 将字符串标记到bits中
	public void add(String value) {
		for (SimpleHash f : func) {
			bits.set(f.hash(value), true);
		}
	}

	// 判断字符串是否已经被bits标记
	public boolean contains(String value) {
		if (value == null) {
			return false;
		}
		boolean ret = true;
		for (SimpleHash f : func) {
			ret = ret && bits.get(f.hash(value));
		}
		return ret;
	}

	/* 哈希函数类 */
	public static class SimpleHash {
		private int cap;
		private int seed;

		public SimpleHash(int cap, int seed) {
			this.cap = cap;
			this.seed = seed;
		}

		// hash函数，采用简单的加权和hash
		public int hash(String value) {
			int result = 0;
			int len = value.length();
			for (int i = 0; i < len; i++) {
				result = seed * result + value.charAt(i);
			}
			return (cap - 1) & result;
		}
	}
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
//		String test1="累计：181812kb";
//		BloomFilter bf=new BloomFilter();
//		System.out.println(bf.contains(test1));
//		
//		bf.add(test1);
//		System.out.println(bf.contains(test1));
		
//		test1="累计：181812kb111";
//		bf.add(test1);
//		System.out.println(bf.contains(test1));
		
		
		BloomFilter bf=new BloomFilter();
		long num=0;
		String str="（2013）昌民一初字第1972号昌吉市人民法院";
//		String str="深圳市赛威特实业有限公司";
		
		for (int jj = 0; jj < 4000000; jj++) {//重的： 15839   重的： 1744      15重的： 357097    30 重的： 27361    50重的： 283990
			System.out.println(jj);
//			str=(str) +jj;
			
//			boolean aa=bf.contains("深圳市赛威特实业有限公司A"+jj);
//			if (!aa) {
//				bf.add("深圳市赛威特实业有限公司A" + jj);
//			} else {
//				num = num + 1;
//			}
			
//			bf.add((str) +jj);
//			if(bf.contains((str) +jj)){
//				num++;
//				System.out.println("-----------------------------------  "+num);
//			}
			
			if(bf.contains( StringUtils.NBG.generate(  (str) +jj).toString()  )  ){
				num++;
				System.out.println("-----------------------------------  "+num);
			}else{
				bf.add(StringUtils.NBG.generate(  (str) +jj).toString()  );
			}
			
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Time : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
		System.out.println("Time : " + (float) (  (float)((endTime - startTime) / 1000) / 60) + "分钟");
		System.out.println("Time : " + (float) ((endTime - startTime) / 1000) + "秒");
		System.out.println("重的： "+num);
		
	}
	
	
	
//	public static void main(String[] args) {
//        int a=0;
//		BloomFilter bo=new BloomFilter();
//		long da=System.currentTimeMillis();
//		for(int i=0;i<1300000;i++){
//			boolean com=bo.contains("深圳市赛威特实业有限公司"+i);
//			System.out.println(com);
//			if(!com){
//				bo.add("深圳市赛威特实业有限公司"+i);
//			}else{
//				a=a+1;
//			}
//		}
//		System.out.println(System.currentTimeMillis()-da);
//		System.out.println(a);
//		//System.out.println("深圳市赛威特实业有限公司".length()<<25);
//	}
	
	
	
	
}