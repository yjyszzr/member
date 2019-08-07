package com.dl.member.util;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

/**
 * 密码2次加盐
 *
 * @author yanpengyu
 */
public class Encryption {

    private static String MD5(String str) {
        String result = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((str).getBytes("UTF-8"));
            byte b[] = md5.digest();

            int i;
            StringBuffer buf = new StringBuffer("");

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            result = buf.toString();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        return result;
    }

    public static String encryption(String str1, String str2) {
        if (StringUtils.isEmpty(str1) || StringUtils.isEmpty(str2)) {
            return "";
        }
        String s1 = MD5(str1);
        String s2 = MD5(s1 + str2);
        return s2;
    }

    public static String salt() {
        String s1 = (int) ((Math.random() * 9 + 1) * 100000) + "";
        return s1;
    }

    public static void main(String[] args) {
//        String str1 = MD5("111111");
//        String str2 = MD5(str1 + "234567");
//        System.out.println("str2 = " + str2);
        //pass = 18bae7fd13f7e119de1efcfc5b175fb5

        Double d = Double.valueOf("-5.5");
        System.out.print("d:"+d);
    }
}
