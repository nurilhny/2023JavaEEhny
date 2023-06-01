package com.hny.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {

    // 当前登录用户
    private transient User curUser;

    // 当前权限
    private List<String> permissionList;

    public SecurityUser() {
    }

    public SecurityUser(User user) {
        if(user!=null){
            this.curUser = user;
        }

    }

    public User getCurUser() {
        return curUser;
    }

    public void setCurUser(User curUser) {
        this.curUser = curUser;
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
    }

    // 获取权限列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String permission: permissionList){
            if(StringUtils.isEmpty(permission)) continue;
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
            authorities.add(authority);

        }
        return authorities;
    }

    // 获取密码
    @Override
    public String getPassword() {
        return curUser.getPassword();
    }

    // 获取账户
    @Override
    public String getUsername() {
        return curUser.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "SecurityUser{" +
                "curUser=" + curUser +
                ", permissionList=" + permissionList +
                '}';
    }
}
