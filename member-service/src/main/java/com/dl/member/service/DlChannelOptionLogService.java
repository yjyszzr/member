package com.dl.member.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.DlChannelOptionLogMapper;
import com.dl.member.dto.IncomeDetailsDTO;
import com.dl.member.dto.PromotionIncomeDTO;
import com.dl.member.model.DlChannelOptionLog;

@Service
@Transactional
public class DlChannelOptionLogService extends AbstractService<DlChannelOptionLog> {
	@Resource
	private DlChannelOptionLogMapper dlChannelOptionLogMapper;

	public List<PromotionIncomeDTO> getPromotionIncomeList(Integer channelDistributorId) {
		return dlChannelOptionLogMapper.getPromotionIncomeList(channelDistributorId);
	}

	public List<IncomeDetailsDTO> getChannelConsumerList(String addTime, Integer userId) {
		return dlChannelOptionLogMapper.getChannelConsumerList(addTime, userId);
	}

}
