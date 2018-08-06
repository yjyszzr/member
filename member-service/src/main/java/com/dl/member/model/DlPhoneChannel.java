package com.dl.member.model;

import javax.persistence.*;

@Table(name = "dl_phone_channel")
public class DlPhoneChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String channel;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "app_code_name")
    private Integer appCodeName;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @param channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * @return channel_name
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * @param channelName
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * @return app_code_name
     */
    public Integer getAppCodeName() {
        return appCodeName;
    }

    /**
     * @param appCodeName
     */
    public void setAppCodeName(Integer appCodeName) {
        this.appCodeName = appCodeName;
    }
}