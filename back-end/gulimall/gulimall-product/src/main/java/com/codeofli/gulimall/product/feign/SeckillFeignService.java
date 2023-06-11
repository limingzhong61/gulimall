package com.codeofli.gulimall.product.feign;

import com.codeofli.common.utils.R;
//import com.codeofli.gulimall.product.feign.fallback.SeckillFallbackService;
import com.codeofli.gulimall.product.feign.fallback.SeckillFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "gulimall-seckill",fallback = SeckillFallbackService.class)
public interface SeckillFeignService {
    /**
     * 根据skuId查询商品是否参加秒杀活动
     * @param skuId
     * @return
     */
    @GetMapping(value = "/sku/seckill/{skuId}")
    R getSkuSeckilInfo(@PathVariable("skuId") Long skuId);
}
