package com.dili.trace.domain;

import java.io.Serializable;

public class AppletPhone implements Serializable
{
    private static final long serialVersionUID = -5871098048262851649L;

    private String phoneNumber;

    private String purePhoneNumber;

    private String countryCode;

    private Watermark watermark;

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPurePhoneNumber()
    {
        return purePhoneNumber;
    }

    public void setPurePhoneNumber(String purePhoneNumber)
    {
        this.purePhoneNumber = purePhoneNumber;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    public Watermark getWatermark()
    {
        return watermark;
    }

    public void setWatermark(Watermark watermark)
    {
        this.watermark = watermark;
    }
}
