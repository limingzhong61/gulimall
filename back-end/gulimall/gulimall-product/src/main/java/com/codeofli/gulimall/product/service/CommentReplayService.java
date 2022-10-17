package com.codeofli.gulimall.product.service;

import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author codeofli
 * @email codeofli@gmail.com
 * @date 2022-10-01 21:08:48
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

