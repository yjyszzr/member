package com.dl.member.service;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.SwitchConfigMapper;
import com.dl.member.dao.UserAccountMapper;
import com.dl.member.dao.UserMapper;
import com.dl.member.dto.SwitchConfigDTO;
import com.dl.member.model.SwitchConfig;
import com.dl.member.model.User;
import com.dl.member.param.QuerySwitchParam;
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
	 * @param plat
	 * @param version
	 * @param chanel
	 * @return 目前返回对象只用到了trunon字段
	 */
	 public BaseResult<SwitchConfigDTO> querySwitch(){
		 UserDeviceInfo userDevice = SessionUtil.getUserDevice();
	    	String inPrams = JSON.toJSONString(userDevice);
	    	String logId = DateUtil.getCurrentDateTime();
	    	log.info(logId + "====================================版本参数:"+inPrams);
	    	String plat = "";
	    	if(userDevice.getPlat().equals("android")) {
	    		plat = "1";
	    	}else if(userDevice.getPlat().equals("iphone")) {
	    		plat = "0";
	    		//黑名单判断
	    		Integer userSwitchByIp = this.userSwitchByIp();
	        	log.info(logId + "===========判断用户ip所属区域是否打开交易返回：" + userSwitchByIp);
	        	if(userSwitchByIp.equals(ProjectConstant.BISINESS_APP_CLOSE)) {
	        		//判决是否需要回捞
	        		boolean isHuiLao = false;
	        		if(!isHuiLao) {//不需要，返回资讯
	        			SwitchConfigDTO switchConfig = new SwitchConfigDTO();
	        			switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
	        			log.info(logId + "====非国内IP或别的区域=======判断用户ip为非需要打开交易,现执行关闭交易版返回");
	        			return ResultGenerator.genSuccessResult("success",switchConfig);
	        		}
	        	}
	    		//idfa 回调、存储  （lidelin）
	    		/*IDFACallBackParam idfaParam = new IDFACallBackParam();
	    		idfaParam.setUserid(-1);
	    		idfaParam.setIdfa(userDevice.getIDFA());
	    		iDFAService.callBackIdfa(idfaParam);*/
	    	}else if(userDevice.getPlat().equals("h5")) {
	    		plat = "2";
	    	}else {
	    		return ResultGenerator.genFailResult("设备信息中的plat参数错误");
	    	}
		 
	    	String version = userDevice.getAppv();
	    	String chanel = userDevice.getChannel();
	    	SwitchConfigDTO switchConfig = new SwitchConfigDTO();
	    	Integer userId = SessionUtil.getUserId();
	    	log.info("开关接口传的登录的userId:"+userId);
	    	//非登录用户
	    	if(userId == null) {
				Integer rst3 = this.channelSwitch(plat, version, chanel);
				log.info("渠道开关:"+rst3);
				if(rst3 == 1) {//渠道开
					//判断该城市是否需要关闭
					boolean channelSwitch = this.channelSwitchByIp(chanel);
					if(channelSwitch) {
						switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
					}else {
						switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
					}
				}else {//渠道关
					switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
				}
				log.info("channel="+chanel + "  turnOn="+switchConfig.getTurnOn());
				return ResultGenerator.genSuccessResult("success",switchConfig);
	    	}
	    	//登录用户判断
	    	Integer rst1 = this.userSwitch(userId);
	    	log.info("用户终极开关:"+rst1);
	    	if(rst1 == 0) {//用户终极开关关闭
	    		switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
	    	}else if(rst1 == 1) {//用户终极开关打开
	    		switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
	    	}else {//用户终极开关取消，不起作用
	    		//判断用户交易行为
	    		Integer rst2 = this.userDealAction(userId);
	    		if(rst2 == 1) {//有交易
	    			log.info("用户交易行为开关:"+rst2);
	    			switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
	    		}else{//无交易
	    			Integer rst3 = this.channelSwitch(plat, version, chanel);
	    			log.info("渠道开关:"+rst3);
	    			//渠道开关判断
	    			if(rst3 == 1) {//渠道开关打开
	    				//判断该城市是否需要关闭
	    				boolean channelSwitch = this.channelSwitchByIp(chanel);
	    				if(channelSwitch) {//不需要关闭
	    					switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
	    				}else {//需要关闭
	    					switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
	    				}
	    			}else {//渠道开关关闭
	    				switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
	    			}
	    		}
	    	}
	    	log.info("channel="+chanel + "  turnOn="+switchConfig.getTurnOn());
	    	return ResultGenerator.genSuccessResult("success",switchConfig);
	    }
	 public BaseResult<SwitchConfigDTO> querySwitchOld(){
		 UserDeviceInfo userDevice = SessionUtil.getUserDevice();
		 String inPrams = JSON.toJSONString(userDevice);
		 String logId = DateUtil.getCurrentDateTime();
		 log.info(logId + "====================================版本参数:"+inPrams);
		 String plat = "";
		 if(userDevice.getPlat().equals("android")) {
			 plat = "1";
		 }else if(userDevice.getPlat().equals("iphone")) {
			 plat = "0";
			 Integer userSwitchByIp = this.userSwitchByIp();
			 log.info(logId + "===========判断用户ip所属区域是否打开交易返回：" + userSwitchByIp);
			 if(userSwitchByIp.equals(ProjectConstant.BISINESS_APP_CLOSE)) {
				 SwitchConfigDTO switchConfig = new SwitchConfigDTO();
				 switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
				 log.info(logId + "====非国内IP或别的区域=======判断用户ip为非需要打开交易,现执行关闭交易版返回");
				 return ResultGenerator.genSuccessResult("success",switchConfig);
			 }
			 //idfa 回调、存储  （lidelin）
			 /*IDFACallBackParam idfaParam = new IDFACallBackParam();
	    		idfaParam.setUserid(-1);
	    		idfaParam.setIdfa(userDevice.getIDFA());
	    		iDFAService.callBackIdfa(idfaParam);*/
		 }else if(userDevice.getPlat().equals("h5")) {
			 plat = "2";
		 }else {
			 return ResultGenerator.genFailResult("设备信息中的plat参数错误");
		 }
		 
		 String version = userDevice.getAppv();
		 String chanel = userDevice.getChannel();
		 SwitchConfigDTO switchConfig = new SwitchConfigDTO();
		 Integer userId = SessionUtil.getUserId();
		 log.info("开关接口传的登录的userId:"+userId);
		 if(userId == null) {
			 Integer rst3 = this.channelSwitch(plat, version, chanel);
			 log.info("渠道开关:"+rst3);
			 if(rst3 == 1) {
				 //判断该城市是否需要关闭
				 boolean channelSwitch = this.channelSwitchByIp(chanel);
				 if(channelSwitch) {
					 switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
				 }else {
					 switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
				 }
			 }else {
				 switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
			 }
			 log.info("channel="+chanel + "  turnOn="+switchConfig.getTurnOn());
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
				 Integer rst3 = this.channelSwitch(plat, version, chanel);
				 log.info("渠道开关:"+rst3);
				 if(rst3 == 1) {
					 //判断该城市是否需要关闭
					 boolean channelSwitch = this.channelSwitchByIp(chanel);
					 if(channelSwitch) {
						 switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
					 }else {
						 switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
					 }
				 }else {
					 switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
				 }
			 }
		 }
		 log.info("channel="+chanel + "  turnOn="+switchConfig.getTurnOn());
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
    	if(null == userId) {
    		return  ProjectConstant.BISINESS_APP_OPEN;
    	}
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
     * 渠道城市是否打开开关
     * @return
     */
   /* public boolean channelCitySwitch(String chanel,QuerySwitchParam param) {
    	String provinceCode = param.getProvinceCode();
    	String cityCode = param.getCityCode();
    	log.info("chanel="+chanel+"渠道城市是否打开开关provinceCode:"+provinceCode+" , cityCode:"+cityCode);
    	if(StringUtils.isBlank(provinceCode) && StringUtils.isBlank(cityCode)) {
    		return true;
    	}
    	String closeCitys = switchConfigMapper.queryChannelCloseCitys(chanel);
    	log.info("chanel="+chanel+"渠道城市是否打开开关provinceCode:"+provinceCode+" , cityCode:"+cityCode+" , closeCitys:"+closeCitys);
    	if(StringUtils.isBlank(closeCitys)) {
    		return true;
    	}
    	List<String> closeCityList = Arrays.asList(closeCitys.split(","));
    	if(StringUtils.isNotBlank(provinceCode) && closeCityList.contains(provinceCode)) {
    		return false;
    	}
    	if(StringUtils.isNotBlank(cityCode) && closeCityList.contains(cityCode)) {
    		return false;
    	}
    	return true;
    }*/
    
    public boolean channelSwitchByIp(String chanel) {
    	UserDeviceInfo userDevice = SessionUtil.getUserDevice();
    	String userIp = userDevice.getUserIp();
    	if(StringUtils.isBlank(userIp)) {
    		return true;
    	}
    	String closeCitys = switchConfigMapper.queryChannelCloseCitys(chanel);
    	log.info("chanel="+chanel+" , closeCitys:"+closeCitys);
    	if(StringUtils.isBlank(closeCitys)) {
    		return true;
    	}
    	String[] closeCityArr = closeCitys.split(",");
    	int num = switchConfigMapper.checkChannelUserIp(userIp, closeCityArr);
    	log.info("chanel="+chanel+" , closeCitys:"+closeCitys+ " , userIp="+userIp + "  ,rst="+num);
    	if(num <= 0) {
    		return true;
    	}
    	return false;
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
    	int num = switchConfigMapper.checkUserIp(userIp);
//    	boolean chinaIp = IpAdrressUtil.isChinaIp(userIp);
    	if(num <= 0) {
    		return ProjectConstant.BISINESS_APP_OPEN;
    	}
    	return ProjectConstant.BISINESS_APP_CLOSE;
    }
    
}
