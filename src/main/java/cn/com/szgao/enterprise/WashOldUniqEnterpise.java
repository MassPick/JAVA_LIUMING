package cn.com.szgao.enterprise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.AbnormalVO;
import cn.com.szgao.dto.BranchVO;
import cn.com.szgao.dto.ChangeVO;
import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.dto.DocEnterpriseVO;
import cn.com.szgao.dto.DocumentVO;
import cn.com.szgao.dto.EnterpriseCheckVO;
import cn.com.szgao.dto.EnterpriseListVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.HolderDetailDtlVO;
import cn.com.szgao.dto.HolderDetailVO;
import cn.com.szgao.dto.HolderVO;
import cn.com.szgao.dto.IllegalVO;
import cn.com.szgao.dto.MainManagerVO;
import cn.com.szgao.dto.MortgageVO;
import cn.com.szgao.dto.PledgeDetVO;
import cn.com.szgao.dto.PledgeVO;
import cn.com.szgao.dto.PrCiCouVO;
import cn.com.szgao.dto.PunishmentDetVO;
import cn.com.szgao.dto.PunishmentVO;
import cn.com.szgao.dto.RemarkVO;
import cn.com.szgao.dto.ReportAssetVO;
import cn.com.szgao.dto.ReportBaseVO;
import cn.com.szgao.dto.ReportGuaranteeVO;
import cn.com.szgao.dto.ReportInvestVO;
import cn.com.szgao.dto.ReportStockEventItemVO;
import cn.com.szgao.dto.ReportVO;
import cn.com.szgao.dto.ReportWebVO;
import cn.com.szgao.dto.ReporteEventItemVO;
import cn.com.szgao.dto.SpotCheckVO;
import cn.com.szgao.dto.TempHolderVO;
import cn.com.szgao.dto.TempMainManagerVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.ElasticSearchConnUtils220;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.ObjectUtils;
import cn.com.szgao.util.PrCiCouText;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.AdministrationUtils;
import cn.com.szgao.wash.data.DataUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 清洗由companyId产生的旧工商数据，
 * @author liuming
 * @Date 2016年6月23日 上午9:54:43
 */
@SuppressWarnings("unused")
public class WashOldUniqEnterpise {
	public WashOldUniqEnterpise() {
	}

//	public FileIntoDataBaseToUniq(Logger log) {
//		this.log = log;
//	}

	/**
	 * json与对象互转
	 */
	static Gson gs = new Gson();
	/**
	 * 工具对象
	 */
	DataUtils utils = new DataUtils();
	/**
	 * 工具类，通过住所、登记机关获得行政区
	 */
	AdministrationUtils u = new AdministrationUtils();

