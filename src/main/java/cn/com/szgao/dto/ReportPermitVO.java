package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 年度报告- 行政许可情况
 * @author xiongchangyi
 * 2015-12-09
 */
public class ReportPermitVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private String reportPermitId;
	/**
	 * 许可文件名称
	 */
	private String permitName;
	/**
	 * 有效期截止
	 */
	private String expiryDate;
	/**
	 * 备注
	 */
	private RemarkVO remark;
	
	public String getPermitName() {
		return permitName;
	}
	public void setPermitName(String permitName) {
		this.permitName = permitName;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getReportPermitId() {
		return reportPermitId;
	}
	public void setReportPermitId(String reportPermitId) {
		this.reportPermitId = reportPermitId;
	}
	
}
