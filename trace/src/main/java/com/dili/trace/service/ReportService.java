package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.OrigionReportDto;
import com.dili.trace.dto.OrigionReportQueryDto;

import java.util.List;

public interface ReportService{

    List<OrigionReportDto> origionReportList(OrigionReportQueryDto queryDto);
}
