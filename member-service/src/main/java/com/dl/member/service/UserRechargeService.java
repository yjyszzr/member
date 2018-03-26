package com.dl.member.service;
import com.dl.member.model.UserRecharge;
import com.dl.member.param.UpdateUserRechargeParam;
import com.dl.member.param.UserAccountParam;

import lombok.extern.slf4j.Slf4j;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserRechargeMapper;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class UserRechargeService extends AbstractService<UserRecharge> {
    @Resource
    private UserRechargeMapper userRechargeMapper;
    
    @Resource
    private UserAccountService userAccountService;
    
    public static SNGenerator snGenerator = new SNGenerator();
    
    
    /**
     * 创建充值单
     * @param amount
     * @return
     */
    public String saveReCharege(BigDecimal amount){
    	Integer userId = SessionUtil.getUserId();
    	UserRecharge userRecharge = new UserRecharge();
    	String rechargeSn = snGenerator.nextSN(SNBusinessCodeEnum.RECHARGE_SN.getCode());
    	userRecharge.setRechargeSn(rechargeSn);
    	userRecharge.setAmount(amount);
    	userRecharge.setUserId(userId);
    	userRecharge.setAddTime(DateUtil.getCurrentTimeLong());
    	userRecharge.setStatus(ProjectConstant.NOT_FINISH);
    	int rst = userRechargeMapper.insertUserRecharge(userRecharge);
    	if(1 != rst) {
    		log.error("保存数据库充值单失败");
    	}

		return rechargeSn;
    }
    
    /**
     * 根据充值单号查询充值单
     */
    public BaseResult<UserRecharge> queryUserRecharge(String rechargeSn){
    	UserRecharge userRechargeQuery = new UserRecharge();
    	userRechargeQuery.setRechargeSn(rechargeSn);
    	List<UserRecharge> userRechargeList = userRechargeMapper.queryUserChargeBySelective(userRechargeQuery);
    	if(CollectionUtils.isEmpty(userRechargeList)) {
    		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), "充值单不存在");
    	}
    	return ResultGenerator.genSuccessResult("查询充值单成功", userRechargeList.get(0));
    }
    
    
    /**
     * 更新充值单
     * @param amount
     * @return
     */
    @Transactional
    public BaseResult<String> updateReCharege(UpdateUserRechargeParam updateUserRechargeParam){
    	BaseResult<UserRecharge> userRechargeRst =  this.queryUserRecharge(updateUserRechargeParam.getRechargeSn());
    	if(userRechargeRst.getCode() != 0) {
    		throw new ServiceException(userRechargeRst.getCode(), userRechargeRst.getMsg());
    	}
    	BigDecimal amount = userRechargeRst.getData().getAmount();
    	
    	UserRecharge userRecharge = new UserRecharge();
    	userRecharge.setAmount(amount);
    	userRecharge.setPaymentCode(updateUserRechargeParam.getPaymentCode());
    	userRecharge.setPaymentId(updateUserRechargeParam.getPaymentId());
    	userRecharge.setPaymentName(updateUserRechargeParam.getPaymentName());
    	userRecharge.setPayTime(updateUserRechargeParam.getPayTime());
    	userRecharge.setStatus(updateUserRechargeParam.getStatus());
    	userRecharge.setRechargeSn(updateUserRechargeParam.getRechargeSn());
    	int rst = userRechargeMapper.updateUserRechargeBySelective(userRecharge);
    	if(1 != rst) {
    		log.error("更新数据库充值单失败");
    		return ResultGenerator.genFailResult("更新数据库充值单失败");
    	}
    	
    	SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = userAccountService.commonCalculateMoney(amount, ProjectConstant.RECHARGE);
        
    	UserAccountParam userAccountParam = new UserAccountParam();
        String accountSn = snGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
        userAccountParam.setAccountSn(accountSn);
        userAccountParam.setAmount(amount);
        userAccountParam.setCurBalance(surplusPaymentCallbackDTO.getCurBalance());
        userAccountParam.setAccountType(ProjectConstant.BUY);
        userAccountParam.setOrderSn("");
        userAccountParam.setPaymentName(updateUserRechargeParam.getPaymentName());
        userAccountParam.setThirdPartName(updateUserRechargeParam.getPaymentName());
        userAccountParam.setThirdPartPaid(amount);
        userAccountParam.setUserSurplus(surplusPaymentCallbackDTO.getUserSurplus());
        userAccountParam.setUserSurplusLimit(surplusPaymentCallbackDTO.getUserSurplusLimit());
        userAccountParam.setUserName("");
        userAccountParam.setNote("");
        userAccountParam.setPayId(updateUserRechargeParam.getPaymentId());
    	String rechargeSn = userAccountService.saveAccount(userAccountParam);
		if(StringUtils.isEmpty(rechargeSn)) {
			return ResultGenerator.genFailResult("更新数据库充值单失败");
		}
    	return ResultGenerator.genSuccessResult("更新数据库充值单失败");
    }

}
