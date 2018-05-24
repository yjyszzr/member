package com.dl.member.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.RandomUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.DlChannelConsumerMapper;
import com.dl.member.dto.IncomeDetailsDTO;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.UserAccount;
import com.dl.member.param.UserReceiveLotteryAwardParam;
import com.dl.member.param.UserRegisterParam;

@Service
@Transactional
public class DlChannelConsumerService extends AbstractService<DlChannelConsumer> {
	@Resource
	private DlChannelConsumerMapper dlChannelConsumerMapper;
	@Resource
	private UserAccountService userAccountService;
	@Resource
	private UserRegisterService userRegisterService;
	@Resource
	private UserBonusService userBonusService;
	@Resource
	private UserService userService;

	public List<DlChannelConsumer> selectByChannelDistributorId(Integer channelDistributorId) {
		Condition condition = new Condition(DlChannelConsumer.class);
		condition.createCriteria().andCondition("channel_distributor_id = ", channelDistributorId);
		return dlChannelConsumerMapper.selectByCondition(condition);
	}

	public DlChannelConsumer selectByChannelDistributorIdAndMobile(Integer channelDistributorId, String mobile) {
		Condition condition = new Condition(DlChannelConsumer.class);
		condition.createCriteria().andCondition("channel_distributor_id = ", channelDistributorId).andCondition("mobile = ", mobile);
		List<DlChannelConsumer> channelConsumers = dlChannelConsumerMapper.selectByCondition(condition);
		if (channelConsumers.size() > 0) {
			return channelConsumers.get(0);
		}
		return null;
	}

	public List<IncomeDetailsDTO> getIncomeDetailsList(Integer userId, String addTime, Double rate) {
		List<IncomeDetailsDTO> incomeDetailsList = dlChannelConsumerMapper.getChannelConsumerList(addTime, userId);
		List<UserAccount> userAccountList = userAccountService.findByProcessType(3);
		BigDecimal bd = new BigDecimal(rate);
		for (int i = 0; i < incomeDetailsList.size(); i++) {
			// 该list也就是注册人数
			if (incomeDetailsList.get(i).getFristLoginTime() != null) {
				incomeDetailsList.get(i).setIncome(1.0);
			}
			IncomeDetailsDTO incomeDetails = new IncomeDetailsDTO();
			// 该list是拿用户Id跟用户流水做对比查出消费列表
			for (int j = 0; j < userAccountList.size(); j++) {
				if (incomeDetailsList.get(i).getUserId().equals(userAccountList.get(j).getUserId())) {
					incomeDetails.setAddTime(DateUtil.getCurrentTimeString(Long.valueOf(userAccountList.get(j).getAddTime()), DateUtil.datetimeFormat));
					incomeDetails.setIncome(0 - userAccountList.get(j).getAmount().multiply(bd).doubleValue());
					incomeDetails.setLotteryAmount(0 - userAccountList.get(j).getAmount().doubleValue());
					incomeDetails.setMobile(incomeDetailsList.get(i).getMobile());
					incomeDetails.setUserId(userId);
					incomeDetailsList.add(incomeDetails);
				}
			}
		}
		return incomeDetailsList;
	}

	/**
	 * 1.需要把用户保存到用户表里 2.需要把该用户的ID回填到consumer表里 3.需要给该用户派发优惠券
	 * 
	 * @param request
	 * @param userReceiveLotteryAwardParam
	 */
	public BaseResult<Integer> receiveLotteryAward(UserReceiveLotteryAwardParam userReceiveLotteryAwardParam, HttpServletRequest request) {
		UserRegisterParam userRegisterParam = new UserRegisterParam();
		userRegisterParam.setLoginSource(userReceiveLotteryAwardParam.getLoginSource());
		userRegisterParam.setMobile(userReceiveLotteryAwardParam.getMobile());
		userRegisterParam.setPassWord(RandomUtil.getRandNum(6) + "i");
		BaseResult<Integer> regRst = userRegisterService.registerUser(userRegisterParam, request);
		if (regRst.getCode() != 0) {
			return ResultGenerator.genResult(regRst.getCode(), regRst.getMsg());
		}
		Integer userId = regRst.getData();
		dlChannelConsumerMapper.updateByChannelDistributorIdAndMobile(userRegisterParam.getMobile(), userId, userReceiveLotteryAwardParam.getChannelDistributorId());
		userBonusService.receiveUserBonus(ProjectConstant.REGISTER, userId);
		return ResultGenerator.genSuccessResult("领取成功");
	}

	public void updateByUserId(Integer userId) {
		dlChannelConsumerMapper.updateBuUserId(userId, DateUtil.getCurrentTimeLong());
	}
}
