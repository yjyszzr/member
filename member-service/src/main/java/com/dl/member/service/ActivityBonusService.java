package com.dl.member.service;
import com.dl.member.model.ActivityBonus;
import com.dl.member.dao.ActivityBonusMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ActivityBonusService extends AbstractService<ActivityBonus> {
    @Resource
    private ActivityBonusMapper activityBonusMapper;

}
