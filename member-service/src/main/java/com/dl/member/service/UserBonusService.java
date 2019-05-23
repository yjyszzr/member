package com.dl.member.service;

import com.alibaba.fastjson.JSON;
import com.dl.base.constant.CommonConstants;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.*;
import com.dl.member.dto.DonationPriceDTO;
import com.dl.member.dto.RechargeBonusLimitDTO;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.ActivityBonus;
import com.dl.member.model.DonationRechargeCard;
import com.dl.member.model.User;
import com.dl.member.model.UserBonus;
import com.dl.member.param.BonusLimitConditionParam;
import com.dl.member.param.OrderSnParam;
import com.dl.member.param.UserBonusParam;
import com.dl.member.util.BonusUtil;
import com.dl.member.util.GeTuiMessage;
import com.dl.member.util.GeTuiUtil;
import com.dl.order.api.IOrderService;
import com.dl.order.dto.OrderDTO;
import com.dl.shop.payment.api.IpaymentService;
import com.dl.shop.payment.dto.PayLogDTO;
import com.dl.shop.payment.dto.PriceDTO;
import com.dl.shop.payment.dto.YesOrNoDTO;
import com.dl.shop.payment.param.PayLogIdParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserBonusService extends AbstractService<UserBonus> {
	@Resource
	private UserBonusMapper userBonusMapper;

	@Resource
	private UserBonusShowDescService userBonusShowDescService;

	@Resource
	private ActivityBonusService activityBonusService;

	@Resource
	private UserMapper userMapper;
	
	@Resource
	private IpaymentService payMentService;
	
    @Resource
    private DLActivityMapper dLActivityMapper;
    
    @Resource
    private ActivityBonusMapper  activityBonusMapper;
    
    @Resource
    private DLActivityService activityService;

	@Resource
	private GeTuiUtil geTuiUtil;
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	@Resource
	private DonationRechargeCardMapper donationRechargeCardMapper;

	@Resource
	private IOrderService iOrderService;

	/**
	 * 下单时的账户变动：目前仅红包置为已使用
	 * 
	 * @param userBonusParam
	 * @return
	 */
	public BaseResult<String> changeUserAccountByCreateOrder(UserBonusParam userBonusParam) {
		this.updateUserBonusStatusUsed(userBonusParam.getUserBonusId(), userBonusParam.getOrderSn());
		return ResultGenerator.genSuccessResult("更新红包状态成功");
	}

	/**
	 * 回滚下单时的账户变动：目前仅红包置为未使用
	 * 
	 * @param userBonusParam
	 * @return
	 */
	public BaseResult<String> rollbackChangeUserAccountByCreateOrder(UserBonusParam userBonusParam) {
		this.updateUserBonusStatusUnused(userBonusParam.getUserBonusId());
		return ResultGenerator.genSuccessResult("回滚红包状态成功");
	}

	/**
	 * 更新红包状态为已使用
	 *
	 * @param userBonusParam
	 * @return
	 */
	@Transactional
	public void updateUserBonusStatusUsed(Integer userBonusId, String orderSn) {
		if (userBonusId == null || StringUtils.isEmpty(orderSn)) {
			throw new ServiceException(MemberEnums.PARAMS_NOT_NULL.getcode(), MemberEnums.PARAMS_NOT_NULL.getMsg());
		}

		UserBonus userBonus = this.findById(userBonusId);
		if (userBonus == null) {
			throw new ServiceException(MemberEnums.BONUS_UNEXITS.getcode(), "用户红包编号为" + userBonusId + MemberEnums.BONUS_UNEXITS.getMsg());
		}
		Condition cUsed = new Condition(UserBonus.class);
		Criteria criteria = cUsed.createCriteria();
		criteria.andCondition("user_bonus_id =", userBonusId);
		criteria.andCondition("bonus_status =", ProjectConstant.BONUS_STATUS_USED);
		criteria.andCondition("is_delete =" + ProjectConstant.NOT_DELETE);
		List<UserBonus> userBonusList = this.findByCondition(cUsed);
		if (userBonusList.size() > 0) {
			throw new ServiceException(MemberEnums.BONUS_USED.getcode(), "用户红包编号为" + userBonusId + MemberEnums.BONUS_USED.getMsg());
		}

		UserBonus usedUserBonus = new UserBonus();
		usedUserBonus.setUserBonusId(userBonus.getUserBonusId());
		usedUserBonus.setUsedTime(DateUtil.getCurrentTimeLong());
		usedUserBonus.setOrderSn(orderSn);
		usedUserBonus.setUserId(SessionUtil.getUserId());
		usedUserBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_USED);
		this.update(usedUserBonus);
	}

	/**
	 * 更新红包状态为未使用
	 *
	 * @param userBonusParam
	 * @return
	 */
	@Transactional
	public void updateUserBonusStatusUnused(Integer userBonusId) {
		UserBonus userBonus = this.findById(userBonusId);
		if (userBonus == null) {
			throw new ServiceException(MemberEnums.BONUS_UNEXITS.getcode(), "用户红包编号为" + userBonusId + MemberEnums.BONUS_UNEXITS.getMsg());
		}
		Condition cUsed = new Condition(UserBonus.class);
		Criteria criteria = cUsed.createCriteria();
		criteria.andCondition("user_bonus_id =", userBonusId);
		criteria.andCondition("bonus_status =", ProjectConstant.BONUS_STATUS_UNUSED);
		criteria.andCondition("is_delete =", ProjectConstant.NOT_DELETE);
		List<UserBonus> userBonusList = this.findByCondition(cUsed);
		if (userBonusList.size() > 0) {
			throw new ServiceException(MemberEnums.BONUS_UNUSED.getcode(), "用户红包编号为" + userBonusId + MemberEnums.BONUS_UNUSED.getMsg());
		}

		UserBonus usedUserBonus = new UserBonus();
		usedUserBonus.setUserBonusId(userBonusId);
		usedUserBonus.setUsedTime(DateUtil.getCurrentTimeLong());
		usedUserBonus.setOrderSn("");
		usedUserBonus.setUserId(SessionUtil.getUserId());
		usedUserBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
		this.update(usedUserBonus);
	}

	/**
	 * 给支付提供查询用户可用的红包列表
	 * 
	 * @return
	 */
	public List<UserBonusDTO> queryValidBonusListForPay(BonusLimitConditionParam bonusLimitConditionParam) {
		Integer userId = SessionUtil.getUserId();
		UserBonus userBonus = new UserBonus();
		userBonus.setUserId(userId);
		userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
		userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
		userBonus.setStartTime(DateUtil.getCurrentTimeLong());
		userBonus.setEndTime(DateUtil.getCurrentTimeLong());
//		userBonus.setMinGoodsAmount(bonusLimitConditionParam.getOrderMoneyPaid());//门槛查询去掉
		List<UserBonus> userBonusList = userBonusMapper.queryUserBonusForPay(userBonus);
		List<UserBonusDTO> userBonusDTOList = new ArrayList<UserBonusDTO>();
		if (CollectionUtils.isEmpty(userBonusList)) {
			return userBonusDTOList;
		}

		userBonusList.forEach(s -> {
			UserBonusDTO userBonusDTO = this.createReturnUserBonusDTO(s,bonusLimitConditionParam.getOrderMoneyPaid());
			userBonusDTOList.add(userBonusDTO);
		});
		return userBonusDTOList;

	}


	/**
	 * 给支付提供查询用户可用的红包列表
	 *
	 * @return
	 */
	public List<UserBonusDTO> queryValidBonusListForPayV2(OrderSnParam orderSnParam,Integer userId) {
		List<UserBonusDTO> userBonusDTOList = new ArrayList<UserBonusDTO>();
		//根据订单号查询支付所用 余额
		com.dl.order.param.OrderSnParam snParam = new com.dl.order.param.OrderSnParam();
		snParam.setOrderSn(orderSnParam.getOrderSn());
		BaseResult<OrderDTO> orderDTOBaseResult = iOrderService.getOrderInfoByOrderSn(snParam);
		if(!orderDTOBaseResult.isSuccess()){
			return userBonusDTOList;
		}

		BigDecimal moneyPaid= orderDTOBaseResult.getData().getMoneyPaid();
		UserBonus userBonus = new UserBonus();
		userBonus.setUserId(userId);
		userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
		userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
		userBonus.setStartTime(DateUtil.getCurrentTimeLong());
		userBonus.setEndTime(DateUtil.getCurrentTimeLong());
		List<UserBonus> userBonusList = userBonusMapper.queryUserBonusForPay(userBonus);
		if (CollectionUtils.isEmpty(userBonusList)) {
			return userBonusDTOList;
		}

		userBonusList.forEach(s -> {
			UserBonusDTO userBonusDTO = this.createReturnUserBonusDTO(s);
			if(s.getMinGoodsAmount().compareTo(moneyPaid) < 0){ userBonusDTO.setBonusStatus("3");}
			userBonusDTOList.add(userBonusDTO);
		});
		return userBonusDTOList;

	}



	// /**
	// * 支付红包排序
	// * @param lhs
	// * @param rhs
	// * @return
	// */
	// public static int compareByConditions(UserBonus s1, UserBonus s2) {
	// if (s1.getBonusPrice().compareTo(s2.getBonusPrice()) == 0) {
	// return
	// s1.getMinGoodsAmount().subtract(s2.getMinGoodsAmount()).intValue();
	// } else {
	// return s1.getBonusPrice().compareTo(s2.getBonusPrice());
	// }
	// }

	/**
	 * 根据状态查询有效的红包集合 ""-全部 0-未使用 1-已使用 2-已过期
	 * 
	 * @param status
	 * @return
	 */
	public PageInfo<UserBonusDTO> queryBonusListByStatus(String status, Integer pageNum, Integer pageSize) {
		Integer userId = SessionUtil.getUserId();
		UserBonus userBonus = new UserBonus();
		userBonus.setUserId(userId);
		userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
		if (!StringUtils.isEmpty(status)) {
			userBonus.setBonusStatus(Integer.valueOf(status));
		}
		PageHelper.startPage(pageNum, pageSize);
		List<UserBonus> userBonusList = null;
		if (!status.equals("1")) {
			userBonusList = userBonusMapper.queryUserBonusBySelective(userBonus);
		} else {
			userBonusList = userBonusMapper.queryUserBonusForUsed(userBonus);
		}

		PageInfo<UserBonus> pageInfo = new PageInfo<UserBonus>(userBonusList);

		List<UserBonusDTO> userBonusDTOList = new ArrayList<UserBonusDTO>();
		userBonusList.forEach(s -> {
			UserBonusDTO userBonusDTO = this.createReturnUserBonusDTO(s);
			userBonusDTOList.add(userBonusDTO);
		});

		PageInfo<UserBonusDTO> result = new PageInfo<UserBonusDTO>();
		try {
			BeanUtils.copyProperties(pageInfo, result);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		result.setList(userBonusDTOList);
		return result;
	}
	/**
	 * 统一构造返回前台红包列表的数据结构
	 * @param mixMoney 当前订单金额
	 * @param shopBonusDTOList
	 * @param userBonusList
	 * @return
	 */
	public UserBonusDTO createReturnUserBonusDTO(UserBonus userBonus) {
		UserBonusDTO userBonusDTO = new UserBonusDTO();
		try {
			BeanUtils.copyProperties(userBonus, userBonusDTO);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

//		if (ProjectConstant.BONUS_STATUS_UNUSED == userBonus.getBonusStatus()) {
//			Integer currentTime = DateUtil.getCurrentTimeLong();
//			userBonusDTO.setSoonExprireBz(this.createSoonExprireBz(currentTime, userBonus));
//			userBonusDTO.setLeaveTime(this.createLeaveTime(currentTime, userBonus));
//		} else {
//			userBonusDTO.setSoonExprireBz("");
//			userBonusDTO.setLeaveTime("");
//		}
		userBonusDTO.setBonusName(userBonus.getBonusPrice()+"元代金券");
		userBonusDTO.setShortDesc("去使用");
		userBonusDTO.setBonusPriceStr(userBonus.getBonusPrice()+"元");
		userBonusDTO.setUseRange(userBonusShowDescService.getUseRange(userBonus.getUseRange()));
		userBonusDTO.setBonusStatus(String.valueOf(userBonus.getBonusStatus()));
		userBonusDTO.setBonusPrice(userBonus.getBonusPrice());
		userBonusDTO.setLimitTime(userBonusShowDescService.getLimitTimeDesc(userBonus.getStartTime(), userBonus.getEndTime()));
		userBonusDTO.setBonusEndTime(DateUtil.getCurrentTimeString(Long.valueOf(userBonus.getEndTime()), DateUtil.date_sdf));
		userBonusDTO.setMinGoodsAmount(userBonusShowDescService.getLimitOrderAmountDesc(userBonus.getMinGoodsAmount(), userBonus.getBonusPrice()));
		return userBonusDTO;
	}
	/**
	 * 统一构造返回前台红包列表的数据结构
	 * @param mixMoney 当前订单金额
	 * @param shopBonusDTOList
	 * @param userBonusList
	 * @return
	 */
	public UserBonusDTO createReturnUserBonusDTO(UserBonus userBonus,BigDecimal mixMoney) {
		UserBonusDTO userBonusDTO = new UserBonusDTO();
		try {
			BeanUtils.copyProperties(userBonus, userBonusDTO);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

//		if (ProjectConstant.BONUS_STATUS_UNUSED == userBonus.getBonusStatus()) {
//			Integer currentTime = DateUtil.getCurrentTimeLong();
//			userBonusDTO.setSoonExprireBz(this.createSoonExprireBz(currentTime, userBonus));
//			userBonusDTO.setLeaveTime(this.createLeaveTime(currentTime, userBonus));
//		} else {
//			userBonusDTO.setSoonExprireBz("");
//			userBonusDTO.setLeaveTime("");
//		}
		//判断红包门槛是否适配当前订单
		if(mixMoney.subtract(userBonus.getMinGoodsAmount()).doubleValue()>=0) {//达到使用门槛
			userBonusDTO.setThisStatus("0");
		}else {
			userBonusDTO.setThisStatus("1");
		}
		userBonusDTO.setMinAmount(userBonus.getMinGoodsAmount());
		userBonusDTO.setBonusName(userBonus.getBonusPrice()+"元代金券");
		userBonusDTO.setShortDesc("去使用");
		userBonusDTO.setBonusPriceStr(userBonus.getBonusPrice()+"元");
		userBonusDTO.setUseRange(userBonusShowDescService.getUseRange(userBonus.getUseRange()));
		userBonusDTO.setBonusStatus(String.valueOf(userBonus.getBonusStatus()));
		userBonusDTO.setBonusPrice(userBonus.getBonusPrice());
		userBonusDTO.setLimitTime(userBonusShowDescService.getLimitTimeDesc(userBonus.getStartTime(), userBonus.getEndTime()));
		userBonusDTO.setBonusEndTime(DateUtil.getCurrentTimeString(Long.valueOf(userBonus.getEndTime()), DateUtil.date_sdf));
		userBonusDTO.setMinGoodsAmount(userBonusShowDescService.getLimitOrderAmountDesc(userBonus.getMinGoodsAmount(), userBonus.getBonusPrice()));
		return userBonusDTO;
	}

	/**
	 * 快过期和未生效标签
	 * 
	 * @param currentTime
	 * @param userBonus
	 * @return
	 */
	public static String createSoonExprireBz(Integer currentTime, UserBonus userBonus) {
		if (userBonus.getEndTime() - currentTime <= ProjectConstant.OneDaySecond && userBonus.getEndTime() - currentTime > 0) {
			return ProjectConstant.BONUS_SOONEXPIREBZ_NOTHIDE;
		}
		if (currentTime - userBonus.getStartTime() < 0) {// 未生效
			return ProjectConstant.BONUS_NOWORK;
		}
		return "";
	}

	/**
	 * 未使用且已生效的才展示leaveTime
	 * 
	 * @param currentTime
	 * @param bonusEndTime
	 * @return
	 */
	public static String createLeaveTime(Integer currentTime, UserBonus userBonus) {
		if (currentTime > userBonus.getStartTime() && currentTime < userBonus.getEndTime()) {
			Integer leaveTime = userBonus.getEndTime() - currentTime;
			Integer leaveDays = leaveTime / ProjectConstant.OneDaySecond;
			Integer yu = leaveTime % ProjectConstant.OneDaySecond;
			if (leaveDays >= 1) {
				if (yu > 0) {
					leaveDays = leaveDays + 1;
				}
				return "剩余" + leaveDays + "天过期";
			} else {
				Integer leaveHours = leaveTime / 3600;
				return "剩余" + leaveHours + "小时过期";
			}
		}
		return "";
	}

	/**
	 * 查询单个红包的数据
	 *
	 * @param userBonusIds
	 * @return
	 */
	public UserBonusDTO queryUserBonus(Integer userBonusId) {
		Integer curTime = DateUtil.getCurrentTimeLong();
		Integer userId = SessionUtil.getUserId();
		Integer[] userBonusIdArr = new Integer[] { userBonusId };

		List<UserBonus> userBonusList = userBonusMapper.queryUserBonusList(userBonusIdArr, userId, curTime);
		if (userBonusList.size() == 0) {
			return null;
		}

		UserBonus userBonus = userBonusList.get(0);
		UserBonusDTO userBonusDTO = new UserBonusDTO();
		userBonusDTO.setUserBonusId(userBonus.getUserBonusId());
		userBonusDTO.setBonusPrice(userBonus.getBonusPrice());
		return userBonusDTO;
	}

	
	
	/**
	 * 领取红包
	 * 
	 * @return
	 */
	@Transactional
	public Boolean receiveUserBonus(Integer type, Integer userId) {
		if (null == userId) {
			return false;
		}
		List<ActivityBonus> activityBonusList = new ArrayList<ActivityBonus>();
		activityBonusList = activityBonusService.queryActivityBonusList(type);
		if (activityBonusList.size() == 0) {
			return false;
		}

		Integer now = DateUtil.getCurrentTimeLong();
		Date currentTime = new Date();
		List<UserBonus> userBonusLisgt = new ArrayList<UserBonus>();
		activityBonusList.stream().forEach(s -> {
			UserBonus userBonus = new UserBonus();
			userBonus.setBonusId(s.getBonusId());
			userBonus.setUserId(userId);
			userBonus.setBonusSn(SNGenerator.nextSN(SNBusinessCodeEnum.BONUS_SN.getCode()));
			userBonus.setBonusPrice(s.getBonusAmount());
			userBonus.setAddTime(now);
			userBonus.setReceiveTime(now);
			userBonus.setStartTime(DateUtil.getTimeAfterDays(currentTime, s.getStartTime(), 0, 0, 0));
			userBonus.setEndTime(DateUtil.getTimeAfterDays(currentTime, s.getEndTime(), 23, 59, 59));
			userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
			userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
			userBonus.setUseRange(ProjectConstant.BONUS_USE_RANGE_ALL);
			userBonus.setMinGoodsAmount(s.getMinGoodsAmount());
			userBonusLisgt.add(userBonus);
		});

		try {
			int rst = userBonusMapper.insertBatchUserBonus(userBonusLisgt);
			if (rst != userBonusLisgt.size()) {
				throw new ServiceException(MemberEnums.COMMON_ERROR.getcode(), "用户" + userId + "领取红包异常,已回滚");
			}
		} catch (Exception e) {
			log.error("用户" + userId + "领取红包异常,已回滚");
		}
		return true;
	}

	/**
	 * 领取充值赠送的随机数额的红包
	 * 
	 * @return
	 */
	public BaseResult<DonationPriceDTO> receiveRechargeUserBonusStr(Integer payLogId) {
		PayLogIdParam payLogIdParam = new PayLogIdParam();
		payLogIdParam.setPayLogId(payLogId);
		BaseResult<PriceDTO> priceRst = payMentService.queryMoneyInRedis(payLogIdParam);
		DonationPriceDTO donationPriceDTO = new DonationPriceDTO();
		if(priceRst.getCode() == 0) {
			donationPriceDTO.setDonationPrice(priceRst.getData().getPrice());
		}
        
		return ResultGenerator.genSuccessResult("success", donationPriceDTO);
	}
	
	
	
	/**
	 * 领取充值赠送的随机数额的红包
	 * 
	 * @return
	 */
	public BaseResult<DonationPriceDTO> receiveRechargeUserBonus(Integer payLogId) {
		//过期的充值活动不能领取该活动的红包
		Integer now = DateUtil.getCurrentTimeLong();
		Integer countRst = dLActivityMapper.countRechargeActivity(now);
		if(countRst == 0) {
			return ResultGenerator.genResult(MemberEnums.ACTIVITY_NOT_VALID.getcode(),MemberEnums.ACTIVITY_NOT_VALID.getMsg());
		}
		
		//已支付的的充值才能参与充值领红包
		DonationPriceDTO donationPriceDTO = new DonationPriceDTO();
		PayLogIdParam payLogIdParam = new PayLogIdParam();
		payLogIdParam.setPayLogId(payLogId);
		BaseResult<PayLogDTO> payLogDTORst = payMentService.queryPayLogByPayLogId(payLogIdParam);
		if(payLogDTORst.getCode() != 0) {
			return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(),"不能参与充值领红包活动");
		}
		if(payLogDTORst.getData().getOrderAmount().compareTo(new BigDecimal(10)) < 0) {
			return ResultGenerator.genResult(MemberEnums.RECHARGE_ACT_MIN_LIMIT.getcode(), MemberEnums.RECHARGE_ACT_MIN_LIMIT.getMsg());
		}	
		
		//已经领取的红包不能再领取
		Integer userId = payLogDTORst.getData().getUserId();
		Condition condition = new Condition(UserBonus.class);
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("user_id =", userId);
		criteria.andCondition("bonus_id =", 2);
		criteria.andCondition("pay_log_id =", payLogId);
		List<UserBonus> reiceiveRechargeBonusList = this.findByCondition(condition);
		if(reiceiveRechargeBonusList.size() > 0) {
			return ResultGenerator.genResult(MemberEnums.DATA_ALREADY_EXIT_IN_DB.getcode(),"用户已经领取过该充值红包");
		}
		
		//判断是否充过值
		com.dl.shop.payment.param.UserIdParam userIdParam = new com.dl.shop.payment.param.UserIdParam();
		userIdParam.setUserId(userId);
		BaseResult<YesOrNoDTO> yesOrNotRst = payMentService.countChargeByUserId(userIdParam);
		if(yesOrNotRst.getCode() != 0) {
			return ResultGenerator.genFailResult("判断是否充过值接口异常");
		}
		
		YesOrNoDTO yesOrNotDTO = yesOrNotRst.getData();
		log.info("判断是否充值过:"+JSON.toJSONString(yesOrNotRst.getData()));
		
		if(yesOrNotDTO.getYesOrNo().equals("0")) {//未成功充过值
			BigDecimal newUserRechargeMoney = payLogDTORst.getData().getOrderAmount();
			Date currentTime = new Date();
			List<UserBonus> uesrBonusList = new ArrayList<UserBonus>();
			if(newUserRechargeMoney.compareTo(new BigDecimal(10)) >= 0 && newUserRechargeMoney.compareTo(new BigDecimal(20)) < 0) {
				newUserRechargeMoney = new BigDecimal(10);
				List<UserBonus> userBonusListForNewUser = this.createRechargeUserBonusListForNewUser(userId,payLogId, newUserRechargeMoney.doubleValue());
				userBonusMapper.insertBatchUserBonusForRecharge(userBonusListForNewUser);
				donationPriceDTO.setDonationPrice(newUserRechargeMoney.doubleValue() + "");	
			}
			
			if(newUserRechargeMoney.compareTo(new BigDecimal(20)) >= 0 && newUserRechargeMoney.compareTo(new BigDecimal(1000)) < 0) {
				newUserRechargeMoney = new BigDecimal(20);
				List<UserBonus> userBonusListForNewUser = this.createRechargeUserBonusListForNewUser(userId,payLogId, newUserRechargeMoney.doubleValue());
				userBonusMapper.insertBatchUserBonusForRecharge(userBonusListForNewUser);
				donationPriceDTO.setDonationPrice(newUserRechargeMoney.doubleValue() + "");	
			}

			//新用户要参与非首次充的充值卡，且>=1000 的按照老用户的规则赠送
			if(newUserRechargeMoney.compareTo(new BigDecimal(1000)) >= 0 ) {
				//存储对应着各个概率的随机金额
				List<Double> randomDataList = BonusUtil.getBonusRandomData(newUserRechargeMoney.doubleValue());
				//领取的随机金额
				Double bonusPrice = RandomUtil.randomBonusPrice(randomDataList.get(0),randomDataList.get(1),randomDataList.get(2),randomDataList.get(3),randomDataList.get(4),randomDataList.get(5));
				//领取的随机金额对应的红包组成
				List<UserBonus> userBonusListRecharge = this.createRechargeUserBonusListForOldUser(userId,payLogId, bonusPrice);
				userBonusMapper.insertBatchUserBonusForRecharge(userBonusListRecharge);
				donationPriceDTO.setDonationPrice(bonusPrice+"");	
			}
		}else {//成功充过值
			BigDecimal recharegePrice = payLogDTORst.getData().getOrderAmount();
			//存储对应着各个概率的随机金额
			List<Double> randomDataList = BonusUtil.getBonusRandomData(recharegePrice.doubleValue());
			//领取的随机金额
			Double bonusPrice = RandomUtil.randomBonusPrice(randomDataList.get(0),randomDataList.get(1),randomDataList.get(2),randomDataList.get(3),randomDataList.get(4),randomDataList.get(5));
			//领取的随机金额对应的红包组成
			List<UserBonus> userBonusListRecharge = this.createRechargeUserBonusListForOldUser(userId,payLogId, bonusPrice);
			userBonusMapper.insertBatchUserBonusForRecharge(userBonusListRecharge);
			donationPriceDTO.setDonationPrice(bonusPrice+"");
		}

		return 	ResultGenerator.genSuccessResult("success", donationPriceDTO);
	}
	
	
	/**
	 * 领取充值赠送的随机数额的红包 
	 * 20180822 新增
	 * 
	 * @return
	 */
	public BaseResult<DonationPriceDTO> receiveRechargeUserBonusNew(Integer payLogId) {
		//过期的充值活动不能领取该活动的红包
		Integer now = DateUtil.getCurrentTimeLong();
		Integer countRst = dLActivityMapper.countRechargeActivity(now);
		if(countRst == 0) {
			return ResultGenerator.genResult(MemberEnums.ACTIVITY_NOT_VALID.getcode(),MemberEnums.ACTIVITY_NOT_VALID.getMsg());
		}
		
		//已支付的的充值才能参与充值领红包
		DonationPriceDTO donationPriceDTO = new DonationPriceDTO();
		PayLogIdParam payLogIdParam = new PayLogIdParam();
		payLogIdParam.setPayLogId(payLogId);
		BaseResult<PayLogDTO> payLogDTORst = payMentService.queryPayLogByPayLogId(payLogIdParam);
		if(payLogDTORst.getCode() != 0) {
			return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(),"不能参与充值领红包活动");
		}
		PayLogDTO payLogDto = payLogDTORst.getData();
		
		//已经领取的红包不能再领取
		Integer userId = payLogDto.getUserId();
		Condition condition = new Condition(UserBonus.class);
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("user_id =", userId);
		criteria.andCondition("pay_log_id =", payLogId);
		List<UserBonus> reiceiveRechargeBonusList = this.findByCondition(condition);
		if(reiceiveRechargeBonusList.size() > 0) {
			return ResultGenerator.genResult(MemberEnums.DATA_ALREADY_EXIT_IN_DB.getcode(),"用户已经领取过该充值红包");
		}
		
		List<UserBonus> userBonusList = new ArrayList<>();
		UserBonus userBonus = this.createRechargeUserBonus(payLogDto.getOrderAmount(), payLogDto.getUserId(), payLogDto.getLogId());
		if(null != userBonus) {
			userBonusList.add(userBonus);
			try {
				userBonusMapper.insertBatchUserBonusForRecharge(userBonusList);
			} catch (Exception e) {
				log.error("用户" + userId + "领取红包异常,已回滚");
				throw new ServiceException(MemberEnums.COMMON_ERROR.getcode(), "用户" + userId + "领取红包异常,已回滚");
			}	
			donationPriceDTO.setDonationPrice(String.valueOf(userBonus.getBonusPrice().intValue()));
		}
		return 	ResultGenerator.genSuccessResult("success", donationPriceDTO);
	}
	
	/**
	 * 根据后台设置的充值卡对应的红包，构建送给用户的红包
	 * @return
	 */
	public UserBonus createRechargeUserBonus(BigDecimal bonusPrice,Integer userId,Integer payLogId) {
		//查询充值卡
		List<DonationRechargeCard> rechargeCardList = donationRechargeCardMapper.queryRechargeCardList();
		Map<BigDecimal,Integer> rechargeMap = rechargeCardList.stream().collect(Collectors.toMap(DonationRechargeCard::getRealValue,DonationRechargeCard::getRechargeCardId));
		//查询充值卡对应的红包,根据概率和后台的红包构建给用户的红包
		Integer rechargeCardId = rechargeMap.get(bonusPrice);
		if(rechargeCardId != null) {
			List<ActivityBonus> activityBonusList = activityBonusMapper.queryActivityBonusListByRechargeCardId(rechargeCardId);
			double randomNum = Math.random(); 
			ActivityBonus activityFocusBonus = null;
			Double randomChance = 0.00;
			for(ActivityBonus activityBonus:activityBonusList) {
				randomChance += activityBonus.getRechargeChance().doubleValue();
				if(randomNum < randomChance) {
					activityFocusBonus = activityBonus;
					break;
				}
			}
			if(null == activityFocusBonus) {
				return null;
			}
			Integer now = DateUtil.getCurrentTimeLong();
			Date currentTime = new Date();
			UserBonus userBonus = new UserBonus();
			userBonus.setUserId(userId);
			userBonus.setBonusId(activityFocusBonus.getBonusId());
			userBonus.setBonusSn(SNGenerator.nextSN(SNBusinessCodeEnum.BONUS_SN.getCode()));
			userBonus.setBonusPrice(activityFocusBonus.getBonusAmount());
			userBonus.setAddTime(now);
			userBonus.setReceiveTime(now);
			userBonus.setStartTime(DateUtil.getTimeAfterDays(currentTime, activityFocusBonus.getStartTime(), 0, 0, 0));
			userBonus.setEndTime(DateUtil.getTimeAfterDays(currentTime, activityFocusBonus.getEndTime(), 23, 59, 59));
			userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
			userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
			userBonus.setUseRange(ProjectConstant.BONUS_USE_RANGE_ALL);
			userBonus.setMinGoodsAmount(activityFocusBonus.getMinGoodsAmount());
			userBonus.setPayLogId(payLogId);
			return userBonus;
		}
		return null;
	}
	
	/**
	 * 根据后台设置的充值卡对应的红包，构建送给用户的红包 new
	 * @return
	 */
	public BaseResult<Integer> createRechargeUserBonusNew(com.dl.member.param.PayLogIdParam payLogIdParam) {
		BigDecimal bonusPrice = payLogIdParam.getOrderAmount();
		Integer userId = payLogIdParam.getUserId();
		Integer payLogId = Integer.valueOf(payLogIdParam.getPayLogId());
		//查询充值卡
		List<DonationRechargeCard> rechargeCardList = donationRechargeCardMapper.queryRechargeCardList();
		List<UserBonus> userBonusList = new ArrayList<>();
		rechargeCardList = rechargeCardList==null?new ArrayList<DonationRechargeCard>():rechargeCardList;
		rechargeCardList.stream().forEach(dto -> {
				if(dto.getStatus()==0 && dto.getType().equals(30) && 
						dto.getLimitRechargeMoney().doubleValue() < bonusPrice.doubleValue()) {//单笔 满足充值赠送金额  且红包处于有效期
					if(dto.getRechargeCardId()!=null) {
						List<ActivityBonus> activityBonusList = activityBonusMapper.queryActivityBonusListByRechargeCardId(dto.getRechargeCardId());
						for(ActivityBonus activityBonus:activityBonusList) {
							Integer now = DateUtil.getCurrentTimeLong();
							Date currentTime = new Date();
							UserBonus userBonus = new UserBonus();
							userBonus.setUserId(userId);
							userBonus.setBonusId(activityBonus.getBonusId());
							userBonus.setBonusSn(SNGenerator.nextSN(SNBusinessCodeEnum.BONUS_SN.getCode()));
							userBonus.setBonusPrice(activityBonus.getBonusAmount());
							userBonus.setAddTime(now);
							userBonus.setReceiveTime(now);
							userBonus.setStartTime(DateUtil.getTimeAfterDays(currentTime, activityBonus.getStartTime(), 0, 0, 0));
							userBonus.setEndTime(DateUtil.getTimeAfterDays(currentTime, activityBonus.getEndTime(), 23, 59, 59));
							userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
							userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
							userBonus.setUseRange(ProjectConstant.BONUS_USE_RANGE_ALL);
							userBonus.setMinGoodsAmount(activityBonus.getMinGoodsAmount());
							userBonus.setPayLogId(payLogId);
							userBonusList.add(userBonus);
						}
					} else if(dto.getStatus()==0 && dto.getType().equals(20) && 
							dto.getLimitRechargeMoney().doubleValue() < bonusPrice.doubleValue()) {//首充  满足充值赠送金额  且红包处于有效期
						List<ActivityBonus> activityBonusList = activityBonusMapper.queryActivityBonusListByRechargeCardId(dto.getRechargeCardId());
						for(ActivityBonus activityBonus:activityBonusList) {
							UserBonus userBonus = new UserBonus();
							userBonus.setUserId(userId);
							userBonus.setBonusId(activityBonus.getBonusId());
							List<UserBonus> isBonusList = userBonusMapper.queryUserBonusForPay(userBonus);
							if(isBonusList==null || isBonusList.size()<=0) { //判断 首充是否拿过  如果数据为空则没拿过首充奖励
								Integer now = DateUtil.getCurrentTimeLong();
								Date currentTime = new Date();
								userBonus.setBonusSn(SNGenerator.nextSN(SNBusinessCodeEnum.BONUS_SN.getCode()));
								userBonus.setBonusPrice(activityBonus.getBonusAmount());
								userBonus.setAddTime(now);
								userBonus.setReceiveTime(now);
								userBonus.setStartTime(DateUtil.getTimeAfterDays(currentTime, activityBonus.getStartTime(), 0, 0, 0));
								userBonus.setEndTime(DateUtil.getTimeAfterDays(currentTime, activityBonus.getEndTime(), 23, 59, 59));
								userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
								userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
								userBonus.setUseRange(ProjectConstant.BONUS_USE_RANGE_ALL);
								userBonus.setMinGoodsAmount(activityBonus.getMinGoodsAmount());
								userBonus.setPayLogId(payLogId);
								userBonusList.add(userBonus);
							}
						}
					}
				}
			}
		);
		return ResultGenerator.genSuccessResult("success", userBonusMapper.insertBatchUserBonusForRecharge(userBonusList));
	}
	
	/**
	 * 构造充值送的红包集合
	 * @param userId
	 * @param randomBonusPrice
	 * @return
	 */
	public List<UserBonus> createRechargeUserBonusListForOldUser(Integer userId,Integer payLogId,Double randomBonusPrice) {
		List<RechargeBonusLimitDTO> recharegeBonusList = BonusUtil.getRandomBonusList(randomBonusPrice);
		Date currentTime = new Date();
		Integer now = DateUtil.getCurrentTimeLong();
		
		List<UserBonus> userBonusList = new ArrayList<UserBonus>();
		for(RechargeBonusLimitDTO rechargeBonusLimit:recharegeBonusList) {
			UserBonus userBonus = new UserBonus();
			userBonus.setUserId(userId);
			userBonus.setBonusId(2);
			userBonus.setBonusSn(SNGenerator.nextSN(SNBusinessCodeEnum.BONUS_SN.getCode()));
			userBonus.setBonusPrice(BigDecimal.valueOf(rechargeBonusLimit.getBonusPrice()));
			userBonus.setAddTime(now);
			userBonus.setReceiveTime(now);
			userBonus.setStartTime(DateUtil.getTimeAfterDays(currentTime, 0, 0, 0, 0));
			userBonus.setEndTime(DateUtil.getTimeAfterDays(currentTime, 7, 23, 59, 59));
			userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
			userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
			userBonus.setUseRange(ProjectConstant.BONUS_USE_RANGE_ALL);
			userBonus.setMinGoodsAmount(BigDecimal.ZERO);
			userBonus.setPayLogId(payLogId);
			userBonusList.add(userBonus);
		}

		return userBonusList;
	}
	
	/**
	 * 构造充值送的红包集合给新用户
	 * @param userId
	 * @param randomBonusPrice
	 * @return
	 */
	public List<UserBonus> createRechargeUserBonusListForNewUser(Integer userId,Integer payLogId,Double randomBonusPrice) {
		List<RechargeBonusLimitDTO> recharegeBonusList = new ArrayList<>();
		if(randomBonusPrice == 10) {
			RechargeBonusLimitDTO rechargeBonusLimitDTO = new RechargeBonusLimitDTO();
			rechargeBonusLimitDTO.setBonusPrice(2.0);
			recharegeBonusList.add(rechargeBonusLimitDTO);
			recharegeBonusList.add(rechargeBonusLimitDTO);
			recharegeBonusList.add(rechargeBonusLimitDTO);
			recharegeBonusList.add(rechargeBonusLimitDTO);
			recharegeBonusList.add(rechargeBonusLimitDTO);
		}else if(randomBonusPrice == 20) {
			RechargeBonusLimitDTO rechargeBonusLimitDTO = new RechargeBonusLimitDTO();
			rechargeBonusLimitDTO.setBonusPrice(4.0);
			recharegeBonusList.add(rechargeBonusLimitDTO);
			recharegeBonusList.add(rechargeBonusLimitDTO);
			recharegeBonusList.add(rechargeBonusLimitDTO);
			recharegeBonusList.add(rechargeBonusLimitDTO);
			recharegeBonusList.add(rechargeBonusLimitDTO);			
		}
		
		Date currentTime = new Date();
		Integer now = DateUtil.getCurrentTimeLong();
		
		List<UserBonus> userBonusList = new ArrayList<UserBonus>();
		for(RechargeBonusLimitDTO rechargeBonusLimit:recharegeBonusList) {
			UserBonus userBonus = new UserBonus();
			userBonus.setUserId(userId);
			userBonus.setBonusId(2);
			userBonus.setBonusSn(SNGenerator.nextSN(SNBusinessCodeEnum.BONUS_SN.getCode()));
			userBonus.setAddTime(now);
			userBonus.setBonusPrice(new BigDecimal(rechargeBonusLimit.getBonusPrice()));
			userBonus.setReceiveTime(now);
			userBonus.setStartTime(DateUtil.getTimeAfterDays(currentTime, 0, 0, 0, 0));
			userBonus.setEndTime(DateUtil.getTimeAfterDays(currentTime, 7, 23, 59, 59));
			userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
			userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
			userBonus.setUseRange(ProjectConstant.BONUS_USE_RANGE_ALL);
			userBonus.setMinGoodsAmount(BigDecimal.ZERO);
			userBonus.setPayLogId(payLogId);
			userBonusList.add(userBonus);
		}

		return userBonusList;
	}
	
	/**
	 * 更新红包为已过期
	 * 
	 * @param userBonusIdList
	 */
	public void updateBonusExpire() {
		log.info("更新过期的红包定时任务开始");
		Integer now = DateUtil.getCurrentTimeLong();
		List<Integer> userBonusIdList = userBonusMapper.queryUserBonusIdsExpire(now);
		if (CollectionUtils.isEmpty(userBonusIdList)) {
			log.info("没有过期的红包，定时任务结束");
			return;
		}

		int rst = userBonusMapper.updateBatchUserBonusExpire(userBonusIdList);
		log.info("本次更新过期的红包" + rst + "个");
		log.info("更新过期的红包的定时任务结束");
	}

	/**
	 * 根据用户id查询有效红包数量和可用红包总金额
	 */
	public UserBonusDTO getBonusByUserId(Integer userId) {
		UserBonus userBonus = userBonusMapper.getBonusByUserId(userId);
		UserBonusDTO userBonusDTO = new UserBonusDTO();
		userBonusDTO.setBonusPrice(userBonus!=null?userBonus.getBonusPrice():BigDecimal.ZERO);
		userBonusDTO.setBonusId(userBonus!=null?userBonus.getBonusId():0);//存放红包数量  字段节用
		return userBonusDTO;
	}
	
	
	/**
	 * 定时任务 针对过期红包发送push消息
	 * 
	 * @param userBonusIdList
	 */
	public void pushBonusMessage() {
		LocalDate now = LocalDate.now();
		LocalDate tomorrow = now.plusDays(1);
		LocalDate houtian = now.plusDays(2);
		Long start = LocalDateTime.of(tomorrow, LocalTime.MIN).atZone(ZoneId.systemDefault()).toEpochSecond();
		Long end = LocalDateTime.of(houtian, LocalTime.MIN).atZone(ZoneId.systemDefault()).toEpochSecond();
		List<UserBonus> queryUnableBonusList = userBonusMapper.queryPUshBonusList(start.intValue(), end.intValue());
		if (queryUnableBonusList == null) {
			return;
		}
		Set<Integer> collect = queryUnableBonusList.stream().map(item -> item.getUserId()).collect(Collectors.toSet());
		List<Integer> list = new ArrayList<Integer>(collect);
		List<User> users = userMapper.queryUserByUserIds(list);
		if (users == null) {
			return;
		}
		users.forEach(item -> {
			String clientId = item.getPushKey();
			if (StringUtils.isNotBlank(clientId)) {
				GeTuiMessage getuiMessage = new GeTuiMessage(CommonConstants.FORMAT_BONUS_TITLE, CommonConstants.FORMAT_BONUS_DESC, DateUtil.getCurrentTimeLong());
				geTuiUtil.pushMessage(clientId, getuiMessage);
			}
		});
	}
}
