package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.dto.IncomeDetailsDTO;
import com.dl.member.dto.PromotionIncomeDTO;
import com.dl.member.model.DlChannelOptionLog;

public interface DlChannelOptionLogMapper extends Mapper<DlChannelOptionLog> {

	List<PromotionIncomeDTO> getPromotionIncomeList(@Param("channelDistributorId") Integer channelDistributorId);

	List<IncomeDetailsDTO> getChannelConsumerList(@Param("addTime") String addTime, @Param("channelDistributorId") Integer channelDistributorId);
}