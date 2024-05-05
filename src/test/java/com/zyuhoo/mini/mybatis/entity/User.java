package com.zyuhoo.mini.mybatis.entity;

import java.util.Date;

/**
 * About User.
 *
 * @since 0.0.1
 */
public class User {

    private Long id;

    private String phone;

    private String head;

    private Date birthday;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", phone='" + phone + '\'' + ", head='" + head + '\'' + ", birthday=" + birthday + '}';
    }
}
