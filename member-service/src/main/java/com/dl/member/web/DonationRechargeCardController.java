package com.dl.member.web;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.member.dto.RechargeActivityDTO;
import com.dl.member.param.PageParam;
import com.dl.member.service.DonationRechargeCardService;

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
