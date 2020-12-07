package com.dili.trace.api.client;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.Access;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.*;
import com.dili.trace.enums.PushTypeEnum;
import com.dili.trace.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户商品上下架接口
 */
@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientTradePush")
@RestController
@Access(role = Role.Client,url = "")
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

    @Autowired
    TradeRequestService tradeRequestService;

    @Autowired
    private LoginSessionContext sessionContext;

    /**
     * 查询库存详情
     * @param tradeDetailId 交易明细ID
     * @param pushType 上下架类型 {@link com.dili.trace.enums.PushTypeEnum}
     * @return 库存明细
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/viewTradeDetail.api", method = {RequestMethod.GET})
    public BaseOutput<RegisterBill> viewTradeDetail(@RequestParam Long tradeDetailId,
                                                    @RequestParam Integer pushType) {
        try {
            TradeDetail tradeDetail = tradeDetailService.get(tradeDetailId);
            RegisterBill registerBill = registerBillService.get(tradeDetail.getBillId());
            if (tradeDetail.getTradeRequestId() != null) {
                TradeRequest tradeRequest = tradeRequestService.get(tradeDetail.getTradeRequestId());
                registerBill.setTradeRequestCode(tradeRequest.getCode());
            }
            Long upStreamId = registerBill.getUpStreamId();
            UpStream upStream = upStreamService.get(upStreamId);
            registerBill.setUpStreamName(upStream.getName());
            if (pushType.equals(PushTypeEnum.DOWN.getCode())) {
                registerBill.setWeight(tradeDetail.getStockWeight());
            } else {
                registerBill.setWeight(tradeDetail.getPushawayWeight());
            }

            List<ImageCert> imageCerts = imageCertService.findImageCertListByBillId(tradeDetail.getBillId());
            registerBill.setImageCerts(imageCerts);
            TradePushLog pushLog = new TradePushLog();
            pushLog.setTradeDetailId(tradeDetailId);
            pushLog.setSort("created");
            pushLog.setOrder("asc");
            List<TradePushLog> tradePushLogs = tradePushService.listByExample(pushLog);
            registerBill.setTradePushLogs(tradePushLogs);
            return BaseOutput.success().setData(registerBill);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("查询交易详情出错", e);
            return BaseOutput.failure("查询数据出错");
        }
    }

    /**
     * 上架/下架
     * @param pushLog 上下架参数
     * @return 上下架结果
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/push.api", method = {RequestMethod.POST})
    public BaseOutput doTradePush(@RequestBody TradePushLog pushLog) {
        try {
            logger.info("上下架，参数:{}", JSON.toJSON(pushLog));
            pushLog.setUserId(this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId());
            tradePushService.tradePush(pushLog);
            return BaseOutput.success();
        } catch (TraceBizException e) {
            logger.error(e.getMessage());
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("系统错误");
        }
    }


    /**
     * 查询上架商品列表
     * @param tradeDetail 查询条件
     * @return 上架商品列表
     */
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
            tradeDetail.setMetadata(IDTO.AND_CONDITION_EXPR, " stock_weight > 0");
            return BaseOutput.success().setData(tradeDetailService.listPageByExample(tradeDetail));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("quit", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 查询下架商品列表
     * @param tradeDetail
     * @return 下架商品列表
     */
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
            tradeDetail.setMetadata(IDTO.AND_CONDITION_EXPR, " pushaway_weight > 0");
            return BaseOutput.success().setData(tradeDetailService.listPageByExample(tradeDetail));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("quit", e);
            return BaseOutput.failure(e.getMessage());
        }
    }
}
