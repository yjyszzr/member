package com.dl.member.service;

import org.springframework.stereotype.Service;



@Service
public class UserBonusShowDescService {
	
	/**
	 * 获取有效期限制
	 * @param startTimeInteger
	 * @param endTimeInteger
	 * @return
	 */
//	public String getLimitTimeDesc(Integer startTimeInteger,Integer endTimeInteger) {
//		String startTime = DateUtil.getCurrentTimeString(Long.valueOf(startTimeInteger), DateUtil.date_sdf);
//		String endTime = DateUtil.getCurrentTimeString(Long.valueOf(endTimeInteger), DateUtil.date_sdf);
//		return startTime+"~"+endTime;
//	}
//	
//	/**
//	 *  获取限制的最小订单金额描述
//	 * @param minGoodsAmountParam
//	 * @param bonusDataDTO
//	 * @return
//	 */
//	public String getLimitOrderAmountDesc(BigDecimal minGoodsAmountParam) {
//		if(minGoodsAmountParam.compareTo(BigDecimal.ZERO) == 0) {
//			return "无使用门槛";
//		}else {
//			return "订单满"+minGoodsAmountParam+"元可用";
//		}
//	}
//	
//	public String getUseRange(Integer useRange) {
//		if(ProjectConstant.BONUS_USE_RANGE_ALLGOODS.equals(useRange)) {
//			return "全部商品";
//		}else if(ProjectConstant.BONUS_USE_RANGE_SOMEGOODS.equals(useRange)) {
//			return "指定商品";
//		}
//		return "";
//	}
//
//	public String getUseRange(Integer useRange,BonusDataDTO bonusDataDTO) {
//		StringBuilder bonusRangeDesc = new StringBuilder();
//		if(ProjectConstant.BONUS_USE_RANGE_ALLGOODS.equals(useRange)) {
//			bonusRangeDesc.append("全部商品");
//		}else if(ProjectConstant.BONUS_USE_RANGE_SOMEGOODS.equals(useRange)) {
//			bonusRangeDesc.append("指定商品");
//		}
//		return bonusRangeDesc.toString();
//	}

}
