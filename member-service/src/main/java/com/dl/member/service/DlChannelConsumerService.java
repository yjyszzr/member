package com.dl.member.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.DlChannelConsumerMapper;
import com.dl.member.dao.DlChannelDistributorMapper;
import com.dl.member.dto.IncomeDetailsDTO;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.DlChannelDistributor;
import com.dl.member.model.DlChannelOptionLog;
import com.dl.member.model.User;
import com.dl.member.model.UserAccount;
import com.dl.member.param.UserReceiveLotteryAwardParam;
import com.dl.member.param.UserRegisterParam;

@Service
@Transactional
public class DlChannelConsumerService extends AbstractService<DlChannelConsumer> {
	@Resource
	private DlChannelConsumerMapper dlChannelConsumerMapper;
	@Resource
	private DlChannelDistributorMapper dlhannelDistributorMapper;
	@Resource
	private UserAccountService userAccountService;
	@Resource
	private UserService userService;
	@Resource
	private UserRegisterService userRegisterService;
	@Resource
	private UserBonusService userBonusService;
	@Resource
	private DlChannelOptionLogService dlChannelOptionLogService;

	public List<DlChannelConsumer> selectByChannelDistributorId(Integer channelDistributorId) {
		Condition condition = new Condition(DlChannelConsumer.class);
		condition.createCriteria().andCondition("channel_distributor_id = ", channelDistributorId).andIsNotNull("userId");
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
					double income = 0;
					income = 0 - userAccountList.get(j).getAmount().multiply(bd).doubleValue();
					incomeDetails.setIncome(income);
					double amount = 0;
					amount = 0 - userAccountList.get(j).getAmount().doubleValue();
					incomeDetails.setLotteryAmount(amount);
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
		userRegisterParam.setPassWord("");// 初始密码为空
		BaseResult<Integer> regRst = userRegisterService.registerUser(userRegisterParam, request);
		if (regRst.getCode() != 0) {
			return ResultGenerator.genResult(regRst.getCode(), regRst.getMsg());
		}
		Integer userId = regRst.getData();
		dlChannelConsumerMapper.updateByChannelDistributorIdAndMobile(userRegisterParam.getMobile(), userId, userReceiveLotteryAwardParam.getChannelDistributorId());
		userBonusService.receiveUserBonus(ProjectConstant.XNREGISTER, userId);
		userBonusService.receiveUserBonus(ProjectConstant.REGISTER, userId);
		return ResultGenerator.genSuccessResult("领取成功");
	}

	public void updateByUserId(Integer userId) {
		dlChannelConsumerMapper.updateByUserId(userId, DateUtil.getCurrentTimeLong());
		List<DlChannelConsumer> consumers = this.findAll();
		List<DlChannelDistributor> distributors = dlhannelDistributorMapper.selectAll();
		DlChannelOptionLog channelOptionLog = new DlChannelOptionLog();
		List<User> userList = new ArrayList<User>();
		userList = userService.findAll();
		for (int j = 0; j < userList.size(); j++) {
			if (userId.equals(userList.get(j).getUserId())) {
				// 封装用户相关信息
				channelOptionLog.setOptionId(0);
				channelOptionLog.setUserName(userList.get(j).getUserName());
				channelOptionLog.setMobile(userList.get(j).getMobile());
				channelOptionLog.setUserId(userList.get(j).getUserId());
			}
		}
		for (int k = 0; k < consumers.size(); k++) {
			if (userId.equals(consumers.get(k).getUserId())) {
				channelOptionLog.setDistributorId(consumers.get(k).getChannelDistributorId());
			}
		}
		channelOptionLog.setOperationNode(1);
		channelOptionLog.setStatus(1);
		channelOptionLog.setSource("h5");
		channelOptionLog.setOptionTime(DateUtil.getCurrentTimeLong());

		for (int j = 0; j < distributors.size(); j++) {
			if (channelOptionLog.getDistributorId().equals(distributors.get(j).getChannelDistributorId())) {
				channelOptionLog.setChannelId(distributors.get(j).getChannelId());
			}
		}
		dlChannelOptionLogService.save(channelOptionLog);

	}

	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//
	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//
	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//
	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//
	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//
	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//
	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//
	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//
	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//
	// ----------------------------------------------------(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)骚骚的分隔线上边是老代码老逻辑(↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑)------------------------------------------------------------//

	public List<IncomeDetailsDTO> getIncomeDetailsListBak(Integer userId, String addTime, Double rate) {
		List<IncomeDetailsDTO> incomeDetailsList = dlChannelOptionLogService.getChannelConsumerList(addTime, userId);
		for (int i = 0; i < incomeDetailsList.size(); i++) {
			double income = 0;
			if (incomeDetailsList.get(i).getOperationNode() == 1) {
				incomeDetailsList.get(i).setIncomeType("注册");
				income += 1;
			} else {
				incomeDetailsList.get(i).setIncomeType("购彩" + incomeDetailsList.get(i).getLotteryAmount() + "元");
				BigDecimal bd = new BigDecimal(rate);
				BigDecimal bdIncome = new BigDecimal(incomeDetailsList.get(i).getLotteryAmount());
				income = bdIncome.multiply(bd).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			incomeDetailsList.get(i).setIncome(income);
			if (null != incomeDetailsList.get(i).getMobile()) {
				incomeDetailsList.get(i).setMobile(incomeDetailsList.get(i).getMobile().substring(0, 3) + "****" + incomeDetailsList.get(i).getMobile().substring(7));
			}
		}
		return incomeDetailsList;
	}

}
