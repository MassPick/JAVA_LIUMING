package cn.com.szgao.dto;

import java.io.Serializable;
/**
 * 变更信息
 * @author dell
 *
 */
public class ChangeVO implements Serializable{

	
	private String changeId;
	
	public String getChangeId() {
		return changeId;
	}
	public void setChangeId(String changeId) {
		this.changeId = changeId;
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
	 * 变更事项
	 */
	private String changeEvent;
	/**
	 * 变更前内容
	 */
	private String changeBefore;
	/**
	 * 变更后内容
	 */
	private String changeAfter;
	/**
	 * 变更日期
	 */
	private String changeDate;
	/**
	 *备注对象 
	 */
	private RemarkVO remark;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
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
}
