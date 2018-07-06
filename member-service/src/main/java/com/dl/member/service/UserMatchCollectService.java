package com.dl.member.service;
import com.dl.member.model.UserMatchCollect;
import com.dl.member.dao.UserMatchCollectMapper;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import javax.annotation.Resource;

@Service
@Transactional
public class UserMatchCollectService extends AbstractService<UserMatchCollect> {
    @Resource
    private UserMatchCollectMapper userMatchCollectMapper;
    
    public List<Integer> queryMyCollectMatchIdList(Integer userId){
    	UserMatchCollect umc = new UserMatchCollect();
    	umc.setUserId(userId);
    	List<Integer> userMatchIdList = userMatchCollectMapper.queryUserMatchCollectListBySelective(umc);
    	return userMatchIdList;
    }
    
    /**
     * 收藏 比赛
     * @param userId
     * @return
     */
    public int saveMyCollectMatch(Integer userId,Integer matchId){
    	UserMatchCollect umc = new UserMatchCollect();
    	umc.setUserId(userId);
    	umc.setMatchId(matchId);
    	umc.setAddTime(DateUtil.getCurrentTimeLong());
    	int rst = userMatchCollectMapper.insertUserCollectMatch(umc);
    	return rst;
    }
    
    /**
     * 查询 收藏 的 比赛
     * @param userId
     * @return
     */
    public List<UserMatchCollect> queryMyCollectMatch(Integer userId,Integer matchId){
    	UserMatchCollect umc = new UserMatchCollect();
    	umc.setUserId(userId);
    	umc.setMatchId(matchId);
    	List<UserMatchCollect> umcList = userMatchCollectMapper.queryUserMatchCollectMapper(userId, matchId);
    	return umcList;
    }

}
