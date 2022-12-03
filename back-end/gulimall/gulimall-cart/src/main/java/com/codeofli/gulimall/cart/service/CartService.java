package com.codeofli.gulimall.cart.service;

import com.codeofli.gulimall.cart.vo.CartItemVo;
import com.codeofli.gulimall.cart.vo.CartVo;

import java.util.List;

public interface CartService {
    /**
     * 将商品添加到购物车
     * @return
     */
    CartItemVo addCartItem(Long skuId, Integer num);

    /**
     * 获取购物车中某个购物项
     * @param skuId
     * @return
     */
    CartItemVo getCartItem(Long skuId);

    /**
     * 获取整个购物车
     * @return
     */
    CartVo getCart();


    void checkCart(Long skuId, Integer isChecked);

    void changeItemCount(Long skuId, Integer num);

    void deleteItem(Long skuId);

    List<CartItemVo> getCurrentUserCheckedItems();
}
