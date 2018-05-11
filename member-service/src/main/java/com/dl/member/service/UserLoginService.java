package com.dl.member.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.dl.member.util.TokenUtil;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.RegexUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserMapper;
import com.dl.member.dto.UserLoginDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.model.UserLoginLog;
import com.dl.member.param.UserDeviceParam;
import com.dl.member.param.UserLoginWithPassParam;
import com.dl.member.param.UserLoginWithSmsParam;
import com.dl.member.util.Encryption;
import lombok.extern.slf4j.Slf4j;


/**
 * 用户登录服务
 *
 * @author zhangzirong
 * @date 2018.03.09
 */
@Service
@Slf4j
public class UserLoginService extends AbstractService<UserLoginLog> {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
	
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserRegisterService userRegisterService;


    /**
     * 密码登录 
     *
     * @param userLoginMobileParam
     * @return
     */
    public BaseResult<UserLoginDTO> loginByPass(UserLoginWithPassParam userLoginMobileParam) {
        String mobile = userLoginMobileParam.getMobile();
        String password = userLoginMobileParam.getPassword();
        UserLoginDTO userLoginDTO = new UserLoginDTO();
//        UserDeviceParam device = userLoginMobileParam.getDevice();
        int passWrongCount = 5;
        User user = userService.findBy("mobile", mobile);
        if (null == user) {
        	return ResultGenerator.genResult(MemberEnums.WRONG_IDENTITY.getcode(), MemberEnums.WRONG_IDENTITY.getMsg());
        }
        
        Integer userStatus = user.getUserStatus();
    	if(userStatus.equals(ProjectConstant.USER_STATUS_NOMAL)) {//账号正常
    		BaseResult<UserLoginDTO> userLoginRst = this.verifyUserPass(password, user, userLoginMobileParam);
    		if(userLoginRst.getCode() != 0) {
    			return ResultGenerator.genResult(userLoginRst.getCode(), userLoginRst.getMsg());
    		}
    		userLoginDTO = userLoginRst.getData();
    		return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
    		
    	}else if(userStatus.equals(ProjectConstant.USER_STATUS_LOCK)){//账号处于被锁状态
    		Integer time  = DateUtil.getCurrentTimeLong() - user.getLastTime();
        	if(time > 60) {
        		BaseResult<UserLoginDTO> userLoginRst = this.verifyUserPass(password, user, userLoginMobileParam);
        		if(userLoginRst.getCode() != 0) {
        			return ResultGenerator.genResult(userLoginRst.getCode(), userLoginRst.getMsg());
        		}
        		userLoginDTO = userLoginRst.getData();
        		
            	User normalUser = new User();
            	normalUser.setUserId(user.getUserId());
            	normalUser.setUserStatus(0);
            	normalUser.setPassWrongCount(0);
            	userService.update(normalUser);
        		
        		return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
        	}else {
        		return ResultGenerator.genResult(MemberEnums.PASS_WRONG_BEYOND_5.getcode(), MemberEnums.PASS_WRONG_BEYOND_5.getMsg());
        	}
    	}else if(userStatus.equals(ProjectConstant.USER_STATUS_FROZEN)) {//账户被冻结
    		return ResultGenerator.genResult(MemberEnums.USER_ACCOUNT_FROZEN.getcode(), MemberEnums.USER_ACCOUNT_FROZEN.getMsg());
    	}        
        
		return null;
    }

