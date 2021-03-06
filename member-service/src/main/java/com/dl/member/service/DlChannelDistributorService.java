package com.dl.member.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.DateUtilNew;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.DlChannelDistributorMapper;
import com.dl.member.dto.ChannelDistributorDTO;
import com.dl.member.dto.IncomeDetailsDTO;
import com.dl.member.dto.IncomeRankingDTO;
import com.dl.member.dto.PromotionIncomeDTO;
import com.dl.member.model.DlChannel;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.DlChannelDistributor;
import com.dl.member.model.DlChannelOptionLog;
import com.dl.member.model.User;
import com.dl.member.model.UserAccount;
import com.dl.member.param.DistributorSmsParam;
import com.dl.member.param.DlChannelDistributorParam;
import com.dl.member.param.UserRegisterParam;

@Service
@Transactional
public class DlChannelDistributorService extends AbstractService<DlChannelDistributor> {
	@Resource
	private DlChannelDistributorMapper dlChannelDistributorMapper;
	@Resource
	private DlChannelConsumerService channelConsumerService;
	@Resource
	private UserAccountService userAccountService;
	@Resource
	private DlChannelOptionLogService dlChannelOptionLogService;
	@Resource
	private UserRegisterService userRegisterService;
	@Resource
	private UserService userService;
	@Resource
	private DlChannelService dlChannelService;
	@Resource
	private UserBonusService userBonusService;

	public List<DlChannelDistributor> getAllDlChannelDistributor() {
		return dlChannelDistributorMapper.getAllDlChannelDistributor();
	}

