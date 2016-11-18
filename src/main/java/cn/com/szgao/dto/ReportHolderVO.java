package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 年度报告-股东及出资信息
 * @author xiongchangyi
 * 2015-11-13
 */
public class ReportHolderVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
	private String reportHolderId;
	/**
	 * 股东
	 */
	private String holder;
	
	/**
	 * 认缴出资额
	 */
	private String subcriCapital;
	/**
	 * 认缴出资方式
	 */
	private String conMethod;
	/**
	 * 认缴出资日期
	 */
	private String considDate;
	/**
	 * 实际出资额
	 */
	private String actualCapital;
	/**
	 * 实际出资方式
	 */
	private String factMethod;
	/**
	 * 实际出资时期
	 */
	private String actualDate;
	/**
	 * 备注
	 */
	private RemarkVO remark;
	
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public String getSubcriCapital() {
		return subcriCapital;
	}
	public void setSubcriCapital(String subcriCapital) {
		this.subcriCapital = subcriCapital;
	}
	public String getConMethod() {
		return conMethod;
	}
	public void setConMethod(String conMethod) {
		this.conMethod = conMethod;
	}
	public String getConsidDate() {
		return considDate;
	}
	public void setConsidDate(String considDate) {
		this.considDate = considDate;
	}
	public String getActualCapital() {
		return actualCapital;
	}
	public void setActualCapital(String actualCapital) {
		this.actualCapital = actualCapital;
	}
	public String getFactMethod() {
		return factMethod;
	}
	public void setFactMethod(String factMethod) {
		this.factMethod = factMethod;
	}
	public String getActualDate() {
		return actualDate;
	}
	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getReportHolderId() {
		return reportHolderId;
	}
	public void setReportHolderId(String reportHolderId) {
		this.reportHolderId = reportHolderId;
	}
	
}
