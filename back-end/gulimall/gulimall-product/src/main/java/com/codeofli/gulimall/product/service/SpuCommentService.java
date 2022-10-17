package com.codeofli.gulimall.product.service;

import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.product.entity.SpuCommentEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 商品评价
 *
 * @author codeofli
 * @email codeofli@gmail.com
 * @date 2022-10-01 21:08:49
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

