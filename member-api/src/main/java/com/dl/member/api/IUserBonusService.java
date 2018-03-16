package com.dl.member.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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


/**
 * 用户账户接口
 *
 * @author zhangzirong
 */
@FeignClient(value = "member-service")
public interface IUserBonusService {

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
