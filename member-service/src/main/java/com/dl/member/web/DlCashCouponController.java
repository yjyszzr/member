package com.dl.member.web;

import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.model.DlCashCoupon;
import com.dl.member.model.DlCashCouponUser;
import com.dl.member.model.User;
import com.dl.member.param.DlCashCouponParam;
import com.dl.member.param.DlCashCouponUserParam;
import com.dl.member.param.PaymentPageParam;
import com.dl.member.param.StrParam;
import com.dl.member.service.DlCashCouponService;
import com.dl.member.service.DlCashCouponUserService;
import com.dl.member.service.UserService;

@RestController
@RequestMapping("/dl/cashCoupon")
public class DlCashCouponController {
	@Resource
	private DlCashCouponService dlCashCouponService;
	@Resource
	private UserService userService;

	@Resource
	private DlCashCouponUserService dlCashCouponUserService;

	@ApiOperation(value = "代金券列表", notes = "代金券列表")
	@PostMapping("/cashCouponList")
	public BaseResult<List<DlCashCoupon>> cashCouponList(@RequestBody StrParam strParam) {
		List<DlCashCoupon> list = dlCashCouponService.findAll();
		return ResultGenerator.genSuccessResult(null, list);
	}

	@ApiOperation(value = "用户代金券列表", notes = "用户代金券列表")
	@PostMapping("/userCashCouponUserList")
	public BaseResult<List<DlCashCouponUser>> cashCouponUserList(@RequestBody DlCashCouponUserParam param) {
		List<DlCashCouponUser> list = dlCashCouponUserService.findByUserId(param.getUserId());
		userService.queryUserByUserIdExceptPass();// 做判断
		return ResultGenerator.genSuccessResult(null, list);
	}

	@ApiOperation(value = "去支付页面", notes = "去支付页面")
	@PostMapping("/toPaymentPage")
	public BaseResult<PaymentPageParam> toPaymentPage(@RequestBody DlCashCouponParam param) {
		DlCashCoupon cashCoupon = dlCashCouponService.findById(param.getCashCouponId());
		PaymentPageParam paymentPage = new PaymentPageParam();
		User user = userService.findById(SessionUtil.getUserId());
		dlCashCouponService.saveForCashCoupon(cashCoupon, user);
		if (cashCoupon != null) {
			paymentPage.setCashCouponPrice(cashCoupon.getCashCouponPrice());
		}
		if (user != null) {
			paymentPage.setMyAmount(user.getUserMoney());
		}
		return ResultGenerator.genSuccessResult(null, paymentPage);
	}

	/**
	 * (下单逻辑) 读取商品价格 读取余额 商品保存进订单 减掉余额 记录订单流水 代金券进入该用户名下
	 */
	// 余额的接口
	// 判断是否够用
	// 减掉余额
	// 记录订单流水

	@ApiOperation(value = "生成订单", notes = "生成订单")
	@PostMapping("/toCreateOrder")
	public BaseResult<String> toCreateOrder(@RequestBody DlCashCouponParam param) {
		DlCashCoupon cashCoupon = dlCashCouponService.findById(param.getCashCouponId());
		User user = userService.findById(SessionUtil.getUserId());
		if (user != null) {
			dlCashCouponService.saveForCashCoupon(cashCoupon, user);
			return ResultGenerator.genSuccessResult(null, "支付成功!");
		}
		return ResultGenerator.genSuccessResult(null, "该用户不存在");
	}
}
