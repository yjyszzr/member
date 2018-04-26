package com.dl.member.service;
import com.dl.member.model.SwitchConfig;
import com.dl.member.dao.SwitchConfigMapper;
import com.dl.member.enums.MemberEnums;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
public class SwitchConfigService extends AbstractService<SwitchConfig> {
    @Resource
    private SwitchConfigMapper switchConfigMapper;
    
    public BaseResult<List<SwitchConfig>> querySwitchConfig(String platform,String version){
    	List<SwitchConfig> switchConfigList = switchConfigMapper.querySwitchConfigTurnOff(platform,version);
    	if(CollectionUtils.isEmpty(switchConfigList)) {
    		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), "未设置开关");
    	}
    	return ResultGenerator.genSuccessResult("success",switchConfigList);
    }

}
