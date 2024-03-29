package com.codeofli.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeofli.gulimall.product.entity.CommentReplayEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author codeofli
 * @email codeofli@gmail.com
 * @date 2022-10-01 21:08:48
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
