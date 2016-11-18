package cn.com.szgao.enterprise;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.com.szgao.dto.CodeVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.IndustryVO;
import cn.com.szgao.util.PrCiCouText;
import cn.com.szgao.util.StringUtils;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// List<CodeVO> code = PrCiCouText.getregNumList("120116000219635
		// 911201160931130117");
		//
		// for (CodeVO codeVO : code) {
		// System.out.println(codeVO.getCode() +" "+codeVO.getStatus());
		// }

		// double f = 111231.5585;
		// BigDecimal b = new BigDecimal(f);
		// double f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		// System.out.println(f1);

		// Analyzer anal = new IKAnalyzer();
		// StringReader reader = new StringReader("生产建筑工程机械");
		// // 分词
		// TokenStream ts = null;
		// ts = anal.tokenStream("", reader);
		// CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		// try {
		// ts.reset();
		//
		// String strs = "";
		// // 遍历分词数据
		// while (ts.incrementToken()) {
		// String temp = term.toString();
		// // System.out.println(temp);
		// // // getIndustry(temp);
		// strs += term.toString() + "|";
		// // System.out.print(term.toString() + "|" );
		// }
		// reader.close();
		//
		//// String[] arrStr1 = strs.split("\\|");
		//
		// String[] arrStr1 = new String[]{"生产","建筑工程","建筑","工程机械","工程学",
		// "建筑工程学"};
		// for (int i = 0; i < arrStr1.length; i++) {
		// System.out.print (arrStr1[i]+",");
		// }
		// StringUtils.sortStringArray(arrStr1, 1);// 排序
		// for (int i = 0; i < arrStr1.length; i++) {
		// System.out.print (arrStr1[i]+",");
		// }
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// 建筑工程学,建筑工程,工程机械,工程学,建筑,生产,
		// 建筑工程学,工程机械,建筑工程,工程学,建筑,生产,

		// String[] s0 = { "abort", "books", "being", "are", "very", " much" };
		// String[] s1 = { "中国", "山东", "泰山", "人民", "zero" };
		// Arrays.sort(s0);
		// System.out.println("Before sorting:");
		// System.out.print(Arrays.asList(s0));
		// Arrays.sort(s1);
		// System.out.println("Before sorting:");
		// System.out.print(Arrays.asList(s1));

		// 处理行业
		// IndustryVO ivo = FileIntoDataBase2p5
		// //.washIndustry("标识标牌设计、生产、销售，企业营销策划，设计、制作、代理发布国内广告业务，室内外装饰装修服务及LED照明工程设计及施工");
		//// .washIndustry("百货.副食品.五金交电.化工产品.日用杂货.针纺织品.其它食品(卷烟零售).农机配件.建筑材料.书刊销售，音像制品零售出租，互联网上网服务兼营范围：农副产品(不含烟叶)运销.铁矿.锰矿.锌矿");
		// .washIndustry("液化气钢瓶检测服务");
		// if (null != ivo) {
		// System.out.println(ivo.getIndustry_name());
		// System.out.println(ivo.getIndustry_id());
		// }

//		Set<String> nameSet = new HashSet<String>();
//		nameSet.add("张三");
//		nameSet.add("李四");
//		nameSet.add("王五");
//		nameSet.add("张三");
//
//		// 输出结果 张三 李四 王五
//		for (String name : nameSet) {
//			System.out.print(name + "\t");
//		}
		
		
		FileIntoDataBase2p5 ff=new FileIntoDataBase2p5();
