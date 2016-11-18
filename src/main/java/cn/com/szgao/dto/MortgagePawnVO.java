package cn.com.szgao.dto;
/**
 * 动产抵押信息 - 动产抵押详细  - 抵押物概况
 * @author xiongchangyi
 *
 */
public class MortgagePawnVO {
	/**
	 * 名称
	 */
	private String pawnName;
	/**
	 * 所有权归属
	 */
	private String ownership;
	/**
	 * 数量
	 */
	private String amount;
	/**
	 * 质量
	 */
	private String quality;
	/**
	 * 状况
	 */
	private String status;	
	/**
	 * 所在地
	 */
	private String location;
	/**
	 * 备注
	 */
	private String remarks;
	
	public String getPawnName() {
		return pawnName;
	}
	public void setPawnName(String pawnName) {
		this.pawnName = pawnName;
	}
	public String getOwnership() {
		return ownership;
	}
	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
