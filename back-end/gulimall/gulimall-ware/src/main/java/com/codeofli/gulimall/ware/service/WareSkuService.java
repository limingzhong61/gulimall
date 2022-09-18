package com.codeofli.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author codeofli
 * @email 1162314270@qq.com
 * @date 2022-05-15 20:13:23
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

