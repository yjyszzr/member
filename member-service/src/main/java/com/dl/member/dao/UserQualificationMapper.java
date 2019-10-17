package com.dl.member.dao;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserQualification;

public interface UserQualificationMapper extends Mapper<UserQualification> {
	int insertUserQualification(UserQualification userQualification);
	
	UserQualification queryQualificationByUserId(@Param("userId") Integer userId,@Param("actId") Integer actId,@Param("actType") Integer actType);
}