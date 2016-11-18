package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 建设通荣誉信息
 * @author liuming
 *
 */
public class BuildHonourVO implements Serializable{

	 
	private static final long serialVersionUID = 1L;
	
	private String buildId;
	private String companyName;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	/**
	 * ID
	 */
	private String bulidHonourId;
	
	public String getBulidHonourId() {
		return bulidHonourId;
	}

	public void setBulidHonourId(String bulidHonourId) {
		this.bulidHonourId = bulidHonourId;
	}

	public String getIncidentTime() {
		return incidentTime;
	}

	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}

	public String getHonorIncident() {
		return honorIncident;
	}

	public void setHonorIncident(String honorIncident) {
		this.honorIncident = honorIncident;
	}

	public String getIncidentLink() {
		return incidentLink;
	}

	public void setIncidentLink(String incidentLink) {
		this.incidentLink = incidentLink;
	}

	/**
	 * 荣誉时间
	 */
	private String incidentTime;
	
	/**
	 * 内容
	 */
	private String honorIncident;
	
	/**
	 * url
	 */
	private String incidentLink;

	 

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
