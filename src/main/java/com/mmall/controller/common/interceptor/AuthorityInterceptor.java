package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jane on 2018/1/30.
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod)o;

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        StringBuffer requestParamBuffer = new StringBuffer();
        Map parmMap = request.getParameterMap();
        Iterator it = parmMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String)entry.getKey();
            String mapValue = StringUtils.EMPTY;
            Object obj = entry.getValue();
            if(obj instanceof String[]){
                String[] strs = (String[])obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue).append(";");
        }

        if(StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName, "login")){
            log.info("拦截器拦截到用户登录请求，className：{}，methodName：{}，param：{}",
                    className, methodName, requestParamBuffer.toString());
            return true;
        }
        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.stringToObject(userJsonStr, User.class);
        }
        if(user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter out = response.getWriter();

            if(user == null){
                if(StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg", ResponseCode.NEED_LOGIN.getDesc());
                    out.print(JsonUtil.objToString(resultMap));
                }else {
                    out.print(JsonUtil.objToString(ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                            "interceptor"+ResponseCode.NEED_LOGIN.getDesc())));
                }
            }else {
                if(StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg", ResponseCode.NO_AUTHORITY.getDesc());
                    out.print(JsonUtil.objToString(resultMap));
                }else {
                    out.print(JsonUtil.objToString(ServerResponse.createByErrorCodeMessage(ResponseCode.NO_AUTHORITY.getCode(),
                            "interceptor"+ResponseCode.NO_AUTHORITY.getDesc())));
                }
            }

            out.flush();
            out.close();

            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
