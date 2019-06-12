package com.dl.member.service;
import com.dl.member.model.DlDeviceActionControl;
import com.dl.member.dao.DlDeviceActionControlMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlDeviceActionControlService extends AbstractService<DlDeviceActionControl> {
    @Resource
    private DlDeviceActionControlMapper dlDeviceActionControlMapper;

    public DlDeviceActionControl queryDeviceByIMEI(String mac,Integer busiType){
       return dlDeviceActionControlMapper.queryDeviceByIMEI(mac,busiType);
    }

    public Integer updateDeviceCtrlUpdteTime(Integer updateTime,String mac,Integer busiType){
        return dlDeviceActionControlMapper.updateDeviceUpdateTime(updateTime,mac,busiType);
    }

}
