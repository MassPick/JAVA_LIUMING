package cn.com.szgao.dto;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
 
import java.util.List;
import java.util.Locale;

/**
 * 对应Bucket=breakFaith里面的字段
 * @author xiongchangyi
 * 2015-6-26
 */
@SuppressWarnings("unused")
public class BreakFaithVO {
	
	/**
	 * ID
	 */
	private String id;
	
	
	private String breakFaithId;
	
	public String getBreakFaithId() {
		return breakFaithId;
	}
	public void setBreakFaithId(String breakFaithId) {
		this.breakFaithId = breakFaithId;
	}
	public char charAt(int arg0) {
		return breakFaithId.charAt(arg0);
	}
	public int codePointAt(int arg0) {
		return breakFaithId.codePointAt(arg0);
	}
	public int codePointBefore(int arg0) {
		return breakFaithId.codePointBefore(arg0);
	}
	public int codePointCount(int arg0, int arg1) {
		return breakFaithId.codePointCount(arg0, arg1);
	}
	public int compareTo(String arg0) {
		return breakFaithId.compareTo(arg0);
	}
	public int compareToIgnoreCase(String arg0) {
		return breakFaithId.compareToIgnoreCase(arg0);
	}
	public String concat(String arg0) {
		return breakFaithId.concat(arg0);
	}
	public boolean contains(CharSequence arg0) {
		return breakFaithId.contains(arg0);
	}
	public boolean contentEquals(CharSequence arg0) {
		return breakFaithId.contentEquals(arg0);
	}
	public boolean contentEquals(StringBuffer arg0) {
		return breakFaithId.contentEquals(arg0);
	}
	public boolean endsWith(String arg0) {
		return breakFaithId.endsWith(arg0);
	}
	public boolean equals(Object arg0) {
		return breakFaithId.equals(arg0);
	}
	public boolean equalsIgnoreCase(String arg0) {
		return breakFaithId.equalsIgnoreCase(arg0);
	}
	public byte[] getBytes() {
		return breakFaithId.getBytes();
	}
	public byte[] getBytes(Charset arg0) {
		return breakFaithId.getBytes(arg0);
	}
	public void getBytes(int arg0, int arg1, byte[] arg2, int arg3) {
		breakFaithId.getBytes(arg0, arg1, arg2, arg3);
	}
	public byte[] getBytes(String arg0) throws UnsupportedEncodingException {
		return breakFaithId.getBytes(arg0);
	}
	public void getChars(int arg0, int arg1, char[] arg2, int arg3) {
		breakFaithId.getChars(arg0, arg1, arg2, arg3);
	}
	public int hashCode() {
		return breakFaithId.hashCode();
	}
	public int indexOf(int arg0, int arg1) {
		return breakFaithId.indexOf(arg0, arg1);
	}
	public int indexOf(int arg0) {
		return breakFaithId.indexOf(arg0);
	}
	public int indexOf(String arg0, int arg1) {
		return breakFaithId.indexOf(arg0, arg1);
	}
	public int indexOf(String arg0) {
		return breakFaithId.indexOf(arg0);
	}
	public String intern() {
		return breakFaithId.intern();
	}
	public boolean isEmpty() {
		return breakFaithId.isEmpty();
	}
	public int lastIndexOf(int arg0, int arg1) {
		return breakFaithId.lastIndexOf(arg0, arg1);
	}
	public int lastIndexOf(int arg0) {
		return breakFaithId.lastIndexOf(arg0);
	}
	public int lastIndexOf(String arg0, int arg1) {
		return breakFaithId.lastIndexOf(arg0, arg1);
	}
	public int lastIndexOf(String arg0) {
		return breakFaithId.lastIndexOf(arg0);
	}
	public int length() {
		return breakFaithId.length();
	}
	public boolean matches(String arg0) {
		return breakFaithId.matches(arg0);
	}
	public int offsetByCodePoints(int arg0, int arg1) {
		return breakFaithId.offsetByCodePoints(arg0, arg1);
	}
	public boolean regionMatches(boolean arg0, int arg1, String arg2, int arg3, int arg4) {
		return breakFaithId.regionMatches(arg0, arg1, arg2, arg3, arg4);
	}
	public boolean regionMatches(int arg0, String arg1, int arg2, int arg3) {
		return breakFaithId.regionMatches(arg0, arg1, arg2, arg3);
	}
	public String replace(char arg0, char arg1) {
		return breakFaithId.replace(arg0, arg1);
	}
	public String replace(CharSequence arg0, CharSequence arg1) {
		return breakFaithId.replace(arg0, arg1);
	}
	public String replaceAll(String arg0, String arg1) {
		return breakFaithId.replaceAll(arg0, arg1);
	}
	public String replaceFirst(String arg0, String arg1) {
		return breakFaithId.replaceFirst(arg0, arg1);
	}
	public String[] split(String arg0, int arg1) {
		return breakFaithId.split(arg0, arg1);
	}
	public String[] split(String arg0) {
		return breakFaithId.split(arg0);
	}
	public boolean startsWith(String arg0, int arg1) {
		return breakFaithId.startsWith(arg0, arg1);
	}
	public boolean startsWith(String arg0) {
		return breakFaithId.startsWith(arg0);
	}
	public CharSequence subSequence(int arg0, int arg1) {
		return breakFaithId.subSequence(arg0, arg1);
	}
	public String substring(int arg0, int arg1) {
		return breakFaithId.substring(arg0, arg1);
	}
	public String substring(int arg0) {
		return breakFaithId.substring(arg0);
	}
	public char[] toCharArray() {
		return breakFaithId.toCharArray();
	}
	public String toLowerCase() {
		return breakFaithId.toLowerCase();
	}
	public String toLowerCase(Locale arg0) {
		return breakFaithId.toLowerCase(arg0);
	}
	public String toString() {
		return breakFaithId.toString();
	}
	public String toUpperCase() {
		return breakFaithId.toUpperCase();
	}
	public String toUpperCase(Locale arg0) {
		return breakFaithId.toUpperCase(arg0);
	}
	public String trim() {
		return breakFaithId.trim();
	}
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
	/**
	 * 年龄
	 */
	private String age;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 立案时间
	 */
	private String openTime;
	
	private String openTimeNew;//原 主要是有yyyy-mm-dd	
	public String getOpenTimeNew() {
		return openTimeNew;
	}
	public void setOpenTimeNew(String openTimeNew) {
		this.openTimeNew = openTimeNew;
	}
	/**
	 * 采集时间
	 */
	private String collectDate;
	/**
	 * 发布时间
	 */
	private String publishDate;
	/**
	 * 关注次数
	 */
	private String follow;
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
	private Boolean flag;
	
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
//	/**
//	 * 修改日期
//	 */
//	//private String modifyData;
//	/**
//	 * 职位
//	 */
//	//private String position;
	

	
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
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
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
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
