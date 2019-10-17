package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.dto.IncomeDetailsDTO;
import com.dl.member.model.DlChannelConsumer;

public interface DlChannelConsumerMapper extends Mapper<DlChannelConsumer> {
	public List<IncomeDetailsDTO> getChannelConsumerList(@Param("addTime") String addTime, @Param("channelDistributorId") Integer channelDistributorId);

	public void updateByUserId(@Param("userId") Integer userId, @Param("fristLoginTime") Integer fristLoginTime);

	public void updateByChannelDistributorIdAndMobile(@Param("mobile") String mobile, @Param("userId") Integer userId, @Param("channelDistributorId") Integer channelDistributorId);
}