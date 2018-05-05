package com.dl.member.service;
import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.enums.RespStatusEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.RegexUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserMapper;
import com.dl.member.dto.UserDTO;
import com.dl.member.dto.UserRealDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.param.UserParam;
import com.dl.member.util.Encryption;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class UserService extends AbstractService<User> {
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private UserRealService userRealService;
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 查询用户信息 （除了密码）
     * @param userId
     * @return
     */
    public UserDTO queryUserByUserIdExceptPass() {
    	Integer userId = SessionUtil.getUserId();
    	User user = userMapper.queryUserExceptPass(userId);
		UserDTO userDTO = new UserDTO();
		try {
			BeanUtils.copyProperties(userDTO, user);
			userDTO.setUserMoney(String.valueOf(user.getUserMoney()));
			userDTO.setIsReal(user.getIsReal().equals("1")?"1":"0");
			userDTO.setBalance(String.valueOf(user.getUserMoney().add(user.getUserMoneyLimit()).subtract(user.getFrozenMoney())));
			
			String realName = "";
			UserRealDTO userRealDTO = userRealService.queryUserReal();
			if(userRealDTO != null) {
				realName = userRealDTO.getRealName();
			}
			
			String mobile = user.getMobile();
			String strStar4 = RandomUtil.generateStarString(4);
			String mobileStr = mobile.replace(mobile.substring(3, 7), strStar4);
			userDTO.setMobile(mobileStr);
			userDTO.setRealName(realName);
			userDTO.setTotalMoney(String.valueOf(user.getUserMoney().add(user.getUserMoneyLimit())));
		} catch (Exception e) {
			throw new ServiceException(RespStatusEnum.SERVER_ERROR.getCode(), RespStatusEnum.SERVER_ERROR.getMsg());
		}
		return userDTO;
    }
    
	/**
	 * 保存用户
	 * @param uParams
	 */
	@Transactional
	public Integer saveUser(UserParam userParam) {
		User user = new User();
		String userName = generateUserName(userParam.getMobile());//账号
		String nickName = generateNickName(userParam.getMobile());//昵称
		user.setMobile(userParam.getMobile());
		user.setUserName(userName);
    	user.setNickname(nickName);
    	user.setHeadImg(ProjectConstant.USER_DEFAULT_HEADING_IMG);
		user.setRegTime(DateUtil.getCurrentTimeLong());
		user.setLastTime(DateUtil.getCurrentTimeLong());
		user.setRegIp(userParam.getRegIp());
		user.setRegFrom(userParam.getLoginSource());
		user.setUserStatus(0);// 用户状态：有效
		user.setUserType(false);// 用户类型：个人用户
		user.setMobileSupplier("");
		user.setMobileProvince("");
		user.setMobileCity("");
		user.setLastIp(userParam.getLastIp());
		user.setSex(false);
		user.setMobile(userParam.getMobile());
		String loginsalt = Encryption.salt();
		user.setSalt(loginsalt);
		String paysalt = Encryption.salt();
		user.setPayPwdSalt(paysalt);
		user.setPassword(Encryption.encryption(userParam.getPassWord(), loginsalt));
		user.setRankPoint(0);
		user.setPassWrongCount(0);
		user.setIsReal(ProjectConstant.USER_IS_NOT_REAL);
		int rst = userMapper.insertWithReturnId(user);
		if(1 != rst) {
			return 0;
		}
		return user.getUserId();
	}
	
	
	/**
	 * 校验用户的手机号
	 * @param mobileNumberParam
	 * @return
	 */
	public BaseResult<String> validateUserMobile(String mobileNumber) {
    	if(!RegexUtil.checkMobile(mobileNumber)) {
    		return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
    	}
    	
    	User user = this.findBy("mobile", mobileNumber);
    	if(null == user) {
    		 return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
    	}
    	   	
    	return ResultGenerator.genSuccessResult("用户手机号校验成功");
	}
	
	/**
	 * 更新用户登录密码
	 * @param mobileNumberParam
	 */
	public BaseResult<String> updateUserLoginPass(String userLoginPass,String mobileNumber,String smsCode){
    	if(!userLoginPass.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$")) {
    		return ResultGenerator.genResult(MemberEnums.PASS_FORMAT_ERROR.getcode(), MemberEnums.PASS_FORMAT_ERROR.getMsg());
    	}
		
    	User user = this.findBy("mobile", mobileNumber);
    	if(null == user) {
   		 	return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
    	}
    	
        String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.RESETPASS_TPLID + "_" + mobileNumber);
        if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(smsCode)) {
            return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
        }
    	
    	User updateUser = new User();
    	updateUser.setUserId(user.getUserId());
    	updateUser.setPassword(Encryption.encryption(userLoginPass, user.getSalt()));
    	this.update(updateUser);
    	
    	return ResultGenerator.genSuccessResult("更新用户登录密码成功");
	}


	/**
	 * 查询用户信息：所有字段
	 * @return
	 */
	public UserDTO queryUserInfo() {
		Integer userId = SessionUtil.getUserId();
		User user = this.findById(userId);
		UserDTO userDTO = new UserDTO();
		try {
			BeanUtils.copyProperties(userDTO, user);
			String strRandom4 = RandomUtil.generateUpperString(4);
			String mobile = user.getMobile();
			mobile = mobile.replace(mobile.substring(3, 7), strRandom4);
			userDTO.setMobile(mobile);
			userDTO.setUserMoney(String.valueOf(user.getUserMoney()));
			userDTO.setUserMoneyLimit(String.valueOf(user.getUserMoneyLimit()));
			BigDecimal totalMoney = user.getUserMoney().add(user.getUserMoneyLimit());
			userDTO.setTotalMoney(String.valueOf(totalMoney));
		} catch (Exception e) {
			throw new ServiceException(RespStatusEnum.SERVER_ERROR.getCode(), RespStatusEnum.SERVER_ERROR.getMsg());
		}
		return userDTO;
	}
	
	
	/**
	 * 生成昵称：
	 *
	 * @param mobile
	 * @return
	 */
	public String generateNickName(String mobile) {
		if(StringUtils.isEmpty(mobile)) {
			return "****彩主";
		}
		String userName = String.format("%s彩主", mobile.substring(mobile.length()-4, mobile.length()));
		return userName.toString();
	}
	
	/**
	 * 生成账号：
	 * 1.随机生成4位字母 2.生成用户名 3.查询重复的用户名条数 4.如果有重复用户名，则重新生成
	 * @param mobile
	 * @return
	 */
	public String generateUserName(String mobile) {
		StringBuffer userName = new StringBuffer("dl");
		String strRandom4 = RandomUtil.generateUpperString(4);
		userName.append(mobile.replace(mobile.substring(3, 7), strRandom4));
		User user = this.findBy("userName", userName.toString());
		if (null != user) {
			generateUserName(mobile);
		}
		return userName.toString();
	}

}
