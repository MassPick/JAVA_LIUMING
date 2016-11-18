package cn.com.szgao.wash.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import cn.com.szgao.util.StringUtils;

/**
 * 通过企业名称和法院名称获得行政区划
 * 
 * @author xiongchangyi
 * @version V1.0
 */
public class AdministrationUtils {
	DataUtils utils = new DataUtils();
	Map<String, String> map = null;

	/**
	 * 工具的主方法
	 * 
	 * @param doString
	 *            要处理的字符串：法院名称、企业名称
	 * @return 数组第1个元素是省，第2个是地级市，第3个元素是县
	 */
	public String[] utils(String doString) {
		if (null == doString) {
			return null;
		}
		String result[] = new String[3];
		if (doString.contains("河南蒙古族")) {
			
			result[0] = "青海省";// result[0]存储省
			result[1] = "黄南藏族自治州"; // result[1]存储地级市
			result[2] = "河南蒙古族自治县"; // result[2]存储县
			return result;
		}
		
		int count=0;
		String resultTemp[] = new String[3];
		
		// 需要处理的字符串
		doString = doString.replace(" ", "");
		// 包含法院
		if (doString.contains("法院")) {
			
			//------------------ 先遍历法院列表
			for (Entry<String, Object> entry : mapCourtName.entrySet()) { //  
				String tempXian = entry.getKey();
				if (doString.equals(tempXian)) {
//					if (doString.contains(tempXian)) {
					resultTemp  =entry.getValue().toString().split("-");
					if( "NULL" .equals(resultTemp[0])){
						resultTemp[0]=null;
					}
					if( "NULL" .equals(resultTemp[1])){
						resultTemp[1]=null;
					}
					if( "NULL" .equals(resultTemp[2])){
						resultTemp[2]=null;
					}
					return resultTemp;
				}
			}
			
			return court(doString);// 法院数据
		}
		// 公司
		else {
			return enterp(doString);// 企业名录数据, 结果数组有可能为空
		}
	}
	
	
	public String[] utils_company(String doString) {
		if (null == doString) {
			return null;
		}
		String result[] = new String[3];
		if (doString.contains("河南蒙古族")) {
			
			result[0] = "青海省";// result[0]存储省
			result[1] = "黄南藏族自治州"; // result[1]存储地级市
			result[2] = "河南蒙古族自治县"; // result[2]存储县
			return result;
		}
		
		int count=0;
		String resultTemp[] = new String[3];
		
		return court(doString);// 法院数据
	}
	
	
	public static Map<String, Object> mapCourtName = null;
	static{
		mapCourtName = new DataUtils().listCourtName();
	}

	/**
	 * 处理企业名录 提取省、市、县
	 * 
	 * @param doString
	 *            ：需要处理的字符串；utils：工具类对象
	 * @Vesion 1.0
	 * @author xiongchangyi
	 * @since 2015-6-12
	 */
	public String[] enterp(String doString) {
		if (null == doString || "".equals(doString)) {
			return null;
		}
		// 将名称分成2截 这里可能的漏洞是分公司、与没有分公司的情况，需要验证是否可行
		int length = doString.length();
		String oneString = doString.substring(0, length / 2);
		String twoString = doString.substring(length / 2);
		Map<Integer, String> resultMap = subCompanyDo(oneString, twoString, doString);// 提取省市县区
		// 处理结果的数组：result[0]存储省 result[1]存储地级市 result[2]存储县
		return doProvinceCityCountry(resultMap);
	}

