package cn.com.szgao.dto;

import java.util.List;

/**
 * 法院4个数据源的实体类
 * 
 * @author xiongchangyi
 *
 */
public class WholeCourtVO {

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

 
 

	private String openTimeNew;// 原 主要是有yyyy-mm-dd

	public String getOpenTimeNew() {
		return openTimeNew;
	}

	public void setOpenTimeNew(String openTimeNew) {
		this.openTimeNew = openTimeNew;
	}
 
	
	private Double subjectMatterN;
	
	public Double getSubjectMatterN() {
		return subjectMatterN;
	}

	public void setSubjectMatterN(Double subjectMatterN) {
		this.subjectMatterN = subjectMatterN;
	}

	 
	 
	
	
	
	/**
	 * 当事人信息
	 */
	private String info_clinets;
	
	/**
	 * 审理经过
	 */
	private String info_trial_by ;
	
	
	
//	一审原告诉称
//	一审被告辩称
	/**
	 * 原告诉称
	 */
	private String info_claims_plai1;
	
	
	/**
	 * 被告辩称
	 */
	private String info_argued_defend1;  
	
	/**
	 * 一审 本院查明
	 */
	private String info_court_find1;
	
	/**
	 * 一审 法院认为
	 */
	private String info_court_held1;
	
	
	
//	二审上诉人诉称
//	二审被上诉人辩称
	
	private String info_claims_plai2;
	private String info_argued_defend2;  
	
	
//	二审法院查明
//	二审法院认为
	private String info_court_find2;
	private String info_court_held2;
	
	
//	再审申请人称/抗诉机关称
//	再审被申请人辩称
	private String info_claims_plai3;
	private String info_argued_defend3;  
	
	
	/**
	 * 本院查明
	 */
	private String info_court_find3;
	
	/**
	 * 法院认为
	 */
	private String info_court_held3;
	
	
	
	/**
	 * 裁判结果
	 */
	private String info_approval;
	
	
	/**
	 * 审判人员
	 */
	private String info_judges;
	
	/**
	 * 引用法规
	 */
	private String info_laws;
	
	
	
	public String getInfo_laws() {
		return info_laws;
	}
	public void setInfo_laws(String info_laws) {
		this.info_laws = info_laws;
	}
	/**
	 * id
	 */
	private String wholeCourtId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 详情URL
	 */
	private String detailLink;
	/**
	 * 文书分类
	 */
	private String catalog;
	/**
	 * 案号
	 */
	private String caseNum;
	/**
	 * 法院名称
	 */
	private String courtName;
	/**
	 * 发布日期 yyyy-MM-dd
	 */
	private String publishDate;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String area;
	/**
	 * 采集日期yyyy-MM-dd'T'HH:mm:ssZZ
	 */
	private String collectDate;
	/**
	 * 原告
	 */
	private String plaintiff;
	/**
	 * 被告
	 */
	private String defendant;
	
   /**
	 * 审批结论
	 */
	private String approval;
	/**
	 * 诉讼类型
	 */
	private String suitType;
	/**
	 * 起诉日期
	 */
	private String suitDate;
	/**
	 * 审结日期
	 */
	private String approvalDate;
	/**
	 * 案由
	 */
	private String caseCause;
	/**
	 * 摘要
	 * 案由+结果 length<=100
	 */
	private String summary;
	/**
	 * 1表示有效，0表示失效
	 */
	private Integer flag;
	/**
	 * 数据来源 ：1表示被执行，2表示失信，3表示裁判文书，4表示法院公告，5表示工商数据
	 */
	private Integer source;	
	/**
	 * 修改日期
	 * yyyy-MM-dd'T'HH:mm:ssZZ
	 *//*
	private String modifyData;*/
	/**
	 * 文书正文html路径
	 */
	private String path;
	//***失信实体****/
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
	/**
	 * 年龄
	 */
	private Integer age;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 立案时间yyyy-MM-dd
	 */
	private String openTime;	
	/**
	 * 关注次数
	 *//*
	private Integer follow;*/
	/**
	 * 职位
	 */
	private String position;
	//***被执行******/
	/**
	 * 案子状态：结案，未结案
	 */
	private String caseState;
	/**
	 * 被执行人、被执行单位
	 */
	private String executed;
	/**
	 * 采集时间yyyy-MM-dd'T'HH:mm:ssZZ
	 */
	private String collectTime;
	/**
	 * 执行标的
	 */
	private String subjectMatter;
	/**
	 * 修改日期yyyy-MM-dd'T'HH:mm:ssZZ
	 */
	private String modifyDate;
	//****公告******/
	/**
	 * 公告内容
	 */
	private String pubContent;
	/**
	 * 法院公告/非法院公告
	 */
	private String isCourtPub;
	/**
	 * 公告类型
	 */
	private String pubType;
	/**
	 * 公告人
	 */
	private String pubPerson;
	/**
	 * 当事人
	 */
	private String client;
	
