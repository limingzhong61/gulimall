package com.codeofli.gulimall.product.feign;

import com.codeofli.common.to.es.SkuEsModel;
import com.codeofli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping("/product")
    R saveProductAsIndices(@RequestBody List<SkuEsModel> skuEsModels);
}
