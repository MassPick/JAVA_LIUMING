package cn.com.szgao.wash.data.executed;

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
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.ElasticSearchConnUtils220;
import cn.com.szgao.util.ObjectUtils;
import cn.com.szgao.util.PrCiCouText;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 深圳
 * 
 * @author xiongchangyi
 *
 */
@SuppressWarnings("unused")
public class FileIntoDataBaseToESExecuted {
	public FileIntoDataBaseToESExecuted() {
	}

	public FileIntoDataBaseToESExecuted(Logger log) {
		this.log = log;
	}

	/**
	 * json与对象互转
	 */
	Gson gs = new Gson();
	 
	// 日志对象
	private static Logger log;
	// 根据字符串生成UUID对象
	NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
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
	int count = 0;
	// 无基本信息统计数据
	private int basicSum = 0;
	// 无注册号统计数据
	private int regSum = 0;

	/**
	 * 循环调用是否是目录
	 * 
	 * @param file
	 * @throws ParseException
	 * @throws IOException
	 */
	public void show(File file, int startNum) throws IOException, ParseException {
		System.out.println(file.getPath());
		if (file.isFile()) {
			count += 1;
			System.out.println("数量:" + count + "---线程名" + Thread.currentThread().getName() + "文件： " + file.getPath());
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
					System.out.println(
							"数量:" + count + "---线程名" + Thread.currentThread().getName() + "文件： " + file.getPath());
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

	@SuppressWarnings("rawtypes")
	private void readFileByLines(File file, int startNum) throws Exception {
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
			// 华 GB18030

			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "UTF-8");
			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "GBK");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encoding_from);
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String tempT = null;

		int readNum = 0;

		int i = 0;
		HashMap<String, String> hashMapEtp = new HashMap<String, String>();

		int numm = 0;
		while ((tempT = reader.readLine()) != null) {

			System.out
					.println("数量:" + (numm++) + "---线程名" + Thread.currentThread().getName() + "文件： " + file.getPath());

		 
 
			readNum++;
			if (readNum < startNum) {
				continue;
			}
			// System.out.println("-->>>>>>> " + (readNum));
			if (tempT == null || tempT == "") {
				continue;
			}

			String doString = tempT.toString();
			JSONObject obj = null;
			// System.out.println("in..."+doString);
			try {
				obj = JSONObject.fromObject(doString);
			} catch (Exception e) {
				log.info(e);
				return;
			}
			// System.out.println("in..." + obj);

			String companyId = null;

			String key = null;
			WholeCourtVO vo=  null;

			try {
				vo = gs.fromJson(doString, WholeCourtVO.class);
			} catch (Exception e) {
				continue;
			}
			
			if(vo.getWholeCourtId()==null){
				key=vo.getKey();
				if(key==null){
					continue;
				}else{
					vo.setWholeCourtId(key);
					vo.setKey(null);
				}
			}else{
				key=vo.getWholeCourtId();
			}
			
			// i++;
			int numn=1000;
			hashMapEtp.put(key, StringUtils.GSON.toJson(vo));
			if (hashMapEtp.size() > numn) {
				// System.out.println("szie:" + hashMap.size());
				ElasticSearchConnUtils220.doListEsData100("wholecourt", "executed", hashMapEtp);
				hashMapEtp = null;
				hashMapEtp = new HashMap<String, String>();
				log.info("企业批次: " + (i / numn) + "   ");
			}
		 

		}

