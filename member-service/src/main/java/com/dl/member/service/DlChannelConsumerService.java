package com.dl.member.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Condition;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.DlChannelConsumerMapper;
import com.dl.member.model.DlChannelConsumer;

@Service
@Transactional
public class DlChannelConsumerService extends AbstractService<DlChannelConsumer> {
	@Resource
	private DlChannelConsumerMapper dlChannelConsumerMapper;

	public List<DlChannelConsumer> selectByChannelDistributorId(Integer channelDistributorId) {
		Condition condition = new Condition(DlChannelConsumer.class);
		condition.createCriteria().andCondition("channel_distributor_id = ", channelDistributorId);
		return dlChannelConsumerMapper.selectByCondition(condition);
	}
}
