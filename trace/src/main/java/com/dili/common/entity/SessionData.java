package com.dili.common.entity;

import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.uap.sdk.domain.Firm;
import com.google.common.base.Objects;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SessionData {
    private static final Logger logger = LoggerFactory.getLogger(SessionData.class);

    private Integer identityType;
    private Long userId;
    private String userName;
    private String tallyAreaNos;
    private Integer validateState;
    private Integer qrStatus;
    private String marketName;

    private Date loginDateTime;
    private boolean invalidate;
    private String sessionId;
    private Long marketId;
    private Set<String> userWeChatMenus;

    private Map<Object, Object> mapData = new HashMap<>();

    private SessionData() {
    }


    public static SessionData fromUser(OperatorUser user, Integer identityType, Firm firm, Set<String> userWeChatMenus) {
        SessionData data = new SessionData();
        data.identityType = identityType;
        data.userId = user.getId();
        data.userName = user.getName();
        data.marketId = firm.getId();
        data.marketName = firm.getName();
        data.userWeChatMenus = userWeChatMenus;

        data.mapData = data.convertThisToMap();
        return data;
    }

    private Map<Object, Object> convertThisToMap() {
        Map<Object, Object> map = new HashMap<>(new BeanMap(this));
        map.remove("mapData");
        return map;
    }

    public Map<Object, Object> toMap() {
        return this.convertThisToMap();
    }

    public static SessionData fromMap(Map<Object, Object> map) {
        SessionData data = new SessionData();
        try {
            BeanUtils.copyProperties(data, map);
            data.mapData = data.convertThisToMap();
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return data;
    }

    public boolean changed() {
        Map<Object, Object> previousMapData=  this.mapData;
        Map<Object, Object> currentMapData=  this.convertThisToMap();
        for(Object key:previousMapData.keySet()){
            Object preValue=previousMapData.get(key);
            Object currentValue=currentMapData.get(key);
            if(!Objects.equal(preValue, currentValue)){
                return true;
            }
        }
        return false;

    }

    public static SessionData fromUser(User user, Integer identityType) {
        SessionData data = new SessionData();
        data.identityType = identityType;
        data.userId = user.getId();
        data.userName = user.getName();
        data.tallyAreaNos = user.getTallyAreaNos();
        data.validateState = -1;
        data.qrStatus = -1;
        data.marketId = user.getMarketId();
        data.marketName = user.getMarketName();
        data.mapData = data.convertThisToMap();
        return data;
    }

    public static SessionData fromUser(OperatorUser user, Integer identityType) {
        SessionData data = new SessionData();
        data.identityType = identityType;
        data.userId = user.getId();
        data.userName = user.getName();
        data.mapData = data.convertThisToMap();
        return data;
    }

    /**
     * @return Integer return the identityType
     */
    public Integer getIdentityType() {
        return identityType;
    }

    /**
     * @param identityType the identityType to set
     */
    public void setIdentityType(Integer identityType) {
        this.identityType = identityType;
    }

    /**
     * @return Long return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return String return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param tallyAreaNos the tallyAreaNos to set
     */
    public void setTallyAreaNos(String tallyAreaNos) {
        this.tallyAreaNos = tallyAreaNos;
    }

    /**
     * @param validateState the validateState to set
     */
    public void setValidateState(Integer validateState) {
        this.validateState = validateState;
    }

    /**
     * @param qrStatus the qrStatus to set
     */
    public void setQrStatus(Integer qrStatus) {
        this.qrStatus = qrStatus;
    }

    /**
     * @param marketName the marketName to set
     */
    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    /**
     * @return Date return the loginDateTime
     */
    public Date getLoginDateTime() {
        return loginDateTime;
    }

    /**
     * @param loginDateTime the loginDateTime to set
     */
    public void setLoginDateTime(Date loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    /**
     * @return boolean return the invalidate
     */
    public boolean isInvalidate() {
        return invalidate;
    }

    /**
     * @param invalidate the invalidate to set
     */
    public void setInvalidate(boolean invalidate) {
        this.invalidate = invalidate;
    }

    /**
     * @return String return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the tallyAreaNos
     */
    public String getTallyAreaNos() {
        return tallyAreaNos;
    }

    /**
     * @return the validateState
     */
    public Integer getValidateState() {
        return validateState;
    }

    /**
     * @return the qrStatus
     */
    public Integer getQrStatus() {
        return qrStatus;
    }

    /**
     * @return the marketName
     */
    public String getMarketName() {
        return marketName;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Set<String> getUserWeChatMenus() {
        return userWeChatMenus;
    }

    public void setUserWeChatMenus(Set<String> userWeChatMenus) {
        this.userWeChatMenus = userWeChatMenus;
    }
}