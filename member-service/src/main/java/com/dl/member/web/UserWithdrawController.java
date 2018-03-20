package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.model.UserWithdraw;
import com.dl.member.service.UserWithdrawService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/03/20.
*/
@RestController
@RequestMapping("/user/withdraw")
public class UserWithdrawController {
    @Resource
    private UserWithdrawService userWithdrawService;

    @PostMapping("/add")
    public BaseResult add(UserWithdraw userWithdraw) {
        userWithdrawService.save(userWithdraw);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        userWithdrawService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(UserWithdraw userWithdraw) {
        userWithdrawService.update(userWithdraw);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        UserWithdraw userWithdraw = userWithdrawService.findById(id);
        return ResultGenerator.genSuccessResult(null,userWithdraw);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<UserWithdraw> list = userWithdrawService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}
