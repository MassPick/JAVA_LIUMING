package cn.com.szgao.dto;
/**
 * 动产抵押信息 - 动产抵押详细  - 抵押权人概况
 * @author xiongchangyi
 * 2015-12-6
 */
public class MortgagePledgeeVO {
	/**
	 *抵押权人名称 
	 */
	private String pledgee;
	/**
	 * 抵押权人证照/证件类型
	 */
	private String licenseType;
	/**
	 * 证照/证件号码
	 */
	private String licenseNum;
	
	public String getPledgee() {
		return pledgee;
	}
	public void setPledgee(String pledgee) {
		this.pledgee = pledgee;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getLicenseNum() {
		return licenseNum;
	}
	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}
	
}
