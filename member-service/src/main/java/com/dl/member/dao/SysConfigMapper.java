package com.dl.member.dao;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.SysConfig;

import java.util.List;

public interface SysConfigMapper extends Mapper<SysConfig> {

    public List<SysConfig> queryBusiIds(@Param("queryBusiIds") List<Integer> queryBusiIds);

}