package com.dl.member.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Test {

	public static void main(String[] args) {
		try {
			String realName = URLEncoder.encode("张子荣", "UTF-8");
			String realNameUTF = URLDecoder.decode(realName,"UTF-8");
			System.out.println(realNameUTF);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
