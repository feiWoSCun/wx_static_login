package com.fei.common.util.redis;

import com.fei.common.util.SpringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.function.Function;

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

    public static void deleteSet(String key, String set) {
        redisTemplate.opsForSet().remove(key, set);
    }

    public static Long setMember(String key, String obj) {
        return redisTemplate.opsForSet().add(key, obj);
    }

    public static <T> T getSetMember(Class<T> tClass, String key, Function<Set<String>, T> function) {
        Set<String> members = redisTemplate.opsForSet().members(key);
        T apply = function.apply(members);
        return apply;
    }

    public static Set<String> getSetMember(String key) {
        Set<String> members = redisTemplate.opsForSet().members(key);

        return members;
    }

    public static boolean delete(String key) {
        Boolean delete = redisTemplate.delete(key);
         if(delete==null){
             throw new RuntimeException("delete false because return is null");
         }
         return delete;
    }

    public static void popUser() {
        //redisTemplate.opsForSet()
    }

    /*---------------------------------map-----------------------------------------*/
    public static void addMap(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public static void updateMap(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }
}
