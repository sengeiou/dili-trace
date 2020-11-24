package com.dili.trace.service.impl;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.util.SpringUtil;
import com.dili.trace.dao.WxAppMapper;
import com.dili.trace.domain.WxApp;
import com.dili.trace.service.IWxAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 *
 * @author asa.lee
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IWxServiceImpl extends BaseServiceImpl<WxApp, Long> implements IWxAppService {
    private static final Logger logger = LoggerFactory.getLogger(IWxServiceImpl.class);

    public WxAppMapper getActualDao() {
        return (WxAppMapper) getDao();
    }

    @Override
    public String getSessionKey(String jsCode, String appId) {

        WxApp wxApp = new WxApp();
        wxApp.setAppId(appId);
        wxApp = getActualDao().selectOne(wxApp);
        if (wxApp == null) {
            throw new TraceBizException("appId在表中不存在");
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxApp.getAppId()
                + "&secret=" + wxApp.getAppSecret() + "&js_code=" + jsCode + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        if (response == null) {
            throw new TraceBizException("获取session key失败，未接收到响应结果");
        }
        return response;
    }

    @Override
    public String getUserinfo(String appId, String openId, String accessToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
                + "OPENID&lang=zh_CN";
        RestTemplate restTemplate = SpringUtil.getApplicationContext().getBean(RestTemplate.class);
        String response = restTemplate.getForObject(url, String.class);
        if (response == null) {
            throw new TraceBizException("获取失败，未接收到响应结果");
        }
        logger.info("user info response:{}", response);
        return response;
    }
}