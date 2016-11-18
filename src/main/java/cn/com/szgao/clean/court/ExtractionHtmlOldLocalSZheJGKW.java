package cn.com.szgao.clean.court;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
//import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.HtmlUtils;
import cn.com.szgao.dto.ClientVO;
import cn.com.szgao.dto.RelativeVO;
import cn.com.szgao.dto.WholeCourtVO;

import cn.com.szgao.wash.data.AdministrationUtils;
import cn.wanghaomiao.xpath.exception.NoSuchAxisException;
import cn.wanghaomiao.xpath.exception.NoSuchFunctionException;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import cn.com.szgao.clean.court.ExtractthepeopleText;
import cn.com.szgao.util.StringUtils;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 处理浙江省_浙江法院公开网
 * 
 * @author liuming
 * @Date 2016年7月6日 下午2:32:59
 */
public class ExtractionHtmlOldLocalSZheJGKW {

	// 劳动争议一案; 纠纷 广水市××山××大道××号; 纷一案中; 到庭 参加 本院 受理 后 认为 吴xx已交纳 XXXXX
	// ----------------自然人、企业名、当事人分类名里不能出现这样的 ----,"本院","本案"
	// --委托代理人蔡红，湖北陵燕律师事务所律师。代理权限：参加诉讼、调解。 "参加",
	// 委托代理人：袁沛良，东莞市启明律师事务所律师（于2015年7月10日被撤回授权）。 "撤回",
	// 诉讼代理人：贺国兵，湖北××律师事务所律师。代理权限为：代为进行答辩，调查，参加开庭审理、陈述事实、质证活动，发表代理意见，代收各种诉讼文书。
	// "答辩", "到庭", "参加", "解除",
	// 被告：中国人寿财产保险给付有限公司芜湖市中心支公司，住所地安徽省芜湖市镜湖区。 "给付",
	// 原审被告李庆华，女，1970年9月22日出生，汉族，创业集团精细化工有偿解除劳动合同工人。 解除
	// 委托代理人任世昭、戴武，该公司法务部顾问。代理权限：代为承认、放弃、变更诉讼请求，进行和解等。 "请求",
	// 被告熊斌，因涉嫌刑事犯罪，现羁押于宜都市看守所。 涉嫌 "涉嫌",
	// 原审被告范有信（已死亡），男，1954年5月5日出生，汉族，无职业。 "死亡",
	// 委托代理人董仕平，湖北维天律师事务所律师。代理权限：代为承认、放弃、变更诉求、和解、代签法律文书。 诉求 "诉求",
	// 委托代理人王剑峰，该行法律顾问室工作人员。代理权限为特别授权，即代为提出诉前保全申请、代为承认、放弃、变更诉讼请求，进行和解等。 提出 提起
	// "提出", "提起",
	// 委托代理人杨和存（代理权限：代为收集证据，参加诉讼，辩论，代收法律文书），随县新街法律服务所法律工作者。 "证据", "证明",
	// 业主张长江，男，1986年1月15日出生，汉族，现住址同上。 "主张",
	// 原告重庆市道路交通事故社会救助基金管理中心，住所地重庆市渝北区青枫北路12号，组织机构代码55409394-Ｘ。 "事故",
	// 委托代理人黎玉石。代理权限为陈述事实、参加辩论等一般授权。 事实
	// 委托代理人刘晓莲，广东维强律师事务所律师。代理权限：特别授权（调查取证、协商、谈判；提起诉讼，办理立案相关事宜；提起反诉、提出答辩，参与庭审活动；提出撤诉、变更、放弃或承认诉讼请求；进行和解、调解；提起上诉等）。
	// "办理",
	// "行驶", "诈骗", "盗窃", "强奸", "聚众斗殴", "寻衅滋事", "贩卖毒品", "运输毒品", "故意伤害", "涉嫌诽谤",
	// "抢劫", "绑架", "勒索", "杀人",
	// "非法拘禁", "运输毒品", "破坏", "违法", "非法", "犯罪",
	// 判决
	// 第三人（本院追加）曾天贵，男，1974年10月22日出生，汉族，因诈骗罪于2013年3月被判处有期徒刑4年2个月，现服刑于福建省泉州市第四监狱。
	// "判处", "判决",
	// "非法", "违法", "劫罪", "危险驾驶", "危险驾驶",
	public static String[] CLIENT_END = { "一案", "纠纷", "争议", "认定", "确认", "认为", "辩称", "承担", "负担", "签订", "无异议", "涉及", "诉称",
			"改判", "作为", "同居", "同居生活", "副本", "享受", "相互认识", "婚姻关系", "驳回", "通告", "纠纷", "未履行生效法律文书", "未履行法律文书", "申请强制执行",
			"受理", "生效" };

	public static String[] CLIENT_END_XinShi = { "一案", "纠纷", "争议", "认定", "确认", "认为", "辩称", "承担", "负担", "签订", "无异议",
			"涉及", "诉称", "改判", "作为", "同居", "同居生活", "副本", "享受", "相互认识", "婚姻关系", "驳回", "通告",

			"公诉机关以",

			"检察院以", "检察院指派", "指派", "公开开庭审理", "开庭审理", "上述事实", "经鉴定", "经审理", "查明", "纠纷", "未履行生效法律文书", "未履行法律文书", "申请强制执行",
			"受理", "生效" };

	/**
	 * 结束处的审判官等
	 */
	// public static String[] JUDGES = { "审判长", "审判员", "书记员", "人民陪审员", "执行长",
	// "代理审判员", "执行员", "代理审判长" };

	/**
	 * 案件分类
	 */
	public static String[] LAWCASE = { "民事案件", "刑事案件", "行政案件", "知识产权", "赔偿案件", "执行案件" };
	/**
	 * 案件分类 简写
	 */
	// public static String[]
	// LAWCASE_AD={"民事","刑事","行政","知识产权","赔偿","执行","涉外","海事"};

	public static HashMap<String, String> MPA_LAWCASE = new HashMap<String, String>();

	static {
		MPA_LAWCASE.put("民事", "民事案件");
		MPA_LAWCASE.put("刑事", "刑事案件");
		MPA_LAWCASE.put("行政", "行政案件");
		MPA_LAWCASE.put("知识产权", "知识产权");
		MPA_LAWCASE.put("赔偿", "赔偿案件");
		MPA_LAWCASE.put("执行", "执行案件");
		MPA_LAWCASE.put("涉外", "涉外案件");
		MPA_LAWCASE.put("海事", "海事案件");

	}

	private static Cluster cluster2 = CouchbaseCluster.create("192.168.1.30");
	public static String[] DATESTATUS = { "提交时间", "提交日期", "发布时间", "发布日期", "关注时间", "编辑时间", "编辑日期", "发表时间", "录入时间",
			"更新时间", "作者点击", "点击时间", "【关闭窗口】", "大中小", "大小", "年月日编辑", "字体", "点击率", "浏览", "点击数", "∣法律社区", "发表于", "阅读",
			"点击", "日期", "作者", "时间", "今天是" };

	// // 被告
	// public static String[] DEFENDANT = { "被上诉人", "被执行人", "被申诉人", "被申请人",
	// "被申请执行人", "原审被告人", "原审原告", "罪犯", "被告",
	// "赔偿义务机关", "一审被告", "二审被上诉人" };
	//
	// // 原告
	// public static String[] PLAINTIFF = { "第三人", "诉讼代理人", "辩护人", "上诉人", "申诉人",
	// "申请执行人", "申请人", "执行人", "原审被告", "赔偿请求人",
	// "原公诉机关", "公诉机关", "执行机构", "原告", "复议机关", "申请复议人", "一审原告", "委托代理人", "法定代表人",
	// "起诉人", "移送执行机构", "二审上诉人", "原审第三人",
	// "负责人", "抗诉机关", "申请再审人", "委托代理", "四被上诉人委托代理人", "两上诉人的委托代理人" };
	//
	// public static String[] KEYWORDKE = { "申请再审人", "被上诉人", "二审被上诉人", "原审被告人",
	// "原审第三人", "二审上诉人", "一审被告", "一审原告", "被申请人",
	// "赔偿请求人", "被告人", "原告", "执行机构", "被申请人", "申请执行人", "申请人", "辩护人", "被申请执行人",
	// "赔偿请求人", "赔偿义务机关", "原公诉机关", "抗诉机关",
	// "公诉机关", "复议机关", "委托代理人", "委托代理", "特别授权代理", "四被上诉人委托代理人", "两上诉人的委托代理人",
	// "移送执行机构", "诉讼代理人", "法定代表人", "申请复议人",
	// "被上诉人", "被申诉人", "被执行人", "反诉被告", "反诉原告", "原审被告", "原审原告", "执行人", "负责人",
	// "上诉人", "起诉人", "申诉人", "被告人", "原告人",
	// "被告", "原告", "罪犯", "第三人" };
	public static String[] THEVERDICT = { "裁定如下", "决定如下", "判决如下", "协议如下", "处理意见如下", "调解协议", "如下协议", "决定" };
	public static String[] CAUSE = { "驳回申诉通知", "赔偿决定书", "提起公诉", "提出公诉", "提起上诉", "提出上诉", "提起诉讼", "提出诉讼", "提起行政诉讼",
			"提出行政诉讼", "争议一案", "纠纷一案", "通告一案", "违法一案", "执行一案", "赔偿一案", "劫罪一案", "确认一案", "涉嫌", "危险驾驶", "诈骗", "盗窃", "死亡",
			"强奸", "聚众斗殴", "寻衅滋事", "贩卖毒品", "运输毒品", "故意伤害", "涉嫌诽谤", "抢劫", "绑架", "勒索", "杀人", "纠纷", "非法拘禁", "运输毒品",
			"破坏电力设备", "一案", "违法", "非法", "犯罪", "未履行生效法律文书", "未履行法律文书", "申请强制执行" };

	public static String[] BOOKCLASS = { "准许强制执行裁定书", "民事附带刑事判决书", "强制医疗决定书", "指定管辖决定书", "非诉行政执行裁定书", "行政审查裁定书",
			"不予受理案件决定书", "暂予监外执行决定书", "民事调解书判决书", "强制医疗决定书", "准予撤诉决定书", "刑事附带民事判决书", "刑事附带民事调解书", "案件执行结束通知书", "减刑假释文书",
			"口头撤诉裁定笔录", "普通民事文书", "普通刑事文书", "民事调解书", "民事裁定书", "民事判决书", "民事决定书", "刑事判决书", "刑事裁定书", "刑事决定书", "行政判决书",
			"行政决定书", "行政裁定书", "执行裁定书", "普通执行文书", "普通行政文书", "行政文书", "商事文书", "执行判决书", "执行决定书", "国家赔偿裁定书", "国家赔偿判决书",
			"国家赔偿决定书", "驳回申诉通知书", "调解书", "决定书", "通知书", "判决书", "裁定书", "民事", "刑事", "行政", "执行" };// 文书类型

