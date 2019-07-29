package com.dili.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class MD5Util {

    private static final Logger LOGGER= LoggerFactory.getLogger(MD5Util.class);

    public static String md5(String content){
        try{
            if(StringUtils.isBlank(content)){
                return "";
            }
            MessageDigest digest=MessageDigest.getInstance("MD5");
            byte[] buf=digest.digest(content.getBytes("UTF-8"));
            return parseByte2HexStr(buf);
        }catch (Exception e){
            LOGGER.error("method: md5",e);
        }
        return "";
    }

    private static String parseByte2HexStr(byte[] buf){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
