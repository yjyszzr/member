package com.dl.member.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.enums.SNBusinessCodeEnum;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SNGenerator;
import com.dl.member.dao.DlCashCouponOrderMapper;
import com.dl.member.model.DlCashCoupon;
import com.dl.member.model.DlCashCouponOrder;
import com.dl.member.model.User;
import com.dl.member.model.UserAccount;

@Service
@Transactional
public class DlCashCouponOrderService extends AbstractService<DlCashCouponOrder> {
	@Resource
	private DlCashCouponOrderMapper dlCashCouponOrderMapper;
	@Resource
	private UserAccountService userAccountService;

	public BaseResult<String> saveForCashCouponOrder(DlCashCoupon cashCoupon, User user) {
		// compareTo()比较的是二者的值是否相等，不考虑精确度，返回值0表示相等，-1表示小于，1表示大于。
		if (user.getUserMoney().compareTo(cashCoupon.getCashCouponPrice()) > 0) {
			DlCashCouponOrder dlCashCouponOrder = new DlCashCouponOrder();
			dlCashCouponOrder.setIsDelete(0);
			dlCashCouponOrder.setAddTime(DateUtil.getCurrentTimeLong());
			dlCashCouponOrder.setMoneyPaid(cashCoupon.getCashCouponPrice());
			dlCashCouponOrder.setOrderId(0);
			dlCashCouponOrder.setOrderSn(SNGenerator.nextSN(SNBusinessCodeEnum.ORDER_SN.getCode()));
			dlCashCouponOrder.setUserId(user.getUserId());
			dlCashCouponOrder.setPayCode("");
			dlCashCouponOrder.setPayName("余额支付");
			// 代金券生成订单并且保存
			this.save(dlCashCouponOrder);
			User userA = new User();
			userA.setUserId(user.getUserId());
			userA.setUserMoney(user.getUserMoney().subtract(cashCoupon.getCashCouponPrice()));
			// 更改自己的可提现余额
			userAccountService.updateUserMoneyForCashCoupon(userA);
			BigDecimal curBalance = user.getUserMoney().add(user.getUserMoneyLimit()).subtract(cashCoupon.getCashCouponPrice());
			UserAccount userAccount = new UserAccount();
			String accountSn = SNGenerator.nextSN(SNBusinessCodeEnum.ACCOUNT_SN.getCode());
			userAccount.setAccountSn(accountSn);
			userAccount.setAddTime(DateUtil.getCurrentTimeLong());
			userAccount.setLastTime(DateUtil.getCurrentTimeLong());
			BigDecimal bigd = new BigDecimal(0);
			userAccount.setAmount(bigd.subtract(cashCoupon.getCashCouponPrice()));
			userAccount.setCurBalance(curBalance);
			if (StringUtils.isNotEmpty(dlCashCouponOrder.getOrderSn())) {
				userAccount.setOrderSn(dlCashCouponOrder.getOrderSn());
			}
			userAccount.setPayId("");
			userAccount.setPaymentName("2");
			userAccount.setThirdPartName("");
			userAccount.setUserSurplus(BigDecimal.ZERO);
			userAccount.setUserSurplusLimit(BigDecimal.ZERO);
			userAccount.setThirdPartPaid(BigDecimal.ZERO);
			userAccount.setProcessType(7);// 类型为7:购券
			userAccount.setUserId(user.getUserId());
			userAccount.setStatus(1);
			userAccount.setNote("购买代金券");
			userAccount.setParentSn("");
			userAccount.setBonusPrice(BigDecimal.ZERO);
			// 记录订单流水
			userAccountService.insertUserAccount(userAccount);
			return ResultGenerator.genSuccessResult(null, "支付成功");
		}
		return ResultGenerator.genSuccessResult(null, "您的余额不足");
	}
}
