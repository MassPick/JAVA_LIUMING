package cn.com.szgao.dto;

import java.io.Serializable;

/**
 * 建设通的 荣誉VO  
 * @author liuming
 *
 */
@SuppressWarnings("serial")
public class HonourDetailVO implements Serializable{

	
	public String getHonourhDetailId() {
		return honourhDetailId;
	}
	public void setHonourhDetailId(String honourhDetailId) {
		this.honourhDetailId = honourhDetailId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getIncidentTime() {
		return incidentTime;
	}
	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}
	public String getIncidentLink() {
		return incidentLink;
	}
	public void setIncidentLink(String incidentLink) {
		this.incidentLink = incidentLink;
	}
	public String getHonorIncident() {
		return honorIncident;
	}
	public void setHonorIncident(String honorIncident) {
		this.honorIncident = honorIncident;
	}
	/**
	 * ID UUID
	 */
	private String honourhDetailId;
	/**
	 * 公司名
	 */
	private String companyName;
	/*
	 * 荣誉日期
	 */
	private String incidentTime;
	/**
	 * url
	 */
	private String incidentLink;
	/**
	 * 荣誉内容
	 */
	private String honorIncident;
	
	private String buildId;

	public String getBuildId() {
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

}
