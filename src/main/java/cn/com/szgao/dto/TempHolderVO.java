package cn.com.szgao.dto;
/**
 * 临时股东信息实体类
 * 加临时类的目的：解决股东详情与股东信息一起的情况
 * @author xiongchangyi
 *
 */
public class TempHolderVO {
	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 股东类型
	 */
	private String type;
	/**
	 * 股东
	 */
	private String holder;
	/**
	 * 证照/证件类型
	 */
	private String licenseType;
	/**
	 * 证照/证件号码
	 */
	private String licenseNum;
	/**
	 * 详情
	 */
	private String particulars;
	/**
	 * 出资方式
	 * equityParticipation的缩写
	 */
	private String equityPart;
	/**
	 * 备注：别名备注、bucket名称
	 */
	private RemarkVO remark;
	/**
	 * 认缴出资额
	 */
	private String subcriCapital;
	/**
	 * 认缴出资方式
	 */
	private String conMethod;
	/**
	 * 认缴出资日期
	 */
	private String considDate;
	/**
	 * 实缴出资额
	 */
	private String actualCapital;
	/**
	 * 实缴出资方式
	 */
	private String factMethod;
	/**
	 * 实缴出资时期
	 */
	private String actualDate;
	/**
	 * 认缴额
	 */
	private String conCapital;
	/**
	 * 实缴额
	 */
	private String factCapital;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getLicenseNum() {
		return licenseNum;
	}
	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public String getEquityPart() {
		return equityPart;
	}
	public void setEquityPart(String equityPart) {
		this.equityPart = equityPart;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getSubcriCapital() {
		return subcriCapital;
	}
	public void setSubcriCapital(String subcriCapital) {
		this.subcriCapital = subcriCapital;
	}
	public String getConMethod() {
		return conMethod;
	}
	public void setConMethod(String conMethod) {
		this.conMethod = conMethod;
	}
	public String getConsidDate() {
		return considDate;
	}
	public void setConsidDate(String considDate) {
		this.considDate = considDate;
	}
	public String getActualCapital() {
		return actualCapital;
	}
	public void setActualCapital(String actualCapital) {
		this.actualCapital = actualCapital;
	}
	public String getFactMethod() {
		return factMethod;
	}
	public void setFactMethod(String factMethod) {
		this.factMethod = factMethod;
	}
	public String getActualDate() {
		return actualDate;
	}
	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}
	public String getConCapital() {
		return conCapital;
	}
	public void setConCapital(String conCapital) {
		this.conCapital = conCapital;
	}
	public String getFactCapital() {
		return factCapital;
	}
	public void setFactCapital(String factCapital) {
		this.factCapital = factCapital;
	}
}
