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
import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsService {

	@Resource
	private RestTemplateConfig restTemplateConfig;
	
	@Resource
	private RestTemplate restTemplate;    
	
	/**
	 * 发送短信
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
		
		StringBuffer url = new StringBuffer("http://v.juhe.cn/sms/send");
		url.append("?mobile=" + mobile);
		url.append("&tpl_id=" + tplId);
		url.append("&tpl_value=" + tplValue);
		url.append("&key=" + "659806f8ed3daa6a383cb8e4d41424f5");
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

}
