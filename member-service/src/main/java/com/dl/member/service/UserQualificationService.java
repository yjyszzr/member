package com.dl.member.service;
import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.dao.UserQualificationMapper;
import com.dl.member.model.UserQualification;

@Service
public class UserQualificationService extends AbstractService<UserQualification> {
    @Resource
    private UserQualificationMapper userQualificationMapper;
    
	@Resource
	private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 查询活动资格
     * @param actId
     * @param actType
     * @return 0-代表无资格 1-代表有资格
     */
    public int queryActQF(Integer actId,Integer actType) {
    	Integer userId = SessionUtil.getUserId();
    	UserQualification uqf = userQualificationMapper.queryQualificationByUserId(userId, actId, actType);
    	if(null == uqf) {
    		return 0;
    	}else if(1 == uqf.getQualification()){
    		return 1;
    	}else if(2 == uqf.getQualification()) {
    		return 0;
    	}else {
    		return 0;
    	}
    }
    
    /**
     * 领取活动资格
     * @param actId
     * @param actType
     * @return
     */
    public int reaceiveActQF(Integer actId,Integer actType) {
    	Integer userId = SessionUtil.getUserId();
    	UserQualification userQualification = new UserQualification();
    	userQualification.setQualification(1);
    	userQualification.setActId(actId);
    	userQualification.setActType(actType);
    	userQualification.setUserId(userId);
    	userQualification.setReceiveTime(DateUtil.getCurrentTimeLong());
    	int rst = userQualificationMapper.insertUserQualification(userQualification);
    	return rst;
    }

}
