package com.dili.sg.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.sg.trace.glossary.*;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.CodeGenerateService;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 委托单
 */
@Service
public class CommissionBillService {

    @Autowired
    CodeGenerateService codeGenerateService;
    @Autowired
    BillService billService;

    /**
     * 分页查询委托单
     *
     * @param input
     * @return
     * @throws Exception
     */
    public String listPage(RegisterBillDto input) throws Exception {
        RegisterBillDto dto = this.preBuildDTO(input);
        dto.setBillType(this.supportedBillType().getCode());
        return this.billService.listEasyuiPageByExample(dto, true).toString();
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
        dto.setState(RegisterBillStateEnum.ALREADY_CHECK.getCode());
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
            return bill;
        }).filter(bill -> this.supportedBillType().equalsToCode(bill.getBillType()))
                .filter(bill -> RegisterBillStateEnum.ALREADY_CHECK.equalsToCode(bill.getState()))
                .filter(bill -> BillDetectStateEnum.NO_PASS.equalsToCode(bill.getDetectState())
                        || BillDetectStateEnum.REVIEW_NO_PASS.equalsToCode(bill.getDetectState()))
        .map(item->{
            item.setOperatorName(operatorUser.getName());
            item.setOperatorId(operatorUser.getId());
            item.setSampleSource(SampleSourceEnum.SAMPLE_CHECK.getCode().intValue());
            item.setState(RegisterBillStateEnum.WAIT_CHECK.getCode().intValue());
            item.setExeMachineNo(null);
            item.setModified(new Date());
            this.billService.update(item);
            return item.getCode();
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
            if (bill.getWeight() == null || bill.getWeight() .compareTo(BigDecimal.ZERO)<= 0) {
            throw new TraceBizException("重量不能小于零");
        }
        if (StringUtils.isAllBlank(bill.getName(), bill.getCorporateName())) {
            throw new TraceBizException("业户名称和企业名称不能同时为空");
        }
        bill.setRegisterSource(RegisterSourceEnum.OTHERS.getCode());
        if (!RegisterBilCreationSourceEnum.getRegisterBilCreationSourceEnum(bill.getCreationSource()).isPresent()) {
            throw new TraceBizException("登记单来源类型错误");
        }
        bill.setCode(this.codeGenerateService.nextCommissionBillCode());
        bill.setSampleCode(this.codeGenerateService.nextCommissionBillSampleCode());
        bill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
        bill.setBillType(this.supportedBillType().getCode());
        bill.setCreated(new Date());
        bill.setModified(new Date());

        bill.setOperatorId(operatorUser.getId());
        bill.setOperatorName(operatorUser.getName());
        bill.setPlate("");
        this.billService.insertSelective(bill);
        return BaseOutput.success();
    }

    /**
     * 用户自己创建登记单
     *
     * @param bill
     * @return
     */
    @Transactional
    public RegisterBill createCommissionBillByUser(RegisterBill bill) {
        if (bill.getWeight() == null || bill.getWeight() .compareTo(BigDecimal.ZERO)<= 0) {
            throw new TraceBizException("重量不能小于零");
        }
        if (StringUtils.isAllBlank(bill.getName(), bill.getCorporateName())) {
            throw new TraceBizException("业户名称和企业名称不能同时为空");
        }
        bill.setRegisterSource(RegisterSourceEnum.OTHERS.getCode());
        if (!RegisterBilCreationSourceEnum.getRegisterBilCreationSourceEnum(bill.getCreationSource()).isPresent()) {
            throw new TraceBizException("登记单来源类型错误");
        }
        bill.setCode(this.codeGenerateService.nextCommissionBillCode());
//		bill.setSampleCode(this.codeGenerateService.nextCommissionBillSampleCode());
        bill.setState(RegisterBillStateEnum.WAIT_AUDIT.getCode());
        bill.setBillType(this.supportedBillType().getCode());
        bill.setCreated(new Date());
        bill.setModified(new Date());

//		bill.setPlate("");
        this.billService.insertSelective(bill);
        return bill;
    }


    /**
     * 用户自己创建登记单
     *
     * @param billList
     * @return
     */
    @Transactional
    public List<RegisterBill> createCommissionBillByUser(List<RegisterBill> billList) {
        return StreamEx.of(billList).filter(Objects::nonNull).map(bill -> {
            return this.createCommissionBillByUser(bill);
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
        if (!RegisterBillStateEnum.WAIT_AUDIT.equalsToCode(item.getState())) {
            throw new TraceBizException("登记单状态错误");
        }
        RegisterBill bill = new RegisterBill();
        bill.setId(item.getBillId());
        bill.setState(RegisterBillStateEnum.WAIT_CHECK.getCode());
        bill.setSampleCode(this.codeGenerateService.nextCommissionBillSampleCode());
        bill.setModified(new Date());
        bill.setOperatorId(operatorUser.getId());
        bill.setOperatorName(operatorUser.getName());
        this.billService.updateSelective(bill);

        return bill.getBillId();

    }

}
