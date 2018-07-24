package com.dl.member.service;
import com.dl.member.model.DLActivity;

import tk.mybatis.mapper.entity.Condition;

import com.dl.member.dao.DLActivityMapper;
import com.dl.member.dto.ActivityDTO;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
public class DLActivityService extends AbstractService<DLActivity> {
    @Resource
    private DLActivityMapper dLActivityMapper;
    
    public DLActivity queryActivityByType(Integer actType) {
    	DLActivity dLActivity = dLActivityMapper.queryActivityByType(actType);
    	return dLActivity;
    }
    
    /**
     * 插叙是否有有效的充值活动
     * @return
     */
    public Integer queryValidActivity() {
    	Integer now = DateUtil.getCurrentTimeLong();
    	Integer count = dLActivityMapper.countRechargeActivity(now);
    	return count;
    }
    
    /**
     * 根据活动类型来查询活动集合
     * @param actTypeParam
     * @return
     */
    public List<ActivityDTO> queryActivityByActType(Integer actType){
		ActivityDTO dlActivityDTO = new ActivityDTO();
		Condition condition = new Condition(DLActivity.class);
		condition.createCriteria().andCondition("act_type =",actType).andCondition("is_finish=", 0).andCondition("status=", 1).andGreaterThan("endTime", DateUtil.getCurrentTimeLong()).andLessThanOrEqualTo("startTime", DateUtil.getCurrentTimeLong());
		List<DLActivity> lotteryActivitys = dLActivityMapper.selectByCondition(condition);
		
		List<ActivityDTO> lotteryActivityDTOs = new ArrayList<>();
		if(lotteryActivitys.size() >= 0) {
			lotteryActivitys.forEach(s->{
				ActivityDTO activityDTO = new ActivityDTO();
				activityDTO.setTitle(s.getActTitle());
				activityDTO.setDetail("");
				activityDTO.setIcon(s.getActImg());
				activityDTO.setActUrl(s.getActUrl());
				activityDTO.setIsFinish(s.getIsFinish());
				lotteryActivityDTOs.add(activityDTO);
			});
		}
		
		return lotteryActivityDTOs;
	}
}
