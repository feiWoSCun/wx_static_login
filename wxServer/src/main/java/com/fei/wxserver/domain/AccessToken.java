package com.fei.wxserver.domain;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/14
 * @Email: 2825097536@qq.com
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: sgw
 * Date 2019/4/5
 * Description:保存微信返回的access_token值
 **/
@ConfigurationProperties
public class AccessToken {
    /**
     * 获取到的凭证
     */
    private String tokenName;
    /**
     * 凭证有效时间  单位:秒
     */
    private int expireSecond;

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
       // System.out.println(tokenName);
        this.tokenName = tokenName;
    }

    public int getExpireSecond() {
        return expireSecond;
    }

    public void setExpireSecond(int expireSecond) {
        this.expireSecond = expireSecond;
    }
}
