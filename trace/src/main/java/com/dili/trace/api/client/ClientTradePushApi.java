package com.dili.trace.api.client;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.TradePushLog;
import com.dili.trace.domain.UpStream;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.TradePushService;
import com.dili.trace.service.UpStreamService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientTradePush")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientTradePush")
public class ClientTradePushApi {
    @Autowired
    private RegisterBillService registerBillService;

    @Autowired
    private UpStreamService upStreamService;

    @Autowired
    private ImageCertService imageCertService;

    @Autowired
    private TradePushService tradePushService;

    /**
     * 查询报备单
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/viewBillDetail.api", method = { RequestMethod.GET })
    public BaseOutput<RegisterBill> viewTradeDetail(@RequestParam Long billId) {
        try {
            RegisterBill registerBill = registerBillService.get(billId);
            Long upStreamId = registerBill.getUpStreamId();
            UpStream upStream = upStreamService.get(upStreamId);
            registerBill.setUpStreamName(upStream.getName());

            List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(billId);
            registerBill.setImageCerts(imageCerts);
            return BaseOutput.success().setData(registerBill);
        }catch (TraceBusinessException e){
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            return BaseOutput.failure("查询数据出错");
        }
    }

    /**
     * 查询报备单
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/push.api", method = { RequestMethod.POST })
    public BaseOutput doTradePush(@RequestBody TradePushLog pushLog) {
        try {
            tradePushService.tradePush(pushLog);
        }catch (TraceBusinessException e){
            BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            BaseOutput.failure("系统错误");
        }

        return BaseOutput.success();
    }
}
