package com.it.controller;

import com.it.entity.ShopCart;
import com.it.service.ShopCartService;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 〈购物车接口实列接口〉<br>
 *
 * @author
 */
@Controller
@RequestMapping("/shopCart")
public class ShopCartController {
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private ItdragonUtils itdragonUtils;

    /**
     * 添加购物车
     *
     * @param shopCart
     * @return
     */
    @ResponseBody
    @PostMapping("/shopCart.do")
    public ResultResponse insert(ShopCart shopCart) {
        boolean result = shopCartService.insert(shopCart);
        if (!result) {
            return Result.resuleError("添加失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除购物车
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/shopCart.do")
    public ResultResponse delete(String id) {
        boolean result = shopCartService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}