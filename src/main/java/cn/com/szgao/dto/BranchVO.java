package cn.com.szgao.dto;

import java.io.Serializable;
/**
 * 分支机构信息 
 * @author dell
 *
 */
public class BranchVO implements Serializable{

	
	private String branchId;
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
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
	 * 序号
	 */
	private String sequence;
	/**
	 * 注册号
	 */
	private String regNum;
	/**
	 * 企业名称
	 */
	private String company;
	/**
	 * 登记机关
	 */
	private String regOfficel;
	/**
	 * 备注对象
	 */
	private RemarkVO remarkVO;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getRegNum() {
		return regNum;
	}
	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRegOfficel() {
		return regOfficel;
	}
	public void setRegOfficel(String regOfficel) {
		this.regOfficel = regOfficel;
	}
	public RemarkVO getRemarkVO() {
		return remarkVO;
	}
	public void setRemarkVO(RemarkVO remarkVO) {
		this.remarkVO = remarkVO;
	}
}
