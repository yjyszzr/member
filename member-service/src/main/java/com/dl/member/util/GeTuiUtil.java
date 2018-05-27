package com.dl.member.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dl.base.util.JSONHelper;
import com.dl.member.configurer.GeTuiConfig;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;

@Component
public class GeTuiUtil {

	private static final Logger logger = Logger.getLogger(GeTuiUtil.class);
	
	private static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	
//	@Resource
	public GeTuiConfig geTuiConfig;

	
	public static void main(String[] args) {
		GeTuiConfig geTuiConfig = new GeTuiConfig();
		geTuiConfig.setAppId("zH0e8aiYaa73oVVsElXX");
		geTuiConfig.setAppkey("R4iI9PslDe5LSXjpd7XSy");
		geTuiConfig.setAppSecret("24e2HwXbm064CiewPXklE6");
		geTuiConfig.setMasterSecret("wyvPFirDwS6iFRKhLR5wk6");
		GeTuiUtil util = new GeTuiUtil();
		util.geTuiConfig = geTuiConfig;
		String clientId = "4554f58b20758db36884ee4b0de2fc63";
		String title = "test";
		String content = "this is a test message!";
		GeTuiMessage getuiMessage = new GeTuiMessage();
		getuiMessage.setContent(content);
		getuiMessage.setTitle(title);
		util.pushMessage(clientId, getuiMessage);
	}
	
	/**
	 * 推送消息
	 * @param clientId
	 * @param title
	 * @param content
	 */
	public void pushMessage(String clientId, GeTuiMessage getuiMessage ){
		IGtPush push = new IGtPush(host, geTuiConfig.getAppkey(), geTuiConfig.getMasterSecret());
//		LinkTemplate template = this.linkTemplateDemo(title, content);
		String messageJson = JSONHelper.bean2json(getuiMessage);
		TransmissionTemplate template = this.transmissionTemplate(messageJson);
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);
		// 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
		message.setPushNetWorkType(0);
		Target target = new Target();
		target.setAppId(geTuiConfig.getAppId());
		target.setClientId(clientId);
		//target.setAlias(Alias);
		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			logger.error("推送异常", e);
			ret = push.pushMessageToSingle(message, target, e.getRequestId());
		}
		if (ret != null) {
			logger.info("推送返回：" + ret.getResponse().toString());
		} else {
			logger.info("推送服务器响应异常" );
		}
	}
	//透传消息
	private TransmissionTemplate transmissionTemplate(String messageJson) {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(geTuiConfig.getAppId());
		template.setAppkey(geTuiConfig.getAppkey());
		template.setTransmissionType(1);
		template.setTransmissionContent(messageJson);
		return template;
	}
	//消息模板,通知消息
	private LinkTemplate linkTemplateDemo(String title, String text) {
		LinkTemplate template = new LinkTemplate();
		// 设置APPID与APPKEY
		template.setAppId(geTuiConfig.getAppId());
		template.setAppkey(geTuiConfig.getAppkey());
		
		Style0 style = new Style0();
		// 设置通知栏标题与内容
		style.setTitle(title);
		style.setText(text);
		// 配置通知栏图标
		//	        style.setLogo("icon.png");
		// 配置通知栏网络图标
		style.setLogoUrl("");
		// 设置通知是否响铃，震动，或者可清除
		style.setRing(true);
		style.setVibrate(true);
		style.setClearable(true);
		template.setStyle(style);
		// 设置打开的网址地址
		template.setUrl("http://caixiaomi.net");
		return template;
	}
}
