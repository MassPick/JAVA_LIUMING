package cn.com.szgao.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import cn.com.szgao.dto.ABC;
import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.PrCiCouVO;
import cn.com.szgao.dto.ReportGuaranteeVO;
import cn.com.szgao.enterprise.FileIntoDataBase2p5;
import cn.com.szgao.util.CouchbaseConnect;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.ObjectUtils;
import cn.com.szgao.util.PrCiCouText;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.DataUtils;
import net.sf.json.JSONObject;

public class TestShenShiXian_Company {

	public static String getBanking(String str) {
		if (null == str || "".equals(str)) {
			return null;
		}
		str = str.replaceAll("&nbsp;", "");
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
		return rel;
	}

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
	 * Spring 提供的 PropertiesLoaderUtils 允许您直接通过基于类路径的文件地址加载属性资源
	 * 最大的好处就是：实时加载配置文件，修改后立即生效，不必重启
	 */
	private static void springUtil() {
		Properties props = new Properties();
		while (true) {
			try {
				props = PropertiesLoaderUtils.loadAllProperties("message.properties");
				String ss = props.getProperty("log4j.propertiespath");
				for (Object key : props.keySet()) {
					System.out.print(key + ":");
					System.out.println(props.get(key));
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// private void test1() throws IOException {
	//
	// new Properties(arg0)
	// //properties(filename).get("ss")
	// // response.setContentType("text/html;charset=utf-8");
	// String path = "/WEB-INF/jdbc_connection.properties"; // 读取WEB-INF中的配置文件
	// String realPath = getServletContext().getRealPath(path);//
	// getServletContext()相当于http://localhost/demo05
	// // 所以后面的path只需要以应用demo/开头具体的部署目录路径即可，如上面的/web-in…
	// System.out.println(realPath);
	// InputStreamReader reader = new
	// InputStreamReader(newFileInputStream(realPath), "utf-8");
	// Properties props = new Properties();
	// props.load(reader); //
	// load个人建议还是用Reader来读，因为reader体系中有个InputStreamReader可以指定编码
	// String jdbcConValue = props.getProperty("jdbc_con");
	// System.out.println(jdbcConValue);
	// System.out.println("加载src包下的资源------------------------");
	// path = "/WEB-INF/classes/com/test/servlet/jdbc_connection.properties"; //
	// 读取WEB-INF中的配置文件
	// realPath = getServletContext().getRealPath(path);
	// System.out.println(realPath);
	// reader = new InputStreamReader(new FileInputStream(realPath), "utf-8");
	// props.load(reader); //
	// load个人建议还是用Reader来读，因为reader体系中有个InputStreamReader可以指定编码
	// jdbcConValue = props.getProperty("jdbc_con");
	// System.out.println("second::" + jdbcConValue);
	//
	// }

	
	public static void main(String[] args) throws IOException {
		
//		System.out.println("AAAA".contains(""));
		
		String encoding_from = "UTF-8";// GB18030
		BufferedReader reader = null;
		
		
		int startNum=0;//第几行
		File file=new File("E:\\刘铭\\data\\合并各省\\台湾省.json");
		
		try {
			
			InputStreamReader isr = null;
			try {
				isr = new InputStreamReader(new FileInputStream(file), encoding_from);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		int readNum = 0;
		String tempT = null;
		while ((tempT = reader.readLine()) != null) {

			// tempT="";
			readNum++;

			if (readNum < startNum) {
				continue;
			}
			
			JSONObject temJson = null;
			EnterpriseVO enterVO = new EnterpriseVO();
			try {
				temJson = JSONObject.fromObject(tempT);
			} catch (Exception e) {
				continue;
			}
			try {
				enterVO =StringUtils.GSON .fromJson(tempT, EnterpriseVO.class);
				enterVO.setProvince(null);
				enterVO.setCity(null);
				enterVO.setArea(null);
				
				FileIntoDataBase2p5 ff=new FileIntoDataBase2p5();
				
				ff.clearRegNum(enterVO);// 获得注册号和信用代码,省市县
				
				System.out.println(enterVO.getProvince());
				System.out.println(enterVO.getCity() );
				System.out.println(enterVO.getArea());
				
			} catch (Exception e) {
			 
				continue;
			}
			
			
			
		}
		
		
//		PrCiCouVO vo = getAdmin("422326000012242");// 获得省市县 //		8d149b49-b798-5985-bf58-74992c0223dc
//		System.out.println(vo.getProvince()+  "  "+vo.getCity()+"  "+vo.getCountry() );
		
//		String admin[] = doAdmin(new AdministrationUtils().enterp2("通山县工商行政管理局"));
//		String admin[] = doAdmin(new AdministrationUtils().enterp2("华容县工商行政管理局"));//台湾省  台中市  北屯市
//		String admin[] = doAdmin(new AdministrationUtils().enterp2("岳阳市华容县工商行政管理局"));//湖南省  岳阳市  null
		
//		String admin[] = doAdmin(new AdministrationUtils().enterp2("通山县通羊镇洋都大道天鸿美庐小区2-204"));
		
//		String admin[] = doAdmin(new AdministrationUtils().enterp2("福建省南靖县工商行政管理局"));
//		System.out.println(admin[0]+  "  "+admin[1]+"  "+admin[2] );
		
		
		
		
		
//		 List<String> li2 = new ArrayList<String>()  ;
//		 li2. add ("8");
//		 li2. add ("8");
//		 li2. add ("9");
//		 li2. add ("9");
//		 li2. add ("0");
//		 li2. add ("9");
//		 
//		 for (int i = 0; i < li2.size(); i++)  //外循环是循环的次数
//         {
//             for (int j = li2.size() - 1 ; j > i; j--)  //内循环是 外循环一次比较的次数
//             {
//
//                 if (li2.get(i)  == li2.get(j) )
//                 {
//                	 li2.remove(j);
//                 }
//             }
//         }
//		 for (String str  : li2) {
//			System.out.println(str);
//		}

//		springUtil();

		//// String sss="\r\n\t\t\t\t\t\t
		//// \r\n\t\t11&nbsp\t\t\t\t\t杜进宁\r\n\t\t\t\t\t\t杜进宁
		//// \t\t\r\n\t\t杜进宁\t\t\t\t ";
		// String sss="";
		//// String ss=sss.replace("[\r\n\t&nbsp]", "");
		// String ss=sss.replace("\r", "").replace("\t", "").replace("\n",
		//// "").replace("&nbsp", "").replace("&nbsp;", "");
		// System.out.println(ss);

		// String ss="\"货币\"";
		//// String s=ss.replace ("", "");
		//// String s=ss ;
		// System.out.println(ss);

		// HashMap<String, String> ss=new HashMap<String, String>();
		// ss.put("11", "231432141");
		// ss.put("11","333333");
		// Iterator iter = ss.entrySet().iterator();
		// while (iter.hasNext()) {
		// Map.Entry entry = (Map.Entry) iter.next();
		// String key1 =(String) entry.getKey();
		// String val1 = (String)entry.getValue();
		// System.out.println(key1+ " "+val1);
		// }

		// String ss1 = "2015-08-24T16:17:34.394789+08:00";
		// String ss2 = "2015-08-24T16:17:32.394789+08:00";
		// Date dt1 = DateUtils.strToDateFromZZ(ss1);
		// Date dt2 = DateUtils.strToDateFromZZ(ss2);
		// System.out.println(compareDate(dt1, dt2));

		// System.out.println(getBanking("1,209.7万元"));

		// try {
		// FileUtils.readZipFile("E:/刘铭/data_ya/北京市/工商-北京市-0001.tar.gz");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		/*
		 * String s=
		 * "件业、住宿、餐饮、房地产、商业服务、科 术咨询服务；电力物资和机电设备材料的销售\r\r   \t\t\t  \n\n。 收起更多";
		 * String temp=s. replaceAll("[&nbsp;\r\n\t]", "");
		 * System.out.println(temp);
		 */

		/*
		 * ABC abc=new ABC(); abc.setName(null); CodeVO cv=new CodeVO();
		 * cv.setCode("null"); abc.setCv(cv);
		 * System.out.println(ObjectUtils.isFieldValueNull(abc));
		 */

		// ReportGuaranteeVO guaranteeVO =new ReportGuaranteeVO();
		// guaranteeVO.setDebtor("");
		// guaranteeVO.setDebtAmount(null);

		// System.out.println(StringUtils.removePunctuation("'2011年3月28日'"));

		/*
		 * String scope = "这是〓保没有去显卡〓" ; if (!StringUtils.isNull(scope)) { scope
		 * = scope.replaceAll("[&nbsp;\r\t〓]", ""); if (scope.length() > 5) {
		 * String temp = ""; System.out.println(scope); if
		 * (scope.substring(scope.length() - 5).contains("^")) { temp =
		 * scope.substring(scope.length() - 5).replace("^", ""); scope =
		 * scope.substring(0, scope.length() - 5) + temp; } else { temp =
		 * scope.substring(scope.length() - 5).replaceAll("[★〓#※*+＊■★]", "");
		 * scope = scope.substring(0, scope.length() - 5) + temp; } }
		 * System.out.println(scope); }
		 */

		/*
		 * String s="2015-09-02T04:07:01.658587+08:00"; if(
		 * s.contains(".")&&s.contains("+")&&s.contains("T")){
		 * 
		 * s=s.replace("T", " "); // SimpleDateFormat sdftime1 = new
		 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat sdftime1 = new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // SimpleDateFormat sdftime1
		 * = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
		 * 
		 * try { Date dd1 = sdftime1.parse(s);
		 * 
		 * SimpleDateFormat sdftime = new
		 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ"); String strTemp =
		 * sdftime.format(dd1); System.out.println(strTemp); } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * //2016-03-11T14:26:26+0800
		 * System.out.println(DateUtils.getDateyyyyMMddhhmmssZZ());
		 * 
		 * // System.out.println(deleteMoreFuhao(" \r\n【企业经营涉及行政许可的，凭许可证件经营 "));
		 * 
		 * }
		 */

	}
	
	static String url = "jdbc:postgresql://192.168.1.2:5432/duplicatedb?useServerPrepStmts=false&rewriteBatchedStatements=true";
	static String usr = "postgres";
	static String psd = "615601.xcy*";
	static {
		try {
			Class.forName("org.postgresql.Driver");
			PrCiCouText.connection = DriverManager.getConnection(url, usr, psd);
			 DataUtils.initData();

			// 加载动名词
			// File file = new
			// File("D:\\data\\mavenSpace6_1\\Mass\\data\\名词＋动词的结果.xlsx");

			try {
				// mapN = ExcelUtils.getListFromExcelByLineTwoColumn(file, 0, 1,
				// 0, 4);
				// mapV = ExcelUtils.getListFromExcelByLineTwoColumn(file, 0, 1,
				// 0, 4);

//				mapN = new DataUtils().listIndustryN();
//				mapV = new DataUtils().listIndustryV();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			while (true) {
//				try {
//					bucket = CouchbaseConnect.commonBucket("192.168.1.30:8091", "businessD");
//					break;
//				} catch (Exception e) {
//					log.info("---------------------------> 连BC超时");
//					log.error(e.getMessage());
//				}
//			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PrCiCouText.init();
	}
	
	
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
//				log.error("getAdmin:" + e);
			}
		}
		return null;
	}
	
	/**
	 * 行政区划数组处理直辖市
	 * 
	 * @param admin
	 *            行政区数组
	 * @return 行政区数组结果
	 */
	public static String[] doAdmin(String admin[]) {
		if (null != admin[0] || null != admin[1]) {
			if (null != admin[1] && (admin[1].equals("北京市") || admin[1].equals("天津市") || admin[1].equals("重庆市")
					|| admin[1].equals("上海市"))) {
				admin[0] = admin[1];
				admin[1] = null;
			}
		}
		return admin;
	}
	

	public static String deleteMoreFuhao(String doTemp) {
		return doTemp.replace("&nbsp", "").replace(";", "").replace("\r", "").replace("\t", "").replace("\n", "")
				.replace(" ", "").replace("\"", "");
	}

}
