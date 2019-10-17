package com.dl.member.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserLoginLog;

public interface UserLoginLogMapper extends Mapper<UserLoginLog> {

	UserLoginLog getLastLog(@Param("userId")Integer userId);

	void updateLogOutTime(UserLoginLog ull);
}