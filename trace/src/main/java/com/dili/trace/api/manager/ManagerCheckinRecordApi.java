package com.dili.trace.api.manager;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CheckinOutRecordQueryDto;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.service.CheckinOutRecordService;
import com.dili.trace.service.RegisterBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员进门信息接口
 */
@RestController
@RequestMapping(value = "/api/manager/managerCheckinRecordApi")
@Api(value = "/api/manager/managerCheckinRecordApi", description = "进门数据接口")
public class ManagerCheckinRecordApi {
    private static final Logger logger = LoggerFactory.getLogger(ManagerCheckinRecordApi.class);
    @Autowired
    private LoginSessionContext sessionContext;

    @Autowired
    CheckinOutRecordService checkinOutRecordService;

    @Autowired
    RegisterBillService registerBillService;

    /**
     * 获得当前管理员进门操作数据列表
     *
     * @param query
     * @return
     */

    @ApiOperation(value = "获得当前管理员进门操作数据列表")
    @RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
    public BaseOutput<BasePage<CheckinOutRecord>> listPage(@RequestBody CheckinOutRecordQueryDto query) {
        try {
            Long operatorUserId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER).getId();
            query.setOperatorId(operatorUserId);
            query.setSort("created");
            query.setOrder("desc");
            BasePage<CheckinOutRecord> page = this.checkinOutRecordService.listPageByExample(query);

            //添加车牌号
            if (null != page.getDatas()) {
                StreamEx.of(page.getDatas()).forEach(record -> {
                    RegisterBill bill = this.registerBillService.get(record.getBillId());
                    if (null != bill) {
                        record.setPlate(bill.getPlate());
                    }
                });
            }
            return BaseOutput.success().setData(page);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }
}
