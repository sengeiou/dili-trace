package com.dili.trace.api.manager;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.output.RegisterBillOutput;
import com.dili.trace.api.output.VerifyBillInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;
import com.dili.trace.util.BasePageUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 管理员接口(审核之后)
 */
@RestController
@RequestMapping(value = "/api/manager/managerAfterCheckInApi")
@Api(value = "/api/manager/managerAfterCheckInApi", description = "登记单相关接口")
public class ManagerAfterCheckInApi {
    private static final Logger logger = LoggerFactory.getLogger(ManagerAfterCheckInApi.class);
    @Autowired
    private RegisterBillService registerBillService;
    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    UserService userService;

    /**
     * 获得场内审核列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获得场内审核列表")
    @RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
    public BaseOutput<BasePage<RegisterBill>> listPage(@RequestBody RegisterBillDto query) {
        try {
            OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
            query.setSort("created");
            query.setOrder("desc");
            BasePage<RegisterBillOutput> data = BasePageUtil.convert(this.registerBillService.listPageAfterCheckinVerifyBill(query),
                    rb -> {
                        RegisterBillOutput dto = RegisterBillOutput.build(rb);
                        return dto;
                    });
            return BaseOutput.success().setData(data);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("操作失败：服务端出错");
        }
    }

    /**
     * 不同审核状态数据统计
     */
    @RequestMapping(value = "/countByVerifyStatus.api", method = {RequestMethod.POST})
    public BaseOutput<List<VerifyStatusCountOutputDto>> countByVerifyStatus(@RequestBody RegisterBillDto query) {

        try {
            OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
            List<VerifyStatusCountOutputDto> list = this.registerBillService.countByVerifyStatuseAfterCheckin(query);
            return BaseOutput.success().setData(list);

        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("操作失败：服务端出错");
        }


    }

    /**
     * 场内审核登记单
     * @param inputDto
     * @return
     */
    @ApiOperation(value = "场内审核登记单")
    @RequestMapping(value = "/doVerify.api", method = RequestMethod.POST)
    public BaseOutput<Long> doVerify(@RequestBody VerifyBillInputDto inputDto) {
        logger.info("场内审核登记单:{}", inputDto.getBillId());
        try {
            if (inputDto == null || inputDto.getVerifyStatus() == null || inputDto.getBillId() == null) {
                return BaseOutput.failure("参数错误");
            }
            OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
            RegisterBill input = new RegisterBill();
            input.setId(inputDto.getBillId());
            input.setVerifyStatus(inputDto.getVerifyStatus());
            input.setReason(inputDto.getReason());
            Long id = this.registerBillService.doVerifyAfterCheckIn(input.getBillId(), input.getVerifyStatus(), input.getReason(), Optional.ofNullable(operatorUser));
            return BaseOutput.success().setData(id);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("操作失败：服务端出错");
        }

    }
}
