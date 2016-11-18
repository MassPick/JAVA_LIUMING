package cn.com.szgao.lm.test_c;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import cn.com.szgao.util.StringUtils;

public class TestReadExcel {

	/**
	 * @param args
	 * @author liuming
	 * @Date 2015-11-4 上午10:39:14
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		

		InputStream is = null;
		try {
			is = new FileInputStream("D:\\WorkDoc\\court.xlsx");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List list = new ArrayList<>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					XSSFCell no = xssfRow.getCell(0);
					if(null!=no){
						System.out.println(no);
						if (!StringUtils.isNull(no.toString())) {
							if (!StringUtils.isNumericDecimal(no.toString())) {
								
							}
						}
					}
					
				}
			}
		}
		
		
	}

}
