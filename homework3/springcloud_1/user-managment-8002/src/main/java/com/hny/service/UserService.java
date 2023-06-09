package com.hny.service;

import com.hny.entity.User;

import java.util.List;

public interface UserService {
    String getPasswordByAccount(String account);

    String getUserTypeByAccount(String account);

    int getUserIdByAccount(String account);

    int addAccount(User user);

    int delUserByUserId(int userId);

    User getUserByUserId(int userId);

    List<User> getAllUsers();

}
