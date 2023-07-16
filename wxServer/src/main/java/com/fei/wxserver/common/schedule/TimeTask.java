package com.fei.wxserver.common.schedule;

import com.alibaba.fastjson.JSON;
import com.fei.common.constant.RedisKey;
import com.fei.common.domain.User;
import com.fei.common.util.redis.RedisUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/16
 * @Email: 2825097536@qq.com
 */
@EnableScheduling
public class TimeTask {
    /**
     * 删除注册的用户 注册的用户多了就会导致缓存太多，所以要定期清理
     */
    @Async
    @Scheduled(cron = "* 25 * * * ?")
    public void deleteRegisterUser() {
        Set<String> setMembers = RedisUtil.getSetMember(RedisKey.USER_LOGIN_KEY);
        boolean ifDelete = ifDelete(setMembers);
        if (ifDelete) {
            RedisUtil.delete(RedisKey.USER_LOGIN_KEY);
        } else {
            //可能有人正在注册，等2.5秒重试
            try {
                Thread.sleep(2500);
                ifDelete = ifDelete(setMembers);
                if (ifDelete) {
                    RedisUtil.delete(RedisKey.USER_LOGIN_KEY);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean ifDelete(Set<String> setMembers) {
        boolean ifDelete = true;
        if (setMembers != null) {
            for (String s : setMembers) {
                User user = JSON.parseObject(s, User.class);
                //有人还要注册，返回false
                if (user.getToken() == null) {
                    ifDelete = false;
                    break;
                }
            }
        }
        return ifDelete;
    }
}