//		IndustryVO vo=ff.washIndustry("数据技术、计算机软硬件、网络技术领域内从事技术开发、技术咨询、技术转让、技术服务，销售计算机软硬件，企业管理咨询，房地产经纪，商务信息咨询，电子商务");
//		
//		IndustryVO vo=ff.washIndustry("领域内的技术开发、技术转让、技术咨询和技术服务，通讯设备及相关产品、机械设备、电子产品、金属材料、电线电缆、日用百货的销售，建筑工程；上海市范围内第二类增值电信业务中的因特网接入服务业务。");
//		IndustryVO vo=ff.getIndustryVOFromIk("领域内的技术开发、技术转让、技术咨询和技术服务，通讯设备及相关产品、机械设备、电子产品、金属材料、电线电缆、日用百货的销售，建筑工程；上海市范围内第二类增值电信业务中的因特网接入服务业务。");
//		IndustryVO vo=ff.getIndustryVOFromIk("房地产开发（凭资质证经营）；（以下项目仅限分公司经营）饮食、住宿、会议接待、美容、美发、停车场服务，游泳、健身、酒吧、娱乐服务，日用百货零售***");
//		System.out.println(vo.getIn_name()  +"  "+vo.getIndustry_name());
		
		
		EnterpriseVO enterVO = new EnterpriseVO();
//		String scopde="房地产开发（凭资质证经营）；（以下项目仅限分公司经营）饮食、住宿、会议接待、美容、美发、停车场服务，游泳、健身、酒吧、娱乐服务，日用百货零售***";
		String scopde=" 电子产品及计算机软硬件的设计、研发、上门安装与维护";
		
		
		
		// 处理行业
					String industry = null;
					String industryId = null;

					// 处理行业
					Analyzer anal = new IKAnalyzer();
					String indu = StringUtils.removeBlank_GuoHao(scopde);

					System.out.println("------ 经营范围>>>>>  " + indu);
					IndustryVO ivo = new IndustryVO();
					// IndustryVO ivo1 = null;
					if (StringUtils.isNull(industry)) {
						if (!StringUtils.isNull(indu)) {
							String[] sourceStrArray = indu.split("[;；。.：:，,]");// 分割出来的字符数组
							for (String str : sourceStrArray) {
								System.out.println("------ 词组：  " + str);
								if (StringUtils.isNull(str)) {
									continue;
								}
								if ("许可经营项目".equals(str) || "经营项目".equals(str)||"一般经营项目".equals(str) ) {
									continue;
								}

								
								// 先词组
								ivo = ff.getIndustry(str);
								if (!StringUtils.isNull(ivo.getIndustry_id())) {
									break;
								}
								
								
//								// 再拆"、"号
								if (str.indexOf("、") != -1) {
									String[] arrStrD = str.split("、");
									for (int i = 0; i < arrStrD.length; i++) {
										if (!StringUtils.isNull(arrStrD[i])) {
											ivo = ff.getIndustry(arrStrD[i]);
											if (!StringUtils.isNull(ivo.getIndustry_id())) {
												break;
											}
										}
										if (null == ivo.getIndustry_id()) {
											System.out.println("-----------进入IK 分词1");
											// ik分词
											ivo = ff.getIndustryVOFromIk(arrStrD[i]);
											break;
										}
									}
								}
								else{
									System.out.println("-----------进入IK 分词2");
									// ik分词
									ivo = ff.getIndustryVOFromIk(str);
								}
								
//								if (!StringUtils.isNull(ivo.getIndustry_id())) {
//									break;
//								}
		//
//								// 再利用 ik分词
//								if (null == ivo.getIndustry_id()) {
//									ivo = getIndustryVOFromIk(str);
//								}

								if (!StringUtils.isNull(ivo.getIndustry_id())) {
									break;
								}
							}
						}
					}
					
					if (null != ivo) {
						System.out.println(ivo.getIn_name() + "  --   " + ivo.getIndustry_name());
						enterVO.setIndustry(ivo.getIndustry_name());
						enterVO.setIndustryId(ivo.getIndustry_id());

						if (StringUtils.isNull(enterVO.getIndustryId())) {
							System.out.println(  "无行业: " + enterVO.getScope() +   " | "
									+ enterVO.getCompany() + " | " + enterVO.getBatchNum());
						}
					}
					
					
					
	}

}