	/**
	 * 在企业名称、法院名称上提取的行政区 去获得完整的省、市、县
	 * 
	 * @param resultMap
	 *            ： 需要完整的行政处
	 * @return
	 */
	public String[] doProvinceCityCountry(Map<Integer, String> resultMap) {
		DataUtils utils = new DataUtils();
		// 处理结果的数组：result[0]存储省 result[1]存储地级市 result[2]存储县
		String result[] = new String[3];
		String province_result = null;
		String city_result = null;
		String country_result = null;
		// 处理省、市、县
		if (null != resultMap) {
			province_result = resultMap.get(1);
			city_result = resultMap.get(2);
			country_result = resultMap.get(3);

			if ("北京市".equals(province_result) || "天津市".equals(province_result) || "重庆市".equals(province_result)
					|| "上海市".equals(province_result)) {
				city_result = null;
			}
			// 有县无地级市，则查地级市，附带判断省是否存在
			if (null == city_result && null != country_result) {

				if (null != province_result) {// 有省
					Map<Integer, String> map = utils.listCityIdByProvinceName(province_result);
					if (null != map && map.size() != 0) {
						Map<Integer, String> map2 = utils.listCountryCityIdBycountryName(country_result); // 得到查询县名对应的市
																											// 可能有多行，如长安区就在不同的市存在
						for (Map.Entry<Integer, String> entry : map.entrySet()) { // 得到某省下的市ID
							for (Map.Entry<Integer, String> entry2 : map2.entrySet()) {
								if (entry.getKey().equals(entry2.getKey())) { // 市ID相等
									city_result = entry.getValue();
									break;
								}
							}
						}
					}
				} else {// 无省
						// 获得地级市ID
					int city_id = DataUtils.xianJiShiXianQuMap.get(country_result);

					if (city_id == 400 || city_id == 401 || city_id == 402 || city_id == 403) {
						String province_name = utils.listProvinceNameByProvinceId(city_id);
						if (null != province_name) {
							province_result = province_name;
						}
					} else {
						// 通过地级市ID，查询地级市名称
						// 查询【地级市名称和省ID】,cityNameAndProvinceIdMap
						Map<String, Integer> map = utils.listCityProvinceIdByCityId(city_id);
						if (null != map && map.size() != 0) {
							Set<String> set = map.keySet();
							Iterator<String> it = set.iterator();
							if (it.hasNext()) {
								city_result = it.next();
							}
							// 无省，则查省
							if (null == province_result) {
								// 获得省ID
								int province_id = map.get(city_result);
								// 通过省ID查询省名称
								String province_name = utils.listProvinceNameByProvinceId(province_id);
								if (null != province_name) {
									province_result = province_name;
								}
							}
						}
					}
				}

			}
			// 有市，县有则有，无则无法查
			// 有市无省
			if (null != city_result && null == province_result) {
				// 通过市名称，查询省ID
				int province_id = DataUtils.province_city_map.get(city_result);
				// 通过省ID查询省名称
				String province_name = utils.listProvinceNameByProvinceId(province_id);
				if (null != province_name) {
					province_result = province_name;
				}
			}
		}
		result[0] = province_result;// result[0]存储省
		result[1] = city_result; // result[1]存储地级市
		result[2] = country_result; // result[2]存储县
		utils = null;
		return result;
	}

