package cn.com.szgao.dto;
/**
 * 备注类
 * @author xiongchangyi
 */
public class RemarkVO {
	/**
	 * EnterpriseVO中的legalRep表示值，
	 * RemarkVO中的legalRep表示前台标签名称,如法定代表人、负责人、投资人、合伙人
	 */
	private String legalRep;
	/**
	 * 营业期限自表示前台标签名称
	 */
	private String startTime;
	/**
	 * 营业期限至表示前台标签名称
	 */
	private String endTime;
	/**
	 * EnterpriseVO中的regCapital表示值
	 * RemarkVO中的regCapital表示前台标签名称
	 */
	private String regCapital;
	/**
	 * HolderVO、HolderDetailVO中的holder表示的是值
	 * RemarkVO中的holder表示前台标签名称，如：股东、姓名、合伙人、出资人
	 * 股东名称、发起人、投资人名称等
	 */
	private String holder;
	/**
	 * HolderVO中的type表示的是值
	 * RemarkVO中的type表示前台标签名称，如：发起人类型、股东类型、合伙人类型
	 */
	private String type;	
	/**
	 * bucketName表示前台标签名称,如：基本信息、股东信息、投资人信息、合伙人信息
	 */
	private String bucketName;
	/**
	 * 营业执照注册号统一社会信用代码
	 */
	private String regNum;
	public String getLegalRep() {
		return legalRep;
	}
	public void setLegalRep(String legalRep) {
		this.legalRep = legalRep;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getRegCapital() {
		return regCapital;
	}
	public void setRegCapital(String regCapital) {
		this.regCapital = regCapital;
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
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getRegNum() {
		return regNum;
	}
	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}
}
