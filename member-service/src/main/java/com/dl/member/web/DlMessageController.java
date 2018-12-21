package com.dl.member.web;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.dl.member.dto.UserNoticeDTO;
import com.dl.member.param.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.constant.CommonConstants;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.dto.DlMessageDTO;
import com.dl.member.model.DlMessage;
import com.dl.member.model.UserMessageListParam;
import com.dl.member.service.DlMessageService;
import com.dl.member.service.UserService;
import com.dl.member.util.GeTuiMessage;
import com.dl.member.util.GeTuiUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;

/**
* Created by CodeGenerator on 2018/03/25.
*/
@RestController
@RequestMapping("/user/message")
public class DlMessageController {
    @Resource
    private DlMessageService dlMessageService;
    
    @Resource
    private GeTuiUtil geTuiUtil;

    @Resource
    private UserService userService;


    @ApiOperation(value = "用户消息列表", notes = "用户消息列表")
    @PostMapping("/list")
    public BaseResult<PageInfo<DlMessageDTO>> list(@RequestBody MessageListParam param) {
    	int page = param.getPageNum() == null?1:param.getPageNum();
    	int size = param.getPageSize() == null?10:param.getPageSize();
    	int msgType = param.getMsgType() == null?0:param.getMsgType();
    	Integer userId = SessionUtil.getUserId();
        PageHelper.startPage(page, size);
        UserMessageListParam userMessageListParam = new UserMessageListParam();
        userMessageListParam.setMsgType(msgType);
        userMessageListParam.setReceiver(userId);
        List<DlMessageDTO> list = dlMessageService.userMessageList(userMessageListParam);
        PageInfo<DlMessageDTO> pageInfo = new PageInfo<DlMessageDTO>(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
    
    @PostMapping("/add")
    public BaseResult<String> add(@RequestBody AddMessageParam addParam) {
    	List<MessageAddParam> params = addParam.getParams();
    	List<Integer> lotteryFailUserIds = new ArrayList<Integer>(params.size());
    	for(MessageAddParam param: params) {
    		DlMessage dlMessage = new DlMessage();
    		dlMessage.setContent(param.getContent());
    		dlMessage.setContentDesc(param.getContentDesc());
    		dlMessage.setMsgDesc(param.getMsgDesc());
    		dlMessage.setMsgType(param.getMsgType());
    		dlMessage.setReceiver(param.getReceiver());
    		dlMessage.setReceiverMobile(param.getReceiveMobile());
    		dlMessage.setObjectType(param.getObjectType());
    		dlMessage.setSendTime(param.getSendTime());
    		dlMessage.setSender(param.getSender());
    		dlMessage.setTitle(param.getTitle());
    		dlMessage.setMsgUrl(param.getMsgUrl());
    		dlMessage.setContentUrl(param.getContentUrl());
    		dlMessageService.save(dlMessage);
    		if(3 == param.getObjectType()) {
    			lotteryFailUserIds.add(param.getReceiver());
    		}
    	}
    	//出票失败提示
    	if(CollectionUtils.isNotEmpty(lotteryFailUserIds)) {
    		List<String> clientIds = userService.getClientIds(lotteryFailUserIds);
    		for(String clientId : clientIds) {
    			GeTuiMessage getuiMessage = new GeTuiMessage(CommonConstants.FORMAT_PRINTLOTTERY_PUSH_TITLE, CommonConstants.FORMAT_PRINTLOTTERY_PUSH_DESC, DateUtil.getCurrentTimeLong());
    			geTuiUtil.pushMessage(clientId, getuiMessage);
    		}
    	}
        return ResultGenerator.genSuccessResult();
    }


    @ApiOperation(value = "推送消息", notes = "推送消息")
    @PostMapping("/push")
    public BaseResult<String> pushMessage(@RequestBody PushMessageParam param) {
    	GeTuiMessage getuiMessage = new GeTuiMessage(param);
    	geTuiUtil.pushMessage(param.getClientId(), getuiMessage);
    	return ResultGenerator.genSuccessResult();
    }

   /* @PostMapping("/update")
    public BaseResult update(DlMessage dlMessage) {
        dlMessageService.update(dlMessage);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlMessage dlMessage = dlMessageService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlMessage);
    }*/

	@ApiOperation(value = "查询用户消息提示", notes = "查询用户消息提示")
	@PostMapping("/queryUserNotice")
	public BaseResult<UserNoticeDTO> queryUserNotice(@RequestBody NoticeParam param) {
		Integer userId = SessionUtil.getUserId();
		UserNoticeDTO queryUserNotice = userService.queryUserNotice(userId);
		return ResultGenerator.genSuccessResult("success", queryUserNotice);
	}

	@ApiOperation(value = "已经读取用户消息提示", notes = "已经读取用户消息提示")
	@PostMapping("/readUserNotice")
	public BaseResult<String> readUserNotice(@RequestBody NoticeParam param) {
		dlMessageService.readMess(param.getUserId(),Integer.valueOf(param.getObjType()));
		return ResultGenerator.genSuccessResult("success");
	}

}
