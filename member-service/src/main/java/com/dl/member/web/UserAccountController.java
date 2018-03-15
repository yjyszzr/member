package com.dl.member.web;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.model.UserAccount;
import com.dl.member.service.UserAccountService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2018/03/15.
*/
@RestController
@RequestMapping("/user/account")
public class UserAccountController {
    @Resource
    private UserAccountService userAccountService;

    @PostMapping("/add")
    public BaseResult add(UserAccount userAccount) {
        userAccountService.save(userAccount);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        userAccountService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(UserAccount userAccount) {
        userAccountService.update(userAccount);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public BaseResult detail(@RequestParam Integer id) {
        UserAccount userAccount = userAccountService.findById(id);
        return ResultGenerator.genSuccessResult(null,userAccount);
    }

    @PostMapping("/list")
    public BaseResult list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<UserAccount> list = userAccountService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}
