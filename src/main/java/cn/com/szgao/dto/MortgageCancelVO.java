package cn.com.szgao.dto;
/**
 *  动产抵押信息 - 动产抵押详细  - 注销
 * @author xiongchangyi
 *
 */
public class MortgageCancelVO {
	/**
	 * 注销日期
	 */
	private String cancelDate;
	/**
	 * 注销原因
	 */
	private String cancelCause;
	
	public String getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}
	public String getCancelCause() {
		return cancelCause;
	}
	public void setCancelCause(String cancelCause) {
		this.cancelCause = cancelCause;
	}
}
