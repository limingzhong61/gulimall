package com.codeofli.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codeofli.common.to.SkuReductionTo;
import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author codeofli
 * @email 1162314270@qq.com
 * @date 2022-05-15 19:36:09
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo reductionTo);
}

