package cn.com.szgao.enterprise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.HolderVO;
import cn.com.szgao.dto.IndustryVO;
import cn.com.szgao.util.ConfigUtils;
import cn.com.szgao.util.FileUtils;
import cn.com.szgao.util.StringUtils;


/**
 * COPY文件夹
 * @author MassPick
 * @Date 2016年7月14日 下午3:40:02
 */
public class CopyFolderFromFolder {
	private static Logger log = LogManager.getLogger(FileIntoDataBase2p5.class.getName());

	static {
		PropertyConfigurator.configure(ConfigUtils.getPropertyValue("log4j.propertiespath"));
	}

	static Map<String, BufferedWriter> mapFW = new HashMap<String, BufferedWriter>();
	static Map<String, String> mapProvince = new HashMap<String, String>();
	

	

//	static String fileP="D:\\工商数据已清洗20160705合并省\\";
//	static {
//
//		BufferedWriter fw = null;
//		String encoding_from1 = "UTF-8";
//		mapProvince = DataUtils.listProince();
////		mapProvince.put("其他", "其他");
//
//		for (Entry<String, String> entry : mapProvince.entrySet()) {
//			// System.out.println("keyV= " + entry.getKey() + " and
//			// value= " + entry.getValue());
//			if (entry.getValue() != null && entry.getKey() != null && !"".equals(entry.getKey())) {
//
//				File fileS = new File(fileP + entry.getValue() +  "总局.json");
//				try {
//					if (!fileS.exists()) {
//						try {
//							fileS.createNewFile();
//						} catch (IOException e) {
//							e.printStackTrace();
//							log.error(e);
//						}
//						try {
//							fw = new BufferedWriter(
//									new OutputStreamWriter(new FileOutputStream(fileS, true), encoding_from1));
//						} catch (UnsupportedEncodingException e) {
//							e.printStackTrace();
//						} // 指定编码格式，以免读取时中文字符异常
//						mapFW.put(entry.getKey(), fw);
//					} else {
//						// fileS.delete();
//						// fileS = new File(filePath);
//						// fw = new BufferedWriter(new OutputStreamWriter(new
//						// FileOutputStream(fileS, true), encoding_from1)); //
//						// 指定编码格式，以免读取时中文字符异常
//					}
//				} catch (FileNotFoundException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}
//	}
	
//	static BufferedWriter fwUnOther = null;
//	static{
//		// 创建文件夹
////				FileUtils.newFolder(folderPath);
////				File fileS = new File(filePath);
//				String encoding_from1 = "UTF-8";
//				BufferedWriter fw = null;
//
////				String folderPathUn = "D:/lm/log/工商清洗数据异常/" + ss2;
////				String filePathUn = "D:\\lm\\log\\工商清洗数据异常\\" + ss;
////				// 创建文件夹
////				FileUtils.newFolder(folderPathUn);
//				File fileSUn = new File(fileP+"其他"+  "总局.json");
//				String encoding_from1U = "UTF-8";
//				
//				try {
//					if (!fileSUn.exists()) {
//						try {
//							fileSUn.createNewFile();
//						} catch (IOException e) {
//							e.printStackTrace();
//							log.error(e);
//						}
//						try {
//							fwUnOther = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U));
//						} catch (UnsupportedEncodingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} // 指定编码格式，以免读取时中文字符异常
//					} else {
////						fileSUn.delete();
////						fileSUn = new File(filePathUn);
////						fwUnOther = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSUn, true), encoding_from1U)); // 指定编码格式，以免读取时中文字符异常
//					}
//				} catch (FileNotFoundException e1) {
//					e1.printStackTrace();
//				}
//		
//	}

	/**
	 * 公司ID集合，
	 */
	List<String> companyIdList = new ArrayList<String>();
	static int count = 0;
	
	static int countNum = 0;
	static Set<String> holderTypeSet = new HashSet<String>();

