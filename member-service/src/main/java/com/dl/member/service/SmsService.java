package com.dl.member.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.enums.ThirdApiEnum;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.RegexUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.api.IUserAccountService;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserMapper;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.MemberThirdApiLog;
import com.dl.member.model.User;
import com.dl.member.param.SmsParam;
import com.dl.member.param.SysConfigParam;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
//import com.aliyuncs.exceptions.ClientException;
//import com.aliyuncs.http.MethodType;
//import com.aliyuncs.profile.DefaultProfile;
//import com.aliyuncs.profile.IClientProfile;

@Service
@Slf4j
public class SmsService {

	@Resource
    private IUserAccountService iUserAccountService;
	
	@Resource
	private RestTemplateConfig restTemplateConfig;

	@Resource
	private RestTemplate restTemplate;

	@Resource
	private MemberConfig memberConfig;

	@Resource
	private UserMapper userMapper;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private SmsService smsService;

	@Resource
	private UserService userService;

	@Resource
	private SmsTemplateService smsTemplateService;

	@Resource
	private DlPhoneChannelService dlPhoneChannelService;

	/**
	 * 发送短信
	 * 
	 * @param smsParam
	 * @return
	 */
	public BaseResult<String> sendSms(SmsParam smsParam) {
		
		
		
		if (!RegexUtil.checkMobile(smsParam.getMobile())) {
			return ResultGenerator.genResult(MemberEnums.MOBILE_VALID_ERROR.getcode(), MemberEnums.MOBILE_VALID_ERROR.getMsg());
		}
		String smsType = smsParam.getSmsType();
//		if("1".equals(smsType)) {
//			//判断当前注册手机号是否为app_code_name=10
//			if(userMapper.queryUserByMobileAndAppCdde(smsParam.getMobile(), "10")==null) {
//				return ResultGenerator.genResult(301065, "注册系统正在维护，请稍后再试");
//			}
//		} 
		if("4".equals(smsType)) {//商户余额预警
			log.info("sendSms()执行商户余额预警：one");
			int num = 0;
			String sendNumKey = "num_send_yj_" + smsParam.getMobile();
			try {
				String sendNumValue = stringRedisTemplate.opsForValue().get(sendNumKey);
				if (StringUtils.isNotBlank(sendNumValue)) {
					num = Integer.parseInt(sendNumValue);
				}
			} catch (NumberFormatException e) {
				log.error("发送短信获取redis中短信发送的数量异常", e);
				return ResultGenerator.genFailResult("获取短信发送数量异常");
			}
			log.info("sendSms()执行商户余额预警：two");
			if (num >=1) {//每天预警异常
				return ResultGenerator.genResult(MemberEnums.MESSAGE_COUNT_FUND_ERROR.getcode(), MemberEnums.MESSAGE_COUNT_FUND_ERROR.getMsg());
			}
			String tplId = "";
			String tplValue = "";
			SysConfigParam cfg = new SysConfigParam();
			cfg.setBusinessId(68);//读取财务账号id
			Integer smsTemplateId = iUserAccountService.queryBusinessLimit(cfg).getData()!=null?iUserAccountService.queryBusinessLimit(cfg).getData().getValue().intValue():0;
			if (0 == smsTemplateId) {
				log.warn("未查询到短信模板id的配置，请检查数据库");
				return ResultGenerator.genFailResult("短信发送异常,请联系管理员");
			}
			log.info("sendSms()执行商户余额预警：three");
			tplId = String.valueOf(smsTemplateId);
			BaseResult<String> smsRst = smsService.sendJuheSms(smsParam.getMobile(), tplId, tplValue);
			log.info("sendSms()执行商户余额预警：smsRst="+smsRst.getCode());
			if (smsRst.getCode() != 0) {
				return ResultGenerator.genFailResult("商户预警失败", smsRst.getData());
			}
			num++;
			int sendNumExpire = this.todayEndTime();
			stringRedisTemplate.opsForValue().set(sendNumKey, num + "", sendNumExpire, TimeUnit.DAYS);
		} else {
			UserDeviceInfo userDeviceInfo = SessionUtil.getUserDevice();
			String appCodeNameStr = org.apache.commons.lang.StringUtils.isEmpty(userDeviceInfo.getAppCodeName())?"10":userDeviceInfo.getAppCodeName();
			User user = userMapper.queryUserByMobileAndAppCdde(smsParam.getMobile(),appCodeNameStr);
			if (ProjectConstant.VERIFY_TYPE_LOGIN.equals(smsType) || ProjectConstant.VERIFY_TYPE_FORGET.equals(smsType)) {// 登录，忘记密码
				if (null == user) {
					return ResultGenerator.genResult(MemberEnums.NO_REGISTER.getcode(), MemberEnums.NO_REGISTER.getMsg());
				}
			} else if (ProjectConstant.VERIFY_TYPE_REG.equals(smsType)) {// 注册
				if (null != user) {
					return ResultGenerator.genResult(MemberEnums.ALREADY_REGISTER.getcode(), MemberEnums.ALREADY_REGISTER.getMsg());
				}
			}
	
			int num = 0;
			String sendNumKey = "num_send_" + smsParam.getMobile();
			String sendNum3Key = sendNumKey + "_3";
			String sendNum4Key = sendNumKey + "_4";
			try {
				String sendNumValue = stringRedisTemplate.opsForValue().get(sendNumKey);
				if (StringUtils.isNotBlank(sendNumValue)) {
					num = Integer.parseInt(sendNumValue);
				}
			} catch (NumberFormatException e) {
				log.error("发送短信获取redis中短信发送的数量异常", e);
				return ResultGenerator.genFailResult("获取短信发送数量异常");
			}
	
			// 短信api规定,参考https://www.juhe.cn/docs/api/id/54
			Long expireTimeLimit10 = stringRedisTemplate.getExpire(sendNum3Key);
			if (num == 3 && expireTimeLimit10 < 600 && expireTimeLimit10 > 0) {// 聚合规定：10min
																				// 内不能超过3条
				return ResultGenerator.genResult(MemberEnums.MESSAGE_10MIN_COUNT_ERROR.getcode(), MemberEnums.MESSAGE_10MIN_COUNT_ERROR.getMsg());
			}
			Long expireTimeLimit60 = stringRedisTemplate.getExpire(sendNum4Key);
			if (num == 4 && expireTimeLimit60 < 3600 && expireTimeLimit60 > 0) {// 聚合规定：60min
																				// 内不能超过4条
				return ResultGenerator.genResult(MemberEnums.MESSAGE_60MIN_COUNT_ERROR.getcode(), MemberEnums.MESSAGE_60MIN_COUNT_ERROR.getMsg());
			}
			if (num >= 10) {
				return ResultGenerator.genResult(MemberEnums.MESSAGE_COUNT_ERROR.getcode(), MemberEnums.MESSAGE_COUNT_ERROR.getMsg());
			}
			String sameMobileKey = ProjectConstant.SMS_PREFIX + smsParam.getMobile();
			Long expireTime = stringRedisTemplate.getExpire(sameMobileKey);
			if (null != expireTime && expireTime < 60 && expireTime > 0) {
				return ResultGenerator.genResult(MemberEnums.MESSAGE_SENDLOT_ERROR.getcode(), MemberEnums.MESSAGE_SENDLOT_ERROR.getMsg());
			}
	
			String tplId = "";
			String tplValue = "";
			String strRandom4 = RandomUtil.getRandNum(4);
			UserDeviceInfo userDevice = SessionUtil.getUserDevice();
			Integer appCodeName = 11;// 默认球多多
			Integer smsTemplateId = smsTemplateService.querySmsByAppCodeName(appCodeName);
			if (null == smsTemplateId) {
				log.warn("未查询到短信模板id的配置，请检查数据库");
				return ResultGenerator.genFailResult("短信发送异常,请联系管理员");
			}
			tplId = String.valueOf(smsTemplateId);
			tplValue = "#code#=" + strRandom4 + "&#m#=" + 5;
	
			// 缓存验证码
			int defineExpiredTime = ProjectConstant.SMS_REDIS_EXPIRED;
			String key = ProjectConstant.SMS_PREFIX + smsType + "_" + smsParam.getMobile();
			stringRedisTemplate.opsForValue().set(sameMobileKey, strRandom4, 300, TimeUnit.SECONDS);
	
			BaseResult<String> smsRst = smsService.sendJuheSms(smsParam.getMobile(), tplId, tplValue);
			if (smsRst.getCode() != 0) {
				return ResultGenerator.genFailResult("发送短信验证码失败", smsRst.getData());
			}
	
			num++;
			stringRedisTemplate.opsForValue().set(key, strRandom4, defineExpiredTime, TimeUnit.SECONDS);
			int sendNumExpire = this.todayEndTime();
			stringRedisTemplate.opsForValue().set(sendNumKey, num + "", sendNumExpire, TimeUnit.SECONDS);
			stringRedisTemplate.opsForValue().set(sendNum3Key, num + "", 600, TimeUnit.SECONDS);
			stringRedisTemplate.opsForValue().set(sendNum4Key, num + "", 3600, TimeUnit.SECONDS);
		}
		return ResultGenerator.genSuccessResult("发送短信验证码成功");
	}

