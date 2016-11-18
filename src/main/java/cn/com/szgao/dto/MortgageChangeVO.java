package cn.com.szgao.dto;
/**
 * 动产抵押信息 - 动产抵押详细  - 变更
 * @author xiongchangyi
 * 2015-12-6
 */
public class MortgageChangeVO {
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
