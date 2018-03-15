package com.dl.member.service;
import java.util.ArrayList;
import java.util.List;

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
import com.dl.base.util.SessionUtil;
import com.dl.member.dao.UserBankMapper;
import com.dl.member.dto.UserBankDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.UserBank;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserBankService extends AbstractService<UserBank> {
    @Resource
    private UserBankMapper userBankMapper;
    
	@Resource
	private RestTemplateConfig restTemplateConfig;
	
	@Resource
	private RestTemplate restTemplate;
    
    @Transactional
    public void saveUserBank(UserBankDTO userBankDTO) {
    	UserBank userBank = new UserBank();
    	Integer userId = SessionUtil.getUserId();
    	userBank.setUserId(userId);
    	userBank.setRealName(userBankDTO.getRealName());
    	userBank.setCardNo(userBankDTO.getCardNo());
    	userBank.setStatus("0");
    	
    	try {
    		this.save(userBank);
    	}catch (Exception e) {
			throw new ServiceException(RespStatusEnum.SERVER_ERROR.getCode(), RespStatusEnum.SERVER_ERROR.getMsg());
		}
    }

    /**
     * 添加银行卡
     * @param realName
     * @param bankCardNo
     * @param idCard
     * @return
     */
	public BaseResult<UserBankDTO> addBankCard(String realName,String bankCardNo,String idCard){
		Integer userId = SessionUtil.getUserId();
		UserBankDTO userBankDTO = new UserBankDTO();
		BaseResult<String> atuhRst = this.bankCardAuth3(realName, bankCardNo,idCard);
		if(atuhRst.getCode() == 0) {
			userBankDTO = this.queryUserBankType(bankCardNo);
		}else {
			throw new ServiceException(MemberEnums.VERIFY_BANKCARD_EROOR.getcode(), MemberEnums.VERIFY_BANKCARD_EROOR.getMsg());
		}
		userBankDTO.setRealName(realName);
		userBankDTO.setCardNo(bankCardNo);
		this.saveUserBank(userBankDTO);
		return ResultGenerator.genSuccessResult("银行卡添加成功",userBankDTO);
	}
	
	
	/**
	 * 银行卡三元素校验
	 * @param realName
	 * @param cardNo
	 * @return
	 */
	public BaseResult<String> bankCardAuth3(String realName,String cardNo,String idcard) {
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);

		StringBuffer url = new StringBuffer("http://v.juhe.cn/verifybankcard3/query");
		url.append("?key=" + "64ef489c912d56440f52be2de8991ca2");
		url.append("&realname=" + realName);
		url.append("&bankcard=" + cardNo);
		url.append("&idcard=" + idcard);
		url.append("&sign=" + "JHbad76ab18dffdedbceba688fdfc9c796");
		String rst = rest.getForObject(url.toString(), String.class);

		JSONObject json = null;
		try {
			json = JSON.parseObject(rst);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		JSONObject json_tmp = (JSONObject) json.get("result");
		String res = json_tmp.getString("res");
		String message = json_tmp.getString("message");
		if("1".equals(res)) {
			return ResultGenerator.genSuccessResult("银行卡校验成功",message);
		}else {
			return ResultGenerator.genBadRequestResult(message);
		}
	}
	
	/**
	 * 查询银行卡类型信息
	 * @param cardNo
	 * @return
	 */
	public UserBankDTO queryUserBankType(String cardNo) {
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);

		StringBuffer url = new StringBuffer("http://bankcardsilk.api.juhe.cn/bankcardsilk/query.php");
		url.append("?key=" + "4f084b2f83477b851dc40404dbef23ce");
		url.append("&num=" + "6214830100586527");
		String rst = rest.getForObject(url.toString(), String.class);

		JSONObject json = null;
		try {
			json = JSON.parseObject(rst);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		JSONObject json_tmp = (JSONObject) json.get("result");
		Integer errorCode = (Integer)json.getInteger("error_code");
		if(0 == errorCode) {
			UserBankDTO userBankDTO = new UserBankDTO();
			userBankDTO.setBankName(json_tmp.getString("bank")+json_tmp.getString("type"));
			userBankDTO.setBankLogo(json_tmp.getString("logo"));
			return userBankDTO;
		}else {
			return null;
		}
	}
	
	/**
	 * 查询用户银行卡列表
	 * @return
	 */
	public BaseResult<List<UserBankDTO>> queryUserBankList(){
		Integer userId = null;
		List<UserBank> userBankList = this.findByIds(String.valueOf(userId));
		List<UserBankDTO>  userBankDTOList = new ArrayList<>();
		for(UserBank userBank:userBankList) {
			UserBankDTO userBankDTO = new UserBankDTO();
			try {
				BeanUtils.copyProperties(userBankDTO, userBank);
			} catch (Exception e) {
				throw new ServiceException(RespStatusEnum.SERVER_ERROR.getCode(), RespStatusEnum.SERVER_ERROR.getMsg());
			} 
			userBankDTOList.add(userBankDTO);
		}
		return ResultGenerator.genSuccessResult("查询银行卡列表成功",userBankDTOList);
	}
}
