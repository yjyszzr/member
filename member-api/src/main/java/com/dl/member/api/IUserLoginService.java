package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.member.dto.UserLoginDTO;
import com.dl.member.param.LoginLogParam;

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
	 * 登录日志添加
	 * 
	 * @param strPaaram
	 * @param request
	 * @return
	 */
	@RequestMapping(path = "/user/findByMobile", method = RequestMethod.POST)
	public UserLoginDTO findByMobile(@RequestBody String mobile);

}