	/**
	 * 当事人词组
	 */
	private String clients;
	
	public String getClients() {
		return clients;
	}
	public void setClients(String clients) {
		this.clients = clients;
	}
	/**
	 * 公告时间
	 */
	private String pubDate;
	/**
	 * 公告内容链接（PDF）
	 */
	private String pdfLink;
	/**
	 * 公告主体
	 */
	private String subject;
	/**
	 * 公告、文书的正文
	 */
	private String content;
	/**
	 * approvalDateNew
	 */
    private String approvalDateNew;
    /**
     * publishDateNew
     */
    private String publishDateNew;
    /**
	 * 关联文书
	 */
	private List<RelativeVO> relativeCases;
	
	
	/**
	 * 结尾处的审判员词组
	 */
	private String judges;
	/**
	 * 数据来源    1   旧版高院    2  旧版地方法院   3  新版    4 旧版高院按法院名查      5  旧版地方法院Excel   7 中国裁判文书网展示文书详细页面（旧版高院）excel  
	 *  81 地方标1  82 地方标2  83地方其他    浙江省_浙江法院公开网 10
	 *  
	 *  地方doc  85   地方swf  86  
	 *  
	 *  人民法院网公告  90
	 */
	private Integer dataFrom;
	/** 
	 * 文书类型   民事裁定书
	 */
	private String writType;
	
	/**
	 * 文件路径
	 */
	private String filePath;
	
	
	/**
	 * 文件路径 Html
	 */
	private String filePathHtml;
	
	
	/**
	 * 律师
	 */
	private String lawyer;
	/**
	 * 律师 原告
	 */
	private String lawyerP;
	
	/**
	 * 律师事务所
	 */
	private String lawOffice;
	

	
	
	//--------------------------------------------
	/**
	 * 审判长
	 */
	private String judgeC;
	
	/**
	 * 结审时间年
	 */
	private String approvalDateY;
	
	/**
	 * 结审时间年月
	 */
	private String approvalDateYM;
	
	/**
	 * 律师事务所 原告
	 */
	private String lawOfficeP;
	
	/**
	 * 律师事务所 被告
	 */
	private String lawOfficeD;
	
	/**
	 * 律师 被告
	 */
	private String lawyerD;
	
	/**
	 * 法院层级   1 最高   2 高级  3 中级  4 基层
	 */
	private String  courtLev;
	
	/**
	 * 案由1级
	 */
	private String type1;
	
	/**
	 * 案由2级
	 */
	private String type2;
	/**
	 * 案由3级
	 */
	private String type3;
	/**
	 * 案由4级
	 */
	private String type4;
	/**
	 * 案由5级
	 */
	private String type5;
	
	/**
	 * 判决TYPE     判决，裁定
	 */
	private String approvalType;
	
	/**
	 * 判决结果
	 */
	private String approvalReslut;
	
	
	/**
	 * 法律条文
	 */
	private String laws;
	
	
	
	
	
	//---企业的省市县
	/**
	 * 省
	 */
	private String province_e;

	/**
	 * 市
	 */
	private String city_e;
	/**
	 * 区
	 */
	private String area_e;
	
	
	//---------------------一、二、再审案号
	private String caseNum1;
	private String caseNum2;
	private String caseNum3;
	
	/**
	 * 案件简介  就是第一段落
	 */
	private String caseBrief;
	
	public String getCaseBrief() {
		return caseBrief;
	}

	public void setCaseBrief(String caseBrief) {
		this.caseBrief = caseBrief;
	}

	public String getCaseNum1() {
		return caseNum1;
	}

	public void setCaseNum1(String caseNum1) {
		this.caseNum1 = caseNum1;
	}

	public String getCaseNum2() {
		return caseNum2;
	}

	public void setCaseNum2(String caseNum2) {
		this.caseNum2 = caseNum2;
	}

	public String getCaseNum3() {
		return caseNum3;
	}

	public void setCaseNum3(String caseNum3) {
		this.caseNum3 = caseNum3;
	}
	
	public String getProvince_e() {
		return province_e;
	}

	public void setProvince_e(String province_e) {
		this.province_e = province_e;
	}

	public String getCity_e() {
		return city_e;
	}

	public void setCity_e(String city_e) {
		this.city_e = city_e;
	}

