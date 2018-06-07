package com.dl.member.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.DlChannelMapper;
import com.dl.member.model.DlChannel;
import com.dl.member.param.DlChannelParam;

@Service
@Transactional
public class DlChannelService extends AbstractService<DlChannel> {
	@Resource
	private DlChannelMapper dlChannelMapper;

	public List<DlChannel> findAllOrderByLetter(DlChannelParam param) {
		return dlChannelMapper.findAllOrderByLetter(param.getChannelName());
	}
}
