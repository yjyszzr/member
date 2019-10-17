package com.dl.member.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.dl.member.dao.IDFACallBackMapper;
import com.dl.member.dao.IDFAMapper;
import com.dl.member.dao.SysConfigMapper;
import com.dl.member.dto.SysConfigDTO;
import com.dl.member.model.IDFA;
import com.dl.member.model.IDFACallBack;
import com.dl.member.param.IDFACallBackParam;
import com.dl.member.param.IDFACheckParam;
import com.dl.member.param.IDFAClickParam;
import com.dl.member.util.DateUtilNew;

@Service
public class IDFAService {
	@Resource
	private IDFAMapper idfaMapper;
	@Resource
	private IDFACallBackMapper idfaCallBackMapper;
	@Resource
    private SysConfigService sysConfigService;
	/**
	 * 排重接口
	 * 
	 * @param idfaCheckParam
	 * @return
	 */
	public Map<String, String> checkDataByIdfa(IDFACheckParam idfaCheckParam) {
		Map<String, String> idfaMap = new HashMap();
		IDFA idfa = idfaMapper.queryIDFAByIdfa(idfaCheckParam.getIdfa());
		if (idfa != null) {
			idfaMap.put(idfaCheckParam.getIdfa(), "1");
		} else {
			idfaMap.put(idfaCheckParam.getIdfa(), "0");
		}
		return idfaMap;
	}

	/**
	 * 点击接口
	 * 
	 * @param idfaClickParam
	 */
	public Map<String, String> saveOrUpdateClickData(IDFAClickParam idfaClickParam) {
		Map<String, String> idfaMap = new HashMap();
		IDFA iDFA = idfaMapper.queryIDFAByIdfa(idfaClickParam.getIdfa());
		if (iDFA == null) {
			try {
				IDFACallBack idfaCB = new IDFACallBack();
				IDFACallBack idfaB = idfaCallBackMapper.queryByIdfa(idfaClickParam.getIdfa());
				idfaCB.setAppid(idfaClickParam.getAppid());
				idfaCB.setIdfa(idfaClickParam.getIdfa());
				idfaCB.setCallback(idfaClickParam.getCallback());
				idfaCB.setSource(idfaClickParam.getSource());
				if (idfaB == null) {
					idfaCB.setCreat_time((long) new DateUtilNew().getCurrentTimeLong());
					idfaCallBackMapper.save(idfaCB);
				} else {
					idfaCallBackMapper.updateByIdfa(idfaCB);
				}
				idfaMap.put("code", "0");
				idfaMap.put("msg", "success");
				return idfaMap;
			} catch (Exception e) {
				idfaMap.put("code", "1");
				idfaMap.put("msg", e.getMessage());
				return idfaMap;
			}
		}
		idfaMap.put("code", "2");
		idfaMap.put("msg", "false");
		return idfaMap;
	}

	/**
	 * 回调接口
	 */
	public void callBackIdfa(IDFACallBackParam idfaParam) {
		int from = 0;
		int backStatus = 1;
		IDFA iDFA = idfaMapper.queryIDFAByIdfa(idfaParam.getIdfa());
		IDFACallBack idfaB = idfaCallBackMapper.queryByIdfa(idfaParam.getIdfa());
		if (iDFA == null) {
			IDFA idfa = new IDFA();
			idfa.setUser_id(idfaParam.getUserid());
			idfa.setIdfa(idfaParam.getIdfa());
			 if (idfaB != null ) { from = 1; }//判断是否广告商推广
			idfa.setIdfa_from(from);
			idfa.setCreat_time((long) new DateUtilNew().getCurrentTimeLong());
			idfaMapper.save(idfa);
			// 回调
			if (idfaB != null && idfaB.getBack_status() == null) {
				int id = idfaB.getId()%10;
				SysConfigDTO sysConfig = sysConfigService.querySysConfig(105);
				String idSTR = sysConfig.getValue()+"";
				if(idSTR.contains(id+"") && id!=0) {
					backStatus = 3;
				}else {
					try {
						String callBack = idfaB.getCallback();
						URL url = new URL(callBack);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
							backStatus = 0;
						}
					} catch (IOException e) {
						backStatus = 2;
					}
				}
				idfaB.setBack_status(backStatus);
				idfaCallBackMapper.updateBackStatusByIdfa(idfaB);
			}
		}else {
			if(iDFA.getUser_id()==-1) {
				iDFA.setUser_id(idfaParam.getUserid());
				idfaMapper.updateUserIdByIdfa(iDFA);
			}
		}
	}
}
