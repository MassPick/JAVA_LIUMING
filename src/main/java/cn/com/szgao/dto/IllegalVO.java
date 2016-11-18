package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 严重违法信息
 * @author dell
 *
 */
public class IllegalVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String illegalId;
	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 列入严重违法企业名单原因
	 */
	private String illegalCause;
	/**
	 * 列入日期
	 */
	private String recordDate;
	/**
	 * 移出严重违法企业名单原因
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
	/**
	 * 标识该记录是否有效
	 */
    private Integer flag;
    private List<IllegalVO> detList;
	public String getIllegalId() {
		return illegalId;
	}
	public void setIllegalId(String illegalId) {
		this.illegalId = illegalId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getIllegalCause() {
		return illegalCause;
	}
	public void setIllegalCause(String illegalCause) {
		this.illegalCause = illegalCause;
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
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public List<IllegalVO> getDetList() {
		return detList;
	}
	public void setDetList(List<IllegalVO> detList) {
		this.detList = detList;
	}
	
}
