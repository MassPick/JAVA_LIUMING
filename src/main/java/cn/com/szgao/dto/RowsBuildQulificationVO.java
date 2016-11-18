package cn.com.szgao.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RowsBuildQulificationVO implements Serializable {
	
	
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	private String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	
//	public String getValue() {
//		return value;
//	}
//	public void setValue(String value) {
//		this.value = value;
//	}
//	private String value;
	
	
	
//	private List<EtpEventItemVO> value;
//	public List<EtpEventItemVO> getValue() {
//		return value;
//	}
//	public void setValue(List<EtpEventItemVO> value) {
//		this.value = value;
//	}
	

//	private  Doc  value;
//
//	public Doc getValue() {
//		return value;
//	}
//	public void setValue(Doc value) {
//		this.value = value;
//	}
	
 
	private  BuildQulificationVO  value;
	public BuildQulificationVO getValue() {
		return value;
	}
	public void setValue(BuildQulificationVO value) {
		this.value = value;
	}
	
}
