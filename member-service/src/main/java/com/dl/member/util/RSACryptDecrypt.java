package com.dl.member.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.json.JSONObject;

public class RSACryptDecrypt {

	/**
	 * 根据私钥字符串加载私钥
	 *
	 * @param privateKeyStr 私钥字符串
	 * @return
	 * @throws Exception
	 */
	public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
		try {
			byte[] buffer = javax.xml.bind.DatatypeConverter.parseBase64Binary(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法", e);
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法", e);
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空", e);
		}
	}

	/**
	 * 私钥解密
	 *
	 * @param privateKey 私钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	public static String decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return new String(output);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw new Exception("密文数据已损坏");
		}
	}

	public static byte[] strToBase64(String str) {
		return javax.xml.bind.DatatypeConverter.parseBase64Binary(str);
	}

	public static void main(String[] args) throws Exception {
		String privateKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKSoYm6JpD3C8rcdUteUToP9gnVxlNh16TC0Mj+ysBoI7ulRyHm53L0fdj+fXWsh5Oh6GzRkHo0UPB3yfE0eF6SFUSUG4mREy+O1UvjOS3xBdRw1vnQwTAqZTR/h6hGDXs6hxjwZemxoO4UBAq/8Z35O//km9JtV7m/0QP4tvWFTAgMBAAECgYEAlGCMetGb6G/xamOJUGz7p+NdmVml6L2wSkxHb7ElEgD00dn5zv9W1DIyy7hfZXggoAHQ3BBQm48dcVse+htg71TJ3b/iNlmiAjSDzneN/pFdCogjjNPzu03Ede9Fo9lMNRtTLDbwGC/QED5Ni2XMwEHh5ZSwjYTyr2eO61SGhqkCQQDbEBDzPTRdiDZzGdyTWUrmzbuza1oZylncocnPf/mXZFaiVrc+pZs/URXKacx4dVpNnVQMxaIkzvrzKBg2OQI/AkEAwGvnY/KP7JGPje0HxdXSLqCC2Tptq1Soz5HsL+Jbg7e4RZa3V3kBakiMAxZ4E3Yshl4VLQWdPXM9CoCavAxz7QJAfeiKFYREKM41nYprwTU9W9M74y/8pX4skCddeyovJtjBAqWcxSEiTg4o3CJbJ5Eryhh3Sq/9NfLRQhj1cZTquQJAYtEHK5TNxLeb+U7YxwhfwAI+MECJQYtghfhySsT8KlCXGWWlEg7aYzKOdKd9UsFFbFu0lEJl1cDwgAw7aocaoQJARArOmyo85lVVsx2TiSZg76TArg21i8XEP4wuyre6wLVC+eAarXyUi6lR3BVxR9/iYBO0puhbzIC0HgJf7Nxvpg==";
		System.out.println("初始化私钥为：" + privateKeyStr);
		// 解密
		String str = "K6VZTheXFm+Il6H67AuqcXABJFiMEL0oI679UOeR/okOM+wRKNy1+4hDj1nNuRX14/NI9U5dOC/wJhPsuUG1zmtU9kaahxJ/LnWZvenW5VLRlVFw9e1MY9Adc22A0yE6yidSEOmyrv7dni52WWOUtOTqwSAnCeXhun0AVk9Rrrk=";
		String decryptData = RSACryptDecrypt.decrypt(RSACryptDecrypt.loadPrivateKey(privateKeyStr), RSACryptDecrypt.strToBase64(str));
		System.out.println("解密后：" + decryptData);
		JSONObject jsonObj = new JSONObject(decryptData);
		String phone = (String) jsonObj.get("phone");
		System.out.println("phone:" + phone);
		int money = jsonObj.getInt("money");
		System.out.println("money:" + money);
	}
}