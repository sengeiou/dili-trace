package com.dili.trace.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author asa.lee
 */
public class WxServerUtil {

    private static Logger log = LoggerFactory.getLogger(WxServerUtil.class);

    public static Map<String, Object> getAccessToken(String appid, String secret)
            throws JsonMappingException, JsonProcessingException {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret="
                + secret;
        Map<String, Object> map = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        if (response == null) {
            log.error("获取失败，未接收到响应结果");
            return map;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode resData = objectMapper.readTree(response);
        String errcode = "errcode";
        if (resData.has(errcode)) {
            log.error(resData.get("errmsg").asText());
            return map;
        }
        map.put("access_token", resData.get("access_token").asText());
        map.put("expires_in", resData.get("expires_in").intValue());
        return map;
    }


    public static Map<String, String> sign(String jsapiTicket, String url)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Map<String, String> ret = new HashMap<String, String>();
        String nonceStr = create_nonce_str();
        String timestamp = createTimestamp();
        String string1;
        String signature = "";

        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;

        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(string1.getBytes("UTF-8"));
        signature = byteToHex(crypt.digest());

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapiTicket);
        ret.put("nonceStr", nonceStr);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
