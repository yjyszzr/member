package com.dl.member.service;
import com.dl.member.model.DlComplain;
import com.dl.member.dao.DlComplainMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlComplainService extends AbstractService<DlComplain> {
    @Resource
    private DlComplainMapper dlComplainMapper;

}
