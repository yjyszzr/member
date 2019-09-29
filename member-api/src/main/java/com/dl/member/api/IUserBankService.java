package com.dl.member.api;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.BankDTO;
import com.dl.member.dto.UserBankDTO;
import com.dl.member.param.BankCardParam;
import com.dl.member.param.BankCardSaveParam;
import com.dl.member.param.IDParam;
import com.dl.member.param.UserBankPurposeQueryParam;
import com.dl.member.param.UserBankQueryParam;


/**
 * 用户银行卡接口
 *
 * @author zhangzirong
 */
@FeignClient(value = "member-service")
public interface IUserBankService {
	
	
	  /**
	  * 查询根据userBankId查询银行卡信息
	  * @return
	  */
	 @PostMapping("/user/bank/queryUserBank")    
	 BaseResult<UserBankDTO> queryUserBank(@RequestBody IDParam idParam);
	 
	  /**
	  * 查询银行卡信息
	  *
	  * @return
	  */
	 @PostMapping("/user/bank/queryUserBankByCondition")    
	 BaseResult<UserBankDTO> queryUserBankByCondition(@RequestBody UserBankQueryParam userBankQueryParam);
	 
	 /**
	  * 查询银行卡信息
	  *
	  * @return
	  */
	 @PostMapping("/user/bank/queryUserBankByUserId")    
	 BaseResult<UserBankDTO> queryUserBankByUserId(@RequestBody UserBankQueryParam userBankQueryParam);
    
	 
	 /**
	  * 根据银行卡号，聚合查询银行信息
	  * @param bankCardParam
	  * @return
	  */
	 @PostMapping("/user/bank/queryUserBankType")    
	 BaseResult<BankDTO> queryUserBankType(@RequestBody BankCardParam bankCardParam);
	 

	 /**
	  * 保存银行卡信息
	  * @param bankCardParam
	  * @return
	  */
	 @PostMapping("/user/bank/saveBankCard")    
	 BaseResult<UserBankDTO> saveBankInfo(@RequestBody BankCardSaveParam bankCardParam);
	 
	 
	 /**
	  * 保存银行卡信息
	  * @param bankCardParam
	  * @return
	  */
	 @PostMapping("/user/bank/queryBankByPurpose")
	 BaseResult<UserBankDTO> queryBankByPurpose(@RequestBody UserBankPurposeQueryParam queryParam);
}
