package com.dili.trace.domain;

import java.io.Serializable;

public class Watermark implements Serializable
{
    private static final long serialVersionUID = 1903052383419199246L;

    private long timestamp;

    private String appid;

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

}
