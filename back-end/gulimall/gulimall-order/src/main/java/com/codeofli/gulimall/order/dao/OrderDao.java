package com.codeofli.gulimall.order.dao;

import com.codeofli.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author codeofli
 * @email 1162314270@qq.com
 * @date 2022-05-15 20:02:08
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
