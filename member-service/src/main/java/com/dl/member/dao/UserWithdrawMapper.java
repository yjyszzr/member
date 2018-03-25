package com.dl.member.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserWithdraw;

public interface UserWithdrawMapper extends Mapper<UserWithdraw> {
	
	int insertUserWithdraw(UserWithdraw userWithdraw);
	
	int updateUserWithdrawBySelective(UserWithdraw userWithdraw);
	
	List<UserWithdraw> queryUserWithdrawBySelective(UserWithdraw userWithdraw);
}