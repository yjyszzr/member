package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserCollect;

public interface UserCollectMapper extends Mapper<UserCollect> {
	
	List<UserCollect> queryUserCollectListBySelective(@Param("userId") Integer userId);
}