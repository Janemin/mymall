package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Jane on 2018/1/19.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping(value = "/{productId}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> detailRestful(@PathVariable Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return iProductService.getProductByKeywordCategory(keyword,categoryId,orderBy,pageNum,pageSize);
    }

    @RequestMapping(value = "/{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRestfulBadCase(@PathVariable(value = "keyword")String keyword,
                                         @PathVariable(value = "categoryId")Integer categoryId,
                                         @PathVariable(value = "pageNum")Integer pageNum,
                                         @PathVariable(value = "pageSize")Integer pageSize,
                                         @PathVariable(value = "orderBy")String orderBy) {
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 1;
        }
        if(orderBy == null){
            orderBy = "price_asc";
        }

        return iProductService.getProductByKeywordCategory(keyword, categoryId,orderBy, pageNum, pageSize);
    }

    @RequestMapping(value = "/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRestfulBadCase(@PathVariable(value = "categoryId")Integer categoryId,
                                                @PathVariable(value = "pageNum")Integer pageNum,
                                                @PathVariable(value = "pageSize")Integer pageSize,
                                                @PathVariable(value = "orderBy")String orderBy) {
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 1;
        }
        if(orderBy == null){
            orderBy = "price_asc";
        }

        return iProductService.getProductByKeywordCategory("", null,orderBy, pageNum, pageSize);
    }

    @RequestMapping(value = "/{keyword}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRestfulBadCase(@PathVariable(value = "keyword")String keyword,
                                                @PathVariable(value = "pageNum")Integer pageNum,
                                                @PathVariable(value = "pageSize")Integer pageSize,
                                                @PathVariable(value = "orderBy")String orderBy) {
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 1;
        }
        if(orderBy == null){
            orderBy = "price_asc";
        }

        return iProductService.getProductByKeywordCategory(keyword, null,orderBy, pageNum, pageSize);
    }

    @RequestMapping(value = "/keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRestful(@PathVariable(value = "keyword")String keyword,
                                                       @PathVariable(value = "pageNum")Integer pageNum,
                                                       @PathVariable(value = "pageSize")Integer pageSize,
                                                       @PathVariable(value = "orderBy")String orderBy) {
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 1;
        }
        if(orderBy == null){
            orderBy = "price_asc";
        }

        return iProductService.getProductByKeywordCategory(keyword, null,orderBy, pageNum, pageSize);
    }

    @RequestMapping(value = "/category/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRestful(@PathVariable(value = "categoryId")Integer categoryId,
                                                       @PathVariable(value = "pageNum")Integer pageNum,
                                                       @PathVariable(value = "pageSize")Integer pageSize,
                                                       @PathVariable(value = "orderBy")String orderBy) {
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 1;
        }
        if(orderBy == null){
            orderBy = "price_asc";
        }

        return iProductService.getProductByKeywordCategory("", null,orderBy, pageNum, pageSize);
    }
}
