package com.hny.mapper;

import com.hny.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getAllUsers();

    String getPasswordByAccount(String account);

    String getUserTypeByAccount(String account);

    int getUserIdByAccount(String account);

    int addAccount(@Param("user") User user);

    int delUserByUserId(int userId);

    User getUserByUserId(int userId);
}
