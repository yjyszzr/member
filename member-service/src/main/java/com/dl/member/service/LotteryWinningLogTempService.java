package com.dl.member.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Condition;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.LotteryWinningLogTempMapper;
import com.dl.member.model.LotteryWinningLogTemp;

@Service
public class LotteryWinningLogTempService extends AbstractService<LotteryWinningLogTemp> {
	@Resource
	private LotteryWinningLogTempMapper lotteryWinningLogTempMapper;

	public List<LotteryWinningLogTemp> selectWinningLogByIsShow() {
		Condition condition = new Condition(LotteryWinningLogTemp.class);
		condition.createCriteria().andCondition("is_show=", 1);
		return lotteryWinningLogTempMapper.selectByCondition(condition);
	}

}
