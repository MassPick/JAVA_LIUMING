package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 年度报告 - 对外提供保证担保信息
 * @author xiongchangyi
 *
 */
public class ReportGuaranteeVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private String reportGuaranteeId;
	/**
	 * 债权人
	 */
	private String loaner;
	/**
	 * 债务人
	 */
	private String debtor;
	/**
	 * 主债权种类
	 */
	private String debtType;
	/**
	 * 主债权数额
	 */
	private String debtAmount;
	/**
	 * 履行债务的期限
	 */
	private String performTerm;
	/**
	 * 保证的期间
	 */
	private String ensureTerm;
	/**
	 * 保证的方式
	 */
	private String ensureMethod;
	/**
	 * 保证担保的范围
	 */
	private String ensureScope;
	/**
	 * 备注
	 */
	private RemarkVO remark;
	
	public String getLoaner() {
		return loaner;
	}
	public void setLoaner(String loaner) {
		this.loaner = loaner;
	}
	public String getDebtor() {
		return debtor;
	}
	public void setDebtor(String debtor) {
		this.debtor = debtor;
	}
	public String getDebtType() {
		return debtType;
	}
	public void setDebtType(String debtType) {
		this.debtType = debtType;
	}
	public String getDebtAmount() {
		return debtAmount;
	}
	public void setDebtAmount(String debtAmount) {
		this.debtAmount = debtAmount;
	}
	public String getPerformTerm() {
		return performTerm;
	}
	public void setPerformTerm(String performTerm) {
		this.performTerm = performTerm;
	}
	public String getEnsureTerm() {
		return ensureTerm;
	}
	public void setEnsureTerm(String ensureTerm) {
		this.ensureTerm = ensureTerm;
	}
	public String getEnsureMethod() {
		return ensureMethod;
	}
	public void setEnsureMethod(String ensureMethod) {
		this.ensureMethod = ensureMethod;
	}
	public String getEnsureScope() {
		return ensureScope;
	}
	public void setEnsureScope(String ensureScope) {
		this.ensureScope = ensureScope;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getReportGuaranteeId() {
		return reportGuaranteeId;
	}
	public void setReportGuaranteeId(String reportGuaranteeId) {
		this.reportGuaranteeId = reportGuaranteeId;
	}
	
}
