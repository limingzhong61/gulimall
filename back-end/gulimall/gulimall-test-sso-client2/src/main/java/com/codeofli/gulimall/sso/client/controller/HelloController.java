package com.codeofli.gulimall.sso.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {
    @Value("${sso.server.url}")
    private  String ssoServerUrl;
    /**
     * 无需登录就可以访问
     */
    @ResponseBody
    @GetMapping("/hello")
    public  String hello(){
        return "hello";
    }

    /**
     * 感知这次是在ssoserver登录成功跳回来的
     * @param model
     * @param session
     * @param token 只要去ssoserver登录成功跳回来就会带上
     * @return
     */
    @GetMapping("/employees")
    public  String employees(Model model, HttpSession session,
                             @RequestParam(value = "token",required = false) String token){
        if(!StringUtils.isEmpty(token)){
            //去ssoserver登录成功跳回来就会带上
            //TODO 1.去ssoserver获取当前token真正对应的用户信息
            session.setAttribute("loginUser","zhangsan");
        }

        Object loginUser = session.getAttribute("loginUser");
        if(loginUser == null){
            //没有登录，跳转到登录服务器进行登录

            //跳转过去以后，使用url上的插叙参数标识我们自己是哪个页面
            return "redirect:" + ssoServerUrl +"?redirect_url=http://client1.com:8081/employees";
        }else{
            List<String> emps = new ArrayList<>();
            emps.add("张三");
            emps.add("李四");

            model.addAttribute("emps",emps);
        }

        return "list";
    }
}
