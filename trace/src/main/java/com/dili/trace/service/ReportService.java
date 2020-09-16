package com.dili.trace.service;

import com.dili.trace.dto.*;

import java.util.List;

public interface ReportService{

    /**
     * 产地进场重量分布统计
     * @param queryDto
     * @return
     */
    List<OrigionReportDto> origionReportList(OrigionReportQueryDto queryDto);

    /**
     * 进场商品产地分布统计
     * @param queryDto
     * @return
     */
    List<ProductOrigionReportDto> productOrigionReportList(OrigionReportQueryDto queryDto);

    /**
     * 采购商品交易统计
     * @param queryDto
     * @return
     */
    List<PurchaseGoodsReportDto> purchaseGoodsReportList(PurchaseGoodsReportQueryDto queryDto);

    /**
     * 采购商户交易统计
     * @param queryDto
     * @return
     */
    List<PurchaseGoodsReportDto> purchaseGoodsReportList(UserPurchaseReportQueryDto queryDto);


}
