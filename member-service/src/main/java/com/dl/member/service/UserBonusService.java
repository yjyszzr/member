package com.dl.member.service;
import com.dl.member.model.UserBonus;
import com.dl.member.param.UserBonusParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserBonusMapper;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class UserBonusService extends AbstractService<UserBonus> {
    @Resource
    private UserBonusMapper userBonusMapper;
    
    @Resource
    private UserBonusShowDescService userBonusShowDescService;
    
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
			BeanUtils.copyProperties(userBonusDTO, userBonus);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		userBonusDTO.setUseRange(userBonusShowDescService.getUseRange(userBonus.getUseRange()));
		userBonusDTO.setBonusStatus(String.valueOf(userBonus.getBonusStatus()));
		userBonusDTO.setBonusPrice(userBonus.getBonusPrice());
		userBonusDTO.setLimitTime(userBonusShowDescService.getLimitTimeDesc(userBonus.getStartTime(),userBonus.getEndTime()));
		userBonusDTO.setMinGoodsAmount(userBonusShowDescService.getLimitOrderAmountDesc(userBonus.getMinGoodsAmount(),userBonus.getBonusPrice()));
		return userBonusDTO;
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

}
