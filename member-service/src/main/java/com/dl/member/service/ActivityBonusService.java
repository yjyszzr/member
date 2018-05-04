package com.dl.member.service;
import com.dl.member.model.ActivityBonus;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;

import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.ActivityBonusMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class ActivityBonusService extends AbstractService<ActivityBonus> {
    @Resource
    private ActivityBonusMapper activityBonusMapper;
    
    public List<ActivityBonus> queryActivityBonusList(Integer type) {
    	ActivityBonus activityBonus = new ActivityBonus();
    	activityBonus.setBonusType(type);
    	List<ActivityBonus> activityBonusList = activityBonusMapper.queryActivityBonusListBySelective(activityBonus);
    	return activityBonusList;
    }

}
