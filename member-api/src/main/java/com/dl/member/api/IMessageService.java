package com.dl.member.api;

import com.dl.base.result.BaseResult;
import com.dl.member.dto.UserNoticeDTO;
import com.dl.member.param.AddMessageParam;
import com.dl.member.param.NoticeParam;
import com.dl.member.param.SmsParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "member-service")
public interface IMessageService {

	@RequestMapping(path = "/user/message/add", method = RequestMethod.POST)
	public BaseResult<String> add(@RequestBody AddMessageParam addParam);

	@RequestMapping(path = "/user/message/queryUserNotice", method = RequestMethod.POST)
	public BaseResult<UserNoticeDTO> queryUserNotice(@RequestBody NoticeParam param);

	@RequestMapping(path = "/user/message/readUserNotice", method = RequestMethod.POST)
	public BaseResult<String> readUserNotice(@RequestBody NoticeParam param);

}
