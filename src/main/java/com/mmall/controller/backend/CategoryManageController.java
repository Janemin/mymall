package com.mmall.controller.backend;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jane on 2018/1/18.
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;


    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpServletRequest request, String categoryName,
                                      @RequestParam(value="parentId", defaultValue = "0") int parentId) {
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(request);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorCodeMessage(
//                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObject(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(
//                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()) {
//            return iCategoryService.addCategory(categoryName, parentId);
//        }else {
//            return  ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//        }
        return iCategoryService.addCategory(categoryName, parentId);
    }

    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpServletRequest request, String categoryName, Integer categoryId) {
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(request);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorCodeMessage(
//                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObject(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(
//                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()) {
//            return iCategoryService.updateCategory(categoryName, categoryId);
//        }else {
//            return  ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//        }
        return iCategoryService.updateCategory(categoryName, categoryId);
    }

    @RequestMapping(value = "get_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(
            HttpServletRequest request, @RequestParam(value="categoryId", defaultValue = "0")Integer categoryId){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(request);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorCodeMessage(
//                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObject(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(
//                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()) {
//            return iCategoryService.getChildrenParallelCategory(categoryId);
//        }else {
//            return  ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//        }
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCatetory(
            HttpServletRequest request, @RequestParam(value="categoryId", defaultValue = "0")Integer categoryId) {
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(request);
//        if(StringUtils.isEmpty(loginToken)){
//            return ServerResponse.createByErrorCodeMessage(
//                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
//        User user = JsonUtil.stringToObject(userJsonStr, User.class);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(
//                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()) {
//            return iCategoryService.selectCategoryAndChildrenCategory(categoryId);
//        }else {
//            return  ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
//        }
        return iCategoryService.selectCategoryAndChildrenCategory(categoryId);
    }
}
