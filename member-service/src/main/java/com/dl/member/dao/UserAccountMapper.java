package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserAccount;

public interface UserAccountMapper extends Mapper<UserAccount> {
	
	List<UserAccount> queryUserAccountBySelective(UserAccount  userAccount);
	
	int insertUserAccount(UserAccount userAccount); 
	
}