	/**
	 * 提取省、地级市、县【县级市、县、旗、区】
	 * 
	 * @param oneString
	 *            企业名称字符串的前半部分
	 * @param twoString
	 *            企业名称字符串的后半部分
	 * @return map: key=1是省，key=2是地级市，key=3是县
	 * @Vesion 1.0
	 * @author xiongchangyi
	 * @since 2015-6-12
	 */
	public Map<Integer, String> subCompanyDo(String oneString, String twoString, String doString) {
		Map<Integer, String> resultMap = new HashMap<Integer, String>();// 省的key是1，市的key是2，县的key是3
		List<String> shengList = new ArrayList<String>();
		shengList.addAll(DataUtils.shengMap.keySet());

		List<String> shiList = new ArrayList<String>();
		shiList.addAll(DataUtils.shiMap.keySet());

		List<String> xianList = new ArrayList<String>();
		xianList.addAll(DataUtils.xianJiShiXianMap.keySet());

		// 遍历省，在第2截里面找【省】
		for (int i = 0; i < shengList.size(); i++) {
			String tempSheng = shengList.get(i);
			if (twoString.contains(tempSheng + "路")) // 避免 陕西路
			{
				if (twoString.contains(DataUtils.shengMap.get(tempSheng))) {
					resultMap.put(1, DataUtils.shengMap.get(tempSheng));// 省
					break;
				}
			} else if (twoString.contains(tempSheng)) {
				resultMap.put(1, DataUtils.shengMap.get(tempSheng));// 省
				break;
			}
		}

		// 遍历地级市 ，在第2截里面找【地级市】
		for (int i = 0; i < shiList.size(); i++) {
			String tempShi = shiList.get(i);
			if (twoString.contains(tempShi + "路")) // 避免类似 "天津路" 的情况
			{
				// 使用类似 "天津市"去判断
				if (twoString.contains(DataUtils.shiMap.get(tempShi))) {
					resultMap.put(2, DataUtils.shiMap.get(tempShi)); // 地级市
					break;
				}
			} else if (twoString.contains(tempShi)) {
				resultMap.put(2, DataUtils.shiMap.get(tempShi)); // 地级市
				break;
			}
		}

		if (null == resultMap.get(2)) {
			// 遍历县级市、县、旗，在第2截里面找 【县级市、县、旗】
			for (int i = 0; i < xianList.size(); i++) {
				String tempXian = xianList.get(i);
				if (twoString.contains(tempXian)) {
					// 需要往前推一个字，避免出现处理：北京市密云县人民法院 结果：省：null 市：北京市 县：云县
					int index = twoString.indexOf(tempXian);
					String temp = twoString.substring(0, index);
					if (temp.length() > 0) // 避免 云县人民法院
					{
						String sub = twoString.substring(index - 1, index);// 云县，再往前推一个字
						if (null != DataUtils.xianJiShiXianMap.get(sub + tempXian)) {
							resultMap.put(3, DataUtils.xianJiShiXianMap.get(sub + tempXian)); // 县、县级市、旗
							break;
						}
					}
					resultMap.put(3, DataUtils.xianJiShiXianMap.get(tempXian)); // 县、县级市、旗
					break;
				}
			}

			// 无县级市、县、旗，则找区
			if (null == resultMap.get(3)) {
				// 遍历区
				if (null == resultMap.get(3)) {
					for (int i = 0; i < DataUtils.quList.size(); i++) {
						String tempArea = DataUtils.quList.get(i);
						if (twoString.contains(tempArea)) {
							// 需要往前推一个字，避免出现处理：北京市密云县人民法院 结果：省：null 市：北京市 县：云县
							int index = twoString.indexOf(tempArea);
							String temp = twoString.substring(0, index);
							if (temp.length() > 0) // 避免 云县人民法院
							{
								String sub = twoString.substring(index - 1, index);// 云县，再往前推一个字
								if (null != DataUtils.xianJiShiXianMap.get(sub + tempArea)) {
									resultMap.put(3, DataUtils.xianJiShiXianMap.get(sub + tempArea)); // 县、县级市、旗
									break;
								}
							}
							resultMap.put(3, tempArea);
							break;
						}
					}
				}
			}
		}
		// 第2截里面，没有找到 【省、市、县】且一个都没没找到，则才去找【第1截】
		if (null == resultMap.get(1) && null == resultMap.get(2) && null == resultMap.get(3)) {
			// 在第1截里面找【省】
			for (int i = 0; i < shengList.size(); i++) {
				String tempSheng = shengList.get(i);
				if (oneString.contains(tempSheng + "路")) // 避免 陕西路
				{
					if (oneString.contains(DataUtils.shengMap.get(tempSheng))) // 拿全面去判断是否存在
					{
						resultMap.put(1, DataUtils.shengMap.get(tempSheng));// 省
						break;
					}
				} else if (oneString.contains(tempSheng)) {
					resultMap.put(1, DataUtils.shengMap.get(tempSheng));// 省
					break;
				}
			}

			// 在第1截里面找【地级市】
			for (int i = 0; i < shiList.size(); i++) {
				String tempShi = shiList.get(i);
				if (oneString.contains(tempShi + "路")) // 避免类似 "天津路" 的情况
				{
					// 使用类似 "天津市"去判断
					if (oneString.contains(DataUtils.shiMap.get(tempShi))) {
						resultMap.put(2, DataUtils.shiMap.get(tempShi)); // 地级市
						break;
					}
				}
				if (oneString.contains(tempShi)) {
					resultMap.put(2, DataUtils.shiMap.get(tempShi)); // 地级市
					break;
				}
			}
			if (null == resultMap.get(2)) {
				// 在第1截里面找【县、县级市、旗】
				if (null == resultMap.get(3)) {
					for (int i = 0; i < xianList.size(); i++) {
						String tempXian = xianList.get(i);
						if (oneString.contains(tempXian)) {
							// 需要往前推一个字，避免出现处理：北京市密云县人民法院 结果：省：null 市：北京市 县：云县
							int index = oneString.indexOf(tempXian);
							String temp = oneString.substring(0, index);
							if (temp.length() > 0) // 避免 云县人民法院
							{
								String sub = oneString.substring(index - 1, index);// 云县，再往前推一个字
								if (null != DataUtils.xianJiShiXianMap.get(sub + tempXian)) {
									resultMap.put(3, DataUtils.xianJiShiXianMap.get(sub + tempXian)); // 县、县级市、旗
									break;
								}
							}
							resultMap.put(3, DataUtils.xianJiShiXianMap.get(tempXian)); // 县、县级市、旗
							break;
						}
					}
				}

				// 【无】县级市、县、旗，则找【区】
				if (null == resultMap.get(3)) {
					// 遍历区
					for (int i = 0; i < DataUtils.quList.size(); i++) {
						String tempArea = DataUtils.quList.get(i);
						if (oneString.contains(tempArea)) {
							// 需要往前推一个字，避免出现处理：北京市密云县人民法院 结果：省：null 市：北京市 县：云县
							int index = oneString.indexOf(tempArea);
							String temp = oneString.substring(0, index);
							if (temp.length() > 0) // 避免 云县人民法院
							{
								String sub = oneString.substring(index - 1, index);// 云县，再往前推一个字
								if (null != DataUtils.xianJiShiXianMap.get(sub + tempArea)) {
									resultMap.put(3, DataUtils.xianJiShiXianMap.get(sub + tempArea)); // 县、县级市、旗
									break;
								}
							}
							resultMap.put(3, tempArea);
							break;
						}
					}
				}
			}
		}
		// 第1、2截都没找到，就两截合起来再找
		if (null != resultMap && 3 != resultMap.size()) {
			if (null == resultMap.get(1)) {
				// 遍历省，在(1和 2)截里面找【省】
				for (int i = 0; i < shengList.size(); i++) {
					String tempSheng = shengList.get(i);
					if (doString.contains(tempSheng + "路")) // 避免 陕西路
					{
						if (doString.contains(DataUtils.shengMap.get(tempSheng))) {
							resultMap.put(1, DataUtils.shengMap.get(tempSheng));// 省
							break;
						}
					} else if (doString.contains(tempSheng)) {
						resultMap.put(1, DataUtils.shengMap.get(tempSheng));// 省
						break;
					}
				}
			}

			if (null == resultMap.get(2)) {

				if (!"北京市".equals(resultMap.get(1)) || !"重庆市".equals(resultMap.get(1))
						|| !"天津市".equals(resultMap.get(1)) || !"上海市".equals(resultMap.get(1))) {
					// 遍历地级市 ，在 (1和 2)截里面找【地级市】
					for (int i = 0; i < shiList.size(); i++) {
						String tempShi = shiList.get(i);
						if (doString.contains(tempShi + "路")) // 避免类似 "天津路" 的情况
						{
							// 使用类似 "天津市"去判断
							if (doString.contains(DataUtils.shiMap.get(tempShi))) {
								resultMap.put(2, DataUtils.shiMap.get(tempShi)); // 地级市
								break;
							}
						} else if (doString.contains(tempShi)) {
							resultMap.put(2, DataUtils.shiMap.get(tempShi)); // 地级市
							break;
						}
					}
				}
			}
			if (null == resultMap.get(3)) {
				// 遍历县级市、县、旗，在 (1和 2)截里面找 【县级市、县、旗】
				for (int i = 0; i < xianList.size(); i++) {
					String tempXian = xianList.get(i);
					if (doString.contains(tempXian)) {
						// 需要往前推一个字，避免出现处理：北京市密云县人民法院 结果：省：null 市：北京市 县：云县
						int index = doString.indexOf(tempXian);
						String temp = doString.substring(0, index);
						if (temp.length() > 0) // 避免 云县人民法院
						{
							String sub = doString.substring(index - 1, index);// 云县，再往前推一个字
							if (null != DataUtils.xianJiShiXianMap.get(sub + tempXian)) {
								resultMap.put(3, DataUtils.xianJiShiXianMap.get(sub + tempXian)); // 县、县级市、旗
								break;
							}
						}
						resultMap.put(3, DataUtils.xianJiShiXianMap.get(tempXian)); // 县、县级市、旗
						break;
					}
				}

				// 无县级市、县、旗，则找区
				if (null == resultMap.get(3)) {
					// 遍历区
					if (null == resultMap.get(3)) {
						for (int i = 0; i < DataUtils.quList.size(); i++) {
							String tempArea = DataUtils.quList.get(i);
							if (doString.contains(tempArea)) {
								// 需要往前推一个字，避免出现处理：北京市密云县人民法院 结果：省：null 市：北京市
								// 县：云县
								int index = doString.indexOf(tempArea);
								String temp = doString.substring(0, index);
								if (temp.length() > 0) // 避免 云县人民法院
								{
									String sub = doString.substring(index - 1, index);// 云县，再往前推一个字
									if (null != DataUtils.xianJiShiXianMap.get(sub + tempArea)) {
										resultMap.put(3, DataUtils.xianJiShiXianMap.get(sub + tempArea)); // 县、县级市、旗
										break;
									}
								}
								resultMap.put(3, tempArea);
								break;
							}
						}
					}
				}
			}
		}
		// 返回的 resultMap有可能为空
		return resultMap;
	}

