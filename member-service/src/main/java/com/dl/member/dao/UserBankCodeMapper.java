package com.dl.member.dao;

import java.util.List;
import com.dl.base.mapper.Mapper;
import com.dl.member.model.UserBankCode;

public interface UserBankCodeMapper extends Mapper<UserBankCode>{
	List<UserBankCode> listAll();
}
