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
public class WxProperties {
    private String appId;
    private String appSecret;
    private String token;
    private String url;
    private String codeUrl;
    private String identifyToken;

    public String getIdentifyToken() {
        return identifyToken;
    }

    public void setIdentifyToken(String identifyToken) {
        this.identifyToken = identifyToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
