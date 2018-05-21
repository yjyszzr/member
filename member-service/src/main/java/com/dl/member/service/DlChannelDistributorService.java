package com.dl.member.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.DlChannelDistributorMapper;
import com.dl.member.dto.ChannelDistributorDTO;
import com.dl.member.dto.IncomeRankingDTO;
import com.dl.member.dto.PromotionIncomeDTO;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.DlChannelDistributor;
import com.dl.member.model.UserAccount;
import com.dl.member.param.DlChannelDistributorParam;

@Service
@Transactional
public class DlChannelDistributorService extends AbstractService<DlChannelDistributor> {
	@Resource
	private DlChannelDistributorMapper dlChannelDistributorMapper;
	@Resource
	private DlChannelConsumerService channelConsumerService;
	@Resource
	private UserAccountService userAccountService;

	public List<DlChannelDistributor> getAllDlChannelDistributor() {
		return dlChannelDistributorMapper.getAllDlChannelDistributor();
	}

	public int findinviteNumByUserId(DlChannelDistributorParam param) {
		return findConsumerByUserList(param).size();
	}

	private List<DlChannelConsumer> findConsumerByUserList(DlChannelDistributorParam param) {
		// 先根据入参ID查出来分销员的ID
		Condition conditionByUserId = new Condition(DlChannelDistributor.class);
		conditionByUserId.createCriteria().andCondition("user_id =", param.getUserId());
		List<DlChannelDistributor> channelDistributorList = dlChannelDistributorMapper.selectByCondition(conditionByUserId);
		if (channelDistributorList.size() > 0) {
			// 根据分销员ID查询分销员下的顾客量
			return channelConsumerService.selectByChannelDistributorId(channelDistributorList.get(0).getChannelDistributorId());
		}
		return null;
	}

	public Double findBettingTotalAmount(DlChannelDistributorParam param) {
		Double bettingTotalAmount = 0.0;
		List<DlChannelConsumer> dlChannelConsumerList = findConsumerByUserList(param);
		if (dlChannelConsumerList != null) {
			List<String> userIds = dlChannelConsumerList.stream().map(dto -> dto.getUserId().toString()).collect(Collectors.toList());
			if (userIds.size() > 0) {
				// 根据分销员ID查询分销员下的顾客量
				// 查询出来该用户的购彩的account然后去订单表里查询购彩金额
				bettingTotalAmount = userAccountService.findByUserId(userIds);
			}
		}
		return bettingTotalAmount;
	}

