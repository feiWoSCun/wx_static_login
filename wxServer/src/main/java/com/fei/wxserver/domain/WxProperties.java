package com.fei.wxserver.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/14
 * @Email: 2825097536@qq.com
 */
@ConfigurationProperties(prefix = "fei.wx", ignoreInvalidFields = true)
@Component
public class WxProperties {
    private String appId;
    private String appSecret;
    public static String token;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        //   System.out.println(appId);
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
