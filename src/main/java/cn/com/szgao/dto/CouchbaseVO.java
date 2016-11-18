package cn.com.szgao.dto;
/**
 * CB对应的对象
 * @author xiongchangyi
 *
 */
public class CouchbaseVO {
	/**
	 * 主键
	 */
	private String key;
	/**
	 * 文档
	 */
	private WholeCourtVO value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public WholeCourtVO getValue() {
		return value;
	}
	public void setValue(WholeCourtVO value) {
		this.value = value;
	}
}
