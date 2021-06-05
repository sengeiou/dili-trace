package com.dili.trace.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.dili.customer.sdk.domain.dto.CustomerSimpleExtendDto;
import com.dili.trace.events.RegisterBillMessageEvent;
import com.dili.common.exception.TraceBizException;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.trace.enums.SalesTypeEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.dto.*;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.*;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.dili.trace.rpc.service.UidRestfulRpcService;
import one.util.streamex.StreamEx;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Service
public class SgRegisterBillServiceImpl implements SgRegisterBillService {
    private static final Logger logger = LoggerFactory.getLogger(SgRegisterBillServiceImpl.class);
    @Autowired
    UidRestfulRpcService uidRestfulRpcService;
    @Autowired
    QualityTraceTradeBillService qualityTraceTradeBillService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    DetectRecordService detectRecordService;
    @Autowired
    CustomerRpcService customerRpcService;
    @Autowired
    CodeGenerateService codeGenerateService;
    @Autowired
    UsualAddressService usualAddressService;
    @Autowired
    DetectTaskService detectTaskService;
    @Autowired
    BillService billService;
    @Autowired
    RegisterBillMapper billMapper;
    @Autowired
    ImageCertService imageCertService;
    @Autowired
    DetectRequestService detectRequestService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    UapRpcService uapRpcService;
    @Autowired
    UserQrHistoryService userQrHistoryService;

    @Autowired
    CheckinOutRecordService checkinOutRecordService;
    @Transactional
    @Override
    public Long createRegisterBill(RegisterBill inputBill, OperatorUser operatorUser) {

        inputBill.setHasDetectReport(0);
        inputBill.setHasOriginCertifiy(0);
        inputBill.setHasHandleResult(0);
        inputBill.setCreated(new Date());
        inputBill.setModified(new Date());
        inputBill.setIsDeleted(YesOrNoEnum.NO.getCode());
        inputBill.setVerifyType(VerifyTypeEnum.NONE.getCode());
        inputBill.setVerifyStatus(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
        inputBill.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        inputBill.setDetectStatus(DetectStatusEnum.NONE.getCode());
        inputBill.setIsPrintCheckSheet(YesOrNoEnum.NO.getCode());
        if (null == inputBill.getTruckTareWeight()) {
            inputBill.setTruckTareWeight(BigDecimal.ZERO);
        }
//        inputBill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());

        String code = uidRestfulRpcService.bizNumber(BizNumberType.REGISTER_BILL);
        logger.debug("registerBill.code={}", code);
        inputBill.setCode(code);

        if (inputBill.getRegisterSource().intValue() == RegisterSourceEnum.TRADE_AREA.getCode().intValue()) {
            // 交易区没有理货区号
            // 交易区数据直接进行待检测状态
            // registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
            // registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
        }
        if (StringUtils.isBlank(inputBill.getOperatorName())) {
            inputBill.setOperatorName(operatorUser.getName());
            inputBill.setOperatorId(operatorUser.getId());
        }

        inputBill.setIdCardNo(StringUtils.trimToEmpty(inputBill.getIdCardNo()).toUpperCase());
        // 车牌转大写
        inputBill.setPlate(StringUtils.trimToEmpty(inputBill.getPlate()).toUpperCase());
        this.checkPlate(inputBill);

        /*
         * else { List<String> otherUserPlateList = this.userPlateService
         * .findUserPlateByPlates(Arrays.asList(registerBill.getPlate())).stream().map(
         * UserPlate::getPlate) .collect(Collectors.toList()); if
         * (!otherUserPlateList.isEmpty()) { return
         * BaseOutput.failure("当前车牌号已经与其他用户绑定,请使用其他牌号"); } }
         */

        this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.REGISTER,
                inputBill.getOriginId());

        inputBill.setCheckinStatus(YesOrNoEnum.NO.getCode());
        int result = this.billService.saveOrUpdate(inputBill);
        this.billService.updateHasImage(inputBill.getId(), inputBill.getImageCertList(), BillTypeEnum.REGISTER_BILL);
        if (result == 0) {
            logger.error("新增登记单数据库执行失败" + JSON.toJSONString(inputBill));
            throw new TraceBizException("创建失败");
        }
        return inputBill.getId();
    }


