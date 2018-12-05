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

    public DlDeviceActionControl queryDeviceByIMEI(String mac){
       return dlDeviceActionControlMapper.queryDeviceByIMEI(mac);
    }

    public Integer updateDeviceCtrlUpdteTime(Integer updateTime,String mac){
        return dlDeviceActionControlMapper.updateDeviceUpdateTime(updateTime,mac);
    }

}
