package cn.com.szgao.dto;

import java.util.List;

/**
 * 法院4个数据源的实体类
 * 
 * @author xiongchangyi
 *
 */
public class WholeCourtNameVO {

	/**
	 * 法院名称
	 */
	private String courtName;

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	
	/**
	 * 1 新数据  0旧数据
	 */
	private Integer flag;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	

}
//
// public class WholeCourtVO {
// private String wholeCourtId;
// @Override
// public String toString() {
// return "WholeCourtVO [wholeCourtId=" + wholeCourtId + ", title=" + title + ",
// detailLink=" + detailLink
// + ", catalog=" + catalog + ", caseNum=" + caseNum + ", courtName=" +
// courtName + ", publishDate="
// + publishDate + ", province=" + province + ", city=" + city + ", area=" +
// area + ", collectDate="
// + collectDate + ", plaintiff=" + plaintiff + ", defendant=" + defendant + ",
// approval=" + approval
// + ", suitType=" + suitType + ", suitDate=" + suitDate + ", approvalDate=" +
// approvalDate
// + ", caseCause=" + caseCause + ", summary=" + summary + ", flag=" + flag + ",
// source=" + source
// + ", modifyData=" + modifyData + ", path=" + path + ", idNum=" + idNum + ",
// repName=" + repName
// + ", department=" + department + ", liability=" + liability + ", detail=" +
// detail + ", personName="
// + personName + ", orgCode=" + orgCode + ", performance=" + performance + ",
// executeNum=" + executeNum
// + ", company=" + company + ", age=" + age + ", gender=" + gender + ",
// openTime=" + openTime
// + ", position=" + position + ", caseState=" + caseState + ", executed=" +
// executed + ", collectTime="
// + collectTime + ", subjectMatter=" + subjectMatter + ", modifyDate=" +
// modifyDate + ", pubContent="
// + pubContent + ", isCourtPub=" + isCourtPub + ", pubType=" + pubType + ",
// pubPerson=" + pubPerson
// + ", client=" + client + ", pubDate=" + pubDate + ", pdfLink=" + pdfLink + ",
// subject=" + subject
// + ", content=" + content + "]";
// }
// //*******裁判文书********/
// /**
// * 标题
// */
// private String title;
// /**
// * 详情URL
// */
// private String detailLink;
// /**
// * 文书分类
// */
// private String catalog;
// /**
// * 案号
// */
// private String caseNum;
// /**
// * 法院名称
// */
// private String courtName;
// /**
// * 发布日期 yyyy-MM-dd
// */
// private String publishDate;
// /**
// * 省
// */
// private String province;
// /**
// * 市
// */
// private String city;
// /**
// * 区
// */
// private String area;
// /**
// * 采集日期yyyy-MM-dd'T'HH:mm:ssZZ
// */
// private String collectDate;
// /**
// * 原告
// */
// private String plaintiff;
// /**
// * 被告
// */
// private String defendant;
//
// /**
// * 审批结论
// */
// private String approval;
// /**
// * 诉讼类型
// */
// private String suitType;
// /**
// * 起诉日期
// */
// private String suitDate;
// /**
// * 审结日期
// */
// private String approvalDate;
// /**
// * 案由
// */
// private String caseCause;
// /**
// * 摘要
// * 案由+结果 length<=100
// */
// private String summary;
// /**
// * 1表示有效，0表示失效
// */
// private Integer flag;
// /**
// * 数据来源 ：1表示被执行，2表示失信，3表示裁判文书，4表示法院公告，5表示工商数据
// */
// private Integer source;
// /**
// * 修改日期
// * yyyy-MM-dd'T'HH:mm:ssZZ
// */
// private String modifyData;
// /**
// * 文书正文html路径
// */
// private String path;
// //***失信实体****/
// /**
// * 身份证
// */
// private String idNum;
// /**
// * 法定代表人或者负责人姓名
// */
// private String repName;
// /**
// * 做出执行依据单位
// */
// private String department;
// /**
// * 生效法律文书确定的义务
// */
// private String liability;
// /**
// * 失信被执行人行为具体情形
// */
// private String detail;
// /**
// * 被执行人姓名
// */
// private String personName;
// /**
// * 组织机构代码
// */
// private String orgCode;
// /**
// * 被执行人的履行情况
// */
// private String performance;
// /**
// * 执行依据文号
// */
// private String executeNum;
// /**
// * 公司
// */
// private String company;
// /**
// * 年龄
// */
// private Integer age;
// /**
// * 性别
// */
// private String gender;
// /**
// * 立案时间yyyy-MM-dd
// */
// private String openTime;
// /**
// * 关注次数
// */
//// private Integer follow;
// /**
// * 职位
// */
// private String position;
// //***被执行******/
// /**
// * 案子状态：结案，未结案
// */
// private String caseState;
// /**
// * 被执行人、被执行单位
// */
// private String executed;
// /**
// * 采集时间yyyy-MM-dd'T'HH:mm:ssZZ
// */
// private String collectTime;
// /**
// * 执行标的
// */
// private String subjectMatter;
// /**
// * 修改日期yyyy-MM-dd'T'HH:mm:ssZZ
// */
// private String modifyDate;
// //****公告******/
// /**
// * 公告内容
// */
// private String pubContent;
// /**
// * 法院公告/非法院公告
// */
// private String isCourtPub;
// /**
// * 公告类型
// */
// private String pubType;
// /**
// * 公告人
// */
// private String pubPerson;
// /**
// * 当事人
// */
// private String client;
// /**
// * 公告时间
// */
// private String pubDate;
// /**
// * 公告内容链接（PDF）
// */
// private String pdfLink;
// /**
// * 公告主体
// */
// private String subject;
// /**
// * 公告、文书的正文
// */
// private String content;
//
// public String getTitle() {
// return title;
// }
// public void setTitle(String title) {
// this.title = title;
// }
// public String getDetailLink() {
// return detailLink;
// }
// public void setDetailLink(String detailLink) {
// this.detailLink = detailLink;
// }
// public String getCatalog() {
// return catalog;
// }
// public void setCatalog(String catalog) {
// this.catalog = catalog;
// }
// public String getCaseNum() {
// return caseNum;
// }
// public void setCaseNum(String caseNum) {
// this.caseNum = caseNum;
// }
// public String getCourtName() {
// return courtName;
// }
// public void setCourtName(String courtName) {
// this.courtName = courtName;
// }
// public String getPublishDate() {
// return publishDate;
// }
// public void setPublishDate(String publishDate) {
// this.publishDate = publishDate;
// }
// public String getProvince() {
// return province;
// }
// public void setProvince(String province) {
// this.province = province;
// }
// public String getCity() {
// return city;
// }
// public void setCity(String city) {
// this.city = city;
// }
// public String getArea() {
// return area;
// }
// public void setArea(String area) {
// this.area = area;
// }
// public String getCollectDate() {
// return collectDate;
// }
// public void setCollectDate(String collectDate) {
// this.collectDate = collectDate;
// }
// public String getPlaintiff() {
// return plaintiff;
// }
// public void setPlaintiff(String plaintiff) {
// this.plaintiff = plaintiff;
// }
// public String getDefendant() {
// return defendant;
// }
// public void setDefendant(String defendant) {
// this.defendant = defendant;
// }
// public String getApproval() {
// return approval;
// }
// public void setApproval(String approval) {
// this.approval = approval;
// }
// public String getSuitType() {
// return suitType;
// }
// public void setSuitType(String suitType) {
// this.suitType = suitType;
// }
// public String getSuitDate() {
// return suitDate;
// }
// public void setSuitDate(String suitDate) {
// this.suitDate = suitDate;
// }
// public String getApprovalDate() {
// return approvalDate;
// }
// public void setApprovalDate(String approvalDate) {
// this.approvalDate = approvalDate;
// }
// public String getCaseCause() {
// return caseCause;
// }
// public void setCaseCause(String caseCause) {
// this.caseCause = caseCause;
// }
// public String getSummary() {
// return summary;
// }
// public void setSummary(String summary) {
// this.summary = summary;
// }
// public Integer getFlag() {
// return flag;
// }
// public void setFlag(Integer flag) {
// this.flag = flag;
// }
// public Integer getSource() {
// return source;
// }
// public void setSource(Integer source) {
// this.source = source;
// }
// public String getModifyData() {
// return modifyData;
// }
// public void setModifyData(String modifyData) {
// this.modifyData = modifyData;
// }
// public String getPath() {
// return path;
// }
// public void setPath(String path) {
// this.path = path;
// }
// public String getIdNum() {
// return idNum;
// }
// public void setIdNum(String idNum) {
// this.idNum = idNum;
// }
// public String getRepName() {
// return repName;
// }
// public void setRepName(String repName) {
// this.repName = repName;
// }
// public String getDepartment() {
// return department;
// }
// public void setDepartment(String department) {
// this.department = department;
// }
// public String getLiability() {
// return liability;
// }
// public void setLiability(String liability) {
// this.liability = liability;
// }
// public String getDetail() {
// return detail;
// }
// public void setDetail(String detail) {
// this.detail = detail;
// }
// public String getPersonName() {
// return personName;
// }
// public void setPersonName(String personName) {
// this.personName = personName;
// }
// public String getOrgCode() {
// return orgCode;
// }
// public void setOrgCode(String orgCode) {
// this.orgCode = orgCode;
// }
// public String getPerformance() {
// return performance;
// }
// public void setPerformance(String performance) {
// this.performance = performance;
// }
// public String getExecuteNum() {
// return executeNum;
// }
// public void setExecuteNum(String executeNum) {
// this.executeNum = executeNum;
// }
// public String getCompany() {
// return company;
// }
// public void setCompany(String company) {
// this.company = company;
// }
// public Integer getAge() {
// return age;
// }
// public void setAge(Integer age) {
// this.age = age;
// }
// public String getGender() {
// return gender;
// }
// public void setGender(String gender) {
// this.gender = gender;
// }
// public String getOpenTime() {
// return openTime;
// }
// public void setOpenTime(String openTime) {
// this.openTime = openTime;
// }
//// public Integer getFollow() {
//// return follow;
//// }
//// public void setFollow(Integer follow) {
//// this.follow = follow;
//// }
// public String getPosition() {
// return position;
// }
// public void setPosition(String position) {
// this.position = position;
// }
// public String getCaseState() {
// return caseState;
// }
// public void setCaseState(String caseState) {
// this.caseState = caseState;
// }
// public String getExecuted() {
// return executed;
// }
// public void setExecuted(String executed) {
// this.executed = executed;
// }
// public String getCollectTime() {
// return collectTime;
// }
// public void setCollectTime(String collectTime) {
// this.collectTime = collectTime;
// }
// public String getSubjectMatter() {
// return subjectMatter;
// }
// public void setSubjectMatter(String subjectMatter) {
// this.subjectMatter = subjectMatter;
// }
// public String getModifyDate() {
// return modifyDate;
// }
// public void setModifyDate(String modifyDate) {
// this.modifyDate = modifyDate;
// }
// public String getPubContent() {
// return pubContent;
// }
// public void setPubContent(String pubContent) {
// this.pubContent = pubContent;
// }
// public String getIsCourtPub() {
// return isCourtPub;
// }
// public void setIsCourtPub(String isCourtPub) {
// this.isCourtPub = isCourtPub;
// }
// public String getPubType() {
// return pubType;
// }
// public void setPubType(String pubType) {
// this.pubType = pubType;
// }
// public String getPubPerson() {
// return pubPerson;
// }
// public void setPubPerson(String pubPerson) {
// this.pubPerson = pubPerson;
// }
// public String getClient() {
// return client;
// }
// public void setClient(String client) {
// this.client = client;
// }
// public String getPubDate() {
// return pubDate;
// }
// public void setPubDate(String pubDate) {
// this.pubDate = pubDate;
// }
// public String getPdfLink() {
// return pdfLink;
// }
// public void setPdfLink(String pdfLink) {
// this.pdfLink = pdfLink;
// }
// public String getSubject() {
// return subject;
// }
// public void setSubject(String subject) {
// this.subject = subject;
// }
// public String getContent() {
// return content;
// }
// public void setContent(String content) {
// this.content = content;
// }
// public String getWholeCourtId() {
// return wholeCourtId;
// }
// public void setWholeCourtId(String wholeCourtId) {
// this.wholeCourtId = wholeCourtId;
// }
// }
