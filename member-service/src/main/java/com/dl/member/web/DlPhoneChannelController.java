package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.model.DlPhoneChannel;
import com.dl.member.service.DlPhoneChannelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/08/06.
*/
@RestController
@RequestMapping("/dl/phone/channel")
public class DlPhoneChannelController {
    @Resource
    private DlPhoneChannelService dlPhoneChannelService;

    @PostMapping("/add")
    public BaseResult add(DlPhoneChannel dlPhoneChannel) {
        dlPhoneChannelService.save(dlPhoneChannel);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlPhoneChannelService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlPhoneChannel dlPhoneChannel) {
        dlPhoneChannelService.update(dlPhoneChannel);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        DlPhoneChannel dlPhoneChannel = dlPhoneChannelService.findById(id);
        return ResultGenerator.genSuccessResult(null,dlPhoneChannel);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<DlPhoneChannel> list = dlPhoneChannelService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}
