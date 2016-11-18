//package cn.com.szgao.wash.data.breakfaith;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.elasticsearch.common.settings.ImmutableSettings;
//import org.elasticsearch.common.settings.Settings;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//
//import cn.com.szgao.dto.BreakFaithVO;
//import cn.com.szgao.dto.CourtVO;
//import cn.com.szgao.util.CouchbaseConnect;
//import cn.com.szgao.util.ElasticSearchConnUtils;
//import cn.com.szgao.util.ElasticSearchUtils;
//import cn.com.szgao.util.StringUtils;
//import cn.com.szgao.wash.data.AdministrationUtils;
//import cn.com.szgao.wash.data.DataUtils;
//
//import com.couchbase.client.java.Bucket;
//import com.couchbase.client.java.document.JsonDocument;
//import com.couchbase.client.java.document.json.JsonObject;
//import com.google.gson.Gson;
//import com.hankcs.hanlp.HanLP;
//import com.hankcs.hanlp.corpus.tag.Nature;
//import com.hankcs.hanlp.seg.common.Term;
//
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.action.search.SearchType;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.NoNodeAvailableException;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.ImmutableSettings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.FilterBuilders;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.WrapperFilterBuilder;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.sort.SortOrder;
//
//@SuppressWarnings("unused")
///**
// * 
// * 
// * 项目名称：MassPick 类名称：TestUpdateBF 类描述： 创建人：liuming 创建时间：2015-9-1 上午10:05:04
// * 修改人：liuming 修改时间：2015-9-1 上午10:05:04 修改备注：
// * 
// * @version
// *
// */
//public class UpdateBreakFaith {
//
//	// 日志对象
//	private static Logger log = LogManager.getLogger(UpdateBreakFaith.class);
//
//	/**
//	 * @param args
//	 */
//	@SuppressWarnings({})
//	public static void main(String[] args) {
//
//		long startTime = System.currentTimeMillis();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
//		Date date = new Date(startTime);
//		log.info("开始时间--------------------" + formatter.format(date));
//		PropertyConfigurator.configure("D:\\workSpace2\\workSpace\\MassPick\\WebContent\\WEB-INF\\log4j.properties");
//		log.debug("-------------------------------------------------- debug!!!!!!!!!!!!!!!!!!!");
//		log.info("-------------------------------------------------- info!!!!!!!!!!!!!!!!!!!");
//
//		// File dicFile = new File(
//		// "D:\\WorkDoc\\ES\\20150702失信数据\\失信数据\\shixin500000.json");
//
//		DataUtils connUtil = new DataUtils();
//		AdministrationUtils util = new AdministrationUtils();
//		// 查询行政区
//		util.initData();
//		// json与对象互转
//		Gson gs = new Gson();
//
//		// Bucket bucket = connUtil.commonBucket("192.168.0.252:8091",
//		// "breakFaith");
//
//		Bucket bucket = null;
//
//		while (true) {
//			try {
//				bucket = CouchbaseConnect.commonBucket("192.168.1.4:8091", "breakFaith");
//				break;
//			} catch (Exception e) {
//				log.info("---------------------------> 连BC超时");
//				log.error(e.getMessage());
//			}
//		}
//
//		BreakFaithVO vo = null;
//		JsonObject obj = null;
//		JsonDocument doc = null;
//		JsonDocument queryDoc = null;
//
//		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch")
//				.put("client.transport.sniff", true).build();
//
//		Client client = new TransportClient(settings)
//				.addTransportAddress(new InetSocketTransportAddress("192.168.1.6", 9306));
//
//		// int countLen = scrollResp.getHits().getHits().length;
//
//		int countLen = 1703985;// 1703985 1 703 634
//		int i = 0;
//		SearchResponse scrollResp = null;
//		try {
//
//			System.out.println("------> " + countLen);
//			StringBuffer sbPersonName = new StringBuffer();// 存人名
//			String sbIdNum = "";// //身份证号
//			String key = "";
//			for (int inum = (64015 + 46088) / 10000; inum <= countLen / 10000; inum++)// 1,703,889
//																						// 跑到了
//																						// 正序
//			// for (int inum
//			// =(18800+17600+11000+8417+15820+2329+2329+4633+1605+1046+107172+12000+3427+218289+95640+353635+30000)/1000;
//			// inum <= countLen / 1000; inum++)//1240000 跑到了 正序 45743
//			// 45743+6438+9000+8000+1000
//			{
//				/*
//				 * scrollResp = client .prepareSearch("breakfaith")
//				 * .setTypes("couchbaseDocument")
//				 * 
//				 * // .setPostFilter(FilterBuilders.andFilter(FilterBuilders.
//				 * inFilter("orgCode", // "－"))) // .setPostFilter( //
//				 * FilterBuilders.andFilter(FilterBuilders.termFilter( // "id",
//				 * // "08ddd2a9-1a80-4f4f-af09-317de19703dd"))) //
//				 * .setPostFilter(FilterBuilders.andFilter(FilterBuilders.
//				 * termFilter("orgCode","752575878"))) //
//				 * .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//				 * .setFrom((inum * 1000)) .setSize(1000) // 起始 from 大小size
//				 * 90985后面 // //fffe3edd-4f61-4b3e-b84d-3b49177797ce
//				 * .addSort("id", SortOrder.ASC).execute() .actionGet();
//				 */
//
//				// scrollResp=ElasticSearchConnUtils.
//				// getSearchResponse("breakfaith",(inum * 1000),1000);
//
//				while (true) {
//					try {
//						log.info("---------------------------->>" + i + "\t" + key);
//						scrollResp = ElasticSearchConnUtils.getSearResponse("breakfaith", (inum * 10000), 10000);
//						break;
//					} catch (Exception e) {
//						log.info("---------------------------> 连ES超时");
//						log.error(e.getMessage());
//					}
//				}
//				for (SearchHit hit : scrollResp.getHits().getHits()) {
//					boolean flag = false;
//					key = hit.getId();
//					i++;
//
//					System.out.println("---------------------------->>" + i + "\t" + key);
//
//					queryDoc = bucket.get(key, 60, TimeUnit.MINUTES);
//					if (null == queryDoc) {
//						log.error("未执行NULL queryDoc-------------------- " + key);
//						continue;
//					}
//
//					vo = gs.fromJson(queryDoc.content().toString(), BreakFaithVO.class);
//
//					if (null == vo) {
//						log.error("未执行NULL-------------------- " + key);
//						continue;
//					}
//					if (null != vo.getCourtName()) {
//
//						/*
//						 * if (!StringUtils.isNull( vo.getPersonName())) {
//						 * String tempPersonName = vo.getPersonName(); //
//						 * 取人名中可能存在的身份证 sbIdNum = StringUtils
//						 * .pickUpManyCardId(tempPersonName); if
//						 * (!StringUtils.isNull(sbIdNum)) {
//						 * vo.setIdNum(StringUtils .subSpeCharBlank(sbIdNum));
//						 * tempPersonName = StringUtils.subSpeCharBlank(
//						 * StringUtils.removeNum3(tempPersonName)) .trim();
//						 * vo.setPersonName(tempPersonName); } } //处理
//						 * 法定代表人或者负责人姓名 out:if
//						 * (!StringUtils.isNull(vo.getRepName())) {
//						 * 
//						 * String tempRepName=vo.getRepName().trim();
//						 * if(tempRepName.length()==1||"0".equals(tempRepName)||
//						 * "无".equals(tempRepName)||"空".equals(tempRepName)||"*"
//						 * .equals(tempRepName)){ vo.setRepName(null); break
//						 * out; } // 取人名中可能存在的身份证 sbIdNum = StringUtils
//						 * .pickUpManyCardId(tempRepName); if
//						 * (!StringUtils.isNull(sbIdNum)) {
//						 * vo.setIdNum(StringUtils .subSpeCharBlank(sbIdNum));
//						 * tempRepName = StringUtils.subSpeCharBlank(
//						 * StringUtils.removeNum3(tempRepName)) .trim();
//						 * vo.setRepName(tempRepName); } } // 判断身份证是否包含“无”等字段 if
//						 * (!StringUtils.isNull(vo.getIdNum())) { String
//						 * tempIdNum = vo.getIdNum().trim(); if
//						 * ("无".equals(tempIdNum) ||
//						 * "000000000000000".equals(tempIdNum) ||
//						 * "000000000000000000".equals(tempIdNum)) {
//						 * vo.setIdNum(null); } else { // 统计转化为大写 X
//						 * vo.setIdNum(tempIdNum.toUpperCase()); } }
//						 * 
//						 * // 性别为空，而身份证不为空时，补齐性别 if ((null == vo.getGender() ||
//						 * "" .equals(vo.getGender())) && null != vo.getIdNum())
//						 * { String gender = StringUtils.getSexFromIdCard(vo
//						 * .getIdNum()); vo.setGender(gender); }
//						 * 
//						 * //处理法院 String tempCourtName=vo.getCourtName();
//						 * tempCourtName=StringUtils.removeBlank(tempCourtName);
//						 * tempCourtName=StringUtils.QtoNull(tempCourtName);
//						 * vo.setCourtName(tempCourtName);
//						 */
//
//						// array[0]省，array[1]地级市，array[2]县级市、县、区
//						String[] array = util.utils(vo.getCourtName());
//						if (null != array) {
//							vo.setProvince(array[0]);
//							vo.setCity(array[1]);
//							vo.setArea(array[2]);
//						}
//						/*
//						 * //处理 执行依据文号 if (null != vo.getExecuteNum()) { String
//						 * tempExNum =StringUtils.toDBC(
//						 * vo.getExecuteNum()).replace("（", "("); tempExNum =
//						 * tempExNum.replace("）", ")"); tempExNum =
//						 * tempExNum.replace("【", "("); tempExNum =
//						 * tempExNum.replace("】", ")"); tempExNum =
//						 * tempExNum.replace("[", "("); tempExNum =
//						 * tempExNum.replace("]", ")"); tempExNum =
//						 * tempExNum.replace("{", "("); tempExNum =
//						 * tempExNum.replace("}", ")"); tempExNum =
//						 * tempExNum.replace("｛", "("); tempExNum =
//						 * tempExNum.replace("｝", ")");
//						 * if("".equals(tempExNum)||"无".equals(tempExNum.trim())
//						 * ||"空".equals(tempExNum.trim())){
//						 * vo.setExecuteNum(null); }else{
//						 * vo.setExecuteNum(tempExNum); } } if (null !=
//						 * vo.getCaseNum()) { String tempCaNum
//						 * =StringUtils.toDBC( vo.getCaseNum() ) .replace("（",
//						 * "("); tempCaNum = tempCaNum.replace("）", ")");
//						 * if("".equals(tempCaNum)){ vo.setCaseNum(null); }else{
//						 * vo.setCaseNum(tempCaNum); } } //处理 生效法律文书确定的义务 if
//						 * (null != vo.getLiability()) { // 替换特殊字符 String
//						 * tempLaNum =StringUtils.removeBlank( vo.getLiability()
//						 * ).replace("（", "("); tempLaNum =
//						 * tempLaNum.replace("）", ")"); tempLaNum =
//						 * tempLaNum.replace("^~^", ""); // tempLaNum =
//						 * tempLaNum.replace("空", ""); // tempLaNum =
//						 * tempLaNum.replace("无", "");
//						 * if("".equals(tempLaNum)||"无".equals(tempLaNum.trim())
//						 * ||"空".equals(tempLaNum.trim())){
//						 * vo.setLiability(null); }else{
//						 * vo.setLiability(tempLaNum); } } //做出执行依据单位
//						 * out:if(null!=vo.getDepartment()){ String
//						 * tempDepartment = StringUtils.removeBlank(
//						 * vo.getDepartment()).replace("（", "("); tempDepartment
//						 * = tempDepartment.replace("）", ")"); tempDepartment =
//						 * tempDepartment.replace("^~^", ""); // tempDepartment
//						 * = tempDepartment.replace("空", ""); // tempDepartment
//						 * = tempDepartment.replace("无", ""); tempDepartment =
//						 * tempDepartment.replace("*", "");
//						 * 
//						 * if(tempDepartment.trim().length()==1||"".equals(
//						 * tempDepartment)||"空".equals(tempDepartment)||"无".
//						 * equals(tempDepartment)){ vo.setDepartment(null);
//						 * break out; }else
//						 * if(StringUtils.isNumeric(tempDepartment.trim())){
//						 * vo.setDepartment(null); break out; }else { //处理单个"("
//						 * if( ( StringUtils.countCharacter(tempDepartment,
//						 * "\\(")==1&&StringUtils.countCharacter(tempDepartment,
//						 * "\\)")==0 ) ){ tempDepartment =
//						 * tempDepartment.replace("(", ""); } //处理单个")" if((
//						 * StringUtils.countCharacter(tempDepartment,
//						 * "\\)")==1&&StringUtils.countCharacter(tempDepartment,
//						 * "\\(")==0 )){ tempDepartment =
//						 * tempDepartment.replace(")", ""); }
//						 * vo.setDepartment(tempDepartment); } }
//						 * 
//						 * 
//						 * if (null != vo.getOrgCode()) { String tempOrgCode =
//						 * vo.getOrgCode(); if (
//						 * StringUtils.isHasHanZi(tempOrgCode)
//						 * ||tempOrgCode.trim().length()<9||"0".equals(
//						 * tempOrgCode)||"-".equals(tempOrgCode) ||
//						 * "00000000".equals(tempOrgCode) ||
//						 * "00000000-0".equals(tempOrgCode) ||
//						 * "12345678".equals(tempOrgCode)||tempOrgCode.contains(
//						 * "*")) { vo.setOrgCode(null); } else { tempOrgCode =
//						 * tempOrgCode.replace(" ", "") .replace("－",
//						 * "-").replace("—", "-"); if (!tempOrgCode
//						 * .matches("[a-zA-Z0-9]{8}-[a-zA-Z0-9]")) { if
//						 * (tempOrgCode.length() == 9) { tempOrgCode =
//						 * tempOrgCode.substring(0, tempOrgCode.length() - 1) +
//						 * "-" + tempOrgCode .substring(tempOrgCode .length() -
//						 * 1); } } vo.setOrgCode(tempOrgCode); } }
//						 */
//						obj = JsonObject.fromJson(gs.toJson(vo));
//						// 创建JSON文档
//						doc = JsonDocument.create(key, obj);
//						while (true) {
//							try {
//								// 更新文档
//								CouchbaseConnect.commonBucket("192.168.1.4:8091", "breakFaith").upsert(doc);
//								break;
//							} catch (Exception e) {
//								log.info("---------------------------> 插入BC超时");
//								log.error(e.getMessage());
//							}
//						}
//					}
//				}
//
//			}
//
//		} catch (Exception e) {
//			log.debug("debug ----------------------------------------------------------------------------- " + e);
//			System.out.println(e.getMessage());
//		} finally {
//			try {
//				log.debug("debug -----------------------------------------------------------------------------测试 ");
//				long endTime = System.currentTimeMillis();
//				Date endDate = new Date(endTime);
//				log.info("结束时间--------------------" + formatter.format(endDate));
//				log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
//				log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
//				log.info("Total : " + i);
//				log.info("Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
//				log.info("Speed : " + (float) (i / ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
//						+ "个/小时");
//				if (null != connUtil) {
//					connUtil = null;
//				}
//				gs = null;
//				util = null;
//			} catch (Exception e2) {
//				log.info(e2);
//			}
//
//		}
//
//	}
//
//}
