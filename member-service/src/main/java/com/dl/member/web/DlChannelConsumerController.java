package com.dl.member.web;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtilNew;
import com.dl.base.util.RandomUtil;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dto.ChannelDistributorDTO;
import com.dl.member.dto.IncomeDetailsDTO;
import com.dl.member.dto.PromotionIncomeDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.DlChannelDistributor;
import com.dl.member.model.User;
import com.dl.member.param.ConsumerSmsParam;
import com.dl.member.param.DlChannelConsumerParam;
import com.dl.member.param.DlChannelDistributorParam;
import com.dl.member.param.UserReceiveLotteryAwardParam;
import com.dl.member.service.DlChannelConsumerService;
import com.dl.member.service.DlChannelDistributorService;
import com.dl.member.service.SmsService;
import com.dl.member.service.UserAccountService;
import com.dl.member.service.UserService;
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
	@Resource
	private UserAccountService userAccountService;
	@Resource
	private UserService userService;
	@Resource
	private MemberConfig memberConfig;
	@Resource
	private SmsService smsService;
	@Resource
	private StringRedisTemplate stringRedisTemplate;

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
	public BaseResult<ChannelDistributorDTO> myRecommendation(@RequestBody DlChannelDistributorParam param) {
		ChannelDistributorDTO channelDistributor = new ChannelDistributorDTO();
		channelDistributor = dlChannelDistributorService.getMyRankingList(param);
		return ResultGenerator.genSuccessResult("success", channelDistributor);
	}

	@ApiOperation(value = "我的推广收入", notes = "我的推广收入")
	@PostMapping("/myPromotionIncome")
	public BaseResult<List<PromotionIncomeDTO>> myPromotionIncome(@RequestBody DlChannelDistributorParam param) {
		List<PromotionIncomeDTO> promotionIncomes = new ArrayList<PromotionIncomeDTO>();
		promotionIncomes = dlChannelDistributorService.getPromotionIncomeList(param);
		return ResultGenerator.genSuccessResult("success", promotionIncomes);
	}

	@ApiOperation(value = "收入明细", notes = "收入明细")
	@PostMapping("/incomeDetails")
	public BaseResult<List<IncomeDetailsDTO>> incomeDetails(@RequestBody DlChannelConsumerParam param) {
		List<IncomeDetailsDTO> incomeDetailss = new ArrayList<IncomeDetailsDTO>();
		DlChannelDistributor channelDistributor = dlChannelDistributorService.findByUserId(param.getUserId());
		incomeDetailss = dlChannelConsumerService.getIncomeDetailsList(channelDistributor.getChannelId(), param.getAddTime(), channelDistributor.getDistributorCommissionRate());
		return ResultGenerator.genSuccessResult("success", incomeDetailss);
	}

	@ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
	@PostMapping("/smsCode")
	public BaseResult<String> sendServiceSms(@RequestBody ConsumerSmsParam smsParam) {
		String smsType = smsParam.getSmsType();
		String tplId = "";
		String tplValue = "";
		String strRandom4 = RandomUtil.getRandNum(4);
		if (ProjectConstant.VERIFY_TYPE_REG.equals(smsType)) {
			User user = userService.findBy("mobile", smsParam.getMobile());
			if (user != null) {
				return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), MemberEnums.ALREADY_REGISTER.getMsg());
			}
			tplId = memberConfig.getREGISTER_TPLID();
			tplValue = "#code#=" + strRandom4;
		}
		if (!TextUtils.isEmpty(tplValue)) {
			BaseResult<String> smsRst = smsService.sendSms(smsParam.getMobile(), tplId, tplValue);
			if (smsRst.getCode() != 0) {
				return ResultGenerator.genFailResult("发送短信验证码失败", smsRst.getData());
			}
			// 缓存验证码
			int expiredTime = ProjectConstant.SMS_REDIS_EXPIRED;
			String key = ProjectConstant.SMS_PREFIX + tplId + "_" + smsParam.getMobile();
			stringRedisTemplate.opsForValue().set(key, strRandom4, expiredTime, TimeUnit.SECONDS);
			// 短信发送成功执行保存操作
			DlChannelConsumer dlChannelConsumer = new DlChannelConsumer();
			dlChannelConsumer.setAddTime(DateUtilNew.getCurrentTimeLong());
			dlChannelConsumer.setDeleted(0);
			dlChannelConsumer.setConsumerId(0);
			dlChannelConsumer.setChannelDistributorId(smsParam.getUserId());
			dlChannelConsumer.setConsumerIp(smsParam.getConsumerIp());
			dlChannelConsumer.setMobile(smsParam.getMobile());
			dlChannelConsumerService.save(dlChannelConsumer);
			return ResultGenerator.genSuccessResult("发送短信验证码成功");
		} else {
			return ResultGenerator.genFailResult("参数异常");
		}
	}

	@ApiOperation(value = "领取彩金", notes = "领取彩金")
	@PostMapping("/receiveLotteryAward")
	public BaseResult<String> receiveLotteryAward(UserReceiveLotteryAwardParam userReceiveLotteryAwardParam, HttpServletRequest request) {
		String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.LOGIN_TPLID + "_" + userReceiveLotteryAwardParam.getMobile());
		if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(userReceiveLotteryAwardParam.getSmsCode())) {
			return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
		}
		// 领取礼金操作
		dlChannelConsumerService.receiveLotteryAward(userReceiveLotteryAwardParam, request);
		return ResultGenerator.genSuccessResult("领取成功");
	}

	@ApiOperation(value = "我的二维码", notes = "我的二维码")
	@PostMapping("/myQRCode")
	public BaseResult<String> myQRCode(UserReceiveLotteryAwardParam userReceiveLotteryAwardParam, HttpServletRequest request) {

		return ResultGenerator.genSuccessResult("获取成功");
	}

}
