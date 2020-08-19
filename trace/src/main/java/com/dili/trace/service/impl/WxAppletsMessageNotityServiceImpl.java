package com.dili.trace.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.dto.wx.*;
import com.dili.trace.dto.wx.MessageConfig.AppletsMessageConfig;
import com.dili.trace.dto.wx.MessageConfig.OfficeMessageConfig;
import com.dili.trace.dto.wx.MessageConfig.OfficeMessageConfig.Content;
import com.dili.trace.rpc.WxRpc;
import com.dili.trace.service.WxAppletsMessageNotityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;

/**
 * 小程序发送通知
 * 
 * <pre>
 * Description:
 * 
 * @author cool.chen
 *
 * </pre>
 */
@Service
public class WxAppletsMessageNotityServiceImpl implements WxAppletsMessageNotityService
{
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    @Autowired
    private MessageConfig config;

    @Autowired
    private WxRpc wxRpc ;
    
    
    @Override
    public void sendSubscribeMessageNotity(String openId, String messageType,
            Map<String, Object> extMap)
    {
        logger.info("==> sendSubscribeMessageNotity begin param: openId[{}] messageType[{}] extMap[{}]"
                ,openId,messageType,extMap);
        
        if(StringUtils.isEmpty(openId)){throw new IllegalArgumentException("openId is not empty");}
        if(StringUtils.isEmpty(messageType)){throw new IllegalArgumentException("messageType is not empty");}
        
        // 构建消息对象
        AppletsSubscribeMessageNotityReq req = buildSubscribeMessageReq(openId,messageType,extMap);
        
        // 发送消息
        sendHttpPost(JSON.toJSONString(req),"https://api.weixin.qq.com/cgi-bin/message/subscribe/send");
        
        logger.info("==> sendSubscribeMessageNotity end");
        
    }
    
    /**
     * 构建订阅消息对象
     * @param openId
     * @param messageType
     * @param extMap
     * @return
     */
    private AppletsSubscribeMessageNotityReq buildSubscribeMessageReq(
            String openId, String messageType, Map<String, Object> extMap)
    {
        AppletsSubscribeMessageNotityReq req = new AppletsSubscribeMessageNotityReq();
        
        
        // 公众号的配置项
        Map<String, AppletsMessageConfig> messageConfig = config.getApplets();
        
        AppletsMessageConfig appletsMessageConfig = messageConfig.get(messageType);
        
        logger.info("==> 获取 [{}] 配置信息 [{}]",messageType,JSON.toJSONString(appletsMessageConfig));
        if(appletsMessageConfig == null){
            logger.error("==> 消息类型 [{}] 配置不存在");
            throw new IllegalArgumentException("消息类型: "+ messageType +" 模板不存在");
        }
        
        // 配置文件中的数据
        Map<String, AppletsSubscribeMessageNotityBody> data = appletsMessageConfig.getData();
        
        // 需要替换的数据
        convertSubscribeExtData(data,extMap);
        
        req.setTouser(openId);
        req.setTemplate_id(appletsMessageConfig.getTemplate_id());
        req.setLang(appletsMessageConfig.getLang());
        req.setPage(mapGetString(extMap, "page",appletsMessageConfig.getPage()));
        req.setMiniprogram_state(appletsMessageConfig.getMiniprogram_state());
        req.setData(data);
            
        return req;
    }

    private String mapGetString(Map<String, Object> extMap, String key,
            String defaultString)
    {
        if(CollectionUtils.isEmpty(extMap)){
            return defaultString;
        }
        
        Object object = extMap.get(key);
        String returnKey = defaultString;
        
        if(object != null){
            returnKey = object.toString();
        }
        
        if(object instanceof String){
            returnKey = (String) object;
        }
        
        
        
        return returnKey;
    }

    /**
     * 转换默认参数 (替换data 中的属性)
     * @param data
     * @param extMap 
     * @return
     */
    private void convertSubscribeExtData(
            Map<String, AppletsSubscribeMessageNotityBody> data, Map<String, Object> extMap)
    {
        if(extMap == null || extMap.size() == 0){
            return;
        }
        
        extMap.forEach((k,v)->{
            String[] kArray = k.split("\\.");
            if(kArray.length > 1 && data.containsKey(kArray[1])){
                String key = kArray[1];
                AppletsSubscribeMessageNotityBody appletsSubscribeMessageNotityBody = data.get(key);
                if(appletsSubscribeMessageNotityBody != null){
                    
                    Object object = extMap.get(k);
                    if(object instanceof List){
                        List<String> list = (List<String>) object;
                        appletsSubscribeMessageNotityBody.setValue(String.format(appletsSubscribeMessageNotityBody.getValue(), list.toArray()));
                        
                    }else{
                        appletsSubscribeMessageNotityBody.setValue(mapGetString(extMap, k,appletsSubscribeMessageNotityBody.getValue()));
                    }
                    
                }
            }
        });
    }
    
    /**
     * 转换Mini page 
     * @param miniprogram
     * @param extMap
     */
    private void convertMiniProgram(Map<String, String> miniprogram,
            Map<String, Object> extMap)
    {
        if(extMap == null || extMap.size() == 0){
            return;
        }
        
        if(extMap.containsKey("miniprogram.pagepath")){
            String mapGetString = mapGetString(extMap, "miniprogram.pagepath", "");
            miniprogram.put("pagepath", mapGetString);
        }
    }
    
