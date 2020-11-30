package com.dili.trace.service;

import com.dili.common.exception.TraceBizException;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.enums.DetectRequestStatusEnum;
import com.dili.trace.enums.DetectTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 检测请求service
 */
@Service
public class DetectRequestService extends BaseServiceImpl<DetectRequest, Long> {

    @Autowired
    BillService billService;

    /**
     * 根据报备单创建检测请求数据
     *
     * @param billId
     * @return
     */
    public DetectRequest createByBillId(Long billId) {
        RegisterBill item = this.billService.get(billId);
        if (item == null) {
            throw new TraceBizException("报备单不存在");
        }

        DetectRequest detectRequest = new DetectRequest();
        detectRequest.setBillId(item.getBillId());
        detectRequest.setCreated(new Date());
        detectRequest.setModified(new Date());
        detectRequest.setDetectType(DetectTypeEnum.NONE.getCode());
        detectRequest.setDetectRequestStatus(DetectRequestStatusEnum.NEW.getCode());
        this.insert(detectRequest);
        return detectRequest;

    }
}
