package com.dl.member.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.model.UserDeviceInfo;
import com.dl.base.util.SessionUtil;
import com.dl.member.param.IDFACallBackParam;
import com.dl.member.param.IDFACheckParam;
import com.dl.member.param.IDFAClickParam;
import com.dl.member.service.IDFAService;
import com.dl.member.util.IpAdrressUtil;

import io.swagger.annotations.Api;

/**
 * 用户登录接口 zhangzirong
 */
@Api(value = "IDFA对外接口（排重、点击接口）")
@RestController
@RequestMapping("/Idfa")
public class IDFACheckController {
	@Resource
	private IDFAService iDFAService;
	
	@Resource
	private HttpServletRequest request;
	
	@Value("${advertiserIP}")
	private String advertiserIP;
	
	//排重接口
	@PostMapping("/checkDataByIdfa")
	public Map<String, String> checkDataByIdfa(IDFACheckParam idfaCheckParam) {
		String ip = IpAdrressUtil.getIpAdrress(request);
		List<String> ipList = Arrays.asList(advertiserIP.split("##"));
		if(ipList.contains(ip)) {
			return iDFAService.checkDataByIdfa(idfaCheckParam);
		}
		return null;
	}

	//点击接口
	@PostMapping("/clickIDFA")
	public Map<String, String> clickIDFA(IDFAClickParam idfaClickParam) {
		String ip = IpAdrressUtil.getIpAdrress(request);
		String ip2 = request.getRemoteAddr();
		List<String> ipList = Arrays.asList(advertiserIP.split("##"));
		idfaClickParam.setCallback(ip+"|"+ip2);
//		if(ipList.contains(ip)) {
			return iDFAService.saveOrUpdateClickData(idfaClickParam);
//		}
//		return null;
	}
	
}
