package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dl.base.result.BaseResult;
import com.dl.member.param.AddMessageParam;


/**
 * 用户接口
 * @author zhangzirong
 *
 */
@FeignClient(value="member-service")
public interface IUserMessageService {
	
	/**
	 * 添加消息
	 * @param 
	 * @return
	 */
	@RequestMapping(path="/user/message/add", method=RequestMethod.POST)
	 public BaseResult add(AddMessageParam params);
	
	
}
