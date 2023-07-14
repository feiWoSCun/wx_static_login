package com.fei.common.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.function.Function;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/14
 * @Email: 2825097536@qq.com
 */
public class ServletUtil {


/**
 * @author: feiWoSCun
 * @Time: 2023/3/2
 * @Email: 2825097536@qq.com
 */


/*    private static HttpServletRequest request;
private static HttpServletResponse response;*/


    /**
     * @return 得到request
     */


    /**
     * 从请求头拿到元素
     *
     * @param headerName
     * @return
     */
    public static String getHeader(String headerName) {
        HttpServletRequest request = getRequest();
        return request.getHeader(headerName);
    }

    /**
     * 什么叫tm的解耦啊（叉腰）
     *
     * @param function
     * @param <R>
     * @return
     */
    public static <R> R baseAttr(Function<ServletRequestAttributes, R> function) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return function.apply(attributes);
    }

    /**
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return baseAttr(t -> t.getRequest());
    }

    /**
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        return baseAttr(t -> t.getResponse());
    }
}

