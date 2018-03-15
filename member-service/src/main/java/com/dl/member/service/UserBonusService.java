package com.dl.member.service;
import com.dl.member.model.UserBonus;
import com.dl.member.param.UserBonusParam;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserBonusMapper;
import com.dl.member.enums.MemberEnums;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
public class UserBonusService extends AbstractService<UserBonus> {
    @Resource
    private UserBonusMapper userBonusMapper;
    
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
	public void updateUserBonusStatusUsed(Integer userBonusId, String sn) {
		if(userBonusId == null || StringUtils.isEmpty(sn)) {
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
		criteria.andCondition("is_delete =" + ProjectConstant.IS_DELETE);
		List<UserBonus> userBonusList = this.findByCondition(cUsed);
		if (userBonusList.size() > 0) {
			throw new ServiceException(MemberEnums.BONUS_USED.getcode(),
					"用户红包编号为" + userBonusId + MemberEnums.BONUS_USED.getMsg());
		}

		UserBonus usedUserBonus = new UserBonus();
		usedUserBonus.setUserBonusId(userBonusId);
		usedUserBonus.setUsedTime(DateUtil.getCurrentTimeLong());
		usedUserBonus.setOrderSn(sn);
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
			criteria.andCondition("is_delete =", ProjectConstant.IS_DELETE);
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

}
