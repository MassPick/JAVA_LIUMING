package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 会计律师事务所
 * @author liuming
 * @Date 2016年10月26日 下午12:01:56
 */
public class LawAccountVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	//------------------主所基本信息
	/**
	 * 网址  有“无”
	 */
	private String website;
	
	
	/**
	 * 传真
	 */
	private String fax;
	
	/**
	 * 联系人
	 */
	private String contact;
	
	/**
	 * 会计师事务所名称 
	 */
	private String name;
	
	/**
	 * 通讯地址 
	 */
	private String postal_address;
	
	/**
	 * 办公地址
	 */
	private  String office_address;
	
	/**
	 * 邮政编码 
	 */
	private String post_code;
	
	/**
	 * 证书编号
	 */
	private String cert_code;
	
	/**
	 * 联系电话
	 */
	private String telephone;
	
	/**
	 * 电子邮箱
	 */
	private String email;
	
	
	
	//-------------------------分所斯基本信息
	
	/**
	 * 税务登记号
	 */
	private String tax_reg_no;
	
	/**
	 * 主所证书编号
	 */
	private String parent_firm_cert_no;
	
	/**
	 * 工商登记号
	 */
	private String reg_no;
	
	/**
	 * 分所负责人
	 */
	private String person_in_charge;
	
	/**
	 * 分所编号
	 */
	private String branch_no;
	
	/**
	 * 成立批准机关 
	 */
	private String approving_authority;
	
	/**
	 * 工商登记日期
	 */
	private String reg_date;
	
	/**
	 * 从业人员总数
	 */
	private String  total_headcount;
	
	
	/**
	 * 所在城市
	 */
	private String location;
	
	/**
	 * 成立批准时间
	 */
	private String open_date;
	
	/**
	 * 成立批准文号
	 */
	private String file_num;
	
	/**
	 * 注册会计师总数
	 */
	private String total_cpa_num;
	
	
	/**
	 * 1 主所  2 会计事务所的分所
	 */
	private Integer flag_sub;
	
	
	
	
	private String url;
	/**
	 * 北京注协
	 */
	private String belong;
	private String collect_time;
	
	
	
	
	
	
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPostal_address() {
		return postal_address;
	}
	public void setPostal_address(String postal_address) {
		this.postal_address = postal_address;
	}
	public String getOffice_address() {
		return office_address;
	}
	public void setOffice_address(String office_address) {
		this.office_address = office_address;
	}
	public String getPost_code() {
		return post_code;
	}
	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}
	public String getCert_code() {
		return cert_code;
	}
	public void setCert_code(String cert_code) {
		this.cert_code = cert_code;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTax_reg_no() {
		return tax_reg_no;
	}
	public void setTax_reg_no(String tax_reg_no) {
		this.tax_reg_no = tax_reg_no;
	}
	public String getParent_firm_cert_no() {
		return parent_firm_cert_no;
	}
	public void setParent_firm_cert_no(String parent_firm_cert_no) {
		this.parent_firm_cert_no = parent_firm_cert_no;
	}
	public String getReg_no() {
		return reg_no;
	}
	public void setReg_no(String reg_no) {
		this.reg_no = reg_no;
	}
	public String getPerson_in_charge() {
		return person_in_charge;
	}
	public void setPerson_in_charge(String person_in_charge) {
		this.person_in_charge = person_in_charge;
	}
	public String getBranch_no() {
		return branch_no;
	}
	public void setBranch_no(String branch_no) {
		this.branch_no = branch_no;
	}
	public String getApproving_authority() {
		return approving_authority;
	}
	public void setApproving_authority(String approving_authority) {
		this.approving_authority = approving_authority;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getTotal_headcount() {
		return total_headcount;
	}
	public void setTotal_headcount(String total_headcount) {
		this.total_headcount = total_headcount;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOpen_date() {
		return open_date;
	}
	public void setOpen_date(String open_date) {
		this.open_date = open_date;
	}
	public String getFile_num() {
		return file_num;
	}
	public void setFile_num(String file_num) {
		this.file_num = file_num;
	}
	public String getTotal_cpa_num() {
		return total_cpa_num;
	}
	public void setTotal_cpa_num(String total_cpa_num) {
		this.total_cpa_num = total_cpa_num;
	}
	public Integer getFlag_sub() {
		return flag_sub;
	}
	public void setFlag_sub(Integer flag_sub) {
		this.flag_sub = flag_sub;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBelong() {
		return belong;
	}
	public void setBelong(String belong) {
		this.belong = belong;
	}
	public String getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
}
