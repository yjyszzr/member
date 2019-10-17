package com.dl.member.service;

import com.dl.base.util.DateUtil;
import com.dl.member.core.ProjectConstant;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



@Service
public class UserBonusShowDescService {
	
	/**
	 * 获取有效期限制
	 * @param startTimeInteger
	 * @param endTimeInteger
	 * @return
	 */
	public String getLimitTimeDesc(Integer startTimeInteger,Integer endTimeInteger) {
		//String startTime = DateUtil.getCurrentTimeString(Long.valueOf(startTimeInteger), DateUtil.date_sdf);
		String endTime = DateUtil.getCurrentTimeString(Long.valueOf(endTimeInteger), DateUtil.date_sdf);
		return "有效期至:"+endTime;
	}
	
	/**
	 * 获取限制的最小订单金额描述
	 * @param minGoodsAmountParam
	 * @param bonusDataDTO
	 * @return
	 */
	public String getLimitOrderAmountDesc(BigDecimal minGoodsAmountParam,BigDecimal bonusPrice) {
		if(minGoodsAmountParam.compareTo(BigDecimal.ZERO) == 0) {
			return "无使用门槛";
		}else {
			return "单笔订单满"+minGoodsAmountParam+"元可用";
		}
	}
	
	/**
	 * 获取使用范围
	 * @param useRange
	 * @return
	 */
	public String getUseRange(Integer useRange) {
		if(ProjectConstant.ALL_LOTERRY_TYPE.equals(useRange)) {
			return "";
//			return "不限彩种";
		}else {
			return "";
//			return "指定彩种";
		}
	}

}
