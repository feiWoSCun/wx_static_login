package com.fei.wxserver.controller;

import com.alibaba.fastjson.JSON;
import com.fei.common.constant.RedisKey;
import com.fei.common.domain.User;
import com.fei.common.domain.WxCode;
import com.fei.common.domain.vo.response.Result;
import com.fei.common.util.FileUtil;
import com.fei.common.util.redis.RedisUtil;
import com.fei.wxserver.domain.WxProperties;
import com.fei.wxserver.servive.LoginService;
import com.fei.wxserver.util.NetWorkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * @description: 登陆相关
 * @author: feiWoSCun
 * @Time: 2023/7/15
 * @Email: 2825097536@qq.com
 */
@RestController()
@RequestMapping("/wx")
public class LoginController {
    @Autowired
    WxProperties properties;

    @Autowired
    LoginService loginService;

    /**
     * 利用api生成带固定参数的二维码
     * 订阅号没有权限获得这个接口，妈的白忙活
     */
    @GetMapping("/getQrCode")
    public String getQrcode(@RequestParam("sceneStr") String sceneStr) {
        WxCode wxCode = new WxCode.TemporaryWxBuilder().build(sceneStr);
        String s = JSON.toJSONString(wxCode);
        final String url = properties.getCodeUrl() + properties.getToken();
        String post = new NetWorkUtil().doPost(url, s, "UTF-8");
        System.out.println(post);
        return post;

    }

    @RequestMapping("/getStaticCode")
    public void getStaticCode() {
        try {
            FileUtil.getStaticFile("feiwoscun.jpg");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/getValidation")
    public Result getValidationId() {
        User user = loginService.getUuid();
        return Result.success(user);
    }

    @RequestMapping("/getLoginStatus")
    public Result getLoginStatus(@RequestParam(value = "uuid",required = false) String uuid, @RequestParam(value = "code",required = false) String code) {
        User member = getMember(uuid, code);
        if (member == null) {
            //按道理来说不会出现null的情况，如果出现，代表有多个线程，一个线程删除了还没来得及更新另一个线程就来访问
            //所以这里尝试重试
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            member = getMember(uuid, code);
        }
        return Result.success(member);
    }
    public User getMember(String uuid, String code) {
        User member = RedisUtil.getSetMember(User.class, RedisKey.USER_LOGIN_KEY, (t) -> {
            User user = null;
            for (String s : t) {
                user = JSON.parseObject(s, User.class);
                if (user.getUuid().equals(uuid) || code.equals(user.getCode())) {
                    break;
                }
                ;
                user = null;
            }
            return user;
        });
        return member;
    }
}
