package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.DlDeviceActionControl;
import com.dl.member.param.DlDeviceActionControlParam;
import com.dl.member.param.MacParam;
import com.dl.member.service.DlDeviceActionControlService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.dl.member.dto.DlDeviceActionControlDTO;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

/**
* Created by CodeGenerator on 2018/11/30.
*/
@RestController
@RequestMapping("/dl/deviceActionControl")
@Slf4j
public class DlDeviceActionControlController {
    @Resource
    private DlDeviceActionControlService dlDeviceActionControlService;

    @PostMapping("/add")
    public BaseResult<String> add(@RequestBody DlDeviceActionControlParam param) {
        DlDeviceActionControl dctrl = new DlDeviceActionControl();
        try {
            BeanUtils.copyProperties(dctrl,param);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        dlDeviceActionControlService.save(dctrl);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("queryDeviceByIMEI")
    public BaseResult<DlDeviceActionControlDTO> queryDeviceByIMEI(@RequestBody MacParam macParam){
        DlDeviceActionControlDTO deviceCtrlDto = new DlDeviceActionControlDTO();
        DlDeviceActionControl deviveCtrl = dlDeviceActionControlService.queryDeviceByIMEI(macParam.getMac());
        if(deviveCtrl != null && deviveCtrl.getAddTime() != null){
            try {
                BeanUtils.copyProperties(deviceCtrlDto,deviveCtrl);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }else{
            return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(),MemberEnums.DBDATA_IS_NULL.getMsg());
        }

        return ResultGenerator.genSuccessResult("success",deviceCtrlDto);
    }

}
