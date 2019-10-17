package com.dl.member.dao;

import java.util.List;

import com.dl.base.mapper.Mapper;
import com.dl.member.model.DonationRechargeCard;

public interface DonationRechargeCardMapper extends Mapper<DonationRechargeCard> {
	List<DonationRechargeCard> queryRechargeCardList();
}