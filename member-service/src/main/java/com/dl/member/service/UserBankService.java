package com.dl.member.service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    	
    	//把已经添加的银行卡 设为非默认
		Condition condition = new Condition(UserBank.class);
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("user_id=", userId);
		List<UserBank> userBankList = this.findByCondition(condition);
		if(!CollectionUtils.isEmpty(userBankList)) {
			 userBankList.removeIf(s->s.getCardNo().equals(userBankDTO.getCardNo()));
			 List<Integer> userBankIds = userBankList.stream().map(s->s.getId()).collect(Collectors.toList());
			 int updateRst = userBankMapper.batchUpdateUserBankStatus(ProjectConstant.USER_BANK_NO_DEFAULT, userBankIds);
		}
    	
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
	public BaseResult<UserBankDTO> addBankCard(String bankCardNo){
		UserRealDTO userRealDTO = userRealService.queryUserReal();
		if(null == userRealDTO) {
			return ResultGenerator.genResult(MemberEnums.NOT_REAL_AUTH.getcode(), MemberEnums.NOT_REAL_AUTH.NOT_REAL_AUTH.getMsg());
		}
		
		UserBank userBank = this.findBy("cardNo", bankCardNo);
		if(null != userBank) {
			return ResultGenerator.genResult(MemberEnums.BANKCARD_ALREADY_AUTH.getcode(), MemberEnums.BANKCARD_ALREADY_AUTH.getMsg());
		}
		
		BaseResult<UserBankDTO> detectRst = this.detectUserBank(bankCardNo);
		if(detectRst.getCode() != 0) {
			return ResultGenerator.genResult(detectRst.getCode(), detectRst.getMsg());
		}
		
		UserBankDTO userBankDTO = detectRst.getData();
		String idCard = userRealDTO.getIdCode();
		String realName = userRealDTO.getRealName();
		
		BaseResult<String> atuhRst = this.bankCardAuth3(realName, bankCardNo,idCard);
		if(atuhRst.getCode() != 0) {
			return ResultGenerator.genResult(MemberEnums.VERIFY_BANKCARD_EROOR.getcode(), MemberEnums.VERIFY_BANKCARD_EROOR.getMsg());
		}
		
		userBankDTO.setRealName(realName);
		userBankDTO.setCardNo(bankCardNo);
		userBankDTO.setStatus(ProjectConstant.USER_BANK_DEFAULT);
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

		JSONObject json_tmp = (JSONObject) json.get("result");
		String res = json_tmp.getString("res");
		String message = json_tmp.getString("message");
		if("1".equals(res)) {
			return ResultGenerator.genSuccessResult("银行卡校验成功",message);
		}else {
			return ResultGenerator.genFailResult(message);
		}
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

		JSONObject json_tmp = (JSONObject) json.get("result");
		Integer errorCode = (Integer)json.getInteger("error_code");
		if(0 == errorCode) {
			UserBankDTO userBankDTO = new UserBankDTO();
			userBankDTO.setBankName(json_tmp.getString("bank"));
			userBankDTO.setBankLogo(json_tmp.getString("logo"));
			userBankDTO.setCardType(json_tmp.getString("cardtype"));
			if(!"借记卡".equals(json_tmp.getString("cardtype"))) {
				return ResultGenerator.genResult(MemberEnums.NOT_DEBIT_CARD.getcode(), MemberEnums.NOT_DEBIT_CARD.getMsg());
			}
			
			return ResultGenerator.genSuccessResult("查询银行卡种类成功", userBankDTO) ;
		}else {
			return ResultGenerator.genFailResult(json_tmp.getString("reason"), null);
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
		
		Condition condition = new Condition(UserBank.class);
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("user_id=", userId);
		List<UserBank> userBankList = this.findByCondition(condition);
		WithDrawShowDTO withDrawShowDTO = new WithDrawShowDTO();
		if(!CollectionUtils.isEmpty(userBankList)) {
			userBankList.stream().filter(s->s.getStatus().equals(ProjectConstant.USER_BANK_DEFAULT));
			UserBank userBank = userBankList.get(0);	
			String cardNo = userBank.getCardNo();
			withDrawShowDTO.setUserMoney(String.valueOf(user.getUserMoney()));
			withDrawShowDTO.setBankName(userBank.getBankName());
			withDrawShowDTO.setLastCardNo4(cardNo.substring(cardNo.length()-4, cardNo.length()));
		}else {
			return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), "用户没有添加银行卡");
		}

		return ResultGenerator.genSuccessResult("查询提现界面的数据显示信息成功",withDrawShowDTO);
	}
	
	
	/**
	 * 查询用户银行卡列表
	 * @return
	 */
	public BaseResult<List<UserBankDTO>> queryUserBankList(){
		Integer userId = SessionUtil.getUserId();		
		List<UserBank> userBankList = userBankMapper.queryUserBankList(userId);
		
		List<UserBankDTO>  userBankDTOList = new ArrayList<>();
		for(UserBank userBank:userBankList) {
			UserBankDTO userBankDTO = new UserBankDTO();
			try {
				BeanUtils.copyProperties(userBankDTO, userBank);
				userBankDTO.setUserBankId(String.valueOf(userBank.getId()));
				userBankDTO.setCardNo(this.hiddenBankCardNo(userBank.getCardNo()));
			} catch (Exception e) {
				throw new ServiceException(RespStatusEnum.SERVER_ERROR.getCode(), RespStatusEnum.SERVER_ERROR.getMsg());
			} 
			userBankDTOList.add(userBankDTO);
		}
		return ResultGenerator.genSuccessResult("查询银行卡列表成功",userBankDTOList);
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
	public BaseResult<UserBankDTO> deleteUserBank(DeleteBankCardParam deleteBankCardParam){
		Integer userId = SessionUtil.getUserId();
		UserBank userBank = new UserBank();
		userBank.setId(deleteBankCardParam.getId());
		userBank.setStatus("1");
		userBank.setLastTime(DateUtil.getCurrentTimeLong());
		int rst = userBankMapper.updateUserBank(userBank);
		if(1 != rst) {
			log.error("删除银行卡失败");
		}
		
		if("0".equals(deleteBankCardParam.getStatus())) {//非默认
			return ResultGenerator.genSuccessResult("删除银行卡成功");
		}else {//默认
			UserBankDTO userBankDTO = new UserBankDTO();
			List<UserBank> uerBankList = userBankMapper.queryUserBankList(userId);
			if(uerBankList.size() == 0) {
				return ResultGenerator.genSuccessResult("删除银行卡成功");
			}else {
				UserBank userBankDefault = uerBankList.get(0);
				UserBank updateUserBank = new UserBank();
				updateUserBank.setId(userBankDefault.getId());
				updateUserBank.setStatus("1");
				int updateRst = userBankMapper.updateUserBank(updateUserBank);
				if(1 != updateRst) {
					log.error("更新银行卡失败");
				}
				
				try {
					BeanUtils.copyProperties(userBankDTO, userBank);
					userBankDTO.setUserBankId(String.valueOf(userBank.getId()));
				} catch (Exception e) {
					log.error(e.getMessage());
				} 
				return null;
			}
		}
	}
	
	/**
	 * 更改银行卡状态
	 * @return
	 */
	public BaseResult<String> updateUserBankDefault(Integer userBankId){
		Integer userId = SessionUtil.getUserId();
		Condition condition = new Condition(UserBank.class);
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("user_id=", userId);
		List<UserBank> userBankList = this.findByCondition(condition);
		if(userBankList.size() == 1) {
			return ResultGenerator.genResult(MemberEnums.CURRENT_ONE_CARD.getcode(), MemberEnums.CURRENT_ONE_CARD.getMsg());
		}
		
		
		UserBank userBank = new UserBank();
		userBank.setId(userBankId);
		userBank.setStatus("1");
		userBank.setLastTime(DateUtil.getCurrentTimeLong());
		int updateRst = userBankMapper.updateUserBank(userBank);	
		if(1 != updateRst) {
			log.error("数据库更新银行卡状态失败");
			return ResultGenerator.genFailResult("更新银行卡状态失败");
		}
		
		Condition conditionNew  = new Condition(UserBank.class);
		Criteria criteriaNew = conditionNew.createCriteria();
		criteriaNew.andCondition("user_id=", userId);
		criteriaNew.andCondition("status=", "1");
		criteriaNew.andCondition("is_delete=", "0");
		List<UserBank> userBankListNew = this.findByCondition(conditionNew);
		UserBank userBankNew = userBankListNew.get(0);
		
		UserBank userBankNewNew = new UserBank();
		userBankNewNew.setId(userBankNew.getId());
		userBankNewNew.setStatus("0");
		userBankNewNew.setLastTime(DateUtil.getCurrentTimeLong());
		int updateNewRst = userBankMapper.updateUserBank(userBankNewNew);
		if(1 != updateNewRst) {
			log.error("数据库更新银行卡状态失败");
			return ResultGenerator.genFailResult("更新银行卡状态失败");
		}

		return ResultGenerator.genSuccessResult("更改银行卡状态成功");
	}
}
