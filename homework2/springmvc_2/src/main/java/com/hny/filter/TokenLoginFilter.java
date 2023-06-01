package com.hny.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hny.bean.SecurityUser;
import com.hny.bean.User;
import com.hny.utils.R;
import com.hny.utils.ResponseUtils;
import com.hny.security.TokenManager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;


public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;

    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager,TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.authenticationManager = authenticationManager;
        this.setPostOnly(false);
        System.out.println("-------------这里是TokenLoginFilter--------------");
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/common/login","POST"));
    }

    // 获取表单提交的用户名和密码
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("-------------这里是TokenLoginFilter提交--------------");
        StringBuffer stringBuffer = new StringBuffer("");
        BufferedReader bufferedReader = null;
        String str = null;
        try {
            bufferedReader = request.getReader();
            while((str=bufferedReader.readLine())!=null){
                stringBuffer.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        try {
            user = objectMapper.readValue(stringBuffer+"",User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 获取表单提交数据
        String username = user.getUsername();
        String password = user.getPassword();

        System.out.println(password);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));

    }

    // 认证成功
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("-----------认证成功！--------");
        // 认证成功，得到用户信息
        SecurityUser user = (SecurityUser) authResult.getPrincipal();
        // 根据用户名生成token
        String token = tokenManager.createToken(user.getUsername());
        // 存入数据库

        R r = R.ok();
        r.getData().put("token",token);
        r.getData().put("user",user.getCurUser());
        ResponseUtils.out(response, r);
    }

    // 认证失败
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("-----------认证失败！--------");
        ResponseUtils.out(response, R.error());
    }
}
