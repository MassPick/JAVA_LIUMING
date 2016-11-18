package cn.com.szgao.dto;

import java.util.ArrayList;
import java.util.List;

public class ListCouchbaseVO {
	private Long total_rows;
	
	private List<CouchbaseVO> rows = new ArrayList<CouchbaseVO>();	

	
	public Long getTotal_rows() {
		return total_rows;
	}

	public void setTotal_rows(Long total_rows) {
		this.total_rows = total_rows;
	}

	public List<CouchbaseVO> getRows() {
		return rows;
	}

	public void setRows(List<CouchbaseVO> rows) {
		this.rows = rows;
	}
}