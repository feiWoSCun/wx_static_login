package com.fei.common.util.redis;

import com.fei.common.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/15
 * @Email: 2825097536@qq.com
 */

public class RedisUtil {
    private static StringRedisTemplate redisTemplate;

    static {
        StringRedisTemplate bean = SpringUtil.getBean(StringRedisTemplate.class);
        redisTemplate = bean;
        System.out.println(bean);
        System.out.println("22142345467");

    }


    public static boolean isMember(String key, Object obj) {
        Boolean member = redisTemplate.opsForSet().isMember(key, obj);
        //npe
        if (member == null) {
            throw new RuntimeException("member is null");
        }
        return member;
    }

    public static Long setMember(String key, String obj) {
        return redisTemplate.opsForSet().add(key, obj);
    }
}
