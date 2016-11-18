package cn.com.szgao.enterprise;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.com.szgao.dto.ChangeVO;
import cn.com.szgao.dto.EnterpriseVO;
import cn.com.szgao.dto.HolderDetailDtlVO;
import cn.com.szgao.dto.HolderDetailVO;
import cn.com.szgao.dto.HolderVO;
import cn.com.szgao.dto.PunishmentVO;
import cn.com.szgao.dto.ReportAssetVO;
import cn.com.szgao.dto.ReportVO;
import cn.com.szgao.util.DateUtils;
import cn.com.szgao.util.StringUtils;

public class WashEtp {
	/**
	 * clearPunishment清洗行政处罚信息
	 * 
	 * @param enterVO
	 */
	public static List<PunishmentVO>   clearPunishment(EnterpriseVO enterVO) {
		List<PunishmentVO> abList = enterVO.getPunishment();
		List<PunishmentVO> abList2 = new ArrayList<PunishmentVO>();
		PunishmentVO vo = null;
		if (null != abList && abList.size() > 0) {
			for (int i = 0; i < abList.size(); i++) {
				vo = abList.get(i);
				if (null != vo) {
					if (null != vo.getIllegalType()) {
						vo.setIllegalType(deleteMoreFuhao(vo.getIllegalType()));
					}
					if (null != vo.getPunishNum()) {
						vo.setPunishNum(deleteMoreFuhao(vo.getPunishNum()));
					}
					if (null != vo.getPunishOffice()) {
						vo.setPunishOffice(deleteMoreFuhao(vo.getPunishOffice()));
					}
					if (!StringUtils.isNull(vo.getPunishDate())) {
						vo.setPunishDate(DateUtils.toYMDOfChaStr_ESZZ2(vo.getPunishDate()));
					}
					if (!StringUtils.isNull(vo.getPublicationDate())) {
						vo.setPublicationDate(DateUtils.toYMDOfChaStr_ESZZ2(vo.getPublicationDate()));
					}

					abList2.add(vo);
					vo = null;
				}
			}
//			enterVO.setPunishment(abList2);
		}
//		abList2 = null;
		return abList2;
	}

	/**
	 * 清洗股东信息
	 * 
	 * @param enterVO
	 * @throws ParseException
	 */
	public static List<HolderVO> clearHolder(EnterpriseVO enterVO)  {
		List<HolderVO> holderList = null;
		List<HolderVO> holderList2 = new ArrayList<HolderVO>();
		HolderVO vo = null;
		if (null != enterVO) {
			holderList = enterVO.getHolder();
			if (null != holderList && holderList.size() > 0) {
				for (int i = 0; i < holderList.size(); i++) {
					vo = holderList.get(i);
					if (null != vo) {
						if (null != vo.getLicenseNum()) {
							vo.setLicenseNum(deleteMoreFuhao(vo.getLicenseNum()));
						}
						if (null != vo.getLicenseType()) {
							vo.setLicenseType(deleteMoreFuhao(vo.getLicenseType()));
						}

						// 股东及类型互换
						if (null != vo.getHolder() && null != vo.getType()) {
							if (vo.getHolder().contains("自然人")) {
								vo.setType(vo.getHolder());
								vo.setHolder(vo.getType());
							}
						}
						//处理股东
						if(!StringUtils.isNull(vo.getHolder())){
							if(vo.getHolder().indexOf("\u0000")!=-1){
								vo.setHolder( vo.getHolder().substring(0,vo.getHolder().indexOf("\u0000")) );
							}
							if(vo.getHolder().indexOf("Struts")!=-1){
								vo.setHolder( vo.getHolder().substring(0,vo.getHolder().indexOf("Struts")) );
							}
						}
						//股东类型
						if(!StringUtils.isNull(vo.getType() )){
							if(vo.getType().indexOf("\u003c")!=-1){
								vo.setType( vo.getType().substring(0,vo.getType().indexOf("\u003c")) );
							}
						}
						
//						vo.setHolder("111111111111111111111111111");
						
						// 处理股东详情
						vo.setHolderDetail(clearDetail(vo.getHolderDetail()));
						holderList2.add(vo);
						vo = null;
					}
				}
				
//				enterVO.setHolder(holderList2);
			}
			holderList = null;
		}
//		holderList2 = null;
		return holderList2;
	}

