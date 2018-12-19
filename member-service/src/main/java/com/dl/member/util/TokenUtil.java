package com.dl.member.util;

import com.dl.base.context.BaseContextHandler;
import com.dl.base.exception.ServiceException;
import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.shop.auth.api.IAuthService;
import com.dl.shop.auth.dto.AuthInfoDTO;
import com.dl.shop.auth.dto.InvalidateTokenDTO;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * token 工具类
 *
 * @author 李洪春
 * @create 2017/12/20 09:59
 **/
@Component
public class TokenUtil implements ApplicationContextAware {
    /**
     * 鉴权服务
     */
    private static IAuthService authService;


    /**
     * 获取token
     *
     * @param userId    用户ID
     * @param loginType 登陆类型
     * @return
     */
    public static String genToken(Integer userId, Integer loginType) {
        AuthInfoDTO authInfoDTO = new AuthInfoDTO();
        authInfoDTO.setUserId(userId);
        authInfoDTO.setLoginType(loginType);
        BaseResult<String> result = authService.genToken(authInfoDTO);
        if (!result.isSuccess()) {
            throw new ServiceException(result);
        }
        return result.getData();
    }


    /**
     * 获取userId
     *
     * @param
     * @return
     */
    public static Integer getUserIdByToken(String token) {
        InvalidateTokenDTO invalidateTokenDTO = new InvalidateTokenDTO();
        invalidateTokenDTO.setToken(token);
        BaseResult<Integer> rst = authService.getUserIdByToken(invalidateTokenDTO);
        if(!rst.isSuccess()){
            throw new ServiceException(rst);
        }
        return rst. getData();
    }

    /**
     * 使当前token失效
     */
    public static void invalidateCurToken() {
        InvalidateTokenDTO invalidateTokenDTO = new InvalidateTokenDTO();
        invalidateTokenDTO.setInvalidateType(1);
        invalidateTokenDTO.setToken(BaseContextHandler.getToken());
        authService.invalidate(invalidateTokenDTO);
    }

    /**
     * 使用户全部token失效
     */
    public static void invalidateUserToken(Integer userId) {
        InvalidateTokenDTO invalidateTokenDTO = new InvalidateTokenDTO();
        invalidateTokenDTO.setInvalidateType(2);
        invalidateTokenDTO.setUserId(userId);
        authService.invalidate(invalidateTokenDTO);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        authService = applicationContext.getBean(IAuthService.class);
    }
}
