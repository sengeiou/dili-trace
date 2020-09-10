package com.dili.trace.service;

import com.dili.trace.dto.OrigionReportDto;
import com.dili.trace.dto.OrigionReportQueryDto;
import com.dili.trace.dto.ProductOrigionReportDto;

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
}
