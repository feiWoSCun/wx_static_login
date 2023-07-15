package com.fei.wxserver.controller;

import com.fei.common.util.ServletUtil;
import com.fei.wxserver.util.MessageHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: sgw
 * @Date 2019/4/5
 * @Description: 公众号入口
 **/
@RequestMapping("wx")
@Controller
@Slf4j
public class GzhController {

    /**
     * 切记：这里是自定义的token，需和你微信配置界面提交的token完全一致
     */
    private final String TOKEN = "feiwoscun";


    @RequestMapping("testToken")
    public void checkSignature(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
                               @RequestParam("nonce") String nonce, @RequestParam(value = "echostr", required = false) String echostr,
    HttpServletResponse response
    ) {
        log.info("校验签名start");
        String result = "校验参数失败";
        String method = ServletUtil.getRequest().getMethod();
        try {
            switch (method) {
                //get请求，说明是在配置微信后台的url过来的请求
                case "GET":
                    String sortStr = this.sort(TOKEN, timestamp, nonce);
                    String mySignature = shal(sortStr);
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

    /**
     * 参数排序
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 字符串进行shal加密
     *
     * @param str
     * @return
     */
    public String shal(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
