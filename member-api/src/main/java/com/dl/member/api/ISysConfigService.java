package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.param.StrParam;
import com.dl.member.param.SysConfigParam;
//import com.pgt.shop.member.dto.UserCapitalDTO;
//import com.pgt.shop.member.param.CancelChangeParam;
//import com.pgt.shop.member.param.ConfirmOrderParam;
import com.dl.member.param.UserBonusParam;


/**
 * 查询配置接口
 * @author zhangzirong
 *
 */
@FeignClient(value="member-service")
public interface ISysConfigService {
	
	/**
	 * 查询业务设置
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/sys/querySysConfig", method=RequestMethod.POST)
	public BaseResult<SysConfigDTO> querySysConfig(@RequestBody SysConfigParam sysConfigParam);
	
	
}
