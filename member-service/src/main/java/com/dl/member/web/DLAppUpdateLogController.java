package com.dl.member.web;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.member.service.DLAppUpdateLogService;

/**
* Created by CodeGenerator on 2018/08/06.
*/
@RestController
@RequestMapping("/d/l/app/update/log")
public class DLAppUpdateLogController {
    @Resource
    private DLAppUpdateLogService dLAppUpdateLogService;

}
