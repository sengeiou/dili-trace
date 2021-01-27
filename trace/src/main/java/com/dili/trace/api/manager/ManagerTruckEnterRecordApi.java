package com.dili.trace.api.manager;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.TruckEnterRecord;
import com.dili.trace.dto.query.TruckEnterRecordQueryDto;
import com.dili.trace.rpc.service.UidRestfulRpcService;
import com.dili.trace.service.TruckEnterRecordService;
import com.dili.trace.util.RegUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

/**
 * (管理员)司机进门报备
 */
@RestController
@RequestMapping(value = "/api/manager/managerTruckEnterRecordApi", method = RequestMethod.POST)
@Api(value = "/api/manager/managerTruckEnterRecordApi", description = "登记单相关接口")
@AppAccess(role = Role.Manager, url = "dili-trace-app-auth", subRoles = {})
public class ManagerTruckEnterRecordApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerTruckEnterRecordApi.class);

    @Autowired
    LoginSessionContext sessionContext;
    @Autowired
    TruckEnterRecordService truckEnterRecordService;
    @Autowired
    UidRestfulRpcService uidRestfulRpcService;

    /**
     * 查询司机报备信息
     *
     * @param queryInput
     * @return
     */
    @RequestMapping(value = "/listPage.api")
    public BaseOutput<BasePage<TruckEnterRecord>> listPage(@RequestBody TruckEnterRecordQueryDto queryInput) {
        SessionData sessionData = this.sessionContext.getSessionData();
        queryInput.setMarketId(sessionData.getMarketId());
        if (StringUtils.isNotBlank(queryInput.getKeyword())) {
            queryInput.setMetadata(IDTO.AND_CONDITION_EXPR,
                    (" (driver_phone like '" + queryInput.getKeyword().trim()
                            + "%'  or driver_phone like '" + queryInput.getKeyword().trim() + "%')"));
        }
        BasePage<TruckEnterRecord> data = this.truckEnterRecordService.listPageByExample(queryInput);

        return BaseOutput.successData(data);

    }

    /**
     * 为司机创建进门报备信息
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "/createTruckEnterRecord.api")
    public BaseOutput createTruckEnterRecord(@RequestBody TruckEnterRecord input) {
        SessionData sessionData = this.sessionContext.getSessionData();
        if (StringUtils.isBlank(input.getTruckPlate())) {
            LOGGER.error("司机报备没有车牌号");
            return BaseOutput.failure("车牌号必填");
        }
        if (StringUtils.isBlank(input.getTruckTypeName()) || Objects.isNull(input.getTruckTypeId())) {
            LOGGER.error("司机报备没有车型");
            return BaseOutput.failure("车型必填");
        }
        if (!RegUtils.isPlate(input.getTruckPlate())) {
            return BaseOutput.failure("车牌格式错误");
        }


        input.setCode(uidRestfulRpcService.getTruckEnterRecordCode());
        input.setMarketId(sessionData.getMarketId());
        input.setCreated(new Date());
        input.setModified(new Date());
        input.setId(null);
        this.truckEnterRecordService.insertSelective(input);
        return BaseOutput.successData(input.getId());
    }

}
