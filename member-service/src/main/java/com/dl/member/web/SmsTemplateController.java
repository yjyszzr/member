package com.dl.member.web;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.member.service.SmsTemplateService;

/**
* Created by CodeGenerator on 2018/09/06.
*/
@RestController
@RequestMapping("/smsTemplate")
public class SmsTemplateController {
    @Resource
    private SmsTemplateService smsTemplateService;

}
