package com.dl.member.service;
import com.dl.member.model.DonationRechargeCard;
import com.dl.member.param.PageParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

import com.dl.member.dao.DonationRechargeCardMapper;
import com.dl.member.dto.DonationRechargeCardDTO;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class DonationRechargeCardService extends AbstractService<DonationRechargeCard> {
    @Resource
    private DonationRechargeCardMapper donationRechargeCardMapper;

	public BaseResult<PageInfo<DonationRechargeCardDTO>> queryAllRechargeCards(PageParam pageParam) {
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<DonationRechargeCard> list = this.findAll();
        PageInfo<DonationRechargeCard> pageInfo = new PageInfo(list);
        
        List<DonationRechargeCardDTO> donationRechargeCardDTOList = new ArrayList<>();
        list.forEach(s->{
        	DonationRechargeCardDTO donationRechargeCardDTO = new DonationRechargeCardDTO();
        	donationRechargeCardDTO.setRechargeCardId(String.valueOf(s.getRechargeCardId()));
        	donationRechargeCardDTO.setDescription(s.getDescription());
        	donationRechargeCardDTO.setName(s.getName());
        	donationRechargeCardDTO.setType(String.valueOf(s.getType()));
        	donationRechargeCardDTO.setTypeLabel(0 == s.getType()?"新用户专享,仅限1次":"单笔充值");
        	donationRechargeCardDTO.setRealValue(String.valueOf(s.getRealValue().intValue()));
        	donationRechargeCardDTOList.add(donationRechargeCardDTO);
        });
        
		PageInfo<DonationRechargeCardDTO> result = new PageInfo<DonationRechargeCardDTO>();
		try {
			BeanUtils.copyProperties(pageInfo, result);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		result.setList(donationRechargeCardDTOList);
        return ResultGenerator.genSuccessResult("success",result);
	}
}
