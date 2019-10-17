package com.dl.member.dao;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.IDFACallBack;
import com.dl.member.param.IDFAClickParam;;

public interface IDFACallBackMapper extends Mapper<IDFACallBack> {

	  void save(IDFACallBack idfaCallBack);
	  
	  void updateByIdfa(IDFACallBack idfaCallBack);
	  
	  void updateBackStatusByIdfa(IDFACallBack idfaCallBack);
	  
	  IDFACallBack queryByIdfa(String idfaClickParam);
}
