package com.dili.trace.controller;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.common.service.BaseInfoRpcService;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.sg.trace.glossary.SalesTypeEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.*;
import com.dili.trace.domain.sg.QualityTraceTradeBill;
import com.dili.trace.dto.*;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.service.*;
import com.dili.trace.util.MarketUtil;
import com.dili.trace.util.MaskUserInfo;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 * @author Alvin.Li
 */
@Api("/newUser")
@Controller
@RequestMapping("/newUser")
public class NewUserController {
    private static final Logger logger = LoggerFactory.getLogger(NewUserController.class);

    @Autowired
    UserService userService;
    @Autowired
    UserPlateService userPlateService;
    @Autowired
    DefaultConfiguration defaultConfiguration;
    @Autowired
    BaseInfoRpcService baseInfoRpcService;
    @Autowired
    UsualAddressService usualAddressService;

    /**
     * 跳转到User页面
     * @param modelMap
     * @return
     */
    @ApiOperation("跳转到User页面")
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        LocalDateTime now = LocalDateTime.now();
        modelMap.put("createdStart", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        modelMap.put("createdEnd", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
        modelMap.put("cities", usualAddressService.findUsualAddressByType(UsualAddressTypeEnum.USER));
        return "new-user/index";
    }

    /**
     * 分页查询User
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "分页查询User", notes = "分页查询User，返回easyui分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = false, dataType = "string") })
    @RequestMapping(value = "/listPage.action", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody String listPage(UserListDto user) throws Exception {
        // 设置市场查询条件
        user.setMarketId(MarketUtil.returnMarket());
        EasyuiPageOutput out = this.userService.listEasyuiPageByExample(user);
        return out.toString();
    }


}