    /**
     * 短信验证码登录 
     *
     * @param userLoginMobileParam
     * @return
     */
    public BaseResult<UserLoginDTO> loginBySms(UserLoginWithSmsParam userLoginMobileParam,HttpServletRequest request) {
    	if(!RegexUtil.checkMobile(userLoginMobileParam.getMobile())) {
    		return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
    	}
    	
    	String mobile = userLoginMobileParam.getMobile();
        String strRandom4 = RandomUtil.generateUpperString(4);
//      UserDeviceParam device = userLoginMobileParam.getDevice();
        String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.LOGIN_TPLID + "_" + userLoginMobileParam.getMobile());
        if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(userLoginMobileParam.getSmsCode())) {
            return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
        }
        int passWrongCount = 5;
        User user = userService.findBy("mobile", mobile);
        if (null == user) {//新用户注册并登录
  	    	return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
//        	UserRegisterParam userRegisterParam = new UserRegisterParam();
//        	userRegisterParam.setMobile(userLoginMobileParam.getMobile());
//        	userRegisterParam.setPassWord("");
//        	userRegisterParam.setLoginSource(userLoginMobileParam.getLoginSource());
//        	userRegisterParam.setDevice(userLoginMobileParam.getDevice());
//        	BaseResult<Integer> regRst = userRegisterService.registerUser(userRegisterParam, request);
//        	if(regRst.getCode() != 0) {
//        		return ResultGenerator.genFailResult(regRst.getMsg());
//        	}
//        	
//        	UserLoginDTO userLoginDTO = queryUserLoginDTOByMobile(userLoginMobileParam.getMobile(), userLoginMobileParam.getLoginSource());
//			return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
//  	    User user = userService.findBy("mobile", userRegisterParam.getMobile());
        }else {
            Integer userStatus = user.getUserStatus();
        	if(userStatus.equals(ProjectConstant.USER_STATUS_NOMAL)) {//账号正常
	   	         UserLoginDTO userLoginDTO = queryUserLoginDTOByMobile(userLoginMobileParam.getMobile(), userLoginMobileParam.getLoginSource());
	   			 return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
        	}else if(userStatus.equals(ProjectConstant.USER_STATUS_LOCK)){//账号处于被锁状态
            	boolean beyond1h = DateUtil.getCurrentTimeLong() - user.getLastTime() > 60 * 1000 ? true:false;
            	if(beyond1h) {
                	User normalUser = new User();
                	normalUser.setUserId(user.getUserId());
                	normalUser.setUserStatus(0);
                	userService.update(normalUser);
            	
                	UserLoginDTO userLoginDTO = queryUserLoginDTOByMobile(userLoginMobileParam.getMobile(), userLoginMobileParam.getLoginSource());
	    			
                	return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
            	}else {
            		return ResultGenerator.genResult(MemberEnums.PASS_WRONG_BEYOND_5.getcode(), MemberEnums.PASS_WRONG_BEYOND_5.getMsg());
            	}
        	}else if(userStatus.equals(ProjectConstant.USER_STATUS_FROZEN)) {//账户被冻结
        		return ResultGenerator.genResult(MemberEnums.USER_ACCOUNT_FROZEN.getcode(), MemberEnums.USER_ACCOUNT_FROZEN.getMsg());
        	} 
        }
    	
		return null;
    }
    
    /**
     * 按照规则校验用户登录密码
     * @param password
     * @param user
     * @param userLoginMobileParam
     * @return
     */
    public BaseResult<UserLoginDTO> verifyUserPass(String password,User user,UserLoginWithPassParam userLoginMobileParam){
		 if(user.getPassword().equals(Encryption.encryption(password, user.getSalt()))) {//密码正确
   	         UserLoginDTO userLoginDTO = queryUserLoginDTOByMobile(userLoginMobileParam.getMobile(), userLoginMobileParam.getLoginSource());
			 return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
			 
		 }else {
        	int nowWrongPassCount = user.getPassWrongCount();
	        if(nowWrongPassCount <= 5) {
	        	User updatePassWrongCountUser = new User();
	        	updatePassWrongCountUser.setUserId(user.getUserId());
	        	updatePassWrongCountUser.setPassWrongCount(++nowWrongPassCount);
	        	userService.update(updatePassWrongCountUser);
        		return ResultGenerator.genResult(MemberEnums.WRONG_IDENTITY.getcode(), "您输入的密码错误，还有"+(5 - nowWrongPassCount)+"次机会");
        	}else {//输入错误密码超过5次，锁定用户
            	User lockUser = new User();
            	lockUser.setUserId(user.getUserId());
            	lockUser.setLastTime(DateUtil.getCurrentTimeLong());
            	lockUser.setUserStatus(1);
            	userService.update(lockUser);
        		return ResultGenerator.genResult(MemberEnums.PASS_WRONG_BEYOND_5.getcode(), MemberEnums.PASS_WRONG_BEYOND_5.getMsg());
        	}
		 }
    }
    
    /**
     * 根据手机号查询用户登录信息
     * @return
     */
    public UserLoginDTO queryUserLoginDTOByMobile(String mobile,String loginSource) {
		 User userInfo = userService.findBy("mobile", mobile);
		 UserLoginDTO userLoginDTO = new UserLoginDTO();
		 userLoginDTO.setMobile(userInfo.getMobile());
		 userLoginDTO.setHeadImg(userInfo.getHeadImg());
		 userLoginDTO.setNickName(userInfo.getNickname());
		 userLoginDTO.setIsReal(userInfo.getIsReal().equals(ProjectConstant.USER_IS_REAL)?"1":"0");
		 userLoginDTO.setToken(TokenUtil.genToken(userInfo.getUserId(), Integer.valueOf(loginSource)));
		 return userLoginDTO;
    }
    
    /**
     * 登录日志添加
     *
     * @param userId    用户ID
     * @param device    登录设备类型
     * @param loginType 登录类型
     */
//    @Transactional
//    public void loginLog(Integer userId, Integer loginType, Integer source, UserDeviceParam device) {
//        if (device == null) {
//            device = new UserDeviceParam();
//        }
//        //登录日志添加
//        UserLoginLog ull = new UserLoginLog();
//        ull.setUserId(userId);
//        ull.setLoginType(loginType);
//        ull.setLoginStatus(ProjectConstant.LOGIN_STATUS);
//        ull.setLoginIp(StringUtils.isBlank(device.getIp()) ? "" : device.getIp());
//        int time = DateUtil.getCurrentTimeLong();
//        ull.setLoginTime(time);
//        ull.setPlat(StringUtils.isBlank(device.getPlat()) ? "" : device.getPlat());
//        ull.setBrand(StringUtils.isBlank(device.getBrand()) ? "" : device.getBrand());
//        ull.setMid(StringUtils.isBlank(device.getMid()) ? "" : device.getMid());
//        ull.setOs(StringUtils.isBlank(device.getOs()) ? "" : device.getOs());
//        ull.setW(StringUtils.isBlank(device.getW()) ? "" : device.getW());
//        ull.setH(StringUtils.isBlank(device.getH()) ? "" : device.getH());
//        ull.setImei(StringUtils.isBlank(device.getImei()) ? "" : device.getImei());
//        ull.setLoginSource(ObjectUtils.defaultIfNull(source, 1).toString());//StringUtils.isBlank(device.getLoginSource()) ? "" : device.getLoginSource()
//        ull.setLoginParams(StringUtils.isBlank(device.getLoginParams()) ? "" : device.getLoginParams());
//
//        int number = userLoginLogMapper.insertSelective(ull);
//        if (number != 1) {
//            throw new ServiceException(UserResultEnums.USER_GIGN_LOG_ERROR);
//        }
//        log.info("登录日志添加");
//    }



}
