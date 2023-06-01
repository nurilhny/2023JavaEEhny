package com.hny.config;

import com.hny.filter.TokenAuthFilter;
import com.hny.filter.TokenLoginFilter;
import com.hny.security.DefaultPasswordEncoder;
import com.hny.security.TokenLogoutHandler;
import com.hny.security.TokenManager;
import com.hny.security.UnAuthEntryPoint;
import com.hny.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@Component
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
    private TokenManager tokenManager;
    private DefaultPasswordEncoder defaultPasswordEncoder;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @Autowired
    public WebAppSecurityConfig(TokenManager tokenManager, DefaultPasswordEncoder defaultPasswordEncoder, UserDetailsService userDetailsService,UserService userService) {
        this.tokenManager = tokenManager;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling()
                .authenticationEntryPoint(new UnAuthEntryPoint()) // 无权限访问时，执行
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/orders/user/**").hasAuthority("user")
                .antMatchers("/orders/courier/**").hasAuthority("courier")
                .antMatchers("/products/**").hasAnyAuthority("user","courier")
                .antMatchers("/sites/**").hasAnyAuthority("user","courier")
                .antMatchers("/users/**").hasAnyAuthority("manager")
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/common/logout") // 退出时，访问路径
                .addLogoutHandler(new TokenLogoutHandler(tokenManager))
                .and().addFilter(new TokenLoginFilter(authenticationManager(),tokenManager))
                .addFilter(new TokenAuthFilter(authenticationManager(),tokenManager,userService)).httpBasic();
    }

    // 调用userDetailService和密码处理
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    // 不进行认证的路径
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/common/register");
    }
}