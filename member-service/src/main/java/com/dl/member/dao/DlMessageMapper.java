package com.dl.member.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.DlMessage;
import com.dl.member.model.UserMessageListParam;

public interface DlMessageMapper extends Mapper<DlMessage> {

	List<DlMessage> findUserMessageList(UserMessageListParam param);
}