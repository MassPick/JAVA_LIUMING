package cn.com.szgao.dto;

import java.io.Serializable;
/**
 * 省市县对象
 * @author xiongchangyi
 *
 */
public class PrCiCouVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String province;
    private String city;
    private String country;
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
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
    
}
