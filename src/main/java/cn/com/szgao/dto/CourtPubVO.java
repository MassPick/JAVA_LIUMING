package cn.com.szgao.dto;
/**
 * 对应Bucket=courtPub里面的字段
 * @author xiongchangyi
 * 2015-8-12
 */

//public class CourtPubVO extends BaseVO{
public class CourtPubVO {
	
	private String courtPubId;

	public String getCourtPubId() {
		return courtPubId;
	}
	public void setCourtPubId(String courtPubId) {
		this.courtPubId = courtPubId;
	}
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
	 * 公告时间
	 */
	private String pubDate;
	/**
	 * 公告内容链接(HTML)
	 */
	private String detailLink;
	/**
	 * 公告内容链接（PDF）
	 */
	private String pdfLink;
	/**
	 * 采集时间
	 */
	private String collectTime;
	/**
	 * 发文机构
	 */
	private String source;
	
	/**
	 * 公告主体
	 */
	private String subject;
	
	
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
	public String getDetailLink() {
		return detailLink;
	}
	public void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}
	public String getPdfLink() {
		return pdfLink;
	}
	public void setPdfLink(String pdfLink) {
		this.pdfLink = pdfLink;
	}
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSubject() {
		return subject;
	}	
	public void setSubject(String subject) {
		this.subject = subject;
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
	
	
}
