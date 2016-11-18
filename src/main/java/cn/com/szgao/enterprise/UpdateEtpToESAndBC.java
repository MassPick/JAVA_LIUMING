//import org.apache.log4j.PropertyConfigurator;
//
//import cn.com.szgao.util.ConfigUtils;

//package cn.com.szgao.enterprise;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.elasticsearch.action.index.IndexRequestBuilder;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//
//import com.couchbase.client.java.Bucket;
//import com.couchbase.client.java.document.JsonDocument;
//import com.couchbase.client.java.document.json.JsonObject;
//import com.fasterxml.uuid.Generators;
//import com.fasterxml.uuid.impl.NameBasedGenerator;
//import com.google.gson.Gson;
//
//import cn.com.szgao.dto.AbnormalVO;
//import cn.com.szgao.dto.BranchVO;
//import cn.com.szgao.dto.BreakFaithVO;
//import cn.com.szgao.dto.ChangeVO;
//import cn.com.szgao.dto.CodeVO;
//import cn.com.szgao.dto.CourtPubVO;
//import cn.com.szgao.dto.CourtVO;
//import cn.com.szgao.dto.DocumentVO;
//import cn.com.szgao.dto.EnterpriseVO;
//import cn.com.szgao.dto.ExecutedVO;
//import cn.com.szgao.dto.HolderDetailVO;
//import cn.com.szgao.dto.HolderVO;
//import cn.com.szgao.dto.IllegalVO;
//import cn.com.szgao.dto.MainManagerVO;
//import cn.com.szgao.dto.MortgageVO;
//import cn.com.szgao.dto.PledgeVO;
//import cn.com.szgao.dto.PunishmentVO;
//import cn.com.szgao.dto.ReportVO;
//import cn.com.szgao.dto.SpotCheckVO;
//import cn.com.szgao.dto.WholeCourtVO;
//import cn.com.szgao.util.CouchbaseConnect;
//import cn.com.szgao.util.CouchbaseUtils;
//import cn.com.szgao.util.ElasticSearchConnUtils;
//import cn.com.szgao.util.StringUtils;
//
//public class UpdateEtpToESAndBC {
//
//	private static Logger logger = LogManager.getLogger(UpdateEtpToESAndBC.class.getName());
//	private static Logger log = LogManager.getLogger(UpdateEtpToESAndBC.class);
//	Gson gs = new Gson();
//
//	static {
//		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
//	}
//
//	String companyId = null;
//
//	/**
//	 * 更新公司数据逻辑
//	 */
//	public void updateCompany(EnterpriseVO ente) {
//
//		try {
//			// 判断companyId是否存在,存在就不生成
//			if (null == companyId || "".equals(companyId)) {
//				// 生成uuid
//				companyId = getCodeid(ente);
//				// 使用新生成的id
//				ente.setCompanyId(companyId);
//			} else {
//				// 使用已有id
//				ente.setCompanyId(companyId);
//			}
//		} catch (Exception e) {
//			logger.error("updateCompany", e);
//		}
//		// 是否生成uuid成功
//		if (null == companyId) {
//			logger.error("生成公司uuid失败!:" + companyId);
//			return;
//		}
//
//		// 得到基本信息
//
//		EnterpriseVO enteData = getEnterprise(ente);
//
//		JsonObject obj = null;
//		JsonDocument doc = null;
//		JsonDocument queryDoc = null;
//
//		obj = JsonObject.fromJson(gs.toJson(enteData));
//
//		JsonDocument jd = CouchbaseUtils.getDocById("192.168.1.30:8091", "etp_t", companyId);
//
//		if (null == jd) {
//
//		}
//
//		// 创建JSON文档
//		doc = JsonDocument.create(companyId, obj);
//		while (true) {
//			try {
//				// 更新文档
//				CouchbaseConnect.commonBucket("192.168.1.30:8091", "etp_t").upsert(doc);
//				break;
//			} catch (Exception e) {
//				log.info("---------------------------> 插入BC超时");
//				log.error(e.getMessage());
//			}
//		}
//
//		DocumentVO dv = new DocumentVO();
//		dv.setDoc(enteData);
//		// 更新ES
//		ElasticSearchConnUtils.updateEsData(enteData.getCompanyId(), gs.toJson(dv), "etp_t", "couchbaseDocument");
//
//		Map<String, DocumentVO> map = null;
//		// // 更新股东与详情数据
//
//		if (null != ente.getHolder() && ente.getHolder().size() > 0) {
//
//			map = getDocumnetList(getHolderVOList(ente.getHolder()), new HolderVO());
//			// 更新es股东数据
//			ElasticSearchConnUtils.updateEsDataList(map, "etp_holder_e", "couchbaseDocument");
//
//			getMapClear(map);
//			// 更新es股东详情数据
//			map = getDocumnetList(getHolderDetail(ente.getHolder()), new HolderDetailVO());
//
//			ElasticSearchConnUtils.updateEsDataList(map, "etp_holder_det_e", "couchbaseDocument");
//			getMapClear(map);
//
//		}
//
//		// // 保留cb历史数据
//		// Integer version = getCompany(companyId);
//		// // 是否更新成功
//		// if (null != version && version == -1) {
//		// logger.error("更新已有公司数据版本失败!:" + version);
//		// return;
//		// }
//
//		// 新增或更新版本数据
//		// ente.setVersion(version);
//
//		// boolean com=CouchbaseUtil.cbThenewUpdate(enteData,"etp_t");
//		// boolean com = bucketOperation.addput(enteData.getCompanyId(),
//		// enteData, "etp_t", "", 3000);
//		// // 是否新增成功
//		// if (!com) {
//		// logger.error("该条cb公司数据更新失败!:" + CouchbaseUtil.GJSON.toJson(ente));
//		// return;
//		// }
//		// // 更新企业es数据
//		// if (!updateEsData("etp_t", "couchbaseDocument",
//		// enteData.getCompanyId(),
//		// new DocumentVO<EnterpriseVO>(enteData))) {
//		// logger.error("该条es公司数据更新失败!:" + CouchbaseUtil.GJSON.toJson(ente));
//		// return;
//		// }
//
//		//
//		// Map<String, DocumentVO<?>> map = null;
//		// // 更新股东与详情数据
//		// updateHolder(ente.getHolder());
//		// if (null != ente.getHolder() && ente.getHolder().size() > 0) {
//		// map = getDocumnetList(getHolderVOList(ente.getHolder()), new
//		// HolderVO());
//		// // 更新es股东数据
//		// updateEsDataList(map, "etp_holder_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// // 更新es股东详情数据
//		// map = getDocumnetList(getHolderDetail(ente.getHolder()), new
//		// HolderDetailVO());
//		// updateEsDataList(map, "etp_holder_det_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//		// // 更新主要人员数据
//		// updateMainManager(ente.getMainManager());
//		// if (null != ente.getMainManager() && ente.getMainManager().size() >
//		// 0) {
//		// map = getDocumnetList(ente.getMainManager(), new MainManagerVO());
//		// // 更新es要人员数据
//		// updateEsDataList(map, "etp_mhold_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//		// // 更新变更数据
//		// updateChange(ente.getChange());
//		// if (null != ente.getChange() && ente.getChange().size() > 0) {
//		// map = getDocumnetList(ente.getChange(), new ChangeVO());
//		// // 更新es变更数据
//		// updateEsDataList(map, "etp_event_item_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//
//		// // 更新分支机构数据
//		// updateBranch(ente.getBranch());
//		// if (null != ente.getBranch() && ente.getBranch().size() > 0) {
//		// map = getDocumnetList(ente.getBranch(), new BranchVO());
//		// // 更新es分支机构数据
//		// updateEsDataList(map, "etp_branch_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//		// // 更新股权出质登记数据
//		// updatePledge(ente.getPledge());
//		// if (null != ente.getPledge() && ente.getPledge().size() > 0) {
//		// map = getDocumnetList(ente.getPledge(), new PledgeVO());
//		// // 更新es股权出质登记数据
//		// updateEsDataList(map, "etp_remise_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//		// // 更新动产抵押数据
//		// updateMortgage(ente.getMortgage());
//		// if (null != ente.getMortgage() && ente.getMortgage().size() > 0) {
//		// map = getDocumnetList(ente.getMortgage(), new MortgageVO());
//		// // 更新es动产抵押数据
//		// updateEsDataList(map, "etp_mortgage_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//		// // 更新年报数据
//		// updateReport(ente.getReport());
//		// if (null != ente.getReport() && ente.getReport().size() > 0) {
//		// map = getDocumnetList(ente.getReport(), new ReportVO());
//		// // 更新es年报数据
//		// updateEsDataList(map, "etp_rept_c", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//		// // 更新行政处罚数据
//		// updatePunishment(ente.getPunishment());
//		// if (null != ente.getPunishment() && ente.getPunishment().size() > 0)
//		// {
//		// map = getDocumnetList(ente.getPunishment(), new PunishmentVO());
//		// // 更新es行政处罚数据
//		// updateEsDataList(map, "etp_punish_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//		// // 更新工商 经营异常信息
//		// updateAbnormal(ente.getAbnormal());
//		// if (null != ente.getAbnormal() && ente.getAbnormal().size() > 0) {
//		// map = getDocumnetList(ente.getAbnormal(), new AbnormalVO());
//		// // 更新es经营异常数据
//		// updateEsDataList(map, "etp_abnormal_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//		// // 更新工商 严重违法信息
//		// updateIllegal(ente.getIllegal());
//		// if (null != ente.getIllegal() && ente.getIllegal().size() > 0) {
//		// map = getDocumnetList(ente.getIllegal(), new IllegalVO());
//		// // 更新es严重违法数据
//		// updateEsDataList(map, "etp_illelnfo_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//		// // 更新抽查检查信息
//		// updateSpotCheck(ente.getSpotCheck());
//		// if (null != ente.getSpotCheck() && ente.getSpotCheck().size() > 0) {
//		// map = getDocumnetList(ente.getSpotCheck(), new SpotCheckVO());
//		// // 更新es抽查检查数据
//		// updateEsDataList(map, "etp_check_e", "couchbaseDocument");
//		// CouchbaseUtil.getMapClear(map);
//		// }
//	}
//
//	CouchbaseUtils bucketOperation = new CouchbaseUtils();
//
//	/**
//	 * 更新股东数据
//	 * 
//	 * @param list
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean updateHolder(List<HolderVO> list) {
//
//		// 查询历史数据
//
//		// ElasticsearchVO enterpr = EsQueryUtil.getIdnex("etp_holder_e", null,
//		// "doc.companyId term " + companyId,
//		// collectDataService);
//		
//		
//		
////		Bucket bucket = null;
////		while (true) {
////			try {
////				bucket = CouchbaseConnect.commonBucket("192.168.1.30:8091", "etp_holder_e");
////				break;
////			} catch (Exception e) {
////				log.info("---------------------------> 连BC超时");
////				log.error(e.getMessage());
////			}
////		}
//
//		SearchResponse sr = null;
//		try {
//			sr = ElasticSearchConnUtils.getSearchResponseByMust("etp_holder_e", "doc.companyId", companyId);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		
//		List<HolderVO> lholder=new ArrayList<HolderVO>();
//		JsonDocument queryDoc = null;
//		for (SearchHit hit : sr.getHits()) {
//			String key = hit.getId();
//			HolderVO hv= gs.fromJson(hit.getSourceAsString(), HolderVO.class); 
//
////			queryDoc = bucket.get(key, 60, TimeUnit.MINUTES);
////			if (null == queryDoc) {
////				log.error("未执行NULL queryDoc-------------------- " + key);
////				continue;
////			}
//			lholder.add(hv);
//			
//		}
//		if(lholder.size()>0){
//			updateRecordsHolder(lholder);
//		}
//
//		//
//
////		// 失效历史数据
////		if (null != enterpr && null != enterpr.getDoc() && enterpr.getDoc().size() > 0) {
////			updateRecordsHolder((List<HolderVO>) enterpr.getDoc());
////			// CouchbaseUtil.getListClear(enterpr.getDoc());
////			enterpr = null;
////		}
//		if (null != list && list.size() > 0) {
//			// 股东id
//			String holderId = null;
//			// 股东详情集合
//			List<HolderDetailVO> holdDetaiList = null;
//			String type = null;
//			Boolean result = false;
//			Integer i = 0;
//			// 遍历股东数据
//			for (HolderVO hold : list) {
//				// 设置公司id
//				hold.setCompanyId(companyId);
//				// 随机生成股东id
//				// holderId=CouchbaseUtil.getRandomUUid();
//				// holderId=CouchbaseUtil.getHolderVOString(hold);
//				holderId = hold.getHolder() == null ? ""
//						: hold.getHolder() + hold.getType() == null ? "" : hold.getType() + companyId;
//				holderId =  getUUid(holderId + (i++));
//				hold.setHolderId(holderId);
//				// 验证股东类型
//				if (null != hold.getType() && !"".equals(hold.getType())) {
//					// 过滤特殊字符
//					type =  getFilterString(hold.getType());
//					// type=CouchbaseUtil.MAPTYPE.get(hold.getType());
//					hold.setType(type);
//				}
//				// 更新股东数据
//				// result=CouchbaseUtil.cbThenewUpdateState(getholder(hold),"etp_holder_e",holderId);
//
//				result = bucketOperation.addput(holderId, getholder(hold), "etp_holder_e", "", 3000);
//				// 是否更新成功
//				if (!result) {
//					// logger.error("该条股东数据更新失败!:" +
//					// CouchbaseUtil.GJSON.toJson(hold));
//					continue;
//				}
//				// 遍历股东详情
//				holdDetaiList = hold.getHolderDetail();
//				if (null != holdDetaiList && holdDetaiList.size() > 0) {
//					String holderDetailId = null;
//					for (HolderDetailVO detai : holdDetaiList) {
//						// 设置股东id
//						detai.setHolderId(holderId);
//						// 随机生成股东详情id
//						// holderDetailId=CouchbaseUtil.getRandomUUid();
//						holderDetailId = getUUid(holderId);
//						detai.setHolderDetailId(holderDetailId);
//						// 更新股东详情数据
//						// result=CouchbaseUtil.cbThenewUpdateState(detai,"etp_holder_det_e",holderDetailId);
//						result = bucketOperation.addput(holderDetailId, detai, "etp_holder_det_e", "", 3000);
//						// 是否更新成功
//						if (!result) {
//							// logger.error("该条股东详情数据更新失败!:" +
//							// CouchbaseUtil.GJSON.toJson(detai));
//						}
//						
//						//写ES
//						ElasticSearchConnUtils.updateEsData(holderDetailId, gs.toJson(detai), "etp_holder_det_e", "couchbaseDocument");
//						
//						
//					}
//				}
//			}
//			result = null;
//			type = null;
//			i = null;
//		}
//		return true;
//	}
//
//	/**
//	 * 更新股东历史数据
//	 * 
//	 * @param list
//	 */
//	public void updateRecordsHolder(List<HolderVO> list) {
//		if (null != list && list.size() > 0) {
//			String holderId = null;
//			Boolean result = null;
//			// 遍历数据
//			for (HolderVO hold : list) {
//				holderId = hold.getHolderId();
//				// hold.setHolderId(null);
//				// 改为失效
//				hold.setFlag(0);
//				// 更新股东历史数据
//				// result=CouchbaseUtil.cbThenewUpdateState(hold,"etp_holder_e",
//				// holderId);
//				
//				//result = bucketOperation.addput(holderId, hold, "etp_holder_e", "", 3000);
//
//				// 删除es股东历史数据
//				elasticsearch.getClient().prepareDelete("etp_holder_e", "couchbaseDocument", holderId).execute()
//						.actionGet();
//				// 删除es股东详情历史数据
//				elasticsearch.getClient().prepareDeleteByQuery("etp_holder_det_e")
//						.setQuery(QueryBuilders.termQuery("doc.holderId", holderId)).execute().actionGet();
//				// 是否更新成功
//				if (!result) {
//					// logger.error("该条历史股东数据更新失败!:" +
//					// CouchbaseUtil.GJSON.toJson(hold));
//				}
//			}
//			holderId = null;
//			result = null;
//
//		}
//	}
//
//	ElasticSearchConnUtils elasticsearch = new ElasticSearchConnUtils();
//
//	public boolean updateEsData(String index, String type, String key, DocumentVO doc) {
//		// return EsQueryUtil.updateEsData(key, CouchbaseUtil.GJSON.toJson(doc),
//		// index, type);
//		return elasticsearch.updateEsData(key, StringUtils.GSON.toJson(doc), index, type);
//	}
//
//	/**
//	 * 去掉股东关联数据
//	 * 
//	 * @param list
//	 * @return
//	 */
//	public List<HolderVO> getHolderVOList(List<HolderVO> list) {
//		List<HolderVO> holders = new ArrayList<HolderVO>();
//		for (HolderVO hold : list) {
//			holders.add(getholder(hold));
//		}
//		return holders;
//	}
//
//	/**
//	 * 去掉股东详情关联数据
//	 * 
//	 * @param list
//	 * @return
//	 */
//	public List<HolderDetailVO> getHolderDetail(List<HolderVO> list) {
//		List<HolderDetailVO> holders = new ArrayList<HolderDetailVO>();
//		for (HolderVO hold : list) {
//			if (null != hold.getHolderDetail() && hold.getHolderDetail().size() > 0) {
//				holders.addAll(hold.getHolderDetail());
//			}
//		}
//		return holders;
//	}
//
//	/**
//	 * 生成公司uuid
//	 */
//	public static String getCodeid(EnterpriseVO ente) {
//		// 获取注册号与信用代码
//		List<CodeVO> codes = getregNumList(ente.getRegNum());
//		String companyId = null;
//		if (null != codes && codes.size() > 0) {
//			for (CodeVO code : codes) {
//				// 注册号
//				if (code.getStatus() == 1) {
//					// 根据注册号生成uuid
//					companyId = getUUid(code.getCode());
//					ente.setRegNum(code.getCode());
//				}
//				// 信用代码
//				if (code.getStatus() == 2) {
//					ente.setCreditCode(code.getCode());
//				}
//			}
//			if (null == companyId) {
//				// 根据注册号或信用代码生成uuid
//				companyId = getUUid(codes.get(0).getCode());
//			}
//		} else {
//			// 根据公司名称与地址生成uuid
//			companyId = getUUid(ente.getCompany() + ente.getLocation());
//		}
//		return companyId;
//	}
//
//	/**
//	 * 根据注册号与信用代码生成UUID方法
//	 * 
//	 * @param value
//	 * @return
//	 */
//	public static String getUUid(String value) {
//		try {
//			NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
//			return nbg.generate(value).toString();
//
//		} catch (Exception e) {
//			logger.error("getUUid", e);
//		}
//		return null;
//	}
//
//	/**
//	 * 根据注册号与信用代码+加版本号生成历史 UUID方法
//	 * 
//	 * @param value
//	 * @return
//	 */
//	public static String getUUid(String value, Integer version) {
//		try {
//			NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
//			return nbg.generate(value + version).toString();
//		} catch (Exception e) {
//			logger.error("getUUid", e);
//		}
//		return null;
//	}
//
//	/**
//	 * 随机生成uuid
//	 * 
//	 * @return
//	 */
//	public static String getRandomUUid() {
//		try {
//			return UUID.randomUUID().toString();
//		} catch (Exception e) {
//			logger.error("getRandomUUid", e);
//		}
//		return null;
//	}
//
//	// 过滤特殊字符表达式
//	public static Pattern pattern = Pattern.compile("[0-9a-zA-Z\u4e00-\u9fa5]*");
//	public static Pattern pattern2 = Pattern.compile("[0-9a-zA-Z]*");
//
//	/**
//	 * 处理注册号与信用代码
//	 * 
//	 * @param value
//	 * @return
//	 */
//	public static List<CodeVO> getregNumList(String value) {
//		if (null == value) {
//			return null;
//		}
//		List<CodeVO> list = new ArrayList<CodeVO>();
//		String code = value.replaceAll("[&nbsp;\r\t]", "");
//		if (code.length() == 15 || code.length() == 13) {
//			if (pattern2.matcher(code).matches()) {
//				// 注册号
//				list.add(new CodeVO(code, 1));
//			} else {
//				if (code.substring(code.length() - 1, code.length()).equals("号") || code.substring(0, 1).equals("企")) {
//					// 注册号
//					list.add(new CodeVO(code, 1));
//				}
//			}
//			return list;
//
//		} else if (code.length() == 18) {
//			if (!pattern2.matcher(code).matches()) {
//				return null;
//			}
//			// 信用代码
//			list.add(new CodeVO(code, 2));
//			return list;
//		} else {
//			// 可能存在问题，可能是注册号与信用代码中间存在特殊字符
//			return getCodes(code);
//		}
//	}
//
//	/**
//	 * 拆分注册码信用代码
//	 * 
//	 * @param value
//	 * @return
//	 */
//	public static List<CodeVO> getCodes(String value) {
//		Matcher matcher = null;
//		StringBuffer sb = new StringBuffer();
//		String[] a = value.split("");
//		for (String c : a) {
//			matcher = pattern.matcher(c);
//			if (matcher.matches()) {
//				sb.append(c);
//			} else {
//				sb.append(",");
//			}
//		}
//		a = sb.toString().split(",");
//		List<CodeVO> list = new ArrayList<CodeVO>();
//		for (String c : a) {
//			if (null != c && !"".equals(c)) {
//				if (c.length() <= 4) {
//					continue;
//				}
//				if (c.length() == 15 || c.length() == 13) {
//					if (pattern2.matcher(c).matches()) {
//						// 注册号
//						list.add(new CodeVO(c, 1));
//					} else {
//						if (c.substring(c.length() - 1, c.length()).equals("号") || c.substring(0, 1).equals("企")) {
//							// 注册号
//							list.add(new CodeVO(c, 1));
//						}
//					}
//
//				} else if (c.length() == 18) {
//					// if(!pattern2.matcher(c).matches()){continue;}
//					// 信用代码
//					list.add(new CodeVO(c, 2));
//				} else if (c.substring(c.length() - 1, c.length()).equals("号") || c.substring(0, 1).equals("企")) {
//					// 注册号
//					list.add(new CodeVO(c, 1));
//				}
//			}
//		}
//		return list;
//	}
//
//	public EnterpriseVO getEnterprise(EnterpriseVO ente) {
//		EnterpriseVO ent = new EnterpriseVO();
//		// 获取省市县
//		// ProvinceScityScountry pr = getPrSiSc(ente);
//		// if (null != pr) {
//		// ent.setArea(pr.getScName());
//		// ent.setCity(pr.getSiName());
//		// }
//
//		ent.setCompany(ente.getCompany());
//		ent.setCompanyId(ente.getCompanyId());
//		ent.setProvince(ente.getProvince());
//		ent.setCreditCode(ente.getCreditCode());
//		ent.setRegNum(ente.getRegNum());
//		ent.setApproveDate(ente.getApproveDate());
//		ent.setFlag(ente.getFlag());
//		ent.setVersion(ente.getVersion());
//		ent.setComposition(ente.getComposition());
//		ent.setEndTime(ente.getEndTime());
//		ent.setLocation(ente.getLocation());
//		ent.setRevokeDate(ente.getRevokeDate());
//		ent.setRegCapital(ente.getRegCapital());
//		ent.setStartTime(ente.getStartTime());
//		ent.setRegOffice(ente.getRegOffice());
//		ent.setType(ente.getType());
//		ent.setUrl(ente.getUrl());
//		ent.setScope(ente.getScope());
//		ent.setRemark(ente.getRemark());
//		ent.setUpdateTime(ente.getUpdateTime());
//		ent.setLegalRep(ente.getLegalRep());
//		ent.setRegState(ente.getRegState());
//		ent.setCreateTime(ente.getCreateTime());
//		ent.setRegDate(ente.getRegDate());
//		ent.setFlag(1);
//		ent.setCurrency(ente.getCurrency());
//
//		// ValidData.getFilterCompanyData(ent);
//		// pr = null;
//		return ent;
//	}
//
//	/**
//	 * 获取纯股东数据
//	 */
//	public HolderVO getholder(HolderVO holder) {
//		HolderVO hold = new HolderVO();
//		hold.setCompanyId(holder.getCompanyId());
//		hold.setEquityPart(holder.getEquityPart());
//		hold.setHolder(holder.getHolder());
//		hold.setHolderId(holder.getHolderId());
//		hold.setLicenseNum(holder.getLicenseNum());
//		hold.setLicenseType(holder.getLicenseType());
//		hold.setParticulars(holder.getParticulars());
//		hold.setRemark(holder.getRemark());
//		hold.setType(holder.getType());
//		hold.setFlag(1);
//		// ValidData.getFilterHolderData(hold);
//		return hold;
//	}
//
//	/**
//	 * 构造document数据
//	 * 
//	 * @param list
//	 * @return
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public Map<String, DocumentVO> getDocumnetList(List<?> list, Object obj) {
//		if (null != list && list.size() > 0) {
//			Map<String, DocumentVO> map = new HashMap<String, DocumentVO>();
//			String key = null;
//			for (Object doc : list) {
//				// 获取公司id
//				if (obj.getClass().equals(EnterpriseVO.class)) {
//					EnterpriseVO entvo = (EnterpriseVO) doc;
//					key = entvo.getCompanyId();
//				}
//				// 获取股东id
//				else if (obj.getClass().equals(HolderVO.class)) {
//					HolderVO holder = (HolderVO) doc;
//					key = holder.getHolderId();
//				}
//				// 获取人员id
//				else if (obj.getClass().equals(MainManagerVO.class)) {
//					MainManagerVO manager = (MainManagerVO) doc;
//					key = manager.getMainManagerId();
//				}
//				// 获取Court id
//				else if (obj.getClass().equals(CourtVO.class)) {
//					CourtVO court = (CourtVO) doc;
//					key = court.getCourtId();
//				}
//				// 获取Executed id
//				else if (obj.getClass().equals(ExecutedVO.class)) {
//					ExecutedVO execut = (ExecutedVO) doc;
//					key = execut.getExecutedId();
//				}
//				// 获取CourtPub id
//				else if (obj.getClass().equals(CourtPubVO.class)) {
//					CourtPubVO pub = (CourtPubVO) doc;
//					key = pub.getCourtPubId();
//				}
//				// 获取breakfaith id
//				else if (obj.getClass().equals(BreakFaithVO.class)) {
//					BreakFaithVO brea = (BreakFaithVO) doc;
//					key = brea.getBreakFaithId();
//				}
//				// 获取SpotCheckVO id
//				else if (obj.getClass().equals(SpotCheckVO.class)) {
//					SpotCheckVO spot = (SpotCheckVO) doc;
//					key = spot.getSpotCheckId();
//				}
//				// 获取PunishmentVO id
//				else if (obj.getClass().equals(PunishmentVO.class)) {
//					PunishmentVO punis = (PunishmentVO) doc;
//					key = punis.getPunishmentId();
//				}
//				// 获取PledgeVO id
//				else if (obj.getClass().equals(PledgeVO.class)) {
//					PledgeVO pledge = (PledgeVO) doc;
//					key = pledge.getPledgeId();
//				}
//				// 获取MortgageVO id
//				else if (obj.getClass().equals(MortgageVO.class)) {
//					MortgageVO mortga = (MortgageVO) doc;
//					key = mortga.getMortgageId();
//				}
//				// 获取IllegalVO id
//				else if (obj.getClass().equals(IllegalVO.class)) {
//					IllegalVO illega = (IllegalVO) doc;
//					key = illega.getIllegalId();
//				}
//				// 获取BranchVO id
//				else if (obj.getClass().equals(BranchVO.class)) {
//					BranchVO breach = (BranchVO) doc;
//					key = breach.getBranchId();
//				}
//				// 获取AbnormalVO id
//				else if (obj.getClass().equals(AbnormalVO.class)) {
//					AbnormalVO abnor = (AbnormalVO) doc;
//					key = abnor.getAbnormalId();
//				}
//				// 获取AbnormalVO id
//				else if (obj.getClass().equals(ReportVO.class)) {
//					ReportVO report = (ReportVO) doc;
//					key = report.getReportId();
//				}
//				// 获取ChangeVO id
//				else if (obj.getClass().equals(ChangeVO.class)) {
//					ChangeVO change = (ChangeVO) doc;
//					key = change.getChangeId();
//				}
//				// 获取WholeCourtVO id
//				else if (obj.getClass().equals(WholeCourtVO.class)) {
//					WholeCourtVO whole = (WholeCourtVO) doc;
//					key = whole.getWholeCourtId();
//				}
//				// 获取HolderDetailVO id
//				else if (obj.getClass().equals(HolderDetailVO.class)) {
//					HolderDetailVO detail = (HolderDetailVO) doc;
//					key = detail.getHolderDetailId();
//				}
//
//				map.put(key, new DocumentVO(doc));
//			}
//			return map;
//		}
//		return null;
//	}
//
//	@SuppressWarnings("rawtypes")
//	public static void getMapClear(Map map) {
//		if (null != map && map.size() > 0) {
//			map.clear();
//			map = null;
//		}
//	}
//	/**
//	 * 过滤特殊字符
//	 */
//	public static String getFilterString(String value) {
//		if (null != value) {
//			return new StringBuffer(value.replaceAll("[&nbsp;!`,@#$%^&\t\r\n\\(\\)，【】\\{\\}\\-\\|/+*]", "")).toString();
//		}
//		return null;
//	}
//
// 
//
//}
