package com.dl.member.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.DLActivityMapper;
import com.dl.member.dao.DonationRechargeCardMapper;
import com.dl.member.dto.DonationRechargeCardDTO;
import com.dl.member.dto.RechargeActivityDTO;
import com.dl.member.model.DLActivity;
import com.dl.member.model.DonationRechargeCard;
import com.dl.member.param.PageParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DonationRechargeCardService extends AbstractService<DonationRechargeCard> {
    @Resource
    private DonationRechargeCardMapper donationRechargeCardMapper;

    @Resource
    private DLActivityMapper dLActivityMapper;
    
	public BaseResult<RechargeActivityDTO> queryAllRechargeCards(PageParam pageParam) {
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
        
    	DLActivity dLActivity = dLActivityMapper.queryActivityByType(ProjectConstant.RECHARGE_ACT);
        RechargeActivityDTO rechargeActivityDTO = new RechargeActivityDTO();
        if(dLActivity.getIsFinish() == true) {//活动失效 
            rechargeActivityDTO.setStartTime(1514736000);
            rechargeActivityDTO.setEndTime(1514822400);
        }else {//活动有效
            rechargeActivityDTO.setStartTime(dLActivity.getStartTime());
            rechargeActivityDTO.setEndTime(dLActivity.getEndTime());
        }
        rechargeActivityDTO.setRechargeCardList(donationRechargeCardDTOList);
        return ResultGenerator.genSuccessResult("success",rechargeActivityDTO);
	}
}
