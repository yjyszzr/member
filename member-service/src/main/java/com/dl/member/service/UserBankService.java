package com.dl.member.service;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
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
import com.dl.base.util.RandomUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserBankMapper;
import com.dl.member.dto.UserBankDTO;
import com.dl.member.dto.UserRealDTO;
import com.dl.member.dto.WithDrawShowDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.User;
import com.dl.member.model.UserBank;
import com.dl.member.param.DeleteBankCardParam;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Slf4j
public class UserBankService extends AbstractService<UserBank> {
    @Resource
    private UserBankMapper userBankMapper;
    
    @Resource
    private UserRealService userRealService;
    
	@Resource
	private RestTemplateConfig restTemplateConfig;
	
	@Resource
	private RestTemplate restTemplate;
	
	@Resource
	private MemberConfig memberConfig;
	
	@Resource
	private UserService userService;
    
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
		UserRealDTO userRealDTO = userRealService.queryUserReal();
		if(null == userRealDTO) {
			return ResultGenerator.genResult(MemberEnums.NOT_REAL_AUTH.getcode(), MemberEnums.NOT_REAL_AUTH.NOT_REAL_AUTH.getMsg());
		}
		
		//已经添加过该银行卡
		UserBank userBankAlready = new UserBank();
		userBankAlready.setCardNo(bankCardNo);
		userBankAlready.setIsDelete(ProjectConstant.NOT_DELETE);
		List<UserBank> userBankList = userBankMapper.queryUserBankBySelective(userBankAlready);
		if(!CollectionUtils.isEmpty(userBankList)) {
			return ResultGenerator.genResult(MemberEnums.BANKCARD_ALREADY_AUTH.getcode(), MemberEnums.BANKCARD_ALREADY_AUTH.getMsg());
		}
		
		//查询银行卡具体信息，并过滤信用卡
//		BaseResult<UserBankDTO> detectRst = this.detectUserBank(bankCardNo);
//		if(detectRst.getCode() != 0) {
//			return ResultGenerator.genResult(detectRst.getCode(), detectRst.getMsg());
//		}
		
		//三元素校验
		UserBankDTO userBankDTO = null;
		String idCard = userRealDTO.getIdCode();
		String realName = userRealDTO.getRealName();
		JSONObject json = this.bankCardAuth3(realName, bankCardNo,idCard);
		String reason = json.getString("reason");
		Integer errorCode = json.getInteger("error_code");
		if(0 == errorCode) {
			JSONObject result = (JSONObject) json.get("result");
			String res = result.getString("res");
			if("2" == res) {
				return ResultGenerator.genResult(MemberEnums.BANKCARD_NOT_MATCH.getcode(), MemberEnums.BANKCARD_NOT_MATCH.getMsg());
			}
		}else {
			return ResultGenerator.genResult(MemberEnums.VERIFY_BANKCARD_EROOR.getcode(), reason);
		}
		
		//把已经添加的默认银行卡 设为非默认
		BaseResult<UserBankDTO> userBankDTORst = this.updateAlreadyAddCardStatus(ProjectConstant.USER_BANK_DEFAULT);
		if(userBankDTORst.getCode() != 0) {
			return ResultGenerator.genFailResult(userBankDTORst.getMsg());
		}
		
