package cn.com.szgao.dto;

public class ClientVO {	
	public String getClientFrom() {
		return clientFrom;
	}

	public void setClientFrom(String clientFrom) {
		this.clientFrom = clientFrom;
	}

	public String getClientFrom2() {
		return clientFrom2;
	}

	public void setClientFrom2(String clientFrom2) {
		this.clientFrom2 = clientFrom2;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	/*
	 * 当事人前缀
	 */
	private String clientFrom;
	
	/*
	 * 当事人前缀
	 */
	private String clientFrom2;
	
	/**
	 * 分类
	 */
	private String clientType;
	

}
