package com.dili.sg.trace.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.dili.sg.trace.dao.RegisterBillMapper;
import com.dili.sg.trace.domain.RegisterBill;
import com.dili.sg.trace.domain.SeparateSalesRecord;
import com.dili.sg.trace.domain.UserPlate;
import com.dili.sg.trace.dto.*;
import com.dili.common.exception.TraceBizException;
import com.dili.sg.trace.glossary.*;
import com.dili.sg.trace.service.*;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.service.CodeGenerateService;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.DetectTaskService;
import com.dili.trace.service.QualityTraceTradeBillService;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:34.
 */
@Service
public class RegisterBillServiceImpl implements RegisterBillService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillServiceImpl.class);

    @Autowired
    QualityTraceTradeBillService qualityTraceTradeBillService;
    @Autowired
    SeparateSalesRecordService separateSalesRecordService;
    @Autowired
    DetectRecordService detectRecordService;
    @Autowired
    UserPlateService userPlateService;
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


    @Transactional
    @Override
    public BaseOutput createRegisterBill(RegisterBill registerBill) {
        BaseOutput recheck = checkBill(registerBill);
        if (!recheck.isSuccess()) {
            return recheck;
        }
        String code = this.codeGenerateService.nextRegisterBillCode();
        registerBill.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        registerBill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
        registerBill.setCode(code);
        registerBill.setVersion(1);
        registerBill.setCreated(new Date());
        if (registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TRADE_AREA.getCode().intValue()) {
            // 交易区没有理货区号
            registerBill.setTallyAreaNo(null);
            // 交易区数据直接进行待检测状态
            // registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
            // registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
        }
        if (StringUtils.isBlank(registerBill.getOperatorName())) {
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
        }

        registerBill.setIdCardNo(StringUtils.trimToEmpty(registerBill.getIdCardNo()).toUpperCase());
        // 车牌转大写
        registerBill.setPlate(StringUtils.trimToEmpty(registerBill.getPlate()).toUpperCase());
        if (!this.checkPlate(registerBill)) {
            return BaseOutput.failure("当前车牌号已经与其他用户绑定,请使用其他牌号");
        }

        /*
         * else { List<String> otherUserPlateList = this.userPlateService
         * .findUserPlateByPlates(Arrays.asList(registerBill.getPlate())).stream().map(
         * UserPlate::getPlate) .collect(Collectors.toList()); if
         * (!otherUserPlateList.isEmpty()) { return
         * BaseOutput.failure("当前车牌号已经与其他用户绑定,请使用其他牌号"); } }
         */
        this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.REGISTER,
                registerBill.getOriginId());
        int result = this.billService.saveOrUpdate(registerBill);
        if (result == 0) {
            LOGGER.error("新增登记单数据库执行失败" + JSON.toJSONString(registerBill));
            recheck = BaseOutput.failure("创建失败");
        }
        return recheck;
    }

    private boolean checkPlate(RegisterBill registerBill) {

        if (registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TALLY_AREA.getCode().intValue()) {

            List<UserPlate> userPlateList = this.userPlateService
                    .findUserPlateByPlates(Arrays.asList(registerBill.getPlate()));

            if (!userPlateList.isEmpty()) {
                boolean noMatch = userPlateList.stream()
                        .noneMatch(up -> up.getUserId().equals(registerBill.getUserId()));
                if (noMatch) {
                    // throw new TraceBizException("当前车牌号已经与其他用户绑定,请使用其他牌号");
                    return false;
                }
            }
        }
        return true;

    }

    private BaseOutput checkBill(RegisterBill registerBill) {

        if (registerBill.getRegisterSource() == null || registerBill.getRegisterSource().intValue() == 0) {
            LOGGER.error("登记来源不能为空");
            return BaseOutput.failure("登记来源不能为空");
        }
        if (StringUtils.isBlank(registerBill.getName())) {
            LOGGER.error("业户姓名不能为空");
            return BaseOutput.failure("业户姓名不能为空");
        }
        if (StringUtils.isBlank(registerBill.getIdCardNo())) {
            LOGGER.error("业户身份证号不能为空");
            return BaseOutput.failure("业户身份证号不能为空");
        }
//		if (StringUtils.isBlank(registerBill.getAddr())) {
//			LOGGER.error("业户身份证地址不能为空");
//			return BaseOutput.failure("业户身份证地址不能为空");
//		}
        if (StringUtils.isBlank(registerBill.getProductName())) {
            LOGGER.error("商品名称不能为空");
            return BaseOutput.failure("商品名称不能为空");
        }
        if (StringUtils.isBlank(registerBill.getOriginName())) {
            LOGGER.error("商品产地不能为空");
            return BaseOutput.failure("商品产地不能为空");
        }

        if (registerBill.getWeight() == null) {
            LOGGER.error("商品重量不能为空");
            return BaseOutput.failure("商品重量不能为空");
        }

        if (registerBill.getRegisterSource().intValue() == RegisterSourceEnum.TALLY_AREA.getCode().intValue()) {
            if (registerBill.getWeight().longValue() <= 0L) {
                LOGGER.error("商品重量不能小于0");
                return BaseOutput.failure("商品重量不能小于0");
            }
        } else {
            if (registerBill.getWeight().longValue() < 0L) {
                LOGGER.error("商品重量不能为负");
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
    public List<RegisterBill> findByProductName(String productName) {
        RegisterBill registerBill = new RegisterBill();
        registerBill.setProductName(productName);
        registerBill.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        return this.billService.list(registerBill);
    }


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
    public int auditRegisterBill(Long id, Boolean pass) {
        RegisterBill registerBill = this.billService.get(id);
        return auditRegisterBill(pass, registerBill);
    }

    private int auditRegisterBill(Boolean pass, RegisterBill registerBill) {
        if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()) {
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
            if (pass) {

                // 理货区
                if (RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())
                        && StringUtils.isNotBlank(registerBill.getDetectReportUrl())) {
                    // 有检测报告，直接已审核
                    // registerBill.setLatestDetectTime(new Date());
                    registerBill.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
                    registerBill.setDetectState(null);
                }
                if (!RegisterBillStateEnum.ALREADY_AUDIT.getCode().equals(registerBill.getState())) {
                    registerBill.setSampleCode(this.codeGenerateService.nextRegisterBillSampleCode());
                    registerBill.setState(RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue());
                }

            } else {
                registerBill.setState(-1);
            }
            return this.billService.update(registerBill);
        } else {
            throw new TraceBizException("操作失败，数据状态已改变");
        }
    }

    @Transactional
    @Override
    public int undoRegisterBill(Long id) {
        return Optional.ofNullable(this.billService.get(id)).map(registerBill -> {
            if (RegisterBillStateEnum.WAIT_AUDIT.equalsToCode(registerBill.getState())
                    || RegisterBillStateEnum.WAIT_SAMPLE.equalsToCode(registerBill.getState())) {
                UserTicket userTicket = getOptUser();
//                LOGGER.info(userTicket.getDepName() + ":" + userTicket.getRealName() + "删除登记单"
//                        + JSON.toJSON(registerBill).toString());
                return this.billService.delete(id);
                // return update(registerBill);
            } else {
                throw new TraceBizException("操作失败，数据状态已改变");
            }
        }).orElse(0);

    }

    @Transactional
    @Override
    public int autoCheckRegisterBill(Long id) {
        RegisterBill registerBill = this.billService.get(id);
        return autoCheckRegisterBill(registerBill);
    }

    private int autoCheckRegisterBill(RegisterBill registerBill) {
        if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue()) {
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
            registerBill.setSampleSource(SampleSourceEnum.AUTO_CHECK.getCode().intValue());
            return this.updateRegisterBillAsWaitCheck(registerBill);
        } else {
            throw new TraceBizException("操作失败，数据状态已改变");
        }
    }

    @Override
    public BaseOutput doBatchAutoCheck(List<Long> idList) {
        BatchResultDto<String> dto = new BatchResultDto<>();
        for (Long id : idList) {
            RegisterBill registerBill = this.billService.get(id);
            if (registerBill == null) {
                continue;
            }
            try {
                this.autoCheckRegisterBill(registerBill);
                dto.getSuccessList().add(registerBill.getCode());
            } catch (Exception e) {
                dto.getFailureList().add(registerBill.getCode());
            }
        }

        return BaseOutput.success().setData(dto);

    }

    @Transactional
    @Override
    public BaseOutput doBatchUndo(List<Long> idList) {

        StreamEx.of(idList).forEach(registerBillId -> {
            try {
                this.undoRegisterBill(registerBillId);
            } catch (TraceBizException e) {
                LOGGER.warn(e.getMessage());
            }

        });
        return BaseOutput.success();
    }

    @Override
    public BaseOutput doBatchSamplingCheck(List<Long> idList) {
        BatchResultDto<String> dto = new BatchResultDto<>();
        for (Long id : idList) {
            RegisterBill registerBill = this.billService.get(id);
            if (registerBill == null) {
                continue;
            }
            try {
                this.samplingCheckRegisterBill(registerBill);
                dto.getSuccessList().add(registerBill.getCode());
            } catch (Exception e) {
                dto.getFailureList().add(registerBill.getCode());
            }
        }
        return BaseOutput.success().setData(dto);

    }

    @Transactional
    @Override
    public BaseOutput doBatchAudit(BatchAuditDto batchAuditDto) {
        BatchResultDto<String> dto = new BatchResultDto<>();

        // id转换为RegisterBill,并通过条件判断partition(true:只有产地证明，且需要进行批量处理,false:其他)
        Map<Boolean, List<RegisterBill>> partitionedMap = CollectionUtils
                .emptyIfNull(batchAuditDto.getRegisterBillIdList()).stream().filter(Objects::nonNull).map(id -> {
                    RegisterBill registerBill = this.billService.get(id);
                    return registerBill;
                }).filter(Objects::nonNull).filter(registerBill -> {
                    if (Boolean.FALSE.equals(batchAuditDto.getPassWithOriginCertifiyUrl())) {
                        if (StringUtils.isNotBlank(registerBill.getOriginCertifiyUrl())
                                && StringUtils.isBlank(registerBill.getDetectReportUrl())) {
                            return false;
                        }
                    }
                    return true;
                }).collect(Collectors.partitioningBy((registerBill) -> {

                    if (Boolean.TRUE.equals(batchAuditDto.getPassWithOriginCertifiyUrl())) {
                        if (StringUtils.isNotBlank(registerBill.getOriginCertifiyUrl())
                                && StringUtils.isBlank(registerBill.getDetectReportUrl())) {
                            return true;
                        }
                    }
                    return false;
                }));

        // 只有产地证明，且需要进行批量处理
        CollectionUtils.emptyIfNull(partitionedMap.get(Boolean.TRUE)).forEach(registerBill -> {
            if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()) {
                UserTicket userTicket = getOptUser();
                registerBill.setOperatorName(userTicket.getRealName());
                registerBill.setOperatorId(userTicket.getId());
                registerBill.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
                registerBill.setDetectState(null);
                this.billService.updateSelective(registerBill);
                dto.getSuccessList().add(registerBill.getCode());
            } else {
                dto.getFailureList().add(registerBill.getCode());
            }

        });
        // 其他登记单
        CollectionUtils.emptyIfNull(partitionedMap.get(Boolean.FALSE)).forEach(registerBill -> {
            try {
                this.auditRegisterBill(batchAuditDto.getPass(), registerBill);
                dto.getSuccessList().add(registerBill.getCode());
            } catch (Exception e) {
                dto.getFailureList().add(registerBill.getCode());
            }

        });

        return BaseOutput.success().setData(dto);
    }

    @Transactional
    @Override
    public int samplingCheckRegisterBill(Long id) {
        RegisterBill registerBill = this.billService.get(id);
        return samplingCheckRegisterBill(registerBill);
    }

    private int samplingCheckRegisterBill(RegisterBill registerBill) {
        if (registerBill.getState().intValue() == RegisterBillStateEnum.WAIT_SAMPLE.getCode().intValue()) {
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
            registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
            return this.updateRegisterBillAsWaitCheck(registerBill);
        } else {
            throw new TraceBizException("操作失败，数据状态已改变");
        }
    }

    @Transactional
    @Override
    public int reviewCheckRegisterBill(Long id) {
        RegisterBill registerBill = this.billService.get(id);
        if (registerBill.getState().intValue() != RegisterBillStateEnum.ALREADY_CHECK.getCode().intValue()) {
            throw new TraceBizException("操作失败，数据状态已改变");
        }

        boolean updateState = false;
        // 第一次复检
        if (registerBill.getDetectState().intValue() == BillDetectStateEnum.NO_PASS.getCode().intValue()) {
            updateState = true;
        } else if (registerBill.getDetectState().intValue() == BillDetectStateEnum.REVIEW_NO_PASS.getCode().intValue()
                && StringUtils.isBlank(registerBill.getHandleResult())) {
            // 多次复检
            updateState = true;
        }
        if (updateState) {
            UserTicket userTicket = getOptUser();
            registerBill.setOperatorName(userTicket.getRealName());
            registerBill.setOperatorId(userTicket.getId());
            registerBill.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
            registerBill.setExeMachineNo(null);
            registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
            registerBill.setModified(new Date());
            return this.billService.update(registerBill);
        } else {
            throw new TraceBizException("操作失败，数据状态已改变");
        }
    }

    private int updateRegisterBillAsWaitCheck(RegisterBill registerBill) {

        registerBill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
        registerBill.setModified(new Date());
        return this.billService.update(registerBill);
    }

    UserTicket getOptUser() {
        return SessionContext.getSessionContext().getUserTicket();
    }

    @Override
    public QualityTraceTradeBillOutDto findQualityTraceTradeBill(String tradeNo) {
        if (StringUtils.isBlank(tradeNo)) {
            return null;
        }
        QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService.findByTradeNo(tradeNo);
        if (qualityTraceTradeBill == null) {
            return null;
        }
        QualityTraceTradeBillOutDto dto = DTOUtils.newDTO(QualityTraceTradeBillOutDto.class);
        dto.setQualityTraceTradeBill(qualityTraceTradeBill);

        RegisterBill registerBill = new RegisterBill();
        if (StringUtils.isNotBlank(qualityTraceTradeBill.getRegisterBillCode())) {
            RegisterBill condition = new RegisterBill();

            condition.setCode(qualityTraceTradeBill.getRegisterBillCode());
            List<RegisterBill> list = this.billService.listByExample(condition);
            if (!list.isEmpty()) {
                registerBill = list.get(0);

            }
        }
        dto.setRegisterBill(registerBill);

        // RegisterBillOutputDto registerBill = findByTradeNo(tradeNo);

        // if (qualityTraceTradeBill.getBuyerIDNo().equalsIgnoreCase(cardNo)) {
        // if (registerBill == null) {
        // int result = matchDetectBind(qualityTraceTradeBill);
        // if (result == 1) {
        // registerBill = findByTradeNo(tradeNo);
        // }
        // }
        // }

        if (registerBill != null && StringUtils.isNotBlank(registerBill.getCode())) {
            List<SeparateSalesRecord> records = separateSalesRecordService
                    .findByRegisterBillCode(registerBill.getCode());
            dto.setSeparateSalesRecords(records);
            // registerBill.setDetectRecord(detectRecordService.findByRegisterBillCode(registerBill.getCode()));
        }
        // 查询交易单信息
        // if(StringUtils.isNotBlank(registerBill.getCode())) {
        // QualityTraceTradeBill example = DTOUtils.newDTO(QualityTraceTradeBill.class);
        // example.setRegisterBillCode(registerBill.getCode());
        // List<QualityTraceTradeBill> qualityTraceTradeBillList =
        // this.qualityTraceTradeBillService
        // .listByExample(example);
        //
        // registerBill.setQualityTraceTradeBillList(qualityTraceTradeBillList);
        // }

        // registerBill.setQualityTraceTradeBill(qualityTraceTradeBill);
        return dto;
    }

    @Override
    public RegisterBillStaticsDto groupByState(RegisterBillDto dto) {
        dto.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        return this.billMapper.groupByState(dto);

    }

    @Override
    public RegisterBillOutputDto conversionDetailOutput(RegisterBill registerBill) {
        LOGGER.info("获取登记单信息信息" + JSON.toJSONString(registerBill));
        RegisterBillOutputDto outputDto = new RegisterBillOutputDto();
        if (registerBill == null) {
            return null;
        } else {
            BeanUtil.copyProperties(registerBill, outputDto);
        }
        // 查询交易单信息
        QualityTraceTradeBill example = DTOUtils.newDTO(QualityTraceTradeBill.class);
        example.setRegisterBillCode(registerBill.getCode());
        List<QualityTraceTradeBill> qualityTraceTradeBillList = this.qualityTraceTradeBillService
                .listByExample(example);

        outputDto.setQualityTraceTradeBillList(qualityTraceTradeBillList);
        // if (StringUtils.isNotBlank(registerBill.getTradeNo())) {
        // // 交易信息
        // QualityTraceTradeBill qualityTraceTradeBill = qualityTraceTradeBillService
        // .findByTradeNo(registerBill.getTradeNo());
        // outputDto.setQualityTraceTradeBill(qualityTraceTradeBill);
        // }
        // 分销信息
        if (registerBill.getSalesType() != null
                && registerBill.getSalesType().intValue() == SalesTypeEnum.SEPARATE_SALES.getCode().intValue()) {
            // 分销
            List<SeparateSalesRecord> records = separateSalesRecordService
                    .findByRegisterBillCode(registerBill.getCode());
            outputDto.setSeparateSalesRecords(records);
        }

        // 检测信息
        // if (registerBill.getLatestDetectRecordId() != null) {
        // // 检测信息
        // outputDto.setDetectRecord(detectRecordService.findByRegisterBillCode(registerBill.getCode()));
        // }
        return outputDto;
    }

    @Override
    public Long saveHandleResult(RegisterBill input) {
        if (input == null || input.getId() == null
                || StringUtils.isAnyBlank(input.getHandleResult(), input.getHandleResultUrl())) {
            throw new TraceBizException("参数错误");
        }
        if (input.getHandleResult().trim().length() > 1000) {
            throw new TraceBizException("处理结果不能超过1000");
        }
        RegisterBill item = this.billService.get(input.getId());
        if (item == null) {
            throw new TraceBizException("数据错误");
        }

        RegisterBill example = new RegisterBill();
        example.setId(item.getId());
        example.setHandleResult(input.getHandleResult());
        example.setHandleResultUrl(input.getHandleResultUrl());
        this.billService.updateSelective(example);

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
        RegisterBill registerBill = this.billService.get(input.getId());
        if (registerBill == null) {
            throw new TraceBizException("数据错误");
        }
        if (StringUtils.isBlank(registerBill.getOriginCertifiyUrl())) {
            throw new TraceBizException("请上传产地证明");
        }
        if (registerBill.getState().intValue() != RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()) {
            throw new TraceBizException("数据状态错误");
        }
        if (!RegisterSourceEnum.TALLY_AREA.getCode().equals(registerBill.getRegisterSource())) {
            throw new TraceBizException("数据来源错误");

        }
        registerBill.setState(RegisterBillStateEnum.ALREADY_AUDIT.getCode());
        // registerBill.setDetectState(null);
        this.billService.updateSelective(registerBill);

        return registerBill.getId();
    }

    @Override
    public Long doEdit(RegisterBill input) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("参数错误");
        }
        RegisterBill registerBill = this.billService.get(input.getId());
        if (registerBill == null) {
            throw new TraceBizException("数据错误");
        }
        if (registerBill.getState().intValue() != RegisterBillStateEnum.WAIT_AUDIT.getCode().intValue()) {
            throw new TraceBizException("数据状态错误");
        }

        if (input.getRegisterSource().intValue() == RegisterSourceEnum.TALLY_AREA.getCode().intValue()) {
            // 理货区
            registerBill.setPlate(input.getPlate());
        } else {

        }
        if (!this.checkPlate(registerBill)) {
            throw new TraceBizException("当前车牌号已经与其他用户绑定,请使用其他牌号");
        }
        this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.REGISTER,
                registerBill.getOriginId(), input.getOriginId());
        registerBill.setProductId(input.getProductId());
        registerBill.setProductName(input.getProductName());

        registerBill.setOriginId(input.getOriginId());
        registerBill.setOriginName(input.getOriginName());

        registerBill.setWeight(input.getWeight());

        // registerBill.setOriginCertifiyUrl(input.getOriginCertifiyUrl());
        this.billService.updateSelective(registerBill);
        return registerBill.getId();
    }

    @Override
    public Long doUploadDetectReport(RegisterBill input) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("参数错误");
        }
        if (StringUtils.isBlank(input.getOriginCertifiyUrl()) && StringUtils.isBlank(input.getDetectReportUrl())) {
            throw new TraceBizException("请上传报告");
        }
        RegisterBill item = this.billService.get(input.getId());
        if (item == null) {
            throw new TraceBizException("数据错误");
        }
        if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(item.getState())) {
            throw new TraceBizException("状态错误,不能上传检测报告");
        }

        RegisterBill example = new RegisterBill();
        example.setId(item.getId());
        example.setOriginCertifiyUrl(StringUtils.trimToNull(input.getOriginCertifiyUrl()));
        example.setDetectReportUrl(StringUtils.trimToNull(input.getDetectReportUrl()));
        this.billService.updateSelective(example);

        return example.getId();
    }

    @Override
    public Long doUploadOrigincertifiy(RegisterBill input) {
        if (input == null || input.getId() == null) {
            throw new TraceBizException("参数错误");
        }
        if (StringUtils.isBlank(input.getOriginCertifiyUrl()) && StringUtils.isBlank(input.getDetectReportUrl())) {
            throw new TraceBizException("请上传报告");
        }
        RegisterBill item = this.billService.get(input.getId());
        if (item == null) {
            throw new TraceBizException("数据错误");
        }
        // if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(item.getState())) {
        // throw new TraceBizException("状态错误,不能上传产地证明");
        // }
        RegisterBill example = new RegisterBill();
        example.setId(item.getId());
        example.setOriginCertifiyUrl(StringUtils.trimToNull(input.getOriginCertifiyUrl()));
        // example.setDetectReportUrl(StringUtils.trimToNull(input.getDetectReportUrl()));
        this.billService.updateSelective(example);

        return example.getId();
    }

    @Override
    public BaseOutput doRemoveReportAndCertifiy(Long id, String deleteType) {
        RegisterBill item = this.billService.get(id);
        if (item == null) {
            throw new TraceBizException("数据错误");
        }
        if (!RegisterBillStateEnum.WAIT_AUDIT.getCode().equals(item.getState())) {
            throw new TraceBizException("状态错误,不能删除产地证明和检测报告");
        }
        if ("all".equalsIgnoreCase(deleteType)) {
            item.setOriginCertifiyUrl(null);
            item.setDetectReportUrl(null);
        } else if ("originCertifiy".equalsIgnoreCase(deleteType)) {
            item.setOriginCertifiyUrl(null);
        } else if ("detectReport".equalsIgnoreCase(deleteType)) {
            item.setDetectReportUrl(null);
        } else {
            // do nothing
            return BaseOutput.success();
        }
        this.billMapper.doRemoveReportAndCertifiy(item);

        return BaseOutput.success();
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

    @Override
    public RegisterBill findHighLightBill(RegisterBillDto input) throws Exception {
        RegisterBillDto dto = new RegisterBillDto();
        UserTicket userTicket = getOptUser();
        dto.setOperatorId(userTicket.getId());
        dto.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
        dto.setRows(1);
        dto.setSort("code");
        dto.setOrder("desc");
        return this.billService.listByExample(dto).stream().findFirst().orElse(new RegisterBill());
    }

    @Override
    public String listPage(RegisterBillDto input) throws Exception {
        RegisterBillDto dto = this.preBuildDTO(input);
        dto.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        return this.billService.listEasyuiPageByExample(dto, true).toString();
    }

    @Override
    public String listStaticsPage(RegisterBillDto dto) throws Exception {
        if (StringUtils.isNotBlank(dto.getAttrValue())) {
            switch (dto.getAttr()) {
                case "code":
                    dto.setCode(dto.getAttrValue());
                    break;
                case "plate":
                    dto.setPlate(dto.getAttrValue());
                    break;
                case "tallyAreaNo":
                    // registerBill.setTallyAreaNo(registerBill.getAttrValue());
                    dto.setLikeTallyAreaNo(dto.getAttrValue());
                    break;
                case "latestDetectOperator":
                    dto.setLatestDetectOperator(dto.getAttrValue());
                    break;
                case "name":
                    dto.setName(dto.getAttrValue());
                    break;
                case "productName":
                    dto.setLikeProductName(dto.getAttrValue());
                    break;
                case "likeSampleCode":
                    dto.setLikeSampleCode(dto.getAttrValue());
                    break;
            }
        }
        StringBuilder sql = this.buildDynamicCondition(dto);
        if (sql.length() > 0) {
            dto.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
        }
        dto.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        dto.setLatestDetectTimeTimeStart(StringUtils.trimToNull(dto.getLatestDetectTimeTimeStart()));
        dto.setLatestDetectTimeTimeEnd(StringUtils.trimToNull(dto.getLatestDetectTimeTimeEnd()));

        dto.setCreatedStart(StringUtils.trimToNull(dto.getCreatedStart()));
        dto.setCreatedEnd(StringUtils.trimToNull(dto.getCreatedEnd()));

        return this.billService.listEasyuiPageByExample(dto, true).toString();
    }

    private StringBuilder buildDynamicCondition(RegisterBillDto registerBill) {
        StringBuilder sql = new StringBuilder();
        if (registerBill.getHasDetectReport() != null) {
            if (registerBill.getHasDetectReport()) {
                sql.append("  (detect_report_url is not null AND detect_report_url<>'') ");
            } else {
                sql.append("  (detect_report_url is  null or detect_report_url='') ");
            }
        }

        if (registerBill.getHasOriginCertifiy() != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            if (registerBill.getHasOriginCertifiy()) {
                sql.append("  (origin_certifiy_url is not null AND origin_certifiy_url<>'') ");
            } else {
                sql.append("  (origin_certifiy_url is  null or origin_certifiy_url='') ");
            }
        }

        if (registerBill.getHasHandleResult() != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            if (registerBill.getHasHandleResult()) {
                sql.append("  (handle_result is not null AND handle_result<>'') ");
            } else {
                sql.append("  (handle_result is  null or handle_result='') ");
            }
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
    public List<RegisterBill> findBySampleCodeList(List<String> sampleCodeList) {

        if (sampleCodeList == null || sampleCodeList.size() == 0) {
            return Collections.emptyList();
        }
        Example example = new Example(RegisterBill.class);
        example.createCriteria().andIn("sampleCode", sampleCodeList);
        return this.billMapper.selectByExample(example);

    }

    @Override
    public RegisterBill selectByIdForUpdate(Long id) {
        return this.billMapper.selectByIdForUpdate(id);
    }

}