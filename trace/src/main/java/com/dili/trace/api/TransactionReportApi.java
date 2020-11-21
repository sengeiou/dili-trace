package com.dili.trace.api;

import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.input.TradeReportDto;
import com.dili.trace.service.BillReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 交易报表统计接口
 * @author asa.lee
 */
@Api(value = "/api/transactionReportApi", description = "交易报表统计接口")
@RestController
@RequestMapping(value = "/api/transactionReportApi")
public class TransactionReportApi {
    private static final Logger logger = LoggerFactory.getLogger(TransactionReportApi.class);

    @Autowired
    BillReportService billReportService;

    /**
     * 报备单统计
     * @param daySize
     * @return
     */
    @ApiOperation(value = "报备单统计", notes = "报备单统计")
    @RequestMapping(value = "/userBillReport.api", method = RequestMethod.GET)
    public BaseOutput userBillReport(@RequestParam String daySize) {
        try {
            if(StringUtils.isBlank(daySize)){
                return BaseOutput.failure("param is null");
            }
            int limitDay = Integer.valueOf(daySize);
            List<TradeReportDto> resultList= billReportService.getUserBillReport(limitDay);
            return BaseOutput.success().setData(resultList);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure();
        }
    }

    /**
     * 交易单统计
     * @param daySize
     * @return
     */
    @ApiOperation(value = "交易单统计", notes = "交易单统计")
    @RequestMapping(value = "/userTradeReport.api", method = RequestMethod.GET)
    public BaseOutput userTradeReport(@RequestParam String daySize) {
        try {
            if(StringUtils.isBlank(daySize)){
                return BaseOutput.failure("param is null");
            }
            int limitDay = Integer.valueOf(daySize);
            List<TradeReportDto> resultList= billReportService.getUserTradeReport(limitDay);
            return BaseOutput.success().setData(resultList);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure();
        }
    }
}
