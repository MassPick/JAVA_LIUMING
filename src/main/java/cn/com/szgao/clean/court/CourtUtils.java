package cn.com.szgao.clean.court;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.StringUtils;

/**
 * 法院数据清洗工具类
 * 
 * @author liuming
 * @Date 2016年7月1日 上午9:42:54
 */
public class CourtUtils {

	public static String[] ERCOEDING = { "�", "й", "෨", "Ժ", "ۼ", "ҩ", "ල", "ɷ", "ص", "δ", "ġ", "Ϊ", "ط", "Ϣ", "ȡ", "Ӫ",
			"ã", "", "Դ", "ڲ", "Ѱ" };

	/**
	 * 结束处的审判官等
	 */
	public static String[] JUDGES = { "见习书记员", "代书记员", "代理审判员", "代理审判长", "人民陪审员", "审判长", "审判员", "书记员", "执行长", "执行员", };

	// 反方 7 ---被告
	public static String[] NEW_DEFENDANT = { "被告（反诉原告）", "被上诉人", "被申请人", "被执行人", "抗诉机关", "被告" };

	// 正方 10 --原告
	public static String[] NEW_PLAINTIFF = { "申请复议人", "申请执行人", "再审申请人", "执行异议人", "申诉人", "申请人", "上诉人", "公诉人", "自诉人",
			"被害人", "原告" };

	// 中性 6
	public static String[] NEW_NEUTRAL = { "委托代理人", "法定代理人", "第三人", "法官", "法院" };

	// 其他 1
	public static String NEW_OTHER = "其他";

	// 提发布日期
	public static String[] PUBLISHDATE = { "发布时间", "发布日期", "提交时间", "提交日期", "关注时间", "编辑时间", "编辑日期", "发表时间", "录入时间",
			"更新时间" };

	// 被告
	public static String[] DEFENDANT = { "被上诉人", "被执行人", "被申诉人", "被申请人", "被申请执行人", "原审被告人", "原审原告", "罪犯", "自诉人", "被告",
			"赔偿义务机关", "一审被告", "二审被上诉人" };

	// 原告
	public static String[] PLAINTIFF = { "上诉人", "申诉人", "申请执行人", "申请人", "执行人", "原审被告", "赔偿请求人", "再审申请人", "执行异议人", "公诉人",
			"被害人", "原公诉机关", "公诉机关", "执行机构", "原告", "复议机关", "申请复议人", "一审原告", "起诉人", "二审上诉人", "原审第三人", "负责人", "抗诉机关",
			"申请再审人" };// "第三人", "诉讼代理人", "辩护人", "委托代理人", "法定代表人","移送执行机构",
						// "委托代理", "四被上诉人委托代理人", "两上诉人的委托代理人"

	public static String[] KEYWORDKE = { "申请再审人", "被上诉人", "二审被上诉人", "原审被告人", "原审第三人", "二审上诉人", "一审被告", "一审原告", "被申请人",
			"赔偿请求人", "被告人", "原告", "执行机构", "被申请人", "申请执行人", "申请人", "辩护人", "被申请执行人", "赔偿请求人", "赔偿义务机关", "原公诉机关", "抗诉机关",
			"公诉机关", "申请复议人", "被上诉人", "被申诉人", "被执行人", "反诉被告", "反诉原告", "原审被告", "原审原告", "执行人", "负责人", "上诉人", "起诉人", "申诉人",
			"被告人", "原告人", "公诉人", "被害人", "再审申请人", "执行异议人", "公诉人", "被害人", "被告", "原告", "罪犯" }; // "复议机关",
																							// "委托代理人",
																							// "委托代理",
																							// "特别授权代理",
																							// "四被上诉人委托代理人",
																							// "两上诉人的委托代理人","移送执行机构",
																							// "诉讼代理人",
																							// "法定代表人",
																							// "第三人"

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

	// 赔偿 因为有赔偿申请人，所以删除
	public static String[] CLIENT_END = { "一案", "纠纷", "争议", "认定", "确认", "认为", "辩称", "承担", "负担", "签订", "无异议", "涉及", "诉称",
			"改判", "作为", "同居", "同居生活", "副本", "享受", "相互认识", "婚姻关系", "驳回", "通告", "纠纷", "未履行生效法律文书", "未履行法律文书", "申请强制执行",
			"受理", "生效",

			"检察院以", "检察院指控", "检察院指派"

	};

	public static String[] CLIENT_END_XinShi = { "一案", "纠纷", "争议", "认定", "确认", "认为", "辩称", "承担", "负担", "签订", "无异议",
			"涉及", "诉称", "改判", "作为", "同居", "同居生活", "副本", "享受", "相互认识", "婚姻关系", "驳回", "通告",

			"公诉机关以",

			"检察院以", "检察院指控", "检察院指派", "指派", "公开开庭审理", "开庭审理", "上述事实", "经鉴定", "经审理", "查明", "纠纷", "未履行生效法律文书", "未履行法律文书",
			"申请强制执行", "受理", "生效" };

	/**
	 * 从hmtl取发布日期
	 * 
	 * @Description: TODO
	 * @param html
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年7月1日 下午3:32:03
	 */
	public static String getPublishDateFromHtml(String html) {
		if (StringUtils.isNull(html)) {
			return null;
		}
		if (html.length() < 27) {
			return null;
		}
		String res = null;
		for (int i = 0; i < PUBLISHDATE.length; i++) {
			if (html.indexOf(PUBLISHDATE[i]) != -1) {
				res = html.substring(html.indexOf(PUBLISHDATE[i]));
				if (res.length() > 26) {
					res = res.substring(0, 25);
					res = DateUtils.getDateFromStringYYYMMDD(DateUtils.getReplaceAllDate(res));
					res = DateUtils.toYMDOfChaStr_ESZZ2(res);
					return res;
				}
			}
		}

		return res;
	}

