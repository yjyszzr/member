package com.dl.member.service;
import com.dl.member.model.DLActivity;
import com.dl.member.dao.DLActivityMapper;
import com.dl.base.service.AbstractService;
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

}
