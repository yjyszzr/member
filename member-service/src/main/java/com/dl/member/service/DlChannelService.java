package com.dl.member.service;
import com.dl.member.model.DlChannel;
import com.dl.member.dao.DlChannelMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlChannelService extends AbstractService<DlChannel> {
    @Resource
    private DlChannelMapper dlChannelMapper;

}