	private int todayEndTime() {
		Calendar date = Calendar.getInstance();
		int hour = date.get(Calendar.HOUR_OF_DAY);
		int minute = date.get(Calendar.MINUTE);
		int second = date.get(Calendar.SECOND);
		return (59 - second) + (59 - minute) * 60 + (23 - hour) * 3600;
	}

	/**
	 * 发送短信:聚合
	 * 
	 * @param mobile
	 * @return
	 */
	public BaseResult<String> sendJuheSms(String mobile, String tplId, String tplValue) {
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		try {
			tplValue = URLEncoder.encode(tplValue, "UTF-8");
		} catch (Exception e1) {
			log.error(e1.getMessage());
		}
		StringBuffer url = new StringBuffer(memberConfig.getJuheSmsApiUrl());
		url.append("?mobile=" + mobile);
		url.append("&tpl_id=" + tplId);
		url.append("&tpl_value=" + tplValue);
		url.append("&key=" + memberConfig.getJuheSmsKey());
		String rst = rest.getForObject(url.toString(), String.class);

		MemberThirdApiLog thirdApiLog = new MemberThirdApiLog(memberConfig.getJuheSmsApiUrl(), ThirdApiEnum.JU_HE.getCode(), url.toString(), rst);
		userMapper.saveMemberThirdApiLog(thirdApiLog);

		JSONObject json = null;
		try {
			json = JSON.parseObject(rst);
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error("短信发送失败。");
		}

		JSONObject json_tmp = (JSONObject) json.get("result");
		Integer errorCode = (Integer) json.get("error_code");
		if (0 == errorCode) {
			return ResultGenerator.genSuccessResult("发送短信成功");
		} else {
			log.error("手机号:" + mobile + "发送短信失败，原因:" + json.toString());
			return ResultGenerator.genBadRequestResult("调用第三方发送短信失败", String.valueOf(errorCode));
		}

	}

