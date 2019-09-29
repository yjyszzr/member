package com.dl.member.api;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.member.dto.ChannelCustomerBindDTO;
import com.dl.member.dto.MediaTokenDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.dto.UserRealDTO;
import com.dl.member.param.MediaTokenParam;
import com.dl.member.param.MobileAndPassParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.TokenParam;
import com.dl.member.param.UserIdParam;
import com.dl.member.param.UserIdRealParam;


/**
 * 用户接口
 * @author zhangzirong
 *
 */
@FeignClient(value="member-service")
public interface IUserService {

	@RequestMapping(path="/user/queryUserByMobileAndPass", method=RequestMethod.POST)
	public BaseResult<UserDTO> queryUserByMobileAndPass(@RequestBody MobileAndPassParam param);
//	@RequestMapping(path="/user/queryUserByMobile", method=RequestMethod.POST)
//	public BaseResult<UserDTO> queryUserByMobile(@RequestBody MobileAndPassParam param);
	@RequestMapping(path="/user/queryUserInfoByToken", method=RequestMethod.POST)
	public BaseResult<UserDTO> queryUserInfoByToken(@RequestBody TokenParam param);
	@RequestMapping(path="/user/updateUserInfoByUserId", method=RequestMethod.POST)
	public BaseResult<Integer> updateUserInfoByUserId(@RequestBody UserIdParam param);
	/**
	 * 查询用户接口
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/userInfoExceptPass", method=RequestMethod.POST)
	public BaseResult<UserDTO> userInfoExceptPass(@RequestBody StrParam strParam);
	
	/**
	 * 查询用户接口 手机号码真实信息
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/userInfoExceptPassReal", method=RequestMethod.POST)
	public BaseResult<UserDTO> userInfoExceptPassReal(@RequestBody StrParam strParam);
	
//	/**
//	 * 批量查询查询用户
//	 * @param UserBonusParam
//	 * @return
//	 */
//	@RequestMapping(path="/user/queryUserInfoListByUserIds", method=RequestMethod.POST)
//	public BaseResult<ChannelDistributorDTO> queryUserInfoListByUserIds(@RequestBody UserIdParam param);
	
	/**
	 * 查询用户接口
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/queryUserInfo", method=RequestMethod.POST)
	public BaseResult<UserDTO> queryUserInfo(@RequestBody UserIdParam params);
	
	/**
	 * 查询用户是否与店员绑定过
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/queryChannelDistributorByUserId", method=RequestMethod.POST)
	public BaseResult<ChannelCustomerBindDTO> queryChannelDistributorByUserId(@RequestBody UserIdParam params);
	
	/**
	 * 查询用户接口
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/queryUserInfoReal", method=RequestMethod.POST)
	public BaseResult<UserDTO> queryUserInfoReal(@RequestBody UserIdRealParam params);
	
	/**
	 * 获取MediaToken接口
	 * @param params
	 * @return
	 */
	@RequestMapping(path="/media/getToken", method=RequestMethod.POST)
	public BaseResult<MediaTokenDTO> getMediaTokenInfo(@RequestBody MediaTokenParam params);
	/**
	 * 查询用户实名认证信息
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/real/queryUserRealByUserId", method=RequestMethod.POST)
	public BaseResult<UserRealDTO> queryUserRealByUserId(@RequestBody UserIdRealParam params);
}
