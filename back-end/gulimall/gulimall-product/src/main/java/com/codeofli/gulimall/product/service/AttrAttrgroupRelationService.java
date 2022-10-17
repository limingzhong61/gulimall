package com.codeofli.gulimall.product.service;

import com.codeofli.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.codeofli.gulimall.product.vo.AttrGroupRelationVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.codeofli.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author codeofli
 * @email codeofli@gmail.com
 * @date 2022-10-01 21:08:49
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBatch(List<AttrGroupRelationVo> vos);

}

