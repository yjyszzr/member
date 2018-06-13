package com.dl.member.schedul;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.dl.base.util.DateUtil;
import com.dl.member.dao.UserBonusMapper;
import com.dl.member.service.UserBonusService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class MemberSchedul {

	@Resource
	private UserBonusService userBonusService;

	@Resource
	private UserBonusMapper userBonusMapper;

	/**
	 * 更新过期的红包
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void updateBonusExpire() {
		log.info("更新过期的红包定时任务开始");
		Integer now = DateUtil.getCurrentTimeLong();
		List<Integer> userBonusIdList = userBonusMapper.queryUserBonusIdsExpire(now);
		if (CollectionUtils.isEmpty(userBonusIdList)) {
			log.info("####################没有过期的红包，定时任务结束");
			return;
		}

		int rst = userBonusService.updateBonusExpire(userBonusIdList);
		log.info("本次更新过期的红包" + rst + "个");
		log.info("更新过期的红包的定时任务结束");
	}

}
