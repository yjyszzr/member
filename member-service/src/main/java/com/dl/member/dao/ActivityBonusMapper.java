package com.dl.member.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.ActivityBonus;

public interface ActivityBonusMapper extends Mapper<ActivityBonus> {
	
	List<ActivityBonus> queryActivityBonusListBySelective(ActivityBonus activityBonus);
}