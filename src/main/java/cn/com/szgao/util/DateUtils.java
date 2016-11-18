package cn.com.szgao.util;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class DateUtils {
	private static Logger log = LogManager.getLogger(DateUtils.class);

	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}

	/**
	 * 得到当前时间 字符串 yyyyMMddhhmmss
	 * 
	 * @return
	 * @author liuming
	 * @Date 2015-9-2 上午11:32:17
	 */
	public static String getDateyyyyMMddhhmmss() {
		// TODO Auto-generated method stub
		java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyyyMMddhhmmss");
		String s = format2.format(new Date());
		return s;
	}

	/**
	 * 得到当前时间 字符串yyyy-MM-dd'T'HH:mm:ssZZ
	 * 
	 * @return
	 * @author liuming
	 * @Date 2015-9-2 上午11:32:17
	 */
	public static String getDateyyyyMMddhhmmssZZ() {
		// TODO Auto-generated method stub
		java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
		String s = format2.format(new Date());
		return s;
	}

	/**
	 * 得到当前时间yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 * @author liuming yyyy-MM-dd hh:mm:ss
	 * @Date 2015-9-10 下午6:33:14
	 */
	public static String getDateyyyy_MMddhhmmss() {
		// 写异常
		// 获取时间，系统时间System.currentTimeMillis()
		Date date = new Date(System.currentTimeMillis());
		// 将时间格式化
		SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strtime = sdftime.format(date);
		return strtime;
	}

	/***
	 * yyyy年MM月dd日转化为 yyyy-MM-dd
	 * 
	 * @param str
	 * @return
	 */
	public static Date toYMDOfCha(String str) {
		if (StringUtils.isNull(str)) {
			return null;
		}
		Date dd = null;

		if (str.contains("年") && str.contains("月") && str.contains("日")) {
			// String year=str.substring(0, str.indexOf("年")) ;
			// String mon=str.substring(str.indexOf("年")+1, str.indexOf("月"));
			// String day=str.substring(str.indexOf("月")+1, str.indexOf("日"));
			// String strNew=year + "-"+mon+"-"+day;
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			// try {
			// dd=formatter.parse(strNew) ;
			// } catch (ParseException e) {
			// e.printStackTrace();
			// }
			// year=null;
			// mon=null;
			// day=null;
			// strNew=null;
			// formatter=null;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			try {
				dd = sdf.parse(str);

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dd;
	}

	public static String toYMDOfChaStr(String str) {
		if (StringUtils.isNull(str)) {
			return null;
		}
		Date dd = null;
		String strTemp = null;
		if (str.contains("年") && str.contains("月") && str.contains("日")) {
			// String year=str.substring(0, str.indexOf("年")) ;
			// String mon=str.substring(str.indexOf("年")+1, str.indexOf("月"));
			// String day=str.substring(str.indexOf("月")+1, str.indexOf("日"));
			// String strNew=year + "-"+mon+"-"+day;
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			// try {
			// dd=formatter.parse(strNew) ;
			// } catch (ParseException e) {
			// e.printStackTrace();
			// }
			// year=null;
			// mon=null;
			// day=null;
			// strNew=null;
			// formatter=null;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd");
				strTemp = sdftime.format(dd);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return strTemp;
	}

	// /**
	// * 获取现在时间
	// *
	// * @return返回长时间格式 yyyy-MM-dd HH:mm:ss
	// */
	// public static Date getNowDate() {
	// Date currentTime = new Date();
	// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// String dateString = formatter.format(currentTime);
	// ParsePosition pos = new ParsePosition(8);
	// Date currentTime_2 = formatter.parse(dateString, pos);
	// return currentTime_2;
	// }
	//
	// /**
	// * 获取现在时间
	// *
	// * @return返回短时间格式 yyyy-MM-dd
	// */
	// public static Date getNowDateShort() {
	// Date currentTime = new Date();
	// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	// String dateString = formatter.format(currentTime);
	// ParsePosition pos = new ParsePosition(8);
	// Date currentTime_2 = formatter.parse(dateString, pos);
	// return currentTime_2;
	// }

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * "yyyy-MM-dd'T'HH:mm:ssZZ 字符串格式转为 Date
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateFromZZ(String strDate) {
		strDate = strDate.replace("T", " ").substring(0, strDate.indexOf("+"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 1 前者时间近 -1 0 时间比较
	 * 
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static int compareDate(Date dt1, Date dt2) {
		if (dt1.getTime() > dt2.getTime()) {
			// System.out.println("dt1 在dt2前");
			return 1;
		} else if (dt1.getTime() < dt2.getTime()) {
			// System.out.println("dt1在dt2后");
			return -1;
		} else {// 相等
			return 0;
		}
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateShort(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStrLong(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	public static String toYMDOfChaStr_ES(String str) {
		if (StringUtils.isNull(str)) {
			return null;
		}
		String strTemp = null;
		try {
			Date dd = strToDateLong(str);

			SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
			// SimpleDateFormat sdftime = new
			// SimpleDateFormat("yyyy-MM-dd'T'HHmmssZ");
			strTemp = sdftime.format(dd);
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
		return strTemp;
	}

	/**
	 * 不同格式的时间转为ES需要的时间格式 SimpleDateFormat formatter = new SimpleDateFormat(
	 * "yyyy-MM-dd HH:mm:ss"); SimpleDateFormat formatter = new
	 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat sdf = new
	 * SimpleDateFormat("yyyy年MM月dd日"); 都转化为 SimpleDateFormat sdftime = new
	 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
	 * 
	 * @param str
	 * @param formatter
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年1月19日 下午3:40:30
	 */
	public static String toYMDOfChaStr_ES(String str, SimpleDateFormat formatter) {

		if (StringUtils.isNull(str)) {
			return null;
		}
		Date dd = null;
		String strTemp = null;
		if (str.contains("年") && str.contains("月") && str.contains("日")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
				strTemp = sdftime.format(dd);
				return strTemp;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		try {
			ParsePosition pos = new ParsePosition(0);
			dd = formatter.parse(str, pos);
			SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
			strTemp = sdftime.format(dd);
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
		return strTemp;
	}

	/**
	 * 将日期STRING转化为 年-日-日
	 * 
	 * yyyy-MM-dd'T'HH:mm:ssZZ -----------> 2013-09-30T00:00:00+0800
	 * 
	 * @param str
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年1月19日 下午4:10:43
	 */
	@SuppressWarnings("deprecation")
	public static String toYMDOfChaStr_ESZZ(String str) {

		if (StringUtils.isNull(str)) {
			return null;
		}
		try {
			String regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}[+][0][8][0][0]$";
			Matcher matcher2 = Pattern.compile(regex).matcher(str);
			if (matcher2.matches()) {
				return str;
			}
		} catch (Exception e) {

		}

		SimpleDateFormat sdf = null;
		Date dd = null;
		String strTemp = null;
		if (str.contains("年") && str.contains("月") && str.contains("日")) {
			sdf = new SimpleDateFormat("yyyy年MM月dd日");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (ParseException e) {
				return null;
			}
		} else if (str.indexOf("+0800") != -1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (ParseException e) {
				return null;
			}
		} else if (str.indexOf("CST") != -1) {
			// sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				// dd = sdf.parse(str);
				dd = new Date(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (Exception e) {
				return null;
			}
		} else if (str.indexOf("-") != -1 && str.indexOf(":") != -1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (ParseException e) {
				return null;
			}
		} else if (str.indexOf("-") != -1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (ParseException e) {
				return null;
			}
		}
		return strTemp;
	}

	/**
	 * string 转 yyyy-MM-dd'T'HH:mm:ssZZ
	 * 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年1月27日 下午9:29:43
	 */
	public static String toYMDOfChaStr_ESZZ2(String str) {

		String openTime_temp_new = null;
		String openTime_temp = null;
		if (StringUtils.isNull(str)) {
			return null;
		}

		// 处理 2015-09-02T04:07:01.658587+08:00
		if (str.contains(".") && str.contains("+") && str.contains("T")) {

			String str1 = str.replace("T", " ");
			SimpleDateFormat sdftime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				Date dd1 = sdftime1.parse(str1);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
				String strTemp = sdftime.format(dd1);
				return strTemp;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 处理 2016-02-20 02:38:59
		String regex3 = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$";
		Matcher matcher3 = Pattern.compile(regex3).matcher(str);
		if (matcher3.matches()) {
			SimpleDateFormat sdftime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date dd1 = sdftime1.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
				String strTemp = sdftime.format(dd1);
				return strTemp;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		String regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}[+][0][8][0][0]$";
		Matcher matcher2 = Pattern.compile(regex).matcher(str);
		if (matcher2.matches()) {
			return str;
		}
		try {
			openTime_temp = DateUtils.formatDate(str);
		} catch (ParseException e) {
		}

		if (StringUtils.isNull(openTime_temp)) {
			openTime_temp = DateUtils.toChiDate_YMD(str); // ------------------------------《》问题，
		}

		if (!StringUtils.isNull(openTime_temp)) {
			openTime_temp_new = DateUtils.toYMDOfChaStr_ESZZ(openTime_temp);
		}

		return openTime_temp_new;
	}
	
	
	/**
	 * 统一日期格式
	 * 
	 * @param value
	 * @return
	 */
	public static String getReplaceAllDate(String value) {
		StringBuffer sb = null;
		if (value != null && !"".equals(value)) {
			value = value.replaceAll("[（,）,(,),【,】,{,},<,>]", "");
			
//			value = value.replaceAll("[-,-,/,\",年,月]", "-");
//			value=value.replace("日", "");
			
			
			value = value.replaceAll("[:,：]", ":");
			value = value.replace("]", "");
			value = value.replace("[", "");
			
			
		
			
			value = value.trim();
			sb = new StringBuffer();
			sb.append(value);
		}
		return sb == null ? null : sb.toString();
	}
	

	/**
	 * 格式化日期 各种格式转为
	 * 
	 * yyyyMMdd yyyy-MM-dd yyyy/MM/dd yyyy.MM.dd CST
	 * 
	 * yyyy年MM月dd日
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unused")
	public static String formatDate(String date) throws ParseException {
		String result = null;
		SimpleDateFormat sf = null;
		try {

			if (null != date) {
				// yyyy年MM月dd日 情况
//				if (date.contains("年") && date.contains("月") && date.contains("日")) {
//					result = date;
//					return result;
//				}

				SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy年MM月dd日");
				try {
					format.parse(date);
					return date;
				} catch (Exception e) {
					
				}
				

				String yyymmdd = "^[12]\\d{3}(0\\d|1[0-2])([0-2]\\d|3[01])$";// yyyyMMdd
				String yyyy_mm_dd = "^[0-9]{4}-[0-9]{1,}-[0-9]{1,}$";// yyyy-MM-dd
				String yyyy = "^\\d{4}\\/\\d{1,}\\/\\d{1,}$";// yyyy/MM/dd
				String yyyyD = "^\\d{4}\\.\\d{1,}\\.\\d{1,}$";// yyyy.MM.dd
				String yyyYD = "^\\d{4}$";// yyyy\

				// yyyyMMdd 情况
				Pattern pattern = Pattern.compile(yyymmdd);
				Matcher matcher = pattern.matcher(date);
				if (matcher.matches()) {
					sf = new SimpleDateFormat("yyyyMMdd");
					Date d = sf.parse(date);
					sf = null;
					sf = new SimpleDateFormat("yyyy年MM月dd日");
					result = sf.format(d);
					sf = null;
				} else {
					// yyyy-MM-dd 情况
					pattern = Pattern.compile(yyyy_mm_dd);
					matcher = pattern.matcher(date);
					if (matcher.matches()) {
						sf = new SimpleDateFormat("yyyy-MM-dd");
						Date d = sf.parse(date);
						sf = null;
						sf = new SimpleDateFormat("yyyy年MM月dd日");
						result = sf.format(d);
						sf = null;
					} else {
						// yyyy/MM/dd 情况
						pattern = Pattern.compile(yyyy);
						matcher = pattern.matcher(date);
						if (matcher.matches()) {
							sf = new SimpleDateFormat("yyyy/MM/dd");
							Date d = sf.parse(date);
							sf = null;
							sf = new SimpleDateFormat("yyyy年MM月dd日");
							result = sf.format(d);
							sf = null;
						}

						else {
							// yyyy.MM.dd 情况
							pattern = Pattern.compile(yyyyD);
							matcher = pattern.matcher(date);
							if (matcher.matches()) {
								sf = new SimpleDateFormat("yyyy.MM.dd");
								Date d = sf.parse(date);
								sf = null;
								sf = new SimpleDateFormat("yyyy年MM月dd日");
								result = sf.format(d);
								sf = null;
							} else {
								int index_xie = date.indexOf("/");
								int index_mao = date.indexOf(":");
								if (4 == index_xie && -1 != index_mao) {
									String newDate = date.substring(0, date.indexOf(" "));
									// yyyy/MM/dd hh:mm:ss 情况
									pattern = Pattern.compile(yyyy);
									matcher = pattern.matcher(newDate);
									if (matcher.matches()) {
										sf = new SimpleDateFormat("yyyy/MM/dd");
										Date d = sf.parse(newDate);
										sf = null;
										sf = new SimpleDateFormat("yyyy年MM月dd日");
										result = sf.format(d);
										sf = null;
									}
								} else {
									int index_xie2 = date.indexOf("-");
									int index_mao2 = date.indexOf(":");
									if (4 == index_xie2 && -1 != index_mao2) {
										String newDate = "";
										if (-1 != date.indexOf(" ")) {
											newDate = date.substring(0, date.indexOf(" "));
										} else {
											newDate = date.replace(" ", "").substring(0, date.indexOf(":") - 2);
										}

										// String newDate = date.replace(" ",
										// "").substring(0, date.indexOf(":") -
										// 2);
										// yyyy-MM-dd hh:mm:ss 情况
										pattern = Pattern.compile(yyyy_mm_dd);
										matcher = pattern.matcher(newDate);
										if (matcher.matches()) {
											sf = new SimpleDateFormat("yyyy-MM-dd");
											Date d = sf.parse(newDate);
											sf = null;
											sf = new SimpleDateFormat("yyyy年MM月dd日");
											result = sf.format(d);
											sf = null;
										} else {
											newDate = date.replace(" ", "").substring(0, date.indexOf(":") - 1);
											// yyyy-MM-dd hh:mm:ss 情况
											pattern = Pattern.compile(yyyy_mm_dd);
											matcher = pattern.matcher(newDate);
											if (matcher.matches()) {
												sf = new SimpleDateFormat("yyyy-MM-dd");
												Date d = sf.parse(newDate);
												sf = null;
												sf = new SimpleDateFormat("yyyy年MM月dd日");
												result = sf.format(d);
												sf = null;
											}

											else {
												// 处理：Mon Apr 22 00:00:00 CST
												// 2013格式
												if (date.contains("CST")) {
													sf = new SimpleDateFormat("yyyy年MM月dd日");
													@SuppressWarnings("deprecation")
													Date dt = new Date(date);
													result = sf.format(dt);
													sf = null;
													dt = null;
												}
											}
										}

									}

								}

							}
						}

					}
				}
			}
		} catch (

		Exception e)

		{
			log.error("formatDate 异常: " + e.getMessage());
			return null;
		}
		return result;

	}

	/**
	 * yyyy-MM-dd'T'HH:mm:ssZZ to yyyy年MM月dd日
	 * 
	 * @Description: TODO
	 * @param strDate
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年1月23日 下午7:14:50
	 */
	public static String toYMDFromZZ(String strDate) {
		String dateString = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
			ParsePosition pos = new ParsePosition(0);
			Date strtodate = formatter.parse(strDate, pos);
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy年MM月dd日");
			dateString = formatter2.format(strtodate);
		} catch (Exception e) {
			return null;
		}
		return dateString;
	}

	/**
	 * 将日期STRING转化为 yyyy-MM-dd'T'HH:mm:ssZZ -----------> 2013-09-30
	 * 
	 * @param str
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年1月19日 下午4:10:43
	 */
	@SuppressWarnings("deprecation")
	public static String toYMDOfChaStr_ESDD(String str) {

		if (StringUtils.isNull(str)) {
			return null;
		}
		SimpleDateFormat sdf = null;
		Date dd = null;
		String strTemp = null;
		if (str.contains("年") && str.contains("月") && str.contains("日")) {
			sdf = new SimpleDateFormat("yyyy年MM月dd日");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (ParseException e) {
				return null;
			}
		} else if (str.indexOf("+0800") != -1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (ParseException e) {
				return null;
			}
		} else if (str.indexOf("-") != -1 && str.indexOf(":") != -1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (ParseException e) {
				return null;
			}
		}

		else if (str.indexOf("CST") != -1) {
			// sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				// dd = sdf.parse(str);
				dd = new Date(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (Exception e) {
				return null;
			}
		} else if (str.indexOf("-") != -1) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dd = sdf.parse(str);
				SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd");
				strTemp = sdftime.format(dd);
				sdf = null;
				sdftime = null;
				return strTemp;
			} catch (ParseException e) {
				return null;
			}
		}
		return strTemp;
	}

	/**
	 * 将 二Ｏ一Ｏ年十月三十一日 转为了 2010年10月31日
	 * 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年1月23日 上午11:57:49
	 */
	public static String toChiDate_YMD(String str) {

		if (StringUtils.isNull(str)) {
			return null;
		}
		String result = null;

		result = str.replaceAll("[（,）,(,),【,】,{,},<,>]", "");
		result = result.replace("三十一", "31");
		result = result.replace("三十", "30");
		result = result.replace("二十九", "29");
		result = result.replace("二十八", "28");
		result = result.replace("二十七", "27");
		result = result.replace("二十六", "26");
		result = result.replace("二十五", "25");
		result = result.replace("二十四", "24");
		result = result.replace("二十三", "23");
		result = result.replace("二十二", "22");
		result = result.replace("二十一", "21");
		result = result.replace("二十", "20");
		result = result.replace("十九", "19");
		result = result.replace("十八", "18");
		result = result.replace("十七", "17");
		result = result.replace("十六", "16");
		result = result.replace("十五", "15");
		result = result.replace("十四", "14");
		result = result.replace("十三", "13");
		result = result.replace("十二", "12");
		result = result.replace("十一", "11");
		result = result.replace("十", "10");
		result = result.replace("九", "9");
		result = result.replace("八", "8");
		result = result.replace("七", "7");
		result = result.replace("六", "6");
		result = result.replace("五", "5");
		result = result.replace("四", "4");
		result = result.replace("三", "3");
		result = result.replace("二", "2");
		result = result.replace("一", "1");
		result = result.replace("元", "1");

		result = result.replaceAll("[Ｏ,〇,0,o,O,０,○]", "0");
		return result;
	}

	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * 
	 * @param dateDate
	 * @param k
	 * @return
	 */
	public static String dateToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将 字符串转为 yyyy-MM-dd'T'HH:mm:ssZZ ---yyyy-MM-dd HH:mm:ss
	 * 
	 * @param str
	 * @return
	 */
	public static String toZZTommss(String str) {
		Date dd = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
		try {
			dd = sdf.parse(str);
			SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdftime.format(dd);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 两个时间比较 1 前时间大 2 前时间小
	 * 
	 * @param DATE1
	 * @param DATE2
	 * @return
	 * @return int
	 * @author liuming
	 * @date 2016年11月1日 下午2:16:54
	 */
	public static int compareDate(String DATE1, String DATE2) { // 1900-01-01
																// 14:10:43
																// 2016-02-15
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				// System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				// System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	static String t_start = "1900-01-01 14:10:43";
	static String t_end = "";

	static {
		Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(format.format(new Date()));
		t_end = format.format(new Date());
	}

	/**
	 * 得到正确的日期 1900~~~~~当前时间
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getOkDate(String str) {
		String rel = null;

		if (StringUtils.isNull(str)) {
			return null;
		} else {
			String t = DateUtils.toZZTommss(str);

			try {
				if (DateUtils.compareDate(t_start, t) == 1 || DateUtils.compareDate(t, t_end) == 1) {
					return null;
				} else {
					return str;
				}
			} catch (Exception e) {
				return null;
			}

		}
	}

	/**
	 * 提取字符串中的时间 yyyy-MM-dd
	 * 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月14日 下午6:05:33
	 */
	public static String getDateFromStringYYYMMDD(String str) {
		if (StringUtils.isNull(str)) {
			return null;
		}
		String reslut = null;

		// String reg = "yyyy-MM-dd";
		String reg = "\\d{4}-(1[0-2]|0?[1-9])-([1-2][0-9]|3[0-1]|0?[1-9])";

		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);

		if (matcher.find()) {
			reslut = matcher.group(0);
		}

		return reslut;
	}

	/**
	 * "yyyy-MM-dd'T'HH:mm:ssZZ" 转 "yyyy年MM月dd日"
	 * 
	 * @param strDate
	 * @return
	 */
	public static String strZZToYMD(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
		Date dd = null;
		try {
			dd = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat sdftime = new SimpleDateFormat("yyyy年MM月dd日");
		String strTemp = sdftime.format(dd);
		return strTemp;
	}

	public static void main(String[] args) {
		System.out.println(getOkDate("3016-08-30T00:00:00+0800"));
	}

}
