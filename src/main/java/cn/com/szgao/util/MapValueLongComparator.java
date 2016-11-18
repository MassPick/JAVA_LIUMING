package cn.com.szgao.util;

import java.util.Comparator;
import java.util.Map;


/**
 * MAP中的值大小排序
 * @author liuming
 * @Date 2016年11月2日 下午3:49:39
 */
public class MapValueLongComparator  implements Comparator<String> {

	Map<String, Long> base;

	public MapValueLongComparator(Map<String, Long> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}