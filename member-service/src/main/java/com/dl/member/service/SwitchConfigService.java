package com.dl.member.service;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.SwitchConfigMapper;
import com.dl.member.dao.UserAccountMapper;
import com.dl.member.dao.UserMapper;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.model.SwitchConfig;
import com.dl.member.model.User;
import com.dl.member.util.IpAdrressUtil;
import com.dl.shop.payment.api.IpaymentService;

import lombok.extern.slf4j.Slf4j;

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
    private UserAccountMapper userAccountMapper;
    
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
    public BaseResult<SwitchConfigDTO> querySwitch(String platform,String version,String chanel){
    	SwitchConfigDTO switchConfig = new SwitchConfigDTO();
    	Integer userId = SessionUtil.getUserId();
    	log.info("开关接口传的登录的userId:"+userId);
    	if(userId == null) {
			Integer rst3 = this.channelSwitch(platform, version, chanel);
			log.info("渠道开关:"+rst3);
			if(rst3 == 1) {
				switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
			}else {
				switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
			}
			return ResultGenerator.genSuccessResult("success",switchConfig);
    	}
    	
    	Integer rst1 = this.userSwitch(userId);
    	log.info("用户终极开关:"+rst1);
    	if(rst1 == 0) {
    		switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
    	}else if(rst1 == 1) {
    		switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
    	}else {
    		Integer rst2 = this.userDealAction(userId);
    		if(rst2 == 1) {
    			log.info("用户交易行为开关:"+rst2);
    			switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
    		}else{
    			Integer rst3 = this.channelSwitch(platform, version, chanel);
    			log.info("渠道开关:"+rst3);
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
//		String paidUser = stringRedisTemplate.opsForValue().get("pay_valid_"+String.valueOf(userId));
//		if(!StringUtils.isEmpty(paidUser)) {
//			return ProjectConstant.BISINESS_APP_OPEN;
//		}
		Integer rst = userAccountMapper.countValidUserAccountByUserId(userId);
		if(rst > 0) {
			//stringRedisTemplate.opsForValue().set("pay_valid_"+String.valueOf(userId),"1");
			return ProjectConstant.BISINESS_APP_OPEN;
		}else {
			return ProjectConstant.BISINESS_APP_CLOSE;
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
    
    /**
     * 通过用户ip判断是否打开开关
     * @return
     */
    public Integer userSwitchByIp() {
    	UserDeviceInfo userDevice = SessionUtil.getUserDevice();
    	String userIp = userDevice.getUserIp();
    	if(StringUtils.isBlank(userIp)) {
    		return ProjectConstant.BISINESS_APP_OPEN;
    	}
    	boolean chinaIp = IpAdrressUtil.isChinaIp(userIp);
    	if(chinaIp) {
    		return ProjectConstant.BISINESS_APP_OPEN;
    	}
    	return ProjectConstant.BISINESS_APP_CLOSE;
    }
    
}
