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
public class AppletsSubscribeMessageNotityBody implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 2741397050080545641L;
    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    
    
    
    
}
