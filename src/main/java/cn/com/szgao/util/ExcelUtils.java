package cn.com.szgao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * excel工具类,都是从 0行0列开始
 * @author liuming
 * @Date 2016年10月31日 下午5:37:34
 */
public class ExcelUtils {

	/**
	 * 根据excel路径返回值 只有一列
	 * 
	 * @return
	 * @return List<String>
	 * @author liuming
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @date 2015年12月7日 下午5:24:59
	 */
	@SuppressWarnings("resource")
	public static List<String> getListFromExcel(File file) throws InvalidFormatException, IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook xssfWorkbook = null;
		// try {
		// xssfWorkbook = new XSSFWorkbook(is);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// if (!is.markSupported()) {
		// is = new PushbackInputStream(is, 8);
		// }
		// if (POIFSFileSystem.hasPOIFSHeader(is)) {
		// return new HSSFWorkbook(is);
		// }
		// if (POIXMLDocument.hasOOXMLHeader(is)) {
		xssfWorkbook = new XSSFWorkbook(OPCPackage.open(is));
		// }

		String strNo = null;
		List<String> list = new ArrayList<String>();
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
					if (null != no) {

						if (!StringUtils.isNull(no.toString())) {
							strNo = no.toString();
							// strNo=StringUtils.removeBlank(strNo);
							list.add(strNo);
						}
					}
				}
			}
		}
		strNo = null;
		return list;
	}

	/**
	 * 根据excel路径返回值 多列 EXCEL2010
	 * 
	 * @param file
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @return List<String>
	 * @author liuming
	 * @date 2015年12月8日 下午12:06:44
	 */
	public static List<String> getListFromExcelMoreLine(File file) throws InvalidFormatException, IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook xssfWorkbook = null;
		// try {
		// xssfWorkbook = new XSSFWorkbook(is);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// if (!is.markSupported()) {
		// is = new PushbackInputStream(is, 8);
		// }
		// if (POIFSFileSystem.hasPOIFSHeader(is)) {
		// return new HSSFWorkbook(is);
		// }
		// if (POIXMLDocument.hasOOXMLHeader(is)) {
		xssfWorkbook = new XSSFWorkbook(OPCPackage.open(is));
		// }

		String strNo = null;
		List<String> list = new ArrayList<String>();
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
					// Iterator<Cell> cells=xssfRow.cellIterator();
					for (Cell no : xssfRow) {
						if (null != no) {
							if (!StringUtils.isNull(no.toString())) {
								strNo = no.toString();
								// strNo=StringUtils.removeBlank(strNo);
								list.add(strNo);
							}
						}
					}

					// XSSFCell no = xssfRow.getCell(0);
					// if (null != no) {
					//
					// if (!StringUtils.isNull(no.toString())) {
					// strNo = no.toString();
					// // strNo=StringUtils.removeBlank(strNo);
					// list.add(strNo);
					// }
					// }
				}
			}
		}
		strNo = null;
		return list;
	}

	/**
	 * 按列读出数据
	 * 
	 * @param file
	 * @param startSheet
	 *            从0开始 0sheet
	 * @param cellNum
	 *            从0开始 0列
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @return List<String>
	 * @author liuming
	 * @date 2015年12月8日 下午3:29:01
	 */
	public static List<String> getListFromExcelByLine(File file, int startSheet, int cellNum)
			throws InvalidFormatException, IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			System.out.println("文件路径:"+file.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook xssfWorkbook = null;

//		try {
//			xssfWorkbook = new XSSFWorkbook(is);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		 xssfWorkbook = new XSSFWorkbook(OPCPackage.open(is));

		String strNo = null;
		List<String> list = new ArrayList<String>();
		// Read the Sheet
//		for (int numSheet = startSheet; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(startSheet);
			if (xssfSheet == null) {
				System.out.println("sheet为NULL");
//				continue;
				return list;
			}

			// Read the Row
			for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					XSSFCell no = xssfRow.getCell(cellNum);
					if (null != no) {
						if (!StringUtils.isNull(no.toString())) {
							strNo = no.toString();
							// strNo=StringUtils.removeBlank(strNo);
							list.add(strNo);
						}
					}
				}
			}
