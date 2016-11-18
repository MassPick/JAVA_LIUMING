package cn.com.szgao.dto;

import java.io.Serializable;

/**
 * 会计事务所信息  
 * @author liuming
 * @Date 2016年10月26日 下午3:02:04
 */
public class LawAccountInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url;
	
	/**
	 * 所在地区  北京注协
	 */
	private String belong;
	
	private String collect_time;
	
	/**
	 * 基本信息
	 */
	private LawAccountInfoVO base_info;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getCollect_time() {
		return collect_time;
	}

	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}

	public LawAccountInfoVO getBase_info() {
		return base_info;
	}

	public void setBase_info(LawAccountInfoVO base_info) {
		this.base_info = base_info;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
}
