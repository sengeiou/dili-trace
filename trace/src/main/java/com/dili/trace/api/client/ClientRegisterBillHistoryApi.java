package com.dili.trace.api.client;

import com.dili.common.annotation.Access;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.RegisterBillHistory;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.service.RegisterBillHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wangguofeng
 */
@SuppressWarnings("deprecation")
@RestController
@RequestMapping(value = "/api/client/clientRegisterBillHistoryApi")
@Api(value = "/api/client/clientRegisterBillHistoryApi", description = "历史相关接口")
@Access(role = Role.Client,url = "")
public class ClientRegisterBillHistoryApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientRegisterBillHistoryApi.class);

    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    private RegisterBillHistoryService billHistoryService;


    /**
     * 获取报备单审核历史列表
     * @param inputDto
     * @return
     */
    @ApiOperation(value = "获取报备单审核历史列表")
    @ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
    @RequestMapping(value = "/list.api", method = RequestMethod.POST)
    public BaseOutput<List<RegisterBillHistory>> list(@RequestBody RegisterBillHistory inputDto) {
        try {
            SessionData sessionData=this.sessionContext.getSessionData();
            Long userId = sessionData.getUserId();

            logger.info("获取报备单历史列表操作用户:{}", userId);
            inputDto.setUserId(userId);
            if (StringUtils.isBlank(inputDto.getOrder())) {
                inputDto.setOrder("desc");
                inputDto.setSort("modified");
            }
            inputDto.setMetadata(IDTO.AND_CONDITION_EXPR, "verify_status <>" + BillVerifyStatusEnum.WAIT_AUDIT.getCode());
            List<RegisterBillHistory> page = this.billHistoryService.listByExample(inputDto);
            return BaseOutput.success().setData(page);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询数据出错");
        }

    }
}
