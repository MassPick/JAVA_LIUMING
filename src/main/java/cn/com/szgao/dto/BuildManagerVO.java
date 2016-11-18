package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;


/**
 * 建设通 经营信息
 * @author dell
 *
 */
public class BuildManagerVO implements Serializable{
	/**
	 * ID UUID
	 */
	private String buildmanagerId;
	 
	public String getBuildmanagerId() {
		return buildmanagerId;
	}
	public void setBuildmanagerId(String buildmanagerId) {
		this.buildmanagerId = buildmanagerId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public List<BuildManagerDetailVO> getBuildManagerDetail() {
		return buildManagerDetail;
	}
	public void setBuildManagerDetail(List<BuildManagerDetailVO> buildManagerDetail) {
		this.buildManagerDetail = buildManagerDetail;
	}
	/**
	 * 公司名
	 */
	private String companyName;
	/*
	 * 经营内容
	 */
	private  List<BuildManagerDetailVO> buildManagerDetail;
}
