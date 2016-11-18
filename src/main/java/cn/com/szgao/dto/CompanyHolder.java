package cn.com.szgao.dto;

public class CompanyHolder {
	/* 
	 * 公司ID
	 */
	private String companyId;
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getRegNum() {
		return regNum;
	}

	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	public String getLegalRep() {
		return legalRep;
	}

	public void setLegalRep(String legalRep) {
		this.legalRep = legalRep;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}


	public String getLicenseNum() {
		return licenseNum;
	}

	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}

	/**
	 * 公司名称
	 */
	private String company ;
	
	/**
	 * 注册号
	 */
	private String regNum;
	
	/**
	 * 统一社会信用代码
	 */
	private String creditCode;
	
	/**
	 * 类型
	 */
//	private String type;
	/**
	 * 法定代表人、负责人、经营者、投资人
	 */
	private String legalRep;
	
	
//	private Double regCapitalN;
	
	/**
	 * 股东
	 */
	private String holder;
	
	/**
	 * 股东类型
	 */
	private String holder_type;
	
	public String getHolder_type() {
		return holder_type;
	}

	public void setHolder_type(String holder_type) {
		this.holder_type = holder_type;
	}

	/**
	 * 证照/证件号码
	 */
	private String licenseNum;
	
	
	/**
	 * 省
	 */
	private String province;
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 市
	 */
	private String city;
	/**
	 * 县
	 */
	private String area;
	
	
	
}
