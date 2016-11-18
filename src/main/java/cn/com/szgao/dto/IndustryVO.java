package cn.com.szgao.dto;

public class IndustryVO {
	private String industrynv_n_id;
	public String getIndustrynv_n_id() {
		return industrynv_n_id;
	}
	public void setIndustrynv_n_id(String industrynv_n_id) {
		this.industrynv_n_id = industrynv_n_id;
	}
	public String getIndustry_id() {
		return industry_id;
	}
	public void setIndustry_id(String industry_id) {
		this.industry_id = industry_id;
	}
	public String getIn_name() {
		return in_name;
	}
	public void setIn_name(String in_name) {
		this.in_name = in_name;
	}
	public String getIndustry_name() {
		return industry_name;
	}
	public void setIndustry_name(String industry_name) {
		this.industry_name = industry_name;
	}
	public String getIndustry_code() {
		return industry_code;
	}
	public void setIndustry_code(String industry_code) {
		this.industry_code = industry_code;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * industry表的id
	 */
	private String industry_id;
	/**
	 * 词条
	 */
	private String in_name;
	/**
	 * 行业名
	 */
	private String industry_name;
	
	/**
	 * 行业code
	 */
	private String industry_code;
	/**
	 * N是名称  V 是动词
	 */
	private String flag;
	
}
