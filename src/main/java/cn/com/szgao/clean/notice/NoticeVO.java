package cn.com.szgao.clean.notice;

public class NoticeVO {
	/**
	 * 键
	 */
	private String uuid;
	/**
	 * URL
	 */
	private String detailLink;
	private String url ;
	/**
	 * 县区
	 */
	private String area;
	/**
	 * 
	 */
	private String loadtime;
	/**
	 * 采集日期
	 */
	private String collectTime;
	/**
	 * 省
	 */
	private String province;
	
	private String pdfLink;
	private String isCourtPub;
	/**
	 * 市
	 */
	private String city;
	
	//需要抓取的数据
	/**
	 * 公告主体
	 */
	private String client;
	/**
	 * 公告类型
	 */
	private String pubType;
	/**
	 * 法院
	 */
	private String pubPerson;
	/**
	 * 公告时间
	 */
	private String pubDate;
	/**
	 * 公告内容
	 */
	private String pubContent;
	/**
	 * 原发文机构现定义为：数据来源
	 */
	private String source;
	/**
	 * 公告主体
	 */
	private String subject;
	/**
	 * HTML/PDF路径
	 */
	private String path;
	/**
	 * 是否有效
	 */
	private String flag;
	/**
	 * 发文机构
	 */
	private String courtName;
	/**
	 * 修改日期
	 */
	private String modifyData;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 日期---公告日期
	 * @return
	 */
	private String date;
	public String getLoadtime() {
		return loadtime;
	}
	public void setLoadtime(String loadtime) {
		this.loadtime = loadtime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getModifyData() {
		return modifyData;
	}
	public void setModifyData(String modifyData) {
		this.modifyData = modifyData;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public NoticeVO(){}
	public NoticeVO(String pubContent,String pubDate,String client,String pubType,String pubPerson){
		this.pubDate = pubDate;
		this.client = client;
		this.pubPerson = pubPerson;
		this.pubType = pubType;
		this.pubContent = pubContent;
	}
	public NoticeVO(String uuid,String detailLink,String area, String collectTime,
			String province,String pdfLink,String isCourtPub, String city){
		this.uuid = uuid;
		this.detailLink = detailLink;
		this.province = province;
		this.area = area;
		this.collectTime = collectTime;
		this.pdfLink = pdfLink;
		this.isCourtPub = isCourtPub;
		this.city = city;
		
	}
	public String getPubContent() {
		return pubContent ==null ?"":pubContent;
	}
	public void setPubContent(String pubContent) {
		this.pubContent = pubContent;
	}
	public String getDetailLink() {
		return detailLink ==null ?"":detailLink;
	}
	public void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}
	public String getPubDate() {
		return pubDate ==null ?"":pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getArea() {
		return area ==null ?"":area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCollectTime() {
		return collectTime ==null ?"":collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public String getClient() {
		return client ==null ?"":client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getPubPerson() {
		return pubPerson ==null ?"":pubPerson;
	}
	public void setPubPerson(String pubPerson) {
		this.pubPerson = pubPerson;
	}
	public String getProvince() {
		return province ==null ?"":province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPubType() {
		return pubType ==null ?"":pubType;
	}
	public void setPubType(String pubType) {
		this.pubType = pubType;
	}
	public String getPdfLink() {
		return pdfLink ==null ?"":pdfLink;
	}
	public void setPdfLink(String pdfLink) {
		this.pdfLink = pdfLink;
	}
	public String getIsCourtPub() {
		return isCourtPub ==null ?"":isCourtPub;
	}
	public void setIsCourtPub(String isCourtPub) {
		this.isCourtPub = isCourtPub;
	}
	public String getCity() {
		return city ==null ?"":city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getUuid() {
		return uuid ==null ?"":uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
