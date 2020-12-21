package com.dili.trace.api.manager;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.TruckEnterRecord;
import com.dili.trace.dto.query.TruckEnterRecordQueryDto;
import com.dili.trace.service.TruckEnterRecordService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * (管理员)司机进门报备
 */
@RestController
@RequestMapping(value = "/api/manager/managerTruckEnterRecordApi", method = RequestMethod.POST)
@Api(value = "/api/manager/managerTruckEnterRecordApi", description = "登记单相关接口")
@AppAccess(role = Role.Manager, url = "dili-trace-app-auth", subRoles = {})
public class ManagerTruckEnterRecordApi {

    @Autowired
    LoginSessionContext sessionContext;
    @Autowired
    TruckEnterRecordService truckEnterRecordService;

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
        if(StringUtils.isNotBlank(queryInput.getKeyword())){
            queryInput.setMetadata(IDTO.AND_CONDITION_EXPR,
                    ( " (driver_phone like '" + queryInput.getKeyword().trim()
                    + "%'  or driver_phone like '" + queryInput.getKeyword().trim() + "%')" ));
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
        input.setMarketId(sessionData.getMarketId());
        input.setCreated(new Date());
        input.setModified(new Date());
        input.setId(null);
        this.truckEnterRecordService.insertSelective(input);
        return BaseOutput.successData(input.getId());
    }

}
