package cn.com.szgao.dto;

import java.io.Serializable;

/**
 * 行政处罚信息
 * @author xiongchangyi
 *
 */
public class PunishmentVO implements Serializable{

	
	private String punishmentId;
	public String getPunishmentId() {
		return punishmentId;
	}
	public void setPunishmentId(String punishmentId) {
		this.punishmentId = punishmentId;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 行政处罚决定书文号
	 */
	private String punishNum;
	/**
	 * 违法行为类型
	 */
	private String illegalType;
	/**
	 * 行政处罚内容
	 */
	private String punishContent;
	/**
	 * 作出行政处罚决定机关名称
	 */
	private String punishOffice;
	/**
	 * 作出行政处罚决定日期
	 */
	private String punishDate;
	/**
	 * 公示日期
	 */
	private String publicationDate;
	/**
	 * 详情
	 */
	private PunishmentDetVO detail;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getPunishNum() {
		return punishNum;
	}
	public void setPunishNum(String punishNum) {
		this.punishNum = punishNum;
	}
	public String getIllegalType() {
		return illegalType;
	}
	public void setIllegalType(String illegalType) {
		this.illegalType = illegalType;
	}
	public String getPunishContent() {
		return punishContent;
	}
	public void setPunishContent(String punishContent) {
		this.punishContent = punishContent;
	}
	public String getPunishOffice() {
		return punishOffice;
	}
	public void setPunishOffice(String punishOffice) {
		this.punishOffice = punishOffice;
	}
	public String getPunishDate() {
		return punishDate;
	}
	public void setPunishDate(String punishDate) {
		this.punishDate = punishDate;
	}
	public String getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}
	public PunishmentDetVO getDetail() {
		return detail;
	}
	public void setDetail(PunishmentDetVO detail) {
		this.detail = detail;
	}
}
