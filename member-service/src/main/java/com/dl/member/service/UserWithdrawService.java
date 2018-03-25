package com.dl.member.service;
import com.dl.member.model.UserWithdraw;
import com.dl.member.param.UpdateUserWithdrawParam;
import com.dl.member.param.UserWithdrawParam;
import lombok.extern.slf4j.Slf4j;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserWithdrawMapper;
import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SNGenerator;
import com.dl.base.util.SessionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class UserWithdrawService extends AbstractService<UserWithdraw> {
    @Resource
    private UserWithdrawMapper userWithdrawMapper;
    
    public static SNGenerator snGenerator = new SNGenerator();
    
    /**
     * 创建提现单
     * @param amount
     * @return
     */
    public String saveWithdraw(UserWithdrawParam  userWithdrawParam){
    	Integer userId = SessionUtil.getUserId();
    	String withdrawalSn = snGenerator.nextSN(SNBusinessCodeEnum.WITHDRAW_SN.getCode());
    	UserWithdraw userWithdraw = new UserWithdraw();
    	userWithdraw.setUserId(userId);
    	userWithdraw.setWithdrawalSn(withdrawalSn);
    	userWithdraw.setAmount(userWithdrawParam.getAmount());
    	userWithdraw.setAddTime(DateUtil.getCurrentTimeLong());
    	userWithdraw.setRealName(userWithdrawParam.getRealName());
    	userWithdraw.setCardNo(userWithdrawParam.getCardNo());
    	userWithdraw.setStatus(ProjectConstant.NOT_FINISH);
    	int rst = userWithdrawMapper.insertUserWithdraw(userWithdraw);
    	if(1 != rst) {
    		log.error("保存数据库提现单失败");
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
    	userWithdraw.setStatus(updateUserWithdrawParam.getStatus());
    	userWithdraw.setWithdrawalSn(updateUserWithdrawParam.getWithdrawalSn());
    	int rst = userWithdrawMapper.updateUserWithdrawBySelective(userWithdraw);
    	if(1 != rst) {
    		log.error("更新数据库提现单失败");
    	}
		return "success";
    }
    
}
