package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserCollect;

public interface UserCollectMapper extends Mapper<UserCollect> {
	
	List<UserCollect> queryUserCollectListBySelective(UserCollect userCollect);
	
	int updateUserCollect(@Param("isDelete") Integer isDelete,@Param("userId") Integer userId,@Param("articleId") Integer articleId);
	
	int deleteUserCollect(@Param("userId") Integer userId,@Param("articleId") Integer articleId);

}