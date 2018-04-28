package com.dl.member.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.dl.base.util.DateUtil;
import com.dl.member.core.ProjectConstant;



@Service
public class UserBonusShowDescService {
	
	/**
	 * 获取有效期限制
	 * @param startTimeInteger
	 * @param endTimeInteger
	 * @return
	 */
	public String getLimitTimeDesc(Integer startTimeInteger,Integer endTimeInteger) {
		String startTime = DateUtil.getCurrentTimeString(Long.valueOf(startTimeInteger), DateUtil.date_sdf);
		String endTime = DateUtil.getCurrentTimeString(Long.valueOf(endTimeInteger), DateUtil.date_sdf);
		Date endDate = new Date(endTimeInteger);
		if(DateUtil.isLatestWeek(endDate, new Date())) {
			return "剩余时间少于等于7天";
		}else {
			return startTime+"~"+endTime;
		}
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
			return "购彩满"+minGoodsAmountParam+"元减"+bonusPrice;
		}
	}
	
	/**
	 * 获取使用范围
	 * @param useRange
	 * @return
	 */
	public String getUseRange(Integer useRange) {
		if(ProjectConstant.ALL_LOTERRY_TYPE.equals(useRange)) {
			return "不限彩种";
		}else {
			return "指定彩种";
		}
	}

}
