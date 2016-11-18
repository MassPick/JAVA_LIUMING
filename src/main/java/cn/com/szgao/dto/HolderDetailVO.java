package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 股东详情
 * @author dell
 *
 */
public class HolderDetailVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String holderDetailId;

	public String getHolderDetailId() {
		return holderDetailId;
	}
	public void setHolderDetailId(String holderDetailId) {
		this.holderDetailId = holderDetailId;
	}
	/**
	 * 股东ID
	 */
	private String holderId;
	/**
	 * 股东名称	别名：股东、发起人、投资人名称
	 */
	private String holder;
	/**
	 * 股东类型、投资人类型
	 */
	private String type;
	/**
	 * 认缴出资额
	 */
	private String subcriCapital;
	/**
	 * 认缴出资方式
	 */
	private String conMethod;
	/**
	 * 认缴出资日期
	 */
	private String considDate;
	/**
	 * 实际出资额
	 */
	private String actualCapital;
	/**
	 * 实际出资方式
	 */
	private String factMethod;
	/**
	 * 实际出资时期
	 */
	private String actualDate;
	/**
	 * 认缴额
	 */
	private String conCapital;
	/**
	 * 实缴额
	 */
	private String factCapital;
	
	
	/**
	 * 详情
	 * @return
	 */
	private List<HolderDetailDtlVO> holderDetailDtl;
	
	
	public List<HolderDetailDtlVO> getHolderDetailDtl() {
		return holderDetailDtl;
	}
	public void setHolderDetailDtl(List<HolderDetailDtlVO> holderDetailDtl) {
		this.holderDetailDtl = holderDetailDtl;
	}
	/**
	 * 备注 
	 */
	private RemarkVO remark;	
	/**
	 * 出资类型：认缴;年度报告里面使用
	 */
	private String investType;
	
	public String getHolderId() {
		return holderId;
	}
	public void setHolderId(String holderId) {
		this.holderId = holderId;
	}
	public String getConCapital() {
		return conCapital;
	}
	public void setConCapital(String conCapital) {
		this.conCapital = conCapital;
	}
	public String getFactCapital() {
		return factCapital;
	}
	public void setFactCapital(String factCapital) {
		this.factCapital = factCapital;
	}
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubcriCapital() {
		return subcriCapital;
	}
	public void setSubcriCapital(String subcriCapital) {
		this.subcriCapital = subcriCapital;
	}
	public String getConMethod() {
		return conMethod;
	}
	public void setConMethod(String conMethod) {
		this.conMethod = conMethod;
	}
	public String getConsidDate() {
		return considDate;
	}
	public void setConsidDate(String considDate) {
		this.considDate = considDate;
	}
	public String getActualCapital() {
		return actualCapital;
	}
	public void setActualCapital(String actualCapital) {
		this.actualCapital = actualCapital;
	}
	public String getFactMethod() {
		return factMethod;
	}
	public void setFactMethod(String factMethod) {
		this.factMethod = factMethod;
	}
	public String getActualDate() {
		return actualDate;
	}
	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getInvestType() {
		return investType;
	}
	public void setInvestType(String investType) {
		this.investType = investType;
	}
}
