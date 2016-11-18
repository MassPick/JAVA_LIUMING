package cn.com.szgao.dto;

import java.io.Serializable;

/**
 * 清算信息
 * @author dell
 *
 */
public class SettlementVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * uuid
	 */
	private String uuid;
	
	/**
	 * 清算组负责人
	 */
	private String principal;
	/**
	 * 清算组成员
	 */
	private String member;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}

}