    private void checkPlate(RegisterBill registerBill) {
        if (registerBill.getMarketId() == null) {
            throw new TraceBizException("市场不能为空");
        }
        if (RegisterSourceEnum.TALLY_AREA.equalsToCode(registerBill.getRegisterSource())) {
            List<Long> userIdList = StreamEx.of(this.customerRpcService.findCustomerByPlate(registerBill.getPlate(), registerBill.getMarketId()))
                    .map(CustomerSimpleExtendDto::getId).distinct().toList();
            if (!userIdList.isEmpty() && !userIdList.contains(registerBill.getUserId())) {
                throw new TraceBizException("当前车牌号已经与其他用户绑定,请使用其他牌号");
            }
        }
    }

    private BaseOutput checkBill(RegisterBill registerBill) {

        if (registerBill.getRegisterSource() == null || registerBill.getRegisterSource().intValue() == 0) {
            logger.error("登记来源不能为空");
            return BaseOutput.failure("登记来源不能为空");
        }
        if (StringUtils.isBlank(registerBill.getName())) {
            logger.error("业户姓名不能为空");
            return BaseOutput.failure("业户姓名不能为空");
        }
        if (StringUtils.isBlank(registerBill.getIdCardNo())) {
            logger.error("业户身份证号不能为空");
            return BaseOutput.failure("业户身份证号不能为空");
        }
//		if (StringUtils.isBlank(registerBill.getAddr())) {
//			LOGGER.error("业户身份证地址不能为空");
//			return BaseOutput.failure("业户身份证地址不能为空");
//		}
        if (StringUtils.isBlank(registerBill.getProductName())) {
            logger.error("商品名称不能为空");
            return BaseOutput.failure("商品名称不能为空");
        }
        if (StringUtils.isBlank(registerBill.getOriginName())) {
            logger.error("商品产地不能为空");
            return BaseOutput.failure("商品产地不能为空");
        }

        if (registerBill.getWeight() == null) {
            logger.error("商品重量不能为空");
            return BaseOutput.failure("商品重量不能为空");
        }

        if (registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TALLY_AREA.getCode().intValue()) {
            if (registerBill.getWeight().longValue() <= 0L) {
                logger.error("商品重量不能小于0");
                return BaseOutput.failure("商品重量不能小于0");
            }
        } else {
            if (registerBill.getWeight().longValue() < 0L) {
                logger.error("商品重量不能为负");
                return BaseOutput.failure("商品重量不能为负");
            }
        }

        return BaseOutput.success();
    }

    // @Override
    // public List<RegisterBill> findByExeMachineNo(String exeMachineNo, int
    // taskCount) {
    // List<RegisterBill> exist = getActualDao().findByExeMachineNo(exeMachineNo);
    // if (!exist.isEmpty()) {
    // LOGGER.info("获取的任务已经有相应的数量了" + taskCount);
    // if (exist.size() >= taskCount) {
    // return exist.subList(0, taskCount);
    // }
    // }

    // int fetchSize = taskCount - exist.size();
    // LOGGER.info("还需要再拿多少个：" + fetchSize);

    // List<Long> ids = getActualDao().findIdsByExeMachineNo(fetchSize);
    // StringBuilder sb = new StringBuilder();
    // sb.append(0);
    // for (Long id : ids) {
    // sb.append(",").append(id);
    // }
    // getActualDao().taskByExeMachineNo(exeMachineNo, sb.toString());
    // return getActualDao().findByExeMachineNo(exeMachineNo);
    // }


    @Override
    public RegisterBillOutputDto findByTradeNo(String tradeNo) {
        QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.findByTradeNo(tradeNo);
        if (qualityTraceTradeBill != null && StringUtils.isNotBlank(qualityTraceTradeBill.getRegisterBillCode())) {
            RegisterBill registerBill = new RegisterBill();
            registerBill.setCode(qualityTraceTradeBill.getRegisterBillCode());
            registerBill.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
            List<RegisterBill> list = this.billService.listByExample(registerBill);
            if (list != null && list.size() > 0) {
                RegisterBillOutputDto target = new RegisterBillOutputDto();
                BeanUtil.copyProperties(list.get(0), target);
                return target;
            }
        }
        return null;
    }

