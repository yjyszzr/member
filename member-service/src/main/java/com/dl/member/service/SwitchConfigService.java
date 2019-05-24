package com.dl.member.service;
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
import com.dl.shop.payment.api.IpaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

	@Resource
	private SysConfigService sysConfigService;
    
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
		 SwitchConfigDTO switchConfig = new SwitchConfigDTO();
		 String inPrams = JSON.toJSONString(userDevice);
		 String logId = DateUtil.getCurrentDateTime();
		 log.info(logId + "====================================版本参数:"+inPrams);
		 String plat = userDevice.getPlat();
		 if(plat.equals("android")) {
			 plat = "1";
		 }else if(plat.equals("iphone")) {
			 plat = "0";
		 }else if(plat.equals("h5")) {
			 plat = "2";
		 }else {
			 return ResultGenerator.genFailResult("设备信息中的plat参数错误");
		 }

		 String version = userDevice.getAppv();
		 String chanel = userDevice.getChannel();
		 Integer userId = SessionUtil.getUserId();
		 log.info("开关接口传的登录的userId:"+userId);
		 if(userDevice.getPlat().equals("h5")) {//h5 的plat,version 和 channel 写死，分别为2和1.0.0和h5
			 version = "1.0.0";
			 chanel = "h5";
		 }
		 Integer rst3 = this.channelSwitch(plat, version, chanel);
		 log.info("渠道开关:"+rst3);
		 if(rst3 == 1) {//渠道开
			 switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
		 }else {//渠道关
                 SysConfigDTO sysConfigDTO = sysConfigService.querySysConfig(70);//用户开关
                 Integer userTurnOn = sysConfigDTO.getValue().intValue();
                 if(userTurnOn == 0){//关
                     switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
                 }else{//开
                    if (userId == null){//未登录
                        switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
                    }else{//已登录
                        switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
                    }
                 }
		 }

		 Integer rst1 = this.userSwitch(userId);
         log.info("用户终极开关:"+rst1);
         if(rst1 == 0) {//用户终极开关关闭,对过审用户限制
             switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_CLOSE);
         }

		 //h5强行打开
		 if("h5".equals(chanel)) {
			 log.info("[querySwitch]" + " channel:" + chanel);
			 switchConfig.setTurnOn(ProjectConstant.BISINESS_APP_OPEN);
		 }
		 log.info("channel="+chanel + "turnOn="+switchConfig.getTurnOn());
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
    	if(null == userId) {
    		return  ProjectConstant.BISINESS_APP_OPEN;
    	}
		Integer rst = userAccountMapper.countValidUserAccountByUserId(userId);
		if(rst > 0) {
			String mobile = userMapper.getMobileById(userId);
			//非白名单都记为黑名单(2018-09-26)
			boolean isWhite = this.checkUserWhiteList(mobile);
			if(!isWhite) {
				return ProjectConstant.BISINESS_APP_CLOSE;
			}
			return ProjectConstant.BISINESS_APP_OPEN;
		}else {
			return ProjectConstant.BISINESS_APP_CLOSE;
		}
    }
    
    /**
     * 判断用户白名单
     * @param mobile
     * @return
     */
    private boolean checkUserWhiteList(String mobile) {
    	Integer isWhite = switchConfigMapper.checkUserWhiteValue(mobile);
    	if(null != isWhite && isWhite == 1) {
    		return true;
    	}
		return false;
	}
    
    
    /**
     * 判断用户黑名单
     * @param mobile
     * @return
     */
    private boolean checkUserBlackList(String mobile) {
    	Integer isBlack = switchConfigMapper.checkUserWhiteValue(mobile);
    	if(isBlack !=null && isBlack == 0) {
    		return true;
    	}
    	return false;
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
