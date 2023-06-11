package com.hny.config;


import com.hny.security.DefaultPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer implements AuthorizationServerConfigurer {


    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    // 令牌访问端点安全策略
    @Override
    public void configure(AuthorizationServerSecurityConfigurer authorizationServerSecurityConfigurer) throws Exception {
        authorizationServerSecurityConfigurer.tokenKeyAccess("permitAll()")   //oauth/token_key是公开
                .checkTokenAccess("permitAll()")                              //oauth/check_token公开
                .allowFormAuthenticationForClients()                          //表单认证(申请令牌)
                ;

    }

    // 客户端详情服务
    @Override
    public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception {
        clientDetailsServiceConfigurer.inMemory()
                .withClient("c1") // client_id
                .secret(new DefaultPasswordEncoder().encode("secret")) // 客户端密钥
                .resourceIds("res1") // 资源列表
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")
                .scopes("all") //允许授权范围
                .autoApprove(false) //跳转到授权页面
                .redirectUris("http://localhost:8181"); //验证回调地址
    }

    //令牌访问端点
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer) throws Exception {

        authorizationServerEndpointsConfigurer.authenticationManager(authenticationManager) //认证管理器
                .authorizationCodeServices(authorizationCodeServices) //授权码服务
                .tokenServices(tokenService()) //令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    // 令牌管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService(){
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService); // 客户端详情服务
        services.setSupportRefreshToken(true); // 支持刷新令牌
        services.setTokenStore(tokenStore); // 令牌存储方式

        // 令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);
        
        services.setAccessTokenValiditySeconds(7200); //令牌默认有效2小时
        services.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return services;
    }

    // 授权码服务
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        return new InMemoryAuthorizationCodeServices();
    }
}
