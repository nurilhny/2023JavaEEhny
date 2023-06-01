package com.hny.filter;

import com.hny.bean.User;
import com.hny.service.UserService;
import com.hny.security.TokenManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TokenAuthFilter extends BasicAuthenticationFilter {
    private TokenManager tokenManager;

    private UserService userService;


    public TokenAuthFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, UserService userService) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取当前认证成功用户权限信息
        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);

        // 判断如果有权限信息，放到权限上下文中
        if(authRequest!=null){
            SecurityContextHolder.getContext().setAuthentication(authRequest);
        }

        chain.doFilter(request,response);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        // 从header获取token
        String token = request.getHeader("token");
        if(token!=null&&!token.equals("")){
            // 获取账户
            String username = tokenManager.getUserFromToken(token);

            // 从数据库获取权限列表
            int userId = userService.getUserIdByAccount(username);
            User user = userService.getUserByUserId(userId);

            String userType = user.getUserType();

            List<String> permissionList = new ArrayList<>();

            permissionList.add(userType);

            Collection<GrantedAuthority> authorities = new ArrayList<>();

            for (String permission: permissionList) {
                SimpleGrantedAuthority auth = new SimpleGrantedAuthority(permission);
                authorities.add(auth);
            }

            return new UsernamePasswordAuthenticationToken(username,token,authorities);

        }
        return null;
    }
}
