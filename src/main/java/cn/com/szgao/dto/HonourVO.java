package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 建设通的 荣誉VO  
 * @author liuming
 *
 */
@SuppressWarnings("serial")
public class HonourVO implements Serializable{

	
	/**
	 * ID UUID
	 */
	private String honourhId;
	public String getHonourhId() {
		return honourhId;
	}
	public void setHonourhId(String honourhId) {
		this.honourhId = honourhId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public List<HonourDetailVO> getHonourDetail() {
		return honourDetail;
	}
	public void setHonourDetail(List<HonourDetailVO> honourDetail) {
		this.honourDetail = honourDetail;
	}
	/**
	 * 公司名
	 */
	private String companyName;
	/*
	 * 荣誉内容
	 */
	private  List<HonourDetailVO> honourDetail;
	

}
