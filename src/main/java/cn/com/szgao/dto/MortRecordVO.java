package cn.com.szgao.dto;
/**
 * 动产抵押信息 - 动产抵押详细  - 登记信息
 * @author xiongchangyi
 * 2015-12-07
 */
public class MortRecordVO {
	/**
	 * 登记编号
	 */
	private String recordNum;
	/**
	 * 登记日期
	 */
	private String recordDate;
	/**
	 * 登记机关
	 */
	private String regOffice;
	/**
	 * 种类
	 */
	private String type;
	/**
	 * 数额
	 */
	private String amount;
	/**
	 * 担保的范围
	 */
	private String ensureScope;
	/**
	 * 债务人履行债务的期限
	 */
	private String performTerm;
	/**
	 * 备注
	 */
	private String remarks;
	
	public String getRecordNum() {
		return recordNum;
	}
	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getEnsureScope() {
		return ensureScope;
	}
	public void setEnsureScope(String ensureScope) {
		this.ensureScope = ensureScope;
	}
	public String getPerformTerm() {
		return performTerm;
	}
	public void setPerformTerm(String performTerm) {
		this.performTerm = performTerm;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	} 
}
