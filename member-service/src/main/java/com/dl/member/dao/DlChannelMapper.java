package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.DlChannel;

public interface DlChannelMapper extends Mapper<DlChannel> {

	List<DlChannel> findAllOrderByLetter(@Param("channelName") String channelName);
}