	/**
	 * 法院
	 */
	public String[] court(String doString) {
		// result[0]是省，result[1]是市， result[3]是县
		@SuppressWarnings("unused")
		String result[] = new String[3];
		Map<Integer, String> resultMap = new HashMap<Integer, String>();

		// 遍历省，在第全截里面找【省】
		for (int ii = 0; ii < shengList.size(); ii++) {
			if (doString.contains(shengList.get(ii))) {
				resultMap.put(1, DataUtils.shengMap.get(shengList.get(ii)));// 省
				// 去掉省
				// doString=doString.replace(tempSheng, "");
				break;
			}
		}

		// 遍历地级市 ，在第全截里面找【地级市】
		for (int i = 0; i < shiList.size(); i++) {
			String tempShi = shiList.get(i);
			if (doString.contains(tempShi)) {
				resultMap.put(2, DataUtils.shiMap.get(tempShi)); // 地级市
				break;
			}
		}

		if (!StringUtils.isNull((resultMap.get(1)))) {// 有省时
			map = utils.listCountryIdByProvinceName((resultMap.get(1)));
			if (null != map && map.size() != 0) {
				for (Map.Entry<String, String> entry : map.entrySet()) { // 得到某省下县
					String tempXian = entry.getKey();
					if (doString.contains(tempXian)) {
						resultMap.put(3, entry.getValue());
						tempXian = null;
						break;
					}
				}
			}
		} else {
			// 遍历县级市、县、旗，找 【县级市、县、旗】
			for (int i = 0; i < xianList.size(); i++) {
				String tempXian = xianList.get(i);
				if (doString.contains(tempXian)) {
					// 需要往前推一个字，避免出现处理：北京市密云县人民法院 结果：省：null 市：北京市 县：云县
					int index = doString.indexOf(tempXian);
					String temp = doString.substring(0, index);
					if (temp.length() > 0) // 避免 云县人民法院
					{
						String sub = doString.substring(index - 1, index);// 云县，再往前推一个字
						if (null != DataUtils.xianJiShiXianMap.get(sub + tempXian)) {
							resultMap.put(3, DataUtils.xianJiShiXianMap.get(sub + tempXian)); // 县、县级市、旗
							break;
						}
					}
					resultMap.put(3, DataUtils.xianJiShiXianMap.get(tempXian)); // 县、县级市、旗
					break;
				}
			}
		}
		return doProvinceCityCountry(resultMap);
	}

