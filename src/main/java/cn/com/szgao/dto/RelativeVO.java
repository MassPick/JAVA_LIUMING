package cn.com.szgao.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RelativeVO {
	private String courtName;//审理法院
	private String approvalDate;//裁判日期
	private String caseNum;//案号
	private String mark;//Mark
	private String relative_id;//文书ID
	private String suitType;//审判程序/诉讼类型
	private String closedType;//结案方式
	private String type;//Type
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getRelative_id() {
		return relative_id;
	}
	public void setRelative_id(String relative_id) {
		this.relative_id = relative_id;
	}
	public String getSuitType() {
		return suitType;
	}
	public void setSuitType(String suitType) {
		this.suitType = suitType;
	}
	public String getClosedType() {
		return closedType;
	}
	public void setClosedType(String closedType) {
		this.closedType = closedType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
