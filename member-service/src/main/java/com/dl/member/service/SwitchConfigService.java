package com.dl.member.service;
import com.dl.member.model.SwitchConfig;
import com.dl.member.model.User;
import com.dl.shop.payment.api.IpaymentService;
import com.dl.shop.payment.dto.ValidPayDTO;

import lombok.extern.slf4j.Slf4j;

import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.SwitchConfigMapper;
import com.dl.member.dao.UserMapper;
import com.dl.member.enums.MemberEnums;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.SessionUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service
@Transactional
@Slf4j
public class SwitchConfigService extends AbstractService<SwitchConfig> {
    @Resource
    private SwitchConfigMapper switchConfigMapper;
 
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private IpaymentService paymentService;
    
	@Resource
	private StringRedisTemplate stringRedisTemplate;    
    
	/**
	 * 交易版开关的优先级：一级比一级弱
	 * 1.用户的超级开关
	 * 2.用户交易行为
	 * 3.渠道开关
	 * @param platform
	 * @param version
	 * @param chanel
	 * @return
	 */
    public BaseResult<SwitchConfig> querySwitch(String platform,String version,String chanel){
    	SwitchConfig switchConfig = new SwitchConfig();
    	Integer userId = SessionUtil.getUserId();
    	if(userId == null) {
			Integer rst3 = this.channelSwitch(platform, version, chanel);
			if(rst3 == 1) {
				switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
			}else {
				switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
			}
			return ResultGenerator.genSuccessResult("success",switchConfig);
    	}
    	
    	Integer rst1 = this.userSwitch(userId);
    	if(rst1 == 0) {
    		switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
    	}else if(rst1 == 1) {
    		switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
    	}else {
    		Integer rst2 = this.userDealAction(userId);
    		if(rst2 == 1) {
    			switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
    		}else{
    			Integer rst3 = this.channelSwitch(platform, version, chanel);
    			if(rst3 == 1) {
    				switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
    			}else {
    				switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
    			}
    		}
    	}
    	return ResultGenerator.genSuccessResult("success",switchConfig);
    }

    /**
     * 用户超级开关
     * @param userId
     * @return
     */
    public Integer userSwitch(Integer userId) {
    	User user = userMapper.queryUserExceptPass(userId);
    	return user.getIsBusiness();
    }
    
    /**
     *用户交易行为开关
     * @return
     */
    public Integer userDealAction(Integer userId) {
		String paidUser = stringRedisTemplate.opsForValue().get("pay_valid_"+String.valueOf(userId));
		if(!StringUtils.isEmpty(paidUser)) {
			return ProjectConstant.BISINESS_APP_OPEN;
		}else {
			com.dl.shop.payment.param.UserIdParam userIdParam = new com.dl.shop.payment.param.UserIdParam();
			userIdParam.setUserId(userId);
			BaseResult<ValidPayDTO> validPayDTORst = paymentService.validUserPay(userIdParam);
			if(validPayDTORst.getCode() == 0) {
				String hasPaid = validPayDTORst.getData().getHasPaid();
				if(hasPaid.equals("1")) {
					stringRedisTemplate.opsForValue().set("pay_valid_"+String.valueOf(userId),"1");
					return ProjectConstant.BISINESS_APP_OPEN;
				}else {
					return ProjectConstant.BISINESS_APP_CLOSE;
				}
			}else {
				log.error("判断开关的时候，查询是否有关交易异常，开关返回交易版开");
				return ProjectConstant.BISINESS_APP_OPEN;
			}
		}
    }
    
    /**
     * 渠道开关
     * @return
     */
    public Integer channelSwitch(String platform,String version,String chanel) {
    	List<SwitchConfig> switchConfigList = switchConfigMapper.querySwitchConfigTurnOff(platform,version,chanel);
    	if(CollectionUtils.isEmpty(switchConfigList)) {
    		return ProjectConstant.BISINESS_APP_CLOSE;
    	}
    	SwitchConfig switchConfig = switchConfigList.get(0);
    	Integer turnOn = switchConfig.getTurnOn();
    	return turnOn;
    }
    
}