	public static void main(String[] args) {
		CopyFolderFromFolder ff=new CopyFolderFromFolder();
		try {
			
//			ff.show(new File("D:\\工商数据已清洗20160705\\rinsecom_new"), 0);
//			ff.show(new File("D:\\工商数据已清洗20160705\\160407后补"), 0);
			ff.show(new File("D:\\工商数据已清洗20160705合并省"), 0);
			
			// 输出结果 张三 李四 王五
			for (String name : holderTypeSet) {
//				System.out.print(name + "\t");
				log.info(name);
			}

			
			log.info("------------------------------------>>  完    ---------总数： "+countNum    +"  文件数: "+count);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void show(File file, int startNum) throws IOException, ParseException {
		System.out.println(file.getPath());
		if (file.isFile()) {
			count += 1;
			log.info("---------------数量:" + count + "---线程名" + Thread.currentThread().getName() + "---"
					+ file.getPath());
//			try {
//				if(file.getName().indexOf("json")!=-1){
//					readFileByLines(file, startNum);
//				}
//				
//			} catch (Exception e) {
//
//				e.printStackTrace();
//			}
			// file.delete();
			return;
		}
		File[] files = file.listFiles();
		if (null != files) {
			for (File fi : files) {
				if (fi.isFile()) {
					count += 1;
					log.info("---------------------数量:" + count + "---线程名" + Thread.currentThread().getName() + "---"
							+ fi.getPath());
					try {
//						if(fi.getName().indexOf("json")!=-1){
//							readFileByLines(fi, startNum);
//						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
						log.info(e);
					}
					// fi.delete();
				} else if (fi.isDirectory()) {
					
					//创建文件夹
//					show(fi, startNum);
					FileUtils.newFolder(fi.getPath().replace("D:", "C:").replace("工商数据已清洗20160705合并省", "股东企业关联中间数据"));
					
				} else {
					continue;
				}
			}
		}
	}

	
	
	
	private void readFileByLines(File file, int startNum) throws Exception {

		// 无基本信息统计数据
		int basicSum = 0;
		// 无注册号统计数据
		int regSum = 0;

		// 文件名当批次号
		String batchNum = file.getName();
		// 拼接字符对象
		StringBuffer sb = new StringBuffer();
		// FileReader fr = new FileReader(file);
		// int ch = 0;
		// while((ch = fr.read())!=-1 )
		// {
		// sb.append((char)ch);
		// }
		// fr.close();
		// fr = null;

		// BufferedWriter fw = null;
		String encoding_from = "UTF-8";// GB18030
		// String encoding_to = "UTF-8";
		BufferedReader reader = null;
		try {
			// 华 GB18030

			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "UTF-8");
			// InputStreamReader isr = new InputStreamReader(new
			// FileInputStream(file), "GBK");
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encoding_from);
			reader = new BufferedReader(isr);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String tempT = null;

		int readNum = 0;

		// E:\刘铭\data\深圳市\工商-深圳市-0008.txt
//		String ss = file.getPath().substring(file.getPath().indexOf("data") + 4);
//		String ss2 = file.getPath().substring(file.getPath().indexOf("data") + 5, file.getPath().lastIndexOf("\\"));
//
//		String folderPath = "D:/lm/log/工商清洗数据/" + ss2;
//		String filePath = "D:\\lm\\log\\工商清洗数据" + ss;

		

		int M = 1;// 1M上限
		while ((tempT = reader.readLine()) != null) {

			// tempT="";

			long size = tempT.getBytes().length;
			if (size > M * 1024 * 1024) {
				log.error("--------数据大于1M :" + tempT.substring(0, 500));
				// writerString(fwUn, readNum + " 数据大于1M|" + tempT);
				continue;
			}
			

			int flag=0;
			EnterpriseVO arch = StringUtils.GSON.fromJson(tempT, EnterpriseVO.class);
			
			if(arch.getHolder()!=null&&arch.getHolder().size()>0){
				
				List<HolderVO> list= arch.getHolder();
				for (HolderVO holderVO : list) {
					if(!StringUtils.isNull(holderVO.getType())){
						System.out.println("---  "+holderVO.getType());
						holderTypeSet.add(holderVO.getType());
					}
				}
				 
			}
			
			
			 
			
			countNum++;
			System.out.println(countNum);

		}
	}
	public static void writerString(BufferedWriter fw, String str) {
		try {
			// fw.append(str + "\n");
			fw.append(str + System.getProperty("line.separator"));

			// fw.newLine();
			fw.flush(); // 全部写入缓存中的内容
		} catch (Exception e) {
			log.error("写文件异常" + e.getMessage());
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

}
