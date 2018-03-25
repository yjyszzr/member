package com.dl.member.service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.DlMessageMapper;
import com.dl.member.dto.DlMessageDTO;
import com.dl.member.model.DlMessage;
import com.dl.member.model.UserMessageListParam;

@Service
@Transactional
public class DlMessageService extends AbstractService<DlMessage> {
    @Resource
    private DlMessageMapper dlMessageMapper;

    public List<DlMessageDTO> userMessageList(UserMessageListParam param) {
    	List<DlMessage> messageList = dlMessageMapper.findUserMessageList(param);
    	if(null == messageList)return new ArrayList<DlMessageDTO>(0);
    	return messageList.stream().map(msg->{
    		DlMessageDTO messageDTO = new DlMessageDTO();
    		messageDTO.setContent(msg.getContent());
    		messageDTO.setIsRead(msg.getIsRead());
    		messageDTO.setMsgDesc(msg.getMsgDesc());
    		messageDTO.setMsgId(msg.getMsgId());
    		messageDTO.setMsgType(msg.getMsgType());
    		messageDTO.setObjectType(msg.getObjectType());
    		messageDTO.setReceiver(msg.getReceiver());
    		messageDTO.setReceiverMobile(msg.getReceiverMobile());
    		messageDTO.setSendTime(msg.getSendTime());
    		messageDTO.setTitle(msg.getTitle());
    		return messageDTO;
    	}).collect(Collectors.toList());
    }
}
