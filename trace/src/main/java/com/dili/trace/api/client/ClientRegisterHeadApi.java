package com.dili.trace.api.client;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.input.CreateRegisterHeadInputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.*;
import com.dili.trace.dto.CreateListRegisterHeadParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterHeadDto;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.RegisgterHeadStatusEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 进门主台账单相关接口
 *
 * @author Lily
 */
@RestController
@RequestMapping(value = "/api/client/clientRegisterHead")
@Api(value = "/api/client/clientRegisterHead", description = "进门主台账单相关接口")
@AppAccess(role = Role.Client, url = "", subRoles = {CustomerEnum.CharacterType.经营户, CustomerEnum.CharacterType.买家})
public class ClientRegisterHeadApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientRegisterHeadApi.class);

    @Autowired
    RegisterHeadService registerHeadService;

    @Autowired
    private LoginSessionContext sessionContext;

    @Autowired
    CustomerRpcService customerRpcService;

    @Autowired
    ImageCertService imageCertService;

    @Autowired
    RegisterBillService registerBillService;

    @Autowired
    UpStreamService upStreamService;

    /**
     * 获取进门主台账单列表
     *
     * @param input 查询条件
     * @return 主台账单列表
     */
    @ApiOperation(value = "获取进门主台账单列表")
    @ApiImplicitParam(paramType = "body", name = "RegisterHead", dataType = "RegisterHead", value = "获取进门主台账单列表")
    @RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
    public BaseOutput<BasePage<CheckinOutRecord>> listPage(@RequestBody RegisterHeadDto input) {
        logger.info("获取进门主台账单列表:{}", JSON.toJSONString(input));
        try {
            Long userId = this.sessionContext.getSessionData().getUserId();
            logger.info("获取进门主台账单列表 操作用户:{}", userId);
            input.setSort("created");
            input.setOrder("desc");
            BasePage<RegisterHead> registerHeadBasePage = registerHeadService.listPageApi(input);

            if (null != registerHeadBasePage && CollectionUtils.isNotEmpty(registerHeadBasePage.getDatas())) {
                registerHeadBasePage.getDatas().forEach(e -> {
                    e.setWeightUnitName(WeightUnitEnum.fromCode(e.getWeightUnit()).get().getName());
                    e.setBillTypeName(BillTypeEnum.fromCode(e.getBillType()).get().getName());
                });
            }

            return BaseOutput.success().setData(registerHeadBasePage);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询数据出错");
        }

    }

    /**
     * 不同审核状态数据统计
     *
     * @param query 查询条件
     * @return 审核状态数据统计
     */
    @RequestMapping(value = "/countByStatus.api", method = {RequestMethod.POST})
    public BaseOutput<List<VerifyStatusCountOutputDto>> countByStatus(@RequestBody RegisterHeadDto query) {
        try {
            // 10: 已启用 20：已关闭 30：废弃
            List<VerifyStatusCountOutputDto> list = new ArrayList<>(3);
            List<RegisterHead> registerHeads = registerHeadService.listByExample(query);
            int activeCount = 0;
            int unactiveCount = 0;
            int deleteCount = 0;
            if (CollectionUtils.isNotEmpty(registerHeads)) {
                //Stream<RegisterHead> registerHeadStream = registerHeads.stream().filter(e -> (e.getActive() == null || e.getIsDeleted() == null));
                // 已启用
                activeCount = (int) registerHeads.stream().filter(e -> (e.getActive() == 1 && e.getIsDeleted() == 0)).count();
                unactiveCount = (int) registerHeads.stream().filter(e -> (e.getActive() == 0 && e.getIsDeleted() == 0)).count();
                deleteCount = (int) registerHeads.stream().filter(e -> e.getIsDeleted() == 1).count();
            }
            list.add(new VerifyStatusCountOutputDto(RegisgterHeadStatusEnum.ACTIVE.getCode(), activeCount));
            list.add(new VerifyStatusCountOutputDto(RegisgterHeadStatusEnum.UNACTIVE.getCode(), unactiveCount));
            list.add(new VerifyStatusCountOutputDto(RegisgterHeadStatusEnum.DELETED.getCode(), deleteCount));
            return BaseOutput.success().setData(list);

        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("操作失败：服务端出错");
        }
    }

    /**
     * 保存多个进门主台账单
     *
     * @param createListRegisterHeadParam 主台账单信息
     * @return 主台账单ID集合
     */
    @ApiOperation("保存多个进门主台账单")
    @RequestMapping(value = "/createRegisterHeadList.api", method = RequestMethod.POST)
    public BaseOutput<List<Long>> createRegisterHeadList(@RequestBody CreateListRegisterHeadParam createListRegisterHeadParam) {
        logger.info("保存多个进门主台账单:{}", JSON.toJSONString(createListRegisterHeadParam));
        if (createListRegisterHeadParam == null || createListRegisterHeadParam.getRegisterBills() == null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            Long userId = sessionContext.getAccountId();
            String userName = sessionContext.getUserName();

            List<CreateRegisterHeadInputDto> registerHeads = StreamEx.of(createListRegisterHeadParam.getRegisterBills())
                    .nonNull().map(dto->{
                        dto.setUserId(userId);
                        return dto;
                    }).toList();
            if (registerHeads.isEmpty()) {
                return BaseOutput.failure("没有进门主台账单");
            }
            logger.info("保存多个进门主台账单操作用户:{}，{}", userId, userName);
            List<Long> idList = this.registerHeadService.createRegisterHeadList(registerHeads,
                     Optional.empty(), sessionContext.getSessionData().getMarketId());
            return BaseOutput.success().setData(idList);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

    /**
     * 修改进门主台账单
     *
     * @param dto 主台账单信息
     * @return 修改结果
     */
    @ApiOperation("修改进门主台账单")
    @RequestMapping(value = "/doEditRegisterHead.api", method = RequestMethod.POST)
    public BaseOutput doEditRegisterBill(@RequestBody CreateRegisterHeadInputDto dto) {
        logger.info("修改进门主台账单:{}", JSON.toJSONString(dto));
        if (dto == null || dto.getId() == null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            SessionData sessionData = this.sessionContext.getSessionData();

            OperatorUser operatorUser = new OperatorUser(sessionData.getUserId(),sessionData.getUserName());
            CustomerExtendDto customer = this.customerRpcService.findCustomerByIdOrEx(sessionData.getUserId(), sessionData.getMarketId());


            RegisterHead registerHead = dto.build(customer);
            logger.info("修改进门主台账单:{}", JSON.toJSONString(registerHead));
            this.registerHeadService.doEdit(registerHead, dto.getImageCertList(), Optional.ofNullable(operatorUser));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
        return BaseOutput.success();
    }

    /**
     * 作废进门主台账单
     *
     * @param dto 主台账单信息
     * @return 作废结果
     */
    @ApiOperation("作废进门主台账单")
    @RequestMapping(value = "/doDeleteRegisterHead.api", method = RequestMethod.POST)
    public BaseOutput doDeleteRegisterHead(@RequestBody CreateRegisterHeadInputDto dto) {
        logger.info("作废进门主台账单:{}", JSON.toJSONString(dto));
        if (dto == null || dto.getId() == null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            SessionData sessionData = this.sessionContext.getSessionData();

            OperatorUser operatorUser = new OperatorUser(sessionData.getUserId(),sessionData.getUserName());
            logger.info("作废进门主台账单:billId:{},userId:{}", dto.getId(), operatorUser.getId());
            this.registerHeadService.doDelete(dto, operatorUser.getId(), Optional.ofNullable(operatorUser));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
        return BaseOutput.success();
    }

    /**
     * 启用/关闭进门主台账单
     *
     * @param dto 主台账单信息
     * @return 启用/关闭结果
     */
    @ApiOperation("启用/关闭进门主台账单")
    @RequestMapping(value = "/doUpdateActiveRegisterHead.api", method = RequestMethod.POST)
    public BaseOutput doUpdateActiveRegisterHead(@RequestBody CreateRegisterHeadInputDto dto) {
        logger.info("启用/关闭进门主台账单:{}", JSON.toJSONString(dto));
        if (dto == null || dto.getId() == null||dto.getActive()==null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            SessionData sessionData = this.sessionContext.getSessionData();

            logger.info("启用/关闭进门主台账单:billId:{},userId:{}", dto.getId(), sessionData.getUserId());
            this.registerHeadService.doUpdateActive(dto, sessionData.getUserId(), sessionData.getOptUser());
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
        return BaseOutput.success();
    }

    /**
     * 查看进门主台账单
     *
     * @param baseDomain 主台账单信息（ID）
     * @return 主台账单详情
     */
    @ApiOperation("查看进门主台账单")
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/viewRegisterHead.api", method = {RequestMethod.POST})
    public BaseOutput<RegisterHead> viewRegisterHead(@RequestBody BaseDomain baseDomain) {
        try {
            RegisterHead registerHead = registerHeadService.get(baseDomain.getId());
            if(registerHead==null){
                return BaseOutput.failure("没有数据");
            }

            List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(baseDomain.getId(), BillTypeEnum.MASTER_BILL.getCode());
            registerHead.setImageCertList(imageCerts);

            UpStream upStream = upStreamService.get(registerHead.getUpStreamId());
            registerHead.setUpStreamName(upStream.getName());

            RegisterBill registerBill = new RegisterBill();
            registerBill.setRegisterHeadCode(registerHead.getCode());
            List<RegisterBill> registerBills = registerBillService.listByExample(registerBill);
            registerHead.setRegisterBills(registerBills);
            return BaseOutput.success().setData(registerHead);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("查询进门主台账单数据出错", e);
            return BaseOutput.failure("查询进门主台账单数据出错");
        }
    }

    /**
     * 分批详情
     *
     * @param code 主台账单编号
     * @return 主台账单分批详情列表
     */
    @ApiOperation("分批详情")
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/viewPartialRegisterHead.api", method = {RequestMethod.GET})
    public BaseOutput<RegisterHead> viewPartialRegisterHead(@RequestParam String code) {
        try {
            RegisterHead registerHead = new RegisterHead();
            registerHead.setCode(code);
            List<RegisterHead> registerHeadList = registerHeadService.listByExample(registerHead);
            if (CollectionUtils.isNotEmpty(registerHeadList)) {
                registerHead = registerHeadList.get(0);
            } else {
                return BaseOutput.failure("没有进门主台账单");
            }
            // 解析状态输出到前台
            if (registerHead.getActive() != null && YesOrNoEnum.YES.getCode().equals(registerHead.getActive())) {
                registerHead.setStatusStr(RegisgterHeadStatusEnum.ACTIVE.getDesc());
            } else if (registerHead.getActive() != null && YesOrNoEnum.NO.getCode().equals(registerHead.getActive())) {
                registerHead.setStatusStr(RegisgterHeadStatusEnum.UNACTIVE.getDesc());
            }
            if (registerHead.getIsDeleted() != null && YesOrNoEnum.YES.getCode().equals(registerHead.getIsDeleted())) {
                registerHead.setStatusStr(RegisgterHeadStatusEnum.DELETED.getDesc());
            }
            UpStream upStream = upStreamService.get(registerHead.getUpStreamId());
            registerHead.setUpStreamName(upStream.getName());
            RegisterBill registerBill = new RegisterBill();
            registerBill.setRegisterHeadCode(code);
            List<RegisterBill> registerBills = registerBillService.listByExample(registerBill);
            if (null != registerBills && CollectionUtils.isNotEmpty(registerBills)) {
                registerBills.forEach(e -> {
                    List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(e.getBillId(), BillTypeEnum.REGISTER_BILL.getCode());
                    e.setImageCerts(imageCerts);
                    UpStream u = upStreamService.get(e.getUpStreamId());
                    e.setUpStreamName(u.getName());
                });
            }
            registerHead.setRegisterBills(registerBills);
            return BaseOutput.success().setData(registerHead);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("查询分批详情出错", e);
            return BaseOutput.failure("查询分批详情数据出错");
        }
    }
}
