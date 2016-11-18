package cn.com.szgao.dto;

import java.io.Serializable;
/**
 * 主要成员信息
 * @author dell
 *
 */
public class MainManagerVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	/**
	 * 公司ID
	 */
	private String companyId;
	
	
	private String mainManagerId;
	public String getMainManagerId() {
		return mainManagerId;
	}
	public void setMainManagerId(String mainManagerId) {
		this.mainManagerId = mainManagerId;
	}
	/**
	 * 序号
	 */
	private String sequence;
	/**
	 * 姓名
	 */
	private String managerName;
	/**
	 * 职务
	 */
	private String position;
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
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}	
}