//		}
			if(null!=is){
				is.close();
			}
		strNo = null;
		return list;
	}
	
	
	/**
	 * 
	 *  返回两列的MAP集合
	 * @param file
	 * @param startSheet
	 * @param startLine  从0开始
	 * @param cellNum1  列key
	 * @param cellNum2 列value
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException   
	 * @return Map<String,Object>  
	 * @author liuming
	 * @date 2016年3月29日  上午10:27:12
	 */
	public static Map<String, Object>  getListFromExcelByLineTwoColumn(File file, int startSheet,int startLine, int cellNum1, int cellNum2)
			throws InvalidFormatException, IOException {
		InputStream is = null;
		Map<String,Object> hash=new HashMap<String ,Object>();
		try {
			is = new FileInputStream(file);
			System.out.println("文件路径:"+file.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook xssfWorkbook = null;

//		try {
//			xssfWorkbook = new XSSFWorkbook(is);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		 xssfWorkbook = new XSSFWorkbook(OPCPackage.open(is));

		String strNo = null;
		List<String> list = new ArrayList<String>();
		// Read the Sheet
//		for (int numSheet = startSheet; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(startSheet);
			if (xssfSheet == null) {
				System.out.println("sheet为NULL");
//				continue;
				return hash;
			}

			// Read the Row
			for (int rowNum = startLine; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					XSSFCell no = xssfRow.getCell(cellNum1);
					XSSFCell no2 = xssfRow.getCell(cellNum2);
					if (null != no) {
						if (!StringUtils.isNull(no.toString())) {
							strNo = no.toString();
							
							// strNo=StringUtils.removeBlank(strNo);
							hash.put(strNo, no2);
//							list.add(strNo);
						}
					}
				}
			}
//		}
			if(null!=is){
				is.close();
			}
		strNo = null;
		return hash;
	}
	
	
	public static List<String> getListFromExcelByLineToTwo(File file, int startSheet, int cellNum1, int cellNum2)
			throws InvalidFormatException, IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			System.out.println("文件路径:"+file.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook xssfWorkbook = null;

//		try {
//			xssfWorkbook = new XSSFWorkbook(is);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		 xssfWorkbook = new XSSFWorkbook(OPCPackage.open(is));

		String strNo = null;
		List<String> list = new ArrayList<String>();
		// Read the Sheet
//		for (int numSheet = startSheet; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(startSheet);
			if (xssfSheet == null) {
				System.out.println("sheet为NULL");
//				continue;
				return list;
			}

			// Read the Row
			for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					XSSFCell no = xssfRow.getCell(cellNum1);
					XSSFCell no2 = xssfRow.getCell(cellNum2);
					if (null != no) {
						if (!StringUtils.isNull(no.toString())) {
							strNo = no.toString();
							// strNo=StringUtils.removeBlank(strNo);
							list.add(strNo);
						}
					}
				}
			}
//		}
			if(null!=is){
				is.close();
			}
		strNo = null;
		return list;
	}
	
	
	/**
	 * 按列读出数据
	 * 
	 * @param file
	 * @param startSheet  sheet名11111111
	 * @param cellNum 从0开始 0列
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @return List<String>
	 * @author liuming
	 * @date 2015年12月8日 下午3:29:01
	 */
	public static List<String> getListFromExcelByLine(File file, String startSheet, int cellNum)
			throws InvalidFormatException, IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			System.out.println("文件路径:"+file.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook xssfWorkbook = null;

		try {
			xssfWorkbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		 xssfWorkbook = new XSSFWorkbook(OPCPackage.open(is));

		String strNo = null;
		List<String> list = new ArrayList<String>();
		// Read the Sheet
//		for (int numSheet = startSheet; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheet(startSheet);
			if (xssfSheet == null) {
				System.out.println("sheet为NULL");
//				continue;
				return list;
			}

			// Read the Row
			for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					XSSFCell no = xssfRow.getCell(cellNum);
					if (null != no) {
						if (!StringUtils.isNull(no.toString())) {
							strNo = no.toString();
							// strNo=StringUtils.removeBlank(strNo);
							list.add(strNo);
						}
					}
				}
			}
