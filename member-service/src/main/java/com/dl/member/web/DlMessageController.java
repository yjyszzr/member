package com.dl.member.web;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.dto.DlMessageDTO;
import com.dl.member.model.DlMessage;
import com.dl.member.model.UserMessageListParam;
import com.dl.member.param.MessageAddParam;
import com.dl.member.param.MessageListParam;
import com.dl.member.service.DlMessageService;
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
    public BaseResult add(@RequestBody MessageAddParam param) {
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
        dlMessageService.save(dlMessage);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlMessage dlMessage) {
        dlMessageService.update(dlMessage);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlMessage dlMessage = dlMessageService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlMessage);
    }

}
