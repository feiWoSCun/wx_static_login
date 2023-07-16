package com.fei.wxserver.servive;

import com.fei.common.util.redis.RedisUtil;
import com.fei.wxserver.common.constant.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/15
 * @Email: 2825097536@qq.com
 */
@Component
@Slf4j
public class LoginService {

    /**
     * 添加登录的验证码
     * @return
     */
    public String getUuid() {
        String loginId = UUID.randomUUID().toString().substring(0, 4);
        while (RedisUtil.isMember(RedisKey.USER_LOGIN_KEY, loginId)) {
            loginId = UUID.randomUUID().toString().substring(0, 4);
        }

        Long aLong = RedisUtil.setMember(RedisKey.USER_LOGIN_KEY, loginId);
        if (aLong == null) {
            throw new RuntimeException("redis value is null");
        }
        return loginId;
    }


}
