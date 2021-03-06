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
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.ElasticSearchConnUtils220;
import cn.com.szgao.util.ObjectUtils;
import cn.com.szgao.util.PrCiCouText;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.AdministrationUtils;
import cn.com.szgao.wash.data.DataUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 深圳
 * 
 * @author xiongchangyi
 *
 */
@SuppressWarnings("unused")
public class FileIntoDataBaseToES2 {
	public FileIntoDataBaseToES2() {
	}

	public FileIntoDataBaseToES2(Logger log) {
		this.log = log;
	}

	/**
	 * json与对象互转
	 */
	Gson gs = new Gson();
	/**
	 * 工具对象
	 */
	DataUtils utils = new DataUtils();
	/**
	 * 工具类，通过住所、登记机关获得行政区
	 */
	AdministrationUtils u = new AdministrationUtils();

	// 日志对象
	private static Logger log;
	// 根据字符串生成UUID对象
	NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
 
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
		HashMap<String, String> hashMapMainManager = new HashMap<String, String>();
		HashMap<String, String> hashMapHolder = new HashMap<String, String>();
		HashMap<String, String> hashMapHolderDetail = new HashMap<String, String>();
		HashMap<String, String> hashMapChange = new HashMap<String, String>();

		int numm = 0;
		while ((tempT = reader.readLine()) != null) {

			System.out
					.println("数量:" + (numm++) + "---线程名" + Thread.currentThread().getName() + "文件： " + file.getPath());

			/**
			 * 股东信息
			 */
			List<HolderVO> holder = new ArrayList<HolderVO>();
			/**
			 * 股东详情
			 */
			List<HolderDetailVO> holderDetail = new ArrayList<HolderDetailVO>();
			/**
			 * 变更信息
			 */
			List<ChangeVO> change = new ArrayList<ChangeVO>();
			/**
			 * 主要成员信息
			 */
			List<MainManagerVO> mainManager = new ArrayList<MainManagerVO>();

			/**
			 * 工商 年报
			 */
			List<ReportVO> report = new ArrayList<ReportVO>();
			/**
			 * 工商 经营异常信息
			 */
			List<AbnormalVO> abnormal = null;
			/**
			 * 工商 分支机构信息
			 */
			List<BranchVO> branch = null;
			/**
			 * 工商 严重违法信息
			 */
			List<IllegalVO> illegal = null;
			/**
			 * 动产抵押
			 */
			List<MortgageVO> mortgage = null;
			/**
			 * 股权出质登记信息
			 */
			List<PledgeVO> pledge = null;
			/**
			 * 处罚信息
			 */
			List<PunishmentVO> punishment = null;
			/**
			 * 抽查检查信息
			 */
			List<SpotCheckVO> spotCheck = null;

			EnterpriseVO enterVO = new EnterpriseVO();
			EnterpriseVO enterVOT = new EnterpriseVO();

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
			// System.out.println();
			// DocEnterpriseVO doc = gs.fromJson(doString,
			// DocEnterpriseVO.class);
			EnterpriseVO vo = null;
			// if (doc.getDoc() != null && doc.getDoc().size() > 0) {
			// vo = doc.getDoc().get(0);

			try {
				vo = gs.fromJson(doString, EnterpriseVO.class);
				companyId=vo.getCompanyId();
			} catch (Exception e) {
				continue;
			}

			// 入CB库，并检查
//			if (isExistNew(vo)) {
//				System.out.println("------------- CB存在");
//				continue;
//			} 
			int igg=0;
			if (null != vo) {
				// 高管
				mainManager = vo.getMainManager();
				if (null != mainManager && mainManager.size() > 0) {
					for (MainManagerVO mainManagerVO : mainManager) {

						key = mainManagerVO.getManagerName() == null ? "" : mainManagerVO.getManagerName() + companyId;
						key = nbg.generate(key + (igg++)).toString();
						mainManagerVO.setMainManagerId(key);
						hashMapMainManager.put(key, StringUtils.GSON.toJson(mainManagerVO));
					}
					// ElasticSearchConnUtils220.doListEsData100("company",
					// "etp_mhold_e", hashMapMainManager);
					igg = 0;
				}

				int ig=0;
				// 股东
				holder = vo.getHolder();
				if (null != holder && holder.size() > 0) {
					for (HolderVO holderVO : holder) {

						key = nbg.generate(holderVO.getHolder() == null ? ""
								: holderVO.getHolder() + holderVO.getType() == null ? ""
										: holderVO.getType() + companyId + (ig++))
								.toString();
//						key = nbg.generate(key + (i++)).toString();
						holderVO.setHolderId(key);
						holderVO.setHolderDetail(null);
						hashMapHolder.put(key, StringUtils.GSON.toJson(holderVO));

						holderDetail = holderVO.getHolderDetail();
						if (null != holderDetail && holderDetail.size() > 0) {
							for (HolderDetailVO holderDetailVO : holderDetail) {

								hashMapHolderDetail.put(key, StringUtils.GSON.toJson(holderDetail));
							}
						}
					}
					// ElasticSearchConnUtils220.doListEsData100("company",
					// "etp_holder_e", hashMapHolder);
					// ElasticSearchConnUtils220.doListEsData100("company",
					// "etp_holder_det_e", hashMapHolderDetail);
					ig = 0;
				}
				// 变更
				change = vo.getChange();
				int i1 = 0;
				if (null != change && change.size() > 0) {
					for (ChangeVO changeVO : change) {
						key = changeVO.getChangeEvent() == null ? "" : changeVO.getChangeEvent() + companyId;
						key = nbg.generate(key + (i1++)).toString();
						hashMapChange.put(key, StringUtils.GSON.toJson(changeVO));
					}

					// ElasticSearchConnUtils220.doListEsData100("company",
					// "etp_event_item_e", hashMapChange);
					i1 = 0;
				}

			} else {
				continue;
			}

			enterVO = getbaseEnterpriseVO(vo);
			// System.out.println(enterVO.getCompanyId());

			// i++;
			int numn=1000;
			hashMapEtp.put(enterVO.getCompanyId(), StringUtils.GSON.toJson(enterVO));
			if (hashMapEtp.size() > numn) {
				// System.out.println("szie:" + hashMap.size());
				ElasticSearchConnUtils220.doListEsData100("company", "etp_t", hashMapEtp);
				hashMapEtp = null;
				hashMapEtp = new HashMap<String, String>();
				log.info("企业批次: " + (i / numn) + "   ");
			}
			if (hashMapChange.size() > numn) {
				ElasticSearchConnUtils220.doListEsData100("company", "etp_event_item_e", hashMapChange);
				hashMapChange = null;
				hashMapChange = new HashMap<String, String>();
				log.info("变更批次: " + (i / numn) + "   ");
			}
			if (hashMapHolder.size() > numn) {
				ElasticSearchConnUtils220.doListEsData100("company", "etp_holder_e", hashMapHolder);
				hashMapHolder = null;
				hashMapHolder = new HashMap<String, String>();
				log.info("股东批次: " + (i / numn) + "   ");
			}
			if (hashMapHolderDetail.size() > numn) {
				ElasticSearchConnUtils220.doListEsData100("company", "etp_holder_det_e", hashMapHolderDetail);
				hashMapHolderDetail = null;
				hashMapHolderDetail = new HashMap<String, String>();
				log.info("股东详情批次: " + (i / numn) + "   ");
			}
			if (hashMapMainManager.size() > numn) {
				ElasticSearchConnUtils220.doListEsData100("company", "etp_mhold_e", hashMapMainManager);
				hashMapMainManager = null;
				hashMapMainManager = new HashMap<String, String>();
				log.info("高管详情批次: " + (i / numn) + "   ");
			}

		}

