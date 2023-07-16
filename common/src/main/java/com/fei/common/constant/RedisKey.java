package com.fei.common.constant;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/16
 * @Email: 2825097536@qq.com
 */
public interface RedisKey {
    /**
     * 用户登录用于公众号验证随机数的参数
     */
    static final String USER_LOGIN_KEY = "user_login";
    /**
     * 绑定用户的openid和token
     */
    static final String USER_OPENID = "user_openId";
    /**
     * 绑定用户的openId和token
     */
    static final String OPENID_TOKEN = "openId_token";

}
