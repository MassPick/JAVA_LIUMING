package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 注册会计师(CPA)个人详细信息。
 * @author liuming
 * @Date 2016年10月26日 下午4:46:28
 */
public class LawAccountCPAVO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	/**
	 * 所学专业 可能有“1”
	 */
	private String major;
	
	private String collect_time;

	/**
	 * 批准注册时间  1996-01-10  
	 */
	private String approval_registered_time;
	
	/**
	 * 毕业学校 
	 */
	private String graduate_school;
	
	/**
	 * 注册会计师证书编号
	 */
	private String cpa_certificate_no;
	
	/**
	 * 本年度应完成学时
	 */
	private String total_class_hours;
	
	/**
	 * 惩戒及处罚信息(披露时限:自2013年至今)
	 */
	private String reward_and_punishment_information;
	
	/**
	 * 是否合伙人（股东）
	 */
	private String is_partner;
	
	/**
	 * 全科合格年份
	 */
	private String approval_time;
	
	/**
	 * 全科合格证书号
	 */
	private String approval_time_no;
	
	/**
	 * 所在事务所
	 */
	private String firm;
	
	private String approval_file_no;
	/**
	 * 学位 
	 */
	private String degree;
	
	/**
	 * 个人信息页面URL
	 */
	private String detail_link;
	
	/**
	 * 参加公益活动 
	 */
	private String volunteer_activity;
	
	/**
	 * 是否党员
	 */
	private String is_party_member;
	
	/**
	 * 姓名 
	 */
	private String name;
	
	/**
	 * 批准注册文件号
	 */
	private String approval_registered_file_no;
	
	/**
	 * 性别
	 */
	private String gender;
	
	/**
	 * 资格取得方式（考试/考核）
	 */
	private String qualification;
	
	/**
	 * 学历
	 */
	private String education_background;
	
	/**
	 * 所内职务
	 */
	private String position;
	
	/**
	 * 本年度已完成学时
	 */
	private String finished_class_hours;
	
	
	
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getCollect_time() {
		return collect_time;
	}

	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}

	public String getApproval_registered_time() {
		return approval_registered_time;
	}

	public void setApproval_registered_time(String approval_registered_time) {
		this.approval_registered_time = approval_registered_time;
	}

	public String getGraduate_school() {
		return graduate_school;
	}

	public void setGraduate_school(String graduate_school) {
		this.graduate_school = graduate_school;
	}

	public String getCpa_certificate_no() {
		return cpa_certificate_no;
	}

	public void setCpa_certificate_no(String cpa_certificate_no) {
		this.cpa_certificate_no = cpa_certificate_no;
	}

	public String getTotal_class_hours() {
		return total_class_hours;
	}

	public void setTotal_class_hours(String total_class_hours) {
		this.total_class_hours = total_class_hours;
	}

	public String getReward_and_punishment_information() {
		return reward_and_punishment_information;
	}

	public void setReward_and_punishment_information(String reward_and_punishment_information) {
		this.reward_and_punishment_information = reward_and_punishment_information;
	}

	public String getIs_partner() {
		return is_partner;
	}

	public void setIs_partner(String is_partner) {
		this.is_partner = is_partner;
	}

	public String getApproval_time() {
		return approval_time;
	}

	public void setApproval_time(String approval_time) {
		this.approval_time = approval_time;
	}

	public String getApproval_time_no() {
		return approval_time_no;
	}

	public void setApproval_time_no(String approval_time_no) {
		this.approval_time_no = approval_time_no;
	}

	public String getFirm() {
		return firm;
	}

	public void setFirm(String firm) {
		this.firm = firm;
	}

	public String getApproval_file_no() {
		return approval_file_no;
	}

	public void setApproval_file_no(String approval_file_no) {
		this.approval_file_no = approval_file_no;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getDetail_link() {
		return detail_link;
	}

	public void setDetail_link(String detail_link) {
		this.detail_link = detail_link;
	}

	public String getVolunteer_activity() {
		return volunteer_activity;
	}

	public void setVolunteer_activity(String volunteer_activity) {
		this.volunteer_activity = volunteer_activity;
	}

	public String getIs_party_member() {
		return is_party_member;
	}

	public void setIs_party_member(String is_party_member) {
		this.is_party_member = is_party_member;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApproval_registered_file_no() {
		return approval_registered_file_no;
	}

	public void setApproval_registered_file_no(String approval_registered_file_no) {
		this.approval_registered_file_no = approval_registered_file_no;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getEducation_background() {
		return education_background;
	}

	public void setEducation_background(String education_background) {
		this.education_background = education_background;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getFinished_class_hours() {
		return finished_class_hours;
	}

	public void setFinished_class_hours(String finished_class_hours) {
		this.finished_class_hours = finished_class_hours;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
