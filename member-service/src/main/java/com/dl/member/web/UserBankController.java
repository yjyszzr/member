package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.UserBankDTO;
import com.dl.member.param.BankCardParam;
import com.dl.member.service.UserBankService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

/**
* Created by zhangzirong on 2018/03/12.
*/
@RestController
@RequestMapping("/user/bank")
public class UserBankController {
    @Resource
    private UserBankService userBankService;
    
    /**
     * 添加银行卡
     * @param userBankParam
     * @return
     */
    @ApiOperation(value = "添加银行卡", notes = "添加银行卡")
    @PostMapping("/addBankCard")
    public BaseResult<UserBankDTO> addBankCard(@RequestBody BankCardParam bankCardParam){
    	return userBankService.addBankCard(bankCardParam.getBankCardNo());
    }
    
    /**
     * 查询银行卡信息
     * @return
     */
    @ApiOperation(value = "查询银行卡信息", notes = "查询银行卡信息")
    @PostMapping("/userBankList")
    public BaseResult<List<UserBankDTO>> queryUserBankList(){
    	return userBankService.queryUserBankList();
    }
}
