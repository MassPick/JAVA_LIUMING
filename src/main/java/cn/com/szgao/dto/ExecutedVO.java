package cn.com.szgao.dto;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;
/**
 * 被执行实体类
 * @author xiongchangyi
 * 2015-6-29
 * @version1.0
 */
@SuppressWarnings("unused")
public class ExecutedVO {
	
	
	private String executedId;
	
	
	public String getExecutedId() {
		return executedId;
	}
	public void setExecutedId(String executedId) {
		this.executedId = executedId;
	}

	private String key;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * id
	 */
	private String id;
	/**
	 * 案子状态：结案，未结案        "caseState": "已结案",  又是0 ????   1 是已结案   0 执行中
	 */
	private String caseState;
//	private Integer caseState;
	
	
public String getCaseState() {
		return caseState;
	}
	public void setCaseState(String caseState) {
		this.caseState = caseState;
	}
	
	//	public Integer getCaseState() {
//		return caseState;
//	}
//	public void setCaseState(Integer caseState) {
//		this.caseState = caseState;
//	}
	/**
	 * 立案时间                  !!!!!!!!!!
	 */
	
	
	private String openTime2;
public String getOpenTime2() {
		return openTime2;
	}
	public void setOpenTime2(String openTime2) {
		this.openTime2 = openTime2;
	}
		private String openTime;
//	private Date openTime;
//	public Date getOpenTime() {
//		return openTime;
//	}
//	public void setOpenTime(Date openTime) {
//		this.openTime = openTime;
//	}
	/**
	 * 被执行人、被执行单位
	 */
	private String executed;
	/**
	 * 采集时间  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	private String collectTime;
//	private Date collectTime;
	
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	private String collectTime2;
	
	public String getCollectTime2() {
		return collectTime2;
	}
	public void setCollectTime2(String collectTime2) {
		this.collectTime2 = collectTime2;
	}
	/**
	 * 身份证号
	 */
	private String idNum;
	/**
	 * 详情URL
	 */
	private String detailLink;
	/**
	 * 执行法院
	 */
	private String courtName;
	/**
	 * 被执行的金额 !!!!!!!!!!!!!!!!!!!
	 */
	private String subjectMatter;
	
//	private Float subjectMatter;
//	public Float getSubjectMatter() {
//		return subjectMatter;
//	}
//	public void setSubjectMatter(Float subjectMatter) {
//		this.subjectMatter = subjectMatter;
//	}
	
	public String getSubjectMatter() {
		return subjectMatter;
	}
	public void setSubjectMatter(String subjectMatter) {
		this.subjectMatter = subjectMatter;
	}

	/**
	 * 案号
	 */
	private String caseNum;
	/**
	 * 组织机构代码
	 */
	private String orgCode;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 地级市
	 */
	private String city;
	/**
	 * 县级市、县、区
	 */
	private String area;
	/**
	 * 责任人   需要与责任公司，一起合并成executed字段
	 */
	private String personName;
	/**
	 * 法定代表人ID
	 */
	private String repId;
	/**
	 * 责任公司，需要与责任人，一起合并成executed字段
	 */
	private String company;
	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 标识是否待验证数据
	 */
	/*private boolean flag;*/	
	/**
	 * 多个行政区划的对象集合
	 */
	/*private List<AdministrativeVO> adminiList = new ArrayList<AdministrativeVO>();*/
	/**
	 * 数据来源 ：1表示被执行，2表示失信示裁判文书，4表示法院公告，5表示工商数据
	 */
	private Integer source;
	/**
	 * 创建日期
	 */
	private Date createDate;
	/**
	 * 修改日期
	 */
	private String modifyDate;
	
//	private String modifyData;
//	
//	
//	public String getModifyData() {
//		return modifyData;
//	}
//	public void setModifyData(String modifyData) {
//		this.modifyData = modifyData;
//	}

	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	/**
	 * 1 表示有效  ,0 表示失效
	 */
	private Integer flag;
	
	/**
	 * 摘要
	 */
	private String summary;
	
	
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
//	public String getCaseState() {
//		return caseState;
//	}
//	public void setCaseState(String caseState) {
//		this.caseState = caseState;
//	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getExecuted() {
		return executed;
	}
	public void setExecuted(String executed) {
		this.executed = executed;
	}
	
	
//	public Date getCollectTime() {
//		return collectTime;
//	}
//	public void setCollectTime(Date collectTime) {
//		this.collectTime = collectTime;
//	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
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
//	public String getSubjectMatter() {
//		return subjectMatter;
//	}
//	public void setSubjectMatter(String subjectMatter) {
//		this.subjectMatter = subjectMatter;
//	}
	
	public String getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	/*public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public List<AdministrativeVO> getAdminiList() {
		return adminiList;
	}
	public void setAdminiList(List<AdministrativeVO> adminiList) {
		this.adminiList = adminiList;
	}*/
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date date) {
		this.createDate = date;
	}
//	public Date getModifyData() {
//		return modifyData;
//	}
//	public void setModifyData(Date date) {
//		this.modifyData = date;
//	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
