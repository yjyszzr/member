package com.dl.member.service;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.configurer.RestTemplateConfig;
import com.dl.base.enums.RespStatusEnum;
import com.dl.base.enums.ThirdApiEnum;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.configurer.MemberConfig;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dao.UserMapper;
import com.dl.member.dao.UserRealMapper;
import com.dl.member.dto.UserRealDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.MemberThirdApiLog;
import com.dl.member.model.User;
import com.dl.member.model.UserReal;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Slf4j
public class UserRealService extends AbstractService<UserReal> {
    @Resource
    private UserRealMapper userRealMapper;
    
    @Resource
    private UserService userService;
    
	@Resource
	private RestTemplateConfig restTemplateConfig;
	
	@Resource
	private RestTemplate restTemplate;    
	
	@Resource
	private MemberConfig memberConfig;
	
	@Resource
	private UserMapper userMapper;
    
    @Transactional
    public void saveUserReal(String realName,String idCode) {
    	UserReal userReal = new UserReal();
    	userReal.setRealName(realName);
    	userReal.setIdCode(idCode);
    	userReal.setUserId(SessionUtil.getUserId());
    	userReal.setAddressInfo("");
    	userReal.setAddressNow("");
    	userReal.setAddTime(DateUtil.getCurrentTimeLong());
    	userReal.setCardPic1("");
    	userReal.setCardPic2("");
    	userReal.setCardPic3("");
    	userReal.setReason("");
    	userReal.setIsDelete(0);
    	userReal.setStatus(ProjectConstant.USER_IS_REAL);
    	this.save(userReal);
    }
    
    /**
     * 实名认证
     * @param realName
     * @param iDCode
     * @return
     * @throws UnsupportedEncodingException 
     */
    @Transactional
    public BaseResult<String> realNameAuth(String realName,String iDCode) {
    	Integer userId = SessionUtil.getUserId();
    	User user = userService.findById(userId);
    	if(null == user) {
    		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(), "用户不存在");
    	}

    	int countUserReal = userRealMapper.countUserRealByIDCodeAndUserId(userId,iDCode);
    	if(countUserReal > 0) {
    		return ResultGenerator.genResult(MemberEnums.DATA_ALREADY_EXIT_IN_DB.getcode(), "已经进行了实名认证");
    	}
    	
    	try {
			realName = URLDecoder.decode(realName, "UTF-8");
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResultGenerator.genResult(MemberEnums.COMMON_ERROR.getcode(), "实名认证的名字包含特殊符号");
		}
    	
    	//一个身份证最多绑定2个用户
    	Condition condition = new Condition(UserReal.class);
    	Criteria criteria = condition.createCriteria();
    	criteria.andCondition("id_code = ", iDCode);
    	List<UserReal> userRealList = this.findByCondition(condition);
    	if(userRealList.size() >= 2) {
			return ResultGenerator.genResult(MemberEnums.USER_REAL_COUNTLIMIT.getcode(),MemberEnums.USER_REAL_COUNTLIMIT.getMsg());
    	}
    		
    	JSONObject json = this.realNameAuth2(realName, iDCode);
    	String reason = json.getString("reason");
		Integer errorCode =  (Integer) json.get("error_code");
		if(0 == errorCode) {
			JSONObject result = (JSONObject) json.get("result");
			String res = result.getString("res");
			if("2".equals(res)) {
				return ResultGenerator.genResult(MemberEnums.NOT_REAL_AUTH.getcode(),MemberEnums.NOT_REAL_AUTH.getMsg());
			}
		}else {
			return ResultGenerator.genResult(MemberEnums.VERIFY_IDCARD_EROOR.getcode(),reason);
		}
    	
    	this.saveUserReal(realName,iDCode);
    	
    	User updateUser = new User();
    	updateUser.setUserId(userId);
    	updateUser.setIsReal(ProjectConstant.USER_IS_REAL);
    	userService.update(updateUser);
    	return ResultGenerator.genSuccessResult("实名认证成功");
    }

	/**
	 * 身份证实名认证
	 * @param realName
	 * @param cardNo
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public JSONObject realNameAuth2(String realName,String idcard) {
		ClientHttpRequestFactory clientFactory = restTemplateConfig.simpleClientHttpRequestFactory();
		RestTemplate rest = restTemplateConfig.restTemplate(clientFactory);
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);

		StringBuffer url = new StringBuffer(memberConfig.getRalNameApiURL());
		url.append("?key=" + memberConfig.getRealNameKey());
		url.append("&realname=" + realName);
		url.append("&idcard=" + idcard);
		String rst = rest.getForObject(url.toString(), String.class);
		
    	MemberThirdApiLog thirdApiLog = new MemberThirdApiLog(memberConfig.getRalNameApiURL(), ThirdApiEnum.JU_HE.getCode(), url.toString(), rst);
    	userMapper.saveMemberThirdApiLog(thirdApiLog);
    	
		JSONObject json = new JSONObject();
		try {
			json = JSON.parseObject(rst);
		} catch (Exception e) {
			log.error(e.getMessage());
			return json;
		}
		return json;
	}    
    
    /**
     * 查询用户真实信息
     * @return
     */
    public UserRealDTO queryUserReal(){
    	Integer userId = SessionUtil.getUserId();
    	UserReal userReal = userRealMapper.queryUserRealByUserId(userId);
    	log.info("[queryUserReal]" + " userId:" + userId + " userReal:" + userReal);
    	UserRealDTO userRealDTO = new UserRealDTO();
    	if(null == userReal) {
    		return null;
    	}
    	
    	try {
			BeanUtils.copyProperties(userRealDTO, userReal);
		} catch (Exception e) {
			throw new ServiceException(RespStatusEnum.SERVER_ERROR.getCode(), RespStatusEnum.SERVER_ERROR.getMsg());
		} 
    	return userRealDTO;
    }
}
