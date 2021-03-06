package cn.com.szgao.dto;

import java.io.Serializable;
/**
 * 股东详情的详情
 * @author dell
 *
 */
public class HolderDetailDtlVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    /**
     * holder
     */
	private String holder;
	/**
	 *认缴出资额  
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
	 * 实缴出资额
	 */
	private String actualCapital;
	/**
	 * 实缴出资方式
	 */
	private String factMethod;
	/**
	 * 实缴出资日期
	 */
	private String actualDate;
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
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
	
	
}
