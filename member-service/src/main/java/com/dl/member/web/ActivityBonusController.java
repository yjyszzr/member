package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.model.ActivityBonus;
import com.dl.member.service.ActivityBonusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/03/16.
*/
@RestController
@RequestMapping("/activity/bonus")
public class ActivityBonusController {
    @Resource
    private ActivityBonusService activityBonusService;

    @PostMapping("/add")
    public BaseResult add(ActivityBonus activityBonus) {
        activityBonusService.save(activityBonus);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        activityBonusService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(ActivityBonus activityBonus) {
        activityBonusService.update(activityBonus);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        ActivityBonus activityBonus = activityBonusService.findById(id);
        return ResultGenerator.genSuccessResult(null,activityBonus);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ActivityBonus> list = activityBonusService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}
