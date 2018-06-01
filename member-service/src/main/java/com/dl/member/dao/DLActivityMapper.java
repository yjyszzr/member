package com.dl.member.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.DLActivity;

public interface DLActivityMapper extends Mapper<DLActivity> {
	
	DLActivity queryActivityByType(@Param("actType") Integer actType);
	
}