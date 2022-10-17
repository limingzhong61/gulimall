package com.codeofli.gulimall.ware.service;

import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.ware.entity.WareInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author codeofli
 * @email codeofli@gmail.com
 * @date 2022-10-08 09:59:40
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

