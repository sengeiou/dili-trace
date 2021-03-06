package com.dili.trace.api.client;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.util.DateUtils;
import com.dili.trace.api.input.CreateDetectRequestInputDto;
import com.dili.trace.api.input.DetectRequestInputDto;
import com.dili.trace.api.input.DetectRequestQueryDto;
import com.dili.trace.api.output.CountDetectStatusDto;
import com.dili.trace.api.output.SampleSourceCountOutputDto;
import com.dili.trace.api.output.SampleSourceListOutputDto;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.RegisterTallyAreaNo;
import com.dili.trace.dto.DetectRecordInputDto;
import com.dili.trace.dto.DetectRequestOutDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectStatusEnum;
import com.dili.trace.enums.DetectTypeEnum;
import com.dili.trace.glossary.SampleSourceEnum;
import com.dili.trace.service.*;
import com.dili.trace.util.DetectRecordUtil;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * ?????????(??????)???????????????
 */
@RestController()
@RequestMapping("/api/client/clientDetectRequest")
@AppAccess(role = Role.Client)
@Api(value = "/api/client/clientDetectRequest", description = "(??????)???????????????")
public class ClientDetectRequestApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientDetectRequestApi.class);

    @Autowired
    LoginSessionContext loginSessionContext;
    @Autowired
    DetectRequestService detectRequestService;
    @Autowired
    BillService billService;
    @Autowired
    DetectRecordService detectRecordService;
    @Autowired
    UserRpcService userRpcService;
    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    CommissionBillService commissionBillService;
    @Autowired
    RegisterTallyAreaNoService registerTallyAreaNoService;

//    /**
//     * ???????????????????????????
//     *
//     * @param createListBillParam ??????????????????????????????
//     * @return ????????????
//     */
//    @RequestMapping(value = "/createCommissionBill.api", method = RequestMethod.POST)
//    public BaseOutput<?> createCommissionBill(@RequestBody CreateListBillParam createListBillParam) {
//
//        try {
//            SessionData sessionData = this.sessionContext.getSessionData();
//
//            Long userId = sessionData.getUserId();
//            List<CreateRegisterBillInputDto> inputList = StreamEx.ofNullable(createListBillParam).filter(Objects::nonNull).map(CreateListBillParam::getRegisterBills).nonNull().flatCollection(Function.identity()).map(bill -> {
//                bill.setCreationSource(RegisterBilCreationSourceEnum.WX.getCode());
//                bill.setUserId(userId);
//                return bill;
//            }).toList();
//            if (inputList.isEmpty()) {
//                return BaseOutput.failure("????????????");
//            }
//           List<RegisterBill>billList= StreamEx.of(inputList).map(input->{
//                logger.info("?????????????????????:" + JSON.toJSONString(input));
//                RegisterBill registerBill = new RegisterBill();
//
//                registerBill.setUserId(this.sessionContext.getSessionData().getUserId());
//                registerBill.setName(this.sessionContext.getSessionData().getUserName());
//
//                if (registerBill.getRegisterSource() == null) {
//                    // ????????????????????????
//                    registerBill.setRegisterSource(RegisterSourceEnum.TALLY_AREA.getCode());
//                }
//
//                registerBill.setCreationSource(RegisterBilCreationSourceEnum.WX.getCode());
//
//               registerBill.setIsPrintCheckSheet(input.getIsPrintCheckSheet());
//                return registerBill;
//            }).toList();
//
//            List<RegisterBill> outlist = this.commissionBillService.createCommissionBillByUser(billList);
//            return BaseOutput.success();
//        } catch (TraceBizException e) {
//            return BaseOutput.failure(e.getMessage());
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return BaseOutput.failure("???????????????");
//        }
//
//    }

    /**
     * ???????????????
     *
     * @param input
     * @return
     */
    @RequestMapping("/createDetectRequest.api")
    public BaseOutput<Long> createDetectRequest(@RequestBody DetectRequestInputDto input) {
        if (input == null || input.getBillId() == null) {
            return BaseOutput.failure("????????????");
        }
        try {
            Optional<OperatorUser> optUser = this.sessionContext.getSessionData().getOptUser();
            OperatorUser operatorUser = optUser.orElseThrow(() -> {
                return new TraceBizException("???????????????");
            });
            DetectRequest item = this.detectRequestService.createDetectRequestForBill(input, operatorUser);

            return BaseOutput.successData(item.getId());
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }

    }