	/**
	 * 从clients得到反方 0 原告 1 被告
	 * 原告#翟敏;委托代理人#汤红梅;委托代理人#许翔;法定代理人#严建国;被告#丁淑华;被告#华安财产保险股份有限公司上海分公司
	 * 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @return String[]
	 * @author liuming
	 * @date 2016年7月1日 上午10:10:25
	 */
	public static String[] getSplitClient(String str) {
		String res[] = new String[5];
		if (StringUtils.isNull(str)) {
			return res;
		}
		String plaintiff = "";// 原告
		String defendant = "";// 被告
		String neutral = "";// 中性
		String other = "";// 其他
		if (str.indexOf(";") != -1) {// 多个
			String res_temp[] = str.split(";");
			for (int i = 0; i < res_temp.length; i++) {
				String re1 = res_temp[i];
				if (!StringUtils.isNull(re1)) {
					String re2[] = re1.split("#");

					if (re2 == null || re2.length != 2) {
						continue;
					}

					int flag = 0;

					for (int i1 = 0; i1 < NEW_PLAINTIFF.length; i1++) {// 原告
						if (NEW_PLAINTIFF[i1].equals(re2[0])) {
							plaintiff += re2[1] + "、";
							flag = 1;
							break;
						}
					}
					if (flag == 1) {
						continue;
					}
					for (int i2 = 0; i2 < NEW_DEFENDANT.length; i2++) {// 被告
						if (NEW_DEFENDANT[i2].equals(re2[0])) {
							defendant += re2[1] + "、";
							flag = 1;
							break;
						}
					}
					if (flag == 1) {
						continue;
					}

					for (int i3 = 0; i3 < NEW_NEUTRAL.length; i3++) {// 中性
						if (NEW_NEUTRAL[i3].equals(re2[0])) {
							neutral += re2[1] + "、";
							flag = 1;
							break;
						}
					}
					if (flag == 1) {
						continue;
					}

					if (NEW_OTHER.equals(re2[0])) {// 其他
						other += re2[1] + "、";
						flag = 1;
					}
					if (flag == 1) {
						continue;
					}

				}
			}
		}

		else if ((str.indexOf(";") == -1) && (str.indexOf("#") != -1)) {// 一个
																		// "clients":
																		// "被上诉人#刘绍平",
			String re1 = str;
			if (!StringUtils.isNull(re1)) {
				String re2[] = re1.split("#");

				if (re2 == null || re2.length != 2) {
					return res;
				}

				int flag = 0;

				for (int i1 = 0; i1 < NEW_PLAINTIFF.length; i1++) {// 原告
					if (NEW_PLAINTIFF[i1].equals(re2[0])) {
						plaintiff += re2[1] + "、";
						flag = 1;
						break;
					}
				}

				if (flag == 0) {
					for (int i2 = 0; i2 < NEW_DEFENDANT.length; i2++) {// 被告
						if (NEW_DEFENDANT[i2].equals(re2[0])) {
							defendant += re2[1] + "、";
							flag = 1;
							break;
						}
					}
				}

				if (flag == 0) {
					for (int i3 = 0; i3 < NEW_NEUTRAL.length; i3++) {// 中性
						if (NEW_NEUTRAL[i3].equals(re2[0])) {
							neutral += re2[1] + "、";
							flag = 1;
							break;
						}
					}
				}

				if (flag == 0) {
					if (NEW_OTHER.equals(re2[0])) {// 其他
						other += re2[1] + "、";
						flag = 1;
					}
				}
			}
		}

		// else if(str.indexOf(";")==-1&&str.indexOf("#")!=-1){//只有一个
		// String re2[]=str.split("#");
		// for (int i1 = 0; i1 < NEW_PLAINTIFF.length; i1++) {//原告
		// if(NEW_PLAINTIFF[i1].equals(re2[0])){
		// plaintiff+=re2[1]+",";
		// }
		// }
		// for (int i2 = 0; i2 < NEW_DEFENDANT.length; i2++) {//被告
		// if(NEW_PLAINTIFF[i2].equals(re2[0])){
		// defendant+=re2[1]+",";
		// }
		// }
		// }
		if (plaintiff.indexOf("、") != -1) {
			plaintiff = plaintiff.substring(0, plaintiff.lastIndexOf("、"));
		} else {
			plaintiff = null;
		}
		if (defendant.indexOf("、") != -1) {
			defendant = defendant.substring(0, defendant.lastIndexOf("、"));
		} else {
			defendant = null;
		}
		if (neutral.indexOf("、") != -1) {
			neutral = neutral.substring(0, neutral.lastIndexOf("、"));
		} else {
			neutral = null;
		}
		if (other.indexOf("、") != -1) {
			other = other.substring(0, other.lastIndexOf("、"));
		} else {
			other = null;
		}
		res[0] = plaintiff;// 原告
		res[1] = defendant;// 被告
		res[2] = neutral;// 中性
		res[3] = other;// 其他

		return res;
	}

