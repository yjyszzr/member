package com.dl.member.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.enums.ActivityEnum;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.RegexUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.DlChannelDistributorMapper;
import com.dl.member.dao.DlMessageMapper;
import com.dl.member.dao.UserBonusMapper;
import com.dl.member.dao.UserMapper;
import com.dl.member.dto.ActivityDTO;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.dto.UserNoticeDTO;
import com.dl.member.dto.UserRealDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.DlChannelDistributor;
import com.dl.member.model.User;
import com.dl.member.param.FindUserByMobileAndAppCodeParam;
import com.dl.member.param.IDFACallBackParam;
import com.dl.member.param.SetLoginPassParam;
import com.dl.member.param.TokenParam;
import com.dl.member.param.UserIdParam;
import com.dl.member.param.UserIdRealParam;
import com.dl.member.param.UserParam;
import com.dl.member.util.Encryption;
import com.dl.member.util.TokenUtil;
import com.dl.shop.auth.api.IAuthService;
import com.dl.shop.payment.api.IpaymentService;
import com.dl.shop.payment.dto.RspOrderQueryDTO;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Transactional
@Slf4j
public class UserService extends AbstractService<User> {

	@Resource
	private IpaymentService paymentService;
	
	@Resource
	private UserMapper userMapper;

	@Resource
	private UserBonusMapper userBonusMapper;

	@Resource
	private DlMessageMapper messageMapper;

	@Resource
	private UserRealService userRealService;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private DLActivityService dlActivityService;

	@Resource
	private DlChannelConsumerService channelConsumerService;

	@Resource
	private DlChannelDistributorMapper dlChannelDistributorMapper;

	@Resource
	private IDFAService iDFAService;

	@Resource
	private SysConfigService sysConfigService;

	@Resource
	private IAuthService iAuthService;

	/**
	 * real真实信息
	 *
	 * @return
	 */
	public BaseResult<UserDTO> queryUserByUserIdExceptPassReal() {
		Integer userId = SessionUtil.getUserId();
		if (null == userId) {
			return ResultGenerator.genNeedLoginResult("请登录");
		}

		User user = userMapper.queryUserExceptPass(userId);
		UserDTO userDTO = new UserDTO();
		try {
			BeanUtils.copyProperties(userDTO, user);
		} catch (Exception e1) {
			log.error("个人信息接口的DTO转换异常");
			return ResultGenerator.genFailResult("个人信息接口的DTO转换异常");
		}

		userDTO.setUserMoney(String.valueOf(user.getUserMoney()));
		userDTO.setIsReal(user.getIsReal().equals("1") ? "1" : "0");
		userDTO.setBalance(String.valueOf(user.getUserMoney().add(user.getUserMoneyLimit()).subtract(user.getFrozenMoney())));
		UserRealDTO userRealDTO = userRealService.queryUserReal();
		userDTO.setRealInfo(this.createRealInfo(userRealDTO));
		String mobile = user.getMobile();
		userDTO.setMobile(mobile);
		userDTO.setRealName(userRealDTO != null ? userRealDTO.getRealName() : "");
		userDTO.setTotalMoney(String.valueOf(user.getUserMoney().add(user.getUserMoneyLimit()).subtract(user.getFrozenMoney())));
		userDTO.setActivityDTOList(this.queryAppPromotion(userId));
		return ResultGenerator.genSuccessResult("查询用户信息成功", userDTO);
	}

	/**
	 * 根据token查询用户的所有信息
	 *
	 * @param userId
	 * @return
	 */
	public BaseResult<UserDTO> queryUserByToken(TokenParam param) {
		Integer userId = TokenUtil.getUserIdByToken(param.getUserToken());

		if (null == userId) {
			return ResultGenerator.genNeedLoginResult("请登录");
		}

		User user = userMapper.queryUserByUserId(userId);
		if (user == null) {
			return ResultGenerator.genFailResult("用户不存在");
		}

		UserDTO userDTO = new UserDTO();
		try {
			BeanUtils.copyProperties(userDTO, user);
		} catch (Exception e1) {
			log.error("个人信息接口的DTO转换异常");
			return ResultGenerator.genFailResult("个人信息接口的DTO转换异常");
		}

		userDTO.setUserId(userId);
		userDTO.setHasPass(StringUtils.isBlank(user.getPassword()) ? 0 : 1);
		userDTO.setIsReal(user.getIsReal().equals("1") ? "1" : "0");
		userDTO.setSalt(user.getSalt());
		BigDecimal userMoney = user.getUserMoney();
		String userMoneyStr = userMoney == null ? "0" : userMoney.toString();
		String mobile = user.getMobile();
		userDTO.setMobile(mobile);
		userDTO.setUserMoney(userMoneyStr);
		userDTO.setBalance(String.valueOf(userMoney.add(user.getUserMoneyLimit()).subtract(user.getFrozenMoney())));
		userDTO.setTotalMoney(String.valueOf(userMoney.add(user.getUserMoneyLimit()).subtract(user.getFrozenMoney())));

		return ResultGenerator.genSuccessResult("查询用户信息成功", userDTO);
	}


