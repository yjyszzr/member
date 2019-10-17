package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_message")
public class DlMessage {
    /**
     * 消息ID
     */
    @Id
    @Column(name = "msg_id")
    private Integer msgId;

    /**
     * 发送者
     */
    private Integer sender;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private Integer sendTime;

    /**
     * 标题
     */
    private String title;

    /**
     * 信息类型：0通知1消息
     */
    @Column(name = "msg_type")
    private Integer msgType;

    /**
     * 推送类型
     */
    @Column(name = "push_type")
    private String pushType;

    /**
     * 推送值
     */
    @Column(name = "push_value")
    private String pushValue;

    /**
     * 接收者
     */
    private Integer receiver;

    /**
     * 接受者手机号
     */
    @Column(name = "receiver_mobile")
    private String receiverMobile;

    /**
     * 业务类型
     */
    @Column(name = "object_type")
    private Integer objectType;

    /**
     * 短信是否发送成功
     */
    @Column(name = "is_mobile_success")
    private Integer isMobileSuccess;

    /**
     * 推送是否发送成功
     */
    @Column(name = "is_push_success")
    private Integer isPushSuccess;

    /**
     * 是否读取
     */
    @Column(name = "is_read")
    private Integer isRead;

    /**
     * 内容
     */
    private String content;
    
    @Column(name = "content_desc")
    private String contentDesc;
    
    @Column(name = "content_url")
    private String contentUrl;

    /**
     * 消息附加信息
     */
    @Column(name = "msg_desc")
    private String msgDesc;
    
    @Column(name="msg_url")
    private String msgUrl;

    /**
     * 获取消息ID
     *
     * @return msg_id - 消息ID
     */
    public Integer getMsgId() {
        return msgId;
    }

    /**
     * 设置消息ID
     *
     * @param msgId 消息ID
     */
    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    /**
     * 获取发送者
     *
     * @return sender - 发送者
     */
    public Integer getSender() {
        return sender;
    }

    /**
     * 设置发送者
     *
     * @param sender 发送者
     */
    public void setSender(Integer sender) {
        this.sender = sender;
    }

    /**
     * 获取发送时间
     *
     * @return send_time - 发送时间
     */
    public Integer getSendTime() {
        return sendTime;
    }

    /**
     * 设置发送时间
     *
     * @param sendTime 发送时间
     */
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取信息类型：0通知1消息
     *
     * @return type - 信息类型：0通知1消息
     */
    public Integer getMsgType() {
        return msgType;
    }

    /**
     * 设置信息类型：0通知1消息
     *
     * @param type 信息类型：0通知1消息
     */
    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    /**
     * 获取推送类型
     *
     * @return push_type - 推送类型
     */
    public String getPushType() {
        return pushType;
    }

    /**
     * 设置推送类型
     *
     * @param pushType 推送类型
     */
    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    /**
     * 获取推送值
     *
     * @return push_value - 推送值
     */
    public String getPushValue() {
        return pushValue;
    }

    /**
     * 设置推送值
     *
     * @param pushValue 推送值
     */
    public void setPushValue(String pushValue) {
        this.pushValue = pushValue;
    }

    /**
     * 获取接收者
     *
     * @return receiver - 接收者
     */
    public Integer getReceiver() {
        return receiver;
    }

    /**
     * 设置接收者
     *
     * @param receiver 接收者
     */
    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    /**
     * 获取接受者手机号
     *
     * @return receiver_mobile - 接受者手机号
     */
    public String getReceiverMobile() {
        return receiverMobile;
    }

    /**
     * 设置接受者手机号
     *
     * @param receiverMobile 接受者手机号
     */
    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    /**
     * 获取业务类型
     *
     * @return object_type - 业务类型
     */
    public Integer getObjectType() {
        return objectType;
    }

    /**
     * 设置业务类型
     *
     * @param objectType 业务类型
     */
    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    /**
     * 获取短信是否发送成功
     *
     * @return is_mobile_success - 短信是否发送成功
     */
    public Integer getIsMobileSuccess() {
        return isMobileSuccess;
    }

    /**
     * 设置短信是否发送成功
     *
     * @param isMobileSuccess 短信是否发送成功
     */
    public void setIsMobileSuccess(Integer isMobileSuccess) {
        this.isMobileSuccess = isMobileSuccess;
    }

    /**
     * 获取推送是否发送成功
     *
     * @return is_push_success - 推送是否发送成功
     */
    public Integer getIsPushSuccess() {
        return isPushSuccess;
    }

    /**
     * 设置推送是否发送成功
     *
     * @param isPushSuccess 推送是否发送成功
     */
    public void setIsPushSuccess(Integer isPushSuccess) {
        this.isPushSuccess = isPushSuccess;
    }

    /**
     * 获取是否读取
     *
     * @return is_read - 是否读取
     */
    public Integer getIsRead() {
        return isRead;
    }

    /**
     * 设置是否读取
     *
     * @param isRead 是否读取
     */
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取消息附加信息
     *
     * @return msg_desc - 消息附加信息
     */
    public String getMsgDesc() {
        return msgDesc;
    }

    /**
     * 设置消息附加信息
     *
     * @param msgDesc 消息附加信息
     */
    public void setMsgDesc(String msgDesc) {
        this.msgDesc = msgDesc;
    }

	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	public String getMsgUrl() {
		return msgUrl;
	}

	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
    
    
}