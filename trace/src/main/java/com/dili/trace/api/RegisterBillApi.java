package com.dili.trace.api;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dili.assets.sdk.dto.CusCategoryDTO;
import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.input.RegisterBillApiInputDto;
import com.dili.trace.api.input.RegisterBillQueryInputDto;
import com.dili.trace.api.manager.ManagerRegisterBillApi;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.RegisterTallyAreaNo;
import com.dili.trace.domain.UserInfo;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.service.AssetsRpcService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.RegisterTallyAreaNoService;
import com.dili.trace.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 */
@RestController
@RequestMapping(value = "/api/registerBillApi")
@Api(value = "/api/registerBillApi", description = "登记单相关接口")
@AppAccess(role = Role.ANY)
public class RegisterBillApi extends AbstractApi {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBillApi.class);

    @Autowired
    ManagerRegisterBillApi managerRegisterBillApi;

    /**
     * 通过登记单ID获取登记单详细信息
     *
     * @param inputDto
     * @return
     */
    @ApiOperation(value = "通过登记单ID获取登记单详细信息")
    @RequestMapping(value = "/viewTradeDetailBill.api", method = RequestMethod.POST)
    public BaseOutput<RegisterBillOutputDto> viewTradeDetailBill(@RequestBody RegisterBillApiInputDto inputDto) {
        return this.managerRegisterBillApi.viewTradeDetailBill(inputDto);
    }
}
