package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;
/**
 * 股权出质登记信息
 * @author dell
 *
 */
public class PledgeVO implements Serializable{
	
	
	private String pledgeId;

	public String getPledgeId() {
		return pledgeId;
	}
	public void setPledgeId(String pledgeId) {
		this.pledgeId = pledgeId;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 登记编号
	 */
	private String recordNum;
	/**
	 * 出质人
	 */
	private String pledgor;
	/**
	 * 证照/证件号码
	 */
	private String pledgorNum;
	/**
	 * 出质股权数额
	 */
	private String pledgeAmount;
	/**
	 * 质权人
	 */
	private String pledgee;
	/**
	 * 证照/证件号码
	 */
	private String pledgeeNum;
	/**
	 * 股权出质设立登记日期
	 */
	private String recordDate;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 变化情况
	 */
	private List<PledgeDetVO> pledgeDetList;
	
	public String getRecordNum() {
		return recordNum;
	}
	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}
	public String getPledgor() {
		return pledgor;
	}
	public void setPledgor(String pledgor) {
		this.pledgor = pledgor;
	}
	public String getPledgorNum() {
		return pledgorNum;
	}
	public void setPledgorNum(String pledgorNum) {
		this.pledgorNum = pledgorNum;
	}
	public String getPledgeAmount() {
		return pledgeAmount;
	}
	public void setPledgeAmount(String pledgeAmount) {
		this.pledgeAmount = pledgeAmount;
	}
	public String getPledgee() {
		return pledgee;
	}
	public void setPledgee(String pledgee) {
		this.pledgee = pledgee;
	}
	public String getPledgeeNum() {
		return pledgeeNum;
	}
	public void setPledgeeNum(String pledgeeNum) {
		this.pledgeeNum = pledgeeNum;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<PledgeDetVO> getPledgeDetList() {
		return pledgeDetList;
	}
	public void setPledgeDetList(List<PledgeDetVO> pledgeDetList) {
		this.pledgeDetList = pledgeDetList;
	}
}
