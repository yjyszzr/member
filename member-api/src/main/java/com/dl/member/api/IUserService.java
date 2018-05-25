package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.StrParam;
//import com.pgt.shop.member.dto.UserCapitalDTO;
//import com.pgt.shop.member.param.CancelChangeParam;
//import com.pgt.shop.member.param.ConfirmOrderParam;
import com.dl.member.param.UserBonusParam;
import com.dl.member.param.UserIdParam;


/**
 * 用户接口
 * @author zhangzirong
 *
 */
@FeignClient(value="member-service")
public interface IUserService {
	
	/**
	 * 查询用户接口
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/userInfoExceptPass", method=RequestMethod.POST)
	public BaseResult<UserDTO> userInfoExceptPass(@RequestBody StrParam strParam);
	
	/**
	 * 批量查询查询用户
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/queryUserInfoListByUserIds", method=RequestMethod.POST)
	public BaseResult<UserDTO> queryUserInfoListByUserIds(@RequestBody StrParam strParam);
	
	/**
	 * 查询用户接口
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/queryUserInfo", method=RequestMethod.POST)
	public BaseResult<UserDTO> queryUserInfo(@RequestBody UserIdParam params);
}
