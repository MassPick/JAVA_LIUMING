package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 年度报告-股权变更信息
 * @author xiongchangyi
 *
 */
public class ReportStockEventItemVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String reportStockEventItemId;
	
	/**
	 * 发起人
	 */
	private String founderMember;
	/**
	 * 变更前股权比例
	 */
	private String upbefRatio;
	/**
	 * 变更后股权比例
	 */
	private String upaftRatio;
	/**
	 * 股权变更日期
	 */
	private String updateDate;
	/**
	 * 备注
	 */
	private RemarkVO remark;
	
	public String getFounderMember() {
		return founderMember;
	}
	public void setFounderMember(String founderMember) {
		this.founderMember = founderMember;
	}
	public String getUpbefRatio() {
		return upbefRatio;
	}
	public void setUpbefRatio(String upbefRatio) {
		this.upbefRatio = upbefRatio;
	}
	public String getUpaftRatio() {
		return upaftRatio;
	}
	public void setUpaftRatio(String upaftRatio) {
		this.upaftRatio = upaftRatio;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getReportStockEventItemId() {
		return reportStockEventItemId;
	}
	public void setReportStockEventItemId(String reportStockEventItemId) {
		this.reportStockEventItemId = reportStockEventItemId;
	}
	
}
