package com.dl.member.model;

import java.time.Instant;
import java.util.Date;

import lombok.Data;
/**
 * 用户调用第三方接口的日志
 * @author 007
 *
 */
@Data
public class MemberThirdApiLog {

	//接口名称
	 private String apiName;
	 //接口类型
	 private Integer apiType;
	 //接口描述
	 private String apiDesc;
	 //操作时间
	 private Date optionTime;
	 //接口参数
	 private String apiParam;
	 //接口返回
	 private String apiResult;
	 
	 public  MemberThirdApiLog(){}
	 
	 public  MemberThirdApiLog(String apiName, Integer apiType, String apiParam, String apiResult){
		 this.apiName = apiName;
		 this.apiType = apiType;
		 this.apiParam = apiParam;
		 this.apiResult = apiResult;
		 this.optionTime = new Date();
	 }
}
