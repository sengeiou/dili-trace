package com.dili.common.entity;

import com.dili.common.annotation.Role;
import com.dili.trace.domain.User;
import com.dili.trace.dto.IdNameDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.idname.AbstraceIdName;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserTicket;
import com.google.common.base.Objects;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SessionData {
    private static final Logger logger = LoggerFactory.getLogger(SessionData.class);
    /**
     * 身份类型
     */
    private Integer identityType;
    /**
     * 角色
     */
    private Role role;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 摊位号
     */
    private String tallyAreaNos;
    /**
     * 验证状态
     */
    private Integer validateState;
    /**
     * 二维码状态
     */
    private Integer qrStatus;
    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 登录时间
     */
    private Date loginDateTime;
    /**
     * 过期
     */
    private boolean invalidate;
    /**
     * sessionid
     */
    private String sessionId;
    /**
     * 市场id
     */
    private Long marketId;
    /**
     * 用户微信菜单
     */
    private Set<String> userWeChatMenus;

    public <T extends AbstraceIdName> Optional<T> to() {
        if (Role.Manager == this.role) {
            Optional opt = Optional.<OperatorUser>of(new OperatorUser(this.userId, this.userName));
            return opt;
        } else if (Role.Client == this.role) {
            Optional opt = Optional.<IdNameDto>of(new IdNameDto(this.userId, this.userName));
            return opt;
        } else {
            return Optional.empty();
        }


    }

    private SessionData() {

    }

    public static SessionData mockClient() {
        SessionData sessionData = new SessionData();
        sessionData.setMarketId(8L);
        sessionData.setUserId(31L);
        sessionData.setUserName("悟空");
        sessionData.setMarketName("寿光");
        return sessionData;
    }


    public static SessionData fromUser(OperatorUser user, Integer identityType, Firm firm, Set<String> userWeChatMenus) {
        SessionData data = new SessionData();
        data.identityType = identityType;
        data.userId = user.getId();
        data.userName = user.getName();
        data.marketId = firm.getId();
        data.marketName = firm.getName();
        data.userWeChatMenus = userWeChatMenus;

        return data;
    }

    private Map<Object, Object> convertThisToMap() {
        Map<Object, Object> map = new HashMap<>(new BeanMap(this));
        return map;
    }

    public Map<Object, Object> toMap() {
        return this.convertThisToMap();
    }

    public static SessionData fromMap(Map<Object, Object> map) {
        SessionData data = new SessionData();
        try {
            BeanUtils.copyProperties(data, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return data;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
        return data;
    }

    public static SessionData fromUser(OperatorUser user, Integer identityType) {
        SessionData data = new SessionData();
        data.identityType = identityType;
        data.userId = user.getId();
        data.userName = user.getName();
        return data;
    }

    public static SessionData fromUserTicket(UserTicket ut) {
        SessionData data = new SessionData();
        data.userId = ut.getId();
        data.userName = ut.getRealName();
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