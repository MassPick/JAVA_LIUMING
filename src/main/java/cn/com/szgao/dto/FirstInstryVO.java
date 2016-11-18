package cn.com.szgao.dto;

/**
 * 经营范围第一个标点的行业名称 及数量
 * @author liuming
 * @Date 2016年7月21日 下午7:04:01
 */
public class FirstInstryVO {
	private Long count ;
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String name;
}
