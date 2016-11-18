package cn.com.szgao.clean.court;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.postgresql.jdbc2.ArrayAssistantRegistry;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.StringUtils;

/**
 * 取文书的案由
 * 
 * @author liuming
 * @ClassName ExtractionHtmlCaseCause
 * @date 2016年5月25日 下午4:19:15
 */
public class ExtractionHtmlCaseCause {

	public static void main(String[] args) throws IOException {
		PropertyConfigurator  
		.configure("D:\\data\\wenhao\\Mass\\Mass\\log\\log4j.properties");
		long da = System.currentTimeMillis();

		File file = new File("D:\\lm\\log\\民事案件-2016-01-04\\88261f10-92d0-5f71-8340-2c8a86a8882b.html");
//		File file = new File("D:\\lm\\log\\民事案件-2016-01-04");
		
//		88261f10-92d0-5f71-8340-2c8a86a8882b.html

	 
		try {

			show(file);
			Statistics(file);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
		} finally {
			file = null;
		}

		logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis() - da) / 1000)) + "秒数");
		logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 1)) + "分钟");
		logger.info("所有文件总耗时" + (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 60)) + "小时");
		logger.info("平均每秒" + (float) (SUM / (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (1 * 1))));
		logger.info("平均每分" + (float) (SUM / (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 1))));
		logger.info("平均每时" + (float) (SUM / (float) ((float) ((System.currentTimeMillis() - da) / 1000) / (60 * 60))));
	}
	
	static int countNum = 0;
	static int countNotHtml = 0;
	
	/**
	 * 生成文件的个数
	 */
	static int countP = 0;
	
	static BufferedWriter fwUn2 = null;
	static BufferedWriter fwUn = null;
	static{
		String filePathUn="D:/lm/log/案号2.txt";
		File fileSUn = new File(filePathUn);
		String encoding_from1U = "UTF-8";

		try {
			if (!fileSUn.exists()) {
				try {
					fileSUn.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fwUn2 = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // 指定编码格式，以免读取时中文字符异常
			} else {
//				fileSUn.delete();
//				fileSUn = new File(filePathUn);
//				try {
//					fwUn2 = new BufferedWriter(
//							new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} // 指定编码格式，以免读取时中文字符异常
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
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
	

	/**
	 * 递归遍历html文件
	 * 
	 * @param file
	 * @param province
	 * @throws Exception
	 */
	private static void show(File file) throws Exception {
		String html = null;
		Document doc;
		int i = 0;

		if (file.isFile()) {
			SUM++;
			System.out.println(SUM);
			String suffix = file.getName();
			suffix = suffix.substring(suffix.indexOf(".") + 1, suffix.length());
			suffix = MAPS.get(suffix);
			if (null == suffix) {
				NOSTANDARD++;
				logger.error("非标准数据：" + NOSTANDARD + "  " + file.getPath());
				return;
			}
			if ("swf".equals(suffix)) {
				NOSTANDARDSWF++;
				logger.info("SWF数据：" + NOSTANDARDSWF + "  " + file.getPath());
				insertDataToBlob(file);
				return;
			}
			if ("doc".equals(suffix)) {
				NOSTANDARDDOC++;
				logger.info("DOC数据：" + NOSTANDARDDOC + "  " + file.getPath());
				insertDataToBlob(file);
				return;
			}

//			logger.info(SUM + "  路径:" + file.getPath());
			for (String val : charset) { // 匹配不同编码格式
				try {
					doc = Jsoup.parse(file, val);
				} catch (Exception e) {
					NOSUM++;
					logger.error("空数据：" + NOSUM + "  " + file.getPath());
					return;
				}
				String html2 = doc.text();

				if (html2 == null || "" == html2) {
					NOSUM++;
					logger.error("空数据：" + NOSUM + "  " + file.getPath());
					continue;
				}
				boolean garbled = getErrorCode(html2);// 判断编码是否错误
				if (garbled == false) {
					i++;
					if (i == 5) {
						html2 = null;
						ERRORSUM++;
						logger.error("异常字符错误条数：" + ERRORSUM + "  " + file.getPath());
					}
					continue;
				}

			 
				System.out.println(file.getPath());
				
				if (countNum % 100 == 0) {
					countP++;
					String folderPathUn = "D:/lm/log/2016法院清洗案由5";
					String filePathUn = "D:\\lm\\log\\2016法院清洗案由5\\" + (countP) + ".json";
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
				

//				html = doc.body()
//						.getElementsByAttributeValue("style",
//								"LINE-HEIGHT: 25pt;TEXT-ALIGN:justify;TEXT-JUSTIFY:inter-ideograph; TEXT-INDENT: 30pt; MARGIN: 0.5pt 0cm;FONT-FAMILY: 仿宋; FONT-SIZE: 16pt;")
//						.text();
//				Elements el = doc.body().getElementsByAttributeValue("style",
//						"LINE-HEIGHT: 25pt;TEXT-ALIGN:justify;TEXT-JUSTIFY:inter-ideograph; TEXT-INDENT: 30pt; MARGIN: 0.5pt 0cm;FONT-FAMILY: 仿宋; FONT-SIZE: 16pt;");
//				for (Element element : el) {
//					String s = element.text();
//					System.out.println(s);
//				}
//				System.out.println(html);
				
				
				
				
				int fa=0;//判断第一个a标签前是不是出现过当事人信息，如果大于0说明出现
				Elements all=doc.body().getAllElements();
				for (Element element_all : all) {
					String tagName=element_all.tagName();
//					System.out.println(tagName);
					
					if("div".equals(tagName)){
						Attributes as=element_all.attributes();
						for (Attribute attribute : as) {
//							System.out.println(attribute.getKey()); 
//							System.out.println(  attribute.getValue() );
							
							if("style".equals( attribute.getKey())&&"LINE-HEIGHT: 25pt;TEXT-ALIGN:justify;TEXT-JUSTIFY:inter-ideograph; TEXT-INDENT: 30pt; MARGIN: 0.5pt 0cm;FONT-FAMILY: 仿宋; FONT-SIZE: 16pt;".equals(attribute.getValue())){
//								System.out.println(element_all.text());
								
								String temp1=element_all.text();
								String temp2=getCase(temp1);
								System.out.println(temp2);
//								writerString(fwUn, temp2+"|"+file.getPath());
								writerString(fwUn, temp2 );
								countNum++;
								fa++;
							}
						}
					}
					
					
					if(fa>0){
						if("a".equals(tagName)){
							 break;
						}
					}
					
					//style   LINE-HEIGHT: 25pt;TEXT-ALIGN:justify;TEXT-JUSTIFY:inter-ideograph; TEXT-INDENT: 30pt; MARGIN: 0.5pt 0cm;FONT-FAMILY: 仿宋; FONT-SIZE: 16pt;
				
//					attributes() to get all attributes
					
//					System.out.println(element_all.html());
//					System.out.println(element_all.text());
				}
				

				// if (file.getPath().contains("Ok文档")) {
				//// html = getContent(doc, null, null, "公告", false);
				//// if(!StringUtils.isNull(html)){
				//// if(html.contains("裁判文书公开是依据国家有关法律及最高人民法院等有关规定，相关事宜请与各审判法院联系。")){
				//// html
				// =html.replaceAll("裁判文书公开是依据国家有关法律及最高人民法院等有关规定，相关事宜请与各审判法院联系。",
				// "");
				//// }
				//// }
				// html = getContent(doc, null, null, "公告",
				// false,null,null,null,new
				// ArrayList<String>(Arrays.asList("裁判文书公开是依据国家有关法律及最高人民法院等有关规定，相关事宜请与各审判法院联系。","PAGE[0-9]*","-[0-9]*-"))
				// );
				//
				// if(!StringUtils.isNull(html)){
				// if(html.length()>10){
				// String temp=html.substring(html.length()-7);
				// if(StringUtils.hasDigit(temp)){
				// temp=StringUtils.removeNum(temp);
				// }
				// html=html.substring(0,html.length()-7)+temp;
				// }
				// }
				// }

				// else {
				// html = getContent(doc, "center", null, "公告", false);
				// }

				if (null == html) {
					NOSUM++;
//					logger.error("无关键数据：" + NOSUM + "  " + file.getPath());
					return;
				} else {
					// 判断是不是文书
					int flag = 0;
					for (String per : PERSONNEL) {
						if (html.contains(per)) {
							flag = 1;
							break;
						}
					}
					if (flag == 0) {
						return;
					}
				}

				// }

				if (garbled == true) {
					if (html.length() > 120) {
						logger.info(
								"----> " + html.substring(0, 50) + "---------" + html.substring(html.length() - 50));
					} else {
						logger.info("----> " + html);
					}
				}

				i = 0;
				break;
			}
			String fileName2 = file.getPath().substring(file.getPath().lastIndexOf("\\") + 1,
					file.getPath().lastIndexOf("."));
			if (null != html) {
				// 写html变量的数据到Blob
				insertData(html, fileName2);
				fileName2 = null;
			}
		} else if (file.isDirectory()) {
			logger.info("文件夹: " + file.getPath());
			File[] files = file.listFiles();
			for (File fi : files) {
				show(fi);
			}
		}
	}
	
	/**
	 * 得到案由
	 * @Description: TODO
	 * @param str
	 * @return   
	 * @return String  
	 * @author liuming
	 * @date 2016年5月25日  下午6:10:30
	 */
	public static String getCase(String str){
		String temp=null;
		if(StringUtils.isNull(str)){
			return null;
		}
		str=str.replace(":", "：").replace(",", "，");
//		if(str.indexOf("：")!=-1&&str.indexOf("，")!=-1){
//			temp=str.substring(0,str.indexOf("："));
//			return temp ;
//		}
		
		String[] cArray = new String[]{"：","，"};
		for (int i = 0; i < cArray.length; i++) {
			
			if(str.indexOf(cArray[i].toString())!=-1){
				temp=str.substring(0,str.indexOf(cArray[i].toString()));
				return temp;
			}
		}
		
		return str;
	}
	

	// 判断是否存在乱码
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

	public static void Statistics(File file) {
		logger.info("--------------------- " + file.getPath());
		COUNT = SUM - ERRORSUM - NOSUM - NOSTANDARD - NOSTANDARDSWF - NOSTANDARDDOC;
		logger.info("所有数据：" + SUM + "条");
		logger.info("正确数据：" + COUNT + "条");
		logger.info("错误数据" + ERRORSUM + "条");
		logger.info("空数据" + NOSUM + "条");
		logger.info("非标准数据" + NOSTANDARD + "条");
		logger.info("SWF数据" + NOSTANDARDSWF + "条");
		logger.info("SWF数据" + NOSTANDARDDOC + "条");
	}

	/**
	 * 把抽取的html放入Blob/ES
	 * 
	 * @param data
	 *            抽取的html正文
	 * @param fileName
	 *            文件名称
	 * @author xiongchangyi
	 * @throws URISyntaxException
	 * @throws InvalidKeyException
	 * @throws StorageException
	 * @throws IOException
	 */
	public static void insertData(String data, String fileName)
			throws InvalidKeyException, URISyntaxException, StorageException, IOException {
		// 往Blob上面写数据，文件名称是Html的名称，以txt格式存储

		while (true) {
			try {
				CloudBlockBlob blob = container.getBlockBlobReference(fileName + ".txt");
				blob.uploadText(data);
				break;
			} catch (Exception e) {
				try {
					Thread.sleep(5000);
					logger.error("连blob 超时------");
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		// System.out.println(blob.downloadText());
	}

	public static void insertDataToBlob(File file) {
		try {
			while (true) {
				try {
					String fileName2 = file.getPath().substring(file.getPath().lastIndexOf("\\") + 1);
					CloudBlockBlob blob = container.getBlockBlobReference(fileName2);
					blob.upload(new FileInputStream(file), file.length());
					// System.out.println("upload swf ok");
					break;
				} catch (Exception e) {
					try {
						Thread.sleep(5000);
						logger.error("连blob 超时------");
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getDataAll_XJ(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}

		Element divs1 = doc.select("div#wenshu").first();

		value = divs1.text();
		value = getReplaceAll(value);
		return value;
	}

	/**
	 * 安徽
	 * 
	 * @param doc
	 * @return
	 */
	public static String getDataAll_AH(Document doc) {
		String value = "";
		String temp = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		value = doc.body().text();

		if (null == value || "" == value) {
			return null;
		} else {
			value = getReplaceAll(value);
		}

		int index = 0;

		for (String date : DATESTATUS) {
			if ("点击".equals(date) || "点击数".equals(date) || "浏览次数".equals(date) || "浏览数".equals(date)
					|| "浏览".equals(date) || "访问人数".equals(date) || "发布时间".equals(date)) {

				if (value.contains(date)) {
					int indexTemp = value.indexOf(date);
					// 数字个数 及- "2015-10-11 20:32"
					int num = 0;

					if (value.contains("发布日期") && value.contains("浏览次数")) {
						int temUp = value.lastIndexOf("发布日期");
						int temDown = value.lastIndexOf("浏览次数");
						if (temUp >= temDown) {
							indexTemp = temUp;
							temp = value.substring(indexTemp + "发布日期".length(), indexTemp + "发布日期".length() + 15);
							for (int i = temp.length(); --i >= 0;) {
								if ("-".charAt(0) == temp.charAt(i)) {
									num++;
								}
							}
						} else {
							indexTemp = temDown;
							temp = value.substring(indexTemp + "浏览次数".length(), indexTemp + "浏览次数".length() + 15);
						}
					}
					num += includeNumericNum(temp);

					if (temp.contains("次")) {
						value = value.substring(indexTemp + date.length() + num + 1);
					} else {
						value = value.substring(indexTemp + date.length() + num);
					}
					break;
				}
			}

			index = value.indexOf(date);
			if (index >= 0) {
				value = value.substring(index + date.length(), value.length());
				break;
			}
		}

		for (String date : DATESTATUSEND) {

			if (value.contains("上一篇") && value.contains("下一篇")) {
				int temUp = value.lastIndexOf("上一篇");
				int temDown = value.lastIndexOf("下一篇");
				if (temUp <= temDown) {
					index = temUp;
				} else {
					index = temDown;
				}
				value = value.substring(0, index);
				return value;
			}
			index = value.lastIndexOf(date);
			if (index >= 0) {
				value = value.substring(0, index);
				return value;
			}
		}

		return value;
	}

	public static String getDataAll_DongZhiXian(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select("div[id=wenshu]").first();

		if (null == divs1) {
			value = doc.text();
			if (value.contains("收藏本页")) {
				value = value.substring(value.lastIndexOf("收藏本页") + 4);
			}
			if (value.contains("来源： 中国法院网")) {
				value = value.substring(0, value.lastIndexOf("来源： 中国法院网"));
			}
		} else {
			value = divs1.text();
		}

		value = getReplaceAll(value);
		return value;
	}

	public static String getDataAll_DuJiQu(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select("div.news-con").first();

		value = divs1.text();
		value = getReplaceAll(value);
		return value;
	}

	public static String getDataAll_HUAIBEI(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select("div.content").first();

		value = divs1.text();
		if (value.contains("(责任编辑：淮北中院网)")) {
			value = value.substring(0, value.indexOf("(责任编辑：淮北中院网)"));
		}
		value = getReplaceAll(value);
		return value;
	}

	/**
	 * 鸠江区人民法院
	 * 
	 * @param doc
	 * @return
	 */
	public static String getDataAll_JiuJiangQu(Document doc) {
		String value = "";
		String temp = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		value = doc.body().text();

		if (null == value || "" == value) {
			return null;
		} else {
			value = getReplaceAll(value);
		}
		int index = 0;

		for (String date : DATESTATUS) {

			if ("点击".equals(date) || "点击数".equals(date) || "浏览".equals(date) || "浏览数".equals(date)
					|| "访问人数".equals(date)) {

				if (value.contains(date)) {
					int indexTemp = value.indexOf(date);
					temp = value.substring(indexTemp, indexTemp + 15);
					int num = includeNumericNum(temp);
					if (temp.contains("次")) {
						value = value.substring(indexTemp + date.length() + num + 1);
					} else {
						value = value.substring(indexTemp + date.length() + num);
					}
					break;
				}
			}

			index = value.indexOf(date);
			if (index >= 0) {
				value = value.substring(index + date.length(), value.length());
				break;
			}
		}
		for (String date : DATESTATUSEND) {

			if ("书记员".equals(date)) {

				if (value.contains(date)) {

					int indexTemp = value.lastIndexOf(date);
					temp = value.substring(indexTemp, indexTemp + 15);
					if (temp.contains("附")) {
						int te = temp.indexOf("附");
						value = value.substring(0, indexTemp + te);
					}
					break;
				}
			}

			index = value.lastIndexOf(date);
			if (index >= 0) {
				value = value.substring(0, index);
				return value;
			}
		}

		return value;
	}

	public static String getDataAll_ShouXian(Document doc) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select("div#zoom").first();
		value = divs1.text();
		value = getReplaceAll(value);
		return value;
	}

	/**
	 * 由DIV的属性得到 DIV的内容
	 * 
	 * @param doc
	 * @param div
	 * @return
	 */
	public static String getDivContent(Document doc, String div) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		Element divs1 = doc.select(div).first();
		value = divs1.text();
		value = getReplaceAll(value);
		return value;
	}

	/**
	 * 由DIV的属性 及开始，结束字符得到 最后的内容
	 * 
	 * @param doc
	 * @param div
	 * @param beginStr
	 * @param endStr
	 * @param flag
	 *            默认为FLASE 及开始字符 后截取
	 * @return
	 */
	public static String getContent(Document doc, String div, String beginStr, String endStr, boolean flag) {

		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (null != div && "" != div) {
			Element divs1 = doc.select(div).first();
			if (null != divs1) {
				value = divs1.text();
			}
		}

		if (value == null || "".equals(value)) {
			return null;
		}

		String valueT = getReplaceAll(value);
		// 判断是不是文书
		int flag1 = 0;
		for (String per : PERSONNEL) {
			if (valueT.contains(per)) {
				flag1 = 1;
				break;
			}
		}
		if (flag1 == 0) {
			return null;
		}

		if (!StringUtils.isNull(beginStr)) {
			int beginIndex = value.indexOf(beginStr);
			if (beginIndex != -1) {
				value = value.substring(beginIndex + beginStr.length());
				if (flag == true) {
					value = value.substring(beginIndex);
				}
			}
		}

		value = getReplaceAll(value);
		if (!StringUtils.isNull(endStr)) {
			int endIndex = value.lastIndexOf(endStr);
			if (endIndex != -1) {
				value = value.substring(0, endIndex);
			}

		}

		return value;
	}

	/**
	 * 
	 * @param doc
	 * @param div
	 * @param beginStr
	 * @param endStr
	 * @param flag
	 * @param subBeginStr
	 *            需截取的开始字符
	 * @param subEndStr
	 *            需截取的结束字符
	 * @return
	 */
	// public static String getContent(Document doc, String div, String
	// beginStr, String endStr, boolean flag,
	// String subBeginStr, String subEndStr, String subStr) {
	// String value = "";
	// if (doc == null || "".equals(doc)) {
	// return null;
	// }
	// // value = doc.text();
	// value = doc.body().text();
	// if (StringUtils.isNull(value)) {
	// return null;
	// }
	// if (null != div && "" != div) {
	// Element divs1 = doc.select(div).first();
	// if (null != divs1) {
	// value = divs1.text();
	// }
	// }
	//
	// String valueT = getReplaceAll(value);
	// // 判断是不是文书
	// int flag1 = 0;
	// for (String per : PERSONNEL) {
	// if (valueT.contains(per)) {
	// flag1 = 1;
	// break;
	// }
	// }
	// if (flag1 == 0) {
	// return null;
	// }
	//
	// if (!StringUtils.isNull(beginStr)) {
	// int beginIndex = value.indexOf(beginStr);
	// if (beginIndex != -1) {
	// value = value.substring(beginIndex + beginStr.length());
	// if (flag == true) {
	// value = value.substring(beginIndex);
	// }
	// }
	// }
	//
	// value = getReplaceAll(value);
	// if (!StringUtils.isNull(endStr)) {
	// int endIndex = value.lastIndexOf(endStr);
	// if (endIndex != -1) {
	// value = value.substring(0, endIndex);
	// }
	//
	// }
	//
	// if (value.length() > 100) {
	// if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {
	//
	// int index1 = value.indexOf(subBeginStr);
	// int index2 = value.indexOf(subEndStr);
	// if ((index2 - index1) > 0 && (index2 - index1) < 80) {
	// String value1 = value.substring(0, index1);
	// String value2 = value.substring(index2 + subEndStr.length());
	// value = value1 + value2;
	// value1 = null;
	// value2 = null;
	// }
	// }
	// }
	// // 截取掉不需求的字符
	// if (!StringUtils.isNull(subStr)) {
	// value = value.replaceAll(subStr, "");
	// }
	//
	// return value;
	// }
	/**
	 * 
	 * @param doc
	 * @param div
	 * @param beginStr
	 * @param endStr
	 * @param flag
	 * @param subBeginStr
	 * @param subEndStr
	 * @param zeStr
	 *            List<String> result=new ArrayList<String>();
	 * @return
	 */
	// public static String getContent(Document doc, String div, String
	// beginStr, String endStr, boolean flag,
	// String subBeginStr, String subEndStr,List<String> zeStr) {
	// String value = "";
	// if (doc == null || "".equals(doc)) {
	// return null;
	// }
	// // value = doc.text();
	// value = doc.body().text();
	// if (StringUtils.isNull(value)) {
	// return null;
	// }
	// if (null != div && "" != div) {
	// Element divs1 = doc.select(div).first();
	// if (null != divs1) {
	// value = divs1.text();
	// }
	// }
	// if (StringUtils.isNull(value)) {
	// return null;
	// }
	//
	// String valueT = getReplaceAll(value);
	// // 判断是不是文书
	// int flag1 = 0;
	// for (String per : PERSONNEL) {
	// if (valueT.contains(per)) {
	// flag1 = 1;
	// break;
	// }
	// }
	// if (flag1 == 0) {
	// return null;
	// }
	//
	// if (!StringUtils.isNull(beginStr)) {
	// int beginIndex = value.indexOf(beginStr);
	// if (beginIndex != -1) {
	// value = value.substring(beginIndex + beginStr.length());
	// if (flag == true) {
	// value = value.substring(beginIndex);
	// }
	// }
	// }
	//
	// value = getReplaceAll(value);
	// if (!StringUtils.isNull(endStr)) {
	// int endIndex = value.lastIndexOf(endStr);
	// if (endIndex != -1) {
	// value = value.substring(0, endIndex);
	// }
	//
	// }
	//
	// if (value.length() > 100) {
	// if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {
	//
	// int index1 = value.indexOf(subBeginStr);
	// int index2 = value.indexOf(subEndStr);
	// if ((index2 - index1) > 0 && (index2 - index1) < 80) {
	// String value1 = value.substring(0, index1);
	// String value2 = value.substring(index2 + subEndStr.length());
	// value = value1 + value2;
	// value1 = null;
	// value2 = null;
	// }
	// }
	// }
	//
	//
	// //正则
	// if(null!=zeStr){
	// if(zeStr.size()>0){
	// for(String s1:zeStr){
	// Pattern p = Pattern.compile(s1);
	// Matcher m = p.matcher(value);
	// String str=null;
	// if(m.find()){
	// str=m.group();
	// value=value.replaceAll(str, "");
	// }
	// }
	// }
	// }
	//
	// return value;
	// }

	/**
	 * 
	 * @param doc
	 * @param div
	 * @param beginStr
	 *            去掉前
	 * @param endStr
	 *            去掉后
	 * @param flag
	 * @param subBeginStr
	 *            需截取的开始字符
	 * @param subEndStr
	 *            需截取的结束字符
	 * @param subStr
	 *            去掉不需要字符
	 * @return
	 */
	public static String getContent(Document doc, String div, String beginStr, String endStr, boolean flag,
			String subBeginStr, String subEndStr, String subStr) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (null != div && "" != div) {
			Element divs1 = doc.select(div).first();
			if (null != divs1) {
				value = divs1.text();
			}
		}
		if (StringUtils.isNull(value)) {
			return null;
		}
		String valueT = getReplaceAll(value);
		// 判断是不是文书
		int flag1 = 0;
		for (String per : PERSONNEL) {
			if (valueT.contains(per)) {
				flag1 = 1;
				break;
			}
		}
		if (flag1 == 0) {
			return null;
		}

		if (!StringUtils.isNull(beginStr)) {
			int beginIndex = value.indexOf(beginStr);
			if (beginIndex != -1) {
				value = value.substring(beginIndex + beginStr.length());
				if (flag == true) {
					value = value.substring(beginIndex);
				}
			}
		}

		value = getReplaceAll(value);
		if (!StringUtils.isNull(endStr)) {
			int endIndex = value.lastIndexOf(endStr);
			if (endIndex != -1) {
				value = value.substring(0, endIndex);
			}

		}

		if (value.length() > 100) {
			if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {

				int index1 = value.indexOf(subBeginStr);
				int index2 = value.indexOf(subEndStr);
				if ((index2 - index1) > 0 && (index2 - index1) < 80) {
					String value1 = value.substring(0, index1);
					String value2 = value.substring(index2 + subEndStr.length());
					value = value1 + value2;
					value1 = null;
					value2 = null;
				}
			}
		}
		// 截取掉不需求的字符
		if (!StringUtils.isNull(subStr)) {
			value = value.replaceAll(subStr, "");
		}

		return value;
	}

	public static String getContent(Document doc, String[] div, String beginStr, String endStr, boolean flag,
			String subBeginStr, String subEndStr, String subStr) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (null != div) {
			if (div.length > 0) {
				for (int i = 0; i < div.length; i++) {
					String str = div[i];
					Element divs1 = doc.select(str).first();
					if (null != divs1) {
						value = divs1.text();
						if (!StringUtils.isNull(value)) {
							value = divs1.text();
							break;
						}
					}
				}
			}
		}

		String valueT = getReplaceAll(value);
		// 判断是不是文书
		int flag1 = 0;
		for (String per : PERSONNEL) {
			if (valueT.contains(per)) {
				flag1 = 1;
				break;
			}
		}
		if (flag1 == 0) {
			return null;
		}

		if (!StringUtils.isNull(beginStr)) {
			int beginIndex = value.indexOf(beginStr);
			if (beginIndex != -1) {
				value = value.substring(beginIndex + beginStr.length());
				if (flag == true) {
					value = value.substring(beginIndex);
				}
			}
		}

		value = getReplaceAll(value);
		if (!StringUtils.isNull(endStr)) {
			int endIndex = value.lastIndexOf(endStr);
			if (endIndex != -1) {
				value = value.substring(0, endIndex);
			}

		}

		if (value.length() > 100) {
			if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {

				int index1 = value.indexOf(subBeginStr);
				int index2 = value.indexOf(subEndStr);
				if (index1 != -1 && index2 != -1) {
					if ((index2 - index1) > 0 && (index2 - index1) < 80) {
						String value1 = value.substring(0, index1);
						String value2 = value.substring(index2 + subEndStr.length());
						value = value1 + value2;
						value1 = null;
						value2 = null;
					}
				}

			}
		}
		// 截取掉不需求的字符
		if (!StringUtils.isNull(subStr)) {
			value = value.replaceAll(subStr, "");
		}

		return value;
	}

	public static String getContent(Document doc, String div, String beginStr, String endStr, boolean flag,
			String subBeginStr, String subEndStr, String subStr, List<String> zeStr) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		if (null != div && "" != div) {
			Element divs1 = doc.select(div).first();
			if (null != divs1) {
				value = divs1.text();
			}
		}
		if (StringUtils.isNull(value)) {
			return null;
		}
		String valueT = getReplaceAll(value);
		// 判断是不是文书
		int flag1 = 0;
		for (String per : PERSONNEL) {
			if (valueT.contains(per)) {
				flag1 = 1;
				break;
			}
		}
		if (flag1 == 0) {
			return null;
		}

		if (!StringUtils.isNull(beginStr)) {
			int beginIndex = value.indexOf(beginStr);
			if (beginIndex != -1) {
				value = value.substring(beginIndex + beginStr.length());
				if (flag == true) {
					value = value.substring(beginIndex);
				}
			}
		}

		value = getReplaceAll(value);
		if (!StringUtils.isNull(endStr)) {
			int endIndex = value.lastIndexOf(endStr);
			if (endIndex != -1) {
				value = value.substring(0, endIndex);
			}

		}

		if (value.length() > 100) {
			if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {

				int index1 = value.indexOf(subBeginStr);
				int index2 = value.indexOf(subEndStr);
				if ((index2 - index1) > 0 && (index2 - index1) < 80) {
					String value1 = value.substring(0, index1);
					String value2 = value.substring(index2 + subEndStr.length());
					value = value1 + value2;
					value1 = null;
					value2 = null;
				}
			}
		}
		// 截取掉不需求的字符
		if (!StringUtils.isNull(subStr)) {
			value = value.replaceAll(subStr, "");
		}

		// 正则
		if (null != zeStr) {
			if (zeStr.size() > 0) {
				for (String s1 : zeStr) {
					Pattern p = Pattern.compile(s1);
					Matcher m = p.matcher(value);
					String str = null;
					if (m.find()) {
						str = m.group();
						value = value.replaceAll(str, "");
					}
				}
			}
		}

		return value;
	}

	/**
	 * 
	 * @param doc
	 * @param subBeginStr
	 *            需要截取字段开始
	 * @param subEndStr
	 *            需要截取字段结束
	 * @param flag
	 *            默认flag为false 从字段结束处开始计算
	 * @param subStr
	 *            需要截取的字段
	 * @return
	 */
	public static String getContent(String doc, String subBeginStr, String subEndStr, Boolean flag, String subStr) {
		String value = "";
		if (doc == null || "".equals(doc)) {
			return null;
		}
		value = doc;
		if (value.length() > 100) {
			if (!StringUtils.isNull(subBeginStr) && !StringUtils.isNull(subEndStr)) {

				int index1 = value.indexOf(subBeginStr);
				int index2 = value.indexOf(subEndStr);
				if ((index2 - index1) > 0 && (index2 - index1) < 80) {
					String value1 = value.substring(0, index1);

					String value2 = value.substring(index2 + subEndStr.length());
					if (flag == true) {
						value2 = value.substring(index2);
					}
					value = value1 + value2;
					value1 = null;
					value2 = null;
				}
			}
		}
		// 截取掉不需求的字符
		if (!StringUtils.isNull(subStr)) {
			value = value.replaceAll(subStr, "");
		}

		return value;
	}

	// 去掉特殊字符
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

	public static String cleanBegin(String value) {
		String[] str = value.split("");
		StringBuffer stt = new StringBuffer();
		for (String val : clean) {

			if (val != str[0]) {
				stt.append(str);
			}
		}
		String aa = stt.toString();
		return aa;
	}

	/**
	 * 往ES里面写数据
	 * 
	 * @param data
	 *            要写的字段
	 * @param id
	 *            ID
	 */
	public static void insertEsData(String data, String id) {

	}

	// 判断字符是否非数字
	public static boolean isDigit(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	// 去掉无用字符
	public static String getReplaceAll(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		StringBuffer sb = null;
		if (value != null && !"".equals(value)) {
			value = value.replaceAll(",", "，");
			value = value.replaceAll(" ,o,O", "〇");
			value = value.replaceAll("[×,X,Ｘ,x,╳,＊,\\*]", "某");

			value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”,:,<,/>,</,>,-,+,=,},{,#,\",',-,%,^,*,〗,〖,?,？]", ""); // a-z,A-Z,没有去掉字母

			// value =
			// value.replaceAll("[\n,\r,“,”,<,/>,</,>,-,+,=,},{,#,\",',%,^,*,〗,〖]",
			// ""); // a-z,A-Z,没有去掉字母，没去冒号，没去空格 ，及 -

			// value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,“,”
			// ,<,/>,</,>,-,+,=,},{,#,\",',-,%,^,*,〗,〖]", ""); //
			// a-z,A-Z,没有去掉字母，没去冒号
			// value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”
			// ,:,<,/>,</,>,-,+,=,},{,#,\",',-,%,^,*]", ""); // a-z,A-Z,没有去掉字母

			// value = value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”,・
			// ,:,<,/>,</,>,a-z,A-Z,-,+,=,},{,.,#,\",',-,%,^,*]",""); //去掉所有字母
			value = getSpecialStringALL(value);
			value = value.trim();
			sb = new StringBuffer();
			sb.append(value);
		}
		return sb == null ? "" : sb.toString();
	}

	// 提取全文
	public static String getDataAll(String value) {
		if (value == null || "".equals(value)) {
			return null;
		}
		value = getReplaceAll(value);
		// value = cleanBegin(value);
		// logger.info("------------"+value);
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
			}
		}
		for (String date : DATESTATUSEND) {
			index = value2.lastIndexOf(date);
			if (index >= 0) {
				value = value.substring(0, index);
				return value;
			}
		}

		return value;
	}

	/**
	 * 删除字符串中的数字
	 * 
	 * @param str
	 * @return
	 */
	public static String removeNum(String str) {
		return str.replaceAll("\\d+", "");
	}

	/**
	 * 判断字符串中是否包含数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIncludeNumeric(String str) {
		if (null == str || "" == str) {
			return false;
		}
		for (int i = str.length(); --i >= 0;) {

			if (Character.isDigit(str.charAt(i))) {
				return true;
			}

		}
		return false;
	}

	/**
	 * 得到字符串中数字的个数
	 * 
	 * @param str
	 * @return
	 */
	public static int includeNumericNum(String str) {
		int fal = 0;
		if (null == str || "" == str) {
			return fal;
		}
		for (int i = str.length(); --i >= 0;) {
			if (Character.isDigit(str.charAt(i))) {
				fal++;
			}

		}
		return fal;
	}

	static long COUNT = 0;// 正确数量
	static long SUM = 0; // 所有数量
	static long ERRORSUM = 0;// 出错数量
	static long NOSUM = 0;// 无数据
	static long NOSTANDARD;// 非标准数据，如不是html数据
	static long NOSTANDARDSWF;// SWF数据
	static long NOSTANDARDDOC;// DOC数据
	static Map<String, String> MAPS = new HashMap<String, String>();

	static {
		MAPS.put("html", "html");
		MAPS.put("htm", "htm");
		// MAPS.put("txt", "txt");
		MAPS.put("swf", "swf");
		MAPS.put("doc", "doc");
	}

	public static String[] clean = { "0-9", "a-z", "A-Z" };
	public static String[] charset = { "utf-8", "gbk", "gb2312", "gb18030", "big5" };
	public static String[] ERCOEDING = { "й", "෨", "Ժ", "ۼ", "ҩ", "ල", "ɷ", "ص", "δ", "ġ", "Ϊ", "ط", "Ϣ", "ȡ", "Ӫ", "ã",
			"", "�" };
	public static String[] DATESTATUS = { "点击数", "点击率", "浏览次数", "浏览", "访问人数", "[大|中|小]", "大中小", "大小",
			"裁判文书公开是依据国家有关法律及最高人民法院等有关规定，相关事宜请与各审判法院联系。", "【关闭窗口】", "【关闭窗口】", "提交时间", "提交日期", "发布时间", "发布日期", "关注时间",
			"编辑时间", "编辑日期", "发表时间", "录入时间", "更新时间", "作者点击", "点击时间", "点击", "字体", "∣法律社区", "发表于", "阅读", "点击", "日期", "作者",
			"时间", "今天是" };

	// public static String[] DATESTATUS = {"", "提交时间", "提交日期", "发布时间", "发布日期",
	// "关注时间", "编辑时间", "编辑日期", "发表时间", "录入时间",
	// "更新时间", "作者点击", "点击时间", "【关闭窗口】", "大中小", "大小", "字体", "点击率", "浏览", "点击数",
	// "∣法律社区", "发表于", "阅读", "点击", "日期",
	// "作者", "时间", "今天是" };

	public static String[] DATESTATUSEND = { "上一篇", "公告", "下一篇", "作者|信息来源", "[打印本页]", "打印本页|", "打印本页 ", "关闭窗口", "相关链接",
			"法院门户", "主办单位" };
	// 文档结束处人员职称
	public static String[] PERSONNEL = { "执行员", "审判长", "审判员", "书记员", "提交日期", "原告", "被告", "申诉人", "委托代理人", "被申诉人",
			"法定代表人", "通 知 书", "驳回申诉通知书", "特此通知" };

	private static Logger logger = LogManager.getLogger(ExtractionHtmlCaseCause.class.getName());

	public static final String storageConnectionString = "DefaultEndpointsProtocol=http;" + "AccountName=masspickdata;"
			+ "AccountKey=qi8o0gS/Jl2/se9uB3LJFi6QMc562IZuHf4oIwfVkGBDKychIwk39Mes9pNu5ni0gBOoru+DF7SN/RZ0V6oEyQ==;"
			+ "EndpointSuffix=core.chinacloudapi.cn";

	static CloudStorageAccount storageAccount = null;
	static CloudBlobClient blobClient = null;
	static CloudBlobContainer container = null;

	static {

		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		blobClient = storageAccount.createCloudBlobClient();
		try {
			container = blobClient.getContainerReference("courtnew");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}
}
