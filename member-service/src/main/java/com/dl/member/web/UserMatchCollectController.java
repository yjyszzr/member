package com.dl.member.web;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.enums.MemberEnums;
import com.dl.member.param.DateStrParam;
import com.dl.member.param.IDParam;
import com.dl.member.param.UserMatchCollectParam;
import com.dl.member.service.UserMatchCollectService;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/07/06.
*/
@RestController
@RequestMapping("/user/matchCollect")
public class UserMatchCollectController {
    @Resource
    private UserMatchCollectService userMatchCollectService;

    @PostMapping("/myCollectMatchIdlist")
    public BaseResult<List<Integer>> list(@RequestBody DateStrParam dateStrParam) {
        Integer userId = SessionUtil.getUserId();
        List<Integer> myCollectMatchIdlist = userMatchCollectService.queryMyCollectMatchIdList(userId,dateStrParam.getDateStr());
        return ResultGenerator.genSuccessResult("success",myCollectMatchIdlist);
    }
    
    @PostMapping("/collectMatchId")
    public BaseResult<Integer> collectMatchId(@RequestBody UserMatchCollectParam userMatchCollectParam) {
        Integer userId = SessionUtil.getUserId();
        Integer matchId = userMatchCollectParam.getMatchId();
        int rstCollect = userMatchCollectService.queryMyCollectMatch(userId, matchId);
        if(rstCollect > 0) {
        	return  ResultGenerator.genResult(MemberEnums.DATA_ALREADY_EXIT_IN_DB.getcode(), "该场比赛已收藏");
        }
        
        int rstSave = userMatchCollectService.saveMyCollectMatch(userId, matchId);
        return ResultGenerator.genSuccessResult("success");
    }
    
	@ApiOperation(value = "取消收藏", notes = "取消收藏")
	@PostMapping("/cancle")
	public BaseResult<String> cancle(@RequestBody IDParam idParam) {
		return userMatchCollectService.cancleCollect(idParam.getId());
	}
}
