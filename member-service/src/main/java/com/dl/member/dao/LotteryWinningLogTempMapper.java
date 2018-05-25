package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.LotteryWinningLogTemp;

public interface LotteryWinningLogTempMapper extends Mapper<LotteryWinningLogTemp> {

	void deleteByLogIds(@Param("ids") List<Integer> ids);
}