package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jane on 2018/1/21.
 */

@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;


    @RequestMapping(value = "create.do")
    @ResponseBody
    public ServerResponse create(HttpServletRequest request, Integer shippingId) {
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(), shippingId);
    }

    @RequestMapping(value = "cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpServletRequest request, Long orderNo) {
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(), orderNo);
    }

    @RequestMapping(value = "get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpServletRequest request) {
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping(value = "detail.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest request, Long orderNo) {
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse list(HttpServletRequest request,
                               @RequestParam(value = "pageNum" , defaultValue = "1")int pageNum,
                               @RequestParam(value = "pageSize" , defaultValue = "10")int pageSize){
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }

    @RequestMapping(value = "pay.do")
    @ResponseBody
    public ServerResponse pay(Long orderNo, HttpServletRequest request) {
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    @RequestMapping(value = "alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String)iter.next();
            String[] values = (String[])requestParams.get(name);
            String valueStr = "";
            for(int i = 0; i< values.length; i++) {
                valueStr = (i == values.length -1)?valueStr + values[i] : valueStr + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调，sign:{}，trade_status:{}, 参数:{}",
                params.get("sign"), params.get("trade_status"), params.toString());

        //验证回调的正确性，避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(
                    params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求，验证不通过");
            }

        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
            e.printStackTrace();
        }

        //todo 验证各种数据

        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping(value = "query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpServletRequest request, Long orderNo) {
        //User user = (User)session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.stringToObject(userJsonStr, User.class);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }
}
