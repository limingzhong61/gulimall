package com.codeofli.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeofli.gulimall.product.entity.AttrEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author codeofli
 * @email codeofli@gmail.com
 * @date 2022-10-01 21:08:49
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectSearchAttrIds(@Param("attrIds") List<Long> attrIds);
}
