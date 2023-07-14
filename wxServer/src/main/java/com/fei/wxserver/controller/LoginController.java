package com.fei.wxserver.controller;

import com.alibaba.fastjson.JSON;
import com.fei.common.domain.WxCode;
import com.fei.common.util.FileUtil;
import com.fei.wxserver.domain.WxProperties;
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

    public String validationId() {


        return null;

    }
}
