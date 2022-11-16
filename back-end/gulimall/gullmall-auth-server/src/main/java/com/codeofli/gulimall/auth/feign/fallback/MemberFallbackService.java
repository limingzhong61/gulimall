package com.codeofli.gulimall.auth.feign.fallback;

import com.codeofli.common.exception.BizCodeEnum;
import com.codeofli.common.utils.R;
import com.codeofli.gulimall.auth.feign.MemberFeignService;
import com.codeofli.gulimall.auth.vo.SocialUser;
import com.codeofli.gulimall.auth.vo.UserLoginVo;
import com.codeofli.gulimall.auth.vo.UserRegisterVo;
import org.springframework.stereotype.Service;

@Service
public class MemberFallbackService implements MemberFeignService {
    @Override
    public R register(UserRegisterVo registerVo) {
        return R.error(BizCodeEnum.READ_TIME_OUT_EXCEPTION.getCode(), BizCodeEnum.READ_TIME_OUT_EXCEPTION.getMsg());
    }

    @Override
    public R login(UserLoginVo loginVo) {
        return R.error(BizCodeEnum.READ_TIME_OUT_EXCEPTION.getCode(), BizCodeEnum.READ_TIME_OUT_EXCEPTION.getMsg());
    }

    @Override
    public R login(SocialUser socialUser) {
        return R.error(BizCodeEnum.READ_TIME_OUT_EXCEPTION.getCode(), BizCodeEnum.READ_TIME_OUT_EXCEPTION.getMsg());
    }
}
