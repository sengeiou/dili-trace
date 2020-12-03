package com.dili.trace.service;

import com.dili.common.annotation.Access;
import com.dili.common.exception.TraceBizException;
import com.dili.trace.dto.IdNameDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import one.util.streamex.StreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * web请求上下文环境变量接口
 */
@Service
public class UapRpcService {
    @Autowired
    FirmRpcService firmRpcService;

    /**
     * 当前登录用户名和id
     * @return
     */
    public Optional<OperatorUser> getCurrentOperator() {
        return this.getCurrentUserTicket().map(ut -> {
            OperatorUser dto = new OperatorUser(ut.getId(), ut.getRealName());
            return dto;
        });
    }

    /**
     * 查询当前登录用户信息
     *
     * @return
     */
    public Optional<UserTicket> getCurrentUserTicket() {
        try {
            //两个方法在没有使用JSF的项目中是没有区别的
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            //RequestContextHolder.getRequestAttributes();
            //从session里面获取对应的值
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        } catch (Exception e) {
            throw new TraceBizException("当前运行环境不是web请求环境");
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        return Optional.ofNullable(userTicket);
    }

    /**
     * 查询当前登录用户所属市场信息
     *
     * @return
     */
    public Optional<Firm> getCurrentFirm() {
        return StreamEx.of(this.getCurrentUserTicket()).flatCollection(u -> {
            return StreamEx.of(this.firmRpcService.getFirmById(u.getFirmId())).toList();
        }).findFirst();

    }

    /**
     * 是否有访问权限
     *
     * @param method
     * @param url
     * @return
     */
    public boolean hasAccess(String method, String url) {
        return SessionContext.hasAccess(method, url);
    }

    /**
     * 是否有访问权限
     *
     * @param access
     * @return
     */
    public boolean hasAccess(Access access) {
        return SessionContext.hasAccess(access.method(), access.url());
    }

}
