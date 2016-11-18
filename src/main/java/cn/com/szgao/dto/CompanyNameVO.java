package cn.com.szgao.dto;

/**
 * 公司实体类
 * 
 * @author xiongchangyi 2015-3-5
 */
public class CompanyNameVO {
	 public String company;
	 
	 public Integer flag ;
	 
	 public String province;
	 public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String city;
	 public String area;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	
	
}
