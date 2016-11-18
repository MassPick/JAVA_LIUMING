package cn.com.szgao.dto;
/**
 * 行政处罚信息详情
 * @author xiongchangyi
 *
 */
public class PunishmentDetVO {
	/**
	 * 行政处罚决定书文号
	 */
	private String punishNum;
	/**
	 * 公司名称
	 */
	private String company;
	/**
	 * 注册号
	 */
	private String regNum;
	/**
	 * 法定代表人（负责人）姓名
	 */
	private String legalRep;
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
	 * 行政处罚决定书
	 */
	private String decision;
	
	public String getPunishNum() {
		return punishNum;
	}
	public void setPunishNum(String punishNum) {
		this.punishNum = punishNum;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRegNum() {
		return regNum;
	}
	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}
	public String getLegalRep() {
		return legalRep;
	}
	public void setLegalRep(String legalRep) {
		this.legalRep = legalRep;
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
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
}
