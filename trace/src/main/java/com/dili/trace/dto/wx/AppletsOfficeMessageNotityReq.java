package com.dili.trace.dto.wx;

import java.io.Serializable;
import java.util.Map;

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
public class AppletsOfficeMessageNotityReq implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -6514884610736648010L;
    
    private String touser;
    private AppletsOfficeMessageNotityBody mp_template_msg;
    
    
    public String getTouser()
    {
        return touser;
    }
    public void setTouser(String touser)
    {
        this.touser = touser;
    }
    public AppletsOfficeMessageNotityBody getMp_template_msg()
    {
        return mp_template_msg;
    }
    public void setMp_template_msg(AppletsOfficeMessageNotityBody mp_template_msg)
    {
        this.mp_template_msg = mp_template_msg;
    }

    
    
    
    
}
