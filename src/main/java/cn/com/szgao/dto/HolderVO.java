package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;


/**
 * 股东信息
 * @author dell
 *
 */
public class HolderVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private String holderId;
	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 股东类型
	 */
	private String type;
	/**
	 * 股东
	 */
	private String holder;
	/**
	 * 证照/证件类型
	 */
	private String licenseType;
	/**
	 * 证照/证件号码
	 */
	private String licenseNum;
	/**
	 * 详情
	 */
	private String particulars;
	/**
	 * 出资方式
	 * equityParticipation的缩写
	 */
	private String equityPart;
	/**
	 * 备注：别名备注、bucket名称
	 */
	private RemarkVO remark;
	/**
	 * 标识该记录是否有效
	 */
    private Integer flag;
	/**
	 * 股东详情集合
	 */
	private List<HolderDetailVO> holderDetail;
	/**
	 * 自身对象集合
	 */
	private List<HolderVO> detList;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getLicenseNum() {
		return licenseNum;
	}
	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getEquityPart() {
		return equityPart;
	}
	public void setEquityPart(String equityPart) {
		this.equityPart = equityPart;
	}
	public RemarkVO getRemark() {
		return remark;
	}
	public void setRemark(RemarkVO remark) {
		this.remark = remark;
	}
	public String getHolderId() {
		return holderId;
	}
	public void setHolderId(String holderId) {
		this.holderId = holderId;
	}
	public List<HolderDetailVO> getHolderDetail() {
		return holderDetail;
	}
	public void setHolderDetail(List<HolderDetailVO> holderDetail) {
		this.holderDetail = holderDetail;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public List<HolderVO> getDetList() {
		return detList;
	}
	public void setDetList(List<HolderVO> detList) {
		this.detList = detList;
	}

}
