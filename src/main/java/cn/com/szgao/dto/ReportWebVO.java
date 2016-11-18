package cn.com.szgao.dto;

import java.io.Serializable;


/**
 * 年报网站、网店实体类
 * @author xiongchangyi
 *
 */
public class ReportWebVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private String reportWebId;
	/**
	 * 类型
	 */
	private String siteType;
	/**
	 * 名称
	 */
	private String siteName;
	/**
	 * 网址
	 */
	private String url;
	/**
	 * 备注
	 */
	private RemarkVO remark;
	
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getReportWebId() {
		return reportWebId;
	}
	public void setReportWebId(String reportWebId) {
		this.reportWebId = reportWebId;
	}
	
}
