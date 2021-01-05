package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.trace.dao.RegisterBillMapper;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.IdNameDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.SampleSourceEnum;
import com.dili.trace.util.MarketUtil;
import com.github.pagehelper.Page;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 委托单
 */
@Service
public class CommissionBillService extends BaseServiceImpl<RegisterBill, Long> {

    @Autowired
    CodeGenerateService codeGenerateService;
    @Autowired
    BillService billService;
    @Autowired
    DetectRequestService detectRequestService;
    @Resource
    RegisterBillMapper billMapper;
    @Autowired
    com.dili.trace.rpc.service.UidRestfulRpcService uidRestfulRpcService;

    /**
     * 分页查询委托单
     *
     * @param query
     * @return
     * @throws Exception
     */
    public String listPage(RegisterBillDto query) throws Exception {
        RegisterBillDto dto = this.preBuildDTO(query);
        dto.setBillType(this.supportedBillType().getCode());
        dto.setMarketId(MarketUtil.returnMarket());
        dto.setIsDeleted(BillDeleteStatusEnum.NORMAL.getCode());
        BasePage<RegisterBillDto> page = this.billService.buildQuery(dto).listPageByFun(q -> this.billMapper.queryListByExample(q));

        Map<Long, DetectRequest> idAndDetectRquestMap = this.detectRequestService.findDetectRequestByIdList(StreamEx.of(page.getDatas()).map(RegisterBill::getDetectRequestId).toList());
        //检测值
        // Map<String, DetectRecord> recordMap = detectRecordService.findMapRegisterBillByIds(StreamEx.of(list).map(RegisterBill::getLatestDetectRecordId).toList());
        StreamEx.of(page.getDatas()).forEach(rb -> {
            rb.setDetectRequest(idAndDetectRquestMap.get(rb.getDetectRequestId()));
        });

        List results = ValueProviderUtils.buildDataByProvider(query, page.getDatas());
        EasyuiPageOutput out = new EasyuiPageOutput(page.getTotalItem(),results);
        return out.toString();
    }

