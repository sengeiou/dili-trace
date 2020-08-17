package com.dili.trace.dto.wx;

import java.io.Serializable;
import java.util.Map;
import com.dili.trace.dto.wx.MessageConfig.OfficeMessageConfig.Content;
/**
 * 微信小程序发送公众号消息通知req
 * 
 * <pre>
 * Description:
 * 
 * @author cool.chen
 *
 * </pre>
 */
public class AppletsOfficeMessageNotityBody implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -6514884610736648010L;
    
    private String appid;
    private String template_id;
    private String url;
    private Map<String, String> miniprogram;
    private Map<String, Content> data;
    public String getAppid()
    {
        return appid;
    }
    public void setAppid(String appid)
    {
        this.appid = appid;
    }
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
    
    
    
    
    
    
    
}