//		}
			if(null!=is){
				is.close();
			}
		strNo = null;
		return list;
	}
	public static List<String> getListFromExcelByLine(File file, String startSheet,int rowNum2, int cellNum)
			throws InvalidFormatException, IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			System.out.println("文件路径:"+file.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook xssfWorkbook = null;

		try {
			xssfWorkbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		 xssfWorkbook = new XSSFWorkbook(OPCPackage.open(is));

		String strNo = null;
		List<String> list = new ArrayList<String>();
		// Read the Sheet
//		for (int numSheet = startSheet; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheet(startSheet);
			if (xssfSheet == null) {
				System.out.println("sheet为NULL");
//				continue;
				return list;
			}

			// Read the Row
			for (int rowNum = rowNum2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					XSSFCell no = xssfRow.getCell(cellNum);
					if (null != no) {
						if (!StringUtils.isNull(no.toString())) {
							strNo = no.toString();
							// strNo=StringUtils.removeBlank(strNo);
							list.add(strNo);
						}
					}
				}
			}
//		}
			if(null!=is){
				is.close();
			}
		strNo = null;
		return list;
	}

	/**
	 * 得到指定EXCEL行的数据
	 * 
	 * @param file
	 * @param row
	 * @return
	 * @return List<String>
	 * @author liuming
	 * @date 2015年12月8日 上午10:51:01
	 */
	public static List<String> getListFromExcelByRow(File file, int row) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String strNo = null;
		List<String> list = new ArrayList<String>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row

			XSSFRow xssfRow = xssfSheet.getRow(row);
			if (xssfRow != null) {

				// Iterator<Cell> cels=
				// xssfRow.cellIterator();

				for (Cell cell : xssfRow) {
					if (null != cell) {
						if (!StringUtils.isNull(cell.toString())) {
							strNo = cell.toString();
							// strNo=StringUtils.removeBlank(strNo);
							list.add(strNo);
						}
					}
				}

				// XSSFCell no = xssfRow.getCell(0);
				// if( null!=no){
				//
				// if(!StringUtils.isNull(no.toString())){
				// strNo=no.toString();
				//// strNo=StringUtils.removeBlank(strNo);
				// list.add(strNo);
				// }
				// }
			}
		}
		strNo = null;
		return list;
	}

	public static List<String> readXlsx(String path) throws Exception {

		Workbook wb = null;
		if (path.endsWith(".xlsx")) {// EXCEL2007
			wb = new XSSFWorkbook(new FileInputStream(new File(path)));
		} else if (path.endsWith(".xls")) {// EXCEL97-2003
			wb = new HSSFWorkbook(new FileInputStream(new File(path)));
		} else {
			throw new Exception("");
		}

		if (wb.getNumberOfSheets() > 1) {
			wb.removeSheetAt(1);
		}

		Sheet sheet1 = wb.getSheetAt(0);
		XSSFRow xssfRow1 = (XSSFRow) sheet1.getRow(0);

		List<String> list = new ArrayList<String>();

		XSSFCell no = xssfRow1.getCell(0);
		if (null != no) {
			list.add(no.toString());
		}

		InputStream is = new FileInputStream(path);
		// XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

		// XSSFWorkbook xssfWorkbook= new XSSFWorkbook(OPCPackage.open(is));
		//
		//// Workbook book = XlSImpUtil.create(in);
		//

		// // Read the Sheet
		// for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets();
		// numSheet++) {
		// XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
		// if (xssfSheet == null) {
		// continue;
		// }
		// // Read the Row
		// for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
		// XSSFRow xssfRow = xssfSheet.getRow(rowNum);
		// if (xssfRow != null) {
		//
		// XSSFCell no = xssfRow.getCell(0);
		// XSSFCell name = xssfRow.getCell(1);
		// XSSFCell age = xssfRow.getCell(2);
		// XSSFCell score = xssfRow.getCell(3);
		// list.add(no.toString());
		// }
		// }
		// }
		return list;
	}

	public static void main(String[] args) throws Exception {
//		// File file = new
//		// File("D:\\WorkDoc\\小数据\\2007年各行业百强企业\\2007年各行业百强企业\\已经分了行业\\存在\\金属表面处理及热处理加工.xlsx");
//		File file = new File("D:\\WorkDoc\\小数据\\2007年各行业百强企业\\2007年各行业百强企业\\aa.xlsx");
//
//		// List<String> list=getListFromExcelByRow(file,0);
//		List<String> list = getListFromExcel(file);
//		// List<String> list =
//		// readXlsx("D:\\WorkDoc\\小数据\\2007年各行业百强企业\\2007年各行业百强企业\\已经分了行业\\存在\\金属表面处理及热处理加工.xlsx");
//		// List<String> list =
//		// readXlsx("D:\\WorkDoc\\小数据\\2007年各行业百强企业\\2007年各行业百强企业\\2007年各行业百强企业名单
//		// - 副本.xls");
//
//		for (String str : list) {
//			System.out.println(str);
//		}
	}

}