	/**
	 * 阿里大于短信
	 * 
	 * @param smsParams
	 * @return
	 */
	// public boolean sendSmsByAliYu(String templateCode, String mobiles, String
	// jsonParams) {
	// //设置超时时间-可自行调整
	// System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
	// System.setProperty("sun.net.client.defaultReadTimeout", "10000");
	// //初始化ascClient需要的几个参数
	// final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
	// final String domain =
	// "dysmsapi.aliyuncs.com";//"dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
	// //替换成你的AK
	// final String accessKeyId =
	// memberConfig.getAliSmsAccessKeyId();//你的accessKeyId,参考本文档步骤2
	// final String accessKeySecret =
	// memberConfig.getAliSmsAccessKeySecret();//你的accessKeySecret，参考本文档步骤2
	// //初始化ascClient,暂时不支持多region（请勿修改）
	// IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
	// accessKeyId,accessKeySecret);
	// try {
	// DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product,
	// domain);
	// } catch (ClientException e) {
	// e.printStackTrace();
	// }
	// IAcsClient acsClient = new DefaultAcsClient(profile);
	// //组装请求对象
	// SendSmsRequest request = new SendSmsRequest();
	// //使用post提交
	// request.setMethod(MethodType.POST);
	// //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
	// request.setPhoneNumbers(mobiles);
	// //必填:短信签名-可在短信控制台中找到
	// TODO systemConfigService.getValue(SMS_SIGN_NAME, isCached)
	// request.setSignName("");
	// //必填:短信模板-可在短信控制台中找到
	// request.setTemplateCode(templateCode);
	// //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
	// //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
	// request.setTemplateParam(jsonParams);
	// //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
	// //request.setSmsUpExtendCode("90997");
	// //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
	// //request.setOutId("yourOutId");
	// //请求失败这里会抛ClientException异常
	// try {
	// SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
	// if(sendSmsResponse.getCode() != null &&
	// sendSmsResponse.getCode().equals("OK")) {
	// return true;
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

}
