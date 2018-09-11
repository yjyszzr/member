package com.dl.member.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.AppDoc;

public interface AppDocMapper extends Mapper<AppDoc> {
	AppDoc queryAppDocByType(@Param("classify") Integer classify);
}