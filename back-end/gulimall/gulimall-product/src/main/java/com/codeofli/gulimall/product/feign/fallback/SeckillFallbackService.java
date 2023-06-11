package com.codeofli.gulimall.product.feign.fallback;

import com.codeofli.common.exception.BizCodeEnum;
import com.codeofli.common.utils.R;
import com.codeofli.gulimall.product.feign.SeckillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class SeckillFallbackService implements SeckillFeignService {

    @Override
    public R getSkuSeckilInfo(Long skuId) {
        log.info("熔断方法调用...getSkukillInfo");
        return R.error(BizCodeEnum.READ_TIME_OUT_EXCEPTION.getCode(), BizCodeEnum.READ_TIME_OUT_EXCEPTION.getMsg());

    }
}
