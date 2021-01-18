package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.PurchaseIntentionRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.DetectStatusEnum;
import com.dili.trace.glossary.BizNumberType;
import com.dili.trace.glossary.RegisterBilCreationSourceEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.rpc.service.UidRestfulRpcService;
import com.dili.trace.util.MarketUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wgf
 */
@Service
public class PurchaseIntentionRecordService extends BaseServiceImpl<PurchaseIntentionRecord, Long> {

    @Autowired
    private BillService billService;
    @Autowired
    private UidRestfulRpcService uidRestfulRpcService;

    /**
     * 买家报备新增
     *
     * @param record
     */
    public void doAddPurchaseIntentionRecord(PurchaseIntentionRecord record, OperatorUser operatorUser) {

        //新增报备单
        RegisterBill bill = buildBillRecord(record, operatorUser);
        createAddBill(bill);
        //新增买家报备记录
        createAddRecord(record);
    }

    /**
     * 创建买家报备记录
     *
     * @param record
     */
    private void createAddRecord(PurchaseIntentionRecord record) {
        insertSelective(record);
    }

    /**
     * 构建报备单
     *
     * @param record
     * @return
     */
    private RegisterBill buildBillRecord(PurchaseIntentionRecord record, OperatorUser operatorUser) {
        if (record.getProductWeight() == null || record.getProductWeight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TraceBizException("重量不能小于零");
        }
        if (StringUtils.isBlank(record.getBuyerName())) {
            throw new TraceBizException("买家名称不能为空");
        }

        RegisterBill bill = new RegisterBill();
        bill.setCode(this.uidRestfulRpcService.bizNumber(BizNumberType.COMMISSION_BILL_CODE.getType()));
        bill.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
        bill.setDetectStatus(DetectStatusEnum.WAIT_DESIGNATED.getCode());
        bill.setRegisterSource(RegisterSourceEnum.OTHERS.getCode());
        bill.setProductId(record.getProductId());
        bill.setProductName(record.getProductName());
        bill.setWeight(record.getProductWeight());
        bill.setWeightUnit(record.getWeightUnit());
        bill.setCreated(new Date());
        bill.setModified(new Date());
        bill.setCreationSource(RegisterBilCreationSourceEnum.PC.getCode());
        bill.setOperatorId(operatorUser.getId());
        bill.setOperatorName(operatorUser.getName());
        bill.setPlate(record.getPlate());
        bill.setMarketId(MarketUtil.returnMarket());
        return bill;
    }

    /**
     * 新增报备单
     * @param bill
     */
    private void createAddBill(RegisterBill bill) {
        this.billService.insertSelective(bill);
    }

}
