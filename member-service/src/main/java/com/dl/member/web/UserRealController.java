package com.dl.member.web;
import com.alibaba.druid.util.StringUtils;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.UserRealDTO;
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
    		return ResultGenerator.genResult(-1, "身份证号不能为空");
    	}
    	if(idCardNo.length() < 15) {
    		return ResultGenerator.genResult(-1, "身份证号不合法");
    	}
    	int age = IdNOToAge(idCardNo);
    	logger.info("[realNameAuth]" + " age:" + age);
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
    }else{
        dates = IdNO.substring(6, 8);
        return Integer.parseInt(dates);
    }

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
