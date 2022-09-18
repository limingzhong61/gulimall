package com.codeofli.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * 属性分组
 *
 * @author codeofli
 * @email 1162314270@qq.com
 * @date 2022-05-15 18:40:03
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

