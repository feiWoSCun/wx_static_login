package com.fei.wxserver.util;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/14
 * @Email: 2825097536@qq.com
 */

import com.fei.common.util.ServletUtil;
import com.fei.common.constant.WxMessageType;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: sgw
 * @Date 2019/4/6
 * @Description:处理公众号发来的消息(XML格式) 将解析结果存储在HashMap中
 **/
public class MessageHandlerUtils {
    private static Logger logger = LoggerFactory.getLogger(MessageHandlerUtils.class);

    /**
     * 获取微信公众号里发送过来的消息
     */
    public static Map<String, String> getMsgFromClient() {

        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap();
        InputStream inputStream = null;
        try {
            HttpServletRequest request = ServletUtil.getRequest();
            inputStream = request.getInputStream();
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();
            // 遍历所有子节点,解析打印微信发来的消息
            for (Element e : elementList) {
                logger.info(e.getName() + "|" + e.getText());
                map.put(e.getName(), e.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    /**
     * 根据消息类型 构造返回消息
     */
    public static String buildXml(Map<String, String> map) {
        String result;
        String msgType = map.get("MsgType").toString();
        logger.info("消息类型：" + map.get("MsgType").toString());
        result = buildTextMessage(map, "来了老弟?");
        if (WxMessageType.TYRE.getType().equals(msgType)) {
            //todo: 判断是不是登录id

            //todo: 为用户进行登录
        } else {
            String fromUserName = map.get("FromUserName");
            // 开发者微信号
            String toUserName = map.get("ToUserName");
            result = String
                    .format(
                            "<xml>" +
                                    "<ToUserName><![CDATA[%s]]></ToUserName>" +
                                    "<FromUserName><![CDATA[%s]]></FromUserName>" +
                                    "<CreateTime>%s</CreateTime>" +
                                    "<MsgType><![CDATA[text]]></MsgType>" +
                                    "<Content><![CDATA[%s]]></Content>" +
                                    "</xml>",
                            fromUserName, toUserName, getUtcTime(),
                            "请输入数字用于验证登录");
        }

        return result;
    }

    /**
     * 构造文本消息
     *
     * @param map
     * @param content
     * @return
     */
    private static String buildTextMessage(Map<String, String> map, String content) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 文本消息XML数据格式
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[text]]></MsgType>" +
                        "<Content><![CDATA[%s]]></Content>" + "</xml>",
                fromUserName, toUserName, getUtcTime(), content);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    private static String getUtcTime() {
        // 如果不需要格式,可直接用dt,dt就是当前系统时间
        Date dt = new Date();
        // 设置显示格式
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");
        String nowTime = df.format(dt);
        long dd = (long) 0;
        try {
            dd = df.parse(nowTime).getTime();
        } catch (Exception e) {

        }
        logger.info("当前时间：" + String.valueOf(dd));
        return String.valueOf(dd);
    }
}

