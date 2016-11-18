package cn.com.szgao.wash.data.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import com.google.gson.Gson;

import cn.com.szgao.dto.BreakFaithVO;
import cn.com.szgao.dto.BuildQulificationVO;
import cn.com.szgao.dto.BuildVO;
import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.dto.PrCiCouVO;
import cn.com.szgao.dto.WholeCourtVO;
import cn.com.szgao.wash.data.DataUtils;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.PostgresqlUtilsLocal;
import cn.com.szgao.util.PrCiCouText;
import cn.com.szgao.util.StringUtils;
import net.sf.json.JSONObject;
import rx.functions.Func1;

import rx.Observable;

/**
 * 将一个个公司入库到CB C:\data\建设通和建设市场发布平台\建筑市场发布平台\建筑市场监控1合并
 * 
 * @author liuming
 *
 */
public class InsertBuildFromOneJsonToBC {

	private static Logger log = LogManager.getLogger(InsertBuildFromOneJsonToBC.class);
	static int count = 0;
	private static Logger logger = LogManager.getLogger(InsertBuildFromOneJsonToBC.class);
	static NameBasedGenerator nbg = Generators.nameBasedGenerator(NameBasedGenerator.NAMESPACE_DNS);
	
	static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	static String usr = "postgres";
	static String psd = "615601.xcy*";
	
