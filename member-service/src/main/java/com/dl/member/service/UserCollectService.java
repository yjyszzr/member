package com.dl.member.service;
import com.dl.member.model.UserCollect;
import com.dl.member.dao.UserCollectMapper;
import com.dl.base.service.AbstractService;
import com.dl.base.util.SessionUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
public class UserCollectService extends AbstractService<UserCollect> {
    @Resource
    private UserCollectMapper userCollectMapper;
    
    public List<UserCollect> queryMyCollectList(){
    	Integer userId = SessionUtil.getUserId();
    	List<UserCollect> userCollectList = userCollectMapper.queryUserCollectListBySelective(userId);
    	return userCollectList;
    }

}