	/**
	 * 初始化省、市、县数据 从数据库查询行政区数据
	 * 
	 * @author xiongchangyi
	 * @since 2018-6-23
	 */
	public void initData() {
		// 查询行政区数据
		DataUtils utils = new DataUtils();
		// 查询【省】，shengMap
		utils.listProvince();
		// 查询【地级市】, shiMap
		utils.listCity();
		// 查询【县级市】, xianJiShiMap
		utils.listAllShortCityName();
		// 查询【县】 , xianMap
		utils.listAllCountry();
		// 查询【区】 , quMap
		// utils.listAllCountry();
		utils.listAllCountryQu();
		// 查询【县级市、县、旗】, xianJiShiXianMap
		utils.listXianJiShiXian();
		// 查询【县级市、县、旗】,xianJiShiXianQuMap
		utils.listXianJiShiXianQu();
		// 查询【市名称】和【省的ID】,province_city_map
		utils.listProvinceCity();
		// 查询所有区，不包括 县、旗、县级市
		utils.listAllArea();
		// 简称县级市、简称县、全称旗、区
		utils.JianChenShiJianChenXianQuanQuQi();

		shengList.addAll(DataUtils.shengMap.keySet());
		shiList.addAll(DataUtils.shiMap.keySet());
		xianList.addAll(DataUtils.xianJiShiXianMap.keySet());
	}

	private static List<String> shengList = new ArrayList<String>();
	private static List<String> shiList = new ArrayList<String>();
	private static List<String> xianList = new ArrayList<String>();

