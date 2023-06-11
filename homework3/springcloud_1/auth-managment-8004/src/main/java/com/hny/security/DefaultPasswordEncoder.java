package com.hny.security;

import com.hny.utils.MD5Utils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

    public DefaultPasswordEncoder() {
        this(-1);
    }

    public DefaultPasswordEncoder(int strength){
        
    }
    
    // MD5加密
    @Override
    public String encode(CharSequence charSequence) {
        return MD5Utils.getPWD(charSequence.toString());
    }

    // 密码比对
    @Override
    public boolean matches(CharSequence charSequence, String encodedPassword) {
        return encodedPassword.equals(MD5Utils.getPWD(charSequence.toString()));
    }
}
