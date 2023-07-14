package com.fei.common.util.redis;

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
@Component
public class RedisUtil implements ApplicationRunner {
    private static StringRedisTemplate redisTemplate;

    @Autowired
    public RedisUtil(StringRedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println();
    }

    public static boolean isMember(String key, Object obj) {
        Boolean member = redisTemplate.opsForSet().isMember(key, obj);
        //npe
        if (member == null) {
            throw new RuntimeException("member is null");
        }
        return member;
    }

    public static void setMember(String key, String obj) {
        //redisTemplate.opsForSet().add(key, obj);


    }
}
