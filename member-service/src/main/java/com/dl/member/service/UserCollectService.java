package com.dl.member.service;
import com.dl.member.model.UserCollect;
import com.dl.member.dao.UserCollectMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class UserCollectService extends AbstractService<UserCollect> {
    @Resource
    private UserCollectMapper userCollectMapper;

}
