package com.dl.member.service;
import com.dl.member.model.UserRecharge;
import com.dl.member.model.UserWithdraw;
import com.dl.member.param.UpdateUserRechargeParam;
import com.dl.member.param.UpdateUserWithdrawParam;
import com.dl.member.param.UserWithdrawParam;

import lombok.extern.slf4j.Slf4j;

import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserWithdrawMapper;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
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
public class UserWithdrawService extends AbstractService<UserWithdraw> {
    @Resource
    private UserWithdrawMapper userWithdrawMapper;
    
    /**
     * 创建提现单
     * @param amount
     * @return
     */
    public String saveWithdraw(UserWithdrawParam  userWithdrawParam){
    	Integer userId = SessionUtil.getUserId();
    	String withdrawalSn = this.createSn("4");
    	UserWithdraw userWithdraw = new UserWithdraw();
    	userWithdraw.setWithdrawalSn(withdrawalSn);
    	userWithdraw.setAmount(userWithdrawParam.getAmount());
    	userWithdraw.setAddTime(DateUtil.getCurrentTimeLong());
    	userWithdraw.setRealName(userWithdrawParam.getRealName());
    	userWithdraw.setCardNo(userWithdrawParam.getCardNo());
    	userWithdraw.setUserId(userId);
    	userWithdraw.setStatus(Integer.valueOf(ProjectConstant.NOT_FINISH));
    	int rst = userWithdrawMapper.insert(userWithdraw);
    	if(1 != rst) {
    		log.error("");
    	}
		return withdrawalSn;
    }
    
    
    /**
     * 更新提现单
     * @param amount
     * @return
     */
    public String updateWithdraw(UpdateUserWithdrawParam updateUserWithdrawParam){
    	UserWithdraw userWithdraw = new UserWithdraw();
    	userWithdraw.setPaymentId(updateUserWithdrawParam.getPaymentId());
    	userWithdraw.setPayTime(updateUserWithdrawParam.getPayTime());
    	userWithdraw.setStatus(Integer.valueOf(updateUserWithdrawParam.getStatus()));
    	int rst = userWithdrawMapper.updateUserWithdrawBySelective(userWithdraw);
    	if(1 != rst) {
    		log.error("更新数据库提现单失败");
    	}
		return "success";
    }
    
    /**
     * 生成流水号
     */
    public static String createSn(String account_type) {
        String s1 = DateUtil.getCurrentTimeString(DateUtil.getCurrentTimeLong() + Long.valueOf(8 * 3600), DateUtil.yyyyMMdd);
        String s2 = StringUtils.rightPad(account_type, 2, "0");
        int s3 = new Random().nextInt(900000) + 100000;
        return s1 + s2 + String.valueOf(s3);
    }

}