		if (hashMapEtp.size() > 0) {
			ElasticSearchConnUtils220.doListEsData("company", "etp_t", hashMapEtp);
			hashMapEtp = null;
			hashMapEtp = new HashMap<String, String>();
			log.info("企业批次完: " + "---线程名" + Thread.currentThread().getName());
		}
		if (hashMapChange.size() > 0) {
			ElasticSearchConnUtils220.doListEsData100("company", "etp_event_item_e", hashMapChange);
			hashMapChange = null;
			hashMapChange = new HashMap<String, String>();
			log.info("变更批次完: " + "---线程名" + Thread.currentThread().getName());
		}
		if (hashMapHolder.size() > 0) {
			ElasticSearchConnUtils220.doListEsData100("company", "etp_holder_e", hashMapHolder);
			hashMapHolder = null;
			hashMapHolder = new HashMap<String, String>();
			log.info("股东批次完: " + "---线程名" + Thread.currentThread().getName());
		}
		if (hashMapHolderDetail.size() > 0) {
			ElasticSearchConnUtils220.doListEsData100("company", "etp_holder_det_e", hashMapHolderDetail);
			hashMapHolderDetail = null;
			hashMapHolderDetail = new HashMap<String, String>();
			log.info("股东详情批次完: " + "---线程名" + Thread.currentThread().getName());
		}
		if (hashMapMainManager.size() > 0) {
			ElasticSearchConnUtils220.doListEsData100("company", "etp_mhold_e", hashMapMainManager);
			hashMapMainManager = null;
			hashMapMainManager = new HashMap<String, String>();
			log.info("高管详情批次完: " + "---线程名" + Thread.currentThread().getName());
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
