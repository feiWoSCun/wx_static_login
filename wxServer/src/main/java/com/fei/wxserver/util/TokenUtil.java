package com.fei.wxserver.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import com.fei.common.constant.TokenSecret;
import com.fei.common.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/16
 * @Email: 2825097536@qq.com
 */
@Slf4j
public class TokenUtil {


    /**
     * @param user
     * @return
     * @author: feiWoSCun
     * @Time: 2023/3/2
     * @Email: 2825097536@qq.com
     * <p>
     * <p>
     * /**
     * 生成token，自定义过期时间 毫秒
     */
    public static String generateToken(User user) {
        try {

            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TokenSecret.SECRET_TOKEN);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");

            return JWT.create()
                    .withHeader(header)
                    .withClaim("token", JSONObject.toJSONString(user))
                    //.withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("generate token occur error, error is:{}", e);
            return null;
        }
    }

    /**
     * 检验token是否正确
     *
     * @param token
     * @return
     */
    public static User parseToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TokenSecret.SECRET_TOKEN);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        String tokenInfo = jwt.getClaim("token").asString();
        return JSON.parseObject(tokenInfo, User.class);
    }


}
