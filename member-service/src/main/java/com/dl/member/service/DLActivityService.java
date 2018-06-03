package com.dl.member.service;
import com.dl.member.model.DLActivity;
import com.dl.member.dao.DLActivityMapper;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
