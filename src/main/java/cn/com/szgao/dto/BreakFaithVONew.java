package cn.com.szgao.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 对应Bucket=breakFaith里面的字段
 * @author xiongchangyi
 * 2015-6-26
 */
@SuppressWarnings("unused")
public class BreakFaithVONew {
	
	/**
	 * ID
	 */
	private String id;
	
	/**
	 * 自然人ID，自然人与公司建立关系使用
	 */
	private String repId;
	/**
	 * 公司ID，自然人与公司建立关系使用
	 */
	private String companyId;	
	/**
	 * 身份证
	 */
	private String idNum;
	/**
	 * 法定代表人或者负责人姓名
	 */
	private String repName;
	/**
	 * 做出执行依据单位
	 */
	private String department;
	/**
	 * 生效法律文书确定的义务
	 */
	private String liability;
	/**
	 * 失信被执行人行为具体情形
	 */
	private String detail;
	/**
	 * 被执行人姓名
	 */
	private String personName;
	/**
	 * 案号
	 */
	private String caseNum;
	/**
	 * 详细页面面的网址
	 */
	private String detailLink;
	/**
	 * 执行法院名称
	 */
	private String courtName;
	/**
	 * 组织机构代码
	 */
	private String orgCode;
	/**
	 * 被执行人的履行情况
	 */
	private String performance;
	/**
	 * 执行依据文号
	 */
	private String executeNum;
	/**
	 * 公司
	 */
	private String company;
//	/**
//	 * 年龄
//	 */
//	private String age;
	/**
	 * 性别
	 */
	private String gender;
//	/**
//	 * 立案时间
//	 */
//	private String openTime;
//	/**
//	 * 采集时间
//	 */
//	private String collectDate;
//	/**
//	 * 发布时间
//	 */
//	private String publishDate;
	/**
	 * 关注次数
	 */
//	private String follow;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 县、区
	 */
	private String area;
	
	/**
	 * 服务查询 flag = true的记录
      flag＝false表示该记录官网已经下架了该记录.
	 */
//	private Boolean flag;
	/**
	 *  1是有效，0是失效
	 */
	private Integer flag;
	
	/**
	 * 立案时间
	 */
	private String openTime;
	/**
	 * 采集时间
	 */
	private String collectDate;
	/**
	 * 发布时间
	 */
	private String publishDate;
	
	/**
	 * 年龄
	 */
	private Integer age;
	
	
	/**
	 * 1表示被执行，2表示失信,3表示裁判文书，4表示法院公告，5表示工商数据
	 */
	private Integer source;
	
	/**
	 * 摘要
	 */
	private String summary;
	
	/**
	 * 修改时间
	 */
//	private String modifyData;
//	private Date modifyData;
	
	
	/**
	 * 键 key
	 */
	private String key;
	
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getModifyData() {
		return modifyData;
	}
	public void setModifyData(String modifyData) {
		this.modifyData = modifyData;
	}
	 
	
	 

	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
	
	
	
	
//	/**
//	 * 行政区划对象集合
//	 */
//	//private List<AdministrativeVO> adminiList = new ArrayList<AdministrativeVO>();
//	/**
//	 * 标识是否待验证数据
//	 */
//	//private boolean flag;	
//	/**
//	 * 数据来源 ：1表示被执行，2表示失信，3表示裁判文书，4表示法院公告，5表示工商数据
//	 */
//	//private Integer source;
//	/**
//	 * 创建日期
//	 */
//	//private String createDate;
	/**
	 * 修改日期
	 */
	private String modifyData;
//	/**
//	 * 职位
//	 */
//	//private String position;
	

	
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getRepName() {
		return repName;
	}
	public void setRepName(String repName) {
		this.repName = repName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getLiability() {
		return liability;
	}
	public void setLiability(String liability) {
		this.liability = liability;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
	public String getDetailLink() {
		return detailLink;
	}
	public void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getPerformance() {
		return performance;
	}
	public void setPerformance(String performance) {
		this.performance = performance;
	}
	public String getExecuteNum() {
		return executeNum;
	}
	public void setExecuteNum(String executeNum) {
		this.executeNum = executeNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	private String openTimeNew;//原 主要是有yyyy-mm-dd	
	public String getOpenTimeNew() {
		return openTimeNew;
	}
	public void setOpenTimeNew(String openTimeNew) {
		this.openTimeNew = openTimeNew;
	}
//	public String getFollow() {
//		return follow;
//	}
//	public void setFollow(String follow) {
//		this.follow = follow;
//	}
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
	/*
	public List<AdministrativeVO> getAdminiList() {
		return adminiList;
	}
	public void setAdminiList(List<AdministrativeVO> adminiList) {
		this.adminiList = adminiList;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getModifyData() {
		return modifyData;
	}
	public void setModifyData(String modifyData) {
		this.modifyData = modifyData;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}	
	*/
	public String getRepId() {
		return repId;
	}
	public void setRepId(String repId) {
		this.repId = repId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
}
