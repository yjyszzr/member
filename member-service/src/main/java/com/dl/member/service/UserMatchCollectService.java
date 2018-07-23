package com.dl.member.service;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.service.AbstractService;
import com.dl.base.util.DateUtil;
import com.dl.base.util.SessionUtil;
import com.dl.member.dao.UserMatchCollectMapper;
import com.dl.member.dto.MatchCollectSomedayCountDTO;
import com.dl.member.enums.MemberEnums;
import com.dl.member.model.UserMatchCollect;
import com.dl.member.param.UserMatchCollectParam;

@Service
@Transactional
public class UserMatchCollectService extends AbstractService<UserMatchCollect> {
    @Resource
    private UserMatchCollectMapper userMatchCollectMapper;
    
    public List<Integer> queryMyCollectMatchIdList(Integer userId,String dateStr){
    	List<Integer> userMatchIdList = userMatchCollectMapper.queryUserMatchCollectListBySelective(userId,dateStr);
    	return userMatchIdList;
    }
    
    /**
     * 收藏 比赛
     * @param userId
     * @return
     */
    public int saveMyCollectMatch(Integer userId,Integer matchId,String dateStr){
    	UserMatchCollect umc = new UserMatchCollect();
    	umc.setUserId(userId);
    	umc.setMatchId(matchId);
    	umc.setAddTime(DateUtil.getTimeSomeDate(DateUtil.strToDate(dateStr)));
    	umc.setIsDelete(0);
    	int rst = userMatchCollectMapper.insertUserCollectMatch(umc);
    	return rst;
    }
    
    /**
     * 查询是否收藏过某场比赛
     * @param userId
     * @return
     */
    public int queryMyCollectMatch(Integer userId,Integer matchId,String dateStr){
    	int rst = userMatchCollectMapper.queryUserMatchCollect(userId, matchId,dateStr);
    	return rst;
    }
    
    /**
     * 取消收藏
     * @param matchId
     * @return
     */
    public BaseResult<MatchCollectSomedayCountDTO> cancleCollect(UserMatchCollectParam userMatchCollectParam) {
	   	Integer userId = SessionUtil.getUserId();
        String strDate = userMatchCollectParam.getDateStr();
        if(StringUtils.isEmpty(strDate)) {
        	strDate = DateUtil.getCurrentDateTime(LocalDateTime.now(), DateUtil.date_sdf);
        }
	   	MatchCollectSomedayCountDTO matchCollectSomedayCountDTO = new MatchCollectSomedayCountDTO();
	   	int rst = userMatchCollectMapper.queryUserMatchCollect(userId, userMatchCollectParam.getMatchId(),strDate);
	   	if(rst <= 0) {
	   		return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(),"用户没有收藏该赛事");
	   	}
	   	
	   	int delRst = userMatchCollectMapper.deleteUserMatchCollect(userId, userMatchCollectParam.getMatchId(),strDate);
	   	Integer nowUserCollect = this.countUserCollectByDate(userId,strDate);
	   	matchCollectSomedayCountDTO.setMatchCollectCount(String.valueOf(nowUserCollect));
	   	return ResultGenerator.genSuccessResult("取消收藏成功",matchCollectSomedayCountDTO);
   }
    
    /**
     * 根据日期查询收藏的比赛数
     * @param userId
     * @return
     */
    public Integer countUserCollectByDate(Integer userId,String dateStr) {
	   	Integer nowUserCollect = userMatchCollectMapper.countUserCollectMatch(userId, dateStr);
	   	return nowUserCollect;
    }
    
}
