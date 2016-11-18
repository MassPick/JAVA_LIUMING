package cn.com.szgao.dto;

import java.io.Serializable;
import java.util.List;

public class FfBuildVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long total_rows;
	private List<RowsBuildVO> rows;
	
	public long getTotal_rows() {
		return total_rows;
	}
	public void setTotal_rows(long total_rows) {
		this.total_rows = total_rows;
	}
	public List<RowsBuildVO> getRows() {
		return rows;
	}
	public void setRows(List<RowsBuildVO> rows) {
		this.rows = rows;
	}
	

}
