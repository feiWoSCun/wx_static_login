package com.fei.common.constant;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/14
 * @Email: 2825097536@qq.com
 */
public enum WxMessageType {

    TYRE("text");
    private final String type;

    WxMessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
