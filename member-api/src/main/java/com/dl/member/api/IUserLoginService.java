package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.member.dto.UserLoginDTO;
import com.dl.member.param.LoginLogParam;
import com.dl.member.param.MobileInfoParam;
import com.dl.member.param.MobilePwdCreateParam;
import com.dl.member.param.UserLoginWithPassParam;
import com.dl.member.param.UserLoginWithSmsParam;
import com.dl.member.param.UserRePwdParam;

/**
 * 用户登录
 * 
 * @author Mr.Li
 *
 */
@FeignClient(value = "member-service")
public interface IUserLoginService {

	/**
	 * 登录日志添加
	 * 
	 * @param strPaaram
	 * @param request
	 * @return
	 */
	@RequestMapping(path = "/login/loginLog", method = RequestMethod.POST)
	public void loginLog(@RequestBody LoginLogParam loginLog);

	/**
	 * 用户登录调用member的登录接口
	 * 
	 * @param loginBySms
	 */
	@RequestMapping(path = "/login/loginBySmsForXN", method = RequestMethod.POST)
	public BaseResult<UserLoginDTO> loginBySms(@RequestBody UserLoginWithSmsParam userLoginMobileParam);

	/**
	 * 登录日志添加
	 * 
	 * @param strPaaram
	 * @param request
	 * @return
	 */
	@RequestMapping(path = "/user/findByMobile", method = RequestMethod.POST)
	public BaseResult<UserLoginDTO> findByMobile(@RequestBody MobileInfoParam mobile);
	
	/***
	 * 如果无该用户，直接创建该用户
	 * @param mobile
	 * @return
	 */
	@RequestMapping(path = "/login/onCreateUserXN", method = RequestMethod.POST)
	public BaseResult<UserLoginDTO> onCreateUser(@RequestBody MobilePwdCreateParam mobile);
	
	/**
	 * 手机号密码登录
	 * @param params
	 * @return
	 */
	@RequestMapping(path = "/login/loginByPwdForXN", method = RequestMethod.POST)
	public BaseResult<UserLoginDTO> loginWithPwd(@RequestBody UserLoginWithPassParam params);
	
	
	/**
	 * 修改密码
	 * @param params
	 * @return
	 */
	@RequestMapping(path = "/login/repwdXN", method = RequestMethod.POST)
	public BaseResult<?> rePwd(@RequestBody UserRePwdParam params);
}
