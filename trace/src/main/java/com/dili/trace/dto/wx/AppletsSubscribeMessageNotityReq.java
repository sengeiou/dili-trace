package com.dili.trace.dto.wx;

import java.io.Serializable;
import java.util.Map;

/**
 * 微信小程序订阅消息通知req
 * 
 * <pre>
 * Description:
 * 
 * @author cool.chen
 *
 * </pre>
 */
public class AppletsSubscribeMessageNotityReq implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -6514884610736648010L;
    
    private String touser;
    
    private String template_id;
    
    private String page;
    
    private String miniprogram_state;
    
    private String lang;
    
    private Map<String, AppletsSubscribeMessageNotityBody> data;

    public String getTouser()
    {
        return touser;
    }

    public void setTouser(String touser)
    {
        this.touser = touser;
    }

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
