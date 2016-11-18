package cn.com.szgao.clean.court;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;

public class TestHtml {

	private static Logger logger = LogManager.getLogger(ExtractionHtml.class.getName());

	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}

	public static String[] charset = { "utf-8", "gbk", "gb2312", "gb18030", "big5" };
	public static String[] ERCOEDING = { "й", "෨", "Ժ", "ۼ", "ҩ", "ල", "ɷ", "ص", "δ", "ġ", "Ϊ", "ط", "Ϣ", "ȡ", "Ӫ", "ã",
			"", "Դ", "ڲ", "Ѱ", "�" };
	static Map<String, String> MAPS = new HashMap<String, String>();

	static {
		MAPS.put("html", "html");
		MAPS.put("htm", "htm");
		MAPS.put("txt", "txt");
	}

	public static void main(String[] args) {

		File file = new File("C:/data/文书新版本/3cb9cf2a-69e3-5075-bdaa-fe656030e3f0.html");

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
			// return null;
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
			/*
			 * variable = getReplaceAll(doc.title()); if (variable != null &&
			 * !"".equals(variable)) { arch.setTitle(variable.trim()); // 标题 √ }
			 */
			break;
		}

		if (html == null || "".equals(html)) {
			logger.info("内容为空的HTML页面：" + file.getPath());
		}

		WholeCourtVO arch_info = null;

		
		ExtractionHtml ff=new ExtractionHtml();
		arch_info = ff.getCourtInfo(doc);
		arch=arch_info;
		System.out.println("---------------");

	}

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

}
