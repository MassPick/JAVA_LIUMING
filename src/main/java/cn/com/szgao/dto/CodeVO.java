package cn.com.szgao.dto;

import java.io.Serializable;
/**
 * 存储注册号与信用代码
 * @author dell
 *
 */
public class CodeVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CodeVO(){}
	public CodeVO(String code,int status){
		this.code=code;
		this.status=status;
	}
	/**
	 * 注册号，或信用代码
	 */
	private String code;
	/**
	 *状态    1是注册号   2是信用代码
	 */
	private int status;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	

}
