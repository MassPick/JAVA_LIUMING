package cn.com.szgao.dto;

import java.io.Serializable;

/**
 * 税务评级+重大税务违法
 * @author liuming
 * @Date 2016年8月8日 下午4:13:08
 */
public class TaxLawVO  implements Serializable{
	private static final long serialVersionUID = 1L;
	private String uuid;
	
	
	
	/**
	 * 身份证
	 */
	private String id_num;
	public String getId_num() {
		return id_num;
	}

	public void setId_num(String id_num) {
		this.id_num = id_num;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * id_type 身份证
	 */
	private String id_type;
	
	private String name;
	
	private String gender;
	
	/**
	 * /**
	 * 评级：违法评级为D，企业信用评级为A，其他为B/C
	 */
	private String rate;
	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	private String year;
	
	

	/**
	 * 案件性质
	 */
	private String case_nature;
	
	private String detail_link;
	
	/**
	 * 负有直接责任的财务负责人姓名
	 */
	private String financial_chief_name;
	
	private String province;
	
	/**
	 *法定代表人或者负责人姓名 
	 */
	private String rep_name;
	
	/**
	 * 组织代码
	 */
	private String org_code;
	
	/**
	 * 负有直接责任的中介机构信息及其从业人员信息
	 */
	private String intermediary_info;
	
	/**
	 * 主要违法事实
	 */
	private String llegal_activity;
	
	/**
	 * 纳税人识别号
	 */
	private String taxpayer_id;
	
	private String collect_time;
	
	
	
	/**
	 * 注册地址
	 */
	private String reg_addr;
	/**
	 * 法定代表人性别
	 */
	private String rep_gender;
	
	/**
	 * 纳税人名称
	 */
	private String taxpayer_name;
	
	/**
	 * 负有直接责任的财务负责人性别
	 */
	private String financial_chief_gender;
	
	/**
	 * 税务处理处罚情况
	 */
	private String punishment;
	
	/**
	 * 负有直接责任的财务负责人证件名称及号码
	 */
	private String financial_chief_id;
	
	/**
	 * 负有直接责任的财务负责人证件名称及号码   身份证
	 */
	private String financial_chief_id_type;
	
	/**
	 * 负有直接责任的财务负责人证件名称及号码
	 */
	private String financial_certificate;
	
	/**
	 * 身份证
	 */
	private String rep_id_type;
	
	/**
	 * 法定代表人证件名称及号码
	 */
	private String rep_id;
	
	/**
	 * 法定代表人证件名称及号码
	 */
	private String legalrep_certificate;
	
	
	/**
	 * true表示企业重大纳税违法；false表示自然人重大纳税违法.
	 */
	private String type;
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCase_nature() {
		return case_nature;
	}

	public void setCase_nature(String case_nature) {
		this.case_nature = case_nature;
	}

	public String getDetail_link() {
		return detail_link;
	}

	public void setDetail_link(String detail_link) {
		this.detail_link = detail_link;
	}

	public String getFinancial_chief_name() {
		return financial_chief_name;
	}

	public void setFinancial_chief_name(String financial_chief_name) {
		this.financial_chief_name = financial_chief_name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRep_name() {
		return rep_name;
	}

	public void setRep_name(String rep_name) {
		this.rep_name = rep_name;
	}

	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public String getIntermediary_info() {
		return intermediary_info;
	}

	public void setIntermediary_info(String intermediary_info) {
		this.intermediary_info = intermediary_info;
	}

	public String getLlegal_activity() {
		return llegal_activity;
	}

	public void setLlegal_activity(String llegal_activity) {
		this.llegal_activity = llegal_activity;
	}

	public String getTaxpayer_id() {
		return taxpayer_id;
	}

	public void setTaxpayer_id(String taxpayer_id) {
		this.taxpayer_id = taxpayer_id;
	}

	public String getCollect_time() {
		return collect_time;
	}

	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}

	public String getReg_addr() {
		return reg_addr;
	}

	public void setReg_addr(String reg_addr) {
		this.reg_addr = reg_addr;
	}

	public String getRep_gender() {
		return rep_gender;
	}

	public void setRep_gender(String rep_gender) {
		this.rep_gender = rep_gender;
	}

	public String getTaxpayer_name() {
		return taxpayer_name;
	}

	public void setTaxpayer_name(String taxpayer_name) {
		this.taxpayer_name = taxpayer_name;
	}

	public String getFinancial_chief_gender() {
		return financial_chief_gender;
	}

	public void setFinancial_chief_gender(String financial_chief_gender) {
		this.financial_chief_gender = financial_chief_gender;
	}

	public String getPunishment() {
		return punishment;
	}

	public void setPunishment(String punishment) {
		this.punishment = punishment;
	}

	public String getFinancial_chief_id() {
		return financial_chief_id;
	}

	public void setFinancial_chief_id(String financial_chief_id) {
		this.financial_chief_id = financial_chief_id;
	}

	public String getFinancial_chief_id_type() {
		return financial_chief_id_type;
	}

	public void setFinancial_chief_id_type(String financial_chief_id_type) {
		this.financial_chief_id_type = financial_chief_id_type;
	}

	public String getFinancial_certificate() {
		return financial_certificate;
	}

	public void setFinancial_certificate(String financial_certificate) {
		this.financial_certificate = financial_certificate;
	}

	public String getRep_id_type() {
		return rep_id_type;
	}

	public void setRep_id_type(String rep_id_type) {
		this.rep_id_type = rep_id_type;
	}

	public String getRep_id() {
		return rep_id;
	}

	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}

	public String getLegalrep_certificate() {
		return legalrep_certificate;
	}

	public void setLegalrep_certificate(String legalrep_certificate) {
		this.legalrep_certificate = legalrep_certificate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
