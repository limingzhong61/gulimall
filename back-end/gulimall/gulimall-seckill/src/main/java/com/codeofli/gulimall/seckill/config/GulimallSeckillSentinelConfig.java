package com.codeofli.gulimall.seckill.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.codeofli.common.exception.BizCodeEnum;
import com.codeofli.common.utils.R;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 自定义阻塞返回方法
 * @Created: with IntelliJ IDEA.
 * @createTime: 2020-07-13 11:30
 **/

@Configuration
public class GulimallSeckillSentinelConfig implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        R error = R.error(BizCodeEnum.TO_MANY_REQUEST.getCode(), BizCodeEnum.TO_MANY_REQUEST.getMsg());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(error));
    }
}
