package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.dto.IncomeDetailsDTO;
import com.dl.member.model.DlChannelConsumer;

public interface DlChannelConsumerMapper extends Mapper<DlChannelConsumer> {
	public List<IncomeDetailsDTO> getChannelConsumerList(@Param("addTime") String addTime, @Param("userId") Integer userId);
}