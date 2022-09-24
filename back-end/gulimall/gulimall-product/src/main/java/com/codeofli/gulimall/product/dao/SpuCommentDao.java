package com.codeofli.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeofli.gulimall.product.entity.SpuCommentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
@Mapper
public interface SpuCommentDao extends BaseMapper<SpuCommentEntity> {
	
}
