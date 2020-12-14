package com.dili.trace.api.manager;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.input.RegisterBillQueryInputDto;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillInputDto;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.*;

import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import java.util.List;
import java.util.Optional;

/**
 * 管理登记单接口
 */
@RestController
@RequestMapping(value = "/api/manager/managerRegisterBill")
@Api(value = "/api/manager/managerRegisterBill", description = "登记单相关接口")
@AppAccess(role = Role.Manager, url = "dili-trace-app-auth", subRoles = {})
public class ManagerRegisterBillApi {
    private static final Logger logger = LoggerFactory.getLogger(ManagerRegisterBillApi.class);
    @Autowired
    private RegisterBillService registerBillService;
    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    CustomerRpcService customerRpcService;
    @Autowired
    UpStreamService upStreamService;
    @Autowired
    ImageCertService imageCertService;

    // @ApiOperation(value = "获得登记单详情")
    // @RequestMapping(value = "/viewRegisterBill.api", method = RequestMethod.POST)
    // public BaseOutput<BasePage<RegisterBill>> viewRegisterBill(@RequestBody RegisterBillDto input) {
    // 	if (input == null || input.getBillId() == null) {
    // 		return BaseOutput.failure("参数错误");
    // 	}
    // 	try {
    // 		OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
    // 		RegisterBill billItem = this.registerBillService.get(input.getBillId());
    // 		if(billItem==null){
    // 			return BaseOutput.failure("数据不存在");
    // 		}

    // 		RegisterBillOutput dto = RegisterBillOutput.build(billItem);

    // 		String upStreamName=StreamEx.ofNullable(billItem.getUpStreamId()).nonNull()
    // 		.map(upId->{return this.upStreamService.get(upId);})
    // 		.nonNull().map(UpStream::getName).findFirst().orElse("");
    // 		dto.setUpStreamName(upStreamName);
    // 		dto.setImageCertList(this.imageCertService.findImageCertListByBillId(billItem.getId()));
    // 		return BaseOutput.success().setData(dto);

    // 	} catch (TraceBusinessException e) {
    // 		return BaseOutput.failure(e.getMessage());
    // 	} catch (Exception e) {
    // 		logger.error(e.getMessage(), e);
    // 		return BaseOutput.failure("操作失败：服务端出错");
    // 	}
    // }

    /**
     * 为经营户创建登记单
     *
     * @return
     */
    @ApiOperation(value = "为经营户创建登记单")
    @RequestMapping(value = "/createRegisterBillForSeller.api", method = RequestMethod.POST)
    public BaseOutput createRegisterBillForSeller(@RequestBody CreateListBillParam createListBillParam) {
        logger.info("保存多个登记单:{}", JSON.toJSONString(createListBillParam));
        if (createListBillParam == null || createListBillParam.getRegisterBills() == null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            SessionData sessionData = this.sessionContext.getSessionData();
            List<CreateRegisterBillInputDto> registerBills = StreamEx.of(createListBillParam.getRegisterBills())
                    .nonNull().toList();
            if (registerBills.isEmpty()) {
                return BaseOutput.failure("没有登记单");
            }
            logger.info("保存多个登记单操作用户:{}，{}", sessionData.getUserId(), sessionData.getUserName());
            List<Long> idList = this.registerBillService.createBillList(registerBills, createListBillParam.getUserId()
                    , Optional.of(new OperatorUser(sessionData.getUserId(), sessionData.getUserName())), sessionData.getMarketId());
            return BaseOutput.success().setData(idList);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

    /**
     * 统计不同审核状态下报备单数量
     *
     * @return
     */
    @RequestMapping("/countBillByVerifyStatus.api")
    public BaseOutput countBillByVerifyStatus() {

        return BaseOutput.success();
//YesOrNoEnum
    }

    /**
     * 分页查询报备单
     *
     * @param query
     * @return
     */
    @RequestMapping("/listPagedBill.api")
    public BaseOutput listPagedBill(@RequestBody RegisterBillDto query) {
        return BaseOutput.success();
    }

}
