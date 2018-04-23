package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.base.util.UUIDGenerator;
import com.dl.lottery.api.ILotteryArticleService;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.param.ArticleIdsParam;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserCollectMapper;
import com.dl.member.dto.UserCollectDTO;
import com.dl.member.model.UserCollect;
import com.dl.member.param.ArticleIdParam;
import com.dl.member.param.IDParam;
import com.dl.member.param.PageParam;
import com.dl.member.param.UserCollectParam;
import com.dl.member.service.UserCollectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* Created by zhangzirong on 2018/04/13.
*/
@RestController
@RequestMapping("/user/collect")
@Slf4j
public class UserCollectController {
    @Resource
    private UserCollectService userCollectService;
    
    @Resource
    private ILotteryArticleService lotteryArticleService;
    
    @Resource
    private UserCollectMapper userCollectMapper;

    /**
     * TODO 为校验文章id 
     * @param userCollectParam
     * @return
     */
    @ApiOperation(value = "添加收藏", notes = "添加收藏")
    @PostMapping("/add")
    public BaseResult<String> add(@RequestBody  UserCollectParam userCollectParam) {
    	ArticleIdsParam articleIdsParam = new ArticleIdsParam();
    	Integer articleId = Integer.valueOf(userCollectParam.getArticleId());
    	List<Integer> articleList = new ArrayList<Integer>();
    	articleList.add(articleId);
    	articleIdsParam.setArticleIds(articleList);
    	BaseResult<PageInfo<DLArticleDTO>>  rst = lotteryArticleService.queryArticlesByIds(articleIdsParam);
        if(rst.getCode() != 0) {
        	log.error(rst.getData().toString());
        }	
        
        PageInfo<DLArticleDTO> page = rst.getData();
        List<DLArticleDTO> aList = page.getList();
        if(CollectionUtils.isEmpty(aList)) {
        	return ResultGenerator.genFailResult("文章不存在");
        }
         
    
    	Integer userId = SessionUtil.getUserId();
    	UserCollect userCollect = new UserCollect();
    	userCollect.setArticleId(Integer.valueOf(userCollectParam.getArticleId()));
    	userCollect.setUserId(userId);
    	List<UserCollect> userCollectList = userCollectMapper.queryUserCollectListBySelective(userCollect);
    	if(!CollectionUtils.isEmpty(userCollectList)) {
    		return ResultGenerator.genSuccessResult("用户已添加收藏");
    	}
    	
    	UserCollect userCollectNew = new UserCollect();
    	userCollectNew.setArticleId(Integer.valueOf(userCollectParam.getArticleId()));
    	userCollectNew.setCollectFrom(userCollectParam.getCollectFrom());
    	userCollectNew.setAddTime(DateUtil.getCurrentTimeLong());
    	userCollectNew.setUserId(userId);
        userCollectService.save(userCollect);
        return ResultGenerator.genSuccessResult("添加收藏成功");
    }

    @ApiOperation(value = "取消收藏", notes = "取消收藏")
    @PostMapping("/delete")
    public BaseResult<String> delete(@RequestBody IDParam idParam) {
    	return userCollectService.cancleCollect(idParam.getId());
    }

    @ApiOperation(value = "用户收藏列表", notes = "用户收藏列表")
    @PostMapping("/list")
    public BaseResult<PageInfo<DLArticleDTO>> list(@RequestBody PageParam pageParam) {
        List<UserCollectDTO> userCollectDTOList = new ArrayList<>();
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<UserCollect> userCollectList = userCollectService.queryMyCollectList();
        if(CollectionUtils.isEmpty(userCollectList)) {
        	return ResultGenerator.genSuccessResult("success",new PageInfo<DLArticleDTO>());
        }
        
        List<Integer> myArticleIds = userCollectList.stream().map(s->Integer.valueOf(s.getArticleId())).collect(Collectors.toList());
        ArticleIdsParam articleIdsParam = new ArticleIdsParam();
        articleIdsParam.setArticleIds(myArticleIds);
        BaseResult<PageInfo<com.dl.lottery.dto.DLArticleDTO>> myCollectArticlesRst = lotteryArticleService.queryArticlesByIds(articleIdsParam);
        if(myCollectArticlesRst.getCode() != 0) {
        	return ResultGenerator.genSuccessResult("success",new PageInfo<DLArticleDTO>());
        }
		
		return ResultGenerator.genSuccessResult("success", myCollectArticlesRst.getData());
    }
    
    
	/**
	 * 查看用户是否收藏
	 * @param ArticleIdParam
	 * @return 0-已收藏 1-取消收藏
	 */
    @ApiOperation(value = "用户收藏列表", notes = "用户收藏列表")
	@RequestMapping(path="/user/isCollect", method=RequestMethod.POST)
	public BaseResult<Integer> isCollect(@RequestBody ArticleIdParam articleIdParam){
    	Integer userId = SessionUtil.getUserId();
    	UserCollect userCollect = new UserCollect();
    	userCollect.setArticleId(articleIdParam.getArticleId());
    	List<UserCollect> userCollectList = userCollectMapper.queryUserCollectListBySelective(userCollect);
    	if(CollectionUtils.isEmpty(userCollectList)) {
    		ResultGenerator.genSuccessResult("success", 1);
    	}
    	return ResultGenerator.genSuccessResult("success", userCollectList.get(0).getIsDelete());
    	
    	
    }
    
}
