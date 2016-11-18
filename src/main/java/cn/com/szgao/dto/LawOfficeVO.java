package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;

public class LawOfficeVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 网址
	 */
	private String website;

	
	private String telephone_o;
	
	private String name_lawOffice;
	
	private String lawOfficeId;

	public String getLawOfficeId() {
		return lawOfficeId;
	}

	public void setLawOfficeId(String lawOfficeId) {
		this.lawOfficeId = lawOfficeId;
	}

	public String getName_lawOffice() {
		return name_lawOffice;
	}

	public void setName_lawOffice(String name_lawOffice) {
		this.name_lawOffice = name_lawOffice;
	}

	private List<LawOfficeLawyerVO> lawyers;

	private String province;

	private String city;

	private String fax;

	private String telephone;

	private String collect_time;

	private String detail_link;

	private String address;

	private String scope_of_business;

	private String name;

	private String introduction;

	private String email;

	private String law_firm_director;

	public String getTelephone_o() {
		return telephone_o;
	}

	public void setTelephone_o(String telephone_o) {
		this.telephone_o = telephone_o;
	}

	public List<LawOfficeLawyerVO> getLawyers() {
		return lawyers;
	}

	public void setLawyers(List<LawOfficeLawyerVO> lawyers) {
		this.lawyers = lawyers;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

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

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCollect_time() {
		return collect_time;
	}

	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}

	public String getDetail_link() {
		return detail_link;
	}

	public void setDetail_link(String detail_link) {
		this.detail_link = detail_link;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getScope_of_business() {
		return scope_of_business;
	}

	public void setScope_of_business(String scope_of_business) {
		this.scope_of_business = scope_of_business;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLaw_firm_director() {
		return law_firm_director;
	}

	public void setLaw_firm_director(String law_firm_director) {
		this.law_firm_director = law_firm_director;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
