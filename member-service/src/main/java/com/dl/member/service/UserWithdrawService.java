package com.dl.member.service;
import com.dl.member.model.UserBank;
import com.dl.member.model.UserWithdraw;
import com.dl.member.param.UpdateUserWithdrawParam;
import com.dl.member.param.UserAccountParam;
import com.dl.member.param.UserWithdrawParam;
import lombok.extern.slf4j.Slf4j;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserWithdrawMapper;
import com.dl.member.dto.SurplusPaymentCallbackDTO;
import com.dl.member.dto.UserWithdrawDTO;
import com.dl.member.dto.WithdrawalSnDTO;
import com.dl.member.enums.MemberEnums;
import com.alibaba.fastjson.JSON;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class UserWithdrawService extends AbstractService<UserWithdraw> {
    @Resource
    private UserWithdrawMapper userWithdrawMapper;
    
    @Resource
    private UserAccountService userAccountService;
    
    @Resource
    private UserBankService userBankService;
    
    public static SNGenerator snGenerator = new SNGenerator();
    
    /**
     * 创建提现单
     * @param amount
     * @return
     */
    public BaseResult<WithdrawalSnDTO> saveWithdraw(UserWithdrawParam  userWithdrawParam){
    	Integer userId = SessionUtil.getUserId();
    	String withdrawalSn = snGenerator.nextSN(SNBusinessCodeEnum.WITHDRAW_SN.getCode());
    	UserWithdraw userWithdraw = new UserWithdraw();
    	userWithdraw.setUserId(userId);
    	userWithdraw.setWithdrawalSn(withdrawalSn);
    	userWithdraw.setAmount(userWithdrawParam.getAmount());
    	userWithdraw.setAddTime(DateUtil.getCurrentTimeLong());
    	userWithdraw.setRealName(userWithdrawParam.getRealName());
    	userWithdraw.setCardNo(userWithdrawParam.getCardNo());
    	String cardNo = userWithdrawParam.getCardNo();
    	UserBank userBank = userBankService.findBy("cardNo", userWithdrawParam.getCardNo());
    	if(userBank == null) {
    		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), MemberEnums.DBDATA_IS_NULL.getMsg());
    	}
    	userWithdraw.setBankName(userBank.getBankName());
    	userWithdraw.setStatus(ProjectConstant.NOT_FINISH);
    	int rst = userWithdrawMapper.insertUserWithdraw(userWithdraw);
    	if(1 != rst) {
    		log.error("保存数据库提现单失败");
    		return ResultGenerator.genFailResult("保存数据库提现单失败");
    		
    	}
    	
    	WithdrawalSnDTO withdrawalSnDTO = new WithdrawalSnDTO();
    	withdrawalSnDTO.setWithdrawalSn(withdrawalSn);
		return  ResultGenerator.genSuccessResult("保存数据库提现单成功", withdrawalSnDTO) ;
    }
 
    
    
    /**
     * 根据提现单号查询提现单
     */
    public BaseResult<UserWithdraw> queryUserWithdraw(String withDrawSn){
    	UserWithdraw userWithdraw = new UserWithdraw();
    	userWithdraw.setWithdrawalSn(withDrawSn);
    	List<UserWithdraw> userWithdrawList = userWithdrawMapper.queryUserWithdrawBySelective(userWithdraw);
    	if(CollectionUtils.isEmpty(userWithdrawList)) {
    		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), "提现单不存在");
    	}
    	return ResultGenerator.genSuccessResult("查询提现单成功", userWithdrawList.get(0));
    }
    
    /**
     * 根据accountId 查询提现单
     * @param accountId
     * @return
     */
    public BaseResult<UserWithdrawDTO> queryUserWithDrawByAccountId(Integer accountId){
    	UserWithdraw userWithdrawQuery =new UserWithdraw();
    	userWithdrawQuery.setAccountId(accountId);
    	List<UserWithdraw> userWithDrawList =  userWithdrawMapper.queryUserWithdrawBySelective(userWithdrawQuery);
    	if(CollectionUtils.isEmpty(userWithDrawList)) {
    		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), MemberEnums.DBDATA_IS_NULL.getMsg());
    	}
    	UserWithdrawDTO userWithDrawDTO = new UserWithdrawDTO();
    	BeanUtils.copyProperties(userWithDrawList.get(0), userWithDrawDTO);
		return ResultGenerator.genSuccessResult("查询提现单成功",userWithDrawDTO);
    }
    
    /**
     * 更新提现单
     * @param amount
     * @return
     */
    public BaseResult<String> updateWithdraw(UpdateUserWithdrawParam updateUserWithdrawParam){
    	String inPrams = JSON.toJSONString(updateUserWithdrawParam);
    	log.info(DateUtil.getCurrentDateTime()+"更新提现单参数:"+inPrams);
    	
    	BaseResult<UserWithdraw> userWithdrawRst = this.queryUserWithdraw(updateUserWithdrawParam.getWithdrawalSn());
    	if(userWithdrawRst.getCode() != 0) {
    		return ResultGenerator.genResult(userWithdrawRst.getCode(), userWithdrawRst.getMsg());
    	}
    	BigDecimal amount = userWithdrawRst.getData().getAmount();
    	
    	UserWithdraw userWithdraw = new UserWithdraw();
    	userWithdraw.setPaymentId(updateUserWithdrawParam.getPaymentId());
    	userWithdraw.setPayTime(updateUserWithdrawParam.getPayTime());
    	userWithdraw.setStatus(updateUserWithdrawParam.getStatus());
    	userWithdraw.setWithdrawalSn(updateUserWithdrawParam.getWithdrawalSn());
    	int rst = userWithdrawMapper.updateUserWithdrawBySelective(userWithdraw);
    	if(1 != rst) {
    		log.error("更新数据库提现单失败");
    		return ResultGenerator.genFailResult("更新数据库提现单失败");
    	}
    	
    	SurplusPaymentCallbackDTO surplusPaymentCallbackDTO = userAccountService.commonCalculateMoney(amount, ProjectConstant.WITHDRAW);
    	
    	UserAccountParam userAccountParam = new UserAccountParam();
        String accountSn = snGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
        userAccountParam.setAccountSn(accountSn);
        userAccountParam.setAmount(amount);
        userAccountParam.setCurBalance(surplusPaymentCallbackDTO.getCurBalance());
        userAccountParam.setAccountType(ProjectConstant.WITHDRAW);
        userAccountParam.setOrderSn("");
        userAccountParam.setPaymentName(updateUserWithdrawParam.getPaymentName());
        userAccountParam.setThirdPartName(updateUserWithdrawParam.getPaymentName());
        userAccountParam.setThirdPartPaid(amount);
        userAccountParam.setUserSurplus(surplusPaymentCallbackDTO.getUserSurplus());
        userAccountParam.setUserSurplusLimit(surplusPaymentCallbackDTO.getUserSurplusLimit());
        userAccountParam.setUserName("");
        userAccountParam.setNote("提现"+amount+"元\n");
        userAccountParam.setLastTime(DateUtil.getCurrentTimeLong());
        userAccountParam.setStatus(Integer.valueOf(updateUserWithdrawParam.getStatus()));
        userAccountParam.setPayId(updateUserWithdrawParam.getPaymentId());
    	String withdrawSn = userAccountService.saveAccount(userAccountParam);
		if(StringUtils.isEmpty(withdrawSn)) {
			return ResultGenerator.genFailResult("更新数据库提现单失败");
		}
		return ResultGenerator.genSuccessResult("更新数据库提现单成功", withdrawSn);
    }
    
}
