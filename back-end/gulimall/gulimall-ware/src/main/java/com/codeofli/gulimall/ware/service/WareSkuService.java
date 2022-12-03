package com.codeofli.gulimall.ware.service;

import com.codeofli.common.to.SkuHasStockVo;
import com.codeofli.common.to.mq.OrderTo;
import com.codeofli.common.to.mq.StockLockedTo;
import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.codeofli.gulimall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author codeofli
 * @email codeofli@gmail.com
 * @date 2022-10-08 09:59:40
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStocks(List<Long> ids);

    Boolean orderLockStock(WareSkuLockVo lockVo);

    void unlock(StockLockedTo stockLockedTo);

    void unlock(OrderTo orderTo);
}

