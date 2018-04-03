package com.dl.member.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.User;

public interface UserMapper extends Mapper<User> {
	
	int insertWithReturnId(User user);
	
	User queryUserExceptPass(@Param("userId") Integer userId);
	
	/**
	 * 更新用户账户资金
	 * @param user
	 * @return
	 */
	int updateUserMoneyAndUserMoneyLimit(User user);
}