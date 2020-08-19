package com.dili.trace.util;

import com.dili.common.exception.TraceBusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author asa.lee
 */
public class WxUserUtil {
	
	public static Map<String, String> callBackMap = new HashMap<String, String>();

	public static String ERRCODE="errcode";

	public static Map<String, Object> getAccessToken(String appId, String secret, String code)
			throws JsonMappingException, JsonProcessingException {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + secret + "&code="
				+ code + "&grant_type=authorization_code";
		RestTemplate restTemplate = SpringbootUtil.getBean(RestTemplate.class);
		String response = restTemplate.getForObject(url, String.class);
		if (response == null) {
			throw new TraceBusinessException("获取失败，未接收到响应结果。appId:[" + appId + "],code:[" + code + "]");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode resData = objectMapper.readTree(response);
		if (resData.has(ERRCODE)){
			throw new TraceBusinessException(resData.get("errmsg").asText() + "。 appId:[" + appId + "],code:[" + code + "]");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("access_token", resData.get("access_token").asText());
		map.put("expires_in", resData.get("expires_in").intValue());
		map.put("refresh_token", resData.get("refresh_token").asText());
		map.put("openid", resData.get("openid").asText());
		map.put("scope", resData.get("scope").asText());
		return map;
	}

	public static String getUserinfo(String openId, String accessToken)
			throws JsonMappingException, JsonProcessingException {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
				+ "OPENID&lang=zh_CN";
		RestTemplate restTemplate = SpringbootUtil.getBean(RestTemplate.class);
		String response = restTemplate.getForObject(url, String.class);
		if (response == null) {
			throw new TraceBusinessException("获取失败，未接收到响应结果");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode resData = objectMapper.readTree(response);
		if (resData.has(ERRCODE)) {
			throw new TraceBusinessException(resData.get("errmsg").asText());
		}
		return response;
	}

	public static String refreshAccessToken(String appId, String refreshToken)
			throws JsonMappingException, JsonProcessingException {
		String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appId
				+ "&grant_type=refresh_token&refresh_token=" + refreshToken;
		RestTemplate restTemplate = SpringbootUtil.getBean(RestTemplate.class);
		String response = restTemplate.getForObject(url, String.class);
		if (response == null) {
			throw new TraceBusinessException("获取失败，未接收到响应结果");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode resData = objectMapper.readTree(response);
		if (resData.has(ERRCODE)) {
			throw new TraceBusinessException(resData.get("errmsg").asText());
		}
		return response;
	}
	
	public static String putCallBackParam(String value)
	{
		 String key = UUID.randomUUID().toString();
		 
		 callBackMap.put(key, value);
		 
		 return key;
	}
	
	public static String getCallBackParam(String key)
	{
		String value = callBackMap.get(key);
		
		callBackMap.remove(key);
		
		return value;
	}
}
