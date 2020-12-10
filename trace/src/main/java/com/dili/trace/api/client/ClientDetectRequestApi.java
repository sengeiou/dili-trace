package com.dili.trace.api.client;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.DetectRequest;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.enums.DetectResultEnum;
import com.dili.trace.enums.DetectStatusEnum;
import com.dili.trace.enums.DetectTypeEnum;
import com.dili.trace.glossary.SampleSourceEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.DetectRequestService;
import com.dili.trace.service.RegisterBillService;
import org.aspectj.weaver.tools.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 检测单接口
 */
@RestController()
@RequestMapping("/api/client/clientDetectRequest")
@AppAccess(role = Role.Client)
public class ClientDetectRequestApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientDetectRequestApi.class);

    @Autowired
    LoginSessionContext loginSessionContext;
    @Autowired
    DetectRequestService detectRequestService;
    @Autowired
    BillService billService;

    /**
     * 创建检测单
     *
     * @param input
     * @return
     */
    @RequestMapping("/createDetectRequest.action")
    public BaseOutput<Long> createDetectRequest(@RequestBody DetectRequest input) {
        if (input == null || input.getBillId() == null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            DetectRequest item = this.detectRequestService.createDetectRequestForBill(input.getBillId(), Optional.empty());
            return BaseOutput.successData(item.getId());
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }


    }
}
