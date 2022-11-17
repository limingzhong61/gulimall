package com.codeofli.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

//import com.codeofli.gulimall.member.feign.CouponFeignService;
import com.codeofli.common.exception.BizCodeEnum;
import com.codeofli.gulimall.member.exception.PhoneNumExistException;
import com.codeofli.gulimall.member.exception.UserNameExistException;
import com.codeofli.gulimall.member.vo.MemberLoginVo;
import com.codeofli.gulimall.member.vo.MemberRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeofli.gulimall.member.entity.MemberEntity;
import com.codeofli.gulimall.member.service.MemberService;
import com.codeofli.common.utils.PageUtils;
import com.codeofli.common.utils.R;



/**
 * 会员
 *
 * @author codeofli
 * @email 1162314270@qq.com
 * @date 2022-05-15 19:53:22
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    //@Autowired
    //CouponFeignService couponFeignService;

    //@RequestMapping("/coupons")
    //public R test(){
    //    MemberEntity memberEntity = new MemberEntity();
    //    memberEntity.setNickname("张三");
    //
    //    R membercoupons = couponFeignService.membercoupons();
    //    return R.ok().put("member",memberEntity).put("coupons",membercoupons.get("coupons"));
    //}

    /**
     * 列表
     */
    @RequestMapping("/list")
        public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
        public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
        public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
        public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 注册会员
     * @return
     */
    @RequestMapping("/register")
    public R register(@RequestBody MemberRegisterVo registerVo) {
        try {
            memberService.register(registerVo);
        } catch (UserNameExistException userException) {
            return R.error(BizCodeEnum.USER_EXIST_EXCEPTION.getCode(), BizCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        } catch (PhoneNumExistException phoneException) {
            return R.error(BizCodeEnum.PHONE_EXIST_EXCEPTION.getCode(), BizCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    @RequestMapping("/login")
    public R login(@RequestBody MemberLoginVo loginVo) {
        MemberEntity entity=memberService.login(loginVo);
        if (entity!=null){
            return R.ok().put("memberEntity",entity);
        }else {
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(), BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMsg());
        }
    }

}
