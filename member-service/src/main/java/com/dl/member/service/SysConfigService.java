package com.dl.member.service;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.SysConfigMapper;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.model.SysConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SysConfigService extends AbstractService<SysConfig> {
    @Resource
    private SysConfigMapper sysConfigMapper;
    
    public SysConfigDTO querySysConfig(Integer businessId) {
    	SysConfig sysConfig = this.findBy("businessId", businessId);
    	if(null == sysConfig) {
    		return new SysConfigDTO();
    	}

		SysConfigDTO sysConfigDTO = new SysConfigDTO();
		BeanUtils.copyProperties(sysConfig, sysConfigDTO);
		return sysConfigDTO;
    }

	public List<SysConfigDTO> querySysConfigList(List<Integer> businessIdList) {
		List<SysConfigDTO> sysConfigDTOList = new ArrayList<SysConfigDTO>();
    	if (CollectionUtils.isEmpty(businessIdList)) return sysConfigDTOList;

		List<SysConfig> sysConfigList =  sysConfigMapper.queryBusiIds(businessIdList);
		sysConfigList.stream().forEach(s->{
			SysConfigDTO sysConfigDTO = new SysConfigDTO();
			BeanUtils.copyProperties(s, sysConfigDTO);
			sysConfigDTOList.add(sysConfigDTO);
		});
		return sysConfigDTOList;
	}

}