    //	@Override
//	public int matchDetectBind(QualityTraceTradeBill qualityTraceTradeBill) {
//
//		MatchDetectParam matchDetectParam = new MatchDetectParam();
//		matchDetectParam.setTradeNo(qualityTraceTradeBill.getOrderId());
//		matchDetectParam.setTradeTypeId(qualityTraceTradeBill.getTradetypeId());
//		matchDetectParam.setTradeTypeName(qualityTraceTradeBill.getTradetypeName());
//		matchDetectParam.setProductName(qualityTraceTradeBill.getProductName());
//		matchDetectParam.setIdCardNo(qualityTraceTradeBill.getSellerIDNo());
//		matchDetectParam.setEnd(qualityTraceTradeBill.getOrderPayDate());
//		Date start = new Date(qualityTraceTradeBill.getOrderPayDate().getTime() - (48 * 3600000));
//		matchDetectParam.setStart(start);
//		LOGGER.info("进行匹配:" + matchDetectParam.toString());
//		Long id = this.billMapper.findMatchDetectBind(matchDetectParam);
//		int rows = 0;
//		if (null != id) {
//			rows = this.billMapper.matchDetectBind(qualityTraceTradeBill.getOrderId(),
//					qualityTraceTradeBill.getNetWeight(), id);
//		}
//		return rows;
//	}

    @Transactional
    @Override
    public int undoRegisterBill(Long id, OperatorUser operatorUser) {
        return billService.getAvaiableBill(id).map(item -> {

            // 待审核、已退回 可以撤销
            if (BillVerifyStatusEnum.RETURNED.equalsToCode(item.getVerifyStatus()) ||
                    BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(item.getVerifyStatus())) {

                RegisterBill bill = new RegisterBill();
                bill.setId(item.getBillId());
                bill.setIsDeleted(YesOrNoEnum.YES.getCode());
                return this.billService.updateSelective(bill);
            } else {
                throw new TraceBizException("操作失败，数据状态已改变");
            }
        }).orElse(0);

    }



    @Transactional
    @Override
    public BaseOutput doBatchUndo(List<Long> idList, OperatorUser operatorUser) {

        StreamEx.of(idList).forEach(registerBillId -> {
            try {
                this.undoRegisterBill(registerBillId, operatorUser);
            } catch (TraceBizException e) {
                logger.warn(e.getMessage());
            }

        });
        return BaseOutput.success();
    }




    @Transactional
    @Override
    public int reviewCheckRegisterBill(Long id, OperatorUser operatorUser) {
        RegisterBill registerBill = this.billService.getAvaiableBill(id).orElseThrow(() -> {
            return new TraceBizException("数据不存在或已删除");
        });
        if (!DetectStatusEnum.FINISH_DETECT.equalsToCode(registerBill.getDetectStatus())) {
            throw new TraceBizException("操作失败，数据状态已改变");
        }

        Integer hasHandleResult = registerBill.getHasHandleResult();
        DetectRequest detectRequest = this.detectRequestService.get(registerBill.getDetectRequestId());

        if (!DetectResultEnum.FAILED.equalsToCode(detectRequest.getDetectResult())) {
            throw new TraceBizException("操作失败，数据状态已改变");
        }
        Predicate<DetectRequest> pred = (DetectRequest item) -> {

            if (DetectTypeEnum.INITIAL_CHECK.equalsToCode(detectRequest.getDetectType())) {
                return true;
            } else if (DetectTypeEnum.RECHECK.equalsToCode(detectRequest.getDetectType()) && hasHandleResult.equals(0)) {
                return true;
            }
            return false;
        };

        boolean updateState = Optional.of(detectRequest).stream().anyMatch(pred);
        if (updateState) {
            registerBill.setId(registerBill.getId());

            registerBill.setOperatorName(operatorUser.getName());
            registerBill.setOperatorId(operatorUser.getId());


            try {
                DetectRequest newDetectRequest = new DetectRequest();
                BeanUtils.copyProperties(newDetectRequest, detectRequest);
                newDetectRequest.setId(null);
//                newDetectRequest.setDetectResult(DetectResultEnum.NONE.getCode());
//                newDetectRequest.setDetectType(DetectTypeEnum.RECHECK.getCode());
                newDetectRequest.setDesignatedName(null);
                newDetectRequest.setDesignatedId(null);
                this.detectRequestService.insertSelective(newDetectRequest);
                registerBill.setDetectRequestId(newDetectRequest.getId());
            } catch (Exception e) {
                throw new TraceBizException("处理失败");
            }

            registerBill.setDetectStatus(DetectStatusEnum.WAIT_DETECT.getCode());
            registerBill.setExeMachineNo(null);
            return this.billService.update(registerBill);
        } else {
            throw new TraceBizException("操作失败，数据状态已改变");
        }
    }