	/**
	 * 查询用户信息 （除了密码）
	 *
	 * @param userId
	 * @return
	 */
	public BaseResult<UserDTO> queryUserByUserIdExceptPass() {
		Integer userId = SessionUtil.getUserId();
		if (null == userId) {
			return ResultGenerator.genNeedLoginResult("请登录");
		}

		User user = userMapper.queryUserExceptPass(userId);
		if (user == null) {
			return ResultGenerator.genFailResult("用户不存在");
		}

		UserDTO userDTO = new UserDTO();
		try {
			BeanUtils.copyProperties(userDTO, user);
		} catch (Exception e1) {
			log.error("个人信息接口的DTO转换异常");
			return ResultGenerator.genFailResult("个人信息接口的DTO转换异常");
		}

		UserRealDTO userRealDTO = userRealService.queryUserReal();
		userDTO.setHasPass(StringUtils.isBlank(user.getPassword()) ? 0 : 1);
		userDTO.setIsReal(user.getIsReal().equals("1") ? "1" : "0");
		userDTO.setRealInfo(this.createRealInfo(userRealDTO));
		BigDecimal userMoney = user.getUserMoney();
		String userMoneyStr = userMoney == null ? "0" : userMoney.toString();
		String mobile = user.getMobile();
		String strStar4 = RandomUtil.generateStarString(4);
		String mobileStr = mobile.replace(mobile.substring(3, 7), strStar4);
		userDTO.setMobile(mobileStr);
		userDTO.setRealName(userRealDTO != null ? userRealDTO.getRealName() : "");
		userDTO.setUserMoney(userMoneyStr);
		userDTO.setBalance(String.valueOf(userMoney.add(user.getUserMoneyLimit()).subtract(user.getFrozenMoney())));
		userDTO.setTotalMoney(String.valueOf(userMoney.add(user.getUserMoneyLimit()).subtract(user.getFrozenMoney())));
		userDTO.setActivityDTOList(this.queryAppPromotion(userId));
		userDTO.setIsSuperWhite(StringUtils.isEmpty(user.getIsSuperWhite())?"0":user.getIsSuperWhite());

		List<Integer> bidList = new ArrayList<>();
		bidList.add(27);
		bidList.add(28);
		bidList.add(59);
		bidList.add(60);
		List<SysConfigDTO> sysConfigDTOS = sysConfigService.querySysConfigListByBid(bidList);
		SysConfigDTO sDto0 = sysConfigDTOS.get(0);
		SysConfigDTO sDto1 = sysConfigDTOS.get(1);
		SysConfigDTO sDto2 = sysConfigDTOS.get(2);
		SysConfigDTO sDto3 = sysConfigDTOS.get(3);
		UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
		String appCodeNameStr = userDeviceInfo.getAppCodeName();
		String appCodeName = StringUtils.isEmpty(appCodeNameStr)?"10":appCodeNameStr;
		if(appCodeName.equals("10")){
			userDTO.setRecharegeTurnOn(String.valueOf(sDto0.getValue().intValue()));
			userDTO.setWithdrawTurnOn(String.valueOf(sDto1.getValue().intValue()));
		}else if(appCodeName.equals("11")){
			userDTO.setRecharegeTurnOn(String.valueOf(sDto2.getValue().intValue()));
			userDTO.setWithdrawTurnOn(String.valueOf(sDto3.getValue().intValue()));
		}
		if(userId==1000000077) {//财务账号--财务账号提现金额为商户余额
			EmptyParam emptyParam = new EmptyParam();
			BaseResult<RspOrderQueryDTO> ymoney = paymentService.getShMoney(emptyParam);
			if(ymoney!=null && ymoney.getData()!=null) {
				userDTO.setUserMoney(ymoney.getData().getDonationPrice()!=null?ymoney.getData().getDonationPrice():"获取失败");//账户余额
				userDTO.setBalance("0");
				userDTO.setTotalMoney("0");
				userDTO.setUserMoneyLimit("0");
			}
		}
		return ResultGenerator.genSuccessResult("查询用户信息成功", userDTO);
	}

