package com.dl.member.dao;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.MemberThirdApiLog;
import com.dl.member.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends Mapper<User> {

	int insertWithReturnId(User user);

	int updateUserOnPassSignByUserId(User user);
	
	User queryUserByUserId(@Param("userId") Integer userId);

	User queryUserExceptPass(@Param("userId") Integer userId);
	
	int deleteUserByUserId(@Param("userId") Integer userId);
	
	int updateUserInfoDlj(User user);
	/**
	 * 更新用户账户资金
	 * 
	 * @param user
	 * @return
	 */
	int updateUserMoneyAndUserMoneyLimit(User user);

	int updateUserInfoStatusByUserId(@Param("userId") Integer userId);
	/**
	 * 在数据库中更新用户账户资金
	 * 
	 * @param user
	 * @return
	 */
	int updateInDBUserMoneyAndUserMoneyLimit(User user);

	/**
	 * 提现，扣除用户可提现余额
	 * 
	 * @param user
	 * @return
	 */
	int reduceUserMoneyInDB(User user);

	/**
	 * 查询多个用户的当前余额
	 */
	List<User> queryUserByUserIds(@Param("userIds") List<Integer> userIds);

	int saveMemberThirdApiLog(MemberThirdApiLog thirdApiLog);

	int updateUserMoneyForCashCoupon(User user);

	List<String> getClientIds(@Param("userIds") List<Integer> userIds);

	Integer updateUserInfo(User user);
	
	int updateIsReal0to1(@Param("userId")Integer userId);

	User selectUserFoUpdateByUserId(@Param("userId")Integer userId);

	String getMobileById(@Param("userId") Integer userId);

	User queryUserByMobileAndAppCdde(@Param("mobile") String mobile,@Param("appCodeName") String appCodeName);

	Integer updateParentUserId(@Param("parentUserId")Integer parentUserId,@Param("userId")Integer userId);

	/**
	 * 查询用户信息
	 * @param mobile
	 * @return
	 */
	User getUserByMobile(String mobile);
}