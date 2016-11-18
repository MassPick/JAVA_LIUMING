package cn.com.szgao.util;

import java.util.List;
import java.util.TreeSet;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;

public class HtmlUtils {

	/**
	 * 用jsoup 得到确定的值
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param div
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月15日 上午10:30:36
	 */
	public static String getContent(Document doc, String div) {

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

		return value;
	}

	/**
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param div
	 * @param order
	 *            -1 就是最后一个 last() 默认为0 first()
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月15日 下午2:23:21
	 */
	public static String getContent(Document doc, String div, int order) {

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
			Element divs1 = null;
			if (-1 == order) {
				divs1 = doc.select(div).last();
			} else {
				divs1 = doc.select(div).first();
			}

			if (null != divs1) {
				value = divs1.text();
			}
		}

		return value;
	}

	public static String getContentFromXpath(Document doc, String xpath) {

		String reslut = null;
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		String value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		JXDocument jxDocument = new JXDocument(doc);
		try {
			List<Object> rs = jxDocument.sel(xpath);
			for (Object o : rs) {
				if (o instanceof Element) {
					// int index = ((Element) o).siblingIndex();
					reslut = ((Element) o).text();
					// reslut = reslut.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "");

					// System.out.println("案件类型："+reslut);
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
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param xpath
	 * @param clean
	 *            1 要清洗 0 不清洗
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年6月28日 下午3:59:37
	 */
	public static String getContentFromXpath(Document doc, String xpath, int clean) {

		String reslut = null;
		if (doc == null || "".equals(doc)) {
			return null;
		}
		// value = doc.text();
		String value = doc.body().text();
		if (StringUtils.isNull(value)) {
			return null;
		}
		JXDocument jxDocument = new JXDocument(doc);
		try {
			List<Object> rs = jxDocument.sel(xpath);
			for (Object o : rs) {
				if (o instanceof Element) {
					// int index = ((Element) o).siblingIndex();
					reslut = ((Element) o).text();
					if (!StringUtils.isNull(reslut)) {
						if (clean == 1) {
							reslut = reslut.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "");
						}
					}

					// System.out.println("案件类型："+reslut);
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
	 * 通过style 取内容
	 * 
	 * @Description: TODO
	 * @param doc
	 * @param xpath_sx
	 * @param clean
	 *            是否清洗
	 * @param isDiv
	 *            是否为DIV
	 * @return
	 * @return String
	 * @author liuming
	 * @date 2016年8月16日 上午10:15:23
	 */
	public static String getContentFromXpath_SX(Document doc, String xpath_sx, int clean, int isDiv) {

		String reslut = null;
		if (doc == null) {
			return null;
		}
		TreeSet<String> ts = new TreeSet<String>();

		// int fa = 0;// 判断第一个a标签前是不是出现过当事人信息，如果大于0说明出现
		// int flag_end = 0; // 判断是不是到了非当事人部分
		Elements all = doc.body().getAllElements();

		// writerString(fwUn2, (case1!=null?case1:"NULL") + "#" + "#" +
		// file.getName());

		for (Element element_all : all) {
			String tagName = element_all.tagName();
			// System.out.println(tagName);

			if (isDiv == 1) {
				if ("div".equals(tagName)) {
					Attributes as = element_all.attributes();
					for (Attribute attribute : as) {
						// System.out.println(attribute.getKey());
						// System.out.println( attribute.getValue() );

						if ("style".equals(attribute.getKey()) && xpath_sx.equals(attribute.getValue())) {
							// System.out.println(element_all.text());
							// fa++;
							String temp1 = element_all.text();
							if (clean == 1) {
								temp1 = temp1.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "");
							}
							return temp1;
							// break;
							// String temp2=getCase(temp1);
							// System.out.println(temp2);

							// if (StringUtils.isNull(temp1)) {
							// continue;
							// }
						}
					}
				}
			} else {

				Attributes as = element_all.attributes();
				for (Attribute attribute : as) {
					// System.out.println(attribute.getKey());
					// System.out.println( attribute.getValue() );

					if ("style".equals(attribute.getKey()) && xpath_sx.equals(attribute.getValue())) {
						// System.out.println(element_all.text());
						// fa++;
						String temp1 = element_all.text();
						if (clean == 1) {
							temp1 = temp1.replaceAll("[\n,\t,\r,\\s,&nbsp;]", "");
						}
						return temp1;
						// break;
						// String temp2=getCase(temp1);
						// System.out.println(temp2);

						// if (StringUtils.isNull(temp1)) {
						// continue;
						// }
					}
				}

			}

		}
		return reslut;
	}

}