	@SuppressWarnings("null")
	public ChannelDistributorDTO getMyRankingList(DlChannelDistributorParam param) {
		ChannelDistributorDTO channelDistributor = new ChannelDistributorDTO();
		List<DlChannelDistributor> channelDistributorList = this.getAllDlChannelDistributor();
		List<DlChannelConsumer> channelConsumerList = channelConsumerService.findAll();
		List<UserAccount> userAccountList = userAccountService.findByProcessType(3);// 查询所以用户购彩的记录
		List<IncomeRankingDTO> incomeRankingList = new ArrayList<IncomeRankingDTO>();
		List<IncomeRankingDTO> incomeRankingListFinal = new ArrayList<IncomeRankingDTO>();
		// 遍历所有分销员
		for (int i = 0; i < channelDistributorList.size(); i++) {
			Integer id = channelDistributorList.get(i).getChannelDistributorId();
			// 取出该分销员下的消费者
			List<DlChannelConsumer> consumerList = channelConsumerList.stream().filter(s -> s.getChannelDistributorId().equals(id)).collect(Collectors.toList());
			// 计算该分销员下消费者的总购彩金额,然后根据提成比例计算该分销员的收入
			BigDecimal consumerTotalAmount = new BigDecimal(0);
			Double registerConsumerAmount = 0.0;
			for (int j = 0; j < consumerList.size(); j++) {
				if (consumerList.get(j).getUserId() != null) {
					for (int k = 0; k < userAccountList.size(); k++) {
						if (userAccountList.get(k).getUserId().equals(consumerList.get(j).getUserId())) {
							consumerTotalAmount = userAccountList.get(k).getAmount().add(consumerTotalAmount);
						}
					}
					registerConsumerAmount += 1.0;
				}
			}
			IncomeRankingDTO incomeRanking = new IncomeRankingDTO();
			// 设置排名
			incomeRanking.setRanking(i);
			// 设置电话
			incomeRanking.setDistributorMobile(channelDistributorList.get(i).getMobile());
			// 设置收入提成
			BigDecimal bd = new BigDecimal(channelDistributorList.get(i).getDistributorCommissionRate());
			// 新注册用户获取一块钱奖励
			incomeRanking.setTotalAmount((0 - consumerTotalAmount.multiply(bd).doubleValue()) + registerConsumerAmount);
			// 设置用户Id
			incomeRanking.setUserId(channelDistributorList.get(i).getUserId());
			// 将incomeRanking实体添加到incomeRankingList返回给客户端
			incomeRankingList.add(incomeRanking);
		}
		if (incomeRankingList.size() != 0) {

			incomeRankingList = incomeRankingList.stream().sorted(Comparator.comparing(IncomeRankingDTO::getTotalAmount).reversed()).collect(Collectors.toList());
			IncomeRankingDTO incomeRankingForSelf = new IncomeRankingDTO();
			// incomeRankingList需要再次排序 首先将自己的记录排到第一,然后按照收入倒序排列
			// 前八名按倒叙额外奖励
			for (int i = 0; i < incomeRankingList.size(); i++) {
				IncomeRankingDTO incomeRankingfinal = new IncomeRankingDTO();
				if (i == 0) {
					incomeRankingfinal.setExtraReward(100.00);
				} else if (i == 1) {
					incomeRankingfinal.setExtraReward(80.00);
				} else if (i == 2) {
					incomeRankingfinal.setExtraReward(60.00);
				} else if (i == 3) {
					incomeRankingfinal.setExtraReward(40.00);
				} else if (i == 4) {
					incomeRankingfinal.setExtraReward(20.00);
				} else if (i == 5) {
					incomeRankingfinal.setExtraReward(15.00);
				} else if (i == 6) {
					incomeRankingfinal.setExtraReward(10.00);
				} else if (i == 7) {
					incomeRankingfinal.setExtraReward(5.00);
				} else {
					incomeRankingfinal.setExtraReward(0.00);
				}
				// 设置电话
				incomeRankingfinal.setDistributorMobile(incomeRankingList.get(i).getDistributorMobile());
				// 设置排行
				incomeRankingfinal.setRanking(i + 1);
				incomeRankingfinal.setUserId(incomeRankingList.get(i).getUserId());
				// 设置总金额
				incomeRankingfinal.setTotalAmount(incomeRankingList.get(i).getTotalAmount());
				// 根据userId做比对 将自己的排名抽出来放到榜首
				if (incomeRankingList.get(i).getUserId().equals(param.getUserId())) {
					incomeRankingForSelf = incomeRankingfinal;
				}
				// 添加到List
				incomeRankingListFinal.add(incomeRankingfinal);
			}
			// 设置自己的排名
			channelDistributor.setChannelDistributor(incomeRankingForSelf);
			// 排名列表
			channelDistributor.setChannelDistributorList(incomeRankingListFinal);
			// 查询该分销员下的扫码量
			Integer inviteNum = findConsumerByUserList(param) == null ? 0 : findConsumerByUserList(param).size();
			channelDistributor.setInviteNum(inviteNum);
			// 查询该分销员下的客户所有的投注金额
			Double bettingTotalAmount = this.findBettingTotalAmount(param);
			channelDistributor.setBettingTotalAmount(bettingTotalAmount);
		}
		return channelDistributor;
	}

	public PromotionIncomeDTO getPromotionIncomeList(DlChannelDistributorParam param) {
		PromotionIncomeDTO promotionIncome = new PromotionIncomeDTO();
		Condition conditionByUserId = new Condition(DlChannelDistributor.class);
		conditionByUserId.createCriteria().andCondition("user_id =", param.getUserId());
		List<DlChannelDistributor> channelDistributorList = dlChannelDistributorMapper.selectByCondition(conditionByUserId);
		if (channelDistributorList.size() > 0) {
			promotionIncome = dlChannelDistributorMapper.getPromotionIncomeList(channelDistributorList.get(0).getChannelDistributorId());
		}
		return promotionIncome;
	}
}
