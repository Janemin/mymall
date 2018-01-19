package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Jane on 2018/1/17.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public static final String FTP_SERVER_HTTP_PREFIX = "ftp.server.http.prefix";

    public static final String DEFAULT_FTP_SERVER_HTTP_PREFIX = "http://img.happymmall.com/";

    public static final String FTP_SERVER_IP = "ftp.server.ip";

    public static final String FTP_SERVER_PORT = "ftp.server.port";

    public static final String FTP_SERVER_USERNAME= "ftp.user";

    public static final String FTP_SERVER_PASSWORD = "ftp.pass";


    public interface ProductListOrderBy{
        Set<String> PRICE_ASE_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Role {
        int ROLE_CUSTOMER = 0;   //普通用户
        int ROLE_ADMIN = 1;     //管理员
    }

    public enum ProductStatusEnum {
        ON_SALE(1, "在线");
        private String value;
        private int code;

        ProductStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}
