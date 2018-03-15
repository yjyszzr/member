package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.UserRealDTO;
import com.dl.member.param.RealNameAuthParam;
import com.dl.member.param.StrParam;
import com.dl.member.service.UserRealService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
* Created by CodeGenerator on 2018/03/12.
*/
@RestController
@RequestMapping("/user/real")
public class UserRealController {
    @Resource
    private UserRealService userRealService;

    /**
     * 实名认证
     * @param realNameAuthParam
     * @return
     */
    @ApiOperation(value = "实名认证", notes = "实名认证")
    @PostMapping("/realNameAuth")
    public BaseResult<UserRealDTO> realNameAuth(@RequestBody RealNameAuthParam realNameAuthParam){
    	return userRealService.realNameAuth(realNameAuthParam.getRealName(), realNameAuthParam.getIDCode());
    }
    
    /**
     * 查询实名认证信息
     */
    @ApiOperation(value = "查询实名认证信息", notes = "查询实名认证信息")
    @PostMapping("/userRealInfo")
    public BaseResult<UserRealDTO> queryUserReal(@RequestBody StrParam strParam){
    	UserRealDTO userRealDTO = userRealService.queryUserReal();
    	return ResultGenerator.genSuccessResult("登录成功", userRealDTO);
    }
}
