package com.fei.common.domain.vo.response;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/15
 * @Email: 2825097536@qq.com
 */

import com.fei.common.exception.ExceptionMsg;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Result<T> {
    private Boolean success;
    private Integer code;
    private String errMsg;
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.setData(null);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setData(data);
        result.setSuccess(Boolean.TRUE);
        result.setCode(HttpStatus.OK.value());
        return result;
    }


    public static <T> Result<T> fail(ExceptionMsg errorEnum) {
        Result<T> result = new Result<T>();
        result.setSuccess(Boolean.FALSE);
        result.setCode(errorEnum.code());
        result.setErrMsg(errorEnum.message());
        return result;
    }

    public static <T> Result<T> fail(int code, String msg) {
        Result<T> result = new Result<T>();
        result.setSuccess(Boolean.FALSE);
        result.setCode(code);
        result.setErrMsg(msg);
        return result;
    }

    public boolean isSuccess() {
        return this.success;
    }
}
