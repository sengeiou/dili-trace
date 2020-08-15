package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.AppletPhone;
import com.dili.trace.domain.Watermark;
import com.dili.trace.domain.WxApp;
import com.dili.trace.service.IWxAppService;
import com.dili.trace.util.WxServerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author asa.lee
 */
@RestController
@RequestMapping(value = "/api/wxTokenApi")
@CrossOrigin(allowCredentials = "true", origins = "*", maxAge = 3600)
public class WxAppTokenApi {
    private Logger log = LoggerFactory.getLogger(WxAppTokenApi.class);
    @Autowired
    private IWxAppService wxAppService;

    @ApiOperation(value = "初始化小程序access_token", notes = "初始化小程序access_token")
    @PostMapping(value = "/initAccessToken", produces = "application/json;charset=utf-8")
    public BaseOutput initAccessToken(@RequestBody Map<String, String> wxInfo) {
        try {
            String appId=null;
            appId = wxInfo.get("appId");
            if (StringUtils.isBlank(appId)) {
                throw new RuntimeException("appId 不能为空");
            }
            WxApp wxapp = new WxApp();
            wxapp.setAppId(appId);
            List<WxApp> list=wxAppService.list(wxapp);
            if(!CollectionUtils.isEmpty(list)){
                wxapp.setAppSecret(list.get(0).getAppSecret());
            }

            Map<String, Object> rs = WxServerUtil.getAccessToken(wxapp.getAppId(), wxapp.getAppSecret());
            WxApp upwxApp = new WxApp();
            upwxApp.setAppId(appId);
            upwxApp.setAppSecret(wxapp.getAppSecret());
            upwxApp.setAccessToken((String) rs.get("access_token"));
            upwxApp.setAccessTokenExpiresIn((Integer) rs.get("expires_in"));
            upwxApp.setAccessTokenUpdateTime(new Date());
            WxApp upconApp = new WxApp();
            upconApp.setAppId(appId);
            wxAppService.updateByExample(upwxApp, upconApp);
            return BaseOutput.success().setData("初始化成功");
        } catch (Exception e) {
            log.error("init_access_token异常", e);
            return BaseOutput.failure("初始化失败").setData(e.getMessage());
        }

    }

    @ApiOperation(value = "获取小程序access_token", notes = "获取小程序access_token")
    @PostMapping(value = "/getAccessToken.api", produces = "application/json;charset=utf-8")
    public BaseOutput getAccessToken(@RequestBody Map<String, String> wxInfo) {
        try {
            String appId = wxInfo.get("appId");
            if (StringUtils.isBlank(appId)) {
                throw new RuntimeException("appId 不能为空");
            }
            WxApp wxapp = getWxAppByAppId(appId);
            return BaseOutput.success("查询成功").setData(wxapp.getAccessToken());
        } catch (Exception e) {
            log.error("get_access_token异常", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "获取授权信息", notes = "获取授权信息")
    @PostMapping(value = "/getWxOpenidUnionId.api", produces = "application/json;charset=utf-8")
    public BaseOutput getWxOpenidUnionId(@RequestBody Map<String, String> wxInfo) {
        try {
            log.info("getWxOpenidUnionId param:" + wxInfo.toString());
            String jsCode = wxInfo.get("js_code");
            if (StringUtils.isBlank(jsCode)) {
                throw new TraceBusinessException("js_code 不能为空");
            }
            String appId = wxInfo.get("appId");
            if (StringUtils.isBlank(appId)) {
                throw new RuntimeException("appId 不能为空");
            }
            String session = wxAppService.getSessionKey(jsCode, appId);
            return BaseOutput.success("查询成功").setData(JSON.parse(session));
        } catch (Exception e) {
            log.error("getWxOpenidUnionId异常", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @PostMapping(value = "/getUserInfo", produces = "application/json;charset=utf-8")
    public BaseOutput getUserInfo(@RequestBody Map<String, String> wxInfo) {
        try {
            String appId = wxInfo.get("appId");
            if (StringUtils.isBlank(appId)) {
                throw new RuntimeException("appId 不能为空");
            }

            String openId = wxInfo.get("openId");
            if (StringUtils.isBlank(openId)) {
                throw new RuntimeException("openId 不能为空");
            }
            WxApp wxApp=getWxAppByAppId(appId);
            String accessToken = wxApp.getAccessToken();
            String userinfo = wxAppService.getUserinfo(appId, openId, accessToken);
            ObjectMapper objectMapper = new ObjectMapper();
            return BaseOutput.success("查询成功").setData(objectMapper.readTree(userinfo));
        } catch (Exception e) {
            log.error("get_userinfo异常", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "解密手机号信息", notes = "解密手机号信息")
    @PostMapping(value = "/decodePhone.api")
    public BaseOutput decodePhone(@RequestBody Map<String, String> wxInfo)
    {
        try
        {
            String sessionKey=wxInfo.get("sessionKey");
            String encryptedData = wxInfo.get("encryptedData");
            String iv = wxInfo.get("iv");

            log.info("param sessionKey:" + sessionKey
                    + ",encryptedData:" + encryptedData + ",iv:" + iv);

            if (StringUtils.isEmpty(sessionKey)
                    || StringUtils.isEmpty(encryptedData)
                    || StringUtils.isEmpty(iv))
            {
                return BaseOutput.failure("参数错误");
            }

            //test-start
            AppletPhone test= new AppletPhone();
            test.setPhoneNumber("13100000000");
            test.setPurePhoneNumber("13100000000");
            test.setCountryCode("86");
            Watermark testWate = new Watermark();
            testWate.setAppid("wx9c2027ae603a1bf3");
            testWate.setTimestamp(System.currentTimeMillis());
            test.setWatermark(testWate);
            return BaseOutput.success().setData(test);
            //test-end

            /*sessionKey = URLDecoder.decode(sessionKey, "UTF-8");
            encryptedData = URLDecoder.decode(encryptedData, "UTF-8");
            iv = URLDecoder.decode(iv, "UTF-8");

            log.info("decodePhone decode sessionKey:" + sessionKey
                    + ",encryptedData:" + encryptedData + ",iv:" + iv);

            String decryStr = AppletAESUtil.decrypt(encryptedData, sessionKey,
                    iv);

            log.info("decodePhone str:" + decryStr);
            AppletPhone phone= JSON.parseObject(decryStr,new TypeReference<AppletPhone>() {});
            return BaseOutput.success().setData(phone);*/
        }
        catch (Exception e)
        {
            log.error("解密手机号码错误", e);
            BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.failure("error");
    }

    /**
     * 获取微信小程序的相关信息
     * @param appId
     * @return
     */
    private WxApp getWxAppByAppId(String appId) {
        WxApp wxapp = new WxApp();
        wxapp.setAppId(appId);
        List<WxApp> list=wxAppService.list(wxapp);
        if(!CollectionUtils.isEmpty(list)){
            wxapp.setAccessToken(list.get(0).getAccessToken());
        }
        return  wxapp;
    }
}
