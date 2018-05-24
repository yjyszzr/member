package com.dl.member.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.DlCashCouponUserMapper;
import com.dl.member.model.DlCashCouponUser;

@Service
@Transactional
public class DlCashCouponUserService extends AbstractService<DlCashCouponUser> {
	@Resource
	private DlCashCouponUserMapper dlCashCouponUserMapper;

	public List<DlCashCouponUser> findByUserId(Integer userId) {
		Condition condition = new Condition(DlCashCouponUser.class);
		condition.createCriteria().andCondition("user_id = ", userId);
		return dlCashCouponUserMapper.selectByCondition(condition);
	}

}
