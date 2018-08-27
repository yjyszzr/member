package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.DonationRechargeCardDTO;
import com.dl.member.dto.RechargeActivityDTO;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.model.DonationRechargeCard;
import com.dl.member.param.PageParam;
import com.dl.member.service.DonationRechargeCardService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
* Created by CodeGenerator on 2018/05/30.
*/
@RestController
@RequestMapping("/donation/rechargeCard")

public class DonationRechargeCardController {
    @Resource
    private DonationRechargeCardService donationRechargeCardService;

    @PostMapping("/list")
    public BaseResult<RechargeActivityDTO> list(@RequestBody PageParam pageParam) {
    	return donationRechargeCardService.queryAllRechargeCardsNew(pageParam);
    }
}
