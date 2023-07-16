package com.fei.wxserver.servive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fei.wxserver.domain.WxProperties;
import com.fei.wxserver.util.NetWorkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/14
 * @Email: 2825097536@qq.com
 * <p>
 * <p>
 * import com.alibaba.fastjson.JSON;
 * import com.alibaba.fastjson.JSONObject;
 * import com.example.maltose.wexin.domain.AccessToken;
 * import com.example.maltose.wexin.domain.AccessTokenInfo;
 * import com.example.maltose.wexin.utils.NetWorkUtil;
 * import org.springframework.boot.ApplicationArguments;
 * import org.springframework.boot.ApplicationRunner;
 * import org.springframework.core.annotation.Order;
 * import org.springframework.stereotype.Component;
 * <p>
 * <p>
 * <p>
 * /**
 * Author: sgw
 * Date 2019/4/5
 * Description: 默认启动项目的时候就启动该类，用来向微信后台定期获取access_token值
 * 继承ApplicationRunner接口的话，项目启动时就会执行里边的run方法
 **/

//@Order定义组件加载顺序
@Order(value = 1)
@EnableConfigurationProperties(WxProperties.class)
@Slf4j
@Component
@EnableScheduling
public class GetToken implements ApplicationRunner {
    @Autowired
    private WxProperties wxProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始获取微信里的access_token");
        getToken();

    }


    @Scheduled(fixedDelay = 1000 * 60 * 100)
    public void getToken() {
        //不会有线程问题的，获取accessToken
        getAccessToken(wxProperties.getAppId(), wxProperties.getAppSecret());
    }

    private void getAccessToken(String appId, String appSecret) {
        NetWorkUtil netHelper = new NetWorkUtil();
        /**
         * 接口地址为https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET，其中grant_type固定写为client_credential即可。
         */
        String Url = String.format(wxProperties.getUrl(), appId, appSecret);
        //此请求为https的get请求，返回的数据格式为{"access_token":"ACCESS_TOKEN","expires_in":7200}
        String result = netHelper.getHttpsResponse(Url, "");
        System.out.println("获取到的access_token=" + result);
        //使用FastJson将Json字符串解析成Json对象
        JSONObject json = JSON.parseObject(result);
        String accessToken = json.getString("access_token");
        System.out.println(accessToken);
        wxProperties.setToken(accessToken);
    }

    /**
     * 参数排序
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public  String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 字符串进行shal加密
     *
     * @param str
     * @return
     */
    public  String shal(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
