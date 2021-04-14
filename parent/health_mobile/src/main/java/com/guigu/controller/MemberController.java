package com.guigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.http.HttpResponse;
import com.guigu.service.MemberService;
import com.health.constant.MessageConstant;
import com.health.constant.RedisMessageConstant;
import com.health.entity.Result;
import com.health.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {


    //注入service方法
    @Autowired
    private JedisPool jedisPool;

    @Reference
        private MemberService memberService;

    //快速登录
    @RequestMapping("/login")
    public Result login(@RequestBody HttpServletResponse response, Map map){

        String telephone = jedisPool.getResource().get("telephone");

        String validataCode = (String) map.get("validataCode");

        //从Redis中获取保存的验证码
        String validataCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (telephone!=null && validataCode!=null && validataCode.equals(validataCodeRedis)){
            Member member = memberService.findByTelephone(telephone);
                if (member==null){
                    //不是会员自动完成注册
                    member.setRegTime(new Date());
                    member.setPhoneNumber(telephone);
                     memberService.add(member);

                }
            //把客户端写入Cookie
            Cookie cookie=new Cookie("login_member_telephone",telephone);
                cookie.setPath("/");    //路径
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            //将会员信息写入reids
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else{
            return new Result(true, MessageConstant.VALIDATECODE_ERROR);
        }

    }

}
