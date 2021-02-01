package com.dili.trace.controller;

import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.IDTO;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.domain.TradeRequest;
import com.dili.trace.enums.TradeOrderStatusEnum;
import com.dili.trace.enums.TradeReportFlagEnum;
import com.dili.trace.service.TradeOrderService;
import com.dili.trace.service.TradeRequestService;
import com.github.pagehelper.Page;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * @author asa.lee
 */
@Controller
@RequestMapping("/tradeRecord")
public class TraceRecordController {
    @Autowired
    TradeRequestService tradeRequestService;

    @Autowired
    TradeOrderService tradeOrderService;


    private static final Logger logger = LoggerFactory.getLogger(TraceRecordController.class);

    /**
     * 进入溯源记录页面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "tradeRecord/index";
    }

    /**
     * 查询数据
     * @param modelMap
     * @param tradeRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage.action", method = RequestMethod.POST)
    @ResponseBody
    public String listPage(ModelMap modelMap, TradeRequest tradeRequest)  throws Exception{
        if (null == tradeRequest) {
            logger.error("查询参数异常");
            return new EasyuiPageOutput(0L, Collections.emptyList()).toString();
        }
        //由于只判断了null值,页面传参为空字符串时需要特殊处理
        String queStr = "";
        if (StringUtils.isBlank(tradeRequest.getBuyerNo())) {
            tradeRequest.setBuyerNo(null);
        }
        if (StringUtils.isBlank(tradeRequest.getBuyerName())) {
            tradeRequest.setBuyerName(null);
        }
        if (StringUtils.isBlank(tradeRequest.getSellerNo())) {
            tradeRequest.setSellerNo(null);
        }
        if (StringUtils.isBlank(tradeRequest.getSellerName())) {
            tradeRequest.setSellerName(null);
        }
        if (StringUtils.isBlank(tradeRequest.getProductCode())) {
            tradeRequest.setProductCode(null);
        }
        if (StringUtils.isBlank(tradeRequest.getProductName())) {
            tradeRequest.setProductName(null);
        }
        if (null == tradeRequest.getReportFlag() || 0 == tradeRequest.getReportFlag()) {
            tradeRequest.setReportFlag(null);
        }
        if (null != tradeRequest.getOrderDateStart()) {
            String startTime = DateUtils.format(tradeRequest.getOrderDateStart());
            if (StringUtils.isNotBlank(queStr)) {
                queStr += " and '" + startTime + "' <= order_date";
            } else {
                queStr += "  '" + startTime + "' <= order_date";
            }
        }
        if (null != tradeRequest.getOrderDateEnd()) {
            String endTime = DateUtils.format(tradeRequest.getOrderDateEnd());
            if (StringUtils.isNotBlank(queStr)) {
                queStr += " and order_date  <= '" + endTime + "'";
            } else {
                queStr += " order_date <= '" + endTime + "'";
            }
        }
        if (StringUtils.isNotBlank(queStr)) {
            tradeRequest.setMetadata(IDTO.AND_CONDITION_EXPR, queStr);
        }
        List<TradeRequest> requestList   = tradeRequestService.listByExample(tradeRequest);
        StreamEx.of(requestList).nonNull().forEach(r -> {
            TradeOrder tradeOrder = this.tradeOrderService.get(r.getTradeOrderId());
//            r.setOrderStatus(tradeOrder.getOrderStatus());
//            r.setOrderStatusName(TradeOrderStatusEnum.fromCode(tradeOrder.getOrderStatus()).get().getName());
            r.setReportFlagStr(TradeReportFlagEnum.fromCode(r.getReportFlag()).get().getName());
        });
        List results = ValueProviderUtils.buildDataByProvider(tradeRequest, requestList);
        long total = results instanceof Page ? ((Page) results).getTotal() : results.size();
        return new EasyuiPageOutput(total, results).toString();
    }

}