package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jane on 2018/1/28.
 */
@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = ".mymall.com";

    private final static String COOKIE_NAME = "mymall_login_token";

    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24*30);
        log.info("write cookieName:{}, cookieValue{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie ckitem : cookies){
                log.info("read cookieName:{}, cookieValue:{}", ckitem.getName(), ckitem.getValue());
                if(StringUtils.equals(ckitem.getName(), COOKIE_NAME)){
                    log.info("return cookieName:{}, cookieValue:{}", ckitem.getName(), ckitem.getValue());
                    return ckitem.getValue();
                }
            }
        }
        return null;
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie ckitem : cookies){
                if(StringUtils.equals(ckitem.getName(), COOKIE_NAME)){
                    ckitem.setDomain(COOKIE_DOMAIN);
                    ckitem.setPath("/");
                    ckitem.setMaxAge(0);   //删除cookie
                    log.info("del cookieName:{}, cookieValue:{}", ckitem.getName(), ckitem.getValue());
                    response.addCookie(ckitem);
                    return;
                }
            }
        }
    }



}