    /**
     * 构造查询条件
     *
     * @param dto
     * @return
     */
    private RegisterBillDto preBuildDTO(RegisterBillDto dto) {
        String attr = dto.getAttr();
        String attrValue = dto.getAttrValue();
        if (attrValue != null && (StringUtils.isNotBlank(attrValue))) {
            switch (attr) {
                case "code":
                    dto.setCode(attrValue);
                    break;
                case "latestDetectOperator":
                    dto.setLatestDetectOperator(attrValue);
                    break;
                case "name":
                    dto.setName(attrValue);
                    break;
                case "likeSampleCode":
                    dto.setLikeSampleCode(attrValue);
                    break;
            }
        }

        StringBuilder sql = new StringBuilder();
        Boolean hasCheckSheet = dto.getHasCheckSheet();
        if (hasCheckSheet != null) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }
            if (hasCheckSheet) {
                sql.append("  (check_sheet_id is not null) ");
            } else {
                sql.append("  (check_sheet_id is null) ");
            }
        }
        if (sql.length() > 0) {
            dto.setMetadata(IDTO.AND_CONDITION_EXPR, sql.toString());
        }

        return dto;
    }

    /**
     * 当前支持的登记单类型
     *
     * @return
     */
    public BillTypeEnum supportedBillType() {
        return BillTypeEnum.COMMISSION_BILL;
    }

    /**
     * 查询要高亮显示的数据
     *
     * @param input
     * @param operatorUser
     * @return
     * @throws Exception
     */
    public RegisterBill findHighLightCommissionBill(RegisterBillDto input, OperatorUser operatorUser) throws Exception {
        RegisterBillDto dto = new RegisterBillDto();
        dto.setOperatorId(operatorUser.getId());
//        dto.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
        dto.setDetectStatus(DetectStatusEnum.FINISH_DETECT.getCode());
        dto.setBillType(this.supportedBillType().getCode());
        dto.setRows(1);
        dto.setSort("code");
        dto.setOrder("desc");
        return this.billService.listByExample(dto).stream().findFirst().orElse(new RegisterBill());
    }


    /**
     * 批量复检
     *
     * @param billIdList
     * @param operatorUser
     */
    public List<String> doBatchReviewCheck(List<Long> billIdList, OperatorUser operatorUser) {
        return StreamEx.of(billIdList).map(billId -> {
            RegisterBill bill = this.billService.get(billId);
            bill.setDetectRequest(this.detectRequestService.findDetectRequestByBillId(bill.getBillId()).orElse(null));
            return bill;
        }).filter(bill -> this.supportedBillType().equalsToCode(bill.getBillType()))
                .filter(bill -> DetectStatusEnum.FINISH_DETECT.equalsToCode(bill.getDetectStatus()))
                .filter(bill -> DetectResultEnum.PASSED.equalsToCode(bill.getDetectRequest().getDetectResult()))
                .map(bill -> {
                    DetectRequest detectRequest = bill.getDetectRequest();

                    if (!DetectResultEnum.FAILED.equalsToCode(detectRequest.getDetectResult())) {
                        throw new TraceBizException("操作失败，数据状态已改变");
                    }
                    bill.setOperatorName(operatorUser.getName());
                    bill.setOperatorId(operatorUser.getId());
//            item.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
//            item.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
                    DetectRequest item = this.detectRequestService.createByBillId(bill.getBillId(), DetectTypeEnum.NEW, new IdNameDto(operatorUser.getId(), operatorUser.getName()), Optional.empty());

                    DetectRequest updatable = new DetectRequest();
                    updatable.setId(item.getId());

                    updatable.setDetectSource(detectRequest.getDetectSource());
                    updatable.setDetectResult(DetectResultEnum.NONE.getCode());
                    updatable.setDetectType(DetectTypeEnum.RECHECK.getCode());
                    this.detectRequestService.updateSelective(detectRequest);

                    bill.setDetectStatus(DetectStatusEnum.WAIT_DETECT.getCode());

                    this.billService.update(bill);
                    return bill.getCode();
                }).toList();
    }

    /**
     * PC管理员创建登记单
     *
     * @param bill
     * @param operatorUser
     * @return
     */
    @Transactional
    public BaseOutput createCommissionBillByManager(RegisterBill bill, OperatorUser operatorUser) {
        if (bill.getWeight() == null || bill.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TraceBizException("重量不能小于零");
        }
        if (StringUtils.isAllBlank(bill.getName(), bill.getCorporateName())) {
            throw new TraceBizException("业户名称和企业名称不能同时为空");
        }
        bill.setRegisterSource(RegisterSourceEnum.OTHERS.getCode());
        if (!RegisterBilCreationSourceEnum.fromCode(bill.getCreationSource()).isPresent()) {
            throw new TraceBizException("登记单来源类型错误");
        }
        bill.setCode(this.codeGenerateService.nextCommissionBillCode());
        bill.setSampleCode(this.codeGenerateService.nextCommissionBillSampleCode());


        bill.setBillType(this.supportedBillType().getCode());
        bill.setCreated(new Date());
        bill.setModified(new Date());

        bill.setOperatorId(operatorUser.getId());
        bill.setOperatorName(operatorUser.getName());
        bill.setDetectStatus(DetectStatusEnum.WAIT_DETECT.getCode());
        bill.setPlate("");

        bill.setMarketId(MarketUtil.returnMarket());
        this.billService.insertSelective(bill);
        DetectRequest item = this.detectRequestService.createDefault(bill.getBillId(), Optional.ofNullable(operatorUser));

        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setId(item.getId());
        detectRequest.setDetectType(DetectTypeEnum.INITIAL_CHECK.getCode());
        detectRequest.setDetectSource(SampleSourceEnum.AUTO_CHECK.getCode());
        detectRequest.setDetectResult(DetectResultEnum.NONE.getCode());
        // 维护检测编号
        detectRequest.setDetectCode(uidRestfulRpcService.detectRequestBizNumber(operatorUser.getMarketName()));
        this.detectRequestService.updateSelective(detectRequest);
        //patch bill单上的requestId
        updateBillDetectRequestId(bill.getBillId(),item.getId());
        return BaseOutput.success();
    }

    /**
     * 创建检测请求单后与委托单进行关联
     * @param billId
     * @param detectRequestId
     */
    private void updateBillDetectRequestId(Long billId, Long detectRequestId) {
        RegisterBill upBill = new RegisterBill();
        upBill.setId(billId);
        upBill.setDetectRequestId(detectRequestId);
        this.billService.updateSelective(upBill);
    }

    /**
     * 用户自己创建登记单
     *
     * @param bill
     * @return
     */
    @Transactional
    public RegisterBill createCommissionBillByUser(RegisterBill bill, OperatorUser operatorUser) {
        if (bill.getWeight() == null || bill.getWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TraceBizException("重量不能小于零");
        }
        if (StringUtils.isAllBlank(bill.getName(), bill.getCorporateName())) {
            throw new TraceBizException("业户名称和企业名称不能同时为空");
        }
        bill.setRegisterSource(RegisterSourceEnum.OTHERS.getCode());
        if (!RegisterBilCreationSourceEnum.fromCode(bill.getCreationSource()).isPresent()) {
            throw new TraceBizException("登记单来源类型错误");
        }
        bill.setCode(this.codeGenerateService.nextCommissionBillCode());
//		bill.setSampleCode(this.codeGenerateService.nextCommissionBillSampleCode());
//        bill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
        bill.setVerifyStatus(BillVerifyStatusEnum.WAIT_AUDIT.getCode());
        bill.setDetectStatus(DetectStatusEnum.NONE.getCode());
        bill.setBillType(this.supportedBillType().getCode());
        bill.setCreated(new Date());
        bill.setModified(new Date());
        bill.setMarketId(bill.getMarketId());
//		bill.setPlate("");
        this.billService.insertSelective(bill);
        DetectRequest detectRequest=this.detectRequestService.createDefault(bill.getBillId(),Optional.empty());

        DetectRequest updateDetectRequest = new DetectRequest();
        updateDetectRequest.setId(detectRequest.getId());
        // 维护检测编号

        updateDetectRequest.setDetectCode(uidRestfulRpcService.detectRequestBizNumber(operatorUser.getMarketName()));
        this.detectRequestService.updateSelective(updateDetectRequest);

        RegisterBill updatable=new RegisterBill();
        updatable.setDetectRequestId(detectRequest.getId());
        updatable.setId(bill.getBillId());
        updatable.setDetectStatus(DetectStatusEnum.WAIT_DESIGNATED.getCode());
        this.billService.updateSelective(updatable);

        return bill;
    }


    /**
     * 用户自己创建登记单
     *
     * @param billList
     * @return
     */
    @Transactional
    public List<RegisterBill> createCommissionBillByUser(List<RegisterBill> billList, OperatorUser operatorUser) {
        return StreamEx.of(billList).filter(Objects::nonNull).map(bill -> {
            return this.createCommissionBillByUser(bill, operatorUser);
        }).toList();
    }

    /**
     * PC管理员创建登记单
     *
     * @param billList
     * @return
     */
    @Transactional
    public BaseOutput createCommissionBillByManager(List<RegisterBill> billList, OperatorUser operatorUser) {
        CollectionUtils.emptyIfNull(billList).stream().filter(Objects::nonNull).forEach(bill -> {
            this.createCommissionBillByManager(bill, operatorUser);
        });
        return BaseOutput.success();

    }

    /**
     * PC管理员审核登记单
     *
     * @param billId
     * @param operatorUser
     * @return
     */
    @Transactional
    public Long doAuditCommissionBillByManager(Long billId, OperatorUser operatorUser) {
        if (billId == null) {
            throw new TraceBizException("参数错误");
        }
        RegisterBill item = this.billService.get(billId);
        if (item == null) {
            throw new TraceBizException("数据不存在");
        }
        if (!BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(item.getVerifyStatus())) {
            throw new TraceBizException("登记单状态错误");
        }
        RegisterBill bill = new RegisterBill();
        bill.setId(item.getBillId());
//        bill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
        bill.setVerifyStatus(BillVerifyStatusEnum.PASSED.getCode());
        bill.setDetectStatus(DetectStatusEnum.WAIT_DETECT.getCode());
        bill.setSampleCode(this.codeGenerateService.nextCommissionBillSampleCode());
        bill.setModified(new Date());
        bill.setOperatorId(operatorUser.getId());
        bill.setOperatorName(operatorUser.getName());
        this.billService.updateSelective(bill);

        return bill.getBillId();

    }

}
