package cn.com.szgao.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import cn.com.szgao.dto.IndustryVO;
import cn.com.szgao.util.ExcelUtils;
import cn.com.szgao.util.StringUtils;
import cn.com.szgao.wash.data.DataUtils;

/**
 * 清洗行业 由名称得到库中的行业对应词条
 * 
 * @author liuming
 * @Date 2016年10月31日 下午5:29:37
 */
public class WashIndustryGet {

	public static Map<String, IndustryVO> mapVN_N = null;

	public static Map<String, IndustryVO> mapVN_N_MO = null;

	public static List<String> list_temp = null;

	static {
		mapVN_N = new DataUtils().listIndustryN_V_New_ASC();
	}

	public static void main(String[] args) {
		File file = new File("C:\\Users\\liuming\\Documents\\Tencent Files\\690452074\\FileRecv\\行报让刘铭读出行业来.xlsx");

		int flag = 0; // 0未匹配到 1 匹配到
		
		int ii=0;
		String[] arrStr1 =  new String[mapVN_N.size()] ;
		for (Entry<String, IndustryVO> entry : mapVN_N.entrySet()) {
			arrStr1[ii]=entry.getKey().trim();
			ii=ii+1;
		}
		
	
		
		StringUtils.sortStringArray(arrStr1, 0);// 排序
		
//		for (int i = 0; i < arrStr1.length; i++) {
//			if(arrStr1[i].length()==2){
//				System.out.println(arrStr1[i]);
//			}
//		}
		

		try {
			list_temp = ExcelUtils.getListFromExcelByLine(file, "Sheet1", 1, 0);
			for (String li : list_temp) {

//				System.out.println(li);
				
				for (int i = 0; i < arrStr1.length; i++) {
					if (li.trim().equals(arrStr1[i])) {
						System.out.println(li + "#" + mapVN_N.get(arrStr1[i]). getIndustry_id() + "#"
								+ mapVN_N.get(arrStr1[i]).getIndustry_name() + "#" + mapVN_N.get(arrStr1[i]).getIndustry_code()
								+ "#" + "1");
						flag = 1;
						break;
					} else {
						flag = 0;
					}
				}
				
				if (flag == 0) {
					
					for (int i = 0; i < arrStr1.length; i++) {
						if (arrStr1[i].indexOf(li.trim())!=-1) {
							System.out.println(li + "#" + mapVN_N.get(arrStr1[i]). getIndustry_id() + "#"
									+ mapVN_N.get(arrStr1[i]).getIndustry_name() + "#" + mapVN_N.get(arrStr1[i]).getIndustry_code()
									+ "#" + "2");
							flag = 1;
							break;
						} else {
							flag = 0;
						}
					}
				}

				if (flag == 0) {
					System.out.println(li + "#" + "#" + "#" + "#" + "0");
				}

			}

		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
