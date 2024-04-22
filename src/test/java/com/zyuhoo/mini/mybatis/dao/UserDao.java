package com.zyuhoo.mini.mybatis.dao;

/**
 * Provide dao interface test.
 */
public interface UserDao {
    String queryUserName(String userId);

    String queryUsrAge(String userId);
}
