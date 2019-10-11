package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserBank;

public interface UserBankMapper extends Mapper<UserBank> {
	int batchUpdateUserBankStatus(@Param("status") String status,@Param("list") List<Integer> list);
	
	int batchUpdateUserBankIsSign(@Param("userId") Integer userId,@Param("isSign") Integer purpose);

	int updateUserBank(UserBank userBank);
	
	List<UserBank> queryUserBankBySelective(UserBank userBank);
	
}