	/**
	 * clearDetail清洗股东详情
	 * 
	 * @param enterVO
	 * @throws ParseException
	 */
	public static List<HolderDetailVO> clearDetail(List<HolderDetailVO> detList)  {
		// List<HolderDetailVO> detList = enterVO.getHolderDetail();
		List<HolderDetailVO> detList2 = new ArrayList<HolderDetailVO>();
		HolderDetailVO vo = null;
		if (null != detList && detList.size() > 0) {
			for (int i = 0; i < detList.size(); i++) {
				vo = detList.get(i);
				if (null != vo) {
					if (null != vo.getActualCapital()) {
						vo.setActualCapital(deleteMoreFuhao(vo.getActualCapital()));
					}
					if (null != vo.getConCapital()) {
						vo.setConCapital(deleteMoreFuhao(vo.getConCapital()));
					}
					if (null != vo.getConMethod()) {
						vo.setConMethod(deleteMoreFuhao(vo.getConMethod()));
					}
					if (null != vo.getFactCapital()) {
						vo.setFactCapital(deleteMoreFuhao(vo.getFactCapital()));
					}
					if (null != vo.getFactMethod()) {
						vo.setFactMethod(deleteMoreFuhao(vo.getFactMethod()));
					}
					List<HolderDetailDtlVO> detlList = vo.getHolderDetailDtl();
					HolderDetailDtlVO dvo = null;
					List<HolderDetailDtlVO> detlList2 = new ArrayList<HolderDetailDtlVO>();
					if (null != detlList && detlList.size() > 0) {
						for (int j = 0; j < detlList.size(); j++) {
							dvo = detlList.get(i);
							if (null != dvo) {
								if (null != dvo.getActualCapital()) {
									dvo.setActualCapital(deleteMoreFuhao(dvo.getActualCapital()));
								}
								if (null != dvo.getConMethod()) {
									dvo.setConMethod(deleteMoreFuhao(dvo.getConMethod()));
								}
								if (null != dvo.getFactMethod()) {
									dvo.setFactMethod(deleteMoreFuhao(dvo.getFactMethod()));
								}
								if (null != dvo.getConsidDate()) {
									// yyyy年MM月dd日
									dvo.setConsidDate(DateUtils.toYMDOfChaStr_ESZZ2(dvo.getConsidDate()));
								}
								if (null != dvo.getActualCapital()) {
									// yyyy年MM月dd日
									dvo.setActualCapital(deleteMoreFuhao(dvo.getActualCapital()));
								}
							}
							detlList2.add(dvo);
							dvo = null;
						}
						vo.setHolderDetailDtl(detlList2);
						detlList2 = null;
					}
					detList2.add(vo);
					vo = null;
				}
			}
		}
		return detList2;
	}

