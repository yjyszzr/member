package com.dl.member.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.DLAppUpdateLog;

public interface DLAppUpdateLogMapper extends Mapper<DLAppUpdateLog> {
	
	DLAppUpdateLog queryUpdateAppLog(@Param("appCodeName") Integer appCodeName,@Param("version") String version);
}