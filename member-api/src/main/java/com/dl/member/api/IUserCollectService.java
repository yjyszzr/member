package com.dl.member.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.member.param.ArticleIdParam;
import com.dl.member.param.DateStrParam;
import com.dl.member.param.UserMatchCollectParam;


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
	public BaseResult<String> isCollect(@RequestBody ArticleIdParam articleIdParam);
	
	/**
	 * 查看用户收藏的比赛id
	 * @param ArticleIdParam
	 * @return
	 */
	@RequestMapping(path="/user/matchCollect/myCollectMatchIdlist", method=RequestMethod.POST)
    public BaseResult<List<Integer>> matchIdlist(@RequestBody DateStrParam dateStrParam);
	
	/**
	 * 收藏比赛
	 * @param ArticleIdParam
	 * @return
	 */	
	@RequestMapping(path="/user/matchCollect", method=RequestMethod.POST)
    public BaseResult<Integer> collectMatchId(@RequestBody UserMatchCollectParam userMatchCollectParam);
	
}
