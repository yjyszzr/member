package com.dl.member.dao;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.SysConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysConfigMapper extends Mapper<SysConfig> {
    public List<SysConfig> queryBusiIds(@Param("businessIds") List<Integer> businessIds);

}