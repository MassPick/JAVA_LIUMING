package cn.com.szgao.dto;
import java.io.Serializable;


/**
 * 年度报告-基本信息
 * @author xiongchangyi
 * 2015-11-13
 */
public class ReportBaseVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String reportBaseId;
	/**
	 * 注册号
	 */
	private String regNum;
	/**
	 * 公司名称
	 */
	private String company;
	/**
	 * 电话
	 */
	private String tel;
	/**
	 * 邮编
	 */
	private String zip;
	/**
	 * 地址
	 */
	private String location;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 企业是否有投资信息或购买其他公司股权
	 */
	private String invest;
	/**
	 * 经营状态
	 */
	private String regState;
	/**
	 * 是否有网站或网店
	 */
	private String hasWeb;
	/**
	 * 从业人数
	 */
	private String headCount;
	/**
	 * 是否发生股东股权转让
	 */
	private String makeOver;
	/**
	 * 隶属关系
	 */
	private String subordination;
	/**
	 * 备注
	 */
	private RemarkVO remark;
	
	public String getRegNum() {
		return regNum;
	}
	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInvest() {
		return invest;
	}
	public void setInvest(String invest) {
		this.invest = invest;
	}
	public String getRegState() {
		return regState;
	}
	public void setRegState(String regState) {
		this.regState = regState;
	}
	public String getHasWeb() {
		return hasWeb;
	}
	public void setHasWeb(String hasWeb) {
		this.hasWeb = hasWeb;
	}
	public String getHeadCount() {
		return headCount;
	}
	public void setHeadCount(String headCount) {
		this.headCount = headCount;
	}
	public String getMakeOver() {
		return makeOver;
	}
	public void setMakeOver(String makeOver) {
		this.makeOver = makeOver;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getSubordination() {
		return subordination;
	}
	public void setSubordination(String subordination) {
		this.subordination = subordination;
	}
	public String getReportBaseId() {
		return reportBaseId;
	}
	public void setReportBaseId(String reportBaseId) {
		this.reportBaseId = reportBaseId;
	}
	
}
