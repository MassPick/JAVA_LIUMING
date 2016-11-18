package cn.com.szgao.dto;
/**
 * 股权出质登记信息 - 变化情况
 * @author xiongchangyi
 * 2015-12-6
 */
public class PledgeDetVO {
	/**
	 * 变更日期
	 */
	private String changeDate;
	/**
	 * 变更内容
	 */
	private String changeContent;
	
	public String getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}
	public String getChangeContent() {
		return changeContent;
	}
	public void setChangeContent(String changeContent) {
		this.changeContent = changeContent;
	}
	
}
