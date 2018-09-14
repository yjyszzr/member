package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.member.param.UserDealActionParam;


/**
 * 交易开关
 * @author 
 *
 */
@FeignClient(value="member-service")
public interface ISwitchConfigService {
	
	/**
	 * 用户购彩行为判断
	 * @param UserBonusParam
	 * @return
	 */
	@RequestMapping(path="/user/switch/config/userDealAciton", method=RequestMethod.POST)
	 public BaseResult<Integer> userDealAction( UserDealActionParam param) ;
	
	
}
