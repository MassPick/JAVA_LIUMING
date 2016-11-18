package cn.com.szgao.dto;

import java.io.Serializable;

public class CodeIdVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CodeIdVO(){}
	public CodeIdVO(String admini_code,String parent_admini_code,String name){
		this.admini_code=admini_code;
		this.parent_admini_code=parent_admini_code;
		this.name=name;
	}
	private String admini_code;
	private String parent_admini_code;
	private String name;
	public String getAdmini_code() {
		return admini_code;
	}
	public void setAdmini_code(String admini_code) {
		this.admini_code = admini_code;
	}
	public String getParent_admini_code() {
		return parent_admini_code;
	}
	public void setParent_admini_code(String parent_admini_code) {
		this.parent_admini_code = parent_admini_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
