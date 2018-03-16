package com.dl.member.service;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.dao.UserRealMapper;
import com.dl.member.dto.UserRealDTO;
import com.dl.member.model.User;
import com.dl.member.model.UserReal;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserRealService extends AbstractService<UserReal> {
    @Resource
    private UserRealMapper userRealMapper;
    
    @Resource
    private UserService userService;
    
	@Resource
	private RestTemplateConfig restTemplateConfig;
	
	@Resource
	private RestTemplate restTemplate;    
	
	@Resource
	private MemberConfig memberConfig;
    
    @Transactional
    public void saveUserReal(String realName,String idCode) {
    	UserReal userReal = new UserReal();
    	userReal.setRealName(realName);
    	userReal.setIdCode(idCode);
    	userReal.setAddressInfo("");
    	userReal.setAddressNow("");
    	userReal.setAddTime(DateUtil.getCurrentTimeLong());
    	userReal.setCardPic1("");
    	userReal.setCardPic2("");
    	userReal.setCardPic3("");
    	userReal.setReason("");
    	userReal.setStatus("1");
    	this.save(userReal);
    }
    
    /**
     * 实名认证
     * @param realName
     * @param iDCode
     * @return
     * @throws UnsupportedEncodingException 
     */
    public BaseResult<UserRealDTO> realNameAuth(String realName,String iDCode) {
    	Integer userId = SessionUtil.getUserId();
    	//TODO 身份证二元素校验接口
    	try {
			realName = URLDecoder.decode(realName, "UTF-8");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
    	BaseResult<String> realNameRst = this.realNameAuth2(realName, iDCode);
    	if(realNameRst.getCode() != 0) {
    		return ResultGenerator.genResult(realNameRst.getCode(), realNameRst.getMsg());
    	}
    	
    	this.saveUserReal(realName,iDCode);
    	User updateUser = new User();
    	updateUser.setIsReal(true);
    	userService.update(updateUser);
    	return ResultGenerator.genSuccessResult("实名认证成功");
    }

	/**
	 * 身份证实名认证
	 * @param realName
	 * @param cardNo
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public BaseResult<String> realNameAuth2(String realName,String idcard) {
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);

		StringBuffer url = new StringBuffer(memberConfig.getRalNameApiURL());
		url.append("?key=" + memberConfig.getRealNameKey());
		url.append("&realname=" + realName);
		url.append("&idcard=" + idcard);
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
			String res = json_tmp.getString("res");
			String reason = json_tmp.getString("reason");
			if("1".equals(res)) {
				return ResultGenerator.genSuccessResult(reason);
			}else {
				return ResultGenerator.genBadRequestResult(reason);
			}
		}else {
			return ResultGenerator.genBadRequestResult("调用第三方实名认证失败");
		}
		
	}    
    
    /**
     * 查询用户真实信息
     * @return
     */
    public UserRealDTO queryUserReal(){
    	Integer userId = SessionUtil.getUserId();
    	UserReal userReal = this.findBy("userId", userId);
    	UserRealDTO userRealDTO = new UserRealDTO();
    	if(null == userRealDTO) {
    		return null;
    	}
    	
    	try {
			BeanUtils.copyProperties(userRealDTO, userReal);
		} catch (Exception e) {
			throw new ServiceException(RespStatusEnum.SERVER_ERROR.getCode(), RespStatusEnum.SERVER_ERROR.getMsg());
		} 
    	return userRealDTO;
    }

}