	/**
	 * 清洗变更信息
	 * 
	 * @param enterVO
	 * @throws ParseException
	 */
	public static List<ChangeVO> clearChangeItem(EnterpriseVO enterVO) {
		List<ChangeVO> changeList = enterVO.getChange();
		List<ChangeVO> changeList2 = new ArrayList<ChangeVO>();
		ChangeVO vo = null;
		if (null != changeList && changeList.size() > 0) {
			for (int i = 0; i < changeList.size(); i++) {
				vo = changeList.get(i);
				if (null != vo) {
					if(StringUtils.isNull(vo.getChangeEvent())){
						continue;
					}
					if (null != vo.getChangeAfter()) {
						String after = vo.getChangeAfter().replaceAll("[★■★^〓]", "").replaceAll("[\\n,\\t,\\r,\\s,&nbsp;]", "");
						if (after.contains("更多") && after.contains("收起更多")) {
							after = after.substring(after.indexOf("更多") + 2, after.indexOf("收起更多")) ;
							
							if (after.length() > 5) {
								String temp = "";
								temp = after.substring(after.length() - 5).replaceAll("[★#※*＊■★]", "");
								after = after.substring(0, after.length() - 5) + temp;
							}
						}after=after.replace("\r", "").replace("\t", "").replace("\n", "").replace("&nbsp", "").replace("&nbsp;", "");
						vo.setChangeAfter(after);
					}
					if (null != vo.getChangeBefore()) {
						String before = vo.getChangeBefore().replaceAll("[★■★^〓]", "").replaceAll("[\\n,\\t,\\r,\\s,&nbsp;]", "");
						if (before.contains("更多") && before.contains("收起更多")) {
							before = before.substring(before.indexOf("更多") + 2, before.indexOf("收起更多")) ;
							
							if (before.length() > 5) {
								String temp = "";
								temp = before.substring(before.length() - 5).replaceAll("[★#※*＊■★]", "");
								before = before.substring(0, before.length() - 5) + temp;
							}
							
						}
						before=before.replace("\r", "").replace("\t", "").replace("\n", "").replace("&nbsp", "").replace("&nbsp;", "");
						vo.setChangeBefore(before) ;
					}
					if (null != vo.getChangeDate()) {
						// if(!vo.getChangeDate().contains("NaN"))
						// {
						vo.setChangeDate(DateUtils.toYMDOfChaStr_ESZZ2(vo.getChangeDate()));// 处理日期格式，显示成带zz
						// }
						// else
						// {
//						vo.setChangeDate(null);
						// }
					}
					changeList2.add(vo);
					vo = null;
				}
			}
//			enterVO.setChange(changeList2);
		}
//		changeList2 = null;
		return changeList2;
	}
	/**
	 * 清洗年报
	 * 
	 * @param enterVO
	 */
	public static List<ReportVO>   clearReport(EnterpriseVO enterVO) {
		List<ReportVO> list = enterVO.getReport();
		List<ReportVO> list2 = new ArrayList<ReportVO>();
		ReportVO vo = null;
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				vo = list.get(i);
				if (null != vo) {
					// 年度报告-企业资产状况信息
					ReportAssetVO asset = vo.getAsset();
					if (null != asset) {
						if (null != asset.getMainTotalSale()) {
							asset.setMainTotalSale(deleteMoreFuhao(asset.getMainTotalSale()));
						}
						if (null != asset.getNetProfit()) {
							asset.setNetProfit(deleteMoreFuhao(asset.getNetProfit()));
						}
						if (null != asset.getOwnerInterest()) {
							asset.setOwnerInterest(deleteMoreFuhao(asset.getOwnerInterest()));
						}
						if (null != asset.getProfit()) {
							asset.setProfit(deleteMoreFuhao(asset.getProfit()));
						}
						if (null != asset.getTotalAsset()) {
							asset.setTotalAsset(deleteMoreFuhao(asset.getTotalAsset()));
						}
						if (null != asset.getTotalDebt()) {
							asset.setTotalDebt(deleteMoreFuhao(asset.getTotalDebt()));
						}
						if (null != asset.getTotalSale()) {
							asset.setTotalSale(deleteMoreFuhao(asset.getTotalSale()));
						}
						if (null != asset.getTotalTax()) {
							asset.setTotalTax(deleteMoreFuhao(asset.getTotalTax()));
						}
						vo.setAsset(asset);
					}

					list2.add(vo);
					vo = null;
				}
			}
//			enterVO.setReport(list2);
		}
//		list2 = null;
		return list2;
	}

	/**
	 * 去掉特殊符号
	 * 
	 * @param doTemp
	 * @return
	 */
	public static String deleteMoreFuhao(String doTemp) {
		return doTemp.replace("&nbsp", "").replace(";", "").replace("\r", "").replace("\t", "").replace("\n", "")
				.replace(" ", "").replace("\"", "");
	}
}