    @Override
    public Long doUploadHandleResult(RegisterBill input) {
        if (input == null || input.getId() == null
                || StringUtils.isAnyBlank(input.getHandleResult())) {
            throw new TraceBizException("参数错误");
        }
        List<ImageCert> imageCertList = StreamEx.ofNullable(input.getImageCertList()).flatCollection(Function.identity())
                .nonNull().toList();
        if (!imageCertList.isEmpty()) {
            imageCertList = StreamEx.of(imageCertList).filter(img -> {
                // 只取uid不为空，并且类型为处理结果的照片
                return StringUtils.isNotBlank(img.getUid()) && ImageCertTypeEnum.Handle_Result.equalsToCode(img.getCertType());
            }).toList();
        }
        if (imageCertList.isEmpty()) {
            throw new TraceBizException("请上传报告");
        }

        if (input.getHandleResult().trim().length() > 1000) {
            throw new TraceBizException("处理结果不能超过1000");
        }
        RegisterBill item = this.billService.getAvaiableBill(input.getId()).orElseThrow(() -> {
            return new TraceBizException("数据不存在或已删除");
        });

        List<ImageCert> imageCerts =
                StreamEx.of(this.findImageCertListByBillId(item.getBillId())).filter(img -> {
                    Integer cerType = img.getCertType();
                    return !ImageCertTypeEnum.Handle_Result.equalsToCode(cerType);
                }).append(imageCertList).toList();

        RegisterBill example = new RegisterBill();
        example.setId(item.getId());
        example.setHandleResult(input.getHandleResult());
        this.billService.updateSelective(example);
        this.billService.updateHasImage(item.getBillId(), imageCerts, BillTypeEnum.REGISTER_BILL);

        return example.getId();
    }

    // @Override
    // public Long doModifyRegisterBill(RegisterBill input) {
    // if (input == null || input.getId() == null) {
    // throw new TraceBizException("参数错误");
    // }
    // if (StringUtils.isBlank(input.getOriginCertifiyUrl()) &&
    // StringUtils.isBlank(input.getDetectReportUrl())) {
    // throw new TraceBizException("请上传报告");
    // }
    // RegisterBill item = this.get(input.getId());
    // if (item == null) {
    // throw new TraceBizException("数据错误");
    // }
    //
    // RegisterBill example =  new RegisterBill();
    // example.setId(item.getId());
    // example.setOriginCertifiyUrl(StringUtils.trimToNull(input.getOriginCertifiyUrl()));
    // example.setDetectReportUrl(StringUtils.trimToNull(input.getDetectReportUrl()));
    // this.updateSelective(example);
    //
    // return example.getId();
    // }

