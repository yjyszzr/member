package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.RechargeDataActivityDTO;
import com.dl.member.param.StrParam;


/**
 * 用户接口
 * @author zhangzirong
 *
 */
@FeignClient(value="member-service")
public interface IActivityService {
	
	/**
	 * 查询是否有充值活动
	 * @return
	 */
	@RequestMapping(path="/activity/queryValidRechargeActivity", method=RequestMethod.POST)
	public BaseResult<RechargeDataActivityDTO> queryValidRechargeActivity(@RequestBody StrParam strParam);

}
