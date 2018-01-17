package com.mmall.common;

/**
 * Created by Jane on 2018/1/17.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";



    public interface Role {
        int ROLE_CUSTOMER = 0;   //普通用户
        int ROLE_ADMIN = 1;     //管理员
    }
}