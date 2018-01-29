package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import com.sun.org.apache.bcel.internal.generic.IUSHR;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Jane on 2018/1/16.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse response) {
        ServerResponse<User> serverResponse = iUserService.login(username, password);
        if (serverResponse.isSuccess()) {
//            session.setAttribute(Const.CURRENT_USER, response.getData());
            CookieUtil.writeLoginToken(response, session.getId());
            RedisPoolUtil.setEx(session.getId(), JsonUtil.objToString(serverResponse.getData()), Const.RedisCacheExtime.REDIE_SESSION_TIME);
        }
        return  serverResponse;
    }

    /**
     * 登出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        //session.removeAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        RedisPoolUtil.del(loginToken);
        return  ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return  iUserService.register(user);
    }

    @RequestMapping(value = "checkValid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return  iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "getUserInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(
            String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(
            String username, String passwordNew, String forgetToken) {
        return  iUserService.forgetResetPassword(username, passwordNew ,forgetToken);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest request,
                                                String passwordOld, String passwordNew) {
        //User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return  iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    @RequestMapping(value = "update_infomation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfomation(HttpServletRequest request, User user, HttpServletResponse response) {
        //User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User currentUser = JsonUtil.stringToObject(userJsonStr, User.class);
        if(currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        user.setId(user.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> serverResponse = iUserService.updateUserInfo(user);
        if (serverResponse.isSuccess()) {
            //session.setAttribute(Const.CURRENT_USER, response.getData());
            RedisPoolUtil.setEx(loginToken, JsonUtil.objToString(serverResponse.getData()), Const.RedisCacheExtime.REDIE_SESSION_TIME);

        }
        return serverResponse;
    }

    @RequestMapping(value = "get_infomation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfomation(HttpServletRequest request) {
        //User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iUserService.getUserInfo(user.getId());
    }
}

