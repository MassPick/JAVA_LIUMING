package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;


/**
 * 建设通\经营信息
 * @author liuming
 *
 */
public class BuildManagerDetailVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String buildId;
	public String getBuildId() {
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	private String companyName;
	
	
	private String buildManagerDetailId;

	public String getBuildManagerDetailId() {
		return buildManagerDetailId;
	}
	public void setBuildManagerDetailId(String buildManagerDetailId) {
		this.buildManagerDetailId = buildManagerDetailId;
	}
	public String getIncidentTime() {
		return incidentTime;
	}
	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}
	public String getIncident() {
		return incident;
	}
	public void setIncident(String incident) {
		this.incident = incident;
	}
	public String getIncidentLink() {
		return incidentLink;
	}
	public void setIncidentLink(String incidentLink) {
		this.incidentLink = incidentLink;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/*
	 * 时间
	 */
	 private String incidentTime;
	 /**
	  * 异常的内容
	  */
	 private String incident;
	 /**
	  * URL
	  */
	 private String incidentLink;
	 
	 
}
