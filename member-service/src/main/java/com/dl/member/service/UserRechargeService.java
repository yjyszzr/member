package com.dl.member.service;
import com.dl.member.model.UserRecharge;
import com.dl.member.param.UpdateUserRechargeParam;
import lombok.extern.slf4j.Slf4j;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserRechargeMapper;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.result.BaseResult;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Random;
import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class UserRechargeService extends AbstractService<UserRecharge> {
    @Resource
    private UserRechargeMapper userRechargeMapper;
    
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
     * 更新充值单
     * @param amount
     * @return
     */
    public String updateReCharege(UpdateUserRechargeParam updateUserRechargeParam){
    	UserRecharge userRecharge = new UserRecharge();
    	userRecharge.setPaymentCode(updateUserRechargeParam.getPaymentCode());
    	userRecharge.setPaymentId(updateUserRechargeParam.getPaymentId());
    	userRecharge.setPaymentName(updateUserRechargeParam.getPaymentName());
    	userRecharge.setPayTime(updateUserRechargeParam.getPayTime());
    	userRecharge.setStatus(updateUserRechargeParam.getStatus());
    	userRecharge.setRechargeSn(updateUserRechargeParam.getRechargeSn());
    	int rst = userRechargeMapper.updateUserRechargeBySelective(userRecharge);
    	if(1 != rst) {
    		log.error("更新数据库充值单失败");
    	}
    	
		return "success";
    }

}
