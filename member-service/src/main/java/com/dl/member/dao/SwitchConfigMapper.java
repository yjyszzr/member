package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.SwitchConfig;

public interface SwitchConfigMapper extends Mapper<SwitchConfig> {
	
	List<SwitchConfig> querySwitchConfigTurnOff(@Param("platform") String platform,@Param("version") String businessType,@Param("channel") String channel);

	int checkUserIp(@Param("userIp")String userIp);
}