package com.dl.member.service;
import com.dl.member.model.DlCashCoupon;
import com.dl.member.dao.DlCashCouponMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlCashCouponService extends AbstractService<DlCashCoupon> {
    @Resource
    private DlCashCouponMapper dlCashCouponMapper;

}
