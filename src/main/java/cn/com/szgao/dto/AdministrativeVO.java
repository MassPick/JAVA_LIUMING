package cn.com.szgao.dto;
/**
 * 行政区划对象
 * @author xiongchangyi
 *
 */
public class AdministrativeVO {
	/**
	 * 省
	 */
	private String province;
	/**
	 * 地级市
	 */
	private String city;
	/**
	 * 县、县级市、区
	 */
	private String area;
	
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
	
}
