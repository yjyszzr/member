package com.dl.member.service;
import com.dl.member.model.UserWithdraw;
import com.dl.member.dao.UserWithdrawMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class UserWithdrawService extends AbstractService<UserWithdraw> {
    @Resource
    private UserWithdrawMapper userWithdrawMapper;

}
