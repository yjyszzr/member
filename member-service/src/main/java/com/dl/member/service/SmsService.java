package com.dl.member.service;

import java.net.URLEncoder;
import javax.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.IAcsClient;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
//import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
//import com.aliyuncs.exceptions.ClientException;
//import com.aliyuncs.http.MethodType;
//import com.aliyuncs.profile.DefaultProfile;
//import com.aliyuncs.profile.IClientProfile;
import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.configurer.MemberConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsService {

	@Resource
	private RestTemplateConfig restTemplateConfig;
	
	@Resource
	private RestTemplate restTemplate;    
	
	@Resource
	private MemberConfig memberConfig;
	
	/**
	 * 发送短信:聚合
	 * @param mobile
	 * @return
	 */
	public BaseResult<String> sendSms(String mobile,String tplId,String tplValue) {
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
		
		JSONObject json = null;
		try {
			json = JSON.parseObject(rst);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		JSONObject json_tmp = (JSONObject) json.get("result");
		Integer errorCode =  (Integer) json.get("error_code");
		if(0 == errorCode) {
			return ResultGenerator.genSuccessResult("发送短信成功");
		}else {
			return ResultGenerator.genBadRequestResult("调用第三方发送短信失败",String.valueOf(errorCode));
		}
	}	
	
    /**
     * 阿里大于短信
     * @param smsParams
     * @return
     */
//    public boolean sendSmsByAliYu(String templateCode, String mobiles, String jsonParams) {
//    	//设置超时时间-可自行调整
//    	System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//    	System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//    	//初始化ascClient需要的几个参数
//    	final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
//    	final String domain = "dysmsapi.aliyuncs.com";//"dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
//    	//替换成你的AK
//    	final String accessKeyId = memberConfig.getAliSmsAccessKeyId();//你的accessKeyId,参考本文档步骤2
//    	final String accessKeySecret = memberConfig.getAliSmsAccessKeySecret();//你的accessKeySecret，参考本文档步骤2
//    	//初始化ascClient,暂时不支持多region（请勿修改）
//    	IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,accessKeySecret);
//    	try {
//			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
//		} catch (ClientException e) {
//			e.printStackTrace();
//		}
//    	IAcsClient acsClient = new DefaultAcsClient(profile);
//    	 //组装请求对象
//    	 SendSmsRequest request = new SendSmsRequest();
//    	 //使用post提交
//    	 request.setMethod(MethodType.POST);
//    	 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
//    	 request.setPhoneNumbers(mobiles);
//    	 //必填:短信签名-可在短信控制台中找到
////????????????????   	 //TODO systemConfigService.getValue(SMS_SIGN_NAME, isCached)
//    	 request.setSignName("");
//    	 //必填:短信模板-可在短信控制台中找到
//    	 request.setTemplateCode(templateCode);
//    	 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//    	 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
//    	 request.setTemplateParam(jsonParams);
//    	 //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
//    	 //request.setSmsUpExtendCode("90997");
//    	 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//    	 //request.setOutId("yourOutId");
//    	//请求失败这里会抛ClientException异常
//    	try {
//			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
//			if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
//				return true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	    return false;
//    }

}
