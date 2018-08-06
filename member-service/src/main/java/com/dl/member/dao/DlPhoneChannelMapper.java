package com.dl.member.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.DlPhoneChannel;

public interface DlPhoneChannelMapper extends Mapper<DlPhoneChannel> {
	DlPhoneChannel queryPhoneChannelByChannel(@Param("channel") String channel);
}