package com.dl.member.web;
import com.alibaba.druid.util.StringUtils;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.UserRealDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.param.RealNameAuthParam;
import com.dl.member.param.StrParam;
import com.dl.member.service.UserRealService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

/**
* Created by zhangzirong on 2018/03/12.
*/
@RestController
@RequestMapping("/user/real")
public class UserRealController {
	private final static Logger logger = LoggerFactory.getLogger(UserRealController.class);
	
    @Resource
    private UserRealService userRealService;

    /**
     * 实名认证
     * @param realNameAuthParam
     * @return
     */
    @ApiOperation(value = "实名认证", notes = "实名认证")
    @PostMapping("/realNameAuth")
    public BaseResult<String> realNameAuth(@RequestBody RealNameAuthParam realNameAuthParam){
    	String idCardNo = realNameAuthParam.getIDCode();
    	if(StringUtils.isEmpty(idCardNo)) {
    		return ResultGenerator.genResult(MemberEnums.REAL_IDCARDNO_EMPTY.getcode(),MemberEnums.REAL_IDCARDNO_EMPTY.getMsg());
    	}
    	if(idCardNo.length() < 15) {
    		return ResultGenerator.genResult(MemberEnums.REAL_IDCARDNO_NOTLEGAL.getcode(),MemberEnums.REAL_IDCARDNO_NOTLEGAL.getMsg());
    	}
    	int age = IdNOToAge(idCardNo);
    	logger.info("[realNameAuth]" + " age:" + age);
    	if(age <= 18) {	//19岁才可以进行实名认证流程
    		return ResultGenerator.genResult(MemberEnums.REAL_IDCARDNO_NOT18.getcode(),MemberEnums.REAL_IDCARDNO_NOT18.getMsg());
    	}
    	return userRealService.realNameAuth(realNameAuthParam.getRealName(), realNameAuthParam.getIDCode());
    }
    
    //根据身份证号输出年龄
    public int IdNOToAge(String IdNO){
     int leh = IdNO.length();
     String dates="";
     if (leh == 18) {
     	dates = IdNO.substring(6, 10);
     	SimpleDateFormat df = new SimpleDateFormat("yyyy");
         String year = df.format(new Date());
         logger.info("[IdNOToAge]" +"year:" + year);
         int u = Integer.parseInt(year)-Integer.parseInt(dates);
         return u;
     }else {
     	return getAgeByIdCard15(IdNO);
     }
    }
    
    
    /**  
     * 15位身份证  
     * 获取 年龄  
     * @param CardCode  
     * @return  
     */  
    private static int getAgeByIdCard15(String CardCode){  
        //身份证上的年月日  
        String idyear = "19" + CardCode.substring(6, 8);  
        String idyue = CardCode.substring(8, 10);  
        String idday = CardCode.substring(10, 12);  
        String idyr = idyue + idday + "";  
  
        //当前年月日  
        String year = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).substring(0, 4);// 当前年份  
        String yue = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).substring(5, 7);// 月份  
        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).substring(8, 10);  
        String yr = yue + day + "";  
  
        int age = 0;  
        if (Integer.parseInt(idyr) <= Integer.parseInt(yr)) { // 表示生日已过  
            age = Integer.parseInt(year) - Integer.parseInt(idyear) + 1;  
        } else {// 生日未过  
            age = Integer.parseInt(year) - Integer.parseInt(idyear);  
        }  
        return age;  
    }  
    
    
    /**
     * 查询实名认证信息
     */
    @ApiOperation(value = "查询实名认证信息", notes = "查询实名认证信息")
    @PostMapping("/userRealInfo")
    public BaseResult<UserRealDTO> queryUserReal(@RequestBody StrParam strParam){
    	UserRealDTO userRealDTO = userRealService.queryUserReal();
    	return ResultGenerator.genSuccessResult("查询实名认证信息成功", userRealDTO);
    }
}
