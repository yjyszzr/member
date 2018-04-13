package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.dto.UserCollectDTO;
import com.dl.member.model.UserCollect;
import com.dl.member.param.IDParam;
import com.dl.member.param.PageParam;
import com.dl.member.param.UserCollectParam;
import com.dl.member.service.UserCollectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* Created by zhangzirong on 2018/04/13.
*/
@RestController
@RequestMapping("/user/collect")
public class UserCollectController {
    @Resource
    private UserCollectService userCollectService;

    @ApiOperation(value = "添加收藏", notes = "添加收藏")
    @PostMapping("/add")
    public BaseResult<String> add(@RequestBody  UserCollectParam userCollectParam) {
    	Integer userId = SessionUtil.getUserId();
    	UserCollect userCollect = new UserCollect();
    	userCollect.setArticleId(userCollectParam.getArticleId());
    	userCollect.setCollectFrom(userCollectParam.getCollectFrom());
    	userCollect.setUserId(userId);
        userCollectService.save(userCollect);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "删除收藏", notes = "删除收藏")
    @PostMapping("/delete")
    public BaseResult<String> delete(@RequestBody IDParam idParam) {
        userCollectService.deleteById(idParam.getId());
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "用户收藏列表", notes = "用户收藏列表")
    @PostMapping("/list")
    public BaseResult<PageInfo<UserCollectDTO>> list(@RequestBody PageParam pageParam) {
        List<UserCollectDTO> userCollectDTOList = new ArrayList<>();
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        Integer userId = SessionUtil.getUserId();
        Condition c = new Condition(UserCollect.class);
        Criteria criteria = c.createCriteria();
        criteria.andCondition("user_id =", userId);
        List<UserCollect> userCollectList = userCollectService.findByCondition(c);
        if(CollectionUtils.isEmpty(userCollectList)) {
        	 return ResultGenerator.genSuccessResult("success",new PageInfo<UserCollectDTO>());
        }
        
        PageInfo<UserCollect> pageInfo = new PageInfo<UserCollect>(userCollectList);
        UserCollectDTO userCollectDTO = new UserCollectDTO();
        userCollectList.forEach(s->{
        	BeanUtils.copyProperties(s, userCollectDTO);
        	userCollectDTOList.add(userCollectDTO);
        });
        
		PageInfo<UserCollectDTO> result = new PageInfo<UserCollectDTO>();
		BeanUtils.copyProperties(pageInfo, result);
		result.setList(userCollectDTOList);
		
		return ResultGenerator.genSuccessResult("success", result);
    }
}