    @Override
    public Long doAuditWithoutDetect(RegisterBill input) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("参数错误");
        }
        RegisterBill registerBill = this.billService.getAvaiableBill(input.getId()).orElseThrow(() -> {
            return new TraceBizException("数据不存在或已删除");
        });
        if (registerBill.getImageCertList() == null || registerBill.getImageCertList().isEmpty()) {
            throw new TraceBizException("请上传产地证明");
        }

        if (!BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(registerBill.getVerifyStatus())) {
            throw new TraceBizException("数据状态错误");
        }
        if (!RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
            throw new TraceBizException("数据来源错误");

        }
        registerBill.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
        // registerBill.setDetectState(null);
        this.billService.updateSelective(registerBill);

        return registerBill.getId();
    }


    @Override
    public Long doUploadOrigincertifiy(RegisterBill input) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("参数错误");
        }
        List<ImageCert> imageCertList = StreamEx.ofNullable(input.getImageCertList()).nonNull().flatCollection(Function.identity()).nonNull().toList();
        if (!imageCertList.isEmpty()) {
            imageCertList = StreamEx.of(imageCertList).filter(img -> {
                // 只取uid不为空，并且类型为处理结果的照片
                return StringUtils.isNotBlank(img.getUid()) && ImageCertTypeEnum.ORIGIN_CERTIFIY.equalsToCode(img.getCertType());
            }).toList();
        }
        if (imageCertList.isEmpty()) {
            throw new TraceBizException("请上传报告");
        }

        RegisterBill item = this.billService.getAvaiableBill(input.getId()).orElseThrow(() -> {
            return new TraceBizException("数据不存在或已删除");
        });
        List<ImageCert> imageCerts =
                StreamEx.of(this.findImageCertListByBillId(item.getBillId())).filter(img -> {
                    Integer cerType = img.getCertType();
                    return !ImageCertTypeEnum.ORIGIN_CERTIFIY.equalsToCode(cerType);
                }).append(imageCertList).toList();
        this.billService.updateHasImage(item.getBillId(), imageCerts, BillTypeEnum.REGISTER_BILL);
        return item.getBillId();
    }



    private RegisterBillDto preBuildDTO(RegisterBillDto dto) {
        if (StringUtils.isNotBlank(dto.getAttrValue())) {
            switch (dto.getAttr()) {
                case "code":
                    dto.setCode(dto.getAttrValue());
                    break;
                // case "plate":
                // registerBill.setPlate(registerBill.getAttrValue());
                // break;
                // case "tallyAreaNo":
                //// registerBill.setTallyAreaNo(registerBill.getAttrValue());
                // registerBill.setLikeTallyAreaNo(registerBill.getAttrValue());
                // break;
                case "latestDetectOperator":
                    dto.setLatestDetectOperator(dto.getAttrValue());
                    break;
                case "name":
                    dto.setName(dto.getAttrValue());
                    break;
                case "productName":
                    dto.setProductName(dto.getAttrValue());
                    break;
                case "likeSampleCode":
                    dto.setLikeSampleCode(dto.getAttrValue());
                    break;
            }
        }
        // if (registerBill.getHasReport() != null) {
        // if (registerBill.getHasReport()) {
        // registerBill.mset(IDTO.AND_CONDITION_EXPR,
        // " (detect_report_url is not null AND detect_report_url<>'')");
        // } else {
        // registerBill.mset(IDTO.AND_CONDITION_EXPR, " (detect_report_url is null or
        // detect_report_url='')");
        // }
        // }

        StringBuilder sql = this.buildDynamicCondition(dto);
        if (sql.length() > 0) {
            dto.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
        }

        return dto;
    }



    private StringBuilder buildDynamicCondition(RegisterBillDto registerBill) {
        StringBuilder sql = new StringBuilder();
        if (registerBill.getHasDetectReport() != null) {
//            if (registerBill.getHasDetectReport()) {
//                sql.append("  (detect_report_url is not null AND detect_report_url<>'') ");
//            } else {
//                sql.append("  (detect_report_url is  null or detect_report_url='') ");
//            }
        }

        if (registerBill.getHasOriginCertifiy() != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
//            if (registerBill.getHasOriginCertifiy()) {
//                sql.append("  (origin_certifiy_url is not null AND origin_certifiy_url<>'') ");
//            } else {
//                sql.append("  (origin_certifiy_url is  null or origin_certifiy_url='') ");
//            }
        }

        if (registerBill.getHasHandleResult() != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
//            if (registerBill.getHasHandleResult()) {
//                sql.append("  (handle_result is not null AND handle_result<>'') ");
//            } else {
//                sql.append("  (handle_result is  null or handle_result='') ");
//            }
        }
        if (registerBill.getHasCheckSheet() != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            if (registerBill.getHasCheckSheet()) {
                sql.append("  (check_sheet_id is not null) ");
            } else {
                sql.append("  (check_sheet_id is null) ");
            }
        }
        return sql;
    }


    @Override
    public int createRegisterBillList(List<RegisterBill> registerBillList, OperatorUser operatorUser) {
        return StreamEx.ofNullable(registerBillList).flatCollection(Function.identity()).nonNull().map(rb -> {
            Long registerBillId = this.createRegisterBill(rb, operatorUser);
            // 寿光管理端，新增完报备单的同时新增检测请求
            DetectRequest item = this.detectRequestService.createByBillId(registerBillId, DetectTypeEnum.NEW, operatorUser, Optional.empty());

            DetectRequest updatable = new DetectRequest();
            updatable.setId(item.getId());
            // 维护接单时间
            updatable.setConfirmTime(new Date());
            // 维护检测编号
            updatable.setDetectCode(uidRestfulRpcService.detectRequestBizNumber(operatorUser.getMarketName()));
            this.detectRequestService.updateSelective(updatable);

            RegisterBill bill = this.billService.get(item.getBillId());
            bill.setOperatorName(operatorUser.getName());
            bill.setOperatorId(operatorUser.getId());
            bill.setDetectStatus(DetectStatusEnum.NONE.getCode()); // 新增完为：待采样
            this.billService.update(bill);
            return 1;
        }).mapToInt(Integer::valueOf).sum();
    }




    @Override
    public List<ImageCert> findImageCertListByBillId(Long billId) {
        return this.imageCertService.findImageCertListByBillId(billId, BillTypeEnum.REGISTER_BILL);
    }


    @Override
    public List<RegisterBillMessageEvent> queryEvents(Long billId) {
        if (billId == null) {
            return Lists.newArrayList();
        }
        RegisterBill item = this.billService.getAvaiableBill(billId).orElse(null);
        if (item == null) {
            return Lists.newArrayList();
        }
//        if(BillTypeEnum.E_COMMERCE_BILL.equalsToCode(item.getBillType())){
//            return Lists.newArrayList();
//        }
        List<RegisterBillMessageEvent> msgStream = Lists.newArrayList(
                RegisterBillMessageEvent.COPY
                , RegisterBillMessageEvent.DETAIL
                , RegisterBillMessageEvent.upload_origincertifiy
                , RegisterBillMessageEvent.upload_handleresult
        );


        if (DetectStatusEnum.NONE.equalsToCode(item.getDetectStatus())) {

            if (BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(item.getVerifyStatus())
                    || BillVerifyStatusEnum.RETURNED.equalsToCode(item.getVerifyStatus())) {
                msgStream.add(RegisterBillMessageEvent.edit);
                msgStream.add(RegisterBillMessageEvent.updateImage);
            }
            if (BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(item.getVerifyStatus())) {
                msgStream.add(RegisterBillMessageEvent.undo);
            }

        }


        if (BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(item.getVerifyStatus())) {
            msgStream.add(RegisterBillMessageEvent.upload_detectreport);
            // 补单PC端不审核
            if (!RegistTypeEnum.SUPPLEMENT.equalsToCode(item.getRegistType())) {
                msgStream.add(RegisterBillMessageEvent.audit);
            }
        }
        if (item.getHasOriginCertifiy() > 0) {
            msgStream.add(RegisterBillMessageEvent.remove_reportAndcertifiy);
        }
        if (RegisterSourceEnum.TALLY_AREA.equalsToCode(item.getRegisterSource()) && item.getHasOriginCertifiy() > 0) {
            msgStream.add(RegisterBillMessageEvent.audit_withoutDetect);
        }
//        if (DetectStatusEnum.WAIT_SAMPLE.equalsToCode(item.getDetectStatus())) {
//            msgStream.addAll(Lists.newArrayList(RegisterBillMessageEvent.undo));
//        }
        if (item.getDetectRequestId() != null) {
            DetectRequest detectRequest = this.detectRequestService.get(item.getDetectRequestId());
            if (detectRequest != null) {

                if (DetectResultEnum.FAILED.equalsToCode(detectRequest.getDetectResult())) {
                    if (DetectTypeEnum.INITIAL_CHECK.equalsToCode(detectRequest.getDetectType())) {
                        msgStream.add(RegisterBillMessageEvent.review);
                    } else if (DetectTypeEnum.RECHECK.equalsToCode(detectRequest.getDetectType()) && item.getHasHandleResult() == 0) {
                        msgStream.add(RegisterBillMessageEvent.review);
                    }
                } else if (DetectResultEnum.PASSED.equalsToCode(detectRequest.getDetectResult())) {
                    if (item.getCheckSheetId() == null) {
                        msgStream.add(RegisterBillMessageEvent.createsheet);
                    }
                }
            }
        }
        if(!RegistTypeEnum.SUPPLEMENT.equalsToCode(item.getRegistType())){
            boolean noCheckinRecord=this.checkinOutRecordService.findAllowedCheckInRecord(item.getBillId()).isEmpty();
            if(noCheckinRecord){
                msgStream.add(RegisterBillMessageEvent.checkin);
            }
        }

        return msgStream;
    }


}