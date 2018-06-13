package com.dl.member.api;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.UserBankDTO;
import com.dl.member.param.BankCardParam;
//import com.pgt.base.result.BaseResult;
//import com.pgt.shop.member.dto.UserBonusCountDTO;
//import com.pgt.shop.member.dto.UserBonusDTO;
//import com.pgt.shop.member.param.GetShopBonusParam;
//import com.pgt.shop.member.param.GetUserShopBonusParam;
//import com.pgt.shop.member.param.OrderInfoIdParam;
//import com.pgt.shop.member.param.ShopBonusParam;
//import com.pgt.shop.member.param.UserBonusIdArrParam;
//import com.pgt.shop.member.param.UserBonusIdsParam;
//import com.pgt.shop.member.param.ValidUserBonusParam;
import com.dl.member.param.IDParam;
import com.dl.member.param.UserBankParam;
import com.dl.member.param.UserBankQueryParam;
import com.dl.member.param.UserBonusIdParam;


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
    
	 
	 @PostMapping("/user/bank/queryUserBankByCondition")    
	 BaseResult<UserBankDTO> queryUserBankType(@RequestBody BankCardParam bankCardParam);
}
