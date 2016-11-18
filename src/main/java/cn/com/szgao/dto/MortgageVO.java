package cn.com.szgao.dto;

import java.io.Serializable;
/**
 * 动产抵押登记信息
 * 2015-12-6
 * @author xiongchangyi
 *
 */
public class MortgageVO implements Serializable{
	
	private String mortgageId;

	public String getMortgageId() {
		return mortgageId;
	}
	public void setMortgageId(String mortgageId) {
		this.mortgageId = mortgageId;
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
	 * 登记编号
	 */
	private String recordNum;
	/**
	 * 公示日期
	 */
	private String publicationDate;
	/**
	 * 登记日期
	 */
	private String recordDate;
	/**
	 * 登记机关
	 */
	private String regOffice;
	/**
	 * 被担保债权额
	 */
	private String debtAmount;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 详情
	 */
	private MortgageDetailVO mortgageDetail;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getRecordNum() {
		return recordNum;
	}
	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}
	public String getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getRegOffice() {
		return regOffice;
	}
	public void setRegOffice(String regOffice) {
		this.regOffice = regOffice;
	}
	public String getDebtAmount() {
		return debtAmount;
	}
	public void setDebtAmount(String debtAmount) {
		this.debtAmount = debtAmount;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public MortgageDetailVO getMortgageDetail() {
		return mortgageDetail;
	}
	public void setMortgageDetail(MortgageDetailVO mortgageDetail) {
		this.mortgageDetail = mortgageDetail;
	}
}
