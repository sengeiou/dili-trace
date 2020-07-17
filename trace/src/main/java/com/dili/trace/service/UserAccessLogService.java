package com.dili.trace.service;

import java.util.Date;
import java.util.Optional;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.UserAccessLog;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserAccessLogService extends BaseServiceImpl<UserAccessLog, Long> {
    private static final Logger logger = LoggerFactory.getLogger(UserAccessLogService.class);

    public Optional<UserAccessLog> createUserAccessLog(Long userId, Integer loginType, String url) {
        if (userId == null && loginType == null && StringUtils.isBlank(url)) {
            return Optional.empty();
        }
        url=url.trim();
        try {
            UserAccessLog log = new UserAccessLog();
            log.setUserId(userId);
            log.setLoginType(loginType);
            if(url.length()>400){
                url=url.substring(url.length()-400);
            }
            log.setUrl(url);
            log.setCreated(new Date());
            log.setModified(new Date());
            this.insertSelective(log);
            return Optional.of(log);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Optional.empty();
        }

    }

}