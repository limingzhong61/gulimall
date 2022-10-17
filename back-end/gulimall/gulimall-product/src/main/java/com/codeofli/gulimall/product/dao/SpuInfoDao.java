package com.codeofli.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeofli.gulimall.product.entity.SpuInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author codeofli
 * @email codeofli@gmail.com
 * @date 2022-10-01 21:08:49
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void upSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
