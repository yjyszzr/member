package com.dl.member.util;

import com.dl.member.dto.ImageRandCodeDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * <p>
 *
 * @author tangyongchun
 * @Description 验证码工具
 * </p>
 * @ClassName RanCodeUtil.java
 * @since 2019/8/13 20:09
 */
public class RanCodeUtil {
    /**
     * 验证码session key
     */
    public static final String VD_RANDOM_CODE = "VD_RANDOM_CODE";

    private static RanCodeUtil instance = null;

    public static RanCodeUtil getInstance() {
        if (instance == null) {
            instance = new RanCodeUtil();
        }
        return instance;
    }

    private final Random random = new Random();

    private String randString = "0123456789";// ABCDEFGHIJKLMNOPQRSTUVWXYZ 随机产生的字符串
    private int width = 80;// 图片宽
    private int height = 26;// 图片高
    private int fontsize = 18;//文字大小
    private int lineSize = 40;// 干扰线数量
    private int pointSize = 30;// 干扰点数量
    private int stringNum = 4;// 随机产生字符数量

    /**
     * 设置字体
     */
    private Font getFontFamily() {
        //Font[] fonts=new Font[]{new Font("微软雅黑", Font.PLAIN,18),new Font("Atlantic Inline", Font.PLAIN,18),new Font("华为细黑",Font.PLAIN,18)};
        Font[] fonts = new Font[]{new Font("Atlantic Inline", Font.PLAIN, fontsize)};
        Font returnFont = fonts[random.nextInt(fonts.length)];
        return returnFont;
    }

    /**
     * 设置字体颜色
     */
    private Color getFontColor() {
        Color[] colors = {Color.BLUE};
        Color returnColor = colors[random.nextInt(colors.length)];
        return returnColor;
    }

    /**
     * 生成随机图片
     *
     * @param w 图片宽
     * @param h 图片高
     * @param f 文字大小
     * @return
     */
    public ImageRandCodeDTO createRandcode(Integer w, Integer h, Integer f) {
        if (w != null) this.width = w;
        if (h != null) this.height = h;
        if (f != null) this.fontsize = f;

        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        // 产生Image对象的Graphics对象,该对象可以在图像上进行各种绘制操作
        Graphics g = image.getGraphics();
        //设定背景颜色
        //g.setColor(new Color(0xF0FFF0));
        g.setColor(new Color(0xFFFFFF));
        g.fillRect(0, 0, width, height);
        //画边框
        g.drawRect(0, 0, width, height);
        // 获得并绘制随机字符串随机字符
        String randomString = "";
        for (int i = 1; i <= stringNum; i++) {
            // 获得字符串
            char chr = randString.charAt(random.nextInt(randString.length()));
            randomString += chr;
            // 绘制字符串
            g.setFont(getFontFamily());
            g.setColor(getFontColor());
            g.translate(random.nextInt(3), random.nextInt(3));
            g.drawString(String.valueOf(chr) + " ", (this.fontsize - 6) * i, (this.fontsize - 2));
        }
        // 绘制干扰线
        drowDisturbPoint(g);
        // 保存到VO
        ImageRandCodeDTO imageRandCodeVO = new ImageRandCodeDTO();
        imageRandCodeVO.setBufferedImage(image);
        imageRandCodeVO.setRandomCodeKey(VD_RANDOM_CODE);
        imageRandCodeVO.setRandomString(randomString);
        //释放图形上下文
        g.dispose();
        return imageRandCodeVO;
    }

    /**
     * 绘制干扰线
     */
    @SuppressWarnings("unused")
    private void drowDisturbLine(Graphics g) {
        for (int i = 0; i <= lineSize; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(13);
            int yl = random.nextInt(15);
            g.drawLine(x, y, x + xl, y + yl);
        }
    }

    //随机产生10个干扰点
    private void drowDisturbPoint(Graphics g) {
        for (int i = 0; i < pointSize; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.setColor(getFontColor());
            g.drawOval(x, y, 1, 1);
        }
    }

    /**
     * 获取验证码
     *
     * @param request
     * @return
     */
    public ImageRandCodeDTO getRandcode(HttpServletRequest request) {
        String strw = request.getParameter("w");
        Integer w = strw != null ? Integer.valueOf(strw) : null;
        String strh = request.getParameter("h");
        Integer h = strh != null ? Integer.valueOf(strh) : null;
        String strf = request.getParameter("f");
        Integer f = strf != null ? Integer.valueOf(strf) : null;
        ImageRandCodeDTO imageRandCodeVO = RanCodeUtil.getInstance().createRandcode(w, h, f);
        HttpSession session = request.getSession();
        session.removeAttribute(VD_RANDOM_CODE);
        session.setAttribute(VD_RANDOM_CODE, imageRandCodeVO.getRandomString());
        return imageRandCodeVO;
    }

    /**
     * 验证验证码是否正确
     *
     * @param checkCode - 获取的验证码
     * @param request
     * @return
     */
    public boolean checkCaptcha(String checkCode, HttpServletRequest request) {
        //1、判断验证码是否存在
        String imgVal = (String) request.getSession().getAttribute(RanCodeUtil.VD_RANDOM_CODE);
        if (null == imgVal) {
            return false;
        }

        //2、判断验证码的准确性
        String checkInput = checkCode.toLowerCase();
        String img = imgVal.toLowerCase();
        if (!img.equals(checkInput)) {
            return false;
        }
        return true;
    }
}
