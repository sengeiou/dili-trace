package com.dili.common.service;

import java.util.Map;
import java.util.Optional;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.SessionData;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.User;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * session与redis同步接口
 */
@Service
public class SessionRedisService {
    private static final Logger logger = LoggerFactory.getLogger(SessionRedisService.class);

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    private static final String SESSION_PREFIX = "HZ_TRACE_SESSION_";
    private static final String USERID_PREFIX = "HZ_TRACE_USERID_";

    /**
     * 创建redis Key
     * @param sessionId
     * @return
     */
    private String getSessionRedisKey(String sessionId) {
        StringBuilder key = new StringBuilder();
        key.append(SESSION_PREFIX);
        key.append(sessionId);
        return key.toString();
    }

    /**
     * 构造account数据
     * @param sessionData
     * @return
     */

    private String getAccountRedisKey(SessionData sessionData) {
        StringBuilder key = new StringBuilder();
        key.append(USERID_PREFIX);
        key.append(sessionData.getUserId());
        key.append("_");
        key.append(sessionData.getIdentityType());
        return key.toString();
    }

    /**
     * 从redis加载数据
     * @param sessionId
     * @return
     */
    public Optional<SessionData> loadFromRedis(String sessionId) {
        logger.info("loadFromRedis:sessionId={}", sessionId);
        if (StringUtils.isBlank(sessionId)) {
            return Optional.empty();
        }
        String sessionRedisKey = this.getSessionRedisKey(sessionId);
        
        Map<Object, Object> sessionMapData = (Map<Object, Object>) this.redisUtil.get(sessionRedisKey);
        if (sessionMapData == null) {
            return Optional.empty();
        }

        SessionData sessionData = SessionData.fromMap(sessionMapData);
        logger.info("loadFromRedis:sessionData={}", sessionData.toMap());
        if (StringUtils.isBlank(sessionData.getSessionId())) {
            sessionData.setSessionId(sessionId);
            this.saveToRedis(sessionData);
        }

        String accountRedisKey = this.getAccountRedisKey(sessionData);
        Map<Object, Object> accountMapData = (Map<Object, Object>) this.redisUtil.get(accountRedisKey);
        if (accountMapData != null) {
            SessionData accountData = SessionData.fromMap(accountMapData);
            logger.info("loadFromRedis:accountData={}", accountData.toMap());
            if (!sessionId.equals(accountData.getSessionId()) && accountData.getSessionId() != null) {
                String oldSessionKey = this.getSessionRedisKey(accountData.getSessionId());
                logger.info("del:session={}", oldSessionKey);
                this.redisUtil.remove(oldSessionKey);
                this.redisUtil.set(accountRedisKey, sessionData.toMap(), defaultConfiguration.getSessionExpire());
            }
        }

        return Optional.of(sessionData);
    }

    /**
     * 移除用户相关信息
     * @param user
     */
    public void removeUser(User user){
        SessionData sessionData=  SessionData.fromUser(user, LoginIdentityTypeEnum.USER.getCode());
        logger.info("removeUser:sessionData={}", sessionData.toMap());
        String accountRedisKey = this.getAccountRedisKey(sessionData);
        Map<Object, Object> accountMapData = (Map<Object, Object>) this.redisUtil.get(accountRedisKey);
        if (accountMapData != null) {
            SessionData accountData = SessionData.fromMap(accountMapData);
            logger.info("removeUser:accountData={}", accountData.toMap());
            this.deleteFromRedis(accountData.getSessionId());
        }
       
    }

    /**
     * 更新用户信息
     * @param user
     */
    public void updateUser(User user) {
        SessionData sessionData=  SessionData.fromUser(user, LoginIdentityTypeEnum.USER.getCode());
        logger.info("updateUser:sessionData={}", sessionData.toMap());
        String accountRedisKey = this.getAccountRedisKey(sessionData);
        Map<Object, Object> accountMapData = (Map<Object, Object>) this.redisUtil.get(accountRedisKey);
        if (accountMapData != null) {
            SessionData accountData = SessionData.fromMap(accountMapData);
            String sessionId=accountData.getSessionId();
            accountData.setSessionId(sessionId);
            Long expire= this.redisUtil.getRedisTemplate().getExpire(accountRedisKey);
            if(expire!=null&&expire>0){
                this.saveToRedis(accountData, expire);
            }
        }

    }

    /**
     * 从redis删除session数据
     * @param sessionId
     */
    public void deleteFromRedis(String sessionId) {
        logger.info("deleteFromRedis:sessionId={}", sessionId);
        if (StringUtils.isBlank(sessionId)) {
            return;
        }
        String sessionRedisKey = this.getSessionRedisKey(sessionId);
        Map<Object, Object> sessionMapData = (Map<Object, Object>) this.redisUtil.get(sessionRedisKey);
        if (sessionMapData != null) {
            this.redisUtil.remove(sessionRedisKey);

            SessionData sessionData = SessionData.fromMap(sessionMapData);
            String accountRedisKey = this.getAccountRedisKey(sessionData);
            this.redisUtil.remove(accountRedisKey);
        }

    }

    /**
     * 刷新redis数据
     * @param sessionData
     */
    public void refresh(SessionData sessionData) {
        if(sessionData==null){
            return;
        }
        logger.info("refresh:sessionId={}", sessionData.toMap());
        if(sessionData.changed()){
            if(sessionData.isInvalidate()){
                this.deleteFromRedis(sessionData.getSessionId());
            }else{
                this.saveToRedis(sessionData);
            }
        }
       
    }

    /**
     * 保存数据到redis
     * @param sessionData
     * @return
     */
    public SessionData saveToRedis(SessionData sessionData) {
        if (sessionData == null) {
            return sessionData;
        }
        this.saveToRedis(sessionData, defaultConfiguration.getSessionExpire());
        return sessionData;
    }

    /**
     * 保存数据到redis
     * @param sessionData
     * @param expire
     * @return
     */
    private SessionData saveToRedis(SessionData sessionData,long expire) {
        if (sessionData == null) {
            return sessionData;
        }
        logger.info("saveToRedis:sessionData={}", sessionData.toMap());
        String accountRedisKey = this.getAccountRedisKey(sessionData);
        String sessionRedisKey = this.getSessionRedisKey(sessionData.getSessionId());

        logger.info("saveToRedis:accountRedisKey={}", accountRedisKey);
        logger.info("saveToRedis:sessionRedisKey={}", sessionRedisKey);

        Map<Object, Object> oldAccountMapData = (Map<Object, Object>) this.redisUtil.get(accountRedisKey);
        logger.info("oldAccountMapData={}", oldAccountMapData);
        if (oldAccountMapData != null) {
            // 删除原有的session及相关数据
            this.redisUtil.remove(accountRedisKey);
            SessionData oldAccountData = SessionData.fromMap(oldAccountMapData);
            logger.info("oldAccountData={}", oldAccountData.toMap());
            if (oldAccountData != null && oldAccountData.getSessionId() != null) {
                String oldSessionKey = this.getSessionRedisKey(oldAccountData.getSessionId());
                logger.info("del oldAccountData:session={}", oldSessionKey);
                this.redisUtil.remove(oldSessionKey);
            }
        }

        this.redisUtil.set(accountRedisKey, sessionData.toMap(), expire);
        this.redisUtil.set(sessionRedisKey, sessionData.toMap(),expire);

        return sessionData;
    }
}