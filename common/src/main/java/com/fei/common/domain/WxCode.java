package com.fei.common.domain;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: feiWoSCun
 * @Time: 2023/7/15
 * @Email: 2825097536@qq.com
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class WxCode {
    @JsonProperty("expire_seconds")
    private Integer expireSeconds;
    @JsonProperty("action_name")
    private String actionName;
    @JsonProperty("action_info")
    private ActionInfoDTO actionInfo;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    static class ActionInfoDTO {
        @JsonProperty("scene")
        private SceneDTO scene;

        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        private static class SceneDTO {
            @JsonProperty
            private String sceneStr;
            @JsonProperty("scene_id")
            private Integer sceneId;
        }
    }

    public interface WxCodeBuilder {
        /**
         * @param scene
         * @return
         * @description 用于构建默认二维码参数
         */
        WxCode build(String scene);
    }

    /**
     * 用于构建临时的二维码
     */
  public static class TemporaryWxBuilder implements WxCodeBuilder {
        @Override
        public WxCode build(String scene) {
            Integer time = 60 * 60 * 24;
            String actionName = "QR_STR_SCENE";
            ActionInfoDTO.SceneDTO sceneDTO = new ActionInfoDTO.SceneDTO();
            sceneDTO.setSceneStr(scene);
            return new WxCode(time, actionName, new ActionInfoDTO(sceneDTO));

        }
    }
//test一下
   /* public static void main(String[] args) {
        String s = JSON.toJSONString(new TemporaryWxBuilder().build());
        System.out.println(s);
    }*/
}
