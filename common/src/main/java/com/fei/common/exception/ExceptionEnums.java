package com.fei.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/15
 * @Email: 2825097536@qq.com
 */
@AllArgsConstructor
@Getter
public enum ExceptionEnums implements ExceptionMsg {
    SYSTEM_Fail(-1, "程序内部异常"),
    PARAM_EXCEPTION(-2, "参数校验错误");


    int code;
    String message;

    @Override
    public int code() {
        return 0;
    }

    @Override
    public String message() {
        return null;
    }
}
