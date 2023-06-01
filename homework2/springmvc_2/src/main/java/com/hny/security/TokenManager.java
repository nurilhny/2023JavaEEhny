package com.hny.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {
    // token有效时长
    private long tokenExpiration = 60*60*1000;
    // 编码密钥
    private String tokenSignKey = "5edaca6ca2e4d32b7a2a64cbaf9f6e07";

    // 根据用户名生成token
    public String createToken(String username){
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis()+tokenExpiration))
                .signWith(SignatureAlgorithm.HS512,tokenSignKey) //加密方式
                .compressWith(CompressionCodecs.GZIP).compact() //压缩算法
                ;

        return token;
    }

    // 根据token字符串得到用户信息
    public String getUserFromToken(String token){
        String username = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return username;
    }

    // 删除token
    public void delToken(String token){

    }

}
