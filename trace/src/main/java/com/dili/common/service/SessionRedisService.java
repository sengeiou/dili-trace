package com.dili.common.service;

import java.util.Map;
import java.util.Optional;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.SessionData;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionRedisService {
    private static final Logger logger=LoggerFactory.getLogger(SessionRedisService.class);
    @Autowired
    RedisService redisService;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    private static final String SESSION_PREFIX = "HZ_TRACE_SESSION_";
	private static final String USERID_PREFIX = "HZ_TRACE_USERID_";

    private String getSessionRedisKey(String sessionId) {
        StringBuilder key = new StringBuilder();
        key.append(SESSION_PREFIX);
        key.append(sessionId);
        return key.toString();
    }

    private String getAccountRedisKey(SessionData sessionData) {
        StringBuilder key = new StringBuilder();
        key.append(USERID_PREFIX);
        key.append(sessionData.getUserId());
        key.append("_");
        key.append(sessionData.getIdentityType());
        return key.toString();
    }

    public Optional<SessionData> loadFromRedis(String sessionId) {
        logger.info("loadFromRedis:sessionId={}",sessionId);
        if(StringUtils.isBlank(sessionId)){
            return Optional.empty();
        }
        String sessionRedisKey = this.getSessionRedisKey(sessionId);
        Map<Object, Object> sessionMapData = (Map<Object, Object>) this.redisService.get(sessionRedisKey);
        if (sessionMapData == null) {
            return Optional.empty();
        }
        SessionData sessionData = SessionData.fromMap(sessionMapData);

        String accountRedisKey = this.getAccountRedisKey(sessionData);
        Map<Object, Object> accountMapData = (Map<Object, Object>) this.redisService.get(accountRedisKey);
        SessionData accountData = SessionData.fromMap(accountMapData);
        if (!accountData.getSessionId().equals(sessionId)) {
            this.redisService.del(this.getSessionRedisKey(accountData.getSessionId()));
            this.redisService.set(accountRedisKey, sessionData.toMap(), defaultConfiguration.getSessionExpire());
        }
        return Optional.of(sessionData);
    }

    public void deleteFromRedis(String sessionId) {
        logger.info("deleteFromRedis:sessionId={}",sessionId);
        if(StringUtils.isBlank(sessionId)){
            return;
        }
        String sessionRedisKey = this.getSessionRedisKey(sessionId);
        Map<Object, Object> sessionMapData = (Map<Object, Object>) this.redisService.get(sessionRedisKey);
        if (sessionMapData != null) {
            this.redisService.del(sessionRedisKey);

            SessionData sessionData = SessionData.fromMap(sessionMapData);
            String accountRedisKey = this.getAccountRedisKey(sessionData);
            this.redisService.del(accountRedisKey);
        }

    }

    public SessionData saveToRedis(SessionData sessionData) {
        if(sessionData==null){
            return sessionData;
        }
        logger.info("saveToRedis:sessionData={}",sessionData.toMap());
        String sessionRedisKey = this.getSessionRedisKey(sessionData.getSessionId());
        this.redisService.set(sessionRedisKey, sessionData.toMap(), defaultConfiguration.getSessionExpire());

        String accountRedisKey = this.getAccountRedisKey(sessionData);
        this.redisService.set(accountRedisKey, sessionData.toMap(), defaultConfiguration.getSessionExpire());

        return sessionData;
    }
}