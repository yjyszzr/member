package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.param.StrParam;
import com.dl.member.param.UserDealActionParam;


/**
 * 交易开关
 * @author 
 *
 */
@FeignClient(value="member-service")
public interface ISwitchConfigService {
	
	/**
	 * 交易版开关的优先级：一级比一级弱
	 * 1.用户的超级开关
	 * 2.用户交易行为
	 * 3.渠道开关
	 * @param plat
	 * @param version
	 * @param chanel
	 * @return 目前返回对象只用到了trunon字段
	 */
	@RequestMapping(path="/switch/config/query", method=RequestMethod.POST)
	public BaseResult<SwitchConfigDTO> querySwitch(@RequestBody StrParam param);
	
	
	/**
	 * 用户购彩行为判断
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/switch/config/userDealAciton", method=RequestMethod.POST)
	 public BaseResult<Integer> userDealAction( UserDealActionParam param) ;
	
	
}
