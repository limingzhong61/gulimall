package com.codeofli.gulimall.ware.service;

import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.ware.entity.PurchaseEntity;
import com.codeofli.gulimall.ware.vo.MergeVo;
import com.codeofli.gulimall.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author codeofli
 * @email codeofli@gmail.com
 * @date 2022-11-17 13:50:10
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils listUnreceive(Map<String, Object> params);

    void mergePurchaseDetail(MergeVo mergeVo);

    void ReceivedPurchase(List<Long> ids);

    void finishPurchase(PurchaseDoneVo purchaseDoneVo);


}

