package cn.com.szgao.dto;


/**
 * 负责人实体类
 * @author xiongchangyi
 *
 */
public class RespPersonVO {
	
	
	/**
	 * 主键
	 */
	private String respPersonId;
	
	/**
	 * 法定代表人、负责人、投资人、经营者、执行事务合伙人、股东
	 */
	private String legalRep;
	/**
	 * 前台标签名称
	 */
	private String labelName;
	
	public String getLegalRep() {
		return legalRep;
	}
	public void setLegalRep(String legalRep) {
		this.legalRep = legalRep;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getRespPersonId() {
		return respPersonId;
	}
	public void setRespPersonId(String respPersonId) {
		this.respPersonId = respPersonId;
	}	
	
}
