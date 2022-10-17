package com.codeofli.gulimall.search.controller;

import com.codeofli.common.exception.BizCodeEnum;
import com.codeofli.common.to.es.SkuEsModel;
import com.codeofli.common.utils.R;
import com.codeofli.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ElasticSaveController {
    @Autowired
    private ProductSaveService productSaveService;
    // 上架商品
    @PostMapping("/product")
    public R saveProductAsIndices(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean status = false;
        try {
            status=productSaveService.saveProductAsIndices(skuEsModels);
        } catch (Exception e) {
            log.error("远程保存索引失败");
        }
        if (status){
            return R.ok();
        }else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

    }
}
