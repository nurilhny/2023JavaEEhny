package com.hny.config.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {

    // 编码密钥
    private String tokenSignKey = "5edaca6ca2e4d32b7a2a64cbaf9f6e07";

    @Bean
    public TokenStore tokenStore(){
        // 使用Jwt存储令牌
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(tokenSignKey);
        return converter;
    }
}