	/**
	 * 构造实名认证信息:包含了部分身份证号
	 *
	 * @return
	 */
	public String createRealInfo(UserRealDTO userRealDTO) {
		String realInfo = "";
		if (userRealDTO != null) {
			String realName = userRealDTO.getRealName();
			realInfo = realName.substring(0, 1) + "**" + "(" + userRealDTO.getIdCode().substring(0, 6) + "****" + userRealDTO.getIdCode().substring(userRealDTO.getIdCode().length() - 4) + ")";
		}
		return realInfo;
	}

	/**
	 * 只有店员才展示有效的推广链接
	 *
	 * @param userId
	 * @return
	 */
	public List<com.dl.member.dto.ActivityDTO> queryAppPromotion(Integer userId) {
		List<com.dl.member.dto.ActivityDTO> activityMemDTOList = new ArrayList<com.dl.member.dto.ActivityDTO>();
		Condition condition = new Condition(DlChannelConsumer.class);
		condition.createCriteria().andCondition("user_id = ", userId);
		List<DlChannelDistributor> channelDistributor = dlChannelDistributorMapper.selectByCondition(condition);
		if (channelDistributor.size() > 0) {
			com.dl.member.dto.ActivityDTO memActivityDTO = new com.dl.member.dto.ActivityDTO();
			List<ActivityDTO> activityDTOList = dlActivityService.queryActivityByActType(ActivityEnum.AppPromotion.getCode());
			if (null != activityDTOList && activityDTOList.size() > 0) {
				activityDTOList.forEach(s -> {
					try {
						BeanUtils.copyProperties(memActivityDTO, s);
					} catch (Exception e) {
						log.error("个人信息接口的DTO转换异常");
					}
					if (0 == s.getIsFinish()) {// 有效
						activityMemDTOList.add(memActivityDTO);
					}
				});
			}
		}
		return activityMemDTOList;
	}

	/**
	 * 保存用户
	 *
	 * @param uParams
	 */
	@Transactional
	public Integer saveUser(UserParam userParam) {
		User user = new User();
		String userName = generateUserName(userParam.getMobile());// 账号
		String nickName = generateNickName(userParam.getMobile());// 昵称
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
		if (!StringUtils.isEmpty(userParam.getPassWord())) {
			user.setPassword(Encryption.encryption(userParam.getPassWord(), loginsalt));
		} else {
			user.setPassword("");
		}
		user.setRankPoint(0);
		user.setPassWrongCount(0);
		user.setIsReal(ProjectConstant.USER_IS_NOT_REAL);
		user.setPushKey(userParam.getPushKey());
		UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		if (userDevice != null) {
			String channel = userDevice.getChannel();
			user.setDeviceChannel(channel);
		}

		if(userParam.getLoginSource().equals("4")){//h5
			userParam.setIsSuperWhite("1");
		}

		UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
		String appCodeName = userDeviceInfo.getAppCodeName();
		log.info("appCodeName:"+appCodeName);
		if(StringUtils.isEmpty(appCodeName)){
			user.setAppCodeName("10");
		}else{
			user.setAppCodeName(appCodeName);
		}

		Integer insertRsult = userMapper.insertWithReturnId(user);
		if (1 != insertRsult) {
			log.error("注册用户失败");
			return null;
		}
		Integer userId = user.getUserId();

		if (userDevice != null && userDevice.getPlat() != null && userDevice.getPlat().equals("iphone")) {
			// idfa 回调、存储 （lidelin）
			IDFACallBackParam idfaParam = new IDFACallBackParam();
			idfaParam.setUserid(userId);
			idfaParam.setIdfa(userDevice.getIDFA());
			iDFAService.callBackIdfa(idfaParam);
		}

		return userId;
	}

	public BaseResult<UserDTO> queryUserByMobileAndPass(String mobile,String pass) {
		UserDTO uDto = new UserDTO();
		if (!RegexUtil.checkMobile(mobile)) {
			return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
		}

		UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
		String appCodeName = org.apache.commons.lang.StringUtils.isEmpty(userDeviceInfo.getAppCodeName())?"10":userDeviceInfo.getAppCodeName();
		User user = userMapper.queryUserByMobileAndAppCdde(mobile,appCodeName);
		if (null == user) {
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}

		if (!user.getPassword().equals(Encryption.encryption(pass, user.getSalt()))) {
			return ResultGenerator.genResult(MemberEnums.WRONG_IDENTITY.getcode(), MemberEnums.WRONG_IDENTITY.getMsg());
		}

		uDto.setMobile(user.getMobile());
		uDto.setPassword(user.getPassword());
		uDto.setSalt(user.getSalt());
		uDto.setUserId(user.getUserId());

		return ResultGenerator.genSuccessResult("success",uDto);
	}

