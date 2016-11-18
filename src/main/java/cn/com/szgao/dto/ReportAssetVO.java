package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 年度报告-企业资产状况信息
 * @author xiongchangyi
 *
 */
public class ReportAssetVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String reportAssetId;
	/**
	 * 资产总额
	 */
	private String totalAsset;
	/**
	 * 所有者权益合计
	 */
	private String ownerInterest;
	/**
	 * 销售总额/营业总收入
	 */
	private String totalSale;
	/**
	 * 利润总额
	 */
	private String profit;
	/**
	 * 营业总收入中主营业务收入
	 */
	private String mainTotalSale;
	/**
	 * 净利润
	 */
	private String netProfit;
	/**
	 * 纳税总额
	 */
	private String totalTax;
	/**
	 * 负债总额
	 */
	private String totalDebt;
	/**
	 * 备注
	 */
	private RemarkVO remark;
	
	public String getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
	}
	public String getOwnerInterest() {
		return ownerInterest;
	}
	public void setOwnerInterest(String ownerInterest) {
		this.ownerInterest = ownerInterest;
	}
	public String getTotalSale() {
		return totalSale;
	}
	public void setTotalSale(String totalSale) {
		this.totalSale = totalSale;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getMainTotalSale() {
		return mainTotalSale;
	}
	public void setMainTotalSale(String mainTotalSale) {
		this.mainTotalSale = mainTotalSale;
	}
	public String getNetProfit() {
		return netProfit;
	}
	public void setNetProfit(String netProfit) {
		this.netProfit = netProfit;
	}
	public String getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(String totalTax) {
		this.totalTax = totalTax;
	}
	public String getTotalDebt() {
		return totalDebt;
	}
	public void setTotalDebt(String totalDebt) {
		this.totalDebt = totalDebt;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getReportAssetId() {
		return reportAssetId;
	}
	public void setReportAssetId(String reportAssetId) {
		this.reportAssetId = reportAssetId;
	}
	
}
