package com.hny.service.impl;

import com.hny.entity.User;
import com.hny.mapper.UserMapper;
import com.hny.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public String getPasswordByAccount(String account) {
        return userMapper.getPasswordByAccount(account);
    }

    @Override
    public String getUserTypeByAccount(String account) {
        return userMapper.getUserTypeByAccount(account);
    }

    @Override
    public int getUserIdByAccount(String account) {
        return userMapper.getUserIdByAccount(account);
    }

    @Override
    public int addAccount(User user) {
        return userMapper.addAccount(user);
    }

    @Override
    public int delUserByUserId(int userId) {
        return userMapper.delUserByUserId(userId);
    }

    @Override
    public User getUserByUserId(int userId) {
        return userMapper.getUserByUserId(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }


}
