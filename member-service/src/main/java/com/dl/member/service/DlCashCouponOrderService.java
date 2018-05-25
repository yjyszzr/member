package com.dl.member.service;
import com.dl.member.model.DlCashCouponOrder;
import com.dl.member.dao.DlCashCouponOrderMapper;
import com.dl.base.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DlCashCouponOrderService extends AbstractService<DlCashCouponOrder> {
    @Resource
    private DlCashCouponOrderMapper dlCashCouponOrderMapper;

}
