package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cn.com.szgao.util.StringUtils;



/**
 * 基本信息
 * @author dell
 *
 */
public class EnterpriseVO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EnterpriseVO(){}
	public EnterpriseVO(Integer level,Integer status){
		this.level=level;
		this.status=status;
	}
	
	/**
	 * 区分企业名称    自然人和企业  如果名称>3 则是企业     1    否则是自然人2    没有名称或空的  0
	 */
	private Integer companyType;
	

	

	public Integer getCompanyType() {
		return companyType;
	}
	public void setCompanyType(Integer companyType) {
		this.companyType = companyType;
	}

	/**
	 * 行业ID
	 */
	private String industryId;

	/**
	 * 行业
	 */
	private String industry;
	
	

	/**
	 * 批号
	 */
	private String batchNum;
	
	/**
	 * 单位
	 */
	private String unit;
	
	/**
	 * 索引
	 */
	private Integer level;
	/**
	 * 临时变量
	 */
	private String holders;
	/**
	 * 临时变量
	 */
	private String hcompanyId;	
	
	/**
	 * url
	 */
	private String url;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * uuid
	 */
	private String uuid;
	/**
	 * 主键
	 */
	private String companyId;
	/**
	 * 注册号
	 */
	private String regNum;
	
	/**
	 * 统一社会信用代码
	 */
	private String creditCode;
	
	
	/**
	 * 企业名称
	 */
	private String company;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 法定代表人、负责人、经营者、投资人
	 */
	private String legalRep;
	
	/**
	 * 注册资本
	 */
	private String regCapital;
	
	private String regCapitalO;
	
	private Double regCapitalN;
	 
	public Double getRegCapitalN() {
		return regCapitalN;
	}
	public void setRegCapitalN(Double regCapitalN) {
		this.regCapitalN = regCapitalN;
	}
	public String getRegCapitalO() {
		return regCapitalO;
	}
	public void setRegCapitalO(String regCapitalO) {
		this.regCapitalO = regCapitalO;
	}
	/**
	 * 住所
	 */
	private String location;


	
	
	/**
	 * 成立时间
	 */
	private String regDate;
	/**
	 * 营业期限自
	 */
	private String startTime;
	
	

	/**
	 * 营业期限至
	 */
	private String endTime;
	
	
	
	/**
	 * 核准日期
	 */
	private String approveDate;
	
	
	
	
	/**
	 * 经营范围
	 */
	private String scope;
	/**
	 * 登记机关
	 */
	private String regOffice;
	
	/**
	 * 登记状态
	 */
	private String regState;
	/**
	 * 备注
	 * 作用：映射字段和中文标签
	 */
	private RemarkVO remark;
	/**
	 * 吊销日期
	 */
	private String revokeDate;
	/**
	 * 组成形式 
	 */
	private String composition;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 县
	 */
	private String area;

	/**
	 * 入库时间
	 */
	private String createTime;
	/**
	 * 更新时间
	 */
	private String updateTime;
	/**
	 * 标识该记录是否有效
	 */
    private Integer flag;
	/**
	 * 版本号
	 */
	private Integer version;
	
	
	/**
	 * 注册资本币种
	 */
	private String currency;
	

	/**
	 * 类本身集合
	 */
	private List<EnterpriseVO> enterprise;
	/**
     * 股东信息
     */
	private List<HolderVO> holder;
	/**
     * 股东详情
     */
	private List<HolderDetailVO> holderDetail;
	/**
	 * 变更信息
	 */
	private List<ChangeVO> change;
	/**
	 * 主要成员信息
	 */
	private List<MainManagerVO> mainManager;
	/**
	 * 工商 年报
	 */
	private List<ReportVO> report;
	/**
	 * 工商 经营异常信息
	 */
	private List<AbnormalVO> abnormal;
	/**
	 * 工商 分支机构信息
	 */
	private List<BranchVO> branch;
	/**
	 * 工商 严重违法信息
	 */
	private List<IllegalVO> illegal;
	/**
	 * 动产抵押
	 */
    private List<MortgageVO> mortgage;
    /**
     * 股权出质登记信息
     */
    private List<PledgeVO> pledge;
    /**
     * 行政处罚信息
     */
    private List<PunishmentVO> punishment;
    /**
     * 抽查检查信息
     */
    private List<SpotCheckVO>  spotCheck; 
    
    /**
     * 撤销信息
     */
    private List<EtpRepealVO> etpRepeal;
    
	public boolean add(EtpRepealVO arg0) {
		return etpRepeal.add(arg0);
	}
	public void add(int arg0, EtpRepealVO arg1) {
		etpRepeal.add(arg0, arg1);
	}
	public boolean addAll(Collection<? extends EtpRepealVO> arg0) {
		return etpRepeal.addAll(arg0);
	}
	public boolean addAll(int arg0, Collection<? extends EtpRepealVO> arg1) {
		return etpRepeal.addAll(arg0, arg1);
	}
	public void clear() {
		etpRepeal.clear();
	}
	public boolean contains(Object arg0) {
		return etpRepeal.contains(arg0);
	}
	public boolean containsAll(Collection<?> arg0) {
		return etpRepeal.containsAll(arg0);
	}
	public boolean equals(Object arg0) {
		return etpRepeal.equals(arg0);
	}
	public EtpRepealVO get(int arg0) {
		return etpRepeal.get(arg0);
	}
	public int hashCode() {
		return etpRepeal.hashCode();
	}
	public int indexOf(Object arg0) {
		return etpRepeal.indexOf(arg0);
	}
	public boolean isEmpty() {
		return etpRepeal.isEmpty();
	}
	public Iterator<EtpRepealVO> iterator() {
		return etpRepeal.iterator();
	}
	public int lastIndexOf(Object arg0) {
		return etpRepeal.lastIndexOf(arg0);
	}
	public ListIterator<EtpRepealVO> listIterator() {
		return etpRepeal.listIterator();
	}
	public ListIterator<EtpRepealVO> listIterator(int arg0) {
		return etpRepeal.listIterator(arg0);
	}
	public EtpRepealVO remove(int arg0) {
		return etpRepeal.remove(arg0);
	}
	public boolean remove(Object arg0) {
		return etpRepeal.remove(arg0);
	}
	public boolean removeAll(Collection<?> arg0) {
		return etpRepeal.removeAll(arg0);
	}
	public boolean retainAll(Collection<?> arg0) {
		return etpRepeal.retainAll(arg0);
	}
	public EtpRepealVO set(int arg0, EtpRepealVO arg1) {
		return etpRepeal.set(arg0, arg1);
	}
	public int size() {
		return etpRepeal.size();
	}
	public List<EtpRepealVO> subList(int arg0, int arg1) {
		return etpRepeal.subList(arg0, arg1);
	}
	public Object[] toArray() {
		return etpRepeal.toArray();
	}
	public <T> T[] toArray(T[] arg0) {
		return etpRepeal.toArray(arg0);
	}
	public String getRegNum() {
		return regNum;
	}
	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRegCapital() {
		return regCapital;
	}
	public void setRegCapital(String regCapital) {
		this.regCapital = regCapital;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getRegOffice() {
		return regOffice;
	}
	public void setRegOffice(String regOffice) {
		this.regOffice = regOffice;
	}
	public String getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}
	public String getRegState() {
		return regState;
	}
	public void setRegState(String regState) {
		this.regState = regState;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}		
	public String getLegalRep() {
		return legalRep;
	}
	public void setLegalRep(String legalRep) {
		this.legalRep = legalRep;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getRevokeDate() {
		return revokeDate;
	}
	public void setRevokeDate(String revokeDate) {
		this.revokeDate = revokeDate;
	}
	public String getComposition() {
		return composition;
	}
	public void setComposition(String composition) {
		this.composition = composition;
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
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCreditCode() {
		return creditCode;
	}
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	
	public String getHcompanyId() {
		return hcompanyId;
	}
	public void setHcompanyId(String hcompanyId) {
		this.hcompanyId = hcompanyId;
	}
	public String getHolders() {
		return holders;
	}
	public void setHolders(String holders) {
		this.holders = holders;
	}
	public List<EnterpriseVO> getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(List<EnterpriseVO> enterprise) {
		this.enterprise = enterprise;
	}
	public List<HolderVO> getHolder() {
		return holder;
	}
	public void setHolder(List<HolderVO> holder) {
		this.holder = holder;
	}
	public List<HolderDetailVO> getHolderDetail() {
		return holderDetail;
	}
	public void setHolderDetail(List<HolderDetailVO> holderDetail) {
		this.holderDetail = holderDetail;
	}
	public List<ChangeVO> getChange() {
		return change;
	}
	public void setChange(List<ChangeVO> change) {
		this.change = change;
	}
	public List<MainManagerVO> getMainManager() {
		return mainManager;
	}
	public void setMainManager(List<MainManagerVO> mainManager) {
		this.mainManager = mainManager;
	}
	public List<ReportVO> getReport() {
		return report;
	}
	public void setReport(List<ReportVO> report) {
		this.report = report;
	}
	public List<AbnormalVO> getAbnormal() {
		return abnormal;
	}
	public void setAbnormal(List<AbnormalVO> abnormal) {
		this.abnormal = abnormal;
	}
	public List<BranchVO> getBranch() {
		return branch;
	}
	public void setBranch(List<BranchVO> branch) {
		this.branch = branch;
	}
	public List<IllegalVO> getIllegal() {
		return illegal;
	}
	public void setIllegal(List<IllegalVO> illegal) {
		this.illegal = illegal;
	}
	public List<MortgageVO> getMortgage() {
		return mortgage;
	}
	public void setMortgage(List<MortgageVO> mortgage) {
		this.mortgage = mortgage;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}		
	
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public List<PledgeVO> getPledge() {
		return pledge;
	}
	public void setPledge(List<PledgeVO> pledge) {
		this.pledge = pledge;
	}
	public List<PunishmentVO> getPunishment() {
		return punishment;
	}
	public void setPunishment(List<PunishmentVO> punishment) {
		this.punishment = punishment;
	}
	public List<SpotCheckVO> getSpotCheck() {
		return spotCheck;
	}
	public void setSpotCheck(List<SpotCheckVO> spotCheck) {
		this.spotCheck = spotCheck;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getIndustryId() {
		return industryId;
	}
	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}
	 
	
}
