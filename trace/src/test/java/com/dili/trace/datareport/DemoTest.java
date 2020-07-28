package com.dili.trace.datareport;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
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

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;

public class DemoTest {
    @Test
    public void getAccessToken() throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> param = Maps.newHashMap();
        param.put("appId", "13254079");
        param.put("appSecret", "0b21c361a44bc5eaf3f9f88db30288e3a3abb697");

        String url = "http://202.101.190.110:9008/nfwlApi/getAccessToken";
        String jsonBody = new ObjectMapper().writeValueAsString(param);
        String responseText = HttpUtil.post(url, jsonBody, 20 * 1000);
        System.out.println(url);
        System.out.println(responseText);
        Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
        ParseContext parseContext = JsonPath.using(conf);
        DocumentContext loginDocumentContext = parseContext.parse(responseText);
        Boolean success = loginDocumentContext.read("$.success");
        System.out.println(success);
        if (success != null && success) {
            String accessToken = loginDocumentContext.read("$.data.token");
            System.out.println(accessToken);

        } else {
            String msg = loginDocumentContext.read("$.msg");
            System.out.println(msg);
        }

    }

    @Test
    public void marketCount() throws JsonGenerationException, JsonMappingException, IOException {

        Map<String, Object> param = Maps.newHashMap();
        param.put("subjectCount", 555);
        param.put("pdtCount", 23);
        param.put("updateTime", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        String url = "http://202.101.190.110:9008/nfwlApi/marketCount";
        String jsonBody = new ObjectMapper().writeValueAsString(param);

        Map<String, String> headeMap = Maps.newHashMap();
        headeMap.put("token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ3ZWIiLCJpc3MiOiJ0eGxjIiwiZXhwIjoxNTk1NTgxMDU1LCJpYXQiOjE1OTU1NzM4NTUsImp0aSI6ImRlZmNhMmZkNTAwMjRkNzNhZDE1MGU3MzU1ZTI3MjUyIiwidXNlcm5hbWUiOiIxMzI1NDA3OSJ9.YjacurAuQKIXUZLPQzITbEoGNP8KiDGBc39g72YIhFc");
        String responseText = HttpUtil.createPost(url).addHeaders(headeMap).body(jsonBody).timeout(20 * 1000)
                .charset("utf-8").contentLength(jsonBody.getBytes("utf-8").length).contentType("application/json")
                .execute().body();
        System.out.println(responseText);
        Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
        ParseContext parseContext = JsonPath.using(conf);
        DocumentContext loginDocumentContext = parseContext.parse(responseText);
        Boolean success = loginDocumentContext.read("$.success");
        System.out.println(success);
        if (success != null && success) {
            System.out.println("上报成功");
        } else {
            String msg = loginDocumentContext.read("$.msg");
            System.out.println(msg);
        }

    }

    @Test
    public void reportCount() throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> param = Maps.newHashMap();
        param.put("subjectCount", 555);
        param.put("pdtCount", 23);
        param.put("updateTime", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        String url = "http://202.101.190.110:9008/nfwlApi/reportCount";
        String jsonBody = new ObjectMapper().writeValueAsString(param);

        Map<String, String> headeMap = Maps.newHashMap();
        headeMap.put("token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ3ZWIiLCJpc3MiOiJ0eGxjIiwiZXhwIjoxNTk1NTgxMDU1LCJpYXQiOjE1OTU1NzM4NTUsImp0aSI6ImRlZmNhMmZkNTAwMjRkNzNhZDE1MGU3MzU1ZTI3MjUyIiwidXNlcm5hbWUiOiIxMzI1NDA3OSJ9.YjacurAuQKIXUZLPQzITbEoGNP8KiDGBc39g72YIhFc");
        String responseText = HttpUtil.createPost(url).addHeaders(headeMap).body(jsonBody).timeout(20 * 1000)
                .charset("utf-8").contentLength(jsonBody.getBytes("utf-8").length).contentType("application/json")
                .execute().body();
        System.out.println(responseText);
        Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
        ParseContext parseContext = JsonPath.using(conf);
        DocumentContext loginDocumentContext = parseContext.parse(responseText);
        Boolean success = loginDocumentContext.read("$.success");
        System.out.println(success);
        if (success != null && success) {
            System.out.println("上报成功");
        } else {
            String msg = loginDocumentContext.read("$.msg");
            System.out.println(msg);
        }
    }

    public void regionCount() throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> param = Maps.newHashMap();
        param.put("regionName", "四川成都");//
        param.put("weight", 23);// KG
        param.put("updateTime", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        String url = "http://202.101.190.110:9008/nfwlApi/regionCount";
        String jsonBody = new ObjectMapper().writeValueAsString(param);

        Map<String, String> headeMap = Maps.newHashMap();
        headeMap.put("token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ3ZWIiLCJpc3MiOiJ0eGxjIiwiZXhwIjoxNTk1NTgxMDU1LCJpYXQiOjE1OTU1NzM4NTUsImp0aSI6ImRlZmNhMmZkNTAwMjRkNzNhZDE1MGU3MzU1ZTI3MjUyIiwidXNlcm5hbWUiOiIxMzI1NDA3OSJ9.YjacurAuQKIXUZLPQzITbEoGNP8KiDGBc39g72YIhFc");
        String responseText = HttpUtil.createPost(url).addHeaders(headeMap).body(jsonBody).timeout(20 * 1000)
                .charset("utf-8").contentLength(jsonBody.getBytes("utf-8").length).contentType("application/json")
                .execute().body();
        System.out.println(responseText);
        Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
        ParseContext parseContext = JsonPath.using(conf);
        DocumentContext loginDocumentContext = parseContext.parse(responseText);
        Boolean success = loginDocumentContext.read("$.success");
        System.out.println(success);
        if (success != null && success) {
            System.out.println("上报成功");
        } else {
            String msg = loginDocumentContext.read("$.msg");
            System.out.println(msg);
        }
    }

    @Test
    public void codeCount() throws JsonGenerationException, JsonMappingException, IOException {
        Map<String, Object> param = Maps.newHashMap();
        param.put("greenCount",10);
        param.put("yellowCount", 0);
        param.put("redCount", 0);
        param.put("waringInfo", Lists.newArrayList());
        param.put("updateTime", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        String url = "http://202.101.190.110:9008/nfwlApi/codeCount";
        String jsonBody = new ObjectMapper().writeValueAsString(param);

        Map<String, String> headeMap = Maps.newHashMap();
        headeMap.put("token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ3ZWIiLCJpc3MiOiJ0eGxjIiwiZXhwIjoxNTk1NTgxMDU1LCJpYXQiOjE1OTU1NzM4NTUsImp0aSI6ImRlZmNhMmZkNTAwMjRkNzNhZDE1MGU3MzU1ZTI3MjUyIiwidXNlcm5hbWUiOiIxMzI1NDA3OSJ9.YjacurAuQKIXUZLPQzITbEoGNP8KiDGBc39g72YIhFc");
        String responseText = HttpUtil.createPost(url).addHeaders(headeMap).body(jsonBody).timeout(20 * 1000)
                .charset("utf-8").contentLength(jsonBody.getBytes("utf-8").length).contentType("application/json")
                .execute().body();
        System.out.println(responseText);
        Configuration conf = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
        ParseContext parseContext = JsonPath.using(conf);
        DocumentContext loginDocumentContext = parseContext.parse(responseText);
        Boolean success = loginDocumentContext.read("$.success");
        System.out.println(success);
        if (success != null && success) {
            System.out.println("上报成功");
        } else {
            String msg = loginDocumentContext.read("$.msg");
            System.out.println(msg);
        }
    }
}