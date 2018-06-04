package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.dto.RechargeDataActivityDTO;
import com.dl.member.param.StrParam;
import com.dl.member.service.DLActivityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
* Created by CodeGenerator on 2018/06/01.
*/
@RestController
@RequestMapping("/activity")
public class DLActivityController {
    @Resource
    private DLActivityService dLActivityService;
    
    @PostMapping("/queryValidRechargeActivity")
    public BaseResult<RechargeDataActivityDTO> list(@RequestBody StrParam strparam) {
        Integer count = dLActivityService.queryValidActivity();
        RechargeDataActivityDTO rechargeDataActivityDTO = new RechargeDataActivityDTO();
        if(count > 0 ) {
        	rechargeDataActivityDTO.setIsHaveRechargeAct(1);
        }else {
        	rechargeDataActivityDTO.setIsHaveRechargeAct(0);
        }
        return ResultGenerator.genSuccessResult(null,rechargeDataActivityDTO);
    }
    
}
