package com.fei.common.exception;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/15
 * @Email: 2825097536@qq.com
 */
public interface ExceptionMsg {


    /**
     * 错误码
     *
     * @return
     */
    int code();

    /**
     * 错误消息
     *
     * @return
     */
    String message();
}
