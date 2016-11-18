package cn.com.szgao.clean.court;

import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.web.client.RestTemplate;

import cn.com.szgao.util.DateUtils;
import cn.com.szgao.court.esAndcb.Ff;
import cn.com.szgao.court.esAndcb.Rows;
import cn.com.szgao.dto.WholeCourtVO;

import com.google.gson.Gson;

public class ExtractionCBToJson {
	private static Logger logger = LogManager
			.getLogger(ExtractionCBToJson.class.getName());
	static long count = 0;
	static int i = 0;

	/**
	 * 裁判文书取CB数据写JSON
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		PropertyConfigurator  
				.configure("D:\\data\\wenhao\\Mass\\Mass\\log\\log4j.properties");
		try {
			show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void show() throws Exception {
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		urlVariables.put("limits", 1000);
		urlVariables.put("inclusive_ends", true);
		String key = null;
		RestTemplate template = new RestTemplate();
		String result = null;
		Gson gson = new Gson();
		List<WholeCourtVO> archs = null;
		Date date = new Date();
		Format format = new SimpleDateFormat("yyyyMMdd");
		String time = format.format(date);
		while (true) {
			archs = new ArrayList<WholeCourtVO>();
			logger.info("-----------" + count + "--------每次开始搜索下一批数据的KEY："
					+ key);
			if (null == key) {
				urlVariables.put("skips", 0);
				result = template
						.getForObject(
								"http://192.168.1.4:8092/executedN/_design/court_only/_view/court_only?"
								+ "inclusive_end={inclusive_ends}&limit={limits}&skip=0",
								String.class, urlVariables);
			} else {
				urlVariables.put("startkeys", "\"" + key + "\"");
				urlVariables.put("skips", 1);
				result = template
						.getForObject(
								"http://192.168.1.4:8092/executedN/_design/court_only/_view/court_only?"+ 
								"inclusive_end={inclusive_ends}&limit={limits}&startkey={startkeys}&skip={skips}",
								String.class, urlVariables);
			}
			String value = new String(result.getBytes("ISO-8859-1"), "utf-8");
			Ff jsn = gson.fromJson(value, Ff.class);
			for (Rows r : jsn.getRows()) {
				i++;
				key = r.getKey();
				System.out.println(key);
				r.getValue().setUpdateTime(DateUtils.toYMDOfChaStr_ESZZ2(time));
				r.getValue().setWholeCourtId(key);
				archs.add(r.getValue());
				if (archs.size() >= 1000) {
					System.out.println("数据大小："+archs.size());
					route(archs);
					archs = null;
					archs = new ArrayList<WholeCourtVO>();
				}
			}
			if (archs.size() > 0) {
				System.out.println("数据大小："+archs.size());
				route(archs);
				archs = null;
			}
			if (i >= jsn.getTotal_rows()) {
				break;
			}
		}

	}

	public static void route(List<WholeCourtVO> list) throws Exception {
		count++;
		long a = count % 100 == 0 ? count / 100 : count / 100 + 1;
		String str = "E:\\JSON\\Notice" + a;//设置文件夹路径
		File file = null;
		Gson gson = null;
		FileWriter out = null;
		try {
			gson = new Gson();
			file = new File(str);
			if (!file.exists() && !file.isDirectory()) {
				logger.info(file + "---目錄不存在");
				file.mkdirs();//创建文件夹路径
			}
			str = str + "\\" + count + ".json";
			out = new FileWriter(new File(str), true);	//指定路径生成JSON文件
			for (WholeCourtVO arch : list) {
				// //往文件写入
				out.write(gson.toJson(arch).toString());
				// //换行
				out.write("\r\n");
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			if (file != null) {
				file = null;
			}
			if (gson != null) {
				gson = null;
			}
		}
	}
}