	/**
	 * 校验用户的手机号
	 *
	 * @param mobileNumberParam
	 * @return
	 */
	public BaseResult<String> validateUserMobile(String mobileNumber) {
		if (!RegexUtil.checkMobile(mobileNumber)) {
			return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
		}

        UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
        String appCodeNameStr = org.apache.commons.lang.StringUtils.isEmpty(userDeviceInfo.getAppCodeName())?"10":userDeviceInfo.getAppCodeName();
        User user = userMapper.queryUserByMobileAndAppCdde(mobileNumber,appCodeNameStr);
		if (null == user) {
			return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
		}

		return ResultGenerator.genSuccessResult("用户手机号校验成功");
	}

		/**
		 * 更新用户登录密码
		 *
		 * @param mobileNumberParam
		 */
		public BaseResult<String> updateUserLoginPass(String userLoginPass, String mobileNumber, String smsCode) {
			if (!userLoginPass.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$")) {
				return ResultGenerator.genResult(MemberEnums.PASS_FORMAT_ERROR.getcode(), MemberEnums.PASS_FORMAT_ERROR.getMsg());
			}

			UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
			String appCodeName = org.apache.commons.lang.StringUtils.isEmpty(userDeviceInfo.getAppCodeName())?"10":userDeviceInfo.getAppCodeName();
			User user = userMapper.queryUserByMobileAndAppCdde(mobileNumber,appCodeName);
			if (null == user) {
				return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
			}

			String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.SMS_TYPE_RESETPASS + "_" + mobileNumber);
			if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(smsCode)) {
				return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
			}

			User updateUser = new User();
			updateUser.setUserId(user.getUserId());
			updateUser.setPassword(Encryption.encryption(userLoginPass, user.getSalt()));
			this.update(updateUser);

			stringRedisTemplate.opsForValue().set(ProjectConstant.SMS_PREFIX + ProjectConstant.SMS_TYPE_RESETPASS + "_" + mobileNumber, "");
			return ResultGenerator.genSuccessResult("更新用户登录密码成功");
		}

		/**
		 * 设置密码
		 *
		 * @param userLoginPass
		 * @param mobileNumber
		 * @param smsCode
		 * @return
		 */
		public BaseResult<String> setUserLoginPass(SetLoginPassParam param, Integer userId) {
			if (param.getType() == 1 && StringUtils.isBlank(param.getOldLoginPass())) {
				return ResultGenerator.genResult(MemberEnums.NO_OLD_LOGIN_PASS_ERROR.getcode(), MemberEnums.NO_OLD_LOGIN_PASS_ERROR.getMsg());
			}
			String userLoginPass = param.getUserLoginPass();
			if (StringUtils.isBlank(userLoginPass) || !userLoginPass.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$")) {
				return ResultGenerator.genResult(MemberEnums.PASS_FORMAT_ERROR.getcode(), MemberEnums.PASS_FORMAT_ERROR.getMsg());
			}
			User user = this.findById(userId);
			if (null == user) {
				return ResultGenerator.genResult(MemberEnums.USER_NOT_FOUND_ERROR.getcode(), MemberEnums.USER_NOT_FOUND_ERROR.getMsg());
			}
			if (param.getType() == 1) {
				String oldPass = Encryption.encryption(param.getOldLoginPass(), user.getSalt());
				if (!oldPass.equals(user.getPassword())) {
					return ResultGenerator.genResult(MemberEnums.ERR_OLD_LOGIN_PASS_ERROR.getcode(), MemberEnums.ERR_OLD_LOGIN_PASS_ERROR.getMsg());
				}
			}
			User updateUser = new User();
			updateUser.setUserId(user.getUserId());
			updateUser.setPassword(Encryption.encryption(userLoginPass, user.getSalt()));
			this.update(updateUser);

			return ResultGenerator.genSuccessResult("用户登录密码设置成功");
		}

		/***
		 * 获取真实的UserDTO
		 *
		 * @param params
		 * @return
		 */
		public UserDTO queryUserInfoReal(UserIdRealParam params) {
			Integer userId = params.getUserId();
			User user = this.findById(userId);
			UserDTO userDTO = new UserDTO();
			try {
				BeanUtils.copyProperties(userDTO, user);
				String mobile = user.getMobile();
				userDTO.setMobile(mobile);
				userDTO.setUserMoney(String.valueOf(user.getUserMoney()));
				userDTO.setUserMoneyLimit(String.valueOf(user.getUserMoneyLimit()));
				BigDecimal totalMoney = user.getUserMoney().add(user.getUserMoneyLimit());
				userDTO.setTotalMoney(String.valueOf(totalMoney));
				userDTO.setIsSuperWhite(user.getIsSuperWhite() == null?"0":user.getIsSuperWhite());
			} catch (Exception e) {
				throw new ServiceException(RespStatusEnum.SERVER_ERROR.getCode(), RespStatusEnum.SERVER_ERROR.getMsg());
			}
			return userDTO;
		}

		public UserDTO queryUserInfo(UserIdParam params) {
			Integer userId = params.getUserId();
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
				userDTO.setIsSuperWhite(user.getIsSuperWhite() == null?"0":user.getIsSuperWhite());
				userDTO.setTotalMoney(String.valueOf(totalMoney));
				userDTO.setMerchantNo(user.getMerchantNo());
				userDTO.setMerchantPass(user.getMerchantPass());
			} catch (Exception e) {
				throw new ServiceException(RespStatusEnum.SERVER_ERROR.getCode(), RespStatusEnum.SERVER_ERROR.getMsg());
			}
			return userDTO;
		}

		/**
		 * 查询用户信息：所有字段
		 *
		 * @return
		 */
		public UserDTO queryUserInfo() {
			Integer userId = SessionUtil.getUserId();
			UserIdParam userIdParams = new UserIdParam();
			userIdParams.setUserId(userId);
			return queryUserInfo(userIdParams);
		}

		/**
		 * 生成昵称：
		 *
		 * @param mobile
		 * @return
		 */
		public String generateNickName(String mobile) {
			if (StringUtils.isEmpty(mobile)) {
				return "****彩主";
			}
			String userName = String.format("%s彩主", mobile.substring(mobile.length() - 4, mobile.length()));
			return userName.toString();
		}

		public void saveUserAndUpdateConsumer(User user) {
			this.update(user);
			Condition condition = new Condition(DlChannelConsumer.class);
			condition.createCriteria().andCondition("user_id = ", user.getUserId());
			List<DlChannelConsumer> channelConsumerlist = channelConsumerService.findByCondition(condition);
			if (channelConsumerlist.size() > 0) {
				if (channelConsumerlist.get(0).getFristLoginTime() == null) {
					channelConsumerService.updateByUserId(user.getUserId());
				}
			}
		}

		/**
		 * 生成账号： 1.随机生成4位字母 2.生成用户名 3.查询重复的用户名条数 4.如果有重复用户名，则重新生成
		 *
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

		/**
		 * 获取用户通知信息
		 *
		 * @return
		 */
		public UserNoticeDTO queryUserNotice(Integer userId) {
			UserNoticeDTO dto = new UserNoticeDTO();
			int bonusNum = userBonusMapper.getUnReadBonusNum(userId);
			dto.setBonusNotice(bonusNum);
			int messageNum = messageMapper.getUnReadMessageNum(userId);
			dto.setMessageNotice(messageNum);
			return dto;
		}

		/**
		 * 标识已读通知
		 *
		 * @param userId
		 * @param type
		 */
		public int updateUnReadNotice(Integer userId, int type) {
			int rst = 0;
			if (type == 1) {
				rst = userBonusMapper.updateUnReadBonus(userId);
			} else if (type == 2) {
				rst = messageMapper.updateUnReadMessage(userId);
			}
			return rst;
		}

		public List<String> getClientIds(List<Integer> userIds) {
			return userMapper.getClientIds(userIds);
		}

		public Integer updateUserInfo(User user) {
			return userMapper.updateUserInfo(user);
		}

		public User findByMobile(String mobile) {
			Condition condition = new Condition(User.class);
			condition.createCriteria().andCondition("mobile = ", mobile);
			List<User> userList = userMapper.selectByCondition(condition);
			if (userList.size() > 0) {
				return userList.get(0);
			} else {
				return null;
			}
		}

		public User findByMobileAndAppCode(FindUserByMobileAndAppCodeParam userFindParam) {
			Condition condition = new Condition(User.class);

			Criteria criteria = condition.createCriteria();
			criteria.andCondition("mobile =", userFindParam.getMobile());
			criteria.andCondition("app_code_name = ", userFindParam.getAppCodeName());
			List<User> userList = userMapper.selectByCondition(condition);
			if (userList.size() > 0) {
				return userList.get(0);
			} else {
				return null;
			}
		}
	}
