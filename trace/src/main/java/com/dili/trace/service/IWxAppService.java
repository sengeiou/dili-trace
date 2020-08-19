package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.WxApp;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
public interface IWxAppService extends BaseService<WxApp, Long> {

    /**
     * 获取用户授权
     * @param jsCode
     * @param appId
     * @return
     */
    String getSessionKey(String jsCode, String appId);

    /**
     * 获取用户信息
     * @param appId
     * @param openId
     * @param accessToken
     * @return
     */
    String getUserinfo(String appId, String openId, String accessToken);
}