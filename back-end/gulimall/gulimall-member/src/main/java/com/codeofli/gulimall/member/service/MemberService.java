package com.codeofli.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codeofli.common.utils.PageUtils;
import com.codeofli.gulimall.member.entity.MemberEntity;
import com.codeofli.gulimall.member.vo.MemberLoginVo;
import com.codeofli.gulimall.member.vo.MemberRegisterVo;
import com.codeofli.gulimall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author codeofli
 * @email 1162314270@qq.com
 * @date 2022-05-15 19:53:22
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVo registerVo);

    MemberEntity login(MemberLoginVo loginVo);

    MemberEntity login(SocialUser socialUser);
}

