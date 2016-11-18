package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;


/**
 * 建筑信息
 * @author liuming
 *
 */
public class BuildVO implements Serializable{

	
	/**
	 * 建设通 建筑师
	 */
	private List<BuildArchitectVO> architects;
	
	
	/**
	 * 全国 建筑师
	 */
	private List<BuildArchitectVO> registrar;
	
	
	/**
	 * 建设通 建筑师 中标信息
	 */
	private List<BuildArchitectDetailVO> successfulBids;
	
	/**
	 * 抓取时间
	 */
	private String get_time;
	 
	/**
	 * 建设通 经营信息
	 */
	private List<BuildManagerDetailVO> managementInfos;
	
	/**
	 * 建设通 荣誉信息
	 */
	private List<BuildHonourVO> honorInfos;
	
	
	/**
	 * 1  全国建设市场监督    0 或null是建设通
	 */
	private Integer source;
	
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	/**
	 * 注册号生成
	 */
	private String buildId;
	private String city;
	private String  companyName;
	private String organizationCode;
	private String detailLink;
	/**
	 * 联系人
	 */
	private String contactPerson;
	/**
	 * 注册号或信息代码 
	 */
	private String businessLicenseNo;
	/**
	 * 联系电话
	 */
	private String contactNumber;
	private String detailAdress;
	private String provice;
	
	private String province;
	                
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	private String collectTime;
	private String contactFax;
	
	
	private String area;
	
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * 荣誉url
	 */
	private String honorLink;
	
	/**
	 * 经营url
	 */
	private String manageLink;
	/**
	 * 安全许可证号
	 */
	private String safetyPermits;
	/**
	 * 公司简介
	 */
	private String companyBrief;
	/**
	 * 行业
	 */
	private String industrial;
	/**
	 * 工商注册地
	 */
	private String commercialRegistered;
	
	
	
	public String getContactFax() {
		return contactFax;
	}
	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}
	/**
	 * 资质
	 */
	private List<BuildQulificationVO> qulification;
	
	private List<BuildQulificationVO> qualificationGrade;
	
	/**
	 * 荣誉信息
	 */
	private List<BuildHonourVO> honourDetail;
	
	
	/**
	 * 经营信息
	 */
	private List<BuildManagerDetailVO> buildManagerDetail;
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	 
	public String getBuildId() {
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}
 
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getDetailLink() {
		return detailLink;
	}
	public void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getBusinessLicenseNo() {
		return businessLicenseNo;
	}
	public void setBusinessLicenseNo(String businessLicenseNo) {
		this.businessLicenseNo = businessLicenseNo;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getDetailAdress() {
		return detailAdress;
	}
	public void setDetailAdress(String detailAdress) {
		this.detailAdress = detailAdress;
	}
	public String getProvice() {
		return provice;
	}
	public void setProvice(String provice) {
		this.provice = provice;
	}
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public List<BuildQulificationVO> getQulification() {
		return qulification;
	}
	public void setQulification(List<BuildQulificationVO> qulification) {
		this.qulification = qulification;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private   String updateTime;
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getHonorLink() {
		return honorLink;
	}
	public void setHonorLink(String honorLink) {
		this.honorLink = honorLink;
	}
	public String getManageLink() {
		return manageLink;
	}
	public void setManageLink(String manageLink) {
		this.manageLink = manageLink;
	}
	public String getSafetyPermits() {
		return safetyPermits;
	}
	public void setSafetyPermits(String safetyPermits) {
		this.safetyPermits = safetyPermits;
	}
	public String getCompanyBrief() {
		return companyBrief;
	}
	public void setCompanyBrief(String companyBrief) {
		this.companyBrief = companyBrief;
	}
	public String getIndustrial() {
		return industrial;
	}
	public void setIndustrial(String industrial) {
		this.industrial = industrial;
	}
	public String getCommercialRegistered() {
		return commercialRegistered;
	}
	public void setCommercialRegistered(String commercialRegistered) {
		this.commercialRegistered = commercialRegistered;
	}
	
	public List<BuildHonourVO> getHonourDetail() {
		return honourDetail;
	}
	public void setHonourDetail(List<BuildHonourVO> honourDetail) {
		this.honourDetail = honourDetail;
	}
	public List<BuildManagerDetailVO> getBuildManagerDetail() {
		return buildManagerDetail;
	}
	public void setBuildManagerDetail(List<BuildManagerDetailVO> buildManagerDetail) {
		this.buildManagerDetail = buildManagerDetail;
	}

	public List<BuildQulificationVO> getQualificationGrade() {
		return qualificationGrade;
	}
	public void setQualificationGrade(List<BuildQulificationVO> qualificationGrade) {
		this.qualificationGrade = qualificationGrade;
	}
	
	public List<BuildArchitectVO> getArchitects() {
		return architects;
	}
	public void setArchitects(List<BuildArchitectVO> architects) {
		this.architects = architects;
	}
	public List<BuildArchitectDetailVO> getSuccessfulBids() {
		return successfulBids;
	}
	public void setSuccessfulBids(List<BuildArchitectDetailVO> successfulBids) {
		this.successfulBids = successfulBids;
	}
	public String getGet_time() {
		return get_time;
	}
	public void setGet_time(String get_time) {
		this.get_time = get_time;
	}
	public List<BuildManagerDetailVO> getManagementInfos() {
		return managementInfos;
	}
	public void setManagementInfos(List<BuildManagerDetailVO> managementInfos) {
		this.managementInfos = managementInfos;
	}
	public List<BuildHonourVO> getHonorInfos() {
		return honorInfos;
	}
	public void setHonorInfos(List<BuildHonourVO> honorInfos) {
		this.honorInfos = honorInfos;
	}
	
	public List<BuildArchitectVO> getRegistrar() {
		return registrar;
	}
	public void setRegistrar(List<BuildArchitectVO> registrar) {
		this.registrar = registrar;
	}
}