		if (hashMapEtp.size() > 0) {
			ElasticSearchConnUtils220.doListEsData("wholecourt", "executed", hashMapEtp);
			hashMapEtp = null;
			hashMapEtp = new HashMap<String, String>();
			log.info("企业批次完: " + "---线程名" + Thread.currentThread().getName());
		}
	 

	}

	public EnterpriseVO getbaseEnterpriseVO(EnterpriseVO enterVOT) {
		EnterpriseVO enterVO = new EnterpriseVO();

		enterVO.setArea(enterVOT.getArea());
		enterVO.setCity(enterVOT.getCity());
		enterVO.setCompany(enterVOT.getCompany());
		enterVO.setCompanyId(enterVOT.getCompanyId());
		enterVO.setComposition(enterVOT.getComposition());

		enterVO.setCreditCode(enterVOT.getCreditCode());

		enterVO.setFlag(enterVOT.getFlag());
		enterVO.setLegalRep(enterVOT.getLegalRep());
		enterVO.setLocation(enterVOT.getLocation());
		enterVO.setProvince(enterVOT.getProvince());
		enterVO.setRegCapital(enterVOT.getRegCapital());

		enterVO.setRegNum(enterVOT.getRegNum());
		enterVO.setRegOffice(enterVOT.getRegOffice());
		enterVO.setRegState(enterVOT.getRegState());
		// enterVO.setRemark(enterVOT.getRemark());

		enterVO.setScope(enterVOT.getScope());

		enterVO.setStatus(enterVOT.getStatus());
		enterVO.setType(enterVOT.getType());

		enterVO.setUrl(enterVOT.getUrl());
		enterVO.setUuid(enterVOT.getUuid());
		enterVO.setVersion(enterVOT.getVersion());

		enterVO.setRevokeDate(enterVOT.getRevokeDate());
		enterVO.setRegDate(enterVOT.getRegDate());
		enterVO.setApproveDate(enterVOT.getApproveDate());
	
		
		
		enterVO.setEndTime(enterVOT.getEndTime());
		enterVO.setCreateTime(enterVOT.getCreateTime());

		enterVO.setBatchNum(enterVOT.getBatchNum());
		enterVO.setRegCapitalN(enterVOT.getRegCapitalN());
		enterVO.setIndustry(enterVOT.getIndustry());
		enterVO.setUnit(enterVOT.getUnit());
		enterVO.setStartTime(enterVOT.getStartTime());
		enterVO.setUpdateTime(DateUtils.getDateyyyyMMddhhmmssZZ());

		return enterVO;
	}

	static Bucket bucket_etp_check = null;

	static {

		try {

			while (true) {
				try {
					bucket_etp_check = CouchbaseConnect.commonBucket("192.168.1.30:8091", "etp_check");
					break;
				} catch (Exception e) {
					log.info("---------------------------> 连BC超时");
					// log.error(e.getMessage());
				}
			}

		} catch (Exception e) {
		}
	}

	/**
	 * 与CB 相比，判断是不是最近时间数据
	 * 
	 * @param enterVO
	 * @return
	 */
	public static boolean isExistNew(EnterpriseVO enterVO) {
		boolean flag = false;
		JsonDocument queryDoc = null;
		JsonObject obj = null;
		JsonDocument doc = null;
		queryDoc = bucket_etp_check.get(enterVO.getCompanyId(), 60, TimeUnit.MINUTES);
		if (null == queryDoc) {
			System.out.println("queryDoc不存在-------------------- " + enterVO.getCompanyId());
			flag = false;

			EnterpriseCheckVO voCheckVO = new EnterpriseCheckVO();
			voCheckVO.setCompany(enterVO.getCompany());
			voCheckVO.setCreateTime(enterVO.getCreateTime());

			obj = JsonObject.fromJson(StringUtils.GSON.toJson(voCheckVO));
			// 创建JSON文档
			doc = JsonDocument.create(enterVO.getCompanyId(), obj);
			while (true) {
				try {
					// 更新文档
					// CouchbaseConnect.commonBucket("192.168.1.30:8091",
					// "etp_check").upsert(doc);
					bucket_etp_check.upsert(doc);
					break;
				} catch (Exception e) {
					log.error("---------------------------> 插入BC超时");
					log.error(e.getMessage());
				}
			}

		}
		if (null != queryDoc) {
			EnterpriseCheckVO vo = StringUtils.GSON.fromJson(queryDoc.content().toString(), EnterpriseCheckVO.class);
			vo.getCreateTime();
			// if(vo.getCreateTime()>=enterVO.getCreateTime()){
			//
			// }
			String ss1 = vo.getCreateTime();
			String ss2 = enterVO.getCreateTime();

			Date dt1 = DateUtils.strToDateFromZZ(ss1);
			Date dt2 = DateUtils.strToDateFromZZ(ss2);
			if (DateUtils.compareDate(dt1, dt2) >= 0) {
				flag = true;
			} else {
				EnterpriseCheckVO voCheckVO = new EnterpriseCheckVO();
				voCheckVO.setCompany(enterVO.getCompany());
				voCheckVO.setCreateTime(enterVO.getCreateTime());

				obj = JsonObject.fromJson(StringUtils.GSON.toJson(voCheckVO));
				// 创建JSON文档
				doc = JsonDocument.create(enterVO.getCompanyId(), obj);
				while (true) {
					try {
						// 更新文档
//						CouchbaseConnect.commonBucket("192.168.1.30:8091", "etp_check").upsert(doc);
						bucket_etp_check.upsert(doc);
						break;
					} catch (Exception e) {
						log.info("---------------------------> 插入BC超时");
						log.error(e.getMessage());
					}
				}

			}

		}

		return flag;

	}

}
