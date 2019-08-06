package com.dl.member.web;
import com.dl.activity.api.IActService;
import com.dl.activity.dto.ActivityDTO;
import com.dl.activity.param.ActTypeParam;
import com.dl.activity.param.ActUserInitParam;
import com.dl.base.model.UserDeviceInfo;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.DateUtilNew;
import com.dl.base.util.SessionUtil;
import com.dl.member.core.ProjectConstant;
import com.dl.member.dto.UserLoginDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.param.IDFACallBackParam;
import com.dl.member.param.UserRegisterParam;
import com.dl.member.service.*;
import com.dl.member.util.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* Created by CodeGenerator on 2018/03/08.
*/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserRegisterController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
	
    @Resource
    private UserRegisterService userRegisterService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private UserLoginService userLoginService;
    
    @Resource
    private UserBonusService userBonusService;
    
    @Resource
    private DLActivityService dLActivityService;

    @Resource
   	private IDFAService iDFAService;

    @Resource
    private IActService iActService;
    /**
     * 新用户注册:
     * @param userRegisterParam
     * @param request
     * @return
     */
    @ApiOperation(value = "新用户注册", notes = "新用户注册")
    @PostMapping("/register")
    public BaseResult<UserLoginDTO> register(@RequestBody UserRegisterParam userRegisterParam, HttpServletRequest request) {
        String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.SMS_TYPE_REGISTER + "_" + userRegisterParam.getMobile());
        if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(userRegisterParam.getSmsCode())) {
            return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
        }
        String passWord = userRegisterParam.getPassWord();
        if(passWord.equals("-1")) {
        	userRegisterParam.setPassWord("");
        } else if(!passWord.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$")) {
    		return ResultGenerator.genResult(MemberEnums.PASS_FORMAT_ERROR.getcode(), MemberEnums.PASS_FORMAT_ERROR.getMsg());
    	} 	
        
    	BaseResult<Integer> regRst = userRegisterService.registerUser(userRegisterParam, request);
    	if(regRst.getCode() != 0) {
    		return ResultGenerator.genResult(regRst.getCode(),regRst.getMsg());
    	}
    	Integer userId = regRst.getData();
    	
    	TokenUtil.genToken(userId, Integer.valueOf(userRegisterParam.getLoginSource()));
    	UserLoginDTO userLoginDTO = userLoginService.queryUserLoginDTOByMobile(userRegisterParam.getMobile(), userRegisterParam.getLoginSource());
		
    	stringRedisTemplate.delete(ProjectConstant.SMS_PREFIX + ProjectConstant.SMS_TYPE_REGISTER + "_" + userRegisterParam.getMobile());
    	
    	UserDeviceInfo userDevice = SessionUtil.getUserDevice();
    	if(userDevice.getPlat().equals("iphone")) {
    		//idfa 回调、存储  （lidelin）
    		IDFACallBackParam idfaParam = new IDFACallBackParam();
    		idfaParam.setUserid(userId);
    		idfaParam.setIdfa(userDevice.getIDFA());
    		iDFAService.callBackIdfa(idfaParam);
    	}
    	return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
    }

    /**
     * 新用户注册V2,含有邀请码:
     * @param userRegisterParam
     * @param request
     * @return
     */
    @ApiOperation(value = "新用户注册V2,含有邀请码", notes = "新用户注册V2,含有邀请码")
    @PostMapping("/registerV2")
    public BaseResult<UserLoginDTO> registerV2(@RequestBody UserRegisterParam userRegisterParam, HttpServletRequest request) {
//        String cacheSmsCode = stringRedisTemplate.opsForValue().get(ProjectConstant.SMS_PREFIX + ProjectConstant.SMS_TYPE_REGISTER + "_" + userRegisterParam.getMobile());
//        if (StringUtils.isEmpty(cacheSmsCode) || !cacheSmsCode.equals(userRegisterParam.getSmsCode())) {
//            return ResultGenerator.genResult(MemberEnums.SMSCODE_WRONG.getcode(), MemberEnums.SMSCODE_WRONG.getMsg());
//        }
        String passWord = userRegisterParam.getPassWord();
        if(passWord.equals("-1")) {
            userRegisterParam.setPassWord("");
        } else if(!passWord.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$")) {
            return ResultGenerator.genResult(MemberEnums.PASS_FORMAT_ERROR.getcode(), MemberEnums.PASS_FORMAT_ERROR.getMsg());
        }

        BaseResult<Integer> regRst = userRegisterService.registerUser(userRegisterParam, request);
        if(regRst.getCode() != 0) {
            return ResultGenerator.genResult(regRst.getCode(),regRst.getMsg());
        }
        Integer userId = regRst.getData();

        Boolean validAct = this.validTgAct();
        if(validAct){
            ActUserInitParam actUserInitParam = new ActUserInitParam();
            actUserInitParam.setUserId(userId);
            actUserInitParam.setMobile(userRegisterParam.getMobile());
            BaseResult<Integer> actUserRst = iActService.initActUserInfo(actUserInitParam);
            if(actUserRst.isSuccess()){
                userService.updateParentUserId(Integer.valueOf(userRegisterParam.getInvitCode()),userId);
            }
        }

        TokenUtil.genToken(userId, Integer.valueOf(userRegisterParam.getLoginSource()));
        UserLoginDTO userLoginDTO = userLoginService.queryUserLoginDTOByMobile(userRegisterParam.getMobile(), userRegisterParam.getLoginSource());

        stringRedisTemplate.delete(ProjectConstant.SMS_PREFIX + ProjectConstant.SMS_TYPE_REGISTER + "_" + userRegisterParam.getMobile());

        UserDeviceInfo userDevice = SessionUtil.getUserDevice();
        if(userDevice.getPlat().equals("iphone")) {
            //idfa 回调、存储  （lidelin）
            IDFACallBackParam idfaParam = new IDFACallBackParam();
            idfaParam.setUserid(userId);
            idfaParam.setIdfa(userDevice.getIDFA());
            iDFAService.callBackIdfa(idfaParam);
        }
        return ResultGenerator.genSuccessResult("登录成功", userLoginDTO);
    }

    //是否有效推广活动
    public Boolean validTgAct(){
        Boolean valid = false;
        Integer currentTime = DateUtilNew.getCurrentTimeLong();
        Boolean withInDuring = false;

        ActTypeParam actTypeParam2 = new ActTypeParam();
        actTypeParam2.setActType(2);
        ActTypeParam actTypeParam3 = new ActTypeParam();
        actTypeParam2.setActType(3);
        BaseResult<ActivityDTO> tg2Rst = iActService.queryActsByType(actTypeParam2);
        BaseResult<ActivityDTO> tg3Rst = iActService.queryActsByType(actTypeParam3);
        if(tg2Rst.isSuccess() && tg3Rst.isSuccess()){
            ActivityDTO tg2DTO = tg2Rst.getData();
            ActivityDTO tg3DTO = tg3Rst.getData();
            if(tg2DTO != null|| tg3DTO !=null){
                withInDuring = false;
            }
        }
        return withInDuring;
    }
    
}
