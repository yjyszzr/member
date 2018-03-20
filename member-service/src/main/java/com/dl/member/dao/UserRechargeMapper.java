package com.dl.member.dao;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserRecharge;

public interface UserRechargeMapper extends Mapper<UserRecharge> {
	
	int insertUserRecharge(UserRecharge userRecharge);
	
	int updateUserRechargeBySelective(UserRecharge userRecharge);
}