package com.codeofli.gulimall.auth.feign;

import com.codeofli.common.utils.R;
import com.codeofli.gulimall.auth.feign.fallback.MemberFallbackService;
import com.codeofli.gulimall.auth.vo.SocialUser;
import com.codeofli.gulimall.auth.vo.UserLoginVo;
import com.codeofli.gulimall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "gulimall-member",fallback = MemberFallbackService.class)
public interface MemberFeignService {

    @RequestMapping("member/member/register")
    R register(@RequestBody UserRegisterVo registerVo);


    @RequestMapping("member/member/login")
     R login(@RequestBody UserLoginVo loginVo);

    @RequestMapping("member/member/oauth2/login")
    R login(@RequestBody SocialUser socialUser);
}
