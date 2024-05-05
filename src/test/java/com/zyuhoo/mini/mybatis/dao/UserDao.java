package com.zyuhoo.mini.mybatis.dao;

import com.zyuhoo.mini.mybatis.entity.User;

/**
 * Provide dao interface test.
 */
public interface UserDao {
    String queryUserName(String phone);

    String queryUsrAge(String phone);

    User queryUserInfoById(Long id);
}
