package cn.com.szgao.dto;

/**
 * 建设通的建筑师
 * 
 * @author liuming
 * @Date 2016年8月25日 下午2:09:50
 */
public class BuildArchitectVO {

	private String companyName;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	private String buildArchitectId;
	private String buildId;

	private Integer source;

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getBuildArchitectId() {
		return buildArchitectId;
	}

	public void setBuildArchitectId(String buildArchitectId) {
		this.buildArchitectId = buildArchitectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBid_num() {
		return bid_num;
	}

	public void setBid_num(String bid_num) {
		this.bid_num = bid_num;
	}

	public String getHour_num() {
		return hour_num;
	}

	public void setHour_num(String hour_num) {
		this.hour_num = hour_num;
	}

	public String getManage_num() {
		return manage_num;
	}

	public void setManage_num(String manage_num) {
		this.manage_num = manage_num;
	}

	public String getBid_info() {
		return bid_info;
	}

	public void setBid_info(String bid_info) {
		this.bid_info = bid_info;
	}

	/**
	 * 建筑师
	 */
	private String name;

	/**
	 * 中标数量
	 */
	private String bid_num;

	/**
	 * 荣誉数量
	 */
	private String hour_num;

	/**
	 * 中标经营
	 */
	private String manage_num;

	/**
	 * 其他信息
	 */
	private String bid_info;

	// ---------------------------全国
	/**
	 * 全国建设市的 url
	 */
	private String personalUrl;

	public String getPersonalUrl() {
		return personalUrl;
	}

	public void setPersonalUrl(String personalUrl) {
		this.personalUrl = personalUrl;
	}

	public String getRegExpireDate() {
		return regExpireDate;
	}

	public void setRegExpireDate(String regExpireDate) {
		this.regExpireDate = regExpireDate;
	}

	public String getRegCertDate() {
		return regCertDate;
	}

	public void setRegCertDate(String regCertDate) {
		this.regCertDate = regCertDate;
	}

	public String getProAndLevel() {
		return proAndLevel;
	}

	public void setProAndLevel(String proAndLevel) {
		this.proAndLevel = proAndLevel;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getRegisterName() {
		return registerName;
	}

	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}

	public String getPracticeSealNo() {
		return practiceSealNo;
	}

	public void setPracticeSealNo(String practiceSealNo) {
		this.practiceSealNo = practiceSealNo;
	}

	/**
	 * 注册有效期
	 */
	private String regExpireDate;

	/**
	 * 发证日期
	 */
	private String regCertDate;

	/**
	 * 建造师等级
	 */
	private String proAndLevel;

	/**
	 * 身份证号码
	 */
	private String idNo;

	/**
	 * 建造师名
	 */
	private String registerName;

	/**
	 * 执业印章号
	 */
	private String practiceSealNo;

}
