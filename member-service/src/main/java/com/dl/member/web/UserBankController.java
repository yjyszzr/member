package com.dl.member.web;
import com.alibaba.fastjson.JSONObject;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.BankDTO;
import com.dl.member.dto.UserBankDTO;
import com.dl.member.dto.WithDrawShowDTO;
import com.dl.member.model.UserBank;
import com.dl.member.param.BankCardParam;
import com.dl.member.param.BankCardSaveParam;
import com.dl.member.param.DeleteBankCardParam;
import com.dl.member.param.IDParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.UserBankParam;
import com.dl.member.param.UserBankPurposeQueryParam;
import com.dl.member.param.UserBankQueryParam;
import com.dl.member.service.UserBankService;
import com.gexin.fastjson.JSON;
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
    	return userBankService.queryUserBankList(0);
    }
    
    
	  /**
	  * 根据userID和银行卡号查询银行卡信息
	  *
	  * @return
	  */
     @ApiOperation(value = "根据userID和银行卡号查询银行卡信息", notes = "根据userID和银行卡号查询银行卡信息")
	 @PostMapping("/queryUserBankByCondition")    
	 public BaseResult<UserBankDTO> queryUserBankByCondition(@RequestBody UserBankQueryParam userBankQueryParam){
		 return userBankService.queryUserBankByCondition(userBankQueryParam);
	 }
    
    /**
     * 删除银行卡
     * @return
     */
    @ApiOperation(value = "删除银行卡", notes = "删除银行卡")
    @PostMapping("/deleteUserBank")
    public BaseResult<UserBankDTO> deleteUserBank(@RequestBody DeleteBankCardParam deleteBankCardParam){
    	return userBankService.deleteUserBank(deleteBankCardParam,0);
    }
    
    /**
     * 设置银行卡为当前默认
     * @return
     */
    @ApiOperation(value = "设置银行卡为当前默认", notes = "设置银行卡为当前默认")
    @PostMapping("/updateUserBankDefault")
    public BaseResult<String> updateUserBankDefault(@RequestBody IDParam iDParam){
    	return userBankService.updateUserBankDefault(iDParam.getId(),0);
    }
    
    /**
     * 提现界面的数据显示：当前可提现余额和当前默认银行卡信息
     * @return
     */
    @ApiOperation(value = "提现界面的数据显示", notes = "提现界面的数据显示")
    @PostMapping("/queryWithDrawShow")
    public BaseResult<WithDrawShowDTO> queryWithDrawShow(@RequestBody StrParam strParam){
    	return userBankService.queryWithDrawShow(0);
    }
    
    
    /**
     * 查询银行卡
     * @return
     */
    @ApiOperation(value = "查询银行卡", notes = "查询银行卡")
    @PostMapping("/queryUserBank")
    public BaseResult<UserBankDTO> queryUserBank(@RequestBody IDParam IDParam){
    	return userBankService.queryUserBank(IDParam.getId(),0);
    }
    
    /**
     * 根据银行卡号码，查询银行信息
     * @return
     */
    @ApiOperation(value = "查询银行卡", notes = "查询银行卡")
    @PostMapping("/queryUserBankType")
    public BaseResult<BankDTO> queryUserBankType(@RequestBody BankCardParam bankCardParam){
    	String bankCardNo = bankCardParam.getBankCardNo();
    	JSONObject jsonObj = userBankService.queryUserBankType(bankCardNo);
    	if(jsonObj == null) {
    		return ResultGenerator.genFailResult("查询失败");
    	}
    	if(jsonObj.containsKey("result")) {
    		JSONObject jsonData = jsonObj.getJSONObject("result");
    		BankDTO bankDTO = JSON.parseObject(jsonData.toString(),BankDTO.class);
        	return ResultGenerator.genSuccessResult("查询成功",bankDTO);
    	}
    	return ResultGenerator.genFailResult("查询失败,result为空");
    }
    
	  /**
	  * 按条件查询银行卡信息
	  *
	  * @return
	  */
	 @PostMapping("/user/bank/queryUserBankByCondition")    
	 BaseResult<UserBankDTO> queryUserBankByCondition(@RequestBody UserBankParam userBankParam){
		 
		 return null;
	 }
    
	 /**
     * 添加银行卡
     * @param userBankParam
     * @return
     */
	@ApiOperation(value = "添加银行卡", notes = "")
	@PostMapping("/saveBankCard")
	public BaseResult<UserBankDTO> saveBankCard(@RequestBody BankCardSaveParam saveParam){
		UserBankDTO userBankDTO = new UserBankDTO();
		userBankDTO.setUserBankId(saveParam.getUserBankId());
		userBankDTO.setRealName(saveParam.getRealName());
		userBankDTO.setCardNo(saveParam.getCardNo());
		userBankDTO.setStatus(saveParam.getStatus());
		userBankDTO.setBankLogo(saveParam.getBankLogo());
		userBankDTO.setBankName(saveParam.getBankName());
		userBankDTO.setCardType(saveParam.getCardType());
		userBankDTO.setLastCardNo4(saveParam.getLastCardNo4());
		userBankDTO.setAbbreviation(saveParam.getAbbreviation());
		userBankDTO.setType(saveParam.getType());
		userBankDTO.setPurpose(saveParam.getPurpose());
		userBankService.saveUserBank(userBankDTO);
		return ResultGenerator.genSuccessResult("succ");
	}

	/**
    private String userBankId;
    private String realName;
    private String cardNo;
    private String status;
    private String bankLogo;
    private String bankName;
    private String cardType;
    private String lastCardNo4;
    private String abbreviation;
	private Integer type;
	private Integer purpose;
	
	 * @param queryParam
	 * @return
	 */
	@ApiOperation(value = "查询银行卡", notes = "")
	@PostMapping("/queryBankByPurpose")
	public BaseResult<UserBankDTO> queryBankByPurpose(@RequestBody UserBankPurposeQueryParam queryParam){
		int userId = queryParam.getUserId();
		String bankCardNo = queryParam.getBankCardCode();
		int purpose = queryParam.getPurpose();
		List<UserBank> mList = userBankService.queryBankByPurpose(userId,bankCardNo,purpose);
		if(mList != null && mList.size() > 0) {
			UserBank userBank = mList.get(0);
			UserBankDTO userBankDTO = new UserBankDTO();
			userBankDTO.setUserBankId(userBank.getId()+"");
			userBankDTO.setRealName(userBank.getRealName());
			userBankDTO.setCardNo(userBank.getCardNo());
			userBankDTO.setStatus(userBank.getStatus());
			userBankDTO.setBankLogo(userBank.getBankLogo());
			userBankDTO.setBankName(userBank.getBankName());
			userBankDTO.setCardType(userBank.getType()+"");
			userBankDTO.setAbbreviation(userBank.getAbbreviation());
			userBankDTO.setPurpose(userBank.getPurpose());
			return ResultGenerator.genSuccessResult("succ",userBankDTO);
		}
		return ResultGenerator.genFailResult("empty");
	}
}
