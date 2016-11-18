package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 年报对象
 * @author xiongchangyi
 * 2015-11-13
 */
public class ReportVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String reportId;
	
	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 年报名称
	 */
	private String reportName;
	/**
	 * 标识该记录是否有效
	 */
    private Integer flag;
	/**
	 * 年报基本信息对象
	 */
	private ReportBaseVO base;
	/**
	 * 股东详情、股东出资信息
	 */
	private List<HolderDetailVO> holderDetailList;
	/**
	 * 网站、网店对象集合
	 */
	private List<ReportWebVO> webList = new ArrayList<ReportWebVO>();
	/**
	 * 股东及出资信息对象集合
	 */
	private List<ReportHolderVO> holderList = new ArrayList<ReportHolderVO>();
	/**
	 * 年度报告-对外投资信息集合
	 */
	private List<ReportInvestVO> investList = new ArrayList<ReportInvestVO>();
	/**
	 * 年度报告-企业资产状况信息
	 */
	private ReportAssetVO asset;
	/**
	 * 年度报告-股权变更信息 
	 */
	private List<ReportStockEventItemVO> stockEventList = new ArrayList<ReportStockEventItemVO>();
	/**
	 * 年度报告-修改记录
	 */
	private List<ReporteEventItemVO> eventItemList = new ArrayList<ReporteEventItemVO>();
	/**
	 * 年度报告-对外担保 
	 */
	private List<ReportGuaranteeVO> guaranteeList = new ArrayList<ReportGuaranteeVO>();
	/**
	 * 年度报告- 行政许可情况
	 */
	private ReportPermitVO permit;
	private List<ReportVO> detList; 
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public ReportBaseVO getBase() {
		return base;
	}
	public void setBase(ReportBaseVO base) {
		this.base = base;
	}
	public List<ReportWebVO> getWebList() {
		return webList;
	}
	public void setWebList(List<ReportWebVO> webList) {
		this.webList = webList;
	}
	public List<ReportHolderVO> getHolderList() {
		return holderList;
	}
	public void setHolderList(List<ReportHolderVO> holderList) {
		this.holderList = holderList;
	}
	public List<ReportInvestVO> getInvestList() {
		return investList;
	}
	public void setInvestList(List<ReportInvestVO> investList) {
		this.investList = investList;
	}
	public ReportAssetVO getAsset() {
		return asset;
	}
	public void setAsset(ReportAssetVO asset) {
		this.asset = asset;
	}
	public List<ReportStockEventItemVO> getStockEventList() {
		return stockEventList;
	}
	public void setStockEventList(List<ReportStockEventItemVO> stockEventList) {
		this.stockEventList = stockEventList;
	}
	public List<ReporteEventItemVO> getEventItemList() {
		return eventItemList;
	}
	public void setEventItemList(List<ReporteEventItemVO> eventItemList) {
		this.eventItemList = eventItemList;
	}
	public List<HolderDetailVO> getHolderDetailList() {
		return holderDetailList;
	}
	public void setHolderDetailList(List<HolderDetailVO> holderDetailList) {
		this.holderDetailList = holderDetailList;
	}
	public List<ReportGuaranteeVO> getGuaranteeList() {
		return guaranteeList;
	}
	public void setGuaranteeList(List<ReportGuaranteeVO> guaranteeList) {
		this.guaranteeList = guaranteeList;
	}
	public ReportPermitVO getPermit() {
		return permit;
	}
	public void setPermit(ReportPermitVO permit) {
		this.permit = permit;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public List<ReportVO> getDetList() {
		return detList;
	}
	public void setDetList(List<ReportVO> detList) {
		this.detList = detList;
	}	
	
}
