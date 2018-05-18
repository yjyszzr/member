package com.dl.member.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.DlChannelDistributor;

public interface DlChannelDistributorMapper extends Mapper<DlChannelDistributor> {
	/**
	 * 获取分销员列表
	 * 
	 * @return
	 */
	public List<DlChannelDistributor> getAllDlChannelDistributor();

	public Double findBettingTotalAmount(List<String> orderSns);
}