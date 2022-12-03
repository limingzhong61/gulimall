package com.codeofli.gulimall.cart.feign.controller;

import com.codeofli.gulimall.cart.interceptor.CartInterceptor;
import com.codeofli.gulimall.cart.service.CartService;
import com.codeofli.gulimall.cart.to.UserInfoTo;
import com.codeofli.gulimall.cart.vo.CartItemVo;
import com.codeofli.gulimall.cart.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 浏览器有一个cookie; user-key;标识用户身份,一个月后过期;如
     * 果第一次使用jd的购物车功能,都会给一个临时的用户身份;
     * 浏览器以后保存，每次访间都会带上这个cookie;
     *
     * 登录:session有
     * 没登录:按照cookie里面带来user-key来做。
     * 第一次:如果没有临时用户,帮忙创建一个临时用户。
     * @param model
     * @return
     */
    @RequestMapping("/cart.html")
    public String getCartList(Model model) {
        //1、快速得到用户信息, id, user-key
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();

        CartVo cartVo=cartService.getCart();
        model.addAttribute("cart", cartVo);
        return "cartList";
    }

    @RequestMapping("/success.html")
    public String success() {
        return "success";
    }

    /**
     * 添加商品到购物车
     * RedirectAttributes.addFlashAttribute():将数据放在session中，可以在页面中取出，但是只能取一次
     * RedirectAttributes.addAttribute():将数据放在url后面
     * @return
     */
    @RequestMapping("/addCartItem")
    public String addCartItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num, RedirectAttributes attributes) {
        cartService.addCartItem(skuId, num);
        attributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.gulimall.com/addCartItemSuccess";
    }

    @RequestMapping("/addCartItemSuccess")
    public String addCartItemSuccess(@RequestParam("skuId") Long skuId,Model model) {
        CartItemVo cartItemVo = cartService.getCartItem(skuId);
        model.addAttribute("cartItem", cartItemVo);
        return "success";
    }


    @RequestMapping("/checkCart")
    public String checkCart(@RequestParam("isChecked") Integer isChecked,@RequestParam("skuId")Long skuId) {
        cartService.checkCart(skuId, isChecked);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    @RequestMapping("/countItem")
    public String changeItemCount(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.changeItemCount(skuId, num);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    @RequestMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId) {
        cartService.deleteItem(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    @ResponseBody
    @RequestMapping("/getCurrentUserCheckedItems")
    public List<CartItemVo> getCurrentUserCheckedItems() {
        return cartService.getCurrentUserCheckedItems();
    }

}
