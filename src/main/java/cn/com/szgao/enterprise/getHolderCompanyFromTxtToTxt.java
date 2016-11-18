package cn.com.szgao.enterprise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
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

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.com.szgao.dto.BusinessDirectoryVO;
import cn.com.szgao.dto.CompanyHolder;
import cn.com.szgao.dto.Doc;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.HolderVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.PostgresqlUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;

/**
 * 从工商文本文件里提取出  股东企业的关系
 * @author liuming
 * @Date 2016年5月18日 下午2:45:37
 */
@SuppressWarnings("unused")
public class getHolderCompanyFromTxtToTxt {

	static int index = 0;

	public static void main(String[] args) throws UnsupportedEncodingException {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));

		long startTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		Date date = new Date(startTime);
		log.info("开始时间--------------------" + formatter.format(date));
		log.debug(
				"-------------------------------------------------- 开始debug!!InsertEtpEventItemFromCBToES!!!!!!!!!!!!!!!!!");
		log.info(
				"-------------------------------------------------- 开始info!!!!InsertEtpEventItemFromCBToES!!!!!!!!!!!!!!!");

		File file=new File("G:\\已清洗后公司数据");
		try {
			show(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		if (lists.size() > 0) {
			index++;
			// System.out.println(true);
			filteText2(lists);

			lists.clear();
			lists = null;
			lists = new ArrayList<CompanyHolder>();
		}
		

		log.debug(
				"-------------------------------------------------- 结束debug!!InsertEtpEventItemFromCBToES!!!!!!!!!!!!!!!!!");
		log.info(
				"-------------------------------------------------- 结束info!!!!InsertEtpEventItemFromCBToES!!!!!!!!!!!!!!!");

		long endTime = System.currentTimeMillis();
		Date endDate = new Date(endTime);
		
		log.info("企业数： "+i  +"  股东数： "+iholder);
		log.info("结束时间--------------------" + formatter.format(endDate));
		log.info("Time : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
		log.info("Time : " + (float) ((float) ((endTime - startTime) / 1000) / 60) + "分钟");
		log.info("Time : " + (float) ((endTime - startTime) / 1000) + "秒");

		log.info("Total : " + i);

		log.info("Speed : " + (float) (i / (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60))
				+ "个/小时");
		log.info("Speed : " + (float) (i / (float) ((float) ((endTime - startTime) / 1000) / 60)) + "个/分种");
		log.info("Speed : " + (float) (i / (float) ((endTime - startTime) / 1000)) + "个/秒");
	}

	static int i = 0;
	static int iholder = 0;
	private static Logger log = LogManager.getLogger(getHolderCompanyFromTxtToTxt.class);
	static Bucket bucket = null;

	static Connection conn = null;
	static PreparedStatement stmtInsert = null;
	// 19个

	static int count = 0;

	/**
	 * 循环调用是否是目录
	 * 
	 * @param file
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void show(File file) throws IOException, ParseException {
		long startTime = System.currentTimeMillis();
		if (file.isFile()) {
			count += 1;
			log.info("数量:" + count + "---线程名" + Thread.currentThread().getName());
			System.out.println(count);
			readFileByLines(file);
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		for (File fi : files) {
			if (fi.isFile()) {
				count += 1;
				log.info("数量:" + count + "---线程名" + Thread.currentThread().getName());
				System.out.println(count);
				readFileByLines(fi);
				// fi.delete();
			} else if (fi.isDirectory()) {
				show(fi);
			} else {
				continue;
			}
		}
	}

	static List<CompanyHolder> lists = new ArrayList<CompanyHolder>();
	

	private static void readFileByLines(File file) throws IOException, ParseException {
		System.out.println(file.getPath());
		JSONObject temJson = null;
		BufferedReader reader = null;
		JsonObject obj = null;
		JsonDocument doc = null;
		int countLen = 0;
		try {
			// reader = new BufferedReader(new FileReader(file));

			// InputStreamReader in= new InputStreamReader(new
			// FileInputStream(file),"GB18030'");
			// reader = new BufferedReader(in);

//			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GB18030");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String key1 = null;
		String temp = null;
		EnterpriseVO vo = null;
		Gson gs = new Gson();
		long startTime = System.currentTimeMillis();
		while ((temp = reader.readLine()) != null) {
			try {
				int kb=0;
				kb=temp.getBytes().length;
				kb=kb/1024/1024;
				if(kb>=1){
					continue;
				}
				try {
					temJson = JSONObject.fromObject(temp);
				} catch (Exception e) {
					// log.error("json异常:"+ file.getPath()+"----------------"+
					// temp);
					log.error("json异常:" + temp);
					continue;
				}
				
				System.out.println(i++);

//				System.out.println(temJson);
				try {
					vo = gs.fromJson(temp, EnterpriseVO.class);
				} catch (Exception e) {
					// log.error("json转vo异常:"+
					// file.getPath()+"----------------"+ temJson);
					log.error("json转vo异常:" + temJson);
					continue;
				}

				if (vo != null) {

					if(null!=vo.getHolder()&&vo.getHolder().size()>0){
						 for (HolderVO holderVo : vo.getHolder()) {
							 
							 CompanyHolder ch = new CompanyHolder();
							 ch.setCompany(vo.getCompany());
								ch.setRegNum(vo.getCreditCode() != null ? vo.getCreditCode()
										: (vo.getRegNum() != null ? vo.getRegNum() : null));
								ch.setHolder(holderVo.getHolder());
								ch.setHolder_type(holderVo.getType());
								ch.setProvince(vo.getProvince());
								ch.setCity(vo.getCity());
								ch.setArea(vo.getArea());
								ch.setCompanyId(vo.getCompanyId());
								lists.add(ch);
								
								System.out.println("---------  " +iholder++);
								
								if (lists.size() >= 100000) {
									index++;
									// System.out.println(true);
									filteText2(lists);

									lists.clear();
									lists = null;
									lists = new ArrayList<CompanyHolder>();
								}
						}
					}
					countLen++;
				}
			} catch (Exception e) {
				log.info(e);
				return;
			}
		}
	}

	public static void filteText2(List<CompanyHolder> value) {
		FileWriter filewr = null;
		try {
			filewr = new FileWriter("G:\\股东反查\\" + index + ".txt", true);

			for (CompanyHolder wholeCourtVO : value) {
				filewr.write(StringUtils.GSON .toJson(wholeCourtVO) + System.getProperty("line.separator"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			if (null != filewr) {
				try {
					filewr.close();
					// outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
