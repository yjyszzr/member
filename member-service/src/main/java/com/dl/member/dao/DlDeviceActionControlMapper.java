package com.dl.member.dao;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.DlDeviceActionControl;
import org.apache.ibatis.annotations.Param;

public interface DlDeviceActionControlMapper extends Mapper<DlDeviceActionControl> {

    DlDeviceActionControl queryDeviceByIMEI(@Param("mac") String mac);

    int updateDeviceUpdateTime(@Param("updateTime") Integer updateTime,@Param("mac") String mac);

}