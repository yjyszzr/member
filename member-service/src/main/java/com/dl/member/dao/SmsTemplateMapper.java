package com.dl.member.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.SmsTemplate;

public interface SmsTemplateMapper extends Mapper<SmsTemplate> {
	
	SmsTemplate querySmsByAppCode(@Param("type") Integer type,@Param("appCodeName") Integer appCodeName);
}