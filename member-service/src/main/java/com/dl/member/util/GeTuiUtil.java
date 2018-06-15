package com.dl.member.util;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dl.base.util.JSONHelper;
import com.dl.member.configurer.GeTuiConfig;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;

@Component
public class GeTuiUtil {

	private static final Logger logger = Logger.getLogger(GeTuiUtil.class);
	
	private static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	
	@Resource
	public GeTuiConfig geTuiConfig;

	
	/*public static void main(String[] args) {
		GeTuiConfig geTuiConfig = new GeTuiConfig();
		geTuiConfig.setAppId("BWgBz2PhAq5ZxmZ7e4yINA");
		geTuiConfig.setAppkey("o74Y1SjdzI73MhkkAwuXp4");
		geTuiConfig.setAppSecret("vaBGD6ddiO7LrnarEJO5hA");
		geTuiConfig.setMasterSecret("ZkxNSKsKAc9zSl5kjmXuN2");
		GeTuiUtil util = new GeTuiUtil();
		util.geTuiConfig = geTuiConfig;
//		String clientId = "f6ca881b596b5dc0149957d0934cb602";//android
		String clientId = "b14b94104f5605e27a41e33ccc6cca78";
		String title = "优惠券到期通知";
		String content = "您有优惠券即将到期，请尽快使用";
		GeTuiMessage getuiMessage = new GeTuiMessage();
		getuiMessage.setContent(content);
		getuiMessage.setTitle(title);
		getuiMessage.setPushTime(DateUtil.getCurrentTimeLong());
		util.pushMessage(clientId, getuiMessage);
	}*/
	
	/**
	 * 推送消息
	 * @param clientId
	 * @param title
	 * @param content
	 */
	public void pushMessage(String clientId, GeTuiMessage getuiMessage ){
		IGtPush push = new IGtPush(host, geTuiConfig.getAppkey(), geTuiConfig.getMasterSecret());
//		LinkTemplate template = this.linkTemplateDemo(title, content);
		TransmissionTemplate template = this.transmissionTemplate(getuiMessage);
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
	private TransmissionTemplate transmissionTemplate(GeTuiMessage getuiMessage ) {
		String messageJson = JSONHelper.bean2json(getuiMessage);
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(geTuiConfig.getAppId());
		template.setAppkey(geTuiConfig.getAppkey());
		template.setTransmissionType(2);
		template.setTransmissionContent(messageJson);
		APNPayload payload = new APNPayload();
		//在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
//		payload.setAutoBadge("+1");
//		payload.setContentAvailable(1);
//		payload.setSound("default");
//		payload.setCategory("$由客户端定义");

		//简单模式APNPayload.SimpleMsg
//		payload.setAlertMsg(new APNPayload.SimpleAlertMsg("测试不确定数据"));

		//字典模式使用APNPayload.DictionaryAlertMsg
		APNPayload.DictionaryAlertMsg alterMsg = new APNPayload.DictionaryAlertMsg();
		alterMsg.setBody(getuiMessage.getContent());
		alterMsg.setTitle(getuiMessage.getTitle());
		payload.setAlertMsg(alterMsg);

		// 添加多媒体资源
		/*payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.video)
				.setResUrl("http://ol5mrj259.bkt.clouddn.com/test2.mp4")
				.setOnlyWifi(true));*/
		//需要使用IOS语音推送，请使用VoIPPayload代替APNPayload
		// VoIPPayload payload = new VoIPPayload();
		// JSONObject jo = new JSONObject();
		// jo.put("key1","value1");         
		//		         payload.setVoIPPayload(jo.toString());
		//
		template.setAPNInfo(payload);
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
