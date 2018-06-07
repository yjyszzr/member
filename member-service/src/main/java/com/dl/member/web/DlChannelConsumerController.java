package com.dl.member.web;

import io.swagger.annotations.ApiOperation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtilNew;
import com.dl.base.util.IpUtil;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.RegexUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dto.ChannelDistributorDTO;
import com.dl.member.dto.DlWinningLogDTO;
import com.dl.member.dto.IncomeDetailsDTO;
import com.dl.member.dto.PromotionIncomeDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.DlChannel;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.DlChannelDistributor;
import com.dl.member.model.LotteryWinningLogTemp;
import com.dl.member.model.User;
import com.dl.member.param.ConsumerSmsParam;
import com.dl.member.param.DistributorSmsParam;
import com.dl.member.param.DlChannelConsumerParam;
import com.dl.member.param.DlChannelDistributorParam;
import com.dl.member.param.MyQRCodeParam;
import com.dl.member.param.SmsParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.UserReceiveLotteryAwardParam;
import com.dl.member.service.DlChannelConsumerService;
import com.dl.member.service.DlChannelDistributorService;
import com.dl.member.service.DlChannelService;
import com.dl.member.service.LotteryWinningLogTempService;
import com.dl.member.service.SmsService;
import com.dl.member.service.UserAccountService;
import com.dl.member.service.UserService;

/**
 * Created by CodeGenerator on 2018/05/18.
 */
@RestController
@RequestMapping("/dl/channelConsumer")
public class DlChannelConsumerController {
	@Resource
	private DlChannelService dlChannelService;
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
	@Resource
	private LotteryWinningLogTempService lotteryWinningLogTempService;

	@ApiOperation(value = "我的推荐", notes = "我的推荐")
	@PostMapping("/myRecommendation")
	public BaseResult<ChannelDistributorDTO> myRecommendation(@RequestBody DlChannelDistributorParam param) {
		// param.setUserId(400160);
		param.setUserId(SessionUtil.getUserId());
		ChannelDistributorDTO channelDistributor = new ChannelDistributorDTO();
		channelDistributor = dlChannelDistributorService.getMyRankingListBak(param);
		return ResultGenerator.genSuccessResult("success", channelDistributor);
	}

	@ApiOperation(value = "我的推广收入", notes = "我的推广收入")
	@PostMapping("/myPromotionIncome")
	public BaseResult<List<PromotionIncomeDTO>> myPromotionIncome(@RequestBody DlChannelDistributorParam param) {
		param.setUserId(SessionUtil.getUserId());
		// param.setUserId(400160);
		List<PromotionIncomeDTO> promotionIncomes = new ArrayList<PromotionIncomeDTO>();
		promotionIncomes = dlChannelDistributorService.getPromotionIncomeListBak(param);
		return ResultGenerator.genSuccessResult("success", promotionIncomes);
	}

	@ApiOperation(value = "收入明细", notes = "收入明细")
	@PostMapping("/incomeDetails")
	public BaseResult<List<IncomeDetailsDTO>> incomeDetails(@RequestBody DlChannelConsumerParam param) {
		param.setUserId(SessionUtil.getUserId());
		// param.setUserId(400160);
		List<IncomeDetailsDTO> incomeDetailss = new ArrayList<IncomeDetailsDTO>();
		DlChannelDistributor channelDistributor = dlChannelDistributorService.findByUserId(param.getUserId());
		if (channelDistributor != null) {
			incomeDetailss = dlChannelConsumerService.getIncomeDetailsListBak(channelDistributor.getChannelDistributorId(), param.getAddTime(), channelDistributor.getDistributorCommissionRate());
		}
		return ResultGenerator.genSuccessResult("success", incomeDetailss);
	}

	@ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
	@PostMapping("/smsCode")
	public BaseResult<String> sendServiceSms(@RequestBody ConsumerSmsParam smsParam, HttpServletRequest request) {
		String smsType = smsParam.getSmsType();
		String tplId = "";
		String tplValue = "";
		String strRandom4 = RandomUtil.getRandNum(4);
		if (ProjectConstant.VERIFY_TYPE_REG.equals(smsType)) {
			User user = userService.findBy("mobile", smsParam.getMobile());
			if (user != null) {
				return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), MemberEnums.ALREADY_REGISTER.getMsg());
			}

