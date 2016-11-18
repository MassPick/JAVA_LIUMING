package cn.com.szgao.clean.court;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

///////\//import cn.com.szgao.court.esAndcb.CommonConstant;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.court.esAndcb.RecordData;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.wash.data.AdministrationUtils;

/**
 * 
 * 将地方的EXCEL文件名  带法院名的 写到  json
 * @author liuming
 * @Date 2016年6月17日 上午10:17:58
 */
public class ExtractionCourtExcel {
	static AdministrationUtils util;
	
	
	public static String[] LAWCASE_AD={"民事","刑事","行政","知识产权","赔偿","执行","涉外","海事"};
	static int countNum = 0;
	static int countNotHtml = 0;

	/**
	 * 生成文件的个数
	 */
	static int countP = 0;
	static BufferedWriter fwUn = null;
	static BufferedWriter fwUn2 = null;
	/**
	 * 写日志
	 */
	private static Logger logger = LogManager.getLogger(ExtractionCourtExcel.class.getName());
	static Map<String, List<RecordData>> MAPS = new HashMap<String, List<RecordData>>();
	static long ERRORSUM = 0; // 出错数据条数
	static long INPUTSUM = 0; //
	static long REPEATSUM = 0; // 去重后数据条数
	static long SUM = 0;
	static long count = 0;

	/**
	 * 裁判文书 数据写库PostgreSql和couchbase JSON导入extracl_url_t表和court桶
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		long da = System.currentTimeMillis();
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 导入文件地址
 
		
		File file = new File("E:\\地方法院\\EXCEL的省份\\通过法院名在裁判文书上采集的\\之前遗漏未采集的\\新疆维吾尔自治区图木舒克垦区人民法院.xlsx");
		
		
		//---------------- 运行以下程序    ------------------这部分可以得到地方的excel
//		File file = new File("E:\\地方法院\\EXCEL的省份\\带法院名");
		//以下几个要单独运行
//		File file = new File("E:\\地方法院\\EXCEL的省份\\带法院名\\云南省\\昆明市中级人民法院.xlsx");
//		File file = new File("E:\\地方法院\\EXCEL的省份\\带法院名\\吉林省\\白山市中级人民法院.xlsx");
//		File file = new File("E:\\地方法院\\EXCEL的省份\\带法院名\\黑龙江省\\双鸭山市尖山区人民法院.xlsx");
		
		util = new AdministrationUtils();
		util.initData(); // 查询行政区
		

//		Bucket bucket = null;
//		bucket = connectionBucket(bucket);
		try {
			show(file );
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		} finally {
			file = null;
//			bucket.close();
			cluster2 = null;
		}
		logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60) + "分钟");
		
		logger.info("总数： "+countNum);
		record();
	}

	// 连接CB
//	private static Bucket connectionBucket(Bucket bucket) {
//		try {
//			bucket = connectionCouchBaseLocal();// 本地CB
//		} catch (Exception e) {
//			while (true) {
//				try {
//					bucket = connectionCouchBaseLocal();// 本地CB
//					break;
//				} catch (Exception ee) {
//					logger.error(ee);
//				}
//			}
//		}
//
//		return bucket;
//	}

	/**
	 * 递归遍历文件
	 * 
	 * @param file
	 * @throws @throws
	 *             Exception
	 */
	private static void show(File file ) throws Exception {
		if (file.isFile()) {
			if ("xlsx".equals(file.getName().substring(file.getName().lastIndexOf(".") + 1))) {
				long da = System.currentTimeMillis();
				
				try {
					create(file );
				} catch (Exception e) {
					logger.error("异常："+e.getMessage()   + " ----->异常文件："+file.getPath());
				}
				
				
//				logger.info("读取<<" + file.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
				return;
			}else{
				logger.error("不是xls文件： "+file.getPath());
			}

		}
		File[] files = file.listFiles();
		System.out.println("----files---" + files);
		for (File fi : files) {
			if (fi.isFile()) {
				if (fi.getName().contains("download_fail")) {
					continue;
				}
				if ("xlsx".equals(fi.getName().substring(fi.getName().lastIndexOf(".") + 1))) {
					long da = System.currentTimeMillis();
					String name = fi.getParentFile().getPath();
					name = name.substring(name.lastIndexOf("\\") + 1, name.length());
					try {
						create(fi );
					} catch (Exception e) {
						logger.error("异常："+e.getMessage()   + " ----->文件："+fi.getPath());
					}
//					logger.info(
//							"读取" + name + "<<" + fi.getName() + ">>文件耗时" + (System.currentTimeMillis() - da) + "毫秒");
				}else{
					logger.error("不是xls文件： "+fi.getPath());
				}

			} else if (fi.isDirectory()) {
				show(fi );
			} else {
				continue;
			}
		}
	}

