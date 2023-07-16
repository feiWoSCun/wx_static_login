package com.fei.wxserver.servive;

import com.alibaba.fastjson.JSON;
import com.fei.common.constant.WxMessageType;
import com.fei.common.domain.User;
import com.fei.common.util.redis.RedisUtil;
import com.fei.common.constant.RedisKey;
import com.fei.wxserver.util.MessageHandlerUtils;
import com.fei.wxserver.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
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
     *
     * @return
     */
    public User getUuid() {
        String uuid = UUID.randomUUID().toString();
        String code = uuid.substring(0, 4);
        while (RedisUtil.isMember(RedisKey.USER_LOGIN_KEY, code)) {
            code = UUID.randomUUID().toString().substring(0, 4);
        }
        User user = User.builder().uuid(uuid).code(code).build();
        Long aLong = RedisUtil.setMember(RedisKey.USER_LOGIN_KEY, JSON.toJSONString(user));
        if (aLong == null) {
            throw new RuntimeException("redis value is null");
        }
        return user;
    }

    public String login() throws Exception {
        String result = "没有bug，登录成功!";
        Map<String, String> map = MessageHandlerUtils.getMsgFromClient();
        String msgType = map.get("MsgType").toString();

        if (WxMessageType.TYRE.getType().equals(msgType)) {
            //todo: 判断是不是登录id
            String content = map.get("Content");
            //用户输入是否正确
            User oldUser = getUnLoginUser(content);
            boolean ifContent = RedisUtil.isMember(RedisKey.USER_LOGIN_KEY, JSON.toJSONString(oldUser));
            //todo: 为用户进行登录
            if (ifContent) {
                //更新用户
                String openId = map.get("FromUserName");
                User newUser = updateRedisUser(oldUser, openId);
                //绑定token和openid
                RedisUtil.addMap(RedisKey.OPENID_TOKEN, newUser.getOpenId(), newUser.getToken());
            } else {
                result = "验证码错误，请仔细核对";
            }
        } else {
            result = "我是智障，只能看懂文字！";
        }
        return MessageHandlerUtils.buildTextMessage(map, result);

    }

    /**
     * 解析user
     *
     * @param content
     * @return
     */
    private User getUnLoginUser(String content) {
        return RedisUtil.getSetMember(User.class, RedisKey.USER_LOGIN_KEY, (t) -> {
            User user = null;
            for (String s : t) {
                user = JSON.parseObject(s, User.class);
                if (user.getCode().equals(content)) {
                    break;
                }
            }
            return user;
        });
    }

    private User updateRedisUser(User user, String openId) {
        RedisUtil.deleteSet(RedisKey.USER_LOGIN_KEY, JSON.toJSONString(user));
        user.setOpenId(openId);
        user.setToken(TokenUtil.generateToken(user));
        RedisUtil.setMember(RedisKey.USER_LOGIN_KEY, JSON.toJSONString(user));
        User newUser = user;
        return newUser;
    }
}
