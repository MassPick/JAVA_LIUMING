package cn.com.szgao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;
//import com.hankcs.hanlp.HanLP;
//import com.hankcs.hanlp.corpus.tag.Nature;
//import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

/**
 * 
 * 
 * 项目名称：MassPick 类名称：StringUtils 类描述： 处理字符串类 创建人：liuming 创建时间：2015-9-1
 * 上午11:16:26 修改人：liuming 修改时间：2015-9-1 上午11:16:26 修改备注：
 * 
 * @version
 * 
 */
public class StringUtils {
	private static Logger log = LogManager.getLogger(StringUtils.class);
	public static NameBasedGenerator NBG = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);

	public static Gson GSON = new Gson();

	// public static String getUUIDRandom(){
	// return
	// }

	public static String getRandomUUid() {
		try {
			return UUID.randomUUID().toString();
		} catch (Exception e) {
			// logger.error("getRandomUUid", e);
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
	public static String toSBC(String input) {

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
	public static String toDBC(String input) {

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

	/**
	 * JAVA自带的函数 判断是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断 字符串是不是数字，是不是 小数
	 * 
	 * @param str
	 * @return
	 * @author liuming
	 * @Date 2015-11-4 上午11:02:40
	 */
	public static boolean isNumericDecimal(String str) {

		if (str != null && "" != str) {
			String regex = "^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";
			Matcher matcher = Pattern.compile(regex).matcher(str);
			return matcher.matches();
		} else {
			return false;
		}
	}

	/**
	 * 转化字符串为数字 转double 及控制小数点位数 四舍五入
	 * 
	 * @param str
	 * @param num
	 * @return
	 */
	public static Double convertStringToDouble(String str, int num) {
		if (null == str || "".equals(str)) {
			return null;
		}
		str = str.replaceAll("[\\n,\\t,\\r,\\s,&nbsp;]", "").replace(" ", "");
		if ("".equals(str)) {
			return null;
		}

		if (!StringUtils.isNull(str) && StringUtils.isNumericDecimal(str)) {
			BigDecimal b = new BigDecimal(str);
			Double f1 = b.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;

			// DecimalFormat df = new DecimalFormat("#.###");
			// Double get_double = Double.parseDouble(df.format(rel));
			// return get_double;

			// return Double.valueOf(rel);

		} else {
			return null;
		}

	}

	/**
	 * 判断一个字符串是否含有数字 (是否有小数 但 .1没判断)
	 * 
	 * @param content
	 * @return
	 */
	public static boolean hasDigit(String content) {

		boolean flag = false;

		Pattern p = Pattern.compile(".*\\d+.*");

		Matcher m = p.matcher(content);

		if (m.matches())

			flag = true;

		return flag;

	}

	/**
	 * 去除全角或半角的空格
	 * 
	 * @param str
	 * @author liuming
	 * @Date 2015-9-14 下午12:04:59
	 */
	public static String removeBlank(String str) {
		if (null != str) {
			return str.replaceAll("[ |　|]", " ").replace(" ", "");
		}
		return str;
	}

	/**
	 * 去空格等
	 * 
	 * @param str
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年10月25日 上午10:01:45
	 */
	public static String removeSpace(String str) {

		if (null == str || "".equals(str)) {
			return null;
		}
		str = str.replaceAll("[\\n,\\t,\\r,\\s,&nbsp;]", "").replaceAll("[ |　|]", " ").replace(" ", "").replace(" ",
				"");
		if ("".equals(str)) {
			return null;
		}
		return str;
	}

	/**
	 * 判断某字符串中包含的某字符的个数 不能处理空格" "，也不能用trim()
	 * 
	 * @param str
	 *            要处理的字符串 c 分割的字符
	 * @return
	 * @author liuming
	 * @Date 2015-9-14 上午11:40:55
	 */
	public static int countCharacter(String str, String c) {
		int count = 0;
		if (StringUtils.isNull(str)) {
			return 0;
		} else {
			String[] ary = (" " + str + " ").split(c);
			count = ary.length - 1;
		}

		return count;
	}

	/**
	 * 效率较高的一种判断字符串为空的方法 为空：true 不为空:false
	 * 
	 * @param: @param
	 *             str @param: @return @return: Boolean 为空：true
	 *             不为空:false @author liuming @Date 2015-9-1 上午11:14:10 @throws
	 */
	public static Boolean isNull(String str) {

		if (str == null || str.length() <= 0) {
			return true;
		}
		return false;
	}

	public static Boolean isCardId(String cardId) {
		// 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
		// 430623198808296435 430623 19880829 6435 15位: 430623670401643 430623
		// 67 0401 643

		// String str15=
		// "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$" ;
		// String str18=
		// "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$"
		// ;

		// "((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])" 0829

		Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
		// 通过Pattern获得Matcher
		Matcher idNumMatcher = idNumPattern.matcher(cardId);
		// 判断用户输入是否为身份证号
		if (idNumMatcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 去除字符串中的数字
	 * 
	 * @param str
	 * @return
	 */
	public static String removeNum(String str) {
		return str.replaceAll("\\d+", "");
	}

	/**
	 * 去字符串中3 个到3 个以上的数字字组
	 * 
	 * @param str
	 * @return
	 * @author liuming
	 * @Date 2015-9-1 下午5:51:44
	 */
	public static String removeNum3(String str) {
		return str.replaceAll("\\d{3,}", "");
	}

	/**
	 * 用正则表达式 判断是否全为整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {

		if (str != null && "" != str) {
			str = str.replace(" ", "");
			Pattern pattern = Pattern.compile("[0-9]+");
			return pattern.matcher(str).matches();
		} else {
			return false;
		}
	}

	/**
	 * 用正则表达式 判断是否为汉字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		if (str != null && "" != str) {
			String regex = "([\u4e00-\u9fa5]+)";
			Matcher matcher = Pattern.compile(regex).matcher(str);
			return matcher.matches();
		} else {
			return false;
		}

	}

	private final static Pattern patternCardId = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");

	// private final static Pattern patternCardId15 =
	// Pattern.compile("(\\d{14}[0-9a-zA-Z])");
	// private final static Pattern patternCardId18 =
	// Pattern.compile("(\\d{17}[0-9a-zA-Z])");

	/**
	 * 提取字符串中的一个身份证 或全是18位的多个
	 * 
	 * @param str
	 * @return
	 */
	public static String pickUpCardId(String str) {

		Matcher matcher = patternCardId.matcher(str);

		StringBuffer bf = new StringBuffer();
		while (matcher.find()) {
			bf.append(matcher.group()).append(",");
		}

		int len = bf.length();
		if (len > 0) {
			bf.deleteCharAt(len - 1);
		}
		return bf.toString();
	}

	/**
	 * 提取字符串中的多个身份证
	 * 
	 * @param str
	 * @return
	 */
	// public static String pickUpManyCardId(String str) {
	//
	// StringBuffer bf = new StringBuffer();
	// List<Term> lists = HanLP.segment(str);
	// for (Term term : lists) {
	//
	// if (term.nature == Nature.m) {
	// Matcher matcher = patternCardId.matcher(term.word);
	// if (matcher.matches()) {
	// bf.append(term.word).append(",");
	// }
	// }
	// }
	//
	// int len = bf.length();
	// if (len > 0) {
	// bf.deleteCharAt(len - 1);
	// }
	// return bf.toString();
	// }

	/**
	 * 将字符串中特殊的字符转化为空格 " "
	 * 
	 * @param str
	 * @return
	 */
	public static String subSpeCharBlank(String str) {
		str = str.replace("-", " ").replace("-", " ").replace("，", " ").replace(",", " ");
		return str;
	}

	/**
	 * 从身份证得性别
	 * 
	 * @param idCard
	 * @return
	 */
	public static String getSexFromIdCard(String idCard) {
		String sex = null;
		try {

			if (idCard.length() == 18) {
				sex = idCard.substring(16, 17);
			} else if (idCard.length() == 15) {
				sex = idCard.substring(14, 15);
			} else {
				return sex;
			}
			if (isNumber(sex)) {
				if (Integer.parseInt(sex) % 2 == 0) {
					sex = "女";
				} else {
					sex = "男";
				}
			} else {
				sex = null;
			}
		} catch (Exception e) {
			// ExceptionUtils.saveException(idCard, e);
			log.info(
					"Exception  -------------------------------------------------------------------------------------------  "
							+ e);
			// log.debug("debug-------------------------------- "+ e);
		}
		return sex;

	}

	/**
	 * 判断是否为汉字
	 * 
	 * @param str
	 * @return
	 */
	public boolean isChinese2(String str) {

		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (int i = 0; i < chars.length; i++) {
			byte[] bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;

				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}

	/**
	 * 用ascii码 判断是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumerAciic(String str) {
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	/**
	 * 判断一个字符串的首字符是否为字母
	 * 
	 * @param s
	 * @return
	 */
	public static boolean test(String s) {
		char c = s.charAt(0);
		int i = (int) c;
		if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean check(String fstrData) {
		char c = fstrData.charAt(0);
		if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否包含中文 (无法判断中文的字符)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isHasHanZi(String str) {

		String regEx = "[\u4e00-\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 断字符串是否包含中文 (可法判断中文的字符) st.getBytes().length来判断，如果为2，就是中文的，这个对中文符号，。！￥、都好使
	 * 
	 * @param str
	 */
	public static boolean isHasHanziSpeChar(String str) {
		if (str.getBytes().length == str.length()) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 判断字符串的每一位都是相同的
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isSameChars(String str) {
		// System.out.println(str);
		// str=toDBC(str);
		// System.out.println(str);
		if (str.length() < 2) {
			return true;
		}
		char first = str.charAt(0);
		for (int i = 1; i < str.length(); i++) {
			if (str.charAt(i) != first)
				return false;
		}
		return true;
	}

	/**
	 * 全角符号转半角
	 * 
	 * @param str
	 * @return
	 * @author liuming
	 * @Date 2015-10-10 下午12:31:21
	 */
	public static String QtoB(String str) {
		String[] regsQ = { "！", "，", "。", "；", "【", "】" };
		String[] regsB = { "!", ",", ".", ";", "[", "]" };
		for (int i = 0; i < regsQ.length; i++) {
			str = str.replaceAll(regsQ[i], regsB[i]);
		}
		return str;
	}

	/**
	 * 将特殊字符转为""
	 * 
	 * @param str
	 * @return
	 * @author liuming
	 * @Date 2015-10-16 上午10:50:03
	 */
	public static String QtoNull(String str) {

		String[] regsQ = { "！", "，", "。", "；", "【", "】", "`", "!", "★", "？", "©㊣·", "＋", "№" };
		for (int i = 0; i < regsQ.length; i++) {
			str = str.replaceAll(regsQ[i], "");
		}
		return str;
	}

	public static String QtoNullCourt(String str) {
		String[] regsQ = { "！", "，", "。", "；", "【", "】", "`", "!", "★", "？", "©㊣·", "＋", "№", "?", "、", "－", "!", "-",
				"／", "/", "¿¬ìå", "￥", "﹤", "﹥", "<", ">", "\\", "、" };
		for (int i = 0; i < regsQ.length; i++) {
			if ("?".equals(regsQ[i])) {
				str = str.replaceAll("\\?", "");
				continue;
			}
			str = str.replaceAll(regsQ[i], "");
		}
		return str;
	}

	/**
	 * 去特殊符号
	 * 
	 * @param str
	 * @return
	 * @author liuming
	 * @Date 2015-10-29 下午6:36:03
	 */
	public static String removeSpecialCharacters(String str) {
		if (null == str) {
			return null;
		}
		str = str.replaceAll("[©㊣№¿¬ìå¿¬ìå★�]", "");
		return str;
	}

	/**
	 * 去标点符号
	 * 
	 * @param str
	 * @return
	 * @author liuming
	 * @Date 2015-10-29 下午6:27:59
	 */
	public static String removePunctuation(String str) {
		if (null == str) {
			return null;
		}
		str = str.replaceAll("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
		return str;
	}

	/**
	 * 根据URL得到返回的ID集合
	 * 
	 * @param url
	 * @return
	 * @author liuming
	 * @Date 2015-10-15 下午5:38:09
	 */
	public static List<String> getKeysFromBC(String url) {
		List<String> list = new ArrayList<String>();
		String json = loadJSON(url);
		JSONObject jsonObj = JSONObject.fromObject(json);
		JSONArray jsonArray = jsonObj.getJSONArray("rows");
		if (null != jsonArray && jsonArray.size() > 0) {
			for (int i = 0; i < jsonArray.size(); i++) {
				Object obj = jsonArray.get(i);
				JSONObject json2 = JSONObject.fromObject(obj);
				list.add(json2.getString("id"));
			}
		}
		return list;
	}

	public static String loadJSON(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL oracle = new URL(url);
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return json.toString();
	}

	/**
	 * 从字符串中取出数字 (有小数)
	 * 
	 * @param url
	 * @return
	 */
	// public static String[] getNumFromStr(String str) {
	// int j=0;
	//// String[] temp = null;
	// String[] temp = new String[] {};
	// StringBuilder json = new StringBuilder();
	// String ss[]=str.split("[^-?\\d+.?\\d*]");
	// for (int i = 0; i < ss.length; i++) {
	// if(ss[i].matches("-?\\d+.?\\d*")){
	//
	// temp[j]=ss[i];
	// j++;
	// }
	// }
	// return temp;
	//
	// }

	public static String getNumFromStr(String str) {
		String temp = "";
		String ss[] = str.split("[^-?\\d+.?\\d*]");
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].matches("-?\\d+.?\\d*")) {
				temp = ss[i];
			}
		}
		return temp;

	}

	// 判断一个字符串是否都为数字
	public boolean isDigit(String strNum) {
		return strNum.matches("[0-9]{1,}");
	}

	// // 判断一个字符串是否都为数字
	// public boolean isDigit(String strNum) {
	// Pattern pattern = Pattern.compile("[0-9]{1,}");
	// Matcher matcher = pattern.matcher((CharSequence) strNum);
	// return matcher.matches();
	// }

	// 截取数字
	public String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	// 截取非数字
	public String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/**
	 * 返回字符串数组中最长的字符
	 * 
	 * @param array
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2015年12月7日 下午6:05:43
	 */
	public static String getMaxFromStrings(String[] array) {
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].length() > array[index].length())
				index = i;
		}
		return array[index];
	}

	/**
	 * 返回List集合中最长的字符串
	 * 
	 * @param array
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2015年12月7日 下午6:08:21
	 */
	public static String getMaxFromList(List<String> array) {
		int index = 0;
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).length() > array.get(index).length())
				index = i;
		}
		return array.get(index);
	}

	/**
	 * 转码
	 * 
	 * @param str
	 * @param strEncode
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return String
	 * @author liuming
	 * @date 2015年12月17日 下午3:23:44
	 */
	public static String convertionString(String str, String strEncode) throws UnsupportedEncodingException {
		if (null == str) {
			return null;
		}
		byte[] b = str.getBytes(strEncode);// 编码
		String sa = new String(b, strEncode);// 解码:用什么字符集编码就用什么字符集解码
		return sa;
	}

	/**
	 * 判断是否存在乱码 存在乱码返回false
	 * 
	 * @param value
	 * @return
	 * @return boolean
	 * @author liuming
	 * @date 2015年12月17日 下午3:28:07
	 */
	public static boolean getErrorCode(String value) {

		for (String val : ERCOEDING) {
			int index = value.indexOf(val);
			if (index <= 0) {
				continue;
			}
			return false;
		}
		return true;
	}

	public static String[] ERCOEDING = { "й", "෨", "Ժ", "ۼ", "ҩ", "ල", "ɷ", "ص", "δ", "ġ", "Ϊ", "ط", "Ϣ", "ȡ", "Ӫ", "ã",
			"", "�" };

	public static String[] CHARSET = { "utf-8", "gbk", "gb2312", "gb18030", "big5" };

	/**
	 * 将乱码转化为正常的字符
	 * 
	 * @param str
	 * @param strEncode
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return String
	 * @author liuming
	 * @date 2015年12月17日 下午3:41:37
	 */
	public static String getRightStringFromMessy(String str) throws UnsupportedEncodingException {

		String temp = "";
		if (StringUtils.isNull(str)) {
			return "";
		}
		int i = 0;
		for (String val : CHARSET) { // 匹配不同编码格式
			temp = convertionString(str, val);
			boolean garbled = getErrorCode(str);// 判断编码是否错误
			if (garbled == true) {
				return temp;
			}

			if (garbled == false) {
				i++;
				if (i == 5) {
					return "乱码";
				}
				continue;
			}
		}
		return str;

	}

	/**
	 * 判断str2 在str1中出现的个数
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 * @return int
	 * @author liuming
	 * @date 2016年5月6日 下午3:59:39
	 */
	public static int countStr(String str1, String str2) {

		if (StringUtils.isNull(str1) || StringUtils.isNull(str2)) {
			return 0;
		}

		int counter = 0;
		if (str1.indexOf(str2) == -1) {
			return 0;
		}
		while (str1.indexOf(str2) != -1) {
			counter++;
			str1 = str1.substring(str1.indexOf(str2) + str2.length());
		}
		return counter;
	}

	/**
	 * 对数组中的 字符串的长度排序 0 正序 1 倒序
	 * 
	 * @param arrStr
	 * @return void
	 * @author liuming
	 * @date 2016年6月1日 下午6:45:42
	 */
	public static void sortStringArray(String[] arrStr, int flag) {
		// String[]str = new String[]{};

		String[] arrStrN = new String[arrStr.length];
		arrStrN = arrStr;

		String temp = null;
		for (int i = 0; i < arrStr.length; i++) {
			// for (int j = i; j <arrStr.length ; j++) {
			for (int j = arrStr.length - 1; j > i; j--) {
				if (flag == 1) {
					if (arrStr[i].length() < arrStr[j].length()) {
						temp = arrStr[i];
						arrStr[i] = arrStr[j];
						arrStr[j] = temp;

						// arrStr[i]="";
						// arrStrN[i] = arrStr[j];
					}
				}
				if (flag == 0) {
					if (arrStr[i].length() > arrStr[j].length()) {
						temp = arrStr[i];
						arrStr[i] = arrStr[j];
						arrStr[j] = temp;
					}
				}

			}
		}
		// str=arrStr;
	}

	public static void main(String[] args) {

		String s = "hjkfs>1.42<jierjk>0.0<jioiure";
		String ss[] = s.split("[^-?\\d+.?\\d*]");
		for (String k : ss)
			if (k.matches("-?\\d+.?\\d*"))
				System.out.println(k);

		// String regex = "^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";
		// Pattern pattern = Pattern.compile(regex);
		// Matcher matcher = pattern.matcher("我32.22");
		// while (matcher.find()) {
		// System.out.println(matcher.group(0));
		// }
		// System.out.println(isNumericDecimal("32.22我"));

		s = "-22我";
		String ss1[] = s.split("[^-?\\d+.?\\d*]");
		for (String k : ss1) {
			if (k.matches("-?\\d+.?\\d*")) {
				System.out.println(k);
			}
		}

		// System.out.println(StringUtils.isSameChars("【["));
		// String str = "强力清除广告，上网更快的浏览器！搞什么飞机嘛。我去；【";
		// System.out.println (StringUtils.QtoB(str));

		// System.out.println(StringUtils.isChinese("无"));
		// System.out.println(StringUtils.isHasHanziSpeChar("中11"));
		// System.out.println(StringUtils.isHasHanziSpeChar("！33"));

		// System.out.println(StringUtils.isNumeric("2141A4"));

		// String testStr = " 西式灯饰受欢迎 尽情演绎奢华味道";
		// // testStr = testStr.replaceAll("[ | ]", " ").trim();
		// testStr = testStr.trim();
		//
		// System.out.println(testStr.replace(" ", ""));
		//
		// if("".equals(" ".trim())){
		// System.out.println("半全相等");
		// }

		// String str = ""+",ab|cd||fdf!!sabcd//fd/sf ,abc fdfda()bc(abc,"+"" ;
		// String [] ary = ("" + str + "").split(",");
		// for (String string : ary) {
		// System.out.println("-----"+string);
		// }
		// System.out.println("ABC的个数 : " + (ary.length - 1));
		//
		// System.out.println(StringUtils.countCharacter(str, "fd"));
		//
		// String tempDepartment="青州市人民法院）".replace("（",
		// "(").replace("）", ")");
		// if( ( StringUtils.countCharacter(tempDepartment,
		// "\\(")==1&&StringUtils.countCharacter(tempDepartment, "\\)")==0 ) ){
		// tempDepartment = tempDepartment.replace("(", "");
		// }
		// if(( StringUtils.countCharacter(tempDepartment,
		// "\\)")==1&&StringUtils.countCharacter(tempDepartment, "\\(")==0 )){
		// tempDepartment = tempDepartment.replace(")", "");
		// }
		// System.out.println(tempDepartment);

		// 1. 用正则表达式处理, 不过好像一点都不省事..
		// Pattern p = Pattern.compile(",",Pattern.CASE_INSENSITIVE);
		// Matcher m = p.matcher(str);
		// int count = 0;
		// while(m.find()){
		// count ++;
		// }
		// System.out.println("ABC的个数 : " + count);

		// System.out.println(StringUtils.pickUpCardId("刘晴天 220104197209251817
		// 15143027777 ")
		// .replace(",", " "));
		// System.out.println(StringUtils.getSexFromIdCard("1326011988085401"));
		// System.out.println(StringUtils.isNumber("1 "));
		// System.out.println(StringUtils.pickUpManyCardId("刚志松132601198808296543、陈怀龙
		// 332601198808296543 432601198808296543 ").replace(",",
		// " "));

		// String []
		// gatherthes="刘晴天 22010419_7209\\251817, safd-AAA,:
		// BBB".split("[0-9,\\-,:,_,:,\\\\]");
		//
		// for (String string : gatherthes) {
		// if(null!=string&&!"".equals(string)){
		// System.out.print(string+"\n");//"©"
		// }
		// }

		// String address="上海^上海市@闵行区#吴中|路";
		// // String[] splitAddress=address.split("\\^|@|#|\\|");
		// String[] splitAddress=address.split("[\\^,@,\\#,\\|]");
		// for (String string : splitAddress) {
		// System.out.println(string);
		// }

		// System.out.println(splitAddress[0]+splitAddress[1]+splitAddress[2]+splitAddress[3]);

		/*
		 * StringUtils su = new StringUtils();
		 * System.out.println(su.isCardId("4306236704016432"));
		 * 
		 * System.out.println(StringUtils.isCardId("4306236704016432"));
		 */
	}

	/**
	 * 去() 及内容
	 * 
	 * @param str
	 * @return
	 */
	public static String removeBlank_GuoHao(String str) {

		if (StringUtils.isNull(str)) {
			return null;
		}
		String sss = str;
		sss = sss.replace("(", "（").replace(")", "）").replace("【", "（").replace("】", "）").replace("[", "（")
				.replace("]", "）").replace("『", "（").replace("』", "）");
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
	 * 判断是不是网址
	 * 
	 * @param str
	 * @return
	 * @return Boolean
	 * @author liuming
	 * @date 2016年10月20日 下午2:47:05
	 */
	public static Boolean isWebsite(String str) {

		if (StringUtils.isNull(str)) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");

		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是不是邮箱
	 * 
	 * @param email
	 * @return
	 * @return boolean
	 * @author liuming
	 * @date 2016年10月20日 下午3:04:26
	 */
	public static boolean isEmail(String email) {

		if (StringUtils.isNull(email)) {
			return false;
		}
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 提取字符串中的多个身份证
	 * 
	 * @param str
	 * @return
	 */
	public static String pickUpManyCardId(String str) {

		StringBuffer bf = new StringBuffer();
		List<Term> lists = HanLP.segment(str);
		for (Term term : lists) {

			if (term.nature == Nature.m) {
				Matcher matcher = patternCardId.matcher(term.word);
				if (matcher.matches()) {
					bf.append(term.word).append(",");
				}
			}
		}

		int len = bf.length();
		if (len > 0) {
			bf.deleteCharAt(len - 1);
		}
		return bf.toString();
	}

	/**
	 * 去 [\n,\t,\r,\\s,&nbsp;]
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSepar(String str) {
		if (StringUtils.isNull(str)) {
			return null;
		}
		return str.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "");
	}

	/**
	 * 判断是不是字符 1个以上 只要有一个"a"就返回true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isCharacter(String str) {
		String regEx = "^[a-zA-Z]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 去字符 "abc ABC"等
	 * 
	 * @param str
	 * @return
	 */
	public static String removeCharacter(String str) {
		if (StringUtils.isNull(str)) {
			return null;
		}
		return str.replaceAll("[a-zA-Z]+", "");
	}

	/**
	 * 取字符串的数字 转double 及控制小数点位数 四舍五入
	 * 
	 * @param str
	 * @return
	 */
	public static Double getStringToDouble(String str, int num) {
		if (null == str || "".equals(str)) {
			return null;
		}
		str = str.replaceAll("[\\n,\\t,\\r,\\s,&nbsp;]", "").replace(" ", "");
		if ("".equals(str)) {
			return null;
		}

		String[] regsQ = { ",", ".", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
		String[] rs = str.split("");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < rs.length; i++) {
			String jj = rs[i];

			for (int j = 0; j < regsQ.length; j++) {
				if (jj.equals(regsQ[j])) {
					sb.append(regsQ[j]);
					break;
				}
			}
		}
		String rel = sb.toString().replace(",", "");

		if (!StringUtils.isNull(rel) && StringUtils.isNumericDecimal(rel)) {

			BigDecimal b = new BigDecimal(rel);
			Double f1 = b.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;

			// DecimalFormat df = new DecimalFormat("#.###");
			// Double get_double = Double.parseDouble(df.format(rel));
			// return get_double;

			// return Double.valueOf(rel);

		} else {
			return null;
		}

	}

	/**
	 * 包含[A-W,Y-Z,a-w,y-z]
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasCharAtoZNotX(String str) {

		String regEx = "[A-W,Y-Z,a-w,y-z]+";

		boolean result = Pattern.compile(regEx).matcher(str).find();
		return result;
	}

	static String[] units = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿" };
	static char[] numArray = { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九' };

	/**
	 * 将数字转化为大写    没有解决  12  100等
	 * 
	 * @Description: TODO
	 * @param num
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年9月29日 下午3:24:13
	 */
//	public static String formatIntegerToMax(int num) {
//		char[] val = String.valueOf(num).toCharArray();
//		int len = val.length;
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < len; i++) {
//			String m = val[i] + "";
//			int n = Integer.valueOf(m);
//			boolean isZero = n == 0;
//			String unit = units[(len - 1) - i];
//			if (isZero) {
//				if ('0' == val[i - 1]) {
//					// not need process if the last digital bits is 0
//					continue;
//				} else {
//					
//					//最后一个是不是0 
//					if(i==len-1  && n==0){
////						break;
//						continue;
//					}
//					// no unit for 0
//					sb.append(numArray[n]);
//				}
//			} else {
//				sb.append(numArray[n]);
//				sb.append(unit);
//			}
//		}
//		return sb.toString();
//	}
//	
	

	/**
	 * 生成UUID
	 */
	public static String getUUID(String value) {
		NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
		return nbg.generate(value).toString();
	}

//	/**
//	 * 判断是否包含字母
//	 * 
//	 * @param str
//	 * @return
//	 * @return String
//	 * @author liuming
//	 * @date 2016年11月7日 上午10:15:03
//	 */
//	public static Boolean isIncludeLetter(String str) {
//
//		if (StringUtils.isNull(str)) {
//			return false;
//		}
//		for (int i = 0; i < str.length(); i++) { // 循环遍历字符串
//			if (Character.isLetter(str.charAt(i))) { // 用char包装类中的判断字母的方法判断每一个字符
//				return true;
//			}
//		}
//		return false;
//	}
	
	
	
	/**
	 * 判断是否包含英文字母  正则
	 * @param str
	 * @return   
	 * @return Boolean  
	 * @author liuming
	 * @date 2016年11月7日  上午10:23:41
	 */
	public static Boolean isIncludeLetterZZ(String str) {

		if (StringUtils.isNull(str)) {
			return false;
		}
		String regex=".*[a-zA-Z]+.*";
		Matcher m=Pattern.compile(regex).matcher(str);
		return m.matches();
	}
	
	
	/**
	 * 将字符串中的阿拉伯数字替换为中文的数字                《中华人民共和国道路交通安全法实施条例》第1000条第30款（10）项                 ====>        《中华人民共和国道路交通安全法实施条例》第一千条第三十款（十）项
	 * @param str
	 * @return   
	 * @return String  
	 * @author liuming
	 * @date 2016年11月7日  下午5:13:42
	 */
	public static String  replaceSZ(String str) {
		Pattern p = Pattern.compile("[0-9\\.]+");
		Matcher m = p.matcher(str);
		if(m.find()){
			
			String m_t=m.group();
			String m_1= str.substring(0, str.indexOf(m_t)    );
			String m_2= str.substring(  str.indexOf(m_t) +m_t.length()  );
			
			Integer ta = Integer.valueOf(m_t);
			String s_max = StringUtils.getChinseSZ(ta);
			
			String res=m_1+s_max+m_2;
			
			return replaceSZ(res);
		}else{
			return str;
		}
	}
	
	
	
	/**
	 * 将数字转化为大写  五位数
	 * @param intInput
	 * @return   
	 * @return String  
	 * @author liuming
	 * @date 2016年11月7日  下午5:01:56
	 */
	public static String getChinseSZ(int intInput){
		if(intInput==0){
			return "零";
		}
		String re=ToCH(intInput);
		//去最后一个零
		if(!StringUtils.isNull(re)){
			
			
			if("零".equals(     re.substring(re.length()-1) )){
				re=re.substring(0, re.length()-1);
			}
			
		}else{
			return "";
		}
		return re;
	}
	
	
	
	public static String ToCH(int intInput) {  
        String si = String.valueOf(intInput);  
        String sd = "";  
        if (si.length() == 1) // 個  
        {  
            sd += GetCH(intInput);  
            return sd;  
        } else if (si.length() == 2)// 十  
        {  
            if (si.substring(0, 1).equals("1"))  
                sd += "十";  
            else  
                sd += (GetCH(intInput / 10) + "十");  
            sd += ToCH(intInput % 10);  
        } else if (si.length() == 3)// 百  
        {  
            sd += (GetCH(intInput / 100) + "百");  
            if (String.valueOf(intInput % 100).length() < 2)  
                sd += "零";  
            sd += ToCH(intInput % 100);  
        } else if (si.length() == 4)// 千  
        {  
            sd += (GetCH(intInput / 1000) + "千");  
            if (String.valueOf(intInput % 1000).length() < 3)  
                sd += "零";  
            sd += ToCH(intInput % 1000);  
        } else if (si.length() == 5)// 萬  
        {  
            sd += (GetCH(intInput / 10000) + "万");  
            if (String.valueOf(intInput % 10000).length() < 4)  
                sd += "零";  
            sd += ToCH(intInput % 10000);  
        }  
  
        return sd;  
    }  
  
    private static String GetCH(int input) {  
        String sd = "";  
        switch (input) {  
        case 1:  
            sd = "一";  
            break;  
        case 2:  
            sd = "二";  
            break;  
        case 3:  
            sd = "三";  
            break;  
        case 4:  
            sd = "四";  
            break;  
        case 5:  
            sd = "五";  
            break;  
        case 6:  
            sd = "六";  
            break;  
        case 7:  
            sd = "七";  
            break;  
        case 8:  
            sd = "八";  
            break;  
        case 9:  
            sd = "九";  
            break;  
        default:  
            break;  
        }  
        return sd;  
    }  
    
    
    /**
     * 判断字符串是不是大于1M
     * @param tempT
     * @return   
     * @return boolean  
     * @author liuming
     * @date 2016年11月17日  上午10:06:13
     */
    public static boolean isBig1M(String tempT){
    	
    	if(StringUtils.isNull(tempT)){
    		return false;
    	}
    	long size = tempT.getBytes().length;
		if (size > 1 * 1024 * 1024) {
			return true;
		}else{
			return false;
		}
    }

}
