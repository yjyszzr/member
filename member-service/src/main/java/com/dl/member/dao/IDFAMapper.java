package com.dl.member.dao;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.IDFA;
import com.dl.member.model.IDFACallBack;
import com.dl.member.param.IDFACheckParam;

public interface IDFAMapper extends Mapper<IDFA> {

	public IDFA queryIDFAByIdfa(String idfaParam);
	
	void save(IDFA idfa);
	
	void updateUserIdByIdfa(IDFA idfa);
}
