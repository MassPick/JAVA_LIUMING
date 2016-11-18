package cn.com.szgao.dto;

public class EtpCouchbaseVO {
	/**
	 * 主键
	 */
	private String key;
	/**
	 * 文档
	 */
	private EnterpriseVO value;
	
	public EnterpriseVO getValue() {
		return value;
	}
	public void setValue(EnterpriseVO value) {
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}
