package com.dl.member.web;

import com.dl.member.util.RanCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *
 * @author tangyongchun
 * @Description 验证码生成
 * </p>
 * @ClassName RandCodeController.java
 * @since 2019/8/13 20:19
 */
@Api(value = "获取验证码接口", tags = "获取验证码接口")
@RequestMapping("/captcha")
@Controller
public class RandCodeController {

    /**
     * 验证码输出
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @GetMapping("/getCode")
    public void get(HttpServletRequest request, HttpServletResponse response) {
        try {
            ImageIO.write(RanCodeUtil.getInstance().getRandcode(request).getBufferedImage(), "JPEG",
                    response.getOutputStream());// 将内存中的图片通过流动形式输出到客户端
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}