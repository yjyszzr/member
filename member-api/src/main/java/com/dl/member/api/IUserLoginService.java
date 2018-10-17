package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.member.dto.UserLoginDTO;
import com.dl.member.param.LoginLogParam;
import com.dl.member.param.MobileInfoParam;
import com.dl.member.param.UserLoginWithSmsParam;

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
	public UserLoginDTO loginBySms(@RequestBody UserLoginWithSmsParam userLoginMobileParam);

	/**
	 * 登录日志添加
	 * 
	 * @param strPaaram
	 * @param request
	 * @return
	 */
	@RequestMapping(path = "/user/findByMobile", method = RequestMethod.POST)
	public UserLoginDTO findByMobile(@RequestBody MobileInfoParam mobile);

}
