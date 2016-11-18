package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 年度报告-修改记录
 * @author xiongchangyi
 *
 */
public class ReporteEventItemVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String reporteEventItemId;
	/**
	 * 修改事项
	 */
	private String changeEvent;
	/**
	 * 修改前内容
	 */
	private String changeBefore;
	/**
	 * 修改后内容
	 */
	private String changeAfter;
	/**
	 * 修改日期
	 */
	private String changeDate;
	/**
	 * 备注
	 */
	private RemarkVO remark;
	
	public String getChangeEvent() {
		return changeEvent;
	}
	public void setChangeEvent(String changeEvent) {
		this.changeEvent = changeEvent;
	}
	public String getChangeBefore() {
		return changeBefore;
	}
	public void setChangeBefore(String changeBefore) {
		this.changeBefore = changeBefore;
	}
	public String getChangeAfter() {
		return changeAfter;
	}
	public void setChangeAfter(String changeAfter) {
		this.changeAfter = changeAfter;
	}
	public String getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getReporteEventItemId() {
		return reporteEventItemId;
	}
	public void setReporteEventItemId(String reporteEventItemId) {
		this.reporteEventItemId = reporteEventItemId;
	}
	
}
