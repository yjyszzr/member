package com.dl.member.api;

import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.dl.base.param.EmptyParam;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.DonationPriceDTO;
import com.dl.member.dto.UserBonusDTO;
import com.dl.member.param.BonusLimitConditionParam;
import com.dl.member.param.PayLogIdParam;
import com.dl.member.param.UserBonusIdParam;


/**
 * 用户账户接口
 *
 * @author zhangzirong
 */
@FeignClient(value = "member-service")
public interface IUserBonusService {
	
	
	 /**
	  * 根userBonusId查询红包
	  *
	  * @return
	  */
	 @PostMapping("/user/bonus/queryUserBonus")    
	 BaseResult<UserBonusDTO> queryUserBonus(@RequestBody UserBonusIdParam userBonusIdParam);
	 /**
	  * 根userBonusId查询有效红包数量和红包总金额
	  *
	  * @return
	  */
	 @PostMapping("/user/bonus/queryUserBonusNumAndPrice")    
	 BaseResult<UserBonusDTO> queryUserBonusNumAndPrice(@RequestBody UserBonusIdParam userBonusIdParam);
	 /**
	  * 查询用户所有有效的红包列表
	  * @param userBonusStatusParam
	  * @return
	  */
     @PostMapping("/user/bonus/queryValidBonusList")
     BaseResult<List<UserBonusDTO>> queryValidBonusList(@RequestBody BonusLimitConditionParam bonusLimitConditionParam);	
     
     
     /**
      * 充值成功后领取充值送随机红包
      * @param payLogIdParam
      * @return
      */
     @PostMapping("/user/bonus/reiceiveBonusAfterRecharge")
     public BaseResult<DonationPriceDTO> reiceiveBonusAfterRecharge(@RequestBody PayLogIdParam payLogIdParam);
     
     
     /**
      * 充值成功后领取充值送随机红包
      * @param payLogIdParam
      * @return
      */
     @PostMapping("/user/bonus/reiceiveBonusAfterRechargeNew")
     public BaseResult<DonationPriceDTO> reiceiveBonusAfterRechargeNew(@RequestBody PayLogIdParam payLogIdParam);

  	/**
  	 * 更新红包的过期状态
  	 * @param UserBonusParam
  	 * @return
  	 */
  	@RequestMapping(path="/user/bonus/updateBonusExpire", method=RequestMethod.POST)
  	public BaseResult<String> updateBonusExpire(@RequestBody EmptyParam emptyParam);
//	 /**
//	 * 获取结算页的店铺红包列表
//	 * @param
//	 * @return
//	 */
//	 @RequestMapping(path = "/bonus/account/getUserShopBonusListNoPages", method =RequestMethod.POST)
//	 public BaseResult<List<UserBonusDTO>> getUserShopBonusList(@RequestBody GetUserShopBonusParam getUserShopBonusParam);
//	 
//	 
//	 /**
//	  * 购物车获取店铺红包
//	  * @param userBonusIdsParam
//	  * @return
//	  */
//	 @RequestMapping(path = "/bonus/account/getShopBonusList", method =RequestMethod.POST)
//	 public BaseResult<List<UserBonusDTO>> getShopBonusList(GetShopBonusParam getShopBonusParam);
//
//	/**
//	 * 获取我领取的平台红包列表
//	 *
//	 * @param
//	 * @return
//	 */
//	@RequestMapping(path = "/bonus/myStoreBonusList", method = RequestMethod.POST)
//	public BaseResult<UserBonusDTO> getStoreBonusList(@Valid @RequestBody OrderInfoIdParam orderInfoIdParam);
//
//	/**
//	 * 获取我领取的平台红包列表（不带有分页）
//	 *
//	 * @param
//	 * @return
//	 */
//	@RequestMapping(path = "/bonus/myStoreBonusListNoPages", method = RequestMethod.POST)
//	public BaseResult<List<UserBonusDTO>> getStoreBonusListNoPages(@Valid @RequestBody OrderInfoIdParam orderInfoIdParam);
//	
//
//    /**
//     * 领取店铺红包
//     *
//     * @param
//     * @return
//     */
//    @RequestMapping(path = "/bonus/getShopBonustoOwner", method = RequestMethod.POST)
//    public BaseResult<String> getShopBonusToOwner(ShopBonusParam shopBonusParam);
//
//    /**
//     * 获取红包发放数量
//     *
//     * @return
//     */
//    @PostMapping("/bonus/getBonusCount")
//    BaseResult<List<UserBonusCountDTO>> getBonusCount(@RequestBody UserBonusIdsParam userBonusIdsParam);
//    
//    /**
//     * 根据多个UserBonusId查询红包，红包类型为21的返回值中添加goods_ids
//     *
//     * @return
//     */
//    @PostMapping("/bonus/queryUserBonusByUserBonusIds")    
//    BaseResult<List<UserBonusDTO>> queryUserBonusByUserBonusIds(@RequestBody UserBonusIdArrParam userBonusIdArrParam);
//    
//    
//    /**
//     * 校验红包是否可用，返回积分退还红包类型的 goods_ids 和红包类型
//     *
//     * @return
//     */
//    @PostMapping("/bonus/validUserShopBonus")    
//    BaseResult<List<UserBonusDTO>> validUserShopBonus(@RequestBody ValidUserBonusParam validUserBonusParam);
//    
//    /**
//     * 获取我领取的免邮红包列表
//     *
//     * @return
//     */
//    @PostMapping("/bonus/queryFreeShippingBonusList")
//    BaseResult<List<UserBonusDTO>> queryFreeShippingBonusList(@RequestBody OrderInfoIdParam orderInfoIdParam);
    
}
