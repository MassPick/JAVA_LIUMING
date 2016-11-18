package cn.com.szgao.dto;

import java.io.Serializable;
/**
 * 抽查检查信息
 * @author dell
 *
 */
public class SpotCheckVO implements Serializable{
	
	private String spotCheckId;

	public String getSpotCheckId() {
		return spotCheckId;
	}
	public void setSpotCheckId(String spotCheckId) {
		this.spotCheckId = spotCheckId;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 检查实施机关
	 */
	private String checkOffice;
	/**
	 * 类型
	 */
	private String checkType;
	/**
	 * 日期
	 */
	private String checkDate;
	/**
	 * 结果 
	 */
	private String checkResult;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCheckOffice() {
		return checkOffice;
	}
	public void setCheckOffice(String checkOffice) {
		this.checkOffice = checkOffice;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	
}