	public String getArea_e() {
		return area_e;
	}

	public void setArea_e(String area_e) {
		this.area_e = area_e;
	}

	
	public String getLaws() {
		return laws;
	}
	public void setLaws(String laws) {
		this.laws = laws;
	}
	public String getApprovalReslut() {
		return approvalReslut;
	}
	public void setApprovalReslut(String approvalReslut) {
		this.approvalReslut = approvalReslut;
	}
	public String getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}
	public String getType1() {
		return type1;
	}
	public void setType1(String type1) {
		this.type1 = type1;
	}
	public String getType2() {
		return type2;
	}
	public void setType2(String type2) {
		this.type2 = type2;
	}
	public String getType3() {
		return type3;
	}
	public void setType3(String type3) {
		this.type3 = type3;
	}
	public String getType4() {
		return type4;
	}
	public void setType4(String type4) {
		this.type4 = type4;
	}
	public String getType5() {
		return type5;
	}
	public void setType5(String type5) {
		this.type5 = type5;
	}
	
	
	public String getCourtLev() {
		return courtLev;
	}
	public void setCourtLev(String courtLev) {
		this.courtLev = courtLev;
	}
	public String getJudgeC() {
		return judgeC;
	}
	public void setJudgeC(String judgeC) {
		this.judgeC = judgeC;
	}
	public String getApprovalDateY() {
		return approvalDateY;
	}
	public void setApprovalDateY(String approvalDateY) {
		this.approvalDateY = approvalDateY;
	}
	public String getApprovalDateYM() {
		return approvalDateYM;
	}
	public void setApprovalDateYM(String approvalDateYM) {
		this.approvalDateYM = approvalDateYM;
	}
	public String getLawOffice() {
		return lawOffice;
	}
	public void setLawOffice(String lawOffice) {
		this.lawOffice = lawOffice;
	}
	public String getLawyer() {
		return lawyer;
	}
	public void setLawyer(String lawyer) {
		this.lawyer = lawyer;
	}
	public String getLawOfficeP() {
		return lawOfficeP;
	}
	public void setLawOfficeP(String lawOfficeP) {
		this.lawOfficeP = lawOfficeP;
	}
	public String getLawyerP() {
		return lawyerP;
	}
	public void setLawyerP(String lawyerP) {
		this.lawyerP = lawyerP;
	}
	public String getLawOfficeD() {
		return lawOfficeD;
	}
	public void setLawOfficeD(String lawOfficeD) {
		this.lawOfficeD = lawOfficeD;
	}
	public String getLawyerD() {
		return lawyerD;
	}
	public void setLawyerD(String lawyerD) {
		this.lawyerD = lawyerD;
	}
	
	
	public String getFilePathHtml() {
		return filePathHtml;
	}
	public void setFilePathHtml(String filePathHtml) {
		this.filePathHtml = filePathHtml;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	 
	public Integer getDataFrom() {
		return dataFrom;
	}
	public void setDataFrom(Integer dataFrom) {
		this.dataFrom = dataFrom;
	}
	
	
	
	public String getWritType() {
		return writType;
	}
	public void setWritType(String writType) {
		this.writType = writType;
	}
	
	
	public String getJudges() {
		return judges;
	}
	public void setJudges(String judges) {
		this.judges = judges;
	}
	private String updateTime;
    private String createTime;
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetailLink() {
		return detailLink;
	}
	public void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
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
	public String getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}
	public String getPlaintiff() {
		return plaintiff;
	}
	public void setPlaintiff(String plaintiff) {
		this.plaintiff = plaintiff;
	}
	public String getDefendant() {
		return defendant;
	}
	public void setDefendant(String defendant) {
		this.defendant = defendant;
	}
	public String getApproval() {
		return approval;
	}
	public void setApproval(String approval) {
		this.approval = approval;
	}
	public String getSuitType() {
		return suitType;
	}
	public void setSuitType(String suitType) {
		this.suitType = suitType;
	}
	public String getSuitDate() {
		return suitDate;
	}
	public void setSuitDate(String suitDate) {
		this.suitDate = suitDate;
	}
	public String getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getCaseCause() {
		return caseCause;
	}
	public void setCaseCause(String caseCause) {
		this.caseCause = caseCause;
	}
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
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getCaseState() {
		return caseState;
	}
	public void setCaseState(String caseState) {
		this.caseState = caseState;
	}
	public String getExecuted() {
		return executed;
	}
	public void setExecuted(String executed) {
		this.executed = executed;
	}
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public String getSubjectMatter() {
		return subjectMatter;
	}
	public void setSubjectMatter(String subjectMatter) {
		this.subjectMatter = subjectMatter;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getPubContent() {
		return pubContent;
	}
	public void setPubContent(String pubContent) {
		this.pubContent = pubContent;
	}
	public String getIsCourtPub() {
		return isCourtPub;
	}
	public void setIsCourtPub(String isCourtPub) {
		this.isCourtPub = isCourtPub;
	}
	public String getPubType() {
		return pubType;
	}
	public void setPubType(String pubType) {
		this.pubType = pubType;
	}
	public String getPubPerson() {
		return pubPerson;
	}
	public void setPubPerson(String pubPerson) {
		this.pubPerson = pubPerson;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getPdfLink() {
		return pdfLink;
	}
	public void setPdfLink(String pdfLink) {
		this.pdfLink = pdfLink;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getWholeCourtId() {
		return wholeCourtId;
	}
	public void setWholeCourtId(String wholeCourtId) {
		this.wholeCourtId = wholeCourtId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getApprovalDateNew() {
		return approvalDateNew;
	}
	public void setApprovalDateNew(String approvalDateNew) {
		this.approvalDateNew = approvalDateNew;
	}
	public String getPublishDateNew() {
		return publishDateNew;
	}
	public void setPublishDateNew(String publishDateNew) {
		this.publishDateNew = publishDateNew;
	}
	public List<RelativeVO> getRelativeCases() {
		return relativeCases;
	}
	public void setRelativeCases(List<RelativeVO> relativeCases) {
		this.relativeCases = relativeCases;
	}


	
	public String getInfo_clinets() {
		return info_clinets;
	}
	public void setInfo_clinets(String info_clinets) {
		this.info_clinets = info_clinets;
	}
	public String getInfo_trial_by() {
		return info_trial_by;
	}
	public void setInfo_trial_by(String info_trial_by) {
		this.info_trial_by = info_trial_by;
	}
	 
	public String getInfo_approval() {
		return info_approval;
	}
	public void setInfo_approval(String info_approval) {
		this.info_approval = info_approval;
	}
	public String getInfo_judges() {
		return info_judges;
	}
	public void setInfo_judges(String info_judges) {
		this.info_judges = info_judges;
	}
	
	public String getInfo_claims_plai1() {
		return info_claims_plai1;
	}
	public void setInfo_claims_plai1(String info_claims_plai1) {
		this.info_claims_plai1 = info_claims_plai1;
	}
	public String getInfo_argued_defend1() {
		return info_argued_defend1;
	}
	public void setInfo_argued_defend1(String info_argued_defend1) {
		this.info_argued_defend1 = info_argued_defend1;
	}
	public String getInfo_court_find1() {
		return info_court_find1;
	}
	public void setInfo_court_find1(String info_court_find1) {
		this.info_court_find1 = info_court_find1;
	}
	public String getInfo_court_held1() {
		return info_court_held1;
	}
	public void setInfo_court_held1(String info_court_held1) {
		this.info_court_held1 = info_court_held1;
	}
	public String getInfo_claims_plai2() {
		return info_claims_plai2;
	}
	public void setInfo_claims_plai2(String info_claims_plai2) {
		this.info_claims_plai2 = info_claims_plai2;
	}
	public String getInfo_argued_defend2() {
		return info_argued_defend2;
	}
	public void setInfo_argued_defend2(String info_argued_defend2) {
		this.info_argued_defend2 = info_argued_defend2;
	}
	public String getInfo_court_find2() {
		return info_court_find2;
	}
	public void setInfo_court_find2(String info_court_find2) {
		this.info_court_find2 = info_court_find2;
	}
	public String getInfo_court_held2() {
		return info_court_held2;
	}
	public void setInfo_court_held2(String info_court_held2) {
		this.info_court_held2 = info_court_held2;
	}
	public String getInfo_claims_plai3() {
		return info_claims_plai3;
	}
	public void setInfo_claims_plai3(String info_claims_plai3) {
		this.info_claims_plai3 = info_claims_plai3;
	}
	public String getInfo_argued_defend3() {
		return info_argued_defend3;
	}
	public void setInfo_argued_defend3(String info_argued_defend3) {
		this.info_argued_defend3 = info_argued_defend3;
	}
	public String getInfo_court_find3() {
		return info_court_find3;
	}
	public void setInfo_court_find3(String info_court_find3) {
		this.info_court_find3 = info_court_find3;
	}
	public String getInfo_court_held3() {
		return info_court_held3;
	}
	public void setInfo_court_held3(String info_court_held3) {
		this.info_court_held3 = info_court_held3;
	}
	
	
	
	
	
	

}
