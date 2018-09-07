package com.dl.member.service;
import com.dl.member.model.SmsTemplate;
import com.dl.member.dao.SmsTemplateMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class SmsTemplateService extends AbstractService<SmsTemplate> {
    @Resource
    private SmsTemplateMapper smsTemplateMapper;
    
    public Integer querySmsByAppCodeName(Integer type,Integer appCodeName) {
    	SmsTemplate sms = smsTemplateMapper.querySmsByAppCode(type,appCodeName);
		return sms.getSmsTemplateId();
    }

}
