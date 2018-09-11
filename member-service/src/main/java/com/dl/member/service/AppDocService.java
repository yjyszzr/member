package com.dl.member.service;
import com.dl.member.model.AppDoc;
import com.dl.member.dao.AppDocMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class AppDocService extends AbstractService<AppDoc> {
    @Resource
    private AppDocMapper appDocMapper;
    
    public AppDoc queryAppDocByType(Integer classify){
    	AppDoc appDoc = appDocMapper.queryAppDocByType(classify);
    	return appDoc;
    }

}
