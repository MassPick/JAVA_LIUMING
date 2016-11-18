package cn.com.szgao.dto;

import java.util.ArrayList;
import java.util.List;

public class ListEtpCouchbaseVO {
	private Long total_rows;
	
	private List<EtpCouchbaseVO> rows = new ArrayList<EtpCouchbaseVO>();	

	
	public Long getTotal_rows() {
		return total_rows;
	}

	public void setTotal_rows(Long total_rows) {
		this.total_rows = total_rows;
	}

	public List<EtpCouchbaseVO> getRows() {
		return rows;
	}

	public void setRows(List<EtpCouchbaseVO> rows) {
		this.rows = rows;
	}
}