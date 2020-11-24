package com.dili.trace.api.components;

import java.util.Map;
import java.util.Optional;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.SessionData;
import com.dili.sg.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.ss.redis.service.RedisUtil;

/**
 * redis service
 */
@Component
public class SessionRedisService {
    private static final Logger logger = LoggerFactory.getLogger(SessionRedisService.class);

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    private static final String SESSION_PREFIX = "SG_TRACE_SESSION_";
    private static final String USERID_PREFIX = "SG_TRACE_USERID_";
    // WAITING_DISABLED KEY 等待被禁用的用户
    private static final String WAITING_DISABLED_USER_PREFIX = "SG_TRACE_WAITING_DISABLED_USERS";

    /**
     *
     * @param sessionId
     * @return
     */
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

    public void addWaitDisabledUser(Long userId) {
        redisUtil.getRedisTemplate().opsForSet().add(WAITING_DISABLED_USER_PREFIX, userId);
    }
    public void removeUserFromWaitDisabled(Long userId) {
        redisUtil.getRedisTemplate().opsForSet().remove(WAITING_DISABLED_USER_PREFIX, userId);
    }
    public boolean checkUserDisabled(Long userId) {
        return redisUtil.getRedisTemplate().opsForSet().isMember(WAITING_DISABLED_USER_PREFIX, userId);
    }

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

    public void removeUser(User user) {
        SessionData sessionData = SessionData.fromUser(user, LoginIdentityTypeEnum.USER.getCode());
        logger.info("removeUser:sessionData={}", sessionData.toMap());
        String accountRedisKey = this.getAccountRedisKey(sessionData);
        Map<Object, Object> accountMapData = (Map<Object, Object>) this.redisUtil.get(accountRedisKey);
        if (accountMapData != null) {
            SessionData accountData = SessionData.fromMap(accountMapData);
            logger.info("removeUser:accountData={}", accountData.toMap());
            this.deleteFromRedis(accountData.getSessionId());
        }

    }

    public void updateUser(User user) {
        SessionData sessionData = SessionData.fromUser(user, LoginIdentityTypeEnum.USER.getCode());
        logger.info("updateUser:sessionData={}", sessionData.toMap());
        String accountRedisKey = this.getAccountRedisKey(sessionData);
        Map<Object, Object> accountMapData = (Map<Object, Object>) this.redisUtil.get(accountRedisKey);
        if (accountMapData != null) {
            SessionData accountData = SessionData.fromMap(accountMapData);
            String sessionId = accountData.getSessionId();
            accountData.setSessionId(sessionId);
            Long expire = this.redisUtil.getRedisTemplate().getExpire(accountRedisKey);
            if (expire != null && expire > 0) {
                this.saveToRedis(accountData, expire);
            }
        }

    }

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

    public void refresh(SessionData sessionData) {
        if (sessionData == null) {
            return;
        }
        logger.info("refresh:sessionId={}", sessionData.toMap());
        if (sessionData.changed()) {
            if (sessionData.isInvalidate()) {
                this.deleteFromRedis(sessionData.getSessionId());
            } else {
                this.saveToRedis(sessionData);
            }
        }

    }

    public SessionData saveToRedis(SessionData sessionData) {
        if (sessionData == null) {
            return sessionData;
        }
        this.saveToRedis(sessionData, defaultConfiguration.getSessionExpire());
        return sessionData;
    }

    private SessionData saveToRedis(SessionData sessionData, long expire) {
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
        this.redisUtil.set(sessionRedisKey, sessionData.toMap(), expire);

        return sessionData;
    }
}