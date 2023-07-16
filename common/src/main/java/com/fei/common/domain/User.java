package com.fei.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/16
 * @Email: 2825097536@qq.com
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {

    private String openId;
    private String uuid;
    private String token;
    private String code;
}
