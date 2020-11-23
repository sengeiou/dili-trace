package com.dili.trace.dto.wx;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;


/**
 *
 * @Author guzman.liu
 * @Description
 * 发送微信消息配置类
 * @Date 2020/11/23 10:20
 * @return
 */
@Component
@EnableConfigurationProperties
@PropertySource(value= "classpath:wx-message-template.properties",encoding="UTF-8")
@ConfigurationProperties(prefix="config.wx.message")
@Configuration
public class MessageConfig
{
    /**公众号appid*/
    private String officeAppId;
    
    /**小程序appid*/
    private String appletsAppId;
    
    // 小程序消息
    private Map<String,AppletsMessageConfig> applets;
    
    // 公众号消息
    private Map<String,OfficeMessageConfig> office;
    
    public static class AppletsMessageConfig{
        private String template_id;
        private String page;
        private String miniprogram_state;
        private String lang;
        private Map<String, AppletsSubscribeMessageNotityBody> data;
        public String getTemplate_id()
        {
            return template_id;
        }
        public void setTemplate_id(String template_id)
        {
            this.template_id = template_id;
        }
        public String getPage()
        {
            return page;
        }
        public void setPage(String page)
        {
            this.page = page;
        }
        public String getMiniprogram_state()
        {
            return miniprogram_state;
        }
        public void setMiniprogram_state(String miniprogram_state)
        {
            this.miniprogram_state = miniprogram_state;
        }
        public String getLang()
        {
            return lang;
        }
        public void setLang(String lang)
        {
            this.lang = lang;
        }
        public Map<String, AppletsSubscribeMessageNotityBody> getData()
        {
            return data;
        }
        public void setData(Map<String, AppletsSubscribeMessageNotityBody> data)
        {
            this.data = data;
        }
        

        
    }
    
    
    
    public static class OfficeMessageConfig{
        private String template_id;
        private String url;
        private Map<String, String> miniprogram;
        private Map<String, Content> data;
        public String getTemplate_id()
        {
            return template_id;
        }
        public void setTemplate_id(String template_id)
        {
            this.template_id = template_id;
        }
        public String getUrl()
        {
            return url;
        }
        public void setUrl(String url)
        {
            this.url = url;
        }
        public Map<String, String> getMiniprogram()
        {
            return miniprogram;
        }
        public void setMiniprogram(Map<String, String> miniprogram)
        {
            this.miniprogram = miniprogram;
        }
        public Map<String, Content> getData()
        {
            return data;
        }
        public void setData(Map<String, Content> data)
        {
            this.data = data;
        }
        
        
        public static class Content{
            private String value;
            private String color;
            public String getValue()
            {
                return value;
            }
            public void setValue(String value)
            {
                this.value = value;
            }
            public String getColor()
            {
                return color;
            }
            public void setColor(String color)
            {
                this.color = color;
            }
            
        }
    }
    
    
    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:20
     * @return
     */
    public String getOfficeAppId()
    {
        return officeAppId;
    }

    /**
     *
     * @Author guzman.liu
     * @Description 
     * @Date 2020/11/23 10:21
     * @return
     */
    public void setOfficeAppId(String officeAppId)
    {
        this.officeAppId = officeAppId;
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:22
     * @return
     */
    public String getAppletsAppId()
    {
        return appletsAppId;
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:22
     * @return
     */
    public void setAppletsAppId(String appletsAppId)
    {
        this.appletsAppId = appletsAppId;
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:22
     * @return
     */
    public Map<String, OfficeMessageConfig> getOffice()
    {
        return office;
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:22
     * @return
     */
    public void setOffice(Map<String, OfficeMessageConfig> office)
    {
        this.office = office;
    }
    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:22
     * @return
     */
    public Map<String, AppletsMessageConfig> getApplets()
    {
        return applets;
    }
    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:22
     * @return
     */
    public void setApplets(Map<String, AppletsMessageConfig> applets)
    {
        this.applets = applets;
    }

    
    
}
