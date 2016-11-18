package cn.com.szgao.dto;

import java.io.Serializable;import java.util.List;

@SuppressWarnings("serial")
public class RowsFirstIndusrtyVO implements Serializable {
	
	
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
	
	
 
	private  FirstInstryVO  value;
	public FirstInstryVO getValue() {
		return value;
	}
	public void setValue(FirstInstryVO value) {
		this.value = value;
	}
	
	
	
}
