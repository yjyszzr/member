package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserMatchCollect;

public interface UserMatchCollectMapper extends Mapper<UserMatchCollect> {
	
	List<Integer> queryUserMatchCollectListBySelective(UserMatchCollect umc);
	
	int insertUserCollectMatch(UserMatchCollect umc);
	
	List<UserMatchCollect> queryUserMatchCollect(@Param("user_id") Integer userId,@Param("match_id") Integer matchId);
	
}