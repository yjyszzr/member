package com.dl.member.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.enums.ThirdApiEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.RandomUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserBankCodeMapper;
import com.dl.member.dao.UserBankMapper;
import com.dl.member.dao.UserMapper;
import com.dl.member.dto.UserBankDTO;
import com.dl.member.dto.UserRealDTO;
import com.dl.member.dto.WithDrawShowDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.MemberThirdApiLog;
import com.dl.member.model.User;
import com.dl.member.model.UserBank;
import com.dl.member.model.UserBankCode;
import com.dl.member.param.DeleteBankCardParam;
import com.dl.member.param.UserBankQueryParam;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserBankService extends AbstractService<UserBank> {
    @Resource
    private UserBankMapper userBankMapper;
    @Resource
    private UserBankCodeMapper userBankCodeMapper;
    @Resource
    private UserRealService userRealService;
    
	@Resource
	private RestTemplateConfig restTemplateConfig;

	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	@Resource
	private RestTemplate restTemplate;
	
	@Resource
	private MemberConfig memberConfig;
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserMapper userMapper;

	private static Map<String,String> mMap = new HashMap<String,String>();
	
    @Transactional
    public void saveUserBank(UserBankDTO userBankDTO) {
    	UserBank userBank = new UserBank();
    	Integer userId = SessionUtil.getUserId();
    	userBank.setUserId(userId);
    	userBank.setRealName(userBankDTO.getRealName());
    	userBank.setCardNo(userBankDTO.getCardNo());
    	userBank.setStatus(ProjectConstant.USER_BANK_DEFAULT);
    	userBank.setBankLogo(userBankDTO.getBankLogo());
    	userBank.setBankName(userBankDTO.getBankName());
    	userBank.setAddTime(DateUtil.getCurrentTimeLong());
    	userBank.setLastTime(DateUtil.getCurrentTimeLong());
    	userBank.setAbbreviation(userBankDTO.getAbbreviation());
    	userBank.setType(userBankDTO.getType());
    	userBank.setPurpose(userBankDTO.getPurpose());
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
    @Transactional
	public BaseResult<UserBankDTO> addBankCard(String bankCardNo){
		//未实名认证
    	UserBankDTO userBankDTO = new UserBankDTO();
		UserRealDTO userRealDTO = userRealService.queryUserReal();
		if(null == userRealDTO) {
			return ResultGenerator.genResult(MemberEnums.NOT_REAL_AUTH.getcode(), MemberEnums.NOT_REAL_AUTH.NOT_REAL_AUTH.getMsg(),userBankDTO);
		}

		Integer userId = SessionUtil.getUserId();
		Boolean exits = stringRedisTemplate.opsForValue().setIfAbsent("user_bank_add_"+userId, "on");
		if(!exits) {
			return ResultGenerator.genResult(MemberEnums.USER_BANK_ADDING.getcode(), MemberEnums.USER_BANK_ADDING.getMsg(),userBankDTO);
		}else {
			log.info("添加银行卡extis:"+exits);
		}
		
		//已经添加过该银行卡
		UserBank userBankAlready = new UserBank();
		userBankAlready.setCardNo(bankCardNo);
		userBankAlready.setUserId(userId);
		userBankAlready.setIsDelete(ProjectConstant.NOT_DELETE);
		userBankAlready.setPurpose(ProjectConstant.BANK_PURPOSE_WITHDRAW);
		List<UserBank> userBankList = userBankMapper.queryUserBankBySelective(userBankAlready);
		if(!CollectionUtils.isEmpty(userBankList)) {
			return ResultGenerator.genResult(MemberEnums.BANKCARD_ALREADY_AUTH.getcode(), MemberEnums.BANKCARD_ALREADY_AUTH.getMsg(),userBankDTO);
		}
		
		//删除过银行卡，再添加，不再验证银行卡，直接把已删除的银行卡置为未删除和默认状态
//		UserBank userBankDelete = new UserBank();
//		userBankDelete.setCardNo(bankCardNo);
//		userBankDelete.setUserId(userId);
//		userBankDelete.setIsDelete(ProjectConstant.DELETE);
//		List<UserBank> userBankDeleteList = userBankMapper.queryUserBankBySelective(userBankDelete);
//		if(userBankDeleteList.size() > 0) {
//			UserBank userBankUpdate = new UserBank();
//			userBankUpdate.setId(userBankDeleteList.get(0).getId());
//			userBankUpdate.setStatus(ProjectConstant.USER_BANK_DEFAULT);
//			userBankUpdate.setIsDelete(ProjectConstant.NOT_DELETE);
//			int rst = userBankMapper.updateByPrimaryKeySelective(userBankUpdate);
//			if(1 == rst) {
//				UserBank userBank = userBankDeleteList.get(0);
//				try {
//					BeanUtils.copyProperties(userBankDTO, userBank);
//				} catch (Exception e) {
//					log.error("银行卡数据转换异常");
//					return ResultGenerator.genResult(MemberEnums.VERIFY_BANKCARD_EROOR.getcode(), MemberEnums.VERIFY_BANKCARD_EROOR.getMsg(),userBankDTO);
//				}
//			}
//			return ResultGenerator.genSuccessResult("银行卡添加成功",userBankDTO);
//		}

		//查询银行卡具体信息，并过滤信用卡
		JSONObject jsonNew =this.queryUserBankType(bankCardNo);
		Integer errorCodeNew = (Integer)jsonNew.getInteger("error_code");
		JSONObject json_tmp = (JSONObject) jsonNew.get("result");
		if(0 == errorCodeNew) {
			String cardtype = json_tmp.getString("cardtype");
			if(!"借记卡".equals(cardtype)) {
				return ResultGenerator.genResult(MemberEnums.NOT_DEBIT_CARD.getcode(), MemberEnums.NOT_DEBIT_CARD.getMsg());
			}
		}else if(230502 == errorCodeNew){
			return ResultGenerator.genResult(MemberEnums.BANKCARD_NOT_MATCH.getcode(), MemberEnums.BANKCARD_NOT_MATCH.getMsg());
		}else {
			log.error("判断银行卡类型异常："+jsonNew.toJSONString());
			return ResultGenerator.genResult(MemberEnums.VERIFY_BANKCARD_EROOR.getcode(), MemberEnums.VERIFY_BANKCARD_EROOR.getMsg(),userBankDTO);
		}
		
		String abbreviation = json_tmp.getString("abbreviation");
		String bankCardLogo = json_tmp.getString("banklogo");
		String bankName =  json_tmp.getString("bankname");
		
		String abbr = getAbbrByMap(bankName);
		if(!StringUtils.isEmpty(abbr)) {
			log.info("=============================");
			log.info("bankname查询到该银行简码:" + abbr +" bankName:" + bankName);
			log.info("=============================");
			abbreviation = abbr;
		}else {
			log.info("先锋支付不支持此家银行，所以不支持该银行绑定:" +" bankName:" + bankName);
			return ResultGenerator.genResult(MemberEnums.USER_BANK_NOT_SURPPORT.getcode(), MemberEnums.USER_BANK_NOT_SURPPORT.getMsg(),userBankDTO);
		}
		//三元素校验
		String idCard = userRealDTO.getIdCode();
		String realName = userRealDTO.getRealName();
		JSONObject json = this.bankCardAuth3(realName, bankCardNo,idCard);
		String reason = json.getString("reason");
		Integer errorCode = json.getInteger("error_code");
		if(0 == errorCode) {
			JSONObject result = (JSONObject) json.get("result");
			String res = result.getString("res");
			if("2".equals(res)) {//不匹配
				return ResultGenerator.genResult(MemberEnums.BANKCARD_NOT_MATCH.getcode(), MemberEnums.BANKCARD_NOT_MATCH.getMsg(),userBankDTO);
			}
		}else {
			return ResultGenerator.genResult(MemberEnums.VERIFY_BANKCARD_EROOR.getcode(), reason,userBankDTO);
		}

		//把已经添加的默认银行卡 设为非默认
		BaseResult<UserBankDTO> userBankDTORst = this.updateAlreadyAddCardStatus(ProjectConstant.USER_BANK_DEFAULT,0);
		if(userBankDTORst.getCode() != 0) {
			log.error(userBankDTORst.getMsg());
		}
		
		//保存到数据库
		userBankDTO.setBankName(bankName);
		userBankDTO.setBankLogo(bankCardLogo);
		userBankDTO.setRealName(realName);
		userBankDTO.setCardNo(bankCardNo);
		userBankDTO.setAbbreviation(abbreviation);
		userBankDTO.setStatus(ProjectConstant.USER_BANK_DEFAULT);
		userBankDTO.setType(0);
		userBankDTO.setPurpose(ProjectConstant.BANK_PURPOSE_WITHDRAW);
		this.saveUserBank(userBankDTO);
		
		stringRedisTemplate.delete("user_bank_add_"+userId);
		String redisValue = stringRedisTemplate.opsForValue().get("user_bank_add_"+userId);
		log.info("删除redis中的key后再查询的值为:"+redisValue);
		return ResultGenerator.genSuccessResult("银行卡添加成功",userBankDTO);
	}

    /**
     * 根据userId和卡号查询银行卡
     * @param userBankQueryParam
     * @return
     */
    public BaseResult<UserBankDTO> queryUserBankByCondition(UserBankQueryParam userBankQueryParam) {
    	UserBank userBankQuery = new UserBank();
    	userBankQuery.setUserId(userBankQueryParam.getUserId());
    	userBankQuery.setCardNo(userBankQueryParam.getBankCardCode());
    	userBankQuery.setIsDelete(ProjectConstant.NOT_DELETE);
    	userBankQuery.setPurpose(ProjectConstant.BANK_PURPOSE_WITHDRAW);
    	List<UserBank> userBankList = userBankMapper.queryUserBankBySelective(userBankQuery);
    	if(CollectionUtils.isEmpty(userBankList)) {
    		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), MemberEnums.DBDATA_IS_NULL.getMsg());
    	}
    	
    	UserBankDTO userBankDTO = new UserBankDTO();
    	UserBank userBank = userBankList.get(0);
    	try {
			BeanUtils.copyProperties(userBankDTO, userBank);
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResultGenerator.genFailResult("查询银行卡异常");
		} 
    	return ResultGenerator.genSuccessResult("查询银行卡成功", userBankDTO);
    }
    
    
    /**
     * 查询有效的银行卡根据默认或非默认状态
     * @param status
     * @return
     */
    public List<UserBank> querValidUserBank(String status,Integer purpose) {
		Integer userId = SessionUtil.getUserId();
		UserBank userBank = new UserBank();
		userBank.setUserId(userId);
		userBank.setStatus(status);
		userBank.setIsDelete(ProjectConstant.NOT_DELETE);
		userBank.setPurpose(purpose);
		List<UserBank> userBankList = userBankMapper.queryUserBankBySelective(userBank);
		if(CollectionUtils.isEmpty(userBankList)) {
			return new ArrayList<UserBank>();
		}
		return userBankList;
    }
    
    /**
     * 把已经添加最近时间的的银行卡 设为默认或非默认
     * @param status
     * @return
     */
    @Transactional
	public BaseResult<UserBankDTO> updateAlreadyAddCardStatus(String status,Integer purpose) {
		Integer userId = SessionUtil.getUserId();
		UserBankDTO userBankDTO = new UserBankDTO();
		List<UserBank> userBankList = this.querValidUserBank(status,purpose);
		if(CollectionUtils.isEmpty(userBankList)) {
			return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), MemberEnums.DBDATA_IS_NULL.getMsg());
		}
		
		UserBank userBank = userBankList.get(0);		
		UserBank updateUserBank = new UserBank();
		updateUserBank.setId(userBank.getId());
		updateUserBank.setUserId(userId);
		updateUserBank.setPurpose(purpose);
		if(status.equals(ProjectConstant.USER_BANK_NO_DEFAULT)) {
			updateUserBank.setStatus(ProjectConstant.USER_BANK_DEFAULT);
		}else if(status.equals(ProjectConstant.USER_BANK_DEFAULT)) {
			updateUserBank.setStatus(ProjectConstant.USER_BANK_NO_DEFAULT);
		}
		updateUserBank.setLastTime(DateUtil.getCurrentTimeLong());
		int rst = userBankMapper.updateUserBank(updateUserBank);
		if(1 != rst) {
			log.error("数据库更新银行卡状态失败");
		}
		
		UserBank noDefaultUserBank = this.findById(userBank.getId());
		try {
			BeanUtils.copyProperties(userBankDTO, noDefaultUserBank);
			userBankDTO.setUserBankId(String.valueOf(noDefaultUserBank.getId()));
			String cardNO = noDefaultUserBank.getCardNo();
			userBankDTO.setLastCardNo4(cardNO.substring(cardNO.length()-4));
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
		
		return ResultGenerator.genSuccessResult("查询默认银行卡成功", userBankDTO) ;
	}
    
    
	/**
	 * 银行卡三元素校验
	 * @param realName
	 * @param cardNo
	 * @return
	 */
	public JSONObject bankCardAuth3(String realName,String cardNo,String idcard) {
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);

		StringBuffer url = new StringBuffer(memberConfig.getBankAuth3ApiUrl());
		url.append("?key=" + memberConfig.getBankAuth3Key());
		url.append("&realname=" + realName);
		url.append("&bankcard=" + cardNo);
		url.append("&idcard=" + idcard);
		url.append("&sign=" + memberConfig.getBankAuth3Sign());
		String rst = rest.getForObject(url.toString(), String.class);

		JSONObject json = null;
		try {
			json = JSON.parseObject(rst);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return json;
	}

	/**
	 * 查询银行卡种类
	 * @param cardNo
	 * @return
	 */
	public JSONObject detectUserBank(String cardNo) {
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);

		StringBuffer url = new StringBuffer(memberConfig.getDetectBankTypeUrl());
		url.append("?key=" + memberConfig.getDetectBankTypeKey());
		url.append("&bankcard=" + cardNo);
		String rst = rest.getForObject(url.toString(), String.class);

    	MemberThirdApiLog thirdApiLog = new MemberThirdApiLog(memberConfig.getJuheSmsApiUrl(), ThirdApiEnum.JU_HE.getCode(), url.toString(), rst);
    	userMapper.saveMemberThirdApiLog(thirdApiLog);
    	
		JSONObject json = null;
		try {
			json = JSON.parseObject(rst);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return json;
	}
	
	
	/**
	 * 查询银行卡类型信息
	 * @param cardNo
	 * @return
	 */
	public JSONObject queryUserBankType(String cardNo) {
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);

		StringBuffer url = new StringBuffer(memberConfig.getBankTypeUrl());
		url.append("?bankcard=" + cardNo);
		url.append("&key=" + memberConfig.getBankTypeKey());
		String rst = rest.getForObject(url.toString(), String.class);

		JSONObject json = null;
		try {
			json = JSON.parseObject(rst);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return json;
	}

	/**
	 * 提现界面的数据显示：当前可提现余额和当前默认银行卡信息
	 * @return
	 */
	public BaseResult<WithDrawShowDTO> queryWithDrawShow(Integer purpose){
		Integer userId = SessionUtil.getUserId();
		User user = userService.findById(userId);
		
		UserBank userBank = new UserBank();
		userBank.setUserId(userId);
		userBank.setStatus(ProjectConstant.USER_BANK_DEFAULT);
		userBank.setIsDelete(ProjectConstant.NOT_DELETE);
		userBank.setPurpose(purpose);
		List<UserBank> userBankList = userBankMapper.queryUserBankBySelective(userBank);
		
		WithDrawShowDTO withDrawShowDTO = new WithDrawShowDTO();
		
		if(!CollectionUtils.isEmpty(userBankList)) {
			userBankList.stream().filter(s->s.getStatus().equals(ProjectConstant.USER_BANK_DEFAULT));
			UserBank userBankDefault = userBankList.get(0);	
			String cardNo = userBankDefault.getCardNo();
			cardNo = cardNo.substring(cardNo.length()-4, cardNo.length());
			String defaultBankLabel = userBankDefault.getBankName()+"储蓄卡"+cardNo;
			withDrawShowDTO.setUserMoney(String.valueOf(user.getUserMoney()));
			withDrawShowDTO.setDefaultBankLabel(defaultBankLabel);
			withDrawShowDTO.setUserBankId(String.valueOf(userBankDefault.getId()));
		}else {
			withDrawShowDTO.setUserMoney(String.valueOf(user.getUserMoney()));
			withDrawShowDTO.setDefaultBankLabel("");
			withDrawShowDTO.setUserBankId("");
		}

		return ResultGenerator.genSuccessResult("查询提现界面的数据显示信息成功",withDrawShowDTO);
	}
	
	
	/**
	 * 查询用户银行卡列表
	 * @return
	 */
	public BaseResult<LinkedList<UserBankDTO>> queryUserBankList(Integer purpose){
		Integer userId = SessionUtil.getUserId();		
		UserBank userBankParam = new UserBank();
		userBankParam.setUserId(userId);
		userBankParam.setIsDelete(ProjectConstant.NOT_DELETE);
		userBankParam.setPurpose(purpose);
		List<UserBank> userBankList = userBankMapper.queryUserBankBySelective(userBankParam);
		
		LinkedList<UserBankDTO> userBankDTOList = new LinkedList<UserBankDTO>();
		for(UserBank userBank:userBankList) {
			UserBankDTO userBankDTO = new UserBankDTO();
			try {
				BeanUtils.copyProperties(userBankDTO, userBank);
				userBankDTO.setUserBankId(String.valueOf(userBank.getId()));
				userBankDTO.setCardNo(this.hiddenBankCardNo(userBank.getCardNo()));
			} catch (Exception e) {
				log.error(e.getMessage());
				return ResultGenerator.genFailResult(RespStatusEnum.SERVER_ERROR.getMsg(),userBankDTOList);
			} 
			if(ProjectConstant.USER_BANK_DEFAULT.equals(userBank.getStatus())) {
				userBankDTOList.addFirst(userBankDTO);
			}else {
				userBankDTOList.add(userBankDTO);
			}
			
		}
		return ResultGenerator.genSuccessResult("查询银行卡列表成功",userBankDTOList);
	}
	
	/**
	 * 查询银行卡
	 * @param userBankId
	 * @return
	 */
	public BaseResult<UserBankDTO> queryUserBank(Integer userBankId,Integer purpose){
		Integer userId = SessionUtil.getUserId();
		
		UserBank userBankParam = new UserBank();
		userBankParam.setUserId(userId);
		userBankParam.setIsDelete(ProjectConstant.NOT_DELETE);
		userBankParam.setId(userBankId);
		userBankParam.setPurpose(purpose);
		List<UserBank> userBankList = userBankMapper.queryUserBankBySelective(userBankParam);
		if(userBankList.size() == 0) {
			return ResultGenerator.genSuccessResult("查询银行卡成功",null);
		}
		
		UserBank userBank = userBankList.get(0);
		UserBankDTO userBankDTO = new UserBankDTO();
		try {
			BeanUtils.copyProperties(userBankDTO, userBank);
			userBankDTO.setUserBankId(String.valueOf(userBank.getId()));
		} catch (Exception e) {
			log.error(e.getMessage());
		} 
		return ResultGenerator.genSuccessResult("查询银行卡成功",userBankDTO);
	}
	
	/**
	 * 隐藏银行卡号第5位到倒数第5位
	 * @param cardNo
	 * @return
	 */
	public String  hiddenBankCardNo(String cardNo) {
		String subStr = cardNo.substring(4, cardNo.length() - 4);
		String hiddenStr = RandomUtil.generateStarString(subStr.length());
		return cardNo.replace(subStr, hiddenStr);
	}
	
	/**
	 * 删除银行卡
	 * @param userBankId
	 * @return
	 */
	@Transactional
	public BaseResult<UserBankDTO> deleteUserBank(DeleteBankCardParam deleteBankCardParam,Integer purpose){
		Integer userId = SessionUtil.getUserId();
		UserBankDTO userBankDTO = new UserBankDTO();
		if(ProjectConstant.USER_BANK_NO_DEFAULT.equals(deleteBankCardParam.getStatus())) {//删除的是非默认
			
		}else {//删除的是默认
			BaseResult<UserBankDTO> userBankDTORst = this.updateAlreadyAddCardStatus(ProjectConstant.USER_BANK_NO_DEFAULT,purpose);
			if(userBankDTORst.getCode() != MemberEnums.DBDATA_IS_NULL.getcode()) {
				userBankDTO = userBankDTORst.getData();
			}
		}
		
		UserBank userBank = new UserBank();
		userBank.setId(Integer.valueOf(deleteBankCardParam.getId()));
		userBank.setUserId(userId);
		userBank.setStatus(ProjectConstant.USER_BANK_NO_DEFAULT);
		userBank.setIsDelete(ProjectConstant.DELETE);
		userBank.setLastTime(DateUtil.getCurrentTimeLong());
		userBank.setPurpose(ProjectConstant.BANK_PURPOSE_WITHDRAW);
		int rst = userBankMapper.updateUserBank(userBank);
		if(1 != rst) {
			log.error("删除银行卡失败");
		}
		return ResultGenerator.genSuccessResult("删除银行卡成功",userBankDTO);
	}
	
	/**
	 * 更改银行卡状态为默认
	 * @return
	 */
	@Transactional
	public BaseResult<String> updateUserBankDefault(Integer userBankId,Integer purpose){
 		Integer userId = SessionUtil.getUserId();
		//当前一张银行卡不允许更改状态
		UserBank userBankParam = new UserBank();
		userBankParam.setUserId(userId);
		userBankParam.setIsDelete(ProjectConstant.NOT_DELETE);
		userBankParam.setPurpose(ProjectConstant.BANK_PURPOSE_WITHDRAW);
		List<UserBank> userBankList = userBankMapper.queryUserBankBySelective(userBankParam);
		
		if(CollectionUtils.isEmpty(userBankList)) {
			return ResultGenerator.genFailResult("当前用户未添加银行卡");
		}else {
			if(userBankList.size() == 1) {
				return ResultGenerator.genResult(MemberEnums.CURRENT_ONE_CARD.getcode(), MemberEnums.CURRENT_ONE_CARD.getMsg());
			}else if(userBankList.size() > 1) {
				BaseResult<UserBankDTO> userBankDTORst = this.updateAlreadyAddCardStatus(ProjectConstant.USER_BANK_DEFAULT,purpose);
				if(userBankDTORst.getCode() != 0) {
					log.error(userBankDTORst.getMsg());
				}
				
				UserBank updateUserBank = new UserBank();
				updateUserBank.setId(userBankId);
				updateUserBank.setUserId(userId);
				updateUserBank.setStatus(ProjectConstant.USER_BANK_DEFAULT);
				updateUserBank.setPurpose(purpose);
				int updateRst = userBankMapper.updateUserBank(updateUserBank);
				if(1 != updateRst) {
					log.error("更新数据库银行卡状态失败");
				}

			}
		}

		return ResultGenerator.genSuccessResult("更改银行卡状态成功");
	}
	
	public String getAbbrByMap(String bankName) {
		if(mMap == null || mMap.size() <= 0) {
			List<UserBankCode> mList = userBankCodeMapper.listAll();
			if(mList != null && mList.size() > 0) {
				for(int i = 0;i < mList.size();i++) {
					UserBankCode bankCode = mList.get(i);
					String bName = bankCode.getBankName();
					String bCode = bankCode.getBankCode();
					mMap.put(bName,bCode);
//					log.info("bName:" + bName +" bCode:" + bCode);
				}
			}
			log.info("====================");
			log.info("load userBankCode succ total size:" + mMap.size());
			log.info("====================");
		}
		String result = "";
		if(!StringUtils.isEmpty(bankName)) {
			for(Map.Entry<String, String> entry : mMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if(key.contains(bankName) || bankName.contains(key)) {
					result = value;
					break;
				}
			}
		}
		return result;
	}
	
	/***
	 * 查询
	 * @param userId
	 * @param bankCardNo
	 * @param purpose
	 * @return
	 */
	public List<UserBank> queryBankByPurpose(Integer userId,String bankCardNo,Integer purpose){
		UserBank userBank = new UserBank();
		userBank.setUserId(userId);
		userBank.setCardNo(bankCardNo);
		userBank.setPurpose(purpose);
		return userBankMapper.queryUserBankBySelective(userBank);
	}
	
	static {
//		mMap.put("中国建设银行","CCB");
//		mMap.put("上海浦东发展银行","SPDB");
//		mMap.put("交通银行","BOCOM");
//		mMap.put("招商银行股份有限公司","CMB");
//		mMap.put("中国银行","BOC");
//		mMap.put("中国工商银行","ICBC");
//		mMap.put("中国光大银行股份有限公司", "CEB");
//		mMap.put("中国农商银行","CNRCB");//20
//		mMap.put("中国农业银行股份有限公司","ABC");
//		mMap.put("广发银行","GDB");
//		mMap.put("中国银联","UPOP");
//		mMap.put("宁波银行","NBBC");
//		mMap.put("华夏银行","HXB");
//		mMap.put("北京银行","BCCB");
//		mMap.put("遵义市商业银行","ZYSB");
//		mMap.put("上海银行","BOS");
//		mMap.put("平安银行","PAB");
//		mMap.put("长春农村商业银行","CCCB");
//		mMap.put("宁波通商银行", "NBC");
//		mMap.put("开封市商业银行","KFBANK");
//		mMap.put("南阳银行","NYBANK");		//5
//		mMap.put("浙江景宁银座村镇银行","JNYZB");//6
//		mMap.put("重庆渝北银座村镇银行","YBYZB");//7
//		mMap.put("重庆黔江银座村镇银行", "QJZYB");//8
//		mMap.put("广西壮族自治区农村信用社联合社","GXNS");//9
//		mMap.put("外换银行","KEB");		//10
//		mMap.put("企业银行","IBK");		//11
//		mMap.put("甘肃银行股份有限公司","GSBC");//12
//		mMap.put("江苏镇江农村商业银行股份有限公司","ZJNSYH");//13
//		mMap.put("天骄蒙银村镇银行","DMVB");//14
//		mMap.put("惠安县农村信用合作联社","FJHAXH");//15
//		mMap.put("大连旅顺口蒙银村镇银行股份有限公司","DLMB");//16
//		mMap.put("中原银行股份有限公司","ZYB");//17
//		mMap.put("贵州花溪农村商业银行股份有限公司","GZNXHX");//18
//		mMap.put("酒泉农村商业银行股份有限公司","RCBJQ");//19
//		mMap.put("佛山农商银行","BFCC");//21
//		mMap.put("南昌农村商业银行股份有限公司","NRCB");//22
//		mMap.put("石河子国民村镇银行有限责任公司","SHZNCB");//23
//		mMap.put("山西长治黎都农村商业银行股份有限公司", "CLRCB");//24
//		mMap.put("浙江杭州余杭农村商业银行股份有限公司", "YHRCB");//25
//		mMap.put("云南景东农村商业银行股份有限公司", "YNJDNSB");//26
//		mMap.put("海口农村商业银行股份有限公司","HKNSBC");//27
//		mMap.put("四川新网银行股份有限公司","XWBC");//28
//		mMap.put("江西银行股份有限公司","JXBC");//29
//		mMap.put("龙湾农村商业银行股份有限公司", "LRCB");//30
//		mMap.put("昆山鹿城村镇银行", "KSLCCB");//31
//		mMap.put("大连金州联丰村镇银行股份有限公司","LFRB");//32
//		mMap.put("微信", "WX");//33
//		mMap.put("支付宝", "ALIPAY");//34
//		mMap.put("QQ钱包", "QQPAY");//35
//		mMap.put("上海华瑞银行", "SHRB");//36
//		mMap.put("深圳南山宝生村镇银行股份有限公司","BSCB");//37
	}
}
