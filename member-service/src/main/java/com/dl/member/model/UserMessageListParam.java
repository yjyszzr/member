package com.dl.member.model;

import lombok.Data;

/**
 * 
 * @author 007
 *
 */
@Data
public class UserMessageListParam {

	//用户id
	private Integer receiver;
	//消息类型
	private Integer msgType;
}
