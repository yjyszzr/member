package com.dl.member.web;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.member.model.DlCashCoupon;
import com.dl.member.model.DlCashCouponUser;
import com.dl.member.param.DlCashCouponUserParam;
import com.dl.member.service.DlCashCouponService;
import com.dl.member.service.DlCashCouponUserService;

@RestController
@RequestMapping("/dl/cashCoupon")
public class DlCashCouponController {
	@Resource
	private DlCashCouponService dlCashCouponService;
	@Resource
	private DlCashCouponUserService dlCashCouponUserService;

	@ApiOperation(value = "代金券列表", notes = "代金券列表")
	@PostMapping("/cashCouponList")
	public BaseResult<List<DlCashCoupon>> cashCouponList() {
		List<DlCashCoupon> list = dlCashCouponService.findAll();
		return ResultGenerator.genSuccessResult(null, list);
	}

	@ApiOperation(value = "用户代金券列表", notes = "用户代金券列表")
	@PostMapping("/userCashCouponUserList")
	public BaseResult<List<DlCashCouponUser>> cashCouponUserList(DlCashCouponUserParam param) {
		List<DlCashCouponUser> list = dlCashCouponUserService.findByUserId(param.getUserId());
		return ResultGenerator.genSuccessResult(null, list);
	}

}
