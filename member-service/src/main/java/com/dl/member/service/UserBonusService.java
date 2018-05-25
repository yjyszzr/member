package com.dl.member.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserBonusMapper;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.ActivityBonus;
import com.dl.member.model.UserBonus;
import com.dl.member.param.BonusLimitConditionParam;
import com.dl.member.param.UserBonusParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

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
    
    
	/**
	 * 下单时的账户变动：目前仅红包置为已使用 
	 * @param userBonusParam
	 * @return
	 */
	public BaseResult<String> changeUserAccountByCreateOrder(UserBonusParam userBonusParam) {
		this.updateUserBonusStatusUsed(userBonusParam.getUserBonusId(),userBonusParam.getOrderSn());
		return ResultGenerator.genSuccessResult("更新红包状态成功");
	}
	
	
	/**
	 * 回滚下单时的账户变动：目前仅红包置为未使用
	 * @param userBonusParam
	 * @return
	 */
	public BaseResult<String> rollbackChangeUserAccountByCreateOrder(UserBonusParam userBonusParam){
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
		if(userBonusId == null || StringUtils.isEmpty(orderSn)) {
			throw new ServiceException(MemberEnums.PARAMS_NOT_NULL.getcode(),MemberEnums.PARAMS_NOT_NULL.getMsg());
		}
		
		UserBonus userBonus = this.findById(userBonusId);
		if (userBonus == null) {
			throw new ServiceException(MemberEnums.BONUS_UNEXITS.getcode(),
					"用户红包编号为" + userBonusId + MemberEnums.BONUS_UNEXITS.getMsg());
		}
		Condition cUsed = new Condition(UserBonus.class);
		Criteria criteria = cUsed.createCriteria();
		criteria.andCondition("user_bonus_id =", userBonusId);
		criteria.andCondition("bonus_status =", ProjectConstant.BONUS_STATUS_USED);
		criteria.andCondition("is_delete =" + ProjectConstant.NOT_DELETE);
		List<UserBonus> userBonusList = this.findByCondition(cUsed);
		if (userBonusList.size() > 0) {
			throw new ServiceException(MemberEnums.BONUS_USED.getcode(),
					"用户红包编号为" + userBonusId + MemberEnums.BONUS_USED.getMsg());
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
				throw new ServiceException(MemberEnums.BONUS_UNEXITS.getcode(),
						"用户红包编号为" + userBonusId + MemberEnums.BONUS_UNEXITS.getMsg());
			}
			Condition cUsed = new Condition(UserBonus.class);
			Criteria criteria = cUsed.createCriteria();
			criteria.andCondition("user_bonus_id =", userBonusId);
			criteria.andCondition("bonus_status =", ProjectConstant.BONUS_STATUS_UNUSED);
			criteria.andCondition("is_delete =", ProjectConstant.NOT_DELETE);
			List<UserBonus> userBonusList = this.findByCondition(cUsed);
			if (userBonusList.size() > 0) {
				throw new ServiceException(MemberEnums.BONUS_UNUSED.getcode(),
						"用户红包编号为" + userBonusId + MemberEnums.BONUS_UNUSED.getMsg());
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
	 * @return
	 */
	public List<UserBonusDTO> queryValidBonusListForPay(BonusLimitConditionParam bonusLimitConditionParam){
		Integer userId = SessionUtil.getUserId();
		UserBonus userBonus = new UserBonus();
		userBonus.setUserId(userId);
		userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
		userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
		userBonus.setStartTime(DateUtil.getCurrentTimeLong());
		userBonus.setEndTime(DateUtil.getCurrentTimeLong());
		userBonus.setMinGoodsAmount(bonusLimitConditionParam.getOrderMoneyPaid());
		List<UserBonus> userBonusList = userBonusMapper.queryUserBonusBySelective(userBonus);
		List<UserBonusDTO> userBonusDTOList = new ArrayList<UserBonusDTO>();
		if(CollectionUtils.isEmpty(userBonusList)) {
			return userBonusDTOList;
		}
		
		//userBonusList.sort(Comparator.comparing(UserBonus::getBonusPrice).thenComparing(UserBonus::getMinGoodsAmount).thenComparing(UserBonus::getEndTime));
		
		userBonusList.forEach(s->{
			UserBonusDTO userBonusDTO = this.createReturnUserBonusDTO(s);
			userBonusDTOList.add(userBonusDTO);
		});
		return userBonusDTOList;		
		
	}
	
//	/**
//	 * 支付红包排序
//	 * @param lhs
//	 * @param rhs
//	 * @return
//	 */
//	public static int compareByConditions(UserBonus s1, UserBonus s2) {
//	    if (s1.getBonusPrice().compareTo(s2.getBonusPrice()) == 0) {
//	        return s1.getMinGoodsAmount().subtract(s2.getMinGoodsAmount()).intValue();
//	    } else {
//	        return s1.getBonusPrice().compareTo(s2.getBonusPrice());
//	    }
//	}
	
	/**
	 * 根据状态查询有效的红包集合 ""-全部   0-未使用 1-已使用 2-已过期
	 * @param status
	 * @return
	 */
	public PageInfo<UserBonusDTO> queryBonusListByStatus(String status,Integer pageNum,Integer pageSize) {
		Integer userId = SessionUtil.getUserId();
		UserBonus userBonus = new UserBonus();
		userBonus.setUserId(userId);
		userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
		if(!StringUtils.isEmpty(status)) {
			userBonus.setBonusStatus(Integer.valueOf(status));
		}
		PageHelper.startPage(pageNum, pageSize);
		List<UserBonus> userBonusList = userBonusMapper.queryUserBonusBySelective(userBonus);
		PageInfo<UserBonus> pageInfo = new PageInfo<UserBonus>(userBonusList);
		
		List<UserBonusDTO> userBonusDTOList = new ArrayList<UserBonusDTO>();
		userBonusList.forEach(s->{
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
	 * @param shopBonusDTOList
	 * @param userBonusList
	 * @return
	 */
	public UserBonusDTO createReturnUserBonusDTO(UserBonus userBonus){
		UserBonusDTO userBonusDTO = new UserBonusDTO();
		try {
			BeanUtils.copyProperties(userBonus,userBonusDTO);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		if(ProjectConstant.BONUS_STATUS_UNUSED == userBonus.getBonusStatus()) {
			Integer currentTime = DateUtil.getCurrentTimeLong();
			userBonusDTO.setSoonExprireBz(this.createSoonExprireBz(currentTime, userBonus));
			userBonusDTO.setLeaveTime(this.createLeaveTime(currentTime, userBonus));
		}else {
			userBonusDTO.setSoonExprireBz("");
			userBonusDTO.setLeaveTime("");
		}
		userBonusDTO.setUseRange(userBonusShowDescService.getUseRange(userBonus.getUseRange()));
		userBonusDTO.setBonusStatus(String.valueOf(userBonus.getBonusStatus()));
		userBonusDTO.setBonusPrice(userBonus.getBonusPrice());
		userBonusDTO.setLimitTime(userBonusShowDescService.getLimitTimeDesc(userBonus.getStartTime(),userBonus.getEndTime()));
		userBonusDTO.setMinGoodsAmount(userBonusShowDescService.getLimitOrderAmountDesc(userBonus.getMinGoodsAmount(),userBonus.getBonusPrice()));
		return userBonusDTO;
	}

	public static String createSoonExprireBz(Integer currentTime,UserBonus userBonus) {
		if(currentTime - userBonus.getEndTime() <= ProjectConstant.OneDaySecond &&
		   userBonus.getEndTime() - currentTime > 0) {
			return ProjectConstant.BONUS_SOONEXPIREBZ_NOTHIDE;
		}
		if(currentTime - userBonus.getStartTime() < 0) {//未生效
			return ProjectConstant.BONUS_NOWORK;
		}
		return "";
	}
	
	/**
	 * 未使用且已生效的才展示leaveTime
	 * @param currentTime
	 * @param bonusEndTime
	 * @return
	 */
	public static String createLeaveTime(Integer currentTime,UserBonus userBonus) {
		if(currentTime > userBonus.getStartTime() && currentTime < userBonus.getEndTime()) {
			Integer leaveTime = userBonus.getEndTime() - currentTime;
			Integer leaveDays = leaveTime / ProjectConstant.OneDaySecond;
			Integer yu = leaveTime % ProjectConstant.OneDaySecond;
			if(leaveDays >= 1) {
				if(yu > 0) {
					leaveDays = leaveDays + 1;
				}
				return "剩余"+leaveDays+"天过期";
			}else {
				Integer leaveHours = leaveTime / 3600;
				return "剩余"+leaveHours+"小时过期";
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
		Integer[] userBonusIdArr = new Integer[] {userBonusId};
		
		List<UserBonus> userBonusList = userBonusMapper.queryUserBonusList(userBonusIdArr,userId,curTime);
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
	 * @return
	 */
	@Transactional
	public Boolean receiveUserBonus(Integer type,Integer userId) {
		if(null == userId) {
			return false;
		}
		List<ActivityBonus> activityBonusList = new ArrayList<ActivityBonus>();
		if(ProjectConstant.REGISTER.equals(type)) {
			activityBonusList = activityBonusService.queryActivityBonusList(type);
		}
		if(activityBonusList.size() == 0) {
			return false;
		}
		
		Integer now = DateUtil.getCurrentTimeLong();
		Date currentTime = new Date();
		List<UserBonus> userBonusLisgt  = new ArrayList<UserBonus>();
		activityBonusList.stream().forEach(s->{
			UserBonus userBonus = new UserBonus();
			userBonus.setBonusId(s.getBonusId());
			userBonus.setUserId(userId);
			userBonus.setBonusSn(SNGenerator.nextSN(SNBusinessCodeEnum.BONUS_SN.getCode()));
			userBonus.setBonusPrice(s.getBonusAmount());
			userBonus.setReceiveTime(DateUtil.getCurrentTimeLong());
			userBonus.setStartTime(DateUtil.getTimeAfterDays(currentTime, s.getStartTime(),0,0,0));
			userBonus.setEndTime(DateUtil.getTimeAfterDays(currentTime,s.getEndTime(),23,59,59));
			userBonus.setBonusStatus(ProjectConstant.BONUS_STATUS_UNUSED);
			userBonus.setIsDelete(ProjectConstant.NOT_DELETE);
			userBonus.setUseRange(ProjectConstant.BONUS_USE_RANGE_ALL);
			userBonus.setMinGoodsAmount(s.getMinGoodsAmount());
			userBonusLisgt.add(userBonus);
		});
		
		try {
			int rst = userBonusMapper.insertBatchUserBonus(userBonusLisgt);
			if(rst != userBonusLisgt.size()) {
				throw new ServiceException(MemberEnums.COMMON_ERROR.getcode(),"用户"+userId+"领取红包异常,已回滚");
			}
		} catch (Exception e) {
			log.error("用户"+userId+"领取红包异常,已回滚");
		}
		return true;
	}
	
	 /**
	  * 更新红包为已过期
	  * @param userBonusIdList
	  */
	 @Transactional
	 public int updateBonusExpire(List<Integer> userBonusIdList) {
		 int rst = userBonusMapper.updateBatchUserBonusExpire(userBonusIdList);
		 return rst;
	 }

}