	/**
	 * 处理前半截(length/2)企业名称或者法院名称 不考虑企业名称的后半截，分公司等
	 */
	public String[] court_tem(String doString) {
		int length = doString.length();
		doString = doString.substring(0, length / 2) + 2;
		// result[0]是省，result[1]是市， result[3]是县
		String result[] = new String[3];
		Map<Integer, String> resultMap = new HashMap<Integer, String>();

		List<String> shengList = new ArrayList<String>();
		shengList.addAll(DataUtils.shengMap.keySet());

		List<String> shiList = new ArrayList<String>();
		shiList.addAll(DataUtils.shiMap.keySet());

		List<String> xianList = new ArrayList<String>();
		xianList.addAll(DataUtils.xianJiShiXianMap.keySet());

		List<String> quList = new ArrayList<String>();
		xianList.addAll(DataUtils.quMap.keySet());

		// 遍历省，在第2截里面找【省】
		for (int i = 0; i < shengList.size(); i++) {
			String tempSheng = shengList.get(i);
			if (doString.contains(tempSheng)) {
				resultMap.put(1, DataUtils.shengMap.get(tempSheng));// 省
				break;
			}
		}

		// 遍历地级市 ，在第2截里面找【地级市】
		for (int i = 0; i < shiList.size(); i++) {
			String tempShi = shiList.get(i);
			if (doString.contains(tempShi)) {
				resultMap.put(2, DataUtils.shiMap.get(tempShi)); // 地级市
				break;
			}
		}

		// 遍历县级市、县、旗，找 【县级市、县、旗】
		for (int i = 0; i < xianList.size(); i++) {
			String tempXian = xianList.get(i);
			if (doString.contains(tempXian)) {
				// 需要往前推一个字，避免出现处理：北京市密云县人民法院 结果：省：null 市：北京市 县：云县
				int index = doString.indexOf(tempXian);
				String temp = doString.substring(0, index);
				if (temp.length() > 0) // 避免 云县人民法院
				{
					String sub = doString.substring(index - 1, index);// 云县，再往前推一个字
					if (null != DataUtils.xianJiShiXianMap.get(sub + tempXian)) {
						resultMap.put(3, DataUtils.xianJiShiXianMap.get(sub + tempXian)); // 县、县级市、旗
						break;
					}
				}
				resultMap.put(3, DataUtils.xianJiShiXianMap.get(tempXian)); // 县、县级市、旗
				break;
			}
		}

		// 无县级市、县、旗，则找区
		if ("".equals(result[2]) || null == result[2]) {
			for (int i = 0; i < quList.size(); i++) {
				String tempArea = quList.get(i);
				if (doString.contains(tempArea)) {
					// 需要往前推一个字，避免出现处理：北京市密云县人民法院 结果：省：null 市：北京市 县：云县
					int index = doString.indexOf(tempArea);
					String temp = doString.substring(0, index);
					if (temp.length() > 0) // 避免 云县人民法院
					{
						String sub = doString.substring(index - 1, index);// 云县，再往前推一个字
						if (quList.contains(sub + tempArea)) {
							resultMap.put(3, sub + tempArea); // 县、县级市、旗
							break;
						}
					}
					resultMap.put(3, tempArea);
					break;
				}
			}
		}
		return doProvinceCityCountry(resultMap);
	}

	/**
	 * 处理前半截(length/2)企业名称 去掉前面的行政区:只去掉省、地级市
	 * 
	 * @author xiongchangyi 2015-6-18
	 */
	public String substringAdminstration(String doString) {
		String result = doString;
		String endString = null;
		int length = result.length();
		// 企业名称长度判断
		if (length <= 6) {
			result = result.substring(0, length / 2);
			endString = doString.substring(length / 2);
		} else {
			result = result.substring(0, length / 2 + 2);
			endString = doString.substring(length / 2 + 2);
		}

		List<String> shengList = new ArrayList<String>();
		shengList.addAll(DataUtils.shengMap.keySet());

		List<String> shiList = new ArrayList<String>();
		shiList.addAll(DataUtils.shiMap.keySet());

		List<String> xianList = new ArrayList<String>();
		xianList.addAll(DataUtils.xianJiShiXianMap.keySet());
		// 简称县、简称市、全旗、全区
		List<String> xianJiShiXianQu = new ArrayList<String>();
		xianJiShiXianQu.addAll(DataUtils.jianChenXianQuanChenXianMap.keySet());

		// 遍历省，在第2截里面找【省】
		for (int i = 0; i < shengList.size(); i++) {
			String tempSheng = shengList.get(i);
			if (result.contains(tempSheng)) {
				// 截取找到的省
				result = result.replace(tempSheng, "");
				int sheng_index = result.indexOf("省");
				int zi_index = result.indexOf("自治区");
				int zhuang_index = result.indexOf("壮族自治区");
				int wei_index = result.indexOf("维吾尔自治区");
				int hui_index = result.indexOf("回族自治区");
				int ao_index = result.indexOf("特别行政区");
				int nei_index = result.indexOf("古自治区");
				if (sheng_index != -1) {
					result = result.replace("省", "");
				} else if (zhuang_index != -1) {
					result = result.replace("壮族自治区", "");
				} else if (wei_index != -1) {
					result = result.replace("维吾尔自治区", "");
				} else if (hui_index != -1) {
					result = result.replace("回族自治区", "");
				} else if (ao_index != -1) {
					result = result.replace("特别行政区", "");
				} else if (nei_index != -1) {
					result = result.replace("古自治区", "");
				} else if (zi_index != -1) {
					result = result.replace("自治区", "");
				}
				break;
			}
		}
		// 遍历地级市 ，在第2截里面找【地级市】
		for (int i = 0; i < shiList.size(); i++) {
			String tempShi = shiList.get(i);
			if (result.contains(tempShi)) {
				// 首先去掉匹配的2个字，然后后面的尾部：海西蒙古族藏族自治州，匹配“蒙古族藏族自治州”
				result = result.replace(tempShi, "");
				int city_index = result.indexOf("市");
				if (city_index != -1) {
					result = result.replace("市", "");
				}
				result = result.replace("(", "");
				result = result.replace(")", "");
				result = result.replace("（", "");
				result = result.replace("）", "");
				break;
			}
		}
		/*
		 * //遍历县级市、县、旗，找 【县级市、县、旗】 for(int i = 0; i < xianList.size(); i++) {
		 * String tempXian = xianList.get(i); if(result.contains(tempXian)) {
		 * //把匹配到的替换掉 result = result.replace(tempXian, ""); int xian_index =
		 * result.indexOf("县"); int shi_index = result.indexOf("市");
		 * if(xian_index !=-1) { result = result.replace("县", ""); } else
		 * if(shi_index !=-1) { result = result.replace("市", ""); } break; } }
		 */
		result = result + endString;
		result = result.replace("(", "");
		result = result.replace(")", "");
		result = result.replace("（", "");
		result = result.replace("）", "");
		result = result.replace("中国", "");
		return result;
	}

