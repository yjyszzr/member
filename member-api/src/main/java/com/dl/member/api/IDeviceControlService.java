package com.dl.member.api;

import com.dl.base.result.BaseResult;
import com.dl.member.dto.DlDeviceActionControlDTO;
import com.dl.member.param.DlDeviceActionControlParam;
import com.dl.member.param.MacParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 设备接口
 * @author zhangzirong
 *
 */
@FeignClient(value="member-service")
public interface IDeviceControlService {

	@RequestMapping(path="/dl/deviceActionControl/add", method=RequestMethod.POST)
	public BaseResult<String> add(@RequestBody DlDeviceActionControlParam param);

	@RequestMapping(path="/dl/deviceActionControl/queryDeviceByIMEI", method=RequestMethod.POST)
	public BaseResult<DlDeviceActionControlDTO> queryDeviceByIMEI(@RequestBody MacParam imeiParam);

}
