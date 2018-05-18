package com.dl.member.web;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.ChannelDistributorDTO;
import com.dl.member.dto.IncomeRankingDTO;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.DlChannelDistributor;
import com.dl.member.param.DlChannelDistributorParam;
import com.dl.member.service.DlChannelConsumerService;
import com.dl.member.service.DlChannelDistributorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by CodeGenerator on 2018/05/18.
 */
@RestController
@RequestMapping("/dl/channelConsumer")
public class DlChannelConsumerController {
	@Resource
	private DlChannelConsumerService dlChannelConsumerService;
	@Resource
	private DlChannelDistributorService dlChannelDistributorService;

	@ApiOperation(value = "顾客添加", notes = "顾客添加")
	@PostMapping("/add")
	public BaseResult add(DlChannelConsumer dlChannelConsumer) {
		dlChannelConsumerService.save(dlChannelConsumer);
		return ResultGenerator.genSuccessResult();
	}

	@ApiOperation(value = "顾客删除", notes = "顾客删除")
	@PostMapping("/delete")
	public BaseResult delete(@RequestParam Integer id) {
		dlChannelConsumerService.deleteById(id);
		return ResultGenerator.genSuccessResult();
	}

	@ApiOperation(value = "顾客修改", notes = "顾客修改")
	@PostMapping("/update")
	public BaseResult update(DlChannelConsumer dlChannelConsumer) {
		dlChannelConsumerService.update(dlChannelConsumer);
		return ResultGenerator.genSuccessResult();
	}

	@ApiOperation(value = "顾客详情", notes = "顾客详情")
	@PostMapping("/detail")
	public BaseResult detail(@RequestParam Integer id) {
		DlChannelConsumer dlChannelConsumer = dlChannelConsumerService.findById(id);
		return ResultGenerator.genSuccessResult(null, dlChannelConsumer);
	}

	@ApiOperation(value = "顾客列表", notes = "顾客列表")
	@PostMapping("/list")
	public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
		PageHelper.startPage(page, size);
		List<DlChannelConsumer> list = dlChannelConsumerService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(null, pageInfo);
	}

	@ApiOperation(value = "我的推荐", notes = "我的推荐")
	@PostMapping("/myRecommendation")
	public BaseResult<ChannelDistributorDTO> list(@RequestBody DlChannelDistributorParam param) {
		ChannelDistributorDTO channelDistributor = new ChannelDistributorDTO();
		Integer inviteNum = dlChannelConsumerService.findinviteNumByUserId(param);
		Double bettingTotalAmount = dlChannelConsumerService.findBettingTotalAmount(param);
		List<DlChannelDistributor> channelDistributorList = dlChannelDistributorService.getAllDlChannelDistributor();
		channelDistributor.setInviteNum(inviteNum);
		channelDistributor.setBettingTotalAmount(bettingTotalAmount);
		List<IncomeRankingDTO> incomeRankingList = new ArrayList<IncomeRankingDTO>();
		for (int i = 0; i < channelDistributorList.size(); i++) {
			IncomeRankingDTO incomeRanking = new IncomeRankingDTO();
			incomeRanking.setRanking(i);
			incomeRanking.setDistributorMobile(channelDistributorList.get(i).getMobile());
			incomeRankingList.add(incomeRanking);
		}
		return ResultGenerator.genSuccessResult("success", channelDistributor);
	}
}
