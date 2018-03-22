package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.member.dto.UserBankDTO;
import com.dl.member.dto.WithDrawShowDTO;
import com.dl.member.param.BankCardParam;
import com.dl.member.param.DeleteBankCardParam;
import com.dl.member.param.IDParam;
import com.dl.member.param.StrParam;
import com.dl.member.service.UserBankService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

import java.util.LinkedList;
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
     * 查询银行卡列表
     * @return
     */
    @ApiOperation(value = "查询银行卡列表", notes = "查询银行卡列表")
    @PostMapping("/queryUserBankList")
    public BaseResult<LinkedList<UserBankDTO>> queryUserBankList(@RequestBody StrParam strParam){
    	return userBankService.queryUserBankList();
    }
    
    
    /**
     * 删除银行卡
     * @return
     */
    @ApiOperation(value = "删除银行卡", notes = "删除银行卡")
    @PostMapping("/deleteUserBank")
    public BaseResult<UserBankDTO> deleteUserBank(@RequestBody DeleteBankCardParam deleteBankCardParam){
    	return userBankService.deleteUserBank(deleteBankCardParam);
    }
    
    /**
     * 设置银行卡为当前默认
     * @return
     */
    @ApiOperation(value = "设置银行卡为当前默认", notes = "设置银行卡为当前默认")
    @PostMapping("/updateUserBankDefault")
    public BaseResult<String> updateUserBankDefault(@RequestBody IDParam iDParam){
    	return userBankService.updateUserBankDefault(iDParam.getId());
    }
    
    /**
     * 提现界面的数据显示：当前可提现余额和当前默认银行卡信息
     * @return
     */
    @ApiOperation(value = "提现界面的数据显示", notes = "提现界面的数据显示")
    @PostMapping("/queryWithDrawShow")
    public BaseResult<WithDrawShowDTO> queryWithDrawShow(@RequestBody StrParam strParam){
    	return userBankService.queryWithDrawShow();
    }
    
    
    /**
     * 查询银行卡
     * @return
     */
    @ApiOperation(value = "查询银行卡", notes = "查询银行卡")
    @PostMapping("/queryUserBank")
    public BaseResult<UserBankDTO> queryUserBank(@RequestBody IDParam IDParam){
    	return userBankService.queryUserBank(IDParam.getId());
    }
    
}
