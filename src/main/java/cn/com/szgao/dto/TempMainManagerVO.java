package cn.com.szgao.dto;
/**
 * 主要人员的临时实体对象
 * @author xiongchangyi
 *
 */
public class TempMainManagerVO {
	/**
	 * 公司ID
	 */
	private String companyId;
	/**
	 * 姓名
	 */
	private String managerName1;
	/**
	 * 职务
	 */
	private String position1;
	/**
	 * 姓名4
	 */
	private String managerName2;
	/**
	 * 职务5
	 */
	private String position2;
	/**
	 * 姓名7
	 */
	private String managerName3;
	/**
	 * 职务8
	 */
	private String position3;
	
	/**
	 * 备注对象
	 */
	private RemarkVO remarkVO;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getManagerName1() {
		return managerName1;
	}

	public void setManagerName1(String managerName1) {
		this.managerName1 = managerName1;
	}

	public String getPosition1() {
		return position1;
	}

	public void setPosition1(String position1) {
		this.position1 = position1;
	}

	public String getManagerName2() {
		return managerName2;
	}

	public void setManagerName2(String managerName2) {
		this.managerName2 = managerName2;
	}

	public String getPosition2() {
		return position2;
	}

	public void setPosition2(String position2) {
		this.position2 = position2;
	}

	public String getManagerName3() {
		return managerName3;
	}

	public void setManagerName3(String managerName3) {
		this.managerName3 = managerName3;
	}

	public String getPosition3() {
		return position3;
	}

	public void setPosition3(String position3) {
		this.position3 = position3;
	}

	public RemarkVO getRemarkVO() {
		return remarkVO;
	}

	public void setRemarkVO(RemarkVO remarkVO) {
		this.remarkVO = remarkVO;
	}
}