	static cn.com.szgao.wash.data.AdministrationUtils util = new cn.com.szgao.wash.data.AdministrationUtils();
	static{
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			PrCiCouText.connection = DriverManager.getConnection(url, usr, psd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DataUtils.initData();
		util.initData();
	}
	

	@SuppressWarnings("null")
	public static void main(String[] args) throws UnsupportedEncodingException {

		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
		// 文件
		// 说明：------------------------------------------------ 先入建设通，再建筑市场进行覆盖

		// File file = new File("C:/data/建设通和建设市场发布平台/建设通/主信息合并");

		// File file = new File("C:/data/建设通和建设市场发布平台/建筑市场发布平台/建筑市场监控1合并");

//		File file = new File("C:/data/建设通和建设市场发布平台/建筑市场发布平台/建筑市场平台");
		
		 File file1 = new File("C:/data/建设通和建设市场发布平台_新/建设通/主信息/主信息");//--------------UTF-8编码
//		 File file = new File("C:/data/建设通和建设市场发布平台_新/建设通/主信息/主信息/jianshetong1/阿克苏地区金鼎水利水电工程有限责任公司.json");//--------------UTF-8编码
		 

		 
		 
		 File file2 = new File("C:/data/建设通和建设市场发布平台_新/建筑市场发布平台/建筑市场平台/建筑市场平台");//--------------- GB2312编码
//		 File file2 = new File("C:/data/建设通和建设市场发布平台_新/建筑市场发布平台/建筑市场平台/建筑市场平台/四川极力建筑劳务有限公司.json");//--------------- GB2312编码
		 
		
		Integer source=1;// 1  全国建设市场监督    0 或null是建设通-----------------------------------------------------------------------重点

		try {
			log.info("----------------------------------------开始");
			log.error("----------------------------------------开始");
			long startTime = System.currentTimeMillis();

			show(file1,0,"UTF-8");
//			show(file2,1,"GB2312");

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
			long endTime = System.currentTimeMillis();
			Date endDate = new Date(endTime);
			log.info("路径：" + file2.getPath());
			log.info("开始时间--------------------" + formatter.format(startTime));
			log.info("结束时间--------------------" + formatter.format(endDate));
			log.info("Took : " + (float) ((endTime - startTime) / 1000) + "秒");
			log.info("Took : " + (float) ((endTime - startTime) / 1000) / 60 + "分钟");
			log.info("Total : " + count);
			log.info("Took : " + (float) ((float) ((float) ((endTime - startTime) / 1000) / 60) / 60) + "小时");
			log.info("Speed : " + (float) (count / ((float) ((float) ((endTime - startTime) / 1000) / 60))) + "个/分钟");
			log.info("Speed : " + (float) (count / ((float)((float) ((float) ((endTime - startTime) / 1000) / 60 ))/60  )) + "个/秒");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static Bucket bucket = null;

	public static void readFile(File file,Integer source,String coding) {
		int flag = 0;
		int countM = 0;
		int countNull = 0;
		int size = 1000;
		String tempCourtName = "";
		long startTime = System.currentTimeMillis();
		long startTime2 = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");

		BufferedReader reader = null;
		try {

			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(new FileInputStream(file), coding);
//				isr = new InputStreamReader(new FileInputStream(file), "GB2312");
//				isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "GB18030");
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			String temp = null;
			BuildVO vo = null;
			Gson gs = new Gson();
			JSONObject temJson = null;
			String key1 = "";

			List<JsonDocument> documents = new ArrayList<JsonDocument>();

			JsonObject content = null;
			int countLen = 0;
			StringBuffer sb = new StringBuffer();

			String te = null;
			while ((te = reader.readLine()) != null) {
				sb.append(te);
			}
			if (sb.length() == 0) {
				return;
			}
			temp = sb.toString();

			// while ((temp = reader.readLine()) != null) {
			count++;
			countM++;
			System.out.println(count);
			try {
				try {
					temJson = JSONObject.fromObject(temp);
				} catch (Exception e) {
					logger.error("json异常:" + file.getPath() + "----" + temp);
					return;
				}
				try {
					vo = gs.fromJson(temp, BuildVO.class);
				} catch (Exception e) {
					logger.error("json转vo异常:" + file.getPath() + "----" + temp);
					return;
				}

				if (vo != null) {

					if (StringUtils.isNull(vo.getCompanyName())) {
						return;
					}

					if (null != vo.getBusinessLicenseNo()) {
						key1 = StringUtils.NBG.generate(vo.getCompanyName()).toString();
					} else if(!StringUtils.isNull(vo.getCompanyName())) {
						key1 = StringUtils.NBG.generate(vo.getCompanyName()).toString();
					}else{
						key1=StringUtils.getRandomUUid();
					}
					
					int flagS=0;
					System.out.println(count+ "  "+key1);
					String[] array =null;
					//省市县
					if(StringUtils.isNull(vo.getProvice())){
						
						if(!StringUtils.isNull(vo.getBusinessLicenseNo())){
							PrCiCouVO voP = getAdmin(vo.getBusinessLicenseNo());// 获得省市县
							if (null != voP) {
								if (null != voP.getProvince()) {
									vo.setProvince(voP.getProvince());// 省
								}
								if (null != voP.getCity()) {
									vo.setCity(voP.getCity());// 市
								}
								if (null != voP.getCountry()) {
									vo.setArea(voP.getCountry());// 县
								}
								flagS=1;
							}
						}
						if(0==flagS){
							array =  util.utils_company(vo.getProvice()+" "+vo.getCity()+" "+vo.getArea()+ " "+vo.getCompanyName()+ " "+vo.getDetailAdress()+ " "+vo.getCommercialRegistered());
							if(null!=array[0]){
								vo.setProvince(array[0]);
								vo.setCity(array[1]);
								vo.setArea(array[2]);
								flagS=1;
							}
						}
						
//						if(0==flagS){
//							array =  util.utils_company(vo.getCompanyName());
//							if(null!=array[0]){
//								vo.setProvince(array[0]);
//								vo.setCity(array[1]);
//								vo.setArea(array[2]);
//								flagS=1;
//							}
//						}
//						if(0==flagS){
//							if(!StringUtils.isNull(vo.getCommercialRegistered()) ){//建设通
//								String[] array1 =  util.utils_company(vo.getCommercialRegistered());
//								if(null!=array1[0]){
//									vo.setProvince(array1[0]);
//									vo.setCity(array1[1]);
//									vo.setArea(array1[2]);
//									flagS=1;
//								}
//							}
//							if(!StringUtils.isNull(vo.getDetailAdress()) ){//建设网
//								String[] array1 =  util.utils_company(vo.getDetailAdress());
////								String[] array1 =doAdmin(util.enterp2(vo.getDetailAdress()));
//								if(null!=array1[0]){
//									vo.setProvince(array1[0]);
//									vo.setCity(array1[1]);
//									vo.setArea(array1[2]);
//									flagS=1;
//								}
//							}
//						}
					}

					if (StringUtils.isNull(vo.getContactFax())) {
						vo.setContactFax(null);
					}
					if (StringUtils.isNull(vo.getContactNumber())) {
						vo.setContactNumber(null);
					}
					if (StringUtils.isNull(vo.getContactPerson())) {
						vo.setContactPerson(null);
					}

					List<BuildQulificationVO> listquliT = new ArrayList<BuildQulificationVO>();
					
					String qualification_item=null;
					List<BuildQulificationVO> listquli2 = vo.getQualificationGrade();
					if (null != listquli2 && listquli2.size() > 0) {
						Iterator<BuildQulificationVO> iter = listquli2.iterator();
						while (iter.hasNext()) {
							BuildQulificationVO vq = iter.next();
							vq.setCertificateDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getCertificateDate()));
							vq.setExpireDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getExpireDate()));
							
							
							if(!StringUtils.isNull(  vq.getQualification_item()) ){
								
								if(vq.getQualification_item().contains("：")){//建设通
									
									qualification_item=vq.getQualification_item().substring(vq.getQualification_item().indexOf("：")+1);
									qualification_item=qualification_item.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("一级", "壹级").replaceAll("二级", "贰级").replaceAll("三级", "叁级").replaceAll("四级", "肆级");
									
									vq.setQualification_item(qualification_item);
									listquliT.add(vq);
									
								}else{//建筑平台
									String  tempStr[]= vq.getQualification_item().split(",");
									for(int i4 = 0; i4 < tempStr.length; i4++)
									{
										BuildQulificationVO vq1 =  new BuildQulificationVO();
										vq1.setCertificateNo(vq.getCertificateNo());
										vq1.setExpireDate(vq.getExpireDate());
										vq1.setCertificateDate(vq.getCertificateDate());
										vq1.setQualification_item(tempStr[i4]);
										listquliT.add(vq1);
									}
								}
								
							}else{
								vq.setQualification_item(null);
							}
							
						}
					}
					
					
//					// 建筑质资信息 (建设通)
//					List<BuildQulificationVO> listquli = vo.getQulification();
//					if (null != listquli && listquli.size() > 0) {
//						Iterator<BuildQulificationVO> iter = listquli.iterator();
//						while (iter.hasNext()) {
//							BuildQulificationVO vq = iter.next();
//							vq.setCertificateDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getCertificateDate()));
//							vq.setExpireDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getExpireDate()));
//							vq.setQualification_item(vq.getQualification_item() != null ? vq.getQualification_item()
//									: vq.getQualifiedScope() != null ? vq.getQualifiedScope() : null);
//							vq.setQualifiedScope(null);
//							listquliT.add(vq);
//						}
//					}
//					// 建筑质资信息 (建筑监督)--------------建设通也用了
//					List<BuildQulificationVO> listquli2 = vo.getQualificationGrade();
//					if (null != listquli2 && listquli2.size() > 0) {
//						Iterator<BuildQulificationVO> iter = listquli2.iterator();
//						while (iter.hasNext()) {
//							BuildQulificationVO vq = iter.next();
//							vq.setCertificateDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getCertificateDate()));
//							vq.setExpireDate(DateUtils.toYMDOfChaStr_ESZZ2(vq.getExpireDate()));
//							
//							vq.setQualification_item(vq.getQualification_item() != null ? vq.getQualification_item()
//									: vq.getQualifiedScope() != null ? vq.getQualifiedScope() : null);
//							vq.setQualifiedScope(null);
//							listquliT.add(vq);
//						}
//					}

					if (listquliT.size() > 0) {
						vo.setQulification(null);
						vo.setQualificationGrade(listquliT);
					} else {
						log.info("无资质： " + temp);
						return;

					}
					
					vo.setSource(source);

					vo.setBuildId(key1);
					// 更新时间
					vo.setUpdateTime(DateUtils.getDateyyyyMMddhhmmssZZ());
					vo.setCollectTime(DateUtils.toYMDOfChaStr_ESZZ2(vo.getCollectTime()));
					content = JsonObject.fromJson(gs.toJson(vo));
					documents.add(JsonDocument.create(key1, content));
					countLen++;

					while (true) {
						try {
							// 更新文档
							bucket = CouchbaseConnect.commonBucket("192.168.1.114:8091", "build");
							break;
						} catch (Exception e) {
							log.info("---------------------------> 插入BC超时");
							log.error(e.getMessage());
						}
					}

					bucket.upsert(JsonDocument.create(key1, content));

					// System.out.println(
					// "---线程名" + Thread.currentThread().getName() + "---- " +
					// countLen + " " + key1);

					System.out.println("---- " + countLen + "   " + key1);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			// }

		} catch (Exception e) {
			logger.info(e);
		} finally {
			try {

				// logger.info("结束!!!!!!!!!!!");
				// logger.info("空数据" + countNull);
				//
				// long endTime = System.currentTimeMillis();
				// Date endDate = new Date(endTime);
				// System.out.println("结束时间--------------------" +
				// formatter.format(endDate));
				// System.out.println("Took : " + (float) ((endTime - startTime)
				// / 1000) + "秒");
				// System.out.println("Took : " + (float) ((endTime - startTime)
				// / 1000) / 60 + "分钟");
				// System.out.println("Total : " + count);
				// System.out.println(
				// "Took : " + (float) ((float) ((float) ((endTime - startTime)
				// / 1000) / 60) / 60) + "小时");
				// System.out.println("Speed : "
				// + (float) (count / ((float) ((float) ((endTime - startTime) /
				// 1000) / 60) / 60)) + "个/小时");

			} catch (Exception e) {
				logger.info(e);
			}
		}
	}

	public static void show(File file,Integer source,String coding ) throws IOException, ParseException {
		if (file.isFile()) {
			try {
				readFile(file,source,coding);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		if (null != files) {
			for (File fi : files) {
				if (fi.isFile()) {
					try {
						readFile(fi,source,coding);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					show(fi,source,coding);
				} else {
					continue;
				}
			}
		}
	}
	
	/**
	 * 处理注册号、信用代码
	 * 
	 * @param regNum
	 * @return
	 */
	public static PrCiCouVO getAdmin(String regNum) {
		List<CodeVO> code = PrCiCouText.getregNumList(regNum);
		PrCiCouVO f = null;
		for (CodeVO ce : code) {
			try {
				// 注册号
				if (ce.getStatus() == 1) {
					String va = ce.getCode().substring(0, 6);
					f = PrCiCouText.prcicouName(va);
				} else {
					// 信用代码
					String va = ce.getCode().substring(2, 8); // 从第2位开始截取
					f = PrCiCouText.prcicouName(va);
				}
				if (null != f) {
					return f;
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("getAdmin:" + e);
			}
		}
		return null;
	}
	

}