	/**
	 * 处理前半截(length/2)企业名称 在substringAdminstration()方法的基础上去掉县、区、镇、乡
	 * 
	 * @author xiongchangyi 2015-6-18
	 */
	public String substringAdminstrationXianQu(String doString) {
		String result = null;
		String endString = null;
		int length = doString.length();
		if (length <= 6) {
			result = doString;
		} else {
			result = doString.substring(0, length / 2 + 2);
			endString = doString.substring(length / 2 + 2);
		}
		if (result.contains("镇")) {
			result = result.substring(result.indexOf("镇") + 1, result.length());
		} else if (result.contains("乡")) {
			result = result.substring(result.indexOf("乡") + 1, result.length());
		}
		// 简称县、简称市、全旗、全区
		List<String> xianJiShiXianQu = new ArrayList<String>();
		xianJiShiXianQu.addAll(DataUtils.jianChenXianQuanChenXianMap.keySet());

		List<String> shiList = new ArrayList<String>();
		shiList.addAll(DataUtils.shiMap.keySet());

		// 遍历地级市 ，在第2截里面找【地级市】
		for (int i = 0; i < shiList.size(); i++) {
			String tempShi = shiList.get(i);
			if (result.contains(tempShi)) {
				// 首先去掉匹配的2个字，然后后面的尾部：海西蒙古族藏族自治州，匹配“蒙古族藏族自治州”
				result = result.replace(tempShi, "");
				int city_index = result.indexOf("市");
				if (city_index != -1) {
					result = result.replace("市", "");
				}
				result = result.replace("(", "");
				result = result.replace(")", "");
				result = result.replace("（", "");
				result = result.replace("）", "");
				break;
			}
		}

		// 遍历县级市、县、旗，找 【县级市、县、旗】
		for (int i = 0; i < xianJiShiXianQu.size(); i++) {
			String tempXian = xianJiShiXianQu.get(i);
			if (result.contains(tempXian)) {
				// 把匹配到的替换掉
				result = result.replace(tempXian, "");
				if (result.contains("市")) {
					result = result.replace("市", "");
				}
				if (result.contains("县")) {
					result = result.substring(result.indexOf("县") + 1);
				}
				break;
			}
		}
		if (null != endString) {
			result = result + endString;
		}
		int area_index = result.indexOf("区");
		if (-1 != area_index) {
			result = result.substring(area_index + 1);
		}
		if (result.length() > 5) {
			result = result.substring(0, 5);
		}
		return result;
	}
	
	/**
	 * 去掉区
	 * @param doString 需要去掉区的字符串
	 * @return 去掉区后的结果
	 */
	public String deleteArea(String doString)
	{
		String result = doString;
		List<String> quList = DataUtils.quList;
		for(int i = 0;i < quList.size(); i++)
		{
			if(doString.contains(quList.get(i)))
			{
				result = result.replace(quList.get(i), "");
			}
		}
		return result;
	}
	/**
	 * 在工商JSON文件数据中使用
	 * 提取省、市、县
	 * @param doString：住所的字符串；utils：工具类对象
	 * @Vesion 1.0
	 * @author xiongchangyi
	 * @since 2015-6-12
	 */
	public  String[] enterp2(String location)
	{
		if(null == location || "".equals(location))
		{
			return null;
		}
		//将名称分成2截	 这里可能的漏洞是分公司、与没有分公司的情况，需要验证是否可行	
		int length = location.length();
		String oneString = location.substring(0, length/2);
		Map<Integer,String> resultMap = subCompanyDo2(oneString,location);//提取省市县区
		//处理结果的数组：result[0]存储省   result[1]存储地级市     result[2]存储县
		return doProvinceCityCountry(resultMap);
	}
	

