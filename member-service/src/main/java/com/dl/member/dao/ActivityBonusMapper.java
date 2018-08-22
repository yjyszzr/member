package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.ActivityBonus;

public interface ActivityBonusMapper extends Mapper<ActivityBonus> {
	
	List<ActivityBonus> queryActivityBonusListBySelective(ActivityBonus activityBonus);
	
	List<ActivityBonus> queryActivityBonusListByRechargeCardId(@Param("rechargeCardId") Integer rechargeCardId);
}