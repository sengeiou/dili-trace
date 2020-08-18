package com.dili.trace.api.client;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.domain.*;
import com.dili.trace.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientTradePush")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientTradePush")
public class ClientTradePushApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientTradePushApi.class);

    @Autowired
    private RegisterBillService registerBillService;

    @Autowired
    private UpStreamService upStreamService;

    @Autowired
    private ImageCertService imageCertService;

    @Autowired
    private TradePushService tradePushService;

    @Autowired
    TradeDetailService tradeDetailService;

    /**
     * 查询报备单
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/viewBillDetail.api", method = {RequestMethod.GET})
    public BaseOutput<RegisterBill> viewTradeDetail(@RequestParam Long billId) {
        try {
            RegisterBill registerBill = registerBillService.get(billId);
            Long upStreamId = registerBill.getUpStreamId();
            UpStream upStream = upStreamService.get(upStreamId);
            registerBill.setUpStreamName(upStream.getName());

            List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(billId);
            registerBill.setImageCerts(imageCerts);
            return BaseOutput.success().setData(registerBill);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            return BaseOutput.failure("查询数据出错");
        }
    }

    /**
     * 查询报备单
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/push.api", method = {RequestMethod.POST})
    public BaseOutput doTradePush(@RequestBody TradePushLog pushLog) {
        try {
            tradePushService.tradePush(pushLog);
        } catch (TraceBusinessException e) {
            BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            BaseOutput.failure("系统错误");
        }

        return BaseOutput.success();
    }


    @ApiOperation(value = "查询上架商品列表", notes = "查询上架商品列表")
    @RequestMapping(value = "/shelvesProduct.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<BasePage<TradeDetail>> shelvesProduct(@RequestBody TradeDetail tradeDetail) {
        if (tradeDetail == null || null == tradeDetail.getBuyerId()) {
            return BaseOutput.failure("参数错误");
        }
        try {
            tradeDetail.setSort("product_name");
            tradeDetail.setOrder("desc");
            Map<String,Object> map=new HashMap<>();
            //报备单
            map.put(IDTO.AND_CONDITION_EXPR, " stock_weight > 0 AND  parent_id IS NULL");
            tradeDetail.setMetadata(map);
            BasePage<TradeDetail> billList= tradeDetailService.listPageByExample(tradeDetail);
            //销售单
            map.put(IDTO.AND_CONDITION_EXPR, " stock_weight > 0 AND  parent_id IS NOT NULL");
            tradeDetail.setMetadata(map);
            BasePage<TradeDetail> saleList= tradeDetailService.listPageByExample(tradeDetail);

            Map<String,BasePage<TradeDetail>> result = new HashedMap();
            result.put("billList",billList);
            result.put("saleList",saleList);
            return BaseOutput.success().setData(result);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("quit", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @ApiOperation(value = "查询下架商品列表", notes = "查询下架商品列表")
    @RequestMapping(value = "/unavailableProduct.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<BasePage<TradeDetail>> unavailableProduct(@RequestBody TradeDetail tradeDetail) {
        if (tradeDetail == null || null == tradeDetail.getBuyerId()) {
            return BaseOutput.failure("参数错误");
        }
        try {

            tradeDetail.setSort("product_name");
            tradeDetail.setOrder("desc");
            Map<String,Object> map=new HashMap<>();
            //报备单
            map.put(IDTO.AND_CONDITION_EXPR, " pushaway_weight > 0 AND  parent_id IS NULL");
            tradeDetail.setMetadata(map);
            BasePage<TradeDetail> billList= tradeDetailService.listPageByExample(tradeDetail);
            //销售单
            map.put(IDTO.AND_CONDITION_EXPR, " pushaway_weight > 0 AND  parent_id IS NOT NULL");
            tradeDetail.setMetadata(map);
            BasePage<TradeDetail> saleList= tradeDetailService.listPageByExample(tradeDetail);
            //结果集
            Map<String,BasePage<TradeDetail>> result = new HashedMap();
            result.put("billList",billList);
            result.put("saleList",saleList);
            return BaseOutput.success().setData(result);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("quit", e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