	/**
	 * 从 去前缀后的内容 得到当事人信息 以 、判断是不是有多个
	 * 
	 * @param str
	 * @return
	 */
	public static String[] splitClient(String str) {

		if (StringUtils.isNull(str)) {
			return null;
		}

		// 去括号内容
		str = removeBlank(str);
		String str1 = null;
		int flagCarray = 0;// 判断是否有结束句标记，如是不是有句号 "申请人：焦方平。"

		str = str.replace(":", "：").replace(",", "，").replace(";", "；");
		String[] cArray = new String[] { "，", "；", "。" };// 行结束
		for (int i = 0; i < cArray.length; i++) {

			if (str.indexOf(cArray[i].toString()) != -1) {
				flagCarray = 1;
				str1 = str.substring(0, str.indexOf(cArray[i].toString()));

				if (str1.equals("：") || str1.equals("。") || str1.equals("；") || str1.equals("，")) {// 原告：。
					str1 = null;
					continue;
				}

				// --------------------- 申请人：焦方平。
				String[] stemp = str1.split("[。，；：]");
				if (stemp.length > 0) {
					for (String strtemp : stemp) {
						if (strtemp.equals("：") || strtemp.equals("。") || strtemp.equals("；") || strtemp.equals("，")) {// 原告：。
							str1 = null;
							continue;
						} else if (StringUtils.isNull(strtemp)) {
							continue;
						} else {
							str1 = strtemp;
							break;
						}
					}
				}

				// str1 =
				// str1.split("[。，；：]").length>0?str1.split("[。，；：]")[0]:"" ;
				// // //委托代理人。陈美琴。 第一个可能是。 被告人邵××。 申请人：焦方平。
				// if (StringUtils.isNull(str1)) {
				// continue;
				// } else {
				// break;
				// }

			}
		}

		if (StringUtils.isNull(str1)) {// 申请人：焦方平 没有。
			if (flagCarray == 0) {
				String[] stemp = str.split("[：]");
				if (stemp.length > 0) {
					for (String strtemp : stemp) {
						if (strtemp.equals("：")) {// 原告：。
							str1 = null;
							continue;
						} else if (StringUtils.isNull(strtemp)) {
							continue;
						} else {
							str1 = strtemp;
							break;
						}
					}
				}
			}
		}

		if (StringUtils.isNull(str1)) {
			return null;
		}

		String strs[] = str1.split("、");
		// String[]strs1 = new String[]{};
		for (int i = 0; i < strs.length; i++) {
			String stri = strs[i].replace("：", "").replace("；", "").replace("，", "").replace("。", "");
			strs[i] = stri;
		}
		return strs;
	}

