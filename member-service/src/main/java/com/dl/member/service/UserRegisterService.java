package com.dl.member.service;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.IpUtil;
import com.dl.base.util.RegexUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserMapper;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.param.UserParam;
import com.dl.member.param.UserRegisterParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public class UserRegisterService extends AbstractService<User> {
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private UserService userService;

    
	/**
	 * 注册并登录
	 * @param userRegisterParam
	 * @param request
	 * @return
	 */
    @Transactional
	public BaseResult<Integer> registerUser(UserRegisterParam userRegisterParam, HttpServletRequest request) {
    	if(!RegexUtil.checkMobile(userRegisterParam.getMobile())) {
    		return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
    	}

		UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
    	String appCodeName = "11";//StringUtils.isEmpty(userDeviceInfo.getAppCodeName())?"10":userDeviceInfo.getAppCodeName();
    	User user = userMapper.queryUserByMobileAndAppCdde(userRegisterParam.getMobile(),appCodeName);
		if(user != null){
			return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), MemberEnums.ALREADY_REGISTER.getMsg());
		}
    	
    	UserParam userParam = new UserParam();
    	userParam.setMobile(userRegisterParam.getMobile());
    	userParam.setPassWord(userRegisterParam.getPassWord());
    	userParam.setRegIp(IpUtil.getIpAddr(request));
    	userParam.setLastIp(IpUtil.getIpAddr(request));
		if (userRegisterParam.getLoginSource().equals(ProjectConstant.LOGIN_SOURCE_ANDROID)) {
			userParam.setLoginSource(ProjectConstant.ANDROID);
		} else if (userRegisterParam.getLoginSource().equals(ProjectConstant.LOGIN_SOURCE_IOS)) {
			userParam.setLoginSource(ProjectConstant.IOS);
		} else if (userRegisterParam.getLoginSource().equals(ProjectConstant.LOGIN_SOURCE_PC)) {
			userParam.setLoginSource(ProjectConstant.PC);
		} else if (userRegisterParam.getLoginSource().equals(ProjectConstant.LOGIN_SOURCE_H5)) {
			userParam.setLoginSource(ProjectConstant.H5);
		} else {
			userParam.setLoginSource("unknown");
		}
		
		if(!userRegisterParam.getLoginSource().equals(ProjectConstant.LOGIN_SOURCE_H5)) {
			if(null == userRegisterParam.getPushKey()) {
				userParam.setPushKey("");
			}else {
				userParam.setPushKey(userRegisterParam.getPushKey());
			}
		}else {
			userParam.setPushKey("");
		}

		userParam.setInvitCode(userRegisterParam.getInvitCode());
		
    	Integer userId = userService.saveUser(userParam);
   	
    	return ResultGenerator.genSuccessResult("注册成功",userId);
    }
 
}
