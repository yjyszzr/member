package com.dl.member.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserAccount;

public interface UserAccountMapper extends Mapper<UserAccount> {
	
	List<UserAccount> queryUserAccountBySelective(UserAccount  userAccount);
	
	int insertUserAccount(UserAccount userAccount); 
	
	int updateUserAccountBySelective(UserAccount userAccount); 
	
	List<UserAccount> queryUserAccountCurMonth(@Param("userId") Integer userId);
	
	List<UserAccount> queryUserAccountRewardByOrdersn(@Param("list") List<String> list);
	
}