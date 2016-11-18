package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 年度报告-对外投资信息
 * @author xiongchangyi
 * 2015-11-13
 */
public class ReportInvestVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String reportInvestId;
	/**
	 * 投资对象(企业名称)
	 */
	private String investTarget;
	/**
	 * 注册号
	 */
	private String regNum;
	/**
	 * 备注
	 */
	private RemarkVO remark;
	
	public String getInvestTarget() {
		return investTarget;
	}
	public void setInvestTarget(String investTarget) {
		this.investTarget = investTarget;
	}
	public String getRegNum() {
		return regNum;
	}
	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getReportInvestId() {
		return reportInvestId;
	}
	public void setReportInvestId(String reportInvestId) {
		this.reportInvestId = reportInvestId;
	}
	
}
