package com.dl.member.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.DlChannelConsumerMapper;
import com.dl.member.dao.DlChannelDistributorMapper;
import com.dl.member.dao.UserAccountMapper;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.DlChannelDistributor;
import com.dl.member.model.UserAccount;
import com.dl.member.param.DlChannelDistributorParam;

@Service
@Transactional
public class DlChannelConsumerService extends AbstractService<DlChannelConsumer> {
	@Resource
	private DlChannelConsumerMapper dlChannelConsumerMapper;
	@Resource
	private DlChannelDistributorMapper dlChannelDistributorMapper;
	@Resource
	private UserAccountMapper dlUserAccountMapper;

	public int findinviteNumByUserId(DlChannelDistributorParam param) {
		// 先根据入参ID查出来分销员的ID
		Condition conditionByUserId = new Condition(DlChannelDistributor.class);
		// 根据分销员ID查询分销员下的顾客量
		conditionByUserId.createCriteria().andCondition("user_id =", param.getUserId());
		List<DlChannelConsumer> dlChannelConsumerList = dlChannelConsumerMapper.selectByCondition(conditionByUserId);
		if (dlChannelConsumerList.size() > 0) {
			Condition condition = new Condition(DlChannelDistributor.class);
			// 根据分销员ID查询分销员下的顾客量
			condition.createCriteria().andCondition("channel_distributor_id=", dlChannelConsumerList.get(0).getChannelDistributorId());
			List<DlChannelDistributor> dlChannelDistributorList = dlChannelDistributorMapper.selectByCondition(condition);
			return dlChannelDistributorList.size();
		}
		return 0;
	}

	public Double findBettingTotalAmount(DlChannelDistributorParam param) {

		// 先根据入参ID查出来分销员的ID
		Condition conditionByUserId = new Condition(DlChannelDistributor.class);
		// 根据分销员ID查询分销员下的顾客量
		conditionByUserId.createCriteria().andCondition("user_id =", param.getUserId());
		List<DlChannelConsumer> dlChannelConsumerList = dlChannelConsumerMapper.selectByCondition(conditionByUserId);
		Double bettingTotalAmount = 0.0;
		if (dlChannelConsumerList.size() > 0) {
			Condition condition = new Condition(DlChannelDistributor.class);
			condition.createCriteria().andCondition("channel_distributor_id=", dlChannelConsumerList.get(0).getChannelDistributorId());
			// 根据分销员ID查询分销员下的顾客量
			List<String> userIds = dlChannelDistributorMapper.selectByCondition(condition).stream().map(dto -> dto.getUserId().toString()).collect(Collectors.toList());
			if (userIds.size() > 0) {
				Condition userAccountCondition = new Condition(UserAccount.class);
				// 根据分销员ID查询分销员下的顾客量
				userAccountCondition.createCriteria().andCondition("user_id  in", userIds).andCondition("process_type", 3);
				// 查询出来该用户的购彩的account然后去订单表里查询购彩金额
				List<String> orderSns = dlUserAccountMapper.selectByCondition(userAccountCondition).stream().map(userAccountDto -> userAccountDto.getOrderSn().toString()).collect(Collectors.toList());

				bettingTotalAmount = dlChannelDistributorMapper.findBettingTotalAmount(orderSns);
			}
			return bettingTotalAmount;
		}
		return bettingTotalAmount;
	}
}
