package com.dl.member.service;
import com.dl.member.model.UserLoginLog;
import com.dl.member.dao.UserLoginLogMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class UserLoginLogService extends AbstractService<UserLoginLog> {
    @Resource
    private UserLoginLogMapper userLoginLogMapper;

}
