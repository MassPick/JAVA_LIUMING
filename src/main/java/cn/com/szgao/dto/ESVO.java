package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 
 * @author liuming
 *
 */
/**
 * @author liuming
 *
 */
@SuppressWarnings("serial")
public class ESVO implements Serializable{
	
	public String index;
	
	public String type;
	public String key;
	public String source;
	
	
	public ESVO(String index, String type, String key, String source) {
		super();
		this.index = index;
		this.type = type;
		this.key = key;
		this.source = source;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	 
}
