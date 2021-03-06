package com.dl.member.service;
import com.dl.member.model.UserCollect;
import com.dl.member.model.UserMatchCollect;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserCollectMapper;
import com.dl.member.dao.UserMatchCollectMapper;
import com.dl.member.enums.MemberEnums;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.SessionUtil;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
public class UserCollectService extends AbstractService<UserCollect> {
    @Resource
    private UserCollectMapper userCollectMapper;
    @Resource
    private UserMatchCollectMapper userMatchCollectMapper;
    
    public List<UserCollect> queryMyCollectList(){
    	Integer userId = SessionUtil.getUserId();
    	UserCollect userCollect = new UserCollect();
    	userCollect.setUserId(userId);
    	List<UserCollect> userCollectList = userCollectMapper.queryUserCollectListBySelective(userCollect);
    	return userCollectList;
    }
    
    
   public BaseResult<String> cancleCollect(Integer articleId) {
	   	Integer userId = SessionUtil.getUserId();
	   	UserCollect userCollect = new UserCollect();
	   	userCollect.setArticleId(articleId);
	   	userCollect.setUserId(userId);
	   	List<UserCollect> userCollectList = userCollectMapper.queryUserCollectListBySelective(userCollect);
	   	if(CollectionUtils.isEmpty(userCollectList)) {
	   		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(),"用户没有收藏该文章");
	   	}
	   	
	   	int rst = userCollectMapper.deleteUserCollect(userId, articleId);
	   	if(1 != rst) {
	   		return ResultGenerator.genFailResult("取消收藏失败");
	   	}
	   	return ResultGenerator.genSuccessResult("取消收藏成功");
   }
}
