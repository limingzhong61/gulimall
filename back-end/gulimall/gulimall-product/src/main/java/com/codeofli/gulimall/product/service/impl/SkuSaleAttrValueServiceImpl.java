package com.codeofli.gulimall.product.service.impl;

import com.codeofli.common.utils.PageUtils;
import com.codeofli.common.utils.Query;
import com.codeofli.gulimall.product.dao.SkuSaleAttrValueDao;
import com.codeofli.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.codeofli.gulimall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codeofli.gulimall.product.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> listSaleAttrs(Long spuId) {
        return baseMapper.listSaleAttrs(spuId);
    }

    @Override
    public List<String> getSkuSaleAttrValuesAsString(Long skuId) {
        return baseMapper.getSkuSaleAttrValuesAsString(skuId);
    }

}