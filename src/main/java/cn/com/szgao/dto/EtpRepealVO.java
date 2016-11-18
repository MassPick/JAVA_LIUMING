package cn.com.szgao.dto;

import java.io.Serializable;

/**
 * 撤销信息
 * @author liuming
 *
 */
public class EtpRepealVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 撤销事项
	 */
	private String repealEvent;
	public String getRepealEvent() {
		return repealEvent;
	}
	public void setRepealEvent(String repealEvent) {
		this.repealEvent = repealEvent;
	}
	public String getRepealBefore() {
		return repealBefore;
	}
	public void setRepealBefore(String repealBefore) {
		this.repealBefore = repealBefore;
	}
	public String getRepealAfter() {
		return repealAfter;
	}
	public void setRepealAfter(String repealAfter) {
		this.repealAfter = repealAfter;
	}
	public String getRepealDate() {
		return repealDate;
	}
	public void setRepealDate(String repealDate) {
		this.repealDate = repealDate;
	}
	/**
	 * 撤销前内容
	 */
	private String repealBefore;
	/**
	 * 撤销后内容
	 */
	private String repealAfter;
	/**
	 * 撤销日期
	 */
	private String repealDate;
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
	 
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}	
}
