package com.dl.member.service;
import com.dl.member.model.UserCapital;
import com.dl.member.dao.UserCapitalMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class UserCapitalService extends AbstractService<UserCapital> {
    @Resource
    private UserCapitalMapper userCapitalMapper;
}