		//保存到数据库
		userBankDTO.setRealName(realName);
		userBankDTO.setCardNo(bankCardNo);
		userBankDTO.setStatus(ProjectConstant.USER_BANK_DEFAULT);
		this.saveUserBank(userBankDTO);
		
		
		return ResultGenerator.genSuccessResult("银行卡添加成功",userBankDTO);
	}

    /**
     * 查询有效的银行卡根据默认或非默认状态
     * @param status
     * @return
     */
    public List<UserBank> querValidUserBank(String status) {
		Integer userId = SessionUtil.getUserId();
		UserBank userBank = new UserBank();
		userBank.setUserId(userId);
		userBank.setStatus(status);
		userBank.setIsDelete(ProjectConstant.NOT_DELETE);
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
	public BaseResult<UserBankDTO> updateAlreadyAddCardStatus(String status) {
		Integer userId = SessionUtil.getUserId();
		UserBankDTO userBankDTO = new UserBankDTO();
		List<UserBank> userBankList = this.querValidUserBank(status);
		if(CollectionUtils.isEmpty(userBankList)) {
			return ResultGenerator.genSuccessResult("查询银行卡种类成功", userBankDTO) ;
		}
		
		UserBank userBank = userBankList.get(0);		
		UserBank updateUserBank = new UserBank();
		updateUserBank.setId(userBank.getId());
		updateUserBank.setUserId(userId);
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
	public BaseResult<UserBankDTO> detectUserBank(String cardNo) {
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);

		StringBuffer url = new StringBuffer(memberConfig.getDetectBankTypeUrl());
		url.append("?key=" + memberConfig.getDetectBankTypeKey());
		url.append("&bankcard=" + cardNo);
		String rst = rest.getForObject(url.toString(), String.class);

		JSONObject json = null;
		try {
			json = JSON.parseObject(rst);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		UserBankDTO userBankDTO = new UserBankDTO();
		Integer errorCode = json.getInteger("error_code");
		if(0 == errorCode) {
			JSONObject result = (JSONObject) json.get("result");
			userBankDTO.setBankName(result.getString("bank"));
			userBankDTO.setBankLogo(result.getString("logo"));
			userBankDTO.setCardType(result.getString("cardtype"));
			if(!"借记卡".equals(result.getString("cardtype"))) {
				return ResultGenerator.genResult(MemberEnums.NOT_DEBIT_CARD.getcode(), MemberEnums.NOT_DEBIT_CARD.getMsg());
			}
			return ResultGenerator.genSuccessResult("查询银行卡种类成功", userBankDTO) ;
		}else {
			return ResultGenerator.genFailResult("查询银行卡种类失败", userBankDTO);
		}
	}
	
	
	/**
	 * 查询银行卡类型信息
	 * @param cardNo
	 * @return
	 */
//	public BaseResult<UserBankDTO> queryUserBankType(String cardNo) {
//		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
//		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
//		HttpHeaders headers = new HttpHeaders();
//		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
//		headers.setContentType(type);
//
//		StringBuffer url = new StringBuffer(memberConfig.getBankTypeUrl());
//		url.append("?key=" + memberConfig.getBankTypeKey());
//		url.append("&num=" + cardNo);
//		String rst = rest.getForObject(url.toString(), String.class);
//
//		JSONObject json = null;
//		try {
//			json = JSON.parseObject(rst);
//		} catch (Exception e) {
//			log.error(e.getMessage());
//		}
//
//		JSONObject json_tmp = (JSONObject) json.get("result");
//		Integer errorCode = (Integer)json.getInteger("error_code");
//		if(0 == errorCode) {
//			UserBankDTO userBankDTO = new UserBankDTO();
//			userBankDTO.setBankName(json_tmp.getString("bank")+json_tmp.getString("type"));
//			userBankDTO.setBankLogo(json_tmp.getString("logo"));
//			return ResultGenerator.genSuccessResult("查询银行卡种类成功", userBankDTO) ;
//		}else {
//			return ResultGenerator.genFailResult(json_tmp.getString("reason"), null);
//		}
//	}

	/**
	 * 提现界面的数据显示：当前可提现余额和当前默认银行卡信息
	 * @return
	 */
	public BaseResult<WithDrawShowDTO> queryWithDrawShow(){
		Integer userId = SessionUtil.getUserId();
		User user = userService.findById(userId);
		
		UserBank userBank = new UserBank();
		userBank.setUserId(userId);
		userBank.setStatus(ProjectConstant.USER_BANK_DEFAULT);
		userBank.setIsDelete(ProjectConstant.NOT_DELETE);
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
	public BaseResult<LinkedList<UserBankDTO>> queryUserBankList(){
		Integer userId = SessionUtil.getUserId();		
		UserBank userBankParam = new UserBank();
		userBankParam.setUserId(userId);
		userBankParam.setIsDelete(ProjectConstant.NOT_DELETE);
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
	public BaseResult<UserBankDTO> queryUserBank(Integer userBankId){
		Integer userId = SessionUtil.getUserId();
		
		UserBank userBankParam = new UserBank();
		userBankParam.setUserId(userId);
		userBankParam.setIsDelete(ProjectConstant.NOT_DELETE);
		userBankParam.setId(userBankId);
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
	public BaseResult<UserBankDTO> deleteUserBank(DeleteBankCardParam deleteBankCardParam){
		Integer userId = SessionUtil.getUserId();
		UserBankDTO userBankDTO = new UserBankDTO();
		if(ProjectConstant.USER_BANK_NO_DEFAULT.equals(deleteBankCardParam.getStatus())) {//删除的是非默认
			
		}else {//删除的是默认
			BaseResult<UserBankDTO> userBankDTORst = this.updateAlreadyAddCardStatus(ProjectConstant.USER_BANK_NO_DEFAULT);
			userBankDTO = userBankDTORst.getData();
		}
		
		UserBank userBank = new UserBank();
		userBank.setId(Integer.valueOf(deleteBankCardParam.getId()));
		userBank.setUserId(userId);
		userBank.setStatus(ProjectConstant.USER_BANK_NO_DEFAULT);
		userBank.setIsDelete(ProjectConstant.DELETE);
		userBank.setLastTime(DateUtil.getCurrentTimeLong());
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
	public BaseResult<String> updateUserBankDefault(Integer userBankId){
 		Integer userId = SessionUtil.getUserId();
		//当前一张银行卡不允许更改状态
		UserBank userBankParam = new UserBank();
		userBankParam.setUserId(userId);
		userBankParam.setIsDelete(ProjectConstant.NOT_DELETE);
		List<UserBank> userBankList = userBankMapper.queryUserBankBySelective(userBankParam);
		
		if(CollectionUtils.isEmpty(userBankList)) {
			return ResultGenerator.genFailResult("当前用户未添加银行卡");
		}else {
			if(userBankList.size() == 1) {
				return ResultGenerator.genResult(MemberEnums.CURRENT_ONE_CARD.getcode(), MemberEnums.CURRENT_ONE_CARD.getMsg());
			}else if(userBankList.size() > 1) {
				BaseResult<UserBankDTO> userBankDTORst = this.updateAlreadyAddCardStatus(ProjectConstant.USER_BANK_DEFAULT);
				if(userBankDTORst.getCode() != 0) {
					log.error(userBankDTORst.getMsg());
				}
				
				UserBank updateUserBank = new UserBank();
				updateUserBank.setId(userBankId);
				updateUserBank.setUserId(userId);
				updateUserBank.setStatus(ProjectConstant.USER_BANK_DEFAULT);
				int updateRst = userBankMapper.updateUserBank(updateUserBank);
				if(1 != updateRst) {
					log.error("更新数据库银行卡状态失败");
				}

			}
		}

		return ResultGenerator.genSuccessResult("更改银行卡状态成功");
	}
}
