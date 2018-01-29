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

    public static final String ALIPAY_CALLBACK_URL = "alipay.callback.url";

    public static final String REDIS_SERVER_IP = "redis.server.ip";

    public static final String REDIS_SERVER_PORT = "redis.server.port";

    public static final String REDIS_MAX_TOTAL = "redis.max.total";

    public static final String DEFAULT_REDIS_MAX_TOTAL = "20";

    public static final String REDIS_MAX_IDLE = "redis.max.idle";

    public static final String DEFAULT_REDIS_MAX_IDLE = "10";

    public static final String REDIS_MIN_IDLE = "redis.min.idle";

    public static final String DEFAULT_REDIS_MIN_IDLE = "2";

    public static final String REDIS_TEST_BORROW = "redis.test.borrow";

    public static final String DEFAULT_REDIS_TEST_BORROW = "true";

    public static final String REDIS_TEST_RETURN = "redis.test.return";

    public static final String DEFAULT_REDIS_TEST_RETURN  = "false";

    public static final Integer FORGET_PASSWORD_RESET_TOKEN_EXPIRE = 3600;

    public static final String TOKEN_PREFIX = "TOKEN_";


    public interface RedisCacheExtime{
        int REDIE_SESSION_TIME = 60*30;//30MIN
    }
    public interface ProductListOrderBy{
        Set<String> PRICE_ASE_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Cart {
        int CHECKED = 1;  //选中
        int UNCHECKED = 0;//未选中

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface Role {
        int ROLE_CUSTOMER = 0;   //普通用户
        int ROLE_ADMIN = 1;     //管理员
    }

    public enum ProductStatusEnum {
        ON_SALE(1, "在线"),
        REMOVED(2, "下架"),
        DELETED(3, "删除");
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

    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");
        private String value;
        private int code;

        OrderStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举（OrderStatusEnum）");
        }
    }

    public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String TRADE_STATUS_TRADE_CLOSED = "TRADE_CLOSED";
        String TRADE_STATUS_TRADE_FINISHED = "TRADE_FINISHED";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");

        private String value;
        private int code;

        PayPlatformEnum(int code, String value) {
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

    public enum PaymentTypeEnum {
        ONLINE_PAY(1, "在线支付");

        private String value;
        private int code;

        PaymentTypeEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static PaymentTypeEnum codeOf(int code){
            for(PaymentTypeEnum paymentTypeEnum : values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举（PaymentTypeEnum）");
        }
    }
}