	/**
	 * 写数据
	 * 
	 * @param <JSONObject>
	 * @param file
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	private static <ObjectDataVO, JSONObject> void create(File file )
			throws Exception, UnsupportedEncodingException {
		String name = file.getParentFile().getPath();
		name = name.substring(name.lastIndexOf("\\") + 1, name.length());
		BufferedReader reader = null;
		Gson gson = new Gson();
		WholeCourtVO arch = null;
		List<WholeCourtVO> list = new ArrayList<WholeCourtVO>();
		String temp = null;
		int sum = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			
			int flag=0;
			String [] array = new String [3] ;
			String tempName=file.getName().substring(0, file.getName().lastIndexOf("."));
			if(!StringUtils.isNull(tempName)&tempName.indexOf("法院")!=-1){
//				if(!StringUtils.isNull(tempName)&tempName.substring(tempName.length()-1).equals("院")){
				array = util.utils(  tempName );
				flag=1;
			}
			
			
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

//			String strNo = null;
//			List<String> list = new ArrayList<String>();
			// Read the Sheet
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0) ;
			if (xssfSheet == null) {
				System.out.println("sheet为NULL");
//				continue;
			}

			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					
					String url =xssfRow.getCell(1)!=null?xssfRow.getCell(1).toString():null;
					
					if(StringUtils.isNull(url)){
						continue;
					}
					
					if (countNum % 10000 == 0) {
						countP++;
						String folderPathUn = "E:\\法院旧版地方Excle";
						String filePathUn = "E:\\法院旧版地方Excle\\"+"地方的EXCEL文件名带法院名的_" + (countP)+"_"+DateUtils.getDateyyyyMMddhhmmss() + ".json";
						// 创建文件夹
						FileUtils.newFolder(folderPathUn);
						File fileSUn = new File(filePathUn);
						String encoding_from1U = "UTF-8";

						try {
							if (!fileSUn.exists()) {
								try {
									fileSUn.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
									logger.error(e);
								}
								fwUn = new BufferedWriter(
										new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
							} else {
								fileSUn.delete();
								fileSUn = new File(filePathUn);
								fwUn = new BufferedWriter(
										new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
							}
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}
					
					
//					String tittle=xssfRow.getCell(0)!=null?xssfRow.getCell(0).toString():null;
//					System.out.println(tittle);
					
					Object tittle=xssfRow.getCell(0);
					System.out.println(tittle==null);
					if(tittle!=null){
						System.out.println(xssfRow.getCell(0).toString());
						
					}else{
						System.out.println("nulll");
					}
					System.out.println("标题："+(tittle!=null?tittle.toString():null))  ;
					System.out.println("URL："+(xssfRow.getCell(1)!=null?xssfRow.getCell(1).toString():null) );
					System.out.println("分类："+(xssfRow.getCell(2)!=null?xssfRow.getCell(2).toString():null) );
					System.out.println("案号："+(xssfRow.getCell(3)!=null?xssfRow.getCell(3).toString():null) );
					System.out.println("法院："+(xssfRow.getCell(4)!=null?xssfRow.getCell(4).toString():null) );
					System.out.println("发布时间："+(xssfRow.getCell(5)!=null?xssfRow.getCell(5).toString():null) );
					System.out.println("省："+(xssfRow.getCell(6)!=null?xssfRow.getCell(6).toString():null) );
					System.out.println("市："+(xssfRow.getCell(7)!=null?xssfRow.getCell(7).toString():null) );
					System.out.println("县："+(xssfRow.getCell(8)!=null?xssfRow.getCell(8).toString():null) );
					System.out.println("采集时间："+(xssfRow.getCell(9)!=null?xssfRow.getCell(9).toString():null) );
					
					
					arch=new WholeCourtVO();
					arch.setTitle(xssfRow.getCell(0)!=null?xssfRow.getCell(0).toString():null);
					arch.setDetailLink(xssfRow.getCell(1)!=null?xssfRow.getCell(1).toString():null);
					arch.setCatalog(xssfRow.getCell(2)!=null?xssfRow.getCell(2).toString():null);
					arch.setCaseNum(xssfRow.getCell(3)!=null?xssfRow.getCell(3).toString():null);
					arch.setCourtName(xssfRow.getCell(4)!=null?xssfRow.getCell(4).toString():null);
					arch.setPublishDate(xssfRow.getCell(5)!=null?xssfRow.getCell(5).toString():null);
					arch.setProvince(xssfRow.getCell(6)!=null?xssfRow.getCell(6).toString():null);
					arch.setCity(xssfRow.getCell(7)!=null?xssfRow.getCell(7).toString():null);
					arch.setArea(xssfRow.getCell(8)!=null?xssfRow.getCell(8).toString():null);
					arch.setCollectDate(xssfRow.getCell(9)!=null?xssfRow.getCell(9).toString():null);
					//案件
					for (String tempLaw : LAWCASE_AD) {
						if(!StringUtils.isNull(arch.getCatalog())){
							if(arch.getCatalog().indexOf(tempLaw)!=-1){
								arch.setCatalog(tempLaw);
								break;
							}
						}
					}
					arch.setPublishDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(arch.getPublishDate())));
					arch.setCollectDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(arch.getCollectDate())));
					//省市县
					
					if(flag==0){
						if (!StringUtils.isNull(arch.getCourtName())||!StringUtils.isNull(arch.getProvince())||!StringUtils.isNull(arch.getCity())||!StringUtils.isNull(arch.getArea())  ) {
							array = util.utils(  arch.getProvince()+arch.getCity()+ arch.getArea()+ arch.getCourtName());
						}
					}
					
					arch.setProvince(array[0]);
					arch.setCity(array[1]);
					arch.setArea(array[2]);
					
					if(StringUtils.isNull(arch.getTitle())){
						arch.setTitle(null);
					}
					if(StringUtils.isNull(arch.getCatalog())){
						arch.setCatalog(null);
					}
					if(StringUtils.isNull(arch.getCaseNum())){
						arch.setCaseNum(null);
					}
					if(StringUtils.isNull(arch.getCourtName())){
						arch.setCourtName(null);
					}
					if(StringUtils.isNull(arch.getPublishDate())){
						arch.setPublishDate(null);
					}
					if(StringUtils.isNull(arch.getProvince())){
						arch.setProvince(null);
					}
					if(StringUtils.isNull(arch.getCity())){
						arch.setCity(null);
					}
					if(StringUtils.isNull(arch.getArea())){
						arch.setArea(null);
					}
					
					arch.setWholeCourtId(StringUtils.getUUID(arch.getDetailLink().toString()));
					arch.setDataFrom(5);
					arch.setFilePath(file.getPath());
					
					countNum++;
					System.out.println(countNum);
					writerString(fwUn, StringUtils.GSON.toJson(arch));
					
					arch=null;
					System.out.println("----------------------------------------------------------------------------------------");
					 
				}
			}
			
			
			
		 
//			list = removeDuplicate(list); // 去除本次集合中重复数据
//			sum = list.size();
//			
//			boolean result = createJsonToCB(list, bucket);
//			
//			REPEATSUM += list.size();
//			if (!result) {
//				logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生JSON异常!");
//			}
			temp = null;
			list = null;
			list = new ArrayList<WholeCourtVO>();
			statisticalCount(file, sum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常读取" + name + "<<" + file.getPath()  + ">>文件时发生IO异常:" + e.getMessage());
		} finally {
//			logger.info(name + "<<" + file.getName() + ">>记录条数为：" + sum);
			reader.close();
			list = null;
			file = null;
			reader.close();
		}
	}
	
	
	/*private static <ObjectDataVO, JSONObject> void create(File file, Bucket bucket)
			throws Exception, UnsupportedEncodingException {
		String name = file.getParentFile().getPath();
		name = name.substring(name.lastIndexOf("\\") + 1, name.length());
		BufferedReader reader = null;
		Gson gson = new Gson();
		WholeCourtVO arch = null;
		List<WholeCourtVO> list = new ArrayList<WholeCourtVO>();
		String temp = null;
		int sum = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			
			int flag=0;
			String [] array = new String [3] ;
			String tempName=file.getName().substring(0, file.getName().lastIndexOf("."));
			if(!StringUtils.isNull(tempName)&tempName.indexOf("法院")!=-1){
//				if(!StringUtils.isNull(tempName)&tempName.substring(tempName.length()-1).equals("院")){
				array = util.utils(  tempName );
				flag=1;
			}
			
			
			InputStream is = null;
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			Workbook book=null;
			 book = getWorkbook(is);
			// try {
			// book = getWorkbook(is);
			// } catch (Exception e) {
			//// logger.error("---------------------------------------->>
			// "+e.getMessage());
			// }
			
			
//			XSSFWorkbook xssfWorkbook = null;
//			try {
//				xssfWorkbook = new XSSFWorkbook(is);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

			String strNo = null;
//			List<String> list = new ArrayList<String>();
			// Read the Sheet
			org.apache.poi.ss.usermodel.Sheet  xssfSheet = book.getSheetAt(0) ;
			if (xssfSheet == null) {
				System.out.println("sheet为NULL");
//				continue;
			}

			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				Row xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					
					String url =xssfRow.getCell(1).toString();
					
					if(StringUtils.isNull(url)){
						continue;
					}
					
					if (countNum % 10000 == 0) {
						countP++;
						String folderPathUn = "E:\\法院旧版地方Excle";
						String filePathUn = "E:\\法院旧版地方Excle\\" + (countP)+"_"+DateUtils.getDateyyyyMMddhhmmss() + ".json";
						// 创建文件夹
						FileUtils.newFolder(folderPathUn);
						File fileSUn = new File(filePathUn);
						String encoding_from1U = "UTF-8";

						try {
							if (!fileSUn.exists()) {
								try {
									fileSUn.createNewFile();
								} catch (IOException e) {
									e.printStackTrace();
									logger.error(e);
								}
								fwUn = new BufferedWriter(
										new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
							} else {
								fileSUn.delete();
								fileSUn = new File(filePathUn);
								fwUn = new BufferedWriter(
										new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
							}
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}
					
					
					
					
					System.out.println(xssfRow.getCell(0)!=null?xssfRow.getCell(0).toString():null);
					System.out.println(xssfRow.getCell(1)!=null?xssfRow.getCell(1).toString():null);
					System.out.println(xssfRow.getCell(2)!=null?xssfRow.getCell(2).toString():null);
					System.out.println(xssfRow.getCell(3)!=null?xssfRow.getCell(3).toString():null);
					System.out.println(xssfRow.getCell(4)!=null?xssfRow.getCell(4).toString():null);
					System.out.println(xssfRow.getCell(5)!=null?xssfRow.getCell(5).toString():null);
					System.out.println(xssfRow.getCell(6)!=null?xssfRow.getCell(6).toString():null);
					System.out.println(xssfRow.getCell(7)!=null?xssfRow.getCell(7).toString():null);
					System.out.println(xssfRow.getCell(8)!=null?xssfRow.getCell(8).toString():null);
					System.out.println(xssfRow.getCell(9)!=null?xssfRow.getCell(9).toString():null);
					System.out.println("----------------------------------------------------------------------------------------");
					
					arch=new WholeCourtVO();
					arch.setTitle(xssfRow.getCell(0)!=null?xssfRow.getCell(0).toString():null);
					arch.setDetailLink(xssfRow.getCell(1)!=null?xssfRow.getCell(1).toString():null);
					arch.setCatalog(xssfRow.getCell(2)!=null?xssfRow.getCell(2).toString():null);
					arch.setCaseNum(xssfRow.getCell(3)!=null?xssfRow.getCell(3).toString():null);
					arch.setCourtName(xssfRow.getCell(4)!=null?xssfRow.getCell(4).toString():null);
					arch.setPublishDate(xssfRow.getCell(5)!=null?xssfRow.getCell(5).toString():null);
					arch.setProvince(xssfRow.getCell(6)!=null?xssfRow.getCell(6).toString():null);
					arch.setCity(xssfRow.getCell(7)!=null?xssfRow.getCell(7).toString():null);
					arch.setArea(xssfRow.getCell(8)!=null?xssfRow.getCell(8).toString():null);
					arch.setCollectDate(xssfRow.getCell(9)!=null?xssfRow.getCell(9).toString():null);
					//案件
					for (String tempLaw : LAWCASE_AD) {
						if(!StringUtils.isNull(arch.getCatalog())){
							if(arch.getCatalog().indexOf(tempLaw)!=-1){
								arch.setCatalog(tempLaw);
								break;
							}
						}
					}
					arch.setPublishDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(arch.getPublishDate())));
					arch.setCollectDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils .getReplaceAllDate(arch.getCollectDate())));
					//省市县
					
					if(flag==0){
						if (!StringUtils.isNull(arch.getCourtName())||!StringUtils.isNull(arch.getProvince())||!StringUtils.isNull(arch.getCity())||!StringUtils.isNull(arch.getArea())  ) {
							array = util.utils(  arch.getProvince()+arch.getCity()+ arch.getArea()+ arch.getCourtName());
						}
					}
					
					arch.setProvince(array[0]);
					arch.setCity(array[1]);
					arch.setArea(array[2]);
					
					if(StringUtils.isNull(arch.getTitle())){
						arch.setTitle(null);
					}
					if(StringUtils.isNull(arch.getCatalog())){
						arch.setCatalog(null);
					}
					if(StringUtils.isNull(arch.getCaseNum())){
						arch.setCaseNum(null);
					}
					if(StringUtils.isNull(arch.getCourtName())){
						arch.setCourtName(null);
					}
					if(StringUtils.isNull(arch.getPublishDate())){
						arch.setPublishDate(null);
					}
					if(StringUtils.isNull(arch.getProvince())){
						arch.setProvince(null);
					}
					if(StringUtils.isNull(arch.getCity())){
						arch.setCity(null);
					}
					if(StringUtils.isNull(arch.getArea())){
						arch.setArea(null);
					}
					
					arch.setWholeCourtId(StringUtils.getUUID(arch.getDetailLink().toString()));
					arch.setDataFrom(5);
					arch.setFilePath(file.getPath());
					
					countNum++;
					System.out.println(countNum);
					writerString(fwUn, StringUtils.GSON.toJson(arch));
					
					arch=null;
					 
				}
			}
			
			
			
		 
//			list = removeDuplicate(list); // 去除本次集合中重复数据
//			sum = list.size();
//			
//			boolean result = createJsonToCB(list, bucket);
//			
//			REPEATSUM += list.size();
//			if (!result) {
//				logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生JSON异常!");
//			}
			temp = null;
			list = null;
			list = new ArrayList<WholeCourtVO>();
			statisticalCount(file, sum);
		} catch (Exception e) {
			logger.error("读取" + name + "<<" + file.getName() + ">>文件时发生IO异常:" + e.getMessage());
		} finally {
			logger.info(name + "<<" + file.getName() + ">>记录条数为：" + sum);
			reader.close();
			list = null;
			file = null;
			reader.close();
		}
	}*/
	
	
	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + System.getProperty("line.separator"));

			// fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			logger.error("写文件异常" + e.getMessage());
		} finally {
			// if (fw != null) {
			// try {
			// fw.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}
	
	public static Workbook getWorkbook(InputStream inp) throws IOException,InvalidFormatException {
	    if (!inp.markSupported()) {
	        inp = new PushbackInputStream(inp, 8);
	    }
	    if (POIFSFileSystem.hasPOIFSHeader(inp)) {
	        return new HSSFWorkbook(inp);
	    }
	    if (POIXMLDocument.hasOOXMLHeader(inp)) {
	        return new XSSFWorkbook(OPCPackage.open(inp));
	    }
	    
	    throw new IllegalArgumentException("你的excel版本目前poi解析不了");
	}

	
	

	/**
	 * 裁判文书json文件导入CB
	 * 
	 * @param list
	 * @param bucket
	 * @return
	 * @throws Exception
	 */
	public static boolean createJsonToCB(List<WholeCourtVO> list, Bucket bucket) throws Exception {
		Gson gson = new Gson();
		JsonDocument doc = null;
		WholeCourtVO archs = null;
		List<RelativeVO> relativeList = null;
		List<RelativeVO> lists = null;
		RelativeVO relative = null;
		try {
			for (int i = 0; i < list.size(); i++) {
				count++;
				archs = new WholeCourtVO();
				String uuid = list.get(i).getWholeCourtId();
				String jsonss = gson.toJson(list.get(i));
				doc = JsonDocument.create(uuid, JsonObject.fromJson(jsonss));
				bucket.upsert(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			gson = null;
			doc = null;
			archs = null;
			relativeList = null;
			lists = null;
			relative = null;
		}
		return true;
	}


	/**
	 * 统计导入的各地的记录条数
	 */
	public static void statisticalCount(File file, long count) {
		// 取省名
		String provinceName = file.getParentFile().getParent();
		provinceName = provinceName.substring(provinceName.lastIndexOf("\\") + 1, provinceName.length());
		// 取市名
		String city = file.getParentFile().getPath();
		city = city.substring(city.lastIndexOf("\\") + 1, city.length());
		List<RecordData> list = MAPS.get(provinceName);
		if (null == list || list.size() <= 0) {
			list = new ArrayList<RecordData>();
			list.add(new RecordData(provinceName, city, count));
			MAPS.put(provinceName, list);
		} else {
			boolean result = true;
			for (RecordData re : list) {
				if (re.getCityName().equalsIgnoreCase(city)) {
					re.setNumberData(re.getNumberData() + count);
					result = false;
					break;
				}
			}
			if (result) {
				list.add(new RecordData(provinceName, city, count));
				MAPS.put(provinceName, list);
			}
		}
	}

	/**
	 * 记录各地数据
	 */
	public static void record() {
		long sumCount = 0;
		long sum = 0;
		for (Map.Entry<String, List<RecordData>> map : MAPS.entrySet()) {
			logger.info("###:" + map.getKey());
			List<RecordData> list = map.getValue();
			sum = 0;
			for (RecordData recordData : list) {
				logger.info("###:" + recordData.getCityName() + "----记录条数:" + recordData.getNumberData());
				sum += recordData.getNumberData();
			}
			sumCount += sum;
			logger.info(map.getKey() + "省总数据条数据:" + sum);
			logger.info("------------------------------");
		}
		logger.info("总文件数据条数:" + sumCount);
		logger.info("去重后的数据条数据:" + REPEATSUM);
		logger.info("错误数据条数:" + ERRORSUM);
	}

	/**
	 * 根据UUID去重
	 * 
	 * @param list
	 * @return
	 */
	public static List<WholeCourtVO> removeDuplicate(List<WholeCourtVO> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getWholeCourtId().equals(list.get(i).getWholeCourtId())) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	/**
	 * 连接postgreSql库
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb";
		String usr = "postgres";
		String psd = "615601.xcy*";
		Connection conn = null;
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, usr, psd);
		return conn;
	}

	/**
	 * 链接couchbase桶
	 * 
	 * @return
	 */
	public static Bucket connectionCouchBaseLocal() {
		// 连接指定的桶
		return cluster2.openBucket("court_New", 1, TimeUnit.MINUTES);
	}

	private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
}
