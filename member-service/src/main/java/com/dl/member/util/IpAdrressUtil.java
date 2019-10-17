package com.dl.member.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class IpAdrressUtil {
	private final static Logger logger = Logger.getLogger(IpAdrressUtil.class);
	/**
	 * 获取Ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAdrress(HttpServletRequest request) {
		String Xip = request.getHeader("X-Real-IP");
		String XFor = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = XFor.indexOf(",");
			if (index != -1) {
				return XFor.substring(0, index);
			} else {
				return XFor;
			}
		}
		XFor = Xip;
		if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
			return XFor;
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
			XFor = request.getRemoteAddr();
		}
		return XFor;
	}
	/**
	 * 获取ip的地理位置
	 * @param ip
	 * @return
	 */
	public static String getIPAddress(String ip) {
		Connection connect = Jsoup.connect("https://www.ipip.net/ip.html");
		try {
			Document doc = connect.data("ip", ip).post();
			Elements tables = doc.getElementsByTag("table");
			Elements trs = tables.get(0).getElementsByTag("tr");
			Elements tds = trs.get(2).getElementsByTag("td");
			String address = tds.get(1).getElementsByTag("span").text();
			return address;
		} catch (Exception e) {
			logger.error(ip + "获取所属地理位置时出错");
		}
		return null;
	}
	 //判断是否是国内ip
	public static boolean isChinaIp(String ip) {
		String ipAddress = getIPAddress(ip);
		if(StringUtils.isBlank(ipAddress)) {
			return true;
		}
		if(ipAddress.contains("中国")) {
			return true;
		}
		return false;
	}
	
/*	public static void main(String[] args) {
//		String ip = "123.117.171.93";
		String ip = "8.8.8.8";
		boolean chinaIp = isChinaIp(ip);
		System.out.println(chinaIp);
	}*/
}