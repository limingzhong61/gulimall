package com.codeofli.gulimall.product.feign.fallback;

import com.codeofli.common.exception.BizCodeEnum;
import com.codeofli.common.utils.R;
import com.codeofli.gulimall.product.feign.SeckillFeignService;
import org.springframework.stereotype.Component;

@Component
public class SeckillFallbackService implements SeckillFeignService {
    @Override
    public R getSeckillSkuInfo(Long skuId) {
        return R.error(BizCodeEnum.READ_TIME_OUT_EXCEPTION.getCode(), BizCodeEnum.READ_TIME_OUT_EXCEPTION.getMsg());
    }
}