			if (!RegexUtil.checkMobile(smsParam.getMobile())) {
				return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
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
			DlChannelDistributor distributor = dlChannelDistributorService.findByUserId(smsParam.getUserId());
			if (null != distributor) {
				DlChannelConsumer channelConsumer = dlChannelConsumerService.selectByChannelDistributorIdAndMobile(distributor.getChannelDistributorId(), smsParam.getMobile());
				if (channelConsumer == null) {
					DlChannelConsumer dlChannelConsumer = new DlChannelConsumer();
					dlChannelConsumer.setAddTime(DateUtilNew.getCurrentTimeLong());
					dlChannelConsumer.setDeleted(0);
					dlChannelConsumer.setConsumerId(0);
					dlChannelConsumer.setChannelDistributorId(distributor.getChannelDistributorId());
					dlChannelConsumer.setConsumerIp(IpUtil.getIpAddr(request));
					dlChannelConsumer.setMobile(smsParam.getMobile());
					dlChannelConsumerService.save(dlChannelConsumer);
					return ResultGenerator.genSuccessResult("发送短信验证码成功", distributor.getChannelDistributorId().toString());
				}
				return ResultGenerator.genSuccessResult("发送短信验证码成功", distributor.getChannelDistributorId().toString());
			}
			return ResultGenerator.genFailResult("参数异常");
		} else {
			return ResultGenerator.genFailResult("参数异常");
		}
	}

	@ApiOperation(value = "领取彩金", notes = "领取彩金")
	@PostMapping("/receiveLotteryAward")
	public BaseResult<Integer> receiveLotteryAward(@RequestBody UserReceiveLotteryAwardParam userReceiveLotteryAwardParam, HttpServletRequest request) {
		String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.REGISTER_TPLID + "_" + userReceiveLotteryAwardParam.getMobile());
		if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(userReceiveLotteryAwardParam.getSmsCode())) {
			return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
		}
		// 领取礼金操作

		return dlChannelConsumerService.receiveLotteryAward(userReceiveLotteryAwardParam, request);
	}

	@ApiOperation(value = "我的二维码", notes = "我的二维码")
	@PostMapping("/myQRCode")
	public BaseResult<MyQRCodeParam> myQRCode(@RequestBody StrParam strParam) {
		MyQRCodeParam myQRCodeParam = new MyQRCodeParam();
		myQRCodeParam.setUserId(SessionUtil.getUserId());
		myQRCodeParam.setUrl("http://192.168.31.205:8080/static/activity_Back/tuiguang/index.html?");
		return ResultGenerator.genSuccessResult("获取成功", myQRCodeParam);
	}

	@ApiOperation(value = "大奖喜报列表", notes = "大奖喜报列表")
	@PostMapping("/getWinningList")
	public BaseResult<List<DlWinningLogDTO>> winningList(@RequestBody StrParam strParam) {
		List<LotteryWinningLogTemp> lotteryWinningLogTemps = lotteryWinningLogTempService.selectWinningLogByIsShow();
		List<DlWinningLogDTO> winningLogList = new ArrayList<DlWinningLogDTO>();
		if (CollectionUtils.isNotEmpty(lotteryWinningLogTemps)) {
			for (LotteryWinningLogTemp winningLog : lotteryWinningLogTemps) {
				DlWinningLogDTO dlWinningLogDTO = new DlWinningLogDTO();
				String phone = winningLog.getPhone();
				phone = phone.substring(0, 3) + "****" + phone.substring(7);
				dlWinningLogDTO.setWinningMsg(MessageFormat.format(ProjectConstant.FORMAT_WINNING_MSG, phone));
				dlWinningLogDTO.setWinningMoney(winningLog.getWinningMoney().toString());
				winningLogList.add(dlWinningLogDTO);
			}
		}
		return ResultGenerator.genSuccessResult("获取成功", winningLogList);
	}

	@ApiOperation(value = "发送推广员短信验证码", notes = "发送推广员短信验证码")
	@PostMapping("/smsCodeForDistributor")
	public BaseResult<String> smsCodeForDistributor(@RequestBody SmsParam smsParam) {
		String smsType = smsParam.getSmsType();
		String tplId = "";
		String tplValue = "";
		String strRandom4 = RandomUtil.getRandNum(4);
		if (ProjectConstant.VERIFY_TYPE_REG.equals(smsType)) {
			User user = userService.findBy("mobile", smsParam.getMobile());
			if (user != null) {// 校验手机号是否存在
				return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), MemberEnums.ALREADY_REGISTER.getMsg());
			}
			if (!RegexUtil.checkMobile(smsParam.getMobile())) {// 校验手机号合不合法
				return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
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
			return ResultGenerator.genSuccessResult("发送短信验证码成功");
		} else {
			return ResultGenerator.genFailResult("参数异常");
		}
	}

	@ApiOperation(value = "注册成推广员", notes = "注册成推广员")
	@PostMapping("/registToDistributor")
	public BaseResult<String> registToDistributor(@RequestBody DistributorSmsParam smsParam, HttpServletRequest request) {
		String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.REGISTER_TPLID + "_" + smsParam.getMobile());
		if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(smsParam.getSmsCode())) {
			return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
		}
		// 短信发送成功执行店员保存操作
		return dlChannelDistributorService.saveUserAndDistributor(smsParam, request);
	}

	@ApiOperation(value = "获取店铺列表", notes = "获取店铺列表")
	@PostMapping("/getChannelList")
	public BaseResult<List<DlChannel>> getChannelList() {
		List<DlChannel> channelList = new ArrayList<DlChannel>();
		channelList = dlChannelService.findAll();
		return ResultGenerator.genSuccessResult("获取成功", channelList);
	}
}
