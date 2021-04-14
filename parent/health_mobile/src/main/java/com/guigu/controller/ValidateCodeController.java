package com.guigu.controller;


import com.aliyuncs.exceptions.ClientException;
import com.health.constant.MessageConstant;
import com.health.constant.RedisConstant;
import com.health.constant.RedisMessageConstant;
import com.health.entity.Result;
import com.health.utils.SMSUtils;
import com.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    //发送验证码
    @RequestMapping("/send4Order.do")
    public Result send4Order(String telephone)  {
        //随机生成验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);

        //给用户发验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存金redis(5分钟)
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300, validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
    @RequestMapping("/send4Login.do")
    public Result send4Login(String telephone)  {
        //随机生成验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);

        //给用户发验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存金redis(5分钟)
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300, validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