	public static String[] PROVINCE = { "北京市", "天津市", "上海市", "重庆市", "河北省", "河南省", "云南省", "辽宁省", "黑龙江省", "湖南省", "安徽省",
			"山东省", "新疆", "江苏省", "浙江省", "江西省", "湖北省", "广西", "甘肃省", "山西省", "内蒙古", "陕西省", "吉林省", "福建省", "贵州省", "广东省",
			"青海省", "西藏", "四川省", "宁夏", "海南省", "台湾省 " };
	public static String[] CAUSENUM = { "（２０", "(2０", "（2０", "〔2０", "[2０", "【2０", "(20", "（20", "〔20", "［2", "[20",
			"【20", "(19", "（19", "〔19", "[19", "【19" };
	public static String[] CAUSENUM2 = { "判决书字号", "字号" };
	public static String[] CAUSENUM3 = { "第", "字" };
	static long count = 0;// 总数量
	static long ALL = 0;// 出错数据
	static long SUM = 0;
	static Map<String, String> MAPS = new HashMap<String, String>();

	static {
		MAPS.put("html", "html");
		MAPS.put("htm", "htm");
		MAPS.put("txt", "txt");
	}

	public static String[] charset = { "utf-8", "gbk", "gb2312", "gb18030", "big5" };
	public static String[] ERCOEDING = { "й", "෨", "Ժ", "ۼ", "ҩ", "ල", "ɷ", "ص", "δ", "ġ", "Ϊ", "ط", "Ϣ", "ȡ", "Ӫ", "ã",
			"", "Դ", "ڲ", "Ѱ", "�" };
	private static Logger logger = LogManager.getLogger(ExtractionHtmlOldLocalSZheJGKW.class.getName());

	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}

	/**
	 * @param args
	 */
	// public static void main(String[] args) throws IOException {
	// PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	// long da = System.currentTimeMillis();
	// // File file = new File("G:/Data/2016新数据/HTML/HTML20140120-20140106");
	// File file = new File("C:/data/文书新版本/HTML/HTML20131231-20130601");
	//
	// // 初始数据
	// CourtData.getlistsCasecause();
	//
	// // 查PG省市县/区
	// Bucket bucket = null;
	// bucket = connectionBucket(bucket);
	// AdministrationUtils util = new AdministrationUtils();
	// util.initData(); // 查询行政区
	// try {
	// show(file, bucket, util);
	// } catch (Exception e) {
	// logger.error(e.getMessage());
	// } finally {
	// file = null;
	// util = null;
	// bucket.close();
	// cluster2 = null;
	// }
	// logger.info(count + ":数量");
	// logger.info("所有文件总耗时" + (((System.currentTimeMillis() - da) / 1000) / 60)
	// + "分钟");
	// }

	// 连接CB
	@SuppressWarnings("unused")
	private static Bucket connectionBucket(Bucket bucket) {
		try {
			bucket = connectionCouchBaseLocal();// 本地CB
		} catch (Exception e) {
			while (true) {
				try {
					bucket = connectionCouchBaseLocal();// 本地CB
					break;
				} catch (Exception ee) {
					logger.error(ee);
				}
			}
		}

		return bucket;
	}

	/**
	 * 递归遍历html文件
	 * 
	 * @param file
	 * @throws @throws
	 *             Exception
	 */
	private static void show(File file, Bucket bucket, AdministrationUtils util) throws Exception {
		String variable = null;
		String html = null;
		WholeCourtVO arch = null;
		Map<String, List<String>> list = null;
		List<WholeCourtVO> listarchs = null;
		Document doc;
		listarchs = new ArrayList<WholeCourtVO>();
		int i = 0;
		if (file.isFile()) {
			arch = new WholeCourtVO();
			String suffix = file.getName();
			suffix = suffix.substring(suffix.indexOf(".") + 1, suffix.length());
			suffix = MAPS.get(suffix);
			if (null == suffix) {
				return;
			}
			logger.info("网址:" + file.getPath());
			for (String val : charset) {// 匹配不同编码格式
				doc = Jsoup.parse(file, val);
				html = doc.body().text();// 取页面body标签中所有内容
				boolean Garbled = getErrorCode(html);// 判断编码是否错误
				if (Garbled == false) {
					logger.info(   "页面编码错误！！！");
					i++;
					if (i == 5) {
						html = null;
					}
					continue;
				}
				i = 0;
				variable = getReplaceAll(doc.title());
				if (variable != null && !"".equals(variable)) {
					arch.setTitle(variable.trim()); // 标题 √
				}
				logger.info("标题:" + arch.getTitle());
				break;
			}
			if (html == null || "".equals(html)) {
				logger.info("内容为空的HTML页面：" + file.getPath());
			}
			html = getReplaceAll(html).trim();
			logger.info("所有内容：" + html);
			list = ExtractthepeopleText.getPersonName(html);
			arch.setPlaintiff(getKeyName(list, 1)); // 原告相关人 √
			logger.info("<<------------------------------------------------------>>");
			logger.info("原告相关人:" + arch.getPlaintiff());
			arch.setDefendant(getKeyName(list, 2)); // 被告相关人 √
			logger.info("被告相关人:" + arch.getDefendant());
			variable = getCourtName(html);
			if (variable != null && !"".equals(variable)) {
				arch.setCourtName(variable); // 法院 √
			}
			if (variable == null || "".equals(variable)) {
				arch.setCourtName(getAtherthe(html)); // 法院 √
			}
			logger.info("法院:" + arch.getCourtName());
			arch.setCaseCause(StringCause(html)); // 案由 √
			logger.info("案由:" + arch.getCaseCause());
			arch.setApprovalDate(getConcludeDate(html)); // 审结日期 √
			logger.info("审结日期:" + arch.getApprovalDate());
			arch.setApproval(getTheVerdictData(html)); // 判决结果 √
			logger.info("判决结果:" + arch.getApproval());
			arch.setCatalog(getCatalog(html)); // 文书类型 √
			logger.info("文书类型:" + arch.getCatalog());
			variable = getCaseNum(html);
			if (!"".equals(variable) && variable != null) {
				arch.setCaseNum(variable); // 案号 √
			}
			if ("".equals(variable) || variable == null) {
				arch.setCaseNum(getSentenceNo3(html)); // 案号 √
			}
			logger.info("案号:" + arch.getCaseNum());
			arch.setPath(suffix);
			logger.info("<<------------------------------------------------------>>");
			arch.setWholeCourtId(file.getName().substring(0, file.getName().lastIndexOf(".")));
			listarchs.add(arch);
			boolean result = updateJsonData(listarchs, bucket, util);
			if (!result) {
				logger.info(file.getPath() + ":更新失败");
			}
			count += listarchs.size();
			logger.info("<<------------------------count------------------------------>>" + count);
			listarchs = null;
			return;
		}
		File[] files = file.listFiles();
		for (File fi : files) {
			if (fi.isFile()) {
				arch = new WholeCourtVO();
				String suffix = fi.getName();
				suffix = suffix.substring(suffix.indexOf(".") + 1, suffix.length());
				suffix = MAPS.get(suffix);
				if (null == suffix) {
					return;
				}
				for (String val : charset) { // 匹配不同编码格式
					doc = Jsoup.parse(fi, val);
					html = doc.body().text();
					boolean Garbled = getErrorCode(html);// 判断编码是否错误
					if (Garbled == false) {
						i++;
						if (i == 5) {
							html = null;
						} // 判断编码格式都不匹配的时候赋予空值
						continue;
					}
					i = 0;
					variable = getReplaceAll(doc.title());
					if (variable != null && !"".equals(variable)) { // 防止title标签为空的情况
						arch.setTitle(variable.trim()); // 标题 √
					}
					break;
				}
				if (html == null || "".equals(html)) {
					logger.info("内容为空的HTML页面：" + fi.getPath());
					continue;
				}
				html = getReplaceAll(html).trim();// 所有内容去掉特殊字符√

				list = ExtractthepeopleText.getPersonName(html);

				arch.setPlaintiff(getKeyName(list, 1)); // 原告相关人√

				arch.setDefendant(getKeyName(list, 2)); // 被告相关人√

				arch.setCatalog(getCatalog(html)); // 文书类型 √

				variable = getCourtName(html);
				if (variable != null && !"".equals(variable)) {
					arch.setCourtName(variable); // 法院 √
				}
				if (variable == null || "".equals(variable)) {
					arch.setCourtName(getAtherthe(html)); // 法院 √
				}

				arch.setCaseCause(StringCause(html)); // 案由 √

				arch.setApprovalDate(getConcludeDate(html)); // 审结日期√

				arch.setApproval(getTheVerdictData(html)); // 判决结果√

				variable = getCaseNum(html);
				if (!"".equals(variable) && variable != null) {
					arch.setCaseNum(variable); // 案号 √
				}
				if ("".equals(variable) || variable == null) {
					arch.setCaseNum(getSentenceNo3(html)); // 案号 √
				}
				arch.setPath(suffix);
				arch.setWholeCourtId(fi.getName().substring(0, fi.getName().lastIndexOf(".")));
				listarchs.add(arch);
				if (listarchs.size() >= 1000) {
					boolean result = updateJsonData(listarchs, bucket, util);
					if (!result) {
						logger.info(fi.getPath() + ":更新失败1");
					}
					count += listarchs.size();
					logger.info("<<------------------------count------------------------------>>" + count);
					listarchs = null;
					listarchs = new ArrayList<WholeCourtVO>();
				}
			} else if (fi.isDirectory()) {
				logger.info(fi.getName());
				show(fi, bucket, util);
			} else {
				continue;
			}
		}
		if (null != listarchs && listarchs.size() > 0) {
			boolean result = updateJsonData(listarchs, bucket, util);
			if (!result) {
				logger.info(":更新失败2");
			}
			count += listarchs.size();
			listarchs = null;
			arch = null;
			return;
		}
	}

	/**
	 * 抓取的数据展示
	 * 
	 * @param arch
	 */
	public static void showData(WholeCourtVO arch) {
		logger.info("UUID:" + arch.getWholeCourtId());
		logger.info("原告相关人:" + arch.getPlaintiff());
		logger.info("被告相关人:" + arch.getDefendant());
		logger.info("法院:" + arch.getCourtName());
		logger.info("审结日期:" + arch.getApprovalDate());
		logger.info("文书类型:" + arch.getCatalog());
		logger.info("案号:" + arch.getCaseNum());
		logger.info("标题:" + arch.getTitle());
		logger.info("案由:" + arch.getCaseCause());
		logger.info("判决结果:" + arch.getApproval());
	}

	/**
	 * 裁判文书 抓取word，HTML修改court桶
	 */
	public static boolean updateJsonData(List<WholeCourtVO> list, Bucket bucket, AdministrationUtils util)
			throws Exception {
		if (null == list || list.size() <= 0) {
			return false;
		}
		String[] array = null;
		JsonDocument doc = null;
		JsonObject obj2 = null;
		com.google.gson.JsonObject json = null;
		Gson gson = new Gson();
		WholeCourtVO archs = null;
		try {
			for (WholeCourtVO arch : list) {
				SUM++;
				// 查询数据
				doc = JsonDocument.create(arch.getWholeCourtId()); // 获取ID
				obj2 = bucket.get(doc) == null ? null : bucket.get(doc).content();// 这是json数据
				if (obj2 == null) {
					logger.info("匹配不到UUID:" + arch.getWholeCourtId());
					continue;
				}
				archs = new WholeCourtVO();
				json = gson.fromJson(obj2.toString(), com.google.gson.JsonObject.class);
				archs = gson.fromJson(json, WholeCourtVO.class);

				if (null != arch.getTitle() && !"".equals(arch.getTitle())) {
					archs.setTitle(arch.getTitle());
				}
				if (null != obj2.get("title") && !"".equals(obj2.get("title"))) {
					archs.setTitle(obj2.get("title").toString());// 标题
				}
				if (null != obj2.get("detailLink") && !"".equals(obj2.get("detailLink"))) {
					archs.setDetailLink(obj2.get("detailLink").toString());// url
				}
				if (null != arch.getCatalog() && !"".equals(arch.getCatalog())) {
					archs.setCatalog(arch.getCatalog());
				}
				if (null != obj2.get("catalog") && !"".equals(obj2.get("catalog"))) {
					archs.setCatalog(obj2.get("catalog").toString());// 分类
				}
				if (null != arch.getCaseNum() && !"".equals(arch.getCaseNum())) {
					archs.setCaseNum(arch.getCaseNum());
				}
				if (null != obj2.get("caseNum") && !"".equals(obj2.get("caseNum"))) {
					archs.setCaseNum(obj2.get("caseNum").toString());// 案号
				}
				if (null != arch.getCourtName() && !"".equals(arch.getCourtName())) {
					archs.setCourtName(arch.getCourtName());
					array = util.utils(arch.getCourtName());
				}
				if (null != obj2.get("courtName") && !"".equals(obj2.get("courtName"))) {
					archs.setCourtName(obj2.get("courtName").toString());// 法院名
					array = util.utils(obj2.get("courtName").toString());
				}
				if (null != obj2.get("publishDate") && !"".equals(obj2.get("publishDate"))) {
					archs.setPublishDate(
							DateUtils.toYMDOfChaStr_ESZZ2(DateUtils.getReplaceAllDate(obj2.get("publishDate").toString())));// 发布日期
				}
				if (null != obj2.get("province") && !"".equals(obj2.get("province"))) {
					archs.setProvince(obj2.get("province").toString());// 省
				}
				if (null != obj2.get("city") && !"".equals(obj2.get("city"))) {
					archs.setCity(obj2.get("city").toString());// 市
				}
				if (null != obj2.get("area") && !"".equals(obj2.get("area"))) {
					archs.setArea(obj2.get("area").toString());// 县
				}
				if (null != array) {
					if (null != array[0] && !"".equals(array[0])) {
						archs.setProvince(array[0]);
					}
					if (null != array[1] && !"".equals(array[1])) {
						archs.setCity(array[1]);
					}
					if (null != array[2] && !"".equals(array[2])) {
						archs.setArea(array[2]);
					}
				}
				if (null != obj2.get("collectDate") && !"".equals(obj2.get("collectDate"))) {
					archs.setCollectDate(
							DateUtils.toYMDOfChaStr_ESZZ2(DateUtils.getReplaceAllDate(obj2.get("collectDate").toString())));// 采集时间
				}
				if (null != arch.getPlaintiff() && !"".equals(arch.getPlaintiff())) {
					archs.setPlaintiff(arch.getPlaintiff());
				}
				if (null != obj2.get("plaintiff") && !"".equals(obj2.get("plaintiff"))) {
					archs.setPlaintiff(obj2.get("plaintiff").toString());// 原告
				}
				if (null != arch.getDefendant() && !"".equals(arch.getDefendant())) {
					archs.setDefendant(arch.getDefendant());
				}
				if (null != obj2.get("defendant") && !"".equals(obj2.get("defendant"))) {
					archs.setDefendant(obj2.get("defendant").toString());// 被告
				}
				if (null != arch.getApproval() && !"".equals(arch.getApproval())) {
					archs.setApproval(arch.getApproval());// 审判结果
				}
				if (null != obj2.get("approval") && !"".equals(obj2.get("approval"))) {
					archs.setApproval(obj2.get("approval").toString());// 审批结果
				}
				if (null != obj2.get("suitType") && !"".equals(obj2.get("suitType"))) {
					archs.setSuitType(obj2.get("suitType").toString());// 起诉类型
				}
				if (null != obj2.get("suitDate") && !"".equals(obj2.get("suitDate"))) {
					archs.setSuitDate(DateUtils.toYMDOfChaStr_ESZZ2(obj2.get("suitDate").toString()));// 起诉日期
				}
				if (null != arch.getApprovalDate() && !"".equals(arch.getApprovalDate())) {
					archs.setApprovalDate(DateUtils.toYMDOfChaStr_ESZZ2(arch.getApprovalDate()));
				}
				if (null != obj2.get("approvalDate") && !"".equals(obj2.get("approvalDate"))) {
					archs.setApprovalDate(DateUtils.toYMDOfChaStr_ESZZ2(obj2.get("approvalDate").toString()));// 审结日期
				}
				if (null != arch.getCaseCause() && !"".equals(arch.getCaseCause())) {
					archs.setCaseCause(arch.getCaseCause());
				}
				if (null != obj2.get("caseCause") && !"".equals(obj2.get("caseCause"))) {
					archs.setCaseCause(obj2.get("caseCause").toString());// 案由
				}
				if (null != arch.getSummary() && !"".equals(arch.getSummary())) {
					archs.setSummary(arch.getSummary());
				}
				if (null != obj2.get("summary") && !"".equals(obj2.get("summary"))) {
					archs.setSummary(obj2.get("summary").toString());// 摘要
				}
				if (null != obj2.get("path") && !"".equals(obj2.get("path"))) {
					archs.setPath(obj2.get("path").toString());// 起诉日期
				}
				if (null != arch.getPath() && !"".equals(arch.getPath())) {
					archs.setPath(arch.getPath());// 路径
				}
				if (null != obj2.get("relativeCases") && !"".equals(obj2.get("relativeCases"))) {
					archs.setRelativeCases(relative(obj2.get("relativeCases").toString()));
				}
				archs.setSource(3);
				archs.setFlag(1);
				String jsonss = gson.toJson(archs);
				doc = JsonDocument.create(arch.getWholeCourtId(), JsonObject.fromJson(jsonss));
				logger.info("更新条数：" + SUM + "-----UUID为：" + arch.getWholeCourtId() + "---省：" + array[0] + "---市："
						+ array[1] + "---县/区：" + array[2]);
				bucket.upsert(doc);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			array = null;
			gson = null;
			json = null;
			archs = null;
			obj2 = null;
			doc = null;
		}
		return true;
	}

	/**
	 * 关联文书转成对象list集合
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<RelativeVO> relative(String value) {
		if (value == null && "".equals(value)) {
			return null;
		}
		List<RelativeVO> list = new ArrayList<RelativeVO>();
		Gson gson = new Gson();
		try {
			list = gson.fromJson(value.toString(), List.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} finally {
			list = null;
			gson = null;
		}
		return list;
	}

	/**
	 * 判断是否存在乱码
	 * 
	 * @param value
	 * @return
	 */
	public static boolean getErrorCode(String value) {
		if (value == null || "".equals(value)) {
			return false;
		}
		for (String val : ERCOEDING) {
			int index = value.lastIndexOf(val);
			if (index <= 0) {
				continue;
			}
			return false;
		}
		return true;
	}

	/**
	 * 提取文书类型
	 * 
	 * @param value
	 * @return
	 */
	public static String getCatalog(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		int index;
		for (String val : BOOKCLASS) {
			index = value.indexOf(val);
			if (index >= 0) {
				return val;
			}
		}
		return null;
	}

	/**
	 * 案件类型
	 * 
	 * @Description: TODO
	 * @param value
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月15日 上午11:58:35
	 */

	public static String getCatalogOld(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		// int index;
		// for (String val : LAWCASE) {
		// index = value.indexOf(val);
		// if (index >= 0) {
		// return val;
		// }
		// }
		// return null;

		Iterator iter = MPA_LAWCASE.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey().toString();
			if (value.indexOf(key) != -1) {
				return key;
			}
		}
		return null;

	}

	/**
	 * 取文书编号
	 * 
	 * @param valthml
	 * @return
	 */
	public static String getSentenceNo3(String valthml) {
		try {
			if (null == valthml || "".equals(valthml)) {
				return null;
			}
			String[] valueSipt = valthml.split("。");
			int index = 0;
			for (String val : valueSipt) {
				index = getDateIndex2(val);
				if (index >= 0) {
					valthml = val;
					break;
				}
			}
			if (index == -1) {
				return null;
			}
			index = valthml.lastIndexOf("书");
			if (index == -1) {
				return null;
			}
			valthml = valthml.substring(index + 1, valthml.length());
			index = valthml.indexOf("号");
			if (index == -1) {
				return null;
			}
			return valthml.substring(0, index + 1);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 判断字符是否非数字
	 * 
	 * @param strNum
	 * @return
	 */
	public static boolean isDigit(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	/**
	 * 根据符号+年份获取案号
	 * 
	 * @param value
	 * @return
	 */
	public static String getSplitCaseNum(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		try {
			for (String val : CAUSENUM) {
				int firstIndex = value.lastIndexOf(val);
				if (firstIndex == -1) {
					continue;
				}
				value = value.substring(firstIndex);
				int secondIndex = value.indexOf("号");
				if (secondIndex <= 0) {// 当案号中没有“号”字的时候
					String spl = null;
					String value2 = null;
					secondIndex = value.indexOf("第");
					if (secondIndex <= 0) {
						secondIndex = value.indexOf("字");
					}
					value2 = value.substring(0, secondIndex + 1);
					value = value.substring(secondIndex + 1);
					String[] split = value.split("");
					for (int i = 1; i < split.length; i++) {
						spl = split[i];
						boolean result = isDigit(spl);
						if (!result) {
							break;
						}
						value2 = value2 + spl; // 取"第"字后面的数字，一个个添加进去
					}
					return value2;
				}
				value = value.substring(0, secondIndex + 1);
				return value;
			}
		} catch (Exception e) {
			logger.error("提取案号出错" + e.getMessage());
		}
		return null;
	}

	/**
	 * 根据文书类型截取获得案号
	 * 
	 * @param value
	 * @return
	 */
	public static String getSplitCaseNum2(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		try {
			//
			for (String val : BOOKCLASS) {
				int firstIndex = value.lastIndexOf(val);
				if (firstIndex == -1) {
					continue;
				}
				firstIndex = firstIndex + val.length();
				value = value.substring(firstIndex);
				int secondIndex = value.indexOf("号");
				if (secondIndex <= 0) {// 当案号中没有“号”字的时候
					String value2 = null;
					String spl = null;
					secondIndex = value.indexOf("第");
					if (secondIndex <= 0) {
						secondIndex = value.indexOf("字");
					}
					value2 = value.substring(0, secondIndex + 1);
					value = value.substring(secondIndex + 1);
					String[] split = value.split("");

					for (int i = 1; i < split.length; i++) {
						spl = split[i];
						boolean result = isDigit(spl);
						if (!result) {
							break;
						}
						value2 = value2 + spl; // 取"第"字后面的数字，一个个添加进去
					}
					return value2;
				}
				value = value.substring(0, secondIndex + 1);
				for (String val3 : CAUSENUM3) {
					int lastIndex = value.indexOf(val3);
					if (lastIndex == -1) {
						continue;
					}
					return value;
				}
			}
		} catch (Exception e) {
			logger.error("提取案号出错" + e.getMessage());
		}
		return null;
	}

	/**
	 * 特殊处理获取案号
	 * 
	 * @param value
	 * @return
	 */
	public static String getSplitCaseNum3(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		for (String val : CAUSENUM2) {
			int firstIndex = value.lastIndexOf(val);
			if (firstIndex == -1) {
				continue;
			}
			firstIndex = firstIndex + val.length();
			value = value.substring(firstIndex);
			int secondIndex = value.indexOf("号");
			if (secondIndex <= 0) {// 当案号中没有“号”字的时候
				String value2 = null;
				String spl = null;
				secondIndex = value.indexOf("第");
				if (secondIndex <= 0) {
					secondIndex = value.indexOf("字");
				}
				value2 = value.substring(0, secondIndex + 1);
				value = value.substring(secondIndex + 1);
				String[] split = value.split("");

				for (int i = 1; i < split.length; i++) {
					spl = split[i];
					boolean result = isDigit(spl);
					if (!result) {
						break;
					}
					value2 = value2 + spl; // 取"第"字后面的数字，一个个添加进去
				}
				value2 = replaceCaseNum1(value);
				return value2;
			}
			value = value.substring(0, secondIndex + 1);
			value = replaceCaseNum1(value);
			return value;
		}

		return null;
	}

	/**
	 * 去掉案号中多余的字
	 * 
	 * @param value
	 * @return
	 */
	public static String replaceCaseNum1(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		int index1 = value.indexOf("共印");
		int index2 = value.indexOf("份");
		if (index1 > 0) {
			String value3 = value.substring(index1, index2 + 1);
			value = value.replace(value3, "");
		}
		return value;
	}

	/**
	 * 按符號切割字符-获取案号
	 * 
	 * @param value
	 * @return
	 */
	public static String getCaseNum(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		String[] split = value.split("。");
		for (int i = 0; i < split.length; i++) {
			value = getSplitCaseNum(split[i]);
			if (value != null && !"".equals(value)) {
				return value;
			}
			value = getSplitCaseNum2(split[i]);
			if (value != null && !"".equals(value)) {
				return value;
			}
			value = getSplitCaseNum3(split[i]);//
			if (value != null && !"".equals(value)) {
				return value;
			}
		}
		return null;
	}

	/**
	 * 取审结日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getConcludeDate(String date) {
		if (date == null || "".equals(date)) {
			return null;
		}
		String[] data = { "二〇", "一九", "二○", "二０", "二0", "二O", "二0", "二Ｏ", "二�", "20", "19" };
		int[] splt = { 9, 10, 11, 12 };
		String value;
		String[] datas;
		boolean result = false;
		try {
			for (int index = 0; index < data.length; index++) {
				if (date.lastIndexOf(data[index]) < 0) {
					continue;
				}
				value = date.substring(date.lastIndexOf(data[index]));
				for (int index2 = 0; index2 < splt.length; index2++) {
					datas = value.split("");
					String da = datas[splt[index2]];
					if ("日".equals(da)) {
						value = value.substring(0, splt[index2]);
						result = true;
						;
						break;
					}
				}
				if (result)
					return value == null ? null : value.replaceAll("�", "0");
			}
		} catch (Exception e) {
			logger.error("取审结日期出错:" + e.getMessage());
		} finally {
			datas = null;
			splt = null;
			data = null;
		}
		return null;
	}

	/**
	 * 提取判决日期
	 * 
	 * @param value
	 * @return
	 */
	public static String getTheVerdictData(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		try {
			String text = null;
			int index = 0;
			int index2 = 0;
			for (String key : THEVERDICT) {
				index = value.lastIndexOf(key);
				if (index >= 0) {
					index2 = value.indexOf("审判长");
					if (index2 < index) {
						index2 = value.length();
					}
					text = value.substring(index, index2);
					index = text.lastIndexOf("公告");
					if (index >= 0) {
						text = text.substring(0, index);
					}
					text = text.substring(0, text.lastIndexOf("。"));
					if (null != text)
						return text;
				}
			}

		} catch (Exception e) {
			logger.error("提取判决结果出错:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 去掉无用字符
	 * 
	 * @param value
	 * @return
	 */
	public static String getReplaceAll(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		StringBuffer sb = null;
		if (value != null && !"".equals(value)) {
			value = value.replaceAll(",", "，");
			value = value.replaceAll("�,o,O", "〇");
			value = value.replaceAll("[×,X,Ｘ,x,╳,＊,\\*]", "某");
			value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”,|,:,<,/>,</,>,-,+,=,},{,#,\",',-,%,^,*]", ""); // a-z,A-Z,没有去掉字母
			// value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”,・
			// ,:,<,/>,</,>,a-z,A-Z,-,+,=,},{,.,#,\",',-,%,^,*]",""); //去掉所有字母
			value = getSpecialStringALL(value);
			value = value.trim();
			sb = new StringBuffer();
			sb.append(value);
		}
		return sb == null ? "" : sb.toString();
	}

	/**
	 * 去掉特殊字符
	 * 
	 * @param value
	 * @return
	 */
	public static String getSpecialStringALL(String value) {
		if (null == value || "".equals(value)) {
			return null;
		}
		char[] chs = value.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (char c : chs) {
			if (((int) c) != 12288 && ((int) c) != 160) {
				sb.append(String.valueOf(c));
			}
		}
		return sb.toString();
	}

	/**
	 * 根据关键字提取人
	 * 
	 * @param map
	 * @param status
	 * @return
	 */
	public static String getKeyName(Map<String, List<String>> map, int status) {
		if (map == null || "".equals(map)) {
			return null;
		}
		String[] keys = null;
		if (status == 1)
			keys = CourtUtils.PLAINTIFF;
		else
			keys = CourtUtils.DEFENDANT;
		Set<String> setNames = null;
		List<String> list = null;
		String[] vals = null;
		for (String key : keys) {
			list = map.get(key);
			if (null != list) {
				for (String val : list) {
					if (null == setNames) {
						setNames = new HashSet<String>();
					}
					if (val.indexOf("、") >= 0) {
						vals = val.split("、");
						for (String va : vals) {
							setNames.add(va);
						}
					} else
						setNames.add(val);
				}
			}
		}
		if (setNames == null || setNames.size() == 0) {
			return null;
		}
		StringBuffer sb = null;
		for (String val : setNames) {
			if (sb == null)
				sb = new StringBuffer(val);
			else
				sb.append("、").append(val);
		}
		return sb == null ? null : sb.toString();
	}

	/**
	 * 清理法院名
	 * 
	 * @param value
	 * @return
	 */
	public static String replaceCourtName(String value) {
		if (value != null && !"".equals(value)) {
			for (String val : DATESTATUS) {
				value = value.replaceAll(val, "");
				continue;
			}
			value = value.replaceAll("[（,）,(,),【,】,{,},<,>,★,?,0-9,a-z,A-Z,!,！,#,$,%,&,*,/,\",／,|,、]", "");
			value = value.replaceAll("[欢迎,登陆,登录,编辑,录入,年月日,首页,发表日期,发布日期,裁判文书,次数次,打印此页,关闭,下载,查看次数,字号,双击,屏幕滚动]", "");
			int index = 0;
			for (String val2 : PROVINCE) {
				index = value.lastIndexOf(val2);
				if (index > 0) {
					value = value.substring(index, value.length());
				}
				continue;
			}
			return value;
		}
		return value;
	}

	/**
	 * 法院提取--提取法院名称方法1
	 * 
	 * @param value
	 * @return
	 */
	public static String getCourtName(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		value = getDataAll(value);
		if (value == null || "".equals(value)) {
			return null;
		}
		String[] valuesplit = value.split("。");
		String courtName = null;
		int index;
		try {
			for (String val : valuesplit) {
				index = val.lastIndexOf("书");
				if (index == -1) {
					index = val.lastIndexOf("号");
					if (index == -1) {
						index = val.lastIndexOf("第");
					}
				}
				courtName = val.substring(0, index + 1);
				courtName = courtName.replaceAll("[0-9,\\-,:,_,：]", "");
				if (null == courtName && "".equals(courtName)) {
					continue;
				}
				index = courtName.lastIndexOf("法院");
				if (index == -1) {
					continue;
				}
				courtName = courtName.substring(0, index + 2);
				// courtName = replaceAll(courtName);
				if (courtName == null && "".equals(courtName)) {
					continue;
				}
				if (courtName.length() >= 4) {
					return courtName;
				}
			}
		} catch (Exception e) {
			logger.error("提取法院名称出错:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 法院提取--提取法院名称方法2
	 * 
	 * @param value
	 * @return
	 */
	public static String getAtherthe(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		String gatherthe = null;
		String[] gatherthes = null;
		StringBuffer sb = null;
		String[] valuesplit = value.split("。");
		try {
			for (String val : valuesplit) {
				value = val;
				break;
			}
			gatherthe = value.substring(0, value.length());
			int index = value.indexOf("书");
			if (index == -1) {
				index = value.indexOf("号");
			}
			gatherthe = gatherthe.substring(0, gatherthe.indexOf(index) + 1);
			gatherthes = gatherthe.split("[0-9,\\-,:,_,：]");
			sb = new StringBuffer();
			for (String val : gatherthes) {
				if (null != val)
					sb.append(val);
			}
			if (sb.toString().lastIndexOf("}") >= 0) {
				gatherthe = sb.toString().substring(sb.toString().lastIndexOf("}") + 1, sb.toString().length());
				sb = new StringBuffer(gatherthe);
			}
			gatherthe = getAthertheReplace(sb.toString().substring(0, sb.toString().lastIndexOf("法院") + 2));
			// gatherthe = replaceAll(gatherthe);
			if (gatherthe != null && !"".equals(gatherthes)) {
				if (gatherthe.length() >= 4) {
					return gatherthe;
				}
			}
		} catch (Exception e) {
			// logger.error("提取法院名称出错:" + e.getMessage());
		} finally {
			sb = null;
			gatherthes = null;
		}
		return null;
	}

	/**
	 * 法院提取--过滤法院
	 * 
	 * @param value
	 * @return
	 */
	public static String getAthertheReplace(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		int index = value.indexOf("法院");
		int index1 = value.lastIndexOf("法院");
		if (index != index1) {
			value = value.substring(index + 2, index1 + 2);
		}
		return value;
	}

	/**
	 * 法院提取--取第一段
	 * 
	 * @param value
	 * @return
	 */
	public static int getDateIndex2(String value) {
		if (value == null || "".equals(value)) {
			return -1;
		}
		int index = 0;
		for (String date : DATESTATUS) {
			index = value.indexOf(date);
			if (index >= 0) {
				return index + date.length();
			}
		}
		return -1;
	}

	/**
	 * 取第一段
	 * 
	 * @param value
	 * @return
	 */
	public static int getDateIndex(String value) {
		if (value == null || "".equals(value)) {
			return -1;
		}
		int index = 0;
		value = value.substring(0, value.indexOf("。"));
		for (String date : DATESTATUS) {
			index = value.indexOf(date);
			if (index >= 0) {
				return index + date.length();
			}
		}
		return -1;
	}

	/**
	 * 提取案由
	 * 
	 * @param value
	 * @return
	 */
	public static String StringCause(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		value = getDataAll(value);
		try {
			int indx = value.lastIndexOf("书记员");
			if (indx > 0) {
				value = value.substring(0, indx);
			}
			int index = 0;
			String firTxt = null;
			String lastxt = null;
			int count = 0;
			for (String val : CAUSE) {
				index = value.indexOf(val);
				if (index >= 0) {
					indx = value.lastIndexOf("公告");
					if (indx > 0) {
						if (index < indx) {
							return null;
						}
					}
					if (val.equals("驳回申诉通知") || val.equals("赔偿决定书")) {
						int index2 = getDateIndex(value);
						if (index == -1) {
							return null;
						}
						firTxt = value.substring(index2, value.length());
						firTxt = firTxt.substring(firTxt.indexOf("号") + 1, firTxt.length());
						value = firTxt;
						if (val.equals("赔偿决定书"))
							val = "国家赔偿";
					}
					index = value.indexOf(val);
					if (index < 0)
						index = value.indexOf("国家赔偿");
					firTxt = value.substring(0, index);
					count = firTxt.lastIndexOf("。");
					if (count == -1) {
						firTxt.lastIndexOf("号");
					}
					firTxt = firTxt.substring(count + 1, firTxt.length());
					index = value.indexOf(val);
					if (index < 0) {
						if (val.equals("驳回申诉通知"))
							val = "国家赔偿";
					}
					lastxt = value.substring(value.indexOf(val), value.length());
					lastxt = lastxt.substring(lastxt.indexOf(val), lastxt.indexOf("。") + 1);
					return new StringBuffer(firTxt).append(lastxt).toString();
				}
			}

		} catch (Exception e) {
			logger.error("提取案由出错:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 提取全文
	 * 
	 * @param value
	 * @return
	 */
	public static String getDataAll(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		int index = 0;
		index = value.indexOf("。");
		if (index == -1) {
			return null;
		}
		String value2 = value.substring(0, index);
		for (String date : DATESTATUS) {
			index = value2.indexOf(date);
			if (index >= 0) {
				value = value.substring(index + date.length(), value.length());
				return value;
			}
		}
		return value;
	}

	/**
	 * 正确的编码读取内容 已停用
	 * 
	 * @param fi
	 * @return
	 * @throws Exception
	 */
	public static String getAllcharset(File fi) throws Exception {
		Document doc;
		String html = "";
		for (String val : charset) {
			doc = Jsoup.parse(fi, val);
			html = getDataAll(doc.text());
			boolean Garbled = getErrorCode(html);// 判断编码是否错误
			if (Garbled == false) {
				continue;
			}
			return html;
		}
		return null;
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}

	public static Bucket connectionCouchBaseLocal() {
		// 连接指定的桶
		return cluster2.openBucket("court_New", 1, TimeUnit.MINUTES);
	}

	/**
	 * 提取html中的vo
	 * 
	 * @param file
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static WholeCourtVO getVoFromHtml(File file) {

		Map<String, List<String>> list = null;
		int i = 0;
		Document doc = null;

		// listarchs = new ArrayList<WholeCourtVO>();

		String variable = null;
		String html = null;
		WholeCourtVO arch = null;
		arch = new WholeCourtVO();
		String suffix = file.getName();
		suffix = suffix.substring(suffix.indexOf(".") + 1, suffix.length());
		suffix = MAPS.get(suffix);
		if (null == suffix) {
			return null;
		}
		arch.setPath(suffix);

		for (String val : charset) {// 匹配不同编码格式
			try {
				doc = Jsoup.parse(file, val);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			html = doc.body().text();// 取页面body标签中所有内容
			boolean Garbled = getErrorCode(html);// 判断编码是否错误
			if (Garbled == false) {
				logger.info(   "页面编码错误！！！");
				i++;
				if (i == 5) {
					html = null;
				}
				continue;
			}
			i = 0;

			break;
		}

		if (html == null || "".equals(html)) {
			logger.info("内容为空的HTML页面：" + file.getPath());
			return null;
		}

		if (html.indexOf("给力加载") != -1 || html.indexOf("加载中") != -1) {
			logger.info("加载的HTML页面：" + file.getPath());
			return null;
		}

		System.out.println("----->文件路径： " + file.getPath());

		// 提取发布时间
		// variable = getCourPublishDateOld(doc, 1);
		// variable = HtmlUtils.getContentFromXpath(doc,
		// "//div[@id='cc']/table/tbody/tr[2]/td/div/font[2]");

		// 提取发布日期
		// variable = DateUtils.getDateFromStringYYYMMDD(variable);
		// arch.setPublishDate(variable);
		// System.out.println("发布日期："+variable);

		// variable = getReplaceAll(doc.title());
		// if (variable != null && !"".equals(variable)) {
		// arch.setTitle(variable.trim()); // 标题 √
		// }
		// 2015-01-28
		// 2015-01-28
		// 2015-02-16

		String writType = null;
		// variable = getCourtTypeOldS1(doc, 1);// 文书类型 √
		writType = HtmlUtils.getContentFromXpath(doc, "//html/body/div/p[2]/span", 1);// 文书类型
																						// √
		// System.out.println("文书类型： "+variable);
		if (!"".equals(writType) && writType != null) {
			arch.setWritType(writType);
		} else {
			arch.setWritType(getCatalog(html)); // 文书类型 √
		}

		variable = null;
		// if ("".equals(variable) || variable == null) {
		// arch.setCatalog(getCatalog(html)); // 文书类型 √
		// }

		// 案件类型
		String catalog = null;
		// 文件的路径是否包含 民事
		Iterator iter = MPA_LAWCASE.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey().toString();
			if (file.getPath().indexOf(key) != -1) {
				// catalog = entry.getValue().toString();
				// arch.setCatalog(getCatalog(catalog));

				arch.setCatalog(key);
				break;
			}
		}

		if (StringUtils.isNull(catalog)) {
			// catalog = HtmlUtils.getContentFromXpath(doc,
			// "//html/body/div/p[2]/span",1);

			// System.out.println("案件类型："+catalog);
			// arch.setCatalog(catalog);
			if (!StringUtils.isNull(writType)) {
				iter = MPA_LAWCASE.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = entry.getKey().toString();
					if (writType.indexOf(key) != -1) {
						// catalog = entry.getValue().toString();
						// arch.setCatalog(getCatalog(catalog));
						arch.setCatalog(key);
						break;
					}
				}
			}
		}
		if (StringUtils.isNull(arch.getCatalog())) {
			catalog = getCatalogOld(html);
			arch.setCatalog(catalog);
		}

		// 提取当事人信息
		String clients = null;
		WholeCourtVO vo_clients = null;
		
		if (file.getPath().indexOf("民事") != -1 || file.getPath().indexOf("ms") != -1
				|| (arch.getCatalog() != null && arch.getCatalog().indexOf("民事") != -1)) {
			vo_clients = getclientOldSZheJGKW(doc, 1);
		} else if (file.getPath().indexOf("刑事") != -1 || file.getPath().indexOf("xs") != -1
				|| (arch.getCatalog() != null && arch.getCatalog().indexOf("刑事") != -1)) {
			vo_clients = getclientOldSZheJGKW(doc, 2);
		} else if (file.getPath().indexOf("执行") != -1
				|| (arch.getCatalog() != null && arch.getCatalog().indexOf("执行") != -1)) {
			vo_clients = getclientOldSZheJGKW(doc, 3);
		} else {
			vo_clients = getclientOldSZheJGKW(doc, 4);
		}
		
		arch.setClients(vo_clients.getClients());
		// if (StringUtils.isNull(clients)) {
		// logger.error("无当事人： " + file.getPath());
		// }
		// 律师
		arch.setLawOfficeP(vo_clients.getLawOfficeP());
		arch.setLawOfficeD(vo_clients.getLawOfficeD());

		// System.out.println("当事人： " + clients);

		// String client=null;
		// arch.setClient(client);

		html = getReplaceAll(html).trim();

		if (null != arch.getClients()) {
			try {
				String res[] = CourtUtils.getSplitClient(arch.getClients());
				arch.setPlaintiff(res[0]); // 原告相关人 √
				arch.setDefendant(res[1]); // 被告相关人 √
			} catch (Exception e) {
				logger.error("-----------------------------------处理当事人异常:" + arch.getClients());
			}
		} else {
			list = ExtractthepeopleText.getPersonName(html);
			arch.setPlaintiff(getKeyName(list, 1)); // 原告相关人 √ 正方
			arch.setDefendant(getKeyName(list, 2)); // 被告相关人 √ 反方
		}

		/*
		 * list = ExtractthepeopleText.getPersonName(html);
		 * arch.setPlaintiff(getKeyName(list, 1)); // 原告相关人 √
		 * arch.setDefendant(getKeyName(list, 2)); // 被告相关人 √
		 */
		// variable = getCourtName(html);

		// variable=getCourtNameFromFile(file);

		// variable = getCourtNameOldS2(doc, 1);
		variable = HtmlUtils.getContentFromXpath(doc, "//html/body/div/p[1]/span", 1);
		if (variable != null && !"".equals(variable)) {
			arch.setCourtName(variable); // 法院 √
		}

		if (variable == null || "".equals(variable)) {
			arch.setCourtName(getAtherthe(html)); // 法院 √
		}

		// arch.setCaseCause(StringCause(html)); // 案由 √

//		String case1 = getCaseFromStr(doc.body().text());
//		arch.setCaseCause(case1); // 案由 √
		// System.out.println("案由：" + case1);
		
		
		WholeCourtVO voCase =CourtUtils. getCaseFromStr_VO(doc.body().text());
		arch.setCaseCause(voCase.getCaseCause());
		arch.setType1(voCase.getType1());
		arch.setType2(voCase.getType2());
		arch.setType3(voCase.getType3());
		arch.setType4(voCase.getType4());
		arch.setType5(voCase.getType5());
		
		

		// 提取标题

		// variable = HtmlUtils.getContent(doc, "div#wsTitle");

		// variable = HtmlUtils.getContentFromXpath(doc,
		// "//html/body/div/p[1]/span",1);
		// //*[@id="border_in"]/div/ul/li[1]
		//
		// // variable=getCourTittleOld(doc, 1);
		// arch.setTitle(variable); // 标题 √

		variable = null;

		arch.setApproval(getTheVerdictData(html)); // 判决结果 √

		// variable = getCaseNum(html);
		// variable = getCourtcaseNumOldS1(doc, 1);
		variable = HtmlUtils.getContentFromXpath(doc, "//html/body/div/p[3]/span", 1);// 案号
																						// √
		// System.out.println("案号： "+variable);
		if (!"".equals(variable) && variable != null) {
			arch.setCaseNum(variable); // 案号 √
		}
		if ("".equals(variable) || variable == null) {
			variable = getCaseNum(html);
			arch.setCaseNum(variable); // 案号 √
		}
		if ("".equals(variable) || variable == null) {
			arch.setCaseNum(getSentenceNo3(html)); // 案号 √
		}

		// 审判员等
		WholeCourtVO vo = getJudgeOldSZheJGKW(doc, 1);
		variable = vo.getJudges();
		// System.out.println("最后结果结果审判员等： " + variable);
		// System.out.println("最后审结日期： " + vo.getApprovalDate());
		if (!"".equals(variable) && variable != null) {
			arch.setJudges(variable); // 审判员等 √
		}

		if (!StringUtils.isNull(vo.getApprovalDate())) {
			arch.setApprovalDate(vo.getApprovalDate());
		} else {
			arch.setApprovalDate(getConcludeDate(html)); // 审结日期 √
		}
		
		//法律条文
		String all = doc.body().text();
		String laws = null;

		if (all.indexOf("本院认为") != -1) {
			String ss =CourtUtils. getLaws(all.substring(all.indexOf("本院认为")));
			laws = ss;

		} else {
			String ss = CourtUtils.getLaws(all);
			laws = ss;
		}
		arch.setLaws(laws);
		System.out.println("---------------------------------------");

		// 此处用的法院名+文书类型+案号当标题：
		String title = (arch.getCourtName() != null ? arch.getCourtName() + " " : "")
				+ (arch.getWritType() != null ? arch.getWritType() + " " : "")
				+ (arch.getCaseNum() != null ? arch.getCaseNum() : "");
		arch.setTitle(title); // 标题 √

		// System.out.println("网址:" + file.getPath());
		// System.out.println("标题:" + arch.getTitle());
		// System.out.println("<<------------------------------------------------------>>");
		// System.out.println("所有内容：" + html);
		// System.out.println("原告相关人:" + arch.getPlaintiff());
		// System.out.println("被告相关人:" + arch.getDefendant());
		// System.out.println("审结日期:" + arch.getApprovalDate());
		// System.out.println("判决结果:" + arch.getApproval());
		// System.out.println("案号:" + arch.getCaseNum());

		System.out.println("标题:" + arch.getTitle());
		System.out.println("案号:" + arch.getCaseNum());
		System.out.println("法院:" + arch.getCourtName());
		System.out.println("案由:" + arch.getCaseCause());
		System.out.println("案由:" + arch.getType1() + "--- " + arch.getType2() + "--- " + arch.getType3() + "--- "
				+ arch.getType4() + "--- " + arch.getType5());
		
		System.out.println("条文:" + arch.getLaws());

		System.out.println("案件类型:" + arch.getCatalog());
		System.out.println("文书类型:" + arch.getWritType());
		System.out.println("发布日期： " + arch.getPublishDate());
		System.out.println("审结日期： " + arch.getApprovalDate());
		System.out.println("最后结果结果审判员等： " + arch.getJudges());
		System.out.println("当事人:" + arch.getClients());

		System.out.println("律师事务所 原告:" + arch.getLawOfficeP());
		System.out.println("律师事务所 被告:" + arch.getLawOfficeD());

		// System.out.println("<<------------------------------------------------------>>");
		arch.setWholeCourtId(file.getName().substring(0, file.getName().lastIndexOf(".")));

		System.out.println("<<------------------------------------------------------>>");
		return arch;

	}

	/**
	 * 得到旧版本的标题
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param flag
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月14日 下午5:37:27
	 */
	public static String getCourTittleOld(Document doc, int flag) {
		String reslut = null;
		if (doc == null) {
			return null;
		}

		String xpath = "//*[@id='wsTitle']";

		// //*[@id="wsTitle"]
		// <td><div id="wsTitle">王某甲危险驾驶罪一审刑事判决书</div></td>
		// String xpath = "//*[@id='wsTime']/span";

		// String xpath = "//div";
		// String xpath = "//div[@id='DocArea']/div[2]";
		// String
		// xpath="//div[@id='post_list']/div[./div/div/span[@class='article_view']/a/num()>1000]/div/h3/allText()";String
		// doc = "...";

		JXDocument jxDocument = new JXDocument(doc);
		try {

			System.out.println(jxDocument.toString());
			List<Object> rs = jxDocument.sel(xpath);
			System.out.println(rs.size());
			for (Object o : rs) {
				if (o instanceof Element) {
					// int index = ((Element) o).siblingIndex();
					reslut = ((Element) o).text();
					reslut = reslut.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "").replace(" ", "");
					System.out.println(reslut);
					// System.out.println(index);
					return reslut;

				}
				// System.out.println(o.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reslut;
	}

	/**
	 * 提取旧版本高院的发布时间 提交时间：2015-01-28
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param flag
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月14日 下午5:47:59
	 */
	public static String getCourPublishDateOld(Document doc, int flag) {
		String reslut = null;
		if (doc == null) {
			return null;
		}
		String xpath = "//*[@id='wsTime']/span";
		JXDocument jxDocument = new JXDocument(doc);
		try {
			List<Object> rs = jxDocument.sel(xpath);
			for (Object o : rs) {
				if (o instanceof Element) {
					// int index = ((Element) o).siblingIndex();
					reslut = ((Element) o).text();
					reslut = reslut.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "").replace(" ", "");

					// reslut=DateUtils.getDateFromStringYYYMMDD(reslut);
					// System.out.println(reslut);
					// System.out.println(index);
					return reslut;

				}
				// System.out.println(o.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reslut;
	}

	/**
	 * 得到旧版本的法院名
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param flag
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月14日 上午10:26:22
	 */
	public static String getCourtNameOldS2(Document doc, int flag) {

		String reslut = null;
		if (doc == null) {
			return null;
		}
		String xpath = "//div[@id='cc']/table/tbody/tr[4]/td/b/font";
		JXDocument jxDocument = new JXDocument(doc);
		try {
			List<Object> rs = jxDocument.sel(xpath);
			for (Object o : rs) {
				if (o instanceof Element) {
					// int index = ((Element) o).siblingIndex();
					reslut = ((Element) o).text();
					reslut = reslut.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "").replace(" ", "");
					// System.out.println(reslut);
					// System.out.println(index);

					if (reslut.length() < 4) {
						return null;
					} else {
						if ("院".equals(reslut.substring(reslut.length() - 1))) {// 最后一个字是XXX法院
							// System.out.println("法院： " + reslut);
							return reslut;
						}
					}

				}
			}
		} catch (XpathSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到旧版本的文书类型
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param flag
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月14日 下午2:19:41
	 */
	public static String getCourtTypeOldS1(Document doc, int flag) {

		String reslut = null;
		if (doc == null) {
			return null;
		}

		String xpath = "//div[@id='border_in']/div/ul/li[4]";

		JXDocument jxDocument = new JXDocument(doc);
		try {
			List<Object> rs = jxDocument.sel(xpath);
			for (Object o : rs) {
				if (o instanceof Element) {
					// int index = ((Element) o).siblingIndex();
					reslut = ((Element) o).text();
					reslut = reslut.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "").replace(" ", "");

					// System.out.println(index);

				}
				// System.out.println(o.toString());
			}
		} catch (XpathSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reslut;
	}

	/**
	 * 得到旧版本的案由
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param flag
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月14日 下午2:21:19
	 */
	public static String getCourtcaseNumOldS1(Document doc, int flag) {

		String reslut = null;
		if (doc == null) {
			return null;
		}

		String xpath = "//div[@id='border_in']/div/ul/li[5]";
		JXDocument jxDocument = new JXDocument(doc);
		try {
			List<Object> rs = jxDocument.sel(xpath);
			for (Object o : rs) {
				if (o instanceof Element) {
					int index = ((Element) o).siblingIndex();
					reslut = ((Element) o).text();
					reslut = reslut.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "").replace(" ", "");
					// System.out.println(reslut);
					// System.out.println(index);
				}
				// System.out.println(o.toString());
			}
		} catch (XpathSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reslut;
	}

	/**
	 * 得到当事人信息
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param flag
	 *            1 民事 2 刑事 3 执行
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月8日 下午4:11:33
	 */
	@SuppressWarnings("rawtypes")
	public static String getclient(Document doc, int flag) {
		if (doc == null) {
			return null;
		}
		TreeSet<String> ts = new TreeSet<String>();

		int fa = 0;// 判断第一个a标签前是不是出现过当事人信息，如果大于0说明出现
		int flag_end = 0; // 判断是不是到了非当事人部分
		Elements all = doc.body().getAllElements();

		// writerString(fwUn2, (case1!=null?case1:"NULL") + "#" + "#" +
		// file.getName());

		for (Element element_all : all) {
			String tagName = element_all.tagName();
			// System.out.println(tagName);

			if ("div".equals(tagName)) {
				Attributes as = element_all.attributes();
				for (Attribute attribute : as) {
					// System.out.println(attribute.getKey());
					// System.out.println( attribute.getValue() );

					if ("style".equals(attribute.getKey()) && !StringUtils.isNull(attribute.getValue())

					&& ("LINE-HEIGHT: 25pt; TEXT-INDENT: 30pt; MARGIN: 0.5pt 0cm;FONT-FAMILY: 宋体; FONT-SIZE: 15pt;"
							.equals(attribute.getValue())
							|| "margin: 0.5pt 0cm; line-height: 25pt; text-indent: 30pt; font-family: 宋体; font-size: 15pt;"
									.equals(attribute.getValue())
							|| "line-height: 25pt; text-indent: 30pt; margin: 0.5pt 0cm; font-family: 宋体; font-size: 15pt"
									.equals(attribute.getValue())
							|| "font-size: 15pt; margin: 0.5pt 0cm; text-indent: 30pt; line-height: 25pt; font-family: 宋体"
									.equals(attribute.getValue())

					)

					) {

						// System.out.println(element_all.text());

						fa++;

						String temp1 = element_all.text();
						// String temp2=getCase(temp1);
						// System.out.println(temp2);

						if (StringUtils.isNull(temp1)) {
							continue;
						}
						temp1 = temp1.replace(":", "：").replace(",", "，").replace(";", "：");

						if (!StringUtils.isNull(temp1)) {

							if (flag == 1) {
								for (String val1 : CLIENT_END) {
									if (temp1.indexOf(val1) != -1) {
										flag_end = 1;

										// writerString(fwUn3, val1 + "#" +
										// temp1 +
										// "#" + SUM + "#" + file.getName());
										break;
									}
								}
							}
							if (flag == 2) {
								for (String val1 : CLIENT_END_XinShi) {
									if (temp1.indexOf(val1) != -1) {
										flag_end = 1;

										// writerString(fwUn3, val1 + "#" +
										// temp1 +
										// "#" + SUM + "#" + file.getName());
										break;
									}
								}
							}
							if (flag == 3) {// 执行
								for (String val1 : CLIENT_END) {
									if (temp1.indexOf(val1) != -1) {
										flag_end = 1;

										// writerString(fwUn3, val1 + "#" +
										// temp1 +
										// "#" + SUM + "#" + file.getName());
										break;
									}
								}
							}
						}

						if (flag_end == 1) {
							break;
						}

						// 先得到要洗内容 如 原告王又阊（曾用名：王有昌），男，1967年12月15日出生。 先得到
						// " 原告王又阊（曾用名：王有昌），"
						// String[] cArray = new String[] { "，", "。" };
						// for (int ii = 0; ii < cArray.length; ii++) {
						// if (temp1.indexOf(cArray[i].toString()) !=
						// -1) {
						// temp1 = temp1.substring(0,
						// temp1.indexOf(cArray[i].toString()));
						// if (temp1.indexOf("（") != -1) {//规避
						// 委托代理人董艳丽（特别授权，公司员工）。
						// temp1 = temp1.substring(0,
						// temp1.indexOf("（"));
						// }
						// break;
						// }
						// }
						for (ClientVO clvo : CourtData.mapClientList) {
							// System.out.println("keyV= " +
							// entry.getKey() + " and
							// value= " + entry.getValue());
							if (!StringUtils.isNull(clvo.getClientFrom())) {

								String key = clvo.getClientFrom();
								temp1=temp1.replace("(", "（").replace(")", "）");
								if (temp1.indexOf(key) != -1) {
									// 得到前缀后的内容
									String cl = temp1.substring(temp1.indexOf(key) + key.length());

									if (key.equals("执行机关")) {
										if (cl.indexOf("于") != -1) {
											cl = cl.substring(0, cl.indexOf("于"));
										} else if (cl.indexOf("以") != -1) {
											cl = cl.substring(0, cl.indexOf("以"));
										} else if (cl.indexOf("提出") != -1) {
											cl = cl.substring(0, cl.indexOf("提出"));
										}
									}

									String[] strs1 = CourtUtils.splitClient(cl);
									// String[] strs =
									// cl.split("[；,。,：]");
									if (null != strs1) {
										for (String s : strs1) {
											// System.out
											// .println(clvo.getClientType() +
											// "#" + key + "#" + s + ";");
											// writerString(fwUn2,
											// clvo.getClientType() + "#" + key
											// + "#" + s + "#"
											// + SUM + "#" + file.getName());

											ts.add(clvo.getClientType() + "#" + s);
											// break;
										}
									}

									break;
								}
							}
						}

						// String resultClient = sbfClient.toString();
						// if (resultClient.indexOf(";") != -1) {
						// resultClient = resultClient.substring(0,
						// resultClient.length() - 1);
						// }

						// System.out.println("--------->"+resultClient);

						// writerString(fwUn, temp2+"|"+file.getPath());
						// writerString(fwUn2, resultClient );

						// countNum++;

					}
					if (fa > 20) {
						flag_end = 1;
						break;
					}
				}
			}

			if (flag_end == 1) {
				break;
			}

			if (fa > 0) {
				// 第一次遇到div 以外的标签就退出
				// if ("a".equals(tagName)) {
				// break;
				// }
			}
		}

		// 遍历TreeSet
		StringBuffer sbfClient = new StringBuffer();
		String resultClient = null;
		if (ts.size() > 0) {
			Iterator it = ts.iterator();
			while (it.hasNext()) {
				String ss = it.next().toString();
				sbfClient.append(ss + ";");
				// System.out.println("结果： " + ss + ",");
			}
			resultClient = sbfClient.toString();
			if (resultClient.indexOf(";") != -1) {
				resultClient = resultClient.substring(0, resultClient.length() - 1);
				System.out.println("最后结果： " + resultClient);
			}
		}

		return resultClient;

	}

	/**
	 * 得到结束位的审判员等信息
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param flag
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月14日 下午3:03:54
	 */
	public static WholeCourtVO getJudgeOldSZheJGKW(Document doc, int flag) {

		String reslut = null;
		if (doc == null) {
			return null;
		}

		WholeCourtVO vo = new WholeCourtVO();
		TreeSet<String> ts = new TreeSet<String>();

		int fa = 0;// 判断第一个a标签前是不是出现过当事人信息，如果大于0说明出现
		int flag_end = 0; // 判断是不是到了非当事人部分
		Elements all = doc.body().getAllElements();

		// writerString(fwUn2, (case1!=null?case1:"NULL") + "#" + "#" +
		// file.getName());

		// String xpath = "//*[@id='DocArea']/div[position()>3]";
		// //*[@id="cc"]/table/tbody/tr[7]/td/div/p[20]
		String xpath = "//html/body/div/p[position()>3]/span";

		// *[@id="border_in"]/div/div/p[34]

		JXDocument jxDocument = new JXDocument(doc);
		try {
			List<Object> rs = jxDocument.sel(xpath);
			for (Object o : rs) {
				if (o instanceof Element) {

					int index = ((Element) o).siblingIndex();
					String temp2 = ((Element) o).text();

					if (StringUtils.isNull(temp2)) {
						continue;
					}
					String res = "";
					String str1 = null;
					temp2 = temp2.replace(" ", "").replace("　", "");
					temp2 = temp2.replace(" ", "").replaceAll("[\n,\t,\r,\\s,&nbsp;]", "");

					String[] data = { "二〇", "二〇", "一九", "二○", "二０", "二0", "二O", "二0", "二Ｏ", "二�" };

					if (temp2.indexOf("年") != -1 && temp2.indexOf("月") != -1 && temp2.indexOf("日") != -1) {
						for (int i = 0; i < data.length; i++) {
							if (temp2.indexOf(data[i]) != -1) {

								vo.setApprovalDate(DateUtils.toYMDOfChaStr_ESZZ2(DateUtils.getReplaceAllDate(temp2)));
								break;
							}
						}
						if (vo.getApprovalDate() != null) {
							continue;
						}
					}

					for (String judge : CourtUtils.JUDGES) {

						if (temp2.indexOf(judge) != -1) {
							str1 = temp2.substring(temp2.indexOf(judge) + judge.length());
							if (StringUtils.isNull(str1)) {
								continue;
							}

							String strs[] = str1.split("、");

							// String[]strs1 = new String[]{};
							for (int i = 0; i < strs.length; i++) {

								if (StringUtils.isNull(strs[i])) {
									continue;
								} else {
									if (strs[i].length() < 2 || strs[i].length() > 12) {
										// 人名过长
										continue;
									}
								}

								res += judge + "#" + strs[i] + ";";
								ts.add(judge + "#" + strs[i]);
							}
							break;

						}
					}
				}
				// System.out.println(o.toString());
			}
		} catch (XpathSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 遍历TreeSet
		StringBuffer sbfClient = new StringBuffer();
		String resultClient = null;
		if (ts.size() > 0) {
			Iterator it = ts.iterator();
			while (it.hasNext()) {
				String ss = it.next().toString();
				sbfClient.append(ss + ";");
				// System.out.println("结果审判员等： " + ss + ",");
			}
			resultClient = sbfClient.toString();
			if (resultClient.indexOf(";") != -1) {
				resultClient = resultClient.substring(0, resultClient.length() - 1);
				// System.out.println("最后结果结果审判员等： " + resultClient);
			}
		}

		vo.setJudges(resultClient);

		return vo;

	}

	/**
	 * 得到浙江公开网当事人信息
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param flag
	 *            1 民事 2 刑事 3 执行
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月8日 下午4:11:33
	 */
	
	public static WholeCourtVO getclientOldSZheJGKW(Document doc, int flag) {
		WholeCourtVO vo = new WholeCourtVO();
		
		String reslut = null;
		if (doc == null) {
			return null;
		}
		TreeSet<String> ts = new TreeSet<String>();

		int fa = 0;// 判断第一个a标签前是不是出现过当事人信息，如果大于0说明出现
		int flag_end = 0; // 判断是不是到了非当事人部分
		Elements all = doc.body().getAllElements();

		
		String lawOfficeP = "";
		String lawOfficeD = "";
		int flag_lawyer_p_d = 0;// 1 原告律师 2 被告律师
		
		
		// writerString(fwUn2, (case1!=null?case1:"NULL") + "#" + "#" +
		// file.getName());

		// String xpath = "//*[@id='DocArea']/div[position()>3]";
		// //*[@id="cc"]/table/tbody/tr[7]/td/div/p[1]

		String xpath = "//html/body/div/p[position()>0]/span";
		JXDocument jxDocument = new JXDocument(doc);
		try {
			List<Object> rs = null;
			rs = jxDocument.sel(xpath);
			for (Object o : rs) {
				if (o instanceof Element) {

					int index = ((Element) o).siblingIndex();
					String temp1 = ((Element) o).text();

					fa++;

					// String temp2=getCase(temp1);
					// System.out.println(temp2);

					if (StringUtils.isNull(temp1)) {
						continue;
					}
					temp1 = temp1.replace(":", "：").replace(",", "，").replace(";", "：");

					if (!StringUtils.isNull(temp1)) {

						if (flag == 1) {
							for (String val1 : CLIENT_END) {
								if (temp1.indexOf(val1) != -1) {
									flag_end = 1;

									// writerString(fwUn3, val1 + "#" +
									// temp1 +
									// "#" + SUM + "#" + file.getName());
									break;
								}
							}
						}
						if (flag == 2) {
							for (String val1 : CLIENT_END_XinShi) {
								if (temp1.indexOf(val1) != -1) {
									flag_end = 1;

									// writerString(fwUn3, val1 + "#" +
									// temp1 +
									// "#" + SUM + "#" + file.getName());
									break;
								}
							}
						}
						if (flag == 3) {// 执行
							for (String val1 : CLIENT_END) {
								if (temp1.indexOf(val1) != -1) {
									flag_end = 1;

									// writerString(fwUn3, val1 + "#" +
									// temp1 +
									// "#" + SUM + "#" + file.getName());
									break;
								}
							}
						} else {
							for (String val1 : CLIENT_END) {
								if (temp1.indexOf(val1) != -1) {
									flag_end = 1;

									// writerString(fwUn3, val1 + "#" +
									// temp1 +
									// "#" + SUM + "#" + file.getName());
									break;
								}
							}
						}
					}

					if (flag_end == 1) {
						break;
					}

					for (ClientVO clvo : CourtData.mapClientList) {
						// System.out.println("keyV= " +
						// entry.getKey() + " and
						// value= " + entry.getValue());
						if (!StringUtils.isNull(clvo.getClientFrom())) {

							String key = clvo.getClientFrom();
							if (temp1.indexOf(key) != -1) {
								// 得到前缀后的内容
								String cl = temp1.substring(temp1.indexOf(key) + key.length());

								if (key.equals("执行机关")) {
									if (cl.indexOf("于") != -1) {
										cl = cl.substring(0, cl.indexOf("于"));
									} else if (cl.indexOf("以") != -1) {
										cl = cl.substring(0, cl.indexOf("以"));
									} else if (cl.indexOf("提出") != -1) {
										cl = cl.substring(0, cl.indexOf("提出"));
									}
								}
								

								// 用来判断是正方律师还是反方律师
								for (int i1 = 0; i1 < CourtUtils.NEW_PLAINTIFF.length; i1++) {// 原告
									if (CourtUtils.NEW_PLAINTIFF[i1].equals(clvo.getClientType())) {
										flag_lawyer_p_d = 1;
										break;
									}
								}

								for (int i2 = 0; i2 < CourtUtils.NEW_DEFENDANT.length; i2++) {// 被告
									if (CourtUtils.NEW_DEFENDANT[i2].equals(clvo.getClientType())) {
										flag_lawyer_p_d = 2;
										break;
									}
								}
								

								String[] strs1 = CourtUtils.splitClient(cl);
								// String[] strs =
								// cl.split("[；,。,：]");
								if (null != strs1) {
									for (String s : strs1) {
										
										// 律师事务所
										if (cl.indexOf("律师事务所") != -1) {

											String[] stemp = cl.split("[。，；：]");
											for (int i = 0; i < stemp.length; i++) {
												String te = stemp[i];
												if (te.indexOf("律师事务所") != -1) {

													if (te.substring(0, 1).equals("系")) {
														te = te.substring(1);
													}
													te = te.substring(0, te.indexOf("律师事务所") + 5);

													if (flag_lawyer_p_d == 1) {
														lawOfficeP += te + "#" + s + ";";
													} else if (flag_lawyer_p_d == 2) {
														lawOfficeD += te + "#" + s + ";";
													}
												}
											}
										}
										
										// System.out
										// .println(clvo.getClientType() +
										// "#" + key + "#" + s + ";");
										// writerString(fwUn2,
										// clvo.getClientType() + "#" + key
										// + "#" + s + "#"
										// + SUM + "#" + file.getName());

										ts.add(clvo.getClientType() + "#" + s);
										// break;
									}
								}

								break;
							}
						}
					}
					if (fa > 20) {
						flag_end = 1;
						break;
					}
					if (flag_end == 1) {
						break;
					}
					// reslut = reslut.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "");
					//
					// System.out.println(reslut);
					// System.out.println(index);
					// if (reslut.indexOf("院") != -1) {
					// return reslut;
					// }

				}
				// System.out.println(o.toString());
			}
		} catch (XpathSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 遍历TreeSet
		StringBuffer sbfClient = new StringBuffer();
		String resultClient = null;
		if (ts.size() > 0) {
			Iterator it = ts.iterator();
			while (it.hasNext()) {
				String ss = it.next().toString();
				sbfClient.append(ss + ";");
				// System.out.println("结果： " + ss + ",");
			}
			resultClient = sbfClient.toString();
			if (resultClient.indexOf(";") != -1) {
				resultClient = resultClient.substring(0, resultClient.length() - 1);
				// System.out.println("最后结果： " + resultClient);
			}
		}

		if (lawOfficeP.length() > 0) {
			vo.setLawOfficeP(lawOfficeP.substring(0, lawOfficeP.length() - 1));
		} else {
			vo.setLawOfficeP(null);
		}

		if (lawOfficeD.length() > 0) {
			vo.setLawOfficeD(lawOfficeD.substring(0, lawOfficeD.length() - 1));
		} else {
			vo.setLawOfficeD(null);
		}

		vo.setClients(resultClient);

		return vo;

	}
	
	
	/*public static String getclientOldSZheJGKW(Document doc, int flag) {
		String reslut = null;
		if (doc == null) {
			return null;
		}
		TreeSet<String> ts = new TreeSet<String>();

		int fa = 0;// 判断第一个a标签前是不是出现过当事人信息，如果大于0说明出现
		int flag_end = 0; // 判断是不是到了非当事人部分
		Elements all = doc.body().getAllElements();

		// writerString(fwUn2, (case1!=null?case1:"NULL") + "#" + "#" +
		// file.getName());

		// String xpath = "//*[@id='DocArea']/div[position()>3]";
		// //*[@id="cc"]/table/tbody/tr[7]/td/div/p[1]

		String xpath = "//html/body/div/p[position()>0]/span";
		JXDocument jxDocument = new JXDocument(doc);
		try {
			List<Object> rs = null;
			rs = jxDocument.sel(xpath);
			for (Object o : rs) {
				if (o instanceof Element) {

					int index = ((Element) o).siblingIndex();
					String temp1 = ((Element) o).text();

					fa++;

					// String temp2=getCase(temp1);
					// System.out.println(temp2);

					if (StringUtils.isNull(temp1)) {
						continue;
					}
					temp1 = temp1.replace(":", "：").replace(",", "，").replace(";", "：");

					if (!StringUtils.isNull(temp1)) {

						if (flag == 1) {
							for (String val1 : CLIENT_END) {
								if (temp1.indexOf(val1) != -1) {
									flag_end = 1;

									// writerString(fwUn3, val1 + "#" +
									// temp1 +
									// "#" + SUM + "#" + file.getName());
									break;
								}
							}
						}
						if (flag == 2) {
							for (String val1 : CLIENT_END_XinShi) {
								if (temp1.indexOf(val1) != -1) {
									flag_end = 1;

									// writerString(fwUn3, val1 + "#" +
									// temp1 +
									// "#" + SUM + "#" + file.getName());
									break;
								}
							}
						}
						if (flag == 3) {// 执行
							for (String val1 : CLIENT_END) {
								if (temp1.indexOf(val1) != -1) {
									flag_end = 1;

									// writerString(fwUn3, val1 + "#" +
									// temp1 +
									// "#" + SUM + "#" + file.getName());
									break;
								}
							}
						} else {
							for (String val1 : CLIENT_END) {
								if (temp1.indexOf(val1) != -1) {
									flag_end = 1;

									// writerString(fwUn3, val1 + "#" +
									// temp1 +
									// "#" + SUM + "#" + file.getName());
									break;
								}
							}
						}
					}

					if (flag_end == 1) {
						break;
					}

					for (ClientVO clvo : CourtData.mapClientList) {
						// System.out.println("keyV= " +
						// entry.getKey() + " and
						// value= " + entry.getValue());
						if (!StringUtils.isNull(clvo.getClientFrom())) {

							String key = clvo.getClientFrom();
							if (temp1.indexOf(key) != -1) {
								// 得到前缀后的内容
								String cl = temp1.substring(temp1.indexOf(key) + key.length());

								if (key.equals("执行机关")) {
									if (cl.indexOf("于") != -1) {
										cl = cl.substring(0, cl.indexOf("于"));
									} else if (cl.indexOf("以") != -1) {
										cl = cl.substring(0, cl.indexOf("以"));
									} else if (cl.indexOf("提出") != -1) {
										cl = cl.substring(0, cl.indexOf("提出"));
									}
								}

								String[] strs1 = CourtUtils.splitClient(cl);
								// String[] strs =
								// cl.split("[；,。,：]");
								if (null != strs1) {
									for (String s : strs1) {
										// System.out
										// .println(clvo.getClientType() +
										// "#" + key + "#" + s + ";");
										// writerString(fwUn2,
										// clvo.getClientType() + "#" + key
										// + "#" + s + "#"
										// + SUM + "#" + file.getName());

										ts.add(clvo.getClientType() + "#" + s);
										// break;
									}
								}

								break;
							}
						}
					}
					if (fa > 20) {
						flag_end = 1;
						break;
					}
					if (flag_end == 1) {
						break;
					}
					// reslut = reslut.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "");
					//
					// System.out.println(reslut);
					// System.out.println(index);
					// if (reslut.indexOf("院") != -1) {
					// return reslut;
					// }

				}
				// System.out.println(o.toString());
			}
		} catch (XpathSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 遍历TreeSet
		StringBuffer sbfClient = new StringBuffer();
		String resultClient = null;
		if (ts.size() > 0) {
			Iterator it = ts.iterator();
			while (it.hasNext()) {
				String ss = it.next().toString();
				sbfClient.append(ss + ";");
				// System.out.println("结果： " + ss + ",");
			}
			resultClient = sbfClient.toString();
			if (resultClient.indexOf(";") != -1) {
				resultClient = resultClient.substring(0, resultClient.length() - 1);
				// System.out.println("最后结果： " + resultClient);
			}
		}

		return resultClient;

	}*/

	/**
	 * 得到案由
	 * 
	 * @Description: TODO
	 * @param text
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月8日 上午10:24:07
	 */
	public static String getCaseFromStr(String text) {

		if (StringUtils.isNull(text)) {
			return null;
		}
		// if (text.length() > 200) {
		// System.out.println(text.substring(0, 199));
		// }
		for (String val : CourtData.LISTCasecause_small_small) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}
		for (String val : CourtData.LISTCasecause_small) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}
		for (String val : CourtData.LISTCasecause_middle) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}
		for (String val : CourtData.LISTCasecause_big) {
			if (text.indexOf(val) != -1) {
				return val;
			}
		}

		String result = null;

		return result;
	}

	/**
	 * E:/法院详细页面/地方/天津市/天津市宝坻区人民法院/0dd9f510-eac3-5874-a2c7-cc27ebddcd5d.html
	 * 取文件路径的 倒算 第二个字符
	 * 
	 * @Description: TODO
	 * @param file
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月28日 下午3:40:44
	 */
	public static String getCourtNameFromFile(File file) {
		if (file == null) {
			return null;
		}

		String temp[] = file.getPath().split("\\\\");
		if (temp != null && temp.length > 1) {

			String tempCourtName = temp[temp.length - 2];

			if (!StringUtils.isNull(tempCourtName)) {
				if (tempCourtName.indexOf("法院") != -1) {
					tempCourtName = tempCourtName.substring(0, tempCourtName.indexOf("法院") + 2);
					return tempCourtName;
				}
			}

		}
		return null;
	}

}
