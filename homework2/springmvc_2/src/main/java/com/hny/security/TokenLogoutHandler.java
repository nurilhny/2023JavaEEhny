package com.hny.security;

import com.hny.utils.R;
import com.hny.utils.ResponseUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// 退出处理器

public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;

    public TokenLogoutHandler(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {


        String token = httpServletRequest.getHeader("token");
        if(token!=null){
            // 移除token
            tokenManager.delToken(token);

        }
        ResponseUtils.out(httpServletResponse, R.ok());

    }
}