//    /**
//     * ???????????????????????????
//     *
//     * @param input
//     * @return
//     */
//    @RequestMapping("/createOffSiteDetectRequest.api")
//    public BaseOutput<Long> createOffSiteDetectRequest(@RequestBody DetectRequestDto input) {
//        if (input == null) {
//            return BaseOutput.failure("????????????");
//        }
//        try {
//            Long userId = loginSessionContext.getSessionData().getUserId();
//            String userName = loginSessionContext.getSessionData().getUserName();
//            Long marketId = loginSessionContext.getSessionData().getMarketId();
//
//            if (null == userId) {
//                return BaseOutput.failure("????????????????????????");
//            }
//            input.setCreatorId(userId);
//            input.setCreatorName(userName);
//            input.setMarketId(marketId);
//            DetectRequest item = this.detectRequestService.createOffSiteDetectRequest(input, Optional.empty());
//            return BaseOutput.successData(item.getId());
//        } catch (TraceBizException e) {
//            return BaseOutput.failure(e.getMessage());
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return BaseOutput.failure("???????????????");
//        }
//
//    }

    /**
     * ?????????????????????????????????
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getBillDetailById.api", method = RequestMethod.GET)
    public BaseOutput<RegisterBill> getBillDetailById(Long id) {
        if (id == null) {
            return BaseOutput.failure("????????????");
        }
        try {
            return BaseOutput.successData(billService.get(id));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }
    }

    /**
     * ???????????????
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getDetectRequestDetail.api", method = RequestMethod.GET)
    public BaseOutput getDetectRequestDetail(Long id) {
        BaseOutput<DetectRequestOutDto> output = null;
        try {
            DetectRequestQueryDto detectRequest = new DetectRequestQueryDto();
            detectRequest.setId(id);
            DetectRequestOutDto detail = detectRequestService.getDetectRequestDetail(detectRequest);


            List<String> arrivalTallynos = StreamEx.ofNullable(detail.getBillCode()).filter(StringUtils::isNotBlank).map(this.billService::findByCode)
                    .nonNull().flatCollection(bill ->
                            this.registerTallyAreaNoService.findTallyAreaNoByBillIdAndType(bill.getBillId(), BillTypeEnum.REGISTER_BILL)
                    ).nonNull().map(RegisterTallyAreaNo::getTallyareaNo).toList();

            detail.setArrivalTallynos(arrivalTallynos);

            output = BaseOutput.successData(detail);
        } catch (TraceBizException e) {
            output = BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            output = BaseOutput.failure("???????????????");
        }
        return output;
    }


    /**
     * ????????????????????????
     *
     * @param likeUserName
     * @return
     */
    @RequestMapping(value = "/getDetectUsers.api", method = RequestMethod.GET)
    public BaseOutput getDetectUsers(@RequestParam(name = "likeUserName") String likeUserName) {
        try {
            Long marketId = this.sessionContext.getSessionData().getMarketId();
            List<User> users = userRpcService.findDetectDepartmentUsers(likeUserName, marketId);
            return BaseOutput.successData(users);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }
    }

    /**
     * ????????????????????????
     *
     * @param detectRequestDto
     * @return
     */
    @RequestMapping("/listPagedDetectRequest.api")
    public BaseOutput<BasePage<DetectRequestOutDto>> listPagedDetectRequest(@RequestBody DetectRequestQueryDto detectRequestDto) {

        List<Integer> detectStatusList = StreamEx.ofNullable(detectRequestDto.getDetectStatusList())
                .flatCollection(Function.identity()).nonNull().toList();
//        if (null == detectRequestDto.getDetectStatus() && detectStatusList.isEmpty()) {
//            return BaseOutput.failure("????????????");
//        }
        detectRequestDto.setMarketId(this.sessionContext.getSessionData().getMarketId());
        detectRequestDto.setUserId(this.sessionContext.getSessionData().getUserId());
        detectRequestDto.setIsDeleted(YesOrNoEnum.NO.getCode());
        detectRequestDto.setDetectStatusList(detectStatusList);

        BasePage<DetectRequestOutDto> basePage = detectRequestService.listPageByUserCategory(detectRequestDto);
        return BaseOutput.success().setData(basePage);
    }

    /**
     * ????????????????????????
     *
     * @param detectRequestDto
     * @return
     */
    @RequestMapping("/getDetectRequest.api")
    public BaseOutput getDetectRequest(@RequestBody DetectRequestQueryDto detectRequestDto) {
        if (null == detectRequestDto.getId()) {
            return BaseOutput.failure("????????????");
        }
        try {
            return BaseOutput.success().setData(detectRequestService.getDetectRequestDetail(detectRequestDto));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("????????????");
        }

    }

    /**
     * ??????????????????
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/returnDetectRequest.api", method = RequestMethod.GET)
    public BaseOutput returnDetectRequest(Long id) {
        if (null == id) {
            return BaseOutput.failure("????????????");
        }
        try {
            RegisterBill bill = getBillByDetectRequestId(id);
            if (bill == null) {
                return BaseOutput.failure("???????????????");
            }
            if (!DetectStatusEnum.NONE.equalsToCode(bill.getDetectStatus())
                    && !DetectStatusEnum.WAIT_DESIGNATED.equalsToCode(bill.getDetectStatus())) {
                return BaseOutput.failure("???????????????,????????????");
            }
            RegisterBill upBill = new RegisterBill();
            upBill.setDetectStatus(DetectStatusEnum.RETURN_DETECT.getCode());
            upBill.setId(bill.getId());
            billService.updateSelective(upBill);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure().setData(e.getMessage());
        }
        return BaseOutput.success();
    }

    /**
     * ??????????????????
     *
     * @param detectRequestDto
     * @return
     */
    @RequestMapping("/receiveDetectRequest.api")
    public BaseOutput receiveDetectRequest(@RequestBody DetectRequest detectRequestDto) {
        if (null == detectRequestDto.getId()) {
            return BaseOutput.failure("????????????");
        }
        try {
            RegisterBill bill = getBillByDetectRequestId(detectRequestDto.getId());
            //??????????????????????????????????????????
            if (null == detectRequestDto.getDetectTime()) {
                detectRequestDto.setDetectTime(DateUtils.getCurrentDate());
            }
            SessionData sessionData = this.sessionContext.getSessionData();

            //????????????????????????????????????????????????
            if (null == detectRequestDto.getDetectorId()) {
                detectRequestDto.setDetectorId(sessionData.getUserId());
                detectRequestDto.setDetectorName(sessionData.getUserName());
            }
            detectRequestService.receiveDetectRequest(bill.getId(), detectRequestDto);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure().setData(e.getMessage());
        }
        return BaseOutput.success();
    }

    /**
     * ????????????????????????
     *
     * @param queryInput
     * @return
     */
    @RequestMapping("/countByDetectStatus.api")
    public BaseOutput<List<CountDetectStatusDto>> countByDetectStatus(@RequestBody DetectRequestQueryDto queryInput) {
        queryInput.setMarketId(this.sessionContext.getSessionData().getMarketId());
        queryInput.setUserId(this.sessionContext.getSessionData().getUserId());

        Map<Integer, Integer> statusCntMap = StreamEx.of(this.detectRequestService.countByDetectStatus(queryInput))
                .toMap(CountDetectStatusDto::getDetectStatus, CountDetectStatusDto::getCnt);

        List<CountDetectStatusDto> list = StreamEx.of(DetectStatusEnum.values()).map(e -> {
            CountDetectStatusDto dto = new CountDetectStatusDto();
            dto.setCnt(statusCntMap.getOrDefault(e.getCode(), 0));
            dto.setDetectStatus(e.getCode());
            return dto;
        }).toList();

        return BaseOutput.successData(list);
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @param id
     * @return
     */
    private RegisterBill getBillByDetectRequestId(Long id) {
        RegisterBill queBill = new RegisterBill();
        queBill.setDetectRequestId(id);
        List<RegisterBill> billList = billService.listByExample(queBill);
        if (CollectionUtils.isEmpty(billList)) {
            throw new TraceBizException("????????????????????????????????????");
        }
        RegisterBill bill = billList.get(0);
        if (!DetectStatusEnum.WAIT_DESIGNATED.equalsToCode(bill.getDetectStatus())) {
            throw new TraceBizException("???????????????????????????????????????");
        }
        return bill;
    }

    /**
     * ????????????????????????
     *
     * @param input
     * @return
     */
    @RequestMapping("/handleDetectRequest.api")
    public BaseOutput handleDetectRequest(@RequestBody CreateDetectRequestInputDto input) {
        if (input == null || input.getDetectRequest() == null) {
            return BaseOutput.failure("????????????");
        }
        DetectRequest detectRequest = input.getDetectRequest();
        if (detectRequest.getDetectSource() == null || detectRequest.getBillId() == null) {
            return BaseOutput.failure("????????????");
        }
        SampleSourceEnum sampleSourceEnum = SampleSourceEnum.fromCode(input.getDetectRequest().getDetectSource()).orElseThrow(() -> {
            return new TraceBizException("??????????????????");
        });
        if (sampleSourceEnum == SampleSourceEnum.MANUALLY) {
            if (input.getDetectRecord() == null) {
                return BaseOutput.failure("????????????");
            }
        }
        try {
            RegisterBill billItem = this.billService.getAvaiableBill(detectRequest.getBillId()).orElseThrow(() -> {
                throw new TraceBizException("???????????????");
            });

            detectRequest.setDetectResult(DetectResultEnum.NONE.getCode());
            detectRequest.setDetectType(DetectTypeEnum.NEW.getCode());
            detectRequest.setCreated(new Date());
            detectRequest.setModified(new Date());
            this.detectRequestService.insertSelective(detectRequest);

            //????????????????????????
            if (sampleSourceEnum == SampleSourceEnum.MANUALLY) {
                DetectRecord detectRecord = input.getDetectRecord();
                detectRecord.setRegisterBillCode(billItem.getCode());
                this.detectRecordService.insertSelective(detectRecord);
                RegisterBill bill = new RegisterBill();
            }

            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("???????????????");
        }


    }

    /**
     * ????????????-??????
     *
     * @param query
     * @return
     */
    @RequestMapping("/listPagedSampleSourceDetect.api")
    public BaseOutput<BasePage<SampleSourceListOutputDto>> listPagedSampleSourceDetect(@RequestBody DetectRequestQueryDto query) {
        try {
            query.setIsDeleted(YesOrNoEnum.NO.getCode());
            query.setUserId(this.sessionContext.getSessionData().getUserId());
            query.setMarketId(this.sessionContext.getSessionData().getMarketId());
            BasePage<SampleSourceListOutputDto> basePage = this.detectRequestService.listPagedSampleSourceDetect(query);
            return BaseOutput.success().setData(basePage);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("??????????????????????????????");
        }
    }

    /**
     * ????????????-????????????????????????
     */
    @RequestMapping(value = "/countBySampleSource.api", method = {RequestMethod.POST})
    public BaseOutput<List<VerifyStatusCountOutputDto>> countBySampleSource(@RequestBody DetectRequestQueryDto query) {
        try {
            query.setMarketId(this.sessionContext.getSessionData().getMarketId());
            query.setUserId(this.sessionContext.getSessionData().getUserId());
            List<SampleSourceCountOutputDto> list = this.detectRequestService.countBySampleSource(query);
            return BaseOutput.success().setData(list);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("??????????????????????????????");
        }
    }

    /**
     * ????????????-????????????
     */
    @RequestMapping(value = "/getSampleSourceDetectDetail.api", method = {RequestMethod.GET})
    public BaseOutput<RegisterBill> getSampleSourceDetectDetail(@RequestParam(name = "id", required = true) Long id) {
        try {
            DetectRequest detectRequest = this.detectRequestService.get(id);
            RegisterBill registerBill = this.billService.getAvaiableBill(detectRequest.getBillId()).orElse(null);
            return BaseOutput.success().setData(registerBill);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("??????????????????????????????");
        }
    }

    /**
     * ????????????-????????????????????????
     */
    @RequestMapping(value = "/createDetectRecord.api", method = {RequestMethod.POST})
    public BaseOutput<Long> createDetectRecord(@RequestBody DetectRecordInputDto input) {
        try {
            if (StringUtils.isBlank(input.getRegisterBillCode())) {
                logger.error("???????????????????????????????????????");
                return BaseOutput.failure("????????????????????????");
            }
            if (StringUtils.isBlank(input.getDetectOperator())) {
                logger.error("?????????????????????????????????????????????");
                return BaseOutput.failure("???????????????????????????");
            }
            DetectResultEnum detectResultEnum = DetectResultEnum.fromCode(input.getDetectResult()).orElseThrow(() -> {
                return new TraceBizException("?????????????????????");
            });

            DetectTypeEnum detectTypeEnum = DetectTypeEnum.fromCode(input.getDetectType()).orElseThrow(() -> {
                return new TraceBizException("?????????????????????");
            });

            if (input.getDetectTime() == null) {
                logger.error("?????????????????????????????????????????????");
                return BaseOutput.failure("???????????????????????????");
            }
            if (StringUtils.isBlank(input.getPdResult())) {
                logger.error("??????????????????????????????????????????");
                return BaseOutput.failure("????????????????????????");
            }
            if (input.getDetectRequestId() == null) {
                logger.error("?????????????????????????????????????????????");
                return BaseOutput.failure("?????????????????????????????????");
            }
            input.setCreated(new Date());
            input.setModified(new Date());

            int i = this.detectRecordService.saveDetectRecordManually(input, this.sessionContext.getSessionData().getOptUser());
            return BaseOutput.success().setData(i);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("??????????????????????????????");
        }
    }

    /**
     * ????????????-????????????
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doAutoCheck.api", method = RequestMethod.GET)
    public BaseOutput doAutoCheck(@RequestParam(name = "id", required = true) Long id) {
        try {
            SessionData sessionData = this.sessionContext.getSessionData();
            if (sessionData == null) {
                throw new TraceBizException("???????????????");
            }
            registerBillService.autoCheckRegisterBill(id, sessionData.getOptUser().get());
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure().setData(e.getMessage());
        }
        return BaseOutput.success("????????????");
    }

    /**
     * ????????????-????????????
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doSamplingCheck.api", method = RequestMethod.GET)
    public BaseOutput doSamplingCheck(@RequestParam(name = "id", required = true) Long id) {
        try {
            SessionData sessionData = this.sessionContext.getSessionData();
            if (sessionData == null) {
                throw new TraceBizException("???????????????");
            }
            registerBillService.samplingCheckRegisterBill(id, sessionData.getOptUser().get());
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure().setData(e.getMessage());
        }
        return BaseOutput.success("????????????");
    }

    /**
     * ????????????-??????
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/doSpotCheck.api", method = RequestMethod.GET)
    public BaseOutput doSpotCheck(@RequestParam(name = "id", required = true) Long id) {
        try {
            SessionData sessionData = this.sessionContext.getSessionData();
            if (sessionData == null) {
                throw new TraceBizException("???????????????");
            }
            registerBillService.spotCheckRegisterBill(id, sessionData.getOptUser().get());
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure().setData(e.getMessage());
        }
        return BaseOutput.success("????????????");
    }
}
