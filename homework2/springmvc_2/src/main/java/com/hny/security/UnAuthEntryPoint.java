package com.hny.security;

import com.hny.utils.R;
import com.hny.utils.ResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 未授权处理
public class UnAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        R r = R.error();
        r.getData().put("hasAuth",false);
        r.setMessage("没授权呢");
        ResponseUtils.out(httpServletResponse,r);
    }
}
