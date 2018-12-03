package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.model.DlDeviceActionControl;
import com.dl.member.param.DlDeviceActionControlParam;
import com.dl.member.param.MacParam;
import com.dl.member.service.DlDeviceActionControlService;
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
public class DlDeviceActionControlController {
    @Resource
    private DlDeviceActionControlService dlDeviceActionControlService;

    @PostMapping("/add")
    public BaseResult<String> add(@RequestBody DlDeviceActionControlParam param) {
        DlDeviceActionControl dctrl = new DlDeviceActionControl();
        try {
            BeanUtils.copyProperties(dctrl,param);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        dlDeviceActionControlService.save(dctrl);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("queryDeviceByIMEI")
    public BaseResult<DlDeviceActionControlDTO> queryDeviceByIMEI(@RequestBody MacParam macParam){
        DlDeviceActionControlDTO deviceCtrlDto = new DlDeviceActionControlDTO();
        DlDeviceActionControl deviveCtrl = dlDeviceActionControlService.queryDeviceByIMEI(macParam.getMac());
        if(deviveCtrl != null){
            try {
                BeanUtils.copyProperties(deviceCtrlDto,deviveCtrl);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return ResultGenerator.genSuccessResult("success",deviceCtrlDto);
    }

}
