package com.dl.member.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.QFDTO;
import com.dl.member.param.QFParam;

import io.swagger.annotations.ApiOperation;

@FeignClient(value="member-service")
public interface IUserQualificationService {
	
    @ApiOperation(value = "查询活动资格", notes = "查询活动资格")
    @RequestMapping(path="/user/qualification/queryActQF", method=RequestMethod.POST)
    public BaseResult<QFDTO> queryActQF(@RequestBody QFParam qfParam);

}
