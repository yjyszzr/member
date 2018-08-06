package com.dl.member.service;
import com.dl.member.model.DLAppUpdateLog;
import com.dl.member.dao.DLAppUpdateLogMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DLAppUpdateLogService extends AbstractService<DLAppUpdateLog> {
    @Resource
    private DLAppUpdateLogMapper dLAppUpdateLogMapper;
    
    public DLAppUpdateLog queryUpdateAppLog(Integer codeAppName,String version) {
    	DLAppUpdateLog appUpdateLog = dLAppUpdateLogMapper.queryUpdateAppLog(codeAppName, version);
		return appUpdateLog;
    }

}
