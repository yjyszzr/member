package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.param.SysConfigParam;
//import com.pgt.shop.member.dto.UserCapitalDTO;
//import com.pgt.shop.member.param.CancelChangeParam;
//import com.pgt.shop.member.param.ConfirmOrderParam;

/**
 * 查询配置接口
 * 
 * @author zhangzirong
 *
 */
@FeignClient(value = "member-service")
public interface ISysConfigService {

	/**
	 * 查询业务设置
	 * 
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path = "/user/sys/querySysConfig", method = RequestMethod.POST)
	public BaseResult<SysConfigDTO> querySysConfig(@RequestBody SysConfigParam sysConfigParam);

	/**
	 * 交易版本或者资讯版本
	 * 
	 * @return
	 */
	@RequestMapping(path = "/switch/config/query", method = RequestMethod.POST)
	public BaseResult<SwitchConfigDTO> querySwitch();

}
