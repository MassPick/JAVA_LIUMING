package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 建筑资质信息
 * @author liuming
 *
 */
public class BuildQulificationVO implements Serializable{

	 
	private static final long serialVersionUID = 1L;
	
	private String buildId;
	private String companyName;
	/**
	 * 1 全国建设市场监督    0或null是建设通
	 */
	private Integer source;
	
	
	/**
	 * 全国建设市的 资质类型
	 */
	private String qualificationType;
	
	
	public String getQualificationType() {
		return qualificationType;
	}

	public void setQualificationType(String qualificationType) {
		this.qualificationType = qualificationType;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	/**
	 * 新数据的 建设通  qualificationGrade
	 */
	private String update_date;
	

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

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
	private String bulidQulificationId;

	/**
	 * 资质项目
	 */
	private String qualifiedScope;
	/**
	 * 资质编号
	 */
	private String certificateNo;
	
	/**
	 * 有效期
	 */
	private String expireDate;
	
	/**
	 * 资质项目
	 */
	private String qualification_item;
	
	
	 public String getQualification_item() {
		return qualification_item;
	}

	public void setQualification_item(String qualification_item) {
		this.qualification_item = qualification_item;
	}

	/**
	  * 发证时间
	  */
	private String certificateDate;
	
	private String collectTime;
	
	public String getBulidQulificationId() {
		return bulidQulificationId;
	}

	public void setBulidQulificationId(String bulidQulificationId) {
		this.bulidQulificationId = bulidQulificationId;
	}

	public String getQualifiedScope() {
		return qualifiedScope;
	}

	public void setQualifiedScope(String qualifiedScope) {
		this.qualifiedScope = qualifiedScope;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getCertificateDate() {
		return certificateDate;
	}

	public void setCertificateDate(String certificateDate) {
		this.certificateDate = certificateDate;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
