package com.codeofli.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.member.entity.IntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author codeofli
 * @email 1162314270@qq.com
 * @date 2022-05-15 19:53:22
 */
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

