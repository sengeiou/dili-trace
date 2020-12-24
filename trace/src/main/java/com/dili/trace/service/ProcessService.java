package com.dili.trace.service;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.enums.CheckinStatusEnum;
import com.dili.trace.enums.MarketEnum;
import com.dili.trace.rpc.service.ProductRpcService;
import com.dili.uap.sdk.domain.Firm;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 流程处理
 */
@Service
public class ProcessService {

    @Resource
    ProductRpcService productRpcService;
    @Resource
    BillService billService;
    @Resource
    LoginSessionContext sessionContext;
    @Resource
    private MarketService marketService;
    @Resource
    CheckinOutRecordService checkinOutRecordService;
    @Autowired
    UapRpcService uapRpcService;

    /**
     * 创建报备之后
     *
     * @param billId
     * @param marketId
     */
    public void afterCreateBill(Long billId, Long marketId) {

    }

    /**
     * 报备审核通过之后
     *
     * @param billId
     * @param marketId
     */
    public void afterBillPassed(Long billId, Long marketId) {
        RegisterBill registerBill = billService.get(billId);
        Optional<OperatorUser> optUser = uapRpcService.getCurrentOperator();

        Map<String, String> marketCodeMap = marketService.getMarketCodeMap();
        String marketCode = marketService.getMarketById(marketId).map(Firm::getCode).orElseThrow(() -> {
            return new TraceBizException("市场不存在");
        });

        // 杭果和寿光市场，审核通过后系统自动进门
        if (marketCode.equals(marketCodeMap.get(MarketEnum.HZSG.getCode()))
                || marketCode.equals(marketCodeMap.get(MarketEnum.SDSG.getCode()))) {
            List<CheckinOutRecord> checkinRecordList = this.checkinOutRecordService
                    .doCheckin(optUser, Lists.newArrayList(billId), CheckinStatusEnum.ALLOWED);
        }
    }

    /**
     * 进门之后
     *
     * @param billId
     * @param marketId
     */
    public void afterCheckIn(Long billId, Long marketId) {
        RegisterBill registerBill = billService.get(billId);
        Optional<OperatorUser> optUser = uapRpcService.getCurrentOperator();

        // 进门之后向 UAP 同步库存
        productRpcService.create(registerBill, optUser);
    }
}
