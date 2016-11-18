package cn.com.szgao.court.esAndcb;

import java.io.Serializable;
import cn.com.szgao.dto.WholeCourtVO;
public class Rows implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String key;
	private WholeCourtVO value;
	 
	
	public WholeCourtVO getValue() {
		return value;
	}
	public void setValue(WholeCourtVO value) {
		this.value = value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	

}
