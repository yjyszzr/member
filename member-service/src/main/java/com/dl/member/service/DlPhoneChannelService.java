package com.dl.member.service;
import com.dl.member.model.DlPhoneChannel;
import com.dl.member.dao.DlPhoneChannelMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlPhoneChannelService extends AbstractService<DlPhoneChannel> {
    @Resource
    private DlPhoneChannelMapper dlPhoneChannelMapper;
    
    /**
     * 根据channel反查appCodeName
     * @param channel
     * @return
     */
    public Integer  queryAppCodeName(String channel) {
    	DlPhoneChannel phoneChannel = dlPhoneChannelMapper.queryPhoneChannelByChannel(channel);
    	Integer appCodeName = phoneChannel.getAppCodeName();
    	return appCodeName;
    }

}