    private void convertOfficeExtData(Map<String, MessageConfig.OfficeMessageConfig.Content> data,
            Map<String, Object> extMap)
    {
        if(extMap == null || extMap.size() == 0){
            return;
        }
        
        extMap.forEach((k,v)->{
            String[] kArray = k.split("\\.");
            if(kArray.length > 1 && data.containsKey(kArray[1])){
                String key = kArray[1];
                Content content = data.get(key);
                if(content != null){
                    if(StringUtils.equalsIgnoreCase(kArray[2], "value")){
                        
                        Object object = extMap.get(k);
                        if(object instanceof List){
                            List<String> list = (List<String>) object;
                            content.setValue(String.format(content.getValue(), list.toArray()));
                            
                        }else{
                            content.setValue(mapGetString(extMap, k, content.getValue()));
                        }
                        
                        
                    }
                    
                    if(StringUtils.equalsIgnoreCase(kArray[2], "color")){
                        content.setColor(mapGetString(extMap, k, content.getColor()));
                    }
                }
            }
        });
        
    }
    

    @Override
    public void sendOfficeMessageNotity(String openId, String messageType,
            Map<String, Object> extMap)
    {
        logger.info("==> sendOfficeMessageNotity begin param: openId[{}] messageType[{}] extMap[{}]"
                ,openId,messageType,extMap);
        
        if(StringUtils.isEmpty(openId)){throw new IllegalArgumentException("openId is not empty");}
        if(StringUtils.isEmpty(messageType)){throw new IllegalArgumentException("messageType is not empty");}
        
        // 构建消息对象
        AppletsOfficeMessageNotityReq req = buildOfficeMessageReq(openId,messageType,extMap);
        
        // 发送消息
        sendHttpPost(JSON.toJSONString(req),"https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send");
        
        logger.info("==> sendOfficeMessageNotity end");
        
    }


    /**
     * 构建小程序发送订阅消息对象
     * @param openId
     * @param messageType
     * @param extMap
     * @return
     */
    private AppletsOfficeMessageNotityReq buildOfficeMessageReq(String openId,
            String messageType, Map<String, Object> extMap)
    {
        AppletsOfficeMessageNotityReq req = new AppletsOfficeMessageNotityReq();
        
        AppletsOfficeMessageNotityBody reqBody = buildOfficeMessageBody(messageType,extMap);
        
        req.setTouser(openId);
        req.setMp_template_msg(reqBody);
            
        return req;
    }


    /**
     * 构建公众号消息对象
     * @param messageType
     * @param extMap
     * @return
     */
    private AppletsOfficeMessageNotityBody buildOfficeMessageBody(
            String messageType, Map<String, Object> extMap)
    {
        // 公众号的配置项
        Map<String, OfficeMessageConfig> messageConfig = config.getOffice();
        
        OfficeMessageConfig officeConfig = messageConfig.get(messageType);
        
        logger.info("==> 获取 [{}] 配置信息 [{}]",messageType,JSON.toJSONString(officeConfig));
        if(officeConfig == null){
            logger.error("==> 消息类型 [{}] 配置不存在");
            throw new IllegalArgumentException("消息类型: "+ messageType +" 模板不存在");
        }
        
        AppletsOfficeMessageNotityBody body = new AppletsOfficeMessageNotityBody();
        
        Map<String, Content> data = officeConfig.getData();
        
        Map<String, String> miniprogram = officeConfig.getMiniprogram();
        
        convertOfficeExtData(data, extMap);
        
        convertMiniProgram(miniprogram,extMap);
        
        body.setAppid(config.getOfficeAppId());
        body.setTemplate_id(officeConfig.getTemplate_id());
        body.setUrl(mapGetString(extMap, "url", officeConfig.getUrl()));
        body.setMiniprogram(miniprogram);
        body.setData(data);
        
        return body;
    }


    
    

    
    

    private void sendHttpPost(String paramStr,String url)
    {
        url = url + "?access_token="+getAccessToken();
        String jsonString = paramStr;
        
        logger.info("==> 待发送的消息 url[{}] req[{}]",url,JSON.toJSONString(paramStr));
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Object outPut = wxRpc.uniformSend(getAccessToken(), jsonObject);
        
        //ResponseResult sendHttpPost = HttpClientUtil.getInstance().sendHttpPost(url, jsonString);
        
        logger.info("send wx Message respose [{}]",JSON.toJSONString(outPut));
        
    }


    private String getAccessToken()
    {
        String accessToken = "36_aKVGkwQ-traa1Uhz80HfnQcz9xxZC3Pg8P_rXF9lo7ruzy1nqQDWgOoBERvLBkuQZZa54OEUKOk5FP8BqYa5gfGG20JdbbwIVLntS8ddaLTIQAmrdpA0s1eATSf_Ld-DdDJ-MGlYcEMzQHU1ZZZiABADDD";
        
        if(StringUtils.isEmpty(accessToken)){
            throw new RuntimeException("accessToken is empty");
        }
        
        return accessToken;
    }
    
    
    
    
}
