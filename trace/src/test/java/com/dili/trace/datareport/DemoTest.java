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
    public void dd() throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> param = Maps.newHashMap();
        param.put("appId", "");
        param.put("appSecret", "");

        String url = "http://202.101.190.110:9008";
        String jsonBody = new ObjectMapper().writeValueAsString(param);
        String responseText = HttpUtil.post(url, jsonBody, 20 * 1000);

        Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
        ParseContext parseContext = JsonPath.using(conf);
        DocumentContext loginDocumentContext = parseContext.parse(responseText);
        Boolean success = loginDocumentContext.read("$.success");

    }
}