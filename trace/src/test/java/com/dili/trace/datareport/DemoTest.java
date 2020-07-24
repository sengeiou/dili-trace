package com.dili.trace.datareport;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Maps;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;

import cn.hutool.http.HttpUtil;

public class DemoTest {
    @Test
    public void getAccessToken() throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> param = Maps.newHashMap();
        param.put("appId", "13254079");
        param.put("appSecret", "0b21c361a44bc5eaf3f9f88db30288e3a3abb697");

        String url = "http://202.101.190.110:9008";
        String jsonBody = new ObjectMapper().writeValueAsString(param);
        String responseText = HttpUtil.post(url, jsonBody, 20 * 1000);
        System.out.println(responseText);
        Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
        ParseContext parseContext = JsonPath.using(conf);

        Map<String, Object> resp = Maps.newHashMap();
        Map<String, Object> data = Maps.newHashMap();
        data.put("accessToken", "123");
        data.put("expires", "7200");
        resp.put("data", data);
        resp.put("success", true);
        DocumentContext loginDocumentContext = parseContext.parse(new ObjectMapper().writeValueAsString(resp));
        Boolean success = loginDocumentContext.read("$.success");
        if (success != null && success) {
            String accessToken = loginDocumentContext.read("$.data.accessToken");
            String expires = loginDocumentContext.read("$.data.expires");
            System.out.println(accessToken);
            System.out.println(expires);

        } else {
            String msg = loginDocumentContext.read("$.msg");
        }
        System.out.println(success);

    }
}