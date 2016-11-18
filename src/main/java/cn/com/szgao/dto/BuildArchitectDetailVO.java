package cn.com.szgao.dto;


/**
 * 建设通的建筑师 的中招内容
 * @author liuming
 * @Date 2016年8月25日 下午2:09:50
 */
public class BuildArchitectDetailVO {
	
	
	private String buildId;
	
	 public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getArchitectDetailId() {
		return architectDetailId;
	}

	public void setArchitectDetailId(String architectDetailId) {
		this.architectDetailId = architectDetailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArchitect() {
		return architect;
	}

	public void setArchitect(String architect) {
		this.architect = architect;
	}

	public String getIncidentTime() {
		return incidentTime;
	}

	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public Double getBid_cost() {
		return bid_cost;
	}

	public void setBid_cost(Double bid_cost) {
		this.bid_cost = bid_cost;
	}

	private String architectDetailId;
	
	 /**
	  *中标项目
	  */
	 private String name;
	 
	 /**
	  * 建筑师
	  */
	 private String architect;
	 
	 /**
	  * 时间
	  */
	 private String incidentTime;
	 
	 /**
	  * 中标金额
	  */
	 private String cost;
	 
	 /**
	  *  中标金额 ---数字类型
	  */
	 private Double bid_cost;
	 
	private String companyName;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	 
	 
	 
	 
}
