package cn.com.szgao.dto;

public class ABC {
	Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CodeVO getCv() {
		return cv;
	}
	public void setCv(CodeVO cv) {
		this.cv = cv;
	}
	String name;
	CodeVO cv;
}
