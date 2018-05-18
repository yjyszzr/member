package com.dl.member.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.service.AbstractService;
import com.dl.member.dao.DlChannelDistributorMapper;
import com.dl.member.model.DlChannelDistributor;

@Service
@Transactional(value = "transactionManager2")
public class DlChannelDistributorService extends AbstractService<DlChannelDistributor> {
	@Resource
	private DlChannelDistributorMapper dlChannelDistributorMapper;

	public List<DlChannelDistributor> getAllDlChannelDistributor() {
		return dlChannelDistributorMapper.getAllDlChannelDistributor();
	}
}
