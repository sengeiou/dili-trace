package com.dili.trace.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.trace.dao.TradeStatisticMapper;
import com.dili.trace.dto.BillSumReportDto;
import com.dili.trace.dto.CommodityWeightReportDto;
import com.dili.trace.service.TradeStatisticReportService;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author asa.lee
 */
@Service
@EnableRetry
public class TradeStatisticReportServiceImpl implements TradeStatisticReportService {

    private static final Logger logger = LoggerFactory.getLogger(TradeStatisticReportServiceImpl.class);

    @Autowired
    private TradeStatisticMapper tradeStatisticMapper;

    @Override
    public List<CommodityWeightReportDto> getCommodityWeightReportList(CommodityWeightReportDto commodityWeightReportDto) {

        return tradeStatisticMapper.getCommodityWeightReportList(commodityWeightReportDto);
    }

    @Override
    public List<BillSumReportDto> getBillSumReportList(BillSumReportDto query) {
        return tradeStatisticMapper.getBillSumReportList(query);
    }

    @Override
    public List<BillSumReportDto> getMerchantReportList(BillSumReportDto query) {
        List<BillSumReportDto> billList = tradeStatisticMapper.getMerchantReportList(query);
        CopyOnWriteArrayList<BillSumReportDto> billResult = new CopyOnWriteArrayList<>();

        String totalStr = "合计";
        String totalEndStr = "总计";
        //取报备单合计数量
        int totalBillCount = StreamEx.of(billList).nonNull().collect(Collectors.summingInt(b -> b.getBillCount()));
        //按经营户、品种求和
        StreamEx.of(billList).nonNull().groupingBy(BillSumReportDto::getUserName).forEach((a, b) -> {
            BillSumReportDto totalItem = new BillSumReportDto();
            BigDecimal tWeight = BigDecimal.ZERO;
            Integer bCount = b.stream().collect(Collectors.summingInt(BillSumReportDto::getBillCount));

            BigDecimal userWeight = b.stream().map(BillSumReportDto::getWeight)
                    // 使用reduce()聚合函数,实现累加器
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            for (BillSumReportDto bItem : b) {
                tWeight = tWeight.add(bItem.getWeight());
                bItem.setBillCount(bCount);
                bItem.setUserWeight(userWeight);
                billResult.add(bItem);
            }
            totalItem.setUserName(a + totalStr);
            totalItem.setWeight(tWeight);
            totalItem.setUserWeight(userWeight);
            billResult.add(totalItem);
        });

        billResult.sort(Comparator.comparing(BillSumReportDto::getUserWeight).reversed().thenComparing(BillSumReportDto::getUserName).reversed().thenComparing(BillSumReportDto::getWeight).reversed());
        billResult.stream().forEach(b -> {
            if (StringUtils.isNotBlank(b.getUserName()) && b.getUserName().indexOf(totalStr) >= 0) {
                b.setUserName(totalStr);
            }
        });
        //取报备单总重量
        Double totalWeight = StreamEx.of(billList).nonNull().collect(Collectors.summingDouble(b -> b.getWeight().doubleValue()));
        BillSumReportDto totalBillSum = new BillSumReportDto();
        totalBillSum.setUserName(totalEndStr);
        totalBillSum.setBillCount(totalBillCount);
        totalBillSum.setWeight(BigDecimal.valueOf(totalWeight).setScale(2, BigDecimal.ROUND_HALF_UP));
        billResult.add(totalBillSum);
        logger.info("经营户进场重量统计:" + JSON.toJSONString(billResult));

        List<BillSumReportDto> result = new ArrayList<>(billResult);
        return result;
    }
}
