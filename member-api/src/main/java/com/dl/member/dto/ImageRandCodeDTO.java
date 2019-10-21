package com.dl.member.dto;

import java.awt.image.BufferedImage;

/**
 * <p>
 * @Description 验证码VO
 * </p>
 * @author tangyongchun
 * @version 0.0.1
 * @ClassName ImageRandCodeVO.java
 * @since  2019/8/13 20:16
 */
public class ImageRandCodeDTO {
	/**
	 * 验证码key
	 */
	private String randomCodeKey;
	/**
	 * 验证码值
	 */
	private String randomString;
	/**
	 * 验证码图片
	 */
	private BufferedImage bufferedImage;

	public String getRandomCodeKey() {
		return randomCodeKey;
	}

	public void setRandomCodeKey(String randomCodeKey) {
		this.randomCodeKey = randomCodeKey;
	}

	public String getRandomString() {
		return randomString;
	}

	public void setRandomString(String randomString) {
		this.randomString = randomString;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

}