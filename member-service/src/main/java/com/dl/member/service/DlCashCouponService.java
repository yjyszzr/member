package com.dl.member.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.result.BaseResult;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.member.dao.DlCashCouponMapper;
import com.dl.member.model.DlCashCoupon;
import com.dl.member.model.DlCashCouponUser;
import com.dl.member.model.User;

@Service
@Transactional
public class DlCashCouponService extends AbstractService<DlCashCoupon> {
	@Resource
	private DlCashCouponMapper dlCashCouponMapper;
	@Resource
	private DlCashCouponOrderService dlCashCouponOrderService;
	@Resource
	private DlCashCouponUserService dlCashCouponUserService;

	public BaseResult<String> saveForCashCoupon(DlCashCoupon cashCoupon, User user) {
		DlCashCouponUser dlCashCouponUser = new DlCashCouponUser();
		dlCashCouponUser.setUserId(user.getUserId());
		dlCashCouponUser.setCashCouponId(cashCoupon.getCashCouponId());
		dlCashCouponUser.setCashCouponDenomination(cashCoupon.getCashCouponDenomination());
		dlCashCouponUser.setCashCouponPrice(cashCoupon.getCashCouponPrice());
		dlCashCouponUser.setId(0);
		dlCashCouponUser.setReceiveTime(DateUtil.getCurrentTimeLong());
		// 将代金券放到自己名下
		dlCashCouponUserService.save(dlCashCouponUser);
		// 创建相应的订单以及订单流水日志
		return dlCashCouponOrderService.saveForCashCouponOrder(cashCoupon, user);
	}
}
