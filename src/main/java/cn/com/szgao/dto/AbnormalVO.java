package cn.com.szgao.dto;

import java.io.Serializable;

/**
 * 经营异常信息
 * @author dell
 *
 */
public class AbnormalVO implements Serializable{

	private String abnormalId;
	public String getAbnormalId() {
		return abnormalId;
	}
	public void setAbnormalId(String abnormalId) {
		this.abnormalId = abnormalId;
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
	 * 列入经营异常名录原因
	 */
	private String recordCause;
	/**
	 * 列入日期
	 */
	private String recordDate;
	/**
	 * 移出经营异常名录原因
	 */
	private String removeCause;
	/**
	 * 移出日期
	 */
	private String removeDate;
	/**
	 * 作出决定机关
	 */
	private String decideOffice;

	/**
	 * 备注对象
	 */
	private RemarkVO remark;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getRecordCause() {
		return recordCause;
	}
	public void setRecordCause(String recordCause) {
		this.recordCause = recordCause;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getRemoveCause() {
		return removeCause;
	}
	public void setRemoveCause(String removeCause) {
		this.removeCause = removeCause;
	}
	public String getRemoveDate() {
		return removeDate;
	}
	public void setRemoveDate(String removeDate) {
		this.removeDate = removeDate;
	}
	public String getDecideOffice() {
		return decideOffice;
	}
	public void setDecideOffice(String decideOffice) {
		this.decideOffice = decideOffice;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	
}
