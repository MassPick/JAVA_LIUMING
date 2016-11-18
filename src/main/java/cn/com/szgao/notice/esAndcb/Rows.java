package cn.com.szgao.notice.esAndcb;

import java.io.Serializable;
//import cn.com.szgao.dto.ArchivesVO;
import cn.com.szgao.dto.NoticeVO;
import cn.com.szgao.dto.WholeCourtVO;
public class Rows implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String key;
	private WholeCourtVO value;
	/*private WholeCourtVO value2;
	public WholeCourtVO getValue2() {
		return value2;
	}
	public void setValue2(WholeCourtVO value2) {
		this.value2 = value2;
	}*/
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
	public WholeCourtVO getValue() {
		return value;
	}
	public void setValue(WholeCourtVO value) {
		this.value = value;
	}
	
	

}
