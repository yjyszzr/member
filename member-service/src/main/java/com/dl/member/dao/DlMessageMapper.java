package com.dl.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.DlMessage;
import com.dl.member.model.UserMessageListParam;

public interface DlMessageMapper extends Mapper<DlMessage> {

	List<DlMessage> findUserMessageList(UserMessageListParam param);

	int getUnReadMessageNum(@Param("userId")Integer userId);
	
	int updateUnReadMessage(@Param("userId")Integer userId);

	int updateUnReadMessageByObjType(@Param("userId")Integer userId,@Param("objectType")Integer objectType);

	int getUnReadMessageNumByObjType(@Param("userId")Integer userId,@Param("objectType")Integer objectType);
}