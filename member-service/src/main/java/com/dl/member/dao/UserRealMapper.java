package com.dl.member.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserReal;

public interface UserRealMapper extends Mapper<UserReal> {
	
	UserReal queryUserRealByUserId(@Param("userId") Integer userId);
	
	int countUserRealByIDCodeAndUserId(@Param("userId") Integer userId,@Param("idCode") String IdCode);
	
}