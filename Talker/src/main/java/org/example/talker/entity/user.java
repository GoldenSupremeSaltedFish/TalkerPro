package org.example.talker.entity;

/**
 * 用户实体类
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
public class user {
    int id;
    String name=null;
    String email=null;
    String password=null;
    String wechat=null;
    public user(int id, String name, String email, String password, String wechat) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.wechat = wechat;
    }
}
