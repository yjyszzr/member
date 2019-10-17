package com.dl.member.service;
import com.dl.member.model.DLApp;
import com.dl.member.model.DLAppUpdateLog;
import com.dl.member.model.DlPhoneChannel;
import com.dl.member.model.SwitchConfig;

import lombok.extern.slf4j.Slf4j;

import com.dl.member.dao.DLAppMapper;
import com.dl.member.dao.DlPhoneChannelMapper;
import com.dl.member.dao.SwitchConfigMapper;
import com.dl.member.dto.UpdateAppDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.CompareUtil;
import com.dl.base.util.JSONHelper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class DLAppService extends AbstractService<DLApp> {
    @Resource
    private SwitchConfigMapper switchConfigMapper;
    
    @Resource
    private DLAppMapper dLAppMapper;
    
    @Resource
    private DlPhoneChannelMapper dlPhoneChannelMapper;
    
    @Resource
    private DLAppUpdateLogService dLAppUpdateLogService;
    
    public BaseResult<UpdateAppDTO> queryUpdateApp(String channel,String version) {
    	log.info("android版本升级接口入参,channel={},version={}",channel,version);
    	List<String> updateLogList = new ArrayList<>();
    	//DlPhoneChannel dlPhoneChannel = dlPhoneChannelMapper.queryPhoneChannelByChannel(channel);
    	DLAppUpdateLog dLAppUpdateLog = dLAppUpdateLogService.queryUpdateAppLog(channel, version);
    	if(null == dLAppUpdateLog) {
    		log.info("android版本升级接口返回数据判断接口没有最新版本");
    		return ResultGenerator.genResult(MemberEnums.NO_UPDATE.getcode(),MemberEnums.NO_UPDATE.getMsg());
    	}
    	int diff = CompareUtil.compareVersion(version, dLAppUpdateLog.getVersion());
    	if(diff >= 0) {
    		log.info("android版本升级接口返回数据判断接口没有最新版本");
    		return ResultGenerator.genResult(MemberEnums.NO_UPDATE.getcode(),MemberEnums.NO_UPDATE.getMsg());
    	}
    	UpdateAppDTO updateAppDTO = new UpdateAppDTO();
    	updateAppDTO.setType(String.valueOf(dLAppUpdateLog.getUpdateInstall()));
    	updateAppDTO.setUrl(dLAppUpdateLog.getDownloadUrl());
    	updateAppDTO.setChannel(dLAppUpdateLog.getChannel());
    	updateAppDTO.setVersion(dLAppUpdateLog.getVersion());
    	String updateLog = dLAppUpdateLog.getUpdateLog();
    	if(!StringUtils.isEmpty(updateLog)) {
    		updateLogList =  Arrays.asList(updateLog.split(";"));
    	}
    	updateAppDTO.setUpdateLogList(updateLogList);
    	log.info("android版本升级接口返回数据:",JSONHelper.bean2json(updateAppDTO));
    	return ResultGenerator.genSuccessResult("success",updateAppDTO);
    }

    
    public BaseResult<Integer> queryUpdateAppI(String channel,String version) {
    	log.info("android版本升级接口入参,channel={},version={}",channel,version);
    	DLAppUpdateLog dLAppUpdateLog = dLAppUpdateLogService.queryUpdateAppLog(channel, version);
    	if(null == dLAppUpdateLog) {
    		log.info("android版本升级接口返回数据判断接口没有最新版本");
    		return ResultGenerator.genSuccessResult("success",1);
    	}
    	int diff = CompareUtil.compareVersion(version, dLAppUpdateLog.getVersion());
    	if(diff >= 0) {
    		log.info("android版本升级接口返回数据判断接口没有最新版本");
    		return ResultGenerator.genSuccessResult("success",1);
    	}
    	return ResultGenerator.genSuccessResult("success",0);
    }
}