	// 日志对象
	public static final Logger log = LogManager.getLogger(ExecutorsTextToUniq.class);
	// 根据字符串生成UUID对象
	NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	
	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}
	
	
	public static void main(String[] args){
		
		try {
			show(new File("E:\\刘铭\\data\\rinsecom\\1pi"),0);
			show(new File("E:\\刘铭\\data\\rinsecom\\2pi"),0);
			show(new File("E:\\刘铭\\data\\rinsecom\\3pi"),0);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 股东数组
	 */
	JSONArray holderArray = null;
	/**
	 * 股东详情数组
	 */
	JSONArray holderDetArray = null;
	List<String> beforeList = new ArrayList<String>();
	List<String> afterList = new ArrayList<String>();

	// 报告集合
	Map<String, JSONArray> reportMap = new HashMap<String, JSONArray>();
	// 公司ID集合
	// List<String> companyIdList = new ArrayList<String>();
	// 股权出质登记信息
	JSONArray recordArray = null;
	// 股权出质登记信息详情
	List<JSONArray> pledgeList = new ArrayList<JSONArray>();
	// 行政处罚信息
	JSONArray punishmentArray = null;
	// 行政处罚信息详情
	JSONArray punishDetArray = null;

	/**
	 * 公司ID集合，
	 */
	List<String> companyIdList = new ArrayList<String>();
	static int count = 0;
	// 无基本信息统计数据
	private int basicSum = 0;
	// 无注册号统计数据
	private int regSum = 0;

	
	BufferedWriter fw2 = null;
	
	/**
	 * 循环调用是否是目录
	 * 
	 * @param file
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void show(File file, int startNum) throws IOException, ParseException {
		System.out.println(file.getPath());
		if (file.isFile()) {
			count += 1;
			log.info("--------------数量:" + count + "---线程名" + Thread.currentThread().getName() + "文件： " + file.getPath());
//			log.info("--------------数量:" + count + "文件： " +( file.getPath() ) );
//			log.info("--------------数量:" + count  );
			try {
				
				readFileByLines(file, startNum);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		if (null != files) {
			
			
			
			for (File fi : files) {
				if (fi.isFile()) {
					count += 1;
					log.info(
							"----------数量:" + count + "---线程名" + Thread.currentThread().getName() + "文件： " + file.getPath());
					try {
						readFileByLines(fi, startNum);
					} catch (Exception e) {
						e.printStackTrace();
						log.info(e);
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					show(fi, startNum);
				} else {
					continue;
				}
			}
		}
	}

	HashMap<String, EnterpriseVO> hash = new HashMap<String, EnterpriseVO>();

	@SuppressWarnings("rawtypes")
	private static void readFileByLines(File file, int startNum) throws Exception {
		// 拼接字符对象
		StringBuffer sb = new StringBuffer();
		// FileReader fr = new FileReader(file);
		// int ch = 0;
		// while((ch = fr.read())!=-1 )
		// {
		// sb.append((char)ch);
		// }
		// fr.close();
		// fr = null;

		// BufferedWriter fw = null;

		// String encoding_to = "UTF-8";

		String encoding_from = "UTF-8";//
		// String encoding_from = "GB18030";//乱码
		// String encoding_from = "GBK";//乱码
		// String encoding_from = "GB2312";//

		BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encoding_from);
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String tempT = null;

		int readNum = 0;

		int i = 0;

		// E:\刘铭\data\深圳市\工商-深圳市-0008.txt
//		String ss2 = file.getPath().substring(file.getPath().indexOf("工商数据已排序") + 8, file.getPath().lastIndexOf("."));// 省名
//
//		// E:/工商数据已排序/宁夏.json
//		String folderPath = "E:\\工商数据已去重\\" + ss2;
//		String filePath = folderPath + "\\" + ss2 + "0.json";

		// 创建文件夹
		FileUtils.newFolder(file.getParent().replace("rinsecom", "rinsecom_new"));

		String filePath=file.getPath().replace("rinsecom", "rinsecom_new");
		File fileS = new File(filePath);

		String encoding_from1 = "UTF-8";
		BufferedWriter fw = null;
		try {
			if (!fileS.exists()) {
				try {
					fileS.createNewFile();
				} catch (IOException e) {
				}
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
			} else {
				fileS.delete();
				fileS = new File(filePath);
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1)); // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		int numOK = 0;

		// 入库数
		int numOKIn = 0;
		// 时间旧数据
		int numOKOut = 0;
		// 总行数
		int numm = 0;
		// 异常数
		int numError = 0;
		while ((tempT = reader.readLine()) != null) {
			

			System.out
					.println("数量:" + (numm++) + "---线程名" + Thread.currentThread().getName() + "文件： " + file.getPath());

			readNum++;
			if (readNum < startNum) {
				numError++;
				continue;
			}
			// System.out.println("-->>>>>>> " + (readNum));
			if (tempT == null || tempT == "") {
				numError++;
				continue;
			}

			String doString = tempT.toString();
			JSONObject obj = null;
			// System.out.println("in..."+doString);
			try {
				obj = JSONObject.fromObject(doString);
			} catch (Exception e) {
				numError++;
				log.error(e.getMessage());
				return;
			}
			// System.out.println("in..." + obj);

			String companyId = null;

			String key = null;
			// System.out.println();
			// DocEnterpriseVO doc = gs.fromJson(doString,
			// DocEnterpriseVO.class);
			EnterpriseVO vo = null;
			// if (doc.getDoc() != null && doc.getDoc().size() > 0) {
			// vo = doc.getDoc().get(0);

			try {
				vo = gs.fromJson(doString, EnterpriseVO.class);
			} catch (Exception e) {
				continue;
			}

			companyId = vo.getCompanyId();

			if (null == companyId) {
				System.out.println("companyID为空");
				continue;
			}
			if (null != companyId) {
				String scope = vo.getScope();
				if (!StringUtils.isNull(scope)) {
					scope = scope.replaceAll("[&nbsp;\r\t〓^]", "");
					if (scope.length() > 5) {
						String temp = "";
						// System.out.println(scope);
						// if (scope.substring(scope.length() - 5).contains("^")) {
						// temp = scope.substring(scope.length() - 5).replace("^", "");
						// scope = scope.substring(0, scope.length() - 5) + temp;
						// } else {
						temp = scope.substring(scope.length() - 5).replaceAll("[★#※*+＊■★]", "");
						scope = scope.substring(0, scope.length() - 5) + temp;
						// }
					}
					vo.setScope(scope);
				}
				writerString(fw, new Gson().toJson(vo));
				numOKIn++;
			}
			numOK++;
			System.out.println(numOK);
			
		}
		 

		log.info("总行数: " + numm + "  " + "入库数: " + numOKIn + "  " + "时间旧数据: " + numOKOut + "  " + "异常数: " + numError
				+ "  相加:" + (numOKIn + numOKOut + numError));

	}

//	public EnterpriseVO getbaseEnterpriseVO(EnterpriseVO enterVOT) {
//		EnterpriseVO enterVO = new EnterpriseVO();
//
//		enterVO.setArea(enterVOT.getArea());
//		enterVO.setCity(enterVOT.getCity());
//		enterVO.setCompany(enterVOT.getCompany());
//		enterVO.setCompanyId(enterVOT.getCompanyId());
//		enterVO.setComposition(enterVOT.getComposition());
//
//		enterVO.setCreditCode(enterVOT.getCreditCode());
//
//		enterVO.setFlag(enterVOT.getFlag());
//		enterVO.setLegalRep(enterVOT.getLegalRep());
//		enterVO.setLocation(enterVOT.getLocation());
//		enterVO.setProvince(enterVOT.getProvince());
//		enterVO.setRegCapital(enterVOT.getRegCapital());
//
//		enterVO.setRegNum(enterVOT.getRegNum());
//		enterVO.setRegOffice(enterVOT.getRegOffice());
//		enterVO.setRegState(enterVOT.getRegState());
//		// enterVO.setRemark(enterVOT.getRemark());
//
//		enterVO.setScope(enterVOT.getScope());
//
//		enterVO.setStatus(enterVOT.getStatus());
//		enterVO.setType(enterVOT.getType());
//
//		enterVO.setUrl(enterVOT.getUrl());
//		enterVO.setUuid(enterVOT.getUuid());
//		enterVO.setVersion(enterVOT.getVersion());
//
//		enterVO.setRevokeDate(enterVOT.getRevokeDate());
//		enterVO.setRegDate(enterVOT.getRegDate());
//		enterVO.setApproveDate(enterVOT.getApproveDate());
//		enterVO.setUpdateTime(enterVOT.getUpdateTime());
//		enterVO.setStartTime(enterVOT.getStartTime());
//		enterVO.setEndTime(enterVOT.getEndTime());
//		enterVO.setCreateTime(enterVOT.getCreateTime());
//
//		enterVO.setBatchNum(enterVOT.getBatchNum());
//		enterVO.setRegCapitalN(enterVOT.getRegCapitalN());
//		enterVO.setIndustry(enterVOT.getIndustry());
//		enterVO.setUnit(enterVOT.getUnit());
//
//		return enterVO;
//	}
//
//	static Bucket bucket_etp_check = null;
//
//	static {
//
//		try {
//
//			while (true) {
//				try {
//					bucket_etp_check = CouchbaseConnect.commonBucket("192.168.1.30:8091", "etp_check");
//					break;
//				} catch (Exception e) {
//					log.info("---------------------------> 连BC超时");
//					// log.error(e.getMessage());
//				}
//			}
//
//		} catch (Exception e) {
//		}
//	}
//
//	/**
//	 * 与CB 相比，判断是不是最近时间数据
//	 * 
//	 * @param enterVO
//	 * @return
//	 */
//	public static boolean isExistNew(EnterpriseVO enterVO) {
//		boolean flag = false;
//		JsonDocument queryDoc = null;
//		JsonObject obj = null;
//		JsonDocument doc = null;
//		queryDoc = bucket_etp_check.get(enterVO.getCompanyId(), 60, TimeUnit.MINUTES);
//		if (null == queryDoc) {
//			System.out.println("queryDoc不存在-------------------- " + enterVO.getCompanyId());
//			flag = false;
//
//			EnterpriseCheckVO voCheckVO = new EnterpriseCheckVO();
//			voCheckVO.setCompany(enterVO.getCompany());
//			voCheckVO.setCreateTime(enterVO.getCreateTime());
//
//			obj = JsonObject.fromJson(StringUtils.GSON.toJson(voCheckVO));
//			// 创建JSON文档
//			doc = JsonDocument.create(enterVO.getCompanyId(), obj);
//			while (true) {
//				try {
//					// 更新文档
//					// CouchbaseConnect.commonBucket("192.168.1.30:8091",
//					// "etp_check").upsert(doc);
//					bucket_etp_check.upsert(doc);
//					break;
//				} catch (Exception e) {
//					log.error("---------------------------> 插入BC超时");
//					log.error(e.getMessage());
//				}
//			}
//
//		}
//		if (null != queryDoc) {
//			EnterpriseCheckVO vo = StringUtils.GSON.fromJson(queryDoc.content().toString(), EnterpriseCheckVO.class);
//			vo.getCreateTime();
//			// if(vo.getCreateTime()>=enterVO.getCreateTime()){
//			//
//			// }
//			String ss1 = vo.getCreateTime();
//			String ss2 = enterVO.getCreateTime();
//
//			Date dt1 = DateUtils.strToDateFromZZ(ss1);
//			Date dt2 = DateUtils.strToDateFromZZ(ss2);
//			if (DateUtils.compareDate(dt1, dt2) >= 0) {
//				flag = true;
//			} else {
//				EnterpriseCheckVO voCheckVO = new EnterpriseCheckVO();
//				voCheckVO.setCompany(enterVO.getCompany());
//				voCheckVO.setCreateTime(enterVO.getCreateTime());
//
//				obj = JsonObject.fromJson(StringUtils.GSON.toJson(voCheckVO));
//				// 创建JSON文档
//				doc = JsonDocument.create(enterVO.getCompanyId(), obj);
//				while (true) {
//					try {
//						// 更新文档
//						// CouchbaseConnect.commonBucket("192.168.1.30:8091",
//						// "etp_check").upsert(doc);
//						bucket_etp_check.upsert(doc);
//						break;
//					} catch (Exception e) {
//						log.info("---------------------------> 插入BC超时");
//						log.error(e.getMessage());
//					}
//				}
//
//			}
//
//		}
//
//		return flag;
//
//	}

	public static void writerString(BufferedWriter fw, String str) {
		try {
			fw.append(str + "\n");

			// fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			// Log.error("写文件异常"+e.getMessage());
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

}