	/**
	 * 去() 及内容
	 * 
	 * @param str
	 * @return
	 */
	public static String removeBlank(String str) {

		if (StringUtils.isNull(str)) {
			return null;
		}
		String sss = str;
		sss = sss.replace("(", "（").replace(")", "）").replace("【", "（").replace("】", "）").replace("[", "（").replace("]",
				"）");
		try {
			int a1 = sss.indexOf("（");
			int b1 = sss.indexOf("）") + 1;
			int num = 0;
			char[] chars = sss.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if ('（' == chars[i]) {
					num++;
				}
			}

			for (int i = 0; i < num; i++) {
				if (a1 != -1) {
					if (b1 != -1 && b1 != 0 && a1 < b1) {
						sss = sss.substring(0, a1) + sss.substring(b1);
						a1 = sss.indexOf("（");
						b1 = sss.indexOf("）") + 1;
						if (b1 < a1) {
							String temp = sss.substring(a1);
							if (temp.indexOf("）") != -1) {
								int tempi = temp.indexOf("）") + 1;
								b1 = a1 + tempi;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			return sss;
		}
		return sss;
	}

	/**
	 * 判断是否存在乱码 false 是乱码 超过20个乱码字符就算是乱码
	 * 
	 * @param value
	 * @return
	 * @return boolean
	 * @author liuming
	 * @date 2016年7月8日 上午9:45:22
	 */
	public static boolean getErrorCode2(String value) {
		if (value == null || "".equals(value)) {
			return false;
		}

		// byte[] temp = value.getBytes();// 使用平台默认的字符集将此 String
		// 解码为字节序列，并将结果存储到一个新的字节数组中。
		int count = 0;
		// 遍历数组的每一个元素，也就是字符串中的每一个字母
		// for (int i = 0; i < temp.length; i++) {

		for (String val : ERCOEDING) {
			String str = value;
			while (str.indexOf(val) != -1) {
				count++;
				// System.out.println(val +" " +str );
				if (count > 20) {
					return false;
				}
				// 将字符串出现c的位置之前的全部截取掉
				int index = str.indexOf(val);
				str = str.substring(str.indexOf(val) + 1);
			}
		}
		// }
		return true;

	}

	/**
	 * 得到法律条文
	 * 
	 * @param s1
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年9月21日 上午11:38:17
	 */
	public static String getLaws(String s1) {
		String rs_n = null;
		try {

			if (StringUtils.isNull(s1)) {
				return null;
			} else {

//				 s1=s1.replace("</br>", "$");

			}

			s1 = s1.replace("<", "〈").replace("﹤", "〈").replace("＜", "〈").replace(">", "〉").replace("﹥", "〉")
					.replace("＞", "〉").replace("&lt;", "〈").replace("&gt;", "〉").replace("&lt;", "〈")
					.replace("&gt;", "〉");

			// 《最高人民法院关于适用<；中华人民共和国刑事诉讼法>；的解释》第一百五十五条
			s1 = s1.replace("〈；；；", "〈").replace("〉；；；", "〉").replace("〈；；", "〈").replace("〉；；", "〉").replace("〈；", "〈")
					.replace("〉；", "〉")

			;

			// 〈 < ﹤ ＜
			// 〉 >﹥ ＞

			s1 = s1.replace("笫", "第").replace("〈最高", "《最高").replace("〉第", "》第").replace("【中华", "《中华")
					.replace("】第", "》第")

			.replace("适用?", "适用〈").replace("?的解释", "〉的解释").replace("《?", "《").replace("?》", "》")

			.replace("条条", "条");

			HashMap<String, String> hashLaw = new HashMap<String, String>();

			// String s2[] = s1.split("[$,，。;；：以及]");
			// String s2[] = s1.split("[$,，。;；：]");
			String s2[] = s1.split("[,，。;；：]");
			for (int i = 0; i < s2.length; i++) {
				String st = s2[i];
				if (StringUtils.isNull(st)) {
					continue;
				}
				// 去空格
				st = StringUtils.removeSpace(st);

				if (StringUtils.isNull(st)) {
					continue;
				}

				// 去标点符号
//				st = st.replaceAll("[+＋~$`^=|～｀＄＾＝｜￥×? ‘~！￥%&*——，。？_]", "").replace("《《", "《").replace("》》", "》")
//						.replace("(", "（").replace(")", "）");
				
				st = st.replaceAll("[~$`^=|～｀＄＾＝｜￥×? ‘！￥%&*——，。？_]", "").replace("《《", "《").replace("》》", "》")
						.replace("(", "（").replace(")", "）");

				String temps[] = st.split("《");
				for (int j = 0; j < temps.length; j++) {
					String t_te = temps[j];
					if (StringUtils.isNull(t_te)) {
						continue;
					}
					// 不包含《》
					if (t_te.indexOf("》") == -1) {
						continue;
					}

					if (!t_te.substring(0, 1).equals("《") && t_te.indexOf("《") == -1) {
						t_te = "《" + t_te;
					}

					if (!StringUtils.isNull(t_te)) {
						st = t_te;

						if (st.indexOf("》第") != -1 && st.indexOf("条") != -1 && st.indexOf("《") != -1) {

							String s_q1 = st.substring(st.indexOf("《"), st.indexOf("》第") + 1);

							String s_q2 = st.substring(st.indexOf("》第") + 1);

							String s21[] = s_q2.split("[／/、和以及]");
							if (s21.length > 0) {

								String tm_law = "";// 某法
								String tm_law_t = "";// 第多少条
								String tm_law_k = "";

								for (int k = 0; k < s21.length; k++) {
									String st2 = s21[k];
									tm_law = s_q1;

									if (st2.indexOf("第") != -1 && st2.indexOf("条") != -1) {
										String temp = tm_law + st2.substring(st2.indexOf("第"));

										tm_law_t = st2.substring(st2.indexOf("第"), st2.indexOf("条") + 1);
										hashLaw.put(temp, temp);
									} else if (st2.indexOf("第") != -1 && st2.indexOf("款") != -1
											&& st2.indexOf("条") == -1) {

										String temp = tm_law + tm_law_t + st2.substring(st2.indexOf("第"));
										tm_law_k = st2.substring(st2.indexOf("第"), st2.indexOf("款") + 1);
										hashLaw.put(temp, temp);
									} else if (st2.indexOf("第") != -1 && st2.indexOf("项") != -1
											&& st2.indexOf("款") == -1 && st2.indexOf("条") == -1) {
										String temp = tm_law + tm_law_t + tm_law_k + st2.substring(st2.indexOf("第"));
										hashLaw.put(temp, temp);
									}
								}

							} else {
								String temp = st.substring(st.indexOf("《"));
								hashLaw.put(temp, temp);
							}
						} else if (st.indexOf("《") != -1 && st.indexOf("》") != -1 && st.indexOf("条") == -1
								&& st.indexOf("第") == -1) {

							String temp = st.substring(st.indexOf("《"), st.lastIndexOf("》") + 1);

							hashLaw.put(temp, temp);

						}

					}
				}

			}

			if (hashLaw.size() <= 0) {
				return null;
			}

			StringBuffer rels = new StringBuffer();// StringBuffer：线程安全的
			String rs = null;
			Collection<String> keyset = hashLaw.keySet();
			List<String> list = new ArrayList<String>(keyset);

			HashMap<String, String> hashLaw_n = new HashMap<String, String>();
			StringBuffer rels_n = new StringBuffer();

			// 对key键值按字典升序排序
			Collections.sort(list);

			// System.out.println("-----------结果------");
			for (int i = 0; i < list.size(); i++) {
				// System.out.println((i + 1) + " key键---值: " + list.get(i));
				String temp = list.get(i);
				if (StringUtils.isNull(temp)) {
					continue;
				}

				String s11 = null;
				String s12 = null;

				// 处理《》中间的《问题，如 《关于贯彻执行《中华人民共和国民法通则》若干问题的意见（试行）》第一百六十一条
				temp = temp.replace("贯彻执行《", "贯彻执行〈").replace("》若干", "〉若干").replace("适用《", "适用〈")
						.replace("》的解释", "〉的解释").replace("适用【", "适用〈").replace("】的解释", "〉的解释").replace("适用〔", "适用〈")
						.replace("〕的解释", "〉的解释").replace("适用（", "适用〈").replace("）的解释", "〉的解释")

				;

				String s_temp = washLaw(temp);
				if (!StringUtils.isNull(s_temp)) {
					hashLaw_n.put(s_temp, s_temp);
				}
				

			}

			if (hashLaw_n.size() > 0) {
				Collection<String> keyset_n = hashLaw_n.keySet();
				List<String> list_n = new ArrayList<String>(keyset_n);
				// 对key键值按字典升序排序
				Collections.sort(list_n);

				for (int i = 0; i < list_n.size(); i++) {
					rels_n.append(list_n.get(i) + "</br>");
				}
				if (rels_n.length() > 0) {
					rs_n = rels_n.toString();
					rs_n = rs_n.substring(0, rs_n.toString().lastIndexOf("</br>"));
				}
			}

		} catch (

		Exception e)

		{
			return null;
		}

		return rs_n;

	}

	public static String NO_Laws[] = { "《解释》", "《证明》", "《意见》", "《证实材料》", "证书》", "对账单》", "＊", "×", "X", "某" };

	/**
	 * 此时传过来只有可能只有一个条文
	 * 
	 * @param temp1
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年11月4日 下午3:30:40
	 */
	public static String washLaw(String temp1) {
		String temp = temp1;
		if (StringUtils.isNull(temp)) {
			return null;
		}

		// 《解释》第二十二条 《中华人民共和国刑法》第二百某十四条第一款

		for (int i = 0; i < NO_Laws.length; i++) {
			if (temp.indexOf(NO_Laws[i]) != -1) {
				return null;
			}
		}

		// 〈 < ﹤ ＜
		// 〉 >﹥ ＞

		temp = temp.replace("(", "（").replace(")", "）").replace(".", "")

		.replace("》第第", "》第").replace("（项）", "项").replace("㈠", "（一）").replace("㈡", "（二）").replace("㈢", "（三）")
				.replace("㈣", "（四）").replace("㈤", "（五）").replace("㈥", "（六）").replace("㈦", "（七）").replace("㈧", "（八）")
				.replace("㈨", "（九）").replace("㈩", "（十）").replace("〇", "零")

				;

		// 《》第第一款第（一）项
		String s_q = null;
		if (temp.indexOf("《") != -1 && temp.indexOf("》") != -1 && temp.indexOf("《") < temp.indexOf("》")) {
			s_q = temp.substring(temp.indexOf("《"), temp.indexOf("》") + 1);
			if ("《》".equals(s_q)) {
				return null;
			} else {
				// 包含字母
				if (StringUtils.isIncludeLetterZZ(s_q)) {
					return null;
				}

			}
		}
		if (!StringUtils.isNull(s_q)) {
			// 《最高人民法院关于适用＜＞的解释》第二百三十三条
			if (temp.indexOf("〈") != -1 && temp.indexOf("〉") != -1 && temp.indexOf("〈") < temp.indexOf("〉")) {
				String s_q1 = temp.substring(temp.indexOf("〈"), temp.indexOf("〉") + 1);
				if ("〈〉".equals(s_q1)) {
					return null;
				}
			}
		}


		if (temp.indexOf("第") != -1 && temp.indexOf("条") != -1) {

			
			
			String str_q2 = temp.substring(0,temp.lastIndexOf("》")+1 );
			String str_h2 = temp.substring( temp.lastIndexOf("》") +1);
			
			str_h2=str_h2.replace("〈", "（").replace("〉", "）").replace("(", "（").replace(")", "）").replace("[", "（").replace("]", "）").replace("【", "（").replace("】", "）").replace("｛","（").replace("｝",")")
					.replace("址", "十").replace("市", "十").replace("+", "十").replace("＋", "十").replace("-", "一").replace("－", "一")
					
					
					;
			str_h2=str_h2.replace("一十一", "十一")
					.replace("一十二", "十二")
					.replace("一十三", "十三")
					.replace("一十四", "十四")
					.replace("一十五", "十五")
					.replace("一十六", "十六")
					.replace("一十七", "十七")
					.replace("一十八", "十八")
					.replace("一十九", "十九")
					;
			
			str_h2=str_h2.replace("之一第", "第").replace("之第", "第");
			
			str_h2=str_h2.replace("条（", "条第（");
			
			temp=str_q2+str_h2 ;
			String str_t = temp.substring(temp.lastIndexOf("条") + 1);
			String str_q = temp.substring(0,temp.lastIndexOf("第") );
			
			if (str_t.indexOf("项") != -1 ) {
				
				temp = temp.substring(temp.indexOf("《"), temp.lastIndexOf("项") + 1);
				
				String s_t=temp.substring(temp.lastIndexOf("第"), temp.lastIndexOf("项") + 1);
				if(s_t.indexOf("（") ==-1&s_t.indexOf("）") ==-1){
					s_t=s_t.replace("第", "第（").replace("项", "）项");
				}
				temp =str_q+s_t;

			} else if (str_t.indexOf("款") != -1 && str_t.substring(str_t.indexOf("款") + 1).length() > 0
					&& str_t.substring(str_t.indexOf("款") + 1).indexOf("项") == -1) {

				String s_t=temp.substring(temp.lastIndexOf("第"), temp.lastIndexOf("款") + 1);
				s_t=s_t.replace("（", "").replace("）", "").replace("〈", "").replace("〉", "");
				temp =str_q+s_t;
				

			} else if (str_t.length() > 0 && str_t.indexOf("款") == -1) {
				// 中华人民共和国民事事诉讼法》第一百四十五条第一款“宣判前",
				// 《最高人民法院关于审理侵害信息网络传播权民事纠纷案件适用法律若干问题的规定》第六条规定
				
				String s_t=temp.substring(temp.lastIndexOf("第"), temp.lastIndexOf("条") + 1);
				s_t=s_t.replace("（", "").replace("）", "").replace("〈", "").replace("〉", "");
				temp =str_q+s_t;
			}

			String t_1 = temp.substring(0, temp.lastIndexOf("》") + 1);
			String t_2 = temp.substring(temp.lastIndexOf("》") + 1);

			t_2 = StringUtils.replaceSZ(t_2);
			temp = t_1 + t_2;

		}

		String tmp_t = temp.substring(temp.lastIndexOf("》") + 1);

		// 《诉讼费用交纳办法》的有关
		if (tmp_t.length() > 0 && tmp_t.indexOf("第") == -1) {
			return null;
		}

		return temp;
	}

	/**
	 * 得到案由
	 * 
	 * @param text
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月8日 上午10:24:07
	 */
	@SuppressWarnings("unused")
	public static WholeCourtVO getCaseFromStr_VO(String text) {

		WholeCourtVO tem = new WholeCourtVO();

		String text_t = text;
		if (StringUtils.isNull(text)) {
			return null;
		}
		if (text.length() > 250) {
			text = text.substring(0, 249);
		}

		for (String val : CourtData.LISTCasecause_small_small) {
			if (text.indexOf(val) != -1) {

				for (Entry<String, WholeCourtVO> entry : CourtData.MAPCasecause_small_small.entrySet()) {
					String tempXian = entry.getKey();
					if (val.equals(tempXian)) {
						tem = entry.getValue();
						tem.setCaseCause(val);
						return tem;
					}
				}
			}
		}
		for (String val : CourtData.LISTCasecause_small) {
			if (text.indexOf(val) != -1) {
				for (Entry<String, WholeCourtVO> entry : CourtData.MAPCasecause_small.entrySet()) {
					String tempXian = entry.getKey();
					if (val.equals(tempXian)) {
						tem = entry.getValue();
						tem.setCaseCause(val);
						tem.setType5(null);
						return tem;
					}
				}
			}
		}
		for (String val : CourtData.LISTCasecause_middle) {
			if (text.indexOf(val) != -1) {
				for (Entry<String, WholeCourtVO> entry : CourtData.MAPCasecause_middle.entrySet()) {
					String tempXian = entry.getKey();
					if (val.equals(tempXian)) {
						tem = entry.getValue();
						tem.setCaseCause(val);
						tem.setType5(null);
						tem.setType4(null);
						return tem;
					}
				}
			}
		}
		for (String val : CourtData.LISTCasecause_big) {
			if (text.indexOf(val) != -1) {
				for (Entry<String, WholeCourtVO> entry : CourtData.MAPCasecause_big.entrySet()) {
					String tempXian = entry.getKey();
					if (val.equals(tempXian)) {
						tem = entry.getValue();
						tem.setCaseCause(val);
						tem.setType5(null);
						tem.setType4(null);
						tem.setType3(null);
						return tem;
					}
				}
			}
		}

		if (StringUtils.isNull(tem.getType1())) {
			tem = getCaseFromStr_VO_2(text_t);// 全词
			return tem;
		}

		return tem;

	}

	public static WholeCourtVO getCaseFromStr_VO_2(String text) {

		WholeCourtVO tem = new WholeCourtVO();

		if (StringUtils.isNull(text)) {
			return null;
		}

		for (String val : CourtData.LISTCasecause_small_small) {
			if (text.indexOf(val) != -1) {

				for (Entry<String, WholeCourtVO> entry : CourtData.MAPCasecause_small_small.entrySet()) {
					String tempXian = entry.getKey();
					if (val.equals(tempXian)) {
						tem = entry.getValue();
						tem.setCaseCause(val);
						return tem;
					}
				}
			}
		}
		for (String val : CourtData.LISTCasecause_small) {
			if (text.indexOf(val) != -1) {
				for (Entry<String, WholeCourtVO> entry : CourtData.MAPCasecause_small.entrySet()) {
					String tempXian = entry.getKey();
					if (val.equals(tempXian)) {
						tem = entry.getValue();
						tem.setCaseCause(val);
						tem.setType5(null);
						return tem;
					}
				}
			}
		}
		for (String val : CourtData.LISTCasecause_middle) {
			if (text.indexOf(val) != -1) {
				for (Entry<String, WholeCourtVO> entry : CourtData.MAPCasecause_middle.entrySet()) {
					String tempXian = entry.getKey();
					if (val.equals(tempXian)) {
						tem = entry.getValue();
						tem.setCaseCause(val);
						tem.setType5(null);
						tem.setType4(null);
						return tem;
					}
				}
			}
		}
		for (String val : CourtData.LISTCasecause_big) {
			if (text.indexOf(val) != -1) {
				for (Entry<String, WholeCourtVO> entry : CourtData.MAPCasecause_big.entrySet()) {
					String tempXian = entry.getKey();
					if (val.equals(tempXian)) {
						tem = entry.getValue();
						tem.setCaseCause(val);
						tem.setType5(null);
						tem.setType4(null);
						tem.setType3(null);
						return tem;
					}
				}
			}
		}
		return tem;

	}

	/*
	 * public static String getLaws(String s1) { String rs_n = null; try {
	 * HashMap<String, String> hashLaw = new HashMap<String, String>();
	 * 
	 * String s2[] = s1.split("[，。；以及]"); for (int i = 0; i < s2.length; i++) {
	 * String st = s2[i]; if (StringUtils.isNull(st)) { continue; } if
	 * (st.indexOf("》第") != -1 && st.indexOf("条") != -1 && st.indexOf("《") !=
	 * -1) {
	 * 
	 * if (st.indexOf("、") != -1) { String s21[] = st.split("、"); String tm_law
	 * = "";// 某法 String tm_law_t = "";// 第多少条 String tm_law_k = ""; for (String
	 * st2 : s21) { if (st2.indexOf("》第") != -1 && st2.indexOf("条") != -1 &&
	 * st2.indexOf("《") != -1) { tm_law = st2.substring(st2.indexOf("《"),
	 * st2.indexOf("》") + 1); // String temp = st2.substring(st2.indexOf("《"),
	 * // st2.indexOf("条") + 1); String temp = st2.substring(st2.indexOf("《"));
	 * 
	 * // if(st2.indexOf("条第") != -1&&st2.indexOf("》款") // != // -1){ //
	 * tm_law_t=st2.substring(st2.lastIndexOf("第"),st2.lastIndexOf("条")); //
	 * //《中华人民共和国民事诉讼法》第一百一十八条第一款 // } tm_law_t =
	 * st2.substring(st2.lastIndexOf("》第") + 1, st2.lastIndexOf("条") + 1);
	 * 
	 * // System.out.println(temp);
	 * 
	 * if (temp.indexOf("之规定") != -1) { temp = st2.substring(st2.indexOf("《"),
	 * st2.lastIndexOf("之规定")); } else if (temp.indexOf("的规定") != -1) { temp =
	 * st2.substring(st2.indexOf("《"), st2.lastIndexOf("的规定")); }else if
	 * (temp.indexOf("规定") != -1) { temp = st2.substring(st2.indexOf("《"),
	 * st2.lastIndexOf("规定")); }
	 * 
	 * 
	 * hashLaw.put(temp, temp); } else if (st2.indexOf("第") != -1 &&
	 * st2.indexOf("条") != -1) { // String temp = tm_law + //
	 * st2.substring(st2.indexOf("第"), // st2.indexOf("条") // + 1); String temp
	 * = tm_law + st2.substring(st2.indexOf("第"));
	 * 
	 * tm_law_t = st2.substring(st2.indexOf("第"), st2.indexOf("条") + 1);
	 * 
	 * // System.out.println(temp);
	 * 
	 * 
	 * if (temp.indexOf("之规定") != -1) { temp = tm_law +
	 * st2.substring(st2.indexOf("第"), st2.lastIndexOf("之规定")); } else if
	 * (temp.indexOf("的规定") != -1) { temp = tm_law +
	 * st2.substring(st2.indexOf("第"), st2.lastIndexOf("的规定")); }else if
	 * (temp.indexOf("规定") != -1) { temp = tm_law +
	 * st2.substring(st2.indexOf("第"), st2.lastIndexOf("规定")); }
	 * 
	 * 
	 * hashLaw.put(temp, temp); } else if (st2.indexOf("第") != -1 &&
	 * st2.indexOf("款") != -1 && st2.indexOf("条") == -1) { // String temp =
	 * tm_law + // st2.substring(st2.indexOf("第"), // st2.indexOf("条") // + 1);
	 * String temp = tm_law + tm_law_t + st2.substring(st2.indexOf("第")); //
	 * System.out.println(temp);
	 * 
	 * 
	 * if (temp.indexOf("之规定") != -1) { temp = temp.substring(0,
	 * st2.lastIndexOf("之规定")); } else if (temp.indexOf("的规定") != -1) { temp =
	 * temp.substring(0, st2.lastIndexOf("的规定")); }else if (temp.indexOf("规定")
	 * != -1) { temp = temp.substring(0, st2.lastIndexOf("规定")); }
	 * 
	 * 
	 * hashLaw.put(temp, temp); }
	 * 
	 * // 《诉讼费用交纳办法》第二条、第二十二条第一款、第四款 }
	 * 
	 * } else { // String temp = st.substring(st.indexOf("《"), //
	 * st.indexOf("条") + 1); String temp = st.substring(st.indexOf("《"));
	 * 
	 * if (temp.indexOf("之规定") != -1) { temp = temp.substring(0,
	 * temp.lastIndexOf("之规定")); } else if (temp.indexOf("的规定") != -1) { temp =
	 * temp.substring(0, temp.lastIndexOf("的规定")); } else if (temp.indexOf("规定")
	 * != -1) { temp = temp.substring(0, temp.lastIndexOf("规定")); }
	 * 
	 * // System.out.println(temp); hashLaw.put(temp, temp); } } else if
	 * (st.indexOf("《") != -1 && st.indexOf("》") != -1 && st.indexOf("条") == -1
	 * && (st.indexOf("根据") != -1 || st.indexOf("依据") != -1 || st.indexOf("依照")
	 * != -1)) { if (st.indexOf("、") != -1) { String s21[] = st.split("、");
	 * String tm_law = ""; for (String st2 : s21) {
	 * 
	 * // String temp = st2.substring(st2.indexOf("《"), // st2.indexOf("条") +
	 * 1); String temp = st2.substring(st2.indexOf("《"));
	 * 
	 * 
	 * if (temp.indexOf("之规定") != -1) { temp = st2.substring(st2.indexOf("第"),
	 * st2.lastIndexOf("之规定")); }else if (temp.indexOf("的规定") != -1) { temp =
	 * st2.substring(st2.indexOf("第"), st2.lastIndexOf("的规定")); }else if
	 * (temp.indexOf("规定") != -1) { temp = st2.substring(st2.indexOf("第"),
	 * st2.lastIndexOf("规定")); }
	 * 
	 * 
	 * hashLaw.put(temp, temp); }
	 * 
	 * } else { // String temp = st.substring(st.indexOf("《"), //
	 * st.indexOf("条") + 1); String temp = st.substring(st.indexOf("《"));
	 * 
	 * 
	 * if (tmp_t.indexOf("之规定") != -1) { temp = temp.substring(st.indexOf("《"),
	 * temp.indexOf("之规定")); }else if (temp.indexOf("的规定") != -1) { temp =
	 * temp.substring(st.indexOf("《"), temp.indexOf("的规定")); }else if
	 * (temp.indexOf("规定") != -1) { temp = temp.substring(st.indexOf("《"),
	 * temp.indexOf("规定")); }
	 * 
	 * 
	 * // System.out.println(temp); hashLaw.put(temp, temp); } }
	 * 
	 * // System.out.println(st); } // System.out.println("-------");
	 * 
	 * if (hashLaw.size() <= 0) { return null; }
	 * 
	 * StringBuffer rels = new StringBuffer();// StringBuffer：线程安全的 String rs =
	 * null; Collection<String> keyset = hashLaw.keySet(); List<String> list =
	 * new ArrayList<String>(keyset);
	 * 
	 * HashMap<String, String> hashLaw_n = new HashMap<String, String>();
	 * StringBuffer rels_n = new StringBuffer();
	 * 
	 * // 对key键值按字典升序排序 Collections.sort(list);
	 * 
	 * // System.out.println("-----------结果------"); for (int i = 0; i <
	 * list.size(); i++) { // System.out.println((i + 1) + " key键---值: " +
	 * list.get(i)); String temp = list.get(i); if (temp.indexOf("第") != -1 &&
	 * temp.indexOf("条") != -1) {
	 * 
	 * String str_t = temp.substring(temp.lastIndexOf("条") + 1);
	 * 
	 * if (str_t.length() > 11) { continue; }
	 * 
	 * 
	 * //没有规定 if (str_t.indexOf("规定") == -1) {
	 * 
	 * if (str_t.length() > 0 && str_t.indexOf("款") == -1) {//
	 * 中华人民共和国民事事诉讼法》第一百四十五条第一款“宣判前", //
	 * 《最高人民法院关于审理侵害信息网络传播权民事纠纷案件适用法律若干问题的规定》第六条规定 continue; }
	 * 
	 * if (str_t.indexOf("款") != -1 && str_t.substring(str_t.indexOf("款") +
	 * 1).length() > 0 && str_t.substring(str_t.indexOf("款") + 1).indexOf("项")
	 * == -1) { continue; } if (str_t.indexOf("项") != -1 &&
	 * str_t.substring(str_t.indexOf("项") + 1).length() > 0) { continue; } }
	 * 
	 * 
	 * String t = temp.substring(temp.indexOf("第") + 1,
	 * temp.lastIndexOf("条"));// 《中华人民共和国道路交通安全法实施条例》第九十一条 //
	 * 《最高的第一解释条例》第75条第30款 // String t = temp; if (StringUtils.isNumeric(t)) {
	 * Integer ta = Integer.valueOf(t); String s_max =
	 * StringUtils.formatIntegerToMax(ta); temp = temp.substring(0,
	 * temp.indexOf("第") + 1) + s_max + temp.substring(temp.indexOf("条"));
	 * 
	 * // rels.append(s_temp + "@_@"); // rels.append(s_temp + "</br>"); //
	 * continue; } }
	 * 
	 * String tmp_t = temp.substring(temp.lastIndexOf("》") + 1); String tmp_q =
	 * temp.substring(temp.indexOf("《"), temp.lastIndexOf("》") + 1);
	 * 
	 * if (tmp_t.indexOf("之规定") != -1) { temp = tmp_q +
	 * temp.substring(temp.lastIndexOf("》") + 1, temp.lastIndexOf("》") +
	 * tmp_t.indexOf("之规定") + 1); } else if (tmp_t.indexOf("的规定") != -1) { temp
	 * = tmp_q + temp.substring(temp.lastIndexOf("》") + 1, temp.lastIndexOf("》")
	 * + tmp_t.indexOf("的规定") + 1); } else if (tmp_t.indexOf("规定") != -1) { temp
	 * = tmp_q + temp.substring(temp.lastIndexOf("》") + 1, temp.lastIndexOf("》")
	 * + tmp_t.indexOf("规定") + 1); }
	 * 
	 * // 《诉讼费用交纳办法》的有关 if (tmp_t.length() > 0 && tmp_t.indexOf("第") == -1) {
	 * continue; }
	 * 
	 * hashLaw_n.put(temp, temp);
	 * 
	 * // rels.append(temp + "</br>"); } // if (rels.length() > 0) { // rs =
	 * rels.toString(); // rs = rs.substring(0,
	 * rs.toString().lastIndexOf("</br>")); // }
	 * 
	 * Collection<String> keyset_n = hashLaw_n.keySet(); List<String> list_n =
	 * new ArrayList<String>(keyset_n); // 对key键值按字典升序排序
	 * Collections.sort(list_n);
	 * 
	 * for (int i = 0; i < list_n.size(); i++) { rels_n.append(list_n.get(i) +
	 * "</br>"); } if (rels_n.length() > 0) { rs_n = rels_n.toString(); rs_n =
	 * rs_n.substring(0, rs_n.toString().lastIndexOf("</br>")); } } catch
	 * (Exception e) { return null; }
	 * 
	 * return rs_n; }
	 */

}
