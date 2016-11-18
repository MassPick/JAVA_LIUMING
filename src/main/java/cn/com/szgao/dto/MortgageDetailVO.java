package cn.com.szgao.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 动产抵押详情
 * @author xiongchangyi
 * 2015-12-6
 */
public class MortgageDetailVO {
	/**
	 * 动产抵押信息 - 动产抵押详细  - 登记信息 + 被担保债权概况
	 */
	private MortRecordVO record;
	/**
	 * 动产抵押信息 - 动产抵押详细  -抵押权人概况
	 */
	private List<MortgagePledgeeVO> pledgeeList = new ArrayList<MortgagePledgeeVO>();
	/**
	 * 动产抵押详细- 抵押物概况
	 */
	private List<MortgagePawnVO> pawnList= new ArrayList<MortgagePawnVO>();
	/**
	 * 动产抵押详细 - 变更
	 */
	private List<MortgageChangeVO> changeList= new ArrayList<MortgageChangeVO>();
	/**
	 * 动产抵押详细  - 注销
	 */
	private MortgageCancelVO cancel;
	
	public MortRecordVO getRecord() {
		return record;
	}
	public void setRecord(MortRecordVO record) {
		this.record = record;
	}
	public List<MortgagePledgeeVO> getPledgeeList() {
		return pledgeeList;
	}
	public void setPledgeeList(List<MortgagePledgeeVO> pledgeeList) {
		this.pledgeeList = pledgeeList;
	}
	public List<MortgagePawnVO> getPawnList() {
		return pawnList;
	}
	public void setPawnList(List<MortgagePawnVO> pawnList) {
		this.pawnList = pawnList;
	}
	public List<MortgageChangeVO> getChangeList() {
		return changeList;
	}
	public void setChangeList(List<MortgageChangeVO> changeList) {
		this.changeList = changeList;
	}
	public MortgageCancelVO getCancel() {
		return cancel;
	}
	public void setCancel(MortgageCancelVO cancel) {
		this.cancel = cancel;
	}
}