	/**
	 * 在工商JSON文件数据中使用
	 * 提取省、地级市、县【县级市、县、旗、区】
	 * @param oneString  住所字符串的前半部分
	 * @param twoString  住所字符串的后半部分
	 * @return map:  key=1是省，key=2是地级市，key=3是县
	 * @Vesion 1.0
	 * @author xiongchangyi
	 * @since 2015-6-12
	 */
	public  Map<Integer,String> subCompanyDo2(String oneString,String doString)
	{
		Map<Integer,String> resultMap = new HashMap<Integer,String>();//省的key是1，市的key是2，县的key是3
		List<String> shengList = new ArrayList<String>();
		shengList.addAll(DataUtils.shengMap.keySet());
		
		List<String> shiList = new ArrayList<String>();
		shiList.addAll(DataUtils.shiMap.keySet());
		
		List<String> xianList = new ArrayList<String>();
		xianList.addAll(DataUtils.xianJiShiXianMap.keySet());
		
		//遍历省，在第1截里面找【省】
		for(int i = 0;i<shengList.size();i++)
		{
			String tempSheng = shengList.get(i);
			if(oneString.contains(tempSheng+"路"))//避免  陕西路
			{
				if(oneString.contains(DataUtils.shengMap.get(tempSheng)))
				{
					resultMap.put(1,DataUtils.shengMap.get(tempSheng));//省
					break;
				}
			}
			else if(oneString.contains(tempSheng))
			{
				resultMap.put(1,DataUtils.shengMap.get(tempSheng));//省
				break;
			}			
		}
		
		//遍历地级市 ，在第1截里面找【地级市】
		for(int i = 0; i < shiList.size(); i++)
		{
			String tempShi = shiList.get(i);
			if(oneString.contains(tempShi+"路"))//避免类似  "天津路"  的情况
			{
				//使用类似  "天津市"去判断
				if(oneString.contains(DataUtils.shiMap.get(tempShi)))
				{
					resultMap.put(2,DataUtils.shiMap.get(tempShi));  //地级市
					break;
				}
			}
			else if(oneString.contains(tempShi))
			{
				resultMap.put(2,DataUtils.shiMap.get(tempShi));  //地级市
				break;
			}	
		}		
		if(null == resultMap.get(2))
		{
			//遍历县级市、县、旗，在第2截里面找 【县级市、县、旗】
			for(int i = 0; i < xianList.size(); i++)
			{
				String tempXian = xianList.get(i);			
				if(oneString.contains(tempXian))
				{
					//需要往前推一个字，避免出现处理：北京市密云县人民法院    结果：省：null 市：北京市	县：云县
					int index = oneString.indexOf(tempXian);
					String temp = oneString.substring(0, index);
					if(temp.length() > 0)//避免 云县人民法院
					{
						String sub = oneString.substring(index-1, index);//云县，再往前推一个字
						if(null != DataUtils.xianJiShiXianMap.get(sub+tempXian))
						{
							resultMap.put(3,DataUtils.xianJiShiXianMap.get(sub+tempXian)); //县、县级市、旗
							break;
						}
					}
					resultMap.put(3,DataUtils.xianJiShiXianMap.get(tempXian)); //县、县级市、旗
					break;
				}	
			}	
			
			//无县级市、县、旗，则找区
			if(null == resultMap.get(3))
			{
				//遍历区			
				if(null == resultMap.get(3))
				{
					for(int i = 0; i < DataUtils.quList.size(); i++)
					{
						String tempArea = DataUtils.quList.get(i);
						if(oneString.contains(tempArea))
						{
							//需要往前推一个字，避免出现处理：北京市密云县人民法院    结果：省：null 市：北京市	县：云县
							int index = oneString.indexOf(tempArea);
							String temp = oneString.substring(0, index);
							if(temp.length() > 0)//避免 云县人民法院
							{
								String sub = oneString.substring(index-1, index);//云县，再往前推一个字
								if(null != DataUtils.xianJiShiXianMap.get(sub+tempArea))
								{
									resultMap.put(3,DataUtils.xianJiShiXianMap.get(sub+tempArea)); //县、县级市、旗
									break;
								}
							}
							resultMap.put(3,tempArea);
							break;
						}
					}
				}
			}
		}		
		//返回的 resultMap有可能为空
		return resultMap;
	}
	
	

}
