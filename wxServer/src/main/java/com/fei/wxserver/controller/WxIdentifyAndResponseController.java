package com.fei.wxserver.controller;

import com.fei.common.util.ServletUtil;
import com.fei.wxserver.domain.WxProperties;
import com.fei.wxserver.servive.GetToken;
import com.fei.wxserver.util.MessageHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * 用于验证和回复消息
 *
 * @author 28250
 */
@RequestMapping("wx")
@RestController
@Slf4j
public class WxIdentifyAndResponseController {

 @Autowired
    WxProperties wxProperties;

    @Autowired
    private GetToken getToken;

    @RequestMapping("testToken")
    public void checkSignature(@RequestParam(value = "signature", required = false) String signature,
                               @RequestParam(value = "timestamp", required = false) String timestamp,
                               @RequestParam(value = "nonce", required = false) String nonce,
                               @RequestParam(value = "echostr", required = false) String echostr
    ) {
        log.info("校验签名start");
        String result = "校验参数失败";
        String method = ServletUtil.getRequest().getMethod();
        try {
            //没怎么用过switch（也没玩过/(ㄒoㄒ)/~~），感觉switch可读性更高?
            switch (method) {
                //get请求，说明是在配置微信后台的url过来的请求
                case "GET":
                    String sortStr = getToken.sort(wxProperties.getIdentifyToken(), timestamp, nonce);
                    String mySignature = getToken.shal(sortStr);
                    if (!"".equals(signature) && !"".equals(mySignature) && signature.equals(mySignature)) {
                        ServletUtil.getResponse().getWriter().write(echostr);
                    }
                    break;
                default:
                    //post请求，说明是微信公众号里来的请求
                    Map<String, String> map = MessageHandlerUtils.getMsgFromClient();
                    log.info("开始构造消息");
                    result = MessageHandlerUtils.buildXml(map);
                    ServletUtil.getResponse().getWriter().write(result.replace(" ", ""));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
