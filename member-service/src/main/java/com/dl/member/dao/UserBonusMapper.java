package com.dl.member.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserBonus;

public interface UserBonusMapper extends Mapper<UserBonus> {
	
	 int insertBatchUserBonus(List<UserBonus> list);
	 
	 int insertBatchUserBonusForRecharge(List<UserBonus> list);
	
	 List<UserBonus> queryUserBonusList(@Param("userBonusIds") Integer[] userBonusIds,@Param("userId")Integer userId,@Param("curTime") Integer curTime);

	 List<UserBonus> queryUserBonusBySelective(UserBonus userBonus);
	 
	 List<UserBonus> queryUserBonusForPay(UserBonus userBonus);
	 
	 List<UserBonus> queryUserBonusForUsed(UserBonus userBonus);
	 
	 List<Integer> queryUserBonusIdsExpire(@Param("now") Integer now);
	 
	 List<Integer> queryUserBonusIdsSoonExpire();
	 
	 int updateBatchUserBonusExpire(@Param("list") List<Integer> userBonusIdList);

	int getUnReadBonusNum(@Param("userId") Integer userId);
	
	int updateUnReadBonus(@Param("userId") Integer userId);
	
	List<UserBonus> queryPUshBonusList(@Param("start")Integer start, @Param("end")Integer end);
}