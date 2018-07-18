package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserMatchCollect;

public interface UserMatchCollectMapper extends Mapper<UserMatchCollect> {
	
	List<Integer> queryUserMatchCollectListBySelective(@Param("userId")Integer userId,@Param("dateStr")String dateStr);
	
	int insertUserCollectMatch(UserMatchCollect umc);
	
	int queryUserMatchCollect(@Param("userId") Integer userId,@Param("matchId") Integer matchId);
	
	int deleteUserMatchCollect(@Param("userId") Integer userId,@Param("matchId") Integer matchId);
	
}