	private List<DlChannelConsumer> findConsumerByUserId(DlChannelDistributorParam param) {
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
		List<DlChannelConsumer> dlChannelConsumerList = findConsumerByUserId(param);
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
					// 首次登录时间不为空 才能判定为该用户登录过该APP只有登录过 才能给员工奖励
					if (consumerList.get(j).getFristLoginTime() != null) {
						registerConsumerAmount += 1.0;
					}
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
			// 查询今天的收入
			// 根据自己的Id查询出自己的渠道分销Id
			DlChannelDistributor cDistributor = this.findByUserId(param.getUserId());
			// 根据分销Id查询该分销下的用户
			List<DlChannelConsumer> list = channelConsumerService.selectByChannelDistributorId(cDistributor.getChannelDistributorId());
			List<String> userIds = list.stream().map(dto -> dto.getUserId().toString()).collect(Collectors.toList());
			String data = DateUtil.getCurrentDateTime(LocalDateTime.now(), DateUtil.date_sdf);
			if (userIds.size() > 0) {
				BigDecimal bigd = new BigDecimal(0);
				List<UserAccount> users = userAccountService.findByUserIdsAndType(userIds, data, 3);
				for (int m = 0; m < users.size(); m++) {
					bigd = bigd.add(users.get(m).getAmount());
				}
				BigDecimal bid = new BigDecimal(cDistributor.getDistributorCommissionRate());
				incomeRankingForSelf.setTodayAmount(0 - bigd.multiply(bid).doubleValue());
			}
			// 设置自己的排名
			channelDistributor.setChannelDistributor(incomeRankingForSelf);
			// 排名列表
			channelDistributor.setChannelDistributorList(incomeRankingListFinal);
			// 查询该分销员下的扫码量
			Integer inviteNum = findConsumerByUserId(param) == null ? 0 : findConsumerByUserId(param).size();
			channelDistributor.setInviteNum(inviteNum);
			// 查询该分销员下的客户所有的投注金额
			Double bettingTotalAmount = this.findBettingTotalAmount(param);
			channelDistributor.setBettingTotalAmount(bettingTotalAmount);
		}
		return channelDistributor;
	}

	public DlChannelDistributor findByUserId(Integer userId) {
		DlChannelDistributor channelDistributor = new DlChannelDistributor();
		Condition conditionByUserId = new Condition(DlChannelDistributor.class);
		conditionByUserId.createCriteria().andCondition("user_id =", userId);
		List<DlChannelDistributor> channelDistributorList = dlChannelDistributorMapper.selectByCondition(conditionByUserId);
		if (channelDistributorList.size() > 0) {
			channelDistributor = channelDistributorList.get(0);
		}
		return channelDistributor;
	}

	public List<PromotionIncomeDTO> getPromotionIncomeList(DlChannelDistributorParam param) {
		DlChannelDistributor channelDistributor = new DlChannelDistributor();
		channelDistributor = this.findByUserId(param.getUserId());
		List<PromotionIncomeDTO> promotionIncomes = new ArrayList<PromotionIncomeDTO>();
		if (channelDistributor != null) {
			promotionIncomes = dlChannelDistributorMapper.getPromotionIncomeList(channelDistributor.getChannelDistributorId());
		}
		List<DlChannelConsumer> channelConsumerList = channelConsumerService.findAll();
		List<UserAccount> userAccountList = userAccountService.findByProcessType(3);
		for (int i = 0; i < promotionIncomes.size(); i++) {
			Double registerConsumerAmount = 0.0;
			String str = promotionIncomes.get(i).getIds();
			List<String> idResult = Arrays.asList(str.split(","));
			BigDecimal lotteryAmount = new BigDecimal(0);
			for (int j = 0; j < idResult.size(); j++) {
				for (int k = 0; k < userAccountList.size(); k++) {
					if (idResult.get(j).equals(userAccountList.get(k).getUserId())) {
						// 购彩金额相加
						lotteryAmount.add(userAccountList.get(k).getAmount());
					}
				}
				for (int j2 = 0; j2 < channelConsumerList.size(); j2++) {
					if (null != channelConsumerList.get(j2).getUserId()) {
						if (channelConsumerList.get(j2).getUserId().equals(idResult.get(j)) && null != channelConsumerList.get(j2).getFristLoginTime()) {
							registerConsumerAmount += 1;
						}
					}
				}
			}
			// 设置收入提成
			BigDecimal bd = new BigDecimal(channelDistributor.getDistributorCommissionRate());
			promotionIncomes.get(i).setLotteryAmount(0 - lotteryAmount.doubleValue());
			promotionIncomes.get(i).setIncome(0 - lotteryAmount.multiply(bd).doubleValue() + registerConsumerAmount);
		}
		return promotionIncomes;
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

	/**
	 * 获取排名列表
	 * 
	 * @param param
	 * @return
	 */
	public ChannelDistributorDTO getMyRankingListBak(DlChannelDistributorParam param) {
		List<DlChannelDistributor> channelDistributorList = this.getAllDlChannelDistributor();
		// 查询所有用户购彩的正常记录
		List<DlChannelOptionLog> optionLogList = dlChannelOptionLogService.findAll().stream().filter(s -> s.getStatus() == 1).collect(Collectors.toList());
		List<IncomeRankingDTO> incomeRankingList = new ArrayList<IncomeRankingDTO>();
		List<IncomeRankingDTO> incomeRankingListFinal = new ArrayList<IncomeRankingDTO>();
		for (int i = 0; i < channelDistributorList.size(); i++) {
			IncomeRankingDTO incomeRanking = new IncomeRankingDTO();
			DlChannelDistributor cDistributor = channelDistributorList.get(i);
			int distributorId = cDistributor.getChannelDistributorId();
			List<DlChannelOptionLog> thisDistributorOptionLogList = optionLogList.stream().filter(s -> s.getDistributorId() == distributorId).collect(Collectors.toList());
			List<DlChannelOptionLog> thisDistributorOptionLogListNode1 = thisDistributorOptionLogList.stream().filter(s -> s.getOperationNode() == 1).collect(Collectors.toList());
			List<DlChannelOptionLog> thisDistributorOptionLogListNode2 = thisDistributorOptionLogList.stream().filter(s -> s.getOperationNode() == 2).collect(Collectors.toList());
			Double totalAmount = thisDistributorOptionLogListNode2.stream().map(s -> s.getOptionAmount().doubleValue()).reduce(Double::sum).orElse(0.00);
			incomeRanking.setDistributorMobile(cDistributor.getMobile());
			BigDecimal bdCommissionRate = new BigDecimal(cDistributor.getDistributorCommissionRate());
			BigDecimal bdTotalAmount = new BigDecimal(totalAmount);
			Double totalA = bdCommissionRate.multiply(bdTotalAmount).doubleValue() + thisDistributorOptionLogListNode1.size();
			DecimalFormat df = new DecimalFormat("#.00");
			incomeRanking.setTotalAmount(Double.parseDouble(df.format(totalA)));
			incomeRanking.setUserId(cDistributor.getUserId());
			incomeRankingList.add(incomeRanking);
		}
		// 设置排名,添加额外奖励和取出自己的排名
		incomeRankingListFinal = incomeRankingList.stream().sorted(Comparator.comparing(IncomeRankingDTO::getTotalAmount).reversed()).collect(Collectors.toList());
		IncomeRankingDTO incomeRankingfinal = new IncomeRankingDTO();
		for (int i = 0; i < incomeRankingListFinal.size(); i++) {
			if (i == 0) {
				incomeRankingListFinal.get(i).setExtraReward(1000.00);
			} else if (i == 1) {
				incomeRankingListFinal.get(i).setExtraReward(800.00);
			} else if (i == 2) {
				incomeRankingListFinal.get(i).setExtraReward(600.00);
			} else if (i == 3) {
				incomeRankingListFinal.get(i).setExtraReward(400.00);
			} else if (i == 4) {
				incomeRankingListFinal.get(i).setExtraReward(200.00);
			} else if (i == 5) {
				incomeRankingListFinal.get(i).setExtraReward(150.00);
			} else if (i == 6) {
				incomeRankingListFinal.get(i).setExtraReward(100.00);
			} else if (i == 7) {
				incomeRankingListFinal.get(i).setExtraReward(50.00);
			} else {
				incomeRankingListFinal.get(i).setExtraReward(0.00);
			}
			incomeRankingListFinal.get(i).setRanking(i + 1);
			if (incomeRankingListFinal.get(i).getUserId().equals(param.getUserId())) {
				incomeRankingfinal = incomeRankingListFinal.get(i);
			}
		}
		ChannelDistributorDTO channelDistributorDTO = new ChannelDistributorDTO();
		// 查询今天的收入(开始)
		DlChannelDistributor cDistributor = this.findByUserId(param.getUserId());
		String data = DateUtil.getCurrentDateTime(LocalDateTime.now(), DateUtil.date_sdf);
		List<IncomeDetailsDTO> totalAmountList = dlChannelOptionLogService.getChannelConsumerList(data, cDistributor.getChannelDistributorId());
		double lotteryAmount = 0;
		int income = 0;
		for (int i = 0; i < totalAmountList.size(); i++) {
			if (totalAmountList.get(i).getOperationNode() == 1) {
				income += 1;
			} else if (totalAmountList.get(i).getOperationNode() == 2) {
				lotteryAmount += totalAmountList.get(i).getLotteryAmount();
			}
		}
		BigDecimal bigdAmount = new BigDecimal(lotteryAmount);
		BigDecimal bigdRate = new BigDecimal(cDistributor.getDistributorCommissionRate());
		Double incomeBd = bigdAmount.multiply(bigdRate).doubleValue() + income;
		DecimalFormat df = new DecimalFormat("#.00");
		incomeRankingfinal.setTodayAmount(Double.parseDouble(df.format(incomeBd)));
		// 查询今天的收入(结束)
		channelDistributorDTO.setChannelDistributor(incomeRankingfinal);
		List<DlChannelConsumer> consumerList = findConsumerByUserId(param);
		Integer inviteNum = 0;
		for (int size = 0; size < consumerList.size(); size++) {
			DlChannelConsumer DlChannelConsumer = consumerList.get(size);
			if (DlChannelConsumer.getFristLoginTime() != null) {
				inviteNum += 1;
			}
		}
		channelDistributorDTO.setInviteNum(inviteNum);
		Double bettingTotalAmount = this.findBettingTotalAmountByDistributorId(param);
		channelDistributorDTO.setBettingTotalAmount(bettingTotalAmount);
		channelDistributorDTO.setChannelDistributorList(incomeRankingListFinal);
		return channelDistributorDTO;
	}

	private Double findBettingTotalAmountByDistributorId(DlChannelDistributorParam param) {
		BigDecimal bettingTotalAmount = new BigDecimal(0);
		List<DlChannelConsumer> dlChannelConsumerList = findConsumerByUserId(param);
		List<DlChannelOptionLog> promotionIncomes = new ArrayList<DlChannelOptionLog>();
		if (dlChannelConsumerList.size() > 0) {
			Condition condition = new Condition(DlChannelOptionLog.class);
			condition.createCriteria().andCondition("distributor_id =", dlChannelConsumerList.get(0).getChannelDistributorId());
			promotionIncomes = dlChannelOptionLogService.findByCondition(condition);
			for (int i = 0; i < promotionIncomes.size(); i++) {
				if (promotionIncomes.get(i).getOperationNode() == 2) {
					bettingTotalAmount = bettingTotalAmount.add(promotionIncomes.get(i).getOptionAmount());
				}
			}
		}
		return bettingTotalAmount.doubleValue();
	}

	/**
	 * 我的推广收入
	 * 
	 * @param param
	 * @return
	 */
	public List<PromotionIncomeDTO> getPromotionIncomeListBak(DlChannelDistributorParam param) {
		DlChannelDistributor channelDistributor = this.findByUserId(param.getUserId());
		List<PromotionIncomeDTO> promotionIncomes = new ArrayList<PromotionIncomeDTO>();
		if (channelDistributor != null) {
			promotionIncomes = dlChannelOptionLogService.getPromotionIncomeList(channelDistributor.getChannelDistributorId());
			for (int i = 0; i < promotionIncomes.size(); i++) {
				// 设置收入提成
				BigDecimal bd = new BigDecimal(channelDistributor.getDistributorCommissionRate());
				if (promotionIncomes.get(i).getLotteryAmount() != null) {
					BigDecimal lotteryAmount = new BigDecimal(promotionIncomes.get(i).getLotteryAmount());
					Double a = lotteryAmount.multiply(bd).doubleValue() + promotionIncomes.get(i).getRegisterNum();
					DecimalFormat df = new DecimalFormat("#.00");
					promotionIncomes.get(i).setIncome(Double.parseDouble(df.format(a)));
				} else {
					promotionIncomes.get(i).setLotteryAmount(0D);
					promotionIncomes.get(i).setIncome(promotionIncomes.get(i).getRegisterNum().doubleValue());
				}
			}
		}
		return promotionIncomes;
	}

	/**
	 * 注册成为店员时 先注册成为用户 注册成为用户需要给该用户派发优惠券
	 * 
	 * @param distributor
	 * @param request
	 * @return
	 */
	public BaseResult<String> saveUserAndDistributor(DistributorSmsParam smsParam, HttpServletRequest request) {
		// 先注册用户
		UserRegisterParam userRegisterParam = new UserRegisterParam();
		userRegisterParam.setLoginSource("4");
		userRegisterParam.setMobile(smsParam.getMobile());
		userRegisterParam.setPassWord(smsParam.getPassword());
		BaseResult<Integer> regRst = userRegisterService.registerUser(userRegisterParam, request);
		if (regRst.getCode() != 0) {
			return ResultGenerator.genResult(regRst.getCode(), regRst.getMsg());
		}
		Integer userId = regRst.getData();
		// 送红包
		userBonusService.receiveUserBonus(ProjectConstant.REGISTER, userId);
		// 注册成为店员
		DlChannelDistributor distributor = new DlChannelDistributor();
		DlChannel dlChannel = dlChannelService.findById(smsParam.getChannelId());
		if (dlChannel != null) {
			distributor.setChannelId(0);
			distributor.setChannelId(smsParam.getChannelId());
			distributor.setChannelName(dlChannel.getChannelName());
			distributor.setMobile(smsParam.getMobile());
			List<DlChannelDistributor> dlChannelDistributorList = this.findAll();
			Integer num = dlChannelDistributorList.size() + 1;
			distributor.setChannelDistributorNum(dlChannel.getChannelNum() + "." + num);
			distributor.setAddTime(DateUtilNew.getCurrentTimeLong());
			distributor.setDeleted(0);
			distributor.setRemark("");
			distributor.setUserId(userId);
			User user = userService.findById(userId);
			distributor.setUserName(user.getUserName());
			distributor.setDistributorCommissionRate(0.02);// 佣金比例2%,经产品人员确认
			this.save(distributor);
			return ResultGenerator.genSuccessResult("注册成功");
		} else {
			return ResultGenerator.genSuccessResult("注册失败,渠道为空");
		}
	}
}
