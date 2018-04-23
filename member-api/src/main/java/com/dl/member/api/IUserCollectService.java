package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.member.param.ArticleIdParam;


/**
 * 用户收藏接口
 * @author zhangzirong
 *
 */
@FeignClient(value="member-service")
public interface IUserCollectService {
	
	/**
	 * 查看用户是否收藏
	 * @param ArticleIdParam
	 * @return
	 */
	@RequestMapping(path="/user/collect/isCollect", method=RequestMethod.POST)
	public BaseResult<Integer> isCollect(@RequestBody ArticleIdParam articleIdParam);
	
	
}
