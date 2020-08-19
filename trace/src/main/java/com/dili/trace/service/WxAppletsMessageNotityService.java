package com.dili.trace.service;

import java.util.Map;

public interface WxAppletsMessageNotityService
{


    /**
     * 发送微信小程序订阅消息
     * @param openId
     * @param messageType
     * @param extMap
     */
    void sendSubscribeMessageNotity(String openId, String messageType,
                                    Map<String, Object> extMap);
    
    /**
     * 发送微信公众号消息
     * @param openId
     * @param messageType
     * @param extMap
     */
    void sendOfficeMessageNotity(String openId, String messageType,
                                 Map<String, Object> extMap);
}
