package cn.com.szgao.dto;

import java.io.Serializable;


public class LawOfficeLawyerVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	
	/**
	 * 律师名
	 */
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_lawOffice() {
		return name_lawOffice;
	}

	public void setName_lawOffice(String name_lawOffice) {
		this.name_lawOffice = name_lawOffice;
	}

	public String getLawOfficeId() {
		return lawOfficeId;
	}

	public void setLawOfficeId(String lawOfficeId) {
		this.lawOfficeId = lawOfficeId;
	}

	public String getProfessional_field() {
		return professional_field;
	}

	public void setProfessional_field(String professional_field) {
		this.professional_field = professional_field;
	}

	public String getHelped_people_num() {
		return helped_people_num;
	}

	public void setHelped_people_num(String helped_people_num) {
		this.helped_people_num = helped_people_num;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getDetail_link() {
		return detail_link;
	}

	public void setDetail_link(String detail_link) {
		this.detail_link = detail_link;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getLike_num() {
		return like_num;
	}

	public void setLike_num(String like_num) {
		this.like_num = like_num;
	}

	public String getTelephone_l() {
		return telephone_l;
	}

	public void setTelephone_l(String telephone_l) {
		this.telephone_l = telephone_l;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String name_lawOffice;
	
	private String lawOfficeId;
	
	/**
	 * 擅长专业
	 */
	private String professional_field;
	
	private String helped_people_num;
	
	private String telephone;
	
	private String detail_link;
	
	private String score;
	
	private String like_num;
	
	private String telephone_l;
	
	
}

