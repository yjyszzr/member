package com.dl.member.web;

import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.dto.UserLoginDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.param.*;
import com.dl.member.service.UserLoginService;
import com.dl.member.service.UserService;
import com.dl.member.util.Encryption;
import com.dl.member.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户登录接口 zhangzirong
 */
@Api(value = "用户登录接口")
@RestController
@RequestMapping("/login")
@Slf4j
public class UserLoginContorller {
	private final static Logger logger = LoggerFactory.getLogger(UserLoginContorller.class);
	@Resource
	private UserLoginService userLoginService;
	@Resource 
	private UserService userService;
	private UserDeviceInfo userDeviceInfo;


	/**
	 *  1.要优化的地方,前端密码要md5加密后传过来;
	 *  2.通过UserDevice 中的appv 版本号来区分版本，让各自版本都按照以前和现在的逻辑运行
	 * @param userLoginMobileParam
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "密码登录", notes = "密码登录")
	@PostMapping("/loginByPass")
	public BaseResult<UserLoginDTO> loginByPass(@RequestBody UserLoginWithPassParam userLoginMobileParam, HttpServletRequest request) {
		UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
		BaseResult<UserLoginDTO> loginByPass = userLoginService.loginByPass(userLoginMobileParam);
		if (loginByPass.getCode() == 0) {
			String token = loginByPass.getData().getToken();
			request.getSession().setAttribute("user_token", token);
		}



		return loginByPass;
	}

	@ApiOperation(value = "短信验证码登录", notes = "短信验证码登录")
	@PostMapping("/loginBySms")
	public BaseResult<UserLoginDTO> loginBySms(@RequestBody UserLoginWithSmsParam userLoginMobileParam, HttpServletRequest request) {
		UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
		BaseResult<UserLoginDTO> loginBySms = userLoginService.loginBySms(userLoginMobileParam, request);
		if (loginBySms.getCode() == 0) {
			String token = loginBySms.getData().getToken();
			request.getSession().setAttribute("user_token", token);
		}
		return loginBySms;
	}

	@ApiOperation(value = "西安短信验证码登录", notes = "西安短信验证码登录")
	@PostMapping("/loginBySmsForXN")
	public BaseResult<UserLoginDTO> loginBySmsForXN(@RequestBody UserLoginWithSmsParam userLoginMobileParam) {
		BaseResult<UserLoginDTO> loginBySms = userLoginService.loginBySmsForXN(userLoginMobileParam);
		logger.info("loginBySmsForXN返回值==========================={}", loginBySms);
		return loginBySms;
	}

	@ApiOperation(value = "西安人工出票系统密码登录", notes = "西安人工出票系统密码登录")
	@PostMapping("/loginByPwdForXN")
	public BaseResult<UserLoginDTO> loginByPwdForXN(@RequestBody UserLoginWithPassParam userLoginMobileParam) {
		logger.info("[loginByPwdForXN]");
		BaseResult<UserLoginDTO> loginByPass = userLoginService.loginByPass(userLoginMobileParam);
		return loginByPass;
	}
	
	@ApiOperation(value = "西安人工出票系统用户创建", notes = "西安人工出票系统用户创建")
	@PostMapping("/onCreateUserXN")
	public BaseResult<UserLoginDTO> onCreateUser(@RequestBody MobilePwdCreateParam params) {
		logger.info("[onCreateUser]" + " mobile:" + params.getMobile() + " pwd:" + params.getPassword() + " loginSrc:" + params.getLoginSource());
		UserParam userParams = new UserParam();
		userParams.setMobile(params.getMobile());
		userParams.setPassWord(params.getPassword());
		userParams.setRegIp("0.0.0.1");
		userParams.setLoginSource("人工出票");
		int cnt = userService.saveUser(userParams);
		if(cnt > 0) {
			logger.info("[onCreateUser]" + " succ");
			return ResultGenerator.genSuccessResult("创建成功");
		}else {
			logger.info("[onCreateUser]" + " fail");
			return ResultGenerator.genFailResult("创建失败");
		}
	}
	
	@ApiOperation(value = "西安人工出票系统重置密码", notes = "西安人工出票系统重置密码")
	@PostMapping("/repwdXN")
	public BaseResult<?> rePwd(@RequestBody UserRePwdParam params) {
		String mobile = params.getMobile();
		String pwd = params.getPassword();
		String newPwd = params.getNewPwd();
		String loginSource = params.getLoginSource();
		logger.info("[rePwd]" + " mobile:" + mobile + " pwd:" + pwd + " newPwd:" + newPwd);
		User user = userService.findByMobile(mobile);
		if(user == null) {
			logger.info("[rePwd]" + "未注册~");
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(),MemberEnums.NO_REGISTER.getMsg());
		}
		if(StringUtils.isEmpty(pwd) || StringUtils.isEmpty(newPwd)) {
			logger.info("[rePwd]" + " pwd:" + pwd + " newPwd:" + newPwd);
			return ResultGenerator.genResult(MemberEnums.PARAMS_NOT_NULL.getcode(),"密码参数错误");
		}
		String oldPwd = user.getPassword();
		logger.info("[rePwd]" + " oldPwd:" + oldPwd);
		String loginsalt = user.getSalt();
		String pass = Encryption.encryption(pwd,loginsalt);
		logger.info("[rePwd]" + " pass:" + pass);
		if(!oldPwd.equals(pass)) {
			return ResultGenerator.genResult(MemberEnums.OLD_PWD_WRONG.getcode(),MemberEnums.OLD_PWD_WRONG.getMsg());
		}
		User updateUser = new User();
		updateUser.setUserId(user.getUserId());
		updateUser.setPassword(Encryption.encryption(newPwd,loginsalt));
		userService.update(updateUser);
		logger.info("[rePwd]" + "更新密码成功..newPwd:" + newPwd + " oldPwd:" + oldPwd);
		//调用用户登录接口
		UserLoginWithPassParam passParams = new UserLoginWithPassParam();
		passParams.setMobile(mobile);
		passParams.setPassword(newPwd);
		passParams.setLoginSource(loginSource);
		BaseResult<UserLoginDTO> baseRLoginByPwd = userLoginService.loginByPass(passParams);
		UserLoginDTO userLoginDTO = null;
		if(baseRLoginByPwd != null && baseRLoginByPwd.isSuccess() && baseRLoginByPwd.getData() != null) {
			userLoginDTO = baseRLoginByPwd.getData();
		}
		return ResultGenerator.genSuccessResult("密码更新成功",userLoginDTO);
	}
	
	
	@ApiOperation(value = "用户注销", notes = "用户注销")
	@PostMapping("/logout")
	public BaseResult<String> logout(@RequestBody StrParam strPaaram, HttpServletRequest request) {
		userLoginService.loginLogOut();
		TokenUtil.invalidateCurToken();
		request.getSession().removeAttribute("user_token");
		return ResultGenerator.genSuccessResult("用户注销成功");
	}

	@ApiOperation(value = "用户登录日志", notes = "用户登录日志")
	@PostMapping("/loginLog")
	public void loginLog(@RequestBody LoginLogParam loginLog) {
		userLoginService.loginLog(loginLog.getUserId(), loginLog.getLoginType(), loginLog.getLoginSstatus(), loginLog.getLoginParams(), loginLog.getLoginResult());
	}
}
